# Asset prompts — Forest Bee Nest (multi-face)

Blocks use parent `minecraft:block/orientable_with_bottom` (same geometry as vanilla bee nest).  
Each face is its own **16×16** PNG. `particle` in the model uses the **side** texture.

## Dev placeholders

Current files are duplicated dev placeholders until final art lands. They do not count as complete assets.

## Target paths (five PNGs; `particle` uses `side`)

```text
neoforge/src/main/resources/assets/curiousbees/textures/block/forest_bee_nest_bottom.png
neoforge/src/main/resources/assets/curiousbees/textures/block/forest_bee_nest_top.png
neoforge/src/main/resources/assets/curiousbees/textures/block/forest_bee_nest_side.png
neoforge/src/main/resources/assets/curiousbees/textures/block/forest_bee_nest_front.png
neoforge/src/main/resources/assets/curiousbees/textures/block/forest_bee_nest_front_honey.png
```

(`particle` reuses `forest_bee_nest_side.png` — no extra file.)

## Size and style

- 16×16 per file, PNG, opaque (no alpha unless you need sparse holes — prefer opaque for block maps).
- Minecraft Java block pixel art: hard edges, flat shading, limited palette, no anti-aliasing.

## Palette anchors (all faces should feel cohesive)

- Primary bark: `#5A3A1A`
- Grain highlights: `#7A5030`
- Moss: `#3A5A2A`
- Entrance hole: `#1A0A00`
- Sap / resin: `#A06020`
- Outline: `#2A1000`

## Per-face brief

| Face | Role |
|------|------|
| **bottom** | Underside of nest; darker bark, subtle grain; no hole. |
| **top** | Upper “cap”; slightly weathered bark, maybe a knot or crack; no hole. |
| **side** | Bark shell without entrance; vertical grain; moss in crevices; used for particle/icon preview. |
| **front** | Entrance hole (oval, lower-center), bark frame; clearest “face” of the nest. |
| **front_honey** | Same as front but with honey drip / sealed glow like vanilla `bee_nest_front_honey` — amber drips, keep hole readable. |

## UV / layout reference

See [docs/art/templates/bee_nest/README.md](../../templates/bee_nest/README.md) for vanilla 1.21.1 bee nest links — match **proportions** (where the hole sits, how thick the border reads) to that layout.

## Image generation prompts (paste per asset)

### forest_bee_nest_bottom.png

```
16x16 Minecraft block texture, bottom face of a wild bee nest in dark forest bark.
Pixel art, hard edges, no anti-aliasing, 6-8 colors max. Dark brown #5A3A1A base, grain #7A5030,
outline #2A1000, tiny moss specks #3A5A2A. No entrance hole. Flat shading.
```

### forest_bee_nest_top.png

```
16x16 Minecraft block texture, top face of a wild bee nest: weathered dark bark cap.
Pixel art, hard edges. Colors: dark brown #5A3A1A, highlights #7A5030, moss #3A5A2A, outline #2A1000.
Optional small crack or knot. No hole. No honey.
```

### forest_bee_nest_side.png

```
16x16 Minecraft block texture, side of wild bee nest without hole: vertical dark bark grain,
moss in crevices #3A5A2A, sap streaks #A06020, palette browns #5A3A1A / #7A5030 / #2A1000.
Pixel art, no anti-aliasing.
```

### forest_bee_nest_front.png

```
16x16 Minecraft block texture, FRONT of wild bee nest: dark bark frame, small dark oval entrance
near lower-center #1A0A00, vertical grain, moss pixels #3A5A2A, sap #A06020. Woodland mood.
Pixel art, hard edges, limited palette, no honey drips yet.
```

### forest_bee_nest_front_honey.png

```
16x16 Minecraft block texture, same forest bee nest FRONT as previous but honey-filled: golden-amber
drip pixels around hole and upper lip like vanilla bee nest honey front, still readable hole.
Keep bark colors #5A3A1A / #7A5030; honey accents warm amber not neon yellow. Pixel art, hard edges.
```

## Species identity

```text
Name: Forest Bee Nest
Species: Forest Bee
Biomes: forest, birch_forest, dark_forest
Feel: deep woodland — dark bark, mossy, aged
```

## Acceptance criteria

- [ ] All five files exist at paths above (side doubles as particle).
- [ ] Each texture is exactly 16×16.
- [ ] Front and front_honey read as the same nest with / without honey.
- [ ] Visually distinct from Meadow and Arid nests at a glance.
- [ ] No copied third-party pixels.

## Status

- [x] Prompt created
- [ ] Assets generated
- [ ] Assets integrated and validated
