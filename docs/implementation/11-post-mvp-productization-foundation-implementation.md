# 11 — Post-MVP Productization Foundation Implementation

## 1. Purpose

This document defines the implementation plan for **Phase 11 — Post-MVP Productization Foundation** of Curious Bees.

Phase 11 does not add a major gameplay system by itself. It prepares the repository, documentation, AI-agent guidance, and project guardrails for the post-MVP productization phase.

The MVP proved the core loop. This phase makes sure future coding work no longer follows the old MVP-only roadmap by accident.

## 2. Phase Goal

Prepare Curious Bees for post-MVP implementation by making the new direction explicit and actionable.

By the end of this phase, the project should have:

```text
- MVP documentation preserved as historical foundation;
- post-MVP documentation established as the current roadmap;
- CLAUDE.md and agent guidance updated;
- root README updated with current status;
- implementation docs for post-MVP phases linked clearly;
- old implementation docs adjusted to the new docs/mvp paths where needed;
- clear guardrails for what is now in scope and still out of scope.
```

## 3. Phase Non-goals

Do not implement:

```text
- new bee species;
- resource bees;
- visual rendering changes;
- analyzer UI;
- apiary GUI;
- frame mechanics;
- Fabric support;
- lifecycle/death/larvae systems;
- temperature/humidity/environment simulation;
- custom models for every species;
- gameplay behavior changes.
```

This phase is documentation and project-structure work.

## 4. Required Inputs

Before implementing this phase, the AI agent should read:

```text
CLAUDE.md
README.md
docs/README.md
docs/post-mvp/11-post-mvp-productization-roadmap.md
docs/post-mvp/12-visual-species-system.md
docs/post-mvp/13-analyzer-ux-and-progression.md
docs/post-mvp/14-genetic-apiary-gui-and-frames.md
docs/post-mvp/15-content-and-asset-pipeline.md
docs/mvp/01-product-vision-and-roadmap.md
docs/mvp/02-technical-architecture.md
docs/mvp/03-genetics-system-spec.md
docs/mvp/04-breeding-and-mutation-spec.md
docs/mvp/06-ai-coding-guidelines.md
```

If `AGENTS.md` exists, read it too and keep it consistent with `CLAUDE.md`.

## 5. Expected Outputs

Phase 11 should produce documentation updates for:

```text
docs/README.md
README.md
CLAUDE.md
AGENTS.md if present
docs/implementation/11-post-mvp-productization-foundation-implementation.md
docs/implementation/12-visual-species-system-implementation.md
docs/implementation/13-analyzer-ux-implementation.md
docs/implementation/14-genetic-apiary-gui-and-frames-implementation.md
docs/implementation/15-content-and-asset-pipeline-implementation.md
```

It may also update existing implementation docs if they still point to old root-level MVP docs.

## 6. Architecture and Process Rules

### 6.1 Preserve MVP Docs

Do not delete or rewrite the MVP docs. They explain why the current architecture exists.

Good:

```text
docs/mvp/03-genetics-system-spec.md remains as the genetics foundation.
docs/post-mvp/11-post-mvp-productization-roadmap.md becomes the current roadmap.
```

Bad:

```text
Delete old docs because the MVP is done.
Rewrite the MVP docs as if they were the post-MVP roadmap.
```

### 6.2 Current Direction Comes From post-mvp

For new work, agents should start from:

```text
docs/post-mvp/11-post-mvp-productization-roadmap.md
```

The old MVP roadmap should be treated as foundation, not as the active product plan.

### 6.3 Technical Architecture Still Comes From MVP Architecture

The core architectural rule remains unchanged:

```text
common/genetics must not depend on Minecraft, NeoForge, Fabric, entities, registries, NBT, attachments, components, events or mixins.
```

Post-MVP work must not weaken this boundary.

### 6.4 Update Path References

Since MVP docs moved under `docs/mvp/`, old references like:

```text
docs/01-product-vision-and-roadmap.md
```

should become:

```text
docs/mvp/01-product-vision-and-roadmap.md
```

Do this in `README.md`, `CLAUDE.md`, `AGENTS.md`, and `docs/implementation/*` where applicable.

## 7. Task Breakdown

## Task 1 — Verify Documentation Layout

### Objective

Confirm that the documentation tree follows the new structure.

### Scope

Expected structure:

```text
docs/
├── README.md
├── mvp/
│   ├── 01-product-vision-and-roadmap.md
│   ├── 02-technical-architecture.md
│   ├── 03-genetics-system-spec.md
│   ├── 04-breeding-and-mutation-spec.md
│   ├── 05-content-design-spec.md
│   ├── 06-ai-coding-guidelines.md
│   └── 07-initial-backlog.md
├── post-mvp/
│   ├── 11-post-mvp-productization-roadmap.md
│   ├── 12-visual-species-system.md
│   ├── 13-analyzer-ux-and-progression.md
│   ├── 14-genetic-apiary-gui-and-frames.md
│   └── 15-content-and-asset-pipeline.md
└── implementation/
    ├── 00-phase-0-documentation-and-decisions.md
    ├── ...
    └── 15-content-and-asset-pipeline-implementation.md
```

### Non-goals

Do not rename source packages or move Java files.

### Acceptance Criteria

```text
- docs/mvp exists.
- docs/post-mvp exists.
- docs/implementation exists.
- MVP docs are no longer at docs root.
- docs/README.md explains the new layout.
```

### Prompt for Claude Code

```text
Read CLAUDE.md and docs/README.md.

Focus only on Task 1 from docs/implementation/11-post-mvp-productization-foundation-implementation.md.

Verify the documentation layout and update docs/README.md if needed.

Do not modify Java code, gameplay behavior, assets, registries, items, blocks or build files.

Before changing files, summarize the expected docs layout and list files you expect to edit.
```

## Verification Gate 1 — Documentation Layout Review

```text
- Can a new contributor find the current roadmap?
- Can a new contributor find the MVP foundation docs?
- Are implementation docs separate from design docs?
```

## Task 2 — Update Root README

### Objective

Make the repository README reflect the current project status.

### Scope

README should say:

```text
- the MVP core has been validated;
- the project is now in post-MVP productization;
- current planning lives in docs/post-mvp;
- MVP foundation lives in docs/mvp;
- implementation task breakdowns live in docs/implementation;
- NeoForge 1.21.1 remains the active target;
- Fabric remains future support.
```

### Non-goals

Do not turn README into a full design document.

### Acceptance Criteria

```text
- README has a Current Status section.
- README links to docs/README.md.
- README links to docs/post-mvp/11-post-mvp-productization-roadmap.md.
- README no longer presents the old MVP sequence as the active roadmap.
```

### Prompt for Claude Code

```text
Read README.md, docs/README.md and docs/post-mvp/11-post-mvp-productization-roadmap.md.

Focus only on Task 2 from docs/implementation/11-post-mvp-productization-foundation-implementation.md.

Update the root README to reflect the current post-MVP productization phase.

Do not modify source code, build files or gameplay docs outside README.

Before changing files, summarize the README changes you plan to make.
```

## Verification Gate 2 — README Review

```text
- Does README clearly say the MVP was validated?
- Does README point to post-MVP docs for current work?
- Does README still preserve the core project identity?
```

## Task 3 — Update CLAUDE.md and AGENTS.md

### Objective

Make AI agents read the correct docs before coding.

### Scope

Update `CLAUDE.md` and `AGENTS.md` if present.

Required reading should include:

```text
docs/README.md
docs/post-mvp/11-post-mvp-productization-roadmap.md
docs/mvp/02-technical-architecture.md
docs/mvp/03-genetics-system-spec.md
docs/mvp/04-breeding-and-mutation-spec.md
```

Task-specific reading should include:

```text
Visual species work:
- docs/post-mvp/12-visual-species-system.md
- docs/implementation/12-visual-species-system-implementation.md

Analyzer work:
- docs/post-mvp/13-analyzer-ux-and-progression.md
- docs/implementation/13-analyzer-ux-implementation.md

Apiary/frames work:
- docs/post-mvp/14-genetic-apiary-gui-and-frames.md
- docs/implementation/14-genetic-apiary-gui-and-frames-implementation.md

Content/assets work:
- docs/post-mvp/15-content-and-asset-pipeline.md
- docs/implementation/15-content-and-asset-pipeline-implementation.md
```

### Current Guardrails

Agents must not implement without explicit request:

```text
- resource bees;
- large species trees;
- lifecycle/death/larvae mechanics;
- temperature/humidity/environment simulation;
- Fabric parity;
- custom model per species as a baseline;
- itemized bee systems that replace living bees;
- automation unlock upgrades;
- complex research systems;
- mod compatibility trees.
```

### Acceptance Criteria

```text
- CLAUDE.md uses docs/mvp paths for MVP foundation docs.
- CLAUDE.md uses docs/post-mvp paths for current direction.
- CLAUDE.md references implementation docs for coding tasks.
- AGENTS.md is consistent if present.
- Old instruction "do not implement GUI" is not used as a blanket rule anymore.
- New guardrails are explicit.
```

### Prompt for Claude Code

```text
Read CLAUDE.md, AGENTS.md if present, docs/README.md and docs/post-mvp/11-post-mvp-productization-roadmap.md.

Focus only on Task 3 from docs/implementation/11-post-mvp-productization-foundation-implementation.md.

Update AI-agent guidance so future tasks read the post-MVP docs and implementation docs.

Do not modify source code, gameplay behavior, assets or build files.

Before changing files, summarize the new required reading order and guardrails.
```

## Verification Gate 3 — Agent Guidance Review

```text
- Will Claude/Codex/Cursor read the new roadmap first?
- Are old MVP docs still referenced as architecture foundation?
- Are post-MVP GUI/visual/apiary tasks now allowed when explicitly scoped?
- Are forbidden future systems still blocked?
```

## Task 4 — Update Old Implementation Doc References

### Objective

Prevent broken references after moving MVP docs into `docs/mvp/`.

### Scope

Search existing docs for old paths:

```text
docs/01-
docs/02-
docs/03-
docs/04-
docs/05-
docs/06-
docs/07-
```

Update to:

```text
docs/mvp/01-
docs/mvp/02-
docs/mvp/03-
docs/mvp/04-
docs/mvp/05-
docs/mvp/06-
docs/mvp/07-
```

### Non-goals

Do not rewrite the content of old implementation docs unless path updates are needed.

### Acceptance Criteria

```text
- Existing implementation docs no longer point to missing old root-level docs.
- Links are updated to docs/mvp paths.
- No unrelated content is rewritten.
```

### Prompt for Claude Code

```text
Read docs/README.md.

Focus only on Task 4 from docs/implementation/11-post-mvp-productization-foundation-implementation.md.

Search documentation files for references to old MVP doc paths at docs/01 through docs/07 and update them to docs/mvp/01 through docs/mvp/07.

Do not rewrite content, do not modify Java code, and do not change gameplay behavior.

Before changing files, list the files containing old references.
```

## Verification Gate 4 — Link Review

```text
- Are old root-level MVP doc paths gone?
- Do updated links point to existing files?
- Were only documentation links changed?
```

## Task 5 — Add MVP Status Banners

### Objective

Make it clear that MVP docs are historical foundation documents.

### Scope

Add a short banner to the top of each moved MVP doc:

```markdown
> Status: MVP foundation document.
>
> This document describes the original MVP design used to validate the Curious Bees core loop.
> It is preserved as historical and architectural context.
> For the current post-MVP direction, see `docs/post-mvp/11-post-mvp-productization-roadmap.md`.
```

### Non-goals

Do not rewrite the body of MVP docs.

### Acceptance Criteria

```text
- All docs/mvp/01-07 files have the status banner.
- The banner points to the current roadmap.
- Existing MVP content remains intact.
```

### Prompt for Claude Code

```text
Read docs/README.md and docs/post-mvp/11-post-mvp-productization-roadmap.md.

Focus only on Task 5 from docs/implementation/11-post-mvp-productization-foundation-implementation.md.

Add the MVP foundation status banner to docs/mvp/01 through docs/mvp/07.

Do not rewrite the body of those documents.

Before changing files, list the MVP documents that will receive the banner.
```

## Verification Gate 5 — MVP Banner Review

```text
- Is each MVP doc clearly marked as foundation/historical?
- Does the banner avoid implying the MVP docs are obsolete or wrong?
- Does the banner point to the current post-MVP roadmap?
```

## 8. Phase Completion Checklist

Phase 11 is complete when:

```text
- docs/README.md accurately explains the docs layout.
- README.md describes the current post-MVP phase.
- CLAUDE.md reads post-MVP docs first for current work.
- AGENTS.md is updated if present.
- Existing implementation docs use docs/mvp paths where needed.
- MVP docs are clearly marked as foundation documents.
- No source code behavior changed.
```

## 9. AI Failure Modes to Watch

### 9.1 Rewriting Old Docs Too Aggressively

Bad:

```text
The agent rewrites MVP docs as if they were current productization docs.
```

Fix:

```text
Keep MVP docs as preserved foundation. Add only a status banner and path fixes.
```

### 9.2 Keeping Old Non-goals as Current Non-goals

Bad:

```text
CLAUDE.md still says GUI or apiary work should not be implemented as a blanket rule.
```

Fix:

```text
GUI and apiary work are now allowed when explicitly scoped in post-MVP implementation docs.
```

### 9.3 Letting Agents Jump to Content Expansion

Bad:

```text
Agent starts implementing resource bees or large species trees.
```

Fix:

```text
Resource bees and large content expansion remain out of scope for this phase.
```

## 10. Recommended Commit Sequence

```text
docs: verify post-mvp documentation layout
docs: update root readme for post-mvp direction
docs: update AI agent guidance for post-mvp work
docs: fix moved MVP documentation links
docs: mark MVP docs as foundation documents
```

## 11. Handoff Prompt for Starting Phase 11

```text
Read CLAUDE.md, README.md and docs/README.md.

Then read:
- docs/post-mvp/11-post-mvp-productization-roadmap.md
- docs/implementation/11-post-mvp-productization-foundation-implementation.md

Do not implement all of Phase 11 at once.

Start only with Task 1 — Verify Documentation Layout.

Before editing files, summarize:
1. the goal of Phase 11;
2. the scope of Task 1;
3. what is explicitly out of scope;
4. which files you expect to inspect or modify.

Then perform only Task 1.
```
