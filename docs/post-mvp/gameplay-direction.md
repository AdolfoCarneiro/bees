# Curious Bees — gameplay direction

Short-lived product intent. Genetics rules live in `docs/architecture/03` and `04`; platform boundaries in `docs/architecture/02`. Next milestones: `docs/ROADMAP.md`.

## Positioning

- **Differentiation:** Forestry-style genetic depth on a **living bee** loop (genomes, breeding, dominance, mutations) with Curious Bees identity — our species, textures, bee products, progression.
- **UX / loop reference:** Aim for the **quality bar** of modern bee mods (clear GUIs, visible bees in hives, extensions, automation-friendly blocks, comb processing). This is **not** a clone of any mod: do not copy code, assets, names, exact GUIs, or written trees from other projects. Study **patterns** (slots, progress, upgrades) and ship our own art and copy.

## World and nests

- **Spawn diversity:** Species appear in the world in sensible biomes/habitats; nests or homes should feel varied in **content** (many nest types over time) while **behavior** stays close to vanilla expectations (entry rules, POI, readable gameplay) unless we intentionally document a deviation.
- **Nest variety vs vanilla feel:** Prefer “Forestry-like breadth of nest **types**” with “vanilla-like **interaction**” where possible.

## Hives and apiaries (goals)

- **Genetic apiary (current direction):** Central block for production and controlled beekeeping; live bees; automation-ready inventories without locking basic automation behind an upgrade.
- **Advanced hive tier (future):** Target parity with the **player fantasy** of reference mods: see the bee inside, extensions, predictable outputs, pipe-friendly sides. Exact block set and GUI layout are TBD and must remain original.
- **Multiblock vs expandable single block:** **Open decision.** Options include a Forestry-style multiblock or an expandable hive (modules/extensions). Capture trade-offs in an ADR when we commit to one path.

## Products and processing

- **Combs and bee outputs:** Species- or tier-linked combs and related outputs are in scope for “pretty and playable,” not deterministic resource-bee grinders unless a future design explicitly allows it.
- **Centrifuge / processing:** Separate honey, primary product, wax (or equivalent) in a dedicated machine loop — milestone after frames and base production are trustworthy.

## Entity vs item (hybrid model)

- **Living bees** remain the default in the world: breeding, flight, nests, analyzer on entity.
- **Item or container representation** is allowed where it clearly improves UX (e.g. advanced hive insertion, transport, automation) — scoped by feature, not as a silent replacement for the whole mod.
- Do not contradict `docs/architecture/` genetics specs; platform code owns Minecraft types.

## First expanded species branch (sketch only)

- A **managed / cultivated** line (extra species, conservative mutations, no resource bees) is a plausible first branch after the production loop is fun — names and parents were explored historically; git history has detail. Implement only after roadmap items ahead of it are done.

## Viability

- Competing mods are established; our bet is **genetics + identity + fair UX**, not feature-for-feature racing. Scope is controlled via `docs/ROADMAP.md` and guardrails in `CLAUDE.md` / `AGENTS.md`.

## Ethics / inspiration

- Competitive analysis belongs in `docs/research/` — use it for patterns and pitfalls, never to copy protected expression.
