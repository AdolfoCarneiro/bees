# 02 — Genetics Core Test Plan

## 1. Purpose

This test plan validates the pure Java genetics core.

This phase is the foundation of the mod. It must work without Minecraft, NeoForge, Fabric, entities, items, blocks, NBT, registries, or data attachments.

## 2. Scope

Test:

```text
Dominance
ChromosomeType
Allele
GeneticRandom
GenePair
Genome
BreedingService
BreedingResult
MutationDefinition
MutationResult
MutationService
```

## 3. Non-Scope

Do not test:

```text
Bee entities
NeoForge events
Fabric hooks
ItemStack data
Block entities
Analyzer item
Production items
GUI
```

Those belong to later phases.

## 4. Test Categories

## 4.1 Allele Tests

### Valid construction

Test:

```text
given stable id, chromosome type, dominance
when creating allele
then allele is created successfully
```

### Invalid construction

Test:

```text
null id fails
blank id fails
null chromosome type fails
null dominance fails
```

### Identity

Test:

```text
alleles can be compared by stable id and chromosome type
display names are not required for logic
```

## 4.2 ChromosomeType Tests

Verify MVP chromosome types exist:

```text
SPECIES
LIFESPAN
PRODUCTIVITY
FERTILITY
FLOWER_TYPE
```

Do not require future chromosomes in MVP tests.

## 4.3 GeneticRandom Tests

If a random abstraction exists, test:

```text
deterministic boolean sequence works
deterministic double sequence works
deterministic int sequence works if used
production wrapper delegates safely
```

## 4.4 GenePair Tests

### Validation

```text
null first allele fails
null second allele fails
different chromosome types fail
same chromosome type succeeds
```

### Dominance resolution

```text
dominant + recessive -> dominant active
recessive + dominant -> dominant active
dominant + dominant -> random-selected active
recessive + recessive -> random-selected active
```

### Persistence

```text
active allele remains stable after repeated reads
inactive allele remains stable after repeated reads
```

### Purity

```text
same allele id -> purebred
different allele id -> hybrid
```

## 4.5 Genome Tests

### Required species chromosome

```text
genome without SPECIES fails
genome with SPECIES succeeds
```

### Accessors

```text
can get gene pair by chromosome
can get active allele by chromosome
can get inactive allele by chromosome
can report purebred/hybrid by chromosome
```

### Immutability

```text
external map mutation does not mutate Genome
returned map is read-only or safe copy
```

## 4.6 BreedingService Tests

### Null validation

```text
null parent A fails
null parent B fails
```

### Pure parent crossing

Input:

```text
Parent A Species: Meadow / Meadow
Parent B Species: Forest / Forest
```

Expected:

```text
Child receives Meadow from A and Forest from B
```

### Hybrid parent crossing

Input:

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

### Multi-chromosome crossing

Test that breeding crosses:

```text
SPECIES
LIFESPAN
PRODUCTIVITY
FERTILITY
FLOWER_TYPE
```

### Parent immutability

```text
parent genomes do not change after breeding
```

## 4.7 BreedingResult Tests

If `BreedingResult` exists, verify it contains:

```text
child genome
optional inherited chromosome details
optional mutation metadata later
```

At minimum:

```text
child genome is not null
```

## 4.8 MutationDefinition Tests

Validation:

```text
id required
parent species A required
parent species B required
result species required
chance cannot be below 0
chance cannot be above 1
0 and 1 are valid boundaries
```

Matching:

```text
Meadow + Forest matches Forest + Meadow
Meadow + Arid does not match Meadow + Forest
```

## 4.9 MutationService Tests

### No match

```text
no matching mutation returns unchanged child
```

### Chance

```text
0% mutation never applies
100% mutation always applies when parents match
```

### Result mode

```text
partial mutation replaces one species allele
full mutation replaces both species alleles
```

### Immutability

```text
parent genomes unchanged
base child genome unchanged if immutable copy semantics are expected
```

## 5. Simulation Tests

## 5.1 Mendelian Distribution

Run:

```text
10,000 hybrid x hybrid crosses
```

Expected approximate distribution:

```text
25% A/A
50% A/B
25% B/B
```

Suggested tolerance:

```text
±5%
```

Adjust if test flakiness appears.

## 5.2 Mutation Distribution

For a mutation chance of:

```text
12%
```

Run:

```text
10,000 matching crosses
```

Expected:

```text
mutation count roughly around 12%
```

Suggested tolerance:

```text
±3%
```

## 5.3 Partial vs Full Mutation Distribution

If configured:

```text
partial: 95%
full: 5%
```

Run enough mutations to check approximate ratio.

Suggested tolerance:

```text
partial within ±5%
full within ±3%
```

## 6. Test Fixtures

Create reusable fixtures for:

```text
Meadow species allele
Forest species allele
Arid species allele
Cultivated species allele
Hardy species allele

Short/Normal/Long lifespan
Slow/Normal/Fast productivity
One/Two/Three fertility
Flowers/Cactus/Leaves flower type

Pure Meadow genome
Pure Forest genome
Pure Arid genome
Hybrid Meadow/Forest genome
Hybrid Forest/Arid genome
```

## 7. Go / No-Go Before Phase 2

Phase 1 can proceed to Phase 2 only when:

```text
- all core model tests pass;
- breeding service tests pass;
- mutation service tests pass;
- simulation tests pass or are intentionally marked as non-blocking;
- core code has no Minecraft/NeoForge/Fabric imports;
- active/inactive resolution is stable and persisted;
- randomness is injectable/testable.
```

## 8. Review Checklist

Before approving the genetics core:

```text
Does common genetics compile without game APIs?
Are invalid states rejected early?
Are all random choices testable?
Are active/inactive alleles persisted?
Are parents immutable during breeding?
Are mutation definitions validated?
Are statistical tests stable?
```
