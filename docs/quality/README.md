# Curious Bees — Quality and Test Plans

This folder contains the testing strategy for Curious Bees.

The goal is to make every phase verifiable before moving to the next one. The mod is intentionally built in layers:

```text
Pure genetics core
→ initial content
→ NeoForge entity storage
→ vanilla breeding integration
→ analyzer MVP
→ production MVP
→ tech apiary
→ data-driven content
→ Fabric support
```

Each layer should have its own validation plan.

## Testing Principles

1. **Test the pure Java core first.**
   The genetics system must be unit-testable without Minecraft, NeoForge, Fabric, entities, items, blocks, or registries.

2. **Prefer deterministic tests.**
   Randomness must be injectable so inheritance and mutation tests can be reproduced.

3. **Use simulation tests where probabilities matter.**
   Statistical assertions should use tolerances, not exact percentages.

4. **Do not rely only on in-game testing.**
   In-game checks are important, but they should verify integration, not replace core tests.

5. **Every phase has a go/no-go checkpoint.**
   Do not advance to the next phase if the current phase cannot be validated.

## Files

```text
docs/quality/
├── README.md
├── 01-testing-and-validation-plan.md
├── 02-genetics-core-test-plan.md
├── 03-initial-content-test-plan.md
├── 04-neoforge-entity-integration-test-plan.md
├── 05-vanilla-breeding-test-plan.md
├── 06-analyzer-test-plan.md
├── 07-production-mvp-test-plan.md
├── 08-tech-apiary-test-plan.md
├── 09-data-driven-content-test-plan.md
├── 10-fabric-support-test-plan.md
├── 11-manual-playtest-checklists.md
└── 12-release-smoke-test.md
```

## Recommended Usage with AI Coding Agents

Before asking an AI agent to implement a task:

```text
Read CLAUDE.md.
Read the relevant implementation spec.
Read the relevant quality/test plan.
Implement only the requested task.
Add or update tests required by the test plan.
Do not move to the next phase until the verification gate passes.
```
