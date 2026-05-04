# Asset Prompt — Genetic Beehive GUI Background

## Target Path

```
neoforge/src/main/resources/assets/curiousbees/textures/gui/genetic_apiary_bg.png
```

## Dev Status

The current screen uses the vanilla dispenser texture as a placeholder.
Replace with this final asset when ready.

## Asset Type

GUI background texture — 176×166 pixels (standard Minecraft container size).

## Size

176×166 pixels.

## Style

Minecraft Java Edition GUI style. Warm honey/wood panel aesthetic with dedicated regions for:
- Title area (top)
- Bee status panel (left column)
- Frame row (center-top)
- Output inventory (center-bottom)
- Player inventory (bottom two rows)

Hard pixel edges, flat shading, limited palette. No gradients.

## Palette Notes

- Panel base: warm parchment/amber — approx. `#C8A858`
- Slot background: dark honey-brown — approx. `#3A2010`
- Slot border: medium amber — approx. `#6A4818`
- Section dividers: dark brown line — approx. `#2A1400`
- Bee status area: slightly darker parchment — approx. `#B89040`
- Honey bar background: dark amber — approx. `#6B4C0A`
- Honey bar fill: bright gold — approx. `#F0C030`

## Layout

```
176x166 pixels total.

y=0-16:   Title area (parchment background, "Genetic Beehive" title drawn by renderer)
y=17-35:  Frame row — 3 slots at x=62,80,98 (18px each). Standard slot recesses.
y=36-42:  Honey bar area (x=8, width=50) — empty bar + fill region
y=43-100: Left info panel (x=8..54) for bee summary + right side for output slots
y=53-88:  Output slots — 2 rows of 3 (x=62,80,98 / y=53,71)
y=102-164: Standard player inventory (3 rows + hotbar, 9 slots each)
```

## UV Template Reference

```text
UV template path: N/A — this is a flat GUI panel, not a UV-mapped model texture.
```

## Image Generation Prompt

```
176x166 pixel art GUI background texture for a Minecraft-style inventory container.
Flat, hard edges, no anti-aliasing, no gradients, limited palette of 8 to 10 colors.

Theme: Genetic Beehive — a beekeeping station with warm honey-wood aesthetic.

Panel layout:
- Full 176x166 canvas.
- Overall background: warm parchment/amber (#C8A858).
- Outer border: 1px dark brown (#2A1400).
- Title strip (y=0-16): slightly darker amber (#B89040) with 1px bottom divider (#2A1400).
- Left info region (x=8-54, y=43-100): lightly recessed area with subtle inset border (#6A4818).
- Frame slots (3 slots at y=17-35, x=62/80/98): standard 18x18 slot recesses with dark inset (#3A2010) and 1px border (#6A4818).
- Output slots (2 rows x 3 at y=53/71, x=62/80/98): same 18x18 recess style.
- Honey bar region (x=8-58, y=36-42): narrow recessed channel, dark background (#6B4C0A), no fill (renderer draws fill).
- Player inventory divider line at y=101: 1px dark brown.
- Player inventory (y=102-164): 3 rows x 9 + hotbar row, standard 18x18 slots.
- Small honeycomb hex accent at x=155-170, y=5-15 (decorative corner detail).

Do not add: text, item icons, or animated elements. Pure background texture only.
Output: exactly 176x166 pixels, PNG with no alpha (fully opaque).
Style: flat pixel art, Minecraft Java Edition GUI texture aesthetic.
```

## Acceptance Criteria

- [ ] File exists at target path.
- [ ] Exactly 176×166 pixels, PNG.
- [ ] Warm amber/wood aesthetic.
- [ ] Slot recesses align with menu slot positions.
- [ ] No text or item icons baked in.
- [ ] No copied third-party textures.

## Status

- [x] Prompt created
- [ ] Asset generated/provided
- [ ] Integrated into GeneticApiaryScreen (replace BG_TEXTURE ResourceLocation)
