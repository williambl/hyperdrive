package com.williambl.demo.shader

import com.williambl.demo.RenderingContext
import org.lwjgl.opengl.GL20.*
import org.lwjgl.opengl.GL45
import java.io.FileNotFoundException
import java.io.InputStream

object ShaderManager {

    private val shaders: MutableMap<String, ShaderProgram> = mutableMapOf()

    /**
     * Loads a [ShaderProgram] from a resource named [shaderName].
     *
     * Shader programs are in the following format:
     * ```
     * shaders
     * ├── shaderName
     * │   ├── shaderName.shad
     * │   ├── shaderName.frag
     * │   └── shaderName.vert
     * ```
     */
    fun getOrCreateShaderProgram(shaderName: String): ShaderProgram {
        return shaders.getOrPut(shaderName) {
            createCompleteShaderProgram(
                this::class.java.getResource("/shaders/$shaderName/$shaderName.shad")?.openStream() ?: throw FileNotFoundException("Could not find /shaders/$shaderName/$shaderName.shad"),
                this::class.java.getResource("/shaders/$shaderName/$shaderName.vert")?.readText() ?: throw FileNotFoundException("Could not find /shaders/$shaderName/$shaderName.vert"),
                this::class.java.getResource("/shaders/$shaderName/$shaderName.frag")?.readText() ?: throw FileNotFoundException("Could not find /shaders/$shaderName/$shaderName.frag"),
                shaderName
            )
        }
    }

    fun setGlobalUniforms(context: RenderingContext) {
        this.shaders.values.forEach {
            it.setUniform("View", context.view)
            it.setUniform("Projection", context.projection)
            it.setUniform("CameraPos", context.cameraPos)
        }
    }

    private fun createCompleteShaderProgram(shaderPropertiesSrc: InputStream, vertexShaderSrc: String, fragmentShaderSrc: String, name: String): ShaderProgram {
        val properties = ShaderProperties.fromInput(shaderPropertiesSrc)
        val vertexId = compileShader(vertexShaderSrc, GL45.GL_VERTEX_SHADER)
        val fragmentId = compileShader(fragmentShaderSrc, GL45.GL_FRAGMENT_SHADER)
        val shaderProgramId = createAndLinkShaderProgram(vertexId, fragmentId)
        GL45.glDeleteShader(vertexId)
        GL45.glDeleteShader(fragmentId)
        return ShaderProgram(name, properties, shaderProgramId)
    }

    private fun compileShader(source: String, type: Int): Int {
        val shader = GL45.glCreateShader(type)
        GL45.glShaderSource(shader, source)
        GL45.glCompileShader(shader)
        if (glGetShaderi(shader, GL_COMPILE_STATUS) != GL_TRUE) {
            throw RuntimeException("Problem compiling shader! ${glGetShaderInfoLog(shader)}")
        }
        return shader
    }

    private fun createAndLinkShaderProgram(vertexShader: Int, fragmentShader: Int): Int {
        val shaderProgram = GL45.glCreateProgram()
        GL45.glAttachShader(shaderProgram, vertexShader)
        GL45.glAttachShader(shaderProgram, fragmentShader)
        GL45.glLinkProgram(shaderProgram)
        return shaderProgram
    }
}