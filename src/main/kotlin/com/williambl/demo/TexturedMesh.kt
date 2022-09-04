package com.williambl.demo

import org.lwjgl.opengl.GL45

open class TexturedMesh(protected val vertices: FloatArray, protected val indices: FloatArray, protected val shaderProgram: ShaderProgram, var texture: Texture): Renderable {
    private val vbo: IntArray = intArrayOf(1)
    private val vao: IntArray = intArrayOf(1)
    private val ebo: IntArray = intArrayOf(1)

    var isSetup = false

    override fun setup() {
        if (this.isSetup) {
            GL45.glBindVertexArray(this.vao[0])
            GL45.glBindBuffer(GL45.GL_ARRAY_BUFFER, this.vbo[0])
            GL45.glBufferSubData(GL45.GL_ARRAY_BUFFER, 0, this.vertices)
            GL45.glBindBuffer(GL45.GL_ELEMENT_ARRAY_BUFFER, this.ebo[0])
            GL45.glBufferSubData(GL45.GL_ELEMENT_ARRAY_BUFFER, 0, this.indices)
            return
        }

        GL45.glGenVertexArrays(this.vao)
        GL45.glCreateBuffers(this.vbo)
        GL45.glCreateBuffers(this.ebo)

        GL45.glBindVertexArray(this.vao[0])

        GL45.glBindBuffer(GL45.GL_ARRAY_BUFFER, this.vbo[0])
        GL45.glBufferData(GL45.GL_ARRAY_BUFFER, this.vertices, GL45.GL_DYNAMIC_DRAW)

        GL45.glBindBuffer(GL45.GL_ELEMENT_ARRAY_BUFFER, this.ebo[0])
        GL45.glBufferData(GL45.GL_ELEMENT_ARRAY_BUFFER, this.indices, GL45.GL_DYNAMIC_DRAW)

        GL45.glVertexAttribPointer(0, 3, GL45.GL_FLOAT, false, 8 * 4, 0)
        GL45.glEnableVertexAttribArray(0)

        GL45.glVertexAttribPointer(1, 3, GL45.GL_FLOAT, false, 8 * 4, 3 * 4)
        GL45.glEnableVertexAttribArray(1)

        GL45.glVertexAttribPointer(2, 2, GL45.GL_FLOAT, false, 8 * 4, 6 * 4)
        GL45.glEnableVertexAttribArray(2)
        this.isSetup = true
    }

    override fun render() {
        this.shaderProgram.use()
        this.texture.bind()
        GL45.glBindVertexArray(this.vao[0])
        GL45.glDrawElements(GL45.GL_TRIANGLES, this.indices.size, GL45.GL_UNSIGNED_INT, 0)
        GL45.glBindVertexArray(0)
    }
}