# 13 — Analyzer UX and Progression

> Status: Post-MVP planning document.  
> This document defines how bee analysis should evolve from MVP debug/chat output into real gameplay UX.

## 1. Purpose

The MVP analyzer proved that bee genomes can be inspected.

The post-MVP analyzer should become a real player-facing system, not just chat output or debug information.

The analyzer should support the core fantasy:

```txt
Find bees -> breed bees -> analyze offspring -> select promising lineages -> stabilize better bees.
```

## 2. Core Design Decision

Genetic data should only be visible if the bee has been analyzed.

A bee may internally carry a complete genome, but the player should not automatically see all genetic details.

This creates:

```txt
- mystery
- progression
- value for analyzer tools
- meaningful selection gameplay
- reason to produce honey/combs before deep breeding
```

## 3. Analyzer Roles

Curious Bees should support two analyzer concepts.

### 3.1 Portable Bee Analyzer

A handheld tool used directly on a living bee.

Purpose:

```txt
- quick field analysis
- identify species and traits
- mark a bee as analyzed
- help the player decide whether to keep or breed a bee
```

This should replace chat/debug output as the normal player experience.

### 3.2 Analyzer Block

A placed block for deeper laboratory-style analysis.

Purpose:

```txt
- more complete genetic inspection
- better UI
- possible future discovery tracking
- possible future comparison between bees
- possible future mutation probability preview
```

The Analyzer Block does not need to be fully advanced in the first implementation, but it should be part of the design direction.

## 4. Analysis State

A bee should have analysis state.

Initial recommended model:

```txt
BeeAnalysisState
- analyzed: boolean
```

Initial behavior:

```txt
- New wild bees are not analyzed.
- New baby bees are not analyzed.
- Using the analyzer marks the target bee as analyzed.
- Tooltips and apiary UI reveal details only when analyzed is true.
```

Future possible extensions:

```txt
- analysisTier
- analyzedByPlayer
- discoveredByTeam/world
- knownMutationHints
- partial vs full analysis
```

Do not implement these extensions until needed.

## 5. What The Player Sees

### 5.1 Before Analysis

Before analysis, UI should hide detailed genetics.

Example tooltip/API display:

```txt
Bee Genetics: Unknown
Analysis Required
```

Optional vague information:

```txt
Visible Species: Meadow Bee
Genetics: Unknown
```

The visible species can be inferred from the rendered skin, but hidden alleles and traits should remain unknown.

### 5.2 After Analysis

After analysis, UI can show genetic information.

Example:

```txt
Species: Cultivated / Forest
Purity: Hybrid
Lifespan: Normal / Long
Productivity: Fast / Normal
Fertility: Two / Two
Flower Type: Flowers / Leaves
```

Dominance can be shown with symbols or icons:

```txt
[A] Active
[I] Inactive
[D] Dominant
[R] Recessive
```

Example:

```txt
Species
[A][D] Cultivated
[I][D] Forest
```

## 6. Analysis Cost

Analysis should require a small resource cost or charge.

This turns analysis into part of progression rather than free debug information.

Possible costs:

```txt
- honey bottle
- honeycomb
- wax
- analyzer durability
- analyzer charge
```

Recommended initial approach:

```txt
Portable Analyzer consumes durability or charge.
Analyzer charge can be restored using honey-based items.
```

Alternative simpler approach:

```txt
Portable Analyzer consumes one honeycomb or honey bottle per analysis.
```

Choose the simplest implementation that feels good in playtesting.

## 7. Suggested Progression

### Stage 0 — No Analyzer

Player can:

```txt
- see bee visuals
- breed bees
- observe mutation feedback
- guess based on visible species
```

Player cannot:

```txt
- see inactive alleles
- see trait pairs
- see purity
```

### Stage 1 — Portable Analyzer

Player can:

```txt
- analyze individual bees
- see core genetic report
- mark bees as analyzed
- view analyzed data in tooltip/apiary UI
```

### Stage 2 — Analyzer Block

Player can:

```txt
- perform deeper analysis
- possibly compare bees
- possibly view discovered species history
- possibly preview breeding outcomes later
```

Stage 2 may be implemented incrementally.

## 8. Portable Analyzer UX

Expected interaction:

```txt
1. Player holds Bee Analyzer.
2. Player right-clicks a living bee.
3. Analyzer checks cost/charge.
4. Analyzer reads bee genome.
5. Bee is marked analyzed.
6. UI opens or report is displayed.
```

Preferred output:

```txt
- small GUI/screen
- compact genetic report
- icons or labels for active/inactive traits
```

Fallback for early implementation:

```txt
- toast/overlay
- formatted message
```

But chat should not remain the final UX.

## 9. Analyzer Block UX

Expected interaction options:

```txt
- right-click block with analyzer tool
- block detects nearby/contained apiary bees
- block accepts a genetic sample in the future
- block can inspect an analyzed bee connected to the apiary system
```

Do not transform living bees into items for the base analyzer block.

Potential GUI sections:

```txt
- selected bee
- species pair
- purity status
- trait chromosomes
- production hints
- discovered status
- future mutation hints
```

## 10. Tooltips

Tooltips should follow the analysis state.

### Bee-related tooltip before analysis

```txt
Genetics: Unknown
Analyze this bee to reveal genetic traits.
```

### Bee-related tooltip after analysis

```txt
Species: Cultivated / Forest
Purity: Hybrid
Productivity: Fast / Normal
```

Tooltips should be concise. The full report belongs in the analyzer UI.

## 11. Apiary Integration

The Genetic Apiary should only show detailed genetic data for analyzed bees.

Before analysis:

```txt
Bee #1: Unanalyzed
Visible Species: Meadow
Genetics: Unknown
```

After analysis:

```txt
Bee #1: Cultivated / Forest
Purity: Hybrid
Productivity: Fast / Normal
```

This makes the analyzer relevant even after apiary automation exists.

## 12. Discovery System

Full discovery/research is future scope.

Do not implement yet:

```txt
- global species encyclopedia
- hidden mutation recipe reveal system
- research points
- mutation tree UI
- per-player genetic knowledge
```

However, analyzer design should not block these future systems.

## 13. Technical Boundaries

Common analyzer logic can format report data, but GUI and rendering belong to platform/client code.

Recommended separation:

```txt
common/gameplay/analysis:
- AnalyzerReport
- AnalyzerReportLine
- AnalyzerService
- analysis visibility rules

neoforge/item:
- analyzer item interaction

neoforge/client/screen:
- analyzer GUI

neoforge/data:
- analyzed state attachment/component
```

Do not put GUI logic in common/genetics.

## 14. Out of Scope For This Phase

Do not implement yet:

```txt
- Larvae lifecycle analysis.
- Bee death/age analysis.
- Temperature/humidity compatibility reports.
- Full mutation tree encyclopedia.
- JEI/REI integration.
- Genetic sample banking.
- Bee itemization as a normal workflow.
```

## 15. Implementation Notes For AI Agents

When implementing analyzer UX:

```txt
Scope:
- Add analyzed state.
- Update analyzer item to mark bees analyzed.
- Add UI or structured report screen.
- Hide tooltip/apiary genetic details unless analyzed.

Out of scope:
- Lifecycle systems.
- Environment simulation.
- Resource bees.
- Full research tree.
- Bee itemization.

Do not:
- Reveal all genetics automatically.
- Keep chat as the final UX.
- Store analysis rules inside random GUI classes.
```

## Analyzer Visual Assets

Analyzer UI and item visuals require final generated assets, not placeholders.

Required assets may include:

```text
- analyzer item icon;
- analyzer GUI background;
- analyzer screen widgets or icons;
- genetic trait icons;
- analyzed/unanalyzed state indicators.
```

For each required asset, the implementation must create an asset prompt under:

```text
docs/art/prompts/items/
docs/art/prompts/gui/
```

The analyzer UI can be implemented structurally before final art exists, but the visual task is not complete until generated assets are integrated and validated.

## 16. Acceptance Criteria

This phase is successful when:

```txt
- Bees have an analyzed/unanalysed state.
- Portable Analyzer can analyze a living bee.
- Genetic details are hidden before analysis.
- Genetic details are visible after analysis.
- Apiary UI and tooltips respect analysis state.
- Analyzer output is presented through real UX, not only debug chat.
- The system remains compatible with future deeper analyzer/research features.
```
