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
Product direction: this file + CLAUDE.md + AGENTS.md + docs/project-guide.md
Technical specs (genetics, architecture, content rules): docs/architecture.md
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

## Priorities

Ordered roadmap: **`docs/roadmap.md`** (operational tasks: **`docs/TASKS.md`**). Day-to-day work can still live in **issues**; UX and parity goals are summarized above (product direction, non-goals).

## Current Non-Goals

Do not implement these without an explicit planning/design task:

- lifecycle/death/larvae mechanics;
- temperature/humidity/environment simulation;
- resource bees;
- large species trees;
- Fabric gameplay parity;
- replacing the entire gameplay loop with item-only bees (scoped item/container UX is allowed — see CLAUDE.md hybrid model);
- custom models for every species;
- complex research systems;
- large compatibility layers with other mods.

These are future topics and may be revisited after the post-MVP foundation is solid.

## Documentation

The repo uses **only seven** docs files (intentional). Start here:

```txt
Readme.md
CLAUDE.md
AGENTS.md
```

Then go to whichever doc answers your question:

```txt
docs/project-guide.md             — entry point + doc index
docs/requirements.md              — what the mod must / must not do
docs/architecture.md              — modules, genetics, breeding, content
docs/roadmap.md                   — phased plan
docs/TASKS.md                     — epics + concrete tasks
docs/decisions.md                 — locked ADRs
docs/asset-generation-guidelines.md — art rules
```

Older per-file specs (architecture/02–05, individual ADRs, research notes) live in **git history**.

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
