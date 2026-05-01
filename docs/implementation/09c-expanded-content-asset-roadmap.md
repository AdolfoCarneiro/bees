# 09C - Expanded Content Asset Roadmap

## Status

Complete.

## Scope

This document completes Task 9.5 from `docs/implementation/09-expanded-content-roadmap.md`.

It defines how assets should be planned for expanded content after the MVP and Phase 8 data-driven content pipeline.

This is planning only.

## Non-Goals

Do not use this task to:

- generate item textures;
- generate block models;
- create Blockbench files;
- add new asset folders unless needed by a concrete asset task;
- implement expanded species;
- implement resource bees;
- introduce MCP automation as a requirement.

## Core Rule

Expanded content assets must follow the existing project principle:

```text
Gameplay comes first.
Assets support implementation.
Assets do not block core systems.
```

Placeholder assets remain acceptable until the related gameplay is stable and ready for visual polish.

## Asset Levels For Expanded Content

### Level 0 - No Asset Required

Use for:

```text
planning docs
species design docs
mutation tree drafts
validation tests
content JSON examples
debug-only workflows
```

No runtime assets should be added for pure planning work.

### Level 1 - Placeholder Item Asset

Use for:

```text
new comb items
new wax/byproduct items
simple analyzer/guide icons
placeholder compatibility byproducts
```

Rules:

- use final runtime paths from the start;
- keep textures simple and clearly temporary;
- use flat generated item models unless a 3D model is required;
- do not polish before gameplay is validated.

### Level 2 - Basic Custom Item Art

Use for:

```text
player-facing comb families
resource foundation byproducts
important progression materials
late-game symbolic items
```

Rules:

- use consistent 16x16 item style;
- keep shape language readable at inventory scale;
- keep naming lowercase snake_case;
- avoid one-off visual styles that cannot scale.

### Level 3 - Blockbench / Custom Block Model

Use for:

```text
new machines
processing blocks
advanced apiary variants
frame housings
centrifuge-like blocks
special display blocks
```

Rules:

- commit the `.bbmodel` source;
- export Java-compatible model JSON;
- verify texture paths and namespace;
- test in inventory and world before calling it done.

## Expanded Content Asset Categories

### Biome / Managed / Harsh Environment Bees

Expected assets:

```text
comb item icons
optional byproduct item icons
no bee entity model changes by default
```

Placeholder approach:

```text
recolored or patterned comb placeholders
simple flat item models
```

Polish trigger:

```text
when the branch is playable and mutation/progression balance is acceptable
```

### Resource Foundation Bees

Expected assets:

```text
foundation combs
mineral/metallic/crystalline byproducts
possibly processing materials
```

Placeholder approach:

```text
simple comb icons with material accent colors
simple generated item models
```

Polish trigger:

```text
after resource readiness criteria are satisfied and output economy is tested
```

### Resource Bees

Expected assets:

```text
resource combs
processed resource fragments/dusts if added
optional guide/analyzer icons
```

Rules:

- do not create these before ADR-0012 readiness criteria are met;
- direct resource assets must not imply resource bees are already implemented;
- placeholder art must be clearly tracked as temporary.

### Industrial / Compatibility Bees

Expected assets:

```text
compat combs
compat byproducts
optional machine or processing icons
```

Rules:

- asset names must avoid hard dependencies in common code;
- missing optional mods must not cause missing asset problems in the base mod;
- compat assets should live with the compat implementation strategy when that exists.

### Exotic / Endgame Bees

Expected assets:

```text
distinct combs
rare byproducts
possibly special block or effect icons
```

Rules:

- visual complexity should match gameplay importance;
- do not create endgame art before earlier progression is fun and balanced.

## Source And Runtime Paths

Source assets should remain separate from exported runtime assets.

Recommended source structure:

```text
assets-source/
  blockbench/
  textures-source/
    item/
    block/
  references/
  exports/
```

Runtime NeoForge resources currently live under:

```text
neoforge/src/main/resources/assets/curiousbees/
  textures/item/
  textures/block/
  models/item/
  models/block/
  blockstates/
  lang/
```

Use the existing runtime namespace:

```text
curiousbees
```

Do not mix runtime asset namespace with data content IDs such as `curious_bees` unless a later namespace decision changes this explicitly.

## Naming Rules

Use lowercase snake_case asset IDs.

Good:

```text
resilient_comb
diligent_comb
noble_comb
industrious_comb
mineral_comb
metallic_fragment
```

Bad:

```text
ResilientComb
resource-comb-final
ironBeeComb2
temporary_good_one
```

Runtime item asset paths should match registry IDs when possible.

## Placeholder Workflow

Before adding placeholder assets:

```text
1. Confirm gameplay needs the item/block in-game.
2. Create an asset list for the specific content slice.
3. Use final runtime filenames.
4. Add model JSON and lang entries at the same time.
5. Run a build.
6. Manually verify no missing textures in-game when practical.
7. Track polish as a follow-up, not as a blocker.
```

Placeholder acceptance criteria:

- loads in-game;
- has final runtime path;
- is recognizable enough for testing;
- can be replaced without code changes;
- is not described as final art.

## Blockbench Workflow

Use Blockbench only when flat item icons are not enough.

Best candidates:

```text
machines
advanced apiary variants
centrifuge-like blocks
processing stations
display blocks
special 3D item models
```

Required review before committing a Blockbench asset:

- `.bbmodel` source is committed;
- exported JSON uses Minecraft Java model conventions;
- texture references use `curiousbees`;
- blockstate exists for placeable blocks;
- item model exists for block items;
- model is checked in-game;
- no missing texture appears.

## MCP / Automation Safety

MCP or plugin automation remains optional future tooling.

Before adopting automation:

- confirm the workflow is repeated enough to justify it;
- document what the tool can read and write;
- avoid tools that execute arbitrary scripts without review;
- keep outputs reviewable in git;
- never let automation replace visual review.

No Phase 9 expanded content depends on MCP.

## Asset Review Gates

### Gate 1 - Need Review

Before creating assets:

- does gameplay require this visible thing now?
- can a placeholder or vanilla visual unblock testing?
- would art work distract from genetics or balance?

### Gate 2 - Asset List Review

Before creating multiple assets, list:

- asset ID;
- type;
- content branch;
- placeholder/final status;
- source path;
- runtime path;
- model/lang requirements.

### Gate 3 - Integration Review

Before committing runtime assets:

- texture path is correct;
- model path is correct;
- lang key exists;
- registry ID and asset ID agree;
- build passes;
- in-game missing texture check is done when practical.

### Gate 4 - Polish Review

Before calling assets final:

- branch gameplay is stable;
- balance is acceptable;
- visual style matches vanilla-adjacent bee/apiculture theme;
- asset review checklist has been used.

## Follow-Up Tasks

1. Create an asset list only when the first expanded content branch is approved for implementation.
2. Create placeholder comb assets for that branch after gameplay definitions exist.
3. Add Blockbench briefs only for new blocks or machines.
4. Revisit MCP automation only after the same asset workflow has repeated several times.
