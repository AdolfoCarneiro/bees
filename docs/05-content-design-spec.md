# 05 — Content Design Specification

## 1. Goal

This document defines the initial content plan for the bee genetics mod.

The purpose of the initial content is not to be large. It is to prove the genetic loop with a small, understandable set of species, traits, mutations, and products.

## 2. Content Philosophy

### 2.1 Start Small

The MVP should use only five species:

```txt
Meadow
Forest
Arid
Cultivated
Hardy
```

This is enough to test:

- Wild species
- Biome-based starting species
- Crossbreeding
- Mutation
- Hybrids
- Purebred stabilization
- Analyzer output
- Basic production

### 2.2 Avoid Resource Bees Early

Do not add iron, gold, diamond, redstone, emerald, netherite, uranium, or other resource bees in the MVP.

Resource bees are future content.

The MVP should make the breeding system fun before resource production exists.

### 2.3 Species Should Have Identity

A species should eventually have:

```txt
- Environmental preference
- Trait tendencies
- Production identity
- Mutation role
- Progression position
```

Avoid species that differ only by output item.

### 2.4 Production Should Support Hybrids

Production should use active and inactive species.

Example:

```txt
Cultivated / Forest
```

Possible behavior:

```txt
Main output comes from Cultivated.
Small secondary chance comes from Forest.
```

This makes hybrid bees interesting instead of just "failed purebreds."

## 3. Initial Species

## 3.1 Meadow Bee

Role:

```txt
Starter wild species.
Common plains/flower field bee.
Stable generalist.
```

Spawn context:

```txt
Plains
Flower Forest
Meadow-like biomes
Fallback starter biome
```

Suggested default traits:

```txt
Species: Meadow / Meadow
Lifespan: Normal / Normal
Productivity: Normal / Normal
Fertility: Two / Two
FlowerType: Flowers / Flowers
```

Dominance:

```txt
Dominant
```

Production concept:

```txt
Honeycomb
Basic Wax
```

Mutation role:

```txt
Meadow + Forest -> Cultivated
```

## 3.2 Forest Bee

Role:

```txt
Starter wild species.
Forest progression bee.
Useful for early mutation paths.
```

Spawn context:

```txt
Forest
Birch Forest
Dark Forest
Taiga-like forest variants, if desired later
```

Suggested default traits:

```txt
Species: Forest / Forest
Lifespan: Normal / Normal
Productivity: Normal / Normal
Fertility: Two / Two
FlowerType: Leaves / Leaves
```

Dominance:

```txt
Dominant or Recessive
```

Initial recommendation:

```txt
Dominant
```

Production concept:

```txt
Honeycomb
Resin-like forest byproduct
```

Mutation role:

```txt
Meadow + Forest -> Cultivated
Forest + Arid -> Hardy
```

## 3.3 Arid Bee

Role:

```txt
Starter wild species for hot/dry environments.
Introduces non-flower environmental identity.
```

Spawn context:

```txt
Desert
Savanna
Badlands
```

Suggested default traits:

```txt
Species: Arid / Arid
Lifespan: Normal / Normal
Productivity: Slow / Normal
Fertility: One / Two
FlowerType: Cactus / Cactus
```

Dominance:

```txt
Recessive
```

Production concept:

```txt
Dry Comb
Wax
Possible cactus-related byproduct later
```

Mutation role:

```txt
Forest + Arid -> Hardy
```

## 3.4 Cultivated Bee

Role:

```txt
First successful mutation.
Early proof that breeding can create new species.
Bridge between wild bees and managed beekeeping.
```

Source:

```txt
Meadow + Forest -> Cultivated
```

Suggested mutation chance:

```txt
12%
```

Suggested default traits:

```txt
Species: Cultivated
Lifespan: Normal
Productivity: Fast
Fertility: Two
FlowerType: Flowers
```

Dominance:

```txt
Dominant
```

Production concept:

```txt
Cultivated Comb
More efficient honey/wax output
```

Progression role:

```txt
Used later as parent for improved bees.
```

## 3.5 Hardy Bee

Role:

```txt
Early environmental mutation.
Represents resilience and difficult climates.
```

Source:

```txt
Forest + Arid -> Hardy
```

Suggested mutation chance:

```txt
8%
```

Suggested default traits:

```txt
Species: Hardy
Lifespan: Long
Productivity: Normal
Fertility: Two
FlowerType: Flowers or Cactus
```

Dominance:

```txt
Recessive or Dominant
```

Initial recommendation:

```txt
Recessive
```

Production concept:

```txt
Hardy Comb
Durable wax / future frame-related material
```

Progression role:

```txt
Foundation for climate-tolerant and harsh-environment bees.
```

## 4. Initial Trait Values

## 4.1 Lifespan

Values:

```txt
Short
Normal
Long
```

Initial gameplay:

```txt
Displayed only or lightly used.
```

Future gameplay:

```txt
Affects tech apiary cycles or productive lifetime.
```

## 4.2 Productivity

Values:

```txt
Slow
Normal
Fast
```

Initial gameplay:

```txt
Affects production chance/rate once production exists.
```

Suggested multipliers:

```txt
Slow: 0.75x
Normal: 1.00x
Fast: 1.25x
```

These numbers are placeholders.

## 4.3 Fertility

Values:

```txt
One
Two
Three
```

Initial gameplay:

```txt
Displayed only or lightly used.
```

Future gameplay:

```txt
Affects extra larvae/samples in tech apiaries.
```

## 4.4 Flower Type

Values:

```txt
Flowers
Cactus
Leaves
```

Initial gameplay:

```txt
Displayed and used for flavor.
```

Future gameplay:

```txt
Determines valid breeding/production environment.
```

## 5. Initial Mutation Tree

MVP tree:

```txt
Meadow ─┐
        ├── Cultivated
Forest ─┘

Forest ─┐
        ├── Hardy
Arid ───┘
```

Future early expansion:

```txt
Cultivated + Hardy -> Noble or Resilient
Cultivated + Meadow -> Diligent
Hardy + Arid -> Desert-adapted species
Forest + Cultivated -> Arboreal/Managed Forest species
```

Do not implement future expansion before the first loop works.

## 6. Production Design

## 6.1 Production Rule

Recommended base rule:

```txt
Active species controls primary production.
Inactive species can contribute secondary production.
```

Example:

```txt
Bee: Cultivated / Forest

Primary:
- Cultivated Comb

Secondary:
- Forest byproduct
```

## 6.2 Why Inactive Production Matters

This makes hybrid bees useful and interesting.

Without inactive influence, players only care about active species. With inactive influence, a hybrid may be valuable even before being purified.

## 6.3 Initial Products

MVP products:

```txt
Honeycomb
Meadow Comb
Forest Comb
Arid Comb
Cultivated Comb
Hardy Comb
Wax
```

It is acceptable to reduce this further during implementation.

## 7. Analyzer Content Display

Initial analyzer report should show:

```txt
Species: Cultivated / Forest
Purity: Hybrid
Lifespan: Normal / Long
Productivity: Fast / Normal
Fertility: Two / Three
Flower Type: Flowers / Leaves
```

Dominance display can use symbols before colors exist:

```txt
[D] Dominant
[R] Recessive
[A] Active
[I] Inactive
```

Example:

```txt
Species:
[A][D] Cultivated
[I][D] Forest
```

## 8. Content Loading Strategy

Initial implementation:

```txt
Hardcoded content definitions in a centralized built-in content registry.
```

Do not scatter species definitions throughout code.

Good:

```txt
BuiltinBeeSpecies
BuiltinBeeTraits
BuiltinBeeMutations
BuiltinBeeProducts
```

Bad:

```txt
if (species == "meadow") scattered across many services
```

Future implementation:

```txt
JSON/datapack-driven content
```

Potential future paths:

```txt
data/bee_genetics/species/meadow.json
data/bee_genetics/mutations/cultivated_from_meadow_forest.json
data/bee_genetics/products/cultivated_comb.json
data/bee_genetics/traits/productivity/fast.json
```

## 9. Balance Defaults

These are initial placeholders:

```txt
Meadow + Forest -> Cultivated: 12%
Forest + Arid -> Hardy: 8%
Partial mutation: 95%
Full mutation: 5%
```

Trait multipliers:

```txt
Slow productivity: 0.75x
Normal productivity: 1.00x
Fast productivity: 1.25x
```

Do not over-balance before gameplay exists.

## 10. Naming Guidelines

Species names should be:

- Easy to understand.
- Thematic.
- Not too close to Productive Bees if avoidable.
- Not necessarily identical to Forestry names.

MVP names can stay simple:

```txt
Meadow
Forest
Arid
Cultivated
Hardy
```

Future resource species can be decided later.

## 11. Future Content Categories

### 11.1 Biome Bees

Examples:

```txt
Swamp
Tundra
Jungle
Mountain
Oceanic
```

### 11.2 Nether Bees

Examples:

```txt
Ashen
Blazing
Soul
Basalt
Crimson
Warped
```

### 11.3 End Bees

Examples:

```txt
Ender
Void
Chorus
Shulker
Draconic
```

### 11.4 Resource Bees

Resource bees should come much later.

Possible design directions:

```txt
Direct names:
- Iron Bee
- Copper Bee
- Redstone Bee

Thematic names:
- Ferric Bee
- Cupric Bee
- Resonant Bee
```

No decision is needed now.

### 11.5 Industrial Bees

Could support tech progression and machine outputs.

### 11.6 Magic Bees

Could support future magic-themed content or mod compat.

### 11.7 Compatibility Bees

Possible future integrations:

```txt
Create
Mekanism
Thermal
Farmer's Delight
Botania-like mods
Ars Nouveau-like mods
```

Do not implement compatibility during MVP.

## 12. Acceptance Criteria

Initial content design is complete when:

```txt
- Five initial species are defined.
- Two initial mutations are defined.
- MVP trait values are defined.
- Wild spawn contexts are defined.
- Basic product concepts are defined.
- Content is centralized.
- Future JSON conversion remains possible.
```
