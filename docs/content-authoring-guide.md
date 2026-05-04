# Curious Bees Content Authoring Guide

This guide describes the Phase 8 data format for Curious Bees content.

The current data-driven system supports:

- trait allele JSON files;
- species JSON files;
- mutation JSON files;
- production JSON files;
- validation and conversion into the same runtime models used by built-in content.

It does not add new resource bees, GUIs, scripting, Fabric loading, or large content trees.

## Loading Layout

NeoForge resource loading looks for JSON files under these datapack resource paths:

```text
data/<namespace>/curious_bees/traits/**/*.json
data/<namespace>/curious_bees/species/*.json
data/<namespace>/curious_bees/mutations/*.json
data/<namespace>/curious_bees/production/*.json
```

Example MVP files live in test resources:

```text
common/src/test/resources/content-examples/curious_bees/
```

They are examples, not active bundled overrides. Built-in IDs are protected, so files that reuse built-in IDs are rejected by the runtime loader.

## IDs And Overrides

Use stable string IDs such as:

```text
my_pack:species/silverleaf
my_pack:traits/flower_type/mushrooms
my_pack:mutations/silverleaf_from_meadow_forest
my_pack:silverleaf_comb
```

Current override policy:

- loaded content may add new IDs;
- loaded content may reference built-in IDs;
- loaded content cannot override built-in trait, species, mutation, or production IDs;
- duplicate loaded IDs are rejected.

## Trait Alleles

Trait alleles define values for non-species chromosomes.

Required fields:

- `id`
- `chromosomeType`
- `displayName`
- `dominance`

Supported `chromosomeType` values:

```text
LIFESPAN
PRODUCTIVITY
FERTILITY
FLOWER_TYPE
```

Supported `dominance` values:

```text
DOMINANT
RECESSIVE
```

Example:

```json
{
  "id": "my_pack:traits/flower_type/mushrooms",
  "chromosomeType": "FLOWER_TYPE",
  "displayName": "Mushrooms",
  "dominance": "RECESSIVE"
}
```

Optional implemented field:

```json
{
  "values": {
    "multiplier": 1.25
  }
}
```

`values` is parsed and preserved on the DTO, but most gameplay currently uses the allele ID and chromosome type.

## Species

Species define the species allele and default trait pairs used to create a default genome.

Required fields:

- `id`
- `displayName`
- `dominance`
- `defaultTraits`

`defaultTraits` must include:

```text
LIFESPAN
PRODUCTIVITY
FERTILITY
FLOWER_TYPE
```

Each trait can be written as one allele ID, a two-element array, or an object pair.

Single allele form:

```json
"LIFESPAN": "curious_bees:traits/lifespan/normal"
```

Array pair form:

```json
"PRODUCTIVITY": [
  "my_pack:traits/productivity/brisk",
  "curious_bees:traits/productivity/normal"
]
```

Object pair form:

```json
"FLOWER_TYPE": {
  "first": "my_pack:traits/flower_type/mushrooms",
  "second": "curious_bees:traits/flower_type/flowers"
}
```

Example:

```json
{
  "id": "my_pack:species/silverleaf",
  "displayName": "Silverleaf Bee",
  "dominance": "RECESSIVE",
  "defaultTraits": {
    "LIFESPAN": "curious_bees:traits/lifespan/normal",
    "PRODUCTIVITY": "curious_bees:traits/productivity/normal",
    "FERTILITY": "curious_bees:traits/fertility/two",
    "FLOWER_TYPE": [
      "my_pack:traits/flower_type/mushrooms",
      "curious_bees:traits/flower_type/flowers"
    ]
  },
  "spawnContextNotes": ["example_only"]
}
```

`spawnContextNotes` is optional and currently informational.

### Visual Metadata

The optional `visual` field declares how the species should be rendered in-game.

Simple string form (texture path only, uses the default bee model):

```json
"visual": "curiousbees:textures/entity/bee/silverleaf.png"
```

Object form (explicit texture and model):

```json
"visual": {
  "texture": "my_pack:textures/entity/bee/silverleaf.png",
  "model": "curiousbees:bee/default"
}
```

If `visual` is omitted, the client falls back to the Curious Bees generic fallback bee texture.

Texture path conventions:

```text
assets/<namespace>/textures/entity/bee/<species_name>.png
```

Example with visual:

```json
{
  "id": "my_pack:species/silverleaf",
  "displayName": "Silverleaf Bee",
  "dominance": "RECESSIVE",
  "defaultTraits": {
    "LIFESPAN": "curious_bees:traits/lifespan/normal",
    "PRODUCTIVITY": "curious_bees:traits/productivity/normal",
    "FERTILITY": "curious_bees:traits/fertility/two",
    "FLOWER_TYPE": "curious_bees:traits/flower_type/flowers"
  },
  "visual": "my_pack:textures/entity/bee/silverleaf.png"
}
```

Bee entity textures must follow the vanilla bee UV layout (64×32 pixels).
See `docs/art/templates/bee/default_bee_uv_template.png` for the UV template.
Do not copy vanilla bee textures — create original artwork.

## Mutations

Mutations define probabilistic species results for parent species pairs.

Required fields:

- `id`
- `parents`
- `result`
- `baseChance`
- `resultModes`

`parents` must contain exactly two species IDs. Parent order does not matter at runtime.

`baseChance`, `partialChance`, and `fullChance` must be between `0.0` and `1.0`. `partialChance + fullChance` must equal `1.0`.

Example:

```json
{
  "id": "my_pack:mutations/silverleaf_from_meadow_forest",
  "parents": [
    "curious_bees:species/meadow",
    "curious_bees:species/forest"
  ],
  "result": "my_pack:species/silverleaf",
  "baseChance": 0.10,
  "resultModes": {
    "partialChance": 1.0,
    "fullChance": 0.0
  }
}
```

Current runtime note: the existing `MutationDefinition` model supports one result mode per mutation. The converter chooses `PARTIAL` when `partialChance >= fullChance`, otherwise `FULL`.

## Production

Production definitions attach outputs to a species.

Required fields:

- `species`
- `primaryOutputs`

Optional implemented field:

- `secondaryOutputs`

Each output requires:

- `item`
- `chance`

Optional output fields:

- `min`
- `max`

Example:

```json
{
  "species": "my_pack:species/silverleaf",
  "primaryOutputs": [
    {
      "item": "my_pack:silverleaf_comb",
      "chance": 0.65,
      "min": 1,
      "max": 1
    }
  ],
  "secondaryOutputs": [
    {
      "item": "minecraft:honeycomb",
      "chance": 0.15
    }
  ]
}
```

`chance` must be between `0.0` and `1.0`. `min` must be at least `1`, and `max` must be greater than or equal to `min`.

## Common Validation Errors

Examples of rejected content:

- missing or blank `id`;
- invalid `dominance`;
- trait allele using `SPECIES` as `chromosomeType`;
- species missing one of the four required trait slots;
- species referencing an unknown trait allele ID;
- mutation referencing an unknown parent or result species;
- mutation chances outside `0.0` to `1.0`;
- mutation result mode chances that do not sum to `1.0`;
- production referencing an unknown species;
- production output missing `item`;
- duplicate loaded IDs;
- attempts to override built-in IDs.

Validation errors include the resource path when loaded through the content loader.

## Testing Content

For Java-side validation, see:

```text
common/src/test/java/com/curiousbees/common/content/
```

Useful tests:

```text
ContentDataJsonParserTest
ContentJsonLoaderTest
ContentExampleFilesTest
```

For manual NeoForge validation:

```text
1. Start the game with only built-ins.
2. Confirm the game loads without content errors.
3. Add a datapack with new, non-built-in IDs.
4. Reload datapacks.
5. Confirm the log reports loaded definitions.
6. Introduce one invalid file and confirm the log names the bad file.
7. Confirm built-in content still works after the invalid file is rejected.
```

## Future Fields

These concepts are planned or possible later, but are not implemented as active gameplay fields in Phase 8:

- biome tag spawn rules;
- dimensions and environmental mutation requirements;
- nearby block mutation requirements;
- content pack override permissions;
- GUI authoring tools;
- Fabric resource loading;
- large expanded species trees;
- resource bee families.

Do not rely on those fields until a later implementation spec adds them.
