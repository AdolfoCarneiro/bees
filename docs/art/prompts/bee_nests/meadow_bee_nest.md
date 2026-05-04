# Asset prompts — Meadow Bee Nest (multi-face)

Parent model: `minecraft:block/orientable_with_bottom`. Five texture files + `particle` = **side**.

## Target paths

```text
neoforge/src/main/resources/assets/curiousbees/textures/block/meadow_bee_nest_bottom.png
neoforge/src/main/resources/assets/curiousbees/textures/block/meadow_bee_nest_top.png
neoforge/src/main/resources/assets/curiousbees/textures/block/meadow_bee_nest_side.png
neoforge/src/main/resources/assets/curiousbees/textures/block/meadow_bee_nest_front.png
neoforge/src/main/resources/assets/curiousbees/textures/block/meadow_bee_nest_front_honey.png
```

## Style

16×16 each, Minecraft pixel art, bright pastoral meadow wood — lighter than Forest, warmer than Arid.

## Palette anchors (suggested)

- Warm blond wood: `#C9A86C`
- Straw / reed: `#E6D08A`
- Soft bark shadow: `#8B6F3A`
- Grass accent: `#6A9E4A`
- Hole: `#2A1A0A`
- Outline: `#5A4020`

## Per-face brief

| Face | Role |
|------|------|
| bottom | Lighter underside, horizontal reed/grass hints optional. |
| top | Sun-bleached cap, maybe small grass blade pixels at edge. |
| side | Friendly bark/reed weave, **no hole** — icon / particle face. |
| front | Entrance hole, cheerful meadow nest framing. |
| front_honey | Honey drips; keep warm amber, readable hole. |

## Layout reference

[docs/art/templates/bee_nest/README.md](../../templates/bee_nest/README.md)

## Example prompts (per file)

### meadow_bee_nest_side.png

```
16x16 Minecraft bee nest SIDE texture, meadow variant: light warm wood #C9A86C, straw accents #E6D08A,
subtle horizontal grain, tiny grass pixels #6A9E4A, outline #5A4020. No hole. Pixel art, hard edges.
```

### meadow_bee_nest_front.png

```
16x16 Minecraft bee nest FRONT, meadow: light bark frame, small dark entrance #2A1A0A lower-center,
horizontal grain, cheerful pastoral. Pixel art, limited palette, no honey drips.
```

### meadow_bee_nest_front_honey.png

```
16x16 meadow bee nest front with honey: warm amber drips around hole, still shows dark entrance.
Pixel art, hard edges, cohesive with meadow_bee_nest_front palette.
```

(Generate bottom/top similarly from the palette and “meadow / sunny / soft” brief.)

## Species identity

```text
Name: Meadow Bee Nest
Species: Meadow Bee
Biomes: plains, flower_forest, meadow
```

## Acceptance criteria

- [ ] Five PNGs at paths above; side used for particle.
- [ ] Distinct from Forest (darker) and Arid (dusty / pale).
- [ ] 16×16 each, no anti-aliasing.

## Status

- [x] Prompt created
- [ ] Assets generated
- [ ] Assets integrated
