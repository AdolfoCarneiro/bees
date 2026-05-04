# Curious Bees — documentation

## What the mod does today (shipped direction)

- **Genetics:** Genomes on bees; breeding; chromosomes/alleles; dominance; mutations — see `docs/mvp/03` and `04`.
- **World:** Species bee nests, habitat-style spawning, compatibility rules — behavior details also in `docs/reference/bee-nest-targeting-behavior.md`.
- **Player tools:** Portable analyzer, analysis state, tooltips respecting analyzed/unanalyzed where implemented.
- **Blocks:** Genetic apiary with GUI, frames slots, bees tied to production, automation-facing inventories.
- **Presentation:** Species textures on bees; content/data conventions for species — see `docs/mvp/05` and `docs/art/`.

## What we are building next

See **`docs/ROADMAP.md`** (ordered list only). High-level intent and UX parity goals: **`docs/post-mvp/gameplay-direction.md`**.

## Essential reading for agents

1. `docs/post-mvp/gameplay-direction.md`  
2. `docs/ROADMAP.md`  
3. `docs/mvp/02-technical-architecture.md`  
4. `docs/mvp/03-genetics-system-spec.md`  
5. `CLAUDE.md` and `AGENTS.md`  

For new species content steps, see `.claude/plugins/local/skills/new-bee-species.md`.

## Other folders (one line each)

- **`docs/mvp/`** — Foundation specs (02–05); genetics source of truth.  
- **`docs/decisions/`** — ADRs; read when touching a locked decision.  
- **`docs/art/`** — Prompts, workflow, manifests — not optional for new art.  
- **`docs/quality/`** — Playtest and smoke lists.  
- **`docs/research/`** — Competitive notes; inspiration only, no copying.  
- **`docs/reference/`** — Short technical behavior notes.  
- **`docs/implementation/`** — Reserved for small migration notes if needed; empty is fine.  

## Repo root

Project overview for humans: `Readme.md` / `README.md` at repository root.
