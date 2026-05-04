# Asset Authoring Guide

> Naming conventions, path rules, and technical requirements for Curious Bees visual assets.
> Follow these conventions when adding or replacing species textures, item icons, or GUI assets.

## Entity Textures (Bee Skins)

### Path convention

```
assets/<namespace>/textures/entity/bee/<species_slug>.png
```

Examples for built-in species:

```
assets/curiousbees/textures/entity/bee/meadow.png
assets/curiousbees/textures/entity/bee/forest.png
assets/curiousbees/textures/entity/bee/arid.png
assets/curiousbees/textures/entity/bee/cultivated.png
assets/curiousbees/textures/entity/bee/hardy.png
assets/curiousbees/textures/entity/bee/fallback.png   ← mod fallback for unknown species
```

### Technical requirements

| Property | Value |
|---|---|
| Width | 64 px |
| Height | 32 px |
| Format | PNG |
| Alpha | Required — transparent outside UV regions |
| UV layout | Vanilla Minecraft bee UV (see template below) |

### UV template

The UV template is required before generating any entity texture:

```
docs/art/templates/bee/default_bee_uv_template.png
```

Every entity texture prompt must reference this template. Do not generate textures without it.
Results that rearrange or resize UV islands must be rejected.

### Slug rules

- Use lowercase with underscores: `meadow`, `dark_forest`, `silver_leaf`
- Match the last segment of the species ID: `curiousbees:species/meadow` → `meadow.png`
- Future texture variants: `meadow_angry.png`, `meadow_nectar.png` (optional, not required)

### Fallback chain

When a texture is missing or invalid, the renderer uses this fallback order:

```
1. species texture   (assets/<ns>/textures/entity/bee/<slug>.png)
2. mod fallback      (assets/curiousbees/textures/entity/bee/fallback.png)
3. vanilla fallback  (minecraft:textures/entity/bee/bee.png)
```

Bees with no Curious Bees genome skip steps 1 and 2 and go directly to vanilla (preserving
angry/nectar texture variants).

---

## Item Textures (Combs and Frames)

### Path convention

```
assets/<namespace>/textures/item/<item_slug>.png
```

Examples:

```
assets/curiousbees/textures/item/meadow_comb.png
assets/curiousbees/textures/item/basic_frame.png
```

### Technical requirements

| Property | Value |
|---|---|
| Size | 16×16 px |
| Format | PNG with alpha |
| Style | Minecraft pixel art (no gradients, no anti-aliasing) |

### Item model paths

```
assets/<namespace>/models/item/<item_slug>.json
```

Standard items use `minecraft:item/generated` as parent and reference the item texture.

---

## Block Textures (Bee Nests, GUI Backgrounds)

### Species bee nest textures

Bee nest blocks use multi-face textures. Three standard faces:

```
assets/<namespace>/textures/block/<species>_bee_nest_top.png    — 16×16 px
assets/<namespace>/textures/block/<species>_bee_nest_side.png   — 16×16 px
assets/<namespace>/textures/block/<species>_bee_nest_front.png  — 16×16 px
```

Future: when a proper oriented nest model is created, the `_side` face is also used as the
representative texture in the habitat definition.

### GUI backgrounds

```
assets/<namespace>/textures/gui/<gui_id>_bg.png
```

GUI background sizes match the screen's `imageWidth × imageHeight` in pixels.

---

## Model Conventions

### Default bee model (shared)

All current MVP species use the vanilla bee model geometry, identified by:

```
curiousbees:bee/default
```

Custom Blockbench models are future scope. They are not required for any species by default.

### Future custom model paths

```
assets/<namespace>/models/entity/bee/<model_slug>.json
```

Blockbench source files live outside the mod jar under:

```
art/blockbench/<model_slug>.bbmodel
```

---

## Language Keys

### Species display name

```json
{ "species.<namespace>.<slug>": "My Species Bee" }
```

Example built-in:

```json
{ "species.curiousbees.meadow": "Meadow Bee" }
```

### Item display name

Standard Minecraft format (auto-generated from item registration):

```json
{ "item.<namespace>.<item_slug>": "Meadow Comb" }
```

### Block display name

```json
{ "block.<namespace>.<block_slug>": "Meadow Bee Nest" }
```

---

## Asset Prompt Workflow

When a required asset does not yet exist:

1. Create a prompt file under `docs/art/prompts/` following `docs/art/asset-prompt-workflow.md`.
2. Commit the prompt file as a project deliverable.
3. Wait for the user-provided generated asset.
4. Integrate, validate dimensions, and mark the prompt as resolved.

A dev placeholder (copy of an existing texture) may be used to prevent crashes, but it must be:
- Clearly marked as a placeholder in the model/blockstate JSON comments.
- Listed as `prompt-created` (not `integrated`) in `docs/art/asset-manifest.md`.
- Replaced before a release.

---

## See Also

- `docs/content/species-authoring-guide.md` — full species authoring workflow
- `docs/art/asset-prompt-workflow.md` — prompt template and workflow
- `docs/art/templates/bee/default_bee_uv_template.png` — UV template (required)
- `docs/art/asset-manifest.md` — current asset status tracker
