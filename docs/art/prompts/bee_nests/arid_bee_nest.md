# Asset prompts — Arid Bee Nest (multi-face)

Parent model: `minecraft:block/orientable_with_bottom`. Five textures + **side** for particle.

## Target paths

```text
neoforge/src/main/resources/assets/curiousbees/textures/block/arid_bee_nest_bottom.png
neoforge/src/main/resources/assets/curiousbees/textures/block/arid_bee_nest_top.png
neoforge/src/main/resources/assets/curiousbees/textures/block/arid_bee_nest_side.png
neoforge/src/main/resources/assets/curiousbees/textures/block/arid_bee_nest_front.png
neoforge/src/main/resources/assets/curiousbees/textures/block/arid_bee_nest_front_honey.png
```

## Style

16×16 each; dry, sun-bleached, cracked clay / pale driftwood / desert scrub vibe — not forest green, not meadow yellow.

## Palette anchors (suggested)

- Bleached wood / clay: `#D4C4A8`
- Dry shadow: `#9A8A6E`
- Crack / crevice: `#6E5A42`
- Sand highlight: `#E8DCC8`
- Hole: `#2A2018`
- Optional sparse scrub: `#8A9E6A` (muted, few pixels)

## Per-face brief

| Face | Role |
|------|------|
| bottom | Dusty underside, fine cracks. |
| top | Sun-scoured cap, maybe wind-swept edge. |
| side | Cracked pale shell, no hole. |
| front | Entrance; frame feels dry and arid. |
| front_honey | Honey reads as richer amber against pale arid frame. |

## Layout reference

[docs/art/templates/bee_nest/README.md](../../templates/bee_nest/README.md)

## Example prompts

### arid_bee_nest_side.png

```
16x16 Minecraft bee nest SIDE, arid biome: pale sun-bleached wood/clay #D4C4A8, cracks #6E5A42,
shadows #9A8A6E, few muted scrub pixels #8A9E6A, outline #6E5A42. No hole. Pixel art, hard edges.
```

### arid_bee_nest_front.png

```
16x16 bee nest FRONT, arid: pale frame, small dark entrance #2A2018 lower-center, cracked texture,
desert feel. Pixel art, limited palette, no honey.
```

### arid_bee_nest_front_honey.png

```
16x16 arid bee nest front with honey drips: warm amber honey contrasting pale dry bark; hole still visible.
Pixel art, hard edges.
```

## Species identity

```text
Name: Arid Bee Nest
Species: Arid Bee
Biomes: desert, savanna, badlands
```

## Acceptance criteria

- [ ] Five PNGs at paths above.
- [ ] Clearly arid / pale vs Forest and Meadow.
- [ ] 16×16, opaque block style.

## Status

- [x] Prompt created
- [ ] Assets generated
- [ ] Assets integrated
