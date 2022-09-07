package com.williambl.demo.model

import com.williambl.demo.util.Vec3
import org.jetbrains.annotations.Range
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer
import kotlin.reflect.KClass

class Vertices(vararg val attributes: Attribute<*>) {
    private val attributeValues: MutableMap<Attribute<*>, MutableList<*>> = mutableMapOf()
    private var vertexCount: Int = 0
    val stride = this.attributes.sumOf(Attribute<*>::size)
    @Suppress("UNCHECKED_CAST")
    operator fun <T: Number> get(attribute: Attribute<T>): MutableList<T>
        = this.attributeValues.getOrPut(attribute) { mutableListOf<T>() } as MutableList<T>

    fun next(): Vertices {
        this.vertexCount++
        return this
    }

    fun toBytes(): ByteBuffer {
        val bytes: ByteBuffer = MemoryUtil.memAlloc(this.stride * this.vertexCount)
        for (vertex in 0 until this.vertexCount) {
            for (attr in this.attributes) {
                for (i in 0 until attr.count) {
                    bytes.put(this[attr][(vertex * attr.count) + i])
                }
            }
        }

        if (bytes.hasRemaining()) {
            throw IllegalStateException("Could not fill vertices buffer! ${bytes.remaining()} bytes remaining.")
        }

        return bytes.rewind()
    }

    open class Attribute<T : Number> protected constructor(val count: @Range(from = 1, to = 4) Int, val name: String, val klass: KClass<T>) {
        val width: Int = this.klass.size
        val type: Int = this.klass.glType
        val size: Int = this.count * this.width

        init {
            byName[this.name] = this
        }

        override fun toString(): String {
            return this.name
        }

        companion object {
            private val byName: MutableMap<String, Attribute<*>> = mutableMapOf()

            fun byName(name: String): Attribute<*>? {
                return this.byName[name]
            }
        }

        object Position: Attribute<Float>(3, "position", Float::class) {
            fun Vertices.position(x: Float, y: Float, z: Float): Vertices {
                this[Position].add(x)
                this[Position].add(y)
                this[Position].add(z)
                return this
            }
            fun Vertices.position(x: Double, y: Double, z: Double): Vertices = this.position(x.toFloat(), y.toFloat(), z.toFloat())
            fun Vertices.position(pos: Vec3): Vertices = this.position(pos.x, pos.y, pos.z)
        }

        object Color: Attribute<Float>(3, "inColor", Float::class) {
            fun Vertices.color(r: Float, g: Float, b: Float): Vertices {
                this[Color].add(r)
                this[Color].add(g)
                this[Color].add(b)
                return this
            }
            fun Vertices.color(r: Double, g: Double, b: Double) = this.color(r.toFloat(), g.toFloat(), b.toFloat())
        }

        object Texture: Attribute<Float>(2, "inTexCoord", Float::class) {
            fun Vertices.tex(u: Float, v: Float): Vertices {
                this[Texture].add(u)
                this[Texture].add(v)
                return this
            }
            fun Vertices.tex(u: Double, v: Double) = this.tex(u.toFloat(), v.toFloat())
        }

        object Normal: Attribute<Float>(3, "normal", Float::class) {
            fun Vertices.normal(x: Float, y: Float, z: Float): Vertices {
                this[Normal].add(x)
                this[Normal].add(y)
                this[Normal].add(z)
                return this
            }
            fun Vertices.normal(x: Double, y: Double, z: Double) = this.normal(x.toFloat(), y.toFloat(), z.toFloat())
            fun Vertices.normal(pos: Vec3) = this.normal(pos.x, pos.y, pos.z)
        }
    }
}

private fun ByteBuffer.put(number: Number) {
    when(number) {
        is Byte -> this.put(number)
        is Short -> this.putShort(number)
        is Int -> this.putInt(number)
        is Long -> this.putLong(number)
        is Float -> this.putFloat(number)
        is Double -> this.putDouble(number)
        else -> throw UnsupportedOperationException("Cannot put a ${number::class} into a ByteBuffer")
    }
}

private val <T: Number> KClass<T>.size: Int
    get() = when (this) {
        Byte::class -> Byte.SIZE_BYTES
        Short::class -> Short.SIZE_BYTES
        Int::class -> Int.SIZE_BYTES
        Long::class -> Long.SIZE_BYTES
        Float::class -> Float.SIZE_BYTES
        Double::class -> Double.SIZE_BYTES
        else -> throw UnsupportedOperationException("$this does not have an OpenGL type")
    }

private val <T: Number> KClass<T>.glType: Int
    get() = when (this) {
        Byte::class -> GL_BYTE
        Short::class -> GL_SHORT
        Int::class -> GL_INT
        Float::class -> GL_FLOAT
        Double::class -> GL_DOUBLE
        else -> throw UnsupportedOperationException("$this does not have an OpenGL type")
    }
