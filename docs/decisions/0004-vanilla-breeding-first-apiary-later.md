# ADR-0004 — Use Vanilla Bee Breeding as the First Gameplay Loop

## Status

Accepted

## Date

2026-04-28

## Context

Curious Bees is inspired by Forestry's genetics, but it should integrate with modern vanilla bees.

The first playable fantasy is:

```txt
Two vanilla bees are fed with flowers.
Minecraft creates a baby bee.
Curious Bees assigns a genome to the baby using inheritance and mutation.
```

A Forestry-like apiary is desirable later, but starting with a machine-based breeding loop would delay the vanilla bee fantasy and increase implementation scope.

## Decision

The first gameplay breeding loop will use vanilla bee breeding.

The project will implement:

- parent genome reading;
- child genome generation;
- mutation application;
- child genome storage;
- minimal mutation feedback.

A tech apiary is future scope and should be introduced only after the natural breeding loop works.

## Consequences

### Positive

- The mod immediately feels connected to vanilla bees.
- The first gameplay loop is easier for players to understand.
- The genetic system can be tested through familiar Minecraft interactions.
- Tech progression can later enhance the loop instead of replacing it.

### Negative

- Vanilla entity behavior may require careful event/hook research.
- Parent detection may depend on NeoForge hooks or mixins.
- Some Forestry concepts, such as queens and drones, are not used in the first loop.
- Fertility and lifespan may have limited gameplay effect until tech systems exist.

### Constraints

- Do not implement a full apiary before vanilla breeding works.
- Do not replace vanilla bees with a custom bee entity in the MVP.
- Do not make breeding deterministic like `A + B = C`.
- Missing parent genomes must be handled safely.

## Applies To

- NeoForge breeding hook research;
- baby bee genome assignment;
- mutation timing;
- analyzer MVP;
- future apiary design.

## Related Documents

- `docs/01-product-vision-and-roadmap.md`
- `docs/04-breeding-and-mutation-spec.md`
- `docs/implementation/04-vanilla-breeding-integration.md`
- `docs/implementation/07-tech-apiary-and-automation.md`
