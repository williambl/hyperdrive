package com.williambl.demo

import com.williambl.demo.animation.AnimatedDouble
import com.williambl.demo.animation.AnimatedTransform
import com.williambl.demo.util.Mat4x4
import com.williambl.demo.util.Vec4

class Camera(val transform: AnimatedTransform, val fov: AnimatedDouble, var aspectRatio: Double, val nearPlane: Double, val farPlane: Double) {
    fun viewMatrix(time: Double): Mat4x4 {
        val pos = this.transform.animatedPosition.valueAt(time)
        val rotation = Mat4x4.rotate(this.transform.animatedRotation.valueAt(time).flip())
        val forwards = (rotation * Vec4(0.0, 0.0, 1.0, 1.0)).toVec3()
        val up = (rotation * Vec4(0.0, 1.0, 0.0, 1.0)).toVec3()

        return Mat4x4.view(pos, forwards, up)
    }

    fun projectionMatrix(time: Double): Mat4x4 {
        return Mat4x4.perspective(this.fov.valueAt(time), this.aspectRatio, this.nearPlane, this.farPlane)
    }
}