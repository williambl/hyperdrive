package com.williambl.demo.model

import com.williambl.demo.RenderingContext
import com.williambl.demo.shader.ShaderProgram
import com.williambl.demo.texture.Texture

class AnimatedTexturedModel(vertices: FloatArray, indices: IntArray, shaderProgram: ShaderProgram, private vararg val textures: Texture) :
    TexturedModel(vertices, indices, shaderProgram, textures[0]) {

    init {
        if (this.textures.isEmpty()) {
            throw IllegalArgumentException("No textures given for animated textured mesh")
        }
    }

    private var currentTextureIndex: Int = 0

    override fun render(ctx: RenderingContext) {
        this.currentTextureIndex = (this.currentTextureIndex + 1) % this.textures.size
        this.texture = this.textures[this.currentTextureIndex]
        super.render(ctx)
    }
}