package com.williambl.demo.util

import org.jetbrains.annotations.Contract
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class Mat4x4() {
    private val row1: Array<Double> = arrayOf(1.0, 0.0, 0.0, 0.0)
    private val row2: Array<Double> = arrayOf(0.0, 1.0, 0.0, 0.0)
    private val row3: Array<Double> = arrayOf(0.0, 0.0, 1.0, 0.0)
    private val row4: Array<Double> = arrayOf(0.0, 0.0, 0.0, 1.0)

    constructor(
        a0: Double, a1: Double, a2: Double, a3: Double,
        b0: Double, b1: Double, b2: Double, b3: Double,
        c0: Double, c1: Double, c2: Double, c3: Double,
        d0: Double, d1: Double, d2: Double, d3: Double,
    ) : this() {
        this[0, 0] = a0
        this[0, 1] = a1
        this[0, 2] = a2
        this[0, 3] = a3
        this[1, 0] = b0
        this[1, 1] = b1
        this[1, 2] = b2
        this[1, 3] = b3
        this[2, 0] = c0
        this[2, 1] = c1
        this[2, 2] = c2
        this[2, 3] = c3
        this[3, 0] = d0
        this[3, 1] = d1
        this[3, 2] = d2
        this[3, 3] = d3
    }

    @Contract(value = "!null -> new", pure = true)
    operator fun times(vec: Vec4): Vec4 {
        return Vec4(
            this[0, 0] * vec.x + this[0, 1] * vec.y + this[0, 2] * vec.z + this[0, 3] * vec.w,
            this[1, 0] * vec.x + this[1, 1] * vec.y + this[1, 2] * vec.z + this[1, 3] * vec.w,
            this[2, 0] * vec.x + this[2, 1] * vec.y + this[2, 2] * vec.z + this[2, 3] * vec.w,
            this[3, 0] * vec.x + this[3, 1] * vec.y + this[3, 2] * vec.z + this[3, 3] * vec.w
        )
    }

    @Contract(value = "!null -> new", pure = true)
    operator fun times(other: Mat4x4): Mat4x4 {
        return Mat4x4().also { newMat ->
            for (i in 0..3) for (j in 0..3) {
                newMat[i, j] = (0..3).sumOf { k -> this[i, k] * other[k, j] }
            }
        }
    }

    @Contract(pure = true)
    operator fun get(row: Int, column: Int): Double {
        return when(row) {
            0 -> this.row1
            1 -> this.row2
            2 -> this.row3
            3 -> this.row4
            else -> throw IllegalArgumentException("Row index $row is too high")
        }[column]
    }

    private operator fun set(row: Int, column: Int, value: Double) {
        when(row) {
            0 -> this.row1
            1 -> this.row2
            2 -> this.row3
            3 -> this.row4
            else -> throw IllegalArgumentException("Row index $row is too high")
        }[column] = value
    }

    /**
     * Calculates the inverse of this matrix.
     *
     * https://stackoverflow.com/a/44446912/5507477
     *
     * @return the inverse
     */
    @Contract(pure = true)
    fun inverse(): Mat4x4 {
        val a2323 = this[2, 2] * this[3, 3] - this[2, 3] * this[3, 2]
        val a1323 = this[2, 1] * this[3, 3] - this[2, 3] * this[3, 1]
        val a1223 = this[2, 1] * this[3, 2] - this[2, 2] * this[3, 1]
        val a0323 = this[2, 0] * this[3, 3] - this[2, 3] * this[3, 0]
        val a0223 = this[2, 0] * this[3, 2] - this[2, 2] * this[3, 0]
        val a0123 = this[2, 0] * this[3, 1] - this[2, 1] * this[3, 0]
        val a2313 = this[1, 2] * this[3, 3] - this[1, 3] * this[3, 2]
        val a1313 = this[1, 1] * this[3, 3] - this[1, 3] * this[3, 1]
        val a1213 = this[1, 1] * this[3, 2] - this[1, 2] * this[3, 1]
        val a2312 = this[1, 2] * this[2, 3] - this[1, 3] * this[2, 2]
        val a1312 = this[1, 1] * this[2, 3] - this[1, 3] * this[2, 1]
        val a1212 = this[1, 1] * this[2, 2] - this[1, 2] * this[2, 1]
        val a0313 = this[1, 0] * this[3, 3] - this[1, 3] * this[3, 0]
        val a0213 = this[1, 0] * this[3, 2] - this[1, 2] * this[3, 0]
        val a0312 = this[1, 0] * this[2, 3] - this[1, 3] * this[2, 0]
        val a0212 = this[1, 0] * this[2, 2] - this[1, 2] * this[2, 0]
        val a0113 = this[1, 0] * this[3, 1] - this[1, 1] * this[3, 0]
        val a0112 = this[1, 0] * this[2, 1] - this[1, 1] * this[2, 0]

        val det = 1.0 / (
                          this[0, 0] * ( this[1, 1] * a2323 - this[1, 2] * a1323 + this[1, 3] * a1223 )
                        - this[0, 1] * ( this[1, 0] * a2323 - this[1, 2] * a0323 + this[1, 3] * a0223 )
                        + this[0, 2] * ( this[1, 0] * a1323 - this[1, 1] * a0323 + this[1, 3] * a0123 )
                        - this[0, 3] * ( this[1, 0] * a1223 - this[1, 1] * a0223 + this[1, 2] * a0123 )
                )

        return Mat4x4().also {
            it[0, 0] = det *   ( this[1, 1] * a2323 - this[1, 2] * a1323 + this[1, 3] * a1223 )
            it[0, 1] = det * - ( this[0, 1] * a2323 - this[0, 2] * a1323 + this[0, 3] * a1223 )
            it[0, 2] = det *   ( this[0, 1] * a2313 - this[0, 2] * a1313 + this[0, 3] * a1213 )
            it[0, 3] = det * - ( this[0, 1] * a2312 - this[0, 2] * a1312 + this[0, 3] * a1212 )
            it[1, 0] = det * - ( this[1, 0] * a2323 - this[1, 2] * a0323 + this[1, 3] * a0223 )
            it[1, 1] = det *   ( this[0, 0] * a2323 - this[0, 2] * a0323 + this[0, 3] * a0223 )
            it[1, 2] = det * - ( this[0, 0] * a2313 - this[0, 2] * a0313 + this[0, 3] * a0213 )
            it[1, 3] = det *   ( this[0, 0] * a2312 - this[0, 2] * a0312 + this[0, 3] * a0212 )
            it[2, 0] = det *   ( this[1, 0] * a1323 - this[1, 1] * a0323 + this[1, 3] * a0123 )
            it[2, 1] = det * - ( this[0, 0] * a1323 - this[0, 1] * a0323 + this[0, 3] * a0123 )
            it[2, 2] = det *   ( this[0, 0] * a1313 - this[0, 1] * a0313 + this[0, 3] * a0113 )
            it[2, 3] = det * - ( this[0, 0] * a1312 - this[0, 1] * a0312 + this[0, 3] * a0112 )
            it[3, 0] = det * - ( this[1, 0] * a1223 - this[1, 1] * a0223 + this[1, 2] * a0123 )
            it[3, 1] = det *   ( this[0, 0] * a1223 - this[0, 1] * a0223 + this[0, 2] * a0123 )
            it[3, 2] = det * - ( this[0, 0] * a1213 - this[0, 1] * a0213 + this[0, 2] * a0113 )
            it[3, 3] = det *   ( this[0, 0] * a1212 - this[0, 1] * a0212 + this[0, 2] * a0112 )
        }
    }

    @Contract(pure = true)
    fun transpose(): Mat4x4 {
        return Mat4x4(
            this[0, 0], this[1, 0], this[2, 0], this[3, 0],
            this[0, 1], this[1, 1], this[2, 1], this[3, 1],
            this[0, 2], this[1, 2], this[2, 2], this[3, 2],
            this[0, 3], this[1, 3], this[2, 3], this[3, 3]
        )
    }

    fun forGl(): FloatArray {
        return floatArrayOf(
            this[0, 0].toFloat(),
            this[1, 0].toFloat(),
            this[2, 0].toFloat(),
            this[3, 0].toFloat(),
            this[0, 1].toFloat(),
            this[1, 1].toFloat(),
            this[2, 1].toFloat(),
            this[3, 1].toFloat(),
            this[0, 2].toFloat(),
            this[1, 2].toFloat(),
            this[2, 2].toFloat(),
            this[3, 2].toFloat(),
            this[0, 3].toFloat(),
            this[1, 3].toFloat(),
            this[2, 3].toFloat(),
            this[3, 3].toFloat()
        )
    }

    companion object {
        fun scale(amount: Vec3): Mat4x4 {
            return Mat4x4(
                amount.x, 0.0, 0.0, 0.0,
                0.0, amount.y, 0.0, 0.0,
                0.0, 0.0, amount.z, 0.0,
                0.0, 0.0, 0.0, 1.0
            )
        }

        fun translate(offset: Vec3): Mat4x4 {
            return Mat4x4().also { mat ->
                mat[0, 3] = offset.x
                mat[1, 3] = offset.y
                mat[2, 3] = offset.z
            }
        }

        fun rotate(axis: Vec3, theta: Double): Mat4x4 {
            val cos = cos(theta)
            val sin = sin(theta)
            val oneMinusCos = 1 - cos

            return Mat4x4(
                cos + axis.x * axis.x * oneMinusCos, axis.x * axis.y * oneMinusCos - axis.z * sin, axis.x * axis.z * oneMinusCos + axis.y * sin, 0.0,
                axis.y * axis.x * oneMinusCos + axis.z * sin, cos + axis.y * axis.y * oneMinusCos, axis.y * axis.z * oneMinusCos - axis.x * sin, 0.0,
                axis.z * axis.x * oneMinusCos - axis.y * sin, axis.z * axis.y * oneMinusCos + axis.x * sin, cos + axis.z * axis.z * oneMinusCos, 0.0,
                0.0, 0.0, 0.0, 1.0
            )
        }

        fun rotate(rotation: Rotation): Mat4x4 = this.rotate(rotation.axis, rotation.theta)

        // https://www.scratchapixel.com/lessons/3d-basic-rendering/perspective-and-orthographic-projection-matrix/building-basic-perspective-projection-matrix
        fun perspective(fov: Double, aspectRatio: Double, nearPlane: Double, farPlane: Double): Mat4x4 {
            val halfHeight = nearPlane * tan(fov/2.0)
            val halfWidth = halfHeight * aspectRatio
            val depth = farPlane - nearPlane

            return Mat4x4(
                nearPlane/halfWidth, 0.0, 0.0, 0.0,
                0.0, nearPlane/halfHeight, 0.0, 0.0,
                0.0, 0.0, -(farPlane+nearPlane)/depth, -2.0 * ((farPlane*nearPlane)/depth),
                0.0, 0.0, -1.0, 0.0
            )
        }

        // https://registry.khronos.org/OpenGL-Refpages/gl2.1/xhtml/gluLookAt.xml
        fun view(camPos: Vec3, forwards: Vec3, up: Vec3): Mat4x4 {
            val right = Vec3(0.0, 1.0, 0.0).cross(forwards).normalised()
            return Mat4x4(
                right.x, right.y, right.z, 0.0,
                up.x, up.y, up.z, 0.0,
                forwards.x, forwards.y, forwards.z, 0.0,
                0.0, 0.0, 0.0, 1.0
            ) * this.translate(camPos * -1.0)
        }
    }
}