# Asset Prompt — Hardy Bee Texture

> **This is not a request for a sprite, icon, or character portrait.**
> This prompt is for a UV-mapped entity texture that fills specific regions of the default bee model.
> The UV template at `docs/art/templates/bee/default_bee_uv_template.png` must exist and be
> attached or referenced before generation begins.

## Target Path

```text
neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/hardy.png
```

## Asset Type

Entity texture — bee body skin.

## Size

```text
64x32 pixels — matches the standard vanilla Minecraft bee texture UV layout.
```

## Style

Minecraft Java Edition pixel art style. Clean, readable at small size. No anti-aliasing. Limited, intentional palette.

The Hardy Bee is a recessive species — harder to breed and stabilize. It should look noticeably stronger and more resilient than the starter bees. Think darker, tougher contrasts — like a bee that has survived harsh conditions. It should feel meaningfully different from the warm starter bees.

## Palette Notes

Core colors with approximate hex anchors:

- Base coat: dark muted amber / tarnished yellow — approx. `#A87820`
- Stripes: deep brown / near-black — approx. `#301800`
- Outlines: very dark brown / near-black — approx. `#1A0800`
- Belly/accent: slate brown / weathered grey-brown — approx. `#806040`

Additional rules:
- Avoid greens (Forest territory) and sandy/orange tones (Arid territory).
- Avoid bright or vivid — it should feel stronger, not friendlier.
- Darker overall than any other species.

## UV Template Reference

```text
UV template path: docs/art/templates/bee/default_bee_uv_template.png
```

The UV template must be attached to the generation tool or referenced explicitly.
The template defines the exact 64×32 canvas with labeled regions for body, head, wings, legs,
stinger, and antennae. All transparent areas outside these regions must remain transparent.

## Usage

Rendered on vanilla `Bee` entities when the active species allele is `curious_bees:species/hardy`. Resolved by `SpeciesTextureResolver` and applied via `CuriousBeeBeeRenderer`.

## Species Identity

```text
Name: Hardy Bee
Role: Recessive species — Forest × Arid mutation result
Biomes: any (no spawn biome — obtained through breeding)
Feel: rugged, resilient, high-contrast, tougher than starters
Gameplay: harder to obtain due to recessive dominance — feels earned
```

## Do

- Use stronger contrast than other species — darker stripes, bolder markings.
- Make it feel tougher and more resilient, not prettier.
- It should be visually distinct enough that a player can immediately tell it apart from Meadow or Forest.

## Don't

- Don't make it look injured, damaged, or diseased.
- Don't use greens, sandy orange, or vivid yellow.
- Don't make it look undead, mechanical, or magical.
- Don't copy vanilla bee or any other mod texture.

## Image Generation Prompt

Attach `docs/art/templates/bee/default_bee_uv_template.png` to the tool, then paste:

```
This is not a request for a sprite, icon, or character portrait.

Use the attached UV template as the exact fixed canvas.
Fill the UV regions with the Hardy Bee color scheme described below.
Do not move, resize, rearrange, crop, rotate, or reinterpret any UV island.
Keep all transparent areas transparent.
Output must be exactly 64x32 pixels.

Color scheme:
- Body region: dark muted amber / tarnished yellow, approximately #A87820.
- Stripe region: deep brown / near-black, approximately #301800.
- Outline region: very dark brown / near-black, approximately #1A0800.
- Belly region: slate brown / weathered grey-brown, approximately #806040.
- Wing region: pale semi-transparent, slightly darker than other species.

Feel: rugged, resilient, and high-contrast — a bee that looks earned and tougher than the starters.
Do not use: greens, sandy/orange tones, bright yellows, vivid colors, anti-aliasing, gradients.
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
- [ ] Visually distinct from Meadow, Forest, Arid, and Cultivated bee textures.
- [ ] Reads as "tougher" or "hardier" compared to starter bees.
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
