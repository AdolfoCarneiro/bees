# CLAUDE.md

This file provides project guidance for Claude Code and other AI coding agents working in this repository.

## Project Summary

This project is a new Minecraft bee genetics mod inspired by the genetics depth of Forestry 1.12.2, applied to modern vanilla-style Minecraft bees.

It is not a Forestry port, not a Productive Bees fork, and not intended to start as a deterministic resource-bee mod.

The first playable goal is:

> Two vanilla bees breed in the world using the normal Minecraft flower interaction, and the baby bee receives a genome generated through Mendelian inheritance, dominance rules, and probabilistic mutation.

## Target Platform

Initial target:

- NeoForge 1.21.1

Future target:

- Fabric support

Architecture must be designed so that a Fabric implementation can be added later without rewriting the genetics core.

## Important Rule

The genetics core must not depend on Minecraft, NeoForge, Fabric, registries, events, entities, NBT, components, attachments, or mixins.

The genetics core should be pure Java and unit-testable.

Minecraft integration should only call the core.

## Read These Docs Before Coding

Before implementing anything, read:

1. `docs/01-product-vision-and-roadmap.md`
2. `docs/02-technical-architecture.md`
3. `docs/03-genetics-system-spec.md`
4. `docs/04-breeding-and-mutation-spec.md`
5. `docs/06-ai-coding-guidelines.md`

If the task is about content, also read:

- `docs/05-content-design-spec.md`

If the task is about planning, also read:

- `docs/07-initial-backlog.md`

## Non-Goals for the Initial MVP

Do not implement these in the initial core phase:

- Resource bees such as iron, gold, diamond, redstone, etc.
- Fabric support
- Full GUI screens
- Advanced apiaries
- Automation
- Create, Mekanism, Thermal, Farmer's Delight, or other mod compatibility
- Complex JSON/datapack content loading before the core rules are stable
- Forestry-compatible items or systems
- Productive Bees-style deterministic recipes

## Initial MVP Scope

The first MVP should include:

- Pure Java genetics core
- A small hardcoded content set
- Vanilla bee genome storage in NeoForge
- Vanilla-style bee breeding with inherited genetics
- Basic mutation
- Basic analyzer feedback
- A small set of initial species:
  - Meadow
  - Forest
  - Arid
  - Cultivated
  - Hardy

## Coding Style

Prefer small, testable changes.

Do not implement multiple layers at once.

Good first task:

> Implement the pure Java genetics core and its unit tests.

Bad first task:

> Implement the whole mod with blocks, items, UI, mutations, hives, Fabric support, and resource bees.

## Package Boundaries

Recommended conceptual modules:

```txt
common/
  genetics/
  content/
  gameplay/
  platform/

neoforge/
  data attachment integration
  event integration
  registry integration
  item/component integration

fabric/
  future implementation
```

The `common/genetics` package must remain independent from Minecraft APIs.

## AI Agent Behavior

When working on this project:

1. Read the relevant documentation first.
2. Restate the task and scope.
3. Avoid changing unrelated files.
4. Add tests for core behavior.
5. Do not invent new species or mechanics unless the task asks for them.
6. Prefer simple implementation over abstraction-heavy code.
7. Keep future Fabric support in mind, but do not implement Fabric prematurely.
8. Ask for clarification only when a decision would meaningfully change the design.
9. When uncertain about Minecraft/NeoForge/Fabric APIs, isolate assumptions behind a platform adapter.

## First Recommended Prompt

Use this prompt to start implementation:

```txt
Read CLAUDE.md and the docs folder.

Implement only the pure Java genetics core. Do not use Minecraft, NeoForge, Fabric, NBT, components, entities, blocks, items, registries, or events.

Create:
- Allele
- Dominance
- GenePair
- Genome
- ChromosomeType
- BreedingService
- MutationService initial version

Add unit tests for:
- Mendelian inheritance
- Dominance resolution
- Active/inactive allele persistence
- Hybrid and purebred detection
- Mutation with 0% chance
- Mutation with 100% chance
- Approximate distribution over many simulated crosses

Do not implement Minecraft integration yet.
```
