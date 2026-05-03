# CLAUDE.md

This file provides project guidance for Claude Code and other AI coding agents working in this repository.

## Project Summary

Curious Bees is a Minecraft bee genetics mod inspired by the genetic depth of Forestry and the polished automation/UX expectations of modern bee mods.

It is not a Forestry port, not a Productive Bees fork, and not a deterministic resource-bee recipe system.

The first MVP proved the core idea:

> Living vanilla bees can carry genomes, breed through Minecraft's natural flow, inherit alleles through Mendelian rules, express active/inactive traits through dominance, and sometimes produce probabilistic mutations.

The project is now in a post-MVP productization phase.

Current goal:

> Turn the validated MVP into a real playable mod with species visuals, analyzer UI, genetic apiary GUI, useful frames, automation-ready apiary behavior, and a healthy content/asset pipeline.

## Target Platform

Initial target:

```txt
NeoForge 1.21.1
```

Future target:

```txt
Fabric
```

Architecture must still be designed so a Fabric implementation can be added later without rewriting the genetics core.

## Most Important Architecture Rule

The genetics core must not depend on Minecraft, NeoForge, Fabric, registries, events, entities, NBT, components, attachments, mixins, rendering, or client UI.

The genetics core must remain pure Java and unit-testable.

Minecraft integration should only call the core.

## Documentation Structure

Current planning lives in:

```txt
docs/post-mvp/
```

The original MVP foundation lives in:

```txt
docs/mvp/
```

The MVP documents are still important, but they are historical/foundation documents. Do not treat the MVP roadmap as the current roadmap.

Start with:

```txt
docs/README.md
```

## Required Reading Before Coding

Always read:

1. docs/README.md
2. docs/post-mvp/11-post-mvp-productization-roadmap.md
3. docs/mvp/02-technical-architecture.md
4. docs/mvp/03-genetics-system-spec.md
5. the relevant docs/post-mvp spec
6. the matching docs/implementation implementation plan

For general post-MVP planning, read:

```txt
docs/post-mvp/11-post-mvp-productization-roadmap.md
```

For visual/species work, read:

```txt
docs/post-mvp/12-visual-species-system.md
docs/mvp/05-content-design-spec.md
docs/art/asset-prompt-workflow.md
```

For analyzer work, read:

```txt
docs/post-mvp/13-analyzer-ux-and-progression.md
docs/mvp/03-genetics-system-spec.md
docs/mvp/05-content-design-spec.md
```

For apiary, frames, or production work, read:

```txt
docs/post-mvp/14-genetic-apiary-gui-and-frames.md
docs/mvp/02-technical-architecture.md
docs/mvp/04-breeding-and-mutation-spec.md
```

For content/data/asset pipeline work, read:

```txt
docs/post-mvp/15-content-and-asset-pipeline.md
docs/mvp/05-content-design-spec.md
```

For core genetics or breeding changes, read:

```txt
docs/mvp/02-technical-architecture.md
docs/mvp/03-genetics-system-spec.md
docs/mvp/04-breeding-and-mutation-spec.md
docs/mvp/06-ai-coding-guidelines.md
```

Task-specific additions:

```text
Visual species work:
- docs/post-mvp/12-visual-species-system.md
- docs/implementation/12-visual-species-system-implementation.md

Analyzer work:
- docs/post-mvp/13-analyzer-ux-and-progression.md
- docs/implementation/13-analyzer-ux-implementation.md

Apiary/frames work:
- docs/post-mvp/14-genetic-apiary-gui-and-frames.md
- docs/implementation/14-genetic-apiary-gui-and-frames-implementation.md

Content/assets work:
- docs/post-mvp/15-content-and-asset-pipeline.md
- docs/implementation/15-content-and-asset-pipeline-implementation.md
```

Current guardrails:

```text
Do not implement without explicit request:
- resource bees;
- large species trees;
- lifecycle/death/larvae mechanics;
- temperature/humidity/environment simulation;
- Fabric parity;
- custom model per species as a baseline;
- itemized bee systems that replace living bees;
- Automation Upgrade as a gate for automation;
- complex research systems;
- mod compatibility trees.
```

## Current Product Direction

Curious Bees should combine:

```txt
Productive Bees-like accessibility, automation quality, and modern UX
+
Forestry-inspired genetics, hidden alleles, probabilistic mutation, and lineage selection
+
living vanilla bees as the gameplay foundation
+
Curious Bees' own visual identity and progression
```

## Current Post-MVP Priorities

The current phase is productization.

Focus on:

- visual identity per species;
- analyzer UI instead of chat/debug-only output;
- genetic apiary GUI;
- real frame behavior;
- apiary behavior that is automation-ready by design;
- content and asset pipeline for future species expansion.

Do not jump directly to large content expansion.

## Current Design Decisions

### Bees Remain Living Entities

Bees should remain living Minecraft entities as the central fantasy.

Do not replace the core gameplay with item-only bees or machine-only bee recipes.

The apiary may show bees in a GUI, but that does not mean bees become simple items.

### Apiary Uses Living Bees

The apiary should work with living bees.

Future containment/simulation-style behavior may keep bees operationally inside the apiary, but the design should still treat them as living bees with genomes.

### Apiary Is Automation-Ready By Design

Do not create a required "automation upgrade" just to allow basic automation.

The apiary should expose sensible input/output behavior from the start where technically feasible.

Future upgrades may affect capacity, containment, filtering, observation, or simulation behavior, but automation itself should not be artificially locked behind an upgrade.

### Genetic Data Requires Analysis

A bee may have a complete genome internally, but the player should not see full genetic details until that bee has been analyzed.

Tooltips, apiary UI, and analyzer views should respect analysis state.

Initial rule:

```txt
Unanalyzed bee:
- limited or unknown genetic information

Analyzed bee:
- active/inactive species
- purity/hybrid status
- visible traits
- dominance/active/inactive markers where appropriate
```

### Visual Species System

Use a hybrid visual approach:

Current/default:

```txt
- shared base bee model
- species-specific texture/skin
```

Future/special species:

```txt
- optional custom model
- optional special renderer behavior
```

Do not require every species to have a custom Blockbench model.

### No Lifecycle For Now

Do not implement bee lifecycle, death, larvae, old-age behavior, queens, incubators, or succession mechanics unless explicitly requested by a future planning task.

Lifespan may remain as a trait, but should not currently force bees to die or expire.

### No Advanced Environment Simulation For Now

Do not implement temperature, humidity, climate, environmental status, complex biome rules, or weather requirements unless explicitly requested.

Simple biome/spawn/mutation metadata may exist where already supported, but do not expand this into a full environment simulation yet.

### Resource Bees Are Future Content

Do not add iron, gold, diamond, redstone, emerald, netherite, uranium, or similar resource bees in the current productization phase.

Resource bees belong to a later content expansion phase after:

- visual species system exists;
- analyzer UX is real;
- apiary GUI is usable;
- frames are meaningful;
- content and asset pipeline is ready;
- a dedicated resource-bee progression design exists.

## Asset Generation Rule

When implementation requires a new visual asset — species texture, GUI background, item icon, block texture, or model texture — do not create a placeholder as a final deliverable.

Instead:

1. Create a complete prompt document under `docs/art/prompts/`.
2. Include target path, size, style, palette, UV/model reference, usage, and acceptance criteria.
3. Wait for the user-provided asset before marking the visual task complete.
4. After the asset is provided, integrate it into the repo and validate all references.

A temporary fallback texture is allowed only for development safety so the game does not crash. It must be clearly marked as a dev placeholder and must not count as a completed asset.

For the full workflow and prompt template, read:

```text
docs/art/asset-prompt-workflow.md
```

## Current Non-Goals

Do not implement without explicit request:

- resource bees;
- large species trees;
- lifecycle/death/larvae mechanics;
- temperature/humidity/environment simulation;
- full Fabric gameplay support;
- complex research systems;
- item-only bee systems that replace living bees;
- custom model for every species;
- broad mod compatibility layers;
- JEI/REI integration unless specifically scoped;
- advanced networking beyond what the scoped feature requires;
- final placeholder textures for species, items, blocks, or GUIs — if an asset is required, generate a prompt document instead.

## Package Boundaries

Conceptual modules:

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
  block/entity/menu/screen integration
  client rendering

fabric/
  future implementation
```

Rules:

- `common/genetics` must be pure Java.
- `common/genetics` must not import Minecraft, NeoForge, or Fabric classes.
- Platform event handlers should be thin.
- Gameplay logic should live in common services where possible.
- Client rendering and UI should stay in platform/client layers.
- Content definitions should remain centralized.
- Do not scatter species-specific `if` checks across unrelated systems.

## AI Agent Workflow

Every coding task should follow this pattern:

```txt
1. Read relevant docs.
2. Restate the task scope.
3. Identify files to change.
4. Implement the smallest useful slice.
5. Add or update tests where appropriate.
6. Run tests or explain why they cannot run.
7. Summarize changes.
8. List follow-up tasks.
```

Prefer small, testable tasks.

Good examples:

```txt
Implement visual metadata fields for species definitions and add fallback validation.
```

```txt
Create the initial analyzer screen using the existing analyzer report model.
```

```txt
Add apiary menu/container data for output slots, frame slots, progress, and analyzed bee summary.
```

Bad examples:

```txt
Implement all post-MVP features.
```

```txt
Add 40 new bees, custom models, resource production, apiary GUI, analyzer block, and Fabric support.
```

```txt
Rewrite the genetics core while adding UI.
```

## Review Checklist

After any AI-generated implementation, check:

```txt
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
- Did it keep automation-ready apiary behavior rather than adding an automation unlock?
- Did it keep visual species support extensible without requiring custom models for every bee?
- Did it avoid silently committing placeholder textures as final assets?
- If a new asset was needed, did it create a prompt under docs/art/prompts/ instead?
```

## Suggested Post-MVP Implementation Order

Recommended order:

```txt
1. Documentation reorganization and post-MVP roadmap
2. Visual species metadata and fallback strategy
3. Species-specific texture resolution for living bees
4. Analyzer analysis-state model
5. Portable analyzer UI
6. Analyzer block design/implementation
7. Genetic apiary GUI
8. Frame behavior 2.0
9. Apiary containment/simulation design if still desired
10. Content and asset pipeline documentation/tooling
11. First small expanded species branch
12. Later: dedicated resource bee progression design
```

## Final Rule

The project should now grow like this:

```txt
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
