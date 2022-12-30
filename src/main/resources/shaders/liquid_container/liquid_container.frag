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

uniform vec3 CameraPos;
uniform float LiquidLevel;
uniform vec3 LiquidColor;
uniform vec3 FoamColor;

float distanceToLiquid (vec3 p)
{
    return p.y - LiquidLevel;
}

void main()
{
    float distance = distanceToLiquid(vPos);
    if (distance > 0) {
        discard;
    }
    vec3 color = gl_FrontFacing ? LiquidColor : FoamColor;
    gPosition = vPos;
    gNormal = gl_FrontFacing ? normalize(vNormal) : vec3(0., 1., 0.);
    gAlbedoSpec.rgb = color;
    gAlbedoSpec.a = 1.;
}