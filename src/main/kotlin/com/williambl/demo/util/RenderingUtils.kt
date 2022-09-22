package com.williambl.demo.util

import com.williambl.demo.Hyperdrive
import com.williambl.demo.RenderingContext
import com.williambl.demo.framebuffer.Framebuffer
import com.williambl.demo.framebuffer.FramebufferManager
import com.williambl.demo.material.Material
import com.williambl.demo.model.TexturedModel
import com.williambl.demo.model.Vertices
import com.williambl.demo.model.Vertices.Attribute.Color.color
import com.williambl.demo.model.Vertices.Attribute.Position.position
import com.williambl.demo.model.Vertices.Attribute.Texture.tex
import com.williambl.demo.shader.ShaderManager
import com.williambl.demo.shader.ShaderProgram
import com.williambl.demo.texture.Texture
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL45.glBlitNamedFramebuffer

private val swapBuffer: Framebuffer = FramebufferManager.getOrCreateFramebuffer("swap", Hyperdrive.windowWidth, Hyperdrive.windowHeight, true, false)

fun applyPostShaderEffect(width: Int, height: Int, shader: ShaderProgram) {
    val currentlyBound = glGetInteger(GL_FRAMEBUFFER_BINDING)
    swapBuffer.setSize(width, height)
    glBlitNamedFramebuffer(currentlyBound, swapBuffer.id, 0, 0, width, height, 0, 0, swapBuffer.width, swapBuffer.height, GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT or GL_STENCIL_BUFFER_BIT, GL_NEAREST)
    swapBuffer.renderToCurrentBuffer(width, height, shader)
}

val fullScreenQuad = TexturedModel(
    Vertices(Vertices.Attribute.Position, Vertices.Attribute.Color, Vertices.Attribute.Texture)
        .position(1.0, 1.0, 0.0).color(1.0, 1.0, 1.0).tex(1.0, 1.0).next()
        .position(1.0, -1.0, 0.0).color(1.0, 1.0, 1.0).tex(1.0, 0.0).next()
        .position(-1.0, -1.0, 0.0).color(1.0, 1.0, 1.0).tex(0.0, 0.0).next()
        .position(-1.0, 1.0, 0.0).color(1.0, 1.0, 1.0).tex(0.0, 1.0).next(),
    intArrayOf(
        0, 1, 3,
        1, 2, 3
    ),
    ShaderManager.getOrCreateShaderProgram("blit").createMaterial { texture("DiffuseTex", null) }
)

fun drawToCurrentBuffer(shader: ShaderProgram, vararg textures: Texture) {
    glDepthMask(false)
    glDisable(GL_DEPTH_TEST)
    fullScreenQuad.material = Material(shader, *textures)
    if (!fullScreenQuad.isSetup) {
        fullScreenQuad.setup()
    }
    fullScreenQuad.render(RenderingContext(MatrixStack(), Mat4x4(), Mat4x4(), Vec3(0.0, 0.0, 0.0), Time(0.0, 0.0)))
    glEnable(GL_DEPTH_TEST)
    glDepthMask(true)
}