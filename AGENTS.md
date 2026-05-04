# AGENTS.md

Agent rules for this repo. **Also read [CLAUDE.md](CLAUDE.md).**

## Summary

Minecraft bee genetics mod (NeoForge 1.21.1): living bees, genomes, breeding, dominance, mutations. Product framing: root **Readme.md** + **CLAUDE.md**; rules and models: **`docs/architecture/`** (02–05).

## Hard rule

**`common/genetics` (and the genetics core)** must not depend on Minecraft, NeoForge, Fabric, registries, events, entities, NBT, components, attachments, mixins, item stacks, levels, or UI. Pure Java, unit-testable. Integration lives under `neoforge/` (and future `fabric/`).

## Documentation order

1. [Readme.md](Readme.md)  
2. [docs/ROADMAP.md](docs/ROADMAP.md) when scope touches priorities or phase order  
3. [docs/TASKS.md](docs/TASKS.md) when picking up the next concrete unit of work  
4. [docs/architecture/README.md](docs/architecture/README.md) then [02](docs/architecture/02-technical-architecture.md), [03](docs/architecture/03-genetics-system-spec.md), [04](docs/architecture/04-breeding-and-mutation-spec.md), [05](docs/architecture/05-content-design-spec.md) as the task requires  
5. [docs/decisions/](docs/decisions/) when changing an ADR-covered area  

**Task hints:** species hive / nest targeting behavior → `neoforge/src/main/java/com/curiousbees/neoforge/event/BeeSpeciesHiveTargetHandler.java`; new species checklist → `.claude/plugins/local/skills/new-bee-species.md`.

## Hybrid model

Living bees are the default world loop. **Scoped** item/container UX (transport, advanced hive) is allowed when a feature calls for it — not a global replacement for entities. Same intent as **Readme.md** / **CLAUDE.md**.

## Non-goals (without explicit design sign-off)

Resource bees; huge species trees in one drop; enforced lifecycle/death/larvae; climate simulation; Fabric gameplay parity; JEI/REI unless scoped; shipping final art as undeclared placeholders.

**Do not** replace the **entire** game loop with item-only bees.

## Allowed when scoped

Frames with real effects; production tuning; analyzer/apiary UX; automation-friendly inventories; datapack-style species content.

## Workflow

1. Read AGENTS.md + CLAUDE.md.  
2. Read relevant `docs/architecture/` specs and ADRs.  
3. Restate scope; list files; smallest complete change; tests for core Java where relevant; **commit** one focused task per commit when practical.

## Commits

Prefix examples: `core:`, `content:`, `neoforge:`, `client:`, `test:`, `docs:`, `build:`.

## Prompt template (short)

Read AGENTS + CLAUDE → relevant `docs/architecture/` → ADRs. No Minecraft inside `common/genetics`. New textures: follow maintainer process (see CLAUDE Art section); no silent placeholder finals.

## Assets

No silent placeholder-as-final. Dev fallback only if crash-proofing and clearly marked.

## Style and validation

Prefer small classes, explicit validation, `Objects.requireNonNull` at boundaries, deterministic tests, services not stuffed in event handlers. **Logger** in services: WARNING on bad/skip, FINE on trace; models throw, no logging.

## Packages

`common/genetics` pure; `neoforge/` integration and client; keep rendering/UI out of core.

## Review checklist

Aligned with **Readme.md** / guardrails here; genetics pure Java; no accidental resource-bee tree; analysis gating respected where applicable; no final placeholder art; automation not artificially paywalled; server/client sync story intact for UI-driving state.

## Growth line

Validated genetics core → polish and production loop (frames, products, processing, advanced hive UX) → species expansion → later resource progression only with its own design → Fabric when scoped.
