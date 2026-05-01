> Status: MVP foundation document.
>
> This document describes the original MVP design used to validate the Curious Bees core loop.
> It is preserved as historical and architectural context.
> For the current post-MVP direction, see:
> `docs/post-mvp/11-post-mvp-productization-roadmap.md`.

# 07 — Initial Backlog

## 1. Purpose

This backlog breaks Curious Bees into implementation-friendly tasks.

It is designed for AI-assisted development, where each task should be small enough for Claude Code, Codex, Cursor, or another agent to implement safely.

This is a **high-level planning backlog**.

Detailed execution instructions live in:

```text
docs/implementation/
```

Testing and validation instructions live in:

```text
docs/quality/
```

Architectural decisions live in:

```text
docs/decisions/
```

Do not use this backlog as the only prompt for coding.

## 2. Backlog Status Model

Recommended statuses:

```text
Not Started
Refining
Waiting Dependency
Next
Ready for AI
In Progress
Review
Blocked
Done
```

Meaning:

```text
Refining = needs more specification
Waiting Dependency = defined, but depends on another task/decision
Next = next real candidate to execute
Ready for AI = can be sent to Claude Code/Codex/Cursor now
In Progress = actively being worked
Review = generated/implemented and needs validation
Blocked = cannot continue until something is resolved
Done = reviewed and accepted
```

## 3. Epics

```text
Epic 1 — Project Documentation
Epic 2 — Genetics Core
Epic 3 — Initial Content Definitions
Epic 4 — Mutation System
Epic 5 — NeoForge Bee Data Integration
Epic 6 — Vanilla Breeding Integration
Epic 7 — Analyzer MVP
Epic 8 — Basic Production
Epic 9 — Tech Apiary
Epic 10 — Data-Driven Content
Epic 11 — Expanded Content
Epic 12 — Fabric Support
```

## 4. Phase-Based Implementation Docs

Use these files as execution sources:

```text
docs/implementation/00-phase-0-documentation-and-decisions.md
docs/implementation/01-genetics-core-implementation.md
docs/implementation/02-initial-content-implementation.md
docs/implementation/03-neoforge-entity-integration.md
docs/implementation/04-vanilla-breeding-integration.md
docs/implementation/05-analyzer-implementation.md
docs/implementation/06-production-mvp.md
docs/implementation/07-tech-apiary-and-automation.md
docs/implementation/08-data-driven-content.md
docs/implementation/09-expanded-content-roadmap.md
docs/implementation/10-fabric-support-implementation.md
```

## 5. Epic 1 — Project Documentation

### US-001 — Add project documentation structure

As a developer,  
I want the repository to contain clear documentation files,  
so AI agents and humans can understand the intended architecture and roadmap.

Acceptance criteria:

```text
- README.md exists.
- CLAUDE.md exists.
- docs folder exists.
- Initial product vision document exists.
- Technical architecture document exists.
- Genetics spec exists.
- Breeding/mutation spec exists.
- Content design spec exists.
- AI coding guidelines exist.
- Initial backlog exists.
- Implementation specs exist.
- ADRs exist.
- Quality/test plans exist.
- Asset pipeline plan exists.
- Release/distribution plan exists.
```

### US-002 — Add AI agent guidance

As a developer,  
I want a CLAUDE.md file at the repository root,  
so Claude Code understands the project constraints before coding.

Acceptance criteria:

```text
- CLAUDE.md summarizes project goal.
- CLAUDE.md states non-goals.
- CLAUDE.md points to docs.
- CLAUDE.md points to docs/implementation.
- CLAUDE.md points to docs/decisions and docs/quality.
- CLAUDE.md forbids Minecraft dependencies in core genetics.
- CLAUDE.md requires one task at a time.
```

## 6. Epic 2 — Genetics Core

Execution spec:

```text
docs/implementation/01-genetics-core-implementation.md
```

### US-003 — Model dominance

Acceptance criteria:

```text
- Dominance enum exists.
- DOMINANT and RECESSIVE values exist.
- Unit tests cover dominance values where useful.
```

### US-004 — Model alleles

Acceptance criteria:

```text
- Allele has a stable ID.
- Allele has a chromosome type.
- Allele has dominance.
- Allele can be compared by ID/type.
- No Minecraft classes are used.
```

### US-005 — Model chromosome types

Acceptance criteria:

```text
- ChromosomeType enum exists.
- MVP types exist:
  - SPECIES
  - LIFESPAN
  - PRODUCTIVITY
  - FERTILITY
  - FLOWER_TYPE
```

### US-006 — Model gene pairs

Acceptance criteria:

```text
- GenePair stores two alleles.
- GenePair stores active allele.
- GenePair stores inactive allele.
- Dominant allele becomes active over recessive allele.
- Equal dominance resolves active allele using random.
- Active/inactive result is persisted.
- GenePair can report purebred/hybrid.
- Tests cover all rules.
```

### US-007 — Model genomes

Acceptance criteria:

```text
- Genome stores gene pairs by chromosome type.
- Genome can get required chromosomes.
- Genome validates missing required chromosomes.
- Genome can expose species gene pair.
- Genome can expose hybrid/purebred status.
- No Minecraft classes are used.
```

### US-008 — Add random abstraction

Acceptance criteria:

```text
- GeneticRandom interface or equivalent exists.
- Production implementation wraps Java random.
- Test implementation can return deterministic values.
- Core services depend on abstraction instead of global random.
```

### US-009 — Implement breeding service

Acceptance criteria:

```text
- BreedingService accepts two parent genomes.
- For each chromosome, child receives one allele from each parent.
- Allele selection is 50/50 per parent.
- Child gene pairs resolve active/inactive alleles.
- BreedingResult returns child genome.
- Tests cover pure parent crosses.
- Tests cover hybrid parent crosses.
- Statistical tests validate approximate 25/50/25 distribution.
```

## 7. Epic 3 — Initial Content Definitions

Execution spec:

```text
docs/implementation/02-initial-content-implementation.md
```

### US-010 — Define built-in species model

Acceptance criteria:

```text
- BeeSpeciesDefinition exists.
- Species definition has ID.
- Species definition has display name.
- Species definition has dominance.
- Species definition can define default trait tendencies.
- No Minecraft registry dependency is required.
```

### US-011 — Add initial wild species

Acceptance criteria:

```text
- Meadow species exists.
- Forest species exists.
- Arid species exists.
- Each has default traits.
- Each has intended biome/spawn context metadata or placeholder.
```

### US-012 — Add initial mutated species

Acceptance criteria:

```text
- Cultivated species exists.
- Hardy species exists.
- Each has default traits.
- Each has intended progression role.
```

### US-013 — Add initial trait alleles

Acceptance criteria:

```text
- Lifespan alleles exist: Short, Normal, Long.
- Productivity alleles exist: Slow, Normal, Fast.
- Fertility alleles exist: One, Two, Three.
- FlowerType alleles exist: Flowers, Cactus, Leaves.
```

## 8. Epic 4 — Mutation System

Execution specs:

```text
docs/implementation/01-genetics-core-implementation.md
docs/implementation/02-initial-content-implementation.md
```

### US-014 — Model mutation definitions

Acceptance criteria:

```text
- MutationDefinition exists.
- Mutation has ID.
- Mutation has two parent species IDs.
- Mutation has result species ID.
- Mutation has base chance.
- Mutation can match parents regardless of order.
- Invalid chance values are rejected.
```

### US-015 — Implement mutation service

Acceptance criteria:

```text
- MutationService accepts parent genomes, child genome, context, and mutation definitions.
- MutationService checks active parent species.
- MutationService evaluates chance.
- MutationService can return no mutation.
- MutationService can return partial mutation.
- MutationService can return full mutation.
- Tests cover 0% chance.
- Tests cover 100% chance.
```

### US-016 — Add initial mutation definitions

Acceptance criteria:

```text
- Meadow + Forest -> Cultivated exists.
- Forest + Arid -> Hardy exists.
- Parent order does not matter.
- Mutation chances are configurable in built-in definitions.
```

## 9. Epic 5 — NeoForge Bee Data Integration

Execution spec:

```text
docs/implementation/03-neoforge-entity-integration.md
```

### US-017 — Research NeoForge genome storage

Acceptance criteria:

```text
- Storage API decision is documented.
- Serialization requirements are documented.
- Sync requirements are documented.
- Risks are documented.
```

### US-018 — Store genome on vanilla bee entity

Acceptance criteria:

```text
- Bee entity can store a Genome.
- Genome persists through save/load.
- Existing bees without genome are handled safely.
- NeoForge-specific code is isolated from core genetics.
```

### US-019 — Initialize wild bee genomes

Acceptance criteria:

```text
- Newly spawned bees receive genome if missing.
- Plains/flower-like biomes can produce Meadow.
- Forest-like biomes can produce Forest.
- Desert/savanna-like biomes can produce Arid.
- Unknown biomes use safe fallback.
```

### US-020 — Add debug inspect command

Acceptance criteria:

```text
- Command or debug item can inspect targeted bee.
- Output includes active/inactive species.
- Output includes MVP traits.
- Output includes hybrid/purebred status.
```

## 10. Epic 6 — Vanilla Breeding Integration

Execution spec:

```text
docs/implementation/04-vanilla-breeding-integration.md
```

### US-021 — Research baby bee creation hook

Acceptance criteria:

```text
- NeoForge hook candidates are researched.
- Parent availability is documented.
- Child modification timing is documented.
- Fallback options are documented.
```

### US-022 — Hook into baby bee creation

Acceptance criteria:

```text
- NeoForge hook detects baby bee spawn/creation.
- Parent bees are identified if possible.
- Child bee is identified.
- Logic delegates to common services.
```

### US-023 — Assign inherited genome to baby bee

Acceptance criteria:

```text
- Parent genomes are read.
- Missing parent genomes are safely initialized.
- BreedingService creates child genome.
- MutationService is applied.
- Child genome is stored on baby bee.
- Result persists through save/load.
```

### US-024 — Add mutation feedback

Acceptance criteria:

```text
- Mutation event triggers minimal feedback.
- Feedback may be particles, sound, advancement, or debug message.
- Feedback does not require a full GUI.
```

## 11. Epic 7 — Analyzer MVP

Execution spec:

```text
docs/implementation/05-analyzer-implementation.md
```

### US-025 — Create Bee Analyzer item

Acceptance criteria:

```text
- Analyzer item exists.
- Analyzer can be used on a bee.
- Analyzer reads bee genome.
- Analyzer displays basic report.
```

### US-026 — Display species genetics

Acceptance criteria:

```text
- Active species is shown.
- Inactive species is shown.
- Purebred/hybrid status is shown.
- Dominance is shown in a simple way.
```

### US-027 — Display trait genetics

Acceptance criteria:

```text
- Lifespan is shown.
- Productivity is shown.
- Fertility is shown.
- FlowerType is shown.
- Active/inactive values are distinguishable.
```

## 12. Epic 8 — Basic Production

Execution spec:

```text
docs/implementation/06-production-mvp.md
```

### US-028 — Define production outputs by species

Acceptance criteria:

```text
- ProductionDefinition exists.
- Active species can define primary output.
- Inactive species can define secondary output.
- Definitions are centralized.
```

### US-029 — Implement basic production calculation

Acceptance criteria:

```text
- Active species determines primary output.
- Inactive species may add secondary output chance.
- Productivity modifies output chance/rate.
- Calculation can be tested without Minecraft.
```

## 13. Epic 9 — Tech Apiary

Execution spec:

```text
docs/implementation/07-tech-apiary-and-automation.md
```

Future scope.

### US-030 — Create Genetic Apiary block

Acceptance criteria:

```text
- Not required for MVP.
```

### US-031 — Add frames

Acceptance criteria:

```text
- Not required for MVP.
```

## 14. Epic 10 — Data-Driven Content

Execution spec:

```text
docs/implementation/08-data-driven-content.md
```

Future scope.

### US-032 — Prepare definitions for JSON loading

Acceptance criteria:

```text
- Not required before core gameplay is stable.
```

### US-033 — Implement species JSON loading

Future acceptance criteria:

```text
- Species can be loaded from data files.
- Invalid definitions produce clear errors.
- Built-in species still work.
```

### US-034 — Implement mutation JSON loading

Future acceptance criteria:

```text
- Mutations can be loaded from data files.
- Parent/result species are validated.
- Invalid chance values are rejected.
```

## 15. Epic 11 — Expanded Content

Execution roadmap:

```text
docs/implementation/09-expanded-content-roadmap.md
```

Future categories:

```text
Biome Bees
Nether Bees
End Bees
Resource Bees
Industrial Bees
Magic Bees
Compatibility Bees
```

Do not add expanded content before the MVP loop is working.

## 16. Epic 12 — Fabric Support

Execution spec:

```text
docs/implementation/10-fabric-support-implementation.md
```

Future scope.

### US-035 — Add Fabric platform module

Acceptance criteria:

```text
- Not started until NeoForge MVP works.
```

### US-036 — Implement Fabric bee genome storage

Future acceptance criteria:

```text
- Fabric Bee entities can store Genome.
- Data persists through save/load.
- Common genetics core remains unchanged.
```

## 17. Suggested Task Order

Recommended order:

```text
1. Phase 0 documentation and decisions
2. Genetics core package structure
3. Dominance / ChromosomeType / Allele
4. GeneticRandom
5. GenePair
6. Genome
7. BreedingService
8. MutationDefinition / MutationService
9. Initial species/traits/mutations
10. Built-in content registry
11. Core simulation tests
12. NeoForge genome storage research
13. NeoForge genome storage implementation
14. Wild bee genome initialization
15. Debug inspect command
16. Baby bee breeding hook research
17. Vanilla breeding integration
18. Mutation feedback
19. Analyzer MVP
20. Production MVP
```

## 18. First Sprint Recommendation

If working alone with AI assistance, the first sprint should focus only on:

```text
- documentation setup;
- pure genetics model;
- breeding service;
- mutation model/service;
- unit tests.
```

Do not include:

```text
- Minecraft integration;
- Bee entity data;
- Analyzer item;
- GUI;
- Apiary;
- Resource bees.
```

## 19. Definition of MVP

The MVP is complete when:

```text
- A vanilla bee can have a genome.
- Wild bees receive starting species.
- Two bees can breed in the world.
- The baby bee inherits genetics from both parents.
- Mutation can produce Cultivated or Hardy.
- The player can inspect the result.
- Species identity affects at least one basic production behavior.
```
