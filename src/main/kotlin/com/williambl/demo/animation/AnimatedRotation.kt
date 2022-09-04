package com.williambl.demo.animation

import com.williambl.demo.util.Rotation
import com.williambl.demo.util.Vec3
import com.williambl.demo.util.lerp

class AnimatedRotation(first: Rotation, vararg rest: Keyframe<Rotation>): Animated<Rotation>(first, *rest) {
    override fun interpolate(first: Rotation, last: Rotation, fac: Double): Rotation {
        return Rotation(Vec3(lerp(first.axis.x, last.axis.x, fac), lerp(first.axis.y, last.axis.y, fac), lerp(first.axis.z, last.axis.z, fac)), lerp(first.theta, last.theta, fac))
    }
}