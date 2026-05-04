# Asset Prompt — Arid Hive Block Texture

## Target Path

```
neoforge/src/main/resources/assets/curiousbees/textures/block/arid_hive.png
```

## Dev Placeholder

A dev placeholder (copy of `genetic_apiary.png`) currently occupies the target path to prevent
client crashes. It must be replaced with the final asset when ready. It does not count as done.

## Asset Type

Block texture — 16×16 pixels, applied to the six faces of the Arid Hive block via the
`cube_all` placeholder model. When a proper oriented hive model is created, individual face
textures will be needed.

## Size

16×16 pixels

## Style

Minecraft Java Edition block pixel art style. Harsh desert feel — bleached bone, cracked clay,
sun-scorched. Hard pixel edges, flat shading, limited palette. No anti-aliasing.

## Palette Notes

Core colors with approximate hex anchors:

- Primary clay/bone: bleached tan — approx. `#C8A87A`
- Crack lines: deeper desert brown — approx. `#7A5030`
- Sun-bleached highlight: off-white/pale sand — approx. `#E8D8B0`
- Hive entrance (dark hole): very dark brown — approx. `#2A1000`
- Wax in harsh conditions: dull amber — approx. `#B07830`
- Outline: dark clay — approx. `#5A3010`

No lush greens, no sky blues, no bright yellows (use muted/bleached versions only).

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

Theme: Arid Hive — a hive carved from sun-bleached clay or bone-dry wood, found in deserts and badlands.
Feel: harsh, sun-scorched, cracked, bleached. Think dried clay pottery or a bleached skull.

Color palette:
- Primary surface: bleached tan/clay, approx #C8A87A
- Crack lines: desert brown, approx #7A5030
- Sun highlight: off-white pale sand, approx #E8D8B0
- Hive entrance (small oval hole near center-bottom): very dark brown, approx #2A1000
- Dull wax marks: muted amber, approx #B07830
- Outline: dark clay, approx #5A3010

The texture should look like cracked, sun-bleached clay or bone with irregular crack marks,
a small entrance hole, and rough surface detail. Looks dry and harsh.

Do not use: lush green, sky blue, bright yellow, pure white, or any metallic colors.
Output: exactly 16x16 pixels, PNG with no alpha (fully opaque).
Style: flat pixel art, Minecraft Java Edition block texture aesthetic.
```

## Species Identity

```text
Name: Arid Hive
Species: Arid Bee
Biomes: desert, savanna, badlands
Feel: harsh desert — bleached clay, cracked, sun-scorched, bone-dry
```

## Do

- Use bleached tans, sandy beiges, and pale clay as the dominant palette.
- Include crack pattern lines across the face.
- Include a small dark entrance hole near the center-bottom.
- Convey dryness — no moisture, no moss, no lush elements.

## Don't

- Don't use greens, blues, or bright yellows.
- Don't copy or reference Forestry, Productive Bees, or vanilla bee nest textures.
- Don't add gradients or anti-aliasing.
- Don't make it look like wood — this hive is clay/bone, not timber.

## Acceptance Criteria

- [ ] File exists at target path.
- [ ] Texture is exactly 16×16 pixels, PNG.
- [ ] Visually harsh and arid-themed (bleached tan/clay dominant).
- [ ] Clearly distinguishable from Meadow Hive and Forest Hive.
- [ ] No copied third-party assets.
- [ ] Dev placeholder replaced.

## Status

- [ ] UV template available (N/A for cube_all — required for future oriented model)
- [x] Prompt created
- [ ] Asset generated/provided
- [ ] Asset validated
- [ ] Asset integrated
