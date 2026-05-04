# Curious Bees — Tasks

Operational breakdown of [`roadmap.md`](roadmap.md) into **epics** (E#) and **tasks** (E#-T##). Use this as the issue-tracker mirror — one row per task is a healthy issue, one epic is a healthy milestone.

> Sibling docs: [`project-guide.md`](project-guide.md) · [`requirements.md`](requirements.md) · [`architecture.md`](architecture.md) · [`decisions.md`](decisions.md) · [`asset-generation-guidelines.md`](asset-generation-guidelines.md).

## Scope rule for this file

> **Adding new species / new mutations / new biome packs is intentionally OUT OF SCOPE for every task below.**
> The work here is **structural** — make adding species **cheap and safe later**. Anything that says “species” refers to the **few existing** ones (`meadow`, `forest`, `arid`, `cultivated`, `hardy`) only.

When you eventually open the “species expansion” epic (post-Phase 5), the tasks will be of the form *“ship species X using the pipeline already built”* and will not require touching engine code.

---

## Conventions

- **ID:** `E<epic>-T<NN>` (e.g. `E3-T05`).
- **Size:** S (≤ ½ day), M (1–2 days), L (multi-day; consider splitting).
- **Status:** `todo` / `wip` / `blocked` / `done` / `skipped`.
- **Done when:** acceptance bullets all true; tests added where the area has tests; no regressions in `common` + `neoforge` test runs.
- **Touches:** rough file/area pointers — not a contract, just guidance for the reviewer / agent.

Each task ends with **Done when** and **Depends on**. If it depends on an open ADR, the dependency is the **ADR**, not a sibling task.

---

## Epic index

| Epic | Theme | Maps to phase |
|------|-------|----------------|
| **E0** | Foundation hardening | P0 |
| **E1** | Visual & analyzer | P1 |
| **E2** | World presence & nest framework | P2 |
| **E3** | Hive UX & automation | P3 |
| **E4** | Processing line | P4 |
| **E5** | Identity polish (no new content) | P5 (structural slice) |
| **E6** | Fabric & compatibility prep | P6 |
| **EX** | Cross-cutting (data-driven, tests, observability) | spans P0–P5 |

---

## Epic E0 — Foundation hardening

**Goal:** keep the shipped loop regression-safe so later epics can move fast.

| ID | Task | Size | Done when | Depends on |
|----|------|------|-----------|------------|
| **E0-T01** | Verify `common/genetics` import boundary in CI | S | Test or static check fails the build if `common/genetics` references `net.minecraft.*` / NeoForge / Fabric. | ADR-0002 |
| **E0-T02** | Snapshot test for `ProductionResolver` distributions | M | Statistical test runs N crosses and asserts known distributions within tolerance for current built-in species. | — |
| **E0-T03** | Multiplayer smoke checklist | S | A checklist (issue template, PR template, or appended to [`architecture.md` §9.3](architecture.md)) lists steps: spawn → breed → analyze → hive insert → output read on dedicated server. | — |
| **E0-T04** | Audit WARNING/FINE logging in services | S | Every recoverable bad-input path either WARNs or throws (no silent skip). PR removes any `// TODO log` left in services. | — |
| **E0-T05** | Document Genetic Apiary persistence quirks | S | Comment block / short doc note explains the `getType()` / ticker overrides described in [`decisions.md` → ADR-0009](decisions.md), so a future contributor does not “fix” it back. | ADR-0009 |
| **E0-T06** | Tag known dev placeholders | S | All `// DEV PLACEHOLDER` strings include a stable token (e.g. `DEV-PLACEHOLDER`) so a grep can find them before release. | — |

**Epic exit:** CI green, regressions caught early, no “mystery state” for new contributors.

---

## Epic E1 — Visual & analyzer

**Goal:** the player can **see** species and **read** genome state through UI, with structures that scale to many species later.

### Subepic E1.A — Rendering pipeline

| ID | Task | Size | Done when | Depends on |
|----|------|------|-----------|------------|
| **E1-T01** | Define species → texture key mapping | M | A single resolver maps `species_id → ResourceLocation`; missing key returns documented fallback (vanilla bee or generic). No `switch` over species in renderers. | [`architecture.md` §7](architecture.md) |
| **E1-T02** | Bee renderer override using mapping | M | `Bee` entity renderer picks species texture via E1-T01; default vanilla appearance preserved when genome missing. | E1-T01 |
| **E1-T03** | Resource pack hygiene | S | Naming follows [`decisions.md` → ADR-0011](decisions.md); fallback texture clearly labeled placeholder. | E1-T01 |
| **E1-T04** | Visual variant hooks (analyzed/unanalyzed) | S | Renderer can express **one** binary variant (e.g. tint on unanalyzed) without per-species `if`. Toggle is data-driven or constant. | E1-T01 |

### Subepic E1.B — Analyzer UX

| ID | Task | Size | Done when | Depends on |
|----|------|------|-----------|------------|
| **E1-T05** | Analyzer report data audit | S | Listed: every field that should appear after analysis vs before. Source = [`architecture.md` §5](architecture.md). | — |
| **E1-T06** | Analyzer screen rework | M | `BeeAnalyzerScreen` shows analyzed report in clear sections (active / inactive / hybrid hint / traits) using report payload only. No raw genome dump. | E1-T05, E0-T03 |
| **E1-T07** | Tooltip gate | S | Item / entity tooltips never reveal post-analysis fields when bee is unanalyzed. Unit/integration test covers both states. | E1-T05 |
| **E1-T08** | Right-click flow polish | S | Analyzer item interaction errors / cooldowns / message keys are localized; no hardcoded English in code paths. | E1-T05 |

### Subepic E1.C — Content hygiene (no new species)

| ID | Task | Size | Done when | Depends on |
|----|------|------|-----------|------------|
| **E1-T09** | Centralize species visual metadata | M | `BuiltinBeeSpecies` (or sibling) carries texture key + display name key; renderer + tooltip read from there only. | E1-T01 |
| **E1-T10** | Localization audit | S | All current species/items/menus have `lang` keys; `en_us.json` complete; checker script (or test) fails on missing keys. | — |
| **E1-T11** | New-species checklist parity | S | `.claude/plugins/local/skills/new-bee-species.md` matches the **actual** code paths a new species touches after E1-T09. | E1-T09 |

**Epic exit:** new species would be a **data + lang + texture** drop; no engine change required.

---

## Epic E2 — World presence & nest framework

**Goal:** generic, data-driven habitat + nest framework — empty of new species, full of structure.

### Subepic E2.A — Habitat / spawn framework

| ID | Task | Size | Done when | Depends on |
|----|------|------|-----------|------------|
| **E2-T01** | Habitat predicate model | M | Model class describes habitat as `(biome tags, height band, light, optional weight)` consumed by spawn logic. Minecraft-free if possible (or thin Forge bridge). | [`architecture.md` §7](architecture.md) |
| **E2-T02** | Wire spawn handler to predicate | M | Existing fallback genome / spawn logic uses E2-T01 instead of inline conditions. Existing 5 species keep current behavior. | E2-T01 |
| **E2-T03** | Habitat debug command | S | `/curiousbees habitat here` prints the predicate that would match the current biome. | E2-T01 |

### Subepic E2.B — Nest framework

| ID | Task | Size | Done when | Depends on |
|----|------|------|-----------|------------|
| **E2-T04** | Generalize `SpeciesBeeNestBlock` | M | Nest block reads species id + texture set from data, not from a hardcoded enum/`switch`. | [`architecture.md` §7](architecture.md), E1-T09 |
| **E2-T05** | Nest variant model | M | A nest can declare **visual variants** (logs, leaves, surface, hanging) without new Java classes per variant. | E2-T04 |
| **E2-T06** | POI / hive targeting consolidation | M | `BeeSpeciesHiveTargetHandler` documents and centralizes the rule: which nests a bee considers “home”; behavior verified for all 5 species by integration test or manual checklist. | ADR-0009 |
| **E2-T07** | Worldgen feature scaffolding | M | One reusable feature places a nest with attached vegetation; concrete feature configs live in **data**, not Java. | E2-T04 |

### Subepic E2.C — Population / observability

| ID | Task | Size | Done when | Depends on |
|----|------|------|-----------|------------|
| **E2-T08** | Bee population caps | S | Per-species or global cap configurable; default avoids bee soup. | — |
| **E2-T09** | Bee inspector overlay (creative) | S | F3-style line / debug screen showing species + analyzed flag for the bee under crosshair, dev/creative only. | E1-T07 |

**Epic exit:** dropping a new species into `data/` (later) reaches the world via existing handlers, nests, and feature configs — **no Java changes needed**.

---

## Epic E3 — Hive UX & automation

**Goal:** make the genetic apiary the clean automation interface; add **advanced hive** decisions and frames structure. Compatibility floor stays [`decisions.md` → ADR-0009](decisions.md).

### Subepic E3.A — GUI overhaul

| ID | Task | Size | Done when | Depends on |
|----|------|------|-----------|------------|
| **E3-T01** | Apiary menu data model | M | `GeneticApiaryMenu` exposes structured state: occupancy list, honey level, frame slots, output slots, error/state enum. No client-only secrets. | E0-T03 |
| **E3-T02** | Apiary screen redesign | M | `GeneticApiaryScreen` shows: bees inside (count + species when analyzed), frame slots, output slots, honey, status text. Dev placeholder background still allowed. | E3-T01 |
| **E3-T03** | Bee “inside” visual | M | Cheap representation (icon + species name when analyzed) rendered per occupant; full 3D render explicitly deferred to a follow-up if needed. | E3-T02, E1-T09 |
| **E3-T04** | Localized status messages | S | All apiary error/status strings are translation keys. | E3-T02 |

### Subepic E3.B — Frames as items

| ID | Task | Size | Done when | Depends on |
|----|------|------|-----------|------------|
| **E3-T05** | Frame item registration | M | At least 2 frame items registered (e.g. `productivity_frame`, `mutation_frame`); recipe stubs in data. | — |
| **E3-T06** | Frame slot capability | M | Apiary block entity exposes frame slots distinct from output slots; insert via menu or hopper to a marked side. | E3-T01 |
| **E3-T07** | Frame modifier wiring | M | `BuiltinFrameModifiers` (existing) is consumed by the apiary tick path; production resolver applies per-frame multipliers. | E3-T06, [`architecture.md` §6](architecture.md) |
| **E3-T08** | Frame durability | S | Frames consume durability per production tick or per output; broken frame disables its modifier and emits a warning state. | E3-T07 |

### Subepic E3.C — Automation contract

| ID | Task | Size | Done when | Depends on |
|----|------|------|-----------|------------|
| **E3-T09** | Sided IO definition | S | Documented (in code + short README) which sides allow insert (frames) vs extract (outputs); top retained for vanilla bee entry. | E3-T06 |
| **E3-T10** | Output `IItemHandler` extract-only | S | Hopper/pipe extract works; insert into output slot rejected. Test covers both directions. | E3-T06 |
| **E3-T11** | Optional redstone behavior | S | If kept, simple rule: redstone signal pauses production (not bee entry/exit). Otherwise explicitly skipped with note. | E3-T07 |

### Subepic E3.D — Advanced hive (gated by ADR)

| ID | Task | Size | Done when | Depends on |
|----|------|------|-----------|------------|
| **E3-T12** | Open ADR for hive footprint | M | New entry in [`decisions.md`](decisions.md) — chosen approach (single block / multiblock / hybrid) and rationale. | Open decision |
| **E3-T13** | Implement chosen footprint (slice) | L | First playable advanced hive matches the ADR; tests cover form/place/break + bee entry. | E3-T12 |
| **E3-T14** | Optional: bee transport item (gated) | M | Only if ADR “Bee transport” chooses to ship it; respects the hybrid model in [`project-guide.md`](project-guide.md). | Open ADR |

**Epic exit:** a player can run an automated hive line (frames in, combs out) with hoppers/pipes; advanced hive footprint decision is committed.

---

## Epic E4 — Processing line

**Goal:** combs become the input of a tech machine that produces standard ingredients.

### Subepic E4.A — Centrifuge core

| ID | Task | Size | Done when | Depends on |
|----|------|------|-----------|------------|
| **E4-T01** | Choose & ADR fluid honey question | M | New ADR decides: **fluid honey** (tank) vs **bottle-only**. Covers cross-mod fluid registration. | Open decision |
| **E4-T02** | Centrifuge block + block entity | M | New block placed, ticking, has menu; no recipes yet. | — |
| **E4-T03** | Centrifuge menu + screen | M | Slot layout: 1 input (comb), N outputs; progress bar synced. | E4-T02 |
| **E4-T04** | Recipe type & serializer | M | Custom recipe type for centrifuge (input item → outputs with weights). JSON-driven. | E4-T01 |
| **E4-T05** | Stub recipes for existing combs | S | Each currently-shipping `*_comb` has a recipe producing wax + honey + by-product placeholder. **No new species combs.** | E4-T04 |

### Subepic E4.B — Honey output

| ID | Task | Size | Done when | Depends on |
|----|------|------|-----------|------------|
| **E4-T06** | Discrete-honey path | S | Centrifuge can output `minecraft:honey_bottle` or a custom honey item until E4-T07 ships. | E4-T04 |
| **E4-T07** | Fluid honey path (if ADR=fluid) | M | Custom fluid registered, bucket interaction works, sided fluid handler on centrifuge. | E4-T01 |

### Subepic E4.C — Recipe ergonomics

| ID | Task | Size | Done when | Depends on |
|----|------|------|-----------|------------|
| **E4-T08** | Recipe data tests | S | Test loads each shipped recipe JSON and asserts schema validity. | E4-T05 |
| **E4-T09** | Optional JEI/REI plugin scaffolding | M | Plugin stub registers centrifuge category; **no detail polish** yet. Defer if loader story is unstable. | E4-T05 |

**Epic exit:** closed loop world → hive → comb → centrifuge → useful ingredients runs end-to-end with current species, no JEI required.

---

## Epic E5 — Identity polish (no new content)

**Goal:** “Curious Bees face” without expanding the species roster.

| ID | Task | Size | Done when | Depends on |
|----|------|------|-----------|------------|
| **E5-T01** | GUI background art pass | M | All Curious Bees GUIs (apiary, analyzer, centrifuge) use Curious Bees backgrounds; dev-placeholder strings removed. | E3-T02, E4-T03 |
| **E5-T02** | Item / block sprite pass | M | All shipping comb/frame/machine items have non-placeholder sprites; manifest updated. | — |
| **E5-T03** | Sound design pass | S | Apiary tick / centrifuge / analyzer have distinct (or distinct-enough) sounds; non-grating volumes. | E4-T03 |
| **E5-T04** | Onboarding text | S | First-time hint (chat or item tooltip on first analyzer use) explains the breed → analyze → hive loop in one sentence. | E1-T08 |
| **E5-T05** | Optional in-game guide stub | M | One pluggable guide (Patchouli or in-house) loads a hello page; **no full content** yet. | E5-T04 |

**Epic exit:** screenshots no longer say “PB clone”; structure ready for Phase “species expansion” to add roster.

---

## Epic E6 — Fabric & compatibility prep

**Goal:** keep the path open without spending effort that does not pay back yet.

| ID | Task | Size | Done when | Depends on |
|----|------|------|-----------|------------|
| **E6-T01** | Loader-agnostic audit of `common` | S | Report (issue or doc) lists any common-side leak that would block Fabric port. | [`decisions.md` → DR-010](decisions.md) |
| **E6-T02** | Recipe / data parity check | S | Recipe JSON paths and schemas readable by both loaders (no NeoForge-only fields where avoidable). | E4-T04 |
| **E6-T03** | Compatibility test world | S | A saved world (or seed + checklist) used to spot regressions across versions. | — |

**Epic exit:** when DR-010 is greenlit, the Fabric port has a documented gap list ≤ 1 page.

---

## Epic EX — Cross-cutting (data-driven, tests, observability)

These tasks are not phase-specific; they unblock multiple epics.

| ID | Task | Size | Done when | Depends on |
|----|------|------|-----------|------------|
| **EX-T01** | Data-driven cutover for species/products | L | Per [`decisions.md` → ADR-0010](decisions.md): species, traits, mutations, products loadable from JSON for the **already-shipping** content. | ADR-0010 |
| **EX-T02** | Datapack reload smoke test | S | `/reload` does not corrupt running bees or apiaries. Test or manual checklist. | EX-T01 |
| **EX-T03** | Telemetry-friendly debug overlay | S | One toggleable HUD piece showing: species count loaded, mutation count, last apiary tick result. | E2-T09 |
| **EX-T04** | Crash-resilience for missing assets | S | Missing texture / lang key produces a logged warning and visible placeholder, never a crash. | E1-T03 |
| **EX-T05** | Test pyramid documentation | S | Short paragraph in [`architecture.md` §9](architecture.md) (or here) describes: unit (`common`), integration (NeoForge), manual smoke. | — |
| **EX-T06** | Issue + PR templates | S | `.github/` (or equivalent) templates aligned with this file’s task IDs. | — |

---

## When to come back here

- Closing an epic → tick its **Epic exit**, archive done tasks (or mark `done`), and pick the next epic with no open dependencies.
- Open ADR resolved → update the row that referenced it (link to ADR, drop “gated” notes).
- New idea before species expansion → add a task in the right epic. **Do not** open a “new species” epic until E1–E5 exits are hit; that prevents content debt before structure is ready.

_Last updated: 2026-05-04._
