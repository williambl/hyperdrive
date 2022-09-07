package com.williambl.demo.framebuffer

import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil.NULL

//TODO make the textures available as Texture objects
class Framebuffer(
    val name: String,
    var width: Int,
    var height: Int,
    val hasDepthAndStencil: Boolean,
    val followsWindowSize: Boolean
) {
    fun setSize(width: Int, height: Int) {
        this.width = width
        this.height = height
        glBindTexture(GL_TEXTURE_2D, this.colourTexId)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, NULL)
        if (this.hasDepthAndStencil) {
            glBindTexture(GL_TEXTURE_2D, this.depthStencilTexId)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH24_STENCIL8, this.width, this.height, 0, GL_DEPTH_STENCIL, GL_UNSIGNED_INT_24_8, NULL)
        }
    }

    private val id: Int
    private val colourTexId: Int
    private val depthStencilTexId: Int
    init {
        this.id = glGenFramebuffers()
        glBindFramebuffer(GL_FRAMEBUFFER, this.id)

        this.colourTexId = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, this.colourTexId)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, NULL)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, this.colourTexId, 0)

        if (this.hasDepthAndStencil) {
            this.depthStencilTexId = glGenTextures()
            glBindTexture(GL_TEXTURE_2D, this.depthStencilTexId)
            glTexImage2D(
                GL_TEXTURE_2D,
                0,
                GL_DEPTH24_STENCIL8,
                this.width,
                this.height,
                0,
                GL_DEPTH_STENCIL,
                GL_UNSIGNED_INT_24_8,
                NULL
            )
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_STENCIL_ATTACHMENT, GL_TEXTURE_2D, this.depthStencilTexId, 0)
        } else {
            this.depthStencilTexId = -1
        }

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw IllegalStateException("Framebuffer ${this.name} is not complete!")
        }
    }
}