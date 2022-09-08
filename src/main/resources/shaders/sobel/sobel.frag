#version 450

out vec4 fragColor;
in vec3 vertColor;
in vec2 texCoord;
in vec2 oneTexel;

uniform sampler2D theTexture;

void main()
{
    vec4 center = texture(theTexture, texCoord);
    vec4 left   = texture(theTexture, texCoord - vec2(oneTexel.x, 0.0));
    vec4 right  = texture(theTexture, texCoord + vec2(oneTexel.x, 0.0));
    vec4 up     = texture(theTexture, texCoord - vec2(0.0, oneTexel.y));
    vec4 down   = texture(theTexture, texCoord + vec2(0.0, oneTexel.y));
    vec4 leftDiff  = center - left;
    vec4 rightDiff = center - right;
    vec4 upDiff    = center - up;
    vec4 downDiff  = center - down;
    vec4 total = clamp(leftDiff + rightDiff + upDiff + downDiff, 0.0, 1.0);
    fragColor = vec4(total.rgb, 1.0);
}