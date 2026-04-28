# 10 — Fabric Support Test Plan

## 1. Purpose

This plan validates future Fabric support.

Fabric support is a platform port, not a second version of the mod.

## 2. Scope

Test:

```text
Fabric module setup
Fabric genome storage
Fabric wild bee initialization
Fabric breeding hook/mixin
Fabric analyzer item
Fabric production integration
cross-loader behavior parity
release packaging
```

## 3. Non-Scope

Do not test:

```text
rewriting common genetics
different Fabric-only mechanics
new content exclusive to Fabric
large refactors without parity goal
```

## 4. Feasibility Spike Validation

Before implementation, document:

```text
Fabric entity data storage API
Fabric item data components
baby bee creation hook or mixin point
registry setup
build layout requirements
risks
```

## 5. Module Smoke Tests

```text
Fabric module compiles
Fabric dev client launches
mod id loads
basic logging works
common module is reused
```

## 6. Genome Storage Tests

Manual:

```text
spawn/find bee
assign genome
save world
reload world
inspect genome
```

Expected:

```text
same behavior as NeoForge
```

## 7. Wild Initialization Tests

Same expected behavior as NeoForge:

```text
plains/flower -> Meadow
forest -> Forest
dry -> Arid
fallback works
existing genomes not overwritten
```

## 8. Breeding Tests

Same as NeoForge:

```text
baby receives inherited genome
mutation can apply
missing parent genomes handled safely
child genome persists
```

## 9. Analyzer Tests

Same as NeoForge:

```text
analyzer item exists
use on bee shows genetics
missing genome safe
no genetic editing
```

## 10. Production Tests

Same as NeoForge if production exists:

```text
resolver behavior identical
platform item registration works
outputs match definitions
```

## 11. Cross-Loader Parity Tests

Create a parity checklist:

```text
same species definitions
same mutation chances
same breeding outputs for deterministic random
same analyzer report content
same production definitions
```

Use shared common tests wherever possible.

## 12. Go / No-Go for Fabric Release

Fabric release can proceed only when:

```text
- Fabric launches;
- Bee genome storage works;
- wild initialization works;
- breeding integration works;
- analyzer works;
- production parity is acceptable if production is in release;
- no common logic was duplicated into Fabric-only code.
```

## 13. Review Checklist

```text
Did Fabric reuse common services?
Did Fabric avoid becoming a fork?
Are behavior differences documented?
Are mixins minimal and well-scoped?
Is NeoForge still working after multiloader changes?
```
