# Asset Prompt — Arid Bee Texture

> **This is not a request for a sprite, icon, or character portrait.**
> This prompt is for a UV-mapped entity texture that fills specific regions of the default bee model.
> The UV template at `docs/art/templates/bee/default_bee_uv_template.png` must exist and be
> attached or referenced before generation begins.

## Target Path

```text
neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/arid.png
```

## Asset Type

Entity texture — bee body skin.

## Size

```text
64x32 pixels — matches the standard vanilla Minecraft bee texture UV layout.
```

## Style

Minecraft Java Edition pixel art style. Clean, readable at small size. No anti-aliasing. Limited, intentional palette.

The Arid Bee lives in deserts, savannas, and badlands. It should feel dry, sun-bleached, and slightly harsh compared to the Meadow or Forest bees. Think sand, sandstone, terracotta, and sun-cracked earth.

## Palette Notes

Core colors with approximate hex anchors:

- Base coat: sandy tan / bleached warm-beige — approx. `#C8A878`
- Stripes: terracotta orange / burnt sienna — approx. `#B05B30`
- Outlines: dark amber / deep brown — approx. `#4A2800`
- Belly/accent: muted buff cream — approx. `#D4B98A`

Additional rules:
- Avoid yellows that read as Meadow Bee — shift toward orange-tan.
- Avoid greens of any shade.
- No pure black (`#000000`) in outlines.

## UV Template Reference

```text
UV template path: docs/art/templates/bee/default_bee_uv_template.png
```

The UV template must be attached to the generation tool or referenced explicitly.
The template defines the exact 64×32 canvas with labeled regions for body, head, wings, legs,
stinger, and antennae. All transparent areas outside these regions must remain transparent.

Do not use the vanilla bee texture as a pixel art source — use it only to understand UV region
boundaries if the template above is not yet available.

## Usage

Rendered on vanilla `Bee` entities when the active species allele is `curious_bees:species/arid`. Resolved by `SpeciesTextureResolver` and applied via `CuriousBeeBeeRenderer`.

## Species Identity

```text
Name: Arid Bee
Role: Desert/savanna variant, recessive species
Biomes: desert, savanna, badlands
Feel: sun-bleached, dry, tougher — slightly harsher than starter bees
```

## Do

- Make it noticeably different from Meadow (warmer-orange vs. golden-yellow) and Forest (no greens).
- Use desert color palette: sand, tan, terracotta, burnt orange.
- It may look slightly "tougher" or more weathered — small detail changes can imply this.

## Don't

- Don't make it lime-green or forest-colored.
- Don't make it look like a Meadow Bee with minor hue shift — it must be clearly distinct.
- Don't copy vanilla bee or any other mod texture.
- Don't use blues, purples, or cool tones.

## Image Generation Prompt

Attach `docs/art/templates/bee/default_bee_uv_template.png` to the tool, then paste:

```
This is not a request for a sprite, icon, or character portrait.

Use the attached UV template as the exact fixed canvas.
Fill the UV regions with the Arid Bee color scheme described below.
Do not move, resize, rearrange, crop, rotate, or reinterpret any UV island.
Keep all transparent areas transparent.
Output must be exactly 64x32 pixels.

Color scheme:
- Body region: sandy tan / bleached warm-beige, approximately #C8A878.
- Stripe region: terracotta orange / burnt sienna, approximately #B05B30.
- Outline region: dark amber / deep brown, approximately #4A2800.
- Belly region: muted buff cream, approximately #D4B98A.
- Wing region: pale semi-transparent cream.

Feel: sun-bleached, dry, and slightly harsh — a desert bee that has survived arid conditions.
Do not use: greens, blues, purples, bright yellows, pure black, anti-aliasing, gradients.
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
- [ ] Visually distinct from Meadow, Forest, Cultivated, and Hardy bee textures.
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
