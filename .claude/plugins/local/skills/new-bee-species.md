---
name: new-bee-species
description: Guia completo para adicionar uma nova espécie de abelha ao mod — data definition, texture prompt doc, habitat config, mutations, localization, e art manifest entry.
---

# New Bee Species Checklist

When adding a new bee species, ALL of the following must be created or updated.

---

## 1. Species Data Definition (built-in path)

File: `common/src/main/java/com/curiousbees/common/content/builtin/BuiltinBeeSpecies.java`

Add:
1. `SPECIES_<ID>` allele constant with correct dominance.
2. `VISUAL_<ID>` constant via `SpeciesVisualDefinition.ofTexture(textureId, displayNameKey)`:
   - `textureId` = `"curiousbees:textures/entity/bee/<id>.png"`
   - `displayNameKey` = `"species.curiousbees.<id>"` (must match key added in step 4)
   - Add `// DEV-PLACEHOLDER` comment on the line if art is not final
3. `HABITAT_<ID>` constant (only for world-spawnable species).
4. `BeeSpeciesDefinition <ID>` constant with traits, biome notes, visual, and habitat.
5. Add the new definition to `BuiltinBeeSpecies.ALL`.

---

## 2. Texture

File: `neoforge/src/main/resources/assets/curiousbees/textures/entity/bee/<id>.png`

- 64×64 sprite, same UV layout as existing species textures.
- If not final art: commit with `// DEV-PLACEHOLDER` in `BuiltinBeeSpecies.java` (adjacent to the VISUAL constant).
- Tag in `BuiltinBeeSpecies.java` block comment: `// DEV-PLACEHOLDER: <id> texture — final art pending.`
- Do NOT commit a placeholder as if it were final art.

---

## 3. Localization

File: `neoforge/src/main/resources/assets/curiousbees/lang/en_us.json`

- Add `"species.curiousbees.<id>": "<Display Name>"` entry.

File: `common/src/test/java/com/curiousbees/common/content/lang/LangKeyCompletenessTest.java`

- Add `"species.curiousbees.<id>"` to `REQUIRED_KEYS` set.
- Run `./gradlew :common:test` to confirm no missing key failure.

---

## 4. Habitat Definition (if world-spawnable)

In `BuiltinBeeSpecies.java`:
- `HABITAT_<ID>` = `new SpeciesHabitatDefinition(nestBlockId, nestSideTexturePath, biomeList)`.

In `BuiltinContentData.java`:
- Add matching `SpeciesDefinitionData` entry with habitat fields.

Nest block registration (if species needs its own nest block):
- `ModBlocks` + `ModItems` — register `<id>_bee_nest`.
- Blockstate + model JSON files.
- Block textures (5 faces).

---

## 5. Mutation Entries (if reachable via mutation)

File: `common/src/main/java/com/curiousbees/common/content/builtin/BuiltinBeeMutations.java`

- Add a `MutationDefinition` for each parent pair that can produce this species.
- Add to `BuiltinBeeMutations.ALL`.

---

## 6. Production Definition

File: `common/src/main/java/com/curiousbees/common/content/builtin/BuiltinProductionDefinitions.java` (or equivalent)

- Define what comb (or other product) this species produces and at what rate.
- Register comb item in `ModItems` if new.
- Add item texture in `textures/item/<id>_comb.png`.
- Add lang key `"item.curiousbees.<id>_comb": "<Name> Comb"`.

---

## 7. Data-driven JSON (post EX-T01)

Once data-driven loading is active, also add:
- `neoforge/src/main/resources/data/curiousbees/species/<id>.json`
- `neoforge/src/main/resources/data/curiousbees/production/<id>.json`
- `neoforge/src/main/resources/data/curiousbees/mutations/<mutation_id>.json` (if applicable)

---

## Guardrails

- Do NOT skip the `LangKeyCompletenessTest` update — the test will fail CI.
- Do NOT add resource-output species without explicit phase approval (ADR-0012).
- Do NOT commit placeholder art without the `DEV-PLACEHOLDER` token in the adjacent code comment.
- Read `docs/architecture.md §7` for trait defaults reference.
- Read `docs/asset-generation-guidelines.md` for placeholder rules.
- Read `docs/decisions.md ADR-0011` for naming conventions.
