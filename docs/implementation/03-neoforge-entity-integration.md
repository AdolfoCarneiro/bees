# 03 — NeoForge Bee Data Integration Implementation

## 1. Purpose

This document defines the implementation plan for **Phase 3 — NeoForge Bee Data Integration**.

The goal of this phase is to make vanilla Minecraft `Bee` entities carry, persist, and expose Curious Bees genome data in NeoForge 1.21.1.

This is the first phase where the pure Java genetics core touches Minecraft through a platform adapter.

## 2. Phase Summary

Phase 3 connects the already-tested common genetics model to living vanilla bees.

By the end of this phase:

```text
A vanilla Bee entity can have a Curious Bees Genome.
The genome persists through save/load.
Naturally spawned bees can receive starter genomes.
A developer can inspect bee genomes in-game through debug tooling.
Common genetics code still has no NeoForge/Minecraft dependency.
```

## 3. Required Previous Phases

Before starting this phase, the following should be completed or consciously accepted as stable enough:

```text
Phase 0 — Documentation and Decisions
Phase 1 — Genetics Core
Phase 2 — Initial Content Definitions
```

Minimum required from Phase 1:

```text
Allele
Dominance
ChromosomeType
GenePair
Genome
GeneticRandom
```

Minimum required from Phase 2:

```text
Built-in species definitions
Built-in trait allele definitions
Default wild genome creation
At least Meadow, Forest, Arid species
```

Mutation does not strictly need to be fully integrated before basic storage, but the model should not make mutation difficult later.

## 4. Phase Goals

### 4.1 Store Genome Data on Vanilla Bees

Vanilla `Bee` entities must be able to carry a `Genome`.

The storage must:

```text
persist through world save/load
survive entity unload/reload
be safe for existing bees without genome data
avoid recalculating random active/inactive state
keep platform-specific code out of common genetics
```

### 4.2 Provide a Platform Adapter

NeoForge code should expose a small API to the rest of the mod.

Example conceptual responsibilities:

```text
get genome from Bee
set genome on Bee
check if Bee has genome
initialize genome if missing
serialize genome
deserialize genome
```

### 4.3 Initialize Wild Bees

Naturally spawned bees should receive starting genomes based on context.

Initial mapping:

```text
Plains / Flower Forest / meadow-like biomes -> Meadow
Forest / Birch Forest / dark forest-like biomes -> Forest
Desert / Savanna / dry biomes -> Arid
Unknown / unsupported biome -> safe fallback
```

### 4.4 Add Debug Inspection

Before building the player-facing Bee Analyzer item, the project needs debug tooling.

The debug tool should allow the developer to inspect a bee genome in-game.

Possible MVP:

```text
/debug command
or
temporary debug item
or
log output on interaction
```

Recommended MVP:

```text
command or simple developer-only inspection behavior
```

## 5. Non-Goals

Do not implement in this phase:

```text
vanilla breeding inheritance
mutation during baby creation
Bee Analyzer item
production outputs
custom bee entity
custom bee renderer
custom bee textures
apiary block
frames
GUI screens
Fabric support
JSON/datapack loading
resource bees
```

Phase 3 is about **data storage, initialization, persistence, and debug visibility** only.

## 6. Architecture Rules

## 6.1 Common Genetics Must Stay Pure

The following modules/classes must not import NeoForge or Minecraft classes:

```text
common/genetics
common/content
common/gameplay where intended to stay platform-neutral
```

Do not introduce these imports into common genetics:

```text
net.minecraft.*
net.neoforged.*
Bee
Entity
Level
ResourceLocation
CompoundTag
AttachmentType
DeferredRegister
Event
CommandSourceStack
```

If a Minecraft type is needed, the code belongs in the NeoForge module or adapter layer.

## 6.2 Event Handlers Should Be Thin

NeoForge event handlers should not contain core genetic rules.

Good:

```text
event handler -> platform adapter -> common service/factory -> store result
```

Bad:

```text
event handler implements species selection, genome creation, mutation, and formatting directly
```

## 6.3 Storage Should Preserve Active/Inactive State

The genome serializer must preserve enough information to reconstruct the same genome.

It must not deserialize a genome and randomly resolve active/inactive alleles again.

Required preservation:

```text
chromosome type
first allele id
second allele id
active allele id
inactive allele id
dominance metadata or resolvable allele definitions
```

Depending on final implementation, allele definitions may be restored from built-in registries by ID.

## 7. Recommended Implementation Units

Phase 3 should be implemented in small tasks.

Recommended order:

```text
1. Research NeoForge entity genome storage
2. Decide genome serialization format
3. Implement genome codec/serializer adapter
4. Implement NeoForge Bee genome attachment/storage
5. Add tests for serialization if feasible
6. Implement wild bee genome initialization
7. Add debug command to inspect targeted bee genome
8. Add in-game persistence validation checklist
9. Review Phase 3 before breeding integration
```

## 8. Verification Gates

This phase has mandatory checkpoints.

Do not continue to later tasks until each gate is reviewed.

## Verification Gate 1 — Storage API Decision

Stop after the research task and document:

```text
Which NeoForge mechanism stores genome data on Bee entities?
Does it persist automatically?
How is it serialized?
Does it need client sync?
How are missing or invalid genomes handled?
```

Expected output:

```text
docs/decisions/ADR-003-neoforge-bee-genome-storage.md
```

or an equivalent decision section in this document.

Do not implement attachment/storage code until this decision is clear.

## Verification Gate 2 — Serialization Review

Stop after defining serialization.

Review:

```text
Can a Genome be serialized without losing active/inactive state?
Can a Genome be deserialized without rerolling dominance?
Can unknown allele IDs fail clearly?
Can old/invalid data degrade safely in gameplay?
```

Do not attach genomes to live bees until serialization is trustworthy.

## Verification Gate 3 — Attachment Persistence Review

Stop after implementing storage.

Validate in-game:

```text
spawn or find a bee
assign genome
save world
reload world
inspect bee
confirm same genome remains
```

Do not implement wild spawn initialization until manual persistence validation succeeds.

## Verification Gate 4 — Wild Initialization Review

Stop after wild initialization.

Validate:

```text
new bees get genomes
existing genetic bees are not overwritten
fallback works for unknown biome
biome mapping is understandable
```

Do not implement breeding integration until wild initialization is stable.

## Verification Gate 5 — Debug Inspection Review

Stop after debug inspection.

Validate:

```text
debug output shows active/inactive species
debug output shows core traits
missing genome does not crash
invalid genome data produces useful warning
```

Do not start analyzer item until debug inspection proves genome data can be read reliably.

## Verification Gate 6 — Phase 3 Exit Review

Before moving to Phase 4, confirm:

```text
Bee genome storage works
Genome save/load works
Wild initialization works
Debug inspection works
Common genetics still has no Minecraft/NeoForge imports
No breeding logic was implemented prematurely
```

## 9. Task 1 — Research NeoForge Entity Genome Storage

## Objective

Research the correct NeoForge 1.21.1 mechanism for storing a custom `Genome` on vanilla `Bee` entities.

## Scope

Investigate:

```text
entity data attachments or equivalent
persistence behavior
serialization requirements
client sync requirements
where registration should happen
how to read/write the data safely
```

## Non-Goals

Do not implement code yet.

Do not create:

```text
events
commands
items
breeding hooks
analyzer
production
```

## Questions to Answer

```text
What is the recommended NeoForge 1.21.1 API for attaching custom persistent data to vanilla entities?
How should the Genome be serialized?
Is automatic persistence available?
Is client sync required for MVP?
What happens when an entity lacks the attachment?
What happens when data fails to deserialize?
How should this be isolated from common code?
```

## Acceptance Criteria

```text
- Recommended storage approach is documented.
- Alternatives are documented.
- Persistence behavior is understood.
- Serialization approach is understood.
- Sync requirements are documented.
- Implementation task is clear.
```

## Expected Output

Create or update a decision note:

```text
docs/decisions/ADR-003-neoforge-bee-genome-storage.md
```

Suggested sections:

```text
Context
Decision
Alternatives considered
Persistence behavior
Serialization strategy
Client sync strategy
Risks
Implementation notes
```

## Prompt for Claude Code

```text
Read CLAUDE.md and the docs folder.

Focus only on: Research NeoForge entity genome storage.

Do not implement code yet.

Research the recommended NeoForge 1.21.1 mechanism for storing custom persistent data on vanilla Bee entities.

Answer:
- which API/mechanism should be used;
- how persistence works;
- how serialization should work;
- whether client sync is needed for the MVP;
- how common genetics code stays free of NeoForge/Minecraft dependencies;
- risks and alternatives.

Write the result as a decision note in docs/decisions/ADR-003-neoforge-bee-genome-storage.md.
```

## 10. Task 2 — Decide Genome Serialization Format

## Objective

Define how a `Genome` is encoded for persistent storage.

## Scope

Create a serialization strategy for:

```text
Genome
GenePair
Allele references
active/inactive allele identity
chromosome types
```

The serializer may live in the common layer if it uses only pure Java types, or in a platform adapter if it depends on Minecraft/NeoForge serialization types.

## Recommended Direction

Prefer a platform-neutral representation first.

Conceptual serialized genome:

```text
GenomeData
- Map<ChromosomeType, GenePairData>

GenePairData
- firstAlleleId
- secondAlleleId
- activeAlleleId
- inactiveAlleleId
```

Allele definitions should be restored from built-in content definitions by stable ID.

## Non-Goals

Do not implement:

```text
JSON/datapack loading
network packets
ItemStack genome components
block entity genome storage
Fabric serializer
```

## Acceptance Criteria

```text
- Serialization preserves active/inactive identity.
- Serialization stores stable allele IDs.
- Serialization does not rely on display names.
- Unknown allele IDs are handled clearly.
- Invalid/missing chromosomes are handled clearly.
- Format can evolve later.
```

## Expected Tests

If serializer is pure Java:

```text
serialize and deserialize pure Meadow genome
serialize and deserialize hybrid Meadow/Forest genome
active/inactive identity remains stable
missing species chromosome fails
unknown allele ID fails clearly
```

If serializer is NeoForge-specific:

```text
at least add manual validation checklist
```

## Prompt for Claude Code

```text
Read CLAUDE.md and the genetics/content docs.

Focus only on: Decide Genome Serialization Format.

Define and implement, if appropriate, a serialization adapter that can preserve a Genome's chromosome pairs and active/inactive allele identity.

Do not implement entity attachments, breeding, analyzer, production, Fabric, JSON/datapack loading, or ItemStack storage.

Before coding, summarize whether the serializer will be common/pure Java or NeoForge-specific, and why.
```

## 11. Task 3 — Implement NeoForge Bee Genome Attachment

## Objective

Implement NeoForge-side storage for a `Genome` on vanilla `Bee` entities.

## Scope

Implement:

```text
register storage/attachment type
read genome from Bee
write genome to Bee
check if Bee has genome
clear or replace genome if needed for debug
safe handling of missing genome
```

Potential class names:

```text
NeoForgeBeeGenomeStorage
BeeGenomeAttachmentRegistration
BeeGenomeData
```

Exact names may vary.

## Architecture Requirement

The storage adapter may depend on NeoForge/Minecraft.

The genetics model must not.

Good dependency direction:

```text
neoforge -> common
common -> no neoforge
```

Bad dependency direction:

```text
common -> neoforge
```

## Non-Goals

Do not implement:

```text
wild initialization
breeding inheritance
mutation during spawn
debug command
analyzer item
production
```

## Acceptance Criteria

```text
- A Bee entity can store a Genome.
- Missing genome can be detected.
- Genome can be read safely.
- Genome can be written safely.
- Genome persists through save/load.
- Invalid data handling is documented.
- Common genetics code remains platform-free.
```

## Manual Validation

Run or prepare this checklist:

```text
1. Start dev world.
2. Spawn/find a bee.
3. Assign a test genome through temporary code or command.
4. Save world.
5. Exit world.
6. Reload world.
7. Inspect same bee.
8. Confirm same genome remains.
```

## Prompt for Claude Code

```text
Read CLAUDE.md, docs/mvp/02-technical-architecture.md, docs/mvp/03-genetics-system-spec.md, and the storage ADR.

Focus only on: Implement NeoForge Bee genome attachment.

Implement the NeoForge-side adapter for storing and reading a Curious Bees Genome on vanilla Bee entities.

Do not implement wild spawn initialization, breeding integration, analyzer item, production, Fabric support, or JSON/datapack loading.

Keep common genetics code free of Minecraft/NeoForge imports.

Before coding, summarize the chosen storage mechanism and list files you expect to create or modify.
```

## 12. Task 4 — Add Genome Storage Tests or Validation Helpers

## Objective

Validate genome storage behavior as much as possible.

## Scope

Depending on test environment feasibility, implement:

```text
unit tests for serializer
adapter tests if possible
debug-only validation helper
manual validation docs
```

## Non-Goals

Do not create a full automated Minecraft integration test suite unless the project already supports it.

## Acceptance Criteria

```text
- Serializer tests exist if serializer is pure Java.
- Manual validation checklist exists for attachment persistence.
- Debug logging is clear enough to diagnose failures.
```

## Prompt for Claude Code

```text
Read CLAUDE.md and the current NeoForge genome storage implementation.

Focus only on: Add Genome Storage Tests or Validation Helpers.

Add tests for pure serialization code if possible, and add a manual validation checklist or debug helper for NeoForge persistence.

Do not implement breeding, analyzer, production, Fabric support, or new gameplay features.
```

## 13. Task 5 — Implement Wild Bee Genome Initialization

## Objective

Assign a default genome to vanilla bees that do not already have one.

## Scope

Implement logic that:

```text
detects Bee entities without genome
selects initial species from biome/context
creates default genome using built-in content
stores genome on Bee
does not overwrite existing genome
```

## Initial Species Mapping

Suggested MVP mapping:

```text
Plains / Flower Forest / meadow-like -> Meadow
Forest / Birch Forest / dark forest-like -> Forest
Desert / Savanna / badlands-like -> Arid
Fallback -> Meadow
```

Use biome tags or explicit biome IDs depending on what is simplest and maintainable in NeoForge 1.21.1.

## Non-Goals

Do not implement:

```text
breeding child genome
mutation
analyzer item
production
custom spawn rules
custom bee entity
```

## Architecture

Event/hook code should be thin:

```text
Bee spawn/load event
-> check if bee has genome
-> resolve environment
-> call common/default genome factory
-> store genome
```

Species selection logic may live in common gameplay if it uses abstract biome categories or IDs.

## Acceptance Criteria

```text
- Bees without genome receive a valid genome.
- Bees with genome are not overwritten.
- Meadow/Forest/Arid mapping exists.
- Unknown biome fallback exists.
- Resulting genome includes all MVP chromosomes.
- Common genetics stays platform-free.
```

## Manual Validation

```text
spawn/find bee in plains-like biome -> Meadow genome
spawn/find bee in forest-like biome -> Forest genome
spawn/find bee in dry biome -> Arid genome
reload world -> genome persists
existing bee genome is not replaced
```

## Prompt for Claude Code

```text
Read CLAUDE.md and the relevant docs.

Focus only on: Implement Wild Bee Genome Initialization.

Use the existing NeoForge Bee genome storage adapter and built-in content definitions.

When a vanilla Bee lacks a genome, assign a default wild genome based on biome/context.

Do not implement breeding integration, mutation during baby creation, analyzer item, production, custom bee entity, Fabric support, or JSON/datapack loading.

Before coding, summarize the hook/event you will use and list files you expect to create or modify.
```

## 14. Task 6 — Add Debug Command to Inspect Targeted Bee Genome

## Objective

Create a developer-friendly way to inspect a bee genome in-game before the player-facing analyzer item exists.

## Scope

Implement a debug command or equivalent inspection helper.

Recommended command concept:

```text
/curiousbees debug inspect_bee
```

Behavior:

```text
target the bee the player is looking at
or nearest bee within a small radius
read genome
print active/inactive species
print core traits
print purebred/hybrid status
handle missing genome safely
```

## Non-Goals

Do not implement:

```text
Bee Analyzer item
GUI
mutation prediction
genetic editing
production output
```

## Output Format

Example:

```text
Bee Genome
Species: Meadow / Forest
Purity: Hybrid
Lifespan: Normal / Normal
Productivity: Normal / Fast
Fertility: Two / Two
Flower Type: Flowers / Leaves
```

If no genome:

```text
This bee has no Curious Bees genome.
```

If no bee targeted:

```text
No bee found.
```

## Acceptance Criteria

```text
- Command can inspect a bee genome.
- Missing genome does not crash.
- No bee found does not crash.
- Output includes active/inactive species.
- Output includes MVP traits.
- Output includes purebred/hybrid status.
- Command does not modify genome.
```

## Prompt for Claude Code

```text
Read CLAUDE.md and the current NeoForge genome storage implementation.

Focus only on: Add Debug Command to Inspect Targeted Bee Genome.

Implement a developer/debug command that reads a nearby or targeted Bee entity's Curious Bees genome and prints a readable report.

Do not implement analyzer item, GUI, breeding integration, production, genetic editing, Fabric support, or JSON/datapack loading.

Before coding, summarize the command behavior and list files you expect to create or modify.
```

## 15. Task 7 — Add Debug Command to Assign Test Genome

## Objective

Create a safe development helper for assigning a known genome to a bee.

This is optional but useful for testing persistence, analyzer output, and future breeding integration.

## Scope

Command concept:

```text
/curiousbees debug set_bee_genome meadow
/curiousbees debug set_bee_genome forest
/curiousbees debug set_bee_genome arid
```

It should:

```text
find targeted/nearby bee
create default genome for requested species
store genome
print confirmation
```

## Non-Goals

Do not implement:

```text
survival gameplay genetic editing
player-facing gene modification
full command UI for every trait
production
breeding
```

This command is a debug/development tool only.

## Acceptance Criteria

```text
- Command assigns a known genome to a bee.
- Command supports at least Meadow, Forest, Arid.
- Command handles invalid species ID clearly.
- Command handles no bee found clearly.
- Assigned genome persists through save/load.
```

## Prompt for Claude Code

```text
Read CLAUDE.md and the current NeoForge genome storage and built-in content code.

Focus only on: Add Debug Command to Assign Test Genome.

Implement a debug-only command that assigns a known default genome to a targeted or nearby Bee.

Do not implement player-facing genetic editing, analyzer UI, breeding, production, Fabric support, or JSON/datapack loading.

Before coding, summarize the command behavior and list files you expect to create or modify.
```

## 16. Task 8 — Phase 3 Integration Review

## Objective

Review Phase 3 before moving to vanilla breeding integration.

## Scope

Perform a code and behavior review.

Checklist:

```text
Genome storage works
Genome persistence works
Wild bee initialization works
Debug inspection works
Debug assignment works, if implemented
Common genetics remains platform-free
Event handlers are thin
No breeding logic was implemented early
No analyzer item was implemented early
```

## Acceptance Criteria

```text
- Review notes are documented.
- Any architecture violations are fixed before Phase 4.
- Any missing validation is converted into tasks.
- Phase 4 is not started until storage is trustworthy.
```

## Prompt for Claude Code

```text
Read CLAUDE.md, the architecture docs, and the current Phase 3 implementation.

Do not implement new features yet.

Review the NeoForge Bee genome storage and wild initialization work.

Check:
- common genetics has no Minecraft/NeoForge imports;
- genome active/inactive identity is persisted;
- missing genomes are handled safely;
- event handlers are thin;
- debug commands work without modifying data unexpectedly;
- no breeding/analyzer/production features were implemented early.

Return a review report with required fixes and recommended follow-up tasks.
```

## 17. Phase 3 Completion Checklist

Phase 3 is complete when:

```text
[ ] Storage approach is documented.
[ ] Genome serialization preserves active/inactive identity.
[ ] Bee entity can store a genome.
[ ] Bee genome persists through save/load.
[ ] Missing genome is handled safely.
[ ] Wild bees receive initial genomes.
[ ] Existing genetic bees are not overwritten.
[ ] Debug inspection exists.
[ ] Debug assignment exists or is intentionally deferred.
[ ] Common genetics has no Minecraft/NeoForge imports.
[ ] No breeding integration was implemented early.
[ ] No analyzer item was implemented early.
```

## 18. Go / No-Go Before Phase 4

Move to Phase 4 only if:

```text
YES: Can a vanilla Bee store and retain a Genome?
YES: Can a developer inspect that Genome in-game?
YES: Can wild bees receive starting genomes?
YES: Is common genetics still platform-free?
YES: Is the storage strategy documented?
```

Do not move to Phase 4 if:

```text
NO: Genome persistence is untested.
NO: Active/inactive state is lost after reload.
NO: Missing genome crashes gameplay.
NO: Common genetics imports NeoForge/Minecraft.
NO: Storage code is mixed with breeding logic.
```

## 19. Common AI Failure Modes in This Phase

### 19.1 The Agent Implements Breeding Too Early

Symptom:

```text
Baby bee inheritance logic appears while implementing storage.
```

Fix:

```text
Reject or revert that part. Storage only.
```

### 19.2 The Agent Puts Platform Types in Common Genetics

Symptom:

```text
common/genetics imports net.minecraft or net.neoforged classes.
```

Fix:

```text
Move the code into the NeoForge module or adapter.
```

### 19.3 The Agent Reinitializes Bees Every Time

Symptom:

```text
Bee genome changes on load/spawn because initialization always runs.
```

Fix:

```text
Only initialize if genome is missing.
```

### 19.4 The Agent Recalculates Active Alleles on Deserialize

Symptom:

```text
Same bee changes active species after reload.
```

Fix:

```text
Persist active/inactive identity and restore it explicitly.
```

### 19.5 The Agent Builds an Analyzer GUI

Symptom:

```text
A GUI screen appears during debug inspection work.
```

Fix:

```text
Defer GUI. Use command/chat output.
```

## 20. Recommended Commit Sequence

```text
docs: add NeoForge bee data integration implementation spec
docs: record NeoForge bee genome storage ADR
core: add genome serialization adapter
neoforge: add bee genome attachment storage
test: add genome serialization tests
neoforge: initialize wild bee genomes
neoforge: add debug bee genome inspection command
neoforge: add debug bee genome assignment command
review: validate Phase 3 before breeding integration
```

## 21. Handoff Prompt for Phase 3

Use this only after Phase 1 and Phase 2 are stable.

```text
Read CLAUDE.md and all docs.

Then read:
- docs/implementation/03-neoforge-entity-integration.md
- docs/mvp/02-technical-architecture.md
- docs/mvp/03-genetics-system-spec.md
- docs/mvp/05-content-design-spec.md

Do not implement breeding, analyzer item, production, Fabric support, resource bees, apiary, GUI, or JSON/datapack loading.

Start with the first task in the Phase 3 implementation spec:
Research NeoForge entity genome storage.

Produce the decision note first.
Do not write implementation code until the storage approach is documented.
```
