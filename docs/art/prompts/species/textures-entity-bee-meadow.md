# Asset Prompt — Meadow Bee Texture

> **This is not a request for a sprite, icon, or character portrait.**
> This prompt is for a UV-mapped entity texture that fills specific regions of the default bee model.
> The UV template at `docs/art/templates/bee/default_bee_uv_template.png` must exist and be
> attached or referenced before generation begins.

## Target Path

```text
neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/meadow.png
```

## Asset Type

Entity texture — bee body skin.

## Size

```text
64x32 pixels — matches the standard vanilla Minecraft bee texture UV layout.
```

## Style

Minecraft Java Edition pixel art style. Clean, readable at small size. No anti-aliasing. Use limited, intentional palette.

The Meadow Bee is the starter species — the first bee most players encounter. It should feel warm, friendly, and natural. Visually close to a vanilla bee but with a distinct identity: richer yellows, flower-adjacent color hints.

## Palette Notes

Core colors with approximate hex anchors:

- Base coat: warm yellow / cream — approx. `#F0C840`
- Stripes: soft gold / amber — approx. `#C88820`
- Outlines: dark brown — approx. `#1A0A00`
- Belly/accent: light cream / off-white — approx. `#F5E8B0`

Additional rules:
- Avoid pure white; avoid cool blues or greys.
- No pure black (`#000000`) in outlines.

## UV Template Reference

```text
UV template path: docs/art/templates/bee/default_bee_uv_template.png
```

The UV template must be attached to the generation tool or referenced explicitly.
The template defines the exact 64×32 canvas with labeled regions for body, head, wings, legs,
stinger, and antennae. All transparent areas outside these regions must remain transparent.

## Usage

Rendered on vanilla `Bee` entities when the active species allele is `curious_bees:species/meadow`. Resolved by `SpeciesTextureResolver` and applied via `CuriousBeeBeeRenderer`.

## Species Identity

```text
Name: Meadow Bee
Role: Starter / foundation species
Biomes: plains, flower_forest, meadow
Feel: warm, approachable, golden-meadow identity
```

## Do

- Keep it recognizably bee-shaped.
- Make it visually distinct from Forest Bee (no green tones) and Arid Bee (no sandy/orange tones).
- Use warm yellows and ambers as the dominant tone.

## Don't

- Don't copy the vanilla bee texture pixel-for-pixel.
- Don't copy textures from Forestry, Productive Bees, Complicated Bees, or any other mod.
- Don't use neon, saturated, or unrealistic colors.
- Don't use partial transparency on body areas (wings may have transparency).

## Image Generation Prompt

Attach `docs/art/templates/bee/default_bee_uv_template.png` to the tool, then paste:

```
This is not a request for a sprite, icon, or character portrait.

Use the attached UV template as the exact fixed canvas.
Fill the UV regions with the Meadow Bee color scheme described below.
Do not move, resize, rearrange, crop, rotate, or reinterpret any UV island.
Keep all transparent areas transparent.
Output must be exactly 64x32 pixels.

Color scheme:
- Body region: warm yellow / cream, approximately #F0C840.
- Stripe region: soft gold / amber, approximately #C88820.
- Outline region: dark brown, approximately #1A0A00.
- Belly region: light cream / off-white, approximately #F5E8B0.
- Wing region: pale semi-transparent cream.

Feel: warm, friendly, and golden — the starter bee that feels approachable and natural.
Do not use: greens, sandy/orange tones, pure black, pure white, cool blues or greys, anti-aliasing, gradients.
Style: flat pixel art, no anti-aliasing, no gradients, hard edges, limited palette of 6 to 8 colors.
```

> If the tool cannot accept an image attachment, add: `The UV layout is: body center-left,
> head faces top-left, wings top-right, legs/stinger lower edges, transparent background
> everywhere outside UV regions.`

## Acceptance Criteria

- [ ] UV template exists at `docs/art/templates/bee/default_bee_uv_template.png`.
- [ ] File exists at target path.
- [ ] Dimensions are exactly 64x32 pixels.
- [ ] Transparent PNG (alpha channel on non-body areas).
- [ ] UV regions visually align with the UV template.
- [ ] Visually distinct from Forest, Arid, Cultivated, and Hardy bee textures.
- [ ] No copied third-party assets.
- [ ] Dev placeholder removed when this asset is committed.

## Rejection Criteria

Reject the generated result if:

- [ ] It looks like a free-form bee sprite or character portrait.
- [ ] It rearranges, resizes, or reinterprets UV islands.
- [ ] It creates detached pixels outside expected UV regions.
- [ ] It does not visually align with the UV template layout.
- [ ] It fills transparent background areas with color.
- [ ] It uses anti-aliasing, gradients, or more than ~8 colors.
- [ ] It cannot be applied to the default bee model without manual rework.

## Status

- [x] Prompt created
- [ ] UV template available at `docs/art/templates/bee/default_bee_uv_template.png`
- [ ] Asset generated/provided
- [ ] Asset validated against UV template
- [ ] Asset integrated
