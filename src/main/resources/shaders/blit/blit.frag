#version 450

out vec4 fragColor;
in vec3 vColor;
in vec2 vTexCoord;

uniform sampler2D DiffuseTex;

void main()
{
    fragColor = texture(DiffuseTex, vTexCoord) * vec4(vColor, 1.0);
}