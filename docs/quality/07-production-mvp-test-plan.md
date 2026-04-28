# 07 — Production MVP Test Plan

## 1. Purpose

This plan validates the first production behavior.

The goal is not to create a full tech production system. The goal is to prove that species and traits matter beyond breeding.

## 2. Scope

Test:

```text
ProductionDefinition
ProductionOutput
ProductionResolver
ProductivityModifier
built-in product definitions
MVP comb items
debug production rolls
first production integration decision
```

## 3. Non-Scope

Do not test:

```text
full apiary
centrifuge
frames
resource bees
advanced automation
complex GUI
Fabric
```

## 4. Production Model Tests

Unit test:

```text
valid production definition succeeds
missing species id fails
invalid chance fails
output item id required
weight/chance boundaries are enforced
```

## 5. Production Resolver Tests

Scenarios:

### Pure species

```text
Genome: Cultivated / Cultivated
Expected: Cultivated primary output
```

### Hybrid species

```text
Genome: Cultivated / Forest
Expected: Cultivated primary output
Optional: Forest secondary output chance
```

### Unknown species

```text
Genome references unknown species
Expected: safe error/fallback depending on design
```

## 6. Productivity Modifier Tests

Test:

```text
Slow reduces output chance/rate
Normal leaves chance/rate unchanged
Fast increases output chance/rate
```

Use placeholder numbers from content spec unless changed.

## 7. Item Registration Manual Tests

For MVP comb items:

```text
Meadow Comb
Forest Comb
Arid Comb
Cultivated Comb
Hardy Comb
Wax
```

Manual checks:

```text
items can be obtained
items have names
placeholder texture/model does not crash
items appear in expected creative tab if one exists
```

## 8. Debug Production Roll Tests

If a debug command exists:

```text
given a bee genome
roll production
display possible outputs
show active/inactive species influence
show productivity modifier
```

Expected:

```text
debug output matches resolver logic
```

## 9. First Real Integration Decision

Before integrating production into a hive/block/entity, decide:

```text
vanilla hive modification?
custom simple genetic hive?
debug-only first?
```

This decision should be documented before implementation.

## 10. Go / No-Go Before Phase 7

Phase 6 can proceed to Tech Apiary only when:

```text
- production model is tested;
- production resolver is tested;
- species output identity works;
- productivity affects output in a clear way;
- MVP items exist or debug-only production is accepted;
- production integration decision is documented.
```

## 11. Review Checklist

```text
Did production stay simple?
Did AI avoid resource bees?
Did AI avoid implementing centrifuge/apiary too early?
Does active species matter?
Does inactive species matter at least conceptually?
Are outputs centralized?
```
