# 09 — Data-Driven Content Test Plan

## 1. Purpose

This plan validates the transition from hardcoded built-in definitions to data-driven definitions.

Data-driven content should not break built-ins.

## 2. Scope

Test:

```text
data DTOs
JSON parsing
definition validation
built-in + loaded definition merge
species loading
trait loading
mutation loading
production loading
authoring errors
```

## 3. Non-Scope

Do not test:

```text
large new species tree
resource bees
mod compatibility content
Fabric-specific data loading
```

## 4. DTO Validation Tests

For each data type:

```text
SpeciesData
TraitData
MutationData
ProductionData
```

Test:

```text
valid data loads
required fields missing fail
invalid chance fails
unknown references fail
duplicate ids fail
clear errors are produced
```

## 5. JSON Parsing Tests

Use sample JSON files for:

```text
Meadow species
Cultivated mutation
Cultivated production
Fast productivity trait
```

Test:

```text
valid JSON parses into DTO
invalid JSON reports clear error
schema-like expectations are documented
```

## 6. Runtime Conversion Tests

Test:

```text
DTO -> runtime definition conversion succeeds
runtime definition matches built-in equivalent
unknown trait references fail
unknown species references fail
```

## 7. Built-In Compatibility Tests

Important:

```text
built-ins still load when no external data exists
external data can add definitions
external duplicate ids are handled according to policy
built-ins are not accidentally removed
```

## 8. Resource/Data Reload Tests

When integrated with NeoForge resource/datapack loading:

Manual test:

```text
start game with built-ins only
add datapack with new species/mutation
reload data
verify definitions are available
remove invalid datapack
verify clear error/fallback
```

## 9. Authoring Guide Validation

The documentation should include:

```text
minimal species example
minimal mutation example
minimal production example
common errors
naming rules
id format rules
```

## 10. Go / No-Go Before Expanded Content

Proceed to large content expansion only when:

```text
- valid data loads;
- invalid data fails clearly;
- built-ins still work;
- no core gameplay depends on hardcoded scattered ids;
- content authoring docs exist.
```

## 11. Review Checklist

```text
Did AI avoid adding resource bees during infrastructure work?
Is validation strict enough?
Are error messages useful?
Can built-ins and external data coexist?
Is common data format independent from NeoForge where possible?
```
