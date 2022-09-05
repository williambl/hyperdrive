package com.williambl.demo

import com.williambl.demo.animation.AnimatedDouble
import com.williambl.demo.transform.Transform
import com.williambl.demo.util.Mat4x4
import com.williambl.demo.util.Time
import com.williambl.demo.util.Vec4

class Camera(val transform: Transform, val fov: AnimatedDouble, var aspectRatio: Double, val nearPlane: Double, val farPlane: Double) {
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