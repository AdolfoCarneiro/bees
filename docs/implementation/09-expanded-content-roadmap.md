# Phase 9 — Expanded Content Roadmap

This document defines the long-term content expansion plan for Curious Bees after the MVP loop is working.

It is intentionally **not** an implementation document for the first playable version.

The goal of this phase is to organize future content categories, progression direction, naming strategy, balancing principles, and expansion checkpoints without allowing late-game content to contaminate the MVP.

---

## 1. Phase Purpose

Phase 9 exists to answer:

```text
What kinds of bee content can Curious Bees grow into after the core system proves itself?
```

It should define a roadmap for future content, not implement the content immediately.

The MVP must remain focused on:

```text
Meadow
Forest
Arid
Cultivated
Hardy
```

Phase 9 begins only after:

```text
- bees can store genomes;
- wild bees can receive starting genomes;
- vanilla breeding can produce inherited child genomes;
- mutation can create at least Cultivated and Hardy;
- the player can inspect bee genetics;
- basic species-based production exists;
- the content system is stable enough to add more species safely.
```

---

## 2. Phase Goals

Phase 9 should define:

```text
- future species categories;
- expansion principles;
- progression tiers;
- naming rules;
- mutation tree strategy;
- resource bee philosophy;
- biome bee direction;
- Nether bee direction;
- End bee direction;
- industrial/tech bee direction;
- magic bee direction;
- compatibility bee direction;
- balancing rules;
- content acceptance criteria;
- content authoring workflow;
- what must remain out of scope until later.
```

---

## 3. Non-Goals

Do not use this phase to immediately implement:

```text
- dozens of new species;
- all resource bees;
- full mod compatibility;
- complex asset pipeline;
- full JEI/REI integration;
- quest book integration;
- huge mutation tree;
- custom dimensions;
- advanced machines;
- magic systems;
- full localization;
- final balancing.
```

Do not add content just because it is easy.

Every new bee species must have a purpose.

---

## 4. Expansion Principles

### 4.1 Expand by Systems, Not by Lists

Bad expansion:

```text
Add 100 bees.
```

Good expansion:

```text
Add a biome bee tier that introduces environment-based breeding constraints.
```

### 4.2 Each Species Needs Identity

A species should ideally have at least three of these:

```text
- mutation role;
- biome/environment role;
- production identity;
- trait tendency;
- progression role;
- compatibility role;
- unique breeding challenge;
- tech/magic dependency;
- useful recessive trait;
- reason to preserve hybrids.
```

Avoid species where the only difference is:

```text
Same bee, different output item.
```

### 4.3 Resource Bees Should Be Earned

Resource bees should not be starter content.

They should require:

```text
- a stable genetic base;
- mutated parent species;
- environmental conditions;
- tech progression;
- lower probability mutations;
- purification work;
- possibly special frames or apiary modifiers.
```

### 4.4 Hybrids Should Stay Useful

Do not design content where players only care about purebreds.

Hybrid usefulness can come from:

```text
- inactive species secondary production;
- inherited traits;
- mutation potential;
- climate/flower tolerance;
- future apiary bonuses;
- compatibility traits.
```

### 4.5 Avoid Deterministic Recipe Thinking

Do not make the expansion tree feel like:

```text
A + B = C, guaranteed.
```

Keep the Forestry-inspired fantasy:

```text
A + B can lead to C, but genetics, chance, environment and lineage matter.
```

---

## 5. Recommended Content Tiers

The exact tier names can change, but the progression should generally follow this shape:

```text
Tier 0 — Wild Starters
Tier 1 — Early Mutations
Tier 2 — Biome Adaptation
Tier 3 — Managed/Tech Bees
Tier 4 — Harsh Environment Bees
Tier 5 — Resource Foundation Bees
Tier 6 — Resource Bees
Tier 7 — Industrial / Compatibility Bees
Tier 8 — Exotic / Endgame Bees
```

---

# Tier 0 — Wild Starters

## Purpose

Provide the player with natural genetic starting points.

## Existing MVP Species

```text
Meadow
Forest
Arid
```

## Expansion Candidates

Future wild starters may include:

```text
Swamp
Jungle
Tundra
Mountain
Coastal
Cave
```

## Design Rule

Do not add too many wild starters early.

Each wild starter increases:

```text
- spawn rules;
- analysis complexity;
- mutation tree complexity;
- content balancing;
- player onboarding burden.
```

## Verification Gate — Wild Starter Expansion Review

Before adding a new wild starter, verify:

```text
- It has a clear biome identity.
- It unlocks at least one meaningful mutation path.
- It does not duplicate an existing starter.
- It has trait tendencies that matter.
- It does not require new systems just to exist.
```

---

# Tier 1 — Early Mutations

## Purpose

Prove the player can create new species by breeding wild starters.

## Existing MVP Species

```text
Cultivated
Hardy
```

## Future Candidates

```text
Diligent
Noble
Resilient
Arboreal
Pastoral
Feral
```

These names are placeholders.

## Design Role

Early mutations should teach:

```text
- chance-based mutation;
- hybrid outcomes;
- purebred stabilization;
- trait inheritance;
- analyzer usage;
- why repeated breeding matters.
```

## Avoid

Do not make early mutations too rare.

Early content should teach the system, not frustrate the player.

## Verification Gate — Early Mutation Review

Before adding a new early mutation, verify:

```text
- It is understandable from its parent species.
- It has a clear progression role.
- Its mutation chance is not too punishing.
- It creates useful hybrid and purebred outcomes.
- It does not require advanced machines.
```

---

# Tier 2 — Biome Adaptation Bees

## Purpose

Introduce environmental progression.

Biome bees should make the world matter.

## Candidate Categories

```text
Swamp Bees
Jungle Bees
Tundra Bees
Mountain Bees
Ocean/Coastal Bees
Cave Bees
Dark Forest Bees
```

## Possible Species

```text
Swamp
Mire
Jungle
Tropical
Tundra
Frost
Alpine
Stonebound
Coastal
Briny
Cavern
Nocturnal
```

These are placeholders, not final names.

## Possible Mechanics

Biome bees can introduce:

```text
- biome-restricted mutation chances;
- humidity preferences;
- temperature preferences;
- flower type variation;
- weather tolerance;
- production variation;
- future territory changes.
```

## Implementation Dependency

Do not implement biome bees until at least one of these exists:

```text
- environment context in mutation service;
- biome-aware spawn resolver;
- analyzer output can explain environment requirements;
- content registry can represent biome/environment conditions.
```

## Verification Gate — Biome System Review

Before adding biome bees, verify:

```text
- EnvironmentContext exists or is planned.
- Mutation definitions can express biome restrictions.
- Analyzer/debug output can show enough information for testing.
- The player has a way to reason about the condition.
- Biome restrictions do not make breeding opaque or annoying.
```

---

# Tier 3 — Managed / Tech Bees

## Purpose

Introduce bees that represent domestication, control and efficiency.

These bees should bridge natural breeding and tech automation.

## Possible Species

```text
Managed
Diligent
Industrious
Noble
Ordered
Stable
Productive
Refined
```

Names are placeholders.

## Possible Traits

Tech/managed bees may tend toward:

```text
- higher productivity;
- more predictable fertility;
- better production outputs;
- lower mutation instability;
- improved apiary compatibility;
- better frame response.
```

## Possible Unlock Path

Examples:

```text
Cultivated + Hardy -> Resilient
Cultivated + Meadow -> Diligent
Cultivated + Forest -> Arboreal
Diligent + Noble -> Industrious
```

Do not treat these as final recipes.

## Verification Gate — Tech Bee Review

Before adding tech bees, verify:

```text
- Tech apiary exists or is planned soon.
- Productivity trait has gameplay relevance.
- Frames or controlled breeding can interact with them.
- They are not just direct upgrades that invalidate wild species.
```

---

# Tier 4 — Harsh Environment Bees

## Purpose

Make difficult dimensions and environments part of breeding progression.

## Categories

```text
Desert/Heat
Cold/Frost
Cave/Darkness
Nether
End
Storm/Weather
```

## Possible Species

```text
Scorched
Ashen
Frosted
Glacial
Cavern
Umbral
Blazing
Soul
Basalt
Ender
Void
Chorus
```

## Possible Mechanics

```text
- dimension restrictions;
- special flower/food requirements;
- temperature tolerance;
- humidity tolerance;
- weather tolerance;
- daylight/nocturnal behavior;
- dangerous environment production.
```

## Verification Gate — Harsh Environment Review

Before implementing harsh environment bees, verify:

```text
- Environment conditions are data/model driven.
- Player can inspect or infer requirements.
- Breeding does not become trial-and-error only.
- There is a meaningful reward for the extra difficulty.
```

---

# Tier 5 — Resource Foundation Bees

## Purpose

Create an intermediate tier before direct resource bees.

This helps avoid jumping from wild bees directly to "Iron Bee".

Resource foundation bees should represent mineral, metallic, crystalline or energetic themes without immediately producing large amounts of resources.

## Possible Species

```text
Mineral
Metallic
Crystalline
Conductive
Resonant
Smelting
Geologic
Deep
```

## Role

Resource foundation bees can unlock:

```text
- resource bee mutation paths;
- low-tier material byproducts;
- special comb processing;
- compatibility with machines;
- tech apiary upgrades.
```

## Example Progression

```text
Hardy + Cavern -> Mineral
Mineral + Industrious -> Metallic
Metallic + specific material condition -> resource bee
```

This is conceptual only.

## Verification Gate — Resource Foundation Review

Before adding resource foundation bees, verify:

```text
- Basic production exists.
- Product processing exists or is planned.
- The economy impact of resource generation is understood.
- The player has already engaged with genetics before reaching resources.
```

---

# Tier 6 — Resource Bees

## Purpose

Allow bees to produce resources, but only after the genetic loop and progression are established.

## Resource Bee Philosophy

Resource bees should not be the identity of the whole mod.

They should be:

```text
- late enough to feel earned;
- balanced around tech progression;
- slower or more involved than direct mining early on;
- compatible with hybrid genetics;
- configurable later through data-driven content.
```

## Naming Strategy Options

### Option A — Direct Names

```text
Iron Bee
Copper Bee
Gold Bee
Redstone Bee
Diamond Bee
Emerald Bee
```

Pros:

```text
- easy to understand;
- good for modpack users;
- searchable.
```

Cons:

```text
- feels close to existing resource bee mods;
- less thematic.
```

### Option B — Thematic Names

```text
Ferric
Cupric
Aureate
Resonant
Crystalline
Verdant
```

Pros:

```text
- stronger identity;
- less directly Productive Bees-like;
- more immersive.
```

Cons:

```text
- less immediately obvious;
- may need better analyzer/guidebook support.
```

### Option C — Hybrid Naming

```text
Ferric Bee (Iron)
Cupric Bee (Copper)
Resonant Bee (Redstone)
```

Pros:

```text
- thematic and understandable;
- works well in tooltips/analyzer.
```

Initial recommendation:

```text
Use thematic names with clear item/tooltips.
```

## Resource Bee Categories

```text
Basic Metals
Redstone/Energy
Gems
Nether Materials
End Materials
Modded Materials
```

## Examples

### Basic Metals

```text
Ferric / Iron
Cupric / Copper
Aureate / Gold
Stannic / Tin, if modded
Plumbic / Lead, if modded
```

### Redstone / Energy

```text
Resonant / Redstone
Charged / Energy
Conductive / Copper/Redstone hybrid
```

### Gems

```text
Crystalline / Diamond
Verdant / Emerald
Lapis / Lapis Lazuli
Quartz / Quartz
```

### Nether

```text
Blazing / Blaze-related
Cindering / Coal/Charcoal
Ancient / Netherite path, very late
Soulbound / Soul materials
```

### End

```text
Ender
Void
Chorus
Shulker
Draconic, if ever appropriate
```

## Balancing Rules

Resource bees should consider:

```text
- low base production;
- need for processing combs;
- mutation rarity;
- environmental constraints;
- tech apiary requirement;
- frame requirement;
- slow purification;
- config toggles later.
```

## Verification Gate — Resource Bee Go/No-Go

Do not implement resource bees until:

```text
- basic production loop exists;
- content loading strategy is stable or at least centralized;
- balance philosophy is documented;
- at least one non-resource progression tier exists;
- analyzer can explain species and traits well;
- tech/apiary or processing loop exists or is planned.
```

---

# Tier 7 — Industrial / Compatibility Bees

## Purpose

Integrate Curious Bees with other mods and tech ecosystems after the core mod is stable.

## Compatibility Philosophy

Do not make compat bees mandatory for the base mod.

Compatibility should be optional and modular.

## Possible Compat Targets

```text
Create
Mekanism
Thermal
Farmer's Delight
Botania-like mods
Ars Nouveau-like mods
Applied Energistics-like mods
Immersive Engineering-like mods
```

## Compatibility Content Types

```text
- species;
- products;
- mutations;
- production processing;
- apiary modifiers;
- frame materials;
- special flower types;
- item conditions;
- dimension/biome conditions.
```

## Implementation Requirement

Before compat content:

```text
- content definitions should be data-driven or easily centralized;
- optional mod checks must be isolated;
- missing mods must not crash the base mod;
- compat content must not pollute the common genetics core.
```

## Verification Gate — Compat Readiness Review

Before implementing any compatibility bees, verify:

```text
- base mod works standalone;
- optional dependency strategy is defined;
- compat content can be disabled;
- common code does not hard-depend on external mods;
- generated outputs do not destabilize economy.
```

---

# Tier 8 — Exotic / Endgame Bees

## Purpose

Create aspirational long-term goals.

Endgame bees should feel rare, strange, powerful or highly specialized.

## Possible Categories

```text
Void
Temporal
Astral
Draconic
Quantum
Ancient
Royal
Mythic
```

## Design Rule

Endgame bees should not simply produce more items.

They can provide:

```text
- unique effects;
- special apiary modifiers;
- high-level production;
- rare mutation pathways;
- automation benefits;
- powerful but constrained traits.
```

## Verification Gate — Endgame Design Review

Before adding endgame bees, verify:

```text
- the earlier progression is fun;
- resource bees are balanced;
- there is a reason to continue breeding;
- endgame rewards do not trivialize all content;
- the mutation tree remains understandable.
```

---

## 6. Mutation Tree Strategy

### 6.1 Keep the Tree Layered

Do not create a giant flat mutation list.

Preferred structure:

```text
Wild Starters
  -> Early Mutations
    -> Biome Adaptations
      -> Managed/Tech Bees
        -> Resource Foundation
          -> Resource Bees
            -> Exotic/Endgame
```

### 6.2 Use Branch Identity

Each branch should have a theme.

Examples:

```text
Forest branch -> leaves, resin, arboreal traits
Arid branch -> heat, cactus, low fertility, resilience
Cultivated branch -> productivity, domestication, managed bees
Hardy branch -> resilience, harsh conditions, environment tolerance
Cavern branch -> minerals, darkness, resource foundation
Nether branch -> heat, blaze, soul, basalt
End branch -> teleportation, void, chorus, exotic materials
```

### 6.3 Avoid Mutation Spam

A mutation should be added only if it does at least one of:

```text
- unlocks a new branch;
- teaches a new mechanic;
- supports an important production role;
- creates a meaningful hybrid;
- enables future content.
```

---

## 7. Content Data Requirements

Expanded content should eventually be representable as data.

A future species definition should support:

```text
id
displayName
category
tier
dominance
defaultTraits
preferredEnvironment
products
mutationRole
lore/description
compat requirements
```

A future mutation definition should support:

```text
id
parents
result
baseChance
environmentRequirements
dimensionRequirements
nearbyBlockRequirements
frameModifiers
resultModeWeights
discoveryVisibility
```

A future production definition should support:

```text
species
primaryOutputs
secondaryOutputs
productivityScaling
processingRequirements
compat requirements
```

---

## 8. Analyzer / Guidebook Requirements for Expanded Content

As content grows, the analyzer may need more layers.

Possible progression:

```text
Basic Analyzer
Advanced Analyzer
Mutation Notes
Species Journal
Research System
Guidebook Integration
```

Expanded content should not rely on external wiki reading only.

Players should eventually be able to learn:

```text
- what species they have;
- what traits are active/inactive;
- whether the bee is hybrid or pure;
- known mutation hints;
- environment requirements;
- production behavior.
```

---

## 9. Asset Strategy for Expanded Content

Do not create full assets before mechanics exist.

### Early Expanded Content

Use:

```text
- shared placeholder comb textures;
- color variants if needed;
- simple item models;
- generated item models;
- minimal block models.
```

### Later Polishing

Use:

```text
- Blockbench for custom blocks;
- consistent item texture style;
- species icons if needed;
- analyzer UI assets;
- apiary/machine models.
```

### Blockbench / MCP Automation

Blockbench or Blockbench MCP can be evaluated later.

Do not make it a hard dependency.

Before adopting automation, define:

```text
- asset naming conventions;
- source asset folder;
- export targets;
- manual review step;
- security rules for MCP/plugin use;
- fallback manual workflow.
```

---

## 10. Configuration Strategy

Expanded content will eventually need configuration.

Possible future config:

```text
enableResourceBees
enableNetherBees
enableEndBees
enableCompatBees
resourceBeeProductionMultiplier
mutationChanceMultiplier
allowPureMutationResults
enableHardBiomeRequirements
```

Do not implement config before core systems are stable.

But design content so config can be added later.

---

## 11. Balancing Principles

### 11.1 Time Investment

Breeding should require time, but not feel impossible.

Mutation chance should be balanced around:

```text
- breeding speed;
- analyzer availability;
- production rewards;
- tech upgrades;
- player patience;
- modpack context.
```

### 11.2 Purification

Purebred stabilization should be valuable.

Do not make every mutation immediately pure.

Suggested philosophy:

```text
hybrid result common
pure result rare
purebred stabilization mostly achieved by selective breeding
```

### 11.3 Production Economy

Resource production should be slower or more conditional than direct early-game mining.

Bees become powerful through:

```text
- automation;
- selective breeding;
- tech progression;
- investment;
- scaling over time.
```

### 11.4 Configurability Later

Balance should eventually be configurable for modpacks.

Do not hardcode final economy assumptions permanently.

---

## 12. Phase 9 Task Breakdown

### Task 9.1 — Define Expanded Content Categories

#### Objective

Define the final list of future content categories.

#### Scope

Categories may include:

```text
Biome Bees
Managed/Tech Bees
Harsh Environment Bees
Resource Foundation Bees
Resource Bees
Industrial Bees
Magic Bees
Compatibility Bees
Exotic/Endgame Bees
```

#### Acceptance Criteria

```text
- Categories are documented.
- Each category has a purpose.
- Categories are ordered by progression tier.
- MVP remains unchanged.
```

#### Prompt

```text
Read CLAUDE.md and docs/implementation/09-expanded-content-roadmap.md.

Focus only on Task 9.1 — Define Expanded Content Categories.

Do not implement code.
Do not add species to the mod yet.
Produce a short content planning update that confirms categories, purpose and ordering.
```

---

### Task 9.2 — Define Naming Strategy

#### Objective

Choose a naming direction for future species.

#### Scope

Compare:

```text
direct names
thematic names
hybrid names
```

#### Acceptance Criteria

```text
- Resource bee naming strategy is chosen or narrowed.
- Thematic rules are documented.
- Player clarity is considered.
- Avoid copying existing mods too closely.
```

#### Prompt

```text
Read the expanded content roadmap.

Focus only on Task 9.2 — Define Naming Strategy.

Do not implement code.
Produce a naming decision record with options, pros/cons and recommendation.
```

---

### Task 9.3 — Draft First Post-MVP Mutation Branch

#### Objective

Draft the first expansion branch after the MVP species.

#### Scope

Possible branches:

```text
Cultivated/Managed branch
Hardy/Biome branch
Forest/Arboreal branch
Arid/Harsh Environment branch
```

#### Acceptance Criteria

```text
- Branch has 3–6 species maximum.
- Each species has a role.
- Each mutation has a reason to exist.
- No resource bees yet unless explicitly approved.
```

#### Prompt

```text
Read the expanded content roadmap and content design spec.

Focus only on Task 9.3 — Draft First Post-MVP Mutation Branch.

Do not implement code.
Do not create a huge tree.
Propose one small branch with species roles, parent combinations, mutation chance suggestions and progression purpose.
```

---

### Task 9.4 — Resource Bee Readiness Decision

#### Objective

Decide what must be true before resource bees are allowed.

#### Scope

Create a decision record.

#### Acceptance Criteria

```text
- Defines hard prerequisites for resource bees.
- Defines balancing principles.
- Defines naming direction.
- Defines what resource bees must not do.
```

#### Prompt

```text
Read the expanded content roadmap.

Focus only on Task 9.4 — Resource Bee Readiness Decision.

Do not implement resource bees.
Create a decision record that defines when resource bees are allowed and what design constraints they must follow.
```

---

### Task 9.5 — Asset Roadmap for Expanded Content

#### Objective

Define when assets become important and how they should be produced.

#### Scope

Include:

```text
placeholder items
comb textures
Blockbench models
source asset folder
review workflow
MCP/plugin safety considerations
```

#### Acceptance Criteria

```text
- Assets are not blocking MVP.
- Placeholder strategy is defined.
- Blockbench usage is assigned to later block/machine work.
- MCP automation remains optional.
```

#### Prompt

```text
Read the expanded content roadmap.

Focus only on Task 9.5 — Asset Roadmap for Expanded Content.

Do not generate assets.
Do not implement code.
Produce a workflow for placeholders, Blockbench models and future asset automation.
```

---

## 13. Verification Gates

### Gate 1 — Category Review

Before drafting any new species, verify:

```text
- content categories are understood;
- progression order is clear;
- MVP boundaries remain intact;
- no resource bee pressure is leaking into early phases.
```

### Gate 2 — Naming Review

Before creating many names, verify:

```text
- naming strategy is chosen;
- names avoid being too close to Productive Bees where possible;
- names remain understandable;
- tooltips/analyzer can help clarify thematic names.
```

### Gate 3 — First Branch Review

Before implementing post-MVP content, verify:

```text
- one small branch has been designed;
- every species has a role;
- mutation paths are not too dense;
- branch can be tested with existing systems.
```

### Gate 4 — Resource Bee Go/No-Go

Before resource bees, verify:

```text
- production system exists;
- tech progression exists or is planned;
- economy impact is understood;
- resource bees have constraints;
- config strategy exists or is planned.
```

### Gate 5 — Asset Pipeline Review

Before polished assets, verify:

```text
- source asset folder exists;
- naming conventions exist;
- placeholder replacement workflow exists;
- Blockbench workflow is documented;
- MCP/plugin security concerns are understood.
```

---

## 14. Go / No-Go Checkpoint Before Implementation

Phase 9 content implementation should not begin until:

```text
- MVP gameplay loop is complete;
- at least one full breeding/mutation/analyzer/production flow works;
- content definitions are centralized;
- the player can understand genetics in-game;
- balance assumptions are documented;
- there is agreement on the first expansion branch.
```

If these are not true, Phase 9 remains planning-only.

---

## 15. Phase 9 Completion Criteria

Phase 9 planning is complete when:

```text
- future content categories are documented;
- progression tiers are defined;
- resource bee philosophy is documented;
- naming strategy is chosen or narrowed;
- post-MVP mutation branch strategy exists;
- asset roadmap exists;
- implementation remains explicitly blocked until MVP is complete.
```

---

## 16. Recommended Commit Strategy

When this phase eventually becomes active:

```text
docs: add expanded content roadmap
docs: add resource bee readiness decision
docs: add naming strategy decision
content: add first post-MVP branch definitions
content: add biome/managed bee definitions
content: add resource foundation definitions
content: add resource bee definitions
assets: add placeholder expanded content assets
assets: replace placeholders with polished assets
```

Do not jump directly to resource bees.

---

## 17. Final Rule

Phase 9 is not a license to add everything.

It is a map.

The project should still grow in this order:

```text
Correct core
Small content
NeoForge storage
Breeding
Analyzer
Production
Tech
Data-driven content
Expanded content
Fabric
```

Expanded content should reward the genetic system, not replace it with a list of recipes.
