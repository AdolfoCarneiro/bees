# 12 — Visual Species System Implementation

## 1. Purpose

This document defines the implementation plan for **Phase 12 — Visual Species System** of Curious Bees.

Phase 12 gives species their own visual identity without making custom models mandatory. The first implementation should use the standard bee model and choose a texture/skin from the bee active species. Custom models remain future/special-case work.

## 2. Phase Goal

By the end of this phase, the project should be able to:

```text
- store visual metadata on species definitions;
- load/resolve visual metadata from built-in and data-driven species;
- render vanilla Bee entities with a texture based on active species;
- fall back safely when a visual profile or asset is missing;
- create asset prompts for Meadow, Forest, Arid, Cultivated and Hardy and integrate final textures when provided;
- document asset naming conventions for future species.
```

## 3. Phase Non-goals

Do not implement:

```text
- custom model per species as the default path;
- Blockbench models for every bee;
- hybrid texture blending;
- dynamic AI image generation inside the mod;
- resource bees or new species trees;
- Fabric rendering;
- custom bee entity replacement;
- lifecycle/death/larvae systems;
- temperature/humidity/environment rendering.
```

## 4. Required Inputs

Before implementing, read:

```text
CLAUDE.md
docs/README.md
docs/post-mvp/11-post-mvp-productization-roadmap.md
docs/post-mvp/12-visual-species-system.md
docs/post-mvp/15-content-and-asset-pipeline.md
docs/mvp/02-technical-architecture.md
docs/mvp/03-genetics-system-spec.md
```

If implementation touches data-driven content, also read the existing Phase 8 docs and current content loader code.

## 5. Expected Outputs

```text
SpeciesVisualDefinition or equivalent
visual metadata fields in species definitions/DTOs
built-in visual profiles for Meadow, Forest, Arid, Cultivated, Hardy
validation for invalid visual references
NeoForge client-side texture resolution by active species
fallback texture handling
asset prompts for all MVP species under docs/art/prompts/species/
final species textures integrated when provided by user
updated asset/content authoring docs
```

## 6. Architecture Rules

### 6.1 Visual metadata is content; rendering is platform/client

Good:

```text
common/content stores string IDs such as curiousbees:bee/meadow.
neoforge/client resolves those IDs to ResourceLocation/texture paths.
```

Bad:

```text
common/genetics imports ResourceLocation or renderer classes.
```

### 6.2 Use active species for initial visuals

Initial render rule:

```text
Rendered bee texture = active species visual profile.
```

Do not implement hybrid blending yet.

### 6.3 Fallbacks are required

Missing genome, species, visual metadata, or texture must fall back to a default Curious Bees bee texture and should log/debug clearly in development.

### 6.4 Do not replace vanilla Bee

This phase renders vanilla Bee entities differently. It should not introduce a new bee entity hierarchy.

## 7. Recommended Source and Asset Structure

Possible common packages:

```text
common/src/main/java/.../content/visual/SpeciesVisualDefinition.java
common/src/main/java/.../content/species/
common/src/main/java/.../content/data/
common/src/main/java/.../content/conversion/
common/src/main/java/.../content/validation/
common/src/main/java/.../content/builtin/
```

Possible NeoForge packages:

```text
neoforge/src/main/java/.../client/render/
neoforge/src/main/java/.../client/texture/
```

Suggested assets:

```text
neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/default.png
neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/meadow.png
neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/forest.png
neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/arid.png
neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/cultivated.png
neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/hardy.png
```

## 8. Initial Visual Metadata Shape

Conceptual JSON:

```json
{
  "visual": {
    "model": "curiousbees:bee/default",
    "texture": "curiousbees:textures/entity/bee/meadow.png"
  }
}
```

Alternative simplified shape is acceptable if the runtime can resolve it safely:

```json
{
  "visual": {
    "profile": "curiousbees:bee/meadow"
  }
}
```

The chosen shape must keep common code Minecraft-independent and must not block future custom models.

## 9. Task Breakdown

## Task 1 — Review Existing Species Content Pipeline

### Objective

Understand where species definitions currently live and how built-ins/data-driven species are converted into runtime definitions.

### Scope

Inspect species/content/data/conversion/validation/registry packages and NeoForge content integration.

### Non-goals

Do not change files in this task.

### Acceptance Criteria

```text
- Identify runtime species definition class.
- Identify species DTOs if they exist.
- Identify built-in species creation points.
- Identify validation location.
- Identify client rendering extension point.
```

### Prompt for Claude Code

```text
Read CLAUDE.md and docs/post-mvp/12-visual-species-system.md.

Focus only on Task 1 from docs/implementation/12-visual-species-system-implementation.md.

Review the existing species content pipeline and produce a short implementation plan. Do not modify files.

Summarize where species definitions are modeled, where built-ins are created, where JSON/data-driven species are loaded or converted, where validation should happen, and where NeoForge client rendering should resolve species visuals.
```

## Verification Gate 1 — Pipeline Review

```text
- Do we know which classes need visual metadata?
- Do we know where tests belong?
- Do we know where client rendering belongs?
```

## Task 2 — Add Species Visual Definition Model

### Objective

Create a Minecraft-independent model for visual metadata.

### Scope

Implement `SpeciesVisualDefinition` or equivalent with string fields such as:

```text
modelId or modelProfileId
textureId or texturePath
optional fallback key
```

### Non-goals

Do not implement renderer registration, ResourceLocation storage in common, custom model loading, asset generation, or Blockbench integration.

### Acceptance Criteria

```text
- SpeciesVisualDefinition exists.
- It uses string IDs/paths, not Minecraft classes.
- It validates required fields.
- It supports default model + species texture strategy.
- Unit tests cover valid/invalid definitions.
```

### Prompt for Claude Code

```text
Read CLAUDE.md and docs/post-mvp/12-visual-species-system.md.

Focus only on Task 2 from docs/implementation/12-visual-species-system-implementation.md.

Implement a Minecraft-independent SpeciesVisualDefinition model using string identifiers.

Do not implement rendering, ResourceLocation usage, custom model loading, Blockbench support, assets or JSON loading in this task.

Add unit tests for valid and invalid visual definitions. Before coding, summarize the model fields and files you expect to modify.
```

## Task 3 — Attach Visual Metadata to Species Definitions

### Objective

Allow each species definition to carry visual metadata.

### Scope

Update runtime and built-in species definitions. Add built-in visual profiles for:

```text
Meadow
Forest
Arid
Cultivated
Hardy
```

### Non-goals

Do not implement renderer changes yet.

### Acceptance Criteria

```text
- Runtime species definitions expose visual metadata.
- All five built-in species have visual profiles.
- Existing species tests are updated.
- No new species or resource bees are added.
- common/genetics remains unchanged.
```

### Prompt for Claude Code

```text
Read docs/post-mvp/12-visual-species-system.md and Task 3 from docs/implementation/12-visual-species-system-implementation.md.

Add visual metadata to species definitions and built-in visual profiles for Meadow, Forest, Arid, Cultivated and Hardy.

Do not implement NeoForge rendering, assets, custom models, resource bees or new species.

Update tests to verify all built-in species have visual metadata. Before coding, list files you expect to modify.
```

## Task 4 — Extend Data-Driven Species DTOs and Validation

### Objective

Allow data-driven species definitions to declare visual metadata.

### Scope

Update species DTOs, conversion, fixtures, and validation.

### Non-goals

Do not implement asset existence validation unless it is already easy and isolated.

### Acceptance Criteria

```text
- Species JSON/DTO can express visual metadata.
- Conversion maps visual DTO to runtime SpeciesVisualDefinition.
- Validation rejects malformed visual metadata.
- Existing JSON fixtures are updated.
- Missing visual metadata uses the chosen fallback or validation rule.
```

### Prompt for Claude Code

```text
Read docs/post-mvp/12-visual-species-system.md and docs/post-mvp/15-content-and-asset-pipeline.md.

Focus only on Task 4 from docs/implementation/12-visual-species-system-implementation.md.

Extend species DTOs/conversion/validation to support visual metadata.

Do not implement NeoForge rendering, custom models, new species, resource bees or asset generation.

Add or update pure Java tests. Before coding, summarize the chosen fallback/required behavior for missing visual metadata.
```

## Task 5 — Implement NeoForge Species Texture Resolution

### Objective

Render vanilla Bee entities using the texture of their active species.

### Scope

Client-side code should:

```text
- read the bee genome from the entity;
- resolve active species ID;
- look up species visual profile;
- resolve texture path;
- fall back to default texture when needed.
```

### Non-goals

Do not implement custom bee entity, custom model per species, hybrid blending, Fabric renderer, new species, or gameplay changes.

### Acceptance Criteria

```text
- Meadow, Forest, Arid, Cultivated, and Hardy can render with different textures.
- Missing genome/species/visual profile uses fallback.
- Logic is isolated to NeoForge client/platform code.
```

### Prompt for Claude Code

```text
Read docs/post-mvp/12-visual-species-system.md.

Focus only on Task 5 from docs/implementation/12-visual-species-system-implementation.md.

Implement NeoForge client-side species texture resolution for vanilla Bee entities based on active species.

Use fallback texture behavior for missing genome/species/visual metadata.

Do not implement custom bee entities, custom models, hybrid blending, Fabric support, new species or gameplay changes.

Before coding, summarize how the renderer will access genome data and where texture resolution code will live.
```

## Task 6 — Create Bee UV Template And Species Asset Prompts

### Objective

Create the default bee UV template and UV-template-driven asset prompts for all MVP species textures.

Text-only prompts are not sufficient for entity textures that must fit a specific UV layout.
The template must be created first. Species prompts reference it.

### Scope

This task must produce:

```text
docs/art/templates/bee/default_bee_uv_template.png  ← prerequisite, must exist first
docs/art/prompts/species/textures-entity-bee-meadow.md
docs/art/prompts/species/textures-entity-bee-forest.md
docs/art/prompts/species/textures-entity-bee-arid.md
docs/art/prompts/species/textures-entity-bee-cultivated.md
docs/art/prompts/species/textures-entity-bee-hardy.md
```

The UV template (`default_bee_uv_template.png`) must show:

```text
- exact 64x32 canvas
- body region boundary
- head region boundary (all cube faces)
- wing region boundaries
- leg region boundaries
- stinger/antennae region boundaries
- transparent background outside all UV regions
- region labels if useful for clarity
```

Each species prompt must:

```text
- reference docs/art/templates/bee/default_bee_uv_template.png explicitly
- include a ready-to-paste Image Generation Prompt block
- instruct the generator to use the UV template as a fixed canvas and fill regions only
- instruct the generator not to move, resize, or rearrange UV islands
- instruct the generator to preserve all transparent areas
- include exact 64x32 output size requirement
- include a Rejection Criteria section
- include palette with approximate hex codes
```

When the user provides final generated textures, integrate them and validate all references.

### Non-goals

```text
- Do not create custom models for MVP species.
- Do not generate final placeholder textures as deliverables.
- Do not mark the task complete with temporary assets.
- Do not copy vanilla bee textures.
- Do not create resource bee visuals.
- Do not generate generic bee sprites — the prompt must specify UV texture fill, not sprite generation.
```

### Acceptance Criteria

```text
- default_bee_uv_template.png exists at docs/art/templates/bee/.
- A complete prompt exists for each MVP species under docs/art/prompts/species/.
- Each prompt references the UV template path.
- Each prompt's Image Generation Prompt block explicitly instructs UV-fill, not sprite generation.
- Each prompt includes a Rejection Criteria section.
- Each prompt includes palette hex codes.
- Generated final textures are integrated when provided by the user.
- Results that do not match the UV template layout are rejected, not integrated.
- No placeholder texture is accepted as final.
```

### Prompt for Claude Code

```text
Read CLAUDE.md, docs/art/asset-prompt-workflow.md, and docs/post-mvp/12-visual-species-system.md.

Focus only on Task 6 from docs/implementation/12-visual-species-system-implementation.md.

Do not generate images.

Step 1: Check whether docs/art/templates/bee/default_bee_uv_template.png exists.
If it does not exist, stop and report that the UV template must be created before proceeding.
The UV template is a prerequisite — do not create species prompts without it.

Step 2 (only if UV template exists): Create or update asset prompt files for the five MVP species
(Meadow, Forest, Arid, Cultivated, Hardy) following the template in docs/art/asset-prompt-workflow.md.

Each prompt must include: UV template reference, ready-to-paste Image Generation Prompt block
instructing UV-fill (not sprite generation), palette with hex codes, Rejection Criteria section,
target path, and acceptance checklist.

Do not create placeholder textures as final deliverables.

If final PNGs are not provided yet, stop after creating the prompts and report which assets
are waiting for user generation.
```

## Task 7 — Update Visual Authoring Documentation

### Objective

Document how future species should define visuals.

### Scope

Update relevant docs with visual metadata format, texture naming conventions, fallback behavior, standard model strategy, future custom model note, and no-copying rule.

### Acceptance Criteria

```text
- A content author can add a species texture using the docs.
- Docs say model-per-species is optional/future.
- Docs explain fallback behavior.
```

### Prompt for Claude Code

```text
Read docs/post-mvp/12-visual-species-system.md and docs/post-mvp/15-content-and-asset-pipeline.md.

Focus only on Task 7 from docs/implementation/12-visual-species-system-implementation.md.

Update visual authoring documentation for species texture conventions and visual metadata.

Do not modify code or assets in this task unless a broken link must be fixed.

Before editing, list the docs you plan to modify.
```

## 10. Phase Completion Checklist

```text
- Species visual metadata exists in runtime species definitions.
- Built-in species have visual profiles.
- Data-driven species can declare visual metadata.
- NeoForge client can render bees with active-species textures.
- Fallback texture behavior exists.
- Asset prompts exist for all current species; final textures are integrated when provided.
- Visual authoring docs are updated.
- No resource bees or large species tree were added.
- No custom model-per-species requirement was introduced.
```

## 11. Go / No-Go Checkpoint Before Phase 13

Do not move to Analyzer UX implementation until:

```text
- current species can be visually distinguished in-game or through verified client render path;
- missing visual metadata does not crash the client;
- visual metadata does not pollute common/genetics;
- asset naming conventions are documented.
```

## 12. AI Failure Modes to Watch

```text
- creating custom bee entities for species;
- making custom models mandatory;
- putting Minecraft classes in common/genetics;
- adding resource bees or new content while implementing visuals;
- copying assets from existing mods.
```

## 13. Recommended Commit Sequence

```text
content: add species visual definition model
content: add visual metadata to built-in species
content: support visual metadata in data-driven species
neoforge: resolve bee texture by active species
docs: add species asset prompts under docs/art/prompts/species/
assets: integrate final bee species textures when provided
docs: document species visual authoring
```

## 14. Handoff Prompt for Starting Phase 12

```text
Read CLAUDE.md and docs/README.md.

Then read:
- docs/post-mvp/11-post-mvp-productization-roadmap.md
- docs/post-mvp/12-visual-species-system.md
- docs/post-mvp/15-content-and-asset-pipeline.md
- docs/implementation/12-visual-species-system-implementation.md

Do not implement all of Phase 12 at once.

Start only with Task 1 — Review Existing Species Content Pipeline.

Before coding, summarize the goal, scope, out-of-scope items, and files/packages you expect to inspect.

Then perform Task 1 without changing files.
```
