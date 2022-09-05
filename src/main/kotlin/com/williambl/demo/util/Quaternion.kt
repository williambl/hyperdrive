package com.williambl.demo.util

open class Quaternion(open val r: Double = 0.0, open val i: Double = 0.0, open val j: Double = 0.0, open val k: Double = 0.0) {
    operator fun times(other: Quaternion): Quaternion {
        val thisIjk = Vec3(this.i, this.j, this.k)
        val otherIjk = Vec3(other.i, other.j, other.k)
        val resultIjk = thisIjk cross otherIjk + otherIjk * this.r + thisIjk * other.r
        val resultR = this.r * other.r - (thisIjk dot otherIjk)
        return Quaternion(resultIjk.x, resultIjk.y, resultIjk.z, resultR)
    }

    operator fun plus(other: Quaternion): Quaternion {
        return Quaternion(this.r + other.r, this.i + other.i, this.j + other.j, this.k + other.k)
    }

    operator fun times(factor: Double): Quaternion {
        return Quaternion(this.r * factor, this.i * factor, this.j * factor, this.k * factor)
    }

    operator fun div(factor: Double): Quaternion {
        return Quaternion(this.r / factor, this.i / factor, this.j / factor, this.k / factor)
    }

    fun toRotation(): Rotation {
        return if (this is Rotation) this else Rotation.create(this.r, this.i, this.j, this.k)
    }
}