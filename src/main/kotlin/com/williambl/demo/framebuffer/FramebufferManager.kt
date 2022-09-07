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
        return this.framebuffers.getOrPut(name) { Framebuffer(name, width, height, hasDepthAndStencil, followsWindowSize) }
    }

    fun updateFramebufferSizes(width: Int, height: Int) {
        for (framebuffer in this.framebuffers.values) {
            if (framebuffer.followsWindowSize) {
                framebuffer.setSize(width, height)
            }
        }
    }
}