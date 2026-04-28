# 05 — Vanilla Breeding Test Plan

## 1. Purpose

This plan validates vanilla-style bee breeding with genetic inheritance.

The player should breed two bees using normal Minecraft interaction, and the baby should receive an inherited and possibly mutated genome.

## 2. Scope

Test:

```text
baby bee creation hook
parent detection
parent genome read/fallback
child genome generation
mutation application
child genome storage
minimal mutation feedback
debug validation
```

## 3. Non-Scope

Do not test:

```text
Bee Analyzer item UI
production outputs
apiary
frames
Fabric
advanced mutation discovery
```

## 4. Hook Research Validation

Before implementation, document:

```text
chosen NeoForge breeding/baby hook
whether parents are available
whether child is available
whether child can be modified before world insertion
fallback if parents are unavailable
risks
```

Verification gate:

```text
Do not implement breeding assignment until hook behavior is understood.
```

## 5. Manual Breeding Tests

## 5.1 Pure Parent Cross

Setup:

```text
Parent A: Meadow / Meadow
Parent B: Forest / Forest
```

Action:

```text
Feed both bees with valid breeding item.
Wait for baby bee.
Inspect baby genome using debug command.
```

Expected:

```text
baby has Species Meadow / Forest or equivalent ordered pair
baby has one allele from each parent per chromosome
genome persists after reload
```

## 5.2 Hybrid Parent Cross

Setup:

```text
Parent A: Meadow / Forest
Parent B: Meadow / Forest
```

Repeat multiple times.

Expected possible results:

```text
Meadow / Meadow
Meadow / Forest
Forest / Forest
```

Approximate distribution should match core tests over large sample, but in-game manual validation only needs to confirm variation exists.

## 5.3 Missing Parent Genome

Setup:

```text
one or both parent bees have no Curious Bees genome
```

Expected:

```text
parent genome is initialized safely or fallback is used
baby still receives valid genome
no crash
warning/debug log if appropriate
```

## 6. Mutation Tests

## 6.1 Forced Mutation Debug Test

Use debug/config/test setup to force:

```text
Meadow + Forest -> Cultivated at 100%
```

Expected:

```text
baby Species includes Cultivated
mutation result metadata/log exists
feedback triggers if implemented
```

## 6.2 Zero Chance Test

Set mutation chance to:

```text
0%
```

Expected:

```text
mutation never occurs
normal inheritance still works
```

## 6.3 Normal Chance Smoke Test

With normal chance:

```text
Meadow + Forest -> Cultivated: 12%
Forest + Arid -> Hardy: 8%
```

Expected:

```text
mutation can occur over repeated attempts
non-mutated outcomes still occur
```

Do not require exact ratio in manual test.

## 7. Mutation Feedback Tests

If feedback exists:

```text
mutation causes particles/sound/debug message/advancement
non-mutation does not trigger mutation feedback
feedback is not spammy
feedback does not require GUI
```

## 8. Persistence Tests

After baby is born:

```text
inspect baby genome
save world
reload world
inspect baby again
```

Expected:

```text
same genome
same active/inactive state
no reinitialization overwrite
```

## 9. Regression Tests

Verify previous Phase 3 behavior still works:

```text
wild bees still initialize
debug inspect still works
bee genome storage still persists
```

## 10. Go / No-Go Before Phase 5

Phase 4 can proceed to Analyzer MVP only when:

```text
- vanilla breeding creates baby with inherited genome;
- missing parent genomes are handled safely;
- mutation can be forced and observed;
- mutation does not always occur at normal chance;
- child genome persists through save/load;
- event handler does not contain core genetic logic.
```

## 11. Review Checklist

```text
Does breeding delegate to common BreedingService?
Does mutation delegate to common MutationService?
Are event handlers thin?
Are parents and child identified safely?
Are fallbacks documented?
Are logs helpful during development?
```
