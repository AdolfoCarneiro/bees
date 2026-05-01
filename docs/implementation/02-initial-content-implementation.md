# 02 — Initial Content Implementation

## 1. Purpose

This document defines the implementation plan for **Phase 2 — Initial Content** of Curious Bees.

Phase 2 takes the pure genetics core from Phase 1 and gives it a small, meaningful set of built-in species, trait alleles, and mutation definitions.

The goal is not to create a large content tree. The goal is to prove that the genetics engine can work with real gameplay data before Minecraft entity integration begins.

## 2. Phase Goal

Implement a minimal built-in content layer that can create usable genomes and mutation definitions for the first playable breeding loop.

By the end of this phase, the project should be able to:

```text
- define the first five bee species;
- define MVP trait alleles;
- create default genomes for each MVP species;
- define the first two species mutations;
- run genetics tests using realistic built-in content;
- keep all content centralized;
- remain ready for future JSON/datapack migration.
```

## 3. Phase Non-goals

Do not implement:

```text
- Minecraft item/block registration;
- NeoForge entity attachments;
- Fabric support;
- wild bee spawn hooks;
- baby bee breeding hooks;
- analyzer item;
- production machines;
- resource bees;
- nether/end bees;
- full mutation tree;
- JSON/datapack loading;
- art assets;
- textures;
- Blockbench models.
```

Phase 2 is still mostly pure Java and should remain independent from Minecraft APIs whenever possible.

## 4. Required Inputs

Before implementing this phase, the AI agent should read:

```text
CLAUDE.md
docs/mvp/01-product-vision-and-roadmap.md
docs/mvp/02-technical-architecture.md
docs/mvp/03-genetics-system-spec.md
docs/mvp/04-breeding-and-mutation-spec.md
docs/mvp/05-content-design-spec.md
docs/mvp/06-ai-coding-guidelines.md
docs/implementation/01-genetics-core-implementation.md
```

## 5. Expected Outputs

Phase 2 should produce source code and tests for:

```text
BeeSpeciesDefinition
BeeTraitDefinitions or equivalent
BuiltinBeeSpecies
BuiltinBeeTraits
BuiltinBeeMutations
BuiltinBeeContent or equivalent facade
DefaultGenomeFactory or equivalent
Content validation tests
Mutation fixture tests using real MVP species
```

Exact class names can change, but the responsibilities must remain clear and centralized.

## 6. Architecture Rules

### 6.1 Keep Built-in Content Centralized

Good:

```text
BuiltinBeeSpecies
BuiltinBeeTraits
BuiltinBeeMutations
BuiltinBeeContent
```

Bad:

```text
if species == "meadow" scattered in services
if species == "forest" inside breeding logic
hardcoded mutation checks inside MutationService
```

### 6.2 Keep Content Definitions Minecraft-independent

The content layer should use stable string IDs.

Good:

```text
curious_bees:species/meadow
curious_bees:traits/productivity/fast
curious_bees:mutations/cultivated_from_meadow_forest
```

Avoid using Minecraft registry classes in Phase 2.

Do not require:

```text
ResourceLocation
Item
Block
Biome
RegistryObject
DeferredRegister
```

Those belong to later NeoForge/platform layers.

### 6.3 Design for JSON Later, But Do Not Implement JSON Yet

The content objects should feel like they could be serialized later.

This means:

```text
- stable IDs;
- explicit fields;
- validation;
- no hidden magic behavior;
- clear references between species and mutations.
```

But do not implement:

```text
- datapack loading;
- JSON codecs;
- reload listeners;
- resource managers;
- schema validation files.
```

### 6.4 Do Not Turn Content Into Gameplay Logic

Species definitions can contain metadata and default values.

They should not perform breeding, mutation, production, or Minecraft behavior directly.

## 7. MVP Species

Phase 2 must define exactly these initial species:

```text
Meadow
Forest
Arid
Cultivated
Hardy
```

### 7.1 Meadow Bee

Role:

```text
Starter wild species.
Common plains/flower biome bee.
Stable generalist.
```

Suggested ID:

```text
curious_bees:species/meadow
```

Display name:

```text
Meadow Bee
```

Dominance:

```text
DOMINANT
```

Default genome tendency:

```text
Species: Meadow / Meadow
Lifespan: Normal / Normal
Productivity: Normal / Normal
Fertility: Two / Two
FlowerType: Flowers / Flowers
```

Spawn context metadata:

```text
plains
flower_forest
meadow-like
fallback starter
```

Production concept placeholder:

```text
Honeycomb
Basic Wax
```

Mutation role:

```text
Meadow + Forest -> Cultivated
```

### 7.2 Forest Bee

Role:

```text
Starter wild forest bee.
Useful for early mutation paths.
```

Suggested ID:

```text
curious_bees:species/forest
```

Display name:

```text
Forest Bee
```

Dominance:

```text
DOMINANT
```

Default genome tendency:

```text
Species: Forest / Forest
Lifespan: Normal / Normal
Productivity: Normal / Normal
Fertility: Two / Two
FlowerType: Leaves / Leaves
```

Spawn context metadata:

```text
forest
birch_forest
dark_forest
taiga-like later
```

Production concept placeholder:

```text
Honeycomb
Forest resin-like byproduct later
```

Mutation role:

```text
Meadow + Forest -> Cultivated
Forest + Arid -> Hardy
```

### 7.3 Arid Bee

Role:

```text
Starter wild dry-climate bee.
Introduces non-flower environmental identity.
```

Suggested ID:

```text
curious_bees:species/arid
```

Display name:

```text
Arid Bee
```

Dominance:

```text
RECESSIVE
```

Default genome tendency:

```text
Species: Arid / Arid
Lifespan: Normal / Normal
Productivity: Slow / Normal
Fertility: One / Two
FlowerType: Cactus / Cactus
```

Spawn context metadata:

```text
desert
savanna
badlands
dry areas
```

Production concept placeholder:

```text
Dry Comb
Wax
```

Mutation role:

```text
Forest + Arid -> Hardy
```

### 7.4 Cultivated Bee

Role:

```text
First successful mutation.
Early proof that breeding can create a new species.
Bridge between wild bees and managed beekeeping.
```

Suggested ID:

```text
curious_bees:species/cultivated
```

Display name:

```text
Cultivated Bee
```

Dominance:

```text
DOMINANT
```

Default genome tendency:

```text
Species: Cultivated / Cultivated
Lifespan: Normal / Normal
Productivity: Fast / Normal
Fertility: Two / Two
FlowerType: Flowers / Flowers
```

Mutation source:

```text
Meadow + Forest -> Cultivated
```

Suggested mutation chance:

```text
12%
```

Production concept placeholder:

```text
Cultivated Comb
Improved honey/wax output later
```

### 7.5 Hardy Bee

Role:

```text
Early environmental mutation.
Represents resilience and harsh-climate adaptation.
```

Suggested ID:

```text
curious_bees:species/hardy
```

Display name:

```text
Hardy Bee
```

Dominance:

```text
RECESSIVE
```

Default genome tendency:

```text
Species: Hardy / Hardy
Lifespan: Long / Normal
Productivity: Normal / Normal
Fertility: Two / Two
FlowerType: Flowers / Cactus
```

Mutation source:

```text
Forest + Arid -> Hardy
```

Suggested mutation chance:

```text
8%
```

Production concept placeholder:

```text
Hardy Comb
Durable wax / frame-related material later
```

## 8. MVP Trait Alleles

Phase 2 must define built-in alleles for:

```text
Lifespan
Productivity
Fertility
FlowerType
```

### 8.1 Lifespan Alleles

Required values:

```text
Short
Normal
Long
```

Suggested IDs:

```text
curious_bees:traits/lifespan/short
curious_bees:traits/lifespan/normal
curious_bees:traits/lifespan/long
```

Suggested dominance:

```text
Short: RECESSIVE
Normal: DOMINANT
Long: RECESSIVE
```

MVP gameplay:

```text
Stored and displayed.
No strong gameplay effect required yet.
```

Future gameplay:

```text
Tech apiary cycles.
Productive lifetime.
Controlled breeding operations.
```

### 8.2 Productivity Alleles

Required values:

```text
Slow
Normal
Fast
```

Suggested IDs:

```text
curious_bees:traits/productivity/slow
curious_bees:traits/productivity/normal
curious_bees:traits/productivity/fast
```

Suggested dominance:

```text
Slow: RECESSIVE
Normal: DOMINANT
Fast: RECESSIVE
```

Suggested placeholder multipliers:

```text
Slow: 0.75
Normal: 1.00
Fast: 1.25
```

MVP gameplay:

```text
Stored and displayed.
May later affect production.
```

### 8.3 Fertility Alleles

Required values:

```text
One
Two
Three
```

Suggested IDs:

```text
curious_bees:traits/fertility/one
curious_bees:traits/fertility/two
curious_bees:traits/fertility/three
```

Suggested dominance:

```text
One: RECESSIVE
Two: DOMINANT
Three: RECESSIVE
```

MVP gameplay:

```text
Stored and displayed.
Do not spawn multiple live baby bees in vanilla breeding yet.
```

Future gameplay:

```text
Extra larvae in tech apiary.
Higher sample yield.
Reduced cooldown.
```

### 8.4 Flower Type Alleles

Required values:

```text
Flowers
Cactus
Leaves
```

Suggested IDs:

```text
curious_bees:traits/flower_type/flowers
curious_bees:traits/flower_type/cactus
curious_bees:traits/flower_type/leaves
```

Suggested dominance:

```text
Flowers: DOMINANT
Cactus: RECESSIVE
Leaves: RECESSIVE
```

MVP gameplay:

```text
Stored and displayed.
Vanilla flower breeding should still be permissive at first.
```

Future gameplay:

```text
Determines valid breeding/production environment.
```

## 9. MVP Mutation Definitions

Phase 2 must define two initial mutations.

### 9.1 Meadow + Forest -> Cultivated

Suggested ID:

```text
curious_bees:mutations/cultivated_from_meadow_forest
```

Parents:

```text
curious_bees:species/meadow
curious_bees:species/forest
```

Result:

```text
curious_bees:species/cultivated
```

Base chance:

```text
0.12
```

Result mode:

```text
Partial mutation: 95%
Full mutation: 5%
```

Allowed environments:

```text
Ignored for Phase 2 or represented as placeholder metadata only.
```

### 9.2 Forest + Arid -> Hardy

Suggested ID:

```text
curious_bees:mutations/hardy_from_forest_arid
```

Parents:

```text
curious_bees:species/forest
curious_bees:species/arid
```

Result:

```text
curious_bees:species/hardy
```

Base chance:

```text
0.08
```

Result mode:

```text
Partial mutation: 95%
Full mutation: 5%
```

Allowed environments:

```text
Ignored for Phase 2 or represented as placeholder metadata only.
```

## 10. Recommended Source Structure

Suggested packages:

```text
common/src/main/java/.../content/
├── species/
│   └── BeeSpeciesDefinition.java
├── traits/
│   └── BeeTraitDefinition.java or TraitAlleleDefinition.java
├── mutations/
│   └── BuiltinMutationDefinitions.java
└── builtin/
    ├── BuiltinBeeSpecies.java
    ├── BuiltinBeeTraits.java
    ├── BuiltinBeeMutations.java
    ├── BuiltinBeeContent.java
    └── DefaultGenomeFactory.java
```

Suggested tests:

```text
common/src/test/java/.../content/
├── BuiltinBeeSpeciesTest.java
├── BuiltinBeeTraitsTest.java
├── BuiltinBeeMutationsTest.java
├── DefaultGenomeFactoryTest.java
└── BuiltinContentIntegrationTest.java
```

Exact package names may vary according to the project.

## 11. Task Breakdown

## Task 1 — Define Species Definition Model

### Objective

Create a model for bee species definitions that is independent from Minecraft APIs.

### Scope

Implement a class or record equivalent to:

```text
BeeSpeciesDefinition
```

Expected fields:

```text
id
displayName
dominance
default trait references or default genome metadata
role/notes optional
spawn context metadata optional
```

### Non-goals

Do not implement:

```text
- Minecraft registry integration;
- textures;
- items;
- production items;
- biome matching;
- JSON loading.
```

### Acceptance Criteria

```text
- BeeSpeciesDefinition exists.
- It has stable id.
- It has display name.
- It has dominance.
- It can express or reference default traits.
- Invalid id/name/dominance fails early.
- It has no Minecraft/NeoForge/Fabric dependency.
```

### Expected Tests

```text
valid species definition succeeds
null/blank id fails
null/blank display name fails
null dominance fails
default trait references can be read
```

### Prompt for Claude Code

```text
Read CLAUDE.md and the relevant docs.

Focus only on: Task 1 — Define Species Definition Model from docs/implementation/02-initial-content-implementation.md.

Implement a Minecraft-independent BeeSpeciesDefinition model.

Do not implement built-in species lists, traits, mutations, JSON loading, NeoForge integration, Fabric support, items, blocks, textures or production.

Add unit tests for valid and invalid construction.

Before coding, summarize your understanding and list the files you expect to create or modify.
```

## Verification Gate 1 — Species Model Review

Before moving to built-in species:

```text
- Does BeeSpeciesDefinition avoid Minecraft APIs?
- Does it use stable IDs?
- Can it represent dominance?
- Can it reference default traits without hardcoding gameplay logic?
- Are invalid definitions rejected?
- Are tests present and passing?
```

Do not proceed if this model is unclear, because all built-in species depend on it.

## Task 2 — Define Trait Allele Definitions

### Objective

Create centralized built-in trait allele definitions for the MVP chromosomes.

### Scope

Define alleles for:

```text
Lifespan
Productivity
Fertility
FlowerType
```

This can use the existing `Allele` model from Phase 1 or a lightweight definition model that produces `Allele` instances.

### Required Trait Values

```text
Lifespan: Short, Normal, Long
Productivity: Slow, Normal, Fast
Fertility: One, Two, Three
FlowerType: Flowers, Cactus, Leaves
```

### Non-goals

Do not implement:

```text
- production effects;
- breeding item restrictions;
- tech apiary cycle effects;
- JSON loading;
- Minecraft registries.
```

### Acceptance Criteria

```text
- All MVP trait alleles are defined.
- Each trait allele has stable id.
- Each trait allele has correct ChromosomeType.
- Each trait allele has dominance.
- Definitions are centralized.
- Tests verify all required values exist.
```

### Expected Tests

```text
all lifespan alleles exist
all productivity alleles exist
all fertility alleles exist
all flower type alleles exist
all trait ids are unique
all trait allele chromosome types are correct
all trait alleles have dominance
```

### Prompt for Claude Code

```text
Read CLAUDE.md and the relevant docs.

Focus only on: Task 2 — Define Trait Allele Definitions from docs/implementation/02-initial-content-implementation.md.

Define centralized built-in MVP trait alleles using the existing genetics core model.

Do not implement species definitions, mutations, production behavior, JSON loading or Minecraft integration.

Add tests to verify all required trait alleles exist, have unique stable ids, have correct chromosome types and have dominance metadata.

Before coding, summarize your understanding and list the files you expect to create or modify.
```

## Verification Gate 2 — Trait Allele Review

Before moving to built-in species:

```text
- Are all MVP trait values present?
- Are IDs stable and unique?
- Are chromosome types correct?
- Is the content centralized?
- Are tests present?
```

## Task 3 — Define Built-in Species

### Objective

Create the five MVP built-in species.

### Scope

Implement centralized definitions for:

```text
Meadow
Forest
Arid
Cultivated
Hardy
```

Suggested class:

```text
BuiltinBeeSpecies
```

Each species should define:

```text
id
display name
dominance
default trait tendencies/references
spawn context notes if modeled
```

### Non-goals

Do not implement:

```text
- actual biome matching;
- entity spawning;
- production items;
- JSON loading;
- resource bees;
- future species tree.
```

### Acceptance Criteria

```text
- Meadow species exists.
- Forest species exists.
- Arid species exists.
- Cultivated species exists.
- Hardy species exists.
- All species IDs are stable and unique.
- All species have dominance.
- All species have default trait references.
- No resource bees are added.
```

### Expected Tests

```text
all five MVP species exist
species ids are unique
species dominance matches spec
species default traits are complete
no unexpected resource species are present
```

### Prompt for Claude Code

```text
Read CLAUDE.md and the relevant docs.

Focus only on: Task 3 — Define Built-in Species from docs/implementation/02-initial-content-implementation.md.

Implement centralized built-in definitions for Meadow, Forest, Arid, Cultivated and Hardy bees.

Do not implement biome spawning, production items, JSON loading, Minecraft integration, resource bees or future species.

Add tests verifying all species exist, ids are unique, dominance is defined and default trait references are complete.

Before coding, summarize your understanding and list the files you expect to create or modify.
```

## Verification Gate 3 — MVP Species Review

Before moving to mutation definitions:

```text
- Are there exactly five MVP species?
- Are resource bees absent?
- Are species IDs stable?
- Are default trait references complete?
- Is built-in species content centralized?
- Are tests passing?
```

## Task 4 — Define Built-in Mutation Definitions

### Objective

Define the first two mutation definitions using the mutation model from Phase 1.

### Scope

Implement centralized mutation definitions for:

```text
Meadow + Forest -> Cultivated
Forest + Arid -> Hardy
```

Suggested class:

```text
BuiltinBeeMutations
```

### Mutation Values

```text
Cultivated from Meadow + Forest:
  chance: 0.12
  partial/full: 95% / 5%

Hardy from Forest + Arid:
  chance: 0.08
  partial/full: 95% / 5%
```

If the Phase 1 mutation model does not yet support partial/full chance, record result mode information in the cleanest compatible way and add a follow-up task.

### Non-goals

Do not implement:

```text
- JSON loading;
- biome requirements;
- frame modifiers;
- advanced mutation tree;
- resource mutations;
- in-game feedback.
```

### Acceptance Criteria

```text
- Two built-in mutation definitions exist.
- Parent order does not matter when used with MutationService.
- Mutation chances match MVP placeholders.
- Result species IDs are valid built-in species.
- Tests verify mutation definitions are valid.
```

### Expected Tests

```text
cultivated mutation exists
hardy mutation exists
mutation ids are unique
mutation parent species ids exist
mutation result species ids exist
mutation chance values are between 0 and 1
parent order behavior works through MutationService if available
```

### Prompt for Claude Code

```text
Read CLAUDE.md and the relevant docs.

Focus only on: Task 4 — Define Built-in Mutation Definitions from docs/implementation/02-initial-content-implementation.md.

Implement centralized built-in mutation definitions for:
- Meadow + Forest -> Cultivated
- Forest + Arid -> Hardy

Do not implement JSON loading, biome requirements, frame modifiers, advanced mutation trees, resource bees or in-game feedback.

Add tests verifying IDs, parent species, result species, chance values and compatibility with MutationService if available.

Before coding, summarize your understanding and list the files you expect to create or modify.
```

## Verification Gate 4 — Mutation Content Review

Before creating default genomes or simulation tests:

```text
- Are only the two MVP mutations present?
- Are chances correct?
- Are species references valid?
- Is parent order handled by the MutationService or tested through it?
- Are tests passing?
```

## Task 5 — Create Default Genome Factory

### Objective

Create default pure or mostly-pure genomes for the five MVP species.

### Scope

Implement a factory or helper that can create default genomes for:

```text
Meadow
Forest
Arid
Cultivated
Hardy
```

Suggested class:

```text
DefaultGenomeFactory
```

### Required Default Genomes

#### Meadow

```text
Species: Meadow / Meadow
Lifespan: Normal / Normal
Productivity: Normal / Normal
Fertility: Two / Two
FlowerType: Flowers / Flowers
```

#### Forest

```text
Species: Forest / Forest
Lifespan: Normal / Normal
Productivity: Normal / Normal
Fertility: Two / Two
FlowerType: Leaves / Leaves
```

#### Arid

```text
Species: Arid / Arid
Lifespan: Normal / Normal
Productivity: Slow / Normal
Fertility: One / Two
FlowerType: Cactus / Cactus
```

#### Cultivated

```text
Species: Cultivated / Cultivated
Lifespan: Normal / Normal
Productivity: Fast / Normal
Fertility: Two / Two
FlowerType: Flowers / Flowers
```

#### Hardy

```text
Species: Hardy / Hardy
Lifespan: Long / Normal
Productivity: Normal / Normal
Fertility: Two / Two
FlowerType: Flowers / Cactus
```

### Non-goals

Do not implement:

```text
- biome-based selection;
- entity spawn initialization;
- random wild variation;
- Minecraft integration;
- item storage.
```

### Acceptance Criteria

```text
- Factory can create a valid Meadow genome.
- Factory can create a valid Forest genome.
- Factory can create a valid Arid genome.
- Factory can create a valid Cultivated genome.
- Factory can create a valid Hardy genome.
- Every default genome includes all MVP chromosomes.
- Every default genome includes a SPECIES chromosome.
- Tests validate active/inactive data is stable.
```

### Expected Tests

```text
create meadow genome
create forest genome
create arid genome
create cultivated genome
create hardy genome
all default genomes contain required chromosomes
species gene pair matches expected species
default genomes are valid Genome objects
```

### Prompt for Claude Code

```text
Read CLAUDE.md and the relevant docs.

Focus only on: Task 5 — Create Default Genome Factory from docs/implementation/02-initial-content-implementation.md.

Implement a default genome factory for Meadow, Forest, Arid, Cultivated and Hardy using the existing genetics core and built-in content definitions.

Do not implement biome selection, Minecraft spawn logic, random wild variation, item storage, JSON loading or production.

Add tests verifying each default genome and all required chromosomes.

Before coding, summarize your understanding and list the files you expect to create or modify.
```

## Verification Gate 5 — Default Genome Review

Before simulation/integration tests:

```text
- Can every MVP species create a valid genome?
- Does every genome include all MVP chromosomes?
- Are trait defaults aligned with the content design?
- Are tests clear and stable?
```

## Task 6 — Create Built-in Content Facade

### Objective

Create a single access point for the built-in MVP content.

### Scope

Suggested class:

```text
BuiltinBeeContent
```

It should expose:

```text
species definitions
trait allele definitions
mutation definitions
default genome factory
lookup helpers by id
```

### Non-goals

Do not implement:

```text
- JSON loading;
- dynamic reload;
- datapack support;
- Minecraft registries;
- production item registration.
```

### Acceptance Criteria

```text
- Built-in content can be accessed from one clear facade.
- Species can be looked up by id.
- Trait alleles can be looked up by id if useful.
- Mutations can be listed.
- Default genomes can be created.
- Invalid lookup behavior is clear.
```

### Expected Tests

```text
lookup existing species succeeds
lookup missing species returns empty or clear error
all built-in mutations are accessible
default genome factory is accessible
all ids across built-in content are unique where relevant
```

### Prompt for Claude Code

```text
Read CLAUDE.md and the relevant docs.

Focus only on: Task 6 — Create Built-in Content Facade from docs/implementation/02-initial-content-implementation.md.

Implement a centralized built-in content facade for MVP species, trait alleles, mutations and default genome creation.

Do not implement JSON loading, datapack reload, Minecraft registries, item registration or production.

Add tests for species lookup, mutation listing, default genome access and missing lookup behavior.

Before coding, summarize your understanding and list the files you expect to create or modify.
```

## Verification Gate 6 — Built-in Content Facade Review

Before Phase 2 is considered complete:

```text
- Is all MVP content accessible from one place?
- Are there no scattered species conditionals?
- Are lookup rules clear?
- Are tests present?
- Could this facade later be replaced or extended by JSON-loaded content?
```

## Task 7 — Add Built-in Content Integration Tests

### Objective

Prove that the Phase 2 built-in content works with the Phase 1 genetics core.

### Scope

Add tests that:

```text
- create default genomes;
- breed MVP species;
- apply MVP mutations;
- verify hybrid/pure results using real built-in species;
- verify no unexpected species are required.
```

### Suggested Scenarios

#### Meadow + Forest

```text
Parents:
Meadow default genome
Forest default genome

Expected:
Breeding produces valid child genome.
Mutation service can produce Cultivated when forced to 100%.
```

#### Forest + Arid

```text
Parents:
Forest default genome
Arid default genome

Expected:
Breeding produces valid child genome.
Mutation service can produce Hardy when forced to 100%.
```

#### No Mutation Pair

```text
Parents:
Meadow default genome
Arid default genome

Expected:
No MVP mutation applies.
```

### Non-goals

Do not implement:

```text
- Minecraft integration tests;
- NeoForge test environment;
- production output tests;
- block/item tests.
```

### Acceptance Criteria

```text
- Built-in Meadow + Forest can trigger Cultivated in tests.
- Built-in Forest + Arid can trigger Hardy in tests.
- Built-in Meadow + Arid does not trigger an MVP mutation.
- Tests use deterministic random.
- Tests do not depend on Minecraft APIs.
```

### Prompt for Claude Code

```text
Read CLAUDE.md and the relevant docs.

Focus only on: Task 7 — Add Built-in Content Integration Tests from docs/implementation/02-initial-content-implementation.md.

Add pure Java tests proving the built-in MVP content works with the genetics core.

Test Meadow + Forest -> Cultivated, Forest + Arid -> Hardy and a no-mutation pair.

Do not implement Minecraft integration, NeoForge tests, production, items, blocks, textures or JSON loading.

Before coding, summarize your understanding and list the files you expect to create or modify.
```

## Verification Gate 7 — Phase 2 Integration Review

Before moving to NeoForge entity storage:

```text
- Can default genomes be created for all five MVP species?
- Can the BreedingService use those genomes?
- Can the MutationService use built-in mutations?
- Are tests deterministic?
- Are all tests independent from Minecraft APIs?
- Are resource bees absent?
- Is content centralized and JSON-ready later?
```

## 12. Phase 2 Completion Checklist

Phase 2 is complete when:

```text
- BeeSpeciesDefinition or equivalent exists.
- Built-in trait alleles exist.
- Built-in MVP species exist.
- Built-in MVP mutations exist.
- Default genomes can be created.
- Built-in content facade exists.
- Pure Java integration tests prove content works with genetics core.
- No Minecraft, NeoForge or Fabric dependencies were introduced into common genetics/content logic.
- No resource bees were added.
- JSON/datapack loading remains future scope.
```

## 13. Go / No-Go Checkpoint Before Phase 3

Do not begin NeoForge entity data integration until:

```text
- Phase 1 genetics core tests pass.
- Phase 2 built-in content tests pass.
- Default genomes exist for Meadow, Forest and Arid at minimum.
- Mutation definitions exist for Cultivated and Hardy.
- The team accepts that content is hardcoded for now.
- The team accepts that JSON/datapack loading is deferred.
```

If any of these are false, stay in Phase 1 or Phase 2.

## 14. AI Failure Modes to Watch

### 14.1 Adding Too Many Species

Bad:

```text
Adds Iron, Gold, Diamond, Nether, End, Magic bees during Phase 2.
```

Fix:

```text
Remove them. Phase 2 has only five MVP species.
```

### 14.2 Scattering Content Logic

Bad:

```text
BreedingService checks if species id is meadow.
MutationService has hardcoded if forest + arid.
```

Fix:

```text
Move content to built-in definitions and pass definitions into services.
```

### 14.3 Starting JSON Too Early

Bad:

```text
Implements datapack reloaders and JSON codecs before content rules stabilize.
```

Fix:

```text
Keep content hardcoded but structured.
```

### 14.4 Using Minecraft Classes in Content

Bad:

```text
BeeSpeciesDefinition stores Item, Block, Biome, ResourceLocation, RegistryObject.
```

Fix:

```text
Use stable string IDs and adapt to Minecraft registries later.
```

### 14.5 Turning Trait Data Into Gameplay Too Soon

Bad:

```text
Fertility spawns three live baby bees immediately.
FlowerType prevents early breeding without analyzer.
```

Fix:

```text
Store and display traits first. Add strong gameplay effects later.
```

## 15. Recommended Commit Sequence

```text
content: add species definition model
content: add built-in trait alleles
content: add built-in MVP species
content: add built-in mutation definitions
content: add default genome factory
content: add built-in content facade
test: add built-in content integration tests
```

## 16. Handoff Prompt for Starting Phase 2

```text
Read CLAUDE.md and the docs folder.

Then read:
- docs/implementation/01-genetics-core-implementation.md
- docs/implementation/02-initial-content-implementation.md

Do not implement all of Phase 2 at once.

Start only with Task 1 — Define Species Definition Model.

Before coding, summarize:
1. the goal of Phase 2;
2. the scope of Task 1;
3. what is explicitly out of scope;
4. which files you expect to create or modify.

Then implement Task 1 with tests.
```
