# Species Authoring Guide

> This guide explains how to add a new bee species to Curious Bees — both as a third-party
> addon and as part of the built-in mod content. It covers data definitions, assets, and
> localization. Read this before creating any new species.

## What Makes a Species Complete

A species is **not** complete just because it has a JSON definition. A complete species needs:

| Artifact | Required | Notes |
|---|---|---|
| Species definition JSON | ✅ | genetic id, dominance, default traits, visual ref |
| Entity texture PNG | ✅ | 64×32 px, UV-mapped, original artwork |
| Localization entry (`en_us.json`) | ✅ | display name |
| Production definition JSON | ✅ if produces anything | comb/output reference |
| Mutation entry JSON | ✅ | at least one way to obtain it |
| Progression role notes | recommended | brief design rationale |
| Asset prompt doc | ✅ if texture pending | under `docs/art/prompts/species/` |

A species without a texture is treated as incomplete — it will render with the fallback texture
and log a warning. A species without a mutation or spawn path is unreachable in survival.

## Repository Layout

```
# Data (loaded via NeoForge datapack system)
data/<namespace>/curious_bees/species/<species_slug>.json
data/<namespace>/curious_bees/mutations/<mutation_id>.json
data/<namespace>/curious_bees/production/<species_slug>_production.json
data/<namespace>/curious_bees/traits/<trait_id>.json     # if adding new traits

# Assets (client-side resource pack)
assets/<namespace>/textures/entity/bee/<species_slug>.png
assets/<namespace>/lang/en_us.json

# Future optional paths (not required for default model)
assets/<namespace>/models/entity/bee/<model_id>.json      # custom geometry only
assets/<namespace>/textures/item/<species_slug>_comb.png  # if comb has unique art
assets/<namespace>/models/item/<species_slug>_comb.json   # if item model needed
```

For built-in Curious Bees species, `<namespace>` is `curiousbees`.

## Data Pack vs Resource Pack

| Content | Pack type | Why |
|---|---|---|
| Species genetics, traits, mutations, production | Data pack | server-authoritative, reloadable |
| Textures, models, lang | Resource pack | client-side, visual |

A third-party species mod should ship **both** a data pack (under `data/`) and a resource pack
(under `assets/`) in the same jar or zip.

## Species JSON Format

See `docs/templates/species-template.json` for the full template.

Minimal required shape:

```json
{
  "id": "my_pack:species/silverleaf",
  "displayName": "Silverleaf Bee",
  "dominance": "RECESSIVE",
  "defaultTraits": {
    "LIFESPAN":     "curious_bees:traits/lifespan/normal",
    "PRODUCTIVITY": "curious_bees:traits/productivity/normal",
    "FERTILITY":    "curious_bees:traits/fertility/two",
    "FLOWER_TYPE":  "curious_bees:traits/flower_type/flowers"
  },
  "visual": "my_pack:textures/entity/bee/silverleaf.png"
}
```

`visual` is optional but strongly recommended. Without it the species uses the fallback texture.

## Mutation JSON Format

See `docs/templates/mutation-template.json`.

Every species needs at least one mutation or a natural spawn path. Mutation-only species
(no wild hive) must be reachable through at least one mutation entry.

```json
{
  "id": "my_pack:mutations/silverleaf_from_meadow_forest",
  "parents": ["curious_bees:species/meadow", "curious_bees:species/forest"],
  "result": "my_pack:species/silverleaf",
  "baseChance": 0.08,
  "resultModes": { "partialChance": 1.0, "fullChance": 0.0 }
}
```

## Production JSON Format

See `docs/templates/production-template.json`.

Only needed if the species produces outputs. Species without production produce nothing in the
Genetic Apiary.

```json
{
  "species": "my_pack:species/silverleaf",
  "primaryOutputs": [
    { "item": "my_pack:silverleaf_comb", "chance": 0.65 }
  ]
}
```

## Entity Texture Requirements

- Size: **64×32 pixels** — vanilla bee UV layout
- Format: **PNG with alpha** — transparent outside UV regions
- Must follow the vanilla bee UV template: `docs/art/templates/bee/default_bee_uv_template.png`
- Path: `assets/<namespace>/textures/entity/bee/<species_slug>.png`

**Do not copy textures from Productive Bees, Forestry, Complicated Bees, or other mods.**
Create original artwork. AI-assisted drafts are allowed but must be converted to Minecraft style.

If your texture is not ready yet, create an asset prompt under `docs/art/prompts/species/`
following the template in `docs/art/asset-prompt-workflow.md`.

## Localization

Add a display name entry to `en_us.json`:

```json
{
  "species.curiousbees.silverleaf": "Silverleaf Bee"
}
```

For built-in species the key pattern is `species.curiousbees.<slug>`.
For third-party species use your namespace: `species.<namespace>.<slug>`.

## Adding a Species Step by Step

1. Write a one-paragraph design brief: what is this bee's role in the progression?
2. Define its genetic traits (dominance, lifespan, productivity, etc.).
3. Define its mutation path (which parent pair produces it).
4. Define its production (what comb or output it produces).
5. Define its visual direction (palette, feel, biome identity).
6. Create or commission the entity texture (64×32 px, original).
7. Create the species JSON with the `visual` field pointing to the texture.
8. Add the mutation JSON entry.
9. Add the production JSON entry.
10. Add the localization key.
11. Test in-game: analyzer shows correct species, apiary shows correct production.
12. Ensure no assets are placeholders before releasing.

## What NOT to Do

- Do not add a species just to have a new output item.
- Do not copy species ideas, mutation trees, or textures from other mods.
- Do not skip the visual step and commit a placeholder as final art.
- Do not add resource bees (iron, gold, diamond) — these are future content.
- Do not add lifecycle/larvae/queen mechanics — these are out of scope.

## Resource Bees

Resource bees (Iron Bee, Gold Bee, etc.) are **not** part of the current content expansion scope.
They are reserved for a dedicated future content expansion phase with proper design, progression
balance, and asset work. Do not add them now.

## See Also

- `docs/templates/` — JSON templates for species, mutation, production
- `docs/art/asset-prompt-workflow.md` — how to create asset prompts
- `docs/art/templates/bee/default_bee_uv_template.png` — UV template for entity textures
- `docs/content-authoring-guide.md` — data format reference with field descriptions
- `docs/art/asset-manifest.md` — status of all required assets
