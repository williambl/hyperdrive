package com.williambl.demo.animation

import com.williambl.demo.Hyperdrive
import com.williambl.demo.transform.Transform
import com.williambl.demo.util.*

class RocketTransform(val name: String): Transform {
    override fun translation(time: Time): Vec3 {
        return Hyperdrive.rocket.getVec3Track(this.name + "_trans").getValue(time.rocketRows)
    }

    override fun rotation(time: Time): Rotation {
        return Hyperdrive.rocket.getRotationTrack(this.name + "_rot").getValue(time.rocketRows)
    }

    override fun scale(time: Time): Vec3 {
        return Hyperdrive.rocket.getVec3Track(this.name + "_scale").getValue(time.rocketRows)
    }
}