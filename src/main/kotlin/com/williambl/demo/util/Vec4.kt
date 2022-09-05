package com.williambl.demo.util

class Vec4(val x: Double, val y: Double, val z: Double, val w: Double) {
    fun toVec3(): Vec3 {
        return Vec3(this.x, this.y, this.z)
    }

}
