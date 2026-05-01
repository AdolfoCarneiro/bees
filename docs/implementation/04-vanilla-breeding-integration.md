# 04 — Vanilla Breeding Integration Implementation

## 1. Purpose

This document defines the detailed implementation plan for **Phase 4 — Vanilla Breeding Integration** of Curious Bees.

This is the phase where the genetics system becomes part of the player's normal Minecraft bee breeding loop.

The target player-facing behavior is:

```text
Two vanilla bees are bred with flowers.
Minecraft creates a baby bee.
Curious Bees assigns the baby a genome inherited from both parents.
A species mutation may occur.
The child keeps its genome through save/load.
```

This phase must not implement production, apiaries, resource bees, advanced GUI, Fabric support, or polished assets.

## 2. Phase 4 Depends On

Do not start Phase 4 until the following are true:

```text
Phase 1 — Genetics Core is complete.
Phase 2 — Initial Content is complete.
Phase 3 — NeoForge Bee Data Integration is complete.
```

Required technical capabilities before Phase 4:

```text
- A Bee entity can store a Genome.
- The Genome persists through save/load.
- Wild bees can receive initial genomes.
- There is a debug command or equivalent way to inspect a bee genome.
- BreedingService exists and is tested.
- MutationService exists and is tested.
- Built-in initial mutation definitions exist.
```

If any of these are missing, Phase 4 should not start.

## 3. Phase Goals

Phase 4 should prove:

```text
- The mod can detect vanilla baby bee creation.
- Parent bees can be identified or safely handled.
- Parent genomes can be read.
- Missing parent genomes can be initialized or handled without crashing.
- A child genome can be generated through common services.
- Mutation can be applied during breeding.
- The generated genome can be stored on the baby bee.
- The result can be inspected in-game.
```

## 4. Non-Goals

Do not implement the following in Phase 4:

```text
- Bee Analyzer item or GUI.
- Production output.
- Comb items.
- Apiary block.
- Frames.
- Centrifuge.
- Resource bees.
- Fabric implementation.
- Advanced networking.
- Mutation discovery/research UI.
- Complex visual effects.
- Custom bee entity replacement.
- Large species tree.
```

Minimal debug feedback is allowed.

## 5. Architecture Rules

### 5.1 Platform Layer Owns Minecraft Events

NeoForge event handlers or mixins may interact with:

```text
Bee entities
Level/world
Player
Entity events
Data attachment APIs
Particles/sounds/debug logs
```

But they must not contain genetic algorithms.

### 5.2 Common Core Owns Genetics

The following logic must remain in common/pure code:

```text
Mendelian allele selection
GenePair active/inactive resolution
Mutation matching
Mutation chance evaluation
Mutation result mode
Breeding result construction
```

### 5.3 Event Handler Should Be Thin

The event/hook implementation should do this:

```text
1. Detect baby bee creation.
2. Identify parent bees and child bee.
3. Read parent genomes.
4. Build environment context.
5. Call common breeding orchestration/service.
6. Store final genome on child bee.
7. Trigger minimal feedback/logging.
```

Avoid this:

```text
Putting allele selection, mutation rules, or species-specific logic directly inside the NeoForge event handler.
```

## 6. Recommended Data Flow

```text
NeoForge baby bee hook
    ↓
ParentResolver
    ↓
BeeGenomeStorage
    ↓
BreedingOrchestrator
    ↓
BreedingService
    ↓
MutationService
    ↓
BreedingOutcome
    ↓
BeeGenomeStorage.set(child, genome)
    ↓
Debug feedback / mutation feedback
```

## 7. Suggested Package Structure

Use the actual project conventions, but keep responsibilities separate.

Suggested NeoForge-side packages:

```text
neoforge/src/main/java/.../neoforge/event/
├── BeeBreedingEvents.java

neoforge/src/main/java/.../neoforge/bee/
├── NeoForgeBeeGenomeStorage.java
├── BeeParentResolver.java
├── BeeEnvironmentContextFactory.java

neoforge/src/main/java/.../neoforge/debug/
├── BreedingDebugLogger.java
```

Suggested common-side orchestration package:

```text
common/src/main/java/.../common/gameplay/breeding/
├── BeeBreedingOrchestrator.java
├── BeeBreedingRequest.java
├── BeeBreedingOutcome.java
```

If the project already has equivalent classes from earlier phases, extend them instead of duplicating concepts.

## 8. Implementation Tasks

## Task 1 — Review Phase 3 Breeding Hook Decision

### Objective

Review the technical decision from Phase 3 about how NeoForge detects vanilla baby bee creation.

### Scope

Confirm:

```text
- Which hook/event/mixin is used.
- Whether parent bees are available.
- Whether child bee is available before or after spawn.
- Whether child genome assignment is safe at that point.
- Whether fallback parent tracking is needed.
```

### Non-Goals

Do not implement code if the decision is still unclear.

### Acceptance Criteria

```text
- The selected hook is documented.
- Parent availability is confirmed.
- Child availability is confirmed.
- Fallback strategy is documented.
- Risks are documented.
```

### Prompt

```text
Read CLAUDE.md, docs/implementation/03-neoforge-entity-integration.md, and any Phase 3 technical decision notes.

Focus only on reviewing the chosen NeoForge hook for vanilla baby bee creation.

Do not implement code yet.

Confirm:
- selected event/hook/mixin;
- whether both parents are available;
- whether the child bee can be modified safely;
- fallback strategy if parent references are not available;
- risks.

Return a short technical decision update.
```

## Verification Gate 1 — Hook Readiness Review

Stop here and verify:

```text
- The hook is known.
- Parent and child availability are understood.
- The team knows whether a mixin or event is required.
- The fallback strategy is clear.
```

Do not proceed if this is unknown.

## Task 2 — Define Bee Breeding Request/Outcome Models

### Objective

Create a common-side request/outcome model for bee breeding orchestration.

This keeps platform code thin and makes the orchestration testable.

### Scope

Create common models such as:

```text
BeeBreedingRequest
BeeBreedingOutcome
```

Possible `BeeBreedingRequest` fields:

```text
Genome parentA
Genome parentB
EnvironmentContext environmentContext
Collection<MutationDefinition> availableMutations
GeneticRandom random
```

Possible `BeeBreedingOutcome` fields:

```text
Genome childGenome
boolean mutationOccurred
Optional<MutationResult> mutationResult
```

Optional future/debug fields:

```text
Map<ChromosomeType, InheritanceResult>
List<String> debugMessages
```

### Non-Goals

Do not implement NeoForge event handling in this task.

Do not implement entity storage in this task.

Do not implement analyzer UI.

### Acceptance Criteria

```text
- Request model exists.
- Outcome model exists.
- Models are common-side and have no Minecraft/NeoForge/Fabric imports.
- Models are immutable or safely immutable.
- They are easy to use in tests.
```

### Expected Tests

```text
request rejects missing parent genomes
request rejects missing random source if required
outcome can represent no mutation
outcome can represent mutation
```

### Prompt

```text
Read CLAUDE.md and the relevant implementation docs.

Focus only on: Define Bee Breeding Request/Outcome Models.

Create common-side request/outcome models for orchestrating bee breeding.

Do not implement NeoForge events, entity storage, analyzer UI, production, items, blocks, or Fabric support.

Add simple unit tests for construction/validation where useful.

Before coding, summarize your understanding and list the files you expect to create or modify.
```

## Task 3 — Implement BeeBreedingOrchestrator

### Objective

Implement a common-side orchestration service that combines inheritance and mutation.

### Scope

The orchestrator should:

```text
1. Receive a BeeBreedingRequest.
2. Call BreedingService to create an inherited child genome.
3. Call MutationService to apply possible species mutation.
4. Return a BeeBreedingOutcome.
```

The orchestrator should be pure/common code.

### Non-Goals

Do not implement NeoForge event code.

Do not read or write Bee entities.

Do not implement environment-specific mutation rules unless already supported by the MutationService.

Do not implement visual feedback.

### Acceptance Criteria

```text
- Orchestrator exists in common gameplay/breeding or equivalent package.
- It delegates inheritance to BreedingService.
- It delegates mutation to MutationService.
- It returns a final child genome.
- It reports whether mutation occurred.
- It has no Minecraft/NeoForge/Fabric dependencies.
```

### Expected Tests

```text
orchestrator returns inherited child when no mutation applies
orchestrator reports no mutation when no mutation applies
orchestrator returns mutated child when mutation applies
orchestrator reports mutation when mutation applies
orchestrator does not mutate parent genomes
```

### Prompt

```text
Read CLAUDE.md, docs/mvp/03-genetics-system-spec.md, docs/mvp/04-breeding-and-mutation-spec.md, and docs/implementation/01-genetics-core-implementation.md.

Focus only on: Implement BeeBreedingOrchestrator.

Implement a common-side orchestration service that receives parent genomes, environment context, mutation definitions and random source, then returns a breeding outcome using BreedingService and MutationService.

Do not implement NeoForge events, Bee entity access, analyzer UI, production, items, blocks, Fabric support, or visual feedback.

Add unit tests for no mutation, mutation, and parent immutability.

Before coding, summarize your understanding and list the files you expect to create or modify.
```

## Verification Gate 2 — Common Orchestration Review

Stop here and verify:

```text
- BeeBreedingOrchestrator is pure Java/common code.
- It has no Minecraft/NeoForge/Fabric imports.
- It is covered by unit tests.
- It can produce child genomes with and without mutation.
```

Do not proceed to NeoForge event integration until this passes.

## Task 4 — Implement Parent Genome Resolution

### Objective

Implement platform-side logic to read or safely initialize parent genomes before breeding.

### Scope

NeoForge-side parent resolution should:

```text
- receive parent Bee entities;
- read genomes from BeeGenomeStorage;
- initialize missing genomes using the wild/default genome initializer if needed;
- return parent genomes for orchestration;
- log or debug missing genome cases.
```

### Non-Goals

Do not implement child genome assignment yet.

Do not implement event hook yet if parent resolver can be tested separately.

Do not implement analyzer UI.

### Missing Genome Policy

If a parent bee has no genome:

```text
1. Try to assign a fallback wild genome based on biome/context.
2. Store that genome on the parent.
3. Continue breeding.
4. Log/debug the fallback.
```

Never crash normal gameplay because a parent genome is missing.

### Acceptance Criteria

```text
- Parent genomes can be read from two Bee entities.
- Missing parent genome is handled safely.
- Fallback genome is stored if generated.
- Logic delegates to existing BeeGenomeStorage and wild genome initializer.
- Common genetics core remains independent from NeoForge APIs.
```

### Expected Validation

```text
Breed two bees with genomes -> reads both.
Breed one bee with missing genome -> fallback initializes one.
Breed two bees with missing genomes -> fallback initializes both.
No crash occurs.
```

### Prompt

```text
Read CLAUDE.md and the Phase 3 NeoForge entity integration implementation.

Focus only on: Implement Parent Genome Resolution.

Create NeoForge-side logic that reads parent Bee genomes and safely initializes missing genomes before breeding.

Do not implement baby bee hook, child assignment, analyzer UI, production, items, blocks, Fabric support, or visual mutation feedback.

Before coding, summarize the storage and fallback strategy and list the files you expect to create or modify.
```

## Task 5 — Implement Child Genome Assignment Hook

### Objective

Use the selected NeoForge hook to assign a generated genome to a vanilla baby bee.

### Scope

The hook should:

```text
1. Detect vanilla baby Bee creation.
2. Resolve parent genomes.
3. Build environment context.
4. Call BeeBreedingOrchestrator.
5. Store outcome.childGenome on the baby bee.
6. Log/debug the result.
```

### Non-Goals

Do not implement polished particle effects.

Do not implement analyzer UI.

Do not implement production.

Do not implement resource bees.

Do not implement Fabric support.

### Acceptance Criteria

```text
- Hook triggers when vanilla bees create a baby.
- Parent genomes are read or safely initialized.
- Child genome is generated by common orchestration.
- Child genome is stored on baby Bee.
- Child genome persists through save/load.
- Missing data does not crash normal gameplay.
- Event handler remains thin.
```

### Expected In-Game Validation

```text
1. Spawn or find two bees with known genomes.
2. Breed them with flowers.
3. Inspect baby using debug command.
4. Confirm child has a genome.
5. Save/reload world.
6. Inspect baby again.
7. Confirm genome persisted.
```

### Prompt

```text
Read CLAUDE.md, docs/implementation/03-neoforge-entity-integration.md, and docs/implementation/04-vanilla-breeding-integration.md.

Focus only on: Implement Child Genome Assignment Hook.

Use the selected NeoForge hook to assign an inherited/mutated genome to baby bees created by vanilla breeding.

Do not implement analyzer UI, production, apiary, frames, resource bees, Fabric support, or polished visual effects.

Keep the event handler thin and delegate genetic work to common services.

Before coding, summarize the hook strategy and list the files you expect to create or modify.
```

## Verification Gate 3 — First In-Game Breeding Review

Stop here and verify in-game:

```text
- Two bees can breed.
- Baby bee receives a genome.
- Baby genome can be inspected through debug tools.
- Baby genome persists after save/load.
- Parents remain valid.
- No crash occurs when genomes are missing.
```

Do not continue to mutation feedback until this works.

## Task 6 — Integrate Initial Mutation Definitions Into Breeding

### Objective

Ensure initial mutations can occur during vanilla breeding.

### Scope

Connect available built-in mutation definitions to the breeding orchestrator for in-game breeding.

Initial mutations:

```text
Meadow + Forest -> Cultivated
Forest + Arid -> Hardy
```

The mutation definitions should come from centralized built-in content definitions, not hardcoded inside event handlers.

### Non-Goals

Do not implement JSON loading.

Do not implement mutation discovery.

Do not implement advanced biome requirements unless already available and simple.

Do not implement frames.

### Acceptance Criteria

```text
- Vanilla breeding uses centralized initial mutation definitions.
- Meadow + Forest can produce Cultivated.
- Forest + Arid can produce Hardy.
- Mutation is probabilistic.
- Parent order does not matter.
- Event handler does not contain species-specific mutation rules.
```

### Expected Validation

For development, use one or more of:

```text
- temporarily high mutation chance in debug/test configuration;
- deterministic random source in integration/dev scenario;
- repeated breeding attempts;
- debug logs that report mutation checks.
```

### Prompt

```text
Read CLAUDE.md, content design spec, breeding/mutation spec, and the Phase 4 implementation doc.

Focus only on: Integrate Initial Mutation Definitions Into Breeding.

Make vanilla breeding use the centralized built-in mutation definitions.

Do not hardcode species-specific mutation rules inside NeoForge event handlers.

Do not implement JSON loading, mutation discovery, frames, production, analyzer UI, resource bees, or Fabric support.

Before coding, summarize where mutation definitions currently live and how they will be passed into the breeding orchestration.
```

## Verification Gate 4 — Mutation Behavior Review

Stop here and verify:

```text
- Mutation definitions are centralized.
- Breeding event uses the centralized definitions.
- Mutation can occur in-game.
- Mutation is still probabilistic.
- Parent order does not matter.
- Debug output makes mutation checks observable.
```

Do not proceed if mutation only works through hardcoded event-handler checks.

## Task 7 — Add Minimal Mutation Feedback

### Objective

Give the player or developer minimal feedback when a mutation occurs.

### Scope

Allowed MVP feedback options:

```text
- debug log;
- chat/debug message;
- simple particle burst;
- subtle sound;
- advancement trigger placeholder.
```

Recommended first version:

```text
debug log + optional simple particle
```

### Non-Goals

Do not implement:

```text
- large GUI announcements;
- mutation discovery screen;
- research system;
- complex particles;
- custom sounds unless already available;
- polished assets.
```

### Acceptance Criteria

```text
- Mutation occurrence is observable.
- Feedback only triggers when mutation actually occurs.
- Feedback code is platform-side.
- Core MutationService does not know about particles/sounds/chat.
- Feedback is minimal and easy to remove or improve later.
```

### Prompt

```text
Read CLAUDE.md and the Phase 4 implementation doc.

Focus only on: Add Minimal Mutation Feedback.

Add minimal platform-side feedback when a breeding mutation occurs.

Prefer debug log and/or simple existing particles. Do not add polished assets, custom sounds, GUI, mutation discovery, production, apiary, or Fabric support.

Before coding, summarize the chosen feedback method and the files you expect to modify.
```

## Verification Gate 5 — Mutation Feedback Review

Stop here and verify:

```text
- Mutation feedback triggers only when mutation occurs.
- Feedback is not excessive.
- Feedback does not require custom assets.
- Feedback does not leak platform code into common genetics.
```

## Task 8 — Add Breeding Debug Report

### Objective

Make breeding outcomes easier to validate during development.

### Scope

When breeding occurs, optionally produce debug output containing:

```text
parent A active/inactive species
parent B active/inactive species
child active/inactive species
mutation checked
mutation result if any
final child genome id/summary
```

This can be a debug log, command-accessible history, or temporary developer-only output.

### Non-Goals

Do not implement a permanent player-facing UI.

Do not implement analyzer item.

Do not store long-term breeding history unless explicitly requested.

### Acceptance Criteria

```text
- Debug output helps verify inheritance and mutation.
- Debug output can be disabled or is not intrusive.
- No core genetics behavior depends on debug output.
```

### Prompt

```text
Read CLAUDE.md and the Phase 4 implementation doc.

Focus only on: Add Breeding Debug Report.

Add minimal development-facing debug output for vanilla bee breeding results.

Do not implement analyzer UI, persistent history, production, apiary, resource bees, or Fabric support.

Before coding, summarize the debug output format and where it will be emitted.
```

## Verification Gate 6 — Phase 4 Exit Review

Before leaving Phase 4, verify:

```text
- Vanilla breeding still feels like vanilla breeding.
- Baby bees receive inherited genomes.
- Mutation can occur.
- Mutation is probabilistic.
- Child genome persists after save/load.
- Parent genomes remain valid.
- Missing parent genomes are handled safely.
- Event handlers are thin.
- Common genetics core remains platform-independent.
- Debug tools can inspect results.
```

## 9. Phase 4 In-Game Test Scenarios

## Scenario 1 — Pure Parents

Setup:

```text
Parent A: Meadow / Meadow
Parent B: Forest / Forest
```

Expected:

```text
Child receives Meadow/Forest species alleles before mutation.
Mutation may produce Cultivated depending on chance.
```

Validation:

```text
Use debug command to inspect child genome.
```

## Scenario 2 — Hybrid Parents

Setup:

```text
Parent A: Meadow / Forest
Parent B: Meadow / Forest
```

Expected over repeated attempts:

```text
Some children Meadow/Meadow.
Some children Meadow/Forest.
Some children Forest/Forest.
```

Validation:

```text
Run repeated breeding attempts in debug/dev environment.
Distribution does not need to be exact in-game, but should look plausible.
```

## Scenario 3 — Missing Parent Genome

Setup:

```text
At least one parent Bee has no Curious Bees genome.
```

Expected:

```text
Fallback genome is assigned.
Breeding continues.
No crash occurs.
```

## Scenario 4 — Mutation Forced for Testing

Setup:

```text
Temporarily set Meadow + Forest -> Cultivated mutation chance to 100% in dev/test.
```

Expected:

```text
Mutation always occurs for matching parents.
Child receives Cultivated species chromosome result.
```

Important:

```text
Do not commit debug-only 100% chance unless intentionally guarded/test-only.
```

## Scenario 5 — Save/Load Child

Setup:

```text
Breed two bees.
Inspect child genome.
Save and reload world.
Inspect same child again.
```

Expected:

```text
Genome remains unchanged.
```

## 10. Phase 4 Completion Criteria

Phase 4 is complete when:

```text
- The mod detects vanilla baby bee creation.
- Parent genomes are read or safely initialized.
- Common breeding orchestration creates child genome.
- Mutation service is applied.
- Child genome is stored on the baby Bee.
- Child genome persists after save/load.
- Mutation can be observed in debug/dev validation.
- Event code remains thin and platform-specific.
- Common genetics remains independent from Minecraft/NeoForge/Fabric.
```

## 11. Go / No-Go Checkpoint Before Phase 5

Before starting Phase 5 — Analyzer MVP, answer:

```text
Can a player/developer inspect parent genomes before breeding?
Can a player/developer inspect child genome after breeding?
Does child genome persist?
Can mutation occur?
Is the output readable enough to validate?
```

If the answer is no, do not start a player-facing analyzer item yet.

## 12. Recommended Commit Sequence

```text
common: add bee breeding request/outcome models
common: add bee breeding orchestrator
neoforge: resolve parent bee genomes for breeding
neoforge: assign inherited genome to baby bees
neoforge: connect initial mutation definitions to breeding
neoforge: add minimal mutation feedback
debug: add breeding result debug output
```

## 13. AI Failure Modes to Watch

### 13.1 Event Handler Contains Genetic Logic

Bad:

```text
The NeoForge event handler selects alleles, checks mutation chances, and edits chromosomes directly.
```

Fix:

```text
Move genetic logic to common BreedingService, MutationService, or BeeBreedingOrchestrator.
```

### 13.2 Deterministic Mutation Recipe

Bad:

```text
Meadow + Forest always becomes Cultivated.
```

Fix:

```text
Mutation must use chance and random source.
```

### 13.3 Missing Genome Crash

Bad:

```text
Breeding crashes if one parent has no genome.
```

Fix:

```text
Initialize fallback genome or skip genetics safely with clear debug log.
```

### 13.4 Child Gets Random Wild Genome Instead of Inherited Genome

Bad:

```text
Baby bee receives a fresh wild genome instead of inherited alleles.
```

Fix:

```text
Baby genome must come from BreedingService + MutationService.
```

### 13.5 Platform Code Leaks Into Core

Bad:

```text
Common breeding code imports Bee, Level, Entity, NeoForge, or Minecraft classes.
```

Fix:

```text
Use platform adapters and common request/context objects.
```

## 14. Handoff Prompt for Claude Code

```text
Read CLAUDE.md.

Then read:
- docs/mvp/01-product-vision-and-roadmap.md
- docs/mvp/02-technical-architecture.md
- docs/mvp/03-genetics-system-spec.md
- docs/mvp/04-breeding-and-mutation-spec.md
- docs/mvp/05-content-design-spec.md
- docs/mvp/06-ai-coding-guidelines.md
- docs/implementation/01-genetics-core-implementation.md
- docs/implementation/02-initial-content-implementation.md
- docs/implementation/03-neoforge-entity-integration.md
- docs/implementation/04-vanilla-breeding-integration.md

Do not implement all of Phase 4 at once.

First, identify which Phase 4 task is next based on completed code and docs.

Before coding:
1. summarize the current state;
2. identify dependencies;
3. list files expected to change;
4. confirm the task scope.

Implement only one Phase 4 task at a time.

Keep NeoForge event handlers thin.
Keep genetics logic in common services.
Do not implement analyzer UI, production, apiary, resource bees, Fabric support, or polished assets during Phase 4.
