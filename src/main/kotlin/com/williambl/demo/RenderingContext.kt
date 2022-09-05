package com.williambl.demo

import com.williambl.demo.util.Mat4x4
import com.williambl.demo.util.MatrixStack

class RenderingContext(val modelStack: MatrixStack, val view: Mat4x4, val projection: Mat4x4, val time: Double) {
}