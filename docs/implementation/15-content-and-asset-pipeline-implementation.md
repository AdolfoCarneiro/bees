# 15 — Content and Asset Pipeline Implementation

## 1. Purpose

This document defines the implementation plan for **Phase 15 — Content and Asset Pipeline** of Curious Bees.

The MVP introduced data-driven content. Phase 15 connects that content direction to the visual and asset requirements of a real mod.

Main idea:

```text
A species is not complete just because it has data.
A complete species needs gameplay data plus assets and localization.
```

## 2. Phase Goal

Create a practical pipeline for adding future species with both data and visuals, without adding a large content tree yet.

By the end of this phase, the project should have:

```text
- documented species content pack structure;
- documented resource pack / asset conventions;
- visual metadata included in species authoring examples;
- fallback behavior for missing visuals;
- validation or debug reporting for incomplete species definitions;
- templates for future species data and assets;
- guidance for AI-assisted asset creation without copying other mods;
- clear boundary between this foundation phase and future content expansion.
```

## 3. Phase Non-goals

Do not implement:

```text
- resource bees;
- first expanded species branch;
- large mutation tree;
- mod compatibility bees;
- full external addon SDK;
- in-game AI image generation;
- automatic asset generation pipeline as mod runtime;
- custom model per species as mandatory;
- Fabric content pipeline parity.
```

This phase prepares expansion. It does not perform real content expansion yet.

## 4. Required Inputs

Before implementing, read:

```text
CLAUDE.md
docs/README.md
docs/post-mvp/11-post-mvp-productization-roadmap.md
docs/post-mvp/12-visual-species-system.md
docs/post-mvp/15-content-and-asset-pipeline.md
docs/mvp/02-technical-architecture.md
docs/mvp/05-content-design-spec.md
```

If touching data-driven content loading, read existing Phase 8 implementation/review docs and current loader code.

## 5. Expected Outputs

```text
species JSON template
mutation JSON template
production JSON template
trait JSON template if needed
visual metadata examples
asset folder conventions
texture naming conventions
localization conventions
fallback behavior documentation
content pack example structure
resource pack example structure
optional debug command/report for loaded species visuals
updated content authoring guide
```

## 6. Architecture Rules

### 6.1 Data pack + resource pack are separate but related

Data definitions:

```text
species genetics
traits
mutations
production
visual metadata references
```

Resource/assets:

```text
textures
models
language entries
UI icons if needed
```

### 6.2 Built-in content uses the same rules

Built-in species should declare visual metadata and use documented asset names. Avoid secret hardcoded paths content authors cannot follow.

### 6.3 Missing assets must have fallbacks

Expected behavior:

```text
missing texture -> fallback texture
missing model -> fallback model
invalid required data -> content validation error
optional visual missing -> fallback + warning, if that is the chosen rule
```

### 6.4 No content expansion yet

This phase may add examples/templates, but should not introduce the first post-MVP branch or resource bee progression.

## 7. Suggested Repository Structure

Possible documentation structure:

```text
docs/content/
├── species-authoring-guide.md
├── asset-authoring-guide.md
├── content-pack-template.md
└── validation-and-debugging.md
```

Possible templates:

```text
docs/templates/species-template.json
docs/templates/mutation-template.json
docs/templates/production-template.json
docs/templates/bee-texture-naming.md
```

Possible asset paths:

```text
neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/default.png
neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/meadow.png
```

## 8. Species Completion Definition

A future species should be considered complete only when it has:

```text
- species definition;
- default trait tendencies;
- visual profile;
- texture asset or intentional fallback;
- localization entry;
- production profile if it produces anything;
- mutation source or spawn/source definition;
- progression role notes;
- tests or validation coverage where applicable.
```

## 9. AI-Assisted Asset Creation Guidance

AI-assisted tools may be used to create draft assets, but committed assets must be original project assets.

Rules:

```text
- Do not copy Productive Bees, Complicated Bees, Forestry or other mod assets.
- Do not copy textures from screenshots or jars.
- Use AI-generated images only as drafts/source inspiration when legally safe.
- Convert drafts into Minecraft-style textures intentionally.
- Keep source prompts/references in docs/art or internal art folder if useful.
- Final assets should be reviewed for originality and consistency.
```

The mod runtime should not depend on an AI image generation service.

### UV-Mapped Textures Require a Template

Text-only prompts are not sufficient for entity textures or model textures that must fit a specific
UV layout. A text prompt describes color and feel. It does not communicate UV island positions,
transparent regions, or canvas structure.

Feeding a text-only prompt to a text-to-image tool for a UV-mapped texture produces a free-form
sprite. The result will have parts in wrong positions, will not align to the model, and cannot be
used directly as a texture.

For all entity textures and model textures, the pipeline must be:

```text
UV template → prompt referencing template → generator fills UV regions → validate → integrate
```

Validation rules for UV-mapped texture results:

```text
- Reject if it looks like a free-form sprite rather than a UV skin.
- Reject if UV islands have been moved, resized, or rearranged.
- Reject if transparent background areas are filled with color.
- Reject if it uses anti-aliasing, gradients, or more than ~8 colors.
- Reject if body parts do not align to the expected UV regions.
- Accept only if the result can be applied to the model without rework.
```

For bee entity textures, the required UV template path is:

```text
docs/art/templates/bee/default_bee_uv_template.png
```

This template must be created before any species texture generation begins.

## 10. Task Breakdown

## Task 1 — Review Existing Data-Driven Content Pipeline

### Objective

Understand current content loader, DTOs, validation, conversion, examples, and authoring docs.

### Scope

Inspect content/data/json/loading/registry/validation/conversion packages, NeoForge content integration, authoring guide, and Phase 8 docs.

### Non-goals

Do not modify code.

### Acceptance Criteria

```text
- Identify current species/mutation/production JSON formats.
- Identify where visual metadata should be documented.
- Identify where validation can report missing/incomplete visual data.
- Identify existing examples/templates.
```

### Prompt for Claude Code

```text
Read CLAUDE.md, docs/post-mvp/15-content-and-asset-pipeline.md and existing Phase 8 content loading docs.

Focus only on Task 1 from docs/implementation/15-content-and-asset-pipeline-implementation.md.

Review the existing data-driven content pipeline and produce a short plan for adding content/asset authoring conventions.

Do not modify files.

Summarize current content formats, validation points, examples, and docs that need updates.
```

## Task 2 — Define Species Content Pack Structure

### Objective

Document expected structure for adding a species with data and assets.

### Scope

Create/update docs explaining where species JSON, mutation JSON, production JSON, textures, visual metadata, and localization go.

### Non-goals

Do not implement new species.

### Acceptance Criteria

```text
- Docs show a complete species content pack layout.
- Docs explain data pack vs resource pack split.
- Docs include one minimal species example.
- Docs warn against copied third-party assets.
```

### Prompt for Claude Code

```text
Read docs/post-mvp/15-content-and-asset-pipeline.md.

Focus only on Task 2 from docs/implementation/15-content-and-asset-pipeline-implementation.md.

Document the expected content pack structure for adding future species with data and assets.

Do not implement new species, resource bees, loaders, assets or code.

Before editing, list docs you plan to create or modify.
```

## Task 3 — Add or Update JSON Templates

### Objective

Provide templates for future species authoring.

### Scope

Create/update templates for species, mutation, production, and trait if useful. Species template must include visual metadata.

### Non-goals

Do not add a real expanded species branch.

### Acceptance Criteria

```text
- Species template includes genetics and visual metadata.
- Mutation template includes parent/result/chance fields.
- Production template includes outputs and species references.
- Templates are clearly marked as templates/examples.
- Templates do not introduce resource bees.
```

### Prompt for Claude Code

```text
Read docs/post-mvp/15-content-and-asset-pipeline.md and existing content examples.

Focus only on Task 3 from docs/implementation/15-content-and-asset-pipeline-implementation.md.

Add or update JSON templates for future species, mutations and production definitions.

Species template must include visual metadata.

Do not add real new species, resource bees or gameplay behavior.

Before editing, list template files you plan to create or modify.
```

## Task 4 — Add Asset Naming Conventions

### Objective

Define stable naming conventions for species textures and future special models.

### Scope

Document conventions such as:

```text
textures/entity/bee/{species_path}.png
textures/entity/bee/default.png
models/entity/bee/{model_name}.json for future special models
lang keys for species display names
```

### Non-goals

Do not require custom models for every species.

### Acceptance Criteria

```text
- Texture naming convention is documented.
- Default/fallback texture path is documented.
- Future custom model convention is documented as optional/future.
- Blockbench source file convention is documented if desired.
```

### Prompt for Claude Code

```text
Read docs/post-mvp/12-visual-species-system.md and docs/post-mvp/15-content-and-asset-pipeline.md.

Focus only on Task 4 from docs/implementation/15-content-and-asset-pipeline-implementation.md.

Document asset naming conventions for bee species textures and future optional custom models.

Do not implement code, add new species or create final art assets in this task.

Before editing, list docs you plan to update.
```

## Task 5 — Add Validation or Debug Reporting for Visual Completeness

### Objective

Help developers detect species missing visual metadata or assets.

### Scope Options

Option A — Pure content validation:

```text
validate required visual metadata fields
warn/fail for malformed visual profiles
```

Option B — NeoForge/client debug report:

```text
command or log listing loaded species and visual profile resolution
report missing fallback usage
```

Start with the smallest useful option compatible with current code.

### Non-goals

Do not make normal gameplay crash because a texture file is missing.

### Acceptance Criteria

```text
- Invalid visual metadata is caught in validation.
- Missing visual metadata uses clear fallback behavior according to spec.
- Developers can identify species using fallback visuals.
- Tests cover pure validation where possible.
```

### Prompt for Claude Code

```text
Read docs/post-mvp/12-visual-species-system.md and docs/post-mvp/15-content-and-asset-pipeline.md.

Focus only on Task 5 from docs/implementation/15-content-and-asset-pipeline-implementation.md.

Add the smallest useful validation/debug reporting for species visual completeness.

Do not crash normal gameplay for missing texture assets. Do not add new species, resource bees or custom model systems.

Before coding, recommend Option A or Option B and explain why.
```

## Task 6 — Update Built-in Content to Follow Pipeline Rules

### Objective

Make existing built-in species follow the same asset/data conventions intended for future content.

### Scope

Verify/update built-in species visual metadata, texture paths, localization keys, content examples, and docs references.

### Non-goals

Do not add new species.

### Acceptance Criteria

```text
- Meadow, Forest, Arid, Cultivated and Hardy follow documented conventions.
- Built-in assets use stable names.
- Built-ins do not rely on secret paths that content authors cannot use.
```

### Prompt for Claude Code

```text
Read docs/post-mvp/15-content-and-asset-pipeline.md and docs/implementation/15-content-and-asset-pipeline-implementation.md.

Focus only on Task 6.

Verify and update built-in species/assets/localization to follow the documented content and asset pipeline conventions.

Do not add new species, resource bees, lifecycle mechanics or custom model requirements.

Before coding, list built-in species and asset references you will check.
```

## Task 7 — Add Content Authoring Checklist

### Objective

Create a checklist for adding future species safely.

### Scope

Checklist should include species definition, visual metadata, texture asset, localization, mutation/source, production definition, tests/validation, no copied assets, and progression role.

### Non-goals

Do not define resource bee progression tree here.

### Acceptance Criteria

```text
- Checklist exists.
- Checklist is linked from docs/README.md or content authoring guide.
- Checklist says resource bees are future expansion content.
```

### Prompt for Claude Code

```text
Read docs/post-mvp/15-content-and-asset-pipeline.md.

Focus only on Task 7 from docs/implementation/15-content-and-asset-pipeline-implementation.md.

Add a content authoring checklist for future species.

Do not define or add resource bees, large species trees or compatibility content.

Before editing, list where the checklist will be added.
```

## Task 8 — Add Missing Asset Report

### Objective

Create a local tool or script that reports required assets that do not yet exist.

### Scope

The tool should detect missing assets from:

```text
- species visual definitions;
- item model references;
- block model references;
- GUI texture references if listed in a manifest;
- prompt manifest if present.
```

### Acceptance Criteria

```text
- Tool reports missing required assets with expected target paths.
- Tool distinguishes missing final assets from intentional fallback assets.
- Tool can be run locally before release.
- Normal gameplay does not crash when the tool is not running.
```

### Prompt for Claude Code

```text
Read docs/post-mvp/15-content-and-asset-pipeline.md.

Focus only on Task 8 from docs/implementation/15-content-and-asset-pipeline-implementation.md.

Implement the smallest useful missing asset report for species visual definitions.

Do not crash normal gameplay. Do not add new species, resource bees or custom model systems.

Before coding, recommend whether this should be a debug command, a startup log scan, or a standalone script.
```

## Task 9 — Add Asset Prompt Manifest

### Objective

Create a manifest tracking all asset prompts and their current status.

### Suggested file

```text
docs/art/asset-manifest.md
```

### Required fields per entry

```text
- asset ID;
- asset type (entity texture / item icon / block texture / GUI);
- prompt path;
- target path;
- required size;
- status;
- notes.
```

### Status values

```text
- prompt-needed;
- prompt-created;
- generated;
- integrated;
- validated.
```

### Acceptance Criteria

```text
- Manifest tracks species, item, block, and GUI assets.
- It is clear which assets still need user generation.
- Placeholders are not listed as integrated or validated.
- Manifest is committed and kept up to date.
```

### Prompt for Claude Code

```text
Read docs/art/asset-prompt-workflow.md and docs/post-mvp/15-content-and-asset-pipeline.md.

Focus only on Task 9 from docs/implementation/15-content-and-asset-pipeline-implementation.md.

Create the initial asset prompt manifest at docs/art/asset-manifest.md.

Populate it with all known required assets from Phase 12, 13, and 14 with status prompt-needed or prompt-created as appropriate.

Do not create placeholder PNGs or generate images.
```

## 11. Phase Completion Checklist

```text
- Content pack structure is documented.
- Data pack vs resource pack responsibilities are clear.
- Species JSON templates include visual metadata.
- Asset naming conventions are documented.
- Missing/malformed visual metadata has validation or debug reporting.
- Built-in species follow the same conventions.
- Future content authoring checklist exists.
- No real content expansion or resource bee tree was added.
```

## 12. Go / No-Go Checkpoint Before Content Expansion

Do not begin first expanded species branch until:

```text
- visual species system works;
- analyzer UX is usable;
- apiary GUI/frame behavior is stable;
- content and asset pipeline is documented;
- adding a species requires a known set of data/assets/tests;
- the team intentionally starts a content expansion phase.
```

## 13. AI Failure Modes to Watch

```text
- adding real expansion content while creating templates;
- treating JSON alone as a complete species;
- requiring custom models too early;
- copying assets/configs/text from existing mods.
```

## 14. Recommended Commit Sequence

```text
docs: document species content pack structure
docs: add species and mutation templates with visual metadata
docs: add bee asset naming conventions
content: add validation for species visual metadata
content: add debug reporting for visual fallback usage
docs: add future species authoring checklist
```

## 15. Handoff Prompt for Starting Phase 15

```text
Read CLAUDE.md and docs/README.md.

Then read:
- docs/post-mvp/11-post-mvp-productization-roadmap.md
- docs/post-mvp/12-visual-species-system.md
- docs/post-mvp/15-content-and-asset-pipeline.md
- docs/implementation/15-content-and-asset-pipeline-implementation.md

Do not implement all of Phase 15 at once.

Start only with Task 1 — Review Existing Data-Driven Content Pipeline.

Before coding, summarize the goal, scope, out-of-scope items, and files/packages/docs you expect to inspect.

Then perform Task 1 without changing files.
```
