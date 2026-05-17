# AGENTS.md

Agent rules for this repo. **Also read [CLAUDE.md](CLAUDE.md).**

## Summary

Minecraft bee genetics mod (NeoForge 1.21.1): living bees, genomes, breeding, dominance, mutations. Product framing: root **Readme.md** + **CLAUDE.md** + **[docs/project-guide.md](docs/project-guide.md)**.

## Hard rule

**`common/genetics` (and the genetics core)** must not depend on Minecraft, NeoForge, Fabric, registries, events, entities, NBT, components, attachments, mixins, item stacks, levels, or UI. Pure Java, unit-testable. Integration lives under `neoforge/` (and future `fabric/`). ([`docs/decisions.md` → ADR-0002](docs/decisions.md).)

## Documentation order

The repo uses **eight** docs files. Do not add new top-level markdown files under `docs/`. (`docs/product-reset.md` is a Phase PR validation reference — temporary, retired after Epic PR passes.)

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
4. Restate scope; list files; smallest complete change; tests for core Java where relevant; commit one focused task per commit (see **Commits** section for prefix format).

## Build Commands

```bash
./gradlew :common:test              # Run pure-Java unit tests (fast, no MC needed)
./gradlew :neoforge:build           # Build the NeoForge JAR
./gradlew :neoforge:runClient       # Launch Minecraft client for manual testing
./gradlew :neoforge:runServer       # Launch headless Minecraft server
./gradlew :neoforge:runGameTestServer  # Run in-game automated tests
```

On Windows use `gradlew` instead of `./gradlew`. **Before opening a PR:** run `:common:test` and verify it passes.

## Code Style

Formatting enforced by Spotless. Run `./gradlew installGitHooks` once to install the pre-commit check.

Key rules: no Lombok, no Java records for game data classes, max 3 levels nesting, no magic numbers (named constants), package-private over `public` when not part of a public API.

Manual fix: `./gradlew :common:spotlessApply :neoforge:spotlessApply :fabric:spotlessApply`

## Commits

Format: `<prefix>: <imperative description>` (≤ 72 characters)

| Prefix | When to use |
|--------|-------------|
| `feat` | New player-visible feature |
| `fix` | Bug fix |
| `refactor` | Internal restructure, no behaviour change |
| `test` | Adding or updating tests |
| `docs` | Documentation only |
| `core` | Change inside `common/` genetics or gameplay logic |
| `neoforge` | NeoForge-specific platform code |
| `assets` | Textures, models, sounds, lang files |
| `chore` | Build, CI, deps, tooling |

Imperative mood. No period. Body only when the *why* is not obvious.

## Pull Requests

Title: same `<prefix>: <description>` format. Body: release notes style — what changed and why it matters. Breaking changes: `feat!:` prefix.

## Packages

```
curious-bees/
├── common/src/main/java/com/curiousbees/common/
│   ├── genetics/          # PURE JAVA — zero MC/NeoForge imports allowed
│   │   ├── model/         # Genome, Allele, Gene — immutable value types
│   │   ├── breeding/      # Mendelian logic, dominance resolution
│   │   ├── mutation/      # Mutation rules and probability
│   │   ├── random/        # Randomness abstraction (testable)
│   │   └── serial/        # Genome ↔ serialisable form (no NBT here)
│   ├── content/           # Bee definitions from JSON/data packs
│   └── gameplay/          # Game logic — may reference MC types via interfaces
└── neoforge/src/main/java/com/curiousbees/
    ├── CuriousBeesMod.java    # NeoForge @Mod entry point
    └── neoforge/
        ├── bee/           # Entity genome lookup
        ├── block/         # Blocks and BlockEntities
        ├── capability/    # NeoForge capability declarations
        ├── client/        # Rendering, screens, GUI (client-only)
        ├── command/       # Debug commands
        ├── config/        # Mod configuration
        ├── content/       # NeoForge content registry and reload
        ├── data/          # NeoForge data attachments (genome storage)
        ├── event/         # NeoForge event subscribers
        ├── gametest/      # In-game automated tests
        ├── item/          # Items (BeeJar, BeeTransporter, etc.)
        ├── menu/          # Container/Menu classes
        ├── network/       # Packets
        ├── recipe/        # Recipe types
        ├── registry/      # DeferredRegister entries
        └── worldgen/      # World generation
```

`genetics/` must never import MC classes. `neoforge/` may import everything in `common/`.

## Style and validation

Prefer small classes, explicit validation, `Objects.requireNonNull` at boundaries, deterministic tests, services not stuffed in event handlers. **Logger** in services: WARNING on bad/skip, FINE on trace; models throw, no logging.

## Review checklist

Aligned with **Readme.md** / `docs/requirements.md`; genetics pure Java; no accidental resource-bee tree; genetic data visible in all Curious Bees controlled UIs (no analysis gate — Analyzer is optional); no final placeholder art; automation not artificially paywalled; server/client sync story intact for UI-driving state.

## Growth line

Validated genetics core → polish and production loop (frames, products, processing, advanced hive UX) → species expansion → later resource progression only with its own design → Fabric when scoped. Phases: [`docs/roadmap.md`](docs/roadmap.md).

## Assets

No silent placeholder-as-final. Dev fallback only if crash-proofing and clearly marked. Full rules: [`docs/asset-generation-guidelines.md`](docs/asset-generation-guidelines.md).
