package com.williambl.demo.animation

import com.williambl.demo.util.Vec3
import com.williambl.demo.util.lerp

class AnimatedVec3(first: Vec3, vararg rest: Keyframe<Vec3>): Animated<Vec3>(first, *rest) {
    override fun interpolate(first: Vec3, last: Vec3, fac: Double): Vec3 {
        return Vec3(lerp(first.x, last.x, fac), lerp(first.y, last.y, fac), lerp(first.z, last.z, fac))
    }
}