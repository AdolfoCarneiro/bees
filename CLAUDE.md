# CLAUDE.md

Guidance for AI coding agents in this repository.

## What this mod is

Curious Bees is a NeoForge bee **genetics** mod: living bees carry genomes, breed through vanilla flow, Mendelian inheritance, dominance, and mutations — with species visuals and UX polish as ongoing goals. High-level product framing lives in **[Readme.md](Readme.md)** and in **[docs/project-guide.md](docs/project-guide.md)**; the rest of the contract lives in the seven docs below.

Not a Forestry port, not a Productive Bees fork, not a deterministic resource-bee recipe mod.

## Platform

- **Now:** NeoForge 1.21.1
- **Later:** Fabric gameplay is a non-goal until explicitly scoped; keep boundaries so `common/genetics` stays portable.

## Non-negotiable architecture

The **genetics core** (`common/genetics` or equivalent) must **not** import Minecraft, NeoForge, Fabric, registries, events, entities, NBT, UI, mixins, or attachments. It stays **pure Java** and unit-testable. Platform code calls the core; the core never references game types. ([`docs/decisions.md` → ADR-0002](docs/decisions.md).)

## Where to read

The repo intentionally uses **only seven** files under `docs/`:

| Order | Path |
|-------|------|
| 1 | [Readme.md](Readme.md) — overview and current focus |
| 2 | [docs/project-guide.md](docs/project-guide.md) — doc index, hybrid bee model, workflow |
| 3 | [docs/requirements.md](docs/requirements.md) — what the mod must do / not do |
| 4 | [docs/architecture.md](docs/architecture.md) — modules, genetics, breeding, content |
| 5 | [docs/roadmap.md](docs/roadmap.md) — phased plan |
| 6 | [docs/TASKS.md](docs/TASKS.md) — epics + concrete tasks |
| 7 | [docs/decisions.md](docs/decisions.md) — locked ADRs |
| 8 | [docs/asset-generation-guidelines.md](docs/asset-generation-guidelines.md) — art rules |

Do **not** add new top-level markdown files under `docs/`. If something doesn't fit, append a section to the closest existing file.

## Hybrid bee model (entity + scoped item)

- **Default:** bees are **living entities** in the world (breeding, nests, analyzer on bee).
- **Allowed:** item or container representation **only where a feature explicitly needs it** (e.g. advanced hive insertion, transport). Do not silently turn the whole mod into item-only bees.

## Guardrails (do not implement without explicit design approval)

- Resource bees (ores, etc.) as a broad category — gated by [`decisions.md` → ADR-0012](docs/decisions.md).
- Large mutation trees or content explosions in one PR.
- Lifecycle/death/larvae as enforced mechanics.
- Temperature/humidity simulation.
- Fabric parity, JEI/REI, deep cross-mod layers — unless scoped.
- Final committed “placeholder” species/GUI textures passed off as finished art (see [`asset-generation-guidelines.md`](docs/asset-generation-guidelines.md)).

## Workflow (every task)

1. Read **Readme.md** + the relevant section of **`docs/architecture.md`** + the **`docs/requirements.md`** rules that touch your area.
2. Pick a task from **`docs/TASKS.md`**. If a phase-level question is unclear, check **`docs/roadmap.md`**.
3. If your work touches a locked decision, read it in **`docs/decisions.md`**. If you are about to **change** one, add a new entry there in the same change.
4. Restate scope, list files, implement the smallest complete slice, add tests for core Java where it matters, commit with a clear message (`docs:`, `neoforge:`, `core:`, etc.).

## Logging (services)

Use `java.util.logging.Logger` in services: **WARNING** before skip/throw on bad input; **FINE** for trace; models throw without logging. See [AGENTS.md](AGENTS.md) for validation detail.

## Review quick-check

- Genetics stays pure Java off the game API.
- Analysis rules: do not leak full genetics in UI/tooltips until analyzed.
- No new undeclared placeholder-as-final art.
- No resource-bee tree unless approved.
- Server state that drives client UI/sync: keep network story consistent.

## Art

See [`docs/asset-generation-guidelines.md`](docs/asset-generation-guidelines.md). Hard rule: every dev placeholder is tagged `DEV-PLACEHOLDER` so a single grep finds them before release.

## gstack

Use `/browse` from gstack for all web browsing. Never use `mcp__claude-in-chrome__*` tools.

Available gstack skills: `/office-hours`, `/plan-ceo-review`, `/plan-eng-review`, `/plan-design-review`, `/design-consultation`, `/design-shotgun`, `/design-html`, `/review`, `/ship`, `/land-and-deploy`, `/canary`, `/benchmark`, `/browse`, `/connect-chrome`, `/qa`, `/qa-only`, `/design-review`, `/setup-browser-cookies`, `/setup-deploy`, `/setup-gbrain`, `/retro`, `/investigate`, `/document-release`, `/codex`, `/cso`, `/autoplan`, `/plan-devex-review`, `/devex-review`, `/careful`, `/freeze`, `/guard`, `/unfreeze`, `/gstack-upgrade`, `/learn`.
