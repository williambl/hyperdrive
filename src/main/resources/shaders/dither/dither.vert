#version 450

layout (location=0) in vec3 inPos;
layout (location=1) in vec3 inColor;
layout (location=2) in vec2 inTexCoord;

out vec3 vColor;
out vec2 vTexCoord;
out vec2 vTexelSize;

uniform vec2 InSize;

void main() {
    gl_Position = vec4(inPos, 1.0);
    vColor = inColor;
    vTexCoord = inTexCoord;
    vTexelSize = 1.0 / InSize;
}