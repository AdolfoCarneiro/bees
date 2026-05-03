# 10.5 — Species Hives And Habitat System

> Status: Post-MVP foundation document.
>
> This document defines how Curious Bees species exist in the world — both as data
> (habitat metadata) and as physical blocks (species hives). It also sets the architectural
> direction for migrating from vanilla-bees-with-attached-data toward custom bee entities,
> while remaining backwards-compatible with vanilla bees.
>
> Required reading before Phase 11 (Productization Roadmap) and any future species expansion.

## 1. Purpose

Curious Bees currently represents species as a genome attached to vanilla `Bee` entities. This is
clean for the MVP, but two gaps appear when planning real productization:

1. There is no way for a species to have a **natural home** in the world — no equivalent of the
   vanilla Bee Nest that distinguishes "Meadow Bee territory" from "Arid Bee territory."
2. There is no formal extensibility model that says: a new species is described by
   `bee X + hive X (optional) + biomes X, Y, Z + obtained via spawn or mutation`.

This document closes both gaps. It also acknowledges a related architectural decision —
whether to migrate from vanilla bees to custom bee entities — and reserves the design space
for that decision without forcing it now.

## 2. Scope

### 2.1 In scope (immediate implementation)

```text
- New Java model: SpeciesHabitatDefinition (common, pure Java).
- New Java service: HiveCompatibilityService (common, pure Java).
- Optional habitat field on BeeSpeciesDefinition.
- Three new species hive blocks in NeoForge: Meadow, Forest, Arid.
- World gen entries placing species hives in their respective biomes.
- Hive entry/exit restricted to matching species.
- Bees emerging from a species hive receive that species' genome.
- Asset pipeline additions for hive textures (prompts, UV template).
- Updated species spec format documentation.
```

### 2.2 In scope (design only, no implementation here)

```text
- Direction for migrating to custom bee entities (deferred ADR).
- Cross-breeding rules between vanilla bees and future custom entities.
- "Wild" default species concept for vanilla bees.
```

### 2.3 Out of scope

```text
- Cultivated and Hardy hives (mutation-only species — no natural hive).
- Resource bee hives.
- Lifecycle, age, or death mechanics tied to hives.
- Climate or environment effects on hive behavior.
- Fabric implementation (mirrors NeoForge structure later).
- Full custom entity implementation (deferred to its own future phase).
```

## 3. Core Concepts

### 3.1 Habitat = World Presence

A species has a **habitat** when it spawns naturally in the world. Habitat groups three things:

```text
- the hive block that represents this species' natural home;
- the texture path of that hive block;
- the list of vanilla biomes where the hive (and the species) appears.
```

Presence or absence of habitat defines how the species enters the game:

```text
habitat present  -> species spawns in the wild via its hive
habitat absent   -> species is mutation-only, obtained via Genetic Apiary
```

### 3.2 Species Hive vs Genetic Apiary

These are intentionally different blocks with different roles:

```text
Species Hive (e.g. Meadow Hive):
- natural, world-generated block
- vanilla beehive mechanics (entry/exit, honey, smoker)
- one block per world-spawnable species
- restricts entry to its own species
- wild equivalent of vanilla Bee Nest

Genetic Apiary:
- crafted, artificial block
- custom GUI (Phase 14)
- accepts any species
- platform for breeding, mutation, controlled production
- managed equivalent of vanilla Beehive
```

Vanilla `minecraft:bee_nest` and `minecraft:beehive` continue to exist and function normally —
they hold the implicit "wild" default species (see 3.4).

### 3.3 Hive-Species Compatibility Rule

```text
A bee may only enter or exit a hive of its own species.
```

Examples:

```text
Meadow Bee  -> may use Meadow Hive only (not Forest Hive, not Arid Hive)
Forest Bee  -> may use Forest Hive only
Arid Bee    -> may use Arid Hive only
Wild vanilla bee -> may use vanilla Bee Nest / Beehive only
Cultivated Bee -> no natural hive; uses Genetic Apiary only
Hardy Bee      -> no natural hive; uses Genetic Apiary only
```

The Genetic Apiary is exempt from this rule — it accepts any species by design.

### 3.4 Vanilla Bee = "Wild" Default Species

Vanilla `minecraft:bee` entities continue to carry genomes (existing behavior). Conceptually
they represent a default "wild" species that can cross-breed with any Curious Bees species.
This concept becomes formal when custom entities arrive (see section 7), but the data model
already supports it today through the genome attached to every vanilla bee.

## 4. Architecture Decisions

### 4.1 Common holds the truth, platform layer wires it

```text
common  = SpeciesHabitatDefinition, HiveCompatibilityService, species data
neoforge = SpeciesHiveBlock implementations, registry, world gen, event handlers
fabric   = future mirror of neoforge using Fabric APIs; reuses everything in common
```

This is the same boundary already enforced by CLAUDE.md for the genetics core. Every piece of
truth (which species exist, which biomes they live in, which hive block represents them) lives
in `common` as plain Java with string IDs. Platform code only wires those strings to
registries, blocks, and events.

### 4.2 One hive block per species, not a generic "species hive" block

Each world-spawnable species gets its own dedicated block class:
`MeadowHiveBlock`, `ForestHiveBlock`, `AridHiveBlock`. This is verbose but keeps:

```text
- world gen targeting trivial (one block, one biome list);
- recipes and loot tables clean per-species;
- texture binding direct (one block, one texture);
- no NBT-driven rendering complexity.
```

Adding a new species hive in the future is "register one more block, add one more world gen
entry" — no shared block needs to be touched.

### 4.3 Hive blocks extend vanilla `BeehiveBlock`

The species hive blocks extend `net.minecraft.world.level.block.BeehiveBlock` and use the
vanilla `BeehiveBlockEntity`. Bee in/out, honey level, smoker pacification, shears interaction —
all vanilla. Curious Bees overrides only:

```text
- entry: a bee may only enter if its species matches the hive species
        (delegated to HiveCompatibilityService).
- exit:  bees released from the hive are stamped with the hive's species genome
        if they do not already have one (handles wild spawns into the hive).
```

### 4.4 Defer custom entity migration

A custom-entity-per-species architecture (Productive Bees-style) is a meaningful direction but
a major change. This document acknowledges the direction and reserves a placeholder for it
(see section 7). It is not implemented as part of the work this document scopes.

## 5. Data Model

### 5.1 New: SpeciesHabitatDefinition (common)

Pure Java, string IDs only:

```java
public final class SpeciesHabitatDefinition {
    private final String hiveBlockId;        // "curiousbees:meadow_hive"
    private final String hiveTextureId;      // "curiousbees:textures/block/meadow_hive.png"
    private final List<String> spawnBiomes;  // ["plains", "flower_forest", "meadow"]
    // immutable, validates non-blank ids and non-empty biomes
}
```

### 5.2 New: HiveCompatibilityService (common)

Pure Java policy:

```java
public final class HiveCompatibilityService {
    public boolean canEnter(String beeSpeciesId, String hiveSpeciesId);
    // trivial id match today; isolated so both NeoForge and future Fabric call the same logic
}
```

### 5.3 BeeSpeciesDefinition gains optional habitat

```java
public final class BeeSpeciesDefinition {
    // ...existing fields...
    public Optional<SpeciesHabitatDefinition> habitat();
}
```

### 5.4 Built-in species populated

```text
Meadow      -> habitat: meadow_hive,   biomes [plains, flower_forest, meadow]
Forest      -> habitat: forest_hive,   biomes [forest, birch_forest, dark_forest]
Arid        -> habitat: arid_hive,     biomes [desert, savanna, badlands]
Cultivated  -> no habitat (mutation-only)
Hardy       -> no habitat (mutation-only)
```

The existing `spawnContextNotes: List<String>` on `BeeSpeciesDefinition` becomes redundant
for habitat-bearing species. It may be deprecated or absorbed into the habitat in a follow-up.

### 5.5 Data-driven (JSON) species shape

```json
{
  "id": "curious_bees:species/x",
  "displayName": "X Bee",
  "speciesAllele": { "id": "curious_bees:species/x", "dominance": "DOMINANT" },
  "defaultTraits": {
    "lifespan":     ["normal", "normal"],
    "productivity": ["normal", "normal"],
    "fertility":    ["two", "two"],
    "flowerType":   ["flowers", "flowers"]
  },
  "visual": {
    "model":   "curiousbees:bee/default",
    "texture": "curiousbees:textures/entity/bee/x.png"
  },
  "habitat": {
    "hiveBlockId":   "curiousbees:x_hive",
    "hiveTextureId": "curiousbees:textures/block/x_hive.png",
    "spawnBiomes":   ["biome1", "biome2"]
  }
}
```

`habitat` is optional. Validation rejects partially populated habitat objects (must have all
three fields if present).

## 6. Hive Block Behavior

### 6.1 Implementation outline

```text
SpeciesHiveBlock (abstract or interface) extends BeehiveBlock:
- knows its associated species id (compile-time per subclass)
- override entity-tries-to-enter: HiveCompatibilityService.canEnter(...)
- override bee-released-from-hive: stamp species genome via existing BeeGenomeStorage

MeadowHiveBlock, ForestHiveBlock, AridHiveBlock:
- thin subclasses, each tied to its species id and texture
- registered via existing ModBlocks.register(...)
```

### 6.2 World gen

NeoForge-specific. Each species hive registers a feature placement in its `spawnBiomes`,
similar to how vanilla `bee_nest` is placed in plains/forest. Density and placement rules
should match vanilla bee nest scarcity to avoid trivializing discovery.

### 6.3 Player interaction

Identical to vanilla `BeehiveBlock`:

```text
- right-click with empty bottle -> honey bottle (if FULL)
- right-click with shears        -> honeycomb (if FULL)
- smoker nearby                  -> bees do not become aggressive when harvested
- silk-touch break               -> drops the hive with bees inside
- regular break                  -> drops the hive empty, bees become aggressive
```

No new player interaction is added. The species hive is mechanically identical to a vanilla
bee nest, only with restricted occupants and a different texture.

## 7. Future Direction: Custom Bee Entity Architecture

### 7.1 Why this section exists here

The user-facing question "should each species be its own entity, like Productive Bees?" is
related to the hive system but is a larger, separate decision. This section reserves the
design space and defines the constraints any future custom-entity ADR must satisfy.

### 7.2 Constraints for the future ADR

Any future migration to custom bee entities must satisfy:

```text
- Every Curious Bees species becomes its own registered entity type with a spawn egg.
- Vanilla minecraft:bee entities continue to exist in worlds with this mod installed.
- Vanilla bees carry a "wild" species genome (existing genome attachment behavior).
- Custom species entities may breed with vanilla bees; offspring genome follows existing rules.
- The genetics core in common is not aware of entity classes — it works with genomes only.
- Save-load compatibility: existing worlds with vanilla bees must continue to work.
- The hive system designed in this document remains valid: each species hive accepts its
  matching custom entity (and the wild vanilla bee continues to use vanilla bee nests).
```

### 7.3 Status

Not designed in detail here. A dedicated future document at
`docs/post-mvp/11-6-custom-bee-entity-architecture.md` (matching Phase 11.6 in the
Productization Roadmap) will treat this when the project is ready to implement it. Until then,
vanilla bees with attached genomes remain the implementation.

## 8. Asset Pipeline Additions

### 8.1 New prompt directory

```text
docs/art/prompts/hives/
  ├── textures-block-meadow-hive.md
  ├── textures-block-forest-hive.md
  └── textures-block-arid-hive.md
```

Each prompt follows the template in `docs/art/asset-prompt-workflow.md` with one important
difference: the asset type is **block texture**, not entity texture. Block textures use a
different UV layout from entity textures.

### 8.2 New UV template for hive blocks

```text
docs/art/templates/hive/default_hive_uv_template.png
```

Required before any hive texture is generated, mirroring the bee entity UV template
requirement already established. The template must show all six block faces with their
boundaries and labels.

### 8.3 Asset deliverables per world-spawnable species

```text
neoforge/.../textures/entity/bee/<species>.png   (already required by Phase 12)
neoforge/.../textures/block/<species>_hive.png   (new)
```

## 9. Extensible Species Spec Format

### 9.1 The "spec" of a species

Adding a new species — now or in the future — means producing exactly this set of artifacts:

```text
1. JSON definition (or built-in Java entry) with:
   - genetic data (id, allele, dominance, default traits)
   - visual block with bee model + texture
   - optional habitat block with hive block id + hive texture + spawn biomes

2. Bee entity texture asset.
3. Bee entity texture prompt (under docs/art/prompts/species/).

4. If habitat present:
   a. Hive block subclass + registry entry.
   b. World gen feature targeting spawnBiomes.
   c. Hive block texture asset.
   d. Hive block texture prompt (under docs/art/prompts/hives/).

5. If habitat absent:
   a. At least one mutation entry must produce this species
      (otherwise the species is unreachable).

6. Localization keys (display name, hive block name when applicable).
7. In-game smoke test: visible bee, visible hive (if applicable), reachable via
   spawn or mutation.
```

### 9.2 Completeness rules

A species is considered complete only when all applicable items above are present and
validated. The existing asset prompt manifest (`docs/art/asset-manifest.md`, planned in Phase
15) should track these per species.

### 9.3 Adding species in batches or one-by-one

The format supports both workflows. A future species expansion phase may define a whole
branch (multiple species + mutations + hives) at once, but the per-species checklist remains
the same. Nothing in the format changes when adding species incrementally.

## 10. Roadmap Integration

This document precedes the Productization Roadmap (`11-post-mvp-productization-roadmap.md`)
and modifies its dependencies as follows:

```text
Phase 12 (Visual Species System) — depends on this document.
  Visual definitions for habitat-bearing species must align with hive textures.

Phase 14 (Genetic Apiary GUI) — coexists with this document.
  The apiary remains the artificial alternative to species hives. No conflict.

Phase 16 (Content And Asset Pipeline) — extends this document.
  Documents the species spec format defined here. Adds hive prompts and UV template
  to the asset pipeline.

Phase 17 (First Expanded Species Branch) — depends on this document.
  Any new species follows the format and rules defined here.

New future phase (Phase 11.6 in roadmap order):
  11.6 — Custom Bee Entity Architecture.
  Treats the migration described in section 7. Authored when the project is ready
  to implement it; until then, vanilla bees with attached genomes remain.
```

The Productization Roadmap is already updated to:

```text
- reference this document as foundational (Section 1 and Section 9 of the roadmap);
- include Phase 11.5 (this document) and Phase 11.6 (custom entity architecture) in the phase list;
- include hive block work before Phase 12 in the Recommended Implementation Order.
```

## 11. AI Agent Guidance

When working on tasks derived from this document, agents must:

```text
- keep SpeciesHabitatDefinition and HiveCompatibilityService in common as pure Java;
- not import Minecraft, NeoForge, or Fabric classes into the habitat data model;
- not introduce a generic "species hive" block — one block per species is the chosen design;
- not implement custom bee entities as part of this work (deferred to Phase 11.5);
- not add Cultivated or Hardy hives — those species are mutation-only by design;
- not commit final hive textures from text-only prompts — UV template is required;
- not assume the existing spawnContextNotes is authoritative — habitat.spawnBiomes is.
```

When unsure whether to extend a hive block with new behavior, the default answer is "no" —
hive blocks are intentionally near-vanilla.

## 12. Success Criteria

This phase is successful when:

```text
- A player can find Meadow, Forest, and Arid Hives in their respective biomes.
- Bees emerging from those hives carry the matching species genome.
- A bee of one species cannot enter a hive of another species (or vanilla bee nest).
- Vanilla bee nests continue to function normally with wild bees.
- The species spec format documented here is referenced by Phase 16.
- The custom entity migration direction is preserved as a future ADR placeholder.
- Adding a sixth species (now or later) requires only the artifacts listed in section 9.1.
```

## 13. Open Questions

```text
- Should world gen frequency for species hives match vanilla bee nest scarcity exactly,
  or be slightly higher to support discovery? (decide during implementation)
- Should the Genetic Apiary visually highlight which species can/cannot enter,
  or is the apiary's "accepts any species" rule purely backend? (Phase 14 concern)
- Should spawn eggs for the three world-spawnable species be added now alongside hives,
  or deferred until custom entities arrive? (recommend: defer; vanilla bee spawn egg
  + biome spawn covers the immediate need)
```
