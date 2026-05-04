# Bee species nest targeting and nectar rendering

Curious Bees behavior for **species bee nests**, **hive AI targeting**, and **nectar/pollen visuals** on species-textured bees.

## Entry (occupancy)

`SpeciesBeeNestBlockEntity.addOccupant` enforces `BeeNestCompatibilityService.canEnter(beeSpeciesId, nestSpeciesId)` (exact active species match). Bees without a Curious Bees genome cannot enter mod species nests.

## Pathfinding vs steering

Vanilla bee “home” discovery uses the `minecraft:bee_home` POI tag. Curious Bees adds nest POIs to that tag (`data/minecraft/tags/point_of_interest_type/bee_home.json`), so bees can still **select** any tagged hive as a navigation target.

Because POI lookup is not species-aware, bees may briefly path toward an incompatible species nest. **Server-side steering** (`BeeSpeciesHiveTargetHandler`) clears the bee’s saved hive when its current hive position is a `SpeciesBeeNestBlock` whose species does not match the bee’s active species (`setHivePos(null)` plus a short stay-out countdown). Compatible targets (matching species nest, **Genetic Beehive / genetic apiary** — universal hive with no species check — vanilla hive/nest) are left unchanged.

## Vanilla hives and nests

Vanilla `BeehiveBlockEntity` does not apply Curious Bees species rules. Mod-genome bees may still enter player/vanilla hives if vanilla AI chooses them.

## Nectar / pollen visuals

Species bees use `CuriousBeeBeeRenderer` with a base texture from `SpeciesTextureResolver`. Vanilla `BeeRenderer` swaps to `bee_nectar.png` / angry variants via `getTextureLocation`; that path is skipped for species bases.

`CuriousBeePollenLayer` draws an extra `entityCutoutNoCull` pass using `minecraft:textures/entity/bee/bee_nectar.png` on top of the species body when `Bee.hasNectar()` is true, so pollen reads similarly to vanilla without requiring per-species nectar PNGs.
