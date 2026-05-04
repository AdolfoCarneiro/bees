# CLAUDE.md

Guidance for AI coding agents in this repository.

## What this mod is

Curious Bees is a NeoForge bee **genetics** mod: living bees carry genomes, breed through vanilla flow, Mendelian inheritance, dominance, and mutations — with Curious Bees species, textures, and UX goals described in **`docs/post-mvp/gameplay-direction.md`**.

Not a Forestry port, not a Productive Bees fork, not a deterministic resource-bee recipe mod.

## Platform

- **Now:** NeoForge 1.21.1  
- **Later:** Fabric gameplay is a non-goal until explicitly scoped; keep boundaries so `common/genetics` stays portable.

## Non-negotiable architecture

The **genetics core** (`common/genetics` or equivalent) must **not** import Minecraft, NeoForge, Fabric, registries, events, entities, NBT, UI, mixins, or attachments. It stays **pure Java** and unit-testable. Platform code calls the core; the core never references game types.

## Where to read

| Order | Path |
|-------|------|
| 1 | [docs/README.md](docs/README.md) — what exists in the mod, doc map |
| 2 | [docs/post-mvp/gameplay-direction.md](docs/post-mvp/gameplay-direction.md) — product intent, hybrid bee model, UX goals |
| 3 | [docs/ROADMAP.md](docs/ROADMAP.md) — forward priorities only |
| Genetics / breeding tasks | [docs/architecture/02-technical-architecture.md](docs/architecture/02-technical-architecture.md), [03](docs/architecture/03-genetics-system-spec.md), [04](docs/architecture/04-breeding-and-mutation-spec.md); content traits: [05](docs/architecture/05-content-design-spec.md) |
| ADRs | [docs/decisions/](docs/decisions/) when a decision is locked |
| Assets | [docs/art/asset-prompt-workflow.md](docs/art/asset-prompt-workflow.md) — prompts, not silent final placeholders |

## Hybrid bee model (entity + scoped item)

- **Default:** bees are **living entities** in the world (breeding, nests, analyzer on bee).
- **Allowed:** item or container representation **only where a feature explicitly needs it** (e.g. advanced hive insertion, transport). Do not silently turn the whole mod into item-only bees.

## Guardrails (do not implement without explicit design approval)

- Resource bees (ores, etc.) as a broad category  
- Large mutation trees or content explosions in one PR  
- Lifecycle/death/larvae as enforced mechanics  
- Temperature/humidity simulation  
- Fabric parity, JEI/REI, deep cross-mod layers — unless scoped  
- Final committed “placeholder” species/GUI textures — use `docs/art/prompts/` and wait for real assets  

## Workflow (every task)

1. Read `docs/README.md` + `gameplay-direction.md` if the task touches player-facing direction.  
2. Read relevant `docs/architecture/` specs (02–05) for genetics/content rules.  
3. Restate scope, list files, implement the smallest complete slice, add tests for core Java where it matters, commit with a clear message (`docs:`, `neoforge:`, `core:`, etc.).

## Logging (services)

Use `java.util.logging.Logger` in services: **WARNING** before skip/throw on bad input; **FINE** for trace; models throw without logging. See [AGENTS.md](AGENTS.md) for validation detail.

## Review quick-check

- Genetics stays pure Java off the game API  
- Analysis rules: do not leak full genetics in UI/tooltips until analyzed (where the code already enforces this)  
- No new placeholder-as-final art  
- No resource-bee tree unless approved  
- Server state that drives client UI/sync: keep network story consistent  

## Art

New visuals need a prompt under `docs/art/prompts/` first; dev-only fallbacks must be labeled as such.
