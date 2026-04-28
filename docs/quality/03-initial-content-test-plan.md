# 03 — Initial Content Test Plan

## 1. Purpose

This plan validates the first built-in content set.

The content is intentionally small. Its purpose is to prove the genetic loop, not to create a large species tree.

## 2. Scope

Test built-in definitions for:

```text
Meadow
Forest
Arid
Cultivated
Hardy

Lifespan: Short, Normal, Long
Productivity: Slow, Normal, Fast
Fertility: One, Two, Three
FlowerType: Flowers, Cactus, Leaves

Meadow + Forest -> Cultivated
Forest + Arid -> Hardy
```

## 3. Non-Scope

Do not test:

```text
resource bees
Nether bees
End bees
magic bees
industrial bees
large mutation trees
JSON/datapack loading
assets/textures/models
```

## 4. Species Definition Tests

For each MVP species, verify:

```text
stable id exists
display name exists or is derivable
dominance exists
default traits exist
species can produce SPECIES allele
definition does not depend on Minecraft registry
```

Species:

```text
Meadow
Forest
Arid
Cultivated
Hardy
```

## 5. Trait Definition Tests

For each trait allele, verify:

```text
stable id exists
chromosome type is correct
dominance exists
allele can be used in GenePair
```

Required trait values:

```text
Lifespan: Short, Normal, Long
Productivity: Slow, Normal, Fast
Fertility: One, Two, Three
FlowerType: Flowers, Cactus, Leaves
```

## 6. Default Genome Tests

For each MVP species, verify default genome:

```text
has SPECIES chromosome
has LIFESPAN chromosome
has PRODUCTIVITY chromosome
has FERTILITY chromosome
has FLOWER_TYPE chromosome
all chromosomes have valid GenePairs
default genome is valid according to Genome rules
```

Suggested default wild genomes:

```text
Meadow / Meadow
Forest / Forest
Arid / Arid
```

Mutated species default genomes may be pure by default:

```text
Cultivated / Cultivated
Hardy / Hardy
```

## 7. Mutation Content Tests

Verify:

```text
Meadow + Forest -> Cultivated exists
Forest + Arid -> Hardy exists
parent order does not matter
mutation chance is valid
result species exists
mutation result mode is explicit or defaulted clearly
```

## 8. Built-In Registry / Facade Tests

If a built-in content facade exists, verify:

```text
can find species by id
can find trait allele by id
can list MVP species
can list mutation definitions
unknown id returns empty/error consistently
duplicate ids are rejected or impossible
```

## 9. No Scattered Content Checks

Review manually or with tests where possible:

```text
species ids are centralized
mutation definitions are centralized
default trait definitions are centralized
no random if (species == "meadow") logic is scattered across services
```

## 10. Integration with Genetics Core

Test scenarios:

```text
create Meadow genome
create Forest genome
breed Meadow x Forest
apply Meadow + Forest mutation at 100%
result can produce Cultivated species
```

Test:

```text
create Forest genome
create Arid genome
apply Forest + Arid mutation at 100%
result can produce Hardy species
```

## 11. Go / No-Go Before Phase 3

Phase 2 can proceed to NeoForge integration only when:

```text
- all MVP species definitions exist;
- all MVP trait definitions exist;
- default genomes can be created;
- initial mutations exist and are valid;
- built-in definitions are centralized;
- content code has no Minecraft/NeoForge/Fabric dependency unless explicitly platform-only;
- tests can create and use all MVP genomes.
```

## 12. Review Checklist

```text
Is the content set still small?
Are resource bees still excluded?
Can every MVP species create a valid genome?
Are mutation references valid?
Can definitions be moved to JSON later without major redesign?
```
