# Phase 8 — Data-Driven Content Implementation

## 1. Purpose

This document defines the implementation plan for **Phase 8 — Data-Driven Content** of Curious Bees.

The purpose of this phase is to move species, trait, mutation, and production definitions from centralized built-in Java definitions into data files that can later be extended through datapacks or config-like content packs.

This phase should only start after the core gameplay loop is stable:

```text
genetics core
initial built-in content
NeoForge bee genome storage
vanilla breeding integration
analyzer MVP
basic production MVP
```

Data-driven loading is powerful, but it adds complexity. It should not be implemented before the rules are stable.

## 2. Phase Goal

By the end of this phase:

```text
- Built-in content still works.
- Species definitions can be represented as external data.
- Trait allele definitions can be represented as external data.
- Mutation definitions can be represented as external data.
- Production definitions can be represented as external data.
- Data validation catches invalid definitions clearly.
- The content model remains compatible with future datapack-driven expansion.
```

The phase is complete when a new species and mutation can be added without changing core Java gameplay logic.

## 3. Non-Goals

Do not implement in this phase unless explicitly requested:

```text
- resource bee content tree
- large biome/nether/end expansion
- GUI for content editing
- in-game mutation research book
- full balancing pass
- Fabric implementation
- custom scripting language
- runtime hot editing UI
- automatic AI-generated content packs
- polished assets for every future species
```

This phase is about **content infrastructure**, not adding a huge amount of content.

## 4. Required Inputs

Before starting Phase 8, these should already exist:

```text
docs/01-product-vision-and-roadmap.md
docs/02-technical-architecture.md
docs/03-genetics-system-spec.md
docs/04-breeding-and-mutation-spec.md
docs/05-content-design-spec.md
docs/06-ai-coding-guidelines.md
docs/implementation/01-genetics-core-implementation.md
docs/implementation/02-initial-content-implementation.md
docs/implementation/06-production-mvp.md
```

Implementation dependencies:

```text
Genome
Allele
Dominance
ChromosomeType
BeeSpeciesDefinition or equivalent
MutationDefinition
ProductionDefinition
Built-in content registry/facade
Validation utilities
Production resolver
Mutation service
```

## 5. Core Design Principle

The same domain models should support both:

```text
built-in Java definitions
external data definitions
```

Avoid creating two separate content systems.

Bad:

```text
BuiltinSpeciesDefinition
JsonSpeciesDefinition
RuntimeSpeciesDefinition
```

Good:

```text
SpeciesDefinition
SpeciesDefinitionLoader
SpeciesDefinitionValidator
BuiltinSpeciesDefinitions
```

The loader should produce the same model objects used by the rest of the mod.

## 6. Recommended Data Folder Layout

Future datapack-style layout:

```text
data/curious_bees/
├── species/
│   ├── meadow.json
│   ├── forest.json
│   ├── arid.json
│   ├── cultivated.json
│   └── hardy.json
│
├── traits/
│   ├── lifespan/
│   │   ├── short.json
│   │   ├── normal.json
│   │   └── long.json
│   ├── productivity/
│   │   ├── slow.json
│   │   ├── normal.json
│   │   └── fast.json
│   ├── fertility/
│   │   ├── one.json
│   │   ├── two.json
│   │   └── three.json
│   └── flower_type/
│       ├── flowers.json
│       ├── cactus.json
│       └── leaves.json
│
├── mutations/
│   ├── cultivated_from_meadow_forest.json
│   └── hardy_from_forest_arid.json
│
└── production/
    ├── meadow.json
    ├── forest.json
    ├── arid.json
    ├── cultivated.json
    └── hardy.json
```

This structure can be adjusted to match Minecraft/NeoForge resource loading conventions, but the conceptual separation should remain.

## 7. Data File Format Drafts

These formats are drafts. They should be treated as implementation targets but may be refined if needed.

### 7.1 Species Definition

Example:

```json
{
  "id": "curious_bees:meadow",
  "displayName": "Meadow Bee",
  "dominance": "DOMINANT",
  "defaultTraits": {
    "lifespan": "curious_bees:lifespan/normal",
    "productivity": "curious_bees:productivity/normal",
    "fertility": "curious_bees:fertility/two",
    "flowerType": "curious_bees:flower_type/flowers"
  },
  "spawnContexts": {
    "biomeTags": [
      "minecraft:is_overworld",
      "curious_bees:bee_spawn/meadow"
    ],
    "fallbackWeight": 1
  },
  "progression": {
    "tier": 0,
    "category": "wild"
  }
}
```

MVP required fields:

```text
id
displayName
dominance
defaultTraits
```

Optional/future fields:

```text
spawnContexts
progression
description
temperaturePreference
humidityPreference
```

### 7.2 Trait Allele Definition

Example:

```json
{
  "id": "curious_bees:productivity/fast",
  "chromosomeType": "PRODUCTIVITY",
  "displayName": "Fast",
  "dominance": "DOMINANT",
  "values": {
    "multiplier": 1.25
  }
}
```

MVP required fields:

```text
id
chromosomeType
displayName
dominance
```

Optional values:

```text
multiplier
numericValue
description
```

### 7.3 Mutation Definition

Example:

```json
{
  "id": "curious_bees:cultivated_from_meadow_forest",
  "parents": [
    "curious_bees:meadow",
    "curious_bees:forest"
  ],
  "result": "curious_bees:cultivated",
  "baseChance": 0.12,
  "resultModes": {
    "partialChance": 0.95,
    "fullChance": 0.05
  },
  "requirements": {
    "biomes": [],
    "dimensions": []
  }
}
```

MVP required fields:

```text
id
parents
result
baseChance
resultModes
```

Optional/future fields:

```text
requirements
modifiers
nearbyBlocks
requiredTraitConditions
```

### 7.4 Production Definition

Example:

```json
{
  "species": "curious_bees:cultivated",
  "primaryOutputs": [
    {
      "item": "curious_bees:cultivated_comb",
      "chance": 0.8,
      "min": 1,
      "max": 1
    }
  ],
  "secondaryOutputs": [
    {
      "item": "minecraft:honeycomb",
      "chance": 0.2,
      "min": 1,
      "max": 1
    }
  ]
}
```

MVP required fields:

```text
species
primaryOutputs
```

Optional fields:

```text
secondaryOutputs
conditions
traitModifiers
```

## 8. Implementation Strategy

Do not replace all built-ins at once.

Recommended approach:

```text
1. Keep built-in Java definitions working.
2. Define serializable DTOs for species/traits/mutations/production.
3. Add validators for DTOs.
4. Convert DTOs into runtime domain definitions.
5. Add file/data loading.
6. Merge loaded definitions with built-ins.
7. Add tests for valid and invalid data.
8. Only then make built-ins optionally backed by data files.
```

## 9. Task Breakdown

## Task 8A — Data-Driven Content Design Decision

### Objective

Create a short design decision record that defines the data-driven content strategy before implementation.

### Scope

Document:

```text
- which content types become data-driven first;
- whether built-ins remain Java or move to bundled data files;
- expected folder layout;
- validation strategy;
- error reporting strategy;
- how this interacts with datapacks;
- how this affects future Fabric support.
```

### Non-Goals

Do not implement loaders yet.

### Acceptance Criteria

```text
- Decision record exists.
- Built-in fallback strategy is clear.
- First data-driven content type is chosen.
- Migration order is clear.
```

### Verification Gate 1 — Data-Driven Strategy Review

Stop and review before implementation.

Questions:

```text
Do we still need this phase now?
Are species/mutations/production rules stable enough?
Will the loader preserve built-in content compatibility?
Are invalid files handled without crashing worlds unnecessarily?
```

### Prompt

```text
Read CLAUDE.md and all docs.

Focus only on: Task 8A — Data-Driven Content Design Decision.

Create a design decision document for the data-driven content strategy.

Do not implement code.

The decision must cover:
- which definitions become data-driven first;
- built-in fallback strategy;
- folder layout;
- validation;
- error reporting;
- future datapack support;
- future Fabric considerations.

Before writing, summarize the current content architecture from the docs.
```

## Task 8B — Define Serializable Content DTOs

### Objective

Create serializable data transfer objects for content definitions.

### Scope

Implement DTO/model classes for:

```text
SpeciesDefinitionData
TraitAlleleDefinitionData
MutationDefinitionData
ProductionDefinitionData
```

Exact names may vary.

These objects should represent external data format, not necessarily runtime behavior.

### Non-Goals

Do not implement file loading yet.

Do not replace built-in content yet.

Do not add resource bees.

### Acceptance Criteria

```text
- DTOs exist for species, traits, mutations, and production.
- DTOs are simple and serializable.
- DTOs do not contain gameplay logic.
- DTOs can represent the MVP built-in content.
- DTOs are independent from NeoForge/Fabric where possible.
```

### Expected Tests

```text
DTOs can represent Meadow species data
DTOs can represent Fast productivity allele data
DTOs can represent Meadow + Forest -> Cultivated mutation data
DTOs can represent Cultivated production data
```

### Prompt

```text
Read CLAUDE.md and docs/implementation/08-data-driven-content.md.

Focus only on: Task 8B — Define Serializable Content DTOs.

Implement simple serializable data objects for species, trait alleles, mutations and production definitions.

Do not implement file loading, datapack reload, registry merging, resource bees, Minecraft integration, or JSON migration yet.

Add tests or test fixtures showing that the DTOs can represent the MVP content.

Before coding, summarize the expected DTOs and list the files you expect to create or modify.
```

## Task 8C — Implement Content Validation

### Objective

Implement validation logic for loaded content data.

### Scope

Validation should check:

```text
species ids are present and valid
display names are present
dominance values are valid
trait references exist
mutation parent species exist
mutation result species exists
mutation chance is between 0.0 and 1.0
partial/full mutation chances are valid
production references valid species
production output chances are valid
production item ids are present
duplicate ids are rejected
```

### Non-Goals

Do not load files yet.

Do not implement datapack reload.

Do not generate content.

### Acceptance Criteria

```text
- Validator exists.
- Valid MVP-like definitions pass.
- Invalid definitions produce clear errors.
- Errors include enough context to identify the file/definition.
- Validator does not silently ignore invalid content.
```

### Expected Tests

```text
valid species passes
missing species id fails
duplicate species id fails
mutation with unknown parent fails
mutation with invalid chance fails
production with invalid chance fails
trait with mismatched chromosome type fails
```

### Verification Gate 2 — Validation Review

Stop and review validation before adding file loading.

Questions:

```text
Are validation errors clear enough for modpack/content creators?
Do validators catch references across definition types?
Do validators avoid crashing on first error when multiple errors could be reported?
```

### Prompt

```text
Read CLAUDE.md and docs/implementation/08-data-driven-content.md.

Focus only on: Task 8C — Implement Content Validation.

Implement validators for species, trait allele, mutation and production definition data.

Do not implement file loading or datapack reload yet.

Add tests for valid MVP definitions and common invalid cases.

Before coding, summarize the validation rules and list the files you expect to modify.
```

## Task 8D — Implement Data-to-Runtime Conversion

### Objective

Convert validated content DTOs into runtime domain definitions.

### Scope

Implement converter/factory classes that transform:

```text
SpeciesDefinitionData -> BeeSpeciesDefinition
TraitAlleleDefinitionData -> Allele or TraitDefinition
MutationDefinitionData -> MutationDefinition
ProductionDefinitionData -> ProductionDefinition
```

### Non-Goals

Do not load JSON files yet.

Do not modify gameplay services.

Do not replace built-ins yet.

### Acceptance Criteria

```text
- Validated species data converts to runtime species definitions.
- Validated trait data converts to runtime alleles/definitions.
- Validated mutation data converts to runtime mutation definitions.
- Validated production data converts to runtime production definitions.
- Conversion preserves stable ids.
- Conversion does not depend on NeoForge/Fabric APIs unless absolutely necessary.
```

### Expected Tests

```text
Meadow species data converts correctly
Fast productivity data converts correctly
Cultivated mutation data converts correctly
Cultivated production data converts correctly
```

### Prompt

```text
Read CLAUDE.md and docs/implementation/08-data-driven-content.md.

Focus only on: Task 8D — Implement Data-to-Runtime Conversion.

Implement conversion from validated content data objects into the runtime domain definitions used by the existing genetics/content/production services.

Do not implement file loading, datapack reload, or replace built-ins yet.

Add tests for converting MVP species, trait, mutation and production definitions.

Before coding, summarize the conversion flow and list expected files.
```

## Task 8E — Implement Built-In Content Export/Mirroring

### Objective

Represent existing built-in content in the same data model used by external content.

### Scope

Add helpers to mirror or export built-in definitions as data objects.

Options:

```text
Option A: Built-ins remain Java runtime definitions but can be mirrored as Data objects for tests.
Option B: Built-ins are rewritten using Data objects and converted into runtime definitions.
```

Recommended initial approach:

```text
Option B if rules are stable.
Option A if implementation risk is high.
```

### Non-Goals

Do not remove built-in fallback behavior.

Do not force external data loading yet.

### Acceptance Criteria

```text
- Built-in MVP species can be represented by data definitions.
- Built-in trait alleles can be represented by data definitions.
- Built-in mutations can be represented by data definitions.
- Built-in production can be represented by data definitions if production exists.
- Existing tests still pass.
```

### Verification Gate 3 — Built-In Migration Review

Stop and review before adding external loading.

Questions:

```text
Do built-ins still work?
Did the migration duplicate too much logic?
Can built-ins and external definitions share validators/converters?
```

### Prompt

```text
Read CLAUDE.md and docs/implementation/08-data-driven-content.md.

Focus only on: Task 8E — Implement Built-In Content Export/Mirroring.

Make the existing built-in content representable through the new content data model.

Do not remove built-in fallback behavior.

Do not implement external file loading yet.

Add tests proving that MVP built-ins can be represented and converted through the data-driven model.
```

## Task 8F — Implement JSON Parsing for Content Definitions

### Objective

Load content definitions from JSON resources/files into DTOs.

### Scope

Implement parsing for:

```text
species JSON
trait allele JSON
mutation JSON
production JSON
```

Depending on project maturity, this may initially be plain test-resource JSON parsing before full datapack integration.

### Non-Goals

Do not implement full datapack reload yet unless the project is ready.

Do not create many new species.

Do not generate assets.

### Acceptance Criteria

```text
- JSON parser can read species definitions.
- JSON parser can read trait allele definitions.
- JSON parser can read mutation definitions.
- JSON parser can read production definitions.
- Parsed definitions are validated.
- Invalid JSON produces clear errors.
```

### Expected Tests

```text
valid species JSON parses
invalid species JSON fails clearly
valid mutation JSON parses
invalid chance fails validation
valid production JSON parses
missing output item fails validation
```

### Prompt

```text
Read CLAUDE.md and docs/implementation/08-data-driven-content.md.

Focus only on: Task 8F — Implement JSON Parsing for Content Definitions.

Implement JSON parsing into the existing content data DTOs, with validation.

Start with test resources if full Minecraft datapack loading is not ready.

Do not implement a large content pack, resource bees, GUI, assets, or Fabric support.

Add tests for valid and invalid JSON definitions.
```

## Task 8G — Implement Runtime Content Registry Merge

### Objective

Merge built-in and loaded data-driven definitions into one runtime content registry/facade.

### Scope

The registry should support:

```text
built-in definitions
loaded definitions
override policy or duplicate rejection
lookup by id
stable definition access for gameplay services
clear errors for invalid references
```

### Key Design Decision

Choose one override strategy:

```text
Option A: Loaded definitions cannot override built-ins.
Option B: Loaded definitions can override built-ins only if explicitly allowed.
Option C: Loaded definitions always override built-ins.
```

Recommended MVP:

```text
Option A or B.
```

Avoid accidental overwrites.

### Non-Goals

Do not build a UI for content management.

Do not add large content.

### Acceptance Criteria

```text
- Runtime registry contains built-ins.
- Runtime registry can include loaded definitions.
- Duplicate handling is explicit.
- Lookups are clear and safe.
- Gameplay services can use the registry without caring whether content came from Java or JSON.
```

### Verification Gate 4 — Registry Merge Review

Stop and review before full datapack integration.

Questions:

```text
Can old built-in gameplay still run?
Can loaded definitions be tested without Minecraft?
Is duplicate/override behavior safe?
Does the registry avoid loader-specific dependencies?
```

### Prompt

```text
Read CLAUDE.md and docs/implementation/08-data-driven-content.md.

Focus only on: Task 8G — Implement Runtime Content Registry Merge.

Implement a runtime content registry/facade that combines built-in definitions and loaded data definitions.

Choose and document a duplicate/override strategy.

Do not implement GUI, large content, resource bees, or Fabric support.

Add tests for built-in-only registry, loaded definitions, duplicates and lookups.
```

## Task 8H — Integrate With Minecraft/NeoForge Resource Loading

### Objective

Connect JSON content loading to the actual Minecraft/NeoForge resource/datapack system.

### Scope

Implement NeoForge-side resource loading for data-driven content.

The platform layer should:

```text
discover content JSON files
parse them into DTOs
validate them
convert them to runtime definitions
merge them into the content registry
log errors clearly
reload safely when datapacks reload, if supported
```

### Non-Goals

Do not implement Fabric loading yet.

Do not implement custom UI.

Do not generate content.

Do not add resource bees.

### Acceptance Criteria

```text
- Content JSON files can be discovered from resources/datapacks.
- Valid files load into runtime registry.
- Invalid files produce clear logs/errors.
- Built-in fallback content still works if no external data exists.
- Gameplay services can use loaded content.
```

### Expected Tests/Validation

Unit tests:

```text
loader accepts valid test resources
loader rejects invalid test resources
loader keeps built-ins when external content is absent
```

Manual/in-game validation:

```text
launch game
load built-in content
optionally add a test datapack species/mutation
confirm content registry sees loaded definition
```

### Verification Gate 5 — Data Loading Integration Review

Stop and review before declaring Phase 8 complete.

Questions:

```text
Does the game still load with only built-ins?
Does a bad content file produce an actionable error?
Does loaded content affect gameplay correctly?
Is the reload behavior safe?
```

### Prompt

```text
Read CLAUDE.md and docs/implementation/08-data-driven-content.md.

Focus only on: Task 8H — Integrate With Minecraft/NeoForge Resource Loading.

Implement NeoForge-side resource/datapack loading for the existing content definition JSON files.

Keep common validation/conversion logic outside NeoForge where possible.

Do not implement Fabric support, GUI, resource bees, large content expansions, or asset generation.

Add tests where possible and describe manual validation steps.
```

## Task 8I — Add Example Data Files for MVP Content

### Objective

Create example data files that mirror the MVP built-in content.

### Scope

Add example JSON files for:

```text
Meadow
Forest
Arid
Cultivated
Hardy
MVP trait alleles
Cultivated mutation
Hardy mutation
MVP production definitions
```

These files may be bundled examples, test resources, or actual loaded defaults depending on the chosen strategy.

### Non-Goals

Do not create a large expansion pack.

Do not add resource bees.

Do not add final assets.

### Acceptance Criteria

```text
- Example files exist.
- Example files validate.
- Example files document the expected format.
- Example files match existing built-in behavior.
```

### Prompt

```text
Read CLAUDE.md, docs/05-content-design-spec.md and docs/implementation/08-data-driven-content.md.

Focus only on: Task 8I — Add Example Data Files for MVP Content.

Create example JSON data files for the existing MVP content.

Do not add new species beyond Meadow, Forest, Arid, Cultivated and Hardy.

Do not add resource bees, new progression branches, GUI, or assets.

Run validation/tests for the example files.
```

## Task 8J — Document Content Authoring Guide

### Objective

Write documentation for future content authors.

### Scope

Create:

```text
docs/content-authoring-guide.md
```

The guide should explain:

```text
species files
trait files
mutation files
production files
ids and references
chance values
validation errors
how to test a content pack
what not to do
```

### Non-Goals

Do not document a large content tree that does not exist.

Do not promise unsupported features.

### Acceptance Criteria

```text
- Content authoring guide exists.
- Guide includes MVP examples.
- Guide explains validation errors.
- Guide explains built-in vs loaded definitions.
- Guide clearly marks future fields as future.
```

### Verification Gate 6 — Content Authoring Review

Stop and review the docs.

Questions:

```text
Could a future contributor add a species using this guide?
Does the guide avoid promising features not implemented?
Are examples aligned with actual schemas?
```

### Prompt

```text
Read CLAUDE.md and docs/implementation/08-data-driven-content.md.

Focus only on: Task 8J — Document Content Authoring Guide.

Create docs/content-authoring-guide.md explaining how to author species, traits, mutations and production definitions.

Use only implemented fields and clearly mark future fields as future.

Do not invent new systems.
```

## 10. Phase 8 Verification Gates Summary

```text
Gate 1 — Data-Driven Strategy Review
Gate 2 — Validation Review
Gate 3 — Built-In Migration Review
Gate 4 — Registry Merge Review
Gate 5 — Data Loading Integration Review
Gate 6 — Content Authoring Review
Gate 7 — Phase 8 Exit Review
```

## 11. Phase 8 Exit Review

Before Phase 8 is marked complete, verify:

```text
Built-in content still works.
External content data can represent MVP definitions.
Invalid content is rejected clearly.
Loaded definitions can be merged safely.
Gameplay services do not care whether content is built-in or loaded.
JSON loading does not force Fabric work early.
No resource bee expansion was added accidentally.
No polished asset requirement was introduced.
```

## 12. Go / No-Go Checkpoint Before Phase 9

Do not proceed to large content expansion or Fabric support until:

```text
- data-driven definitions are stable;
- content validation is reliable;
- built-ins and loaded content can coexist;
- the MVP loop still works after the migration;
- content authoring docs exist;
- there is a clear decision on whether Phase 9 is expanded content or Fabric work.
```

## 13. Common AI Failure Modes

### 13.1 The Agent Adds Too Much Content

Symptom:

```text
It adds 30 resource bees while implementing JSON loading.
```

Fix:

```text
Reject. Phase 8 is infrastructure, not content expansion.
```

### 13.2 The Agent Replaces Built-ins Too Early

Symptom:

```text
Built-in content no longer works without external files.
```

Fix:

```text
Restore built-in fallback. External data should extend or mirror, not break MVP defaults.
```

### 13.3 The Agent Hides Validation Errors

Symptom:

```text
Bad files are silently ignored.
```

Fix:

```text
Validation errors must be explicit and actionable.
```

### 13.4 The Agent Creates Loader Dependencies in Common Content Models

Symptom:

```text
Common content models import NeoForge or Minecraft resource classes.
```

Fix:

```text
Move loader-specific code to platform layer.
```

### 13.5 The Agent Designs an Overly Complex Schema

Symptom:

```text
The schema supports dozens of unused future fields and becomes hard to validate.
```

Fix:

```text
Keep MVP schema small. Mark future fields as future.
```

## 14. Suggested Commit Sequence

```text
docs: add data-driven content implementation spec
content: add content data DTOs
content: add content validators
content: add data-to-runtime converters
content: mirror built-in definitions through data model
content: add JSON parsing for content definitions
content: add runtime content registry merge
neoforge: load content definitions from resources
docs: add content authoring guide
```

## 15. Handoff Prompt for Phase 8

```text
Read CLAUDE.md and all docs.

Then read:
- docs/implementation/08-data-driven-content.md
- docs/05-content-design-spec.md
- docs/02-technical-architecture.md
- docs/06-ai-coding-guidelines.md

Do not implement code yet.

First summarize:
1. the current built-in content model;
2. what should become data-driven first;
3. the recommended migration order;
4. validation risks;
5. how built-ins should remain compatible.

Then propose the first implementation task.
```
