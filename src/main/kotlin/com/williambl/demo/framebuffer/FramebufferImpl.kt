package com.williambl.demo.framebuffer

import com.williambl.demo.RenderingContext
import com.williambl.demo.model.TexturedModel
import com.williambl.demo.model.Vertices
import com.williambl.demo.model.Vertices.Attribute.Color.color
import com.williambl.demo.model.Vertices.Attribute.Position.position
import com.williambl.demo.model.Vertices.Attribute.Texture.tex
import com.williambl.demo.shader.ShaderProgram
import com.williambl.demo.texture.Texture
import com.williambl.demo.util.*
import org.lwjgl.opengl.GL45.*
import org.lwjgl.system.MemoryUtil.*

class FramebufferImpl(
    name: String,
    width: Int,
    height: Int,
    val hasDepthAndStencil: Boolean,
    followsWindowSize: Boolean
) : Framebuffer(name, width, height, followsWindowSize) {

    val colourAsTexture: Texture
            = object : Texture {
        override fun bind() = glBindTexture(GL_TEXTURE_2D, this@FramebufferImpl.colourTexId)
    }

    override val id: Int = glCreateFramebuffers()
    override val hasDepth: Boolean = this.hasDepthAndStencil

    private val colourTexId: Int
    private val depthStencilTexId: Int
    private var fullScreenQuad: TexturedModel? = null
    private var fullScreenQuadTexWidth: Int = 0
    private var fullScreenQuadTexHeight: Int = 0

    init {
        this.colourTexId = glGenTextures()
        glBindTexture(GL_TEXTURE_2D, this.colourTexId)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, NULL) // not using DSA here - glTextureStorage makes immutable textures.
        glTextureParameteri(this.colourTexId, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTextureParameteri(this.colourTexId, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glNamedFramebufferTexture(this.id, GL_COLOR_ATTACHMENT0, this.colourTexId, 0)

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
            glTextureParameteri(this.depthStencilTexId, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
            glTextureParameteri(this.depthStencilTexId, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
            glNamedFramebufferTexture(
                this.id,
                GL_DEPTH_STENCIL_ATTACHMENT,
                this.depthStencilTexId,
                0
            )
        } else {
            this.depthStencilTexId = -1
        }

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw IllegalStateException("Framebuffer ${this.name} is not complete!")
        }
    }

    override fun renderToCurrentBuffer(width: Int, height: Int, shader: ShaderProgram) {
        drawToCurrentBuffer(shader, this.colourAsTexture)
    }

    override fun bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, this.id)
    }

    override fun setSize(width: Int, height: Int) {
        this.width = width
        this.height = height
        this.fullScreenQuad = null
        glBindTexture(GL_TEXTURE_2D, this.colourTexId)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, NULL) // not using DSA here - glTextureStorage makes immutable textures.
        if (this.hasDepthAndStencil) {
            glBindTexture(GL_TEXTURE_2D, this.depthStencilTexId)
            glTexImage2D(GL_TEXTURE_2D, 0,
                GL_DEPTH24_STENCIL8, this.width, this.height, 0,
                GL_DEPTH_STENCIL,
                GL_UNSIGNED_INT_24_8,
                NULL
            )
        }
    }

}