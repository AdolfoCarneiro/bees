# 04 — NeoForge Entity Integration Test Plan

## 1. Purpose

This plan validates storing and retrieving Curious Bees genomes on vanilla Bee entities in NeoForge.

This phase proves that the pure Java genome can exist on living Minecraft bees and persist through save/load.

## 2. Scope

Test:

```text
NeoForge genome storage mechanism
Genome serialization/deserialization
Bee entity attachment/storage
Wild bee genome initialization
Debug inspection command
Save/load persistence
```

## 3. Non-Scope

Do not test yet:

```text
vanilla breeding child inheritance
mutation during actual bee breeding
Bee Analyzer item
production
apiary
Fabric
```

## 4. Storage API Research Validation

Before implementation, document:

```text
chosen NeoForge storage API
why it was chosen
save/load behavior
sync behavior
serialization requirements
fallback strategy
risks
```

Verification gate:

```text
No implementation should proceed until storage decision is documented.
```

## 5. Serialization Tests

If serialization can be tested without Minecraft, add unit tests for:

```text
serialize pure genome
deserialize pure genome
serialize hybrid genome
deserialize hybrid genome
active/inactive alleles are preserved
unknown allele id fails or falls back clearly
missing chromosome fails or recovers according to policy
```

Important:

```text
active/inactive must not be recalculated incorrectly during load.
```

## 6. Bee Storage Integration Tests / Manual Checks

Manual validation:

```text
Start dev client.
Enter test world.
Spawn or find a Bee.
Assign a genome.
Inspect genome using debug command.
Save and exit world.
Reload world.
Inspect same bee.
Verify genome persists.
```

Expected:

```text
same species pair
same active/inactive species
same MVP traits
no crash
no duplicate reinitialization
```

## 7. Wild Bee Initialization Tests

Manual validation by biome:

```text
Spawn/find bee in plains or flower-like biome -> Meadow-like genome
Spawn/find bee in forest-like biome -> Forest-like genome
Spawn/find bee in desert/savanna/dry biome -> Arid-like genome
Unknown biome -> safe fallback
```

Expected:

```text
bees without genomes receive one
bees with genomes are not overwritten
```

## 8. Debug Command Tests

Debug command should verify:

```text
targeted bee can be inspected
missing genome reports clear message
genetic bee reports active/inactive species
genetic bee reports core traits
non-bee target fails gracefully
```

Possible command behavior:

```text
/curiousbees genome inspect
/curiousbees genome inspect_target
```

Exact syntax can vary.

## 9. Error Handling Tests

Cases:

```text
corrupted genome data
missing allele id
unknown species id
bee without genome
world upgraded from previous mod version
```

Expected:

```text
normal gameplay should not crash
development log should provide clear warning
fallback should be safe
```

## 10. Go / No-Go Before Phase 4

Phase 3 can proceed to vanilla breeding integration only when:

```text
- Bee entity can store a Genome;
- genome persists through save/load;
- wild bees can receive initial genomes;
- missing/corrupt data is handled safely;
- debug inspection works;
- core genetics still has no NeoForge imports.
```

## 11. Review Checklist

```text
Does platform code call common services instead of duplicating logic?
Is storage isolated in NeoForge module/package?
Is serialization stable?
Does save/load preserve active/inactive alleles?
Can old/missing bees be handled safely?
```
