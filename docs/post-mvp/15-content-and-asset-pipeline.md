# 15 — Content and Asset Pipeline

> Status: Post-MVP planning document.
> This document defines how Curious Bees should add species, production, mutations, visuals, and assets after the MVP.
>
> **Species spec format reference:** the canonical "what makes a species complete" format
> (genetic + visual + optional habitat) is defined in
> `docs/post-mvp/10-5-species-bee-nests-and-habitat-system.md`, section 9. This document
> formalizes that format into authoring guides, templates, and validation rules.

## 1. Purpose

The MVP established a small content set and a data-driven direction.

The post-MVP phase must make content expansion safe, repeatable, and visually supported.

The key problem:

```txt
A species is not complete if it only has JSON data.
```

A real species needs:

```txt
- genetic definition
- mutation role
- production behavior
- visual identity
- language entries
- item/block assets if needed
- documentation and balance notes
```

## 2. Core Principle

Curious Bees should treat new species as content packages, not isolated JSON entries.

A complete species should include:

```txt
Data:
- species definition
- default genome/traits
- mutation definitions
- production definition
- spawn/progression metadata if applicable

Assets:
- entity texture
- optional model
- item icons/combs if needed
- language keys

Design:
- role in progression
- reason to exist
- relationship to other species
```

## 3. Data Pack + Resource Pack Mental Model

Data-driven content and visual assets should work together.

Conceptual split:

```txt
data/...
- genetics
- mutations
- production
- traits
- species metadata

assets/...
- textures
- models
- lang
- item models
- block models
```

For built-in Curious Bees content, both live inside the mod jar.

For future third-party expansion, species may require both a datapack and resource pack, or an addon mod that provides both.

## 4. Species Definition Requirements

A species definition should eventually contain or reference:

```txt
- id
- display name/lang key
- dominance
- default trait tendencies
- progression tier/category
- visual profile
- production profile reference
- mutation role/reference
```

Conceptual JSON shape:

```json
{
  "id": "curiousbees:meadow",
  "displayNameKey": "species.curiousbees.meadow",
  "dominance": "dominant",
  "defaultTraits": {
    "lifespan": "curiousbees:normal",
    "productivity": "curiousbees:normal",
    "fertility": "curiousbees:two",
    "flowerType": "curiousbees:flowers"
  },
  "visual": {
    "texture": "curiousbees:textures/entity/bee/meadow.png"
  },
  "production": "curiousbees:meadow_production"
}
```

This shape is conceptual. Actual implementation may use the existing DTO/model structure.

## 5. Visual Asset Requirements

For the post-MVP phase, every built-in species should have:

```txt
- entity texture
- language entry
- optional icon if needed by UI
```

Required texture path convention:

```txt
neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/{species_slug}.png
```

Examples:

```txt
neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/meadow.png
neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/forest.png
neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/arid.png
neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/cultivated.png
neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/hardy.png
```

Language files:

```txt
neoforge/src/main/resources/assets/curiousbees/lang/en_us.json
neoforge/src/main/resources/assets/curiousbees/lang/pt_br.json
```

Optional future paths:

```txt
assets/curiousbees/models/entity/bee/{model_id}.json
assets/curiousbees/textures/item/comb/{species_slug}_comb.png
assets/curiousbees/models/item/{species_slug}_comb.json
```

## 5a. Visual Authoring Conventions — Implemented

This section documents the visual system as implemented in Phase 12.

### Visual metadata format

Species definitions support an optional `visual` field.

Simple string form (preferred for default model):

```json
{
  "visual": "curiousbees:textures/entity/bee/meadow.png"
}
```

Object form (used when model must be specified):

```json
{
  "visual": {
    "texture": "curiousbees:textures/entity/bee/meadow.png",
    "model": "curiousbees:bee/custom_model"
  }
}
```

The `texture` field uses the full resource path including `textures/`. The `model` field is optional and defaults to `curiousbees:bee/default` (the shared vanilla-style bee model).

### Default visual strategy

All MVP species use the shared vanilla bee model with a species-specific texture:

```txt
- model: curiousbees:bee/default  (shared — uses vanilla bee geometry)
- texture: curiousbees:textures/entity/bee/{species}.png  (per species)
```

Custom models are supported by the `SpeciesVisualDefinition` class but are not required for any species by default. Custom models are future scope for special species only.

### Texture dimensions

Standard Minecraft bee texture UV layout:

```txt
64 × 32 pixels
PNG with alpha channel
Transparent background on non-body areas (wings, gaps)
```

### Fallback behavior

When a bee entity has no genome, unknown species, missing visual definition, or invalid texture ID, the renderer falls back to the vanilla bee texture:

```txt
minecraft:textures/entity/bee/bee.png
```

Fallback is logged at DEBUG level so developers can identify missing assets during development.

### How the renderer works

1. `CuriousBeeBeeRenderer` extends vanilla `BeeRenderer` and overrides `getTextureLocation`.
2. It calls `SpeciesTextureResolver.resolve(bee)` to get the species texture.
3. The resolver reads the genome via `BeeGenomeStorage`, gets the active species allele, looks up the species definition in `NeoForgeContentRegistry`, and reads the `SpeciesVisualDefinition.textureId()`.
4. If any step fails, the resolver returns the vanilla fallback.
5. When the species texture is returned, `CuriousBeeBeeRenderer` uses it instead of calling `super.getTextureLocation` — preserving vanilla state-based texture logic (angry, nectar) only for unregistered bees.

### Adding a new species texture

1. Define the texture path in the species definition `visual` field.
2. Place the texture PNG at the referenced path.
3. Ensure dimensions are 64×32 with correct UV layout.
4. Verify with `SpeciesTextureResolver` that the path resolves without fallback.

If the asset does not exist yet, create an asset prompt under `docs/art/prompts/species/` following the template in `docs/art/asset-prompt-workflow.md`.

## 6. Production Asset Requirements

Species production should be designed together with assets.

If a species produces a special comb, the content package should define:

```txt
- production definition
- comb item registration/reference
- comb item texture
- lang key
- processing result if applicable
```

Avoid adding production outputs with no visual or gameplay meaning.

## 7. Mutation Content Requirements

A mutation should define:

```txt
- id
- parent species A
- parent species B
- result species
- base chance
- result mode weights if supported
- optional simple requirements
```

For now, avoid adding complex requirements such as:

```txt
- temperature
- humidity
- precise climate simulation
- lifecycle stage
```

These are future systems.

## 8. Content Expansion Rule

Do not add a species just because an output is desired.

Each species should have at least one clear reason to exist:

```txt
- progression bridge
- genetic trait role
- visual identity
- production role
- environmental flavor
- future branch foundation
```

Bad content pattern:

```txt
Iron Bee exists only to output iron.
```

Better content pattern:

```txt
A species exists because it fits a progression branch, has a genetic role, has visual identity, and may eventually unlock resource production or processing.
```

## 9. Built-In Species Pack

The built-in Curious Bees content should be treated as the reference implementation.

Initial built-in species to polish:

```txt
Meadow
Forest
Arid
Cultivated
Hardy
```

Before adding many new species, each of these should have:

```txt
- clean species definition
- visual texture
- production definition
- analyzer display support
- apiary display support
- balance notes
```

## 10. First Expanded Species Branch

The first real content expansion should happen only after productization basics are in place.

Candidate branch:

```txt
Cultivated / Managed Branch
```

Possible planned species:

```txt
Cultivated + Hardy -> Resilient
Cultivated + Meadow -> Diligent
Resilient + Diligent -> Noble
Diligent + Noble -> Industrious
```

Do not implement this branch until visual, analyzer, apiary, and asset pipeline decisions are stable.

## 11. Resource Bees

Resource bees are not part of this post-MVP productization phase.

They belong to a later major content expansion phase.

Do not create detailed resource bee progression docs yet unless the project is explicitly entering the content expansion phase.

Current rule:

```txt
Resource bees are future content.
They should be designed as part of a deliberate species tree, not added ad hoc.
```

## 12. Asset Creation Workflow

Recommended workflow for new built-in species:

```txt
1. Write species design brief.
2. Define genetic role and progression position.
3. Define mutation path.
4. Define production behavior.
5. Define visual direction.
6. Create or generate visual concepts.
7. Convert concept into Minecraft-style texture.
8. Add species data and assets.
9. Test in analyzer and apiary UI.
10. Playtest breeding and production.
```

Do not start by generating random bee textures without species design.

## AI-Assisted Asset Prompt Pipeline

The content and asset pipeline is not only about paths and JSON references. It must also define how final assets are requested and delivered.

Curious Bees uses an AI-assisted but user-approved asset workflow:

```text
content definition
-> visual metadata
-> missing asset detection
-> asset prompt generation
-> external image generation by user
-> asset import
-> validation
-> final integration
```

Coding agents are responsible for:

```text
- detecting required assets;
- creating complete prompt files under docs/art/prompts/;
- integrating generated files when provided;
- validating dimensions, paths, alpha, and references;
- updating models, blockstates, lang entries, and visual definitions.
```

Coding agents are not responsible for:

```text
- generating final image files directly;
- inventing final placeholder art;
- marking asset tasks complete without generated assets.
```

### Prompt Files as Deliverables

For every missing required asset, the agent must create a prompt file under:

```text
docs/art/prompts/
```

Prompt files are project deliverables and should be committed. They document the intended visual identity of the asset and allow consistent regeneration later.

### Asset Completion Rule

An asset task is complete only when:

```text
- prompt exists;
- generated asset exists at the expected path;
- asset passes validation (dimensions, alpha, references);
- no temporary placeholder is used as final art.
```

## 13. AI-Assisted Content Workflow

AI tools can help with:

```txt
- species concepts
- naming exploration
- mutation tree brainstorming
- texture prompts
- palette ideas
- documentation drafts
- code implementation of small slices
```

AI tools should not:

```txt
- copy other mods' assets
- copy full mutation trees from other mods
- invent large content branches without review
- add many species in one implementation task
```

## 14. Blockbench Workflow

Blockbench should be used for:

```txt
- special species models
- block models
- apiary/block asset polish
- future non-standard bee silhouettes
```

For the first visual species pass, prefer texture-only species.

Potential future source layout:

```txt
art/blockbench/
art/textures/source/
art/species-briefs/
```

Committed game assets should live under normal `assets/curiousbees/...` paths.

## 15. Validation

The content pipeline should validate references clearly.

Validation should catch:

```txt
- unknown species IDs
- unknown trait IDs
- invalid dominance values
- invalid mutation chances
- production referencing missing species
- visual metadata missing required fields
- duplicate species IDs
```

Asset existence validation may initially be development-only because runtime resource pack resolution can be tricky.

Recommended dev warning:

```txt
Species curiousbees:resilient references missing texture curiousbees:textures/entity/bee/resilient.png
```

## 16. Suggested Folder Layout

Conceptual built-in content layout:

```txt
common/src/main/resources/data/curiousbees/species/
common/src/main/resources/data/curiousbees/mutations/
common/src/main/resources/data/curiousbees/production/

neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/
neoforge/src/main/resources/assets/curiousbees/textures/item/
neoforge/src/main/resources/assets/curiousbees/models/item/
neoforge/src/main/resources/assets/curiousbees/lang/
```

Actual module placement may differ depending on the current Gradle/multiloader setup.

## 17. Documentation For Content Authors

Eventually create:

```txt
docs/content-authoring/species-authoring-guide.md
docs/content-authoring/visual-assets-guide.md
docs/content-authoring/mutation-authoring-guide.md
docs/content-authoring/production-authoring-guide.md
```

These should be created after the internal format stabilizes.

## 18. Out of Scope For This Phase

Do not implement yet:

```txt
- Large content expansion.
- Resource bee progression tree.
- Full third-party addon API.
- Procedural texture generation at runtime.
- Custom models for every species.
- Complex environment/climate requirements.
- Lifecycle/larvae content requirements.
```

## 19. Implementation Notes For AI Agents

When working on content and assets:

```txt
Scope:
- Improve species definitions to reference visual metadata.
- Add asset conventions.
- Add fallback behavior.
- Keep MVP species polished first.

Out of scope:
- Dozens of new species.
- Resource bees.
- Full custom model system for every species.
- Lifecycle systems.
- Environment simulation.

Do not:
- Add species without visual/design role.
- Copy assets or mutation trees from existing mods.
- Scatter species-specific checks across services.
```

## 20. Acceptance Criteria

This phase is successful when:

```txt
- There is a clear definition of what makes a species complete.
- Species can reference visual metadata.
- MVP species have or can receive distinct textures.
- Missing visual assets have safe fallback behavior.
- Content and assets follow documented naming conventions.
- The project can add the next species branch without redesigning the pipeline.
```
