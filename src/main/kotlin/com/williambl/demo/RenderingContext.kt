package com.williambl.demo

import com.williambl.demo.util.Mat4x4
import com.williambl.demo.util.MatrixStack
import com.williambl.demo.util.Time
import com.williambl.demo.util.Vec3

class RenderingContext(val modelStack: MatrixStack, val view: Mat4x4, val projection: Mat4x4, val cameraPos: Vec3, val time: Time) {
}