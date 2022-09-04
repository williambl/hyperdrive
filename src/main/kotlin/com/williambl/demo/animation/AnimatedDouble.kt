package com.williambl.demo.animation

import com.williambl.demo.util.lerp

class AnimatedDouble(first: Double, vararg rest: Keyframe<Double>): Animated<Double>(first, *rest) {
    override fun interpolate(first: Double, last: Double, fac: Double): Double {
        return lerp(first, last, fac)
    }
}