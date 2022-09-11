package com.williambl.demo.util

import kotlin.math.*

class Quaternion(val r: Double = 1.0, val i: Double = 0.0, val j: Double = 0.0, val k: Double = 0.0) {
    val axis: Vec3 by lazy { Vec3(this.i, this.j, this.k).normalised() }
    val theta: Double by lazy { 2 * atan2(sqrt(this.i * this.i + this.j * this.j + this.k * this.k), this.r) }

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

    fun flip(): Quaternion {
        return create(this.axis * -1.0, this.theta)
    }

    companion object {
        fun slerp(a: Quaternion, b: Quaternion, fac: Double): Quaternion {
            val cosTheta = a.r * b.r + a.i * b.i + a.j * b.j + a.k * b.k // oh neat, it's the dot product
            val theta = acos(cosTheta)
            val sinTheta = sin(theta)

            if (abs(sinTheta) < 0.001) {
                return Quaternion(
                    a.r * 0.5 + b.r * 0.5,
                    a.i * 0.5 + b.i * 0.5,
                    a.j * 0.5 + b.j * 0.5,
                    a.k * 0.5 + b.k * 0.5
                )
            }

            return ((a * sin((1-fac)*theta) + b * sin(fac*theta)) / sin(theta))
        }

        fun create(axis: Vec3, theta: Double): Quaternion {
            val scaledAxis = axis * sin(theta/2.0)
            return Quaternion(cos(theta/2.0), scaledAxis.x, scaledAxis.y, scaledAxis.z)
        }
    }
}