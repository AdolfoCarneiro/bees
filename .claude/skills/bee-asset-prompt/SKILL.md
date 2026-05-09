---
name: bee-asset-prompt
description: Generate ultra-detailed GPT-4o Images prompts for Curious Bees mod assets (bee sprites, items, blocks, GUIs, block models, lang keys)
type: project-skill
---

# Bee Asset Prompt Generator

Generates ready-to-paste prompts for GPT-4o Images (ChatGPT) to create Curious Bees mod assets. Zero drawing required — paste the output directly into ChatGPT.

For `block_model`, outputs a Blockbench commissioning brief instead (requires Blockbench, not AI image generation).

---

## Quick reference

| Type | Canvas | Tool | Palette needed? |
|------|--------|------|-----------------|
| `bee_sprite` | 64×64 | GPT-4o Images | Yes (3–5 hex) |
| `item_icon` | 16×16 | GPT-4o Images | Yes (3–5 hex) |
| `block_texture` | 16×16 per face | GPT-4o Images | Yes (3–5 hex) |
| `block_model` | n/a (JSON) | Blockbench | No |
| `gui` | variable | GPT-4o Images | Yes (4 hex) |
| `lang_key` | n/a (JSON) | Text editor | No |

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
3. **Distinctive trait**: one sentence — what makes this visually unique (or the display label for lang_key)
4. **Palette** *(skip for `block_model` and `lang_key`)*: 3–5 hex codes OR describe colors in words
5. **Mood/character** *(bee sprites only)*: calm / busy / regal / wild / mysterious / ancient / aggressive
6. **Canvas size** *(gui only)*: WIDTH×HEIGHT in pixels, e.g. `256×168`
7. **Face** *(block_texture only)*: `top` | `side` | `bottom` — generate once per face
8. **Angry variant needed?** *(bee sprites only)*: yes/no — vanilla bee has separate angry texture

If user already provided any of these, skip asking for those.

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

If the user needs an angry variant, generate a second prompt with the same palette but a different body pose/expression. Vanilla bee has two texture files: `bee.png` (calm) and `bee_angry.png`.

```
[PROMPT START]
Minecraft pixel art sprite sheet. Canvas: 64×64 pixels, transparent background, zero anti-aliasing, zero gradients, zero sub-pixel rendering. Every pixel must be exactly one of the palette colors listed below — no exceptions, no blending.

Subject: a bee named [SPECIES_ID]. [DISTINCTIVE_TRAIT]. Mood: [MOOD].

Palette (STRICT — use ONLY these colors, no others):
- [#HEX1] — [role, e.g. "main body"]
- [#HEX2] — [role]
- [#HEX3] — [role]
- [#HEX4] — [role, e.g. "outline/stripes"]
- [#HEX5] — [role, e.g. "eye highlight"]

Layout (CRITICAL — must match vanilla Minecraft bee UV map):
- Top-left quadrant (0,0 to 32,32): front and side body segments, head, antennae
- Top-right quadrant (32,0 to 64,32): back body and sting detail
- Bottom-left quadrant (0,32 to 32,64): wing pair (top and bottom wing, semi-spread)
- Bottom-right quadrant (32,32 to 64,64): leg detail and remaining body parts

Style: Minecraft Java Edition retro pixel art (similar to 1.16 era). Readable silhouette from 4-block distance. Character visible in wing posture and eye shape.

AVOID: photorealism, Productive Bees art style, Forestry mod art style, rounded shapes, outlines thicker than 1 pixel, any color not in the palette above, anti-aliasing, gradients.

TIP: I will upload the vanilla Minecraft bee texture (bee.png) as a reference image. Match its UV layout exactly — only change colors, body shape, and distinctive markings.
[PROMPT END]
```

**Critical:** Tell the user — upload the vanilla bee texture file in ChatGPT alongside this prompt. Path in a default Minecraft install:
`.minecraft/versions/<version>/assets/minecraft/textures/entity/bee/bee.png`
This is the single most important thing for UV alignment.

**Wiring reminder:** After placing the PNG at its target path, the user must also update the **species→texture map** in the NeoForge platform layer. Without this step, the species renders with the fallback texture.

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

AVOID: photorealism, outlines thicker than 1 pixel, gradients, anti-aliasing, any color not in the palette above.
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

AVOID: photorealism, transparency, gradients, anti-aliasing, non-tileable edges, any color not in the palette above.
[PROMPT END]
```

### For `block_model` (Blockbench)

Block models require Blockbench, not AI image generation. Output a commissioning brief:

```
[BLOCKBENCH BRIEF START]
Block ID: [BLOCK_ID]
Model path:      neoforge/src/main/resources/assets/curiousbees/models/block/[BLOCK_ID].json
Blockstate path: neoforge/src/main/resources/assets/curiousbees/blockstates/[BLOCK_ID].json
Texture paths:   neoforge/src/main/resources/assets/curiousbees/textures/block/[BLOCK_ID]/{top,side,bottom}.png

Steps:
1. Generate each face PNG using the `block_texture` prompt type first.
2. Import face PNGs into Blockbench and build the model.
3. Export model JSON and blockstate JSON to the paths above.

Notes:
- Match vanilla block scale (1×1×1 unless explicitly a multi-block)
- If directional: add `facing` variants to blockstate JSON
[BLOCKBENCH BRIEF END]
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

AVOID: modern flat UI, rounded corners, drop shadows, gradients, anti-aliasing, any color not in the palette above.
[PROMPT END]
```

Tip: upload a screenshot of the genetic apiary GUI as a reference image for style consistency.

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
Source:       GPT-4o Images — prompt v1 (generated by /bee-asset-prompt)
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
