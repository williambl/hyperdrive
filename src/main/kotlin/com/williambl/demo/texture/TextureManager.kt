package com.williambl.demo.texture

object TextureManager {
    private val textures: MutableMap<String, TextureImpl> = mutableMapOf()

    /**
     * Loads a [TextureImpl] from the jar resource at [location].
     *
     * The texture file can be in any format supported by [org.lwjgl.stb.STBImage].
     */
    fun getOrCreateTexture(location: String): Texture {
        return this.textures.getOrPut(location) { TextureImpl(location) }
    }
}