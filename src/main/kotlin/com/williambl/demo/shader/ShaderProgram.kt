package com.williambl.demo.shader

import com.williambl.demo.util.Mat3x3
import com.williambl.demo.util.Mat4x4
import com.williambl.demo.util.Vec3
import org.lwjgl.opengl.GL20.*

/**
 * A representation of an OpenGL Shader Program (i.e. a fragment and vertex shader pair).
 *
 * Do not call the constructor, use [ShaderManager.getOrCreateShaderProgram] instead.
 */
class ShaderProgram(val name: String, val properties: ShaderProperties, private val id: Int) {
    private val uniforms: MutableMap<String, Int> = mutableMapOf()

    fun use() {
        glUseProgram(this.id)
    }

    fun setUniform(name: String, value: Mat4x4) {
        this.use()
        val loc = this.uniforms.computeIfAbsent(name) { glGetUniformLocation(this.id, name) }
        if (loc != -1) {
            glUniformMatrix4fv(loc, false, value.forGl())
        }
    }

    fun setUniform(name: String, value: Mat3x3) {
        this.use()
        val loc = this.uniforms.computeIfAbsent(name) { glGetUniformLocation(this.id, name) }
        if (loc != -1) {
            glUniformMatrix3fv(loc, false, value.forGl())
        }
    }


    fun setUniform(name: String, value: Float) {
        this.use()
        val loc = this.uniforms.computeIfAbsent(name) { glGetUniformLocation(this.id, name) }
        glUniform1f(loc, value)
    }

    fun setUniform(name: String, x: Float, y: Float) {
        this.use()
        val loc = this.uniforms.computeIfAbsent(name) { glGetUniformLocation(this.id, name) }
        if (loc != -1) {
            glUniform2f(loc, x, y)
        }
    }

    fun setUniform(name: String, value: Vec3) {
        this.use()
        val loc = this.uniforms.computeIfAbsent(name) { glGetUniformLocation(this.id, name) }
        if (loc != -1) {
            glUniform3f(loc, value.x.toFloat(), value.y.toFloat(), value.z.toFloat())
        }
    }
}