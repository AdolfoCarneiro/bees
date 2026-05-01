# 06 — Production MVP Implementation

## 1. Purpose

This document defines the implementation plan for **Phase 6 — Production MVP** of Curious Bees.

At this point, the project should already have:

```text
- a pure Java genetics core;
- initial built-in species and trait content;
- NeoForge genome storage on vanilla Bee entities;
- vanilla bee breeding integration;
- mutation during breeding;
- a basic Bee Analyzer.
```

The purpose of this phase is to make bee species matter beyond breeding.

The production MVP should be small, testable, and intentionally limited. It should prove that a bee's active and inactive species can influence outputs without turning the project into a full tech/apiary mod too early.

## 2. Phase Goal

Implement the first production logic where:

```text
Active species determines primary production.
Inactive species may influence secondary production.
Productivity may influence chance/rate.
```

The goal is not to build the full Forestry-style production ecosystem yet.

The goal is to prove:

```text
Genome -> Species identity -> Production output
```

## 3. Design Principles

### 3.1 Keep Production Secondary to Genetics

The production system exists to reward genetic progress.

It should not replace the genetic loop.

Players should still care about:

```text
- purebred lines;
- hybrids;
- recessive traits;
- mutation results;
- analyzer inspection;
- selective breeding.
```

### 3.2 Hybrids Should Matter

A hybrid bee should not be treated as a failed purebred.

Example:

```text
Cultivated / Forest
```

Recommended behavior:

```text
Primary output:
- Cultivated product

Secondary output:
- small chance of Forest byproduct
```

This makes inactive species meaningful.

### 3.3 Do Not Add Resource Bees Yet

Do not implement:

```text
Iron Bee
Copper Bee
Gold Bee
Diamond Bee
Redstone Bee
Emerald Bee
Netherite Bee
Ore processing bees
```

Resource bees are future expansion content, not the production MVP.

### 3.4 Avoid Full Tech Apiary in This Phase

Do not implement:

```text
Genetic Apiary
Centrifuge
Frame Housing
Automation
Pipes
Energy systems
Item input/output automation
```

Those belong to later phases.

### 3.5 Start With a Pure/Common Resolver

Before integrating with a Minecraft block/hive, implement a common production resolver that can be tested without Minecraft.

This keeps production rules testable and prevents NeoForge event code from containing gameplay logic.

## 4. Production MVP Scope

The MVP should include:

```text
ProductionDefinition
ProductionOutput
ProductionRoll or ProductionResult
ProductionResolver
Built-in production definitions for MVP species
Basic comb/product item registration
A minimal NeoForge production integration path
```

The exact integration point can be simple and may be revised later.

Possible MVP integration approaches:

```text
Option A: Debug command/tool that rolls production for a targeted bee.
Option B: Simple item generated when using an analyzer/debug command.
Option C: Simple Genetic Hive placeholder block.
Option D: Modify vanilla hive behavior minimally.
```

Initial recommendation:

```text
Start with a pure ProductionResolver and a debug/dev validation path.
Only add a simple in-game production path after the resolver works.
```

## 5. Non-Goals

Do not implement in this phase:

```text
- resource bees;
- full apiary block;
- centrifuge;
- frames;
- energy system;
- automation;
- pipes;
- complex GUI;
- JEI/REI integration;
- Fabric support;
- JSON/datapack loading;
- advanced recipes;
- polished textures;
- custom bee models;
- custom bee rendering;
- complex vanilla hive rewrites.
```

## 6. Required Concepts

## 6.1 ProductionDefinition

Represents what a species can produce.

Suggested fields:

```text
speciesId
primaryOutputs
secondaryOutputs
baseProductionChance
```

Example conceptual definition:

```text
Species: curious_bees:cultivated

Primary:
- curious_bees:cultivated_comb, chance 1.0

Secondary:
- minecraft:honeycomb, chance 0.25
```

For the common core, avoid depending directly on Minecraft `Item`.

Use stable output IDs as strings or a domain-specific ID type.

Example:

```text
curious_bees:cultivated_comb
minecraft:honeycomb
curious_bees:forest_resin
```

## 6.2 ProductionOutput

Represents one possible output.

Suggested fields:

```text
outputId
chance
minCount
maxCount
```

Initial count can always be 1 if simpler.

Example:

```text
outputId: curious_bees:forest_comb
chance: 0.75
minCount: 1
maxCount: 1
```

## 6.3 ProductionContext

Represents external context that may later influence production.

MVP fields can be minimal:

```text
random
environment context optional
```

Future fields:

```text
biome
dimension
timeOfDay
weather
nearbyBlocks
apiaryFrames
machineModifiers
```

Do not overbuild the context in this phase.

## 6.4 ProductionResolver

Given a `Genome`, it returns possible/generated production output.

Suggested responsibilities:

```text
- read active species from Species chromosome;
- read inactive species from Species chromosome;
- find production definition for active species;
- roll primary outputs;
- optionally roll secondary outputs from inactive species;
- apply productivity modifier if implemented;
- return a result object.
```

## 6.5 ProductionResult

Represents the outcome of one production roll.

Suggested fields:

```text
generatedOutputs
activeSpeciesId
inactiveSpeciesId
productivityAlleleId
debugRolls optional
```

Keep it simple.

## 7. MVP Species Production Concepts

Initial product concepts from the content design:

```text
Honeycomb
Meadow Comb
Forest Comb
Arid Comb
Cultivated Comb
Hardy Comb
Wax
```

It is acceptable to reduce this list during implementation.

Recommended minimum:

```text
Meadow Comb
Forest Comb
Arid Comb
Cultivated Comb
Hardy Comb
```

Optional shared product:

```text
Wax
```

## 7.1 Meadow Bee

Concept:

```text
Starter plains/flower bee.
Basic honey/wax production.
```

Suggested primary output:

```text
curious_bees:meadow_comb
```

Optional secondary output:

```text
minecraft:honeycomb
```

## 7.2 Forest Bee

Concept:

```text
Forest starter bee.
Early byproduct identity.
```

Suggested primary output:

```text
curious_bees:forest_comb
```

Optional secondary output:

```text
curious_bees:forest_resin
```

If `forest_resin` feels too much for MVP, skip it.

## 7.3 Arid Bee

Concept:

```text
Dry/hot starter bee.
Introduces biome identity.
```

Suggested primary output:

```text
curious_bees:arid_comb
```

Optional secondary output:

```text
curious_bees:dry_wax
```

If `dry_wax` feels too much for MVP, skip it.

## 7.4 Cultivated Bee

Concept:

```text
First successful mutation.
More efficient honey/wax output.
```

Suggested primary output:

```text
curious_bees:cultivated_comb
```

Optional secondary output:

```text
minecraft:honeycomb
```

## 7.5 Hardy Bee

Concept:

```text
Early resilience mutation.
Future base for harsh-environment progression.
```

Suggested primary output:

```text
curious_bees:hardy_comb
```

Optional secondary output:

```text
curious_bees:durable_wax
```

If `durable_wax` feels too much for MVP, skip it.

## 8. Productivity Trait Interaction

The MVP has a `PRODUCTIVITY` chromosome.

Initial values:

```text
SLOW
NORMAL
FAST
```

Suggested placeholder multipliers:

```text
SLOW   -> 0.75x
NORMAL -> 1.00x
FAST   -> 1.25x
```

Implementation options:

```text
Option A: Productivity affects chance.
Option B: Productivity affects cooldown.
Option C: Productivity is displayed but not used until tech apiary.
```

Recommended MVP approach:

```text
Use productivity as a chance multiplier in the pure ProductionResolver.
Do not implement cooldown or machine timing yet.
```

Example:

```text
finalChance = baseChance * productivityMultiplier
```

Clamp final chance to:

```text
0.0 <= finalChance <= 1.0
```

## 9. Active and Inactive Species Rule

Recommended MVP rule:

```text
Active species controls primary output.
Inactive species can contribute secondary output at reduced chance.
```

Example:

```text
Genome species: Cultivated / Forest
Active: Cultivated
Inactive: Forest
```

Possible production result:

```text
Primary roll:
- Cultivated Comb

Secondary roll:
- Forest Comb or Forest byproduct at reduced chance
```

Secondary chance multiplier:

```text
0.10 to 0.25
```

Recommended placeholder:

```text
inactiveSpeciesMultiplier = 0.15
```

Do not overbalance yet.

## 10. Item Registration Strategy

When registering actual items in NeoForge, keep item definitions simple.

Initial items:

```text
curious_bees:meadow_comb
curious_bees:forest_comb
curious_bees:arid_comb
curious_bees:cultivated_comb
curious_bees:hardy_comb
```

Optional:

```text
curious_bees:wax
```

Use standard generated item models.

No Blockbench needed for these items.

A placeholder texture is acceptable.

## 11. Asset Strategy for Production MVP

Production MVP may require item textures.

However:

```text
Assets must not block implementation.
```

Acceptable early asset strategies:

```text
- use simple placeholder 16x16 textures;
- reuse temporary generated icons;
- create plain colored comb icons;
- use missing texture temporarily in dev if necessary;
- add TODO markers for final art.
```

Do not introduce Blockbench for comb items.

Blockbench becomes relevant later for:

```text
Genetic Apiary
Centrifuge
Frame Housing
custom machine blocks
custom block models
```

Optional future doc:

```text
docs/art/asset-style-guide.md
docs/art/blockbench-workflow.md
```

## 12. Recommended Package Structure

Common production logic:

```text
common/src/main/java/.../common/gameplay/production/
├── ProductionDefinition.java
├── ProductionOutput.java
├── ProductionResult.java
├── ProductionResolver.java
└── ProductivityModifier.java
```

Built-in production definitions:

```text
common/src/main/java/.../common/content/products/
├── BuiltinBeeProducts.java
└── BuiltinProductionDefinitions.java
```

NeoForge item registration:

```text
neoforge/src/main/java/.../neoforge/registry/
├── ModItems.java
```

NeoForge resources:

```text
neoforge/src/main/resources/assets/curious_bees/
├── models/item/meadow_comb.json
├── models/item/forest_comb.json
├── models/item/arid_comb.json
├── models/item/cultivated_comb.json
├── models/item/hardy_comb.json
├── textures/item/meadow_comb.png
├── textures/item/forest_comb.png
├── textures/item/arid_comb.png
├── textures/item/cultivated_comb.png
├── textures/item/hardy_comb.png
└── lang/en_us.json
```

Adjust paths according to the actual mod id and project layout.

## 13. Task Breakdown

## Task 1 — Define MVP Production Model

### Objective

Define the pure/common production domain model.

### Scope

Implement model classes/interfaces only:

```text
ProductionDefinition
ProductionOutput
ProductionResult
ProductionResolver contract or skeleton
```

This task may define the shape of the production API without fully implementing all rolls.

### Non-goals

Do not implement:

```text
- item registration;
- NeoForge integration;
- vanilla hive integration;
- block entities;
- apiary;
- JSON loading;
- GUI.
```

### Acceptance Criteria

```text
- ProductionDefinition exists.
- ProductionOutput exists.
- ProductionResult exists.
- Production model uses stable output IDs, not direct Minecraft Item references.
- Code has no Minecraft, NeoForge or Fabric imports.
- Validation rejects invalid chance values.
- Unit tests cover model validation.
```

### Expected Tests

```text
valid production output succeeds
chance below 0 fails
chance above 1 fails
blank output id fails
production definition requires species id
```

### Verification Gate 1 — Production Model Review

Stop and verify:

```text
- Is the model pure Java?
- Does it avoid Minecraft Item references?
- Are chances validated?
- Is it flexible enough for active/inactive species?
- Is it still small?
```

Do not proceed to resolver implementation until this is true.

### Prompt

```text
Read CLAUDE.md and the relevant docs.

Focus only on: Task 1 — Define MVP Production Model from docs/implementation/06-production-mvp.md.

Implement the pure Java production model:
- ProductionDefinition
- ProductionOutput
- ProductionResult

Use stable string/domain IDs for outputs, not Minecraft Item references.

Do not implement item registration, NeoForge integration, hive integration, apiary, JSON loading, GUI, or resource bees.

Add unit tests for validation.

Before coding, summarize your understanding and list the files you expect to create or modify.
```

## Task 2 — Implement Productivity Modifier

### Objective

Implement pure/common logic for converting the active productivity allele into a multiplier.

### Scope

Implement:

```text
ProductivityModifier
```

or equivalent.

Initial mapping:

```text
SLOW   -> 0.75
NORMAL -> 1.00
FAST   -> 1.25
```

### Non-goals

Do not implement:

```text
- cooldowns;
- machine speed;
- API for frames;
- advanced balancing;
- Minecraft integration.
```

### Acceptance Criteria

```text
- SLOW maps to 0.75.
- NORMAL maps to 1.00.
- FAST maps to 1.25.
- Unknown productivity value is handled safely.
- Unit tests cover mapping.
```

### Expected Tests

```text
slow productivity returns 0.75
normal productivity returns 1.00
fast productivity returns 1.25
unknown productivity returns default or fails clearly
```

### Verification Gate 2 — Productivity Modifier Review

Stop and verify:

```text
- Is this still pure/common code?
- Is the fallback behavior explicit?
- Are values easy to rebalance later?
```

### Prompt

```text
Read CLAUDE.md and docs/implementation/06-production-mvp.md.

Focus only on: Task 2 — Implement Productivity Modifier.

Implement pure Java logic that maps MVP productivity allele IDs to production multipliers:
- Slow -> 0.75
- Normal -> 1.00
- Fast -> 1.25

Do not implement cooldowns, machines, frames, NeoForge integration, item registration, GUI, or resource bees.

Add unit tests for all mappings and fallback behavior.

Before coding, summarize your understanding and list the files you expect to create or modify.
```

## Task 3 — Implement Species-Based ProductionResolver

### Objective

Implement the pure/common resolver that turns a genome into production output rolls.

### Scope

The resolver should:

```text
- read active species;
- read inactive species;
- find production definitions;
- roll primary output from active species;
- roll optional secondary output from inactive species;
- apply productivity multiplier;
- return ProductionResult.
```

### Non-goals

Do not implement:

```text
- item spawning;
- inventory insertion;
- block entities;
- vanilla hive modification;
- apiary;
- NeoForge integration;
- JSON loading.
```

### Acceptance Criteria

```text
- Resolver accepts Genome and production definitions.
- Active species controls primary output.
- Inactive species can contribute secondary output at reduced chance.
- Productivity modifies production chance.
- Deterministic GeneticRandom can control roll outcomes.
- Tests cover pure species production.
- Tests cover hybrid species production.
- Tests cover productivity modifier.
- Tests cover no-output cases.
```

### Expected Tests

```text
meadow/meadow can produce Meadow Comb
cultivated/forest uses Cultivated as primary
cultivated/forest can use Forest as secondary
slow productivity lowers chance
fast productivity raises chance
deterministic random below chance produces output
deterministic random above chance produces no output
missing production definition fails clearly or returns no output based on chosen design
```

### Verification Gate 3 — Production Resolver Review

Stop and verify:

```text
- Is production logic testable without Minecraft?
- Does active species matter?
- Does inactive species matter?
- Does productivity matter?
- Is random injectable/deterministic in tests?
```

Do not proceed to item registration until this is true.

### Prompt

```text
Read CLAUDE.md and docs/implementation/06-production-mvp.md.

Focus only on: Task 3 — Implement Species-Based ProductionResolver.

Implement a pure Java ProductionResolver that uses a Genome, built-in production definitions and GeneticRandom to generate production results.

Rules:
- active species controls primary output;
- inactive species may contribute secondary output at reduced chance;
- productivity modifies output chance;
- no Minecraft, NeoForge or Fabric dependencies.

Do not implement item registration, item spawning, inventories, block entities, vanilla hive modification, apiary, JSON loading, GUI, or resource bees.

Add unit tests for pure species, hybrid species, productivity modifiers and deterministic random outcomes.

Before coding, summarize your understanding and list the files you expect to create or modify.
```

## Task 4 — Define Built-In Production Definitions

### Objective

Define production data for the five MVP species.

### Scope

Create centralized built-in production definitions for:

```text
Meadow
Forest
Arid
Cultivated
Hardy
```

Minimum outputs:

```text
curious_bees:meadow_comb
curious_bees:forest_comb
curious_bees:arid_comb
curious_bees:cultivated_comb
curious_bees:hardy_comb
```

### Non-goals

Do not implement:

```text
- resource products;
- JSON loading;
- item registration;
- recipes;
- balancing beyond rough placeholder values.
```

### Acceptance Criteria

```text
- Each MVP species has a production definition.
- Definitions are centralized.
- Definitions can be used by ProductionResolver.
- Definitions use stable IDs.
- Tests verify all MVP species have production definitions.
```

### Verification Gate 4 — Built-In Production Review

Stop and verify:

```text
- Are all five species covered?
- Are output IDs stable?
- Is content centralized?
- Did we avoid resource bees?
- Are values intentionally placeholder?
```

### Prompt

```text
Read CLAUDE.md, docs/mvp/05-content-design-spec.md, and docs/implementation/06-production-mvp.md.

Focus only on: Task 4 — Define Built-In Production Definitions.

Create centralized built-in production definitions for:
- Meadow
- Forest
- Arid
- Cultivated
- Hardy

Use stable output IDs:
- curious_bees:meadow_comb
- curious_bees:forest_comb
- curious_bees:arid_comb
- curious_bees:cultivated_comb
- curious_bees:hardy_comb

Do not implement resource products, JSON loading, recipes, item registration, NeoForge integration, GUI, or apiary.

Add tests verifying all MVP species have production definitions.

Before coding, summarize your understanding and list the files you expect to create or modify.
```

## Task 5 — Register MVP Comb Items

### Objective

Register the first physical item outputs in NeoForge.

### Scope

Register items for:

```text
meadow_comb
forest_comb
arid_comb
cultivated_comb
hardy_comb
```

Optional:

```text
wax
```

Add basic:

```text
- item registration;
- lang entries;
- generated item model JSON;
- placeholder textures if available.
```

### Non-goals

Do not implement:

```text
- production generation;
- recipes;
- centrifuge;
- apiary;
- custom block models;
- Blockbench workflow;
- polished art.
```

### Acceptance Criteria

```text
- MVP comb items exist in-game.
- Items have names in lang file.
- Items have basic generated model JSON.
- Placeholder textures are acceptable.
- Item registration follows existing NeoForge registry conventions.
```

### Asset Notes

If textures do not exist yet, use the simplest acceptable placeholder strategy.

Do not block the task on final art.

Possible placeholder approaches:

```text
- create simple 16x16 placeholders;
- reuse one generic comb placeholder temporarily;
- leave TODO for final art if the dev environment allows missing texture during early testing.
```

### Verification Gate 5 — Item Registration Review

Stop and verify:

```text
- Do items appear in-game?
- Are IDs stable?
- Are lang entries present?
- Are models valid?
- Did the task avoid production logic?
```

### Prompt

```text
Read CLAUDE.md and docs/implementation/06-production-mvp.md.

Focus only on: Task 5 — Register MVP Comb Items.

Register basic NeoForge items for:
- meadow_comb
- forest_comb
- arid_comb
- cultivated_comb
- hardy_comb

Add lang entries and simple generated item models.

Use placeholder textures if needed. Do not block on final art.

Do not implement production generation, recipes, centrifuge, apiary, custom block models, Blockbench workflow, GUI, or resource bees.

Before coding, summarize your understanding and list the files you expect to create or modify.
```

## Task 6 — Add Debug Production Roll Command

### Objective

Add a simple development validation path for production before creating a full hive/apiary.

### Scope

Create a debug command or dev-only interaction that:

```text
- targets or selects a bee;
- reads its genome;
- runs ProductionResolver;
- prints generated production result to chat/log;
- does not spawn items unless explicitly chosen.
```

This is a validation tool, not final gameplay.

### Non-goals

Do not implement:

```text
- automatic production loops;
- hive behavior;
- apiary;
- block entity inventory;
- item automation;
- recipes;
- GUI.
```

### Acceptance Criteria

```text
- Developer can run production roll against a bee with genome.
- Output shows active species, inactive species, productivity and generated outputs.
- Missing genome is handled safely.
- Command delegates production logic to common ProductionResolver.
- No production logic is implemented directly inside command handler.
```

### Verification Gate 6 — Debug Production Review

Stop and verify:

```text
- Can a bee's genome drive a production result?
- Is output readable?
- Is logic delegated to common resolver?
- Did this avoid premature hive/apiary logic?
```

### Prompt

```text
Read CLAUDE.md and docs/implementation/06-production-mvp.md.

Focus only on: Task 6 — Add Debug Production Roll Command.

Implement a simple debug/development command that reads a targeted bee's genome, runs the common ProductionResolver and prints the production result.

Do not implement automatic production loops, hive modification, apiary, block entities, inventories, recipes, GUI, or resource bees.

Before coding, summarize your understanding and list the files you expect to create or modify.
```

## Task 7 — Choose First Real Production Integration

### Objective

Make a deliberate decision about how production becomes real gameplay after the debug resolver works.

### Scope

Research/decide between:

```text
Option A: minimal vanilla hive modification
Option B: simple Genetic Hive placeholder block
Option C: delay real production until Tech Apiary phase
Option D: simple bee interaction/dev-only proof
```

### Recommended Initial Bias

Prefer:

```text
simple Genetic Hive placeholder
```

or:

```text
delay real production until Tech Apiary
```

Avoid heavy vanilla hive rewrites early.

### Non-goals

Do not implement code in this task.

This is a decision task.

### Acceptance Criteria

```text
- A production integration approach is chosen.
- Pros/cons are documented.
- Risks are documented.
- Next implementation task is created.
```

### Verification Gate 7 — Production Integration Decision

Stop and verify:

```text
- Is the chosen approach small?
- Does it avoid premature automation?
- Does it preserve future apiary design?
- Does it avoid risky vanilla hive rewrites?
```

### Prompt

```text
Read CLAUDE.md and docs/implementation/06-production-mvp.md.

Focus only on: Task 7 — Choose First Real Production Integration.

Do not implement code.

Compare:
- minimal vanilla hive modification;
- simple Genetic Hive placeholder block;
- delaying real production until Tech Apiary;
- debug/dev-only proof.

Return a decision note with recommendation, pros, cons, risks, and next implementation task.
```

## 14. Phase 6 Verification Gates Summary

```text
Gate 1 — Production Model Review
Gate 2 — Productivity Modifier Review
Gate 3 — Production Resolver Review
Gate 4 — Built-In Production Review
Gate 5 — Item Registration Review
Gate 6 — Debug Production Review
Gate 7 — Production Integration Decision
```

## 15. Go / No-Go Before Phase 7

Do not proceed to **Phase 7 — Tech Apiary** until:

```text
- active species can determine production output;
- inactive species can influence secondary output or the decision to delay it is documented;
- productivity has either an implemented modifier or a documented deferral;
- MVP comb items exist or placeholder strategy is accepted;
- production logic is tested without Minecraft;
- a debug/dev path can validate production from a bee genome;
- the first real production integration approach is selected.
```

## 16. Common AI Failure Modes

## 16.1 The Agent Adds Resource Bees

Symptom:

```text
Adds iron/copper/gold/redstone/diamond bees.
```

Fix:

```text
Reject or revert. Resource bees are future content.
```

## 16.2 The Agent Builds a Full Apiary

Symptom:

```text
Creates block entity, inventory, GUI, automation and frames.
```

Fix:

```text
Reject or split. Apiary belongs to Phase 7.
```

## 16.3 The Agent Puts Production Logic in NeoForge Command/Event

Symptom:

```text
Production rules live directly inside command handler or event handler.
```

Fix:

```text
Move logic to common ProductionResolver.
```

## 16.4 The Agent Uses Minecraft Item in Common Definitions

Symptom:

```text
ProductionDefinition stores Item or ItemStack.
```

Fix:

```text
Use stable IDs or a platform-neutral output reference.
```

## 16.5 The Agent Blocks on Art

Symptom:

```text
Refuses to register items because textures are not final.
```

Fix:

```text
Use placeholders. Final art is not required for MVP.
```

## 17. Suggested Commit Sequence

```text
production: add production model
production: add productivity modifier
production: add species-based production resolver
content: add built-in production definitions
neoforge: register MVP comb items
neoforge: add debug production roll command
docs: record first production integration decision
```

## 18. Handoff Prompt for Phase 6

```text
Read CLAUDE.md and the docs folder.

Then read:
- docs/implementation/06-production-mvp.md

We are implementing Phase 6 — Production MVP.

Do not implement resource bees, full apiary, centrifuge, frames, GUI, Fabric support, JSON loading, advanced recipes or polished assets.

Start with Task 1 only:
Define MVP Production Model.

Before coding:
1. summarize the task;
2. list expected files;
3. list assumptions;
4. confirm what is out of scope.

Then implement the smallest complete version with tests.
```

## 19. Phase 6 Definition of Done

Phase 6 is complete when:

```text
- a pure/common production model exists;
- a production resolver can use a Genome to determine outputs;
- active species controls primary output;
- inactive species has a defined role or explicit deferral;
- productivity has a defined role or explicit deferral;
- MVP production definitions exist for the five initial species;
- MVP comb items are registered or placeholder strategy is documented;
- production behavior can be validated in development;
- no resource bees were added;
- no full apiary was implemented prematurely;
- common production logic has no Minecraft/NeoForge/Fabric dependency.
```
