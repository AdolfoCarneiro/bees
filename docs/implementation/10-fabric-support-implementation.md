# 10 — Fabric Support Implementation

## 1. Purpose

This document defines the implementation plan for adding Fabric support to Curious Bees after the NeoForge MVP is stable.

Fabric support is a future target, not part of the initial MVP. The goal of this phase is to reuse the existing pure Java genetics core and port only the platform-specific integration layer.

This phase should not rewrite the genetics system.

It should prove that the project architecture can support more than one loader without duplicating gameplay rules.

## 2. Phase Position

This phase should happen after:

```text
Phase 1 — Genetics Core
Phase 2 — Initial Content
Phase 3 — NeoForge Bee Data Integration
Phase 4 — Vanilla Breeding Integration
Phase 5 — Analyzer MVP
Phase 6 — Production MVP
```

It may happen before or after:

```text
Phase 7 — Tech Apiary and Automation
Phase 8 — Data-Driven Content
Phase 9 — Expanded Content Roadmap
```

Recommended timing:

```text
Implement Fabric only after the NeoForge natural breeding loop is playable and stable.
```

## 3. Goals

The goals of Fabric support are:

```text
- Reuse the existing common genetics core.
- Reuse common content definitions.
- Reuse common gameplay orchestration where possible.
- Add Fabric-specific entity genome storage.
- Add Fabric-specific vanilla bee breeding integration.
- Add Fabric-specific item registration for analyzer and MVP items.
- Add Fabric-specific debug tooling.
- Keep behavior equivalent to the NeoForge MVP.
```

## 4. Non-Goals

Do not use the Fabric port as an excuse to redesign the mod.

Do not implement:

```text
- a new genetics core;
- Fabric-only species;
- Fabric-only mutation rules;
- Fabric-only gameplay balancing;
- new resource bees;
- advanced apiary features;
- new GUI systems;
- major architecture rewrites;
- NeoForge code changes unrelated to platform abstraction;
- data-driven content if it is not already in scope;
- new assets beyond what already exists or placeholders.
```

## 5. Core Rule

The Fabric implementation must call the same common services as NeoForge.

Good:

```text
Fabric event/mixin -> common breeding orchestration -> common BreedingService + MutationService
```

Bad:

```text
Fabric event/mixin contains its own breeding and mutation rules
```

The common genetics core must remain free of Fabric imports.

## 6. Expected Repository Shape

By this phase, the project should ideally look like:

```text
curious-bees/
├── common/
│   └── src/
│       ├── main/java/
│       └── test/java/
├── neoforge/
│   └── src/
│       ├── main/java/
│       └── main/resources/
├── fabric/
│   └── src/
│       ├── main/java/
│       └── main/resources/
├── docs/
└── build.gradle / settings.gradle
```

If the project started as a NeoForge-only setup, this phase may require a build-system migration.

That migration should be handled carefully and separately.

## 7. Implementation Strategy

Fabric support should be implemented in small slices:

```text
10A — Fabric Feasibility Spike
10B — Multi-loader Build Layout Decision
10C — Fabric Module Setup
10D — Fabric Genome Storage
10E — Fabric Wild Bee Initialization
10F — Fabric Breeding Hook / Mixin
10G — Fabric Analyzer Item
10H — Fabric Production Integration
10I — Cross-loader Behavior Parity Tests
10J — Fabric Release Packaging
```

Each slice should be reviewed before moving to the next one.

# 10A — Fabric Feasibility Spike

## Objective

Research the best Fabric integration strategy before writing port code.

## Questions to Answer

```text
- Which Fabric/Minecraft version should match the NeoForge target?
- Is Fabric 1.21.1 still the correct target by the time this phase starts?
- What API is best for storing persistent genome data on Bee entities?
- Is a Fabric Data Attachment API available and stable for the target version?
- Are Data Components needed for items later?
- What Fabric event or mixin is needed to intercept vanilla bee baby creation?
- Can parent bee references be accessed cleanly?
- Will a mixin be required?
- What build layout should be used?
```

## Output

Produce a technical decision note:

```text
docs/decisions/DR-010-fabric-support-strategy.md
```

The note should include:

```text
- target Minecraft version;
- Fabric Loader/API version;
- storage strategy;
- breeding hook strategy;
- build layout recommendation;
- known risks;
- next implementation task.
```

## Non-Goals

Do not implement code in this spike.

## Verification Gate 1 — Fabric Feasibility Review

Stop and review:

```text
- Is Fabric support still worth doing now?
- Is the target version clear?
- Is the storage API clear?
- Is the breeding hook/mixin strategy clear?
- Is the build layout decision clear?
```

Do not continue until the decision note is accepted.

# 10B — Multi-loader Build Layout Decision

## Objective

Decide and implement the build/module structure needed for Fabric support.

## Possible Strategies

### Strategy A — True multi-loader workspace

```text
common/
neoforge/
fabric/
```

Pros:

```text
- clean long-term separation;
- common code is shared;
- platform code is isolated;
- easier future parity checks.
```

Cons:

```text
- build setup complexity;
- harder initial configuration;
- AI agents may struggle if docs are unclear.
```

### Strategy B — NeoForge-first with common package boundaries

```text
src/main/java/common-like packages
src/main/java/neoforge packages
```

Pros:

```text
- simpler early setup;
- easier for initial MVP.
```

Cons:

```text
- harder to add Fabric later;
- refactor required in this phase.
```

### Strategy C — Architectury or equivalent abstraction

Pros:

```text
- existing multi-loader ecosystem;
- shared abstractions for registry/events may help.
```

Cons:

```text
- extra dependency;
- abstraction may hide loader details;
- version compatibility must be checked.
```

## Recommended Approach

If the project already has a clean `common/neoforge/fabric` structure, use it.

If the project started NeoForge-only, perform a separate refactor before adding Fabric behavior.

## Acceptance Criteria

```text
- Build layout decision is documented.
- Common code is separated from platform code.
- NeoForge still builds.
- Existing tests still run.
- No gameplay behavior changes during layout refactor.
```

## Verification Gate 2 — Build Layout Review

Stop and verify:

```text
- Can common tests run independently?
- Can the NeoForge module still build?
- Is there a Fabric module placeholder?
- Did the refactor avoid gameplay changes?
```

# 10C — Fabric Module Setup

## Objective

Create the minimal Fabric module that loads without implementing gameplay features.

## Scope

Implement:

```text
- Fabric module/build configuration;
- fabric.mod.json;
- main Fabric initializer;
- basic logging;
- shared mod id/constants if appropriate;
- resource folder structure.
```

## Non-Goals

Do not implement:

```text
- genome storage;
- breeding;
- analyzer item;
- production;
- custom blocks;
- custom GUI;
- new species.
```

## Acceptance Criteria

```text
- Fabric module exists.
- Fabric module can be built.
- Fabric dev environment can launch or at least compile.
- Common module is reused, not copied.
- No NeoForge imports exist in Fabric code.
- No Fabric imports exist in common code.
```

## Expected Files

Possible files:

```text
fabric/src/main/resources/fabric.mod.json
fabric/src/main/java/.../fabric/CuriousBeesFabric.java
fabric/src/main/java/.../fabric/registry/FabricItems.java
```

Only create registry files if required by the chosen structure.

## Prompt

```text
Read CLAUDE.md and docs/implementation/10-fabric-support-implementation.md.

Focus only on: 10C — Fabric Module Setup.

Create the minimal Fabric module setup required for the project to compile/load on Fabric.

Do not implement genome storage, breeding hooks, analyzer behavior, production, blocks, GUI, resource bees or new content.

Before coding, summarize the build layout and list files you expect to create or modify.
```

## Verification Gate 3 — Fabric Module Smoke Test

Stop and verify:

```text
- Fabric module compiles.
- Common module is referenced correctly.
- No common code imports Fabric.
- No NeoForge code was broken.
```

# 10D — Fabric Genome Storage

## Objective

Implement Fabric-side storage for `Genome` data on vanilla `Bee` entities.

## Scope

The Fabric platform layer should:

```text
- attach or associate a Genome with a Bee entity;
- read a Genome from a Bee;
- write a Genome to a Bee;
- detect missing Genome data;
- persist Genome data through save/load;
- use the same serialization strategy as NeoForge when possible.
```

## Storage Options

Investigate and use the chosen strategy from the decision note:

```text
- Fabric Data Attachment API, if available and stable;
- custom component system if appropriate;
- mixin-based persistent NBT bridge if necessary;
- another documented Fabric-compatible persistence mechanism.
```

## Non-Goals

Do not implement:

```text
- wild genome initialization;
- breeding integration;
- analyzer UI;
- production behavior;
- new serialization format unless required;
- new genetics rules.
```

## Acceptance Criteria

```text
- A Fabric Bee can store a Genome.
- Stored Genome persists through save/load.
- Missing Genome is handled safely.
- Common serialization is reused where possible.
- Common genetics core remains unchanged.
- Unit tests or integration/dev validation steps are documented.
```

## Expected Validation

Manual/dev validation:

```text
1. Spawn or locate a bee.
2. Assign a test genome through debug/dev code.
3. Save and reload the world.
4. Confirm the genome still exists.
```

## Prompt

```text
Read CLAUDE.md, docs/implementation/03-neoforge-entity-integration.md, and docs/implementation/10-fabric-support-implementation.md.

Focus only on: 10D — Fabric Genome Storage.

Implement Fabric-side storage for Genome data on vanilla Bee entities using the selected Fabric persistence strategy.

Do not implement wild initialization, breeding, analyzer UI, production, blocks, GUI, or new genetics rules.

Before coding, summarize the storage strategy and list expected files.
```

## Verification Gate 4 — Fabric Genome Persistence Review

Stop and verify:

```text
- Can a Bee store a Genome?
- Does it persist after save/load?
- Does missing data fail safely?
- Is serialization shared with NeoForge where possible?
- Did common code remain platform-free?
```

# 10E — Fabric Wild Bee Initialization

## Objective

Initialize genomes on wild Fabric bees using the same rules as NeoForge.

## Scope

Implement Fabric-side hook/event/mixin to detect bees that need initial genomes.

Use common services for:

```text
- biome/environment mapping;
- default wild genome creation;
- fallback species.
```

## Non-Goals

Do not implement:

```text
- breeding integration;
- mutation during spawn;
- analyzer item;
- production;
- Fabric-only species.
```

## Acceptance Criteria

```text
- Bees without genomes receive one.
- Bees with existing genomes are not overwritten.
- Meadow/Forest/Arid mapping matches NeoForge behavior.
- Unknown biomes use fallback.
- Genome persists after reload.
```

## Prompt

```text
Read CLAUDE.md, the content specs, and docs/implementation/10-fabric-support-implementation.md.

Focus only on: 10E — Fabric Wild Bee Initialization.

Implement Fabric-side wild bee genome initialization using the same common services and content definitions used by NeoForge.

Do not implement breeding, mutation, analyzer UI, production, or Fabric-only behavior.

Before coding, summarize the chosen hook and files to modify.
```

## Verification Gate 5 — Fabric Wild Initialization Review

Stop and verify:

```text
- Wild bees receive genomes.
- Existing genomes are not overwritten.
- Biome mapping matches NeoForge.
- Fallback works.
- Save/load works.
```

# 10F — Fabric Breeding Hook / Mixin

## Objective

Apply genetics to baby bees created through vanilla breeding on Fabric.

## Scope

Implement Fabric-specific hook/mixin to:

```text
- detect baby bee creation;
- identify parent bees if possible;
- identify child bee;
- read parent genomes;
- call common breeding orchestration;
- write final child genome to baby bee;
- trigger minimal mutation feedback if already supported.
```

## Non-Goals

Do not implement:

```text
- new breeding algorithm;
- Fabric-only mutation behavior;
- analyzer UI;
- production;
- multiple baby bees;
- fertility gameplay changes;
- resource bees.
```

## Parent Resolution

The Fabric implementation should match the NeoForge behavior as closely as possible.

If Fabric cannot access parent references cleanly, document the fallback.

Fallback options may include:

```text
- mixin at the exact method where parents are known;
- temporary parent tracking;
- nearest parent inference only if clearly safe;
- deferring Fabric breeding support until a safe hook is found.
```

Do not silently implement unreliable parent inference.

## Acceptance Criteria

```text
- Breeding two genetic bees creates a child with inherited genome.
- Mutations use the same common MutationService.
- Parent genomes are not modified.
- Missing parent genomes are handled safely.
- Behavior matches NeoForge as closely as possible.
- Hook/mixin code is thin and delegates to common services.
```

## Prompt

```text
Read CLAUDE.md, docs/implementation/04-vanilla-breeding-integration.md, and docs/implementation/10-fabric-support-implementation.md.

Focus only on: 10F — Fabric Breeding Hook / Mixin.

Implement Fabric-side vanilla bee breeding integration by detecting baby bee creation, reading parent genomes, calling common breeding/mutation orchestration, and storing the final child genome.

Do not implement new genetics rules, analyzer UI, production, resource bees, apiary behavior, or Fabric-only mechanics.

Before coding, summarize how parent bees are identified and what fallback is used if needed.
```

## Verification Gate 6 — Fabric Breeding Review

Stop and verify:

```text
- Parent bees are correctly identified.
- Child bee receives genome.
- Mutation can occur.
- Missing parent genome is safe.
- Behavior matches NeoForge expectations.
```

# 10G — Fabric Analyzer Item

## Objective

Port the Analyzer MVP to Fabric.

## Scope

Implement Fabric-side item registration and interaction behavior for the Bee Analyzer.

Reuse common analyzer services:

```text
BeeAnalysisService
BeeAnalysisReport
BeeAnalysisFormatter
```

The Fabric layer should only:

```text
- register item;
- handle use-on-entity interaction;
- read genome through Fabric storage;
- render/send output to player.
```

## Non-Goals

Do not implement:

```text
- Fabric-only analyzer report;
- advanced GUI;
- mutation prediction;
- genetic editing;
- research/discovery system.
```

## Acceptance Criteria

```text
- Analyzer item exists on Fabric.
- Analyzer can be used on a Bee.
- Analyzer reads Fabric-stored genome.
- Output matches NeoForge MVP behavior.
- Missing genome is handled safely.
```

## Verification Gate 7 — Fabric Analyzer Review

Stop and verify:

```text
- Analyzer output matches NeoForge behavior.
- Common analyzer formatting is reused where possible.
- No Fabric logic leaks into common report classes.
```

# 10H — Fabric Production Integration

## Objective

Port MVP production behavior to Fabric if production exists by this phase.

## Scope

Depending on what Phase 6 implemented, Fabric should support:

```text
- item registration for comb/product items;
- production resolver usage;
- debug production command/tool;
- any initial hive/block integration if already implemented on NeoForge.
```

## Non-Goals

Do not implement:

```text
- new production balancing;
- Fabric-only products;
- resource bees;
- tech apiary if not already implemented;
- advanced machines.
```

## Acceptance Criteria

```text
- Fabric product items match NeoForge names/ids where possible.
- Common ProductionResolver is reused.
- Behavior matches NeoForge MVP.
- No production logic is duplicated in Fabric event code.
```

## Verification Gate 8 — Fabric Production Review

Stop and verify:

```text
- Product items exist.
- Production behavior matches NeoForge.
- Common resolver is reused.
- No Fabric-only balancing exists.
```

# 10I — Cross-Loader Behavior Parity Tests

## Objective

Verify that NeoForge and Fabric behave consistently.

## Scope

Create a manual or automated parity checklist.

Compare:

```text
- wild bee genome initialization;
- genome persistence;
- breeding inheritance;
- mutation behavior;
- analyzer output;
- production behavior;
- item ids/names;
- config/default values.
```

## Non-Goals

Do not require perfect automated integration testing if tooling is too expensive.

Manual validation is acceptable for early Fabric support.

## Acceptance Criteria

```text
- A parity checklist exists.
- Known differences are documented.
- Critical differences are fixed before release.
- Non-critical differences are logged as follow-up tasks.
```

## Suggested Document

```text
docs/testing/cross-loader-parity-checklist.md
```

## Verification Gate 9 — Cross-Loader Parity Review

Stop and verify:

```text
- Is behavior equivalent enough for release?
- Are differences intentional and documented?
- Are critical bugs fixed?
```

# 10J — Fabric Release Packaging

## Objective

Prepare Fabric build artifacts for testing or release.

## Scope

Document and verify:

```text
- build command;
- output jar location;
- dependency requirements;
- supported Minecraft/Fabric versions;
- known limitations;
- testing steps.
```

## Non-Goals

Do not publish automatically unless explicitly requested.

## Acceptance Criteria

```text
- Fabric jar can be built.
- Required dependencies are documented.
- Known limitations are listed.
- Release notes mention parity with NeoForge MVP.
```

## Verification Gate 10 — Fabric Release Readiness

Stop and verify:

```text
- Fabric build works.
- NeoForge build still works.
- Common tests still pass.
- Fabric smoke test passes.
- Version metadata is correct.
```

# 11. Asset Strategy for Fabric

Fabric support should not create a separate visual identity.

Use the same assets where possible:

```text
assets/curiousbees/textures/
assets/curiousbees/models/
assets/curiousbees/lang/
```

If source structure requires loader-specific resource folders, keep files mirrored or generated from a shared source.

Do not use Fabric support as a reason to create new assets.

# 12. Configuration Strategy

If config exists by this phase, Fabric should use equivalent defaults.

Config values that should match across loaders:

```text
mutation chances
wild species fallback
production rates
debug options
feature toggles
```

Differences should be documented.

# 13. Common Failure Modes

## 13.1 Rewriting Genetics for Fabric

Symptom:

```text
Fabric code implements its own allele selection or mutation rules.
```

Fix:

```text
Use common BreedingService and MutationService.
```

## 13.2 Common Code Imports Fabric

Symptom:

```text
common/genetics imports Fabric classes.
```

Fix:

```text
Move Fabric references to fabric module/platform adapter.
```

## 13.3 Parent Detection Is Guessy

Symptom:

```text
Fabric breeding uses nearest bees instead of actual parents without documentation.
```

Fix:

```text
Find a safer mixin point or block the task until a reliable approach is found.
```

## 13.4 NeoForge Breaks During Fabric Refactor

Symptom:

```text
Fabric setup modifies shared code and breaks NeoForge MVP.
```

Fix:

```text
Run NeoForge build/tests after every build-layout change.
```

## 13.5 Divergent Content

Symptom:

```text
Fabric has different species/mutation definitions.
```

Fix:

```text
Move definitions to common content and reuse them.
```

# 14. Phase 10 Completion Checklist

Phase 10 is complete when:

```text
- Fabric module exists and builds.
- Common genetics core is reused.
- Fabric Bee entities can store genomes.
- Fabric wild bees can receive genomes.
- Fabric vanilla breeding assigns inherited genomes.
- Fabric mutations use the common MutationService.
- Fabric analyzer can inspect bees.
- Production parity exists if production is implemented.
- NeoForge still builds and behaves correctly.
- Cross-loader parity checklist is complete.
- Known Fabric limitations are documented.
```

# 15. Go / No-Go for Public Fabric Release

Before releasing Fabric support publicly:

```text
- Does the Fabric jar build cleanly?
- Was it tested in a real Fabric client?
- Does breeding work in-game?
- Does save/load preserve genomes?
- Does analyzer output match NeoForge?
- Are known limitations documented?
- Is the version marked clearly as Fabric?
- Are dependencies listed correctly?
```

If any answer is no, do not publish Fabric support yet.

# 16. Handoff Prompt for Claude Code

Use this prompt when starting Fabric support:

```text
Read CLAUDE.md and all existing docs.

Then read:
- docs/implementation/10-fabric-support-implementation.md
- docs/implementation/03-neoforge-entity-integration.md
- docs/implementation/04-vanilla-breeding-integration.md
- docs/implementation/05-analyzer-implementation.md

Do not implement code yet.

First produce a Fabric support feasibility note covering:
1. target Minecraft/Fabric version;
2. Fabric module/build strategy;
3. entity genome storage strategy;
4. vanilla bee breeding hook or mixin strategy;
5. risks and unknowns;
6. recommended first implementation task.

Do not modify files until the strategy is reviewed.
```

# 17. Final Rule

Fabric support is a platform port, not a second version of the mod.

The correct dependency direction is:

```text
common genetics/content/gameplay
        ↓
NeoForge adapter
        ↓
Fabric adapter
```

The Fabric adapter must not become a fork of the gameplay rules.
