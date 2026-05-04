---
name: new-bee-species
description: Guia completo para adicionar uma nova espécie de abelha ao mod — data definition, texture prompt doc, habitat config, mutations, localization, e art manifest entry.
---

# New Bee Species Checklist

When adding a new bee species, ALL of the following must be created or updated:

## 1. Species Data Definition
File: `src/main/resources/data/curiousbees/species/<id>.json`
- id, display_name, dominance, default_traits
- visual profile (texture path)
- optional habitat definition

## 2. Texture Prompt Document
File: `docs/art/prompts/species/textures-entity-bee-<id>.md`
- Use template from `docs/art/templates/asset-request-template.md`
- Include: target path, size (64x64), style notes, palette with hex codes, UV reference
- DO NOT commit placeholder texture as final asset

## 3. Habitat Definition (if world-spawnable)
File: included in species JSON OR `data/curiousbees/habitat/<id>.json`
- biome tags for spawning
- nest block type

## 4. Mutation Entries
File: `src/main/resources/data/curiousbees/mutations/<mutation_id>.json`
- parent species IDs (order-independent)
- result species ID
- base mutation chance

## 5. Localization
File: `src/main/resources/assets/curiousbees/lang/en_us.json`
- `species.curiousbees.<id>` key with display name

## 6. Asset Manifest
File: `docs/art/asset-manifest.md`
- Add entry for the new texture (status: PENDING until asset provided)

## Guardrails
- Do NOT implement without a texture prompt doc ready
- Do NOT add resource-output species without explicit phase approval
- Read docs/architecture/05-content-design-spec.md for trait defaults reference
- Read docs/art/asset-prompt-workflow.md for the full asset workflow
