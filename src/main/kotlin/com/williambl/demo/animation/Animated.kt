package com.williambl.demo.animation

abstract class Animated<T>(first: T, vararg rest: Keyframe<T>) {
    private val keyframes: MutableList<Keyframe<T>> = mutableListOf(Keyframe(0.0, first))

    init {
        rest.forEach { k -> this.add(k) }
    }

    abstract fun interpolate(first: T, last: T, fac: Double): T

    fun valueAt(time: Double): T {
        val firstIdx = this.keyframes.indexOfLast { it.time < time }
        val secondIdx = firstIdx + 1
        if (secondIdx > this.keyframes.lastIndex) {
            return this.keyframes[firstIdx].value
        }

        if (firstIdx < 0) {
            return this.keyframes[0].value
        }

        val first = this.keyframes[firstIdx]
        val second = this.keyframes[secondIdx]
        val totalTimeDelta = second.time - first.time
        val timeDelta = time - first.time
        return interpolate(first.value, second.value, timeDelta/totalTimeDelta)
    }

    fun add(keyframe: Keyframe<T>) {
        this.keyframes.removeAll { it.time == keyframe.time }
        this.keyframes.add(this.keyframes.indexOfLast { it.time < keyframe.time } + 1, keyframe)
    }

    class Keyframe<T>(val time: Double, val value: T)
}