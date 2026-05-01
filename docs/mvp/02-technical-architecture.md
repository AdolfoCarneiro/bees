> Status: MVP foundation document.
>
> This document describes the original MVP design used to validate the Curious Bees core loop.
> It is preserved as historical and architectural context.
> For the current post-MVP direction, see:
> `docs/post-mvp/11-post-mvp-productization-roadmap.md`.

# 02 — Technical Architecture

## 1. Architecture Goal

The project must support a clean separation between:

1. pure genetics logic;
2. Minecraft-independent gameplay orchestration;
3. NeoForge-specific integration;
4. future Fabric-specific integration;
5. documentation-driven AI implementation.

The most important architectural rule is:

```text
The genetics core must not depend on Minecraft APIs.
```

This allows the most complex logic to be tested quickly and safely.

## 2. Documentation Architecture

The repository documentation is part of the architecture. AI agents should use it as the source of truth before coding.

```text
docs/
├── 01-product-vision-and-roadmap.md
├── 02-technical-architecture.md
├── 03-genetics-system-spec.md
├── 04-breeding-and-mutation-spec.md
├── 05-content-design-spec.md
├── 06-ai-coding-guidelines.md
├── 07-initial-backlog.md
├── implementation/
├── decisions/
├── quality/
├── art/
└── release/
```

### 2.1 Core docs

Core docs describe product intent, architecture, genetics rules, breeding/mutation behavior, content direction, and AI workflow.

### 2.2 Implementation docs

`docs/implementation/` contains execution-level specs for each phase.

Implementation docs are the primary source for coding tasks.

If a backlog item is short, do not rely only on the backlog. Read the relevant implementation spec.

### 2.3 ADRs

`docs/decisions/` contains Architectural Decision Records.

ADRs explain why decisions were made. Do not casually reverse an ADR without creating a new decision record.

### 2.4 Quality docs

`docs/quality/` contains unit test plans, integration test plans, manual validation checklists, and release smoke tests.

### 2.5 Art docs

`docs/art/` contains asset strategy, placeholder policy, Blockbench workflow, and future AI/skill tooling plans.

### 2.6 Release docs

`docs/release/` contains versioning, changelog, publishing, release automation, and distribution plans.

## 3. Recommended Repository Layout

Initial repository layout:

```text
curious-bees/
├── CLAUDE.md
├── README.md
├── docs/
│   ├── 01-product-vision-and-roadmap.md
│   ├── 02-technical-architecture.md
│   ├── 03-genetics-system-spec.md
│   ├── 04-breeding-and-mutation-spec.md
│   ├── 05-content-design-spec.md
│   ├── 06-ai-coding-guidelines.md
│   ├── 07-initial-backlog.md
│   ├── implementation/
│   ├── decisions/
│   ├── quality/
│   ├── art/
│   └── release/
├── common/
│   └── src/
│       ├── main/java/
│       └── test/java/
├── neoforge/
│   └── src/
│       ├── main/java/
│       └── test/java/
├── fabric/
│   └── src/
│       ├── main/java/
│       └── test/java/
├── build.gradle
├── settings.gradle
└── gradle.properties
```

If multiloader setup feels too heavy at the beginning, use this temporary layout:

```text
curious-bees/
├── CLAUDE.md
├── README.md
├── docs/
├── src/
│   ├── main/java/
│   └── test/java/
└── build.gradle
```

However, even in a temporary layout, keep package boundaries clear.

## 4. Conceptual Modules

### 4.1 common/genetics

Pure Java.

No Minecraft imports.

Responsible for:

- alleles;
- gene pairs;
- chromosome types;
- genomes;
- dominance resolution;
- Mendelian inheritance;
- mutation evaluation;
- purebred/hybrid detection;
- genetic randomness abstraction.

Example packages:

```text
com.curiousbees.common.genetics.model
com.curiousbees.common.genetics.breeding
com.curiousbees.common.genetics.mutation
com.curiousbees.common.genetics.random
```

### 4.2 common/content

Minecraft-independent definitions for species, traits, mutations, and production.

Responsible for:

- species definitions;
- trait definitions;
- mutation definitions;
- production definitions;
- built-in initial content;
- future data-driven conversion models.

Example packages:

```text
com.curiousbees.common.content.species
com.curiousbees.common.content.traits
com.curiousbees.common.content.mutations
com.curiousbees.common.content.products
com.curiousbees.common.content.builtin
```

### 4.3 common/gameplay

Minecraft-adjacent orchestration, but still avoiding direct loader APIs when possible.

Responsible for:

- bee breeding orchestration;
- analyzer report generation;
- spawn genome selection rules;
- production calculation;
- environment abstraction usage.

Example packages:

```text
com.curiousbees.common.gameplay.breeding
com.curiousbees.common.gameplay.analysis
com.curiousbees.common.gameplay.spawn
com.curiousbees.common.gameplay.production
```

### 4.4 common/platform

Interfaces that platform modules implement.

Responsible for abstraction over:

- bee entity data access;
- random source adaptation;
- biome/environment context;
- logging;
- networking if needed later;
- registry access if needed later.

Example interfaces:

```java
public interface BeeGenomeStorage<B> {
    Optional<Genome> getGenome(B bee);
    void setGenome(B bee, Genome genome);
}

public interface EnvironmentContext {
    String biomeId();
    String dimensionId();
    boolean isRaining();
    boolean isDay();
}
```

Avoid exposing Minecraft classes in common APIs when practical. If this becomes awkward, create platform-specific adapters at the edges.

### 4.5 neoforge

NeoForge-specific implementation.

Responsible for:

- mod initialization;
- deferred registers;
- entity data attachments;
- item data components;
- events;
- commands;
- items;
- blocks;
- networking;
- client rendering later;
- resource/data loading later.

Example packages:

```text
com.curiousbees.neoforge
com.curiousbees.neoforge.data
com.curiousbees.neoforge.event
com.curiousbees.neoforge.registry
com.curiousbees.neoforge.command
com.curiousbees.neoforge.item
```

### 4.6 fabric

Future Fabric-specific implementation.

Responsible for:

- Fabric mod initialization;
- Fabric data attachment/component APIs;
- Fabric events or mixins;
- Fabric registries;
- Fabric commands;
- Fabric item/block/client implementation.

This module should be added only after the NeoForge MVP proves the core gameplay.

## 5. Data Flow: Wild Bee Spawn

```text
1. Minecraft spawns a Bee entity.
2. NeoForge spawn event/initialization hook runs.
3. Platform code checks if the bee already has a genome.
4. If not, the spawn initializer determines a wild species from biome/context.
5. A default wild genome is created.
6. The genome is stored on the bee entity.
```

Important:

- do not reinitialize an existing bee genome;
- do not assign random species on every load;
- default genome generation should be deterministic enough for testing but still allow controlled variation later.

## 6. Data Flow: Bee Breeding

```text
1. Two bees enter love mode through vanilla interaction.
2. Minecraft creates or attempts to create a baby bee.
3. NeoForge event/platform hook identifies parents and child.
4. Parent genomes are read.
5. If a parent is missing a genome, initialize or fallback safely.
6. BreedingService creates a child genome.
7. MutationService evaluates possible mutation.
8. Final child genome is stored on baby bee.
9. Optional feedback occurs if mutation happened.
```

Important:

- the core does not know about Bee entities;
- the core receives only parent genomes and simplified context;
- the platform layer handles all entity/event details;
- event handlers should be thin and delegate to services.

## 7. Data Flow: Analyzer

```text
1. Player uses Analyzer on a bee.
2. NeoForge interaction event reads bee genome.
3. Common analyzer service converts genome into a player-facing report.
4. Platform code renders result as chat, tooltip, overlay, or screen.
```

Initial analyzer output can be simple text.

A GUI is not required for MVP.

## 8. Data Flow: Production

Initial production should be simple.

```text
1. A bee or hive has a genome.
2. Active species determines primary output.
3. Inactive species may influence secondary output.
4. Productivity trait modifies output rate/chance.
5. Production result is generated.
```

Production should be implemented after breeding and analyzer.

## 9. Genome Persistence Strategy

### NeoForge

Use entity data storage suitable for vanilla entities.

Conceptually:

```text
Bee entity -> Genome attachment
ItemStack -> Genome component, if needed later
BlockEntity -> Inventory + genome/product state, if needed later
```

The exact API decision should be recorded in `docs/decisions/` and implemented according to `docs/implementation/03-neoforge-entity-integration.md`.

### Fabric

Future implementation should use equivalent concepts:

```text
Bee entity -> data attachment or component equivalent
ItemStack -> data component
BlockEntity -> component/data storage
```

Do not implement Fabric before the NeoForge MVP works.

## 10. Serialization

The genome must be serializable.

Required use cases:

- save/load bee entity genome;
- store genome in items later;
- debug commands;
- potential network sync;
- potential JSON export/import for tests.

Recommended internal representation:

```text
Genome
- Map<ChromosomeType, GenePair>

GenePair
- alleleA
- alleleB
- activeAllele
- inactiveAllele

Allele
- id
- chromosomeType
- dominance
```

The active/inactive result should be persisted after creation.

Do not recalculate active/inactive randomly every time the genome is read.

## 11. Randomness

The genetics core should not directly use global random state.

Prefer injecting a random interface:

```java
public interface GeneticRandom {
    double nextDouble();
    boolean nextBoolean();
    int nextInt(int bound);
}
```

This allows deterministic tests.

## 12. Error Handling

The core should fail fast for invalid definitions:

- missing chromosome;
- unknown species;
- unknown trait;
- mutation references unknown species;
- gene pair with incompatible allele type;
- invalid probability below 0 or above 1.

Platform integration should avoid crashing worlds for recoverable data issues, but development/debug builds should log clearly.

## 13. Testing Strategy

Detailed test plans are in:

```text
docs/quality/
```

### Unit tests

Must cover:

- dominance resolution;
- Mendelian inheritance;
- hybrid/purebred detection;
- mutation probability;
- active/inactive persistence;
- invalid content definitions;
- approximate distributions over many simulations.

### Integration tests / manual validation

Later:

- bee spawn receives genome;
- bee genome persists after save/load;
- bee breeding event assigns child genome;
- analyzer reads genome from entity;
- production resolver behaves according to species/traits.

## 14. Asset Architecture

Assets are not part of the critical path for the genetics core.

Detailed asset workflow is in:

```text
docs/art/
```

Rules:

- use placeholders early;
- do not block core, storage, breeding, or analyzer on polished art;
- Blockbench is useful later for custom blocks/machines;
- Blockbench/MCP automation is optional future tooling, not a dependency.

## 15. Release Architecture

Release and distribution planning is documented in:

```text
docs/release/
```

Do not optimize release automation before the MVP works, but keep versioning, changelog, and packaging conventions documented.

## 16. Future Multiloader Strategy

Do not implement Fabric in the first MVP, but keep these rules:

- no NeoForge imports in common genetics;
- no Fabric imports in common genetics;
- no direct Minecraft entity dependency in core logic;
- keep platform-specific hooks isolated;
- keep item/block registration out of the core;
- avoid writing logic inside event handlers;
- event handlers should call services.

## 17. Naming Guidelines

Use clear names rather than clever names.

Good:

```text
BreedingService
MutationService
Genome
GenePair
BeeSpeciesDefinition
MutationDefinition
AnalyzerReport
```

Avoid:

```text
BeeMagicManager
GeneticStuff
MutationThing
ForestryCompatSomething
```

## 18. Suggested First Package Structure

```text
common/src/main/java/com/curiousbees/common/
├── genetics/
│   ├── model/
│   │   ├── Allele.java
│   │   ├── Dominance.java
│   │   ├── GenePair.java
│   │   ├── Genome.java
│   │   └── ChromosomeType.java
│   ├── breeding/
│   │   ├── BreedingService.java
│   │   └── BreedingResult.java
│   ├── mutation/
│   │   ├── MutationService.java
│   │   ├── MutationDefinition.java
│   │   └── MutationResult.java
│   └── random/
│       └── GeneticRandom.java
├── content/
│   ├── species/
│   ├── traits/
│   └── builtin/
└── gameplay/
    ├── analysis/
    ├── spawn/
    └── production/
```

## 19. Anti-Patterns

Avoid:

- implementing genetics directly inside an event handler;
- storing raw unstructured NBT everywhere;
- hardcoding species checks across many classes;
- starting with resource bees;
- starting with GUI before analyzer logic exists;
- starting with JSON loading before definitions stabilize;
- mixing NeoForge and future Fabric logic in the same classes;
- letting AI agents generate large unrelated systems in one pass;
- treating short backlog text as a substitute for implementation specs.
