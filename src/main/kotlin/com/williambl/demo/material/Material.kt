package com.williambl.demo.material

import com.williambl.demo.shader.ShaderProgram
import com.williambl.demo.texture.Texture
import com.williambl.demo.texture.TextureSlot

class Material(val shader: ShaderProgram, val textures: List<TextureSlot>) {
    val texturesByString: Map<String, TextureSlot> = this.textures.associateBy { it.name }

    constructor(shader: ShaderProgram): this(
        shader,
        shader.getSamplers().map { TextureSlot(it.name, null) }
    )

    constructor(shader: ShaderProgram, vararg textures: Texture): this(
        shader,
        shader.getSamplers().withIndex().map { TextureSlot(it.value.name, textures.getOrNull(it.index)) }
    )
}