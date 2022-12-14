package com.williambl.demo

import com.williambl.demo.transform.Transform

open class WorldObject(var transform: Transform, vararg var children: Renderable): Renderable {
    override fun setup() {
        this.children.forEach(Renderable::setup)
    }

    override fun render(ctx: RenderingContext) {
        ctx.modelStack.autoPop {
            ctx.modelStack.push(this.transform.matrix(ctx.time))
            this.children.forEach { it.render(ctx) }
        }
    }
}