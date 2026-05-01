# 07J — Phase 7 Review and Balancing

## Status

Complete.

## Scope of This Review

This document records the current Phase 7 state against:

- `docs/implementation/07-tech-apiary-and-automation.md` (7J checklist and exit gate)
- `docs/decisions/0009-genetic-apiary-design.md`

It is the Phase 7 closure checkpoint.

## Current Baseline Values (Post-7F Tuning)

Current built-in frame values:

```text
Basic Frame:
  mutation x1.03
  production x1.03

Mutation Frame:
  mutation x1.18
  production x1.00

Productivity Frame:
  mutation x1.00
  production x1.18
```

These values are intentionally conservative for initial balance safety.
Simulation tests in `ApiaryProductionBalancingTest` document the expected hit
rates for each frame configuration and serve as a regression guard for future
tuning. In-game tuning can still adjust these values via `BuiltinFrameModifiers`.

## Implemented vs. Planned (Phase 7)

### 7A — Tech Apiary Design Decision

Status: done.

Notes:

- Design is documented in ADR-0009.
- Genetic Apiary is production-focused, not breeding-focused.
- Living bees use vanilla home behavior; no capture-item model.

### 7B — Minimal Apiary Block Registration

Status: done.

Notes:

- Genetic Apiary block/item are registered and placeable.
- Basic registration path is stable for iterative feature work.

### 7C — Apiary Block Entity and Inventory

Status: done (MVP level).

Notes:

- Genetic Apiary block entity exists and persists output inventory.
- Custom block entity type serialization behavior is documented.

### 7D — Apiary Breeding Cycle

Status: removed from scope.

Notes:

- ADR-0009 defines the Genetic Apiary as production-only.
- Breeding via this block is not planned for any future slice.
- Natural vanilla breeding is the sole breeding path.
- No breeding logic, parent slots, or cycle progress exists in GeneticApiaryBlockEntity.

### 7E — Frame Modifier Model

Status: done.

Notes:

- Common frame model exists (`FrameModifier` + `FrameModifiers`).
- Frame combination behavior is unit-tested and platform-neutral.

### 7F — Basic Frames

Status: done (first slice).

Notes:

- Basic/Mutation/Productivity frame items are registered.
- Genetic Apiary has frame slots with validation and persistence.
- Frame production multipliers are applied during apiary production rolls.
- Placeholder frame item models and lang entries are in place.

### 7G — Apiary Production Cycle

Status: done (first functional slice).

Notes:

- Production is triggered when bees with nectar enter the apiary.
- Production uses common `ProductionResolver` and built-in definitions.
- Output insertion handles overflow safely and logs drops.
- Missing-genome fallback is applied with warning logs.

### 7H — Automation Hooks

Status: done (minimal slice).

Notes:

- Output inventory is exposed as extract-only item handler capability.
- Insert is blocked via capability view, matching current rules.

### 7I — Visual Assets and Blockbench Workflow

Status: done (documentation slice).

Notes:

- Phase 7I asset workflow documented with placeholder-first policy.
- Canonical runtime paths and Blockbench source layout documented.

### 7J — Phase 7 Review and Balancing

Status: done.

## 7J Checklist Review

- Natural breeding still matters: yes.
- Apiary improves control without removing uncertainty: yes (production only, probabilistic resolver retained).
- Apiary uses common genetics/gameplay services: yes (`ProductionResolver` in common).
- Frames optional/understandable: yes (implemented with minimal first-slice behavior).
- Outputs balanced enough for MVP: yes (simulation tests in `ApiaryProductionBalancingTest` validate rates for all five species and all frame configurations).
- Automation basic but not overpowered: yes (extract-only output).
- Assets acceptable: yes for current stage (placeholder-first and docs complete).
- Resource bees avoided: yes.
- Complex machine chain avoided: yes.
- Fabric implementation avoided: yes.

## Risks and Open Questions

- Balance values are provisional; fine-grained in-game tuning may still be needed after extended play.
- No GUI yet; debugging relies on `/curiousbees debug apiary_metrics` and other debug commands.
- `ProductionDefinition.secondaryOutputs` field is not yet consumed by `ProductionResolver`; reserved for future use.

## Phase 7 Exit Gate (Current Assessment)

From the 7J exit criteria in the Phase 7 spec:

- Tech apiary design documented: yes.
- Minimal apiary block exists: yes.
- Block entity/inventory works: yes.
- Breeding cycle works if included: not applicable — breeding removed from apiary scope per ADR-0009.
- Frame model exists if included: yes.
- Basic frames work if included: yes (first slice).
- Production cycle decision is made: yes (ADR-0009).
- Automation rules documented or implemented: yes (minimal implemented).
- Placeholder/final assets exist: placeholder strategy and workflow documented.
- Natural breeding remains useful: yes.

Conclusion:

- Phase 7 is complete.
- All planned sub-phases (7A–7J) are either implemented or explicitly removed from scope.
- Breeding references have been removed from the apiary code and docs.
- Simulation tests document and guard the production rate baselines.
- The codebase is ready to move to Phase 8 (Data-Driven Content).

## Optional Follow-Up Tasks (Post-Phase 7, Non-Blocking)

1. GUI slice for player-facing frame/output visibility.
2. Frame durability behavior if balancing requires it.
3. In-game fine-tuning of `BuiltinFrameModifiers` values after extended play.
4. Investigate using `ProductionDefinition.secondaryOutputs` in `ProductionResolver`.
