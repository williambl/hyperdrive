package com.williambl.demo.framebuffer

import com.williambl.demo.RenderingContext
import com.williambl.demo.model.TexturedModel
import com.williambl.demo.model.Vertices
import com.williambl.demo.model.Vertices.Attribute.Color.color
import com.williambl.demo.model.Vertices.Attribute.Position.position
import com.williambl.demo.model.Vertices.Attribute.Texture.tex
import com.williambl.demo.shader.ShaderProgram
import com.williambl.demo.texture.Texture
import com.williambl.demo.util.Mat4x4
import com.williambl.demo.util.MatrixStack
import com.williambl.demo.util.Time
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil.NULL

class Framebuffer(
    val name: String,
    var width: Int,
    var height: Int,
    val hasDepthAndStencil: Boolean,
    val followsWindowSize: Boolean
) {

    val colourAsTexture: Texture
        = object : Texture {
            override fun bind() = glBindTexture(GL_TEXTURE_2D, this@Framebuffer.colourTexId)
        }

    private var fullScreenQuad: TexturedModel? = null
    private var fullScreenQuadTexWidth: Int = 0
    private var fullScreenQuadTexHeight: Int = 0

    private fun fullScreenQuadForDrawing(width: Int, height: Int, shader: ShaderProgram): TexturedModel {
        if (this.fullScreenQuadTexWidth == width && this.fullScreenQuadTexHeight == height && this.fullScreenQuad?.shaderProgram == shader) {
            return this.fullScreenQuad as TexturedModel
        } else {
            this.fullScreenQuad = TexturedModel(
                Vertices(Vertices.Attribute.Position, Vertices.Attribute.Color, Vertices.Attribute.Texture)
                    .position(1.0, 1.0, 0.0).color(1.0, 1.0, 1.0).tex(width / this.width.toDouble(), height / this.height.toDouble()).next()
                    .position(1.0, -1.0, 0.0).color(1.0, 1.0, 1.0).tex(width / this.width.toDouble(), 0.0).next()
                    .position(-1.0, -1.0, 0.0).color(1.0, 1.0, 1.0).tex(0.0, 0.0).next()
                    .position(-1.0, 1.0, 0.0).color(1.0, 1.0, 1.0).tex(0.0, height / this.height.toDouble()).next(),
                intArrayOf(
                    0, 1, 3,
                    1, 2, 3
                ),
                shader,
                this.colourAsTexture
            ).also { it.setup() }
            this.fullScreenQuadTexWidth = width
            this.fullScreenQuadTexHeight = height
            return this.fullScreenQuad!!
        }
    }

    fun renderToCurrentBuffer(width: Int, height: Int, shader: ShaderProgram) {
        glDepthMask(false)
        glDisable(GL_DEPTH_TEST)
        this.fullScreenQuadForDrawing(width, height, shader).render(RenderingContext(MatrixStack(), Mat4x4(), Mat4x4(), Time(0.0, 0.0)))
        glEnable(GL_DEPTH_TEST)
        glDepthMask(true)
    }

    fun bind() {
        glBindFramebuffer(GL_FRAMEBUFFER, this.id)
    }

    fun setSize(width: Int, height: Int) {
        this.width = width
        this.height = height
        this.fullScreenQuad = null
        glBindTexture(GL_TEXTURE_2D, this.colourTexId)
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, NULL)
        if (this.hasDepthAndStencil) {
            glBindTexture(GL_TEXTURE_2D, this.depthStencilTexId)
            glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH24_STENCIL8, this.width, this.height, 0, GL_DEPTH_STENCIL, GL_UNSIGNED_INT_24_8, NULL)
        }
    }

    val id: Int
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