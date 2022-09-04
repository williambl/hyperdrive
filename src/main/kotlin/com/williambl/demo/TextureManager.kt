package com.williambl.demo

object TextureManager {
    private val textures: MutableMap<String, Texture> = mutableMapOf()

    /**
     * Loads a [Texture] from the jar resource at [location].
     *
     * The texture file can be in any format supported by [org.lwjgl.stb.STBImage].
     */
    fun getOrCreateTexture(location: String): Texture {
        return this.textures.getOrPut(location) { Texture(location) }
    }
}