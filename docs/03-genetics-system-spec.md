# 03 — Genetics System Specification

## 1. Goal

The genetics system models bee inheritance in a way inspired by Forestry:

- each relevant trait is represented by a chromosome;
- each chromosome has two alleles;
- one allele becomes active;
- one allele becomes inactive;
- dominance affects which allele is active;
- offspring inherit one allele from each parent;
- mutations can modify the species chromosome.

The system should be deterministic when given deterministic random input, so it can be unit-tested.

Detailed execution tasks for this system are in:

```text
docs/implementation/01-genetics-core-implementation.md
```

## 2. Core Terms

### Genome

A complete set of chromosomes for one bee.

Example:

```text
Genome
- Species: Meadow / Forest
- Lifespan: Normal / Long
- Productivity: Normal / Fast
- Fertility: 2 / 3
- FlowerType: Flowers / Leaves
```

### Chromosome

A category of genetic information.

Examples:

```text
Species
Lifespan
Productivity
Fertility
FlowerType
```

### Allele

A single genetic value inside a chromosome.

Examples:

```text
Species allele: Meadow
Productivity allele: Fast
Fertility allele: 3
FlowerType allele: Flowers
```

### Gene Pair

A pair of alleles for one chromosome.

Example:

```text
Species:
- first allele: Meadow
- second allele: Forest
- active allele: Meadow
- inactive allele: Forest
```

### Active Allele

The allele currently expressed by the bee.

For species, this is the visible or behaviorally dominant species.

### Inactive Allele

The allele carried by the bee but not currently expressed.

This can still be inherited by offspring.

### Purebred

A gene pair where both alleles are the same.

Example:

```text
Meadow / Meadow
```

### Hybrid

A gene pair where the two alleles are different.

Example:

```text
Cultivated / Forest
```

## 3. Chromosome Types

MVP chromosome types:

```text
SPECIES
LIFESPAN
PRODUCTIVITY
FERTILITY
FLOWER_TYPE
```

Future chromosome types:

```text
TEMPERATURE_TOLERANCE
HUMIDITY_TOLERANCE
TERRITORY
EFFECT
NOCTURNAL
CAVE_DWELLING
WEATHER_TOLERANCE
```

Do not implement future chromosome behavior in the MVP unless explicitly requested.

## 4. Dominance

Each allele has a dominance value.

Initial values:

```text
DOMINANT
RECESSIVE
```

Possible future values:

```text
INCOMPLETE_DOMINANT
CO_DOMINANT
```

Do not implement future dominance modes in the MVP.

## 5. Dominance Resolution

When a gene pair is created, active/inactive alleles are resolved once.

Rules:

```text
If one allele is dominant and the other is recessive:
    active = dominant
    inactive = recessive

If both alleles have the same dominance:
    active = random allele
    inactive = the other allele
```

Important:

```text
The active/inactive result must be persisted.
```

Do not recalculate active/inactive every time the gene is read.

## 6. Mendelian Inheritance

For each chromosome:

```text
childAlleleA = random allele from parent A gene pair
childAlleleB = random allele from parent B gene pair
childGenePair = resolve(childAlleleA, childAlleleB)
```

Each parent contributes exactly one allele per chromosome.

The selection from each parent is 50/50.

## 7. Example: Pure Parents

Input:

```text
Parent A Species: Meadow / Meadow
Parent B Species: Forest / Forest
```

Possible child:

```text
Meadow / Forest
```

Expected distribution:

```text
100% Meadow / Forest
```

Active species depends on dominance.

## 8. Example: Hybrid Parents

Input:

```text
Parent A Species: Meadow / Forest
Parent B Species: Meadow / Forest
```

Expected approximate distribution:

```text
25% Meadow / Meadow
50% Meadow / Forest
25% Forest / Forest
```

Order may vary, but genetically equivalent pairs should be normalized or compared carefully in tests.

## 9. Gene Pair Normalization

For display and comparison, it may be useful to normalize pairs.

However, do not destroy active/inactive information.

Possible internal structure:

```text
GenePair
- alleleA
- alleleB
- activeAllele
- inactiveAllele
```

Possible helper methods:

```text
isPurebred()
isHybrid()
containsAllele(id)
hasActiveAllele(id)
hasInactiveAllele(id)
```

## 10. Species Chromosome

The species chromosome is special because mutations mainly affect it.

Example:

```text
Species:
- active: Cultivated
- inactive: Forest
```

This means:

- the bee behaves primarily as Cultivated;
- the bee can still pass Forest to offspring;
- production may use Cultivated as primary and Forest as secondary.

## 11. Trait Chromosomes

### Lifespan

Initial values:

```text
SHORT
NORMAL
LONG
```

Possible effects:

```text
SHORT: fewer cycles, easier early balance
NORMAL: default
LONG: more cycles or longer productive lifetime
```

In MVP, lifespan can exist without strong gameplay impact.

### Productivity

Initial values:

```text
SLOW
NORMAL
FAST
```

Possible effects:

```text
SLOW: lower production rate
NORMAL: default production rate
FAST: higher production rate
```

### Fertility

Initial values:

```text
ONE
TWO
THREE
```

Possible effects:

```text
ONE: fewer offspring or lower breeding yield
TWO: default
THREE: more offspring or better chance of extra larvae later
```

In vanilla-style breeding, one baby normally spawns. Fertility can initially be recorded but not heavily used, or it can affect future tech apiary output.

### Flower Type

Initial values:

```text
FLOWERS
CACTUS
LEAVES
```

Possible effects:

```text
FLOWERS: standard flower breeding/production
CACTUS: desert/arid progression
LEAVES: forest progression
```

For MVP, Flower Type can be used as a future condition and displayed in analyzer.

## 12. Allele Identity

Every allele should have a stable ID.

Examples:

```text
curious_bees:species/meadow
curious_bees:species/forest
curious_bees:productivity/fast
curious_bees:fertility/three
curious_bees:flower_type/flowers
```

Avoid relying on display names as IDs.

## 13. Built-in Definitions

Initial built-in species:

```text
Meadow
Forest
Arid
Cultivated
Hardy
```

Initial built-in trait alleles:

```text
Lifespan: Short, Normal, Long
Productivity: Slow, Normal, Fast
Fertility: One, Two, Three
FlowerType: Flowers, Cactus, Leaves
```

Detailed implementation is in:

```text
docs/implementation/02-initial-content-implementation.md
```

## 14. Genome Creation

Wild bee genome creation should produce pure or mostly pure species.

Example:

```text
Meadow wild bee:
Species: Meadow / Meadow
Lifespan: Normal / Normal
Productivity: Normal / Normal
Fertility: Two / Two
FlowerType: Flowers / Flowers
```

Optional variation can be added later.

## 15. Hybrid Detection

A genome can have hybrid status at different levels.

Species hybrid:

```text
Species gene pair has two different species alleles.
```

Trait hybrid:

```text
Any trait gene pair has two different alleles.
```

Overall hybrid:

```text
At least one relevant chromosome is hybrid.
```

For initial analyzer, species purity is the most important.

## 16. Mutation Interaction

Mutation normally modifies the species chromosome only.

Possible mutation result modes:

```text
PARTIAL
FULL
```

### Partial Mutation

Only one species allele is replaced.

Example:

```text
Before: Meadow / Forest
After: Cultivated / Forest
```

### Full Mutation

Both species alleles become the mutation result.

Example:

```text
Before: Meadow / Forest
After: Cultivated / Cultivated
```

Full mutation should be rarer.

## 17. Randomness and Testing

Core services should accept an injectable random abstraction.

Example behavior:

```text
random.nextBoolean() chooses inherited allele.
random.nextDouble() evaluates mutation probability.
```

Tests should use deterministic random sequences where exact behavior matters.

Statistical tests should allow tolerance.

Example:

```text
Run 10,000 crosses.
Expected 25/50/25 distribution.
Accept if each bucket is within tolerance.
```

Detailed validation is in:

```text
docs/quality/02-genetics-core-test-plan.md
```

## 18. Required Core Classes

Minimum model:

```text
Allele
Dominance
GenePair
Genome
ChromosomeType
BreedingService
BreedingResult
MutationDefinition
MutationService
MutationResult
GeneticRandom
```

## 19. Acceptance Criteria

The genetics core is complete when:

```text
- It can represent a full bee genome.
- It can resolve active/inactive alleles by dominance.
- It persists active/inactive state.
- It can cross two genomes.
- It can detect purebred and hybrid gene pairs.
- It can apply mutation rules to species.
- It has unit tests for expected inheritance behavior.
- It has no dependency on Minecraft, NeoForge, Fabric, or game classes.
```
