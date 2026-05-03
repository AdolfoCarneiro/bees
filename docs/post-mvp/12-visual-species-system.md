# 12 — Visual Species System

> Status: Post-MVP planning document.
> This document defines how Curious Bees should represent bee species visually after the MVP validation phase.
>
> **Depends on `docs/post-mvp/10-5-species-hives-and-habitat-system.md`.** Visual definitions for
> habitat-bearing species (Meadow, Forest, Arid) must align with the hive textures defined there.
> The bee entity texture and the hive block texture are separate assets, generated from
> separate UV templates.

## 1. Purpose

The MVP proved that vanilla bees can carry genomes, inherit traits, mutate, be analyzed, and participate in production.

The next phase must make species visible and recognizable in-game.

The goal is not to replace the genetic system with cosmetics. The goal is to make the genetic system readable, satisfying, and expandable by giving each species a clear visual identity.

## 2. Design Goal

Every bee species should eventually have a visual identity.

For the post-MVP productization phase, this means:

```txt
- Use the vanilla bee shape/model as the default visual base.
- Give each species its own texture/skin.
- Render the bee according to its active species allele.
- Keep custom models possible for special future species.
- Avoid making every species require a Blockbench model.
```

The initial visual system should support five MVP species:

```txt
Meadow
Forest
Arid
Cultivated
Hardy
```

## 3. Design Pillars

### 3.1 Visual Identity Supports Genetics

The player should be able to look at a bee and have a rough sense of what species it expresses.

The bee's visible appearance should initially come from the active species allele.

Example:

```txt
Genome species pair: Cultivated / Forest
Active species: Cultivated
Visible skin: Cultivated Bee
```

The inactive allele remains genetically important, but it does not need to be visually represented in the first version.

### 3.2 Start With Textures, Not Full Custom Models

The first implementation should use species-specific textures on a shared bee model.

Good first target:

```txt
One vanilla-like bee model
Five species textures
Fallback texture
Renderer chooses texture by active species
```

Do not block the productization phase on custom Blockbench models for every species.

### 3.3 Support Special Models Later

Some future species may deserve custom geometry.

Examples:

```txt
- Crystal-like bees
- Nether bees with unusual wings or glow effects
- End bees with elongated or alien silhouettes
- Mechanical/industrial bees
- Magic bees with particles or overlays
```

The visual system should allow this later without requiring it now.

### 3.4 Data and Assets Are Separate But Connected

A species definition can reference visual assets, but JSON data alone does not create art.

A complete species needs:

```txt
- data definition
- texture or model references
- lang entries
- item/block assets if applicable
- fallback behavior if assets are missing
```

## 4. Visual Strategy

Curious Bees should use a hybrid visual strategy.

### 4.1 Default Strategy: Shared Model + Species Texture

Most species use:

```txt
- shared bee model
- species-specific texture
```

Example conceptual species visual definition:

```json
{
  "visual": {
    "model": "curiousbees:bee/default",
    "texture": "curiousbees:textures/entity/bee/meadow.png"
  }
}
```

### 4.2 Advanced Strategy: Custom Model + Texture

Special species may later use:

```txt
- custom entity model
- custom texture
- optional overlays or emissive effects
```

Example conceptual species visual definition:

```json
{
  "visual": {
    "model": "curiousbees:bee/crystal",
    "texture": "curiousbees:textures/entity/bee/crystal.png",
    "emissiveTexture": "curiousbees:textures/entity/bee/crystal_emissive.png"
  }
}
```

This is future scope. The first implementation should not require custom models.

## Asset Creation Policy

Species visuals must not be completed using placeholder textures.

For each species texture required by this phase, the implementation must create an asset prompt under:

```text
docs/art/prompts/species/
```

The prompt must describe the species identity, target texture path, expected dimensions, palette, vanilla/default bee model compatibility, and UV/template requirements.

## Species Texture Generation Rule

Species textures are UV-mapped entity textures. They are not free-form sprites, icons, or
character portraits.

A text-only prompt fed to a text-to-image tool will produce a sprite-style output that does not
fit the bee model UV layout. Body parts, head, wings, and legs will not align to the correct
canvas regions. The result cannot be used as a final texture without extensive manual correction.

The correct generation workflow for bee species textures is:

```text
UV template first → prompt referencing template → fill UV regions → validate → integrate
```

This means:

```text
1. A default bee UV template must exist at docs/art/templates/bee/default_bee_uv_template.png
   before any species texture is generated.
2. Every species texture prompt must attach or reference this template.
3. The generation prompt must explicitly instruct the tool to fill UV regions, not create a sprite.
4. The result must be validated against the UV template before it is accepted.
5. Results that rearrange, resize, or reinterpret UV regions must be rejected.
```

The default bee UV template is a prerequisite for all MVP species textures. It must be created
as part of Task 6 in the visual species system implementation plan.

A species visual task is complete only when:

```text
- the generated texture exists at the expected path;
- the visual definition points to it;
- fallback behavior exists for missing assets;
- the asset is not a temporary placeholder.
```

## 5. Active Species Rendering Rule

Initial rendering rule:

```txt
A bee is rendered using the visual profile of its active species allele.
```

Examples:

```txt
Meadow / Meadow -> Meadow texture
Cultivated / Forest -> Cultivated texture, if Cultivated is active
Forest / Cultivated -> Forest texture, if Forest is active
Hardy / Arid -> Hardy texture, if Hardy is active
```

This keeps rendering easy to understand and consistent with the current genetics model.

## 6. Hybrid Visuals

Hybrid-specific visuals are future scope.

Do not implement these in the first visual pass:

```txt
- blended textures
- mixed color inheritance
- split-body hybrid patterns
- inactive allele overlays
- dynamic procedural bee skins
```

These ideas are interesting, but they add rendering and asset complexity too early.

Future possible hybrid visual behaviors:

```txt
- optional overlay based on inactive species
- subtle pattern showing hybrid state
- analyzer-only hybrid icon rather than visible skin change
- special visuals for stable hybrid lines
```

## 7. Visual Metadata

Species definitions should eventually support visual metadata.

Initial conceptual fields:

```txt
visual.model
visual.texture
visual.fallbackTexture
visual.tintColor optional
visual.icon optional
```

Recommended MVP-plus minimum:

```txt
visual.texture
```

The renderer can use a built-in default bee model unless a custom model is explicitly supported later.

## 8. Asset Naming Conventions

Use stable, predictable paths.

Recommended entity textures:

```txt
assets/curiousbees/textures/entity/bee/meadow.png
assets/curiousbees/textures/entity/bee/forest.png
assets/curiousbees/textures/entity/bee/arid.png
assets/curiousbees/textures/entity/bee/cultivated.png
assets/curiousbees/textures/entity/bee/hardy.png
assets/curiousbees/textures/entity/bee/fallback.png
```

Optional future variants:

```txt
assets/curiousbees/textures/entity/bee/meadow_angry.png
assets/curiousbees/textures/entity/bee/meadow_nectar.png
assets/curiousbees/textures/entity/bee/meadow_emissive.png
```

Do not require all variants in the first version.

## 9. Fallback Behavior

The game should not crash if a species has missing or invalid visual metadata.

Recommended fallback order:

```txt
1. Species texture if present.
2. Species fallback texture if present.
3. Curious Bees generic fallback bee texture.
4. Vanilla bee texture as final fallback.
```

During development, missing visuals should log clear warnings.

Example warning:

```txt
Missing visual texture for species curiousbees:resilient. Using fallback bee texture.
```

## 10. Client/Platform Boundaries

The genetics core must not know about models, textures, renderers, Minecraft resources, or client classes.

Correct separation:

```txt
common/content:
- species visual metadata as plain IDs/strings

neoforge/client:
- renderer integration
- texture resolution
- fallback handling
```

Do not add Minecraft rendering imports to `common/genetics`.

## 11. Blockbench Role

Blockbench should be used when a species needs a custom model, not for every species by default.

Recommended near-term approach:

```txt
- Generate/edit species textures first.
- Keep the vanilla-like model.
- Create Blockbench templates later for special species.
```

Potential future artifacts:

```txt
art/blockbench/bee_default_template.bbmodel
art/blockbench/special_species_template.bbmodel
art/guides/bee_texture_template.png
```

## 12. AI-Assisted Asset Workflow

The project may use AI tools to help create concept art or texture ideas, but final assets should be reviewed and adapted to Minecraft style.

Suggested workflow:

```txt
1. Define species identity in a short art brief.
2. Generate concept references or palettes.
3. Convert into Minecraft-compatible pixel texture.
4. Review in-game scale/readability.
5. Commit final PNG and source files if available.
```

Do not directly copy assets from other mods.

## 13. Initial Species Visual Direction

### Meadow Bee

Visual direction:

```txt
- warm yellow/cream base
- flower/plains identity
- friendly starter look
```

### Forest Bee

Visual direction:

```txt
- green/brown accents
- leaf/forest identity
- natural and earthy
```

### Arid Bee

Visual direction:

```txt
- sand/orange/desert tones
- dry climate identity
- slightly harsher look
```

### Cultivated Bee

Visual direction:

```txt
- cleaner/brighter variant
- managed beekeeping feel
- first successful mutation should feel rewarding
```

### Hardy Bee

Visual direction:

```txt
- stronger contrast
- rugged/resilient feel
- less delicate than starter bees
```

## 14. Out of Scope For This Phase

Do not implement yet:

```txt
- Custom model for every species.
- Procedural hybrid textures.
- Advanced emissive rendering.
- Dynamic species coloration inheritance.
- Full animation pipeline.
- Visual mutation effects beyond simple particles/feedback.
- Resource bee visual taxonomy.
```

## 15. Implementation Notes For AI Agents

When implementing this system:

```txt
Scope:
- Add visual metadata support to species definitions.
- Add client-side texture selection by active species.
- Add fallback texture handling.
- Create asset prompts for MVP species under docs/art/prompts/species/ and integrate final textures when provided.

Out of scope:
- Custom models for all species.
- Procedural hybrid visuals.
- Resource bees.
- Lifecycle systems.
- Environment simulation.

Do not:
- Add rendering code to common/genetics.
- Hardcode species visuals throughout random classes.
- Copy textures, names, or assets from other mods.
```

## 16. Acceptance Criteria

This phase is successful when:

```txt
- Each MVP species can have a distinct texture.
- Bee rendering uses active species visual metadata.
- Missing species visuals fall back safely.
- Visual metadata does not pollute the genetics core.
- Asset paths and naming conventions are documented.
- Future custom models remain possible but not required.
```
