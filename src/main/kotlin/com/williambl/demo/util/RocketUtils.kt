package com.williambl.demo.util

import com.williambl.demo.rocket4j.Rocket4J
import com.williambl.demo.rocket4j.Track

data class Vec3Track(private val trackX: Track, private val trackY: Track, private val trackZ: Track) {
    fun getValue(rows: Double): Vec3 {
        return Vec3(this.trackX.getValue(rows), this.trackY.getValue(rows), this.trackZ.getValue(rows))
    }
}

data class RotationTrack(private val trackX: Track, private val trackY: Track, private val trackZ: Track, private val trackTheta: Track) {
    fun getValue(rows: Double): Quaternion {
        val keysX = this.trackX.getKeysForLerp(rows)
        val keysY = this.trackY.getKeysForLerp(rows)
        val keysZ = this.trackZ.getKeysForLerp(rows)
        val keysTheta = this.trackTheta.getKeysForLerp(rows)

        if (keysX.isEmpty() || keysY.isEmpty() || keysZ.isEmpty() || keysTheta.isEmpty()) {
            return Quaternion()
        }

        val rotationA = Quaternion.create(Vec3(keysX.getOrNull(0)?.value?.toDouble() ?: 0.0, keysY.getOrNull(0)?.value?.toDouble() ?: 0.0, keysZ.getOrNull(0)?.value?.toDouble() ?: 0.0), keysTheta.getOrNull(0)?.value?.toDouble() ?: 1.0)
        val rotationB = Quaternion.create(Vec3(keysX.getOrNull(1)?.value?.toDouble() ?: 0.0, keysY.getOrNull(1)?.value?.toDouble() ?: 0.0, keysZ.getOrNull(1)?.value?.toDouble() ?: 0.0), keysTheta.getOrNull(1)?.value?.toDouble() ?: 1.0)
        val fac = if (keysTheta.size == 2) keysTheta[0].keyType.apply((rows - keysTheta[0].row.toDouble()) / (keysTheta[1].row - keysTheta[0].row).toDouble()) else 0.0

        return Quaternion.slerp(rotationA, rotationB, fac)
    }
}

fun Rocket4J.getVec3Track(name: String): Vec3Track {
    return Vec3Track(
        this.getTrack(name + "_x"),
        this.getTrack(name + "_y"),
        this.getTrack(name + "_z"),
    )
}

fun Rocket4J.getRotationTrack(name: String): RotationTrack {
    return RotationTrack(
        this.getTrack(name + "_x"),
        this.getTrack(name + "_y"),
        this.getTrack(name + "_z"),
        this.getTrack(name + "_θ")
    )
}