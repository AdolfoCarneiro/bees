# 03 — Genetics System Specification

## 1. Goal

The genetics system models bee inheritance in a way inspired by Forestry:

- Each relevant trait is represented by a chromosome.
- Each chromosome has two alleles.
- One allele becomes active.
- One allele becomes inactive.
- Dominance affects which allele is active.
- Offspring inherit one allele from each parent.
- Mutations can modify the species chromosome.

The system should be deterministic when given deterministic random input, so it can be unit-tested.

## 2. Core Terms

### Genome

A complete set of chromosomes for one bee.

Example:

```txt
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

```txt
Species
Lifespan
Productivity
Fertility
FlowerType
```

### Allele

A single genetic value inside a chromosome.

Examples:

```txt
Species allele: Meadow
Productivity allele: Fast
Fertility allele: 3
FlowerType allele: Flowers
```

### Gene Pair

A pair of alleles for one chromosome.

Example:

```txt
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

```txt
Meadow / Meadow
```

### Hybrid

A gene pair where the two alleles are different.

Example:

```txt
Cultivated / Forest
```

## 3. Chromosome Types

MVP chromosome types:

```txt
SPECIES
LIFESPAN
PRODUCTIVITY
FERTILITY
FLOWER_TYPE
```

Future chromosome types:

```txt
TEMPERATURE_TOLERANCE
HUMIDITY_TOLERANCE
TERRITORY
EFFECT
NOCTURNAL
CAVE_DWELLING
WEATHER_TOLERANCE
```

## 4. Dominance

Each allele has a dominance value.

Initial values:

```txt
DOMINANT
RECESSIVE
```

Possible future values:

```txt
INCOMPLETE_DOMINANT
CO_DOMINANT
```

Do not implement future dominance modes in the MVP.

## 5. Dominance Resolution

When a gene pair is created, active/inactive alleles are resolved once.

Rules:

```txt
If one allele is dominant and the other is recessive:
    active = dominant
    inactive = recessive

If both alleles have the same dominance:
    active = random allele
    inactive = the other allele
```

Important:

```txt
The active/inactive result must be persisted.
```

Do not recalculate active/inactive every time the gene is read.

## 6. Mendelian Inheritance

For each chromosome:

```txt
childAlleleA = random allele from parent A gene pair
childAlleleB = random allele from parent B gene pair
childGenePair = resolve(childAlleleA, childAlleleB)
```

Each parent contributes exactly one allele per chromosome.

The selection from each parent is 50/50.

## 7. Example: Pure Parents

Input:

```txt
Parent A Species: Meadow / Meadow
Parent B Species: Forest / Forest
```

Possible child:

```txt
Meadow / Forest
```

Expected distribution:

```txt
100% Meadow / Forest
```

Active species depends on dominance.

## 8. Example: Hybrid Parents

Input:

```txt
Parent A Species: Meadow / Forest
Parent B Species: Meadow / Forest
```

Expected approximate distribution:

```txt
25% Meadow / Meadow
50% Meadow / Forest
25% Forest / Forest
```

Order may vary, but genetically equivalent pairs should be normalized or compared carefully in tests.

## 9. Gene Pair Normalization

For display and comparison, it may be useful to normalize pairs.

However, do not destroy active/inactive information.

Possible internal structure:

```txt
GenePair
- alleleA
- alleleB
- activeAllele
- inactiveAllele
```

Possible helper methods:

```txt
isPurebred()
isHybrid()
containsAllele(id)
hasActiveAllele(id)
hasInactiveAllele(id)
```

## 10. Species Chromosome

The species chromosome is special because mutations mainly affect it.

Example:

```txt
Species:
- active: Cultivated
- inactive: Forest
```

This means:

- The bee behaves primarily as Cultivated.
- The bee can still pass Forest to offspring.
- Production may use Cultivated as primary and Forest as secondary.

## 11. Trait Chromosomes

### Lifespan

Initial values:

```txt
SHORT
NORMAL
LONG
```

Possible effects:

```txt
SHORT: fewer cycles, easier early balance
NORMAL: default
LONG: more cycles or longer productive lifetime
```

In MVP, lifespan can exist without strong gameplay impact.

### Productivity

Initial values:

```txt
SLOW
NORMAL
FAST
```

Possible effects:

```txt
SLOW: lower production rate
NORMAL: default production rate
FAST: higher production rate
```

### Fertility

Initial values:

```txt
ONE
TWO
THREE
```

Possible effects:

```txt
ONE: fewer offspring or lower breeding yield
TWO: default
THREE: more offspring or better chance of extra larvae later
```

In vanilla-style breeding, one baby normally spawns. Fertility can initially be recorded but not heavily used, or it can affect future tech apiary output.

### Flower Type

Initial values:

```txt
FLOWERS
CACTUS
LEAVES
```

Possible effects:

```txt
FLOWERS: standard flower breeding/production
CACTUS: desert/arid progression
LEAVES: forest progression
```

For MVP, Flower Type can be used as a future condition and displayed in analyzer.

## 12. Allele Identity

Every allele should have a stable ID.

Examples:

```txt
bee_genetics:species/meadow
bee_genetics:species/forest
bee_genetics:productivity/fast
bee_genetics:fertility/three
bee_genetics:flower_type/flowers
```

Avoid relying on display names as IDs.

## 13. Built-in Definitions

Initial built-in species:

```txt
Meadow
Forest
Arid
Cultivated
Hardy
```

Initial built-in trait alleles:

```txt
Lifespan: Short, Normal, Long
Productivity: Slow, Normal, Fast
Fertility: One, Two, Three
FlowerType: Flowers, Cactus, Leaves
```

## 14. Genome Creation

Wild bee genome creation should produce pure or mostly pure species.

Example:

```txt
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

```txt
Species gene pair has two different species alleles.
```

Trait hybrid:

```txt
Any trait gene pair has two different alleles.
```

Overall hybrid:

```txt
At least one relevant chromosome is hybrid.
```

For initial analyzer, species purity is the most important.

## 16. Mutation Interaction

Mutation normally modifies the species chromosome only.

Possible mutation result modes:

```txt
PARTIAL
FULL
```

### Partial Mutation

Only one species allele is replaced.

Example:

```txt
Before: Meadow / Forest
After: Cultivated / Forest
```

### Full Mutation

Both species alleles become the mutation result.

Example:

```txt
Before: Meadow / Forest
After: Cultivated / Cultivated
```

Full mutation should be rarer.

## 17. Randomness and Testing

Core services should accept an injectable random abstraction.

Example behavior:

```txt
random.nextBoolean() chooses inherited allele.
random.nextDouble() evaluates mutation probability.
```

Tests should use deterministic random sequences where exact behavior matters.

Statistical tests should allow tolerance.

Example:

```txt
Run 10,000 crosses.
Expected 25/50/25 distribution.
Accept if each bucket is within tolerance.
```

## 18. Required Core Classes

Minimum model:

```txt
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

```txt
- It can represent a full bee genome.
- It can resolve active/inactive alleles by dominance.
- It persists active/inactive state.
- It can cross two genomes.
- It can detect purebred and hybrid gene pairs.
- It can apply mutation rules to species.
- It has unit tests for expected inheritance behavior.
- It has no dependency on Minecraft, NeoForge, Fabric, or game classes.
```
