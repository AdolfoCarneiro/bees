> Status: MVP foundation document.
>
> This document describes the original MVP design used to validate the Curious Bees core loop.
> It is preserved as historical and architectural context.
> For the current post-MVP direction, see:
> `docs/post-mvp/11-post-mvp-productization-roadmap.md`.

# 05 — Content Design Specification

## 1. Goal

This document defines the initial content plan for Curious Bees.

The purpose of the initial content is not to be large. It is to prove the genetic loop with a small, understandable set of species, traits, mutations, and products.

Detailed execution specs:

```text
docs/implementation/02-initial-content-implementation.md
docs/implementation/08-data-driven-content.md
docs/implementation/09-expanded-content-roadmap.md
```

## 2. Content Philosophy

### 2.1 Start Small

The MVP should use only five species:

```text
Meadow
Forest
Arid
Cultivated
Hardy
```

This is enough to test:

- wild species;
- biome-based starting species;
- crossbreeding;
- mutation;
- hybrids;
- purebred stabilization;
- analyzer output;
- basic production.

### 2.2 Avoid Resource Bees Early

Do not add iron, gold, diamond, redstone, emerald, netherite, uranium, or other resource bees in the MVP.

Resource bees are future content.

The MVP should make the breeding system fun before resource production exists.

### 2.3 Species Should Have Identity

A species should eventually have:

```text
- environmental preference
- trait tendencies
- production identity
- mutation role
- progression position
```

Avoid species that differ only by output item.

### 2.4 Production Should Support Hybrids

Production should use active and inactive species.

Example:

```text
Cultivated / Forest
```

Possible behavior:

```text
Main output comes from Cultivated.
Small secondary chance comes from Forest.
```

This makes hybrid bees interesting instead of just failed purebreds.

### 2.5 Built-ins First, JSON Later

Initial content should be hardcoded but centralized.

Future content can be data-driven after the rules stabilize.

Good:

```text
BuiltinBeeSpecies
BuiltinBeeTraits
BuiltinBeeMutations
BuiltinBeeProducts
```

Bad:

```text
if (species == "meadow") scattered across many services
```

## 3. Initial Species

## 3.1 Meadow Bee

Role:

```text
Starter wild species.
Common plains/flower field bee.
Stable generalist.
```

Spawn context:

```text
Plains
Flower Forest
Meadow-like biomes
Fallback starter biome
```

Suggested default traits:

```text
Species: Meadow / Meadow
Lifespan: Normal / Normal
Productivity: Normal / Normal
Fertility: Two / Two
FlowerType: Flowers / Flowers
```

Dominance:

```text
Dominant
```

Production concept:

```text
Honeycomb
Basic Wax
Meadow Comb, if unique combs are used
```

Mutation role:

```text
Meadow + Forest -> Cultivated
```

## 3.2 Forest Bee

Role:

```text
Starter wild species.
Forest progression bee.
Useful for early mutation paths.
```

Spawn context:

```text
Forest
Birch Forest
Dark Forest
Taiga-like forest variants, if desired later
```

Suggested default traits:

```text
Species: Forest / Forest
Lifespan: Normal / Normal
Productivity: Normal / Normal
Fertility: Two / Two
FlowerType: Leaves / Leaves
```

Dominance:

```text
Dominant
```

Production concept:

```text
Honeycomb
Resin-like forest byproduct
Forest Comb, if unique combs are used
```

Mutation role:

```text
Meadow + Forest -> Cultivated
Forest + Arid -> Hardy
```

## 3.3 Arid Bee

Role:

```text
Starter wild species for hot/dry environments.
Introduces non-flower environmental identity.
```

Spawn context:

```text
Desert
Savanna
Badlands
```

Suggested default traits:

```text
Species: Arid / Arid
Lifespan: Normal / Normal
Productivity: Slow / Normal
Fertility: One / Two
FlowerType: Cactus / Cactus
```

Dominance:

```text
Recessive
```

Production concept:

```text
Dry Comb
Wax
Possible cactus-related byproduct later
```

Mutation role:

```text
Forest + Arid -> Hardy
```

## 3.4 Cultivated Bee

Role:

```text
First successful mutation.
Early proof that breeding can create new species.
Bridge between wild bees and managed beekeeping.
```

Source:

```text
Meadow + Forest -> Cultivated
```

Suggested mutation chance:

```text
12%
```

Suggested default traits:

```text
Species: Cultivated
Lifespan: Normal
Productivity: Fast
Fertility: Two
FlowerType: Flowers
```

Dominance:

```text
Dominant
```

Production concept:

```text
Cultivated Comb
More efficient honey/wax output
```

Progression role:

```text
Used later as parent for improved bees.
```

## 3.5 Hardy Bee

Role:

```text
Early environmental mutation.
Represents resilience and difficult climates.
```

Source:

```text
Forest + Arid -> Hardy
```

Suggested mutation chance:

```text
8%
```

Suggested default traits:

```text
Species: Hardy
Lifespan: Long
Productivity: Normal
Fertility: Two
FlowerType: Flowers or Cactus
```

Dominance:

```text
Recessive
```

Production concept:

```text
Hardy Comb
Durable wax / future frame-related material
```

Progression role:

```text
Foundation for climate-tolerant and harsh-environment bees.
```

## 4. Initial Trait Values

## 4.1 Lifespan

Values:

```text
Short
Normal
Long
```

Initial gameplay:

```text
Displayed only or lightly used.
```

Future gameplay:

```text
Affects tech apiary cycles or productive lifetime.
```

## 4.2 Productivity

Values:

```text
Slow
Normal
Fast
```

Initial gameplay:

```text
Affects production chance/rate once production exists.
```

Suggested multipliers:

```text
Slow: 0.75x
Normal: 1.00x
Fast: 1.25x
```

These numbers are placeholders.

## 4.3 Fertility

Values:

```text
One
Two
Three
```

Initial gameplay:

```text
Displayed only or lightly used.
```

Future gameplay:

```text
Affects extra larvae/samples in tech apiaries.
```

## 4.4 Flower Type

Values:

```text
Flowers
Cactus
Leaves
```

Initial gameplay:

```text
Displayed and used for flavor.
```

Future gameplay:

```text
Determines valid breeding/production environment.
```

## 5. Initial Mutation Tree

MVP tree:

```text
Meadow ─┐
        ├── Cultivated
Forest ─┘

Forest ─┐
        ├── Hardy
Arid ───┘
```

Future early expansion:

```text
Cultivated + Hardy -> Noble or Resilient
Cultivated + Meadow -> Diligent
Hardy + Arid -> Desert-adapted species
Forest + Cultivated -> Arboreal/Managed Forest species
```

Do not implement future expansion before the first loop works.

## 6. Production Design

Detailed execution spec:

```text
docs/implementation/06-production-mvp.md
```

## 6.1 Production Rule

Recommended base rule:

```text
Active species controls primary production.
Inactive species can contribute secondary production.
```

Example:

```text
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

```text
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

Detailed execution spec:

```text
docs/implementation/05-analyzer-implementation.md
```

Initial analyzer report should show:

```text
Species: Cultivated / Forest
Purity: Hybrid
Lifespan: Normal / Long
Productivity: Fast / Normal
Fertility: Two / Three
Flower Type: Flowers / Leaves
```

Dominance display can use symbols before colors exist:

```text
[D] Dominant
[R] Recessive
[A] Active
[I] Inactive
```

Example:

```text
Species:
[A][D] Cultivated
[I][D] Forest
```

## 8. Asset Strategy

Detailed planning:

```text
docs/art/
```

Rules:

- no polished assets are required for genetics core;
- placeholder textures are acceptable for analyzer and comb items;
- Blockbench becomes useful for custom blocks/machines in the tech phase;
- Blockbench/MCP automation is future optional tooling, not a MVP dependency.

## 9. Content Loading Strategy

Initial implementation:

```text
Hardcoded content definitions in a centralized built-in content registry.
```

Future implementation:

```text
JSON/datapack-driven content
```

Potential future paths:

```text
data/curious_bees/species/meadow.json
data/curious_bees/mutations/cultivated_from_meadow_forest.json
data/curious_bees/products/cultivated_comb.json
data/curious_bees/traits/productivity/fast.json
```

## 10. Balance Defaults

These are initial placeholders:

```text
Meadow + Forest -> Cultivated: 12%
Forest + Arid -> Hardy: 8%
Partial mutation: 95%
Full mutation: 5%
```

Trait multipliers:

```text
Slow productivity: 0.75x
Normal productivity: 1.00x
Fast productivity: 1.25x
```

Do not over-balance before gameplay exists.

## 11. Naming Guidelines

Species names should be:

- easy to understand;
- thematic;
- not too close to Productive Bees if avoidable;
- not necessarily identical to Forestry names.

MVP names can stay simple:

```text
Meadow
Forest
Arid
Cultivated
Hardy
```

Future resource species can be decided later.

## 12. Future Content Categories

Expanded content is planned in:

```text
docs/implementation/09-expanded-content-roadmap.md
```

Potential categories:

```text
Biome Bees
Nether Bees
End Bees
Resource Bees
Industrial Bees
Magic Bees
Compatibility Bees
```

Do not implement these during the MVP unless explicitly requested.

## 13. Acceptance Criteria

Initial content design is complete when:

```text
- Five initial species are defined.
- Two initial mutations are defined.
- MVP trait values are defined.
- Wild spawn contexts are defined.
- Basic product concepts are defined.
- Content is centralized.
- Future JSON conversion remains possible.
```
