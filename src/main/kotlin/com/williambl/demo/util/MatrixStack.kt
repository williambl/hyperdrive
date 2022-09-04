package com.williambl.demo.util

class MatrixStack {
    private val stack: MutableList<Mat4x4> = mutableListOf(Mat4x4())

    fun value(): Mat4x4 {
        return this.stack.reduce(Mat4x4::times)
    }

    fun push(mat: Mat4x4): MatrixStack {
        this.stack.add(mat)
        return this
    }

    fun push(mat: MatrixStack): MatrixStack {
        this.stack.add(mat.value())
        return this
    }

    fun pop(count: Int = 1): MatrixStack {
        for (i in 0 until count) {
            this.stack.removeLast()
        }
        return this
    }

    fun autoPop(action: () -> Unit): MatrixStack {
        val mark = this.stack.size
        action()
        val newMark = this.stack.size
        this.pop(newMark - mark)

        return this
    }
}