# Curious Bees Documentation

This folder contains the design, architecture, research, implementation, and release documentation for Curious Bees.

Curious Bees has moved past its initial MVP validation phase. The original MVP documents are preserved as foundation and historical context, while current planning now lives under `docs/post-mvp/`.

## Current Status (May 2026)

Phases 12–14 and 16 (per `docs/post-mvp/11-post-mvp-productization-roadmap.md`) are implemented. The mod has:

- species-specific textures on living bees
- portable analyzer with analysis state
- genetic apiary GUI
- content and asset pipeline foundation

Next: Phase 15 (frames with real effects), then Phase 17 (first expanded species branch). Phase 11.6 (custom bee entity ADR) remains design-only.

## Start Here

For current work, read these first:

1. `docs/post-mvp/11-post-mvp-productization-roadmap.md`
2. `docs/post-mvp/12-visual-species-system.md`
3. `docs/post-mvp/13-analyzer-ux-and-progression.md`
4. `docs/post-mvp/14-genetic-apiary-gui-and-frames.md`
5. `docs/post-mvp/15-content-and-asset-pipeline.md`

The post-MVP direction is focused on turning the validated MVP into a polished, playable mod:

- visual identity per bee species;
- real analyzer UX instead of chat/debug output;
- genetic apiary GUI;
- useful frames and apiary behavior;
- automation-ready apiary design;
- a sustainable content and asset pipeline.

## MVP Foundation

The original MVP documents live in `docs/mvp/`.

These documents explain how the first playable foundation was designed and why the project was initially constrained to a small, testable scope.

- `docs/mvp/02-technical-architecture.md`
- `docs/mvp/03-genetics-system-spec.md`
- `docs/mvp/04-breeding-and-mutation-spec.md`
- `docs/mvp/05-content-design-spec.md`

Use these documents as architectural and conceptual foundation, especially for:

- genetics core rules;
- Mendelian inheritance;
- active/inactive alleles;
- mutation behavior;
- separation between `common`, `neoforge`, and future `fabric`;
- AI-assisted coding guardrails.

Do not treat the MVP roadmap as the current roadmap.

## Current Post-MVP Direction

The validated genetic core is in place; the product layer for visuals, analyzer, apiary GUI, and content pipeline (Phase 16) is implemented. The next roadmap focus is Phase 15 (frames) and then Phase 17 (species expansion).

Current direction:

```txt
Productive Bees-like UX and automation quality
+
Forestry-inspired genetic depth
+
living vanilla bees as the gameplay foundation
+
Curious Bees' own visual identity and progression
```

Current principles:

- Bees remain living entities, not item-only machine inputs.
- The apiary works with living bees.
- The apiary should be automation-ready by design.
- Genetic data should only be shown to the player after analysis.
- Analyzer output should move from chat/debug to real UI.
- Species should have visual identity.
- The default visual strategy is one base bee model with species-specific textures.
- Special species may use custom models later.
- Lifecycle, death, larvae, temperature, humidity, and resource bees are future topics, not current implementation priorities.

## Research

Research documents live in `docs/research/`.

Planned/current research:

- `docs/research/existing-bee-mods-review.md`

Research should be used to identify useful patterns and avoid bad directions. It should not copy code, assets, names, mutation trees, configs, or written content from other mods.

## Decisions

Architecture and product decisions live in `docs/decisions/`.

ADRs should be used when a decision affects long-term design, for example:

- whether bees can become items;
- how visual species definitions work;
- whether lifecycle is introduced;
- when resource bees become allowed;
- how apiary automation is exposed.

## Implementation Notes

`docs/implementation/` is reserved for narrow technical plans or migration notes when needed. Completed phase execution plans were removed; design intent remains in `docs/post-mvp/` and the codebase.

## Quality

Testing, validation, balancing, and playtest notes live in `docs/quality/`.

Use this for:

- manual playtest checklists;
- regression test plans;
- balancing notes;
- validation reports;
- in-game verification checklists.

## Art and Assets

Art direction and asset planning live in `docs/art/`.

Use this for:

- bee texture conventions;
- Blockbench model notes;
- visual references;
- asset naming;
- palette decisions;
- placeholder vs final asset tracking.

## Release

Release planning lives in `docs/release/`.

Use this for:

- packaging notes;
- release checklist;
- changelogs;
- compatibility notes;
- known issues;
- public-facing summaries.

## AI Agent Reading Order

For AI-assisted development, agents should usually read:

1. `CLAUDE.md`
2. `docs/README.md`
3. The relevant post-MVP document for the task
4. The relevant MVP foundation documents
5. Any related ADRs

For the first expanded species branch design input, see `docs/post-mvp/17-cultivated-branch-design-input.md`. For live nest targeting and nectar rendering behavior, see `docs/reference/bee-nest-targeting-behavior.md`.

The `common/genetics` architectural rule remains unchanged:

```txt
common/genetics must not depend on Minecraft, NeoForge, Fabric, entities, registries, NBT, events, attachments, components, or mixins.
```
