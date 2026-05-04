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
Add a **prompt / asset request** where your team tracks art (GitHub issue, PR description, or a markdown file you agree on). Include: target resource path, size (e.g. 64x64), style notes, palette with hex codes, UV reference.
- DO NOT commit placeholder texture as final asset
- There is no fixed `docs/art/prompts/` layout in this repo right now

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

## 6. Asset tracking
If you keep an asset manifest or checklist, update it in the same place you track other textures (not a fixed path in this repo).

## Guardrails
- Do NOT implement without a texture prompt doc ready (wherever you track it)
- Do NOT add resource-output species without explicit phase approval
- Read docs/architecture.md §7 for trait defaults reference
- Read CLAUDE.md (Art section) for placeholder / final asset rules
