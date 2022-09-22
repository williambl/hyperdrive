package com.williambl.demo.framebuffer

import com.williambl.demo.RenderingContext
import com.williambl.demo.model.TexturedModel
import com.williambl.demo.model.Vertices
import com.williambl.demo.model.Vertices.Attribute.Color.color
import com.williambl.demo.model.Vertices.Attribute.Position.position
import com.williambl.demo.model.Vertices.Attribute.Texture.tex
import com.williambl.demo.shader.ShaderProgram
import com.williambl.demo.texture.Texture
import com.williambl.demo.texture.SimpleTexture
import com.williambl.demo.util.*
import org.lwjgl.opengl.GL45.*
import org.lwjgl.system.MemoryUtil.NULL

class GBuffer(
    name: String,
    width: Int,
    height: Int,
    followsWindowSize: Boolean
) : Framebuffer(name, width, height, followsWindowSize) {

    override val id: Int = glCreateFramebuffers()
    override val hasDepth: Boolean = true

    private val positionTexId: Int = glGenTextures()
    private val normalTexId: Int = glGenTextures()
    private val albedoSpecTexId: Int = glGenTextures()
    private val depthStencilTexId: Int = glGenTextures()

    private var fullScreenQuad: TexturedModel? = null
    private var fullScreenQuadTexWidth: Int = 0
    private var fullScreenQuadTexHeight: Int = 0

    val positionTex: Texture = SimpleTexture(this.positionTexId)
    val normalTex: Texture = SimpleTexture(this.normalTexId)
    val albedoSpecTex: Texture = SimpleTexture(this.albedoSpecTexId)

    init {
        glBindTexture(GL_TEXTURE_2D, this.positionTexId)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA16F, this.width, this.height, 0, GL_RGBA, GL_FLOAT, NULL) // not using DSA here - glTextureStorage makes immutable textures.
        glTextureParameteri(this.positionTexId, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTextureParameteri(this.positionTexId, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glNamedFramebufferTexture(this.id, GL_COLOR_ATTACHMENT0, this.positionTexId, 0)

        glBindTexture(GL_TEXTURE_2D, this.normalTexId)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA16F, this.width, this.height, 0, GL_RGBA, GL_FLOAT, NULL)
        glTextureParameteri(this.normalTexId, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTextureParameteri(this.normalTexId, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glNamedFramebufferTexture(this.id, GL_COLOR_ATTACHMENT1, this.normalTexId, 0)

        glBindTexture(GL_TEXTURE_2D, this.albedoSpecTexId)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, NULL)
        glTextureParameteri(this.albedoSpecTexId, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTextureParameteri(this.albedoSpecTexId, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glNamedFramebufferTexture(this.id, GL_COLOR_ATTACHMENT2, this.albedoSpecTexId, 0)

        glBindTexture(GL_TEXTURE_2D, this.depthStencilTexId)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH24_STENCIL8, this.width, this.height, 0, GL_DEPTH_STENCIL, GL_UNSIGNED_INT_24_8, NULL)
        glTextureParameteri(this.depthStencilTexId, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTextureParameteri(this.depthStencilTexId, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glNamedFramebufferTexture(this.id, GL_DEPTH_STENCIL_ATTACHMENT, this.depthStencilTexId, 0)

        glNamedFramebufferDrawBuffers(this.id, intArrayOf(GL_COLOR_ATTACHMENT0, GL_COLOR_ATTACHMENT1, GL_COLOR_ATTACHMENT2))

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw IllegalStateException("Framebuffer ${this.name} is not complete!")
        }
    }

    override fun renderToCurrentBuffer(width: Int, height: Int, shader: ShaderProgram) {
        drawToCurrentBuffer(shader, this.positionTex, this.normalTex, this.albedoSpecTex)
    }

    override fun bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, this.id)
    }

    override fun setSize(width: Int, height: Int) {
        this.width = width
        this.height = height
        this.fullScreenQuad = null

        glBindTexture(GL_TEXTURE_2D, this.positionTexId)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA16F, this.width, this.height, 0, GL_RGBA, GL_FLOAT, NULL) // not using DSA here - glTextureStorage makes immutable textures.

        glBindTexture(GL_TEXTURE_2D, this.normalTexId)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA16F, this.width, this.height, 0, GL_RGBA, GL_FLOAT, NULL)

        glBindTexture(GL_TEXTURE_2D, this.albedoSpecTexId)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, NULL)

        glBindTexture(GL_TEXTURE_2D, this.depthStencilTexId)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH24_STENCIL8, this.width, this.height, 0, GL_DEPTH_STENCIL, GL_UNSIGNED_INT_24_8, NULL)
    }

}