# 07J — Phase 7 Review and Balancing

## Status

In progress (post-7I review snapshot).

## Scope of This Review

This document records the current Phase 7 state against:

- `docs/implementation/07-tech-apiary-and-automation.md` (7J checklist and exit gate)
- `docs/decisions/0009-genetic-apiary-design.md`

It is a lightweight implementation checkpoint, not a final balancing pass.

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

Status: intentionally deferred.

Notes:

- ADR-0009 defines first Genetic Apiary version as production, not breeding.
- Natural vanilla breeding remains the primary breeding path.

### 7E — Frame Modifier Model

Status: not started.

### 7F — Basic Frames

Status: not started.

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

Status: current document.

## 7J Checklist Review

- Natural breeding still matters: yes.
- Apiary improves control without removing uncertainty: yes (production only, probabilistic resolver retained).
- Apiary uses common genetics/gameplay services: yes (`ProductionResolver` in common).
- Frames optional/understandable: partially (optional, but not implemented yet).
- Outputs balanced enough for MVP: partial (functional, balancing pass still pending).
- Automation basic but not overpowered: yes (extract-only output).
- Assets acceptable: yes for current stage (placeholder-first and docs complete).
- Resource bees avoided: yes.
- Complex machine chain avoided: yes.
- Fabric implementation avoided: yes.

## Risks and Open Questions

- Balance values are still placeholder-level; output rates may need tuning.
- No GUI yet; debugging relies on command-based inspection.
- Frame system is still absent, so long-term balancing knobs are limited.

## Phase 7 Exit Gate (Current Assessment)

From the 7J exit criteria in the Phase 7 spec:

- Tech apiary design documented: yes.
- Minimal apiary block exists: yes.
- Block entity/inventory works: yes.
- Breeding cycle works if included: not included by design (deferred).
- Frame model exists if included: not included yet.
- Basic frames work if included: not included yet.
- Production cycle decision is made: yes (ADR-0009).
- Automation rules documented or implemented: yes (minimal implemented).
- Placeholder/final assets exist: placeholder strategy and workflow documented.
- Natural breeding remains useful: yes.

Conclusion:

- Current implementation satisfies the chosen first-slice direction
  (production-focused apiary).
- Full “Phase 7 complete” should wait for explicit decision on whether 7E/7F are required
  before formal phase close.

## Recommended Next Official Tasks (From Phase 7 Plan)

1. 7E — Frame Modifier Model (common, testable).
2. 7F — Basic Frames (registration + slot validation + simple modifiers).
3. 7J follow-up balancing pass after 7E/7F are in place.
