# 02 — MVP Asset List and Placeholder Strategy

## 1. Purpose

This document lists the assets likely needed for the first playable MVP and defines which ones can be placeholders.

## 2. MVP Asset Philosophy

The MVP should prove gameplay first.

Polished art is not required to validate:

```text
genome storage
wild bee initialization
vanilla breeding inheritance
mutation
analyzer output
basic production calculation
```

## 3. Required Asset Levels

### Level 0 — No Asset Required

Use for:

```text
genetics core
content definitions
breeding services
mutation services
debug commands
```

### Level 1 — Placeholder Asset Required

Use for:

```text
Bee Analyzer item
MVP comb/product items
debug-only helper items if needed
```

### Level 2 — Basic Custom Asset

Use for:

```text
items that players interact with regularly
comb/product items once production is playable
```

### Level 3 — Blockbench / Custom Model

Use for:

```text
Genetic Apiary
Centrifuge
Frame Housing
other custom machines
```

## 4. MVP Item Assets

| Asset ID | Type | Phase | Required? | Placeholder? | Notes |
|---|---|---:|---|---|---|
| bee_analyzer | item texture | 5 | yes | yes | Can be flat 16x16. |
| meadow_comb | item texture | 6 | likely | yes | May use recolored comb placeholder. |
| forest_comb | item texture | 6 | likely | yes | May use recolored comb placeholder. |
| arid_comb | item texture | 6 | likely | yes | May use recolored comb placeholder. |
| cultivated_comb | item texture | 6 | likely | yes | May use recolored comb placeholder. |
| hardy_comb | item texture | 6 | likely | yes | May use recolored comb placeholder. |
| wax | item texture | 6 | optional | yes | Can be added later if production outputs require it. |

## 5. Future Block Assets

| Asset ID | Type | Phase | Required? | Placeholder? | Tool |
|---|---|---:|---|---|---|
| genetic_apiary | block model/texture | 7 | yes, if apiary implemented | yes | Blockbench recommended |
| centrifuge | block model/texture | 7+ | optional | yes | Blockbench recommended |
| frame_housing | block model/texture | 7+ | optional | yes | Blockbench recommended |

## 6. Frame Item Assets

| Asset ID | Type | Phase | Required? | Placeholder? |
|---|---|---:|---|---|
| basic_frame | item texture | 7 | yes, if frames implemented | yes |
| mutation_frame | item texture | 7 | yes, if frames implemented | yes |
| productivity_frame | item texture | 7 | yes, if frames implemented | yes |
| stability_frame | item texture | 7 | optional | yes |

## 7. Naming Rules

Use lowercase snake_case.

Good:

```text
bee_analyzer
meadow_comb
forest_comb
genetic_apiary
basic_frame
```

Bad:

```text
BeeAnalyzer
MeadowComb
genetic-apiary
apiaryBlockFinal2
```

## 8. Runtime Paths

Assuming namespace `<modid>`:

```text
assets/<modid>/textures/item/bee_analyzer.png
assets/<modid>/models/item/bee_analyzer.json
assets/<modid>/textures/item/meadow_comb.png
assets/<modid>/models/item/meadow_comb.json
assets/<modid>/textures/block/genetic_apiary.png
assets/<modid>/models/block/genetic_apiary.json
assets/<modid>/blockstates/genetic_apiary.json
```

## 9. Placeholder Acceptance Criteria

A placeholder is acceptable if:

```text
- it loads in-game;
- it is recognizable enough for testing;
- it has the final runtime path;
- it has a clear source note or tracking task;
- replacing it later does not require code changes.
```

A placeholder is not acceptable if:

```text
- it breaks model loading;
- it uses inconsistent namespaces;
- it causes missing texture errors;
- it requires code changes when replaced;
- it is confused with final art in release notes.
```

## 10. Asset Backlog Tasks

Suggested future tasks:

```text
TASK — Define final mod id and resource namespace
TASK — Create placeholder Bee Analyzer item texture
TASK — Create placeholder MVP comb textures
TASK — Validate item model JSON and lang keys
TASK — Define Genetic Apiary visual brief
TASK — Create Genetic Apiary Blockbench source model
TASK — Export Genetic Apiary Java block model
TASK — Validate Genetic Apiary model in-game
```

## 11. Do Not Do Yet

Do not create polished assets for:

```text
resource bees
Nether bees
End bees
magic bees
industrial bees
compatibility bees
advanced machines
large GUI screens
```

until the related gameplay exists.
