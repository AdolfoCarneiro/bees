# 01 — Asset Pipeline Plan

## 1. Purpose

This document defines how Curious Bees should plan, create, validate, and integrate assets.

It also clarifies when to use:

- placeholder textures;
- manually created icons;
- Blockbench models;
- Claude Code;
- Cursor;
- future Claude Code skills;
- future MCP integrations.

## 2. Main Decision

Assets are not part of the critical path until the project has at least:

```text
- pure Java genetics core;
- initial content definitions;
- NeoForge bee genome storage;
- vanilla breeding integration;
- basic analyzer behavior.
```

Before that, the project should use either:

```text
- no assets;
- Minecraft built-in visuals;
- temporary generated placeholder textures;
- simple item JSON models using the vanilla generated item parent.
```

## 3. Phase-by-Phase Asset Requirements

### Phase 0 — Documentation and Decisions

Required assets:

```text
None.
```

Optional docs:

```text
docs/art/*
```

### Phase 1 — Genetics Core

Required assets:

```text
None.
```

Reason:

```text
This phase is pure Java and test-only.
```

### Phase 2 — Initial Content

Required assets:

```text
None.
```

Optional:

```text
Temporary icons in design docs only.
```

Reason:

```text
Species and trait definitions do not require in-game models yet.
```

### Phase 3 — NeoForge Entity Integration

Required assets:

```text
None.
```

Optional:

```text
Debug-only command output.
```

Reason:

```text
Genome storage on vanilla Bee entities can be validated without new items or blocks.
```

### Phase 4 — Vanilla Breeding Integration

Required assets:

```text
None.
```

Optional:

```text
Particle/sound feedback can use vanilla assets.
Debug logs are enough for early validation.
```

### Phase 5 — Analyzer MVP

Required assets:

```text
One Bee Analyzer item icon, placeholder acceptable.
```

Minimal files:

```text
assets/<modid>/textures/item/bee_analyzer.png
assets/<modid>/models/item/bee_analyzer.json
assets/<modid>/lang/en_us.json
```

The item model can use:

```json
{
  "parent": "minecraft:item/generated",
  "textures": {
    "layer0": "<modid>:item/bee_analyzer"
  }
}
```

### Phase 6 — Production MVP

Required assets:

```text
Placeholder item icons for MVP combs/products.
```

Likely MVP icons:

```text
meadow_comb.png
forest_comb.png
arid_comb.png
cultivated_comb.png
hardy_comb.png
wax.png
```

These should be simple 16x16 textures.

### Phase 7 — Tech Apiary and Automation

Required assets:

```text
Custom block textures and possibly Blockbench models.
```

Likely assets:

```text
genetic_apiary block
apiary frame slot/item icons
basic_frame
mutation_frame
productivity_frame
stability_frame
centrifuge, if included
```

This is the first phase where Blockbench becomes truly useful.

### Phase 8 — Data-Driven Content

Required assets:

```text
None for the loader itself.
```

Optional:

```text
Example content pack assets.
```

### Phase 9 — Expanded Content

Required assets:

```text
Many species/product icons and possibly additional block models.
```

This phase should have a separate content/art plan before implementation.

### Phase 10 — Fabric Support

Required assets:

```text
No new assets.
```

Fabric should reuse the same resource structure when possible.

## 4. Asset Source vs In-Game Asset Structure

Keep source files separate from exported mod assets.

Recommended structure:

```text
assets-source/
├── blockbench/
│   ├── genetic_apiary.bbmodel
│   └── centrifuge.bbmodel
├── textures-source/
│   ├── item/
│   └── block/
├── references/
└── exports/
```

Mod resource output:

```text
src/main/resources/assets/<modid>/
├── textures/
│   ├── item/
│   └── block/
├── models/
│   ├── item/
│   └── block/
├── blockstates/
└── lang/
```

If the project uses a multi-loader layout, resources may live in a common resource module or be copied into loader-specific modules depending on the build setup.

## 5. Mod ID / Namespace Decision

Before finalizing real assets, decide the final namespace.

Temporary working namespace:

```text
curious_bees
```

Do not mix these accidentally:

```text
curiousbees
curious_bees
bee_genetics
```

Use one namespace consistently for:

```text
textures
models
blockstates
lang keys
item ids
block ids
data definitions
allele/species ids
```

## 6. Placeholder Strategy

Placeholder assets should be:

```text
- obviously temporary;
- easy to replace;
- named the same as the final asset path;
- visually distinct enough for in-game testing;
- not allowed to block gameplay implementation.
```

Do not spend time polishing placeholders.

Good placeholder naming:

```text
assets-source/textures-source/item/bee_analyzer.placeholder.png
```

But exported runtime path should already match final expected path:

```text
assets/<modid>/textures/item/bee_analyzer.png
```

This avoids code/model churn when the final art replaces the placeholder.

## 7. Tool Responsibilities

### GPT Project

Use for:

```text
- planning;
- asset lists;
- style guides;
- workflow docs;
- prompts;
- reviewing generated asset specs.
```

### Claude Code

Use for:

```text
- creating folders;
- creating JSON model files;
- wiring item/block model paths;
- updating lang files;
- validating references;
- adding placeholder files if provided;
- writing scripts/checklists.
```

Claude Code should not be trusted as the only visual judge of art quality.

### Cursor

Use for:

```text
- inspecting files;
- reviewing diffs;
- manually editing JSON/model/lang files;
- making quick adjustments;
- navigating resources while testing.
```

### Blockbench

Use for:

```text
- custom block models;
- machine models;
- item models that need more than flat 16x16 icons;
- visual iteration;
- exporting Java block/item models.
```

### Image/Pixel Tools

Use for:

```text
- 16x16 item icons;
- comb icons;
- simple placeholder textures;
- final texture painting.
```

Possible tools:

```text
Aseprite
Piskel
GIMP/Krita
Blockbench texture editor
AI image generation followed by manual cleanup
```

### MCP / Automation

Treat as optional future tooling.

Do not require MCP for the MVP.

## 8. When to Create a Claude Code Skill

Do not create a full asset skill before the asset workflow is repeated at least a few times.

Use a simple prompt or slash command first.

A Claude Code skill becomes useful when the project repeatedly needs a workflow such as:

```text
- create placeholder item asset;
- create model JSON;
- validate texture/model/lang paths;
- generate asset request from backlog item;
- validate Blockbench export structure;
- check for missing textures.
```

## 9. When to Use MCP

MCP may be useful later if:

```text
- Blockbench is open locally;
- an MCP server/plugin can safely inspect or export models;
- the workflow is repeatable;
- permissions are understood;
- the tool can be sandboxed or isolated.
```

Do not use a powerful MCP integration casually if it can execute arbitrary scripts or modify local files without review.

## 10. Asset Review Gates

### Gate 1 — Placeholder Need Review

Before adding placeholder assets, answer:

```text
Does this phase actually require an in-game asset?
Can a debug command or vanilla item be used instead?
Will lack of asset block gameplay validation?
```

### Gate 2 — Asset List Review

Before creating multiple assets, create a list:

```text
asset id
type
phase
required/optional
placeholder acceptable?
source file path
runtime file path
owner/tool
```

### Gate 3 — Integration Review

Before considering an asset done, verify:

```text
texture path is correct
model path is correct
lang key exists
asset loads in-game
no purple/black missing texture
no incorrect namespace
no unnecessary custom model complexity
```

### Gate 4 — Blockbench Review

Before committing Blockbench models, verify:

```text
source .bbmodel is saved
exported JSON is in the right path
texture references are relative and correct
model follows Minecraft Java limitations
model displays correctly in inventory/world
```

### Gate 5 — Skill/MCP Review

Before adding a skill or MCP workflow, answer:

```text
Is this workflow repeated enough to automate?
Can it be done with a simple slash command instead?
Does it require external app control?
What permissions does it need?
Can it damage project files if wrong?
Can the result be reviewed in git?
```

## 11. Definition of Done for Asset Pipeline Planning

This planning phase is done when:

```text
- the project knows when assets are actually needed;
- placeholder strategy is documented;
- Blockbench usage is documented;
- AI tooling strategy is documented;
- MCP is treated as optional future tooling;
- asset review gates exist;
- early implementation is not blocked by art.
```
