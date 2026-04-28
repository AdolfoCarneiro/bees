# 01 — Testing and Validation Plan

## 1. Purpose

This document defines the overall testing approach for Curious Bees.

Curious Bees relies on a complex genetics system. If the genetics core is wrong, every later feature becomes unreliable. Therefore, the testing strategy must start at the core and expand outward.

## 2. Quality Goals

The project should be able to prove that:

```text
- genomes are represented correctly;
- active/inactive alleles are stable;
- dominance rules work;
- Mendelian inheritance works;
- mutation probabilities work;
- initial species and traits are valid;
- vanilla bees can store genomes;
- saved worlds preserve genome data;
- baby bees inherit genetics;
- analyzer output matches the genome;
- production uses active/inactive species correctly;
- platform integrations do not duplicate common logic.
```

## 3. Test Layers

### 3.1 Unit Tests

Used for:

```text
Genetics core
Content definitions
Mutation rules
Production resolver
Analyzer report formatting
```

Unit tests should run without Minecraft.

### 3.2 Simulation Tests

Used for probabilistic behavior.

Examples:

```text
10,000 hybrid x hybrid crosses
Mutation chance approximation
Partial vs full mutation ratio
Production output distribution
```

Simulation tests should use deterministic seeds or deterministic random sequences when possible.

### 3.3 Integration Tests

Used for platform behavior.

Examples:

```text
Bee entity genome storage
Save/load persistence
Event hook behavior
Item registration
Command behavior
```

These may require NeoForge test environment or manual in-game validation.

### 3.4 Manual In-Game Validation

Used for player-facing flows:

```text
Find wild bees
Breed bees
Observe baby genetics
Use analyzer
See mutation feedback
Verify production output
```

Manual tests should be written as checklists, not vague notes.

### 3.5 Release Smoke Tests

Used before publishing or sharing builds.

Examples:

```text
Game launches
World loads
Bee spawn works
Bee breeding works
Analyzer works
No obvious crashes
```

## 4. Randomness Strategy

Randomness must be injectable in common code.

Recommended abstraction:

```java
public interface GeneticRandom {
    boolean nextBoolean();
    double nextDouble();
    int nextInt(int bound);
}
```

Tests should avoid global randomness.

For exact tests:

```text
Use deterministic random returning a known sequence.
```

For distribution tests:

```text
Use a fixed seed and tolerance thresholds.
```

## 5. Definition of Done for Any Task

A task is done when:

```text
- implementation matches the task scope;
- non-goals were respected;
- relevant tests were added or updated;
- tests pass or failures are documented;
- no unrelated systems were implemented early;
- common code remains independent from platform APIs;
- follow-up tasks are documented if discovered.
```

## 6. Definition of Done for Any Phase

A phase is done when:

```text
- all must-have tasks in the phase are complete;
- phase-specific tests pass;
- manual validation, if required, is documented;
- known limitations are recorded;
- go/no-go checklist passes;
- next phase dependencies are clear.
```

## 7. Test Naming Guidelines

Use descriptive test names.

Good:

```text
dominantAlleleBecomesActiveWhenPairedWithRecessive()
hybridParentsProduceApproximateMendelianDistribution()
mutationWithZeroChanceNeverApplies()
wildBeeGenomePersistsAfterWorldReload()
```

Bad:

```text
test1()
works()
beeTest()
```

## 8. AI Agent Requirements

When AI writes code, it must also write or update tests when the task affects behavior.

If a task cannot be tested automatically yet, the agent should add:

```text
- explanation of why;
- manual validation checklist;
- follow-up task for automation.
```

## 9. Go / No-Go Philosophy

Do not proceed by vibes.

Proceed only when the current layer is proven.

Example:

```text
Do not start NeoForge entity storage until the genetics core tests pass.
Do not start vanilla breeding integration until bee genome storage persists through save/load.
Do not start production balancing until analyzer can inspect genetics.
```
