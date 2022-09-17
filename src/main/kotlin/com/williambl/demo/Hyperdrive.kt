package com.williambl.demo

import com.williambl.demo.animation.AnimatedDouble
import com.williambl.demo.animation.RocketTransform
import com.williambl.demo.framebuffer.FramebufferManager
import com.williambl.demo.rocket4j.Rocket4J
import com.williambl.demo.rocket4j.TimeController
import com.williambl.demo.shader.ShaderManager
import com.williambl.demo.texture.TextureManager
import com.williambl.demo.util.Time
import com.williambl.demo.util.Vec3
import com.williambl.demo.util.applyPostShaderEffect
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL30.*
import org.lwjgl.opengl.GL45.glBlitNamedFramebuffer
import org.lwjgl.system.MemoryUtil.NULL

object Hyperdrive {

    var window: Long = 0
    var windowHeight: Int = 480
    var windowWidth: Int = 640
    var windowTitle: String = "Hyperdrive"

    lateinit var deferredCamera: Camera
    lateinit var forwardCamera: Camera
    private val renderingPasses = mutableMapOf<String, MutableList<Renderable>>()

    val rocket = Rocket4J.create(TimeController(5))

    fun getRenderablesForPass(name: String): List<Renderable> = this.renderingPasses.getOrPut(name, ::mutableListOf)
    fun addRenderable(pass: String, renderable: Renderable) = this.renderingPasses.getOrPut(pass, ::mutableListOf).add(renderable)

    @JvmStatic
    fun main(args: Array<String>) {
        this.initGlfw()
        this.initGl()

        this.deferredCamera = Camera(RocketTransform("camera"), AnimatedDouble.byValue(45.0), 0.1, 1000.0, FramebufferManager.getOrCreateGBuffer("gBuffer", this.windowWidth, this.windowHeight, true)) {
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
        }

        this.forwardCamera = Camera(RocketTransform("camera"), AnimatedDouble.byValue(45.0), 0.1, 1000.0, FramebufferManager.getOrCreateFramebuffer("forward", this.windowWidth, this.windowHeight, true, true)) {
            glClearColor(0.0f, 0.0f, 0.0f, 0.0f)
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
            glBlitNamedFramebuffer(this.deferredCamera.framebuffer.id, this.forwardCamera.framebuffer.id, 0, 0, this.windowWidth, this.windowHeight, 0, 0, this.forwardCamera.framebuffer.width, this.forwardCamera.framebuffer.height, GL_DEPTH_BUFFER_BIT, GL_NEAREST)
        }

        setupWorld()

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
        this.deferredCamera.render(time, this.getRenderablesForPass("deferred"))

        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        glClearColor(1f, 1f, 1f, 1f)
        glClear(GL_COLOR_BUFFER_BIT)
        ShaderManager.getOrCreateShaderProgram("mergeGBuffer").let { shader -> //TODO a better system for lights
            shader.setUniform("Lights[0].Position", Vec3(-3.0, 1.0, 0.0))
            shader.setUniform("Lights[0].Color", Vec3(0.7, 0.2, 0.0))
            shader.setUniform("Lights[1].Position", Vec3(3.0, 1.0, 0.0))
            shader.setUniform("Lights[1].Color", Vec3(0.0, 0.3, 0.9))
        }
        this.deferredCamera.framebuffer.renderToCurrentBuffer(this.windowWidth, this.windowHeight, ShaderManager.getOrCreateShaderProgram("mergeGBuffer"))

        this.forwardCamera.render(time, this.getRenderablesForPass("forward"))
        glBindFramebuffer(GL_FRAMEBUFFER, 0)
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)
        this.forwardCamera.framebuffer.renderToCurrentBuffer(this.windowWidth, this.windowHeight, ShaderManager.getOrCreateShaderProgram("blit"))
        glDisable(GL_BLEND)

        ShaderManager.getOrCreateShaderProgram("dither").setUniform("InSize", this.windowWidth.toFloat(), this.windowHeight.toFloat())
        glActiveTexture(GL_TEXTURE1)
        TextureManager.getOrCreateTexture("/dither.png").bind()
        glActiveTexture(GL_TEXTURE0)
        //applyPostShaderEffect(this.camera.framebuffer.width, this.camera.framebuffer.height, ShaderManager.getOrCreateShaderProgram("dither"))

        // Swap the color buffers
        glfwSwapBuffers(this.window)
    }
}