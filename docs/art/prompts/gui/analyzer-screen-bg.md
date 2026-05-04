# Asset Prompt — Bee Analyzer Screen Background

## Target Path

```
neoforge/src/main/resources/assets/curiousbees/textures/gui/analyzer_bg.png
```

## Dev Status

The current screen uses a hard-coded color panel (no texture). This prompt defines the
final background texture. Integrate when the user provides the generated asset.

## Asset Type

GUI background texture — 220×148 pixels.

## Size

220×148 pixels.

## Style

Minecraft Java Edition GUI style. Dark amber/wood-panel aesthetic. Resembles aged wood with
honeycomb inlay accents. Hard pixel edges, flat shading, limited palette. No gradients.

## Palette Notes

- Panel base: deep amber-brown — approx. `#1A1006`
- Wood grain lines: slightly lighter — approx. `#2A1A0A`
- Border: golden amber — approx. `#8B6914`
- Inner highlight strip (top): pale gold — approx. `#C8A020`
- Honeycomb hex pattern (subtle, corners): dark honey — approx. `#6A4A0A`

## Usage

Background panel rendered behind the analyzer report text in `BeeAnalyzerScreen`.
Panel size: 220×148 pixels. Text and labels are drawn on top by the screen renderer.

## UV Template Reference

```text
UV template path: N/A — this is a flat GUI panel, not a UV-mapped model texture.
```

## Image Generation Prompt

```
220x148 pixel art GUI background panel for a Minecraft-style inventory screen.
Flat, hard edges, no anti-aliasing, no gradients, limited palette of 6 to 8 colors.

Theme: Bee Analyzer — a scientific tool in a nature/beekeeping aesthetic.
Feel: aged dark wood panel with subtle honeycomb corner accents and a golden border.

Layout:
- Full 220x148 canvas.
- Outer border: 1-pixel golden amber line (#8B6914) on all four edges.
- Inner panel fill: deep amber-brown (#1A1006).
- Subtle horizontal wood grain lines (every 4-6 pixels, slightly lighter #2A1A0A).
- Small honeycomb hex pattern in bottom-left and bottom-right corners (very subtle, dark honey #6A4A0A, 8-12 pixels).
- Thin inner border highlight at top: 1-pixel pale gold line (#C8A020) at y=1.

Do not add: text, icons, slot indicators, or bee imagery. This is a blank background panel only.
Output: exactly 220x148 pixels, PNG with no alpha (fully opaque).
Style: flat pixel art, Minecraft Java Edition GUI texture aesthetic.
```

## Acceptance Criteria

- [ ] File exists at target path.
- [ ] Exactly 220×148 pixels, PNG.
- [ ] Dark amber-wood aesthetic with gold border.
- [ ] No text, icons, or slot outlines drawn on the texture.
- [ ] No copied third-party textures.

## Status

- [x] Prompt created
- [ ] Asset generated/provided
- [ ] Integrated into BeeAnalyzerScreen
