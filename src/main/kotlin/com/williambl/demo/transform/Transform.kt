package com.williambl.demo.transform

import com.williambl.demo.util.Mat4x4
import com.williambl.demo.util.Rotation
import com.williambl.demo.util.Time
import com.williambl.demo.util.Vec3

interface Transform {
    fun matrix(time: Time): Mat4x4 {
        return Mat4x4.translate(this.translation(time)) * Mat4x4.rotate(this.rotation(time)) * Mat4x4.scale(this.scale(time))
    }

    fun translation(time: Time): Vec3
    fun rotation(time: Time): Rotation
    fun scale(time: Time): Vec3
}