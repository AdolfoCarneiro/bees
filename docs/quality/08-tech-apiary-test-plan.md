# 08 — Tech Apiary Test Plan

## 1. Purpose

This plan validates the future tech apiary and automation layer.

This phase should only begin after the natural breeding loop, analyzer, and basic production are working.

## 2. Scope

Test:

```text
Genetic Apiary block
Apiary block entity
inventory persistence
controlled breeding cycles
frame modifiers
apiary production cycles
automation hooks
placeholder/custom assets
```

## 3. Non-Scope

Do not test in this phase unless explicitly added:

```text
large resource bee tree
Fabric support
complex GUI beyond what apiary needs
advanced multiblock automation
```

## 4. Block Registration Manual Tests

```text
block exists
block item exists
block can be placed
block can be broken
block has placeholder model if final asset missing
no crash on world reload
```

## 5. Block Entity Persistence Tests

Manual or integration:

```text
insert test items/bees/samples
save world
reload world
verify contents persist
verify progress/cycle state persists if required
```

## 6. Apiary Breeding Cycle Tests

Scenarios:

```text
valid parent inputs produce child result
invalid inputs do not start cycle
cycle uses common BreedingService
cycle uses common MutationService
cycle does not duplicate core logic
```

## 7. Frame Modifier Tests

Unit test common frame model if possible:

```text
mutation frame increases mutation chance
productivity frame affects production
stability frame affects unwanted randomness if implemented
invalid modifier values rejected
```

## 8. Automation Tests

Manual validation with common automation systems:

```text
hopper input works if intended
hopper output works if intended
side restrictions work if implemented
automation does not duplicate/delete items
```

Mod compatibility automation should remain future unless explicitly targeted.

## 9. Asset Tests

For placeholder assets:

```text
placeholder block model loads
missing texture does not crash
asset paths are correct
```

For Blockbench assets later:

```text
exported model path is correct
texture path is correct
model appears in-game
```

## 10. Go / No-Go Before Phase 8

Proceed to data-driven content only when:

```text
- apiary mechanics are stable or intentionally postponed;
- frame model is clear;
- production/breeding cycles do not duplicate common logic;
- persistence works;
- automation basics are safe.
```

## 11. Review Checklist

```text
Is apiary enhancing natural breeding, not replacing it entirely?
Are frames modifiers, not hardcoded special cases?
Is block entity code isolated from common genetics?
Are assets placeholders acceptable?
```
