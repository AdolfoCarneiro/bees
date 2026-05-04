# Asset Prompt — Forest Hive Block Texture

## Target Path

```
neoforge/src/main/resources/assets/curiousbees/textures/block/forest_hive.png
```

## Dev Placeholder

A dev placeholder (copy of `genetic_apiary.png`) currently occupies the target path to prevent
client crashes. It must be replaced with the final asset when ready. It does not count as done.

## Asset Type

Block texture — 16×16 pixels, applied to the six faces of the Forest Hive block via the
`cube_all` placeholder model. When a proper oriented hive model is created, individual face
textures will be needed.

## Size

16×16 pixels

## Style

Minecraft Java Edition block pixel art style. Deep woodland feel — dark, mossy, aged bark.
Hard pixel edges, flat shading, limited palette. No anti-aliasing.

## Palette Notes

Core colors with approximate hex anchors:

- Primary bark: dark forest brown — approx. `#5A3A1A`
- Grain highlights: medium brown — approx. `#7A5030`
- Moss accent: muted dark green — approx. `#3A5A2A`
- Hive entrance (dark hole): near-black — approx. `#1A0A00`
- Resin/sap accent: dark amber — approx. `#A06020`
- Outline: very dark brown — approx. `#2A1000`

No bright yellows, no sky blues, no pure white.

## UV / Model Reference

Current model: `minecraft:block/cube_all` — all six faces use the same 16×16 tile.

Future model: will need UV template at `docs/art/templates/hive/default_hive_uv_template.png`.

## UV Template Reference

```text
UV template path: N/A for cube_all placeholder model.
Required for future oriented hive model — see docs/art/templates/hive/default_hive_uv_template.png (to be created).
```

## Image Generation Prompt

```
16x16 pixel art block texture for a Minecraft-style bee hive. Flat, hard edges, no anti-aliasing,
no gradients, limited palette of 6 to 8 colors.

Theme: Forest Hive — a natural wooden hive carved into old bark, found in forests and dark woods.
Feel: deep woodland, aged bark, mossy, cool shadows. Think a hollow in a dark oak tree.

Color palette:
- Primary bark: dark forest brown, approx #5A3A1A
- Grain highlights: medium brown, approx #7A5030
- Moss accent pixels: muted dark green, approx #3A5A2A
- Hive entrance (small dark oval hole near center-bottom): near-black, approx #1A0A00
- Resin/sap drip marks: dark amber, approx #A06020
- Outline pixels: very dark brown, approx #2A1000

The texture should look like old dark bark with vertical wood grain, a small entrance hole,
a few moss pixels scattered in crevices, and sap/resin marks along edges.

Do not use: bright yellow, sky blue, red, pure white, or any metallic colors.
Output: exactly 16x16 pixels, PNG with no alpha (fully opaque).
Style: flat pixel art, Minecraft Java Edition block texture aesthetic.
```

## Species Identity

```text
Name: Forest Hive
Species: Forest Bee
Biomes: forest, birch_forest, dark_forest
Feel: deep woodland — dark bark, mossy, aged, shaded
```

## Do

- Use dark browns and muted greens as the dominant palette.
- Include vertical wood grain lines (contrast with Meadow's horizontal grain).
- Include a small dark oval or rectangular entrance hole near the center-bottom.
- Add subtle moss pixels in crevices and sap/resin drips.

## Don't

- Don't use bright yellows or warm ambers (that's the Meadow Hive palette).
- Don't copy or reference Forestry, Productive Bees, or vanilla bee nest textures.
- Don't add gradients or anti-aliasing.

## Acceptance Criteria

- [ ] File exists at target path.
- [ ] Texture is exactly 16×16 pixels, PNG.
- [ ] Visually dark and woodland-themed (brown/green dominant).
- [ ] Clearly distinguishable from Meadow Hive and Arid Hive.
- [ ] No copied third-party assets.
- [ ] Dev placeholder replaced.

## Status

- [ ] UV template available (N/A for cube_all — required for future oriented model)
- [x] Prompt created
- [ ] Asset generated/provided
- [ ] Asset validated
- [ ] Asset integrated
