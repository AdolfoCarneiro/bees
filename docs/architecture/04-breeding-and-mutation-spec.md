> **Canonical technical spec — breeding and mutation.**  
> Vanilla-style breeding flow with genetic rules and mutation behavior.  
> Product direction: `docs/post-mvp/gameplay-direction.md`, priorities: `docs/ROADMAP.md`.

# 04 — Breeding and Mutation Specification

## 1. Goal

Breeding should feel like vanilla Minecraft interaction, but with Forestry-inspired genetic depth.

The player should be able to breed two bees in the world using flowers, and the baby bee should receive a genome based on the parents.

Mutation should be probabilistic and should sometimes create a new species.

Detailed execution specs:

```text
docs/implementation/01-genetics-core-implementation.md
docs/implementation/04-vanilla-breeding-integration.md
```

## 2. Player-Facing Breeding Flow

Initial player flow:

```text
1. Player finds two bees.
2. Player feeds each bee a valid breeding item, usually flowers.
3. Bees enter love mode.
4. Vanilla Minecraft creates a baby bee.
5. The mod assigns the baby a genetic result based on both parents.
6. If a mutation occurs, the player gets subtle feedback.
7. The player can later inspect the baby with an analyzer.
```

## 3. System Breeding Flow

Technical flow:

```text
1. Platform hook detects baby bee creation.
2. Platform layer identifies parent bees and child bee.
3. Platform layer reads parent genomes.
4. If needed, missing parent genomes are initialized.
5. Common breeding orchestrator receives:
   - parent A genome
   - parent B genome
   - environment context
   - random source
6. BreedingService creates inherited child genome.
7. MutationService checks mutation possibilities.
8. Final child genome is returned.
9. Platform layer stores final genome on child bee.
10. Platform layer triggers feedback if mutation occurred.
```

## 4. Missing Genome Handling

There should be a safe fallback for bees without genome data.

Possible cases:

- bee spawned before the mod was installed;
- bee created by another mod;
- data failed to deserialize;
- development/debug world has old entities.

Recommended behavior:

```text
If a bee has no genome:
    assign a wild fallback genome based on biome/context.
```

Alternative debug behavior:

```text
Log a warning when a parent has no genome.
```

Do not crash normal gameplay because a bee is missing a genome.

## 5. Breeding Service Input

The breeding service should receive:

```text
ParentGenome A
ParentGenome B
GeneticRandom random
```

The mutation service should receive:

```text
InheritedChildGenome
ParentGenome A
ParentGenome B
EnvironmentContext
AvailableMutationDefinitions
GeneticRandom random
```

## 6. Breeding Service Output

Output should include more than just the child genome.

Recommended:

```text
BreedingResult
- Genome childGenome
- List<InheritedChromosomeResult>
- boolean mutationOccurred
- Optional<MutationResult> mutationResult
```

The extra information is useful for:

- debug commands;
- analyzer history later;
- advancement triggers;
- visual feedback;
- unit tests.

## 7. Inheritance Algorithm

For every chromosome:

```text
1. Choose one allele from parent A.
2. Choose one allele from parent B.
3. Create a child gene pair.
4. Resolve active/inactive allele by dominance.
5. Store result.
```

Pseudo-code:

```text
for each chromosomeType:
    parentAGene = parentA.get(chromosomeType)
    parentBGene = parentB.get(chromosomeType)

    alleleFromA = random.pick(parentAGene.alleleA, parentAGene.alleleB)
    alleleFromB = random.pick(parentBGene.alleleA, parentBGene.alleleB)

    childGene = GenePair.resolve(alleleFromA, alleleFromB, random)

    childGenome.set(chromosomeType, childGene)
```

## 8. Mutation Timing

Mutation should happen after normal inheritance.

Recommended order:

```text
1. Generate inherited child genome.
2. Evaluate mutation rules using parents and environment.
3. If mutation occurs, modify species chromosome.
4. Re-resolve species active/inactive if needed.
5. Return final genome.
```

Reason:

```text
This preserves Mendelian inheritance as the base rule, while mutation acts as an additional event.
```

## 9. Mutation Definition

A mutation should include:

```text
id
parent species A
parent species B
result species
base chance
environment requirements
modifiers
result mode rules
```

Initial conceptual structure:

```json
{
  "id": "curious_bees:cultivated_from_meadow_forest",
  "parents": ["curious_bees:meadow", "curious_bees:forest"],
  "result": "curious_bees:cultivated",
  "baseChance": 0.12,
  "allowedBiomes": [
    "minecraft:plains",
    "minecraft:forest",
    "minecraft:flower_forest"
  ],
  "resultModes": {
    "partialChance": 0.95,
    "fullChance": 0.05
  }
}
```

This JSON is a future format. Initial implementation can be hardcoded using the same model.

## 10. Parent Matching

Mutation should match parent species regardless of order.

This should match:

```text
Parent A = Meadow
Parent B = Forest
```

And also:

```text
Parent A = Forest
Parent B = Meadow
```

## 11. Which Parent Species Count?

For MVP, use active species of each parent.

Example:

```text
Parent A active species: Meadow
Parent B active species: Forest

Can trigger:
Meadow + Forest -> Cultivated
```

Future options:

- consider inactive species too;
- require specific active/inactive combinations;
- give lower chance if mutation pair exists only in inactive alleles.

Do not implement those future rules in the first version unless explicitly requested.

## 12. Mutation Chance

Mutation chance formula for MVP:

```text
finalChance = baseChance * environmentMultiplier * frameMultiplier * otherModifiers
```

For initial vanilla breeding, frameMultiplier can be `1.0`.

Environment requirements can be simple:

```text
If mutation requires biome and current biome is not allowed:
    chance = 0
```

## 13. Partial vs Full Mutation

If mutation occurs, choose result mode.

### Partial Result

One allele becomes the result species.

Example:

```text
Inherited child species: Meadow / Forest
Mutation result: Cultivated
Final species: Cultivated / Forest
```

This should be common.

### Full Result

Both alleles become the result species.

Example:

```text
Inherited child species: Meadow / Forest
Mutation result: Cultivated
Final species: Cultivated / Cultivated
```

This should be rare.

Initial default:

```text
Partial mutation: 95%
Full mutation: 5%
```

These numbers are placeholders.

## 14. Mutation Feedback

When a mutation occurs, the player should receive minimal feedback.

MVP feedback options:

```text
- special particles around the baby bee;
- subtle sound;
- advancement trigger;
- debug log during development.
```

Avoid large GUI or complex announcements in the MVP.

## 15. Mutation Discovery

For development:

```text
Show all mutation details in debug/analyzer output.
```

For final gameplay, possible progression:

```text
Basic analyzer:
    Shows species and main traits.

Advanced analyzer:
    Shows inactive alleles and dominance.

Research/progress system:
    Reveals known mutation recipes after discovery.
```

This discovery system is future scope.

## 16. Environment Context

Environment context should be passed to the mutation service.

Initial context fields:

```text
biomeId
dimensionId
isDay
isRaining
temperatureCategory
humidityCategory
nearbyBlocks
```

MVP may only use:

```text
biomeId
dimensionId
```

## 17. Flower Type and Breeding Item

For MVP, keep vanilla flower breeding.

Future rule:

```text
The active FlowerType trait may determine what item can breed that bee.
```

Example:

```text
Flowers -> normal flowers
Cactus -> cactus flower or cactus-related item
Leaves -> leaves/saplings/forest item
```

Do not make early breeding too restrictive before the analyzer exists.

## 18. Fertility and Vanilla Breeding

Vanilla breeding normally creates one child.

For MVP, fertility can be stored and displayed but not strongly affect vanilla breeding.

Future possibilities:

```text
- Higher fertility increases chance of extra larvae in apiaries.
- Higher fertility reduces breeding cooldown.
- Higher fertility increases chance of multiple genetic samples.
```

Avoid spawning multiple live baby bees from vanilla breeding until balance is understood.

## 19. Lifespan and Vanilla Bees

Vanilla bees do not naturally die from production cycles like Forestry queens.

For MVP, lifespan can be stored and displayed but not heavily used.

Future possibilities:

```text
- Lifespan affects tech apiary cycles.
- Lifespan affects number of production operations.
- Lifespan affects bee aging in controlled environments.
```

## 20. Validation

Detailed test plans:

```text
docs/quality/02-genetics-core-test-plan.md
docs/quality/05-vanilla-breeding-test-plan.md
```

Implementation execution:

```text
docs/implementation/01-genetics-core-implementation.md
docs/implementation/04-vanilla-breeding-integration.md
```

## 21. Acceptance Criteria

Breeding/mutation is implemented when:

```text
- Two bees with genomes can produce a child genome.
- The child genome inherits one allele from each parent per chromosome.
- Active/inactive alleles are resolved and persisted.
- A mutation can occur after inheritance.
- Mutation can create a partial hybrid.
- Mutation can create a purebred result.
- Mutation rules can be tested without Minecraft.
- NeoForge integration assigns the resulting genome to the baby bee.
- Missing parent genomes are handled safely.
```
