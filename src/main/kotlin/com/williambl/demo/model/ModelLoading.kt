package com.williambl.demo.model

import com.williambl.demo.WorldObject
import com.williambl.demo.animation.StaticTransform
import com.williambl.demo.model.Vertices.Attribute.Color.color
import com.williambl.demo.model.Vertices.Attribute.Normal.normal
import com.williambl.demo.model.Vertices.Attribute.Position.position
import com.williambl.demo.model.Vertices.Attribute.Texture.tex
import com.williambl.demo.shader.ShaderProgram
import com.williambl.demo.texture.Texture
import com.williambl.demo.texture.TextureManager
import com.williambl.demo.texture.TextureSlot
import com.williambl.demo.util.Quaternion
import com.williambl.demo.util.Vec3
import org.lwjgl.PointerBuffer
import org.lwjgl.assimp.AIMaterial
import org.lwjgl.assimp.AIMesh
import org.lwjgl.assimp.AINode
import org.lwjgl.assimp.AIString
import org.lwjgl.assimp.Assimp.*
import java.io.IOException
import java.nio.IntBuffer
import java.nio.file.Path

//TODO: shader from material?
fun loadModel(path: Path, shader: ShaderProgram, flags: Int = aiProcess_GenNormals or aiProcess_OptimizeMeshes or aiProcess_Triangulate or aiProcess_FlipUVs or aiProcess_JoinIdenticalVertices): WorldObject {
    val scene = aiImportFile(path.toString(), flags) ?: throw IOException("Could not load model $path")
    val numMaterials: Int = scene.mNumMaterials()
    val aiMaterials: List<AIMaterial> = scene.mMaterials()?.map { AIMaterial.createSafe(it) }?.filterNotNull() ?: throw IllegalStateException()

    val numMeshes: Int = scene.mNumMeshes()
    val meshes: PointerBuffer = scene.mMeshes() ?: throw IllegalStateException()
    val rootNode: AINode = scene.mRootNode() ?: throw IllegalStateException()

    return processNode(rootNode, meshes, aiMaterials, shader)
}

fun processNode(node: AINode, meshPointers: PointerBuffer, materials: List<AIMaterial>, shader: ShaderProgram): WorldObject {
    val meshes: List<TexturedModel> = node.mMeshes()?.map(meshPointers::get)?.mapNotNull(AIMesh::createSafe)?.map { mesh -> toModel(mesh, materials, shader) } ?: listOf()
    val children = node.mChildren()?.map(AINode::createSafe)?.filterNotNull()?.map { processNode(it, meshPointers, materials, shader) } ?: listOf()

    //TODO: transform
    return WorldObject(StaticTransform(Vec3(0.0, 0.0, 0.0), Quaternion(), Vec3(1.0, 1.0, 1.0)), *(meshes + children).toTypedArray())
}

fun toModel(aiMesh: AIMesh, materials: List<AIMaterial>, shader: ShaderProgram): TexturedModel {
    val vertexCount = aiMesh.mNumVertices()
    val vertexPositions = aiMesh.mVertices()
    val vertexColours = aiMesh.mColors(0)
    val vertexTextureCoords = aiMesh.mTextureCoords(0)
    val vertexNormals = aiMesh.mNormals()
    val vertices = Vertices(Vertices.Attribute.Position, Vertices.Attribute.Color, Vertices.Attribute.Texture, Vertices.Attribute.Normal)
    for (i in 0 until vertexCount) {
        val pos = vertexPositions.get(i)
        val colour = vertexColours?.get(i)
        val texCoords = vertexTextureCoords?.get(i)
        val normal = vertexNormals?.get(i)
        vertices.position(pos.x(), pos.y(), pos.z())
        if (colour != null) {
            vertices.color(colour.r(), colour.g(), colour.b())
        } else {
            vertices.color(1.0, 1.0, 1.0)
        }
        if (texCoords != null) {
            vertices.tex(texCoords.x(), texCoords.y())
        } else {
            vertices.tex(0.0, 0.0)
        }
        if (normal != null) {
            vertices.normal(normal.x(), normal.y(), normal.z())
        } else {
            vertices.normal(0.0, 0.0, 0.0)
        }
        vertices.next()
    }

    val indices = aiMesh.mFaces().flatMap { it.mIndices().toList() }.toIntArray()

    val material = materials[aiMesh.mMaterialIndex()]

    return TexturedModel(vertices, indices, shader.createMaterial {
        for (i in aiTextureType_NONE..aiTextureType_UNKNOWN) {
            material.textures(i).forEachIndexed { texIndex, tex ->
                texture(
                    TextureSlot.namesForAssimpTypes[i] + if (texIndex == 0) { "" } else { "_${texIndex}"},
                    tex
                )
            }
        }
    })
}

private fun AIMaterial.textures(textureType: Int): Array<Texture> {
    return Array(aiGetMaterialTextureCount(this, textureType)) { i ->
        val path = AIString.create()
        aiGetMaterialTexture(this, textureType, i, path, null as IntArray?, null, null, null, null, null)
        return@Array TextureManager.getOrCreateTexture(Path.of(path.dataString()))
    }
}

private fun <T> PointerBuffer.map(func: (Long) -> T): List<T> {
    val results = mutableListOf<T>()
    while (this.hasRemaining()) {
        results.add(func(this.get()))
    }

    return results
}

private fun <T> IntBuffer.map(func: (Int) -> T): List<T> {
    val results = mutableListOf<T>()
    while (this.hasRemaining()) {
        results.add(func(this.get()))
    }

    return results
}

private fun IntBuffer.toList(): List<Int> {
    val results = mutableListOf<Int>()
    while (this.hasRemaining()) {
        results.add(this.get())
    }

    return results
}