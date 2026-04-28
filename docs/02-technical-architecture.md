# 02 — Technical Architecture

## 1. Architecture Goal

The project must support a clean separation between:

1. Pure genetics logic.
2. Minecraft-independent gameplay orchestration.
3. NeoForge-specific integration.
4. Future Fabric-specific integration.

The most important architectural rule is:

```txt
The genetics core must not depend on Minecraft APIs.
```

This allows the most complex logic to be tested quickly and safely.

## 2. Recommended Repository Layout

Initial repository layout:

```txt
bee-genetics-mod/
├── CLAUDE.md
├── README.md
├── docs/
│   ├── 01-product-vision-and-roadmap.md
│   ├── 02-technical-architecture.md
│   ├── 03-genetics-system-spec.md
│   ├── 04-breeding-and-mutation-spec.md
│   ├── 05-content-design-spec.md
│   ├── 06-ai-coding-guidelines.md
│   └── 07-initial-backlog.md
│
├── common/
│   └── src/
│       ├── main/java/
│       └── test/java/
│
├── neoforge/
│   └── src/
│       ├── main/java/
│       └── test/java/
│
├── fabric/
│   └── src/
│       ├── main/java/
│       └── test/java/
│
├── build.gradle
├── settings.gradle
└── gradle.properties
```

If multiloader setup feels too heavy at the beginning, use this temporary layout:

```txt
bee-genetics-mod/
├── CLAUDE.md
├── README.md
├── docs/
├── src/
│   ├── main/java/
│   └── test/java/
└── build.gradle
```

However, even in a temporary layout, keep package boundaries clear.

## 3. Conceptual Modules

### 3.1 common/genetics

Pure Java.

No Minecraft imports.

Responsible for:

- Alleles
- Gene pairs
- Chromosome types
- Genomes
- Dominance resolution
- Mendelian inheritance
- Mutation evaluation
- Purebred/hybrid detection

Example packages:

```txt
com.example.beegenetics.common.genetics.model
com.example.beegenetics.common.genetics.breeding
com.example.beegenetics.common.genetics.mutation
com.example.beegenetics.common.genetics.random
```

### 3.2 common/content

Minecraft-independent definitions for species, traits, mutations, and production.

Responsible for:

- Species definitions
- Trait definitions
- Mutation definitions
- Production definitions
- Built-in initial content

Example packages:

```txt
com.example.beegenetics.common.content.species
com.example.beegenetics.common.content.traits
com.example.beegenetics.common.content.mutations
com.example.beegenetics.common.content.products
```

### 3.3 common/gameplay

Minecraft-adjacent orchestration, but still ideally avoiding direct loader APIs when possible.

Responsible for:

- Bee breeding orchestration
- Analyzer result formatting
- Spawn genome selection rules
- Production calculation
- Environment abstraction usage

Example packages:

```txt
com.example.beegenetics.common.gameplay.breeding
com.example.beegenetics.common.gameplay.analysis
com.example.beegenetics.common.gameplay.spawn
com.example.beegenetics.common.gameplay.production
```

### 3.4 common/platform

Interfaces that platform modules implement.

Responsible for abstraction over:

- Bee entity data access
- Random source
- Biome/environment context
- Logging
- Networking if needed later
- Registry access if needed later

Example interfaces:

```java
public interface BeeGenomeStorage {
    Optional<Genome> getGenome(Object bee);
    void setGenome(Object bee, Genome genome);
}

public interface EnvironmentContext {
    String biomeId();
    boolean isRaining();
    boolean isDay();
    boolean hasNearbyBlock(String blockId, int radius);
}
```

Avoid exposing Minecraft classes in the first version of these interfaces if possible. If this becomes awkward, create platform-specific adapters at the edges.

### 3.5 neoforge

NeoForge-specific implementation.

Responsible for:

- Mod initialization
- Deferred registers
- Entity data attachment
- Item data components
- Events
- Commands
- Items
- Blocks
- Networking
- Client rendering later

Example packages:

```txt
com.example.beegenetics.neoforge
com.example.beegenetics.neoforge.data
com.example.beegenetics.neoforge.event
com.example.beegenetics.neoforge.registry
com.example.beegenetics.neoforge.command
```

### 3.6 fabric

Future Fabric-specific implementation.

Responsible for:

- Fabric mod initialization
- Fabric data attachment/component APIs
- Fabric events or mixins
- Fabric registries
- Fabric commands
- Fabric item/block/client implementation

This module should be added only after NeoForge MVP proves the core gameplay.

## 4. Data Flow: Wild Bee Spawn

```txt
1. Minecraft spawns a Bee entity.
2. NeoForge spawn event/initialization hook runs.
3. Platform code checks if the bee already has a genome.
4. If not, the spawn initializer determines a wild species from biome/context.
5. A default wild genome is created.
6. The genome is stored on the bee entity.
```

Important:

- Do not reinitialize an existing bee genome.
- Do not assign random species on every load.
- Default genome generation should be deterministic enough for testing but still allow variation.

## 5. Data Flow: Bee Breeding

```txt
1. Two bees enter love mode through vanilla interaction.
2. Minecraft creates or attempts to create a baby bee.
3. NeoForge event/platform hook identifies parents and child.
4. Parent genomes are read.
5. If a parent is missing a genome, initialize or fallback safely.
6. BreedingService creates a child genome.
7. MutationService evaluates possible mutation.
8. Final child genome is stored on baby bee.
9. Optional visual/audio feedback occurs if mutation happened.
```

Important:

- The core does not know about Bee entities.
- The core receives only parent genomes and a simplified context.
- The platform layer handles all entity/event details.

## 6. Data Flow: Analyzer

```txt
1. Player uses Analyzer on a bee.
2. NeoForge interaction event reads bee genome.
3. Common analyzer service converts genome into a player-facing report.
4. Platform code renders result as chat, tooltip, overlay, or screen.
```

Initial analyzer output can be simple text.

A GUI is not required for MVP.

## 7. Data Flow: Production

Initial production should be simple.

```txt
1. A bee or hive has a genome.
2. Active species determines primary output.
3. Inactive species may influence secondary output.
4. Productivity trait modifies output rate/chance.
5. Production result is generated.
```

Production should be implemented after breeding and analyzer.

## 8. Genome Persistence Strategy

### NeoForge

Use entity data storage suitable for vanilla entities.

Conceptually:

```txt
Bee entity -> Genome attachment
ItemStack -> Genome component, if needed later
BlockEntity -> Inventory + genome/product state, if needed later
```

### Fabric

Future implementation should use equivalent concepts:

```txt
Bee entity -> Data attachment
ItemStack -> Data component
BlockEntity -> component/data storage
```

## 9. Serialization

The genome must be serializable.

Required use cases:

- Save/load bee entity genome.
- Store genome in items later.
- Debug commands.
- Potential network sync.
- Potential JSON export/import for tests.

Recommended internal representation:

```txt
Genome
- Map<ChromosomeType, GenePair>

GenePair
- Allele first
- Allele second
- Allele active
- Allele inactive

Allele
- id
- dominance
- value/type
```

The active/inactive result should be persisted after creation.

Do not recalculate active/inactive randomly every time the genome is read.

## 10. Randomness

The genetics core should not directly use global random state.

Prefer injecting a random interface:

```java
public interface RandomSource {
    double nextDouble();
    boolean nextBoolean();
    int nextInt(int bound);
}
```

This allows deterministic tests.

## 11. Error Handling

The core should fail fast for invalid definitions:

- Missing chromosome.
- Unknown species.
- Unknown trait.
- Mutation references unknown species.
- Gene pair with incompatible allele type.
- Invalid probability below 0 or above 1.

Platform integration should avoid crashing worlds for recoverable data issues, but development/debug builds should log clearly.

## 12. Testing Strategy

### Unit Tests

Must cover:

- Dominance resolution
- Mendelian inheritance
- Hybrid/purebred detection
- Mutation probability
- Active/inactive persistence
- Invalid content definitions
- Approximate distributions over many simulations

### Integration Tests

Later:

- Bee spawn receives genome.
- Bee genome persists after save/load.
- Bee breeding event assigns child genome.
- Analyzer reads genome from entity.

## 13. Future Multiloader Strategy

Do not implement Fabric in the first MVP, but keep these rules:

- No NeoForge imports in common genetics.
- No Fabric imports in common genetics.
- No direct Minecraft entity dependency in core logic.
- Keep platform-specific hooks isolated.
- Keep item/block registration out of the core.
- Avoid writing logic inside event handlers; event handlers should call services.

## 14. Naming Guidelines

Use clear names rather than clever names.

Good:

```txt
BreedingService
MutationService
Genome
GenePair
BeeSpeciesDefinition
MutationDefinition
AnalyzerReport
```

Avoid:

```txt
BeeMagicManager
GeneticStuff
MutationThing
ForestryCompatSomething
```

## 15. Suggested First Package Structure

```txt
common/src/main/java/com/example/beegenetics/common/
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
│
├── content/
│   ├── species/
│   ├── traits/
│   └── builtin/
│
└── gameplay/
    ├── analysis/
    ├── spawn/
    └── production/
```

## 16. Anti-Patterns

Avoid:

- Implementing genetics directly inside an event handler.
- Storing raw unstructured NBT everywhere.
- Hardcoding species checks across many classes.
- Starting with resource bees.
- Starting with GUI before analyzer logic exists.
- Starting with JSON loading before definitions stabilize.
- Mixing NeoForge and future Fabric logic in the same classes.
- Letting AI agents generate large unrelated systems in one pass.
