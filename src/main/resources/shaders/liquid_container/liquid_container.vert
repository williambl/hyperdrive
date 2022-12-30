#version 450

layout (location=0) in vec3 inPos;
layout (location=1) in vec3 inColor;
layout (location=2) in vec2 inTexCoord;
layout (location=3) in vec3 inNormal;

out vec3 vColor;
out vec2 vTexCoord;
out vec3 vNormal;
out vec3 vPos;

uniform mat4 Model;
uniform mat4 View;
uniform mat4 Projection;

uniform vec3 CameraPos;

void main()
{
    gl_Position = Projection * View * Model * vec4(inPos, 1.0);
    vPos = vec3(Model * vec4(inPos, 1.0));
    vColor = inColor;
    vTexCoord = inTexCoord;
    vNormal = inNormal;
}