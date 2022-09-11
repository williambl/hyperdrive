package com.williambl.demo.util

import org.jetbrains.annotations.Contract
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.tan

class Mat3x3() {
    private val row1: Array<Double> = arrayOf(1.0, 0.0, 0.0)
    private val row2: Array<Double> = arrayOf(0.0, 1.0, 0.0)
    private val row3: Array<Double> = arrayOf(0.0, 0.0, 1.0)

    constructor(
        a0: Double, a1: Double, a2: Double,
        b0: Double, b1: Double, b2: Double,
        c0: Double, c1: Double, c2: Double
    ) : this() {
        this[0, 0] = a0
        this[0, 1] = a1
        this[0, 2] = a2
        this[1, 0] = b0
        this[1, 1] = b1
        this[1, 2] = b2
        this[2, 0] = c0
        this[2, 1] = c1
        this[2, 2] = c2
    }

    @Contract(value = "!null -> new", pure = true)
    operator fun times(vec: Vec3): Vec3 {
        return Vec3(
            this[0, 0] * vec.x + this[0, 1] * vec.y + this[0, 2] * vec.z,
            this[1, 0] * vec.x + this[1, 1] * vec.y + this[1, 2] * vec.z,
            this[2, 0] * vec.x + this[2, 1] * vec.y + this[2, 2] * vec.z
        )
    }

    @Contract(value = "!null -> new", pure = true)
    operator fun times(other: Mat3x3): Mat3x3 {
        return Mat3x3().also { newMat ->
            for (i in 0..2) for (j in 0..2) {
                newMat[i, j] = (0..2).sumOf { k -> this[i, k] * other[k, j] }
            }
        }
    }

    @Contract(pure = true)
    operator fun get(row: Int, column: Int): Double {
        return when(row) {
            0 -> this.row1
            1 -> this.row2
            2 -> this.row3
            else -> throw IllegalArgumentException("Row index $row is too high")
        }[column]
    }

    @Contract(pure = true)
    private operator fun set(row: Int, column: Int, value: Double) {
        when(row) {
            0 -> this.row1
            1 -> this.row2
            2 -> this.row3
            else -> throw IllegalArgumentException("Row index $row is too high")
        }[column] = value
    }

    fun forGl(): FloatArray {
        return floatArrayOf(
            this[0, 0].toFloat(),
            this[1, 0].toFloat(),
            this[2, 0].toFloat(),
            this[0, 1].toFloat(),
            this[1, 1].toFloat(),
            this[2, 1].toFloat(),
            this[0, 2].toFloat(),
            this[1, 2].toFloat(),
            this[2, 2].toFloat()
        )
    }
}