package com.williambl.demo.shader

import com.williambl.demo.freeMemoryManaged
import com.williambl.demo.material.Material
import com.williambl.demo.toStackManaged
import com.williambl.demo.util.Mat3x3
import com.williambl.demo.util.Mat4x4
import com.williambl.demo.util.Vec3
import org.lwjgl.opengl.GL20
import org.lwjgl.opengl.GL41.*
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer

/**
 * A representation of an OpenGL Shader Program (i.e. a fragment and vertex shader pair).
 *
 * Do not call the constructor, use [ShaderManager.getOrCreateShaderProgram] instead.
 */
class ShaderProgram(val name: String, val properties: ShaderProperties, private val id: Int) {
    private val uniforms: MutableMap<String, Uniform> = mutableMapOf()

    init {
        val uniformCount = glGetProgrami(this.id, GL_ACTIVE_UNIFORMS)
        val maxNameSize = if (uniformCount == 0) { 0 } else { glGetProgrami(this.id, GL_ACTIVE_UNIFORM_MAX_LENGTH) }

        val nameLength = IntArray(1)
        val arrayLength = IntArray(1)
        val uniformType = IntArray(1)
        ByteBuffer.allocate(maxNameSize).toStackManaged {name ->
            for (i in 0 until uniformCount) {
                glGetActiveUniform(this.id, i, nameLength, arrayLength, uniformType, name)
                val nameString = MemoryUtil.memASCII(name, nameLength[0])
                this.uniforms[nameString] = Uniform(nameString, glGetUniformLocation(this.id, nameString), arrayLength[0], uniformType[0])
            }
        }
    }

    fun use() {
        glUseProgram(this.id)
    }

    fun createMaterial(builderConsumer: MaterialBuilder.() -> Unit): Material = MaterialBuilder(this).also(builderConsumer).build()

    fun getSamplers(): List<Uniform> = this.uniforms.values.filter { it.type in GL_SAMPLER_1D..GL_SAMPLER_2D_SHADOW } //TODO are there later sampler types we should check for here?

    //TODO more powerful utilities with uniforms

    fun setUniform(name: String, value: Mat4x4) {
        val uniform = this.uniforms[name]
        if (uniform != null && uniform.type == GL_FLOAT_MAT4) {
            glProgramUniformMatrix4fv(this.id, uniform.id, false, value.forGl())
        }
    }

    fun setUniform(name: String, value: Mat3x3) {
        val uniform = this.uniforms[name]
        if (uniform != null && uniform.type == GL_FLOAT_MAT3) {
            glProgramUniformMatrix3fv(this.id, uniform.id, false, value.forGl())
        }
    }

    fun setUniform(name: String, value: Int) {
        val uniform = this.uniforms[name]
        if (uniform != null && uniform.type == GL_INT) {
            glProgramUniform1i(this.id, uniform.id, value)
        }
    }

    fun setUniform(name: String, value: Float) {
        val uniform = this.uniforms[name]
        if (uniform != null && uniform.type == GL_FLOAT) {
            glProgramUniform1f(this.id, uniform.id, value)
        }
    }

    fun setUniform(name: String, x: Float, y: Float) {
        val uniform = this.uniforms[name]
        if (uniform != null && uniform.type == GL_FLOAT_VEC2) {
            glProgramUniform2f(this.id, uniform.id, x, y)
        }
    }

    fun setUniform(name: String, value: Vec3) {
        val uniform = this.uniforms[name]
        if (uniform != null && uniform.type == GL_FLOAT_VEC3) {
            glProgramUniform3f(this.id, uniform.id, value.x.toFloat(), value.y.toFloat(), value.z.toFloat())
        }
    }
}