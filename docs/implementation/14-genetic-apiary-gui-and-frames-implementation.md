# 14 — Genetic Apiary GUI and Frames Implementation

## 1. Purpose

This document defines the implementation plan for **Phase 14 — Genetic Apiary GUI and Frames** of Curious Bees.

The MVP apiary proved that a technical bee block can exist. Phase 14 turns it into a real gameplay block with proper UI, visible state, frame modifiers, and automation-ready behavior.

Core identity:

```text
The apiary works with living vanilla bees. Bees do not become the main item form of the system.
```

## 2. Phase Goal

By the end of this phase, the project should be able to:

```text
- open a proper Genetic Apiary screen;
- show output inventory and production progress;
- show frame slots and active frame effects;
- show living bees associated with the apiary;
- show genetic details only for analyzed bees;
- keep unanalyzed bees redacted/unknown;
- support automation baseline without an Automation Upgrade;
- keep apiary breeding out of scope unless explicitly redesigned later.
```

## 3. Phase Non-goals

Do not implement:

```text
- Automation Upgrade as a permission gate;
- bee itemization as the default workflow;
- apiary breeding as the main breeding flow;
- lifecycle/death/larvae mechanics;
- temperature/humidity/environment status simulation;
- resource bees;
- large species trees;
- Fabric parity;
- complex research system.
```

Automation readiness is baseline behavior, not an upgrade.

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

If touching current apiary code, read existing Phase 7 implementation/review docs and source code first.

## 5. Expected Outputs

```text
Genetic Apiary menu/container
Genetic Apiary screen
bee summary display for living bees near/inside/associated with apiary
analyzed-state-aware bee genetic display
production progress display
output inventory display
frame slot display
frame modifier model/service
basic frame behavior updates
automation-ready item output handling
manual test checklist for apiary behavior
```

## 6. Architecture Rules

### 6.1 Apiary uses living bees

Good:

```text
Apiary discovers/tracks living Bee entities associated with the block.
GUI displays those living bees.
```

Bad:

```text
Apiary requires converting bees into item stacks as the baseline flow.
```

### 6.2 Automation is baseline

Good:

```text
Output inventory exposes extract behavior through standard capability/item handler patterns.
```

Bad:

```text
Automation only works after installing an Automation Upgrade.
```

### 6.3 Frames affect biological/production behavior

Frames are for modifiers such as productivity, mutation, stability, fertility later, and durability/cost tradeoffs.

Block behavior upgrades, if added later, are separate from frames.

### 6.4 No environment simulation yet

Do not show or simulate temperature, humidity, climate, biome suitability, rainfall/daylight optimization.

### 6.5 Reuse analyzer visibility rules

The apiary GUI must not reveal full genetics for unanalyzed bees.

## 7. Recommended Source Structure

Possible NeoForge packages:

```text
neoforge/src/main/java/.../block/
neoforge/src/main/java/.../blockentity/
neoforge/src/main/java/.../menu/
neoforge/src/main/java/.../client/screen/
neoforge/src/main/java/.../item/frame/
neoforge/src/main/java/.../automation/
```

Possible common packages:

```text
common/src/main/java/.../gameplay/apiary/
common/src/main/java/.../gameplay/frames/
```

## 8. Initial GUI Scope

The first real apiary GUI should show:

```text
- output slots;
- frame slots;
- production progress;
- current working state;
- associated bee summaries;
- active frame effects;
- basic status text such as Working / Waiting for bee / Output full.
```

Do not show environmental stats in this phase.

## 9. Initial Frame Scope

Initial frame types:

```text
Basic Frame
Productivity Frame
Mutation Frame
Stability Frame
```

Suggested frame model fields:

```text
id
displayName
productionMultiplier
mutationMultiplier
stabilityModifier
durability or damagePerOperation
notes/tooltip
```

If apiary does not perform mutation by design, Mutation Frame should be shown as planned/future or only applied to an existing supported mechanic. Do not secretly add apiary breeding.

## 10. Task Breakdown

## Task 1 — Review Existing Apiary Implementation

### Objective

Understand the current apiary block, block entity, frame slots, production logic, and automation behavior.

### Scope

Inspect apiary block/blockentity/menu packages if present, common production/apiary packages, frame items, capability/item handler code, and debug commands.

### Non-goals

Do not change code.

### Acceptance Criteria

```text
- Identify current apiary block entity state.
- Identify current inventory layout.
- Identify current frame behavior.
- Identify current automation/capability behavior.
- Identify missing pieces for GUI.
```

### Prompt for Claude Code

```text
Read CLAUDE.md and docs/post-mvp/14-genetic-apiary-gui-and-frames.md.

Focus only on Task 1 from docs/implementation/14-genetic-apiary-gui-and-frames-implementation.md.

Review the existing Genetic Apiary implementation and produce a short implementation plan. Do not modify files.

Summarize current apiary structure, frame behavior, automation support, and the smallest safe next implementation task.
```

## Task 2 — Define Apiary GUI Data Model

### Objective

Create a clear server-to-client data shape for the apiary screen.

### Scope

Define data needed by the GUI:

```text
output inventory summary
frame slot summary
production progress
working state
associated bee summaries
active frame effects
```

Bee summaries must respect analyzed state.

### Non-goals

Do not implement the actual screen yet.

### Acceptance Criteria

```text
- Apiary GUI data model exists or is clearly represented by menu fields.
- Bee summaries can be redacted for unanalyzed bees.
- Data model does not expose raw internal-only data unnecessarily.
- Common summary objects are testable where practical.
```

### Prompt for Claude Code

```text
Read docs/post-mvp/13-analyzer-ux-and-progression.md and docs/post-mvp/14-genetic-apiary-gui-and-frames.md.

Focus only on Task 2 from docs/implementation/14-genetic-apiary-gui-and-frames-implementation.md.

Define the data needed for the Genetic Apiary GUI, including redacted bee summaries for unanalyzed bees.

Do not implement the screen, frame mechanics overhaul, resource bees, lifecycle mechanics or environment status.

Before coding, list the data fields the GUI needs and where they will come from.
```

## Task 3 — Implement Genetic Apiary Menu/Container

### Objective

Create or update the server-side menu/container for the Genetic Apiary.

### Scope

Menu should support output slots, frame slots, player inventory, progress sync, and basic apiary state sync.

### Non-goals

Do not implement final visuals/polish yet.

### Acceptance Criteria

```text
- Player can open apiary menu.
- Output slots are visible and correctly restricted.
- Frame slots accept only valid frames.
- Progress/state data can sync to client.
- Menu does not allow invalid item insertion/extraction.
```

### Prompt for Claude Code

```text
Read docs/post-mvp/14-genetic-apiary-gui-and-frames.md.

Focus only on Task 3 from docs/implementation/14-genetic-apiary-gui-and-frames-implementation.md.

Implement or update the Genetic Apiary menu/container with output slots, frame slots, player inventory and progress sync.

Do not implement final screen visuals, resource bees, lifecycle mechanics, environment stats or automation upgrade.

Before coding, describe the slot layout and restrictions.
```

## Task 4 — Implement Genetic Apiary Screen

### Objective

Create a basic player-facing GUI for the Genetic Apiary.

### Scope

Display title, output slots, frame slots, production progress, working state, associated bee summaries, and active frame effects.

### Non-goals

Do not implement polished art as a blocker.

### Acceptance Criteria

```text
- Screen opens from the apiary block.
- Screen shows production progress.
- Screen shows frame slots and basic effect descriptions.
- Screen shows bee summaries with analyzed-state redaction.
- Screen does not show temperature/humidity/environment stats.
```

### Prompt for Claude Code

```text
Read docs/post-mvp/14-genetic-apiary-gui-and-frames.md.

Focus only on Task 4 from docs/implementation/14-genetic-apiary-gui-and-frames-implementation.md.

Implement a basic Genetic Apiary screen using menu/container data.

Show output, frames, progress, working state, associated bee summaries and active frame effects.

Respect analyzed-state visibility rules.

Do not implement environment stats, resource bees, lifecycle mechanics, custom polished art or automation upgrade.

Before coding, describe the screen layout in text.
```

## Task 5 — Define Frame Modifier Model

### Objective

Create a clear model for frame effects.

### Scope

Implement/refactor frame modifiers so effects can be resolved consistently.

Initial fields may include:

```text
productionMultiplier
mutationMultiplier
stabilityModifier
durabilityCost
```

### Non-goals

Do not implement every future frame type.

### Acceptance Criteria

```text
- Frame effects are modeled centrally.
- Basic/Productivity/Mutation/Stability frames have explicit effects.
- Effects can be summarized for GUI tooltips.
- Tests cover frame effect resolution if common/testable.
```

### Prompt for Claude Code

```text
Read docs/post-mvp/14-genetic-apiary-gui-and-frames.md.

Focus only on Task 5 from docs/implementation/14-genetic-apiary-gui-and-frames-implementation.md.

Define or refactor the frame modifier model so Basic, Productivity, Mutation and Stability frames have explicit centralized effects.

Do not implement future frame types, resource bees, environment simulation, lifecycle mechanics or automation unlocks.

Add tests where the modifier resolver is pure Java/testable. Before coding, summarize initial frame effect fields.
```

## Task 6 — Apply Frame Effects to Supported Systems

### Objective

Make frame modifiers affect actual apiary behavior in a narrow, testable way.

### Scope

Apply effects only to systems that already exist or are explicitly scoped:

```text
Productivity Frame -> production multiplier
Mutation Frame -> only if current apiary mutation behavior exists; otherwise planned/future
Stability Frame -> only if current stability behavior exists; otherwise planned/future
Basic Frame -> small baseline modifier or durability/cost behavior
```

### Non-goals

Do not add apiary breeding unless explicitly approved.

### Acceptance Criteria

```text
- Frame effects alter only supported existing systems.
- Unsupported effects are not falsely presented as active.
- GUI distinguishes active vs planned/no-op effects if needed.
- Existing production tests are updated where applicable.
```

### Prompt for Claude Code

```text
Read docs/post-mvp/14-genetic-apiary-gui-and-frames.md.

Focus only on Task 6 from docs/implementation/14-genetic-apiary-gui-and-frames-implementation.md.

Apply frame effects to existing apiary behavior only where the system already supports it.

Do not add apiary breeding, lifecycle mechanics, environment simulation, resource bees or automation upgrade.

If a frame effect cannot be applied yet, document it as planned/future rather than pretending it works.

Before coding, list each current frame and whether its effect will be active or planned.
```

## Task 7 — Verify Automation-Ready Output Behavior

### Objective

Ensure the apiary is ready for automation without requiring an Automation Upgrade.

### Scope

Review/update item handler/capability behavior:

```text
- output extraction works through standard automation;
- frame slots are protected from unintended extraction/insertion if needed;
- input/output side behavior is clear;
- no Automation Upgrade is required.
```

### Non-goals

Do not implement complex filtering or logistics upgrades.

### Acceptance Criteria

```text
- Output can be extracted by supported automation mechanisms.
- Automation does not require a special unlock upgrade.
- Frame slots are not accidentally drained as outputs.
- Behavior is documented.
```

### Prompt for Claude Code

```text
Read docs/post-mvp/14-genetic-apiary-gui-and-frames.md.

Focus only on Task 7 from docs/implementation/14-genetic-apiary-gui-and-frames-implementation.md.

Verify and update Genetic Apiary automation-ready output behavior.

Automation must be baseline behavior, not gated behind an Automation Upgrade.

Do not implement filters, resource bees, lifecycle mechanics, environment simulation or new upgrade systems.

Before coding, describe current item handler/capability behavior and intended final behavior.
```

## Task 8 — Document Containment/Simulation as Future Slice

### Objective

Capture the idea of future containment/simulation modifier without implementing it prematurely.

### Scope

Document that bees remain living; containment/simulation may prevent bees from leaving operationally; it is not bee itemization; it is not an Automation Upgrade; it should be implemented after base GUI/frame behavior is stable.

### Prompt for Claude Code

```text
Read docs/post-mvp/14-genetic-apiary-gui-and-frames.md.

Focus only on Task 8 from docs/implementation/14-genetic-apiary-gui-and-frames-implementation.md.

Document containment/simulation as a future or separate implementation slice.

Do not implement code.
```

## Task 9 — Create Genetic Apiary Asset Prompts

### Objective

Create complete prompts for the Genetic Apiary block and GUI assets.

### Scope

Create prompt files under `docs/art/prompts/blocks/` and `docs/art/prompts/gui/` for:

```text
- Genetic Apiary block texture(s);
- Genetic Apiary GUI background;
- production progress indicator;
- frame slot visuals if custom;
- bee occupancy or status icons if used.
```

### Acceptance Criteria

```text
- Prompt files exist for every required apiary visual asset.
- Prompts include target paths and dimensions.
- Block prompts reference model/UV requirements.
- GUI prompts include layout constraints and slot/progress positions.
- No placeholder is accepted as final.
```

### Prompt for Claude Code

```text
Read CLAUDE.md and docs/art/asset-prompt-workflow.md.

Focus only on Task 9 from docs/implementation/14-genetic-apiary-gui-and-frames-implementation.md.

Create complete asset prompt files for the Genetic Apiary block texture, GUI background, progress indicator, and any other required apiary visuals.

Do not create placeholder PNGs. Do not generate images.
```

## Task 10 — Create Frame Item Asset Prompts

### Objective

Create prompts for frame item icons.

### Scope

Create prompt files under `docs/art/prompts/items/` for:

```text
- Basic Frame;
- Productivity Frame;
- Mutation Frame;
- Stability Frame.
```

### Acceptance Criteria

```text
- Each frame has a prompt under docs/art/prompts/items/.
- Each prompt defines a clear visual identity.
- Icons are described as Minecraft-style item textures (16x16).
- No generated placeholder icon counts as final.
```

### Prompt for Claude Code

```text
Read CLAUDE.md and docs/art/asset-prompt-workflow.md.

Focus only on Task 10 from docs/implementation/14-genetic-apiary-gui-and-frames-implementation.md.

Create complete asset prompt files for the four frame item icons: Basic, Productivity, Mutation, Stability.

Do not create placeholder PNGs. Do not generate images.

Place prompts under docs/art/prompts/items/.
```

## 11. Phase Completion Checklist

```text
- Genetic Apiary has a real menu/container.
- Genetic Apiary has a basic screen.
- Output inventory and progress are visible.
- Frame slots and active frame effects are visible.
- Bee summaries respect analyzed-state visibility.
- Frame modifiers are centralized and at least partly functional.
- Apiary output automation is baseline and verified.
- No Automation Upgrade was introduced.
- No lifecycle/environment/resource bee system was introduced.
```

## 12. Go / No-Go Checkpoint Before Phase 15

```text
- apiary UI has stable information needs;
- frame effect metadata is clear enough to document;
- automation baseline behavior is understood;
- analyzed-state display rules are reused correctly.
```

## 13. AI Failure Modes to Watch

```text
- adding Automation Upgrade as a gate;
- itemizing bees as the default apiary flow;
- adding environment simulation;
- revealing unanalyzed genetics;
- adding apiary breeding by accident.
```

## 14. Recommended Commit Sequence

```text
apiary: define gui data model
neoforge: add genetic apiary menu
client: add genetic apiary screen
gameplay: centralize frame modifiers
apiary: apply supported frame effects
apiary: verify automation-ready output behavior
docs: document containment simulation follow-up
```

## 15. Handoff Prompt for Starting Phase 14

```text
Read CLAUDE.md and docs/README.md.

Then read:
- docs/post-mvp/11-post-mvp-productization-roadmap.md
- docs/post-mvp/13-analyzer-ux-and-progression.md
- docs/post-mvp/14-genetic-apiary-gui-and-frames.md
- docs/implementation/14-genetic-apiary-gui-and-frames-implementation.md

Do not implement all of Phase 14 at once.

Start only with Task 1 — Review Existing Apiary Implementation.

Before coding, summarize the goal, scope, out-of-scope items, and files/packages you expect to inspect.

Then perform Task 1 without changing files.
```
