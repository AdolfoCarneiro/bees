---
name: bee-asset-prompt
description: Generate ultra-detailed GPT-4o Images prompts for Curious Bees mod assets (bee sprites, items, blocks, GUI)
type: project-skill
---

# Bee Asset Prompt Generator

Generates ready-to-paste prompts for GPT-4o Images (ChatGPT) to create Curious Bees mod assets. Zero drawing required — paste the output directly into ChatGPT.

## Step 1 — Collect inputs

Ask the user (in one message, all at once):

1. **Asset type**: `bee_sprite` | `item_icon` | `block_texture` | `gui`
2. **ID** (snake_case): e.g. `forest_bee`, `honey_comb`, `genetic_apiary`
3. **Distinctive visual trait**: one sentence describing what makes this visually unique
4. **Palette**: 3–5 hex codes, OR describe colors in words and the skill will suggest hex codes
5. **Mood/character** (bee sprites only): calm / busy / regal / wild / mysterious / ancient / aggressive

If user already provided any of these in the invoking message, skip asking for those.

---

## Step 2 — Suggest palette if needed

If user gave colors in words, convert to 3–5 hex codes following these rules:
- High contrast between body color, accent, and eye color
- Avoid colors already associated with vanilla bee (`#F9A825` yellow, `#1A1A1A` stripe)
- Pick saturated, readable tones (Minecraft palette tends toward earthy/vivid)
- Always include one dark shade for outlines/stripes (`#1A1A1A` to `#3D3D3D`)

Show suggested palette before generating the prompt and ask "OK to use this palette?" before continuing.

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

**Important:** Tell the user — in ChatGPT, upload the vanilla bee texture file alongside this prompt. Path in a default Minecraft install: `.minecraft/versions/<version>/assets/minecraft/textures/entity/bee/bee.png`. This is the single most important thing for UV alignment.

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

### For `block_texture` (16×16, each face)

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

### For `gui` (variable size)

```
[PROMPT START]
Minecraft GUI panel background texture. Canvas: [WIDTH]×[HEIGHT] pixels, zero anti-aliasing, pixel art style.

Purpose: [GUI_ID] — [DISTINCTIVE_TRAIT].

Palette:
- [#HEX1] — panel background
- [#HEX2] — border/frame
- [#HEX3] — slot highlight
- [#HEX4] — dark inset areas

Style: Minecraft Java Edition GUI, similar to vanilla crafting table or furnace interface. Clear slot zones visible. Border has subtle pixel bevel. No gradients.

AVOID: modern flat UI, rounded corners, drop shadows, any style not matching vanilla Minecraft GUI language.
[PROMPT END]
```

---

## Step 4 — Output the asset manifest entry

After the prompt block, always output the filled §6 template from `docs/asset-generation-guidelines.md`:

```
Asset:        [id]
Target path:  assets/curiousbees/textures/[category]/[file].png
Size:         [e.g. 64x64 / 16x16]
Style notes:  pixel art, vanilla Minecraft aesthetic
Palette:      [list hex codes]
References:   vanilla bee (bee.png), vanilla Minecraft [item/block] style
Negative:     no photorealism, no Productive Bees clone, no Forestry clone, no gradients
Status:       PENDING
Source:       GPT-4o Images — prompt v1 (generated by /bee-asset-prompt)
License:      mod-internal
```

---

## Step 5 — Iteration tips

Tell the user:

- If result has wrong colors: add "IMPORTANT: ONLY use these exact hex codes: [list]" at the very top of the prompt
- If UV layout is wrong (bee sprite): upload vanilla bee.png as reference image — this fixes 90% of layout issues
- If style looks too "modern": add "reference: Minecraft Java Edition 1.16 art style, NOT modded texture packs"
- If palette strays: reduce to 3 colors max and be more explicit about which part uses which color
- For item icons: if result is too complex, add "simple iconic shape, minimal detail, readable at 16x16"

---

## DEV-PLACEHOLDER reminder

When user commits the generated asset before it's final, remind them to:
1. Name it with `DEV-PLACEHOLDER` in the commit or code comment
2. Follow the process in `docs/asset-generation-guidelines.md §4`
3. Remove the tag only after the final reviewed version lands
