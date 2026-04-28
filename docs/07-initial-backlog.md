# 07 — Initial Backlog

## 1. Purpose

This backlog breaks the bee genetics mod into implementation-friendly tasks.

It is designed for AI-assisted development, where each task should be small enough for Claude Code, Codex, Cursor, or another agent to implement safely.

## 2. Epics

```txt
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
Epic 11 — Fabric Support
```

## 3. Epic 1 — Project Documentation

### US-001 — Add project documentation structure

As a developer,  
I want the repository to contain clear documentation files,  
so AI agents and humans can understand the intended architecture and roadmap.

Acceptance criteria:

```txt
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
```

### US-002 — Add AI agent guidance

As a developer,  
I want a CLAUDE.md file at the repository root,  
so Claude Code understands the project constraints before coding.

Acceptance criteria:

```txt
- CLAUDE.md summarizes project goal.
- CLAUDE.md states non-goals.
- CLAUDE.md points to docs.
- CLAUDE.md forbids Minecraft dependencies in core genetics.
- CLAUDE.md recommends the first implementation prompt.
```

## 4. Epic 2 — Genetics Core

### US-003 — Model dominance

As a system,  
I want to represent allele dominance,  
so active traits can be resolved from gene pairs.

Acceptance criteria:

```txt
- Dominance enum exists.
- DOMINANT and RECESSIVE values exist.
- Unit tests cover dominance values where useful.
```

### US-004 — Model alleles

As a system,  
I want to represent a genetic allele,  
so chromosomes can contain inherited values.

Acceptance criteria:

```txt
- Allele has a stable ID.
- Allele has a chromosome type.
- Allele has dominance.
- Allele can be compared by ID/type.
- No Minecraft classes are used.
```

### US-005 — Model chromosome types

As a system,  
I want to define chromosome types,  
so genomes can organize different genetic traits.

Acceptance criteria:

```txt
- ChromosomeType enum exists.
- MVP types exist:
  - SPECIES
  - LIFESPAN
  - PRODUCTIVITY
  - FERTILITY
  - FLOWER_TYPE
```

### US-006 — Model gene pairs

As a system,  
I want a gene pair containing two alleles,  
so a chromosome can carry active and inactive genetic values.

Acceptance criteria:

```txt
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

As a system,  
I want a genome made of gene pairs,  
so each bee can have a complete set of genetic traits.

Acceptance criteria:

```txt
- Genome stores gene pairs by chromosome type.
- Genome can get required chromosomes.
- Genome validates missing required chromosomes.
- Genome can expose species gene pair.
- Genome can expose hybrid/purebred status.
- No Minecraft classes are used.
```

### US-008 — Implement breeding service

As a system,  
I want to cross two genomes,  
so offspring can inherit one allele from each parent per chromosome.

Acceptance criteria:

```txt
- BreedingService accepts two parent genomes.
- For each chromosome, child receives one allele from each parent.
- Allele selection is 50/50 per parent.
- Child gene pairs resolve active/inactive alleles.
- BreedingResult returns child genome.
- Tests cover pure parent crosses.
- Tests cover hybrid parent crosses.
- Statistical tests validate approximate 25/50/25 distribution.
```

### US-009 — Add random abstraction

As a developer,  
I want genetic randomness to be injectable,  
so inheritance and mutation can be tested deterministically.

Acceptance criteria:

```txt
- GeneticRandom interface or equivalent exists.
- Production implementation wraps Java random.
- Test implementation can return deterministic values.
- Core services depend on abstraction instead of global random.
```

## 5. Epic 3 — Initial Content Definitions

### US-010 — Define built-in species model

As a system,  
I want species definitions,  
so species alleles have metadata and gameplay identity.

Acceptance criteria:

```txt
- BeeSpeciesDefinition exists.
- Species definition has ID.
- Species definition has display name.
- Species definition has dominance.
- Species definition can define default trait tendencies.
- No Minecraft registry dependency is required.
```

### US-011 — Add initial wild species

As a player,  
I want starter bees in the world,  
so I can begin breeding.

Acceptance criteria:

```txt
- Meadow species exists.
- Forest species exists.
- Arid species exists.
- Each has default traits.
- Each has intended biome/spawn context metadata or placeholder.
```

### US-012 — Add initial mutated species

As a player,  
I want early mutations,  
so breeding can produce new species.

Acceptance criteria:

```txt
- Cultivated species exists.
- Hardy species exists.
- Each has default traits.
- Each has intended progression role.
```

### US-013 — Add initial trait alleles

As a system,  
I want initial alleles for MVP traits,  
so genomes can represent more than species.

Acceptance criteria:

```txt
- Lifespan alleles exist:
  - Short
  - Normal
  - Long
- Productivity alleles exist:
  - Slow
  - Normal
  - Fast
- Fertility alleles exist:
  - One
  - Two
  - Three
- FlowerType alleles exist:
  - Flowers
  - Cactus
  - Leaves
```

## 6. Epic 4 — Mutation System

### US-014 — Model mutation definitions

As a system,  
I want mutation definitions,  
so species can appear probabilistically during breeding.

Acceptance criteria:

```txt
- MutationDefinition exists.
- Mutation has ID.
- Mutation has two parent species IDs.
- Mutation has result species ID.
- Mutation has base chance.
- Mutation can match parents regardless of order.
- Invalid chance values are rejected.
```

### US-015 — Implement mutation service

As a system,  
I want to apply mutation rules after inheritance,  
so new species can appear during breeding.

Acceptance criteria:

```txt
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

As a player,  
I want early crossbreeding paths,  
so I can discover new species.

Acceptance criteria:

```txt
- Meadow + Forest -> Cultivated exists.
- Forest + Arid -> Hardy exists.
- Parent order does not matter.
- Mutation chances are configurable in built-in definitions.
```

## 7. Epic 5 — NeoForge Bee Data Integration

### US-017 — Store genome on vanilla bee entity

As a system,  
I want vanilla Bee entities to carry genomes,  
so living bees can participate in genetic breeding.

Acceptance criteria:

```txt
- Bee entity can store a Genome.
- Genome persists through save/load.
- Existing bees without genome are handled safely.
- NeoForge-specific code is isolated from core genetics.
```

### US-018 — Initialize wild bee genomes

As a player,  
I want naturally spawned bees to have species,  
so the world contains genetic starting points.

Acceptance criteria:

```txt
- Newly spawned bees receive genome if missing.
- Plains/flower-like biomes can produce Meadow.
- Forest-like biomes can produce Forest.
- Desert/savanna-like biomes can produce Arid.
- Unknown biomes use safe fallback.
```

### US-019 — Add debug inspect command

As a developer,  
I want to inspect a bee genome in-game,  
so I can debug breeding and mutation.

Acceptance criteria:

```txt
- Command or debug item can inspect targeted bee.
- Output includes active/inactive species.
- Output includes MVP traits.
- Output includes hybrid/purebred status.
```

## 8. Epic 6 — Vanilla Breeding Integration

### US-020 — Hook into baby bee creation

As a system,  
I want to detect when vanilla bees create a baby,  
so the baby can receive inherited genetics.

Acceptance criteria:

```txt
- NeoForge hook detects baby bee spawn/creation.
- Parent bees are identified if possible.
- Child bee is identified.
- Logic delegates to common services.
```

### US-021 — Assign inherited genome to baby bee

As a player,  
I want baby bees to inherit genetics from parents,  
so breeding has meaningful outcomes.

Acceptance criteria:

```txt
- Parent genomes are read.
- Missing parent genomes are safely initialized.
- BreedingService creates child genome.
- MutationService is applied.
- Child genome is stored on baby bee.
- Result persists through save/load.
```

### US-022 — Add mutation feedback

As a player,  
I want to notice when a mutation occurs,  
so rare breeding results feel exciting.

Acceptance criteria:

```txt
- Mutation event triggers minimal feedback.
- Feedback may be particles, sound, advancement, or debug message.
- Feedback does not require a full GUI.
```

## 9. Epic 7 — Analyzer MVP

### US-023 — Create Bee Analyzer item

As a player,  
I want a Bee Analyzer,  
so I can inspect bee genetics.

Acceptance criteria:

```txt
- Analyzer item exists.
- Analyzer can be used on a bee.
- Analyzer reads bee genome.
- Analyzer displays basic report.
```

### US-024 — Display species genetics

As a player,  
I want to see active and inactive species,  
so I can identify hybrids and purebreds.

Acceptance criteria:

```txt
- Active species is shown.
- Inactive species is shown.
- Purebred/hybrid status is shown.
- Dominance is shown in a simple way.
```

### US-025 — Display trait genetics

As a player,  
I want to see important traits,  
so I can select better bees.

Acceptance criteria:

```txt
- Lifespan is shown.
- Productivity is shown.
- Fertility is shown.
- FlowerType is shown.
- Active/inactive values are distinguishable.
```

## 10. Epic 8 — Basic Production

### US-026 — Define production outputs by species

As a system,  
I want species to define possible products,  
so bees can produce different outputs.

Acceptance criteria:

```txt
- ProductionDefinition exists.
- Active species can define primary output.
- Inactive species can define secondary output.
- Definitions are centralized.
```

### US-027 — Implement basic production calculation

As a system,  
I want production to consider active species and productivity,  
so genetic traits affect gameplay.

Acceptance criteria:

```txt
- Active species determines primary output.
- Inactive species may add secondary output chance.
- Productivity modifies output chance/rate.
- Calculation can be tested without Minecraft.
```

## 11. Epic 9 — Tech Apiary

Future scope.

### US-028 — Create Genetic Apiary block

As a tech-focused player,  
I want a controlled apiary block,  
so I can breed and produce more efficiently.

Acceptance criteria:

```txt
- Not required for MVP.
```

### US-029 — Add frames

As a tech-focused player,  
I want frames,  
so I can influence mutation, productivity, and stability.

Acceptance criteria:

```txt
- Not required for MVP.
```

## 12. Epic 10 — Data-Driven Content

Future scope.

### US-030 — Prepare definitions for JSON loading

As a content designer,  
I want definitions to be JSON-ready,  
so new species and mutations can be added without code changes.

Acceptance criteria:

```txt
- Not required before core gameplay is stable.
```

### US-031 — Implement species JSON loading

Future acceptance criteria:

```txt
- Species can be loaded from data files.
- Invalid definitions produce clear errors.
- Built-in species still work.
```

### US-032 — Implement mutation JSON loading

Future acceptance criteria:

```txt
- Mutations can be loaded from data files.
- Parent/result species are validated.
- Invalid chance values are rejected.
```

## 13. Epic 11 — Fabric Support

Future scope.

### US-033 — Add Fabric platform module

As a developer,  
I want a Fabric implementation,  
so the mod can support Fabric later.

Acceptance criteria:

```txt
- Not started until NeoForge MVP works.
```

### US-034 — Implement Fabric bee genome storage

Future acceptance criteria:

```txt
- Fabric Bee entities can store Genome.
- Data persists through save/load.
- Common genetics core remains unchanged.
```

## 14. Suggested Task Order

Recommended order:

```txt
1. US-001
2. US-002
3. US-003
4. US-004
5. US-005
6. US-006
7. US-007
8. US-009
9. US-008
10. US-010
11. US-011
12. US-012
13. US-013
14. US-014
15. US-015
16. US-016
17. US-017
18. US-018
19. US-019
20. US-020
21. US-021
22. US-022
23. US-023
24. US-024
25. US-025
26. US-026
27. US-027
```

## 15. First Sprint Recommendation

If working alone with AI assistance, the first sprint should focus only on:

```txt
- Documentation setup
- Pure genetics model
- Breeding service
- Mutation model/service
- Unit tests
```

Do not include:

```txt
- Minecraft integration
- Bee entity data
- Analyzer item
- GUI
- Apiary
- Resource bees
```

## 16. Definition of MVP

The MVP is complete when:

```txt
- A vanilla bee can have a genome.
- Wild bees receive starting species.
- Two bees can breed in the world.
- The baby bee inherits genetics from both parents.
- Mutation can produce Cultivated or Hardy.
- The player can inspect the result.
- Species identity affects at least one basic production behavior.
```
