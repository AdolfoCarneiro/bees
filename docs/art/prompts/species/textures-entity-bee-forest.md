# Asset Prompt — Forest Bee Texture

> **This is not a request for a sprite, icon, or character portrait.**
> This prompt is for a UV-mapped entity texture that fills specific regions of the default bee model.
> The UV template at `docs/art/templates/bee/default_bee_uv_template.png` must exist and be
> attached or referenced before generation begins.

## Target Path

```text
neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/forest.png
```

## Asset Type

Entity texture — bee body skin.

## Size

```text
64x32 pixels — matches the standard vanilla Minecraft bee texture UV layout.
```

## Style

Minecraft Java Edition pixel art style. Clean, readable at small size. No anti-aliasing. Limited, intentional palette.

The Forest Bee lives in dark forests, birch forests, and oak forests. It should feel earthy and natural — less sunny than Meadow, more grounded and woodland-adjacent.

## Palette Notes

Core colors with approximate hex anchors:

- Base coat: muted olive / earthy yellow — approx. `#A89830`
- Stripes: forest green / bark brown — approx. `#4A6830`
- Outlines: dark bark brown — approx. `#2A1800`
- Belly/accent: dull olive cream — approx. `#C0AA60`

Additional rules:
- Avoid bright saturated greens — keep earthy, not tropical.
- Avoid greys or blues.
- No pure black (`#000000`) in outlines.

## UV Template Reference

```text
UV template path: docs/art/templates/bee/default_bee_uv_template.png
```

The UV template must be attached to the generation tool or referenced explicitly.
The template defines the exact 64×32 canvas with labeled regions for body, head, wings, legs,
stinger, and antennae. All transparent areas outside these regions must remain transparent.

## Usage

Rendered on vanilla `Bee` entities when the active species allele is `curious_bees:species/forest`. Resolved by `SpeciesTextureResolver` and applied via `CuriousBeeBeeRenderer`.

## Species Identity

```text
Name: Forest Bee
Role: Starter / forest variant
Biomes: forest, birch_forest, dark_forest
Feel: earthy, woodland, natural — not tropical or mossy-bright
```

## Do

- Make it distinct from Meadow (no warm-gold dominance) and Arid (no sandy/orange tones).
- Use forest-floor browns, earthy yellows, and soft greens.
- Keep it recognizably bee-shaped.

## Don't

- Don't make it look like a leaf or tree. It is still a bee.
- Don't use bright lime-green.
- Don't copy vanilla bee or any other mod's texture.
- Don't use cool blues or purples.

## Image Generation Prompt

Attach `docs/art/templates/bee/default_bee_uv_template.png` to the tool, then paste:

```
This is not a request for a sprite, icon, or character portrait.

Use the attached UV template as the exact fixed canvas.
Fill the UV regions with the Forest Bee color scheme described below.
Do not move, resize, rearrange, crop, rotate, or reinterpret any UV island.
Keep all transparent areas transparent.
Output must be exactly 64x32 pixels.

Color scheme:
- Body region: muted olive / earthy yellow, approximately #A89830.
- Stripe region: forest green / bark brown, approximately #4A6830.
- Outline region: dark bark brown, approximately #2A1800.
- Belly region: dull olive cream, approximately #C0AA60.
- Wing region: pale semi-transparent cream.

Feel: earthy, woodland, and grounded — a bee that belongs in dark and birch forests.
Do not use: bright saturated greens, warm golden yellows, sandy/orange tones, blues, greys, anti-aliasing, gradients.
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
- [ ] Visually distinct from Meadow, Arid, Cultivated, and Hardy bee textures.
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
