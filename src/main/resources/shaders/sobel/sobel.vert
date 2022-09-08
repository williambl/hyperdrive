#version 450

layout (location=0) in vec3 position;
layout (location=1) in vec3 inColor;
layout (location=2) in vec2 inTexCoord;

out vec3 vertColor;
out vec2 texCoord;
out vec2 oneTexel;

uniform vec2 InSize;

void main() {
    gl_Position = vec4(position, 1.0);
    vertColor = inColor;
    texCoord = inTexCoord;
    oneTexel = 1.0 / InSize;
}