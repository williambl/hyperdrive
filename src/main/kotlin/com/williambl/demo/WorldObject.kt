package com.williambl.demo

import com.williambl.demo.transform.Transform

open class WorldObject(val transform: Transform, val renderable: Renderable): Renderable {
    override fun setup() {
        this.renderable.setup()
    }

    override fun render(ctx: RenderingContext) {
        ctx.modelStack.autoPop {
            ctx.modelStack.push(this.transform.matrix(ctx.time))
            this.renderable.render(ctx)
        }
    }
}