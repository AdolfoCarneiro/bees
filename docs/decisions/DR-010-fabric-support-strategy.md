# DR-010 - Fabric Support Strategy

## Status

Proposed

## Date

2026-04-29

## Context

Phase 10 adds Fabric support after the NeoForge MVP and planning phases.

Fabric support must be a platform port, not a gameplay fork. The Fabric adapter should reuse:

- common genetics models;
- common breeding and mutation services;
- common content definitions and registry;
- common analyzer formatting;
- common production resolver.

The current repository already has:

```text
common/
neoforge/
fabric/
```

The `fabric` module currently exists as a placeholder, so the first implementation work should confirm build setup before gameplay integration.

## Target Versions

Recommended target:

```text
Minecraft: 1.21.1
Fabric Loader: 0.16.14 initially, with a compatibility check before implementation
Fabric API: 0.116.11+1.21.1 initially, with a compatibility check before implementation
Java: 21
```

Rationale:

- NeoForge target is already Minecraft 1.21.1.
- Keeping Fabric on Minecraft 1.21.1 preserves cross-loader behavior parity.
- Fabric Maven currently lists Fabric API `0.116.11+1.21.1` for Minecraft 1.21.1.
- Fabric Loader has newer versions, but `0.16.14` is a conservative known 1.21.1-era loader baseline. Recheck before coding because loader compatibility can move independently from Fabric API.

## Build Layout Recommendation

Use the existing module shape:

```text
common/
neoforge/
fabric/
```

Recommended strategy:

```text
Strategy A - true multi-loader workspace
```

Do not introduce Architectury or another abstraction layer during the first Fabric slice.

Rationale:

- common code is already separate;
- NeoForge code is isolated;
- Fabric can depend on `common`;
- extra abstraction would add complexity before proving Fabric viability.

## Entity Genome Storage Strategy

Use Fabric API Data Attachments for Bee entity genome storage.

Recommended approach:

```text
AttachmentType<GenomeData>
```

The Fabric storage adapter should:

- store serialized `GenomeData`, not raw `Genome`;
- use common `GenomeSerializer`;
- use the current runtime content registry for allele lookup;
- expose a Fabric equivalent of `BeeGenomeStorage`;
- handle missing or invalid data safely with warning logs.

Persistence:

```text
Use a persistent Data Attachment with a Codec.
```

Reasoning:

- Fabric documentation says Data Attachments can attach data to entities and persist with a Codec.
- This maps closely to NeoForge entity attachments.
- It keeps serialization shared through common code.

Open question:

```text
Confirm the exact Codec implementation for GenomeData after Fabric module dependencies compile.
```

## Item Data Strategy

Use vanilla/Fabric Data Components for future ItemStack genome data if captured bees or genome-bearing items are added.

This is not required for the first Fabric storage slice.

## Wild Bee Initialization Strategy

Use a Fabric entity-load or spawn-style hook only after genome storage is implemented.

The Fabric implementation should:

- detect vanilla Bee entities server-side;
- skip bees that already have a genome;
- map biome context to the same Meadow/Forest/Arid categories as NeoForge;
- call `WildBeeSpawnService.createWildGenome`;
- store the result through Fabric genome storage.

Open question:

```text
Choose the exact Fabric event/mixin point during Task 10E.
```

## Breeding Hook Strategy

Fabric does not appear to provide a direct equivalent to NeoForge `BabyEntitySpawnEvent` in the core docs reviewed for this spike.

Recommended strategy:

```text
Use a narrow mixin at the vanilla animal/bee breeding method where both parents and the baby are available.
```

Target concept:

```text
Animal breeding method that receives:
- ServerLevel / ServerWorld
- other parent
- baby entity
```

The mixin should:

- be as narrow as possible;
- only run for Bee parent/child combinations;
- read both parent genomes;
- call common `BeeBreedingOrchestrator`;
- write the child genome through Fabric storage;
- log and skip safely if parent genomes cannot be resolved.

Do not use nearest-parent inference unless a later decision documents why it is safe.

Open question:

```text
Confirm the exact Yarn-named method and injection point after Fabric/Yarn dependencies are available locally.
```

## Analyzer Strategy

Fabric analyzer support should reuse common analyzer services.

Fabric-specific code should only:

- register the item;
- handle use-on-entity interaction;
- read genome through Fabric storage;
- send formatted output to the player.

No Fabric-only analyzer report model should be created.

## Production Strategy

Fabric production should reuse:

- `ProductionResolver`;
- `ContentRegistry`;
- the same content definitions and output IDs.

If the Genetic Apiary is ported later, treat it as a separate slice after base Fabric MVP behavior.

## Known Risks

### Risk: Fabric Breeding Hook Requires Mixin Details

Mitigation:

```text
Do a small mixin spike before implementing full breeding behavior.
Document exact target method and fallback.
```

### Risk: Data Attachment Codec Shape

Mitigation:

```text
Implement Fabric genome storage in one isolated task.
Use GenomeData as the attachment payload.
Write serialization tests or document manual validation.
```

### Risk: Build Setup Breaks NeoForge

Mitigation:

```text
Keep Fabric build changes isolated.
Run :common:test and :neoforge:build after build layout changes.
```

### Risk: Fabric Becomes A Gameplay Fork

Mitigation:

```text
Fabric adapters must call common services.
No genetics logic belongs in Fabric mixins/events.
```

## Recommended Next Task

Proceed with:

```text
10B - Multi-loader Build Layout Decision
```

Current assessment:

- the repo already has `common`, `neoforge`, and `fabric` modules;
- the build decision should confirm this layout and document that no large refactor is needed;
- only after that should `10C - Fabric Module Setup` add Fabric Loom/build metadata.

## Verification Gate 1 - Fabric Feasibility Review

Current status:

```text
Ready for review.
```

Review questions:

- Is Fabric support still worth doing now?
- Is Minecraft 1.21.1 still the target?
- Is Fabric Data Attachment acceptable for Bee genome storage?
- Is a mixin-based breeding hook acceptable if no direct event exists?
- Should the existing `common/neoforge/fabric` module layout be kept?

## Sources Checked

- Fabric Maven Fabric API index: `https://maven.fabricmc.net/net/fabricmc/fabric-api/fabric-api/`
- Fabric Maven Fabric Loader index: `https://maven.fabricmc.net/net/fabricmc/fabric-loader/`
- Fabric Data Attachments docs: `https://docs.fabricmc.net/develop/data-attachments`
- Fabric Custom Data Components docs: `https://docs.fabricmc.net/develop/items/custom-data-components`
- Fabric Events docs: `https://docs.fabricmc.net/develop/events`
- Fabric 1.21 / 1.21.1 notes: `https://fabricmc.net/2024/05/31/121.html`

## Related Documents

- `docs/decisions/0006-fabric-after-neoforge-mvp.md`
- `docs/implementation/10-fabric-support-implementation.md`
- `docs/quality/10-fabric-support-test-plan.md`
- `docs/implementation/03-neoforge-entity-integration.md`
- `docs/implementation/04-vanilla-breeding-integration.md`
