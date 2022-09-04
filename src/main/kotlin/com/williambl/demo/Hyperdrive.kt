package com.williambl.demo

import com.williambl.demo.animation.AnimatedTransform
import com.williambl.demo.model.TexturedModel
import com.williambl.demo.shader.ShaderManager
import com.williambl.demo.texture.TextureManager
import com.williambl.demo.util.Mat4x4
import com.williambl.demo.util.MatrixStack
import com.williambl.demo.util.Rotation
import com.williambl.demo.util.Vec3
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL
import kotlin.math.PI

object Hyperdrive {

    var window: Long = 0
    var windowHeight: Int = 480
    var windowWidth: Int = 640
    var windowTitle: String = "Hyperdrive"

    val renderables = mutableListOf<Renderable>()


    @JvmStatic
    fun main(args: Array<String>) {
        this.initGlfw()
        this.initGl()

        this.renderables.add(
            WorldObject(
                AnimatedTransform().also {
                    it[0.0] = {
                        //position = Vec3(0.0, 0.0, 0.0)
                        rotation = Rotation(Vec3(0.0, 1.0, 0.0), 0.0)
                    }

                    it[5.0] = {
                        //position = Vec3(0.0, 2.0, 0.0)
                        rotation = Rotation(Vec3(0.0, 1.0, 0.0), PI * 4)
                        scale = Vec3(1.0, 1.0, 1.0)
                    }

                    it[10.0] = {
                        scale = Vec3(5.0, 1.0, 1.0)
                    }
                },
                TexturedModel(
                    floatArrayOf(
                        0.5f,  0.5f, 0.0f,    1.0f, 0.0f, 0.0f,   1.0f, 0.0f,   // top right
                        0.5f, -0.5f, 0.0f,    0.0f, 1.0f, 0.0f,   1.0f, 1.0f,   // bottom right
                        -0.5f, -0.5f, 0.0f,   0.0f, 0.0f, 1.0f,   0.0f, 1.0f,   // bottom left
                        -0.5f,  0.5f, 0.0f,   1.0f, 1.0f, 0.0f,   0.0f, 0.0f    // top left
                    ),
                    intArrayOf(
                        0, 1, 3,
                        1, 2, 3
                    ),
                    ShaderManager.getOrCreateShaderProgram("flatTextured"),
                    TextureManager.getOrCreateTexture("/will.png")
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
        }

        // Make the window visible
        glfwShowWindow(this.window)
    }

    private fun initGl() {
        GL.createCapabilities()
        glViewport(0, 0, this.windowWidth, this.windowHeight)
        // Set the clear color
        glClearColor(0.2f, 0.5f, 0.8f, 0.0f)
    }

    private fun renderLoop() {
        // Clear the framebuffer
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

        // Poll for window events. The key callback above will only be
        // invoked during this call.
        glfwPollEvents()

        this.renderables.forEach { it.render(RenderingContext(MatrixStack(), glfwGetTime())) }

        // Swap the color buffers
        glfwSwapBuffers(this.window)
    }
}