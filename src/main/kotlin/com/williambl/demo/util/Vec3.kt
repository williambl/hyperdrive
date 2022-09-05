package com.williambl.demo.util

import kotlin.math.sqrt

class Vec3(val x: Double, val y: Double, val z: Double) {
    val length by lazy { sqrt(this.x * this.x + this.y * this.y + this.z * this.z) }

    infix fun cross(other: Vec3): Vec3 {
        return Vec3(this.y * other.z - this.z * other.y, this.z * other.x - this.x * other.z, this.x * other.y - this.y * other.x)
    }

    operator fun times(factor: Double): Vec3 {
        return Vec3(this.x * factor, this.y * factor, this.z * factor)
    }

    operator fun plus(other: Vec3): Vec3 {
        return Vec3(this.x + other.x, this.y + other.y, this.z + other.z)
    }

    operator fun minus(other: Vec3): Vec3 {
        return Vec3(this.x - other.x, this.y - other.y, this.z - other.z)
    }

    fun normalised(): Vec3 {
        return if (this.length == 0.0) Vec3(0.0, 0.0, 0.0) else Vec3(this.x / this.length, this.y / this.length, this.z / this.length)
    }
}
