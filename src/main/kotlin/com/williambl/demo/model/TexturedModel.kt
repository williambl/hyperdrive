package com.williambl.demo.model

import com.williambl.demo.Renderable
import com.williambl.demo.RenderingContext
import com.williambl.demo.freeMemoryManaged
import com.williambl.demo.shader.ShaderProgram
import com.williambl.demo.texture.Texture
import org.lwjgl.opengl.GL45.*

open class TexturedModel(protected val vertices: Vertices, protected val indices: IntArray, var shaderProgram: ShaderProgram, vararg var textures: Texture):
    Renderable {
    private val vbo: Int = glCreateBuffers() // vertex buffer object
    private val vao: Int = glCreateVertexArrays() // vertex array object
    private val ebo: Int = glCreateBuffers() // element (indices) buffer object

    var isSetup = false

    override fun setup() {
        if (this.isSetup) {
            this.vertices.toBytes().let {
                glNamedBufferStorage(this.vbo, it, GL_DYNAMIC_STORAGE_BIT)
                it.freeMemoryManaged()
            }

            glNamedBufferStorage(this.ebo, this.indices, GL_DYNAMIC_STORAGE_BIT)
            return
        }

        if (!this.shaderProgram.properties.attributes.contentEquals(this.vertices.attributes)) {
            throw RuntimeException("Trying to use a shader with different attributes to the vertices!\nShader Program: ${this.shaderProgram.properties.attributes.contentToString()}\nVertices: ${this.vertices.attributes.contentToString()}")
        }

        this.vertices.toBytes().let {
            glNamedBufferStorage(this.vbo, it, GL_DYNAMIC_STORAGE_BIT)
            it.freeMemoryManaged()
        }

        glNamedBufferStorage(this.ebo, this.indices, GL_DYNAMIC_STORAGE_BIT)

        glVertexArrayVertexBuffer(this.vao, 0, this.vbo, 0, this.vertices.stride)
        glVertexArrayElementBuffer(this.vao, this.ebo)

        var offset = 0
        for ((index, attr) in this.vertices.attributes.withIndex()) {
            glEnableVertexArrayAttrib(this.vao, index)
            glVertexArrayAttribFormat(this.vao, index, attr.count, attr.type, false, offset)
            glVertexArrayAttribBinding(this.vao, index, 0)
            offset += attr.size
        }
        this.isSetup = true
    }

    override fun render(ctx: RenderingContext) {
        this.shaderProgram.use()
        this.shaderProgram.setUniform("Model", ctx.modelStack.value())
        if (this.vertices.attributes.contains(Vertices.Attribute.Normal)) {
            this.shaderProgram.setUniform("NormalModelView", ctx.modelStack.normal(ctx.view))
        }

        for ((index, texture) in this.textures.withIndex()) {
            glActiveTexture(GL_TEXTURE0 + index)
            texture.bind()
        }

        glBindVertexArray(this.vao)
        glDrawElements(GL_TRIANGLES, this.indices.size, GL_UNSIGNED_INT, 0)
        glBindVertexArray(0)
    }
}