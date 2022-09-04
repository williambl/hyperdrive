package com.williambl.demo.animation

import com.williambl.demo.util.Mat4x4
import com.williambl.demo.util.Rotation
import com.williambl.demo.util.Vec3

class AnimatedTransform(position: Vec3 = Vec3(0.0, 0.0, 0.0), rotation: Rotation = Rotation(Vec3(0.0, 0.0, 0.0), 0.0), scale: Vec3 = Vec3(1.0, 1.0, 1.0)) {
    val animatedPosition = AnimatedVec3(position)
    val animatedRotation = AnimatedRotation(rotation)
    val animatedScale = AnimatedVec3(scale)

    operator fun set(time: Double, transformer: TransformData.() -> Unit) {
        val data = TransformData(null, null, null)
        data.transformer()
        data.position?.let { this.animatedPosition.add(Animated.Keyframe(time, it)) }
        data.rotation?.let { this.animatedRotation.add(Animated.Keyframe(time, it)) }
        data.scale?.let { this.animatedScale.add(Animated.Keyframe(time, it)) }
    }

    fun getMatrix(time: Double): Mat4x4 {
        return Mat4x4.scale(this.animatedScale.valueAt(time)) * Mat4x4.rotate(this.animatedRotation.valueAt(time)) * Mat4x4.translate(this.animatedPosition.valueAt(time))
    }

    class TransformData(var position: Vec3?, var rotation: Rotation?, var scale: Vec3?) {}
}