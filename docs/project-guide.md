# Project Guide

Entry point for the **Curious Bees** repository. Read this first; then go to whichever of the six sibling docs answers your question:

| Doc | Use when |
|-----|----------|
| [`requirements.md`](requirements.md) | You need to know **what the mod must do** (and must not do). |
| [`architecture.md`](architecture.md) | You need module boundaries, genetics model, breeding/mutation, content data shape. |
| [`roadmap.md`](roadmap.md) | You want the **phased plan** and exit gates. |
| [`TASKS.md`](TASKS.md) | You want the **next concrete unit of work** (epics + tasks). |
| [`decisions.md`](decisions.md) | You want the **why** behind a locked choice (ADR log). |
| [`asset-generation-guidelines.md`](asset-generation-guidelines.md) | You are about to create or commit any visual asset. |

Root files at the repo top — **`Readme.md`** (GitHub landing), **`CLAUDE.md`** and **`AGENTS.md`** (AI agent rules) — are tooling conventions; they point back into this `docs/` set.

---

## What Curious Bees is

Curious Bees is a Minecraft **bee genetics** mod for NeoForge 1.21.1. Its identity: **living vanilla bees** carry **genomes**, breed through **vanilla flow**, with **Mendelian inheritance**, **dominance**, **mutations**, **species visuals**, and a **hive / processing line** that aims for Productive-Bees-grade UX clarity — without being a fork.

Inspirations:

- **Forestry** — genetic depth (alleles, dominance, mutations, hidden traits, lineage work).
- **Productive Bees** — hive UX clarity, automation hygiene, frame/upgrade language.
- **Vanilla bees** — the world fantasy stays. Bees are entities, not items in cages.

What it **is not**: a Forestry port; a Productive Bees fork; a deterministic `A + B → C` recipe mod; an item-only-bee mod; a resource-bee mod (yet).

---

## Hybrid bee model

- **Default:** bees are **living entities** in the world (breeding, nests, analyzer used on bee).
- **Allowed:** item or container representation **only where a feature explicitly needs it** (transport into an advanced hive, etc.). Do not silently turn the whole mod into item-only bees.
- This rule is non-negotiable until a new ADR overrides it ([`decisions.md` → ADR-0009](decisions.md)).

---

## Non-negotiable architecture rule

The **genetics core** (`common/genetics`) must **not** import Minecraft, NeoForge, Fabric, registries, events, entities, NBT, UI, mixins, attachments, or any loader-specific class. It stays **pure Java** and unit-testable. Platform code calls the core; the core never references game types. See [`architecture.md`](architecture.md) and [`decisions.md` → ADR-0002](decisions.md).

---

## Workflow (every task)

1. Read this file + [`requirements.md`](requirements.md) + the relevant section of [`architecture.md`](architecture.md).  
2. Pick a task from [`TASKS.md`](TASKS.md). If a phase-level question is unclear, check [`roadmap.md`](roadmap.md).  
3. If your work touches a locked decision, read it in [`decisions.md`](decisions.md). If you are about to **change** a locked decision, add or amend an ADR there in the same change.  
4. Restate scope, list files, implement the smallest complete slice, add tests where the area has tests, commit with a clear message (`docs:`, `core:`, `neoforge:`, `client:`, `test:`, `build:`).

## Logging

Use `java.util.logging.Logger` in services: **WARNING** before skip/throw on bad input, **FINE** for trace. Models in `common/genetics` throw, never log.

## Review quick-check

- `common/genetics` stays Minecraft-free.
- Analysis gating respected (do not leak full genome in UI/tooltips before analysis).
- No new placeholder-as-final art (see [`asset-generation-guidelines.md`](asset-generation-guidelines.md)).
- No accidental resource-bee tree (see [`decisions.md` → ADR-0007 / 0012](decisions.md)).
- Server state that drives client UI/sync: keep network story consistent.

## Doc maintenance rule

This repo deliberately uses **only these seven files** under `docs/`. Do not scatter new markdown across new folders. If something genuinely doesn't fit, add a section to the closest existing file or open a discussion.

_Last updated: 2026-05-04._
