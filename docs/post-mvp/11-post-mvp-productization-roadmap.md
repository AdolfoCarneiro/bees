# 11 — Post-MVP Productization Roadmap

## 1. Status

```txt
Current roadmap document.
```

This document defines the first major direction after the initial Curious Bees MVP.

> **Foundation document:** before reading this roadmap in detail, read
> `docs/post-mvp/10-5-species-hives-and-habitat-system.md`. It defines how species exist
> in the world (habitat + hive blocks) and reserves the design space for the future
> custom bee entity migration. Several phases below depend on it.

The original MVP documentation is preserved under:

```txt
docs/mvp/
```

Those documents remain valid as architectural and historical context, especially for the genetics core, breeding rules, content foundations, and platform boundaries.

This document defines the new product direction after the MVP loop has been validated.

## 2. Purpose

The initial MVP proved the core idea:

```txt
Vanilla bees can carry genomes, breed naturally, inherit traits, mutate, be analyzed, and produce species-based outputs.
```

The next phase is not about adding a large amount of new content yet.

The next phase is about turning the working MVP into a real, polished, playable mod foundation.

This means improving:

```txt
- player-facing UX;
- species visuals;
- analyzer experience;
- genetic apiary experience;
- frame behavior;
- automation readiness;
- content and asset pipeline;
- documentation for future expansion.
```

## 3. Product Direction

Curious Bees should now move toward this identity:

```txt
A modern Minecraft bee progression mod that combines the accessibility, automation quality, and polished UX expected from modern bee mods with a Forestry-inspired genetic system based on living vanilla bees, diploid inheritance, hidden alleles, probabilistic mutations, and species identity.
```

In simpler terms:

```txt
Productive Bees-like accessibility and polish.
Forestry-like genetic depth.
Curious Bees identity and rules.
```

This does not mean copying Productive Bees, Forestry, Complicated Bees, or any other existing mod.

Existing mods may be studied for design references, UX patterns, datapack structure, and player expectations, but Curious Bees must keep its own identity, content, assets, progression, and implementation.

## 4. What Has Changed Since The MVP

During the MVP, the project intentionally avoided many systems:

```txt
- complex GUI;
- polished assets;
- advanced apiaries;
- complex automation;
- large species trees;
- resource bees;
- advanced lifecycle mechanics;
- temperature/humidity systems;
- complex visual variants.
```

That was correct for the MVP.

Now that the core loop is validated, some of those restrictions change.

The project should now actively pursue:

```txt
- a real analyzer UX;
- a real genetic apiary GUI;
- species-specific visuals;
- useful frames;
- automation-ready apiary behavior;
- a better content and asset pipeline;
- a clearer post-MVP roadmap.
```

However, some restrictions remain in place.

## 5. Core Principles For The Post-MVP Phase

### 5.1 Keep Living Bees Central

Curious Bees should continue using vanilla bees as living organisms.

A bee is not primarily:

```txt
- a recipe;
- a machine item;
- a static resource generator;
- a block-only production unit.
```

A bee is:

```txt
A living Minecraft bee carrying a genetic identity.
```

The genetic apiary may control, display, guide, or optimize bees, but it should not erase the fantasy of living bees.

### 5.2 Do Not Turn Bees Into Items By Default

The apiary should use live bees.

The interface may show bees that are inside or associated with the apiary, but the mod should not make the core loop depend on turning bees into inventory items.

Future systems may introduce transport or containment tools, but these should be treated carefully and must not replace the living-bee fantasy.

Potential future tools:

```txt
- bee cage;
- bee capsule;
- genetic sample;
- advanced containment device.
```

These are future topics and should not be implemented casually during this phase.

### 5.3 Genetics Remains The Core

The genetics system remains the heart of the mod.

The project must preserve:

```txt
- diploid genomes;
- active and inactive alleles;
- dominance;
- Mendelian inheritance;
- probabilistic mutation;
- purebred and hybrid value;
- hidden genetic potential.
```

New UX, visuals, apiary behavior, and automation should expose or enhance genetics, not replace it with deterministic recipes.

### 5.4 Productization Before Content Explosion

Do not add a large species tree yet.

The next phase should make the current foundation feel good first.

The project should avoid adding many new species before solving:

```txt
- how species are represented visually;
- how the analyzer displays genetics;
- how the apiary presents bees, frames, and outputs;
- how species definitions reference assets;
- how future content packs should be authored;
- how assets are named, stored, generated, and validated.
```

### 5.5 Automation-Ready By Design

The genetic apiary should be ready for automation by default.

Automation should not require a dedicated "automation upgrade" just to become possible.

The apiary should be designed so item input/output and integration with hoppers, pipes, or other item transport systems can work naturally where technically feasible.

Upgrades may still exist, but they should modify behavior rather than unlock basic automation.

Examples of possible future upgrades:

```txt
- containment/simulation upgrade;
- capacity upgrade;
- observation upgrade;
- filtering upgrade.
```

### 5.6 Reveal Genetics Through Analysis

The player should not automatically see all genetic data everywhere.

A bee may carry a full genome internally, but detailed genetic data should only be shown after the bee has been analyzed.

This applies to:

```txt
- tooltips;
- analyzer screens;
- apiary GUI;
- future registry/history screens;
- future guidebook/research systems.
```

Before analysis, the UI may show limited or unknown information.

Example:

```txt
Species: Unknown
Traits: Unknown
Purity: Unknown
```

After analysis, the UI may show:

```txt
Species: Cultivated / Forest
Purity: Hybrid
Productivity: Fast / Normal
Fertility: Two / Two
Flower Type: Flowers / Leaves
```

### 5.7 Visual Identity Matters

A species should not feel like a hidden NBT value on a vanilla bee forever.

Every official species should eventually have its own visual identity.

For the post-MVP phase, the recommended approach is hybrid:

```txt
Default approach:
- use the base bee model;
- provide a species-specific texture/skin.

Future special cases:
- allow special species to use custom models when there is a strong reason.
```

This keeps the first visual pass achievable while leaving room for special Blockbench models later.

## 6. Explicit Non-Goals For This Phase

The following should not be implemented during this first post-MVP productization phase unless a new decision explicitly changes the roadmap.

### 6.1 No Full Bee Lifecycle Yet

Do not add complex lifecycle mechanics yet.

Out of scope for now:

```txt
- bees dying from production cycles;
- age-based death;
- queens;
- larvae;
- hatcheries;
- apiary death/rebirth loops;
- Forestry-style generational lifecycle.
```

This is an interesting future direction, but it does not fit the current vanilla-bee foundation without deeper design work.

The current rule is:

```txt
Bees do not die as part of normal Curious Bees production.
```

### 6.2 No Advanced Environment Simulation Yet

Do not add temperature, humidity, climate, weather tolerance, or similar environmental simulation yet.

Out of scope for now:

```txt
- temperature requirements;
- humidity requirements;
- climate categories;
- detailed biome climate simulation;
- rain/day/night genetic restrictions;
- nearby block requirements for complex mutation rules.
```

These may return later as advanced progression mechanics.

For now, keep the experience focused on:

```txt
- bees;
- genetics;
- analyzer;
- apiary;
- frames;
- visuals;
- production.
```

### 6.3 No Resource Bees Yet

Resource bees remain future content.

Do not add:

```txt
- Iron Bee;
- Gold Bee;
- Diamond Bee;
- Redstone Bee;
- Emerald Bee;
- Netherite Bee;
- other resource-output species.
```

Resource bee progression should be designed during a later content expansion phase, after the visual system, analyzer UX, apiary UX, frame system, and content/asset pipeline are mature.

### 6.4 No Large Species Tree Yet

Do not add many new species during this phase.

Small additions may be allowed only if they directly support productization work, test visual systems, or validate content tooling.

The first real expanded species branch belongs after the post-MVP foundation is stronger.

### 6.5 No Fabric Parity Work Yet

Fabric support remains future work.

Post-MVP productization should keep common logic clean and platform boundaries healthy, but should not shift focus to Fabric implementation yet unless explicitly planned.

### 6.6 No Custom Model For Every Bee Yet

Do not require every species to have a unique Blockbench model.

For now:

```txt
- texture per species is the default;
- custom model per species is optional and future-oriented.
```

## 7. Post-MVP Phase Breakdown

## Phase 11 — Post-MVP Product Direction

Purpose:

```txt
Define the new product direction after the MVP and prevent old MVP constraints from being mistaken for the current roadmap.
```

Deliverables:

```txt
- reorganized documentation structure;
- docs/mvp/ for original MVP foundation docs;
- docs/post-mvp/ for current direction;
- updated root README;
- updated CLAUDE.md;
- this roadmap document;
- explicit post-MVP non-goals;
- confirmed product identity.
```

Done when:

```txt
A human or AI coding agent can clearly understand that the MVP is complete and that the next focus is productization, not immediate content explosion.
```

## Phase 11.5 — Species Hives And Habitat System

Purpose:

```txt
Give world-spawnable species a natural home (species hive blocks) and define the
extensible spec format for adding new species (data + visual + optional habitat).
```

This phase is fully designed in:

```txt
docs/post-mvp/10-5-species-hives-and-habitat-system.md
```

Deliverables:

```txt
- SpeciesHabitatDefinition + BeeNestCompatibilityService in common (pure Java).
- Optional habitat field on BeeSpeciesDefinition.
- Three species hive blocks: Meadow, Forest, Arid (extends vanilla BeehiveBlock).
- World gen entries for species hives in their respective biomes.
- Hive entry/exit restricted to matching species.
- Bees emerging from species hives carry the matching genome.
- New asset prompts under docs/art/prompts/bee_nests/.
- Bee nest UV/layout reference at docs/art/templates/bee_nest/README.md.
- Documented species spec format used by all future species.
- Reserved design space for Phase 11.6 (Custom Bee Entity Architecture).
```

Done when:

```txt
A player can find species-specific hives in the world, bees emerging from them
carry the matching species, and a new species can be added by following the
documented spec format.
```

## Phase 11.6 — Custom Bee Entity Architecture (deferred)

Purpose:

```txt
Decide and document the migration from vanilla-bees-with-attached-genome toward
custom bee entities per species (with spawn eggs), while keeping vanilla bees
functional and breedable as a "wild" default species.
```

Status:

```txt
Design only. No implementation in this phase. The detailed ADR will live at
docs/post-mvp/11-6-custom-bee-entity-architecture.md when authored.
```

Constraints documented in:

```txt
docs/post-mvp/10-5-species-hives-and-habitat-system.md (section 7.2)
```

Done when:

```txt
A future implementer can read the ADR and execute the migration without
revisiting open architectural questions.
```

## Phase 12 — Visual Species System

Purpose:

```txt
Make species visible and recognizable in-game.
```

Depends on:

```txt
Phase 11.5 — visual definitions for habitat-bearing species must align with hive textures.
```

Deliverables:

```txt
- visual metadata in species definitions;
- species-to-texture resolution;
- fallback texture strategy;
- texture naming conventions;
- asset folder conventions;
- renderer/client-side integration for species textures;
- first proper textures for MVP species;
- documentation for how species visuals work.
```

Recommended initial visual model:

```txt
- all MVP species use the base bee model;
- each species has its own texture;
- future special species may use custom models.
```

MVP species needing visual identity:

```txt
- Meadow;
- Forest;
- Arid;
- Cultivated;
- Hardy.
```

Done when:

```txt
A player can visually distinguish the initial species in-game without relying only on analyzer text or debug information.
```

## Phase 13 — Analyzer UX And Progression

Purpose:

```txt
Turn analysis from a debug/chat interaction into real gameplay UX.
```

Deliverables:

```txt
- portable analyzer UX;
- analyzer screen or structured UI;
- analyzer cost or charge system using bee-related resources;
- analyzed state for bees;
- tooltips that respect analyzed/unanalyzed state;
- apiary display that respects analyzed/unanalyzed state;
- clear display of active/inactive alleles;
- clear display of purebred/hybrid status;
- clear display of key traits.
```

Important rule:

```txt
Detailed genetic data should only be shown after analysis.
```

Potential analyzer tiers:

```txt
Portable Analyzer:
- quick field analysis;
- used directly on living bees;
- shows enough information for selection.

Analyzer Block:
- more complete analysis station;
- may support deeper reports later;
- may be tied to discovery/research in future.
```

Done when:

```txt
The player can inspect and compare bees through proper UI instead of relying on chat output or debug commands.
```

## Phase 14 — Genetic Apiary GUI

Purpose:

```txt
Turn the genetic apiary into a real central block for production and controlled beekeeping.
```

Deliverables:

```txt
- apiary GUI;
- visible output inventory;
- visible frame slots;
- visible production progress;
- visible bees associated with the apiary;
- visible modifiers from frames;
- automation-ready inventory behavior;
- no basic automation upgrade required;
- display respects analyzed/unanalyzed bee state.
```

The apiary should continue using live bees.

The UI may show bees associated with the apiary, but the system should not turn the core bee loop into an item-only machine.

Out of scope for this phase:

```txt
- temperature/humidity display;
- lifecycle/death display;
- larvae/hatchery flow;
- advanced resource bee production trees.
```

Done when:

```txt
The genetic apiary feels like a real usable mod block rather than a debug or placeholder system.
```

## Phase 15 — Frames And Apiary Behavior

Purpose:

```txt
Make frames meaningful and define the difference between biological modifiers and apiary behavior modifiers.
```

Frames should affect bee/genetic/production behavior.

Possible frame categories:

```txt
- Basic Frame;
- Productivity Frame;
- Mutation Frame;
- Stability Frame;
- Fertility Frame.
```

Potential frame effects:

```txt
Productivity Frame:
- increases production rate or output chance.

Mutation Frame:
- increases mutation chance.

Stability Frame:
- reduces unwanted variation or helps preserve desired traits.

Fertility Frame:
- future-facing; may affect breeding, samples, or offspring-related systems later.
```

Apiary behavior modifiers are separate from frames.

Possible future apiary behavior modifiers:

```txt
Containment/Simulation:
- bees remain operationally inside the apiary;
- reduces bees flying away;
- supports cleaner automation;
- still treats bees as living bees, not normal items.

Capacity:
- supports more bees or more internal tracking.

Observation:
- improves visible information in the GUI.

Filtering:
- controls which bees can enter or be used.
```

Important:

```txt
Automation readiness is not an upgrade.
The apiary should support automation by default where technically feasible.
```

Done when:

```txt
Frames and apiary behavior modifiers have clear roles and do not overlap confusingly.
```

## Phase 16 — Content And Asset Pipeline

Purpose:

```txt
Define how future species can be added with both data and visual assets.
```

This phase should solve the concern that JSON definitions alone are not enough for real species expansion.

A complete species should eventually define or reference:

```txt
- species ID;
- display name;
- dominance;
- default traits;
- mutation role;
- production profile;
- visual profile;
- texture paths;
- optional model path;
- localization keys;
- optional spawn/progression metadata.
```

Recommended concept:

```txt
Species data comes from data definitions.
Species visuals come from assets/resource packs.
A complete species content pack may need both.
```

Potential structure:

```txt
data/curiousbees/species/meadow.json
assets/curiousbees/textures/entity/bee/meadow.png
assets/curiousbees/lang/en_us.json
assets/curiousbees/lang/pt_br.json
```

Future support may include:

```txt
- texture templates;
- Blockbench model templates;
- generated placeholder assets;
- validation warnings for missing visual assets;
- guide for adding species;
- guide for official content expansion;
- guide for addon/resource-pack authors.
```

Done when:

```txt
The project has a clear, documented way to add a new species with both gameplay data and visual identity.
```

## Phase 17 — First Expanded Species Branch

Purpose:

```txt
Add the first real post-MVP species branch only after the product foundation is stronger.
```

This phase should not start until the project has made progress on:

```txt
- visual species system;
- analyzer UX;
- apiary GUI;
- frame behavior;
- content/asset pipeline.
```

A possible future branch is the managed/cultivated progression branch.

Previously discussed examples:

```txt
Cultivated + Hardy -> Resilient
Cultivated + Meadow -> Diligent
Resilient + Diligent -> Noble
Diligent + Noble -> Industrious
```

These are not automatically approved for implementation by this document.

Before implementation, the branch should receive its own design document defining:

```txt
- species roles;
- visuals;
- mutation chances;
- production identity;
- progression purpose;
- balance expectations;
- required assets.
```

Done when:

```txt
Curious Bees gains its first meaningful content expansion without weakening the genetic identity or overwhelming the project with content too early.
```

## 8. Future Major Part — Content Expansion And Resource Bees

Resource bees belong to a later major part of the mod.

They should not be designed in detail during the first post-MVP productization phase.

When the project reaches the content expansion stage, resource bee progression should receive dedicated planning.

That future planning should answer:

```txt
- Should resource bees use direct names or thematic names?
- Should they produce combs, dusts, nuggets, shards, fluids, or something else?
- How do they avoid becoming simple ore generators?
- What genetic gates should exist before resource production?
- What intermediate species are required?
- How are resource bees balanced against mining and other mods?
- How do resource bees interact with automation?
- How do resource bees interact with hybrid production?
- Which resource bees belong in the base mod, if any?
- Which should be compatibility content?
```

Until then, resource bees remain out of scope.

## 9. Documentation Updates Needed After This Document

Already authored:

```txt
docs/post-mvp/10-5-species-hives-and-habitat-system.md   (foundational)
docs/post-mvp/12-visual-species-system.md
docs/post-mvp/13-analyzer-ux-and-progression.md
docs/post-mvp/14-genetic-apiary-gui-and-frames.md
docs/post-mvp/15-content-and-asset-pipeline.md
```

Still to be authored:

```txt
docs/post-mvp/11-6-custom-bee-entity-architecture.md   (deferred ADR; see Phase 11.6)
docs/research/existing-bee-mods-review.md
```

The most important next implementation work derives from:

```txt
docs/post-mvp/10-5-species-hives-and-habitat-system.md
```

Reason:

```txt
Phase 12 (Visual Species System) and Phase 16 (Content And Asset Pipeline) both depend
on the habitat data model and species spec format defined in 10.5. Implementing 10.5
unblocks the rest of the productization roadmap.
```

## 10. Recommended Implementation Order

Recommended order after this roadmap:

```txt
1.  Add SpeciesHabitatDefinition + BeeNestCompatibilityService in common.
2.  Add optional habitat to BeeSpeciesDefinition and built-in species.
3.  Implement three species hive blocks (Meadow, Forest, Arid) in NeoForge.
4.  Implement world gen entries for species hives.
5.  Restrict hive entry/exit to matching species; stamp genomes on emergence.
6.  Create UV template + asset prompts for hive textures; integrate final hive textures.
7.  Define visual species system (Phase 12).
8.  Add visual metadata to species definitions.
9.  Implement species texture resolution.
10. Add first species-specific textures for MVP bees.
11. Define analyzer UX and analyzed-state rules.
12. Implement portable analyzer UI improvements.
13. Implement genetic data visibility only after analysis.
14. Define apiary GUI layout and behavior.
15. Implement apiary GUI.
16. Refine frame behavior.
17. Separate frames from apiary behavior modifiers.
18. Document content and asset pipeline (formalize species spec format from 10.5).
19. Author Phase 11.6 ADR for custom bee entity migration (design only).
20. Only then plan first expanded species branch.
```

## 11. AI Agent Guidance

When asking an AI coding agent to work during this phase, prompts should clearly state:

```txt
Scope:
Out of scope:
Files likely involved:
Tests required:
Manual validation required:
Do not:
```

Agents must not casually add:

```txt
- resource bees;
- large species trees;
- lifecycle/death/larvae mechanics;
- temperature/humidity simulation;
- item-only bee systems;
- Fabric implementation;
- custom models for every species;
- unrelated mod compatibility;
- hidden changes to the genetics core.
```

Agents should preserve:

```txt
- common/genetics as pure Java;
- platform-specific code isolated in platform modules;
- genetics rules from the MVP;
- living-bee identity;
- data-driven future compatibility;
- small, reviewable implementation slices.
```

## 12. Success Criteria For The First Post-MVP Productization Phase

This phase is successful when:

```txt
- the initial species are visually distinguishable;
- analyzer UX is no longer chat/debug-only;
- genetic data visibility depends on analysis state;
- the genetic apiary has a usable GUI;
- frames have clear, useful behavior;
- the apiary is automation-ready by design;
- species definitions can reference visual assets;
- there is a documented path for adding species with data and assets;
- no large content expansion has been rushed before the foundation is ready.
```

## 13. Summary

The MVP proved that Curious Bees can work.

The post-MVP phase should make it feel like a real mod.

The project should now focus on:

```txt
- visuals;
- UX;
- apiary polish;
- analyzer polish;
- frames;
- automation readiness;
- content/asset pipeline.
```

The project should still avoid:

```txt
- resource bees;
- large content trees;
- complex lifecycle systems;
- advanced climate simulation;
- item-only bee mechanics;
- premature Fabric parity.
```

The next most important question is:

```txt
How does a species become visually represented in-game?
```

That should be answered in:

```txt
docs/post-mvp/12-visual-species-system.md
```
