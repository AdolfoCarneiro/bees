# 08K - Phase 8 Exit Review

## Status

Complete.

## Scope of This Review

This document records the current Phase 8 state against:

- `docs/implementation/08-data-driven-content.md`
- `docs/decisions/0010-data-driven-content-strategy.md`
- `docs/quality/09-data-driven-content-test-plan.md`

It is the Phase 8 closure checkpoint.

## Implemented vs. Planned

### 8A - Data-Driven Content Design Decision

Status: done.

Notes:

- ADR-0010 documents the data-driven content strategy.
- Built-ins remain protected fallback content.
- Loaded content cannot override built-ins in the MVP policy.

### 8B - Serializable Content DTOs

Status: done.

Notes:

- DTOs exist for trait alleles, species, mutations, production definitions, production outputs, mutation result modes, and trait allele pairs.
- DTOs remain in `common` and have no Minecraft, NeoForge, or Fabric dependencies.

### 8C - Content Validation

Status: done.

Notes:

- `ContentValidator` validates structural and referential rules.
- Tests cover valid definitions, invalid fields, duplicate IDs, unknown references, and invalid chances.
- Validation errors are accumulated and explicit.

### 8D - Data-to-Runtime Conversion

Status: done.

Notes:

- `ContentConverter` converts validated DTOs into `Allele`, `BeeSpeciesDefinition`, `MutationDefinition`, and `ProductionDefinition`.
- Conversion remains in `common`.

### 8E - Built-In Content Mirroring

Status: done.

Notes:

- `BuiltinContentData` represents MVP built-ins as DTOs.
- Tests prove built-in DTOs validate and convert through the same pipeline.
- Existing runtime built-ins remain available as fallback.

### 8F - JSON Parsing

Status: done.

Notes:

- `ContentDataJsonParser` parses MVP JSON formats into DTOs.
- Tests cover valid trait, species, mutation, and production JSON, plus invalid JSON and validation failures.

### 8G - Runtime Content Registry Merge

Status: done.

Notes:

- `ContentRegistry` exposes built-ins plus loaded definitions through a single runtime facade.
- Duplicate loaded IDs and built-in override attempts are rejected.
- Production lookups, mutation lists, trait lookups, species lookups, and allele lookups are available through the registry.

### 8H - NeoForge Resource Loading

Status: done.

Notes:

- NeoForge registers `ContentReloadListener` through `AddReloadListenerEvent`.
- The reload listener discovers JSON under:

```text
curious_bees/traits
curious_bees/species
curious_bees/mutations
curious_bees/production
```

- Common loading logic is handled by `ContentJsonLoader`.
- `NeoForgeContentRegistry` stores the current runtime registry.
- Breeding, production rolls, genome deserialization, and debug set-genome behavior now read from the current registry where appropriate.
- Built-ins remain available when no external content is loaded or when invalid files are rejected.

### 8I - Example Data Files

Status: done.

Notes:

- MVP example JSON files exist under:

```text
common/src/test/resources/content-examples/curious_bees/
```

- Examples cover all five MVP species, all MVP trait alleles, both MVP mutations, and all MVP production definitions.
- `ContentExampleFilesTest` validates the examples and checks that they mirror built-in IDs.
- They are test examples, not active bundled overrides, because built-in IDs are protected by the current override policy.

### 8J - Content Authoring Guide

Status: done.

Notes:

- `docs/content-authoring-guide.md` explains the implemented JSON formats, references, validation errors, override policy, examples, and unsupported future fields.
- The guide avoids promising unimplemented systems.

## Verification Gates

### Gate 1 - Data-Driven Strategy Review

Passed.

ADR-0010 defines content types, migration order, fallback strategy, validation, error reporting, datapack interaction, and Fabric considerations.

### Gate 2 - Validation Review

Passed.

Validation catches required fields, invalid enum-like values, invalid chances, unknown references, and duplicates. Errors are explicit.

### Gate 3 - Built-In Migration Review

Passed.

Built-ins are mirrored through DTOs and still work as runtime fallback definitions.

### Gate 4 - Registry Merge Review

Passed.

Built-ins and loaded definitions can coexist. Built-in override attempts are rejected.

### Gate 5 - Data Loading Integration Review

Passed for automated validation.

NeoForge resource loading compiles and delegates parsing, validation, conversion, and merging to common code.

Manual in-game datapack validation has not been run in this review.

### Gate 6 - Content Authoring Review

Passed.

The authoring guide includes implemented fields, examples, validation errors, testing guidance, and future-only warnings.

### Gate 7 - Phase 8 Exit Review

Passed with the residual risks listed below.

## Automated Validation

Commands run:

```text
.\gradlew.bat :common:test
.\gradlew.bat :neoforge:build
```

Result:

```text
Both passed.
```

## Phase 8 Exit Checklist

- Built-in content still works: yes.
- External content data can represent MVP definitions: yes.
- Invalid content is rejected clearly: yes.
- Loaded definitions can be merged safely: yes.
- Gameplay services do not care whether mutations/production came from built-in or loaded content: yes for breeding, production, and genome deserialization.
- JSON loading does not force Fabric work early: yes.
- No resource bee expansion was added: yes.
- No polished asset requirement was introduced: yes.
- Content authoring docs exist: yes.

## Risks and Open Questions

- Manual in-game datapack reload validation still needs to be performed before a public release.
- The mutation DTO supports partial/full chances, but the current runtime `MutationDefinition` supports one result mode. The converter selects `PARTIAL` when `partialChance >= fullChance`, otherwise `FULL`.
- Built-in JSON examples are stored as test resources instead of active default datapack resources to avoid conflicting with the no-override policy.
- Spawn selection still uses the existing built-in wild species categories; data-driven spawn rules remain future work.

## Phase 8 Conclusion

Phase 8 is complete.

The project now has a data-driven content pipeline that can parse, validate, convert, merge, and load external content while preserving the MVP built-in fallback and future Fabric compatibility.

## Optional Follow-Up Tasks

1. Run manual in-game validation with a small external datapack using non-built-in IDs.
2. Add a debug command to inspect the current content registry counts in-game.
3. Extend mutation runtime models to represent weighted partial/full result modes directly.
4. Design data-driven spawn context rules in a future phase if expanded content needs it.
