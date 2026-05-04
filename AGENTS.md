# AGENTS.md

Agent rules for this repo. **Also read [CLAUDE.md](CLAUDE.md).**

## Summary

Minecraft bee genetics mod (NeoForge 1.21.1): living bees, genomes, breeding, dominance, mutations — Curious Bees identity and goals in **`docs/post-mvp/gameplay-direction.md`**.

## Hard rule

**`common/genetics` (and the genetics core)** must not depend on Minecraft, NeoForge, Fabric, registries, events, entities, NBT, components, attachments, mixins, item stacks, levels, or UI. Pure Java, unit-testable. Integration lives under `neoforge/` (and future `fabric/`).

## Documentation order

1. [docs/README.md](docs/README.md)  
2. [docs/post-mvp/gameplay-direction.md](docs/post-mvp/gameplay-direction.md)  
3. [docs/ROADMAP.md](docs/ROADMAP.md)  
4. [docs/architecture/02-technical-architecture.md](docs/architecture/02-technical-architecture.md), [03](docs/architecture/03-genetics-system-spec.md), [04](docs/architecture/04-breeding-and-mutation-spec.md), [05](docs/architecture/05-content-design-spec.md) as the task requires  
5. [docs/decisions/](docs/decisions/) when changing an ADR-covered area  
6. [docs/art/asset-prompt-workflow.md](docs/art/asset-prompt-workflow.md) for any new asset  

**Task hints:** nests/world reference → `docs/reference/bee-nest-targeting-behavior.md`; new species checklist → `.claude/plugins/local/skills/new-bee-species.md`.

## Hybrid model

Living bees are the default world loop. **Scoped** item/container UX (transport, advanced hive) is allowed when a feature calls for it — not a global replacement for entities. Details: `gameplay-direction.md`.

## Non-goals (without explicit design sign-off)

Resource bees; huge species trees in one drop; enforced lifecycle/death/larvae; climate simulation; Fabric gameplay parity; JEI/REI unless scoped; shipping final art as undeclared placeholders.

**Do not** replace the **entire** game loop with item-only bees.

## Allowed when scoped

Frames with real effects; production tuning; analyzer/apiary UX; automation-friendly inventories; datapack-style species content; prompts and pipelines under `docs/art/`.

## Workflow

1. Read AGENTS.md + CLAUDE.md.  
2. Read `gameplay-direction.md` / `ROADMAP.md` if direction is involved.  
3. Read relevant `docs/architecture/` specs and ADRs.  
4. Restate scope; list files; smallest complete change; tests for core Java where relevant; **commit** one focused task per commit when practical.

## Commits

Prefix examples: `core:`, `content:`, `neoforge:`, `client:`, `test:`, `docs:`, `build:`.

## Prompt template (short)

Read AGENTS + CLAUDE → `gameplay-direction.md` if needed → relevant `docs/architecture/` → ADRs → quality doc if behavior changes. No Minecraft inside `common/genetics`. New textures → prompt doc first.

## Assets

No silent placeholder-as-final. Prompt under `docs/art/prompts/`; dev fallback only if crash-proofing and clearly marked.

## Style and validation

Prefer small classes, explicit validation, `Objects.requireNonNull` at boundaries, deterministic tests, services not stuffed in event handlers. **Logger** in services: WARNING on bad/skip, FINE on trace; models throw, no logging.

## Packages

`common/genetics` pure; `neoforge/` integration and client; keep rendering/UI out of core.

## Review checklist

Follows `gameplay-direction` / `ROADMAP`; genetics pure Java; no accidental resource-bee tree; analysis gating respected where applicable; no final placeholder art; automation not artificially paywalled; server/client sync story intact for UI-driving state.

## Growth line

Validated genetics core → polish and production loop (frames, products, processing, advanced hive UX) → species expansion → later resource progression only with its own design → Fabric when scoped.
