# Asset Prompt — Hardy Bee Texture

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

- Darker, more muted yellow or amber base — less vivid than Meadow or Cultivated.
- Stronger, deeper brown/black stripes — high contrast, rugged appearance.
- Denser, rougher-looking fuzz hints — can use darker amber-brown tones.
- Slightly grayish or slate-brown belly area — more weathered look.
- Dark near-black outlines — darker than other species, but avoid pure black if possible.
- Avoid greens (Forest territory) and sandy/orange tones (Arid territory).
- Avoid looking bright or vivid — it should feel stronger, not friendlier.

## UV / Model Reference

Matches the vanilla Minecraft bee entity model UV layout:
- Body, head, wing, and leg regions follow vanilla Bee texture layout (`minecraft:textures/entity/bee/bee.png`).
- Use the vanilla bee texture as UV template only — do not copy the pixel art directly.
- Transparent background (PNG with alpha).

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

## Acceptance Criteria

- [ ] File exists at target path.
- [ ] Dimensions are exactly 64x32 pixels.
- [ ] Transparent PNG (alpha channel on non-body areas).
- [ ] UV regions align with vanilla bee model layout.
- [ ] Visually distinct from Meadow, Forest, Arid, and Cultivated bee textures.
- [ ] Reads as "tougher" or "hardier" compared to starter bees.
- [ ] No copied third-party assets.
- [ ] Dev placeholder removed when this asset is committed.

## Status

- [x] Prompt created
- [ ] Asset generated/provided
- [ ] Asset integrated and validated
