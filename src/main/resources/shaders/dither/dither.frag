#version 450

out vec4 fragColor;
in vec3 vertColor;
in vec2 texCoord;
in vec2 oneTexel;

layout (binding = 0) uniform sampler2D textureSampler;
layout (binding = 1) uniform sampler2D ditherSampler;

uniform vec2 InSize;

void main() {
    vec2 reducedSize = InSize * (200.0 / InSize.x); // downsample (gets weird with horizontal res < 200)

    vec2 steppedCoord = vec2(
        float(int(texCoord.x*reducedSize.x)) / reducedSize.x,
        float(int(texCoord.y*reducedSize.y)) / reducedSize.y
    );

    vec4 noise = texture(ditherSampler, steppedCoord * reducedSize / 4.0); // dithering
    vec4 col = texture(textureSampler, steppedCoord) + noise * vec4(1.0/12.0, 1.0/12.0, 1.0/6.0, 1.0);
    float r = float(int(col.r*4.0))/4.0; // crush colours
    float g = float(int(col.g*4.0))/4.0;
    float b = float(int(col.b*4.0))/4.0;
    fragColor = vec4(r, g, b, 1.0);
}