# 11 — Manual Playtest Checklists

## 1. Purpose

This file contains manual test scripts for validating Curious Bees in-game.

Manual testing should be repeatable. Each checklist should say what to prepare, what to do, and what result is expected.

## 2. Environment Checklist

Before any playtest:

```text
Correct Minecraft version
Correct NeoForge version
Correct mod jar loaded
Fresh test world or known test world
Cheats enabled for debug commands if needed
Logs visible
Known seed or testing area if biome-specific tests are needed
```

## 3. Wild Bee Genome Checklist

Setup:

```text
Create or load world.
Find or spawn bees in different biome contexts.
```

Test:

```text
[ ] Bee in plains/flower-like biome has Meadow-like genome.
[ ] Bee in forest-like biome has Forest-like genome.
[ ] Bee in dry biome has Arid-like genome.
[ ] Unknown biome uses fallback.
[ ] Existing genome is not overwritten.
[ ] Save and reload preserves genome.
```

## 4. Breeding Checklist

Setup:

```text
Use two known parent bees.
Inspect both parent genomes.
```

Test:

```text
[ ] Feed parent A.
[ ] Feed parent B.
[ ] Baby bee spawns.
[ ] Baby has genome.
[ ] Baby inherited one allele from each parent.
[ ] Parent genomes unchanged.
[ ] Save/reload preserves baby genome.
```

## 5. Mutation Checklist

Setup:

```text
Use known parent pair:
Meadow + Forest
or
Forest + Arid
```

Test:

```text
[ ] Mutation can occur with forced 100% chance.
[ ] Mutation does not occur with forced 0% chance.
[ ] Normal chance allows both mutation and non-mutation outcomes.
[ ] Mutation feedback appears when mutation occurs.
[ ] Mutation feedback does not appear for non-mutation.
```

## 6. Analyzer Checklist

Setup:

```text
Obtain Bee Analyzer.
Have at least one pure bee and one hybrid bee.
```

Test:

```text
[ ] Analyzer works on pure bee.
[ ] Analyzer works on hybrid bee.
[ ] Active species is shown.
[ ] Inactive species is shown.
[ ] Purebred/hybrid state is shown.
[ ] MVP traits are shown.
[ ] Missing genome does not crash.
[ ] Analyzer does not modify genome.
```

## 7. Production Checklist

Setup:

```text
Have known species genomes.
Have production debug command or integration available.
```

Test:

```text
[ ] Pure species produces expected primary output.
[ ] Hybrid species can show inactive/secondary influence if implemented.
[ ] Productivity affects output/rate/chance.
[ ] Unknown species handles safely.
[ ] Production does not create resource bee outputs in MVP.
```

## 8. Tech Apiary Checklist

Future:

```text
[ ] Apiary block can be placed.
[ ] Inventory persists.
[ ] Controlled breeding cycle works.
[ ] Frames modify behavior.
[ ] Automation does not dupe/delete items.
[ ] Placeholder model is acceptable.
```

## 9. Regression Checklist

Run after any significant change:

```text
[ ] Game launches.
[ ] Existing test world loads.
[ ] Wild bees still initialize.
[ ] Bee genomes still persist.
[ ] Breeding still works.
[ ] Analyzer still works.
[ ] No obvious log spam.
[ ] No crash on save/reload.
```
