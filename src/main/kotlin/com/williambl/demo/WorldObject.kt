package com.williambl.demo

import com.williambl.demo.animation.AnimatedTransform

open class WorldObject(val transform: AnimatedTransform, val renderable: Renderable): Renderable {
    override fun setup() {
        this.renderable.setup()
    }

    override fun render(ctx: RenderingContext) {
        ctx.modelStack.autoPop {
            ctx.modelStack.push(this.transform.getMatrix(ctx.time))
            this.renderable.render(ctx)
        }
    }
}