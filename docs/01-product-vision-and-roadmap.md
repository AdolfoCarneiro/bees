# 01 — Product Vision and Roadmap

## 1. Product Vision

Curious Bees brings Forestry-inspired bee genetics to modern Minecraft bees.

The central fantasy is not simply producing resources from bees. The central fantasy is building, selecting, stabilizing, and improving bee lineages through a real genetic system.

The player should feel that every new generation can matter:

- a baby bee may inherit a valuable recessive trait;
- a hybrid may carry hidden potential;
- a rare mutation may appear after several attempts;
- a purebred species should feel like an achievement;
- better tools and tech should improve control, not replace the genetic loop.

## 2. Inspiration

Curious Bees is inspired by:

- Forestry's bee genetics and breeding depth;
- vanilla Minecraft bees as living entities in the world;
- the long-term grind and satisfaction of genetic selection;
- tech progression and automation from classic modded Minecraft.

## 3. What This Mod Is

This mod is:

- a new mod from scratch;
- a genetics-focused bee breeding mod;
- a vanilla-bee-based experience;
- a tech-leaning progression mod;
- a system where mutation and inheritance are probabilistic;
- a mod designed to support future expansion through data-driven content.

## 4. What This Mod Is Not

This mod is not:

- a port of Forestry;
- a fork of Productive Bees;
- a deterministic `A + B = C` recipe system;
- a resource bee mod as its first priority;
- a content-heavy mod before the core system is stable;
- a mod that should start by implementing dozens of bee species;
- a project that should require polished assets before gameplay works.

## 5. Design Pillars

### 5.1 Genetic Depth First

The genetics system is the main product.

If the genetic engine is shallow, the mod fails even if it has many species.

### 5.2 Vanilla Interaction First

The first playable breeding flow should use vanilla-like interaction:

```text
Two bees + flowers -> baby bee
```

The difference is that the baby receives a meaningful genome.

### 5.3 Tech Progression Later

Apiaries, analyzers, frames, centrifuges, automation, and production optimizers should exist, but they should enhance the natural breeding loop instead of replacing it too early.

### 5.4 Small Content, Strong Rules

The MVP should use very few species.

A small number of species with strong genetic behavior is better than many species with shallow deterministic behavior.

### 5.5 Future Multiloader Support

NeoForge 1.21.1 is the first target.

Fabric support is a future target, so platform-specific code should be isolated.

### 5.6 Specs Before Large Implementation

Implementation should be driven by detailed local specs in `docs/implementation/`, not by short backlog summaries alone.

## 6. Core Gameplay Loop

Initial loop:

```text
1. Find bees in the world.
2. Analyze or inspect their genetics.
3. Breed two bees using flowers.
4. A baby bee is born with inherited genes.
5. Sometimes a mutation occurs.
6. Keep promising offspring.
7. Breed again to stabilize traits.
8. Obtain a purebred or valuable hybrid.
9. Use bees for production.
10. Later, improve efficiency through tech blocks and frames.
```

## 7. MVP Scope

The first MVP should prove:

- bees can carry a genome;
- wild bees receive initial species based on their spawn context;
- two bees can breed and produce a baby with inherited genes;
- dominance affects active/inactive traits;
- mutations can produce a new species;
- the player can inspect the result;
- a tiny content set is enough to demonstrate the loop;
- species identity affects at least one basic production behavior.

## 8. Initial Species

Wild species:

```text
Meadow Bee
Forest Bee
Arid Bee
```

Mutated species:

```text
Cultivated Bee
Hardy Bee
```

## 9. Initial Mutations

```text
Meadow + Forest -> Cultivated
Forest + Arid -> Hardy
```

Development defaults:

```text
Meadow + Forest -> Cultivated: 12%
Forest + Arid -> Hardy: 8%
```

These are placeholders, not final balance values.

## 10. Initial Traits

MVP chromosomes:

```text
Species
Lifespan
Productivity
Fertility
FlowerType
```

Possible future chromosomes:

```text
TemperatureTolerance
HumidityTolerance
Territory
Effect
Behavior
Nocturnal
CaveDwelling
WeatherTolerance
```

## 11. Roadmap

### Phase 0 — Documentation and Decisions

Detailed execution spec:

```text
docs/implementation/00-phase-0-documentation-and-decisions.md
```

Deliverables:

- product vision;
- technical architecture;
- genetics specification;
- breeding and mutation specification;
- content design specification;
- AI coding guidelines;
- initial backlog;
- implementation specs;
- ADRs;
- quality/test plans;
- asset pipeline plan;
- release/distribution plan.

Done when:

```text
An AI coding agent can read the repository docs and understand what to build first, what not to build yet, and how to validate each phase.
```

### Phase 1 — Pure Genetics Core

Detailed execution spec:

```text
docs/implementation/01-genetics-core-implementation.md
```

Deliverables:

- Allele model;
- Dominance model;
- Chromosome types;
- Gene pair model;
- Genome model;
- GeneticRandom abstraction;
- Breeding service;
- Mutation service;
- unit and simulation tests.

Done when:

```text
The project can simulate thousands of bee crosses without Minecraft.
```

### Phase 2 — Minimal Hardcoded Content

Detailed execution spec:

```text
docs/implementation/02-initial-content-implementation.md
```

Deliverables:

- Meadow, Forest, Arid, Cultivated, Hardy species definitions;
- Lifespan trait definitions;
- Productivity trait definitions;
- Fertility trait definitions;
- Flower type trait definitions;
- initial mutation definitions;
- built-in content registry/facade.

Done when:

```text
The pure Java core can generate normal offspring, hybrids, purebreds, and mutations using the initial content.
```

### Phase 3 — NeoForge Bee Genome Storage

Detailed execution spec:

```text
docs/implementation/03-neoforge-entity-integration.md
```

Deliverables:

- attach genome data to vanilla bees;
- serialize and deserialize genome data;
- initialize wild bee genomes;
- persist genome through save/load;
- provide debug commands for reading bee genome.

Done when:

```text
A spawned vanilla bee receives a genome and keeps it after saving and reloading the world.
```

### Phase 4 — Vanilla Breeding Integration

Detailed execution spec:

```text
docs/implementation/04-vanilla-breeding-integration.md
```

Deliverables:

- hook into baby bee creation;
- read parent genomes;
- generate child genome through the core;
- apply mutation context;
- save child genome;
- add minimal mutation feedback.

Done when:

```text
Breeding two vanilla bees creates a baby bee with inherited and possibly mutated genetics.
```

### Phase 5 — Basic Analyzer

Detailed execution spec:

```text
docs/implementation/05-analyzer-implementation.md
```

Deliverables:

- analyzer item or debug-first tool;
- active and inactive species display;
- important trait display;
- purebred/hybrid status display;
- simple readable formatting.

Done when:

```text
The player can decide which bees to keep without relying only on debug commands.
```

### Phase 6 — Basic Production

Detailed execution spec:

```text
docs/implementation/06-production-mvp.md
```

Deliverables:

- production definitions by species;
- active species determines primary output;
- inactive species may influence secondary output;
- productivity affects speed or output chance;
- basic comb items or placeholder outputs.

Done when:

```text
Species identity matters beyond breeding.
```

### Phase 7 — Tech Apiary and Frames

Detailed execution spec:

```text
docs/implementation/07-tech-apiary-and-automation.md
```

Deliverables:

- Genetic Apiary;
- basic frames;
- mutation frames;
- productivity frames;
- optional centrifuge;
- controlled breeding and production;
- automation hooks;
- asset/Blockbench workflow for custom blocks.

Done when:

```text
Players can continue breeding naturally or use tech blocks for more efficient genetic progression.
```

### Phase 8 — Data-Driven Content

Detailed execution spec:

```text
docs/implementation/08-data-driven-content.md
```

Deliverables:

- species data format;
- mutation data format;
- production data format;
- trait data format;
- validation and error reporting;
- built-in + loaded definition merge strategy.

Done when:

```text
Adding a new species does not require recompiling the mod.
```

### Phase 9 — Expanded Content Roadmap

Detailed planning spec:

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

Done when:

```text
The expansion plan is clear enough to grow content without damaging the core genetic system.
```

### Phase 10 — Fabric Support

Detailed execution spec:

```text
docs/implementation/10-fabric-support-implementation.md
```

Deliverables:

- Fabric feasibility spike;
- multiloader build decision;
- Fabric module setup;
- Fabric genome storage;
- Fabric wild bee initialization;
- Fabric breeding hook/mixin;
- Fabric analyzer behavior;
- cross-loader behavior parity tests.

Done when:

```text
Fabric behavior matches the NeoForge MVP without duplicating genetics logic.
```

## 12. Transversal Documentation

The roadmap is supported by additional documentation:

```text
docs/decisions/   -> ADRs and architectural decisions
docs/quality/     -> testing and validation plans
docs/art/         -> asset pipeline and Blockbench planning
docs/release/     -> release and distribution planning
```

## 13. Risks

### Risk: Too Much Content Too Early

Mitigation:

```text
Keep the first playable version to five species.
```

### Risk: Minecraft Integration Before Core Stability

Mitigation:

```text
Implement and test the genetics core before touching entities, items, blocks, and events.
```

### Risk: Multiloader Complexity Too Early

Mitigation:

```text
Design platform boundaries now, but implement NeoForge first.
```

### Risk: Analyzer UX Becoming Too Complex

Mitigation:

```text
Start with simple text/tooltip output. Build GUI later.
```

### Risk: Data-Driven Loading Delays the Core

Mitigation:

```text
Start with hardcoded definitions in centralized registries. Move to JSON after rules stabilize.
```

### Risk: Assets Blocking Development

Mitigation:

```text
Use placeholders. Blockbench and polished assets are not required before gameplay works.
```

## 14. Open Questions

These do not block the MVP:

- final species names;
- whether resource bees use direct resource names or more thematic names;
- whether mutation discovery should be hidden, partially hidden, or analyzer-tier-based;
- how much production should happen through vanilla hives versus custom tech blocks;
- whether apiaries should use living bees, captured bee items, or both;
- how to balance bee lifespan without making vanilla breeding frustrating.
