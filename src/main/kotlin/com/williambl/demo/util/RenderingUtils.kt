package com.williambl.demo.util

import com.williambl.demo.Hyperdrive
import com.williambl.demo.framebuffer.Framebuffer
import com.williambl.demo.framebuffer.FramebufferManager
import com.williambl.demo.shader.ShaderProgram
import org.lwjgl.opengl.GL30
import org.lwjgl.opengl.GL30.*

private val swapBuffer: Framebuffer = FramebufferManager.getOrCreateFramebuffer("swap", Hyperdrive.windowWidth, Hyperdrive.windowHeight, true, false)

fun applyPostShaderEffect(width: Int, height: Int, shader: ShaderProgram) {
    val currentlyBound = glGetInteger(GL_FRAMEBUFFER_BINDING)
    swapBuffer.setSize(width, height)
    glBindFramebuffer(GL_DRAW_FRAMEBUFFER, swapBuffer.id)
    glBindFramebuffer(GL_READ_FRAMEBUFFER, currentlyBound)
    glBlitFramebuffer(0, 0, width, height, 0, 0, swapBuffer.width, swapBuffer.height, GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT or GL_STENCIL_BUFFER_BIT, GL_NEAREST)
    glBindFramebuffer(GL_FRAMEBUFFER, currentlyBound)
    swapBuffer.renderToCurrentBuffer(width, height, shader)
}