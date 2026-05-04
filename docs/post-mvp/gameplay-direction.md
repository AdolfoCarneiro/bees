# Curious Bees — gameplay direction

Product intent. Technical rules: `docs/architecture/` (02–05). Priorities: `docs/ROADMAP.md`.

## Positioning

- **Differentiation:** Forestry-style genetic depth on **living bees** (genomes, breeding, dominance, mutations) with Curious Bees identity — species, textures, bee products, progression.
- **UX / loop reference:** Aim for the **quality bar** of modern bee mods (clear GUIs, bees visible in hives, extensions, automation-friendly blocks, comb processing). **Not** a clone: do not copy code, assets, names, exact GUIs, or written trees from other projects. Study **patterns** only.

## World and nests

- **Spawn diversity:** Species in sensible biomes/habitats; nest **variety** in content, **interaction** close to vanilla where possible.
- **Nest variety vs vanilla feel:** Prefer breadth of nest **types** with vanilla-like **behavior** unless documented otherwise.

## Hives and apiaries (goals)

- **Genetic apiary:** Central block; live bees; automation-ready inventories without paywalling basic automation.
- **Advanced hive (future):** Extensions, bee visible in GUI, predictable outputs — original UX, not a copy.
- **Multiblock vs expandable hive:** **Open decision** — capture in an ADR when chosen.

## Products and processing

- Combs and bee outputs in scope for a polished loop; avoid deterministic resource-bee grinders unless a future design allows it.
- **Centrifuge / processing:** Separate honey, product, wax — milestone after frames and base production.

## Entity vs item (hybrid model)

- **Default:** living bees in the world (breeding, nests, analyzer on entity).
- **Allowed:** item/container representation **only where a feature needs it** (advanced hive, transport). Do not replace the whole mod with item-only bees.

## First expanded species branch (sketch)

- A **managed / cultivated** line is a plausible first branch after the production loop is fun — detail lived in removed docs; **git history** if you need the full draft.

## Viability

Differentiation = genetics + identity + fair UX; scope controlled via `ROADMAP.md` and agent guardrails.

## Ethics / inspiration

Use `docs/research/` for patterns and pitfalls, never to copy protected expression.
