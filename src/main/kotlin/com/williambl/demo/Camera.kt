package com.williambl.demo

import com.williambl.demo.animation.AnimatedDouble
import com.williambl.demo.framebuffer.Framebuffer
import com.williambl.demo.shader.ShaderManager
import com.williambl.demo.transform.Transform
import com.williambl.demo.util.Mat4x4
import com.williambl.demo.util.MatrixStack
import com.williambl.demo.util.Time
import com.williambl.demo.util.Vec4
import org.lwjgl.opengl.GL11.*

class Camera(val transform: Transform, val fov: AnimatedDouble, val nearPlane: Double, val farPlane: Double, val framebuffer: Framebuffer, val beforeRender: () -> Unit = { glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT) }) {
    val aspectRatio: Double
        get() = this.framebuffer.width.toDouble()/this.framebuffer.height.toDouble()

    fun render(time: Time, renderables: List<Renderable>) {
        val renderContext = RenderingContext(MatrixStack(), this.viewMatrix(time), this.projectionMatrix(time), this.transform.translation(time), time)
        ShaderManager.setGlobalUniforms(renderContext)
        this.framebuffer.bind()
        this.beforeRender()
        if (this.framebuffer.hasDepth) {
            glEnable(GL_DEPTH_TEST)
        }
        renderables.forEach { it.render(renderContext) }
    }

    fun viewMatrix(time: Time): Mat4x4 {
        val pos = this.transform.translation(time)
        val rotation = Mat4x4.rotate(this.transform.rotation(time).flip())
        val forwards = (rotation * Vec4(0.0, 0.0, 1.0, 1.0)).toVec3().normalised()
        val up = (rotation * Vec4(0.0, 1.0, 0.0, 1.0)).toVec3().normalised()

        return Mat4x4.view(pos, forwards, up)
    }

    fun projectionMatrix(time: Time): Mat4x4 {
        return Mat4x4.perspective(this.fov.valueAt(time), this.aspectRatio, this.nearPlane, this.farPlane)
    }
}