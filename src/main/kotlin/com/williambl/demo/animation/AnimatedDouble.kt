package com.williambl.demo.animation

import com.williambl.demo.Hyperdrive
import com.williambl.demo.util.Time

class AnimatedDouble private constructor(private val name: String?, private val value: Double?) {

    fun valueAt(time: Time): Double {
        return this.name?.let { Hyperdrive.rocket.getTrack(it).getValue(time.rocketRows) } ?: this.value ?: 0.0
    }

    companion object {
        fun byRocket(name: String): AnimatedDouble {
            return AnimatedDouble(name, null)
        }

        fun byValue(value: Double): AnimatedDouble {
            return AnimatedDouble(null, value)
        }
    }
}