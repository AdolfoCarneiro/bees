---
name: Task
about: New work item aligned with TASKS.md
labels: task
---

## Task ID

<!-- Assign from TASKS.md. Format: E#-T## or EX-T##. If this is a new epic, open an epic issue first. -->
ID:

## Epic

<!-- Which epic does this belong to? E0–E6 or EX. -->

## Goal

<!-- One sentence: what does "done" look like for a player or developer? -->

## Done when

<!-- Bullet list of acceptance criteria. Be specific — no vague "works correctly". -->

- [ ]
- [ ]

## Depends on

<!-- List task IDs or ADRs this blocks on. "None" if standalone. -->

## Scope guard

- [ ] No new species beyond the 5 MVP species (meadow, forest, arid, cultivated, hardy) unless ADR-0012 is open
- [ ] Genetics core stays pure Java — no `net.minecraft.*` / NeoForge imports in `common/genetics`
- [ ] No resource bees unless ADR-0012 gate is explicitly open
- [ ] No new top-level docs files — append to existing docs if needed
