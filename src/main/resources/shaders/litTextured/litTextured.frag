#version 450

out vec4 fragColor;
in vec3 vColor;
in vec2 vTexCoord;
in vec3 vNormal;

uniform sampler2D DiffuseTex;

void main()
{
    fragColor = texture(DiffuseTex, vTexCoord) * vec4(vNormal, 1.0);
    if (fragColor.a < 0.1)
        discard;
}