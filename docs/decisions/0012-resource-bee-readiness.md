# ADR-0012 - Resource Bee Readiness

## Status

Accepted

## Date

2026-04-29

## Context

Curious Bees may eventually include bees that produce resource-adjacent or direct resource outputs.

However, the project's identity is genetic breeding first:

```text
wild bees -> inherited genomes -> mutation -> hybrid value -> purebred stabilization -> analyzer -> production -> tech support
```

Resource bees are attractive content, but adding them too early risks turning Curious Bees into a deterministic resource bee mod before the genetic loop has enough depth.

ADR-0007 already keeps resource bees out of the MVP. Phase 9 needs a stronger decision about what must be true before resource bees are allowed at all.

## Decision

Resource bees are not allowed until all readiness criteria in this ADR are satisfied.

This does not block planning language, naming strategy, or conceptual categories. It blocks implementation of actual resource bee species, mutations, outputs, assets, and recipes.

## Hard Prerequisites

Before implementing resource bees, the project must have:

```text
1. Stable genetics inheritance and mutation behavior.
2. Stable data-driven content loading.
3. Analyzer output that can explain species, traits, hybrid status, and relevant requirements.
4. Basic production that is tested and balanced enough for non-resource species.
5. At least one non-resource post-MVP branch implemented or approved for implementation.
6. A resource foundation tier or equivalent intermediate progression.
7. A documented economy/balance model for resource outputs.
8. A config or future config strategy for enabling, disabling, and scaling resource production.
9. A manual playtest checklist for resource progression.
10. Placeholder asset strategy for new combs/items.
```

If any prerequisite is missing, resource bees remain blocked.

## Design Constraints

Resource bees must:

- be earned through genetics, not given as starter content;
- require lineage work and purification;
- be probabilistic, not deterministic recipes;
- preserve hybrid usefulness;
- have low or constrained base production;
- depend on production, processing, environment, tech, or frame constraints where appropriate;
- be understandable through analyzer, guide text, or tooltips;
- be optional or configurable for modpacks later.

Resource bees must not:

- replace mining in early gameplay;
- become the first meaningful reward in the mod;
- encourage adding dozens of species in one implementation slice;
- make wild, biome, managed, or foundation bees irrelevant;
- hard-depend on external mod APIs in common genetics;
- bypass the existing content validation pipeline.

## Required Progression Shape

Resource content should follow this rough ladder:

```text
Wild / Early MVP
  -> Managed or Biome Progression
    -> Harsh Environment or Tech Progression
      -> Resource Foundation Bees
        -> Resource Bees
          -> Industrial / Compatibility / Endgame Bees
```

Do not jump directly from:

```text
Meadow + Forest -> Iron Bee
```

That shape is too deterministic and too resource-first.

## Balance Principles

Resource bee balance should consider:

- low base production chances;
- comb processing requirements;
- mutation rarity;
- difficult lineage requirements;
- purification work;
- tech/apiary investment;
- frame or environment modifiers;
- output scaling over time rather than immediate abundance;
- modpack economy differences.

Direct resource outputs should be conservative by default.

## Naming Direction

Use ADR-0011 naming strategy:

```text
Thematic species names with clear player-facing explanation.
```

Examples:

```text
Ferric - iron association
Cupric - copper association
Aureate - gold association
Resonant - redstone association
Crystalline - diamond association
```

Direct names like `Iron Bee` are allowed only if clarity is judged more important than identity for a specific case.

## Implementation Scope Rule

The first resource bee implementation slice, when eventually approved, must be small.

Recommended maximum:

```text
1 resource foundation species
1 to 2 resource bees
1 to 2 mutations
minimal production definitions
placeholder assets only
tests for validation/conversion/production behavior
```

Do not implement a full material tree in the first slice.

## Consequences

### Positive

- Keeps Curious Bees genetics-first.
- Prevents resource content from overwhelming early design.
- Creates clear review criteria before economy-impacting content.
- Gives future AI agents firm guardrails.
- Preserves data-driven content discipline.

### Negative

- Delays high-demand resource bee content.
- Requires more planning before obvious resource outputs are added.
- May disappoint players expecting immediate resource bees.

### Constraints

- No resource bee species may be implemented until readiness criteria are met.
- No direct iron, copper, gold, redstone, diamond, emerald, netherite, or uranium bee should be added early.
- Any exception requires a new ADR that supersedes this decision.

## Applies To

- Phase 9 expanded content planning;
- future content implementation specs;
- resource foundation bees;
- direct resource bees;
- production balancing;
- analyzer/guide explanations;
- data-driven content packs.

## Related Documents

- `docs/decisions/0007-no-resource-bees-in-mvp.md`
- `docs/decisions/0011-expanded-content-naming-strategy.md`
- `docs/implementation/09-expanded-content-roadmap.md`
- `docs/implementation/09a-expanded-content-categories.md`
- `docs/implementation/09b-first-post-mvp-mutation-branch.md`
- `docs/mvp/05-content-design-spec.md`
