# Asset Prompt — Cultivated Bee Texture

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

- Brighter, cleaner yellow than Meadow Bee — more vivid, less sun-baked.
- Softer gold or pale amber stripes — more polished than wild bees.
- Near-white or pale cream belly — cleaner than the starter bees.
- Dark warm brown outlines — avoid pure black.
- Optional: a very subtle greenish or golden sheen in the fuzz to hint at its Forest Bee heritage.
- Avoid: dark, muted, or earthy tones — it should feel cultivated/managed, not wild.

## UV / Model Reference

Matches the vanilla Minecraft bee entity model UV layout:
- Body, head, wing, and leg regions follow vanilla Bee texture layout (`minecraft:textures/entity/bee/bee.png`).
- Use the vanilla bee texture as UV template only — do not copy the pixel art directly.
- Transparent background (PNG with alpha).

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

## Acceptance Criteria

- [ ] File exists at target path.
- [ ] Dimensions are exactly 64x32 pixels.
- [ ] Transparent PNG (alpha channel on non-body areas).
- [ ] UV regions align with vanilla bee model layout.
- [ ] Visually distinct from Meadow, Forest, Arid, and Hardy bee textures.
- [ ] Feels like a rewarding upgrade from starter species.
- [ ] No copied third-party assets.
- [ ] Dev placeholder removed when this asset is committed.

## Status

- [x] Prompt created
- [ ] Asset generated/provided
- [ ] Asset integrated and validated
