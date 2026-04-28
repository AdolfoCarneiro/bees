# CLAUDE.md

This file provides project guidance for Claude Code and other AI coding agents working in this repository.

## Project Summary

Curious Bees is a new Minecraft bee genetics mod inspired by the genetic depth of Forestry, applied to modern vanilla-style Minecraft bees.

It is not a Forestry port, not a Productive Bees fork, and not intended to start as a deterministic resource-bee mod.

The first playable goal is:

> Two vanilla bees breed in the world using the normal Minecraft flower interaction, and the baby bee receives a genome generated through Mendelian inheritance, dominance rules, and probabilistic mutation.

## Target Platform

Initial target:

```text
NeoForge 1.21.1
```

Future target:

```text
Fabric
```

Architecture must be designed so that a Fabric implementation can be added later without rewriting the genetics core.

## Most Important Rule

The genetics core must not depend on Minecraft, NeoForge, Fabric, registries, events, entities, NBT, components, attachments, mixins, item stacks, levels, or game classes.

The genetics core should be pure Java and unit-testable.

Minecraft integration should call the core. The core must not know about Minecraft entities.

## Documentation Sources

### Always read before coding

```text
docs/01-product-vision-and-roadmap.md
docs/02-technical-architecture.md
docs/06-ai-coding-guidelines.md
```

### Read for genetics tasks

```text
docs/03-genetics-system-spec.md
docs/04-breeding-and-mutation-spec.md
docs/implementation/01-genetics-core-implementation.md
```

### Read for content tasks

```text
docs/05-content-design-spec.md
docs/implementation/02-initial-content-implementation.md
```

### Read for NeoForge entity data tasks

```text
docs/implementation/03-neoforge-entity-integration.md
```

### Read for vanilla breeding integration tasks

```text
docs/implementation/04-vanilla-breeding-integration.md
```

### Read for analyzer tasks

```text
docs/implementation/05-analyzer-implementation.md
```

### Read for production tasks

```text
docs/implementation/06-production-mvp.md
```

### Read for tech apiary tasks

```text
docs/implementation/07-tech-apiary-and-automation.md
```

### Read for data-driven content tasks

```text
docs/implementation/08-data-driven-content.md
```

### Read for expanded content planning

```text
docs/implementation/09-expanded-content-roadmap.md
```

### Read for Fabric support

```text
docs/implementation/10-fabric-support-implementation.md
```

### Read for architectural decisions

```text
docs/decisions/
```

If a task touches an architectural decision, read the relevant ADR before coding.

### Read for validation

```text
docs/quality/
```

If a task changes behavior, read the relevant test plan or validation checklist.

### Read for assets

```text
docs/art/
```

Use placeholder assets until gameplay requires polish. Do not block core systems on Blockbench or polished art.

### Read for releases

```text
docs/release/
```

Use only when preparing packaging, release notes, publishing, or public distribution.

## Source of Truth Priority

Use these sources in this order:

```text
1. CLAUDE.md for agent behavior and guardrails.
2. ADRs for architectural decisions.
3. docs/implementation/*.md for execution details.
4. Core specs for domain rules.
5. Quality docs for validation.
6. Backlog/Notion for status and planning.
```

If a backlog task is short, do not assume it contains the full implementation detail. Find the relevant implementation spec.

## Non-Goals for the Initial MVP

Do not implement these during the initial core/MVP phases unless explicitly requested:

- resource bees such as iron, gold, diamond, redstone, emerald, netherite, etc.;
- Fabric support;
- full GUI screens;
- advanced apiaries;
- automation systems;
- Create, Mekanism, Thermal, Farmer's Delight, Botania, Ars Nouveau, or other mod compatibility;
- complex JSON/datapack loading before the core rules are stable;
- Forestry-compatible items or systems;
- Productive Bees-style deterministic recipes;
- polished assets as a blocker for core systems;
- Blockbench/MCP automation as a dependency for MVP.

## Initial MVP Scope

The first MVP should include:

- pure Java genetics core;
- small hardcoded content set;
- vanilla bee genome storage in NeoForge;
- vanilla-style bee breeding with inherited genetics;
- basic mutation;
- basic analyzer feedback;
- basic production identity;
- five initial species:
  - Meadow
  - Forest
  - Arid
  - Cultivated
  - Hardy

## Development Order

Follow this order unless explicitly told otherwise:

```text
0. Documentation and decisions
1. Pure genetics core
2. Initial built-in content
3. NeoForge bee genome storage
4. Vanilla breeding integration
5. Analyzer MVP
6. Production MVP
7. Tech apiary and automation
8. Data-driven content
9. Expanded content roadmap
10. Fabric support
```

Do not invert this order without a documented reason.

## Task Execution Rule

Work on one small task at a time.

A good task is usually:

```text
- one model class plus tests;
- one service plus tests;
- one platform integration hook;
- one item with simple behavior;
- one narrow refactor.
```

A risky task is:

```text
- genetics + NeoForge + UI in one request;
- common + NeoForge + Fabric in one request;
- blocks + GUI + networking + content in one request;
- adding future systems early.
```

## Required Agent Workflow

For every task:

```text
1. Read CLAUDE.md.
2. Read the relevant implementation spec.
3. Read related domain specs and ADRs.
4. Restate the task scope.
5. List files expected to be created or modified.
6. List assumptions and risks.
7. Implement the smallest complete version.
8. Add or update tests when applicable.
9. Run tests or explain why they cannot run.
10. Commit the changes with a small, focused commit message.
11. Summarize changes.
12. List follow-up tasks if any.
```

## Commit Rule

After every completed task, create a small and focused git commit before moving to the next task.

A good commit:

```text
- covers exactly one task;
- has a clear message following the pattern: scope: short description;
- stages only files relevant to that task;
- does not bundle multiple tasks in one commit.
```

Suggested message prefixes:

```text
core:     genetics core tasks
content:  content definition tasks
neoforge: NeoForge integration tasks
test:     test-only changes
docs:     documentation updates
build:    build configuration changes
```

## Prompt Template

Use this pattern for implementation:

```text
Read CLAUDE.md first.

Then read:
- [relevant implementation spec]
- [relevant domain spec]
- [relevant ADRs if any]
- [relevant quality/test plan]

Focus only on:
[TASK NAME]

Do not implement unrelated features.
Do not implement future backlog items early.
Do not introduce Minecraft, NeoForge, or Fabric dependencies into common genetics code.

Before coding:
1. summarize your understanding;
2. list the files you expect to create or modify;
3. list assumptions and risks.

Then implement the smallest complete version.

After coding:
1. run or describe tests;
2. summarize what changed;
3. list follow-up tasks;
4. mention any deviation from the docs/specs.
```

## First Recommended Implementation Prompt

Do not ask for the whole core at once unless intentionally doing a prototype.

Recommended first implementation prompt:

```text
Read CLAUDE.md.
Read docs/implementation/01-genetics-core-implementation.md.
Read docs/03-genetics-system-spec.md.

Focus only on Task 1 from the Phase 1 implementation spec:
Create genetics core package structure.

Do not implement alleles, genomes, breeding, mutation, Minecraft integration, NeoForge integration, Fabric support, items, blocks or UI.

Before coding, summarize your understanding and list the files/directories you expect to create or modify.
```

Then proceed task by task through `docs/implementation/01-genetics-core-implementation.md`.

## Coding Style

Prefer:

- small classes with clear responsibilities;
- immutable or safely encapsulated domain objects;
- explicit validation;
- deterministic tests;
- dependency injection for randomness;
- services outside event handlers;
- simple names over clever names.

Avoid:

- hidden global random state in core logic;
- game/platform imports in common genetics;
- raw unstructured data scattered everywhere;
- species-specific `if` checks across many services;
- implementation inside event handlers;
- premature abstractions for future content that does not exist yet.

## Package Boundaries

Recommended conceptual modules:

```text
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
  commands

fabric/
  future implementation
```

`common/genetics` must remain independent from Minecraft APIs.

## Asset Rule

Assets are not part of the critical path until Analyzer MVP / Production MVP.

Use placeholders when needed.

Blockbench is useful later for custom blocks and machines, especially Tech Apiary. Blockbench/MCP automation is optional future tooling and must not block the MVP.

## Review Checklist

After every AI-generated implementation, check:

```text
- Did it follow the relevant implementation spec?
- Did it implement only the requested scope?
- Did it add tests where appropriate?
- Did it avoid platform imports in common genetics?
- Did it avoid inventing mechanics?
- Did it avoid future systems?
- Did it keep content centralized?
- Did it preserve future Fabric support?
- Is the code explainable and testable?
```

## Final Rule

The project should grow like this:

```text
Correct core -> small content -> NeoForge storage -> breeding -> analyzer -> production -> tech -> JSON -> expanded content -> Fabric
```

Do not skip the foundation.
