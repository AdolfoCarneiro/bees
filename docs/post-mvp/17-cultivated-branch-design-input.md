> **Status:** Design input para Phase 17 — First Expanded Species Branch.
> Não implementado. Consulte `docs/post-mvp/11-post-mvp-productization-roadmap.md` (Phase 17) antes de executar.

# 09B - First Post-MVP Mutation Branch

## Status

Complete (as design input only; not implemented in-game).

## Scope

This document was migrated from `docs/implementation/09b-first-post-mvp-mutation-branch.md`. It originally completed Task 9.3 from the MVP expanded content roadmap (removed from the repo as historical).

It drafts one small post-MVP branch for future implementation.

This is planning only.

## Non-Goals

Do not implement:

- new species;
- new JSON content files;
- new mutations;
- new comb items;
- new assets;
- resource bees;
- biome/environment requirements;
- Fabric support.

## Branch Choice

Chosen first branch:

```text
Cultivated / Managed Branch
```

Why this branch first:

- it builds directly from the MVP species `Cultivated` and `Hardy`;
- it reinforces the genetics and stabilization loop;
- it does not require new environment-condition systems;
- it pairs naturally with the existing analyzer, production, and Genetic Apiary;
- it creates a bridge toward later tech bees without jumping to resources.

## Design Goals

The first post-MVP branch should teach:

- selectively breeding from existing MVP species;
- using productive and resilient traits together;
- preserving hybrids for mutation potential;
- stabilizing a managed lineage;
- preparing for future tech/resource foundation content without adding it yet.

## Proposed Species

This branch contains four candidate species.

Names are placeholders until implementation review.

```text
Resilient
Diligent
Noble
Industrious
```

## Species Roles

### Resilient Bee

Role:

```text
Bridge species between Hardy and Cultivated.
Represents a managed bee that can tolerate rougher conditions.
```

Parent concept:

```text
Cultivated + Hardy -> Resilient
```

Trait tendency:

```text
Longer lifespan
Normal productivity
Stable fertility
Flowers or cactus tolerance as a carried trait
```

Progression purpose:

```text
Combines the early domestication branch with the harsh-environment branch.
```

Suggested mutation chance:

```text
10%
```

Reason to exist:

```text
Creates a durable managed baseline before harsher biome or tech progression.
```

### Diligent Bee

Role:

```text
Early work-focused managed species.
Introduces stronger productivity identity without becoming an automatic upgrade.
```

Parent concept:

```text
Cultivated + Meadow -> Diligent
```

Trait tendency:

```text
Fast productivity
Normal lifespan
Two fertility
Flowers
```

Progression purpose:

```text
Rewards revisiting starter genetics after creating Cultivated.
```

Suggested mutation chance:

```text
12%
```

Reason to exist:

```text
Gives players a practical managed-production target before advanced machinery.
```

### Noble Bee

Role:

```text
Stabilization milestone.
Represents selective breeding success rather than raw output.
```

Parent concept:

```text
Resilient + Diligent -> Noble
```

Trait tendency:

```text
Normal or long lifespan
Normal or fast productivity
Two fertility
Flowers
```

Progression purpose:

```text
Acts as a genetic stepping stone for later managed or high-tier branches.
```

Suggested mutation chance:

```text
8%
```

Reason to exist:

```text
Encourages players to combine and stabilize two post-MVP lineages.
```

### Industrious Bee

Role:

```text
Late branch capstone for the first managed line.
Represents efficient production and apiary compatibility.
```

Parent concept:

```text
Diligent + Noble -> Industrious
```

Trait tendency:

```text
Fast productivity
Normal lifespan
Two or three fertility
Flowers
```

Progression purpose:

```text
Prepares the design space for later tech bees and resource foundation bees.
```

Suggested mutation chance:

```text
6%
```

Reason to exist:

```text
Provides an aspirational post-MVP managed bee without producing direct resources.
```

## Branch Shape

Conceptual branch:

```text
Cultivated + Hardy
  -> Resilient

Cultivated + Meadow
  -> Diligent

Resilient + Diligent
  -> Noble

Diligent + Noble
  -> Industrious
```

This is intentionally small. It creates a managed progression path with four new species, not a broad content tree.

## Production Identity

Production should remain modest.

Possible outputs:

```text
Resilient Comb
Diligent Comb
Noble Comb
Industrious Comb
Honeycomb as secondary output
Wax or frame-related byproducts later
```

Do not add direct resource outputs in this branch.

Production identity should emphasize:

- better honey/wax-style output;
- apiary/frame materials later;
- managed beekeeping progression;
- hybrid secondary production when supported.

## Implementation Dependencies

Before implementing this branch, verify:

- data-driven content loading is stable in-game;
- example external datapack loading has been manually tested;
- analyzer output remains readable with more species;
- production outputs for new combs have placeholder assets or a placeholder policy;
- mutation chances are simulated enough to avoid frustrating players.

## Acceptance Criteria For Future Implementation

When this branch is eventually implemented:

- no more than these four species are added in the first slice;
- each species has a data definition;
- each mutation has tests or validation coverage;
- production definitions are present but conservative;
- no resource bees are added;
- no advanced environment systems are required;
- built-in MVP species remain unchanged;
- player-facing analyzer output remains understandable.

## Gate 3 - First Branch Review

Current assessment:

```text
Gate 3 passed for planning.
```

Reason:

- one small branch is drafted;
- each species has a role;
- mutations are sparse and purposeful;
- no resource bees are included;
- implementation remains blocked until explicit approval.

## Follow-Up Tasks

1. Task 9.4 - Resource Bee Readiness Decision.
2. Task 9.5 - Asset Roadmap for Expanded Content.
3. Manual in-game validation of datapack loading before implementing this branch.
4. Future implementation spec for the managed branch, if approved.
