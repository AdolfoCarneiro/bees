# Curious Bees

Curious Bees is a Minecraft bee genetics mod for modern vanilla-style bees.

The project is inspired by the genetic depth of Forestry and the polished automation/UX expectations of modern bee mods, while keeping its own identity.

The goal is not to port Forestry and not to fork Productive Bees. The goal is to build a new mod where vanilla bees can carry genetic data, reproduce through Minecraft's natural breeding flow, mutate into new species, receive species-specific visuals, and gradually support a deeper tech-oriented bee progression.

## Current Status

The initial MVP foundation has been validated.

The project already proved the core loop:

```txt
living bee
-> genome
-> breeding
-> inheritance
-> mutation
-> analyzer
-> production
-> initial apiary/data-driven foundation
```

The project is now entering a post-MVP productization phase.

The current focus is not to add a large amount of content yet. The focus is to turn the validated MVP into a real playable mod with better UX, visuals, apiary interaction, analyzer interfaces, frames, and a sustainable content/asset pipeline.

```text
Current product direction is documented in docs/post-mvp/.
Implementation task breakdowns for AI-assisted coding live in docs/implementation/.
The original MVP planning docs are preserved under docs/mvp/.
```

## Target

Initial loader:

```txt
NeoForge 1.21.1
```

Future target:

```txt
Fabric
```

Development style:

```txt
AI-assisted coding with Claude Code, Codex, Cursor, and similar tools.
```

Main architectural rule:

```txt
The genetics core must remain pure Java and independent from Minecraft APIs.
```

## Product Direction

Curious Bees now aims for:

```txt
Productive Bees-like accessibility, automation quality, and modern UX
+
Forestry-inspired genetics, inheritance, mutation, hidden alleles, and lineage selection
+
living vanilla bees as the gameplay foundation
+
Curious Bees' own identity, progression, visuals, and content structure
```

## What This Mod Is

Curious Bees is:

- a new mod from scratch;
- a genetics-focused bee breeding mod;
- a vanilla-bee-based experience;
- a tech-leaning progression mod;
- a mod where mutation and inheritance are probabilistic;
- a mod where species should eventually have their own visual identity;
- a mod designed to support future expansion through data-driven content and assets.

## What This Mod Is Not

Curious Bees is not:

- a Forestry port;
- a Productive Bees fork;
- a deterministic `A + B = C` recipe system;
- a mod where bees are only item inputs for machines;
- a resource bee mod as its immediate next priority;
- a content-heavy mod before UX, visuals, and asset pipelines are ready.

## Current Post-MVP Priorities

The current phase focuses on productization:

1. Visual species system
   - species-specific textures;
   - fallback visuals;
   - optional custom models for special species later.

2. Analyzer UX
   - move analysis out of chat/debug output;
   - add real interfaces;
   - show genetic details only after a bee has been analyzed.

3. Genetic Apiary GUI
   - show bees present in the apiary;
   - show production status;
   - show frames;
   - show output inventory;
   - support automation by design.

4. Frames and Apiary Behavior
   - frames affect production, mutation, and stability;
   - apiary behavior can later support containment/simulation-style modifiers;
   - automation should not require a special automation upgrade.

5. Content and Asset Pipeline
   - species data should be paired with assets;
   - adding a species should have clear conventions for JSON/data, textures, lang entries, and optional models.

## Current Non-Goals

Do not implement these without an explicit planning/design task:

- lifecycle/death/larvae mechanics;
- temperature/humidity/environment simulation;
- resource bees;
- large species trees;
- Fabric gameplay parity;
- item-only bee systems that replace living bees;
- custom models for every species;
- complex research systems;
- large compatibility layers with other mods.

These are future topics and may be revisited after the post-MVP foundation is solid.

## Documentation

Start here:

```txt
docs/README.md
```

Current planning lives in:

```txt
docs/post-mvp/
```

Original MVP foundation documents live in:

```txt
docs/mvp/
```

The MVP docs remain important for architecture and genetics rules, but they are no longer the current product roadmap.

## Recommended Reading

For current planning:

```txt
docs/post-mvp/10-5-species-bee-nests-and-habitat-system.md  (foundational — read first)
docs/post-mvp/11-post-mvp-productization-roadmap.md
docs/post-mvp/12-visual-species-system.md
docs/post-mvp/13-analyzer-ux-and-progression.md
docs/post-mvp/14-genetic-apiary-gui-and-frames.md
docs/post-mvp/15-content-and-asset-pipeline.md
docs/post-mvp/11-6-custom-bee-entity-architecture.md    (deferred ADR)
```

For design context and reference:

```txt
docs/research/existing-bee-mods-review.md
```

For architecture and genetics foundation:

```txt
docs/mvp/02-technical-architecture.md
docs/mvp/03-genetics-system-spec.md
docs/mvp/04-breeding-and-mutation-spec.md
docs/mvp/05-content-design-spec.md
docs/mvp/06-ai-coding-guidelines.md
```

## Core Architecture Rule

The genetics core must not depend on:

```txt
Minecraft
NeoForge
Fabric
registries
events
entities
NBT
components
attachments
mixins
client rendering
```

Minecraft integration should call the core, not live inside the core.

## Development Guidance

Prefer small, testable implementation slices.

Good task:

```txt
Implement visual metadata for species definitions and fallback texture resolution.
```

Good task:

```txt
Implement the first analyzer screen using existing analyzer report data.
```

Bad task:

```txt
Add 40 new bees, resource production, custom models, apiary GUI, JEI integration, and Fabric support.
```

Keep the project growing in layers:

```txt
validated core
-> productized UX
-> visual identity
-> apiary/analyzer polish
-> asset/content pipeline
-> expanded content
-> resource bee progression
-> Fabric parity
```
