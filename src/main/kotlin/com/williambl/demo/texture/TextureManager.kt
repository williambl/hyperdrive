package com.williambl.demo.texture

import java.nio.file.Path
import kotlin.io.path.absolutePathString
import kotlin.io.path.inputStream

object TextureManager {
    private val textures: MutableMap<String, TextureImpl> = mutableMapOf()

    /**
     * Loads a [TextureImpl] from the jar resource at [location].
     *
     * The texture file can be in any format supported by [org.lwjgl.stb.STBImage].
     */
    fun getOrCreateTexture(location: String): Texture {
        return this.textures.getOrPut(location) { TextureImpl(location, this::class.java.getResourceAsStream(location)) }
    }

    fun getOrCreateTexture(path: Path): Texture {
        return this.textures.getOrPut(path.absolutePathString()) { TextureImpl(path.absolutePathString(), path.inputStream()) }
    }
}