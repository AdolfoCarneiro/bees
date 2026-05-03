# Asset Prompt — Arid Bee Texture

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

- Sandy tan or buff base coat — bleached, warm-beige with a hint of orange.
- Terracotta orange or burnt sienna stripes — desert-sand feel.
- Dark amber or deep brown outlines — no pure black.
- Optional: very slight dust-orange particles on fuzz hints.
- Avoid yellows that read as Meadow Bee. Shift toward orange-tan.
- Avoid greens of any shade.

## UV / Model Reference

Matches the vanilla Minecraft bee entity model UV layout:
- Body, head, wing, and leg regions follow vanilla Bee texture layout (`minecraft:textures/entity/bee/bee.png`).
- Use the vanilla bee texture as UV template only — do not copy the pixel art directly.
- Transparent background (PNG with alpha).

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

## Acceptance Criteria

- [ ] File exists at target path.
- [ ] Dimensions are exactly 64x32 pixels.
- [ ] Transparent PNG (alpha channel on non-body areas).
- [ ] UV regions align with vanilla bee model layout.
- [ ] Visually distinct from Meadow, Forest, Cultivated, and Hardy bee textures.
- [ ] No copied third-party assets.
- [ ] Dev placeholder removed when this asset is committed.

## Status

- [x] Prompt created
- [ ] Asset generated/provided
- [ ] Asset integrated and validated
