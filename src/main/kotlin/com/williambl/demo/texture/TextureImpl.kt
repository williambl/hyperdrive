package com.williambl.demo.texture

import com.williambl.demo.freeMemoryManaged
import com.williambl.demo.toByteBuffer
import com.williambl.demo.toMemoryManaged
import org.lwjgl.opengl.GL45.*
import org.lwjgl.stb.STBImage
import java.io.IOException

/**
 * A representation of a texture.
 *
 * The texture is loaded from the jar resource at [location], from any format supported by [STBImage].
 *
 * Do not call the constructor, use [TextureManager.getOrCreateTexture] instead.
 */
class TextureImpl(val location: String) : Texture {
    val id: Int
    val width: Int
    val height: Int

    init {
        val imageData = try {
            this::class.java.getResourceAsStream(this.location)?.toByteBuffer()?.toMemoryManaged() ?: throw IOException("Null Resource")
        } catch (e: IOException) {
            throw RuntimeException("\"${this.location}\" is not a valid texture", e)
        }

        val widthBuffer = IntArray(1)
        val heightBuffer = IntArray(1)
        val channelsBuffer = IntArray(1)

        val texture = STBImage.stbi_load_from_memory(imageData, widthBuffer, heightBuffer, channelsBuffer, 4)

        imageData.freeMemoryManaged()

        if (texture == null) {
            throw RuntimeException("\"${this.location}\" is not a valid texture: ${STBImage.stbi_failure_reason()}")
        }

        this.width = widthBuffer.first()
        this.height = heightBuffer.first()

        this.id = glCreateTextures(GL_TEXTURE_2D)

        glTextureParameteri(this.id, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTextureParameteri(this.id, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1) // stbi
        //TODO: work out why everything is rgba8
        glTextureStorage2D(this.id, 1, GL_RGBA8, this.width, this.height)
        glTextureSubImage2D(this.id, 0, 0, 0, this.width, this.height, GL_RGBA, GL_UNSIGNED_BYTE, texture)
    }

    override fun bind() {
        if (this.id == -1) {
            println("\"${this.location}\" is not a valid texture")
            return
        }
        glBindTexture(GL_TEXTURE_2D, this.id)
    }
}