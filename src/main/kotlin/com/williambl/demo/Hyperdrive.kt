package com.williambl.demo

import com.williambl.demo.animation.AnimatedDouble
import com.williambl.demo.animation.RocketTransform
import com.williambl.demo.framebuffer.FramebufferManager
import com.williambl.demo.model.TexturedModel
import com.williambl.demo.model.Vertices
import com.williambl.demo.model.Vertices.Attribute.Color.color
import com.williambl.demo.model.Vertices.Attribute.Normal.normal
import com.williambl.demo.model.Vertices.Attribute.Position.position
import com.williambl.demo.model.Vertices.Attribute.Texture.tex
import com.williambl.demo.rocket4j.Rocket4J
import com.williambl.demo.rocket4j.TimeController
import com.williambl.demo.shader.ShaderManager
import com.williambl.demo.texture.TextureManager
import com.williambl.demo.util.Time
import com.williambl.demo.util.applyPostShaderEffect
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL30.*
import org.lwjgl.system.MemoryUtil.NULL

object Hyperdrive {

    var window: Long = 0
    var windowHeight: Int = 480
    var windowWidth: Int = 640
    var windowTitle: String = "Hyperdrive"

    lateinit var camera: Camera
    val renderables = mutableListOf<Renderable>()

    val rocket = Rocket4J.create(TimeController(5))

    @JvmStatic
    fun main(args: Array<String>) {
        this.initGlfw()
        this.initGl()

        this.camera = Camera(RocketTransform("camera"), AnimatedDouble.byValue(45.0), 0.1, 1000.0, FramebufferManager.getOrCreateFramebuffer("main", this.windowWidth, this.windowHeight, true, true)) {
            glClearColor(1.0f, 0.5f, 0.8f, 0.0f)
        }

        this.renderables.add(
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
                    ShaderManager.getOrCreateShaderProgram("flatTextured"),
                    TextureManager.getOrCreateTexture("/will.png")
                )
            ).also { it.setup() }
        )

        this.renderables.add(
            WorldObject(
                RocketTransform("floor"),
                TexturedModel(
                    Vertices(Vertices.Attribute.Position, Vertices.Attribute.Color, Vertices.Attribute.Texture)
                        .position(0.5, 0.5, 0.0).color(1.0, 0.0, 0.0).tex(1.0, 0.0).next()
                        .position(0.5, -0.5, 0.0).color(0.0, 1.0, 0.0).tex(1.0, 1.0).next()
                        .position(-0.5, -0.5, 0.0).color(0.0, 0.0, 1.0).tex(0.0, 1.0).next()
                        .position(-0.5, 0.5, 0.0).color(1.0, 1.0, 0.0).tex(0.0, 0.0).next(),
                    intArrayOf(
                        0, 1, 3,
                        1, 2, 3
                    ),
                    ShaderManager.getOrCreateShaderProgram("flatTextured"),
                    TextureManager.getOrCreateTexture("/bottom-text.jpg")
                )
            ).also { it.setup() }
        )

        while (!glfwWindowShouldClose(this.window)) {
            this.renderLoop()
        }
    }

    private fun initGlfw() {
        //Create an error callback to tell us if something goes wrong
        GLFWErrorCallback.createPrint(System.err).set()

        //Initialize GLFW. Most GLFW functions will not work before doing this.
        if (!glfwInit()) {
            throw IllegalStateException("Unable to initialize GLFW")
        }

        // Configure our window
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 4)
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 5)
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE)

        this.window = glfwCreateWindow(this.windowWidth, this.windowHeight, this.windowTitle, NULL, NULL)
        if (this.window == NULL) {
            throw RuntimeException("Failed to create the GLFW window")
        }

        // Make the OpenGL context current
        glfwMakeContextCurrent(this.window)
        // Enable v-sync
        glfwSwapInterval(1)

        glfwSetFramebufferSizeCallback(this.window) { _, width, height ->
            glViewport(0, 0, width, height)
            this.windowWidth = width
            this.windowHeight = height
            FramebufferManager.updateFramebufferSizes(width, height)
        }

        // Make the window visible
        glfwShowWindow(this.window)
    }

    private fun initGl() {
        GL.createCapabilities()
        glViewport(0, 0, this.windowWidth, this.windowHeight)
        glEnable(GL_DEPTH_TEST)
        // Set the clear color
        glClearColor(0.2f, 0.5f, 0.8f, 0.0f)
    }

    private fun renderLoop() {
        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents()


        this.rocket.update()
        val time = Time(this.rocket.currentTime, this.rocket.currentRow)
        this.camera.render(time)
        ShaderManager.getOrCreateShaderProgram("sobel").setUniform("InSize", this.camera.framebuffer.width.toFloat(), this.camera.framebuffer.height.toFloat())
        //applyPostShaderEffect(this.camera.framebuffer.width, this.camera.framebuffer.height, ShaderManager.getOrCreateShaderProgram("sobel"))

        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        glClearColor(1f, 1f, 1f, 1f)
        glClear(GL_COLOR_BUFFER_BIT)
        this.camera.framebuffer.renderToCurrentBuffer(this.windowWidth, this.windowHeight, ShaderManager.getOrCreateShaderProgram("blit"))

        // Swap the color buffers
        glfwSwapBuffers(this.window)
    }
}