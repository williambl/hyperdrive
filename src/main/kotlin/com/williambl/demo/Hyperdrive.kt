package com.williambl.demo

import org.lwjgl.glfw.GLFW
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.glfw.GLFWErrorCallback
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryUtil.NULL

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
            TexturedMesh(
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

        this.renderables.forEach(Renderable::render)

        // Swap the color buffers
        glfwSwapBuffers(this.window)
    }
}