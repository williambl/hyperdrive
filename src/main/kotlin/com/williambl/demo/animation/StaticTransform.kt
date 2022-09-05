package com.williambl.demo.animation

import com.williambl.demo.transform.Transform
import com.williambl.demo.util.Mat4x4
import com.williambl.demo.util.Rotation
import com.williambl.demo.util.Time
import com.williambl.demo.util.Vec3

class StaticTransform(val translation: Vec3, val rotation: Rotation, val scale: Vec3): Transform {
    override fun translation(time: Time): Vec3 {
        return this.translation
    }

    override fun rotation(time: Time): Rotation {
        return this.rotation
    }

    override fun scale(time: Time): Vec3 {
        return this.scale
    }
}