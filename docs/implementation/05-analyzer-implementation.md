# 05 — Analyzer MVP Implementation

## 1. Purpose

This document defines the detailed implementation plan for **Phase 5 — Analyzer MVP** of Curious Bees.

The Analyzer MVP is the first player-facing tool that makes bee genetics understandable in-game.

Until this phase, the project has focused on:

- pure genetics logic;
- built-in content;
- genome persistence on vanilla bees;
- vanilla breeding integration;
- mutation assignment.

At this point, bees may already have genomes, inherit alleles, and mutate. However, without an analyzer, most of that system is invisible to the player.

The goal of this phase is to let the player inspect a bee and make breeding decisions.

## 2. Phase Goal

Implement a minimal but useful **Bee Analyzer** that can inspect vanilla bees and display their genetics.

The first version should be practical, testable, and simple.

It does **not** need a polished GUI.

A chat/action-bar/report style output is acceptable for the MVP.

## 3. Phase Non-Goals

Do not implement the following in this phase:

```text
- Advanced GUI screen
- Research/discovery progression
- Mutation prediction UI
- JEI/REI integration
- Gene editing
- Genetic manipulation
- Apiary UI
- Production UI
- Custom bee models
- Custom bee textures
- Full analyzer tiers
- Fabric support
- Resource bees
- Full data-driven analyzer formatting
```

This phase is about **reading and displaying existing genetics**, not changing them.

## 4. Required Inputs from Previous Phases

This phase assumes the following already exist:

```text
Phase 1:
- Allele
- GenePair
- Genome
- ChromosomeType
- BreedingService
- MutationService
- GeneticRandom
- tests

Phase 2:
- initial species definitions
- initial trait alleles
- built-in content registry/facade
- default genome factory

Phase 3:
- genome storage on vanilla Bee entities
- genome persistence through save/load
- safe missing-genome handling
- debug inspection command or equivalent

Phase 4:
- baby bee receives inherited genome
- mutation can occur during breeding
- mutation result is stored on the baby bee
```

If these are not implemented, this phase should not begin.

## 5. Player-Facing Behavior

The player should be able to inspect a bee using a Bee Analyzer item.

Initial player flow:

```text
1. Player crafts or obtains Bee Analyzer.
2. Player right-clicks / uses the analyzer on a vanilla bee.
3. The mod reads the bee's Genome.
4. The player receives a readable genetics report.
5. The report shows enough information to decide whether the bee is worth breeding.
```

The first output can be chat-based:

```text
Bee Genetics
Species: Cultivated / Forest
Purity: Hybrid
Lifespan: Normal / Long
Productivity: Fast / Normal
Fertility: Two / Three
Flower Type: Flowers / Leaves
```

The analyzer should not mutate, edit, or otherwise modify the bee.

## 6. Development Strategy

Implement the Analyzer MVP in small slices:

```text
1. Create analyzer report model in common code.
2. Create analyzer report formatter in common code.
3. Register a basic Bee Analyzer item in NeoForge.
4. Implement use-on-bee behavior.
5. Connect item interaction to genome storage.
6. Display simple report to player.
7. Improve formatting.
8. Add validation and in-game test checklist.
```

The common report logic should not depend on Minecraft APIs.

The NeoForge item should only call services and render output.

## 7. Recommended Package Structure

Common code:

```text
common/src/main/java/.../common/gameplay/analysis/
├── BeeAnalysisReport.java
├── BeeAnalysisService.java
├── BeeAnalysisFormatter.java
└── FormattedGeneLine.java
```

NeoForge code:

```text
neoforge/src/main/java/.../neoforge/item/
├── BeeAnalyzerItem.java

neoforge/src/main/java/.../neoforge/registry/
├── ModItems.java

neoforge/src/main/resources/assets/curiousbees/lang/
├── en_us.json

neoforge/src/main/resources/assets/curiousbees/models/item/
├── bee_analyzer.json

neoforge/src/main/resources/assets/curiousbees/textures/item/
├── bee_analyzer.png
```

If a proper texture is not ready, use a placeholder texture or temporary generated item model.

## 8. Asset Strategy for This Phase

The Analyzer MVP may require an item asset.

Do not block implementation on polished art.

Acceptable MVP asset options:

```text
- simple placeholder 16x16 texture
- recolored vanilla-looking placeholder
- generated item model using a temporary texture
- missing texture during early dev, if acceptable locally
```

Blockbench is not required for this phase.

Blockbench becomes relevant later for custom blocks/machines such as Apiary, Centrifuge, and Frame Housing.

## 9. Output Strategy

The first Analyzer should use simple output.

Preferred MVP output options:

```text
Option A — chat messages
Option B — action bar summary + chat details
Option C — temporary debug-style report
```

Recommended first implementation:

```text
Use chat messages.
```

Reason:

```text
- easy to implement
- easy to test
- does not require GUI
- does not require networking beyond normal server-to-player messages
- enough for early gameplay validation
```

## 10. Analyzer Report Model

### Objective

Create a Minecraft-independent report model representing what the analyzer should display.

### Suggested Model

```java
public final class BeeAnalysisReport {
    private final GeneReport species;
    private final GeneReport lifespan;
    private final GeneReport productivity;
    private final GeneReport fertility;
    private final GeneReport flowerType;
    private final boolean speciesPurebred;
}
```

Alternative shapes are acceptable if they remain simple and testable.

### Suggested Gene Report

```java
public final class GeneReport {
    private final String chromosomeName;
    private final String activeAlleleId;
    private final String inactiveAlleleId;
    private final Dominance activeDominance;
    private final Dominance inactiveDominance;
    private final boolean purebred;
}
```

If display names are not available yet, allele IDs are acceptable.

Future content can provide localized/display names.

### Non-Goals

Do not include:

```text
- mutation chances
- hidden discoveries
- production forecasts
- genetic history
- parent history
- editable genes
```

### Acceptance Criteria

```text
- BeeAnalysisReport can represent Species, Lifespan, Productivity, Fertility, and FlowerType.
- It can represent active and inactive values.
- It can represent purebred/hybrid state.
- It is independent from Minecraft/NeoForge/Fabric.
```

## 11. BeeAnalysisService

### Objective

Create a service that converts a `Genome` into a `BeeAnalysisReport`.

### Input

```text
Genome genome
```

### Output

```text
BeeAnalysisReport
```

### Expected Behavior

For each MVP chromosome:

```text
1. Read GenePair from Genome.
2. Read active allele.
3. Read inactive allele.
4. Read dominance values.
5. Determine purebred/hybrid.
6. Add it to the report.
```

### Missing Chromosomes

The genome should normally contain all MVP chromosomes.

If a chromosome is missing due to old data or failed migration, the service should fail clearly or return an incomplete report depending on the project's error handling strategy.

Recommended MVP behavior:

```text
- fail fast in unit tests and development
- handle gracefully in platform UI if needed
```

### Acceptance Criteria

```text
- BeeAnalysisService exists in common gameplay/analysis code.
- It accepts Genome and returns BeeAnalysisReport.
- It does not import Minecraft, NeoForge, or Fabric classes.
- It covers Species, Lifespan, Productivity, Fertility, and FlowerType.
- Unit tests validate pure and hybrid genome reports.
```

### Expected Tests

```text
pure Meadow genome produces pure species report
Cultivated / Forest genome produces hybrid species report
active and inactive alleles are preserved in report
all MVP chromosomes appear in report
service does not mutate genome
```

## 12. BeeAnalysisFormatter

### Objective

Create a common text formatter that turns a `BeeAnalysisReport` into simple lines.

The formatter should still be Minecraft-independent.

### Input

```text
BeeAnalysisReport
```

### Output

```text
List<String>
```

### Example Output

```text
Bee Genetics
Species: Cultivated / Forest (Hybrid)
Lifespan: Normal / Long
Productivity: Fast / Normal
Fertility: Two / Three
Flower Type: Flowers / Leaves
```

### Optional Symbols

The first version may include simple symbols:

```text
[A] Active
[I] Inactive
[D] Dominant
[R] Recessive
```

Example:

```text
Species:
[A][D] Cultivated
[I][D] Forest
```

For MVP, keep it readable rather than clever.

### Acceptance Criteria

```text
- Formatter returns readable lines.
- Formatter includes active and inactive species.
- Formatter includes purebred/hybrid state.
- Formatter includes MVP traits.
- Formatter does not depend on Minecraft text components.
```

### Expected Tests

```text
formatter includes report title
formatter includes species line
formatter includes purebred/hybrid text
formatter includes all MVP traits
formatter handles allele ids or display labels consistently
```

## 13. Verification Gate 1 — Common Analyzer Review

Stop after implementing:

```text
BeeAnalysisReport
BeeAnalysisService
BeeAnalysisFormatter
unit tests
```

Review before moving to NeoForge item code.

### Checklist

```text
- No Minecraft imports in analyzer common code.
- Service reads all MVP chromosomes.
- Formatter output is readable.
- Tests cover pure and hybrid genomes.
- Output preserves active/inactive order.
- The analyzer does not mutate Genome.
```

Do not register the item until this gate passes.

## 14. Bee Analyzer Item Registration

### Objective

Register the first Bee Analyzer item in NeoForge.

### Scope

Implement:

```text
Bee Analyzer item registration
basic item class if needed
lang entry
item model
placeholder texture
creative tab placement, if project has a tab
```

### Recommended Item ID

```text
curiousbees:bee_analyzer
```

Or, if the actual mod id differs:

```text
<modid>:bee_analyzer
```

### Non-Goals

Do not implement:

```text
- advanced GUI
- screen handler
- networking
- mutation prediction
- item durability
- analyzer tiers
```

### Acceptance Criteria

```text
- Bee Analyzer item exists in-game.
- It has a readable name.
- It appears in an appropriate creative inventory location or can be obtained for testing.
- Registration follows existing NeoForge project conventions.
```

### Expected Manual Validation

```text
start dev client
open creative inventory or give command
confirm Bee Analyzer item exists
confirm item name is readable
confirm no missing model crash
```

## 15. Verification Gate 2 — Item Registration Review

Stop after item registration.

### Checklist

```text
- Item exists in-game.
- No analyzer behavior implemented yet unless explicitly part of item class skeleton.
- Placeholder asset strategy is acceptable.
- No common code imports NeoForge.
- Item registration follows project conventions.
```

## 16. Use-On-Bee Behavior

### Objective

Allow a player to use the Bee Analyzer item on a vanilla Bee entity.

### Expected Flow

```text
1. Player uses Bee Analyzer on Bee.
2. NeoForge/item interaction detects target entity.
3. Platform code reads Genome from Bee genome storage.
4. If genome exists:
   - call BeeAnalysisService
   - call BeeAnalysisFormatter
   - send lines to player
5. If genome is missing:
   - show safe message
   - do not crash
```

### Missing Genome Message

Example:

```text
This bee has no Curious Bees genome data yet.
```

Optional dev-friendly message:

```text
No genome found. This bee may need initialization.
```

### Non-Goals

Do not implement:

```text
- genome initialization from analyzer
- gene editing
- breeding trigger
- mutation trigger
- analyzer GUI
- analyzer tiers
```

The analyzer should inspect, not modify.

### Acceptance Criteria

```text
- Using the item on a Bee reads the bee's genome.
- Output is sent to the player.
- Missing genome is handled safely.
- Using the item on non-bee entities either does nothing or shows a safe message.
- Analyzer does not modify genome data.
```

### Expected Manual Validation

```text
spawn or find bee with genome
use analyzer on bee
verify species line appears
verify trait lines appear
use analyzer on bee without genome if possible
verify safe missing-genome message
use analyzer on non-bee entity
verify no crash
```

## 17. Verification Gate 3 — First In-Game Analyzer Review

Stop after use-on-bee behavior works.

### Checklist

```text
- Analyzer can inspect a genetic bee.
- Analyzer does not crash on missing genome.
- Analyzer does not mutate genome.
- Output includes active/inactive species.
- Output includes purebred/hybrid status.
- Output includes MVP traits.
- Output is readable enough for breeding decisions.
```

Do not build a GUI until this passes.

## 18. Output Formatting Polish

### Objective

Improve the readability of the analyzer output without adding GUI complexity.

### Possible Improvements

```text
- use separate lines per chromosome
- use color only if simple and stable
- distinguish active/inactive values
- mark dominant/recessive with text or symbols
- show IDs only in debug mode if display names exist
```

### Recommended MVP Formatting

```text
Bee Genetics
Species: [A] Cultivated (D) / [I] Forest (D) — Hybrid
Lifespan: [A] Normal / [I] Long
Productivity: [A] Fast / [I] Normal
Fertility: [A] Two / [I] Three
Flower Type: [A] Flowers / [I] Leaves
```

If too verbose, use shorter lines.

### Non-Goals

Do not implement:

```text
- screen UI
- icons
- hover tooltips
- discovery system
- mutation tree display
```

### Acceptance Criteria

```text
- Output is easier to read than raw debug IDs.
- Active/inactive values are distinguishable.
- Purebred/hybrid state is obvious.
- Dominance is shown if available without clutter.
```

## 19. Verification Gate 4 — UX Readability Review

Stop after formatting polish.

### Checklist

```text
- Can a player tell what species the bee expresses?
- Can a player tell what species the bee carries?
- Can a player tell whether it is purebred or hybrid?
- Can a player compare two bees and choose which to breed?
- Is the output short enough to read during gameplay?
```

If the answer is no, adjust formatting before moving on.

## 20. Optional: Debug vs Player Mode

This is optional and may be deferred.

### Debug Mode

Could show:

```text
- raw allele IDs
- chromosome IDs
- dominance flags
- storage status
```

### Player Mode

Should show:

```text
- friendly names
- active/inactive values
- hybrid/purebred status
```

Do not implement if it adds too much complexity.

## 21. Optional: Analyzer Durability or Cost

Do not implement durability, fuel, charge, or crafting cost complexity in the MVP unless explicitly requested.

For early development, the analyzer can be simple.

Future options:

```text
- Analyzer consumes honeycomb
- Analyzer has durability
- Advanced analyzer reveals inactive genes
- Basic analyzer reveals only active genes
```

These are future gameplay design decisions.

## 22. Optional: Partial Visibility Design

Future analyzer progression may include:

```text
Basic Analyzer:
- active species
- active traits
- pure/hybrid indicator

Advanced Analyzer:
- inactive alleles
- dominance
- mutation hints

Research System:
- unlocks known mutation recipes after discovery
```

Do not implement this in MVP.

For now, full visibility is acceptable for development and testing.

## 23. Relationship to Assets

This phase may require:

```text
- one analyzer item texture
- one item model JSON
- one lang entry
```

This phase does not require:

```text
- Blockbench
- custom block models
- animated models
- custom bee models
- custom bee textures
```

If no final art exists, use placeholder assets and document them.

## 24. Relationship to Phase 6

Phase 6 will introduce production.

The Analyzer MVP should make Phase 6 easier by already showing:

```text
active species
inactive species
productivity trait
```

These values will later explain why production differs between bees.

Do not implement production output preview in Phase 5 unless explicitly requested.

## 25. AI Task Breakdown

Recommended tasks:

```text
TASK 5.1 — Implement common BeeAnalysisReport model
TASK 5.2 — Implement BeeAnalysisService
TASK 5.3 — Implement BeeAnalysisFormatter
TASK 5.4 — Register Bee Analyzer item
TASK 5.5 — Implement Bee Analyzer use-on-bee behavior
TASK 5.6 — Improve analyzer text formatting
TASK 5.7 — Add analyzer validation checklist / dev notes
```

## 26. Task 5.1 — Implement common BeeAnalysisReport model

### Prompt

```text
Read CLAUDE.md and docs/implementation/05-analyzer-implementation.md.

Focus only on TASK 5.1 — Implement common BeeAnalysisReport model.

Create a pure Java report model for analyzer output.

Do not implement NeoForge item registration, item behavior, GUI, mutation prediction, production, or Minecraft integration.

The model should represent active/inactive values and purebred/hybrid state for Species, Lifespan, Productivity, Fertility, and FlowerType.

Add unit tests if useful for validation.

Before coding, summarize your understanding and list files you expect to create or modify.
```

## 27. Task 5.2 — Implement BeeAnalysisService

### Prompt

```text
Read CLAUDE.md and docs/implementation/05-analyzer-implementation.md.

Focus only on TASK 5.2 — Implement BeeAnalysisService.

Implement a pure Java service that converts a Genome into a BeeAnalysisReport.

Do not implement NeoForge item behavior, GUI, mutation prediction, production, or Minecraft integration.

Add tests for pure and hybrid genomes.

Before coding, summarize your understanding and list files you expect to create or modify.
```

## 28. Task 5.3 — Implement BeeAnalysisFormatter

### Prompt

```text
Read CLAUDE.md and docs/implementation/05-analyzer-implementation.md.

Focus only on TASK 5.3 — Implement BeeAnalysisFormatter.

Implement a Minecraft-independent formatter that converts BeeAnalysisReport into readable text lines.

Do not use Minecraft Component/Text APIs in common code.

Add tests that verify the formatter includes species, purity, and all MVP traits.

Before coding, summarize your understanding and list files you expect to create or modify.
```

## 29. Task 5.4 — Register Bee Analyzer item

### Prompt

```text
Read CLAUDE.md and docs/implementation/05-analyzer-implementation.md.

Focus only on TASK 5.4 — Register Bee Analyzer item.

Register a basic Bee Analyzer item in NeoForge.

Do not implement analyzer behavior yet, except for a minimal item class if needed.

Do not implement GUI, mutation prediction, production, breeding, or genome editing.

Use placeholder item assets if final art does not exist.

Before coding, summarize your understanding and list files you expect to create or modify.
```

## 30. Task 5.5 — Implement Bee Analyzer use-on-bee behavior

### Prompt

```text
Read CLAUDE.md and docs/implementation/05-analyzer-implementation.md.

Focus only on TASK 5.5 — Implement Bee Analyzer use-on-bee behavior.

When a player uses the Bee Analyzer on a vanilla Bee entity, read the bee genome from the existing NeoForge storage adapter, convert it into a BeeAnalysisReport, format it, and send the result to the player.

Do not implement GUI, mutation prediction, production, breeding, genome editing, or Fabric support.

Handle missing genome safely.

Before coding, summarize your understanding and list files you expect to create or modify.
```

## 31. Task 5.6 — Improve analyzer text formatting

### Prompt

```text
Read CLAUDE.md and docs/implementation/05-analyzer-implementation.md.

Focus only on TASK 5.6 — Improve analyzer text formatting.

Improve the analyzer output so active/inactive values, dominance, and purebred/hybrid status are easy to understand.

Do not implement GUI, icons, discovery progression, mutation prediction, production, or genome editing.

Before coding, summarize your understanding and list files you expect to create or modify.
```

## 32. Phase Completion Checklist

Phase 5 is complete when:

```text
- Bee Analyzer item exists.
- Player can use it on a vanilla bee.
- It reads the bee's Genome.
- It displays active and inactive species.
- It displays purebred/hybrid status.
- It displays Lifespan, Productivity, Fertility, and FlowerType.
- Missing genomes are handled safely.
- Analyzer does not modify genome data.
- No advanced GUI is required.
- No production behavior is implemented in this phase.
```

## 33. Go / No-Go Checkpoint Before Phase 6

Before moving to Phase 6 — Basic Production, verify:

```text
- Player can inspect bees without debug commands.
- Player can distinguish purebred from hybrid bees.
- Player can compare offspring from breeding attempts.
- Mutation results are visible through analyzer output.
- Analyzer output is readable enough for MVP testing.
```

If the analyzer is not usable enough for player decision-making, improve it before starting production.

## 34. Common AI Failure Modes

Watch for these issues:

### Failure Mode 1 — Building a GUI too early

Symptom:

```text
Agent creates screen handlers, menus, networking, custom GUI textures.
```

Fix:

```text
Reject or revert. MVP analyzer should use chat/text output first.
```

### Failure Mode 2 — Analyzer edits genes

Symptom:

```text
Analyzer changes genome values, fixes missing genome, or triggers breeding.
```

Fix:

```text
Analyzer must inspect only.
```

### Failure Mode 3 — Common formatter uses Minecraft Components

Symptom:

```text
common/gameplay/analysis imports net.minecraft.network.chat.Component.
```

Fix:

```text
Common formatter should return strings or platform-neutral objects.
NeoForge layer converts to Minecraft components.
```

### Failure Mode 4 — Adding mutation prediction

Symptom:

```text
Analyzer shows possible mutation recipes, percentages, undiscovered species.
```

Fix:

```text
Mutation prediction is future scope.
```

### Failure Mode 5 — Blocking on polished assets

Symptom:

```text
Implementation stops because item art is not final.
```

Fix:

```text
Use placeholder texture/model and continue.
```

## 35. Suggested Commit Sequence

```text
gameplay: add analyzer report model
gameplay: add analyzer service
gameplay: add analyzer formatter
neoforge: register bee analyzer item
neoforge: inspect bee genome with analyzer
ux: improve analyzer output formatting
docs: add analyzer validation notes
```
