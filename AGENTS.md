# AGENTS.md

Agent rules for this repo. **Also read [CLAUDE.md](CLAUDE.md).**

## Summary

Minecraft bee genetics mod (NeoForge 1.21.1): living bees, genomes, breeding, dominance, mutations. Product framing: root **Readme.md** + **CLAUDE.md** + **[docs/project-guide.md](docs/project-guide.md)**.

## Hard rule

**`common/genetics` (and the genetics core)** must not depend on Minecraft, NeoForge, Fabric, registries, events, entities, NBT, components, attachments, mixins, item stacks, levels, or UI. Pure Java, unit-testable. Integration lives under `neoforge/` (and future `fabric/`). ([`docs/decisions.md` → ADR-0002](docs/decisions.md).)

## Documentation order

The repo uses **only seven** docs files. Do not add new top-level markdown files under `docs/`.

1. [Readme.md](Readme.md)
2. [docs/project-guide.md](docs/project-guide.md) — entry point + doc index
3. [docs/requirements.md](docs/requirements.md) — must do / must not do
4. [docs/architecture.md](docs/architecture.md) — modules, genetics, breeding, content
5. [docs/roadmap.md](docs/roadmap.md) — phased plan
6. [docs/TASKS.md](docs/TASKS.md) — epics + tasks
7. [docs/decisions.md](docs/decisions.md) — ADR log
8. [docs/asset-generation-guidelines.md](docs/asset-generation-guidelines.md) — art rules

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
2. Read the relevant section of `docs/architecture.md` and any `docs/requirements.md` rules that apply.
3. Pick a task from `docs/TASKS.md`; consult `docs/roadmap.md` if phase order is unclear; consult `docs/decisions.md` for any locked choice your work touches.
4. Restate scope; list files; smallest complete change; tests for core Java where relevant; **commit** one focused task per commit when practical.

## Commits

Prefix examples: `core:`, `content:`, `neoforge:`, `client:`, `test:`, `docs:`, `build:`.

## Prompt template (short)

Read AGENTS + CLAUDE → relevant `docs/architecture.md` section → `docs/requirements.md` for guardrails → `docs/decisions.md` for locked choices. No Minecraft inside `common/genetics`. New textures: follow [`docs/asset-generation-guidelines.md`](docs/asset-generation-guidelines.md); no silent placeholder finals.

## Assets

No silent placeholder-as-final. Dev fallback only if crash-proofing and clearly marked. Full rules: [`docs/asset-generation-guidelines.md`](docs/asset-generation-guidelines.md).

## Style and validation

Prefer small classes, explicit validation, `Objects.requireNonNull` at boundaries, deterministic tests, services not stuffed in event handlers. **Logger** in services: WARNING on bad/skip, FINE on trace; models throw, no logging.

## Packages

`common/genetics` pure; `neoforge/` integration and client; keep rendering/UI out of core.

## Review checklist

Aligned with **Readme.md** / `docs/requirements.md`; genetics pure Java; no accidental resource-bee tree; analysis gating respected where applicable; no final placeholder art; automation not artificially paywalled; server/client sync story intact for UI-driving state.

## Growth line

Validated genetics core → polish and production loop (frames, products, processing, advanced hive UX) → species expansion → later resource progression only with its own design → Fabric when scoped. Phases: [`docs/roadmap.md`](docs/roadmap.md).
