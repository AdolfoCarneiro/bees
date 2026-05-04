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

## UV-Template Requirement For Entity And Model Textures

Text-only prompts are not acceptable as the sole input for entity textures or model textures that must fit a specific UV layout.

Text prompts describe color and feel well. They do not communicate UV island positions, transparency regions, or canvas layout. Feeding a text-only prompt to a text-to-image tool produces a free-form sprite, not a UV-compatible skin.

For any asset that maps to a model UV layout, the workflow must be:

```text
1. Identify the model used by the asset.
2. Obtain or create the UV template image for that model.
3. Save the UV template under docs/art/templates/.
4. Create an asset prompt that explicitly references the template.
5. Instruct the image generator to use the template as a fixed canvas — fill regions, do not rearrange.
6. Validate the generated result against the UV template before accepting it.
```

Asset types that require a UV template:

```text
- bee entity textures
- custom entity textures
- custom block model textures
- any Blockbench model texture
```

Text-only prompts may be used for concept art, color palette exploration, or style references. They must not be used as the sole prompt for a final UV-mapped texture.

## Bee Texture Rule

All default bee species textures must use the default bee UV template:

```text
docs/art/templates/bee/default_bee_uv_template.png
```

This template does not yet exist. It must be created before any species texture generation begins.

The template must show:

```text
- exact 64x32 canvas
- body region boundary
- head region boundary
- wing region boundaries
- leg region boundaries
- stinger/antennae region boundaries
- transparent background outside all UV regions
```

Every bee species prompt must:

```text
- reference the UV template path
- instruct the generator to use the template as a fixed canvas
- tell the generator not to move, resize, or rearrange UV islands
- tell the generator to preserve all transparent areas
- include a Rejection Criteria section
```

Prompts that do not reference the UV template must not be used for final texture generation.

## Prompt Document Template

Use this template when creating a new asset prompt.

The `## Image Generation Prompt` section is mandatory — it must contain a ready-to-paste block
that can be dropped directly into GPT Images, DALL-E, or a similar text-to-image tool without editing.
The `## Palette Notes` section must include approximate hex codes, not only verbal descriptions.

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

Core colors with approximate hex anchors:

- Base coat: {description} — approx. `#{HEX}`
- Stripes: {description} — approx. `#{HEX}`
- Outlines: {description} — approx. `#{HEX}`
- Belly/accent: {description} — approx. `#{HEX}`

{any additional color rules or exclusions}

## UV / Model Reference

{reference to model or UV layout this texture maps to, if applicable}

For bee entity textures: the canvas is 64×32 pixels and follows the vanilla Minecraft bee UV layout
(`minecraft:textures/entity/bee/bee.png`). Use the vanilla texture as UV template only — trace
the region boundaries, do not copy the pixel art. Key regions: body occupies the center-left of
the canvas, head faces across the top-left, wings span the top-right, stinger and antennae fill
the remaining small regions. Transparent background (PNG with alpha) outside all body regions.

## Usage

{where this asset is used — e.g. rendered on vanilla Bee entity when active species is Meadow}

## UV Template Reference

> **Required for entity textures.** If this asset maps to a model UV layout, a UV template
> must exist at `docs/art/templates/` before generation begins. Reference it explicitly below.

```text
UV template path: {e.g. docs/art/templates/bee/default_bee_uv_template.png — or N/A if not a model texture}
```

## Image Generation Prompt

> **This section is mandatory.** It must contain a ready-to-paste block for GPT Images, DALL-E,
> or a similar text-to-image tool. Do not leave it as a description — write the actual prompt text.

For **entity/model textures**, the prompt must reference the UV template and instruct the
generator to fill UV regions, not generate a free-form sprite. Use the format below:

---

```
This is not a request for a sprite, icon, or character portrait.

Use the attached UV template [{UV_TEMPLATE_PATH}] as the exact fixed canvas.
Fill the UV regions with the {SPECIES_NAME} Bee color scheme described below.
Do not move, resize, rearrange, crop, rotate, or reinterpret any UV island.
Keep all transparent areas transparent.
Output must be exactly {WIDTHx HEIGHT} pixels.

Color scheme:
- Body region: {DESCRIPTION}, approximately {HEX1}.
- Stripe region: {DESCRIPTION}, approximately {HEX2}.
- Outline region: {DESCRIPTION}, approximately {HEX3}.
- Wing region: {DESCRIPTION}, approximately {HEX4} with partial transparency.
- {any additional regions}

Feel: {one sentence — e.g. sun-bleached and harsh, earthy and woodland, warm and friendly}.
Do not use: {comma-separated exclusions — e.g. greens, blues, pure black, anti-aliasing}.
Style: flat pixel art, no anti-aliasing, no gradients, hard edges, limited palette of 6 to 8 colors.
```

---

> **If a UV template image cannot be attached** (tool limitation), also include this line:
> `The UV layout is: body center-left, head faces top-left, wings top-right, legs/stinger lower edges, transparent background everywhere outside UV regions.`

## Species Identity

```text
Name: {display name}
Role: {role in genetics progression}
Biomes: {biomes}
Feel: {one-line visual identity}
```

## Do

- {specific things the image should achieve}

## Don't

- {specific things to avoid}

## Acceptance Criteria

- [ ] File exists at target path.
- [ ] Texture dimensions match expected size.
- [ ] Visually distinguishable from other species textures.
- [ ] No copied third-party assets.
- [ ] Dev placeholder replaced or removed.

## Rejection Criteria

Reject the generated result if:

- [ ] It looks like a free-form bee sprite, icon, or character portrait.
- [ ] It rearranges, resizes, or reinterprets UV islands.
- [ ] It creates detached decorative pixels outside expected UV regions.
- [ ] It does not visually align with the UV template layout.
- [ ] It fills transparent background areas with color.
- [ ] It uses anti-aliasing, gradients, or more than ~8 colors.
- [ ] It cannot be applied directly to the target model without manual rework.

## Status

- [ ] UV template available at referenced path
- [ ] Prompt created
- [ ] Asset generated/provided
- [ ] Asset validated against UV template
- [ ] Asset integrated
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