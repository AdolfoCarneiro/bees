# ADR-0002 — Keep the Genetics Core Pure Java

## Status

Accepted

## Date

2026-04-28

## Context

The genetics system is the core feature of Curious Bees.

It must support:

- alleles;
- dominance;
- gene pairs;
- active/inactive allele resolution;
- genomes;
- Mendelian inheritance;
- mutation rules;
- purebred/hybrid detection;
- deterministic unit tests.

This logic is complex enough that it must be tested outside Minecraft. If it depends on Minecraft, NeoForge, Fabric, registries, entities, NBT, components, or event hooks, it will become harder to test, port, and reason about.

## Decision

The genetics core must be implemented as pure Java.

It must not depend on:

```txt
Minecraft classes
NeoForge APIs
Fabric APIs
entities
events
registries
NBT
data components
attachments
mixins
client rendering
item/block classes
```

Minecraft and loader-specific code may call the genetics core, but the genetics core must not know about Minecraft.

## Consequences

### Positive

- Genetics can be tested with fast unit tests.
- AI coding agents can implement and review core rules more safely.
- Fabric support remains possible later.
- Breeding and mutation behavior can be simulated without launching the game.
- Bugs in inheritance and mutation can be isolated from Minecraft integration bugs.

### Negative

- Platform adapters are required to connect the core to real Bee entities.
- Some Minecraft concepts must be represented through simplified context objects.
- Serialization needs careful boundary design.

### Constraints

The common genetics module may include:

```txt
Allele
Dominance
ChromosomeType
GenePair
Genome
BreedingService
BreedingResult
MutationDefinition
MutationService
MutationResult
GeneticRandom
```

It must not include NeoForge/Fabric event logic, registry calls, item registration, block registration, GUI code, or direct Bee entity access.

## Applies To

- `common/genetics`
- `common/content`
- `common/gameplay`
- unit tests
- simulation tests
- future Fabric support

## Related Documents

- `docs/mvp/02-technical-architecture.md`
- `docs/mvp/03-genetics-system-spec.md`
- `docs/mvp/04-breeding-and-mutation-spec.md`
- `docs/implementation/01-genetics-core-implementation.md`
