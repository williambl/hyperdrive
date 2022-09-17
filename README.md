# Hyperdrive

lorem ipsum etc.

## Roadmap
 - HDR
 - Bloom
 - Normal Mapping
 - Proper Materials system
 - Make a demo!
 - SSAO
 - Make the code a little nicer

## Standard Shader Uniforms
 - Model
 - View
 - Projection
 - CameraPos
 - InSize

probably add:
 - Time
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