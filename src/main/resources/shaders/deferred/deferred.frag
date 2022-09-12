#version 450

layout (location = 0) out vec3 gPosition;
layout (location = 1) out vec3 gNormal;
layout (location = 2) out vec4 gAlbedoSpec;

in vec3 vColor;
in vec2 vTexCoord;
in vec3 vNormal;
in vec3 vPos;

uniform sampler2D DiffuseTex;
uniform sampler2D SpecularTex;

void main()
{
    gPosition = vPos;
    gNormal = normalize(vNormal);
    gAlbedoSpec.rgb = texture(DiffuseTex, vTexCoord).rgb;
    gAlbedoSpec.a = texture(SpecularTex, vTexCoord).r;
}