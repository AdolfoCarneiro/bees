# ADR-0001 — Use NeoForge 1.21.1 as the Initial Loader

## Status

Accepted

## Date

2026-04-28

## Context

Curious Bees is a new Minecraft mod focused on bringing Forestry-inspired bee genetics to modern vanilla-style bees.

The project needs one initial loader target to avoid splitting implementation effort too early. Supporting multiple loaders from the start would increase project complexity before the core gameplay loop is proven.

The user wants the initial implementation to target NeoForge 1.21.1, with possible Fabric support later.

## Decision

The first implementation target is:

```txt
NeoForge 1.21.1
```

Fabric support is a future goal, but it is not part of the first MVP implementation.

## Consequences

### Positive

- The project can focus on one loader while proving the gameplay.
- NeoForge-specific APIs can be researched and implemented first.
- The first MVP has a clearer technical target.
- Debugging entity storage, events, items, and commands is simpler with one platform.

### Negative

- Fabric users are not supported in the first release.
- Some NeoForge-specific choices may need adapters later.
- If code is not carefully separated, future Fabric support may become harder.

### Constraints

- Common genetics code must not import NeoForge APIs.
- NeoForge integration must stay in the NeoForge/platform layer.
- Any Fabric-related work should be treated as future work until the NeoForge MVP is stable.

## Applies To

- Project setup
- Build structure
- Entity genome storage
- Vanilla bee breeding integration
- Analyzer item
- Debug commands
- Release planning

## Related Documents

- `docs/mvp/01-product-vision-and-roadmap.md`
- `docs/architecture/02-technical-architecture.md`
- `docs/implementation/03-neoforge-entity-integration.md`
- `docs/implementation/10-fabric-support-implementation.md`
