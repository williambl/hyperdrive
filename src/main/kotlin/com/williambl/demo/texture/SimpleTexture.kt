package com.williambl.demo.texture

import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import org.lwjgl.opengl.GL11.glBindTexture

class SimpleTexture(val id: Int) : Texture {
    override fun bind() = glBindTexture(GL_TEXTURE_2D, id)
}