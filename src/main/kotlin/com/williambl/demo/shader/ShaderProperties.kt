package com.williambl.demo.shader

import com.williambl.demo.model.Vertices
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

data class ShaderProperties(val attributes: Array<Vertices.Attribute<*>>) {
    companion object {
        fun fromInput(input: InputStream): ShaderProperties {
            val reader = BufferedReader(InputStreamReader(input))

            var isReadingAttributes = false
            val attributes = mutableListOf<Vertices.Attribute<*>>()

            reader.useLines { lines ->
                for (line in lines) {
                    if (line == "attributes:") {
                        isReadingAttributes = true
                        continue
                    }

                    if (isReadingAttributes) {
                        Vertices.Attribute.byName(line)?.let { attributes.add(it) }
                    }
                }
            }

            return ShaderProperties(attributes.toTypedArray())
        }
    }
}
