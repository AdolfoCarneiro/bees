# ADR-0007 — Keep Resource Bees Out of the MVP

## Status

Accepted

## Date

2026-04-28

## Context

Many modern bee mods focus on bees that produce resources such as iron, copper, redstone, diamond, emerald, or netherite.

Curious Bees may eventually support resource bees, but the initial project goal is to prove the genetic loop:

```txt
wild bees -> inherited genomes -> mutations -> hybrids -> purebreds -> analyzer -> basic production
```

Adding resource bees too early would distract from genetic depth and risk turning the mod into a deterministic output mod before the breeding system is interesting.

## Decision

Resource bees are not part of the MVP.

The initial species are limited to:

```txt
Meadow
Forest
Arid
Cultivated
Hardy
```

Resource bees may be designed later after the genetic system, breeding loop, analyzer, and basic production are working.

## Consequences

### Positive

- The MVP stays focused.
- The design proves genetics before content scale.
- Early balance remains manageable.
- The mod is less likely to become a Productive Bees clone.

### Negative

- Early gameplay has fewer high-value rewards.
- Some players may expect resource bees and not see them at first.
- Future resource bee naming and balance decisions remain open.

### Constraints

- Do not add iron, copper, gold, diamond, redstone, emerald, netherite, uranium, or similar bees in early implementation.
- Do not build content trees around resources before the MVP works.
- Basic production may include honey, wax, combs, resin-like byproducts, or hardy materials.

## Applies To

- content design;
- species naming;
- mutation tree planning;
- production MVP;
- expanded content roadmap.

## Related Documents

- `docs/mvp/01-product-vision-and-roadmap.md`
- `docs/mvp/05-content-design-spec.md`
- `docs/implementation/02-initial-content-implementation.md`
- `docs/implementation/09-expanded-content-roadmap.md`
