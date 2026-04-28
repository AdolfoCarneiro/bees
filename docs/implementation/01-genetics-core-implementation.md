# Implementation Spec — Phase 1: Genetics Core

This document defines the detailed implementation plan for **Phase 1 — Pure Genetics Core** of Curious Bees.

It is intended to be read by humans and AI coding agents such as Claude Code, Codex, Cursor, or similar tools.

The goal of this phase is to implement the core genetics engine without touching Minecraft, NeoForge, Fabric, entities, items, blocks, assets, GUIs, registries, NBT, data components, attachments, or world events.

---

## 1. Phase Goal

Implement a pure Java genetics engine capable of representing bee genomes, resolving active/inactive alleles, crossing two parent genomes using Mendelian inheritance, and applying probabilistic species mutations.

This phase is complete when the project can simulate bee breeding entirely through unit tests, without launching Minecraft.

The engine must support:

```text
Alleles
Dominance
Chromosome types
Gene pairs
Genomes
Injectable randomness
Mendelian inheritance
Species mutation rules
Hybrid/purebred detection
Test fixtures
Statistical simulation tests
```

---

## 2. Critical Architecture Rule

The genetics core must be completely independent from Minecraft and loaders.

Do not import or reference:

```text
net.minecraft.*
net.neoforged.*
net.fabricmc.*
Forge/NeoForge event classes
Fabric events or mixins
Minecraft Bee entity
ItemStack
Level
ResourceLocation
NBT
Data Components
Data Attachments
Registries
Blocks
Items
```

If a value needs an identifier, use a plain string or a project-owned value object. Do not use Minecraft `ResourceLocation` in the core during this phase.

Allowed:

```text
Java standard library
JUnit/test framework
Project-owned classes/interfaces
```

---

## 3. Phase Non-Goals

Do not implement any of the following in Phase 1:

```text
NeoForge mod initialization
Fabric support
Bee entity data storage
Baby bee spawn hook
Wild bee spawn initialization
Analyzer item
Items
Blocks
Comb items
Production logic
GUI
Textures
Blockbench models
JSON/datapack loading
Networking
Commands
Advancements
Apiary
Frames
Resource bees
```

This phase should only create the genetics engine and tests.

---

## 4. Recommended Package Structure

Use the project’s actual base package. The examples below use a placeholder package.

Suggested structure:

```text
common/src/main/java/<base_package>/common/genetics/
├── model/
│   ├── Allele.java
│   ├── ChromosomeType.java
│   ├── Dominance.java
│   ├── GenePair.java
│   └── Genome.java
│
├── random/
│   ├── GeneticRandom.java
│   └── JavaGeneticRandom.java
│
├── breeding/
│   ├── BreedingService.java
│   └── BreedingResult.java
│
├── mutation/
│   ├── MutationDefinition.java
│   ├── MutationResult.java
│   ├── MutationResultMode.java
│   └── MutationService.java
│
└── validation/
    └── GeneticsValidation.java        # optional
```

Suggested test structure:

```text
common/src/test/java/<base_package>/common/genetics/
├── model/
│   ├── AlleleTest.java
│   ├── GenePairTest.java
│   └── GenomeTest.java
│
├── random/
│   └── DeterministicGeneticRandom.java
│
├── breeding/
│   └── BreedingServiceTest.java
│
├── mutation/
│   └── MutationServiceTest.java
│
└── fixtures/
    ├── AlleleFixtures.java
    └── GenomeFixtures.java
```

If the repository does not yet have a multiloader `common/` module, create the smallest compatible structure, but keep the package boundaries clear.

---

## 5. Development Order

Implement this phase in small tasks:

```text
1. Create genetics core package structure
2. Implement Dominance, ChromosomeType, and Allele
3. Implement GeneticRandom abstraction
4. Implement GenePair active/inactive resolution
5. Implement Genome aggregate
6. Add test fixtures and sample alleles/genomes
7. Implement BreedingService and BreedingResult
8. Implement MutationDefinition, MutationResultMode, MutationResult, and MutationService
9. Add genetics simulation tests
10. Review core architecture before Minecraft integration
```

Each task should be implemented, tested, and reviewed before moving to the next one.

---

# Task 1 — Create Genetics Core Package Structure

## Objective

Create the minimal source and test package structure required for the pure Java genetics core.

## Scope

Create or identify:

```text
main genetics package
test genetics package
model package
breeding package
mutation package
random package
fixtures/test-support package
```

## Non-Goals

Do not implement domain behavior yet.

Do not create:

```text
Minecraft integration
NeoForge module code
Fabric module code
items
blocks
commands
assets
models
textures
```

## Acceptance Criteria

```text
- There is a clear location for common genetics source files.
- There is a clear location for genetics tests.
- Project still compiles.
- No Minecraft/NeoForge/Fabric dependency is introduced into core genetics.
```

## Prompt for Claude Code

```text
Read CLAUDE.md and the docs folder.

Focus only on: Task 1 — Create Genetics Core Package Structure.

Create the minimal common genetics source/test package structure required for pure Java genetics code.

Do not implement alleles, genomes, breeding, mutation, Minecraft integration, NeoForge integration, Fabric support, items, blocks, commands, assets or UI.

Before coding, summarize your understanding and list the files/directories you expect to create or modify.
```

---

# Task 2 — Implement Dominance, ChromosomeType, and Allele

## Objective

Implement the first genetics primitives used by every later part of the engine.

## Scope

Implement:

```text
Dominance
ChromosomeType
Allele
```

## Required Concepts

### Dominance

Represents whether an allele is dominant or recessive.

Required values:

```java
DOMINANT
RECESSIVE
```

Do not implement incomplete dominance, codominance, or advanced genetic expression modes yet.

### ChromosomeType

Represents a genetic category.

Required MVP values:

```java
SPECIES
LIFESPAN
PRODUCTIVITY
FERTILITY
FLOWER_TYPE
```

Do not add future chromosomes unless the docs explicitly request it.

Future examples, not required now:

```java
TEMPERATURE_TOLERANCE
HUMIDITY_TOLERANCE
TERRITORY
EFFECT
NOCTURNAL
CAVE_DWELLING
WEATHER_TOLERANCE
```

### Allele

Represents one genetic value for one chromosome.

Minimum fields:

```text
id: String
chromosomeType: ChromosomeType
dominance: Dominance
```

The `id` must be stable and future-serializable.

Recommended ID examples:

```text
curious_bees:species/meadow
curious_bees:species/forest
curious_bees:species/arid
curious_bees:productivity/fast
curious_bees:fertility/two
curious_bees:flower_type/flowers
```

Do not use display names as IDs.

## Design Constraints

```text
- Allele should be immutable or safely immutable.
- Null values should be rejected.
- Blank IDs should be rejected.
- Logic should compare alleles by stable identity, not display name.
- No Minecraft ResourceLocation in Phase 1 core.
```

## Non-Goals

Do not implement:

```text
GenePair
Genome
BreedingService
MutationService
Built-in species registry
JSON loading
Minecraft integration
NeoForge entity data
Fabric support
Analyzer
Production
```

## Acceptance Criteria

```text
- Dominance enum exists.
- DOMINANT and RECESSIVE exist.
- ChromosomeType enum exists.
- Required MVP chromosome types exist.
- Allele stores stable ID, chromosome type, and dominance.
- Allele rejects null ID.
- Allele rejects blank ID.
- Allele rejects null chromosome type.
- Allele rejects null dominance.
- Allele is immutable or safely immutable.
- Unit tests cover valid and invalid construction.
- Core still has no Minecraft/NeoForge/Fabric dependency.
```

## Expected Tests

```text
valid allele construction succeeds
blank id fails
null id fails
null chromosome type fails
null dominance fails
allele exposes expected id
allele exposes expected chromosome type
allele exposes expected dominance
```

## Prompt for Claude Code

```text
Read CLAUDE.md and the relevant docs.

Focus only on: Task 2 — Implement Dominance, ChromosomeType, and Allele.

Implement the pure Java genetics primitives:
- Dominance
- ChromosomeType
- Allele

Do not implement GenePair, Genome, breeding, mutation, built-in content, Minecraft integration, NeoForge integration, Fabric support, items, blocks, assets, commands or UI.

Add unit tests for valid construction and invalid construction.

Before coding, summarize your understanding and list the files you expect to create or modify.
```

---

# Task 3 — Implement GeneticRandom Abstraction

## Objective

Introduce injectable randomness so genetics behavior can be tested deterministically.

## Scope

Implement a project-owned random abstraction.

Suggested interface:

```java
public interface GeneticRandom {
    boolean nextBoolean();
    double nextDouble();
    int nextInt(int bound);
}
```

Suggested production implementation:

```java
public final class JavaGeneticRandom implements GeneticRandom {
    // wraps java.util.Random or java.util.random.RandomGenerator
}
```

Suggested test implementation:

```java
DeterministicGeneticRandom
```

The exact implementation can vary, but services must not directly depend on global random state.

## Non-Goals

Do not implement:

```text
BreedingService
MutationService
Minecraft random integration
World seed integration
Serialization
```

## Acceptance Criteria

```text
- GeneticRandom or equivalent exists.
- Production implementation wraps standard Java randomness.
- Tests can provide deterministic boolean/double/int values.
- Future GenePair, BreedingService, and MutationService can depend on this abstraction.
```

## Expected Tests

```text
java random wrapper returns values in expected range
nextInt rejects invalid bounds if applicable
 deterministic test random can return configured booleans
 deterministic test random can return configured doubles
 deterministic test random can return configured ints
```

## Prompt for Claude Code

```text
Read CLAUDE.md and the relevant docs.

Focus only on: Task 3 — Implement GeneticRandom Abstraction.

Create a pure Java GeneticRandom abstraction and a production Java-backed implementation. Add a deterministic test implementation if useful for upcoming tests.

Do not implement GenePair, Genome, breeding, mutation, Minecraft integration, NeoForge integration, Fabric support, items, blocks, assets, commands or UI.

Add tests for deterministic behavior where appropriate.

Before coding, summarize your understanding and list the files you expect to create or modify.
```

---

# Task 4 — Implement GenePair Active/Inactive Resolution

## Objective

Implement a pair of alleles for one chromosome and resolve which allele is active.

## Scope

Implement:

```text
GenePair
```

A `GenePair` should contain:

```text
first allele
second allele
active allele
inactive allele
```

It should resolve active/inactive order at construction time and preserve that result.

## Rules

```text
DOMINANT + RECESSIVE -> dominant active
RECESSIVE + DOMINANT -> dominant active
DOMINANT + DOMINANT -> active selected by GeneticRandom
RECESSIVE + RECESSIVE -> active selected by GeneticRandom
```

The active allele must not be recalculated on every getter call.

## Required Behavior

```text
- Reject null alleles.
- Reject alleles from different chromosome types.
- Resolve active/inactive once.
- Preserve active/inactive after construction.
- Report chromosome type.
- Report purebred/hybrid status.
```

Purebred rule:

```text
same allele ID -> purebred
```

Hybrid rule:

```text
different allele IDs -> hybrid
```

## Suggested API

Exact names can vary, but useful methods include:

```java
Allele first();
Allele second();
Allele active();
Allele inactive();
ChromosomeType chromosomeType();
boolean isPurebred();
boolean isHybrid();
boolean containsAllele(String alleleId);
boolean hasActiveAllele(String alleleId);
boolean hasInactiveAllele(String alleleId);
```

## Non-Goals

Do not implement:

```text
Genome
BreedingService
MutationService
Built-in content
Serialization
Minecraft integration
NeoForge integration
Fabric support
```

## Acceptance Criteria

```text
- GenePair stores two alleles.
- Both alleles must have the same ChromosomeType.
- Dominant allele becomes active over recessive allele.
- Equal dominance uses GeneticRandom to choose active allele.
- Active/inactive result is stable after construction.
- Purebred/hybrid detection works.
- Unit tests cover all rules.
```

## Expected Tests

```text
null first allele fails
null second allele fails
mixed chromosome types fail
dominant first beats recessive second
recessive first loses to dominant second
dominant + dominant uses deterministic random
recessive + recessive uses deterministic random
active allele remains stable over repeated reads
same allele ID is purebred
different allele ID is hybrid
containsAllele works
hasActiveAllele works
hasInactiveAllele works
```

## Prompt for Claude Code

```text
Read CLAUDE.md and the relevant docs.

Focus only on: Task 4 — Implement GenePair Active/Inactive Resolution.

Use the existing Allele, Dominance, ChromosomeType, and GeneticRandom types.

Implement GenePair with stable active/inactive allele resolution, validation, and purebred/hybrid checks.

Do not implement Genome, breeding, mutation, built-in content, serialization, Minecraft integration, NeoForge integration, Fabric support, items, blocks, assets, commands or UI.

Add unit tests for validation, dominance resolution, same-dominance deterministic resolution, stability, and purebred/hybrid checks.

Before coding, summarize your understanding and list the files you expect to create or modify.
```

---

# Task 5 — Implement Genome Aggregate

## Objective

Implement a complete genetic representation for one bee.

## Scope

Implement:

```text
Genome
```

A genome should store:

```text
Map<ChromosomeType, GenePair>
```

The `SPECIES` chromosome is required.

## Required Behavior

```text
- Reject null chromosome map.
- Reject missing SPECIES chromosome.
- Reject mismatched map keys and GenePair chromosome types.
- Provide active allele lookup by chromosome type.
- Provide inactive allele lookup by chromosome type.
- Provide GenePair lookup by chromosome type.
- Provide species GenePair convenience method.
- Protect internal mutable state.
```

## Suggested API

Exact names can vary, but useful methods include:

```java
GenePair getGenePair(ChromosomeType type);
Allele getActiveAllele(ChromosomeType type);
Allele getInactiveAllele(ChromosomeType type);
GenePair species();
boolean isPurebred(ChromosomeType type);
boolean isHybrid(ChromosomeType type);
Map<ChromosomeType, GenePair> genePairs(); // read-only copy/view
Genome withGenePair(ChromosomeType type, GenePair pair); // optional, useful for mutation
```

A `withGenePair` method is optional but recommended if the design uses immutable genomes. It will help MutationService return a changed genome without mutating the original.

## Non-Goals

Do not implement:

```text
BreedingService
MutationService
Built-in species
Serialization
Minecraft entity storage
NeoForge Data Attachments
Fabric components
Analyzer
Production
```

## Acceptance Criteria

```text
- Genome stores chromosome types mapped to GenePair.
- Genome requires SPECIES chromosome.
- Genome validates GenePair chromosome type against map key.
- Genome can read active/inactive allele by chromosome.
- Genome can read species gene pair.
- Genome can detect purebred/hybrid by chromosome.
- Genome does not expose mutable internal state.
- Unit tests cover validation and read behavior.
```

## Expected Tests

```text
genome without species fails
null map fails
map with null key fails if applicable
map with null GenePair fails
mismatched map key and GenePair chromosome type fails
genome with species succeeds
active allele can be read by chromosome
inactive allele can be read by chromosome
species gene pair can be read
isPurebred works by chromosome
isHybrid works by chromosome
external map mutation does not mutate genome
read-only map cannot mutate genome
withGenePair returns changed copy if implemented
withGenePair does not mutate original if implemented
```

## Prompt for Claude Code

```text
Read CLAUDE.md and the relevant docs.

Focus only on: Task 5 — Implement Genome Aggregate.

Use the existing Allele, ChromosomeType, and GenePair types.

Implement a pure Java Genome aggregate that stores chromosome gene pairs, requires a SPECIES chromosome, validates input, and exposes safe read-only access.

Do not implement breeding, mutation, built-in content, serialization, Minecraft integration, NeoForge integration, Fabric support, items, blocks, assets, commands or UI.

Add unit tests for required species chromosome, active/inactive lookup, purebred/hybrid status, validation, and immutability/copy behavior.

Before coding, summarize your understanding and list the files you expect to create or modify.
```

---

# Task 6 — Add Test Fixtures and Sample Genomes

## Objective

Create reusable fixtures for unit tests and simulation tests.

## Scope

Create test-only helpers for:

```text
species alleles
trait alleles
purebred genomes
hybrid genomes
deterministic random sequences
```

## Required Fixture Data

Species alleles:

```text
Meadow
Forest
Arid
Cultivated
Hardy
```

Trait alleles:

```text
Lifespan: Short, Normal, Long
Productivity: Slow, Normal, Fast
Fertility: One, Two, Three
FlowerType: Flowers, Cactus, Leaves
```

Sample genomes:

```text
pure Meadow genome
pure Forest genome
pure Arid genome
hybrid Meadow/Forest genome
hybrid Forest/Arid genome
```

## Non-Goals

Do not implement production built-in content yet.

Do not create runtime registries unless needed by tests.

Do not add Minecraft dependencies.

## Acceptance Criteria

```text
- Test fixtures can create common alleles easily.
- Test fixtures can create pure species genomes easily.
- Test fixtures can create hybrid species genomes easily.
- Test fixtures reduce duplication in BreedingService and MutationService tests.
- Fixtures remain test-only unless explicitly promoted to built-in content later.
```

## Prompt for Claude Code

```text
Read CLAUDE.md and the relevant docs.

Focus only on: Task 6 — Add Test Fixtures and Sample Genomes.

Create test fixtures for sample alleles and genomes used by genetics core tests.

Do not implement runtime built-in content, JSON loading, Minecraft integration, NeoForge integration, Fabric support, items, blocks, assets, commands or UI.

Before coding, summarize your understanding and list the files you expect to create or modify.
```

---

# Task 7 — Implement BreedingService and BreedingResult

## Objective

Implement Mendelian inheritance between two parent genomes.

## Scope

Implement:

```text
BreedingService
BreedingResult
```

The service should produce a child genome from two parent genomes.

## Core Algorithm

For each chromosome type present in the required genome:

```text
1. Read parent A GenePair.
2. Read parent B GenePair.
3. Randomly select one allele from parent A.
4. Randomly select one allele from parent B.
5. Create child GenePair from the two selected alleles.
6. Store it in the child Genome.
```

Each parent allele has 50% chance of being selected.

## BreedingResult

A result object is recommended because later phases need debug/analyzer/mutation metadata.

Minimum:

```text
childGenome
```

Optional now, useful later:

```text
inherited chromosome details
selected allele from parent A per chromosome
selected allele from parent B per chromosome
```

Do not add mutation fields here unless the docs/implementation prefer a combined orchestrator later. In Phase 1, BreedingService should not apply mutation.

## Required Behavior

```text
- Reject null parent genomes.
- Cross all chromosomes present in the parents.
- Ensure both parents have compatible chromosome sets.
- Do not mutate parent genomes.
- Use GeneticRandom.
- Delegate active/inactive resolution to GenePair.
```

## Non-Goals

Do not implement:

```text
MutationService
Biome/environment context
Frames
NeoForge breeding event
Minecraft Bee entity
Analyzer
Production
```

## Acceptance Criteria

```text
- BreedingService accepts two parent genomes and GeneticRandom.
- Child receives one allele from each parent per chromosome.
- Parent allele selection is 50/50.
- Child GenePairs resolve active/inactive correctly.
- Parent genomes remain unchanged.
- BreedingResult returns child genome.
- Deterministic tests can force exact child results.
- Statistical tests validate approximate inheritance distribution.
```

## Expected Tests

```text
null parent A fails
null parent B fails
pure Meadow x pure Forest produces Meadow/Forest species pair
hybrid Meadow/Forest x hybrid Meadow/Forest approximates 25/50/25
all MVP chromosomes are crossed
parents remain unchanged
deterministic random source selects expected alleles
mismatched chromosome sets fail or are handled explicitly
```

## Distribution Test Guidance

For hybrid x hybrid:

```text
Parent A Species: Meadow / Forest
Parent B Species: Meadow / Forest
```

Expected approximate distribution:

```text
25% Meadow / Meadow
50% Meadow / Forest or Forest / Meadow
25% Forest / Forest
```

Tests should allow tolerance and avoid flakiness.

Example:

```text
Run 10,000 crosses.
Accept each bucket within reasonable tolerance, such as +/- 5% absolute.
```

## Prompt for Claude Code

```text
Read CLAUDE.md and the relevant docs.

Focus only on: Task 7 — Implement BreedingService and BreedingResult.

Implement pure Java Mendelian inheritance between two Genome objects using GeneticRandom.

Do not implement mutation, biome checks, frames, Minecraft integration, NeoForge integration, Fabric support, analyzer UI, production, items, blocks, assets or commands.

Add unit tests for deterministic inheritance, parent immutability, multi-chromosome crossing, and approximate 25/50/25 distribution for hybrid parents.

Before coding, summarize your understanding and list the files you expect to create or modify.
```

---

# Task 8 — Implement MutationDefinition, MutationResultMode, MutationResult, and MutationService

## Objective

Implement species mutation logic after Mendelian inheritance.

## Scope

Implement:

```text
MutationDefinition
MutationResultMode
MutationResult
MutationService
```

## MutationDefinition

Minimum fields:

```text
id
parentSpeciesAId
parentSpeciesBId
resultSpeciesAllele
baseChance
resultMode or result mode chances
```

Recommended chance validation:

```text
0.0 <= chance <= 1.0
```

Parent matching should be order-insensitive.

```text
Meadow + Forest == Forest + Meadow
```

## MutationResultMode

Recommended values:

```text
PARTIAL
FULL
```

Alternative names are acceptable:

```text
HYBRID_RESULT
PURE_RESULT
```

Use names that match the rest of the implementation.

## MutationService

The service should:

```text
1. Receive parent genomes.
2. Receive inherited child genome.
3. Receive available mutation definitions.
4. Use active species of parent genomes for MVP matching.
5. Evaluate mutation chance.
6. If no mutation applies, return unchanged child/result.
7. If mutation succeeds, return a result with modified species chromosome.
```

## Parent Species Rule

For MVP, use active species only.

Do not implement inactive-parent species matching yet.

## Mutation Timing

Mutation happens after inheritance.

```text
BreedingService -> inherited child genome
MutationService -> final child genome
```

## Partial Mutation

A partial mutation replaces one species allele with the result species.

Example:

```text
Before: Meadow / Forest
After: Cultivated / Forest
```

The replacement strategy must be deterministic/testable.

Suggested strategy:

```text
Replace the active species allele with mutation result, then resolve a new GenePair.
```

Or:

```text
Use GeneticRandom to choose which side to replace.
```

Whichever strategy is chosen must be documented in tests.

## Full Mutation

A full mutation replaces both species alleles with the result species.

Example:

```text
Before: Meadow / Forest
After: Cultivated / Cultivated
```

## Non-Goals

Do not implement:

```text
Biome/environment requirements
Frame modifiers
Nearby block requirements
JSON/datapack loading
NeoForge integration
Fabric integration
Mutation feedback particles
Analyzer mutation prediction
Production
```

## Acceptance Criteria

```text
- MutationDefinition validates required fields.
- MutationDefinition rejects invalid chance below 0.
- MutationDefinition rejects invalid chance above 1.
- Mutation matching is order-insensitive.
- MutationService uses active species of parents for MVP matching.
- 0% chance never mutates.
- 100% chance always mutates when parents match.
- No matching mutation returns no mutation/unchanged child.
- Partial mutation result is supported.
- Full mutation result is supported.
- Parent genomes are not mutated.
- Original child genome is not mutated in place if immutable design is used.
```

## Expected Tests

```text
invalid chance below zero fails
invalid chance above one fails
null parent species fails
null result species fails
parent order does not matter
no matching mutation returns unchanged child
0 percent chance never mutates
100 percent chance always mutates
partial mutation produces expected species pair
full mutation produces pure result species pair
parents remain unchanged
child copy/immutability behavior is clear and tested
```

## Prompt for Claude Code

```text
Read CLAUDE.md and the relevant docs.

Focus only on: Task 8 — Implement MutationDefinition, MutationResultMode, MutationResult, and MutationService.

Implement pure Java species mutation logic that runs after Mendelian breeding.

Use active parent species for MVP mutation matching.

Do not implement biome conditions, frame modifiers, JSON loading, Minecraft integration, NeoForge integration, Fabric support, analyzer UI, production, items, blocks, assets or commands.

Add unit tests for mutation definition validation, parent order matching, no-match behavior, 0% chance, 100% chance, partial mutation, full mutation, and immutability/copy behavior.

Before coding, summarize your understanding and list the files you expect to create or modify.
```

---

# Task 9 — Add Genetics Simulation Tests

## Objective

Add higher-level simulation tests that validate the genetics core behaves correctly across many crosses.

## Scope

Add tests that run repeated breeding/mutation scenarios using deterministic or seeded random behavior.

## Simulation Scenarios

### Scenario 1 — Pure Species Cross

```text
Parent A: Meadow / Meadow
Parent B: Forest / Forest
Expected: 100% Meadow / Forest children
```

### Scenario 2 — Hybrid Species Cross

```text
Parent A: Meadow / Forest
Parent B: Meadow / Forest
Expected approximate distribution:
25% Meadow / Meadow
50% Meadow / Forest
25% Forest / Forest
```

### Scenario 3 — 0% Mutation

```text
Mutation: Meadow + Forest -> Cultivated at 0%
Expected: no mutation ever occurs
```

### Scenario 4 — 100% Mutation

```text
Mutation: Meadow + Forest -> Cultivated at 100%
Expected: mutation always occurs when parents match
```

### Scenario 5 — MVP Mutation Chance Smoke Test

```text
Mutation: Meadow + Forest -> Cultivated at 12%
Run many attempts.
Verify mutation rate is approximately near expected tolerance.
```

This test must be designed carefully to avoid flakiness.

## Non-Goals

Do not create Minecraft integration tests.

Do not launch Minecraft.

Do not test NeoForge attachments.

## Acceptance Criteria

```text
- Simulation tests cover inheritance distribution.
- Simulation tests cover mutation probability behavior.
- Tests avoid flaky exact probabilistic assertions.
- Tests document expected tolerance.
- Tests run as normal unit tests.
```

## Prompt for Claude Code

```text
Read CLAUDE.md and the relevant docs.

Focus only on: Task 9 — Add Genetics Simulation Tests.

Add simulation-style unit tests for repeated genetics crosses and mutation scenarios.

Do not implement Minecraft integration, NeoForge integration, Fabric support, analyzer UI, production, items, blocks, assets or commands.

Ensure probabilistic tests use stable random behavior or tolerances to avoid flakiness.

Before coding, summarize your understanding and list the files you expect to create or modify.
```

---

# Task 10 — Review Genetics Core Before Minecraft Integration

## Objective

Perform a review of the genetics core before starting NeoForge integration.

This is a review task, not an implementation task.

## Scope

Check:

```text
architecture boundaries
tests
immutability
randomness injection
active/inactive persistence
mutation behavior
Mendelian distribution
future compatibility with content definitions
```

## Review Checklist

```text
- Does common genetics import Minecraft, NeoForge, Fabric, NBT, components, entities, registries, or events?
- Are active/inactive alleles resolved once and persisted?
- Is randomness injectable/testable?
- Are invalid states rejected early?
- Are GenePair and Genome safe from external mutation?
- Does BreedingService mutate parent genomes?
- Does MutationService mutate parent genomes?
- Are probabilistic tests non-flaky?
- Are species IDs stable and future-serializable?
- Is the API understandable enough for platform adapters to call?
- Did any AI implementation add future features too early?
```

## Acceptance Criteria

```text
- Review identifies any architecture violations.
- Review identifies missing tests.
- Review identifies over-engineering or premature features.
- Review produces a clear list of fixes if needed.
- NeoForge integration does not start until major issues are resolved.
```

## Prompt for Claude Code

```text
Read CLAUDE.md and all docs.

Review the implemented genetics core before Minecraft integration.

Do not change files yet.

Check specifically:
- no Minecraft/NeoForge/Fabric dependencies in common genetics;
- active/inactive allele persistence;
- injectable randomness;
- Mendelian inheritance tests;
- mutation probability tests;
- immutability/copy behavior;
- invalid state validation;
- whether any future features were implemented too early.

Return a review report with recommended fixes and a go/no-go recommendation for starting NeoForge integration.
```

---

## 6. Phase 1 Completion Criteria

Phase 1 is complete when:

```text
- Genetics model classes exist.
- A genome can represent all MVP chromosomes.
- Gene pairs resolve active/inactive alleles correctly.
- Active/inactive result is stable after construction.
- Purebred/hybrid detection works.
- BreedingService can cross two parent genomes.
- MutationService can apply species mutation after inheritance.
- Randomness is injectable/testable.
- Unit tests cover main behavior.
- Simulation tests cover inheritance distribution.
- No Minecraft, NeoForge, or Fabric APIs are used in core genetics.
```

---

## 7. Go/No-Go Checklist for Phase 2

Before moving to Phase 2 — Initial Content Definitions, confirm:

```text
[ ] Allele exists and is tested.
[ ] Dominance exists and is tested where useful.
[ ] ChromosomeType exists with MVP chromosomes.
[ ] GeneticRandom abstraction exists.
[ ] GenePair exists and is tested.
[ ] Genome exists and is tested.
[ ] BreedingService exists and is tested.
[ ] MutationService exists and is tested.
[ ] Simulation tests exist.
[ ] Core has no loader/game dependencies.
[ ] Review task found no blocking architecture issue.
```

If any item is unchecked, do not start NeoForge integration yet.

---

## 8. Recommended Commit Sequence for Phase 1

```text
core: add genetics package structure
core: add allele and chromosome primitives
core: add genetic random abstraction
core: add gene pair resolution
core: add genome aggregate
core: add genetics test fixtures
core: add mendelian breeding service
core: add species mutation service
core: add genetics simulation tests
core: review genetics architecture before platform integration
```

---

## 9. Common AI Failure Modes in Phase 1

### Failure Mode: Agent Adds Minecraft Classes

Symptom:

```text
Core imports ResourceLocation, Bee, ItemStack, Level, NBT, or NeoForge classes.
```

Fix:

```text
Reject/revert or move that code to a platform layer.
```

### Failure Mode: Active Allele Is Recalculated

Symptom:

```text
GenePair getter randomly changes active allele.
```

Fix:

```text
Resolve active/inactive once at construction and store the result.
```

### Failure Mode: Deterministic Mutation Recipe

Symptom:

```text
Meadow + Forest always becomes Cultivated without chance.
```

Fix:

```text
Mutation must be probabilistic and testable with 0% and 100% cases.
```

### Failure Mode: Only One Allele Is Stored

Symptom:

```text
Genome stores only active trait/species.
```

Fix:

```text
Every chromosome must store two alleles plus active/inactive expression.
```

### Failure Mode: Task Scope Expands

Symptom:

```text
Agent adds analyzer, items, blocks, JSON loading, or NeoForge events during core tasks.
```

Fix:

```text
Stop and reduce scope. Phase 1 is pure genetics only.
```

---

## 10. Handoff Prompt for Starting Phase 1

Use this after the Phase 0 docs are in the repository.

```text
Read CLAUDE.md.
Read all files in docs/.
Read docs/implementation/01-genetics-core-implementation.md.

Do not implement the whole phase at once.

Start only with:
Task 1 — Create Genetics Core Package Structure.

Before coding:
1. summarize your understanding of the task;
2. list files/directories you expect to create or modify;
3. identify any assumptions.

Then implement only Task 1.

Do not implement alleles, genomes, breeding, mutation, Minecraft integration, NeoForge integration, Fabric support, items, blocks, commands, assets or UI.
```

---

## 11. Phase 1 Final Note

Do not rush into Minecraft.

The genetics engine is the foundation of the whole mod. If this phase is clean, deterministic, well-tested, and independent from platform APIs, the later NeoForge and Fabric work will be much safer.

A small but correct genetics core is more valuable than a flashy in-game prototype with hidden genetic bugs.
