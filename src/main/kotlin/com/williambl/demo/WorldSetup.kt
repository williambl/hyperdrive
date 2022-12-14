package com.williambl.demo

import com.williambl.demo.animation.RocketTransform
import com.williambl.demo.animation.StaticTransform
import com.williambl.demo.model.TexturedModel
import com.williambl.demo.model.Vertices
import com.williambl.demo.model.Vertices.Attribute.Color.color
import com.williambl.demo.model.Vertices.Attribute.Normal.normal
import com.williambl.demo.model.Vertices.Attribute.Position.position
import com.williambl.demo.model.Vertices.Attribute.Texture.tex
import com.williambl.demo.model.loadModel
import com.williambl.demo.shader.ShaderManager
import com.williambl.demo.texture.TextureManager
import com.williambl.demo.util.Quaternion
import com.williambl.demo.util.Vec3
import kotlin.io.path.Path

fun setupWorld() {
    Hyperdrive.addRenderable("deferred",
        WorldObject(
            RocketTransform("will"),
            TexturedModel(
                Vertices(Vertices.Attribute.Position, Vertices.Attribute.Color, Vertices.Attribute.Texture, Vertices.Attribute.Normal)
                    .position(-0.5f, -0.5f, -0.5f).color(1.0, 1.0, 1.0).tex(0.0f, 0.0f).normal(0.0f,  0.0f, -1.0f).next()
                    .position(0.5f, -0.5f, -0.5f).color(1.0, 1.0, 1.0).tex(1.0f, 0.0f).normal(0.0f,  0.0f, -1.0f).next()
                    .position(0.5f,  0.5f, -0.5f).color(1.0, 1.0, 1.0).tex(1.0f, 1.0f).normal(0.0f,  0.0f, -1.0f).next()
                    .position(0.5f,  0.5f, -0.5f).color(1.0, 1.0, 1.0).tex(1.0f, 1.0f).normal(0.0f,  0.0f, -1.0f).next()
                    .position(-0.5f,  0.5f, -0.5f).color(1.0, 1.0, 1.0).tex(0.0f, 1.0f).normal(0.0f,  0.0f, -1.0f).next()
                    .position(-0.5f, -0.5f, -0.5f).color(1.0, 1.0, 1.0).tex(0.0f, 0.0f).normal(0.0f,  0.0f, -1.0f).next()

                    .position(-0.5f, -0.5f,  0.5f).color(1.0, 1.0, 1.0).tex(0.0f, 0.0f).normal(0.0f,  0.0f, 1.0f).next()
                    .position(0.5f, -0.5f,  0.5f).color(1.0, 1.0, 1.0).tex(1.0f, 0.0f).normal(0.0f,  0.0f, 1.0f).next()
                    .position(0.5f,  0.5f,  0.5f).color(1.0, 1.0, 1.0).tex(1.0f, 1.0f).normal(0.0f,  0.0f, 1.0f).next()
                    .position(0.5f,  0.5f,  0.5f).color(1.0, 1.0, 1.0).tex(1.0f, 1.0f).normal(0.0f,  0.0f, 1.0f).next()
                    .position(-0.5f,  0.5f,  0.5f).color(1.0, 1.0, 1.0).tex(0.0f, 1.0f).normal(0.0f,  0.0f, 1.0f).next()
                    .position(-0.5f, -0.5f,  0.5f).color(1.0, 1.0, 1.0).tex(0.0f, 0.0f).normal(0.0f,  0.0f, 1.0f).next()

                    .position(-0.5f,  0.5f,  0.5f).color(1.0, 1.0, 1.0).tex(1.0f, 0.0f).normal(-1.0f,  0.0f,  0.0f).next()
                    .position(-0.5f,  0.5f, -0.5f).color(1.0, 1.0, 1.0).tex(1.0f, 1.0f).normal(-1.0f,  0.0f,  0.0f).next()
                    .position(-0.5f, -0.5f, -0.5f).color(1.0, 1.0, 1.0).tex(0.0f, 1.0f).normal(-1.0f,  0.0f,  0.0f).next()
                    .position(-0.5f, -0.5f, -0.5f).color(1.0, 1.0, 1.0).tex(0.0f, 1.0f).normal(-1.0f,  0.0f,  0.0f).next()
                    .position(-0.5f, -0.5f,  0.5f).color(1.0, 1.0, 1.0).tex(0.0f, 0.0f).normal(-1.0f,  0.0f,  0.0f).next()
                    .position(-0.5f,  0.5f,  0.5f).color(1.0, 1.0, 1.0).tex(1.0f, 0.0f).normal(-1.0f,  0.0f,  0.0f).next()

                    .position(0.5f,  0.5f,  0.5f).color(1.0, 1.0, 1.0).tex(1.0f, 0.0f).normal(1.0f,  0.0f,  0.0f).next()
                    .position(0.5f,  0.5f, -0.5f).color(1.0, 1.0, 1.0).tex(1.0f, 1.0f).normal(1.0f,  0.0f,  0.0f).next()
                    .position(0.5f, -0.5f, -0.5f).color(1.0, 1.0, 1.0).tex(0.0f, 1.0f).normal(1.0f,  0.0f,  0.0f).next()
                    .position(0.5f, -0.5f, -0.5f).color(1.0, 1.0, 1.0).tex(0.0f, 1.0f).normal(1.0f,  0.0f,  0.0f).next()
                    .position(0.5f, -0.5f,  0.5f).color(1.0, 1.0, 1.0).tex(0.0f, 0.0f).normal(1.0f,  0.0f,  0.0f).next()
                    .position(0.5f,  0.5f,  0.5f).color(1.0, 1.0, 1.0).tex(1.0f, 0.0f).normal(1.0f,  0.0f,  0.0f).next()

                    .position(-0.5f, -0.5f, -0.5f).color(1.0, 1.0, 1.0).tex(0.0f, 1.0f).normal(0.0f, -1.0f,  0.0f).next()
                    .position(0.5f, -0.5f, -0.5f).color(1.0, 1.0, 1.0).tex(1.0f, 1.0f).normal(0.0f, -1.0f,  0.0f).next()
                    .position(0.5f, -0.5f,  0.5f).color(1.0, 1.0, 1.0).tex(1.0f, 0.0f).normal(0.0f, -1.0f,  0.0f).next()
                    .position(0.5f, -0.5f,  0.5f).color(1.0, 1.0, 1.0).tex(1.0f, 0.0f).normal(0.0f, -1.0f,  0.0f).next()
                    .position(-0.5f, -0.5f,  0.5f).color(1.0, 1.0, 1.0).tex(0.0f, 0.0f).normal(0.0f, -1.0f,  0.0f).next()
                    .position(-0.5f, -0.5f, -0.5f).color(1.0, 1.0, 1.0).tex(0.0f, 1.0f).normal(0.0f, -1.0f,  0.0f).next()

                    .position(-0.5f,  0.5f, -0.5f).color(1.0, 1.0, 1.0).tex(0.0f, 1.0f).normal(0.0f,  1.0f,  0.0f).next()
                    .position(0.5f,  0.5f, -0.5f).color(1.0, 1.0, 1.0).tex(1.0f, 1.0f).normal(0.0f,  1.0f,  0.0f).next()
                    .position(0.5f,  0.5f,  0.5f).color(1.0, 1.0, 1.0).tex(1.0f, 0.0f).normal(0.0f,  1.0f,  0.0f).next()
                    .position(0.5f,  0.5f,  0.5f).color(1.0, 1.0, 1.0).tex(1.0f, 0.0f).normal(0.0f,  1.0f,  0.0f).next()
                    .position(-0.5f,  0.5f,  0.5f).color(1.0, 1.0, 1.0).tex(0.0f, 0.0f).normal(0.0f,  1.0f,  0.0f).next()
                    .position(-0.5f,  0.5f, -0.5f).color(1.0, 1.0, 1.0).tex(0.0f, 1.0f).normal(0.0f,  1.0f,  0.0f).next(),
                (0 until 36).toList().toIntArray(),
                ShaderManager.getOrCreateShaderProgram("deferred").createMaterial {
                    texture("DiffuseTex", TextureManager.getOrCreateTexture("/will.png"))
                    texture("SpecularTex", TextureManager.getOrCreateTexture("/will.png"))
                }
            )
        ).also { it.setup() }
    )

    Hyperdrive.addRenderable("deferred",
        WorldObject(
            RocketTransform("floor"),
            TexturedModel(
                Vertices(Vertices.Attribute.Position, Vertices.Attribute.Color, Vertices.Attribute.Texture, Vertices.Attribute.Normal)
                    .position(0.5, 0.5, 0.0).color(1.0, 0.0, 0.0).tex(1.0, 0.0).normal(0.0f, 1.0f, 0.0f).next()
                    .position(0.5, -0.5, 0.0).color(0.0, 1.0, 0.0).tex(1.0, 1.0).normal(0.0f, 1.0f, 0.0f).next()
                    .position(-0.5, -0.5, 0.0).color(0.0, 0.0, 1.0).tex(0.0, 1.0).normal(0.0f, 1.0f, 0.0f).next()
                    .position(-0.5, 0.5, 0.0).color(1.0, 1.0, 0.0).tex(0.0, 0.0).normal(0.0f, 1.0f, 0.0f).next(),
                intArrayOf(
                    0, 1, 3,
                    1, 2, 3
                ),
                ShaderManager.getOrCreateShaderProgram("deferred").createMaterial {
                    texture("DiffuseTex", TextureManager.getOrCreateTexture("/bottom-text.jpg"))
                    texture("SpecularTex", TextureManager.getOrCreateTexture("/bottom-text.jpg"))
                }
            )
        ).also { it.setup() }
    )

    Hyperdrive.addRenderable("forward",
        WorldObject(
            StaticTransform(Vec3(3.0, 1.0, 0.0), Quaternion(), Vec3(0.25, 0.25, 0.25)),
            TexturedModel(
                Vertices(Vertices.Attribute.Position, Vertices.Attribute.Color, Vertices.Attribute.Texture)
                    .position(-0.5f, -0.5f, -0.5f).color(0.0, 0.5, 1.0).tex(0.0f, 0.0f).next()
                    .position(0.5f, -0.5f, -0.5f).color(0.0, 0.5, 1.0).tex(1.0f, 0.0f).next()
                    .position(0.5f,  0.5f, -0.5f).color(0.0, 0.5, 1.0).tex(1.0f, 1.0f).next()
                    .position(0.5f,  0.5f, -0.5f).color(0.0, 0.5, 1.0).tex(1.0f, 1.0f).next()
                    .position(-0.5f,  0.5f, -0.5f).color(0.0, 0.5, 1.0).tex(0.0f, 1.0f).next()
                    .position(-0.5f, -0.5f, -0.5f).color(0.0, 0.5, 1.0).tex(0.0f, 0.0f).next()

                    .position(-0.5f, -0.5f,  0.5f).color(0.0, 0.5, 1.0).tex(0.0f, 0.0f).next()
                    .position(0.5f, -0.5f,  0.5f).color(0.0, 0.5, 1.0).tex(1.0f, 0.0f).next()
                    .position(0.5f,  0.5f,  0.5f).color(0.0, 0.5, 1.0).tex(1.0f, 1.0f).next()
                    .position(0.5f,  0.5f,  0.5f).color(0.0, 0.5, 1.0).tex(1.0f, 1.0f).next()
                    .position(-0.5f,  0.5f,  0.5f).color(0.0, 0.5, 1.0).tex(0.0f, 1.0f).next()
                    .position(-0.5f, -0.5f,  0.5f).color(0.0, 0.5, 1.0).tex(0.0f, 0.0f).next()

                    .position(-0.5f,  0.5f,  0.5f).color(0.0, 0.5, 1.0).tex(1.0f, 0.0f).next()
                    .position(-0.5f,  0.5f, -0.5f).color(0.0, 0.5, 1.0).tex(1.0f, 1.0f).next()
                    .position(-0.5f, -0.5f, -0.5f).color(0.0, 0.5, 1.0).tex(0.0f, 1.0f).next()
                    .position(-0.5f, -0.5f, -0.5f).color(0.0, 0.5, 1.0).tex(0.0f, 1.0f).next()
                    .position(-0.5f, -0.5f,  0.5f).color(0.0, 0.5, 1.0).tex(0.0f, 0.0f).next()
                    .position(-0.5f,  0.5f,  0.5f).color(0.0, 0.5, 1.0).tex(1.0f, 0.0f).next()

                    .position(0.5f,  0.5f,  0.5f).color(0.0, 0.5, 1.0).tex(1.0f, 0.0f).next()
                    .position(0.5f,  0.5f, -0.5f).color(0.0, 0.5, 1.0).tex(1.0f, 1.0f).next()
                    .position(0.5f, -0.5f, -0.5f).color(0.0, 0.5, 1.0).tex(0.0f, 1.0f).next()
                    .position(0.5f, -0.5f, -0.5f).color(0.0, 0.5, 1.0).tex(0.0f, 1.0f).next()
                    .position(0.5f, -0.5f,  0.5f).color(0.0, 0.5, 1.0).tex(0.0f, 0.0f).next()
                    .position(0.5f,  0.5f,  0.5f).color(0.0, 0.5, 1.0).tex(1.0f, 0.0f).next()

                    .position(-0.5f, -0.5f, -0.5f).color(0.0, 0.5, 1.0).tex(0.0f, 1.0f).next()
                    .position(0.5f, -0.5f, -0.5f).color(0.0, 0.5, 1.0).tex(1.0f, 1.0f).next()
                    .position(0.5f, -0.5f,  0.5f).color(0.0, 0.5, 1.0).tex(1.0f, 0.0f).next()
                    .position(0.5f, -0.5f,  0.5f).color(0.0, 0.5, 1.0).tex(1.0f, 0.0f).next()
                    .position(-0.5f, -0.5f,  0.5f).color(0.0, 0.5, 1.0).tex(0.0f, 0.0f).next()
                    .position(-0.5f, -0.5f, -0.5f).color(0.0, 0.5, 1.0).tex(0.0f, 1.0f).next()

                    .position(-0.5f,  0.5f, -0.5f).color(0.0, 0.5, 1.0).tex(0.0f, 1.0f).next()
                    .position(0.5f,  0.5f, -0.5f).color(0.0, 0.5, 1.0).tex(1.0f, 1.0f).next()
                    .position(0.5f,  0.5f,  0.5f).color(0.0, 0.5, 1.0).tex(1.0f, 0.0f).next()
                    .position(0.5f,  0.5f,  0.5f).color(0.0, 0.5, 1.0).tex(1.0f, 0.0f).next()
                    .position(-0.5f,  0.5f,  0.5f).color(0.0, 0.5, 1.0).tex(0.0f, 0.0f).next()
                    .position(-0.5f,  0.5f, -0.5f).color(0.0, 0.5, 1.0).tex(0.0f, 1.0f).next(),
                (0 until 36).toList().toIntArray(),
                ShaderManager.getOrCreateShaderProgram("flatTextured").createMaterial {
                    texture("DiffuseTex", TextureManager.getOrCreateTexture("/dither.png"))
                }
            )
        ).also { it.setup() }
    )

    Hyperdrive.addRenderable("forward",
        WorldObject(
            StaticTransform(Vec3(-3.0, 1.0, 0.0), Quaternion(), Vec3(0.25, 0.25, 0.25)),
            TexturedModel(
                Vertices(Vertices.Attribute.Position, Vertices.Attribute.Color, Vertices.Attribute.Texture)
                    .position(-0.5f, -0.5f, -0.5f).color(1.0, 0.5, 0.0).tex(0.0f, 0.0f).next()
                    .position(0.5f, -0.5f, -0.5f).color(1.0, 0.5, 0.0).tex(1.0f, 0.0f).next()
                    .position(0.5f,  0.5f, -0.5f).color(1.0, 0.5, 0.0).tex(1.0f, 1.0f).next()
                    .position(0.5f,  0.5f, -0.5f).color(1.0, 0.5, 0.0).tex(1.0f, 1.0f).next()
                    .position(-0.5f,  0.5f, -0.5f).color(1.0, 0.5, 0.0).tex(0.0f, 1.0f).next()
                    .position(-0.5f, -0.5f, -0.5f).color(1.0, 0.5, 0.0).tex(0.0f, 0.0f).next()

                    .position(-0.5f, -0.5f,  0.5f).color(1.0, 0.5, 0.0).tex(0.0f, 0.0f).next()
                    .position(0.5f, -0.5f,  0.5f).color(1.0, 0.5, 0.0).tex(1.0f, 0.0f).next()
                    .position(0.5f,  0.5f,  0.5f).color(1.0, 0.5, 0.0).tex(1.0f, 1.0f).next()
                    .position(0.5f,  0.5f,  0.5f).color(1.0, 0.5, 0.0).tex(1.0f, 1.0f).next()
                    .position(-0.5f,  0.5f,  0.5f).color(1.0, 0.5, 0.0).tex(0.0f, 1.0f).next()
                    .position(-0.5f, -0.5f,  0.5f).color(1.0, 0.5, 0.0).tex(0.0f, 0.0f).next()

                    .position(-0.5f,  0.5f,  0.5f).color(1.0, 0.5, 0.0).tex(1.0f, 0.0f).next()
                    .position(-0.5f,  0.5f, -0.5f).color(1.0, 0.5, 0.0).tex(1.0f, 1.0f).next()
                    .position(-0.5f, -0.5f, -0.5f).color(1.0, 0.5, 0.0).tex(0.0f, 1.0f).next()
                    .position(-0.5f, -0.5f, -0.5f).color(1.0, 0.5, 0.0).tex(0.0f, 1.0f).next()
                    .position(-0.5f, -0.5f,  0.5f).color(1.0, 0.5, 0.0).tex(0.0f, 0.0f).next()
                    .position(-0.5f,  0.5f,  0.5f).color(1.0, 0.5, 0.0).tex(1.0f, 0.0f).next()

                    .position(0.5f,  0.5f,  0.5f).color(1.0, 0.5, 0.0).tex(1.0f, 0.0f).next()
                    .position(0.5f,  0.5f, -0.5f).color(1.0, 0.5, 0.0).tex(1.0f, 1.0f).next()
                    .position(0.5f, -0.5f, -0.5f).color(1.0, 0.5, 0.0).tex(0.0f, 1.0f).next()
                    .position(0.5f, -0.5f, -0.5f).color(1.0, 0.5, 0.0).tex(0.0f, 1.0f).next()
                    .position(0.5f, -0.5f,  0.5f).color(1.0, 0.5, 0.0).tex(0.0f, 0.0f).next()
                    .position(0.5f,  0.5f,  0.5f).color(1.0, 0.5, 0.0).tex(1.0f, 0.0f).next()

                    .position(-0.5f, -0.5f, -0.5f).color(1.0, 0.5, 0.0).tex(0.0f, 1.0f).next()
                    .position(0.5f, -0.5f, -0.5f).color(1.0, 0.5, 0.0).tex(1.0f, 1.0f).next()
                    .position(0.5f, -0.5f,  0.5f).color(1.0, 0.5, 0.0).tex(1.0f, 0.0f).next()
                    .position(0.5f, -0.5f,  0.5f).color(1.0, 0.5, 0.0).tex(1.0f, 0.0f).next()
                    .position(-0.5f, -0.5f,  0.5f).color(1.0, 0.5, 0.0).tex(0.0f, 0.0f).next()
                    .position(-0.5f, -0.5f, -0.5f).color(1.0, 0.5, 0.0).tex(0.0f, 1.0f).next()

                    .position(-0.5f,  0.5f, -0.5f).color(1.0, 0.5, 0.0).tex(0.0f, 1.0f).next()
                    .position(0.5f,  0.5f, -0.5f).color(1.0, 0.5, 0.0).tex(1.0f, 1.0f).next()
                    .position(0.5f,  0.5f,  0.5f).color(1.0, 0.5, 0.0).tex(1.0f, 0.0f).next()
                    .position(0.5f,  0.5f,  0.5f).color(1.0, 0.5, 0.0).tex(1.0f, 0.0f).next()
                    .position(-0.5f,  0.5f,  0.5f).color(1.0, 0.5, 0.0).tex(0.0f, 0.0f).next()
                    .position(-0.5f,  0.5f, -0.5f).color(1.0, 0.5, 0.0).tex(0.0f, 1.0f).next(),
                (0 until 36).toList().toIntArray(),
                ShaderManager.getOrCreateShaderProgram("flatTextured").createMaterial {
                    texture("DiffuseTex", TextureManager.getOrCreateTexture("/dither.png"))
                }
            )
        ).also { it.setup() }
    )

    Hyperdrive.addRenderable("deferred", loadModel(Path("/home/william/dev/hyperdrive_demo/teapot.dae"), ShaderManager.getOrCreateShaderProgram("liquid_container")).also { it.setup(); it.transform = RocketTransform("teapot") })
}