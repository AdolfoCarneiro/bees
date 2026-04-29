# ADR-0010 — Data-Driven Content Strategy for Phase 8

## Status

Accepted

## Date

2026-04-29

## Context

Phases 1–7 established a stable genetics core, built-in content, NeoForge integration, vanilla
breeding, analyzer, production, and tech apiary.

All content is currently defined in centralized Java classes:

```text
BuiltinBeeTraits
BuiltinBeeSpecies
BuiltinBeeMutations
BuiltinProductionDefinitions
```

Adding a new species currently requires recompiling the mod.

Phase 8 makes content definitions loadable from external data files, enabling:

- modpack authors to add new species/mutations without a code change;
- future datapack-driven expansion in Phase 9;
- a clear separation between content schema (data) and gameplay logic (code).

The genetics rules and the content model are now stable enough for this migration.

## Decision

### Which content types become data-driven first

All four content types are migrated in Phase 8, in this order:

```text
1. Trait allele definitions
2. Species definitions (reference trait allele IDs)
3. Mutation definitions (reference species IDs)
4. Production definitions (reference species IDs)
```

This order mirrors the dependency graph: mutations and production reference species, which
reference traits.

### Built-in fallback strategy

**Option B is chosen** — built-in Java definitions are rewritten as data objects and converted
into runtime domain definitions through the same pipeline as external data.

Rationale: rules are stable; a single pipeline is safer than two parallel ones.

The five MVP species remain bundled with the mod. They are registered first, before any external
data is loaded. External data cannot override built-ins unless explicitly permitted (see override
policy below).

### Folder layout

```text
data/curious_bees/
├── species/
│   ├── meadow.json
│   ├── forest.json
│   ├── arid.json
│   ├── cultivated.json
│   └── hardy.json
├── traits/
│   ├── lifespan/
│   │   ├── short.json
│   │   ├── normal.json
│   │   └── long.json
│   ├── productivity/
│   │   ├── slow.json
│   │   ├── normal.json
│   │   └── fast.json
│   ├── fertility/
│   │   ├── one.json
│   │   ├── two.json
│   │   └── three.json
│   └── flower_type/
│       ├── flowers.json
│       ├── cactus.json
│       └── leaves.json
├── mutations/
│   ├── cultivated_from_meadow_forest.json
│   └── hardy_from_forest_arid.json
└── production/
    ├── meadow.json
    ├── forest.json
    ├── arid.json
    ├── cultivated.json
    └── hardy.json
```

### Override policy

**Option A is chosen** — loaded definitions cannot override built-ins unless the built-in is
absent from the registry.

This protects the MVP gameplay loop from accidental breakage by external content packs.

A future ADR may relax this if datapack override is explicitly requested.

Duplicate IDs within the same content source (e.g., two JSON files with the same species ID) are
rejected with a clear error.

### Validation strategy

Validation runs in two stages:

1. **Structural validation** — checks that required fields are present and have valid types/ranges.
   Runs immediately after parsing, before cross-reference checks.

2. **Referential validation** — checks that IDs referenced by one content type exist in another
   (e.g., a species' defaultTraits must reference known trait allele IDs; a mutation's parents
   must reference known species IDs).
   Runs after all content of a loading pass is collected.

Validation is implemented in pure Java in the common module. No Minecraft or NeoForge classes.

### Error reporting strategy

- Validation errors are never silently swallowed.
- All errors in a loading pass are collected before reporting, so multiple problems surface at once.
- Each error includes the definition ID (and file path when available).
- A single invalid file rejects only that file's definitions; it does not abort the entire load.
- Built-in content is always present regardless of external file errors.

### Common module package layout

```text
com.curiousbees.common.content.data/
    SpeciesDefinitionData.java
    TraitAlleleDefinitionData.java
    MutationDefinitionData.java
    ProductionDefinitionData.java
    ProductionOutputData.java
    MutationResultModesData.java

com.curiousbees.common.content.validation/
    ContentValidationResult.java
    ContentValidator.java

com.curiousbees.common.content.conversion/
    ContentConverter.java

com.curiousbees.common.content.registry/
    ContentRegistry.java
```

### Interaction with Minecraft datapacks

The NeoForge platform layer subscribes to the `AddReloadListenerEvent` (or equivalent) and
triggers a content reload when datapacks reload. The common content pipeline (parse → validate →
convert → merge) is reused unchanged.

The platform layer is responsible for file discovery and stream provision. The common module is
responsible for what to do with the data.

### Future Fabric considerations

Because the common content pipeline is pure Java, a Fabric platform layer needs only to:

- discover files using Fabric's resource system;
- call the same common parse/validate/convert/merge pipeline.

No duplication of genetics or content logic is needed for Fabric support.

## Consequences

### Positive

- New species/mutations can be added without recompiling.
- Content validation catches errors early with actionable messages.
- Built-in and external definitions share the same validation and conversion pipeline.
- The common module remains Minecraft-free, preserving Fabric compatibility.
- The MVP gameplay loop is protected by built-in fallback.

### Negative

- The built-in content migration (from raw Java constants to data objects) is a refactor step
  that must not break existing tests.
- Two-stage validation (structural + referential) adds pipeline complexity.
- Content authors need documentation to understand the schema and error messages.

### Constraints

- Do not allow common content DTOs or validators to import NeoForge or Minecraft classes.
- Do not remove built-in fallback content; external data extends, not replaces, built-ins.
- Do not add resource bee content during Phase 8; this phase is infrastructure only.
- Do not implement a GUI or in-game content editor in this phase.

## Applies To

- all content DTO classes;
- all content validator classes;
- all content converter classes;
- the runtime content registry;
- the NeoForge resource loading integration;
- Phase 8 example data files.

## Related Documents

- `docs/implementation/08-data-driven-content.md`
- `docs/decisions/0003-hardcoded-builtins-before-json.md`
- `docs/05-content-design-spec.md`
