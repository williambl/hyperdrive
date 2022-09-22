package com.williambl.demo.texture

class TextureSlot(val name: String, var texture: Texture?): Texture {
    override fun bind() {
        this.texture?.bind()
    }

    companion object {
        val namesForAssimpTypes = listOf(
            "",
            "DiffuseTex",
            "SpecularTex",
            "AmbientTex",
            "EmissiveTex",
            "HeightTex",
            "NormalTex",
            "GlossyTex",
            "OpacityTex",
            "DisplacementTex",
            "LightmapTex",
            "ReflectionTex",
            "BaseTex",
            "NormalCamTex",
            "EmissionTex",
            "MetallicTex",
            "RoughnessTex",
            "AmbientOcclusionTex",
            "SheenTex",
            "ClearCoatTex",
            "TransmissionTex",
            ""
        )
    }
}
