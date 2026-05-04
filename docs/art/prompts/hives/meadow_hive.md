# Asset Prompt — Meadow Hive Block Texture

## Target Path

```
neoforge/src/main/resources/assets/curiousbees/textures/block/meadow_hive.png
```

## Dev Placeholder

A dev placeholder (copy of `genetic_apiary.png`) currently occupies the target path to prevent
client crashes. It must be replaced with the final asset when ready. It does not count as done.

## Asset Type

Block texture — 16×16 pixels, applied to the six faces of the Meadow Hive block via the
`cube_all` placeholder model. When a proper oriented hive model is created (front face with
hive entrance, side/top/bottom with wood grain), individual face textures will be needed.

## Size

16×16 pixels

## Style

Minecraft Java Edition block pixel art style. Warm, sun-drenched grassland feel.
Hard pixel edges, flat shading, limited palette. No anti-aliasing.

## Palette Notes

Core colors with approximate hex anchors:

- Primary wood: warm honey-yellow — approx. `#C8A84B`
- Dark grain lines: deep amber — approx. `#8C6A1A`
- Light grain highlights: pale straw — approx. `#E8D080`
- Hive entrance (dark hole): near-black — approx. `#2A1A00`
- Wax/honey drip accent: bright amber — approx. `#F5C040`
- Outline: dark brown — approx. `#4A2E00`

No greens, no blues, no pure white.

## UV / Model Reference

Current model: `minecraft:block/cube_all` — all six faces use the same 16×16 tile.

Future model (when a proper hive model is created): will need a UV template at
`docs/art/templates/hive/default_hive_uv_template.png` showing the entrance face, side panels,
top, and bottom as separate UV regions. That template must be created before final hive-model
textures are generated.

## UV Template Reference

```text
UV template path: N/A for cube_all placeholder model.
Required for future oriented hive model — see docs/art/templates/hive/default_hive_uv_template.png (to be created).
```

## Image Generation Prompt

```
16x16 pixel art block texture for a Minecraft-style bee hive. Flat, hard edges, no anti-aliasing,
no gradients, limited palette of 6 to 8 colors.

Theme: Meadow Hive — a natural wooden hive found in grassy plains and flower fields.
Feel: warm, sun-drenched, golden-yellow. Think honey-soaked driftwood or dried straw weave.

Color palette:
- Primary wood: warm honey-yellow, approx #C8A84B
- Dark grain lines: deep amber, approx #8C6A1A
- Light highlights: pale straw, approx #E8D080
- Hive entrance (small dark oval hole centered on the face): near-black, approx #2A1A00
- Wax/honey accent drips: bright amber, approx #F5C040
- Outline pixels: dark brown, approx #4A2E00

The texture should look like aged wood with horizontal grain lines, a small oval entrance hole
near the bottom-center, and subtle wax drip marks along the bottom edge.

Do not use: green, blue, purple, pure white, pure black, or any metallic colors.
Output: exactly 16x16 pixels, PNG with no alpha (fully opaque).
Style: flat pixel art, Minecraft Java Edition block texture aesthetic.
```

## Species Identity

```text
Name: Meadow Hive
Species: Meadow Bee
Biomes: plains, flower_forest, meadow
Feel: warm grassland — golden-yellow wood, honey-touched, sun-baked
```

## Do

- Use warm yellows and ambers as the dominant palette.
- Include horizontal wood grain lines for texture.
- Include a small dark oval or rectangular entrance hole near the center-bottom.
- Add subtle honey/wax drip marks at the bottom edge.

## Don't

- Don't use green, blue, or purple hues.
- Don't copy or reference Forestry, Productive Bees, or vanilla bee nest textures.
- Don't add gradients or anti-aliasing.
- Don't make the entrance hole dominate the whole face.

## Acceptance Criteria

- [ ] File exists at target path.
- [ ] Texture is exactly 16×16 pixels, PNG.
- [ ] Visually warm and grassland-themed (yellow/amber dominant).
- [ ] Clearly distinguishable from Forest Hive and Arid Hive.
- [ ] No copied third-party assets.
- [ ] Dev placeholder replaced.

## Status

- [ ] UV template available (N/A for cube_all — required for future oriented model)
- [x] Prompt created
- [ ] Asset generated/provided
- [ ] Asset validated
- [ ] Asset integrated
