# 09A - Expanded Content Categories

## Status

Complete.

## Scope

This document completes Task 9.1 from `docs/implementation/09-expanded-content-roadmap.md`.

It defines the ordered future content categories for Curious Bees after the MVP loop and Phase 8 data-driven infrastructure.

This is planning only.

## Non-Goals

Do not use this document to implement:

- new species definitions;
- new mutation JSON files;
- resource bees;
- compatibility bees;
- new items, blocks, GUIs, or assets;
- Fabric support.

The MVP remains unchanged:

```text
Meadow
Forest
Arid
Cultivated
Hardy
```

## Category Order

Expanded content should grow in this order:

```text
Tier 0 - Wild Starters
Tier 1 - Early Mutations
Tier 2 - Biome Adaptation Bees
Tier 3 - Managed / Tech Bees
Tier 4 - Harsh Environment Bees
Tier 5 - Resource Foundation Bees
Tier 6 - Resource Bees
Tier 7 - Industrial / Compatibility Bees
Tier 8 - Exotic / Endgame Bees
```

This order is intentionally conservative. It keeps the genetic loop, analyzer, production, and content loading infrastructure ahead of large content volume.

## Tier 0 - Wild Starters

Purpose:

```text
Provide natural genetic starting points found in the world.
```

Current MVP members:

```text
Meadow
Forest
Arid
```

Future examples:

```text
Swamp
Jungle
Tundra
Mountain
Coastal
Cave
```

Acceptance rules:

- must have a clear biome identity;
- must unlock at least one meaningful mutation path;
- must not duplicate an existing starter;
- must have trait tendencies that matter;
- must not require a new system just to exist.

## Tier 1 - Early Mutations

Purpose:

```text
Teach mutation, hybrid outcomes, analyzer usage, and purebred stabilization.
```

Current MVP members:

```text
Cultivated
Hardy
```

Future examples:

```text
Diligent
Noble
Resilient
Arboreal
Pastoral
Feral
```

Acceptance rules:

- mutation should be understandable from its parents;
- mutation chance should not be punishing;
- species should create useful hybrid and purebred outcomes;
- species should not require advanced machines.

## Tier 2 - Biome Adaptation Bees

Purpose:

```text
Make the world and environment matter in breeding progression.
```

Future examples:

```text
Mire
Tropical
Frost
Alpine
Briny
Cavern
Nocturnal
```

Implementation dependencies:

- environment context model or equivalent;
- mutation definitions that can express biome restrictions;
- analyzer or debug output that can explain requirements;
- content data fields for environment requirements.

Acceptance rules:

- conditions must be inspectable or inferable;
- restrictions must not become pure trial-and-error;
- rewards must justify the extra difficulty.

## Tier 3 - Managed / Tech Bees

Purpose:

```text
Bridge natural breeding and controlled beekeeping/automation.
```

Future examples:

```text
Managed
Diligent
Industrious
Ordered
Stable
Refined
```

Acceptance rules:

- should interact meaningfully with productivity, fertility, frames, or apiary behavior;
- should not invalidate wild species;
- should not be direct upgrades with no tradeoff;
- should reward players who stabilize lineages.

## Tier 4 - Harsh Environment Bees

Purpose:

```text
Introduce difficult climates, dimensions, weather, cave, Nether, and End-adjacent progression.
```

Future examples:

```text
Scorched
Ashen
Frosted
Glacial
Cavern
Umbral
Blazing
Soul
Basalt
Ender
Void
Chorus
```

Implementation dependencies:

- data/model-driven environment conditions;
- analyzer/debug output for requirements;
- production rewards that justify the difficulty.

Acceptance rules:

- must be more than a cosmetic variant;
- must not hide requirements from the player;
- must not trivialize earlier breeding tiers.

## Tier 5 - Resource Foundation Bees

Purpose:

```text
Prepare the player for resource bees without jumping directly to direct material generation.
```

Future examples:

```text
Mineral
Metallic
Crystalline
Conductive
Resonant
Geologic
Deep
```

Acceptance rules:

- should unlock future resource paths;
- should produce thematic intermediate byproducts, not major resources directly;
- should require prior engagement with genetics;
- should connect to production or processing systems.

## Tier 6 - Resource Bees

Purpose:

```text
Provide direct or semi-direct resource production after the genetic and tech progression is established.
```

This category is explicitly blocked until a separate readiness decision is complete.

Hard blockers:

- resource bee readiness ADR;
- economy and production balance constraints;
- content authoring format stable enough for expansion;
- at least one non-resource progression tier beyond MVP;
- analyzer/debug support for explaining requirements;
- tech or processing loop ready enough to constrain output.

Acceptance rules:

- must be earned;
- must be slower or more constrained than early direct mining;
- must not become the identity of the whole mod;
- must not use deterministic recipe thinking;
- must be configurable later.

## Tier 7 - Industrial / Compatibility Bees

Purpose:

```text
Integrate with optional mod ecosystems without making compat mandatory.
```

Possible targets:

```text
Create
Mekanism
Thermal
Farmer's Delight
Botania-like mods
Ars Nouveau-like mods
Applied Energistics-like mods
Immersive Engineering-like mods
```

Acceptance rules:

- base mod must work standalone;
- optional dependency strategy must be defined;
- missing mods must not crash Curious Bees;
- common genetics must not depend on external mod APIs;
- compat content must be disableable later.

## Tier 8 - Exotic / Endgame Bees

Purpose:

```text
Create aspirational late-game goals after earlier progression is fun and balanced.
```

Future examples:

```text
Void
Temporal
Astral
Ancient
Royal
Mythic
```

Acceptance rules:

- should not simply produce more items;
- should offer unusual effects, modifiers, or late-game production;
- should not trivialize earlier content;
- should keep mutation trees understandable.

## Category Review Gate

Gate 1 from Phase 9 is passed when:

- future content categories are ordered;
- each category has a purpose;
- MVP boundaries remain intact;
- resource bees are explicitly blocked behind readiness criteria;
- no implementation content was added.

Current assessment:

```text
Gate 1 passed.
```

## Follow-Up Tasks

1. Task 9.2 - Define Naming Strategy.
2. Task 9.3 - Draft First Post-MVP Mutation Branch.
3. Task 9.4 - Resource Bee Readiness Decision.
4. Task 9.5 - Asset Roadmap for Expanded Content.
