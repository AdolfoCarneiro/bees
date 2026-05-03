# Asset Prompt — Forest Bee Texture

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

- Muted olive or earthy yellow base coat — less warm than Meadow, shifted toward yellow-green.
- Brown and deep forest-green accents on stripes or limbs.
- Dark bark-brown or forest shadow tones for outlines — avoid pure black.
- Optional: subtle moss-green or leaf-green fuzz hints on the back or head.
- Avoid bright saturated greens. Keep it earthy, not tropical.
- Avoid greys or blues.

## UV / Model Reference

Matches the vanilla Minecraft bee entity model UV layout:
- Body, head, wing, and leg regions follow vanilla Bee texture layout (`minecraft:textures/entity/bee/bee.png`).
- Use the vanilla bee texture as UV template only — do not copy the pixel art directly.
- Transparent background (PNG with alpha).

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

## Acceptance Criteria

- [ ] File exists at target path.
- [ ] Dimensions are exactly 64x32 pixels.
- [ ] Transparent PNG (alpha channel on non-body areas).
- [ ] UV regions align with vanilla bee model layout.
- [ ] Visually distinct from Meadow, Arid, Cultivated, and Hardy bee textures.
- [ ] No copied third-party assets.
- [ ] Dev placeholder removed when this asset is committed.

## Status

- [x] Prompt created
- [ ] Asset generated/provided
- [ ] Asset integrated and validated
