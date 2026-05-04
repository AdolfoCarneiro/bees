# AGENTS.md

This file provides project guidance for Codex and other AI coding agents working in this repository.

## Project Summary

Curious Bees is a Minecraft bee genetics mod inspired by the genetic depth of Forestry and the polished automation/UX expectations of modern bee mods.

It is not a Forestry port, not a Productive Bees fork, and not a deterministic resource-bee recipe system.

The initial MVP proved the core loop:

> Living vanilla bees can carry genomes, breed through Minecraft's natural flow, inherit alleles through Mendelian rules, express active/inactive traits through dominance, and sometimes produce probabilistic mutations.

The project is now in a **post-MVP productization phase**.

Current goal:

> Turn the validated MVP into a real playable mod with species visuals, analyzer UI, genetic apiary GUI, useful frames, automation-ready apiary behavior, and a healthy content/asset pipeline.

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
docs/README.md
docs/post-mvp/11-post-mvp-productization-roadmap.md
docs/mvp/02-technical-architecture.md
docs/mvp/03-genetics-system-spec.md
```

### Read for visual species work

```text
docs/post-mvp/12-visual-species-system.md
docs/mvp/05-content-design-spec.md
docs/art/asset-prompt-workflow.md
```

### Read for species bee nests, habitat, and world gen

```text
docs/post-mvp/10-5-species-bee-nests-and-habitat-system.md
```

### Read for analyzer work

```text
docs/post-mvp/13-analyzer-ux-and-progression.md
docs/mvp/03-genetics-system-spec.md
docs/mvp/05-content-design-spec.md
```

### Read for apiary or frame work

```text
docs/post-mvp/14-genetic-apiary-gui-and-frames.md
docs/mvp/02-technical-architecture.md
docs/mvp/04-breeding-and-mutation-spec.md
```

### Read for content and asset pipeline work

```text
docs/post-mvp/15-content-and-asset-pipeline.md
docs/mvp/05-content-design-spec.md
```

### Read for genetics or breeding changes

```text
docs/mvp/02-technical-architecture.md
docs/mvp/03-genetics-system-spec.md
docs/mvp/04-breeding-and-mutation-spec.md
```

Agent workflow, guardrails, and logging conventions: `AGENTS.md` and `CLAUDE.md`.

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
docs/art/asset-prompt-workflow.md
```

Do not create placeholder textures as final deliverables. If an asset is required, create a prompt document under `docs/art/prompts/` instead.

### Read for releases

```text
docs/release/
```

Use only when preparing packaging, release notes, publishing, or public distribution.

## Source of Truth Priority

Use these sources in this order:

```text
1. AGENTS.md for agent behavior and guardrails.
2. docs/post-mvp/ for current product direction.
3. ADRs for architectural decisions.
4. docs/mvp/ for genetics/architecture foundation.
5. docs/quality/ for validation.
```

## Current Non-Goals

Do not implement these without an explicit planning/design task:

- resource bees (iron, gold, diamond, redstone, emerald, netherite, etc.);
- large species trees;
- lifecycle/death/larvae mechanics;
- temperature/humidity/environment simulation;
- full Fabric gameplay support;
- item-only bee systems that replace living bees;
- custom models for every species as a baseline requirement;
- complex research systems;
- large mod compatibility layers;
- JEI/REI integration unless specifically scoped;
- placeholder textures submitted as final assets — create a prompt doc instead.

## Current Post-MVP Scope

The project is in the productization phase.

Allowed when explicitly scoped:

```text
- visual metadata in species definitions;
- species-to-texture resolution;
- fallback texture strategy;
- renderer/client-side integration for species textures;
- analyzer UI (screen, progression, analyzed/unanalyzed state);
- genetic apiary GUI;
- frame behavior and frame slot integration;
- automation-ready apiary inventory behavior;
- content and asset pipeline tooling and conventions.
```

Not yet allowed:

```text
- resource bees;
- large species trees;
- lifecycle/death mechanics;
- temperature/humidity simulation;
- Fabric gameplay parity;
- advanced networking beyond what a scoped feature requires.
```

## Development Order

The initial MVP sequence (phases 0–10) has been completed.

The current post-MVP productization order is:

```text
11. Post-MVP foundation and documentation (complete)
12. Visual species system (complete)
13. Analyzer UX and progression (complete)
14. Genetic apiary GUI (complete)
15. Frames and apiary behavior (next)
16. Content and asset pipeline (complete)
17. First expanded species branch (only after 15 is solid)
```

Do not skip to species expansion before frame behavior is solid.

## Required Agent Workflow

For every task:

```text
1. Read AGENTS.md.
2. Read docs/post-mvp/ for the relevant phase.
3. Read related domain specs from docs/mvp/ as needed.
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
client:   client-side rendering or UI tasks
test:     test-only changes
docs:     documentation updates
build:    build configuration changes
```

## Prompt Template

Use this pattern for implementation:

```text
Read AGENTS.md first.

Then read:
- docs/post-mvp/[relevant phase doc]
- [relevant docs/mvp/ foundation specs if needed]
- [relevant ADRs if any]
- [relevant quality/test plan]

Focus only on:
[TASK NAME]

Do not implement unrelated features.
Do not implement future backlog items early.
Do not introduce Minecraft, NeoForge, or Fabric dependencies into common genetics code.
Do not add placeholder textures as final deliverables — create a prompt doc instead.

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

## Asset Rule

When implementation requires a new visual asset, do not create a placeholder as a final deliverable.

Instead:

1. Create a complete prompt document under `docs/art/prompts/`.
2. Include target path, size, style, palette, UV/model reference, usage, and acceptance criteria.
3. Wait for the user-provided asset before marking the visual task complete.
4. After the asset is provided, integrate it into the repo and validate all references.

A temporary fallback texture is allowed only for development safety so the game does not crash. It must be clearly marked as a dev placeholder.

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

## Defensive Coding and Logging

### Validation

Every public method that receives external input must validate defensively at the boundary:

```text
- Use Objects.requireNonNull for every reference parameter — never raw `if (x == null)`.
- Validate collection contents: if a List/Map is passed to a service, guard against null elements.
- Validate domain invariants early (e.g., allele ChromosomeType must match the chromosome slot).
- Model classes (Allele, GenePair, Genome) throw immediately on invalid state.
- Services log a WARNING before throwing or before skipping invalid inputs.
```

### Logging

Use `java.util.logging.Logger` in all service classes (not in model classes):

```java
private static final Logger LOGGER = Logger.getLogger(MyService.class.getName());
```

Log levels:

```text
WARNING  — unexpected but recoverable input: null list entries, incompatible genomes, unknown IDs.
           Always log before throwing or before silently skipping.
INFO     — significant game events visible to the player.
FINE     — internal operations useful for debugging: mutation applied, breeding completed.
           FINE is off by default in production; use it freely for traceability.
```

Rules:

```text
- Model classes (Allele, GenePair, Genome, MutationDefinition): do NOT log — just throw.
- Services (BreedingService, MutationService, and future services): log at WARNING for
  any defensive skip or unexpected state, and at FINE for normal operations.
- Platform integration (NeoForge events): log at WARNING for recoverable data issues
  such as a bee entity missing a genome.
- Never swallow an exception silently — always log before recovering or re-throwing.
```

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
  block/entity/menu/screen integration
  client rendering

fabric/
  future implementation
```

`common/genetics` must remain independent from Minecraft APIs.

Client rendering and UI code must stay in platform/client layers.

## Review Checklist

After every AI-generated implementation, check:

```text
- Did it follow the current post-MVP docs?
- Did it accidentally follow the old MVP roadmap as if it were current?
- Did it preserve common/genetics as pure Java?
- Did it keep platform-specific code outside the genetics core?
- Did it keep bees as living entities?
- Did it avoid resource bees?
- Did it avoid lifecycle/death/larvae mechanics?
- Did it avoid temperature/humidity simulation?
- Did it avoid inventing large species trees?
- Did it add tests for common/core logic where appropriate?
- Did it avoid hardcoding species behavior in random places?
- Did it respect analysis state before showing genetic details?
- Did it keep the apiary automation-ready without requiring an automation unlock?
- Did it keep visual species support extensible without requiring custom models for every bee?
- Did it avoid silently committing placeholder textures as final assets?
- If a new asset was needed, did it create a prompt under docs/art/prompts/ instead?
```

## Final Rule

The project should now grow like this:

```text
validated MVP core
-> post-MVP productization
-> visual identity
-> analyzer UX
-> apiary GUI
-> frames and apiary behavior
-> content and asset pipeline
-> small expanded species branches
-> later resource bee progression
-> future Fabric parity
```

Do not skip directly from MVP core to large content or resource bees.
