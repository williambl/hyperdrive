package com.williambl.demo.framebuffer

import com.williambl.demo.shader.ShaderProgram

abstract class Framebuffer(val name: String, var width: Int, var height: Int, val followsWindowSize: Boolean) {
    abstract val id: Int
    abstract val hasDepth: Boolean

    abstract fun renderToCurrentBuffer(width: Int, height: Int, shader: ShaderProgram)
    abstract fun bind()
    abstract fun setSize(width: Int, height: Int)
}

