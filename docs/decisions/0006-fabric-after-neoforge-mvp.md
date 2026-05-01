# ADR-0006 — Add Fabric Support After the NeoForge MVP

## Status

Accepted

## Date

2026-04-28

## Context

The user wants Fabric support if possible, but the initial loader target is NeoForge 1.21.1.

Supporting Fabric too early would require solving two platform integrations before the core gameplay loop is proven. Fabric may require mixins or different APIs for entity data storage, breeding hooks, item components, and events.

## Decision

Fabric support is future work.

The project should design for Fabric compatibility by keeping common logic platform-independent, but it should not implement Fabric until the NeoForge MVP works.

## Consequences

### Positive

- Initial implementation remains focused.
- Common code can still be structured to support Fabric later.
- The team can learn from the NeoForge implementation before porting.
- Fabric becomes a platform port, not a separate gameplay implementation.

### Negative

- Fabric players cannot use the first MVP.
- Some NeoForge decisions may need adaptation later.
- Build structure may need refactoring when Fabric support starts.

### Constraints

- Common genetics logic must not import NeoForge.
- NeoForge event handlers should be thin and call common services.
- Any platform-specific concepts should be isolated behind adapters where reasonable.
- Fabric work should start with a feasibility spike and parity checklist.

## Applies To

- common module boundaries;
- platform adapters;
- build layout;
- entity genome storage;
- breeding hook implementation;
- analyzer item;
- production integration.

## Related Documents

- `docs/mvp/02-technical-architecture.md`
- `docs/implementation/03-neoforge-entity-integration.md`
- `docs/implementation/04-vanilla-breeding-integration.md`
- `docs/implementation/10-fabric-support-implementation.md`
