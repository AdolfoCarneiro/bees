# Asset Prompt Workflow

## Purpose

This document defines how visual assets should be requested and integrated into Curious Bees.

AI coding agents must not silently create placeholder textures as final deliverables. When an implementation task requires a new asset, the agent creates a prompt document and waits for the user to provide the asset.

The user will use the prompt with an external image generation tool or pixel art software, then provide the resulting asset back to the agent for integration.

## Core Rule

Do not create placeholder assets as final deliverables.

Allowed:

- temporary fallback texture for development safety (must be marked as placeholder);
- missing asset warning in logs;
- generated prompt files under `docs/art/prompts/`;
- asset manifest updates;
- validation scripts.

Not allowed as final completion:

- flat-color placeholder PNG committed as a species texture;
- obviously temporary bee texture counted as done;
- “TODO texture later” counted as completed visual task;
- purple/black missing texture accepted as a result.

## Workflow

### Step 1 — Implementation reaches an asset requirement

During implementation, when a new visual asset is needed, the agent should:

1. Note the required asset path and purpose.
2. Create a prompt document under `docs/art/prompts/`.
3. Reference it in the implementation summary.
4. Do **not** commit a final placeholder as a completed deliverable.

A temporary fallback texture may be created for development safety only — so the game does not crash — but it must be clearly marked as a dev placeholder and must not count as a completed asset.

### Step 2 — Prompt document

Prompt documents live under:

```text
docs/art/prompts/
```

Naming convention:

```text
{asset-path-slug}.md
```

For example:

```text
docs/art/prompts/textures-entity-bee-meadow.md
```

### Step 3 — User generates and provides the asset

The user generates or creates the asset using AI tools, pixel art software, or a combination.

Rules for final assets:

```text
- Must be original work.
- Must not copy textures from Productive Bees, Complicated Bees, Forestry, or other mods.
- Must not be taken from screenshots or mod JARs.
- AI-generated images may be used as drafts, but must be converted to Minecraft-style intentionally.
- Source prompts or reference material may be saved in docs/art/sources/ for traceability.
```

### Step 4 — Integration

Once the user provides the asset:

1. Place it at the documented target path.
2. Verify the reference in the species definition, renderer, or screen matches.
3. Remove or replace any dev placeholder that was left in place.
4. Mark the asset prompt as resolved.

## Required Prompt Location

Asset prompts must be written under:

```text
docs/art/prompts/
```

Resolved prompts may be archived or marked with a `[RESOLVED]` prefix rather than deleted, so the design intent is preserved.

## Prompt Document Template

Use this template when creating a new asset prompt:

```markdown
# Asset Prompt — {asset description}

## Target Path

{exact asset path in the repo, e.g. neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/meadow.png}

## Asset Type

{e.g. entity texture / GUI background / item icon / block texture}

## Size

{e.g. 64x32 pixels — standard Minecraft bee texture UV layout}

## Style

Minecraft Java Edition pixel art style.
{additional style notes}

## Palette Notes

{palette guidance, e.g. warm yellows, browns, soft golds — avoid pure black outlines}

## UV / Model Reference

{reference to model or UV layout this texture maps to, if applicable}

## Usage

{where this asset is used — e.g. rendered on vanilla Bee entity when active species is Meadow}

## Acceptance Criteria

- [ ] File exists at target path.
- [ ] Texture dimensions match expected size.
- [ ] Visually distinguishable from other species textures.
- [ ] No copied third-party assets.
- [ ] Dev placeholder replaced or removed.

## Status

- [ ] Prompt created
- [ ] Asset generated/provided
- [ ] Asset integrated and validated
```

## Dev Placeholder Rule

A dev placeholder texture is allowed when:

```text
- the implementation would otherwise crash the client due to a missing texture;
- it uses a clearly temporary name or suffix such as _placeholder;
- it is tracked in a corresponding prompt document as unresolved.
```

A dev placeholder must not be:

```text
- used as the final committed texture for a species, item, block, or GUI;
- committed silently without a corresponding prompt document;
- counted as completing the visual task.
```

## Related Docs

- `docs/post-mvp/12-visual-species-system.md`
- `docs/post-mvp/15-content-and-asset-pipeline.md`
- `docs/implementation/12-visual-species-system-implementation.md`
- `docs/implementation/15-content-and-asset-pipeline-implementation.md`