# Phase 7 Implementation Spec — Tech Apiary and Automation

## 1. Purpose

This document defines the future implementation plan for the **Tech Apiary and Automation** phase of Curious Bees.

This phase introduces Forestry-like technical control on top of the vanilla bee genetics loop.

It is intentionally **not part of the initial MVP critical path**.

The natural breeding loop should work first:

```text
wild bee genome storage
+ vanilla breeding inheritance
+ probabilistic mutation
+ analyzer
+ basic production
```

Only after that should the project add controlled tech blocks such as apiaries, frames, machines, and automation support.

## 2. Phase Position

Recommended phase order:

```text
Phase 0 — Documentation and Decisions
Phase 1 — Genetics Core
Phase 2 — Initial Content
Phase 3 — NeoForge Bee Data Integration
Phase 4 — Vanilla Breeding Integration
Phase 5 — Analyzer MVP
Phase 6 — Production MVP
Phase 7 — Tech Apiary and Automation
Phase 8 — Data-Driven Content
Phase 9 — Fabric Support
Phase 10 — Expanded Content
```

Phase 7 depends on:

```text
- Bee genomes existing and persisting on vanilla bees.
- BreedingService and MutationService being stable.
- Built-in content definitions existing.
- Analyzer MVP existing.
- Basic production definitions existing.
```

## 3. Design Goal

The tech apiary should provide **control and efficiency**, not replace the identity of the mod.

The main fantasy is still:

```text
build lineages
breed generations
stabilize genes
discover mutations
select valuable offspring
```

The apiary should make that loop more manageable and automatable.

It should not turn breeding into a deterministic crafting recipe.

## 4. Product Philosophy

### 4.1 Natural Breeding First

Players should be able to breed bees naturally in the world.

The apiary is a later upgrade.

### 4.2 Tech Should Improve Control

The apiary may improve:

```text
- breeding convenience
- production rate
- mutation chance
- output handling
- frame-based modifiers
- automation compatibility
```

It should not remove:

```text
- Mendelian inheritance
- recessive traits
- hybrid outcomes
- probabilistic mutation
- need for selection
```

### 4.3 Avoid Forestry Cloning

The apiary may be inspired by Forestry, but it should not copy Forestry 1:1.

Curious Bees should remain vanilla-bee-based and modern.

### 4.4 Avoid Early Complexity

Do not implement:

```text
full multiblock apiary
large machine chain
complex power system
large GUI tree
resource bees
automation compatibility
```

until the basic apiary behavior is proven.

## 5. Phase 7 Non-Goals

Do not implement during the first Tech Apiary slice:

```text
- resource bees;
- Fabric support;
- large GUI frameworks;
- complex networking;
- full JEI/REI integration;
- custom energy system;
- full item transport compatibility;
- multiblock structures;
- automatic genetic editing;
- deterministic mutation recipes;
- polished Blockbench asset pack as a blocker;
- advanced research system;
- large species tree.
```

## 6. Recommended Phase 7 Sub-Phases

Split this phase into smaller implementation waves:

```text
7A — Tech Apiary Design Decision
7B — Minimal Apiary Block Registration
7C — Apiary Block Entity and Inventory
7D — Apiary Breeding Cycle
7E — Frame Modifier Model
7F — Basic Frames
7G — Apiary Production Cycle
7H — Automation Hooks
7I — Visual Assets and Blockbench Workflow
7J — Phase 7 Review and Balancing
```

Each sub-phase should have its own verification gate.

---

# 7A — Tech Apiary Design Decision

## Objective

Decide the first version of the Genetic Apiary design before coding it.

This is a design task, not implementation.

## Key Questions

Answer these before implementing the apiary:

```text
1. Does the apiary use living bees, captured bee items, or both?
2. Does the apiary perform breeding, production, or both?
3. Does it require two parent bees, or a queen/drone-like model?
4. Does it consume bees, duplicate bees, or produce larvae/samples?
5. Does it need flowers nearby?
6. Does it need frames?
7. How much should it resemble Forestry?
8. How much should it remain compatible with vanilla bees?
9. Should it be automatable from day one?
10. What is the minimum useful MVP apiary behavior?
```

## Recommended Initial Decision

For the first apiary implementation, prefer:

```text
- a single-block Genetic Apiary;
- uses bee-containing items or captured bee samples, not free-flying bees;
- accepts two parent genome inputs;
- runs a timed breeding cycle;
- produces one child bee item/sample;
- optionally produces combs;
- supports frame slots later;
- does not require energy initially;
- does not replace natural breeding.
```

## Alternative Designs

### Option A — Captured Bee Items

The apiary accepts bee items with stored genomes.

Pros:

```text
- easier to automate;
- less entity complexity;
- simpler UI;
- easier persistence;
- safer for tests.
```

Cons:

```text
- less vanilla-living-bee feeling;
- requires a capture/storage item system.
```

### Option B — Nearby Living Bees

The apiary scans for living bees nearby.

Pros:

```text
- more natural;
- keeps bees in the world.
```

Cons:

```text
- much harder to automate;
- entity selection can be confusing;
- harder to debug;
- chunk/loading issues.
```

### Option C — Hybrid Model

The apiary can accept bee items but can also interact with living bees later.

Pros:

```text
- best long-term flexibility.
```

Cons:

```text
- more design complexity.
```

## Recommended First Choice

Use **captured bee items or bee samples** for the first tech apiary.

Natural breeding already covers living bees.

The tech apiary should be a controlled workstation.

## Acceptance Criteria

The design decision is complete when the project has documented:

```text
- input model;
- output model;
- whether production is included;
- whether frames are included;
- whether energy is required;
- whether automation is supported in the first version;
- what is explicitly out of scope.
```

## Verification Gate 1 — Apiary Design Review

Stop and review before coding.

Checklist:

```text
- Is the apiary MVP small enough?
- Does it avoid replacing vanilla breeding entirely?
- Is it still probabilistic?
- Does it avoid implementing resource bees?
- Is the chosen input/output model clear?
- Is automation delayed or scoped clearly?
```

## Claude Prompt

```text
Read CLAUDE.md and the docs.

Focus only on Phase 7A — Tech Apiary Design Decision.

Do not implement code.

Produce a short technical/product decision note for the first Genetic Apiary design:
- inputs
- outputs
- cycle behavior
- whether it breeds, produces, or both
- whether frames exist in first version
- whether energy exists in first version
- what is out of scope

Keep the first version small and compatible with the existing genetics architecture.
```

---

# 7B — Minimal Apiary Block Registration

## Objective

Register the first Genetic Apiary block and item with placeholder visuals.

This task should not implement full functionality yet.

## Scope

Implement:

```text
GeneticApiaryBlock
GeneticApiaryBlockItem
basic blockstate/model placeholder
basic lang entry
creative tab placement if applicable
```

## Non-Goals

Do not implement:

```text
- full GUI;
- inventory;
- block entity logic;
- breeding cycles;
- frames;
- production;
- automation;
- energy;
- custom Blockbench model requirement.
```

## Placeholder Asset Strategy

Use standard Minecraft block model JSON first.

Example:

```text
cube_all with a temporary texture
```

or a simple generated placeholder.

Do not block this task on final art.

## Suggested Files

```text
neoforge/src/main/java/.../registry/ModBlocks.java
neoforge/src/main/java/.../block/GeneticApiaryBlock.java
neoforge/src/main/resources/assets/curiousbees/blockstates/genetic_apiary.json
neoforge/src/main/resources/assets/curiousbees/models/block/genetic_apiary.json
neoforge/src/main/resources/assets/curiousbees/models/item/genetic_apiary.json
neoforge/src/main/resources/assets/curiousbees/lang/en_us.json
```

Exact paths should follow the actual project structure.

## Acceptance Criteria

```text
- Genetic Apiary block is registered.
- Genetic Apiary item exists.
- Block can be placed in the world.
- Block can be broken and picked up.
- Placeholder model/texture is acceptable.
- No breeding or production behavior exists yet.
```

## Expected Validation

```text
- Launch dev client.
- Confirm block appears in creative inventory or can be given by command.
- Place block.
- Break block.
- Confirm no crash.
```

## Verification Gate 2 — Block Registration Review

Stop and review before adding block entity logic.

Checklist:

```text
- Did the block register cleanly?
- Did it avoid premature GUI logic?
- Did it avoid premature breeding logic?
- Are placeholder assets acceptable?
- Are resources correctly named and namespaced?
```

## Claude Prompt

```text
Read CLAUDE.md and the relevant docs.

Focus only on Phase 7B — Minimal Apiary Block Registration.

Register a Genetic Apiary block and block item with placeholder resources.

Do not implement inventory, block entity, GUI, breeding, production, frames, automation, energy, Fabric support or custom model complexity.

Before coding, list expected files.
After coding, describe how to validate the block in-game.
```

---

# 7C — Apiary Block Entity and Inventory

## Objective

Add a block entity and minimal inventory for the Genetic Apiary.

## Scope

Implement block entity storage for:

```text
Parent Slot A
Parent Slot B
Output Slot
Optional Frame Slot 1
Optional Frame Slot 2
Optional Frame Slot 3
```

For the first implementation, frames may be omitted or left reserved.

Recommended initial slots:

```text
Slot 0 — Bee input A
Slot 1 — Bee input B
Slot 2 — Child/output
```

## Bee Input Model

This depends on Phase 7A.

If bee item/samples exist:

```text
input slots accept bee genome item stacks
```

If bee items do not exist yet:

```text
implement only inventory skeleton and defer actual valid item checks
```

## Non-Goals

Do not implement:

```text
- full breeding cycle;
- production cycle;
- GUI screen;
- automation;
- frame modifiers;
- energy;
- advanced syncing.
```

## Persistence Requirements

The block entity should persist:

```text
- inventory contents;
- progress value, if introduced;
- simple state needed for future cycle logic.
```

## Acceptance Criteria

```text
- Genetic Apiary has a block entity.
- Block entity persists its inventory.
- It can be opened or debugged enough to validate contents.
- No breeding occurs yet.
- No production occurs yet.
```

If GUI is not implemented yet, validation can be done with debug commands/logging.

## Verification Gate 3 — Block Entity Persistence Review

Stop and verify before adding cycle logic.

Checklist:

```text
- Does the block entity save/load safely?
- Does it avoid losing inventory on world reload?
- Are inventory slots clearly defined?
- Is the inventory model small enough?
- Is networking deferred unless needed?
```

## Claude Prompt

```text
Read CLAUDE.md and the relevant docs.

Focus only on Phase 7C — Apiary Block Entity and Inventory.

Add a block entity for Genetic Apiary with minimal persistent inventory.

Do not implement breeding, production, GUI screen, frames, automation, energy or Fabric support.

Before coding, describe the slot layout and persistence approach.
After coding, describe how to validate save/load behavior.
```

---

# 7D — Apiary Breeding Cycle

## Objective

Implement a controlled breeding cycle inside the Genetic Apiary.

This cycle should use the existing common genetics services.

## Scope

The apiary should:

```text
- read two parent genomes from input items or samples;
- run a timed cycle;
- call common BreedingService;
- call common MutationService;
- produce one child bee item/sample with a genome;
- consume or preserve inputs according to the design decision;
- store progress safely.
```

## Important Architecture Rule

The block entity should not contain genetics logic.

Good:

```text
ApiaryBlockEntity -> ApiaryBreedingController -> BreedingService + MutationService
```

Bad:

```text
ApiaryBlockEntity directly implements Mendelian inheritance and mutation.
```

## Cycle Model

Initial simple cycle:

```text
if input A valid and input B valid and output slot empty:
    increment progress each tick
    when progress >= maxProgress:
        generate child genome
        place child item in output
        reset progress
```

## Non-Goals

Do not implement:

```text
- production outputs;
- frames;
- energy;
- advanced GUI;
- automation;
- multiple children;
- resource bees;
- complex environmental conditions.
```

## Acceptance Criteria

```text
- Apiary can generate a child genome from two parent genomes.
- It uses existing BreedingService and MutationService.
- It does not duplicate genetic logic.
- It handles missing/invalid input safely.
- It does not overwrite occupied output.
- Progress persists or resets safely according to documented behavior.
```

## Expected Validation

```text
- Put two valid bee items/samples in input slots.
- Wait for cycle.
- Output child item appears.
- Inspect child genome.
- Confirm parent input behavior matches design.
- Confirm mutation can occur if mutation rules match.
```

## Verification Gate 4 — Apiary Breeding Review

Stop before adding frames or production.

Checklist:

```text
- Does the apiary use common genetics services?
- Does it preserve probabilistic inheritance?
- Does it avoid deterministic A+B=C recipes?
- Does it handle output slot safely?
- Can child output be inspected?
- Is cycle behavior understandable?
```

## Claude Prompt

```text
Read CLAUDE.md and the relevant docs.

Focus only on Phase 7D — Apiary Breeding Cycle.

Implement a minimal controlled breeding cycle for the Genetic Apiary using existing common BreedingService and MutationService.

Do not implement production, frames, energy, automation, advanced GUI, resource bees, Fabric support or genetic editing.

Before coding, explain how parent genomes are read and how the child genome is written.
After coding, describe in-game validation steps.
```

---

# 7E — Frame Modifier Model

## Objective

Define a small model for frame-based modifiers without implementing many frame items yet.

Frames are optional upgrade items that influence apiary behavior.

## Scope

Create a common model for:

```text
mutationChanceMultiplier
productionRateMultiplier
lifespanModifier or cycleModifier
durability/uses
```

Do not add all possible modifiers at once.

Initial frame modifiers should support only:

```text
mutation chance
production speed/chance
```

## Non-Goals

Do not implement:

```text
- many frame tiers;
- complex durability mechanics;
- special frame effects;
- genetic editing;
- JSON loading;
- power systems.
```

## Suggested Model

```text
FrameModifier
- id
- mutationMultiplier
- productionMultiplier
- durabilityCostPerCycle
```

or equivalent.

## Acceptance Criteria

```text
- Frame modifier model exists.
- It is independent from NeoForge item classes if possible.
- It can combine multiple frame modifiers.
- It can be tested without Minecraft if implemented in common code.
```

## Verification Gate 5 — Frame Model Review

Stop before adding frame items.

Checklist:

```text
- Is the frame model small?
- Is it testable?
- Does it avoid overengineering?
- Does it avoid specific item dependencies in common code?
```

## Claude Prompt

```text
Read CLAUDE.md and the relevant docs.

Focus only on Phase 7E — Frame Modifier Model.

Implement or specify a minimal frame modifier model that can influence mutation chance and production rate.

Do not implement many frame items, JSON loading, energy, GUI, resource bees or genetic editing.

Add tests if this is implemented in common code.
```

---

# 7F — Basic Frames

## Objective

Add the first basic frame items and connect them to the apiary.

## Initial Frames

Recommended first frames:

```text
Basic Frame
Mutation Frame
Productivity Frame
```

## Scope

Implement:

```text
frame item registration
basic placeholder texture/model
frame slot validation
frame modifier extraction
modifier application in apiary cycle
```

## Non-Goals

Do not implement:

```text
- many tiers;
- complex durability;
- advanced effects;
- special crafting chain;
- resource bees;
- energy system.
```

## Suggested Behavior

```text
Basic Frame:
    small general efficiency bonus

Mutation Frame:
    increases mutation chance

Productivity Frame:
    increases production rate/chance
```

Exact numbers are placeholders.

Example:

```text
Basic Frame: mutation x1.05, production x1.05
Mutation Frame: mutation x1.25, production x1.00
Productivity Frame: mutation x1.00, production x1.25
```

## Acceptance Criteria

```text
- Frame items exist.
- Apiary can accept frame items in frame slots.
- Frame modifiers affect the intended cycle.
- Effects are visible in debug logs or analyzer/debug output.
- Placeholder assets are acceptable.
```

## Verification Gate 6 — Basic Frames Review

Checklist:

```text
- Do frame effects work?
- Are numbers easy to adjust?
- Are frames optional?
- Does mutation remain probabilistic?
- Did the implementation avoid complex durability unless planned?
```

## Claude Prompt

```text
Read CLAUDE.md and the relevant docs.

Focus only on Phase 7F — Basic Frames.

Add Basic Frame, Mutation Frame and Productivity Frame with simple modifier behavior for the Genetic Apiary.

Do not add advanced tiers, energy, complex durability, resource bees, GUI complexity, JSON loading or Fabric support.

Use placeholder assets.
```

---

# 7G — Apiary Production Cycle

## Objective

Allow the Genetic Apiary to produce outputs based on bee genomes.

This should use the existing production resolver from Phase 6.

## Scope

The apiary may:

```text
- read a bee genome from an input bee/sample;
- run a production cycle;
- call common ProductionResolver;
- place output items in output slots;
- apply productivity trait and frame modifiers;
- avoid overflowing outputs.
```

## Design Decision Needed

Before implementation, decide whether the first apiary:

```text
A. breeds only;
B. produces only;
C. can do both;
D. has mode switching;
E. requires separate blocks for breeding and production.
```

Recommended early approach:

```text
Keep breeding and production separate until the breeding apiary works.
```

Option:

```text
Genetic Apiary = breeding
Genetic Hive = production
```

or:

```text
Genetic Apiary has a simple mode later
```

## Non-Goals

Do not implement:

```text
- centrifuge;
- complex item routing;
- resource bee outputs;
- advanced machine chain;
- energy;
- multiblock.
```

## Acceptance Criteria

```text
- Production cycle uses common ProductionResolver.
- Active species determines primary output.
- Inactive species can contribute secondary output if supported.
- Productivity and frames modify behavior.
- Output handling is safe.
```

## Verification Gate 7 — Apiary Production Decision

Stop before coding production if the block responsibility is unclear.

Checklist:

```text
- Is production in the same block or a separate block?
- Does the first production cycle need a living bee or bee item?
- Are outputs simple enough?
- Is centrifuge intentionally postponed?
```

## Claude Prompt

```text
Read CLAUDE.md and the relevant docs.

Focus only on Phase 7G — Apiary Production Cycle.

First confirm whether production belongs in Genetic Apiary or a separate Genetic Hive based on the existing docs and current implementation.

Do not implement centrifuge, resource bees, energy, multiblock, complex item routing or Fabric support.

If coding, use the existing common ProductionResolver and keep the platform block entity thin.
```

---

# 7H — Automation Hooks

## Objective

Make the tech blocks reasonably compatible with item automation.

## Scope

Support basic inventory automation for:

```text
input slots
frame slots
output slots
```

This may involve NeoForge item handler capabilities or equivalent inventory exposure.

## Non-Goals

Do not implement:

```text
- specific mod compatibility;
- Create integration;
- pipe networks;
- custom logistics;
- advanced filters;
- Fabric automation.
```

## Automation Rules

Recommended slot behavior:

```text
Bee input slots:
    accept only bee items/samples

Frame slots:
    accept only frame items

Output slots:
    cannot accept inserted items
    allow extraction
```

## Acceptance Criteria

```text
- Inputs reject invalid items.
- Outputs cannot be inserted into.
- Outputs can be extracted.
- Frame slots validate frame items.
- Manual interaction still works.
```

## Verification Gate 8 — Automation Review

Checklist:

```text
- Are slot rules clear?
- Is automation basic but usable?
- Did we avoid hardcoding compatibility for other mods?
- Does automation preserve gameplay balance?
```

## Claude Prompt

```text
Read CLAUDE.md and the relevant docs.

Focus only on Phase 7H — Automation Hooks.

Expose basic inventory automation for the Genetic Apiary or related tech block.

Do not implement specific mod compatibility, logistics networks, Create integration, Fabric support or advanced filtering.

Keep slot rules simple and documented.
```

---

# 7I — Visual Assets and Blockbench Workflow

## Objective

Define and implement visual assets for tech blocks only after functionality is stable.

## Important Rule

Assets should not block core functionality.

Use placeholders first.

## Asset Types

Phase 7 may need:

```text
Genetic Apiary block texture/model
Frame item textures
Optional animated block model
Optional UI background later
```

## Blockbench Usage

Blockbench is useful for:

```text
custom low-poly block models
machine-style block models
animated block models later
```

It is not required for:

```text
simple item textures
generated item models
basic cube blocks
```

## Recommended Workflow

```text
1. Define asset list.
2. Use placeholder assets during coding.
3. Create Blockbench source files under assets-source/blockbench/.
4. Export Minecraft model JSON when appropriate.
5. Put final runtime assets under src/main/resources/assets/curiousbees/.
6. Keep source art files versioned if useful.
```

## Suggested Source Layout

```text
assets-source/
├── blockbench/
│   ├── genetic_apiary.bbmodel
│   └── future_centrifuge.bbmodel
├── textures-source/
└── references/
```

Runtime layout:

```text
src/main/resources/assets/curiousbees/
├── textures/
│   ├── block/
│   └── item/
├── models/
│   ├── block/
│   └── item/
├── blockstates/
└── lang/
```

## MCP / Automation Caution

Blockbench MCP or AI-driven asset tooling may be useful later, but it should not be required for implementation.

If used:

```text
- run in a controlled environment;
- do not expose arbitrary scripts blindly;
- review generated assets manually;
- keep source files versioned;
- validate models in-game.
```

## Acceptance Criteria

```text
- Placeholder assets exist before polished assets.
- Final assets do not block gameplay implementation.
- Blockbench source files are optional but organized.
- Runtime assets use correct Minecraft paths and namespaces.
```

## Verification Gate 9 — Asset Pipeline Review

Checklist:

```text
- Are assets needed now or can placeholders remain?
- Are source files stored separately from runtime assets?
- Are model paths correct?
- Are textures namespaced correctly?
- Did art work avoid blocking code tasks?
```

## Claude Prompt

```text
Read CLAUDE.md and the relevant docs.

Focus only on Phase 7I — Visual Assets and Blockbench Workflow.

Do not implement gameplay code.

Create or update documentation for the asset pipeline, placeholder strategy and Blockbench source file organization.

If adding placeholder runtime assets, keep them minimal.
```

---

# 7J — Phase 7 Review and Balancing

## Objective

Review the tech apiary feature set before expanding into data-driven content, Fabric support, or large content trees.

## Review Checklist

```text
- Does natural breeding still matter?
- Does the apiary improve control without removing genetic uncertainty?
- Does the apiary use common genetics services?
- Are frames optional and understandable?
- Are outputs balanced enough for MVP?
- Is automation basic but not overpowered?
- Are assets acceptable?
- Did we avoid resource bees?
- Did we avoid complex machine chains?
- Did we avoid Fabric implementation too early?
```

## Balance Questions

```text
- How long should a breeding cycle take?
- How much should mutation frames increase mutation chance?
- Should apiary breeding consume parent samples?
- Should apiary breeding require flowers or flower-type inputs?
- Should productivity affect apiary output speed?
- Should fertility matter in apiary output?
- Should lifespan matter in apiary cycles?
```

## Potential Follow-Up Tasks

```text
TASK — Tune apiary breeding cycle duration
TASK — Tune frame modifier values
TASK — Add frame durability
TASK — Add simple apiary GUI
TASK — Add recipe for Genetic Apiary
TASK — Add recipe for Basic Frame
TASK — Add recipe for Mutation Frame
TASK — Add recipe for Productivity Frame
TASK — Add Blockbench model for Genetic Apiary
TASK — Decide whether centrifuge belongs in MVP+
```

## Verification Gate 10 — Phase 7 Exit Review

Phase 7 is complete only when:

```text
- Tech apiary design is documented.
- Minimal apiary block exists.
- Block entity/inventory works.
- Breeding cycle works if included.
- Frame model exists if included.
- Basic frames work if included.
- Production cycle decision is made.
- Automation rules are documented or implemented.
- Placeholder or final assets exist.
- Natural breeding remains useful.
```

---

# Phase 7 Go / No-Go Checklist

Before starting Phase 7, confirm:

```text
- Phase 1 Genetics Core is complete and tested.
- Phase 2 Initial Content exists.
- Phase 3 Bee genome storage works.
- Phase 4 Vanilla breeding integration works.
- Phase 5 Analyzer MVP works.
- Phase 6 Production MVP is at least partially defined.
```

If any of these are not true, do not start full apiary implementation.

It is acceptable to draft Phase 7 docs early, but not to implement the apiary before the core gameplay loop works.

---

# Recommended First Phase 7 Task

The first actual task should be:

```text
TASK — Decide first Genetic Apiary design
```

Not:

```text
TASK — Implement full Genetic Apiary
```

The second task should be:

```text
TASK — Register minimal Genetic Apiary block
```

Only then:

```text
TASK — Add Genetic Apiary block entity and inventory
```

---

# AI Failure Modes to Watch

## Failure Mode 1 — Building Forestry Clone Too Early

Symptom:

```text
Agent implements queen/drone princess system, apiary, centrifuge, frames, lifespan death cycles and full machinery in one pass.
```

Fix:

```text
Stop. Split into smaller tasks. Keep vanilla bee identity.
```

## Failure Mode 2 — Deterministic Apiary Recipes

Symptom:

```text
Apiary turns Meadow + Forest directly into Cultivated every time.
```

Fix:

```text
Apiary must use BreedingService and MutationService.
Mutation remains probabilistic.
```

## Failure Mode 3 — Genetics Logic in Block Entity

Symptom:

```text
Block entity manually picks alleles and applies mutation.
```

Fix:

```text
Move genetics logic to common services.
Block entity only orchestrates inventory and cycle.
```

## Failure Mode 4 — Asset Pipeline Blocks Gameplay

Symptom:

```text
Implementation waits for perfect Blockbench model before block logic works.
```

Fix:

```text
Use placeholder block model and continue.
```

## Failure Mode 5 — Automation Too Complex Too Early

Symptom:

```text
Agent adds custom pipe network, filters, routing rules and mod compat.
```

Fix:

```text
Only expose basic inventory rules.
```

## Failure Mode 6 — Energy System Appears Prematurely

Symptom:

```text
Agent adds RF/FE-style energy system before apiary loop is proven.
```

Fix:

```text
Remove energy unless explicitly planned.
```

---

# Phase 7 Handoff Prompt

Use this prompt when Phase 7 is ready to begin:

```text
Read CLAUDE.md and all docs.

Then read:
- docs/implementation/07-tech-apiary-and-automation.md
- docs/implementation/01-genetics-core-implementation.md
- docs/implementation/02-initial-content-implementation.md
- docs/implementation/04-vanilla-breeding-integration.md
- docs/implementation/06-production-mvp.md

Do not implement code yet.

First focus only on Phase 7A — Tech Apiary Design Decision.

Produce a concise design decision note for the first Genetic Apiary:
- input model
- output model
- breeding behavior
- production behavior
- frame behavior
- energy decision
- automation decision
- non-goals

Do not implement apiary, frames, GUI, production machines, assets, Fabric support or resource bees yet.
```

---

# Phase 7 Final Definition of Done

Phase 7 is done when the project has a controlled tech beekeeping layer that:

```text
- is optional after natural breeding works;
- uses the common genetics core;
- supports controlled breeding or production;
- preserves probabilistic genetics;
- supports basic upgrade frames if included;
- has safe inventory behavior;
- has basic placeholder or final visuals;
- does not introduce resource bee progression prematurely;
- does not break future data-driven or Fabric support.
```
