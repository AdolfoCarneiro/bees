# 01 — Product Vision and Roadmap

## 1. Product Vision

This mod brings Forestry-inspired bee genetics to modern Minecraft bees.

The central fantasy is not simply producing resources from bees. The central fantasy is building, selecting, stabilizing, and improving bee lineages through a real genetic system.

The player should feel that every new generation can matter:

- A baby bee may inherit a valuable recessive trait.
- A hybrid may carry hidden potential.
- A rare mutation may appear after several attempts.
- A purebred species should feel like an achievement.
- Better tools and tech should improve control, not replace the genetic loop.

## 2. Inspiration

The mod is inspired by:

- Forestry's bee genetics and breeding depth.
- Vanilla Minecraft bees as living entities in the world.
- The long-term grind and satisfaction of genetic selection.
- Tech progression and automation from classic modded Minecraft.

## 3. What This Mod Is

This mod is:

- A new mod from scratch.
- A genetics-focused bee breeding mod.
- A vanilla-bee-based experience.
- A tech-leaning progression mod.
- A system where mutation and inheritance are probabilistic.
- A mod designed to support future expansion through data-driven content.

## 4. What This Mod Is Not

This mod is not:

- A port of Forestry.
- A fork of Productive Bees.
- A deterministic "A + B = C" recipe system.
- A resource bee mod as its first priority.
- A content-heavy mod before the core system is stable.
- A mod that should start by implementing dozens of bee species.

## 5. Design Pillars

### 5.1 Genetic Depth First

The genetics system is the main product.

If the genetic engine is shallow, the mod fails even if it has many species.

### 5.2 Vanilla Interaction First

The first playable breeding flow should use vanilla-like interaction:

```txt
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

## 6. Core Gameplay Loop

Initial loop:

```txt
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

- Bees can carry a genome.
- Wild bees receive initial species based on their spawn context.
- Two bees can breed and produce a baby with inherited genes.
- Dominance affects active/inactive traits.
- Mutations can produce a new species.
- The player can inspect the result.
- A tiny content set is enough to demonstrate the loop.

## 8. Initial Species

Wild species:

```txt
Meadow Bee
Forest Bee
Arid Bee
```

Mutated species:

```txt
Cultivated Bee
Hardy Bee
```

## 9. Initial Mutations

```txt
Meadow + Forest -> Cultivated
Forest + Arid -> Hardy
```

These are not final balance numbers, but useful development defaults:

```txt
Meadow + Forest -> Cultivated: 12%
Forest + Arid -> Hardy: 8%
```

## 10. Initial Traits

MVP chromosomes:

```txt
Species
Lifespan
Productivity
Fertility
FlowerType
```

Possible future chromosomes:

```txt
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

Deliverables:

- Product vision
- Technical architecture
- Genetics specification
- Breeding and mutation specification
- Content design specification
- AI coding guidelines
- Initial backlog

Done when:

```txt
An AI coding agent can read the repository docs and understand what to build first and what not to build yet.
```

### Phase 1 — Pure Genetics Core

Deliverables:

- Allele model
- Dominance model
- Gene pair model
- Genome model
- Chromosome types
- Breeding service
- Mutation service
- Unit tests

Done when:

```txt
The mod can simulate thousands of bee crosses without Minecraft.
```

### Phase 2 — Minimal Hardcoded Content

Deliverables:

- Meadow, Forest, Arid, Cultivated, Hardy species definitions
- Lifespan trait definitions
- Productivity trait definitions
- Fertility trait definitions
- Flower type trait definitions
- Initial mutation definitions

Done when:

```txt
The pure Java core can generate normal offspring, hybrids, purebreds, and mutations using the initial content.
```

### Phase 3 — NeoForge Bee Genome Storage

Deliverables:

- Attach genome data to vanilla bees.
- Initialize wild bee genomes.
- Persist genome through save/load.
- Provide debug commands for reading bee genome.

Done when:

```txt
A spawned vanilla bee receives a genome and keeps it after saving and reloading the world.
```

### Phase 4 — Vanilla Breeding Integration

Deliverables:

- Hook into baby bee creation.
- Read parent genomes.
- Generate child genome through the core.
- Apply mutation context.
- Save child genome.
- Add minimal mutation feedback.

Done when:

```txt
Breeding two vanilla bees creates a baby bee with inherited and possibly mutated genetics.
```

### Phase 5 — Basic Analyzer

Deliverables:

- Analyzer item or debug-first tool.
- Show active and inactive species.
- Show important traits.
- Show purebred/hybrid status.
- Show dominance clearly.

Done when:

```txt
The player can decide which bees to keep without relying only on debug commands.
```

### Phase 6 — Basic Production

Deliverables:

- Production definitions by species.
- Active species determines primary output.
- Inactive species may influence secondary output.
- Productivity affects speed or output chance.
- Basic comb items.

Done when:

```txt
Species identity matters beyond breeding.
```

### Phase 7 — Tech Apiary and Frames

Deliverables:

- Genetic Apiary
- Basic frames
- Mutation frames
- Productivity frames
- Optional centrifuge
- More controlled breeding/production

Done when:

```txt
Players can continue breeding naturally or use tech blocks for more efficient genetic progression.
```

### Phase 8 — Data-Driven Content

Deliverables:

- Species JSON
- Mutation JSON
- Production JSON
- Trait JSON
- Validation and error reporting

Done when:

```txt
Adding a new species does not require recompiling the mod.
```

### Phase 9 — Expanded Content

Future categories:

```txt
Biome Bees
Nether Bees
End Bees
Resource Bees
Industrial Bees
Magic Bees
Compatibility Bees
```

Done when:

```txt
The genetic system supports a larger progression tree without changing core mechanics.
```

## 12. Risks

### Risk: Too Much Content Too Early

Mitigation:

```txt
Keep the first playable version to five species.
```

### Risk: Minecraft Integration Before Core Stability

Mitigation:

```txt
Implement and test the genetics core before touching entities, items, blocks, and events.
```

### Risk: Multiloader Complexity Too Early

Mitigation:

```txt
Design platform boundaries now, but implement NeoForge first.
```

### Risk: Analyzer UX Becoming Too Complex

Mitigation:

```txt
Start with simple text/tooltip output. Build GUI later.
```

### Risk: Data-Driven Loading Delays the Core

Mitigation:

```txt
Start with hardcoded definitions in centralized registries. Move to JSON after rules stabilize.
```

## 13. Open Questions

These do not block the MVP:

- Final species names.
- Whether resource bees use direct resource names or more thematic names.
- Whether mutation discovery should be fully hidden, partially hidden, or analyzer-tier-based.
- How much production should happen through vanilla hives versus custom tech blocks.
- Whether apiaries should use living bees, captured bee items, or both.
- How to balance bee lifespan without making vanilla breeding frustrating.
