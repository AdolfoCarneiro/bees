# CLAUDE.md

Guidance for AI coding agents in this repository.

## What this mod is

Curious Bees is a NeoForge bee **genetics** mod: living bees carry genomes, breed through vanilla flow, Mendelian inheritance, dominance, and mutations — with species visuals and UX polish as ongoing goals. High-level product framing lives in the root **[Readme.md](Readme.md)** and here; technical contracts live under **`docs/architecture/`**.

Not a Forestry port, not a Productive Bees fork, not a deterministic resource-bee recipe mod.

## Platform

- **Now:** NeoForge 1.21.1  
- **Later:** Fabric gameplay is a non-goal until explicitly scoped; keep boundaries so `common/genetics` stays portable.

## Non-negotiable architecture

The **genetics core** (`common/genetics` or equivalent) must **not** import Minecraft, NeoForge, Fabric, registries, events, entities, NBT, UI, mixins, or attachments. It stays **pure Java** and unit-testable. Platform code calls the core; the core never references game types.

## Where to read

| Order | Path |
|-------|------|
| 1 | [Readme.md](Readme.md) — overview and current focus |
| 2 | [docs/architecture/README.md](docs/architecture/README.md) — index of 02–05 |
| Genetics / breeding / content rules | [docs/architecture/02-technical-architecture.md](docs/architecture/02-technical-architecture.md), [03](docs/architecture/03-genetics-system-spec.md), [04](docs/architecture/04-breeding-and-mutation-spec.md), [05](docs/architecture/05-content-design-spec.md) |
| ADRs | [docs/decisions/](docs/decisions/) when a decision is locked |

## Hybrid bee model (entity + scoped item)

- **Default:** bees are **living entities** in the world (breeding, nests, analyzer on bee).
- **Allowed:** item or container representation **only where a feature explicitly needs it** (e.g. advanced hive insertion, transport). Do not silently turn the whole mod into item-only bees.

## Guardrails (do not implement without explicit design approval)

- Resource bees (ores, etc.) as a broad category  
- Large mutation trees or content explosions in one PR  
- Lifecycle/death/larvae as enforced mechanics  
- Temperature/humidity simulation  
- Fabric parity, JEI/REI, deep cross-mod layers — unless scoped  
- Final committed “placeholder” species/GUI textures passed off as finished art  

## Workflow (every task)

1. Read **Readme.md** and relevant **`docs/architecture/`** specs (02–05) for the task.  
2. Restate scope, list files, implement the smallest complete slice, add tests for core Java where it matters, commit with a clear message (`docs:`, `neoforge:`, `core:`, etc.).

## Logging (services)

Use `java.util.logging.Logger` in services: **WARNING** before skip/throw on bad input; **FINE** for trace; models throw without logging. See [AGENTS.md](AGENTS.md) for validation detail.

## Review quick-check

- Genetics stays pure Java off the game API  
- Analysis rules: do not leak full genetics in UI/tooltips until analyzed (where the code already enforces this)  
- No new undeclared placeholder-as-final art  
- No resource-bee tree unless approved  
- Server state that drives client UI/sync: keep network story consistent  

## Art

There is **no** bundled prompt/manifest tree in `docs/` right now. For new visuals: agree format with the maintainer (issue, PR description, or a doc you add later). Dev-only fallbacks must be clearly labeled, not shipped as final assets.
