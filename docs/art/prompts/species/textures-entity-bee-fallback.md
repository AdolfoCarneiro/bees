# Asset Prompt — Fallback Bee Texture

## Target Path

```
neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/fallback.png
```

## Dev Placeholder

A dev placeholder (copy of `arid.png`) currently occupies the target path.
Replace with the final asset when ready.

## Asset Type

Entity texture — bee UV layout, 64×32 pixels.
Used as the Curious Bees generic fallback when a bee has a genome but its
species-specific texture is missing or invalid.

## Size

64×32 pixels — standard Minecraft vanilla bee UV layout.

## Style

Minecraft Java Edition pixel art style. Neutral, generic bee look.
Should be clearly "a bee" without strong species identity.
Hard pixel edges, flat shading, limited palette. No anti-aliasing.

## Palette Notes

Core colors with approximate hex anchors:

- Body base: neutral amber/golden — approx. `#D4A020`
- Stripe: dark brown — approx. `#3A2000`
- Highlight: pale straw — approx. `#E8CC70`
- Wing: semi-transparent light blue-grey — approx. `#B0C8D8` with alpha
- Eye: near-black — approx. `#1A0A00`
- Outline: dark brown — approx. `#2A1000`

## UV Template Reference

```text
UV template path: docs/art/templates/bee/default_bee_uv_template.png
```

The UV template is required. Do not generate without referencing it.

## Image Generation Prompt

```
This is not a request for a sprite, icon, or character portrait.

Use the attached UV template [docs/art/templates/bee/default_bee_uv_template.png]
as the exact fixed canvas.
Fill the UV regions with a neutral, generic bee color scheme described below.
Do not move, resize, rearrange, crop, rotate, or reinterpret any UV island.
Keep all transparent areas transparent.
Output must be exactly 64x32 pixels.

Color scheme (neutral fallback bee — no strong species identity):
- Body region: neutral amber-gold, approximately #D4A020.
- Stripe region: dark brown, approximately #3A2000.
- Highlight pixels: pale straw, approximately #E8CC70.
- Wing region: semi-transparent light blue-grey, approximately #B0C8D8 with alpha ~50%.
- Eye region: near-black, approximately #1A0A00.
- Outline pixels: dark brown, approximately #2A1000.

Feel: generic, clean, readable bee silhouette. No strong biome or environmental identity.
Do not use: green, blue, purple, pure white, pure black, or any metallic colors in the body.
Style: flat pixel art, no anti-aliasing, no gradients, hard edges, limited palette of 6 to 8 colors.
```

## Usage

Shown when:
- a bee has a Curious Bees genome but its species is unknown to the current content registry;
- a species has no visual definition;
- a visual definition has an invalid or missing texture path.

Not shown for unmodded vanilla bees — those use vanilla's own texture (including angry/nectar variants).

## Acceptance Criteria

- [ ] File exists at target path.
- [ ] Texture is exactly 64×32 pixels, PNG with alpha.
- [ ] Visually generic — no strong species identity.
- [ ] Passes UV template validation.
- [ ] Dev placeholder replaced.

## Rejection Criteria

- [ ] Looks like a free-form sprite or character portrait.
- [ ] Rearranges, resizes, or reinterprets UV islands.
- [ ] Does not follow the UV template layout.
- [ ] Has strong species-specific color identity (would be confused with a named species).
- [ ] Uses anti-aliasing or gradients.

## Status

- [x] UV template available at docs/art/templates/bee/default_bee_uv_template.png
- [x] Prompt created
- [ ] Asset generated/provided
- [ ] Asset validated against UV template
- [ ] Asset integrated
