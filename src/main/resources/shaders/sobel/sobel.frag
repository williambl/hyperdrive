#version 450

out vec4 fragColor;
in vec3 vColor;
in vec2 vTexCoord;
in vec2 vTexelSize;

uniform sampler2D DiffuseTex;

void main()
{
    vec4 center = texture(DiffuseTex, vTexCoord);
    vec4 left   = texture(DiffuseTex, vTexCoord - vec2(vTexelSize.x, 0.0));
    vec4 right  = texture(DiffuseTex, vTexCoord + vec2(vTexelSize.x, 0.0));
    vec4 up     = texture(DiffuseTex, vTexCoord - vec2(0.0, vTexelSize.y));
    vec4 down   = texture(DiffuseTex, vTexCoord + vec2(0.0, vTexelSize.y));
    vec4 leftDiff  = center - left;
    vec4 rightDiff = center - right;
    vec4 upDiff    = center - up;
    vec4 downDiff  = center - down;
    vec4 total = clamp(leftDiff + rightDiff + upDiff + downDiff, 0.0, 1.0);
    fragColor = vec4(total.rgb, 1.0);
}