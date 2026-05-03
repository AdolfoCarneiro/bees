# 13 — Analyzer UX Implementation

## 1. Purpose

This document defines the implementation plan for **Phase 13 — Analyzer UX** of Curious Bees.

The MVP analyzer proved that genetic information can be read. Phase 13 turns analysis into real gameplay UX instead of chat/debug output.

Core rule:

```text
Genetic details should only be visible to the player after the bee has been analyzed.
```

## 2. Phase Goal

By the end of this phase, the project should be able to:

```text
- track whether a bee has been analyzed;
- use a portable analyzer on a living bee;
- reveal genetic details only after analysis;
- show a real analyzer UI instead of relying on chat output;
- show redacted/unknown data for unanalyzed bees;
- consume honey/combs/wax or analyzer charge as an analysis cost;
- keep analysis compatible with living vanilla bees.
```

## 3. Phase Non-goals

Do not implement:

```text
- full research/discovery tree;
- mutation recipe encyclopedia;
- per-player species knowledge unless explicitly scoped;
- bee itemization as the main workflow;
- lifecycle/death/larvae systems;
- temperature/humidity/environment analysis;
- resource bees;
- Fabric UI parity;
- JEI/REI integration.
```

A simple Analyzer Block may be planned or implemented as a later task in this phase, but the first deliverable should be portable analyzer UX and analyzed-state behavior.

## 4. Required Inputs

Before implementing, read:

```text
CLAUDE.md
docs/README.md
docs/post-mvp/11-post-mvp-productization-roadmap.md
docs/post-mvp/13-analyzer-ux-and-progression.md
docs/post-mvp/14-genetic-apiary-gui-and-frames.md
docs/mvp/02-technical-architecture.md
docs/mvp/03-genetics-system-spec.md
docs/mvp/04-breeding-and-mutation-spec.md
```

If touching existing analyzer code, read the existing analyzer implementation docs and source code first.

## 5. Expected Outputs

```text
BeeAnalysisState or equivalent
persistent analyzed flag on bees
common analyzer report redaction rules
portable analyzer interaction update
analyzer cost/charge behavior
NeoForge UI/screen for analyzer report
tooltip/display behavior for analyzed vs unanalyzed bees
apiary/analyzer shared display rules
updated analyzer docs
```

## 6. Architecture Rules

### 6.1 The genome exists before it is revealed

Good:

```text
Genome storage remains complete.
Analyzer/report layer decides what is visible.
```

Bad:

```text
Analysis generates genetics or mutates the genome.
```

### 6.2 Start with per-bee analysis state

Initial rule:

```text
Analysis state is stored per bee.
```

A child bee should start unanalyzed unless explicitly decided otherwise.

### 6.3 UI should consume common analyzer reports

Good:

```text
common/gameplay/analysis creates AnalyzerReport.
neoforge/client displays AnalyzerReport.
```

Bad:

```text
GUI directly parses Genome and duplicates genetic formatting rules.
```

### 6.4 Chat output becomes debug/fallback

Chat output can remain as fallback/debug, but should not be the primary player experience.

## 7. Recommended Source Structure

Possible common packages:

```text
common/src/main/java/.../gameplay/analysis/
├── AnalyzerReport.java
├── AnalyzerReportService.java
├── AnalysisVisibility.java
└── RedactedAnalyzerReport.java or equivalent
```

Possible NeoForge packages:

```text
neoforge/src/main/java/.../data/BeeAnalysisAttachments.java
neoforge/src/main/java/.../data/BeeAnalysisStorage.java
neoforge/src/main/java/.../item/BeeAnalyzerItem.java
neoforge/src/main/java/.../client/screen/BeeAnalyzerScreen.java
neoforge/src/main/java/.../network/ if needed
```

## 8. Analysis State Model

Initial model:

```text
BeeAnalysisState
- analyzed: boolean
```

Future fields, not required now:

```text
analyzedAtGameTime
analyzedByPlayerId
analysisTier
reportSnapshot
```

## 9. Display Rules

Unanalyzed bee:

```text
Species: Unknown
Purity: Unknown
Traits: Unknown
```

Analyzed bee:

```text
Species: Cultivated / Forest
Purity: Hybrid
Lifespan: Normal / Long
Productivity: Fast / Normal
Fertility: Two / Two
Flower Type: Flowers / Leaves
```

## 10. Task Breakdown

## Task 1 — Review Existing Analyzer Implementation

### Objective

Understand how the current analyzer reads genomes and displays data.

### Scope

Inspect common analysis code, NeoForge analyzer item, genome storage, and any network/client code.

### Non-goals

Do not change code.

### Acceptance Criteria

```text
- Identify current analyzer report model/service.
- Identify where chat output happens.
- Identify how the item reads a bee genome.
- Identify where analyzed state should be stored.
- Identify whether networking is needed for UI.
```

### Prompt for Claude Code

```text
Read CLAUDE.md and docs/post-mvp/13-analyzer-ux-and-progression.md.

Focus only on Task 1 from docs/implementation/13-analyzer-ux-implementation.md.

Review the existing analyzer implementation and produce an implementation plan. Do not modify files.

Summarize current analyzer flow, files involved, and the smallest safe first code change.
```

## Task 2 — Add Persistent Bee Analysis State

### Objective

Store whether a living bee has been analyzed.

### Scope

Implement NeoForge-side storage for analysis state on vanilla Bee entities.

### Non-goals

Do not implement UI yet.

### Acceptance Criteria

```text
- Bee entities can store analyzed true/false.
- Default state is unanalyzed.
- State persists through save/load.
- Existing bees without analysis state are handled safely.
- Storage is separate from genetic rules.
```

### Prompt for Claude Code

```text
Read docs/post-mvp/13-analyzer-ux-and-progression.md and Task 2 from docs/implementation/13-analyzer-ux-implementation.md.

Implement persistent analyzed-state storage for vanilla Bee entities in NeoForge.

Default state must be unanalyzed. Existing bees must be handled safely.

Do not implement analyzer UI, analyzer block, research systems, per-player knowledge, resource bees or lifecycle mechanics.

Before coding, summarize where the state will be stored and which files you expect to modify.
```

## Task 3 — Add Redacted Analyzer Report Rules

### Objective

Make analyzer reports respect analyzed vs unanalyzed visibility.

### Scope

Update common report generation or add a wrapper that can produce full or redacted reports.

### Non-goals

Do not implement GUI yet.

### Acceptance Criteria

```text
- Report generation can produce unknown/redacted fields.
- Full reports still show active/inactive species and traits.
- Existing analyzer tests are updated.
- Redaction logic is common/testable where possible.
```

### Prompt for Claude Code

```text
Read docs/post-mvp/13-analyzer-ux-and-progression.md.

Focus only on Task 3 from docs/implementation/13-analyzer-ux-implementation.md.

Add redacted analyzer report behavior so genetic details are hidden when a bee has not been analyzed.

Do not implement GUI, networking, analyzer block, resource bees or per-player knowledge.

Add tests for full vs redacted reports. Before coding, summarize the report model changes.
```

## Task 4 — Update Portable Analyzer Interaction

### Objective

Using the portable analyzer on a bee should analyze it, mark it analyzed, and open/show the report flow.

### Scope

Update `BeeAnalyzerItem` or equivalent.

Possible placeholder cost options:

```text
- consume honeycomb;
- consume honey bottle;
- consume wax/comb item;
- consume analyzer durability/charge.
```

The exact cost can be placeholder-balanced but must be easy to change later.

### Non-goals

Do not implement analyzer block yet.

### Acceptance Criteria

```text
- Analyzer can be used on a living bee.
- If analysis cost is available, bee becomes analyzed.
- If cost is missing, user gets clear feedback.
- Analyzed bees show full report.
- Unanalyzed bees remain redacted outside analyzer use.
```

### Prompt for Claude Code

```text
Read docs/post-mvp/13-analyzer-ux-and-progression.md and Task 4 from docs/implementation/13-analyzer-ux-implementation.md.

Update the portable analyzer item so using it on a living bee marks that bee as analyzed after paying a simple placeholder analysis cost.

Do not implement analyzer block, research tree, resource bees, bee itemization or lifecycle mechanics.

Before coding, summarize the chosen placeholder cost and where it will be checked.
```

## Task 5 — Implement Basic Analyzer UI

### Objective

Display analyzer reports in a real UI instead of relying on chat/debug output.

### Decided UI Approach

The **Portable Analyzer uses Option A: packet + simple client-side screen**.

Flow:

```text
1. Player uses analyzer on a living bee.
2. Server validates cost and marks bee as analyzed.
3. Server generates AnalyzerReport.
4. Server sends report to client via a network packet.
5. Client opens a simple overlay/screen with the report data.
```

Do NOT use a menu/container (Option B) for the Portable Analyzer. Menu/container is the correct pattern for the future Analyzer Block (Task 7), not for the item.

### Scope

Initial UI should show:

```text
- species active/inactive;
- purity/hybrid status;
- lifespan;
- productivity;
- fertility;
- flower type;
- dominance/active/inactive indicators if available;
- unknown/redacted state where applicable.
```

### Non-goals

Do not implement full research database, mutation tree UI, probability calculator, JEI/REI integration, or advanced animated UI.

Do not use a menu/container for the portable analyzer screen.

### Acceptance Criteria

```text
- Analyzer item triggers a network packet to the client after successful analysis.
- Client screen opens from the packet payload, not from a container/menu.
- UI uses AnalyzerReport or equivalent common report model.
- UI handles redacted reports.
- Chat output can remain only as fallback/debug.
```

### Prompt for Claude Code

```text
Read docs/post-mvp/13-analyzer-ux-and-progression.md.

Focus only on Task 5 from docs/implementation/13-analyzer-ux-implementation.md.

Implement a basic Analyzer UI for the Portable Analyzer using a network packet + client-side screen (Option A).

The flow is: server validates + analyzes -> server generates AnalyzerReport -> server sends packet -> client opens screen.

Do NOT use a menu/container for this. That pattern belongs to the future Analyzer Block.

Use the common analyzer report model rather than parsing Genome directly in the screen.

Do not implement research database, mutation tree UI, JEI/REI integration, analyzer block or resource bees.

Before coding, list the packet class, screen class, and any other files you expect to create.
```

## Task 5b — Create Analyzer Asset Prompts

### Objective

Create complete prompts for all required analyzer-related visual assets.

### Scope

Generate prompt files under `docs/art/prompts/items/` and `docs/art/prompts/gui/` for:

```text
- portable analyzer item icon;
- analyzer screen background;
- trait or status icons if used;
- analyzed/unknown state indicators if used.
```

### Non-goals

```text
- Do not create final placeholder PNGs.
- Do not generate images directly.
- Do not block core analyzer logic on polished art.
- Do not mark UI art as complete without generated assets.
```

### Acceptance Criteria

```text
- Prompt files exist under docs/art/prompts/items/ and docs/art/prompts/gui/.
- Each prompt includes target path, size, style, usage, and acceptance criteria.
- UI implementation references expected final paths.
- Temporary fallback is allowed only for dev safety and does not count as final.
```

### Prompt for Claude Code

```text
Read CLAUDE.md and docs/art/asset-prompt-workflow.md.

Focus only on Task 5b from docs/implementation/13-analyzer-ux-implementation.md.

Create complete asset prompt files for the portable analyzer item icon, analyzer screen background, and any trait/status icons needed.

Do not create placeholder PNGs. Do not generate images.

Place prompts under docs/art/prompts/items/ and docs/art/prompts/gui/.
```

## Task 6 — Apply Analyzed-State Display Rules Where Applicable

### Objective

Only show detailed genetic data in tooltips/containers if the bee has been analyzed.

### Scope

Apply visibility rules to existing genetic tooltips, apiary bee list display if present, and analyzer previews.

### Non-goals

Do not introduce bee itemization just to support tooltips.

### Acceptance Criteria

```text
- Any genetic tooltip/display respects analyzed state.
- Unanalyzed bees show unknown/redacted info.
- Analyzed bees show full genetic info.
- No new bee item workflow is introduced.
```

### Prompt for Claude Code

```text
Read docs/post-mvp/13-analyzer-ux-and-progression.md and docs/post-mvp/14-genetic-apiary-gui-and-frames.md.

Focus only on Task 6 from docs/implementation/13-analyzer-ux-implementation.md.

Apply analyzed-state visibility rules to existing genetic tooltips or bee display summaries.

Do not introduce bee itemization, analyzer block, research systems or new species.

Before coding, list where genetic tooltip/display summaries currently exist.
```

## Task 7 — Plan Analyzer Block Follow-up

### Objective

Decide whether the Analyzer Block belongs now or should be split into a future task.

### Decided UI Approach

When the Analyzer Block is eventually implemented, it should use **Option B: menu/container + screen**, the standard NeoForge block GUI pattern.

This is intentionally different from the Portable Analyzer (Task 5), which uses a packet + simple client screen.

```text
Portable Analyzer -> packet + client-side screen (no container)
Analyzer Block    -> menu/container + screen (standard block GUI)
```

### Recommended Default

Planning only for now. The Analyzer Block should be split into a future task or Phase 13 extension, not implemented in the initial Phase 13 scope.

### Scope

Document:

```text
- block purpose;
- input/output/cost;
- relation to portable analyzer;
- whether it analyzes living nearby bees or future samples;
- that it should use menu/container approach when implemented;
- non-goals.
```

### Prompt for Claude Code

```text
Read docs/post-mvp/13-analyzer-ux-and-progression.md.

Focus only on Task 7 from docs/implementation/13-analyzer-ux-implementation.md.

Produce a short plan for the Analyzer Block and recommend whether to implement it now or split it into a future task.

When the block is eventually implemented, it should use a menu/container + screen (standard block GUI pattern), not a packet overlay.

Do not implement code unless explicitly asked after review.
```

## 11. Phase Completion Checklist

```text
- Bees have persistent analyzed state.
- Default state is unanalyzed.
- Portable analyzer can mark a living bee as analyzed.
- Analyzer report redaction works.
- A basic Analyzer UI displays genetic data.
- Genetic details are hidden for unanalyzed bees.
- Analysis cost/charge exists as a placeholder-balanced mechanic.
- Chat/debug output is no longer the primary player UX.
- No lifecycle, environment simulation, resource bees or bee itemization were introduced.
```

## 12. Go / No-Go Checkpoint Before Phase 14

```text
- analyzed-state behavior is stable;
- apiary display rules can reuse analyzer report/redaction logic;
- portable analyzer has a clear player-facing flow;
- save/load does not lose analyzed state.
```

## 13. AI Failure Modes to Watch

```text
- revealing full genetics everywhere;
- making analysis generate or mutate genetics;
- implementing a huge research system;
- itemizing bees as the analyzer workflow;
- ignoring living vanilla bees.
```

## 14. Recommended Commit Sequence

```text
neoforge: add persistent bee analysis state
gameplay: add redacted analyzer report behavior
neoforge: update portable analyzer interaction cost
client: add basic analyzer report UI
ui: apply analyzed-state display rules
docs: plan analyzer block follow-up
```

## 15. Handoff Prompt for Starting Phase 13

```text
Read CLAUDE.md and docs/README.md.

Then read:
- docs/post-mvp/11-post-mvp-productization-roadmap.md
- docs/post-mvp/13-analyzer-ux-and-progression.md
- docs/implementation/13-analyzer-ux-implementation.md

Do not implement all of Phase 13 at once.

Start only with Task 1 — Review Existing Analyzer Implementation.

Before coding, summarize the goal, scope, out-of-scope items, and files/packages you expect to inspect.

Then perform Task 1 without changing files.
```
