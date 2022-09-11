package com.williambl.demo.model

import com.williambl.demo.Renderable
import com.williambl.demo.RenderingContext
import com.williambl.demo.freeMemoryManaged
import com.williambl.demo.shader.ShaderProgram
import com.williambl.demo.texture.Texture
import org.lwjgl.opengl.GL45.*

open class TexturedModel(protected val vertices: Vertices, protected val indices: IntArray, val shaderProgram: ShaderProgram, var texture: Texture):
    Renderable {
    private val vbo: IntArray = intArrayOf(0)
    private val vao: IntArray = intArrayOf(0)
    private val ebo: IntArray = intArrayOf(0)

    var isSetup = false

    override fun setup() {
        if (this.isSetup) {
            glBindVertexArray(this.vao[0])
            glBindBuffer(GL_ARRAY_BUFFER, this.vbo[0])
            this.vertices.toBytes().let {
                glBufferSubData(GL_ARRAY_BUFFER, 0, it)
                it.freeMemoryManaged()
            }
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.ebo[0])
            glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, this.indices)
            return
        }

        if (!this.shaderProgram.properties.attributes.contentEquals(this.vertices.attributes)) {
            throw RuntimeException("Trying to use a shader with different attributes to the vertices!\nShader Program: ${this.shaderProgram.properties.attributes.contentToString()}\nVertices: ${this.vertices.attributes.contentToString()}")
        }

        glGenVertexArrays(this.vao)
        glCreateBuffers(this.vbo)
        glCreateBuffers(this.ebo)

        glBindVertexArray(this.vao[0])

        glBindBuffer(GL_ARRAY_BUFFER, this.vbo[0])
        this.vertices.toBytes().let {
            glBufferData(GL_ARRAY_BUFFER, it, GL_STATIC_DRAW)
            it.freeMemoryManaged()
        }

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.ebo[0])
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.indices, GL_STATIC_DRAW)

        var offset: Long = 0
        for ((index, attr) in this.vertices.attributes.withIndex()) {
            glVertexAttribPointer(index, attr.count, attr.type, false, this.vertices.stride, offset)
            glEnableVertexAttribArray(index)
            offset += attr.size
        }
        this.isSetup = true
    }

    override fun render(ctx: RenderingContext) {
        this.shaderProgram.use()
        this.shaderProgram.setUniform("model", ctx.modelStack.value())
        if (this.vertices.attributes.contains(Vertices.Attribute.Normal)) {
            this.shaderProgram.setUniform("normalModelView", ctx.modelStack.normal(ctx.view))
        }
        this.texture.bind()
        glBindVertexArray(this.vao[0])
        glDrawElements(GL_TRIANGLES, this.indices.size, GL_UNSIGNED_INT, 0)
        glBindVertexArray(0)
    }
}