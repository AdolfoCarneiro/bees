# 14 — Genetic Apiary GUI and Frames

> Status: Post-MVP planning document.  
> This document defines the post-MVP direction for the Genetic Apiary, its GUI, frame system, and automation behavior.

## 1. Purpose

The MVP/early post-MVP apiary proved that controlled bee production can exist.

The next phase should turn the Genetic Apiary into a real player-facing block with clear UI, meaningful frames, and automation-ready behavior.

The apiary should support the Curious Bees identity:

```txt
Living vanilla bees + Forestry-like genetics + modern automation UX.
```

## 2. Core Design Decisions

### 2.1 Apiary Uses Living Bees

The apiary should work with living bees.

Rule:

```txt
The apiary does not transform bees into normal items as its main workflow.
```

Product implementation note (NeoForge): the **Genetic Beehive** (`genetic_apiary`) block extends vanilla `BeehiveBlock`, uses the same crafted-hive recipe pattern and honey mechanics as `minecraft:beehive`, reuses vanilla hive textures for parity, and keeps **no species restriction** on bee entry (contrast with wild species bee nests). Right-click with an empty hand opens the genetics GUI (frames + outputs + honey readouts).

The GUI may display bees associated with the apiary, but those bees are still living entities in the world or simulated as living bees through a future containment system.

### 2.2 Apiary Is Automation-Ready By Default

Automation should not require an upgrade just to become possible.

Rule:

```txt
The apiary should be designed from the start to support item input/output automation where appropriate.
```

Examples:

```txt
- output inventory can be extracted
- frame slots can be inserted/extracted with sensible restrictions
- honey/combs/fuel-like resources can be inserted if used
```

Do not add an "Automation Upgrade" whose only purpose is to unlock basic automation.

### 2.3 No Lifecycle System For Now

Do not add bee aging, death, larvae, queens, or lifecycle simulation in this phase.

Future lifecycle ideas may be revisited later, but they are not part of productization.

### 2.4 No Advanced Environment Simulation For Now

Do not add temperature, humidity, climate matching, or complex biome requirements to the apiary GUI in this phase.

The apiary UI should focus on:

```txt
- bees
- production
- frames
- outputs
- analysis state
- simple operation status
```

## 3. Apiary Player Fantasy

The apiary is the player's first serious beekeeping workstation.

It should feel like:

```txt
- a controlled place where bees work
- a visible production hub
- a genetics-aware station
- a block that improves but does not erase vanilla bee behavior
```

The player should understand:

```txt
- which bees are involved
- what they are producing
- what frames are modifying
- whether genetics are known or still unanalyzed
- whether outputs can be collected
```

## 4. Apiary Modes

### 4.1 Natural/Open Apiary

Initial mode.

Behavior:

```txt
- Bees can enter/leave according to vanilla-like behavior.
- Apiary detects associated bees.
- Production occurs when bees meet simple requirements.
- GUI shows bees currently associated with the apiary.
```

This preserves the living-bee fantasy.

### 4.2 Controlled/Contained Apiary

Future modifier/upgrade.

Behavior concept:

```txt
- Bees remain operationally inside or bound to the apiary.
- They do not constantly fly away.
- The apiary simulates their work while keeping them conceptually alive.
- This improves automation and stability.
```

This should not be treated as itemization. It is a controlled living-bee mode.

Possible names:

```txt
- Containment Upgrade
- Controlled Chamber
- Bee Chamber
- Simulated Foraging Upgrade
```

Do not implement this until the basic GUI and open apiary are solid.

## 5. GUI Goals

The apiary GUI should answer these questions:

```txt
- Is the apiary working?
- Which bees are currently associated with it?
- Are those bees analyzed?
- What are they producing?
- What frames are installed?
- What modifiers are active?
- What outputs are available?
```

## 6. Suggested GUI Layout

Conceptual layout:

```txt
+------------------------------------------------+
| Genetic Apiary                                 |
+---------------------+--------------------------+
| Bees                | Production               |
| - Bee Slot/Card 1   | Progress: ####----       |
| - Bee Slot/Card 2   | Current Output: Comb     |
| - Bee Slot/Card 3   | Modifier Summary         |
+---------------------+--------------------------+
| Frames                                         |
| [Frame 1] [Frame 2] [Frame 3]                  |
+------------------------------------------------+
| Output Inventory                               |
| [ ][ ][ ][ ][ ][ ][ ][ ][ ]                    |
+------------------------------------------------+
```

This is conceptual, not a required pixel layout.

## 7. Bee Display Rules

The apiary may show bee cards or rows.

### Before Analysis

```txt
Bee: Unanalyzed
Visible Species: Meadow
Genetics: Unknown
```

### After Analysis

```txt
Bee: Cultivated / Forest
Purity: Hybrid
Productivity: Fast / Normal
```

The GUI must respect analysis state.

Do not reveal full genetic data for unanalyzed bees.

## 8. Production Display

Production display should be simple at first.

Show:

```txt
- current progress/cycle
- expected primary output if known
- output inventory
- productivity modifier summary
```

If the bee is unanalyzed, avoid showing deep genetic details, but the block can still show practical output status.

Example:

```txt
Producing: Unknown Comb
Progress: 45%
```

After analysis:

```txt
Producing: Cultivated Comb
Productivity: Fast
Progress: 45%
```

## 9. Frames

Frames are items inserted into the apiary to modify biological/genetic operation.

Frames are not the same as apiary upgrades.

Frames should affect things like:

```txt
- production
- mutation
- stability
- fertility-related future behavior
```

## 10. Initial Frame Types

### 10.1 Basic Frame

Role:

```txt
General early frame.
Small production improvement or durability-based baseline.
```

### 10.2 Productivity Frame

Role:

```txt
Increases production speed/chance/output rate.
```

Possible tradeoff:

```txt
May reduce durability faster or affect stability later.
```

### 10.3 Mutation Frame

Role:

```txt
Increases mutation chance when breeding/mutation-related apiary systems exist.
```

If the current apiary does not perform breeding, this frame may initially affect only mutation-related future systems or be deferred.

Important:

```txt
Do not reintroduce apiary breeding unless explicitly designed.
Vanilla/natural breeding remains the primary breeding path for now.
```

### 10.4 Stability Frame

Role:

```txt
Supports genetic stabilization and purebred selection in future systems.
```

Possible future effects:

```txt
- reduce unwanted variation
- improve consistency
- reduce mutation chance when desired
```

### 10.5 Fertility Frame

Future role:

```txt
Could affect larvae/samples/offspring-related systems later.
```

Do not implement lifecycle or larvae just to justify this frame.

## 11. Frame Modifier Model

A frame should define modifiers in data, not scattered hardcoded checks.

Conceptual model:

```txt
FrameDefinition
- id
- displayName
- durability
- productionMultiplier
- mutationChanceMultiplier
- stabilityModifier
- fertilityModifier
```

Not every frame needs every modifier.

Example conceptual JSON:

```json
{
  "id": "curiousbees:productivity_frame",
  "durability": 128,
  "modifiers": {
    "productionMultiplier": 1.25,
    "mutationChanceMultiplier": 1.0,
    "stabilityModifier": 0.0
  }
}
```

## 12. Apiary Upgrades / Modifiers

Apiary upgrades affect block behavior, not bee genetics directly.

Potential future upgrades:

```txt
- Containment / Simulation
- Capacity
- Observation
- Filter
```

Do not include:

```txt
- Automation Upgrade
```

Automation readiness is part of the apiary's baseline design.

## 13. Automation Rules

The apiary should expose automation-friendly inventories/capabilities.

Recommended rules:

```txt
- Outputs are extract-only.
- Frame slots accept valid frame items.
- Frame extraction may be allowed or controlled depending on durability behavior.
- Input slots should be clearly separated from output slots.
- Living bees should not be extracted as normal items.
```

If future bee cages/capsules exist, they should be designed separately.

## 14. Bee Cage / Capsule / Genetic Sample

These are future concepts, not immediate apiary requirements.

Possible future items:

```txt
Bee Cage / Capsule:
- temporarily contains a living bee
- helps transport or insert bees into systems
- preserves genome

Genetic Sample:
- not a living bee
- used for research, analysis, or crafting
```

Do not make itemized bees the default apiary workflow.

## 15. Technical Boundaries

Recommended separation:

```txt
common/gameplay/production:
- production resolution
- productivity modifiers

common/gameplay/apiary:
- apiary operation model if Minecraft-independent enough
- frame modifier aggregation

neoforge/block:
- block and block entity
- inventory
- capabilities

neoforge/client/screen:
- GUI rendering
```

Do not put frame or apiary operation logic directly inside renderer classes.

Do not put Minecraft imports into common/genetics.

## Apiary, Frames, and Block Asset Policy

The Genetic Apiary block, frame items, and GUI visuals must use generated final assets instead of placeholders.

Required prompt categories:

```text
docs/art/prompts/blocks/
docs/art/prompts/items/
docs/art/prompts/gui/
```

Block and model-related textures should reference a known model or UV layout whenever possible.

For custom block models:

```text
- define or export the Blockbench model first;
- identify the UV texture layout;
- generate texture prompts using that UV reference;
- integrate generated textures after validation.
```

## 16. Out of Scope For This Phase

Do not implement yet:

```txt
- Bee lifecycle/death/larvae systems.
- Queen/princess/drone systems.
- Temperature/humidity simulation.
- Full environmental requirements.
- Resource bees.
- Itemized bee workflow as default.
- Automation unlock upgrade.
- Complex custom block rendering.
```

## 17. Implementation Notes For AI Agents

When implementing apiary GUI and frames:

```txt
Scope:
- Add/upgrade apiary GUI.
- Display associated bees.
- Respect analysis state.
- Display frames and modifiers.
- Display production progress and outputs.
- Keep output automation available by default.

Out of scope:
- Lifecycle mechanics.
- Resource bees.
- Environment simulation.
- Fabric parity.
- Custom bee itemization systems.

Do not:
- Add Automation Upgrade.
- Turn bees into items as the main workflow.
- Reveal unanalyzed genetics.
- Put genetics rules inside GUI code.
```

## 18. Acceptance Criteria

This phase is successful when:

```txt
- Genetic Apiary has a real GUI.
- GUI shows associated living bees.
- GUI respects analyzed/unanalyzed state.
- GUI shows production progress and outputs.
- Frame slots are visible and functional.
- Frame modifiers affect production or relevant systems.
- Apiary output automation works by default.
- No lifecycle or advanced environment system is introduced prematurely.
```
