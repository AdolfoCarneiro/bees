---
name: bee-asset-prompt
description: Generate ultra-detailed GPT-4o Images prompts for Curious Bees mod assets (bee sprites, items, blocks, GUIs, sounds, block models, lang keys)
type: project-skill
---

# Bee Asset Prompt Generator

Generates ready-to-paste prompts for GPT-4o Images (ChatGPT) to create Curious Bees mod assets. Zero drawing required — paste the output directly into ChatGPT.

For `sound` and `block_model` types, outputs a commissioning brief instead (these require Blockbench or audio tools, not AI image generation).

---

## Step 0 — Hard rules reminder (HR-2, HR-5)

Before collecting inputs, confirm two non-negotiables with the user:

**HR-2 — Never crash on missing assets.** Consumer code must fall back visibly (vanilla bee texture, generic icon, silence) and log a WARNING if the asset file is absent. If consumer code doesn't handle this yet, fix it before wiring the new asset.

**HR-5 — Naming rules.** Asset filenames must be:
- **Lowercase only** — no `ForestBee.png`, no `Forest_Bee.png`
- **Underscores** as word separators — no hyphens, no spaces
- **No platform separators** — forward slash only in paths
- Valid: `forest_bee.png`, `honey_comb.png`, `genetic_apiary.png`
- Invalid: `ForestBee.png`, `forest-bee.png`, `Forest Bee.png`

If the user's proposed ID violates these rules, correct it before continuing.

---

## Step 1 — Collect inputs

Ask the user in one message. Only ask fields relevant to the asset type:

1. **Asset type**: `bee_sprite` | `item_icon` | `block_texture` | `block_model` | `gui` | `sound` | `lang_key`
2. **ID** (snake_case): e.g. `forest_bee`, `honey_comb`, `genetic_apiary`
3. **Distinctive trait**: one sentence — what makes this visually/sonically unique, or the game event it represents
4. **Palette** *(skip for `sound`, `block_model`, `lang_key`)*: 3–5 hex codes OR describe colors in words
5. **Mood/character** *(bee sprites only)*: calm / busy / regal / wild / mysterious / ancient / aggressive
6. **Canvas size** *(gui only)*: WIDTH×HEIGHT in pixels, e.g. `256×168`
7. **Face** *(block_texture only)*: `top` | `side` | `bottom` — generate once per face

If user already provided any of these, skip asking for those.

---

## Step 1b — Consumer code pre-check

Before generating, ask:

> "Does the consumer code (renderer, model JSON, screen constant, species→texture map, or sounds.json entry) already exist for this asset?"

- **Yes** → proceed.
- **No** → advise the user to ship a labeled placeholder first (guidelines §4.2 step 1). Do not commission final art ahead of consumer code. Remind them to tag it `DEV-PLACEHOLDER`.

---

## Step 2 — Suggest palette if needed *(pixel-art types only)*

Skip for `sound`, `block_model`, and `lang_key`.

If user gave colors in words, convert to 3–5 hex codes:
- High contrast between body color, accent, and eye color
- Avoid colors already associated with vanilla bee (`#F9A825` yellow, `#1A1A1A` stripe)
- Pick saturated, readable tones (Minecraft palette tends toward earthy/vivid)
- Always include one dark shade for outlines/stripes (`#1A1A1A` to `#3D3D3D`)

Show suggested palette and ask "OK to use this palette?" before continuing.

---

## Step 3 — Generate the prompt

### For `bee_sprite` (64×64 atlas)

```
[PROMPT START]
Minecraft pixel art sprite sheet. Canvas: 64×64 pixels, transparent background, zero anti-aliasing, zero gradients, zero sub-pixel rendering. Every pixel must be exactly one of the palette colors listed below.

Subject: a bee named [SPECIES_ID]. [DISTINCTIVE_TRAIT]. Mood: [MOOD].

Palette (STRICT — use NO other colors):
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

Style: Minecraft Java Edition texture pack, 16-bit pixel art era. Readable silhouette from 4-block distance. Character visible in wing posture and eye shape.

AVOID: photorealism, Productive Bees art style, Forestry mod art style, rounded shapes, outlines thicker than 1 pixel, any color not in the palette above.

TIP: I will upload the vanilla Minecraft bee texture (bee.png) as a reference image. Match its UV layout exactly — only change colors, body shape, and distinctive markings.
[PROMPT END]
```

**Important:** Tell the user — upload the vanilla bee texture file in ChatGPT alongside this prompt. Path in a default Minecraft install: `.minecraft/versions/<version>/assets/minecraft/textures/entity/bee/bee.png`. This is the single most important thing for UV alignment.

**Wiring reminder:** After placing the PNG at its target path, the user must also update the **species→texture map** in the NeoForge platform layer so the renderer picks it up. Without this step, the species will still render with the fallback texture.

### For `item_icon` (16×16)

```
[PROMPT START]
Minecraft pixel art item icon. Canvas: 16×16 pixels, transparent background, zero anti-aliasing, zero gradients. Every pixel must be exactly one of the palette colors listed below.

Subject: [ITEM_ID] — [DISTINCTIVE_TRAIT].

Palette (STRICT — use NO other colors):
- [#HEX1] — [role]
- [#HEX2] — [role]
- [#HEX3] — [role, e.g. "outline"]

Style: Minecraft Java Edition item sprite, isometric-leaning pixel art. Single item, centered, occupying ~80% of canvas. No drop shadow.

AVOID: photorealism, more than 5 colors total, outlines thicker than 1 pixel, gradients.
[PROMPT END]
```

### For `block_texture` (16×16, [FACE] face)

Generate once per face. Run the `block_model` flow after all face PNGs are ready.

```
[PROMPT START]
Minecraft pixel art block face texture. Canvas: 16×16 pixels, tileable (edges must connect seamlessly when tiled), transparent background replaced with solid [DOMINANT_COLOR], zero anti-aliasing.

Subject: [BLOCK_ID] — [DISTINCTIVE_TRAIT]. This texture is for the [top/side/bottom] face.

Palette (STRICT):
- [#HEX1] — [role]
- [#HEX2] — [role]
- [#HEX3] — [role]

Style: Minecraft Java Edition block texture. Subtle surface variation, natural-looking pixel noise. Avoid perfectly uniform fills.

AVOID: photorealism, more than 5 colors, gradients, non-tileable edges.
[PROMPT END]
```

### For `block_model` (Blockbench)

Block models and blockstates require Blockbench, not AI image generation. Output a commissioning brief:

```
[BLOCKBENCH BRIEF START]
Block ID: [BLOCK_ID]
Model path:      neoforge/src/main/resources/assets/curiousbees/models/block/[BLOCK_ID].json
Blockstate path: neoforge/src/main/resources/assets/curiousbees/blockstates/[BLOCK_ID].json
Texture paths:   neoforge/src/main/resources/assets/curiousbees/textures/block/[BLOCK_ID]/{top,side,bottom}.png

Notes:
- Match vanilla block scale (1×1×1 unless explicitly a multi-block)
- Generate face PNGs first using the `block_texture` prompt type
- If directional: add `facing` variants to blockstate JSON
[BLOCKBENCH BRIEF END]
```

### For `gui` ([WIDTH]×[HEIGHT])

```
[PROMPT START]
Minecraft GUI panel background texture. Canvas: [WIDTH]×[HEIGHT] pixels, zero anti-aliasing, pixel art style.

Purpose: [GUI_ID] — [DISTINCTIVE_TRAIT].

Palette:
- [#HEX1] — panel background
- [#HEX2] — border/frame
- [#HEX3] — slot highlight
- [#HEX4] — dark inset areas

Style: Minecraft Java Edition GUI, similar to vanilla crafting table or furnace interface. Clear slot zones visible. Border has subtle pixel bevel. No gradients. Match the genetic apiary's visual language — all Curious Bees screens must share one GUI style.

AVOID: modern flat UI, rounded corners, drop shadows, any style not matching vanilla Minecraft GUI language.
[PROMPT END]
```

### For `sound` (OGG)

Sounds are not generated by GPT-4o Images. Output a commissioning brief:

```
[SOUND BRIEF START]
Sound ID: [SOUND_ID]
Target path: neoforge/src/main/resources/assets/curiousbees/sounds/[SOUND_ID].ogg

Purpose: [DISTINCTIVE_TRAIT — what game event triggers this sound]

Spec:
- Format: OGG Vorbis, mono, 44.1 kHz
- Duration: short loop ≤2s preferred; one-shot ≤1s
- Volume: non-grating at default Minecraft ambient volume
- Must be distinct from vanilla bee sounds

Negative: no music, no voice, nothing grating at looped volume.

Sources to explore: freesound.org (CC0), Minecraft SFX style reference.
[SOUND BRIEF END]
```

Also remind user to add an entry to `sounds.json`:
```json
"curiousbees:[SOUND_ID]": {
  "category": "ambient",
  "sounds": [{ "name": "curiousbees:[SOUND_ID]", "volume": 0.8 }]
}
```

### For `lang_key`

Lang keys are JSON entries, not image assets. Output a snippet and rules:

```json
// In: neoforge/src/main/resources/assets/curiousbees/lang/en_us.json
"[NAMESPACE].[TYPE].[ID]": "[Human-readable English label]"
```

Key format by type: `item.curiousbees.<id>`, `block.curiousbees.<id>`, `entity.curiousbees.<id>`, `gui.curiousbees.<id>`.

Rules:
- English (`en_us.json`) is required; other locales optional.
- Missing keys → WARNING logged + key name shown as fallback (HR-2).
- **Never expose internal allele IDs in player-facing labels.**

---

## Step 4 — Output the asset manifest entry

After the prompt/brief block, output the filled §6 template from `docs/asset-generation-guidelines.md`.

Correct target paths by type:

| Type | Target path |
|------|------------|
| `bee_sprite` | `neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/<species_id>.png` |
| `item_icon` | `neoforge/src/main/resources/assets/curiousbees/textures/item/<item_id>.png` |
| `block_texture` | `neoforge/src/main/resources/assets/curiousbees/textures/block/<block_id>/<face>.png` |
| `block_model` | `neoforge/src/main/resources/assets/curiousbees/models/block/<block_id>.json` |
| `gui` | `neoforge/src/main/resources/assets/curiousbees/textures/gui/<screen_id>.png` |
| `sound` | `neoforge/src/main/resources/assets/curiousbees/sounds/<sound_id>.ogg` |
| `lang_key` | `neoforge/src/main/resources/assets/curiousbees/lang/en_us.json` |

```
Asset:        [id]
Target path:  [see table above — use the full path]
Size:         [e.g. 64x64 / 16x16 / N seconds OGG mono / N keys]
Style notes:  pixel art, vanilla Minecraft aesthetic  (omit for sounds/lang)
Palette:      [list hex codes — omit for sound, block_model, lang_key]
References:   vanilla bee (bee.png), vanilla Minecraft [item/block] style
Negative:     no photorealism, no Productive Bees clone, no Forestry clone, no gradients
Status:       PENDING / IN-REVIEW / FINAL  ← delete inapplicable
Source:       GPT-4o Images — prompt v1 (generated by /bee-asset-prompt)
License:      mod-internal  (change to CC0 or other if externally sourced)
```

---

## Step 5 — Iteration tips *(pixel-art types)*

- **Wrong colors**: add "IMPORTANT: ONLY use these exact hex codes: [list]" at the very top of the prompt
- **Wrong UV layout (bee sprite)**: upload vanilla bee.png as reference image — fixes 90% of layout issues
- **Too modern**: add "reference: Minecraft Java Edition 1.16 art style, NOT modded texture packs"
- **Palette straying**: reduce to 3 colors max and be explicit about which part uses which color
- **Item icon too complex**: add "simple iconic shape, minimal detail, readable at 16x16"
- **Block texture not tileable**: add "verify all four edges tile seamlessly — left edge matches right edge, top matches bottom"
- **GUI doesn't match apiary style**: upload a screenshot of the genetic apiary GUI as a reference image

---

## Step 6 — After committing

Definition of done (from guidelines §4.3):

1. File at the correct path with the correct snake_case name.
2. Consumer code wired: model JSON, screen constant, lang key, species→texture map, sounds.json as applicable.
3. `DEV-PLACEHOLDER` tag removed for that asset — run `rg DEV-PLACEHOLDER` to confirm scope.
4. **Test multiplayer**: texture/atlas issues often only surface on a dedicated server or when mods reorder registries.
5. License / source noted in the PR description if AI-generated or externally sourced.
6. **Commit prefix**: use `client:` for asset files + screen wiring, `neoforge:` for block/item registration, `docs:` only if updating these guidelines.

**Pre-release sweep** — before any release tag:
```
rg DEV-PLACEHOLDER
```
Any hit in an area whose phase is marked complete in `docs/roadmap.md` must be fixed or the release downgraded. Old / unused assets must be removed — orphan textures bloat the jar.

**Tagging placeholders** — when committing art that isn't final yet:
1. Add `DEV-PLACEHOLDER` in the commit **or** the code constant/model `_comment` (any form a single grep can find).
2. Follow the process in `docs/asset-generation-guidelines.md §4`.
3. Remove the tag only after the final reviewed version lands.
