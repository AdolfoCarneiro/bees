# 06 — Analyzer Test Plan

## 1. Purpose

This plan validates the Analyzer MVP.

The analyzer makes genetics visible to the player and makes manual validation possible without relying only on debug commands.

## 2. Scope

Test:

```text
common analyzer report model
analyzer formatting
Bee Analyzer item registration
use-on-bee behavior
missing genome handling
readability of output
```

## 3. Non-Scope

Do not test:

```text
advanced GUI
mutation prediction
research/discovery system
genetic editing
resource bee guidebook
```

## 4. Common Analyzer Tests

If common analyzer services exist, unit test:

```text
purebred species report
hybrid species report
active/inactive species display
lifespan display
productivity display
fertility display
flower type display
missing chromosome behavior
```

## 5. Formatter Tests

If a common formatter exists, ensure it returns platform-neutral output.

Good common output:

```text
plain strings
report DTOs
structured sections
```

Avoid in common:

```text
Minecraft Component
TextColor
client-only rendering classes
```

Test:

```text
formatter distinguishes active and inactive values
formatter shows purebred/hybrid
formatter handles missing genome gracefully
```

## 6. Item Registration Test / Manual Check

Manual validation:

```text
start dev client
open creative inventory or use give command
obtain Bee Analyzer item
verify name appears correctly
verify placeholder texture/model does not crash
```

## 7. Use-on-Bee Tests

Manual validation:

```text
use analyzer on pure Meadow bee
use analyzer on Meadow/Forest hybrid
use analyzer on bee without genome
use analyzer on non-bee target
```

Expected:

```text
genetic bee shows readable report
missing genome shows clear message
non-bee target fails gracefully or does nothing
analyzer does not modify genome
```

## 8. Readability Checklist

Analyzer output should show at minimum:

```text
Active Species
Inactive Species
Purity: Purebred or Hybrid
Lifespan active/inactive
Productivity active/inactive
Fertility active/inactive
FlowerType active/inactive
```

It should be readable in chat or action bar.

## 9. Regression Tests

After analyzer is implemented, verify:

```text
wild bee initialization still works
debug inspect still works
breeding still assigns child genome
mutation still applies
```

## 10. Go / No-Go Before Phase 6

Phase 5 can proceed to production MVP only when:

```text
- player can inspect a bee in-game;
- analyzer output is readable;
- purebred/hybrid species is obvious;
- active/inactive traits are distinguishable;
- missing genome does not crash;
- analyzer does not edit genetic data.
```

## 11. Review Checklist

```text
Did AI avoid creating a complex GUI?
Did analyzer remain read-only?
Did common code avoid Minecraft UI classes?
Is output useful for breeding decisions?
Can player validate mutation results with it?
```
