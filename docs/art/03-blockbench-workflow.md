# 03 — Blockbench Workflow

## 1. Purpose

This document defines how Blockbench should be used for Curious Bees.

Blockbench is not required for the early genetics phases. It becomes useful when the project needs custom block models, machine models, or more complex item models.

## 2. When to Use Blockbench

Use Blockbench for:

```text
Genetic Apiary block
Centrifuge block
Frame Housing block
custom machine-like blocks
special 3D item models
```

Do not use Blockbench for:

```text
pure genetics code
trait definitions
mutation definitions
simple flat item icons
debug-only commands
```

## 3. Source File Policy

Always keep the `.bbmodel` source file.

Recommended source path:

```text
assets-source/blockbench/<asset_id>.bbmodel
```

Example:

```text
assets-source/blockbench/genetic_apiary.bbmodel
```

Exported runtime files should go into the mod resources:

```text
src/main/resources/assets/<modid>/models/block/genetic_apiary.json
src/main/resources/assets/<modid>/textures/block/genetic_apiary.png
src/main/resources/assets/<modid>/blockstates/genetic_apiary.json
```

## 4. Blockbench Project Type

For Minecraft Java block assets, prefer the Minecraft Java block/item model workflow.

Do not create complex generic models if the target is a normal Java block model.

## 5. Model Design Rules

Curious Bees should visually feel:

```text
vanilla-adjacent
tech-leaning
warm/wooden/industrial
bee/apiculture themed
readable at Minecraft scale
```

Avoid:

```text
overly high-poly models
hyper-realistic textures
too much visual noise
large animations before gameplay is stable
models that look unrelated to bees/apiculture
```

## 6. Genetic Apiary Visual Brief

Possible direction:

```text
wooden/bronze apiary box
glass inspection slot
subtle honey/wax accents
small frame slots visible on side/top
not too modern/sci-fi
```

Color/material ideas:

```text
oak/dark oak wood
wax/honey amber
copper/bronze trim
dark inset slots
small bee emblem
```

## 7. Export Checklist

Before committing exported files:

```text
- .bbmodel source is saved in assets-source/blockbench/
- exported model JSON is in assets/<modid>/models/block/
- texture PNG is in assets/<modid>/textures/block/
- blockstate JSON references the correct model
- model JSON references the correct texture namespace
- lang entry exists
- item form exists if block is placeable
- model loads in-game without missing texture
```

## 8. Common Issues to Avoid

### Wrong namespace

Bad:

```json
"texture": "minecraft:block/genetic_apiary"
```

Good:

```json
"texture": "<modid>:block/genetic_apiary"
```

### Wrong texture path

Bad:

```text
textures/items/
```

Good:

```text
textures/item/
```

### Missing block item model

A block may place correctly but show incorrectly in inventory if the item model is missing.

### Source file not committed

Do not only commit exported JSON.

Commit the `.bbmodel` source too.

## 9. Claude Code Role in Blockbench Workflow

Claude Code can help with:

```text
creating model/blockstate/lang JSON
checking paths
renaming files consistently
adding registry entries
adding placeholder models
reviewing exported JSON structure
```

Claude Code should not be the only judge of whether the model looks good.

## 10. Cursor Role in Blockbench Workflow

Cursor can help with:

```text
manual editing
diff review
path search
quick fixes
JSON validation
resource tree navigation
```

## 11. MCP Role in Blockbench Workflow

A Blockbench MCP integration may be useful later to:

```text
inspect open Blockbench projects
export files
apply repetitive transformations
generate simple model variations
validate model metadata
```

However, do not require it for the MVP.

Before using MCP, verify:

```text
what tools it exposes
what filesystem access it has
whether it can execute scripts
whether it can overwrite files
whether the results can be reviewed in git
```

## 12. Blockbench Workflow Task Template

```text
TASK — Create [asset name] Blockbench source model

Objective:
Create a source Blockbench model for [asset].

Scope:
- create .bbmodel source;
- create/export Java model JSON;
- create or reference texture;
- ensure paths follow project namespace.

Non-goals:
- no registry changes unless explicitly included;
- no unrelated assets;
- no animation unless required.

Acceptance Criteria:
- .bbmodel source exists;
- exported model JSON exists;
- texture references are correct;
- asset loads in-game;
- no missing texture.
```
