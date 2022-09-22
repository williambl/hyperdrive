package com.williambl.demo.shader

import com.williambl.demo.material.Material
import com.williambl.demo.texture.Texture
import com.williambl.demo.texture.TextureSlot

class MaterialBuilder(val shader: ShaderProgram) {
    private val slots: MutableList<TextureSlot> = mutableListOf()

    fun texture(name: String, value: Texture?) {
        this.slots.add(TextureSlot(name, value))
    }

    fun build(): Material = Material(this.shader, this.slots.sortedBy { this.shader.getSamplers().find { u -> u.name == it.name }?.id ?: -1 })
}
