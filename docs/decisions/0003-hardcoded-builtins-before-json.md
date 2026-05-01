# ADR-0003 — Start with Hardcoded Built-In Content Before JSON/Data-Driven Loading

## Status

Accepted

## Date

2026-04-28

## Context

Curious Bees should eventually support data-driven species, mutations, traits, and production definitions.

However, implementing JSON/datapack loading too early would increase complexity before the genetic rules are stable.

The first MVP only needs a very small content set:

```txt
Meadow
Forest
Arid
Cultivated
Hardy
```

Initial mutation rules:

```txt
Meadow + Forest -> Cultivated
Forest + Arid -> Hardy
```

Initial trait categories:

```txt
Lifespan
Productivity
Fertility
FlowerType
```

## Decision

Initial content will be hardcoded in centralized built-in definition classes.

The project should use clean definition models that can later be loaded from JSON, but the first implementation does not need JSON/datapack loading.

Recommended built-in structure:

```txt
BuiltinBeeSpecies
BuiltinBeeTraits
BuiltinBeeMutations
BuiltinBeeProducts
BuiltinBeeContent
```

## Consequences

### Positive

- The genetics and gameplay loop can be implemented faster.
- The content model can evolve while rules are still changing.
- AI agents are less likely to overbuild a complex data loading system.
- The MVP remains small and testable.

### Negative

- Adding new content initially requires code changes.
- Content creators cannot extend the mod through datapacks in the first version.
- A migration step will be required later.

### Constraints

- Do not scatter species-specific conditionals across services.
- Do not hardcode species behavior inside event handlers.
- Keep built-in definitions centralized.
- Use stable IDs suitable for future serialization.

## Applies To

- initial species definitions;
- trait alleles;
- mutation definitions;
- production definitions;
- default genome factories;
- test fixtures.

## Related Documents

- `docs/mvp/05-content-design-spec.md`
- `docs/implementation/02-initial-content-implementation.md`
- `docs/implementation/08-data-driven-content.md`
