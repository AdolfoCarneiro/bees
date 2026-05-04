# Curious Bees — Roadmap

High-level plan for turning the validated **genetics core** into a **polished, automation-friendly** bee mod: Productive Bees–grade UX and loop quality, **without** being a fork — Curious Bees keeps **living bees**, **Mendelian genomics**, and its own **species / textures / products**.

**Technical contracts:** [`docs/architecture/README.md`](architecture/README.md) (specs 02–05).  
**Product framing:** root `Readme.md`, `CLAUDE.md`, `AGENTS.md`.  
**Hive philosophy (v1):** [`docs/decisions/0009-genetic-apiary-design.md`](decisions/0009-genetic-apiary-design.md).

---

## North star

- **World:** diverse species in sensible habitats; **wild nests** with Forestry-like *variety* (many nest types / looks) but **vanilla-like interaction** (pathing, occupancy, harvest patterns players already understand).
- **Base loop:** breeding and discovery stay **entity-first** in the open world; genetics remain readable after analysis, not chat-only.
- **Production:** **crafted hive / advanced hive** layer that feels as **clear, extensible, and automation-ready** as Productive Bees: slots, upgrades/extensions where needed, visible bee state, predictable outputs, hopper/pipe-friendly sides.
- **Processing:** **combs** → machine line (e.g. **centrifuge**) separating **honey**, **by-products**, and **wax** — same *role* as familiar mods, Curious Bees IDs and balance.
- **Identity:** own GUI art and layout can start “PB-shaped” for learnability, then diverge; **no** goal of pixel-perfect clone.

---

## Open decisions (capture in ADRs when you start the slice)

| Topic | Question |
|--------|----------|
| **Advanced hive footprint** | Single expandable block vs **multiblock** (Forestry-style) vs hybrid (core + extensions as attached blocks). |
| **Bee transport** | Optional **scoped** item/container for moving a bee into an advanced hive vs **only** vanilla-style housing pathing. Must stay compatible with “living bee” fantasy and [`0009`](decisions/0009-genetic-apiary-design.md) (apiary is not a breeding gate). |
| **PB parity depth** | Which PB features are **must** (automation, upgrade slots, JEI later) vs **nice** (cosmetic parity, exact slot counts). |

---

## Phase 0 — Shipped foundation (keep healthy)

**Goal:** genetics, breeding, mutations, analyzer touchpoint, production resolver, combs, first **Genetic Apiary** path.

**Done when:** CI/tests green; `common/genetics` stays Minecraft-free per ADR-0002; apiary behavior matches ADR-0009.

---

## Phase 1 — Visual & species readability

**Goal:** players *see* Curious Bees, not “vanilla bees with a secret spreadsheet.”

- Species **textures** (and fallback rules) for living bees.
- Analyzer **UI** (and analysis state respected in tooltips / apiary UI).
- Minimal **content pipeline** so adding a species does not scatter `if` checks.

**Exit:** new player can tell species apart in-world and after analysis without debug commands.

---

## Phase 2 — World presence & nests

**Goal:** “bees in the right places” + nest variety.

- Spawn / habitat rules per species (tags, biomes, light, height — keep rules **simple** until a dedicated climate sim is approved).
- **Wild nest** set: more **visual / structural variety** (Forestry inspiration), **interaction** aligned with vanilla bee nest expectations (POI, anger, harvest, occupancy).
- Optional: rare pockets / structures — only if performance and maintenance stay sane.

**Exit:** exploration feels rewarding; each common species has a believable default home in the world.

---

## Phase 3 — Hive UX & automation (Productive-shaped)

**Goal:** the **crafted / advanced hive** is the main factory interface for production.

- GUI: frames / outputs / bee summary (respecting **analyzed** state), clear feedback (occupancy, progress, errors).
- **Extensions / upgrades** (or multiblock modules) — pick approach from open decisions; implement the **smallest** set that proves automation and growth.
- **Bee visible / represented** in UI (even if render is simple at first).
- Automation: sided IO, void rules, redstone/hopper contracts documented in-code and briefly in `docs/architecture/02` if behavior is non-obvious.

**Exit:** a modded kitchen-sink player can run a hive line with hoppers/pipes without babysitting every tick.

---

## Phase 4 — Processing line

**Goal:** combs become the hub item between **hive** and **tech**.

- **Centrifuge** (or equivalent) with recipes: comb → honey + wax + species outputs (data-driven when ready, per ADR-0010).
- Bottling / fluid honey if the mod already heads that way; otherwise discrete honey items first.

**Exit:** closed loop: world → hive → comb → centrifuge → useful ingredients — no dead-end inventory clutter.

---

## Phase 5 — Differentiation & “our face”

**Goal:** no longer “PB with genomes stapled on.”

- Distinct **GUI** skin, sounds, guidebook or ponder-style hints (optional).
- **Species roster** growth in **small branches** (not 40 bees in one drop) — see [`0012-resource-bee-readiness.md`](decisions/0012-resource-bee-readiness.md) for gating resource bees.
- Balance pass once loops are playable.

**Exit:** trailer-worthy identity; genetics is the reason to pick Curious Bees, UX is the reason to stay.

---

## Phase 6 — Fabric & compatibility (when scoped)

Per existing ADRs: **NeoForge first**; Fabric gameplay when explicitly planned. JEI/REI and cross-mod pipes only when a phase calls for them.

---

## How to use this file

1. Turn the **next phase** into **issues** with acceptance bullets copied from **Exit** clauses.  
2. When an **open decision** is resolved, add or update an ADR under `docs/decisions/` and link it here.  
3. Avoid starting a later phase before the **previous Exit** is true — otherwise art and automation debt stack up.

_Last updated: 2026-05-04._
