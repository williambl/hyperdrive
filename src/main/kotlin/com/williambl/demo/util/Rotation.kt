package com.williambl.demo.util

import kotlin.math.*

class Rotation(axis: Vec3, val theta: Double): Quaternion() {
    val axis = axis.normalised()
    override val r = cos(this.theta/2.0)
    override val i: Double
    override val j: Double
    override val k: Double

    constructor(): this(Vec3(0.0, 0.0, 0.0), 1.0)

    init {
        val sinHalfTheta = sin(this.theta/2.0)
        this.i = this.axis.x * sinHalfTheta
        this.j = this.axis.y * sinHalfTheta
        this.k = this.axis.z * sinHalfTheta
    }

    fun flip(): Rotation = Rotation(this.axis * -1.0, this.theta)

    companion object {
        fun create(r: Double, i: Double, j: Double, k: Double): Rotation {
            val len = sqrt(i * i + j * j + k * k)
            if (len == 0.0) {
                return Rotation()
            }

            val axis = Vec3(i/len, j/len, k/len)
            val theta = 2 * atan2(len, r)
            return Rotation(axis, theta)
        }

        fun slerp(a: Rotation, b: Rotation, fac: Double): Rotation {
            val cosTheta = a.r * b.r + a.i * b.i + a.j * b.j + a.k * b.k // oh neat, it's the dot product
            val theta = acos(cosTheta)
            val sinTheta = sin(theta)
            if (abs(sinTheta) < 0.001) {
                return create(a.r * 0.5 + b.r * 0.5, a.i * 0.5 + b.i * 0.5, a.j * 0.5 + b.j * 0.5, a.k * 0.5 + b.k * 0.5)
            }
            return ((a * sin((1-fac)*theta) + b * sin(fac*theta)) / sin(theta)).toRotation()
        }
    }
}