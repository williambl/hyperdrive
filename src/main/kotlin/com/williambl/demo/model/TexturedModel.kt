package com.williambl.demo.model

import com.williambl.demo.Renderable
import com.williambl.demo.RenderingContext
import com.williambl.demo.shader.ShaderProgram
import com.williambl.demo.texture.Texture
import org.lwjgl.opengl.GL45.*

open class TexturedModel(protected val vertices: FloatArray, protected val indices: IntArray, protected val shaderProgram: ShaderProgram, var texture: Texture):
    Renderable {
    private val vbo: IntArray = intArrayOf(0)
    private val vao: IntArray = intArrayOf(0)
    private val ebo: IntArray = intArrayOf(0)

    var isSetup = false

    override fun setup() {
        if (this.isSetup) {
            glBindVertexArray(this.vao[0])
            glBindBuffer(GL_ARRAY_BUFFER, this.vbo[0])
            glBufferSubData(GL_ARRAY_BUFFER, 0, this.vertices)
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.ebo[0])
            glBufferSubData(GL_ELEMENT_ARRAY_BUFFER, 0, this.indices)
            return
        }

        glGenVertexArrays(this.vao)
        glCreateBuffers(this.vbo)
        glCreateBuffers(this.ebo)

        glBindVertexArray(this.vao[0])

        glBindBuffer(GL_ARRAY_BUFFER, this.vbo[0])
        glBufferData(GL_ARRAY_BUFFER, this.vertices, GL_DYNAMIC_DRAW)

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.ebo[0])
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.indices, GL_DYNAMIC_DRAW)

        glVertexAttribPointer(0, 3, GL_FLOAT, false, 8 * 4, 0)
        glEnableVertexAttribArray(0)

        glVertexAttribPointer(1, 3, GL_FLOAT, false, 8 * 4, 3 * 4)
        glEnableVertexAttribArray(1)

        glVertexAttribPointer(2, 2, GL_FLOAT, false, 8 * 4, 6 * 4)
        glEnableVertexAttribArray(2)
        this.isSetup = true
    }

    override fun render(ctx: RenderingContext) {
        this.shaderProgram.use()
        this.shaderProgram.setUniform("model", ctx.modelStack.value())
        this.texture.bind()
        glBindVertexArray(this.vao[0])
        glDrawElements(GL_TRIANGLES, this.indices.size, GL_UNSIGNED_INT, 0)
        glBindVertexArray(0)
    }
}