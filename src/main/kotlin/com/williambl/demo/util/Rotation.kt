package com.williambl.demo.util

class Rotation(val axis: Vec3, val theta: Double) {
    fun flip(): Rotation = Rotation(this.axis * -1.0, this.theta)
}