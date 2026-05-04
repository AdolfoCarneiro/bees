# Decisions Log (ADRs)

Single log of accepted/proposed decisions for Curious Bees. Each entry preserves the locked outcome and the **reason** so future contributors don't reverse it by accident. Detailed rationale and alternatives that did not survive review live in **git history** under the previous `docs/decisions/` per-file ADRs.

> When you change a decision, **add a new entry** at the bottom of this file (do not silently rewrite an old one) with status `Superseded` on the previous one.

## Index

| ID | Title | Status |
|----|-------|--------|
| [ADR-0001](#adr-0001--use-neoforge-1211-as-the-initial-loader) | Use NeoForge 1.21.1 as the initial loader | Accepted |
| [ADR-0002](#adr-0002--keep-the-genetics-core-pure-java) | Keep the genetics core pure Java | Accepted |
| [ADR-0003](#adr-0003--start-with-hardcoded-built-in-content-before-jsondata-driven-loading) | Start with hardcoded built-in content before JSON | Accepted |
| [ADR-0004](#adr-0004--use-vanilla-bee-breeding-as-the-first-gameplay-loop) | Use vanilla bee breeding as the first gameplay loop | Accepted |
| [ADR-0005](#adr-0005--use-placeholder-assets-before-polished-assets) | Use placeholder assets before polished assets | Accepted |
| [ADR-0006](#adr-0006--add-fabric-support-after-the-neoforge-mvp) | Add Fabric support after the NeoForge MVP | Accepted |
| [ADR-0007](#adr-0007--keep-resource-bees-out-of-the-mvp) | Keep resource bees out of the MVP | Accepted |
| [ADR-0008](#adr-0008--use-detailed-local-specs-for-ai-implementation-guidance) | Use detailed local specs for AI implementation guidance | Accepted |
| [ADR-0009](#adr-0009--genetic-apiary-first-version-design) | Genetic Apiary first version design | Accepted |
| [ADR-0010](#adr-0010--data-driven-content-strategy) | Data-driven content strategy | Accepted |
| [ADR-0011](#adr-0011--expanded-content-naming-strategy) | Expanded content naming strategy | Accepted |
| [ADR-0012](#adr-0012--resource-bee-readiness) | Resource bee readiness | Accepted |
| [ADR-003](#adr-003--neoforge-bee-genome-storage) | NeoForge bee genome storage | Accepted |
| [DR-010](#dr-010--fabric-support-strategy) | Fabric support strategy | Proposed |

---

## ADR-0001 — Use NeoForge 1.21.1 as the initial loader

**Status:** Accepted · 2026-04-28

**Context.** The project needs a single initial loader to avoid splitting effort before the gameplay loop is proven.

**Decision.** First implementation target is **NeoForge 1.21.1**. Fabric is future work.

**Consequences.**

- (+) Focus on one loader; clearer technical target; simpler debugging.
- (−) Fabric users not supported in the first release; some NeoForge-specific decisions may need adapters later.
- **Constraints:** common genetics must not import NeoForge; integration stays in the platform layer; Fabric work is treated as future until the NeoForge MVP is stable.

---

## ADR-0002 — Keep the genetics core pure Java

**Status:** Accepted · 2026-04-28

**Context.** Genetics is the core feature; it must be testable outside Minecraft.

**Decision.** The genetics core is **pure Java**. It must not depend on Minecraft, NeoForge, Fabric, registries, events, NBT, components, attachments, mixins, items/blocks, or client rendering. Loader code calls the core; the core never references game types.

**Allowed in `common/genetics`:**

```text
Allele · Dominance · ChromosomeType · GenePair · Genome
BreedingService · BreedingResult
MutationDefinition · MutationService · MutationResult
GeneticRandom
```

**Consequences.**

- (+) Fast unit tests; agents can implement and review safely; future Fabric port stays a port.
- (−) Adapters required at the boundary; Minecraft concepts mapped to neutral context objects.

---

## ADR-0003 — Start with hardcoded built-in content before JSON/data-driven loading

**Status:** Accepted · 2026-04-28 (numbered differently from `ADR-003` below — keep both IDs)

**Context.** Data-driven loading too early would slow the genetic rules from stabilizing.

**Decision.** Initial content lives in centralized built-in classes (`BuiltinBeeSpecies`, `BuiltinBeeTraits`, `BuiltinBeeMutations`, `BuiltinBeeProducts`, `BuiltinBeeContent`) using clean models that can later become JSON. No JSON pipeline in the first MVP.

**Constraints.** Do not scatter species-specific conditionals across services; do not hardcode species behavior in event handlers; keep stable IDs suitable for future serialization.

(Migration to JSON: see [ADR-0010](#adr-0010--data-driven-content-strategy).)

---

## ADR-0004 — Use vanilla bee breeding as the first gameplay loop

**Status:** Accepted · 2026-04-28

**Context.** The first playable fantasy is `vanilla bees + flowers → baby bee → mod assigns genome with inheritance & mutation`.

**Decision.** First gameplay loop uses **vanilla** bee breeding. Implement parent genome reading, child genome generation, mutation application, child genome storage, minimal mutation feedback. A tech apiary is **future** scope.

**Constraints.**

- Do not implement a full apiary before vanilla breeding works.
- Do not replace vanilla bees with a custom Bee subclass in the MVP.
- Do not make breeding deterministic (`A + B = C`).
- Missing parent genomes must be handled safely.

---

## ADR-0005 — Use placeholder assets before polished assets

**Status:** Accepted · 2026-04-28

**Context.** Early phases focus on docs, pure Java genetics, NeoForge integration, breeding, analyzer behavior. Polished assets are not on the critical path.

**Decision.** Use no assets or simple placeholders until gameplay requires them. Blockbench / MCP automation are optional future tooling, not dependencies.

**Constraints.** Do not block early phases on assets; clearly mark dev placeholders so they don't ship as final. Detailed rules in [`asset-generation-guidelines.md`](asset-generation-guidelines.md).

---

## ADR-0006 — Add Fabric support after the NeoForge MVP

**Status:** Accepted · 2026-04-28

**Context.** Supporting both loaders too early would double integration cost before the loop is proven.

**Decision.** Fabric is future work. Design for Fabric compatibility (common code stays loader-agnostic), but do not implement Fabric until the NeoForge MVP works. Strategy details: [DR-010](#dr-010--fabric-support-strategy).

---

## ADR-0007 — Keep resource bees out of the MVP

**Status:** Accepted · 2026-04-28

**Context.** Resource bees would distract from genetic depth and risk turning the mod into a deterministic recipe mod.

**Decision.** **No** iron / copper / gold / redstone / diamond / emerald / netherite / uranium bees in the MVP. Initial species are limited to **Meadow, Forest, Arid, Cultivated, Hardy**. Resource bees may be designed later — gated by [ADR-0012](#adr-0012--resource-bee-readiness).

---

## ADR-0008 — Use detailed local specs for AI implementation guidance

**Status:** Accepted · 2026-04-28

**Context.** AI agents need detailed scope, non-goals, acceptance, expected tests, and prompts; backlog cards alone are insufficient.

**Decision.** Use Markdown specs in the repo (this `docs/` folder, plus `CLAUDE.md` / `AGENTS.md`) as the primary detailed instructions for AI coding agents. Backlog stays concise and points here.

**Constraints.** Implementation docs are in English; user-facing planning discussion may be in Portuguese; specs include objective, scope, non-goals, suggested files, acceptance, expected tests, prompt, and definition of done.

---

## ADR-0009 — Genetic Apiary first version design

**Status:** Accepted

**Context.** First controlled beekeeping block. Genome storage, breeding, analyzer, production resolver, and 5 comb items already exist. An earlier "Caught Bee item" model was rejected because it requires a capture tool before the apiary is usable, diverging from the vanilla bee fantasy.

**Decision (core philosophy).** The Genetic Apiary is a **production block**, not a breeding machine. Breeding still happens **naturally in the world**; the apiary gives players a controlled, automatable place for bees to live and produce combs.

**Bee entry — vanilla housing parity.**

- `GeneticApiaryBlock extends BeehiveBlock`; `GeneticApiaryBlockEntity extends BeehiveBlockEntity`.
- Bees recognize the apiary as a valid home automatically (vanilla AI). No capture item needed.
- The apiary is the mod's analog of `minecraft:beehive` (same placement, properties, `facing`, `honey_level`, vanilla honey progression, shears/bottle harvest).
- Recipe matches the vanilla beehive pattern (planks + honeycomb), producing `curiousbees:genetic_apiary`.
- Species-specific wild nests stay separate (`SpeciesBeeNestBlock`); the genetic beehive has **no species filter** — all bees may enter.

**NeoForge subclass quirks.** Two explicit overrides keep vanilla behavior + correct persistence without mixins:

1. `GeneticApiaryBlockEntity#getType()` returns `ModBlockEntities.GENETIC_APIARY.get()` (so NBT save/load uses `curiousbees:genetic_apiary`, not `minecraft:beehive`).
2. `GeneticApiaryBlock#getTicker(...)` overridden to use `createTickerHelper(type, ModBlockEntities.GENETIC_APIARY.get(), BeehiveBlockEntity::serverTick)` because vanilla ticker wiring checks `BlockEntityType.BEEHIVE` equality.

**Production (additive).** When a bee with nectar enters the hive, **after** `super.addOccupant`:

1. Read genome via `BeeGenomeStorage.getGenome(bee)`.
2. Run `ProductionResolver.resolve(...)` with frame modifiers.
3. Add outputs to the apiary output inventory. Vanilla honey fill still happens.

**No-genome fallback.** Edge cases (pre-existing world bees, other mods) → assign biome-appropriate fallback genome, log WARNING, continue. Never crash.

**Inventory & GUI.**

- Slots: vanilla hive occupancy + honey (unchanged) + frames + extract-only outputs.
- Right-click empty hand opens the menu (shears / bottle still trigger vanilla harvest via `useItemOn`).
- Screen shows frame slots, output slots, synced honey level, simple bees-present indicator. Deeper genome readouts can layer in later without changing housing behavior.

**Energy / automation.** No power. `IItemHandler` on output slots (extract-only). Manual extraction from GUI also works. No insert from automation; bees enter on their own.

**Out of scope for first version (kept for later phases):**

```text
- Caught Bee items / capture mechanic
- Breeding inside the apiary
- Frame items and frame modifier effects
- Energy or power system
- Centrifuge
- Resource bees
- Fabric support
- Polished custom hive textures
- Complex GUI (progress bars, full genome display)
- Specific automation compat (Create, AE2, etc.)
```

**Consequences.**

- The apiary block + block entity must remain subclasses of vanilla hive classes.
- Honey level logic stays on the inherited `BeehiveBlockEntity` tick path (not suppressed).
- Production intercept point is the bee-with-nectar enters hive path.
- A simple GUI screen + menu is required.
- `BeeGenomeStorage` is called from the block entity (already platform-side; no architecture violation).

---

## ADR-0010 — Data-driven content strategy

**Status:** Accepted · 2026-04-29

**Context.** Phases 1–7 stabilized genetics, breeding, mutation, analyzer, production, and the genetic apiary. Adding species currently requires recompiling.

**Decision.**

**Order of migration:** trait alleles → species (reference traits) → mutations (reference species) → production (reference species).

**Built-in fallback strategy (Option B).** Built-in Java definitions are rewritten as data objects and converted into runtime domain definitions through the **same** pipeline as external data. The five MVP species remain bundled and registered first.

**Folder layout:**

```text
data/curious_bees/
├── species/<id>.json
├── traits/<chromosome>/<value>.json
├── mutations/<id>.json
└── production/<species>.json
```

**Override policy (Option A).** External data **cannot** override built-ins unless the built-in is absent from the registry. Duplicate IDs within the same source are rejected with a clear error. (A future ADR may relax this if datapack override is requested.)

**Validation strategy.** Two stages, both pure Java in `common`:

1. **Structural** — required fields, types, ranges; runs immediately after parsing.
2. **Referential** — IDs referenced by one type exist in another (species → traits, mutations → species).

**Error reporting.** Errors are never silently swallowed; collected per loading pass; each error includes definition ID + file path; a single invalid file rejects only its own definitions; built-ins always present.

**Common module package layout:**

```text
com.curiousbees.common.content.data        (DTOs)
com.curiousbees.common.content.validation  (validators)
com.curiousbees.common.content.conversion  (DTO → domain)
com.curiousbees.common.content.registry    (ContentRegistry)
```

**Datapack interaction.** NeoForge platform layer subscribes to `AddReloadListenerEvent` and triggers the common pipeline. Fabric reuses the same common pipeline through its resource API.

**Constraints.** No NeoForge/Minecraft imports in DTOs/validators; do not remove built-in fallback; no resource bees in this phase; no in-game content editor.

---

## ADR-0011 — Expanded content naming strategy

**Status:** Accepted · 2026-04-29

**Context.** Future content (biome / managed / harsh-environment / resource foundation / resource / compatibility / endgame bees) needs names that help players understand what bees do without making the mod feel like a Productive-Bees-style resource list.

**Decision.** Use a **hybrid** naming strategy for resource-adjacent and late-game content: thematic species names with player-facing explanation. Direct names like `Iron Bee` are allowed only when clarity is judged more important than identity for a specific case. MVP species (`Meadow`, `Forest`, `Arid`, `Cultivated`, `Hardy`) are not renamed.

**Rules.**

- **General:** short enough for analyzer output; readable in tooltips; thematic but not opaque; distinct from MVP and from major existing bee mods; tied to a species role, not only an item output.
- **Biome / environment:** names imply habitat/adaptation (`Mire`, `Tropical`, `Frost`, `Alpine`, `Briny`, `Cavern`, `Nocturnal`).
- **Managed / tech:** imply domestication, selection, stability, work (`Managed`, `Diligent`, `Industrious`, `Ordered`, `Stable`, `Refined`).
- **Resource foundation:** broad material themes before direct resource names (`Mineral`, `Metallic`, `Crystalline`, `Conductive`, `Resonant`, `Geologic`, `Deep`).
- **Resource bees:** thematic with clear explanation — `Ferric` (iron), `Cupric` (copper), `Aureate` (gold), `Resonant` (redstone), `Crystalline` (diamond), `Verdant` (emerald). Analyzer/guide/tooltip text carries clarity.
- **Compatibility:** fits both Curious Bees and the target mod; never hardcoded into common genetics.

**Constraints.** Do not rename MVP species; do not add resource bees as part of this decision; never use obscure names without analyzer/guide/tooltip support; do not let output item names replace species identity.

---

## ADR-0012 — Resource bee readiness

**Status:** Accepted · 2026-04-29

**Context.** [ADR-0007](#adr-0007--keep-resource-bees-out-of-the-mvp) keeps resource bees out of the MVP. Phase 9 needs a stronger gate.

**Decision.** Resource bees are **not allowed** until **all** prerequisites below are satisfied:

```text
1.  Stable genetics inheritance and mutation behavior.
2.  Stable data-driven content loading.
3.  Analyzer output that can explain species, traits, hybrid status, and relevant requirements.
4.  Basic production tested and balanced enough for non-resource species.
5.  At least one non-resource post-MVP branch implemented or approved.
6.  A resource foundation tier (or equivalent intermediate progression).
7.  A documented economy/balance model for resource outputs.
8.  A config (or future config strategy) for enabling/disabling/scaling resource production.
9.  A manual playtest checklist for resource progression.
10. A placeholder asset strategy for new combs/items.
```

**Design constraints (must).** Earned through genetics; require lineage work and purification; probabilistic; preserve hybrid usefulness; low/constrained base production; depend on production/processing/environment/tech/frame constraints where appropriate; understandable through analyzer/guide/tooltips; optional or configurable for modpacks later.

**Design constraints (must not).** Replace mining in early gameplay; become the first meaningful reward; encourage adding dozens of species in one slice; make wild/biome/managed/foundation bees irrelevant; hard-depend on external mod APIs in common genetics; bypass the content validation pipeline.

**Required progression shape.**

```text
Wild / Early MVP
  → Managed or Biome Progression
    → Harsh Environment or Tech Progression
      → Resource Foundation Bees
        → Resource Bees
          → Industrial / Compatibility / Endgame Bees
```

Do **not** jump from `Meadow + Forest → Iron Bee`.

**First implementation slice (when eventually approved) is small:** 1 resource foundation species + 1–2 resource bees + 1–2 mutations + minimal production + placeholder assets + tests.

**Naming.** Per [ADR-0011](#adr-0011--expanded-content-naming-strategy).

---

## ADR-003 — NeoForge bee genome storage

**Status:** Accepted

> Note: predates the ADR-00NN scheme; kept under its original ID so existing code comments still resolve.

**Context.** Vanilla `Bee` entities must carry, persist, and expose a `Genome` across save/load in NeoForge 1.21.1, while the genetics core stays free of NeoForge imports and active/inactive identity is preserved on reload.

**Decision (storage).** Use NeoForge **`AttachmentType<GenomeData>`** (entity data attachments) registered via `DeferredRegister`:

```java
DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
    DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MOD_ID);

DeferredHolder<AttachmentType<?>, AttachmentType<GenomeData>> BEE_GENOME =
    ATTACHMENT_TYPES.register("bee_genome",
        () -> AttachmentType.builder(() -> null) // null = no genome by default
                            .serialize(GenomeData.CODEC)
                            .build());
```

API: `bee.getData(...)` / `bee.hasData(...)` / `bee.setData(...)`. `.serialize(Codec)` makes NeoForge auto-save/load to the entity's NBT — no custom save event handling.

Client sync: not required initially; add when a screen needs it (`.syncWith(...)` or a packet).

**Decision (serialization shape).**

```java
record GenePairData(
    String firstAlleleId,
    String secondAlleleId,
    String activeAlleleId,
    String inactiveAlleleId
) {}

record GenomeData(Map<String, GenePairData> chromosomes) {}
```

Only stable string IDs; no Minecraft / no domain objects.

**Decision (preserving active/inactive).** Add a **package-private** static factory `GenePair.restored(first, second, active, inactive)` plus a private constructor that does **not** call `GeneticRandom`. Public `GenePair` construction always resolves randomness; only the deserializer uses `restored`.

A `GenomeSerializer` adapter converts `Genome ↔ GenomeData` and looks up alleles by stable ID through the content registry. Unknown allele IDs **log a WARNING and skip** (or return null genome) — never crash.

**Persistence behavior** matches the table in [`architecture.md` §4.3](architecture.md#43-persistence-behavior).

**Alternatives rejected.** Manual NBT (no codec validation, mixes with domain logic); JSON-string-in-NBT (fragile); custom Bee subclass (mixins / mod incompatibility); store-only-allele-IDs (re-rolls active/inactive on load).

**Risks & mitigations.** Unknown allele on load → WARN + skip. Active/inactive lost → serialized form stores it explicitly. Attachment registration → in mod constructor via `modEventBus`. Client access before sync → guard until sync added.

**Implementation order.**

```text
1. Add restored() factory to GenePair    (common — small Phase 1 fix)
2. Define GenePairData / GenomeData / Codec   (neoforge or neutral adapter)
3. Implement GenomeSerializer (toData / fromData)   (neoforge or neutral adapter)
4. Register AttachmentType   (neoforge)
5. Implement BeeGenomeStorage adapter   (neoforge)
6. Validate persistence manually in-game
```

---

## DR-010 — Fabric support strategy

**Status:** Proposed · 2026-04-29

**Context.** Fabric is added **after** the NeoForge MVP. Must be a port, not a fork.

**Target versions.**

```text
Minecraft: 1.21.1
Fabric Loader: 0.16.14 (recheck before coding)
Fabric API:    0.116.11+1.21.1 (recheck)
Java: 21
```

Rationale: keep parity with the NeoForge target on 1.21.1; loader/API are pinned conservatively because they move independently.

**Build layout.** Use the existing `common/`, `neoforge/`, `fabric/` module shape. **Do not** introduce Architectury or another abstraction layer in the first slice; Fabric depends on `common`.

**Entity genome storage.** **Fabric API Data Attachments**, persistent, with the same `Codec` over `GenomeData`. Reuse the common `GenomeSerializer`. The Fabric storage adapter handles missing/invalid data with WARNING logs.

**Item data.** Use vanilla/Fabric **Data Components** for future genome-bearing items. Not required for the first storage slice.

**Wild bee initialization.** Fabric entity-load/spawn-style hook, after genome storage exists. Detects vanilla `Bee` server-side, skips bees that already have a genome, maps biome to Meadow/Forest/Arid categories, calls common `WildBeeSpawnService`, stores via Fabric storage.

**Breeding hook.** Fabric provides no direct equivalent of NeoForge `BabyEntitySpawnEvent` in the docs reviewed for this spike. Use a **narrow mixin** at the vanilla animal/bee breeding method where parents and baby are simultaneously available; only run for `Bee`; read both parent genomes; call common `BeeBreedingOrchestrator`; write the child genome through Fabric storage; log+skip if a parent genome cannot be resolved. Do **not** use nearest-parent inference unless a later decision documents why it is safe.

**Analyzer.** Reuse common analyzer services. Fabric-specific code only registers the item, handles use-on-entity, reads genome through Fabric storage, sends formatted output.

**Production.** Reuse `ProductionResolver`, `ContentRegistry`, same definitions and IDs. If the Genetic Apiary is ported, do it as a separate slice after base Fabric MVP.

**Risks & mitigations.**

- *Breeding hook needs mixin details* → small mixin spike before full implementation; document target method and fallback.
- *Data Attachment codec shape* → implement Fabric storage in one isolated task with `GenomeData` as payload; test serialization.
- *Build setup breaks NeoForge* → keep Fabric build changes isolated; run `:common:test` and `:neoforge:build` after build layout changes.
- *Fabric becomes a gameplay fork* → adapters call common services; no genetics logic in Fabric mixins/events.

**Recommended next task.** `10B — Multi-loader Build Layout Decision` (the repo already has the three modules; the decision should confirm and document that no large refactor is needed). After that: `10C — Fabric Module Setup` adds Loom/build metadata.

**Open questions.**

- Confirm exact `Codec` implementation for `GenomeData` after Fabric module dependencies compile.
- Choose the exact Fabric event/mixin point for wild bee initialization at implementation time.
- Confirm exact Yarn-named breeding method and injection point after Fabric/Yarn dependencies are available locally.

**Verification gate 1 — Fabric feasibility review.** Ready for review. Open questions: is Fabric still worth doing now? Is Minecraft 1.21.1 still the target? Is Data Attachment acceptable? Is a mixin-based breeding hook acceptable? Should we keep the existing `common/neoforge/fabric` layout?

**Sources checked.**

- Fabric Maven (Fabric API): https://maven.fabricmc.net/net/fabricmc/fabric-api/fabric-api/
- Fabric Maven (Loader): https://maven.fabricmc.net/net/fabricmc/fabric-loader/
- Fabric Data Attachments: https://docs.fabricmc.net/develop/data-attachments
- Fabric Custom Data Components: https://docs.fabricmc.net/develop/items/custom-data-components
- Fabric Events: https://docs.fabricmc.net/develop/events
- Fabric 1.21 / 1.21.1 notes: https://fabricmc.net/2024/05/31/121.html

_Last updated: 2026-05-04._
