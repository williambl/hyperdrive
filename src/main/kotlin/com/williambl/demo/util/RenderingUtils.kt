package com.williambl.demo.util

import com.williambl.demo.Hyperdrive
import com.williambl.demo.framebuffer.Framebuffer
import com.williambl.demo.framebuffer.FramebufferManager
import com.williambl.demo.shader.ShaderProgram
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