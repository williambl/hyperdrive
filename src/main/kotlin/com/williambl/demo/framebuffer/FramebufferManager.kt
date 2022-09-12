package com.williambl.demo.framebuffer

object FramebufferManager {
    private val framebuffers: MutableMap<String, Framebuffer> = mutableMapOf()

    /**
     * Gets a framebuffer with the given name, or creates one with the parameters given.
     */
    fun getOrCreateFramebuffer(
        name: String,
        width: Int,
        height: Int,
        hasDepthAndStencil: Boolean,
        followsWindowSize: Boolean
    ): Framebuffer {
        return this.framebuffers.getOrPut(name) { FramebufferImpl(name, width, height, hasDepthAndStencil, followsWindowSize) }
    }

    fun getOrCreateGBuffer(
        name: String,
        width: Int,
        height: Int,
        followsWindowSize: Boolean
    ): GBuffer {
        return this.framebuffers.getOrPut(name) { GBuffer(name, width, height, followsWindowSize) }.let { if (it is GBuffer) it else throw RuntimeException("$name is not a GBuffer!") }
    }

    fun updateFramebufferSizes(width: Int, height: Int) {
        for (framebuffer in this.framebuffers.values) {
            if (framebuffer.followsWindowSize) {
                framebuffer.setSize(width, height)
            }
        }
    }
}