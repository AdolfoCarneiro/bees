# ADR-0005 — Use Placeholder Assets Before Polished Assets

## Status

Accepted

## Date

2026-04-28

## Context

Curious Bees will eventually need assets such as:

- item icons;
- comb textures;
- analyzer icon;
- block models;
- apiary model;
- centrifuge model;
- frame item textures;
- possible UI textures.

The user has Blockbench available and is considering future AI/MCP-assisted asset generation.

However, polished assets are not required for the first technical phases. The early phases are focused on docs, pure Java genetics, built-in content, NeoForge entity storage, breeding, and analyzer behavior.

## Decision

Polished assets are not part of the critical path.

Use no assets or simple placeholders until gameplay requires them.

Blockbench and potential MCP automation are future tooling options, mainly for custom block/machine models, not for the genetics core.

## Consequences

### Positive

- Development remains focused on core behavior.
- Lack of art does not block implementation.
- The mod can be validated with debug output and placeholder items.
- Blockbench work can happen after requirements are clearer.

### Negative

- Early builds may look rough.
- Some visual polish is deferred.
- Placeholder assets may need replacement later.

### Constraints

- Do not block Phase 1 or Phase 2 on assets.
- Do not block NeoForge entity storage or breeding integration on assets.
- Analyzer MVP may use a simple item icon or placeholder.
- Production MVP may use placeholder comb textures.
- Blockbench is only required for custom blocks/machines, especially in the tech apiary phase.

## Applies To

- Analyzer item;
- comb items;
- production outputs;
- tech apiary block;
- centrifuge block;
- future machine models.

## Related Documents

- `docs/implementation/00-phase-0-documentation-and-decisions.md`
- `docs/implementation/05-analyzer-implementation.md`
- `docs/implementation/06-production-mvp.md`
- `docs/implementation/07-tech-apiary-and-automation.md`
