# Bee Genetics Mod

A Minecraft mod concept focused on bringing Forestry-inspired bee genetics to modern vanilla-style bees.

The goal is not to port Forestry and not to fork Productive Bees. The goal is to build a new mod from scratch where vanilla bees can carry genetic data, reproduce through Minecraft's natural breeding flow, mutate into new species, and gradually support a deeper tech-oriented bee progression.

## Target

- Initial loader: NeoForge 1.21.1
- Future target: Fabric support
- Development style: AI-assisted coding with Claude Code, Codex, Cursor, and similar tools
- Main design rule: implement the genetics core first, isolated from Minecraft APIs

## Project Documentation

Start with these files:

```txt
docs/
├── 01-product-vision-and-roadmap.md
├── 02-technical-architecture.md
├── 03-genetics-system-spec.md
├── 04-breeding-and-mutation-spec.md
├── 05-content-design-spec.md
├── 06-ai-coding-guidelines.md
└── 07-initial-backlog.md
```

The `CLAUDE.md` file at the repository root summarizes the project constraints and tells Claude Code how to work with the documentation.

## Recommended First Development Step

Do not start with blocks, items, GUIs, textures, hives, or Minecraft event integration.

Start with a pure Java genetics core:

- `Allele`
- `Dominance`
- `GenePair`
- `Genome`
- `ChromosomeType`
- `BreedingService`
- `MutationService`
- Unit tests for Mendelian inheritance, dominance, hybrid/purebred detection, and mutation probability

Only after that should Minecraft integration begin.
