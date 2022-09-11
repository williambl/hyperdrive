#version 450

layout (location=0) in vec3 position;
layout (location=1) in vec3 inColor;
layout (location=2) in vec2 inTexCoord;
layout (location=3) in vec3 normal;

out vec3 vertColor;
out vec2 texCoord;
out vec3 vertNormal;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform mat3 normalModelView;

void main()
{
    gl_Position = projection * view * model * vec4(position, 1.0);
    vertColor = inColor;
    texCoord = inTexCoord;
    vertNormal = normalModelView * normal;
}