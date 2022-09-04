package com.williambl.demo.shader

import com.williambl.demo.util.Mat4x4
import com.williambl.demo.util.Vec3
import org.lwjgl.opengl.GL20.*

/**
 * A representation of an OpenGL Shader Program (i.e. a fragment and vertex shader pair).
 *
 * Do not call the constructor, use [ShaderManager.getOrCreateShaderProgram] instead.
 */
class ShaderProgram(val name: String, private val id: Int) {
    private val uniforms: MutableMap<String, Int> = mutableMapOf()

    fun use() {
        glUseProgram(this.id)
    }

    fun setUniform(name: String, value: Mat4x4) {
        val loc = this.uniforms.computeIfAbsent(name) { glGetUniformLocation(this.id, name) }
        glUniformMatrix4fv(loc, false, value.forGl())
    }

    fun setUniform(name: String, value: Float) {
        val loc = this.uniforms.computeIfAbsent(name) { glGetUniformLocation(this.id, name) }
        glUniform1f(loc, value)
    }

    fun setUniform(name: String, value: Vec3) {
        val loc = this.uniforms.computeIfAbsent(name) { glGetUniformLocation(this.id, name) }
        glUniform3f(loc, value.x.toFloat(), value.y.toFloat(), value.z.toFloat())
    }
}