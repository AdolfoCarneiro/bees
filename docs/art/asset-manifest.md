# Asset Manifest

> Tracks all required visual assets for Curious Bees, their prompt status, and integration state.
>
> Status values: `prompt-needed` | `prompt-created` | `generated` | `integrated` | `validated`
>
> Dev placeholders are listed as `prompt-created` — not `integrated` — until the final asset replaces them.

## Entity Textures — Bee Skins

| Asset | Type | Size | Prompt | Target Path | Status |
|---|---|---|---|---|---|
| Meadow Bee | entity texture | 64×32 | [textures-entity-bee-meadow.md](prompts/species/textures-entity-bee-meadow.md) | `textures/entity/bee/meadow.png` | `integrated` |
| Forest Bee | entity texture | 64×32 | [textures-entity-bee-forest.md](prompts/species/textures-entity-bee-forest.md) | `textures/entity/bee/forest.png` | `integrated` |
| Arid Bee | entity texture | 64×32 | [textures-entity-bee-arid.md](prompts/species/textures-entity-bee-arid.md) | `textures/entity/bee/arid.png` | `integrated` |
| Cultivated Bee | entity texture | 64×32 | [textures-entity-bee-cultivated.md](prompts/species/textures-entity-bee-cultivated.md) | `textures/entity/bee/cultivated.png` | `integrated` |
| Hardy Bee | entity texture | 64×32 | [textures-entity-bee-hardy.md](prompts/species/textures-entity-bee-hardy.md) | `textures/entity/bee/hardy.png` | `integrated` |
| Fallback Bee | entity texture | 64×32 | [textures-entity-bee-fallback.md](prompts/species/textures-entity-bee-fallback.md) | `textures/entity/bee/fallback.png` | `prompt-created` |

## Block Textures — Bee Nests

| Asset | Type | Size | Prompt | Target Path | Status |
|---|---|---|---|---|---|
| Meadow Bee Nest | block texture | 16×16 | [meadow_hive.md](prompts/hives/meadow_hive.md) | `textures/block/meadow_bee_nest_*.png` | `integrated` |
| Forest Bee Nest | block texture | 16×16 | [forest_hive.md](prompts/hives/forest_hive.md) | `textures/block/forest_bee_nest_*.png` | `integrated` |
| Arid Bee Nest | block texture | 16×16 | [arid_hive.md](prompts/hives/arid_hive.md) | `textures/block/arid_bee_nest_*.png` | `integrated` |

## GUI Backgrounds

| Asset | Type | Size | Prompt | Target Path | Status |
|---|---|---|---|---|---|
| Bee Analyzer Screen | GUI bg | 220×148 | [analyzer-screen-bg.md](prompts/gui/analyzer-screen-bg.md) | `textures/gui/analyzer_bg.png` | `prompt-created` |
| Genetic Beehive Screen | GUI bg | 176×166 | [genetic-apiary-bg.md](prompts/gui/genetic-apiary-bg.md) | `textures/gui/genetic_apiary_bg.png` | `prompt-created` |

## Item Icons

| Asset | Type | Size | Prompt | Target Path | Status |
|---|---|---|---|---|---|
| Bee Analyzer | item icon | 16×16 | — | `textures/item/bee_analyzer.png` | `integrated` |
| Meadow Comb | item icon | 16×16 | — | `textures/item/meadow_comb.png` | `integrated` |
| Forest Comb | item icon | 16×16 | — | `textures/item/forest_comb.png` | `integrated` |
| Arid Comb | item icon | 16×16 | — | `textures/item/arid_comb.png` | `integrated` |
| Cultivated Comb | item icon | 16×16 | — | `textures/item/cultivated_comb.png` | `integrated` |
| Hardy Comb | item icon | 16×16 | — | `textures/item/hardy_comb.png` | `integrated` |
| Basic Frame | item icon | 16×16 | `prompt-needed` | `textures/item/basic_frame.png` | `prompt-needed` |
| Mutation Frame | item icon | 16×16 | `prompt-needed` | `textures/item/mutation_frame.png` | `prompt-needed` |
| Productivity Frame | item icon | 16×16 | `prompt-needed` | `textures/item/productivity_frame.png` | `prompt-needed` |

## Templates

| Asset | Notes | Path | Status |
|---|---|---|---|
| Default Bee UV Template | Required for all entity textures | `templates/bee/default_bee_uv_template.png` | `integrated` |

---

## Summary (as of last update)

| Category | Total | integrated | prompt-created | prompt-needed |
|---|---|---|---|---|
| Entity textures | 6 | 5 | 1 | 0 |
| Block textures | 3 | 3 | 0 | 0 |
| GUI backgrounds | 2 | 0 | 2 | 0 |
| Item icons | 9 | 6 | 0 | 3 |
| Templates | 1 | 1 | 0 | 0 |

Run `python tools/check-assets.py` to verify which file paths actually exist on disk.
