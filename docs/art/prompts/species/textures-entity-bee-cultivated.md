# Asset Prompt — Cultivated Bee Texture

> **This is not a request for a sprite, icon, or character portrait.**
> This prompt is for a UV-mapped entity texture that fills specific regions of the default bee model.
> The UV template at `docs/art/templates/bee/default_bee_uv_template.png` must exist and be
> attached or referenced before generation begins.

## Target Path

```text
neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/cultivated.png
```

## Asset Type

Entity texture — bee body skin.

## Size

```text
64x32 pixels — matches the standard vanilla Minecraft bee texture UV layout.
```

## Style

Minecraft Java Edition pixel art style. Clean, readable at small size. No anti-aliasing. Limited, intentional palette.

The Cultivated Bee is the first mutation result — created when a Meadow Bee and a Forest Bee breed under the right conditions. It should feel like a step up from the starter bees: cleaner, brighter, more refined. It represents successful early genetic management and should feel rewarding to obtain.

## Palette Notes

Core colors with approximate hex anchors:

- Base coat: bright clean yellow — approx. `#F8D840`
- Stripes: pale soft gold / amber — approx. `#D8A820`
- Outlines: dark warm brown — approx. `#2A1400`
- Belly/accent: near-white / pale cream — approx. `#FAF0C8`

Additional rules:
- Avoid dark, muted, or earthy tones — it should feel managed, not wild.
- No pure black (`#000000`) in outlines.

## UV Template Reference

```text
UV template path: docs/art/templates/bee/default_bee_uv_template.png
```

The UV template must be attached to the generation tool or referenced explicitly.
The template defines the exact 64×32 canvas with labeled regions for body, head, wings, legs,
stinger, and antennae. All transparent areas outside these regions must remain transparent.

## Usage

Rendered on vanilla `Bee` entities when the active species allele is `curious_bees:species/cultivated`. Resolved by `SpeciesTextureResolver` and applied via `CuriousBeeBeeRenderer`.

## Species Identity

```text
Name: Cultivated Bee
Role: First mutation result — Meadow × Forest
Biomes: any (no spawn biome — obtained through breeding)
Feel: cleaner, brighter, refined — more managed than wild species
Gameplay reward: first successful mutation the player achieves
```

## Do

- Make it visually brighter and cleaner than Meadow and Forest bees.
- It should look like it belongs in a beehive you're proud of, not just found in the world.
- Keep it distinct: more vivid yellow, cleaner coat, less earthy.

## Don't

- Don't make it look exotic or unnatural — it is still a bee.
- Don't add mechanical, glowing, or magical elements.
- Don't copy vanilla bee or any other mod texture.
- Don't use cool blues, purples, or neon colors.

## Image Generation Prompt

Attach `docs/art/templates/bee/default_bee_uv_template.png` to the tool, then paste:

```
This is not a request for a sprite, icon, or character portrait.

Use the attached UV template as the exact fixed canvas.
Fill the UV regions with the Cultivated Bee color scheme described below.
Do not move, resize, rearrange, crop, rotate, or reinterpret any UV island.
Keep all transparent areas transparent.
Output must be exactly 64x32 pixels.

Color scheme:
- Body region: bright clean yellow, approximately #F8D840.
- Stripe region: pale soft gold / amber, approximately #D8A820.
- Outline region: dark warm brown, approximately #2A1400.
- Belly region: near-white / pale cream, approximately #FAF0C8.
- Wing region: pale semi-transparent cream.

Feel: clean, bright, and polished — more refined than wild starter bees, a rewarding early mutation.
Do not use: dark earthy tones, sandy/orange hues, greens, blues, pure black, anti-aliasing, gradients.
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
- [ ] Visually distinct from Meadow, Forest, Arid, and Hardy bee textures.
- [ ] Feels like a rewarding upgrade from starter species.
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
