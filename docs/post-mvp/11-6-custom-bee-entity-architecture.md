# 11.6 — Custom Bee Entity Architecture (Deferred ADR)

> Status: Design only. Not yet implemented.
>
> This document reserves the design space for migrating Curious Bees from vanilla bees with
> attached genomes to custom entity types per species. It defines the constraints any future
> implementation must satisfy and the questions that must be answered before implementation
> begins. No code changes are scoped here.
>
> The immediate hive system (species hives, habitat, world gen) is implemented in Phase 11.5
> and remains valid regardless of which entity strategy is adopted.

## 1. Why This Decision Exists

Curious Bees currently represents all species as `minecraft:bee` entities carrying a genome
attachment. This is correct for the MVP and for Phase 11.5.

Two pressures push toward custom entities in the future:

```text
1. Visual differentiation: custom textures per species work today, but species-specific
   animations, model variants, and spawn eggs require custom entity types.

2. Mod ecosystem compatibility: other mods and tools that filter on entity type (e.g.
   mob farms, entity selectors, spawn egg creative tabs) cannot distinguish a Meadow Bee
   from a Forest Bee while both are minecraft:bee.
```

This ADR is written now to:

```text
- acknowledge that the migration is a natural future direction;
- define what constraints any migration must satisfy;
- prevent the migration from being attempted prematurely;
- avoid having the Phase 11.5 hive system or genetics core depend on vanilla bees forever.
```

## 2. Current State (Phase 11.5 and earlier)

```text
Entity:         minecraft:bee
Genome:         attached via NeoForge DataAttachment (BeeGenomeStorage)
Species ID:     read from active SPECIES allele in the genome
Visual:         CuriousBeeBeeRenderer overrides the texture for minecraft:bee entities
Nest entry:     SpeciesBeeNestBlockEntity.addOccupant enforces genome-based species check
Nest exit:      SpeciesBeeNestFeature stamps species genome on world-gen-placed bees
Breeding:       BeeBreedingEventHandler intercepts BabyEntitySpawnEvent
Spawn:          BeeSpawnEventHandler stamps wild genome on EntityJoinLevelEvent
```

This system works and is the foundation. Nothing in Phase 11.6 should break it or make it
incompatible with existing saves.

## 3. Future Direction: One Entity Per Species

The anticipated future migration:

```text
minecraft:bee               → remains; represents the "wild" default species
curiousbees:meadow_bee      → new custom entity, same AI as vanilla bee
curiousbees:forest_bee      → new custom entity
curiousbees:arid_bee        → new custom entity
curiousbees:cultivated_bee  → new custom entity
curiousbees:hardy_bee       → new custom entity
...
```

Each custom entity type:

```text
- extends Bee (or equivalent Minecraft bee base class);
- has its own registered EntityType and spawn egg;
- has its own registered entity renderer bound to the species texture;
- continues to carry a genome (genome attachment migrates with the entity);
- can breed with vanilla bees and other custom species;
- offspring entity type is determined by the dominant SPECIES allele in the child genome.
```

## 4. Constraints Any Future ADR Must Satisfy

Before implementation begins, a detailed ADR (`docs/post-mvp/11-6-custom-bee-entity-migration-adr.md`)
must address all of the following:

### 4.1 Vanilla bee backwards compatibility

```text
- Worlds with vanilla minecraft:bee entities must continue to load without error.
- Vanilla bees in an upgraded world must still carry genomes (no data loss).
- Vanilla bees should remain functional as the "wild" default species.
- The migration must not require users to clear bees from their worlds.
```

### 4.2 Genetics core independence

```text
- The genetics core (common/genetics) must not reference entity classes.
- Genome creation, inheritance, mutation, and serialization must be unaffected.
- BeeGenomeStorage may be adapted but must remain thin.
```

### 4.3 Hive system compatibility

```text
- SpeciesBeeNestBlock and SpeciesBeeNestBlockEntity must continue to work.
- Entry policy (BeeNestCompatibilityService.canEnter) must remain valid.
- Custom entity types must be recognized by hive AI as valid occupants.
- Vanilla bee nests must continue to be the home for vanilla minecraft:bee entities.
```

### 4.4 Breeding rules

```text
- Custom species entities must be able to breed with vanilla bees.
- Breeding offspring entity type = entity type matching the child's dominant SPECIES allele.
- BeeBreedingEventHandler must be adapted or replaced to handle cross-type breeding.
- Mutation rules from the genetics core must be unaffected.
```

### 4.5 Spawn rules

```text
- Wild spawning of custom entities in their native biomes must be supported.
- BeeSpawnEventHandler may be replaced by species-specific spawn conditions.
- Vanilla bees may still spawn naturally as the wild/unspecialized base species.
```

### 4.6 Renderer and visual

```text
- Each custom entity has its own renderer bound to its species texture.
- CuriousBeeBeeRenderer (which currently patches vanilla bee) may be retired or specialized.
- The shared-model-with-species-texture pattern may be replaced by per-entity renderer classes.
- Custom model support (optional Blockbench model per special species) should be feasible.
```

### 4.7 Save compatibility strategy

```text
- The ADR must describe how vanilla bees in existing saves are handled:
  Option A: leave them as vanilla bees forever (no migration, gradual natural attrition).
  Option B: convert vanilla bees with a species genome to their custom entity equivalent on load.
  Option C: provide a one-time migration command or loot table event.
- The chosen strategy must be explicit and justified.
```

## 5. Open Questions To Resolve Before Implementation

```text
Q1: Should offspring entity type be determined at birth (entity type = species allele),
    or should all offspring be vanilla bees that receive a genome like today?

Q2: What happens when a vanilla bee breeds with a custom Meadow Bee?
    Is the offspring a vanilla bee or a curiousbees:meadow_bee?

Q3: Can vanilla bees and custom species bees share the same hives,
    or does vanilla bee AI need to be updated?

Q4: Should save migration be automatic, manual, or optional?

Q5: How does the creative tab and spawn egg interact with mutation-only species
    (Cultivated, Hardy) that do not have a natural spawn?

Q6: Should the migration happen in a single phase or incrementally per species?
```

## 6. What Makes This Premature Now

The migration is deferred because:

```text
- The visual species system (Phase 12) is still being built. Adding custom entities before
  the renderer architecture is settled would create churn.

- The analyzer UX (Phase 13) and apiary GUI (Phase 14) do not need custom entities.
  They work with genomes, not entity types.

- The content/asset pipeline (Phase 16) should define naming, texture, and registry
  conventions before those conventions are baked into entity class names.

- Backwards compatibility and save migration require careful planning that should not
  be rushed during the productization phase.
```

The gains from custom entities (spawn eggs, entity type selectors, cleaner renderer binding)
do not justify the risk and scope during post-MVP productization.

## 7. Trigger Conditions For Resuming This Design

Resume this ADR when all of the following are true:

```text
- Phase 12 (visual species system) is fully implemented and the renderer pattern is stable.
- Phase 13 (analyzer UX) and Phase 14 (apiary GUI) are shipped.
- Phase 16 (content/asset pipeline) defines naming conventions for species assets.
- At least one content expansion (Phase 17 or later) has shipped using the
  data-driven species system, so real-world patterns are known.
- The team is ready to commit to a save migration strategy.
```

## 8. Status

```text
Phase 11.6 status: DEFERRED — design only, no implementation target date.

The detailed migration ADR will be authored at:
docs/post-mvp/11-6-custom-bee-entity-migration-adr.md

The hive system from Phase 11.5 is valid for both the current vanilla-bee approach
and the future custom-entity approach. No changes to hive blocks are needed before
this migration is designed.
```

## 9. Relationship To Other Phases

```text
Phase 11.5 — Species Hives And Habitat System
  Foundational. Valid for both vanilla-bee and custom-entity approaches.
  BeeNestCompatibilityService and SpeciesHabitatDefinition are stable.

Phase 12 — Visual Species System
  Must stabilize the renderer pattern before custom entities inherit from it.

Phase 16 — Content And Asset Pipeline
  Must define naming conventions before entity class names are decided.

Phase 17+ — Expanded Species Branches
  Adding new species with custom entities is easier if this migration is already done.
  Consider aligning Phase 17 with this migration if timing allows.

Future Phase 11.6 implementation
  Replaces CuriousBeeBeeRenderer with per-entity renderers.
  Adapts BeeBreedingEventHandler for cross-type breeding.
  Adds registered EntityTypes and spawn eggs.
  Handles save migration per the chosen strategy.
```
