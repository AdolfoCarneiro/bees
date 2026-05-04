# Requirements

What Curious Bees **must do**, **must not do**, and **must remain true** as it grows. This is the design contract; the [`architecture.md`](architecture.md) explains how it is achieved, and [`decisions.md`](decisions.md) records the locked choices behind specific lines below.

> **Stability rule:** changing anything tagged **MUST** in this file requires updating [`decisions.md`](decisions.md) (new or amended ADR) in the same change.

---

## 1. Identity (what the mod is)

- **R-1.1 (MUST)** Living **vanilla** `Bee` entities are the **default** game-world representation of bees. Item-form bees are allowed only in **scoped** features (e.g. transport into an advanced hive).
- **R-1.2 (MUST)** Each bee carries a **genome** (chromosomes / alleles / dominance / active+inactive resolution).
- **R-1.3 (MUST)** Breeding follows **vanilla** Minecraft flow (two bees, flowers, baby bee). The mod assigns the baby's genome **after** vanilla creates it.
- **R-1.4 (MUST)** Inheritance is **Mendelian**; mutation is **probabilistic**, never `A + B → C` deterministic.
- **R-1.5 (MUST)** Genetics may not be revealed in full to the player **before analysis**. Tooltips and chat must respect analyzed state.
- **R-1.6 (SHOULD)** The mod targets **Productive-Bees-grade** UX clarity for the hive/automation layer while keeping the **Forestry-grade** depth of genetic curiosity.

## 2. Identity (what the mod is not)

- **R-2.1 (MUST NOT)** Be a Forestry port or a Productive Bees fork.
- **R-2.2 (MUST NOT)** Replace the entire game loop with item-only bees.
- **R-2.3 (MUST NOT)** Ship resource bees (iron / copper / gold / diamond / redstone / emerald / netherite / etc.) as a broad category before the readiness criteria in [`decisions.md` → ADR-0012](decisions.md) are satisfied.
- **R-2.4 (MUST NOT)** Enforce lifecycle / death / larvae mechanics by default.
- **R-2.5 (MUST NOT)** Implement temperature / humidity / climate simulation by default.
- **R-2.6 (MUST NOT)** Add deep cross-mod compatibility layers (Create, AE2, JEI/REI deep integration, etc.) without an explicit phase scope.

## 3. Functional requirements (today)

These describe the validated foundation the mod **already** has and must not regress.

- **R-3.1 (MUST)** Wild bees spawned in the world receive a biome-appropriate genome.
- **R-3.2 (MUST)** Genome **persists** across save/load (entity attachment + codec; see [`decisions.md` → ADR-003 NeoForge bee genome storage](decisions.md)). Active/inactive resolution must **not** be re-rolled on load.
- **R-3.3 (MUST)** Two bees breeding produce a baby with a genome derived from the parents (one allele per chromosome from each parent) plus possible mutation.
- **R-3.4 (MUST)** Missing parent genome → safe fallback (assign biome-appropriate wild genome, log WARNING). Never crash.
- **R-3.5 (MUST)** An **Analyzer** item turns a bee from "unanalyzed" to "analyzed" and produces a player-facing report.
- **R-3.6 (MUST)** A **Genetic Apiary** block exists with vanilla-style housing parity (subclass of beehive, see [`decisions.md` → ADR-0009](decisions.md)). Bees enter/leave on their own; the apiary may add **mod production** on top of vanilla honey, not in place of it.
- **R-3.7 (MUST)** A **production resolver** generates outputs from the active species (and optionally inactive species) modulated by traits.

## 4. Functional requirements (incoming, defined by the roadmap)

These are required for upcoming phases. Detail and order live in [`roadmap.md`](roadmap.md) and [`TASKS.md`](TASKS.md).

- **R-4.1 (SHOULD)** Players must be able to **distinguish species** in-world (visual variants) and after analysis (analyzer screen, tooltips).
- **R-4.2 (SHOULD)** Wild **nest variety** (Forestry-flavored visual breadth) with **vanilla-grade interaction** (POI, anger, occupancy, harvest with shears/bottle).
- **R-4.3 (SHOULD)** A **frame** system: items in apiary slots that modulate production via the resolver.
- **R-4.4 (SHOULD)** A **processing line** (centrifuge or equivalent) turning combs into honey + wax + species by-products. Same role as familiar mods, Curious Bees IDs and balance.
- **R-4.5 (SHOULD)** Sided IO contracts on the apiary and processing blocks (extract-only outputs, designated frame insert side, vanilla bee entry preserved).
- **R-4.6 (MAY)** An **advanced hive** (single block / multiblock / hybrid) — footprint is an open decision; trigger an ADR before coding.
- **R-4.7 (MAY)** Optional **bee transport item** for moving bees into an advanced hive — open decision.

## 5. Non-functional requirements

- **R-5.1 (MUST)** Genetics core (`common/genetics` and supporting `common/content` / `common/gameplay`) is **pure Java** — no Minecraft, NeoForge, Fabric, registries, events, NBT, components, attachments, mixins, item/block, GUI, or client rendering imports. (See [`decisions.md` → ADR-0002](decisions.md).)
- **R-5.2 (MUST)** Genetics core is **deterministic** when given a deterministic random source.
- **R-5.3 (MUST)** Validation (content schema, ID references) runs in pure Java; errors are **collected** and **reported** with the offending IDs (and file paths when available); single bad files never abort full content load. ([`decisions.md` → ADR-0010](decisions.md).)
- **R-5.4 (MUST)** Built-in content is **always** registered first; external (data-driven) content **may not override** built-ins unless explicitly allowed.
- **R-5.5 (MUST)** Logging: services use `java.util.logging.Logger` — `WARNING` before skip/throw on bad input, `FINE` for trace. Models throw without logging.
- **R-5.6 (MUST)** Multiplayer correctness: any state that drives client UI must have a defined sync path.
- **R-5.7 (SHOULD)** Performance: spawn / nest / breeding code is chunk-friendly; no per-tick allocations in hot paths.

## 6. Content scope guardrails

- **R-6.1 (MUST)** MVP species set is fixed at: **Meadow, Forest, Arid, Cultivated, Hardy** (see [`architecture.md` → §Content / built-ins](architecture.md)).
- **R-6.2 (MUST)** Adding new species after the MVP must follow [`decisions.md` → ADR-0010 (data-driven) and ADR-0011 (naming)](decisions.md).
- **R-6.3 (MUST)** Resource-bee implementation is gated by **all** prerequisites in [`decisions.md` → ADR-0012](decisions.md).
- **R-6.4 (MUST)** Adding a new species **MUST NOT** require touching engine code beyond what the structural epics in [`TASKS.md`](TASKS.md) (E1.C, E2.A/B) make data-driven.

## 7. Asset requirements

Detailed rules live in [`asset-generation-guidelines.md`](asset-generation-guidelines.md). Hard rules:

- **R-7.1 (MUST)** No silent placeholder-as-final assets. Every dev placeholder is labeled `DEV-PLACEHOLDER` in code/comment so a grep can find it before release.
- **R-7.2 (MUST)** Missing texture / lang key never crashes; logs a WARNING and falls back visibly.

## 8. Platform requirements

- **R-8.1 (MUST)** Initial loader: **NeoForge 1.21.1** ([`decisions.md` → ADR-0001](decisions.md)).
- **R-8.2 (MAY → MUST when scoped)** Fabric port via the strategy in [`decisions.md` → DR-010](decisions.md). The port is platform-only; no genetics logic in Fabric mixins.
- **R-8.3 (MUST)** Build layout supports `common/`, `neoforge/`, `fabric/` modules; common code does not import loader-specific APIs.

## 9. Definition of done (per task)

A task is "done" when, in addition to its own acceptance bullets in [`TASKS.md`](TASKS.md):

1. CI green (`common` + `neoforge` test suites).
2. New behavior covered by tests where the area already has tests.
3. No regression of any **MUST** above.
4. If it touches a **MUST**, [`decisions.md`](decisions.md) is updated in the same change.
5. Commit message uses the conventional prefix (`docs:` / `core:` / `neoforge:` / `client:` / `test:` / `build:`).

_Last updated: 2026-05-04._
