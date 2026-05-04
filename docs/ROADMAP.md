# Curious Bees — Roadmap

High-level plan for turning the validated **genetics core** into a **polished, automation-friendly** bee mod: Productive Bees–grade UX and loop quality, **without** being a fork — Curious Bees keeps **living bees**, **Mendelian genomics**, and its own **species / textures / bee products**.

| Audience | Use |
|----------|-----|
| **Maintainers** | Phase ordering, exit gates, open decisions → ADRs |
| **Contributors / AI agents** | Scope guardrails + links to specs; pair with `CLAUDE.md` workflow |
| **Players / pack makers** | Expectation management only — not a changelog |

**Technical contracts:** [`docs/architecture/README.md`](architecture/README.md) (specs 02–05).  
**Product framing:** root `Readme.md`, `CLAUDE.md`, `AGENTS.md`.  
**Hive v1 (floor):** [`docs/decisions/0009-genetic-apiary-design.md`](decisions/0009-genetic-apiary-design.md) — subclass of vanilla hive, production additive to honey, no capture item required for first use.

---

## North star

- **World:** diverse species in sensible habitats; **wild nests** with Forestry-like *variety* (many nest types / looks) but **vanilla-like interaction** (pathing, occupancy, harvest patterns players already understand).
- **Base loop:** breeding and discovery stay **entity-first** in the open world; genetics remain readable **after analysis**, not chat-only.
- **Production:** **crafted hive / advanced hive** layer that feels as **clear, extensible, and automation-ready** as Productive Bees: slots, upgrades/extensions where needed, visible bee state, predictable outputs, hopper/pipe-friendly sides.
- **Processing:** **combs** → machine line (e.g. **centrifuge**) separating **honey**, **by-products**, and **wax** — same *role* as familiar mods, Curious Bees IDs and balance.
- **Identity:** own GUI art and layout can start “PB-shaped” for learnability, then diverge; **no** goal of pixel-perfect clone.

### Non-goals (until explicitly replanned)

Aligned with current ADRs and `Readme.md`: **resource bee trees**, **lifecycle/death/larvae as enforced sim**, **temperature/humidity climate engine**, **Fabric gameplay parity** before NeoForge loop is solid, **JEI/REI** before in-game UX explains the basics.

### Relationship to ADR-0009 (important)

ADR-0009 defines the **first** Genetic Apiary: vanilla housing parity, additive comb production, **no** centrifuge, **no** real frame items, **no** “caught bee” requirement. This roadmap **extends** that foundation — later phases **explicitly** add what was listed “out of scope” there (frames, centrifuge, richer GUI, optional transport) **without** rewriting the core decision that bees **enter hives voluntarily** like vanilla.

---

## Positioning vs. “Productive Bees–like”

Curious Bees is **not** trying to replace Productive Bees in packs. It is trying to be the option where **lineage, dominance, and mutation** matter and the **factory layer still feels modern**.

| Dimension | Productive Bees (reference) | Curious Bees (target) |
|-----------|----------------------------|------------------------|
| **Core fantasy** | Often block + cage bee loop | **Living** `Bee` entities + genetics on entity |
| **Progression** | Species discovery + recipes | **Breeding + analysis** + controlled production |
| **Automation** | Very mature | **Must** reach hopper/pipe clarity; deep per-mod compat later |
| **Content volume** | Huge roster | **Small branches**; quality over count ([`0012`](decisions/0012-resource-bee-readiness.md)) |
| **Art / GUI** | Established house style | Learnable layout first, **own** art pass in Phase 5 |

**Viability:** overlap is inevitable and healthy — same audience. Differentiation is **genetics depth + vanilla bee feel + your art/names**. Marketing line: “**Productive-grade logistics, Forestry-grade curiosity.**”

---

## Open decisions (→ ADR when the slice starts)

| Topic | Question | Suggested trigger |
|--------|----------|-------------------|
| **Advanced hive footprint** | Single expandable block vs **multiblock** (Forestry-style) vs **hybrid** (core + attached extension blocks) | Before coding Phase 3 “extensions” |
| **Bee transport** | Optional **scoped** item/container for hive insertion vs **only** pathing | Before “advanced hive only” onboarding flows |
| **PB parity depth** | Must-have automation features vs cosmetic slot parity | Before large GUI refactors |
| **Fluid honey** | Tanks/pipes vs bottle-only discrete honey | Before centrifuge output schema is frozen |
| **Data-driven cutover** | When JSON/datapack species ship for players ([`0010`](decisions/0010-data-driven-content-strategy.md)) vs dev-only | Before inviting pack authors to depend on paths |

---

## Phase dependency (read order)

```mermaid
flowchart LR
  P0[Phase 0 Foundation] --> P1[Phase 1 Visual and Analyzer]
  P1 --> P2[Phase 2 World and Nests]
  P2 --> P3[Phase 3 Hive UX and Automation]
  P3 --> P4[Phase 4 Processing]
  P4 --> P5[Phase 5 Identity and Roster]
  P5 --> P6[Phase 6 Fabric and Compat]
```

**Parallelism rule:** art in P1 can overlap **early** P2 only if the same people are not blocked — otherwise finish P1 **Exit** first so nest work reuses final texture IDs. **Do not** start Phase 4 recipes until Phase 3 outputs and slot contracts exist, or you will rewrite recipes twice.

---

## Phase 0 — Shipped foundation (keep healthy)

**Goal:** genetics, breeding, mutations, analyzer touchpoint, production resolver, combs, first **Genetic Apiary** path — all stay regression-safe.

**Inventory (what already justifies this phase):**

- Pure Java genetics + tests (`ADR-0002`).
- NeoForge genome storage + spawn integration.
- `ProductionResolver` + built-in species/products.
- `GeneticApiaryBlock` / `BlockEntity` / menu / screen (see codebase + `0009`).

**Hardening checklist (ongoing):**

- [ ] CI green on `common` + `neoforge` tests.
- [ ] Any attachment or menu field that affects **client** UI is **synced** (document in code if non-obvious).
- [ ] WARNING logs on recoverable bad data (no silent swallow).
- [ ] New content follows centralized definitions until [`0010`](decisions/0010-data-driven-content-strategy.md) migration is executed.

**Exit:** no open P0 regressions; a new contributor can run the mod and complete world breed → hive → comb without cheats.

---

## Phase 1 — Visual & species readability

**Goal:** players *see* Curious Bees, not “vanilla bees with a secret spreadsheet.”

**Deliverables**

| Track | Items |
|-------|--------|
| **Rendering** | Species → texture resolution for **living** bees; sane fallback when missing asset. |
| **Analyzer** | Screen (or block+screen) that respects **unanalyzed vs analyzed** ([`03` genetics spec](architecture/03-genetics-system-spec.md) visibility rules). |
| **Tooltips** | Analyzed: species / hybrid hints / traits as designed; unanalyzed: vague or gated — no full chromosome dump. |
| **Content hygiene** | Adding a species touches **data + lang + visual key**, not random `if` in handlers ([`05`](architecture/05-content-design-spec.md), naming [`0011`](decisions/0011-expanded-content-naming-strategy.md)). |
| **Assets** | Prompts / requests tracked per maintainer workflow (`CLAUDE.md` Art); no undeclared “final” placeholders. |

**Risks**

- Client-only desync → **test multiplayer** for analyzer + tooltip paths.
- Texture explosion → enforce **manifest or checklist** per species before merge.

**Exit:** new player can tell species apart in-world and after analysis **without** debug commands; analyzer is usable in survival.

---

## Phase 2 — World presence & nests

**Goal:** “bees in the right places” + nest variety that sells the mod in the first five minutes.

**Deliverables**

| Track | Items |
|-------|--------|
| **Spawn** | Habitat metadata per species: biome tags, height bands, light — **simple** predicates only ([`05`](architecture/05-content-design-spec.md)). |
| **Nests** | Multiple wild nest blocks or variants; **vanilla-grade** interaction (anger, harvest, occupancy); align POI / hive targeting with code paths such as `BeeSpeciesHiveTargetHandler`. |
| **Population** | Sane caps / chunk friendliness; no bee soup lag spikes. |
| **Discovery** | Optional journal / patchouli **later** — not a gate for Phase 2 exit. |

**Risks**

- Wrong nest ↔ species pairing → angry bees + confused players; add **debug overlay** or creative-only inspector if needed.
- Worldgen churn → prefer **surface features** over heavy structures until stable.

**Exit:** each **shipping** species has a believable default home; exploration visibly rewards finding new nests/bees.

---

## Phase 3 — Hive UX & automation (Productive-shaped)

**Goal:** the **crafted / advanced hive** is the main factory interface for production; ADR-0009 remains the **compatibility floor** for the base genetic hive.

**Deliverables**

| Track | Items |
|-------|--------|
| **GUI** | Frames + outputs + bee summary; occupancy and honey level obvious; errors human-readable. |
| **Frames** | Real **items** with effects wired through production resolver / modifiers (extends `0009` out-of-scope list intentionally). |
| **Representation** | “Bee inside” — start with **2D / entity preview / icon + status** if full 3D render is costly. |
| **Automation** | Sided `IItemHandler` (or successor) contracts: which sides insert/extract; output extract-only; document redstone if any. |
| **Advanced hive** | Either upgraded block or new tier — **decision table** in open decisions above. |
| **Optional energy** | Only if a future ADR adds it — default remains **no power** per `0009` unless design revises. |

**Risks**

- Feature parity creep → timebox “PB-like” to **automation clarity**, not every PB block.
- Breeding inside hive → still **out of default design** unless new ADR; keep breeding in the world to reduce coupling.

**Exit:** kitchen-sink pack player runs hive lines with hoppers/pipes **without** per-tick babysitting; frames change outputs in measurable ways.

---

## Phase 4 — Processing line

**Goal:** combs are the **hub** between hive output and the rest of a tech modlist.

**Deliverables**

| Track | Items |
|-------|--------|
| **Machine** | Centrifuge (name TBD) + block entity + menu. |
| **Recipes** | Comb → **wax** + **honey** (fluid or item) + **species by-products**; stub recipes for all shipping combs. |
| **Data** | Prefer moving recipe JSON together with [`0010`](decisions/0010-data-driven-content-strategy.md) timing — avoid hardcoding 30 JSON in Java. |
| **JEI/REI** | Phase 6 or late Phase 4 — only when recipes are stable enough not to churn. |

**Risks**

- Fluid registration order / modlist conflicts → start **discrete honey** if fluids block the exit.

**Exit:** closed loop **world → hive → comb → centrifuge → ingredients** with no dead-end inventory clutter.

---

## Phase 5 — Differentiation & “our face”

**Goal:** trailer-worthy identity; genetics is the reason to pick Curious Bees, UX is the reason to stay.

**Deliverables**

- Custom **GUI** backgrounds and sounds; replace dev placeholders.
- **Narrative / guide** (optional): Patchouli, in-game tips, or quest hooks — pick one, do it well.
- **Roster growth** in **small branches** (mutation lines that tell a story), not bulk species drops.
- **Balance pass** after telemetry from real play (rates, mutation pain, comb flow).
- **Resource bees** only per [`0012`](decisions/0012-resource-bee-readiness.md) — dedicated design, not accidental ore bees.

**Exit:** blind playtesters describe the mod in **one sentence** without saying “it’s a PB clone.”

---

## Phase 6 — Fabric & compatibility (when scoped)

- **Fabric:** gameplay port when explicitly scheduled ([`DR-010`](decisions/DR-010-fabric-support-strategy.md)); genetics core should already be loader-agnostic.
- **Cross-mod:** Create / AE2 / pipe mods — **explicit** issues per integration; no blanket “compat layer” spike.
- **Distribution:** `docs/release/` or project publish docs when versions go public.

**Exit:** each scoped integration has a test world checklist and an owner.

---

## Operating model (how to run this roadmap)

**Operational breakdown:** each phase is broken into **epics + tasks** in [`TASKS.md`](TASKS.md). Use that file to open issues; use this one to argue about phase order.

1. **Issues:** one issue per task row in `TASKS.md`; title uses the task id (e.g. `E3-T05`).
2. **Exit gates:** closing a phase = **all Exit bullets true**; if not, split scope rather than lowering the bar.
3. **ADR hygiene:** open-decision table rows graduate to `docs/decisions/` with status **Accepted** before merge-heavy work lands.
4. **Playtests:** short `docs/research/` or issue notes after each phase (what confused players).
5. **Review cadence:** re-read this file after major releases; trim stale bullets rather than append forever.

---

## How to use this file (short)

1. Pick the **lowest phase** whose **Exit** is false — that is the priority lane.  
2. Resolve **open decisions** with ADRs before building the expensive side (multiblock, fluids, transport).  
3. Keep **`common/genetics`** free of Minecraft imports in every PR ([`0002`](decisions/0002-pure-java-genetics-core.md)).

_Last updated: 2026-05-04._
