# Asset Prompt — Meadow Bee Texture

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

- Warm yellow and cream base coat — slightly richer than vanilla bee yellow.
- Soft gold or amber stripes — slightly deeper than vanilla, giving a sun-baked meadow feel.
- Light cream/off-white belly area.
- Dark brown or near-black outlines — no pure black (#000000), prefer dark brown (#1a0a00 range).
- Optional: very subtle pollen-yellow dust on legs or fuzz to hint at flower association.
- Avoid pure white; avoid cool blues or greys.

## UV / Model Reference

Matches the vanilla Minecraft bee entity model UV layout:
- Body, head, wing, and leg regions follow vanilla Bee texture layout (`minecraft:textures/entity/bee/bee.png`).
- Use the vanilla bee texture as UV template only — do not copy the pixel art directly.
- Transparent background (PNG with alpha).

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

## Acceptance Criteria

- [ ] File exists at target path.
- [ ] Dimensions are exactly 64x32 pixels.
- [ ] Transparent PNG (alpha channel on non-body areas).
- [ ] UV regions align with vanilla bee model layout.
- [ ] Visually distinct from Forest, Arid, Cultivated, and Hardy bee textures.
- [ ] No copied third-party assets.
- [ ] Dev placeholder removed when this asset is committed.

## Status

- [x] Prompt created
- [ ] Asset generated/provided
- [ ] Asset integrated and validated
