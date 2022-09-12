#version 450

out vec4 FragColor;

in vec3 vColor;
in vec2 vTexCoord;

layout (binding=0) uniform sampler2D GPositionTex;
layout (binding=1) uniform sampler2D GNormalTex;
layout (binding=2) uniform sampler2D GAlbedoTex;

struct Light {
    vec3 Position;
    vec3 Color;
};

const int NR_LIGHTS = 32;
uniform Light Lights[NR_LIGHTS];
uniform vec3 CameraPos;

void main() {
    // retrieve data from G-buffer
    vec3 FragPos = texture(GPositionTex, vTexCoord).rgb;
    vec3 Normal = texture(GNormalTex, vTexCoord).rgb;
    vec3 Albedo = texture(GAlbedoTex, vTexCoord).rgb;
    float Specular = texture(GAlbedoTex, vTexCoord).a;

    // then calculate lighting as usual
    vec3 lighting = Albedo * 0.1; // hard-coded ambient component
    vec3 viewDir = normalize(CameraPos - FragPos);
    for(int i = 0; i < NR_LIGHTS; ++i)
    {
        // diffuse
        vec3 lightDir = normalize(Lights[i].Position - FragPos);
        vec3 diffuse = max(dot(Normal, lightDir), 0.0) * Albedo * Lights[i].Color;
        lighting += diffuse;
    }

    FragColor = vec4(lighting * vColor, 1.0);
}