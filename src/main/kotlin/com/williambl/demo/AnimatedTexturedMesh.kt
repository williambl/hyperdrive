package com.williambl.demo

class AnimatedTexturedMesh(vertices: FloatArray, indices: FloatArray, shaderProgram: ShaderProgram, private vararg val textures: Texture) :
    TexturedMesh(vertices, indices, shaderProgram, textures[0]) {

    init {
        if (this.textures.isEmpty()) {
            throw IllegalArgumentException("No textures given for animated textured mesh")
        }
    }

    private var currentTextureIndex: Int = 0

    override fun render() {
        this.currentTextureIndex = (this.currentTextureIndex + 1) % this.textures.size
        this.texture = this.textures[this.currentTextureIndex]
        super.render()
    }
}