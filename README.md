# Hyperdrive

lorem ipsum etc.

## Standard Shader Uniforms
 - Model
 - View
 - Projection
 - CameraPos

probably add:
 - Time
 - InSize
 - ???

## Naming Conventions

### Shaders

### General

 - Vectors should generally be prefixed with `world`/`view`/`model`/`tex`/`ndc` to indicate which coordinate space they're in.

### Uniforms

 - Begin with a capital letter.

#### Sampler Uniforms

 - End with `Tex`
 - Example names:
   - `DiffuseTex`
   - `NormalTex`
   - `SpecularTex`

### Vertex Attributes (vertex-in)

 - Begin with `in`

### Vertex Outputs (vertex-out, fragment-in)

 - Begin with `v`