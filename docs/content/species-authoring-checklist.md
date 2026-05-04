# Species Authoring Checklist

> Use this checklist before releasing or merging any new bee species into Curious Bees.
> A species is not complete unless every applicable item is checked.

## Data

- [ ] Species definition JSON exists at `data/<ns>/curious_bees/species/<slug>.json`
- [ ] `id` follows the pattern `<namespace>:species/<slug>`
- [ ] `displayName` is set (plain string; future: use lang key)
- [ ] `dominance` is `DOMINANT` or `RECESSIVE`
- [ ] All four required default traits are present: `LIFESPAN`, `PRODUCTIVITY`, `FERTILITY`, `FLOWER_TYPE`
- [ ] All trait allele IDs reference known alleles (built-in or added alongside this species)
- [ ] `visual` field is set pointing to the species entity texture

## Mutation / Reachability

- [ ] At least one mutation entry produces this species, **OR** the species has a natural hive spawn
- [ ] Mutation chance (`baseChance`) is balanced (see existing species for reference values)
- [ ] `partialChance + fullChance` sums to `1.0`
- [ ] Parent species referenced in mutations exist in the registry

## Production

- [ ] Production definition exists at `data/<ns>/curious_bees/production/<slug>_production.json`
  — *OR* — this species is intentionally non-producing (document why)
- [ ] Output item IDs are valid registered items
- [ ] Output chances are between `0.0` and `1.0`

## Assets

- [ ] Entity texture PNG exists at `assets/<ns>/textures/entity/bee/<slug>.png`
- [ ] Texture is **64×32 pixels**, PNG with alpha
- [ ] Texture follows the vanilla bee UV layout (`docs/art/templates/bee/default_bee_uv_template.png`)
- [ ] Texture is **original artwork** — not copied from other mods or vanilla
- [ ] Asset prompt exists at `docs/art/prompts/species/textures-entity-bee-<slug>.md`
- [ ] Asset status in `docs/art/asset-manifest.md` is `integrated` (not `prompt-created` or placeholder)

## Localization

- [ ] `species.<namespace>.<slug>` key exists in `assets/<ns>/lang/en_us.json`
- [ ] Comb item lang key exists if species has a unique comb item
- [ ] Bee nest block lang key exists if species has a natural hive block

## Design

- [ ] Species has a clear progression role (bridge species, production species, etc.)
- [ ] Species is not a resource bee (iron, gold, diamond, etc.) — those are future content
- [ ] Species does not require lifecycle/larvae/queen mechanics — those are out of scope
- [ ] Balance notes written in the species design brief or commit message

## Tests

- [ ] Built-in species: test coverage in `BuiltinBeeSpeciesTest` or equivalent
- [ ] Data-driven species: loads cleanly through `ContentJsonLoader` (no validation errors)
- [ ] In-game smoke test: species visible, analyzer shows correct data, apiary produces correct output

## Before Merging

- [ ] No placeholder textures are used as final art
- [ ] No copied assets from third-party mods
- [ ] No resource bees or lifecycle mechanics introduced
- [ ] `docs/art/asset-manifest.md` updated with this species' assets
- [ ] `content-authoring-guide.md` does not need updates for this species

---

> **Resource bees** (iron, gold, diamond, netherite, etc.) are **not** part of the current
> Curious Bees post-MVP scope. They belong to a dedicated future content expansion phase.
> Do not add them until that phase is explicitly started.
