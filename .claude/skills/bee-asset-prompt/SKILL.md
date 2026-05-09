---
name: bee-asset-prompt
description: Generate ultra-detailed GPT-4o Images prompts for Curious Bees mod assets (bee sprites, items, blocks, GUIs, block models, lang keys)
type: project-skill
prompt_version: 2
---

> **Prompt version: 2.** Bump when templates change. Stamp the version in the manifest's `Source:` field so we can trace which generation a committed asset came from.

# Bee Asset Prompt Generator

Generates ultra-detailed GPT-4o Images prompts for Curious Bees mod assets. Paste the prompt into ChatGPT, attach reference image(s), then **post-process** the result before committing — see "Post-processing" section below. GPT-4o renders ~1024×1024 to ~1280×1280 stylized output (the model decides), never pixel-perfect 16×16 or 64×64. A simple downsample is usually enough to ship; strict palette quantize is a second pass when colors drift.

For `block_model`, outputs a Blockbench commissioning brief instead (requires Blockbench, not AI image generation).

---

## Quick reference

| Type | Canvas | Tool | Palette? | Ref image? | Post-process? |
|------|--------|------|----------|------------|---------------|
| `bee_sprite` | 64×64 | GPT-4o Images | 3–5 hex | **Critical** (bee.png) | Yes |
| `item_icon` | 16×16 | GPT-4o Images | 3–5 hex | Helpful (vanilla item) | Yes |
| `block_texture` | 16×16 per face | GPT-4o Images | 3–5 hex | Optional | Yes |
| `block_model` | n/a (JSON) | Blockbench | — | — | — |
| `gui` | variable | GPT-4o Images | 4 hex | Helpful (apiary screenshot) | Yes |
| `lang_key` | n/a (JSON) | Text editor | — | — | — |

**Default GUI sizes:** inventory-style `176×166` · larger machine `256×166` · analyzer/tall `256×222`.

---

## Post-processing (MANDATORY)

GPT-4o Images outputs ~1024×1024 to ~1280×1280 stylized images, NOT pixel-perfect targets. Every generation needs at minimum a downsample. Real-world result: a clean point-resize is usually enough to ship as a working asset — strict palette enforcement is only needed when color drift is visible.

### Two-pass workflow

**Pass 1 — Fast path (try first):** point-filter downsample only.

```bash
magick input.png -filter Point -resize 16x16 output.png
```

Replace `16x16` with target canvas (`64x64` for bee sprites, `WxH` for GUI). `-filter Point` (= nearest-neighbor) prevents the resize from inventing in-between colors. Open the result in Aseprite/GIMP and check if it looks acceptable. If yes — ship it.

**Pass 2 — Strict palette (when colors drift):** resize + quantize to the exact palette.

Save your palette as `palette.png` (one row of N pixels, one per palette color), then:

```bash
magick input.png -filter Point -resize 16x16 -dither None -remap palette.png output.png
```

`-dither None` prevents palette dithering. `-remap` forces every pixel to the closest palette color.

**When to use which:**
- bee_sprite, item_icon, GUI → fast path usually suffices; AI tends to honor prompt palette closely after the v2 prompts.
- block_texture (tileable) → fast path; verify edges visually.
- Any asset where palette consistency across a series matters (a family of related bees) → use Pass 2 for all of them.

### Recommended tooling

| Tool | Use for | Notes |
|------|---------|-------|
| **Aseprite** | Final cleanup, hand-tuning | Best pixel-art editor; supports indexed palettes |
| **GIMP** | Free alternative | `Image → Mode → Indexed → Custom palette` |
| **ImageMagick CLI** | Batch / scripted | Both passes above |
| **Online resizer** | Quick sanity check | iloveimg.com or similar — point/nearest-neighbor only |

### Fallback if GPT-4o output is unusable

If 3–5 generation attempts still produce off-style results:
1. **Use GPT output as reference, hand-paint in Aseprite** — fastest path to ship.
2. **Try a pixel-art-specialized model** — PixelLab, Scenario.gg, or Stable Diffusion + Pixel-Art LoRA.
3. **Start from a tinted vanilla texture** — keeps DEV-PLACEHOLDER tag until proper art arrives.

---

## Mood → visual cues *(bee sprites only)*

Translate mood to concrete visual descriptors when filling the prompt:

| Mood | Visual cues |
|------|-------------|
| `calm` | Symmetric markings, smooth outline, neutral eye, relaxed antenna angle |
| `busy` | Slightly motion-blurred wing posture, alert eye, forward-leaning body |
| `regal` | Upright posture, gold/silver accent stripes, longer antennae, larger eye |
| `wild` | Asymmetric markings, ruffled outline, sharp angles, intense eye |
| `mysterious` | Desaturated palette skew, partial shadow on body, eye highlight prominent |
| `ancient` | Weathered texture, muted/earth tones, slightly cracked outline pixels |
| `aggressive` | Spiked silhouette, exposed sting, narrowed eye, contrasting alert colors |

Append the relevant cue line to the prompt's `Mood:` field.

---

## Step 0 — Hard rules reminder (HR-2, HR-5)

Before collecting inputs, confirm two non-negotiables with the user:

**HR-2 — Never crash on missing assets.** Consumer code must fall back visibly (vanilla bee texture, generic icon) and log a WARNING if the asset file is absent. If consumer code doesn't handle this yet, fix it before wiring the new asset.

**HR-5 — Naming rules.** Asset filenames must be:
- **Lowercase only** — no `ForestBee.png`, no `Forest_Bee.png`
- **Underscores** as word separators — no hyphens, no spaces
- **No platform separators** — forward slash only in paths
- Valid: `forest_bee.png`, `honey_comb.png`, `genetic_apiary.png`
- Invalid: `ForestBee.png`, `forest-bee.png`, `Forest Bee.png`

Correct the ID before continuing if the user's proposal violates these rules.

---

## Step 1 — Collect inputs

Ask the user in one message. Only ask fields relevant to the asset type:

1. **Asset type**: `bee_sprite` | `item_icon` | `block_texture` | `block_model` | `gui` | `lang_key`
2. **ID** (snake_case): e.g. `forest_bee`, `honey_comb`, `genetic_apiary`
3. **Palette** *(skip for `block_model` and `lang_key`)*: 3–5 hex codes OR describe colors in words

### Per-type extra fields

**For `bee_sprite`** — ask all of:
- **Body markings**: stripes / spots / gradient / solid / patches
- **Eye style**: compound (multi-pixel) / single-dot / glowing / sleepy
- **Wing pattern**: clear / veined / iridescent / tattered
- **Antenna style**: short straight / long curled / single-segment / feathered
- **Mood/character**: calm / busy / regal / wild / mysterious / ancient / aggressive
- **Angry variant needed?**: yes/no — produces a second prompt for `<id>_angry.png`

**For `item_icon`**, `block_texture`, `gui`, `block_model`:
- **Distinctive trait**: one sentence — what makes this visually unique

**For `block_texture` only**:
- **Face**: `top` | `side` | `bottom` — generate once per face

**For `gui` only**:
- **Canvas size**: WIDTH×HEIGHT in pixels (defaults: see Quick reference)

**For `lang_key`**:
- **Display label**: human-readable English string

If user already provided any of these in the invoking message, skip asking for those.

---

## Step 1b — Consumer code pre-check

Before generating, ask:

> "Does the consumer code (renderer, model JSON, screen constant, species→texture map) already exist for this asset?"

- **Yes** → proceed.
- **No** → advise the user to ship a labeled placeholder first (guidelines §4.2 step 1). Do not commission final art ahead of consumer code. Tag the placeholder `DEV-PLACEHOLDER`.

---

## Step 2 — Suggest palette if needed *(pixel-art types only)*

Skip for `block_model` and `lang_key`.

If user gave colors in words, convert to 3–5 hex codes:
- High contrast between body color, accent, and eye color
- Avoid colors already associated with vanilla bee (`#F9A825` yellow, `#1A1A1A` stripe)
- Pick saturated, readable tones (Minecraft palette tends toward earthy/vivid)
- Always include one dark shade for outlines/stripes (`#1A1A1A` to `#3D3D3D`)

Show suggested palette and ask "OK to use this palette?" before continuing.

---

## Step 3 — Generate the prompt

### For `bee_sprite` (64×64 atlas)

If the user needs an angry variant, generate a second prompt with the same palette but the `aggressive` mood cues (see Mood→visual cues table). Vanilla bee has two texture files: `bee.png` (calm) and `bee_angry.png`.

```
[PROMPT START]
Minecraft entity texture sheet for a bee. Canvas: 64×64 pixels, transparent background, retro pixel-art style. Every pixel must be exactly one of the palette colors listed below — no exceptions, no blending. Hard 1-pixel edges only, no anti-aliasing, no gradients, no sub-pixel rendering.

Subject: a bee named [SPECIES_ID].
- Body markings: [BODY_MARKINGS]
- Eye style: [EYE_STYLE]
- Wing pattern: [WING_PATTERN]
- Antenna style: [ANTENNA_STYLE]
- Mood: [MOOD] — [VISUAL_CUES_FROM_MOOD_TABLE]

Palette (STRICT — use ONLY these colors, no others):
- [#HEX1] — [role, e.g. "main body"]
- [#HEX2] — [role]
- [#HEX3] — [role]
- [#HEX4] — [role, e.g. "outline/stripes"]
- [#HEX5] — [role, e.g. "eye highlight"]

Layout (CRITICAL): match the vanilla Minecraft bee UV map exactly. I am uploading `bee.png` as a reference image. Replicate every body part's position and rectangle size from that reference — only change colors, markings, eye, antennae, and wing details. Do NOT redesign the layout.

Style: Minecraft Java Edition retro pixel art (1.16 era). Readable silhouette from 4-block distance.

AVOID: photorealism, soft airbrush rendering, plush-toy proportions, cartoon-mascot style, rounded outline shapes, neon saturation, glossy/3D-shaded look, outlines thicker than 1 pixel, anti-aliasing, gradients, any color not in the palette above.
[PROMPT END]
```

**Critical:** the user MUST upload the vanilla bee texture in ChatGPT alongside this prompt. Without it, the result will fail UV alignment.

**How to get bee.png** (modern Minecraft layout):
1. Locate `client.jar` — usually at `.minecraft/versions/<version>/<version>.jar` (or `client.jar`).
2. Open it with any zip tool (7-Zip, WinRAR, `unzip`).
3. Extract `assets/minecraft/textures/entity/bee/bee.png`.

Alternative: download from a Minecraft texture mirror (search "minecraft bee.png texture").

**Wiring reminder:** After placing the PNG, the user must update the **species→texture map** in the NeoForge platform layer. Without this step, the species renders with the fallback texture.

### For `item_icon` (16×16)

```
[PROMPT START]
Minecraft pixel art item icon. Canvas: 16×16 pixels, transparent background, zero anti-aliasing, zero gradients, zero sub-pixel rendering. Every pixel must be exactly one of the palette colors listed below — no exceptions, no blending.

Subject: [ITEM_ID] — [DISTINCTIVE_TRAIT].

Palette (STRICT — use ONLY these colors, no others):
- [#HEX1] — [role]
- [#HEX2] — [role]
- [#HEX3] — [role, e.g. "outline"]

Style: Minecraft Java Edition item sprite, retro pixel art. Single item, centered, occupying ~80% of canvas. No drop shadow.

AVOID: photorealism, soft airbrush rendering, neon saturation, glossy/3D-shaded look, plastic/toy-like finish, mobile-game cartoon style, outlines thicker than 1 pixel, gradients, anti-aliasing, any color not in the palette above.
[PROMPT END]
```

Tip: upload a similar vanilla item (e.g. `honeycomb.png`) as a reference image — helps AI match the sprite density and pixel weight.

### For `block_texture` (16×16, [FACE] face)

Generate once per face. Run the `block_model` flow after all face PNGs are ready.

```
[PROMPT START]
Minecraft pixel art block face texture. Canvas: 16×16 pixels. All pixels are opaque — no transparency. Zero anti-aliasing, zero gradients, zero sub-pixel rendering. Every pixel must be exactly one of the palette colors listed below — no exceptions, no blending.

Subject: [BLOCK_ID] — [DISTINCTIVE_TRAIT]. This texture is for the [top/side/bottom] face.

Palette (STRICT — use ONLY these colors, no others):
- [#HEX1] — [role]
- [#HEX2] — [role]
- [#HEX3] — [role]

Style: Minecraft Java Edition block texture, retro pixel art. Subtle surface variation and natural-looking pixel noise. Avoid perfectly uniform fills. The texture must tile seamlessly — left edge must match right edge, top edge must match bottom edge.

AVOID: photorealism, soft airbrush rendering, hand-painted look, RPG-tile style, neon saturation, dramatic lighting/highlights, transparency, gradients, anti-aliasing, non-tileable edges, any color not in the palette above.
[PROMPT END]
```

### For `block_model` (Blockbench)

Block models require Blockbench, not AI image generation. First ask the user:

> "Is this a **cuboid** (vanilla cube shape, e.g. nest block) or a **custom geometry** block (e.g. genetic apiary, centrifuge)?"

#### Cuboid branch

```
[BLOCKBENCH BRIEF — CUBOID START]
Block ID: [BLOCK_ID]
Model path:      neoforge/src/main/resources/assets/curiousbees/models/block/[BLOCK_ID].json
Blockstate path: neoforge/src/main/resources/assets/curiousbees/blockstates/[BLOCK_ID].json
Texture paths:   neoforge/src/main/resources/assets/curiousbees/textures/block/[BLOCK_ID]/{top,side,bottom}.png

Steps:
1. Generate each face PNG using the `block_texture` prompt type first.
2. Use the `cube_all` or `cube_bottom_top` parent in the model JSON — no Blockbench needed for simple cubes.
3. Wire blockstate JSON. Add `facing` variants if directional.
[BLOCKBENCH BRIEF — CUBOID END]
```

#### Custom-geometry branch

```
[BLOCKBENCH BRIEF — CUSTOM START]
Block ID: [BLOCK_ID]
Model path:      neoforge/src/main/resources/assets/curiousbees/models/block/[BLOCK_ID].json
Blockstate path: neoforge/src/main/resources/assets/curiousbees/blockstates/[BLOCK_ID].json
Texture sheet:   neoforge/src/main/resources/assets/curiousbees/textures/block/[BLOCK_ID]/sheet.png

Steps:
1. Model the block from scratch in Blockbench (`File → New → Java Block/Item`).
2. Stay within the 16×16×16 voxel grid unless this is a multi-block — Minecraft block models cannot exceed 1.5 blocks in any axis.
3. UV-map every cube face onto a single texture sheet PNG.
4. Hand-paint or commission the texture sheet (do NOT use the `block_texture` AI prompt — sheet layout is irregular).
5. Export → Java Block to model JSON. Wire blockstate.

Notes:
- Use `block/block` as the parent model.
- Add `display` transforms (`gui`, `firstperson_righthand`, etc.) for inventory rendering.
- Animated machines: add a `tickets` blockstate variant per state, or use a NeoForge BlockEntityRenderer.
[BLOCKBENCH BRIEF — CUSTOM END]
```

### For `gui` ([WIDTH]×[HEIGHT])

```
[PROMPT START]
Minecraft GUI panel background texture. Canvas: [WIDTH]×[HEIGHT] pixels, zero anti-aliasing, zero gradients, zero sub-pixel rendering. Every pixel must be exactly one of the palette colors listed below — no exceptions, no blending.

Purpose: [GUI_ID] — [DISTINCTIVE_TRAIT].

Palette (STRICT — use ONLY these colors, no others):
- [#HEX1] — panel background
- [#HEX2] — border/frame
- [#HEX3] — slot highlight
- [#HEX4] — dark inset areas

Style: Minecraft Java Edition GUI, retro pixel art. Similar to vanilla crafting table or furnace interface. Clear slot zones. Border has subtle 1-pixel bevel. All Curious Bees screens must share one visual language — match the genetic apiary GUI style.

AVOID: modern flat UI (Material/iOS style), rounded corners, drop shadows, glassmorphism, mobile-app aesthetic, RPG-fantasy parchment look, gradients, anti-aliasing, any color not in the palette above.
[PROMPT END]
```

Tip: upload a screenshot of the genetic apiary GUI as a reference image for style consistency. Also see Quick reference for default GUI canvas sizes.

### For `lang_key`

Lang keys are JSON entries, not image assets. Output a snippet and rules:

```json
// In: neoforge/src/main/resources/assets/curiousbees/lang/en_us.json
"[NAMESPACE].[TYPE].[ID]": "[Human-readable English label]"
```

Key format by type:
- `item.curiousbees.<id>` for items
- `block.curiousbees.<id>` for blocks
- `entity.curiousbees.<id>` for entities
- `gui.curiousbees.<id>` for GUI labels

Rules:
- English (`en_us.json`) is required; other locales optional.
- Missing keys → WARNING logged + key name shown as fallback (HR-2).
- **Never expose internal allele IDs in player-facing labels.**
- No manifest entry needed — lang keys are tracked via the JSON file directly.

---

## Step 4 — Output the asset manifest entry

After the prompt/brief block, output the filled §6 template from `docs/asset-generation-guidelines.md`.

Skip for `lang_key` — lang entries are not tracked in the asset manifest.

Correct target paths by type:

| Type | Target path |
|------|------------|
| `bee_sprite` | `neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/<species_id>.png` |
| `item_icon` | `neoforge/src/main/resources/assets/curiousbees/textures/item/<item_id>.png` |
| `block_texture` | `neoforge/src/main/resources/assets/curiousbees/textures/block/<block_id>/<face>.png` |
| `block_model` | `neoforge/src/main/resources/assets/curiousbees/models/block/<block_id>.json` |
| `gui` | `neoforge/src/main/resources/assets/curiousbees/textures/gui/<screen_id>.png` |

```
Asset:        [id]
Target path:  [see table above — use the full path]
Size:         [e.g. 64x64 / 16x16]
Style notes:  pixel art, vanilla Minecraft aesthetic
Palette:      [list hex codes — omit for block_model]
References:   vanilla bee (bee.png), vanilla Minecraft [item/block] style
Negative:     no photorealism, no Productive Bees clone, no Forestry clone, no gradients
Status:       PENDING / IN-REVIEW / FINAL  ← delete inapplicable
Source:       GPT-4o Images — prompt v2 (generated by /bee-asset-prompt)
License:      mod-internal  (change to CC0 or other if externally sourced)
```

---

## Step 5 — Iteration tips *(pixel-art types)*

**Palette ignored by AI:**
Add this at the very top of the prompt (before everything else):
```
CRITICAL CONSTRAINT: Use ONLY these exact hex colors: [#HEX1], [#HEX2], [#HEX3]. Any other color is a failure. Do not blend, average, or approximate.
```

**UV layout wrong (bee sprite):** Upload vanilla `bee.png` as a reference image — fixes 90% of layout issues.

**Style too modern:** Add `"reference: Minecraft Java Edition 1.16 art style, NOT modded texture packs, NOT modern indie games"`

**Palette keeps straying:** Reduce to 3 colors max and explicitly name which body part uses which color.

**Item icon too complex:** Add `"simple iconic shape, minimal detail, must be readable at actual 16×16 pixel size"`

**Block texture not tileable:** Add `"verify: left edge pixel row identical to right edge pixel row; top edge identical to bottom edge"`

**GUI doesn't match apiary style:** Upload a screenshot of the genetic apiary GUI as a reference image.

**Result has anti-aliasing:** Add `"NO anti-aliasing, NO sub-pixel rendering, NO blending between adjacent pixels — hard edges only"`

---

## Step 6 — After committing

Definition of done (from guidelines §4.3):

1. File at the correct path with the correct snake_case name.
2. Consumer code wired: model JSON, screen constant, lang key, species→texture map as applicable.
3. `DEV-PLACEHOLDER` tag removed for that asset — run `rg DEV-PLACEHOLDER` to confirm scope.
4. **Test multiplayer:** texture/atlas issues often only surface on a dedicated server or when mods reorder registries.
5. License / source noted in the PR description if AI-generated or externally sourced.
6. **Commit prefix:** use `client:` for asset files + screen wiring, `neoforge:` for block/item registration, `docs:` only if updating these guidelines.

**Pre-release sweep** — before any release tag:
```
rg DEV-PLACEHOLDER
```
Any hit in an area whose phase is marked complete in `docs/roadmap.md` must be fixed or the release downgraded. Old / unused assets must be removed — orphan textures bloat the jar.

**Tagging placeholders** — when committing art that isn't final yet:
1. Add `DEV-PLACEHOLDER` in the commit **or** the code constant/model `_comment` (any form a single grep can find).
2. Follow the process in `docs/asset-generation-guidelines.md §4`.
3. Remove the tag only after the final reviewed version lands.

---

## Appendix — Worked example: `forest_bee`

End-to-end run to show what a complete invocation looks like.

### Inputs collected

- **Asset type**: `bee_sprite`
- **ID**: `forest_bee`
- **Palette**: deep mossy green body, ochre stripes, dark brown outline, amber eye
  - Suggested hex: `#3A5F3A` (body) · `#C68A2E` (stripes) · `#2A1B0F` (outline) · `#E8B547` (eye highlight) · `#1A1A1A` (deep shadow)
- **Body markings**: thin horizontal stripes
- **Eye style**: compound (multi-pixel)
- **Wing pattern**: veined
- **Antenna style**: short straight
- **Mood**: `calm` → "Symmetric markings, smooth outline, neutral eye, relaxed antenna angle"
- **Angry variant needed?**: yes

### Generated prompt (calm)

```
[PROMPT START]
Minecraft entity texture sheet for a bee. Canvas: 64×64 pixels, transparent background, retro pixel-art style. Every pixel must be exactly one of the palette colors listed below — no exceptions, no blending. Hard 1-pixel edges only, no anti-aliasing, no gradients, no sub-pixel rendering.

Subject: a bee named forest_bee.
- Body markings: thin horizontal stripes
- Eye style: compound (multi-pixel)
- Wing pattern: veined
- Antenna style: short straight
- Mood: calm — symmetric markings, smooth outline, neutral eye, relaxed antenna angle

Palette (STRICT — use ONLY these colors, no others):
- #3A5F3A — main body (mossy green)
- #C68A2E — stripes (ochre)
- #2A1B0F — outline (dark brown)
- #E8B547 — eye highlight (amber)
- #1A1A1A — deep shadow

Layout (CRITICAL): match the vanilla Minecraft bee UV map exactly. I am uploading `bee.png` as a reference image. Replicate every body part's position and rectangle size from that reference — only change colors, markings, eye, antennae, and wing details. Do NOT redesign the layout.

Style: Minecraft Java Edition retro pixel art (1.16 era). Readable silhouette from 4-block distance.

AVOID: photorealism, soft airbrush rendering, plush-toy proportions, cartoon-mascot style, rounded outline shapes, neon saturation, glossy/3D-shaded look, outlines thicker than 1 pixel, anti-aliasing, gradients, any color not in the palette above.
[PROMPT END]
```

### Manifest entry

```
Asset:        forest_bee
Target path:  neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/forest_bee.png
Size:         64x64
Style notes:  pixel art, vanilla Minecraft aesthetic
Palette:      #3A5F3A, #C68A2E, #2A1B0F, #E8B547, #1A1A1A
References:   vanilla bee (bee.png)
Negative:     no photorealism, no Productive Bees clone, no Forestry clone, no gradients
Status:       PENDING
Source:       GPT-4o Images — prompt v2 (generated by /bee-asset-prompt)
License:      mod-internal
```

### Post-processing commands

**Pass 1 (try first):**
```bash
magick raw_output.png -filter Point -resize 64x64 forest_bee.png
```
Open in Aseprite — if palette and silhouette look right, ship it.

**Pass 2 (only if colors drifted):** save palette as `palette_forest_bee.png` (5 pixels in a row, one per hex), then:
```bash
magick raw_output.png -filter Point -resize 64x64 -dither None -remap palette_forest_bee.png forest_bee.png
```

Sweep for stray pixels in Aseprite, save final.

### Wiring

- Place at `neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/forest_bee.png`
- Add to species→texture map in the NeoForge platform layer
- Add lang key: `entity.curiousbees.forest_bee` → `"Forest Bee"` in `en_us.json`
- If angry variant: repeat for `forest_bee_angry.png` with `aggressive` mood cues
