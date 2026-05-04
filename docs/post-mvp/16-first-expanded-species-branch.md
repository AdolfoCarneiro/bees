# 16 — First Expanded Species Branch (Cultivated Progression)

> Status: Design document — approved for implementation.
>
> This document defines the first post-MVP content expansion: the Cultivated Progression Branch.
> It satisfies the design document requirement from Phase 17 of the Productization Roadmap.
>
> Prerequisites met before this document was authored:
> - Phase 12 (visual species system) ✅
> - Phase 13 (analyzer UX) ✅
> - Phase 14 (genetic apiary GUI) ✅
> - Phase 15 (frames and apiary behavior) ✅
> - Phase 16 (content and asset pipeline) ✅

## 1. Purpose

The five MVP species (Meadow, Forest, Arid, Cultivated, Hardy) proved the genetic engine.

This phase adds the first real progression branch — species that reward deliberate selective breeding
and give players genetic goals beyond the starter set.

The branch is grounded in the two advanced MVP species: **Cultivated** and **Hardy**. Both are
mutation-only (no natural hive). Players who reached them are ready for the next step.

## 2. Branch Identity

The Cultivated Progression Branch represents **managed genetic refinement** — bees selectively
developed through human-guided breeding rather than wild natural selection.

```
Wild natural bees       →  Meadow / Forest / Arid
Mutation-accessible     →  Cultivated / Hardy
Selective breeding      →  Resilient / Diligent     (this phase)
Advanced refinement     →  Noble / Industrious       (future phase)
```

This branch does not require lifecycle mechanics, resource bee outputs, or temperature simulation.
It rewards the player for understanding and selecting genetic traits.

## 3. Species Defined In This Phase

### 3.1 Resilient Bee

**Origin:** Cultivated + Hardy → Resilient

**Identity:** The hardy mutation of cultivated genetics. A bee that survives and produces
steadily even in suboptimal conditions. Represents durability over peak performance.

**Role in progression:**
- First step past the Cultivated/Hardy starter pair
- Rewards Hardy-line breeding
- Foundation species for the Noble line (future)

**Default traits:**

| Chromosome | Active | Inactive | Notes |
|---|---|---|---|
| LIFESPAN | LONG | LONG | key differentiator — sustained over cycles |
| PRODUCTIVITY | NORMAL | NORMAL | reliable, not spectacular |
| FERTILITY | TWO | TWO | standard |
| FLOWER_TYPE | FLOWERS | FLOWERS | vanilla-friendly |

**Dominance:** DOMINANT (can be stabilized as purebred)

**Mutation chance:** 10% base, PARTIAL result mode

**Production:** Resilient Comb (`curiousbees:resilient_comb`)
- Represents durable genetic material; future use in stability-related crafting
- Primary output chance: 0.60

**Visual direction:**
- Muted amber body with grey-blue accents — the colours of durability and endurance
- Less bright than Meadow/Cultivated; slightly rougher texture feel
- Palette: `#9E7A3A` (body), `#5A6E7A` (stripes), `#C8A060` (highlight)

---

### 3.2 Diligent Bee

**Origin:** Cultivated + Meadow → Diligent

**Identity:** The productivity mutation of cultivated genetics. A tireless worker that produces
more combs per cycle. Represents efficiency and output over endurance.

**Role in progression:**
- First step past the Cultivated/Meadow pair
- Rewards Meadow-line breeding of Cultivated
- Foundation species for the Noble line (future)

**Default traits:**

| Chromosome | Active | Inactive | Notes |
|---|---|---|---|
| LIFESPAN | NORMAL | NORMAL | standard longevity |
| PRODUCTIVITY | FAST | FAST | key differentiator — more output per cycle |
| FERTILITY | TWO | TWO | standard |
| FLOWER_TYPE | FLOWERS | FLOWERS | vanilla-friendly |

**Dominance:** DOMINANT

**Mutation chance:** 10% base, PARTIAL result mode

**Production:** Diligent Comb (`curiousbees:diligent_comb`)
- Represents refined production efficiency; future use in advanced frame crafting
- Primary output chance: 0.70 (higher than Resilient — it's a productivity-focused bee)

**Visual direction:**
- Bright golden yellow — energetic, clean, highly visible
- Sharper stripe contrast than Meadow; cleaner than wild bees
- Palette: `#F0C030` (body), `#2A1800` (stripes), `#FFF0A0` (highlight)

---

## 4. Future Branch (Design Only — Not This Phase)

The Noble and Industrious species are reserved for a future expansion:

```
Resilient + Diligent → Noble       (endgame genetics — durability × productivity)
Diligent + Noble     → Industrious (maximum output, requires genetic mastery)
```

These are documented here for context but not implemented in this phase.

## 5. Implementation Scope

### 5.1 In scope

```
- BuiltinBeeSpecies entries: RESILIENT, DILIGENT
- BuiltinBeeMutations entries: RESILIENT mutation, DILIGENT mutation
- BuiltinProductionDefinitions entries: resilient_comb, diligent_comb production
- New item registrations: resilient_comb, diligent_comb
- Visual metadata on each species definition
- Asset prompts for each species texture + comb item texture
- Lang entries for species and combs
- Item model JSON for each comb
- Tests for new species/mutations/production
```

### 5.2 Out of scope

```
- Noble and Industrious species (future phase)
- Natural hive blocks for Resilient/Diligent (mutation-only species by design)
- Lifecycle mechanics
- Resource bee outputs
- Temperature/humidity requirements
- Custom bee models
```

## 6. Mutation Balance Notes

| Mutation | Parents | Result | Chance | Notes |
|---|---|---|---|---|
| Resilient | Cultivated + Hardy | Resilient | 10% | Hardy is RECESSIVE, so pairs are less common |
| Diligent | Cultivated + Meadow | Diligent | 10% | Meadow is abundant; slightly easier to reach |

Both mutations use PARTIAL result mode (one allele mutates, preserving hybrid genetic value).

10% is in line with Hardy's mutation chance (8%) and slightly below Cultivated (12%). These are
second-tier mutations — harder to reach than first-tier but not gated behind rare conditions.

## 7. Visual Asset Requirements

| Asset | Size | Path | Prompt |
|---|---|---|---|
| Resilient Bee texture | 64×32 | `textures/entity/bee/resilient.png` | `docs/art/prompts/species/textures-entity-bee-resilient.md` |
| Diligent Bee texture | 64×32 | `textures/entity/bee/diligent.png` | `docs/art/prompts/species/textures-entity-bee-diligent.md` |
| Resilient Comb icon | 16×16 | `textures/item/resilient_comb.png` | `docs/art/prompts/items/resilient-comb.md` |
| Diligent Comb icon | 16×16 | `textures/item/diligent_comb.png` | `docs/art/prompts/items/diligent-comb.md` |

All four assets must follow conventions in `docs/content/asset-authoring-guide.md`.
Entity textures require the UV template at `docs/art/templates/bee/default_bee_uv_template.png`.

## 8. Success Criteria

This phase is successful when:

```
- A player can breed Cultivated + Hardy and occasionally obtain Resilient offspring.
- A player can breed Cultivated + Meadow and occasionally obtain Diligent offspring.
- Resilient and Diligent bees are visually distinct from each other and from all MVP species.
- The Genetic Apiary produces the correct combs for each new species.
- The Bee Analyzer correctly identifies Resilient and Diligent bees after analysis.
- No lifecycle, resource bee, or climate mechanics were introduced.
- Adding Noble and Industrious later requires only the species definition checklist.
```

## 9. AI Agent Guidance

```
Scope:
- Add Resilient and Diligent species definitions and mutations to common.
- Add production definitions.
- Register comb items in NeoForge.
- Create asset prompts (do not generate final textures).
- Add tests.

Do not:
- Add Noble or Industrious (those are future).
- Add natural hive blocks for Resilient or Diligent (mutation-only by design).
- Add resource bees.
- Add lifecycle mechanics.
- Copy assets from other mods.
```
