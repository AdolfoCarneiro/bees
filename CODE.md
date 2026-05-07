<!-- generated-by: gsd-doc-writer -->
# Curious Bees — Code Reference

## Module structure

```
curious-bees/
├── common/           Pure Java — no Minecraft/NeoForge/Fabric imports
│   └── src/main/java/com/curiousbees/common/
│       ├── genetics/         Core genetic model, breeding, mutation, serialization
│       ├── content/          Species/trait/mutation/production definitions + loading
│       └── gameplay/         Platform-neutral orchestration (breeding, analysis, production, spawn)
└── neoforge/         NeoForge 1.21.1 integration
    └── src/main/java/com/curiousbees/
        ├── CuriousBeesMod.java     Mod entrypoint
        └── neoforge/
            ├── block/          Block + BlockEntity implementations
            ├── item/           Item implementations
            ├── menu/           Container menu classes (server logic)
            ├── client/         Screens, overlays, renderers (client only)
            ├── event/          NeoForge event subscribers
            ├── registry/       DeferredRegister wrappers (Mod*, Tags)
            ├── capability/     IItemHandler sided capability registration
            ├── data/           Entity attachments, genome codec, data components
            ├── content/        ContentReloadListener + NeoForgeContentRegistry
            ├── network/        Packets and client-side network handlers
            ├── command/        Debug slash commands (/curiousbees ...)
            └── worldgen/       World feature for species bee nest placement
```

**Hard boundary:** nothing under `common/` may import `net.minecraft.*`, `net.neoforged.*`, or any game API. All Minecraft references live in `neoforge/`.

---

## common/genetics

### `model` package

| Class | Role |
|-------|------|
| `Allele` | Immutable value: stable string ID, `ChromosomeType`, and `Dominance`. Equality is id + type + dominance. |
| `ChromosomeType` | Enum of chromosome categories: `SPECIES`, `PRODUCTIVITY`, `FLOWER_TYPE` (MVP); `LIFESPAN`, `FERTILITY` present but not used. |
| `Dominance` | Enum: `DOMINANT`, `RECESSIVE`. Drives active/inactive resolution in `GenePair`. |
| `GenePair` | Pair of alleles for one chromosome with resolved `active` / `inactive`. Public constructor takes `GeneticRandom`; package-private `restored(...)` factory re-applies saved active/inactive without re-randomizing. |
| `Genome` | Immutable map of `ChromosomeType → GenePair`. Requires a `SPECIES` chromosome. Produces new instances via `withGenePair()` — never mutated in place. |

### `breeding` package

| Class | Role |
|-------|------|
| `BreedingService` | Applies Mendelian inheritance: for each chromosome, picks one allele randomly from each parent and constructs a child `GenePair`. Validates chromosome-set compatibility before crossing. |
| `BreedingResult` | Value object holding the `Genome` produced by `BreedingService.breed()`. |

### `mutation` package

| Class | Role |
|-------|------|
| `MutationService` | Evaluates a list of `MutationDefinition` against both parents' active species. Rolls `baseChance`; on success applies either `PARTIAL` (replace active allele) or `FULL` (replace both alleles) mutation to the child genome. |
| `MutationDefinition` | Immutable data: id, two parent species alleles (order-independent), result allele, base chance, and `MutationResultMode`. |
| `MutationResult` | Value object: mutated or un-mutated child `Genome` plus the `MutationDefinition` that fired (if any). |
| `MutationResultMode` | Enum: `PARTIAL` (~95%), `FULL` (~5%). Controls how many alleles are replaced on mutation. |

### `random` package

| Class | Role |
|-------|------|
| `GeneticRandom` | Interface: `nextDouble()`, `nextBoolean()`, `nextInt(int)`. Injected everywhere so tests can use deterministic sequences. |
| `JavaGeneticRandom` | Production wrapper around `java.util.Random`. |

### `serial` package

| Class | Role |
|-------|------|
| `GenomeData` | Serializable record: `Map<String, GenePairData>` keyed by `ChromosomeType.name()`. |
| `GenePairData` | Record: `firstAlleleId`, `secondAlleleId`, `activeAlleleId`, `inactiveAlleleId` (all stable string IDs). |
| `GenomeSerializer` | Converts `Genome ↔ GenomeData`. `toData()` never fails; `fromData()` returns `Optional.empty()` on unknown allele IDs or missing SPECIES chromosome, always logs a WARNING before returning empty. Allele lookup is injected via a `Function<String, Optional<Allele>>`. |

---

## common/content

### `builtin` package

| Class | Role |
|-------|------|
| `BuiltinBeeSpecies` | Static constants for the six MVP species definitions (Common, Meadow, Forest, Arid, Cultivated, Hardy) plus an `ALL` list. |
| `BuiltinBeeTraits` | Static constants for all MVP trait alleles (Slow/Normal/Fast productivity; Flowers/Cactus/Leaves flower type) plus an `ALL` list. |
| `BuiltinBeeMutations` | Static constants for the two MVP mutations (Meadow+Forest→Cultivated ~12%, Forest+Arid→Hardy ~8%) plus an `ALL` list. |
| `BuiltinBeeContent` | Single access point: wraps `BuiltinBeeSpecies`, `BuiltinBeeTraits`, and `BuiltinBeeMutations` with look-up methods (`findSpecies`, `findAllele`, `allMutations`) and delegates to `DefaultGenomeFactory`. |
| `BuiltinContentData` | Provides raw DTO representations of all built-in content for use by the JSON pipeline smoke tests. |
| `DefaultGenomeFactory` | Creates a purebred default `Genome` for a species using its definition's default trait alleles. |

### `species`, `habitat`, `products`, `frames` packages

| Class | Role |
|-------|------|
| `BeeSpeciesDefinition` | Immutable species record: id, species `Allele`, display name key, dominance, default trait allele pairs, optional `SpeciesHabitatDefinition`, optional `SpeciesVisualDefinition`. |
| `BuiltinProductionDefinitions` | Static map of species ID → `ProductionDefinition` for the six MVP species. Combs are the primary output for each species. |
| `BuiltinFrameModifiers` | Static map of item ID → `FrameModifier` for Basic, Mutation, and Productivity frames. |

### `data` package (DTOs)

Lightweight records used by the JSON parser and validator. All are plain Java with no game dependencies.

| Record | Role |
|--------|------|
| `SpeciesDefinitionData` | DTO for species JSON. |
| `TraitAlleleDefinitionData` | DTO for trait allele JSON; includes optional `values` map for multipliers. |
| `MutationDefinitionData` | DTO for mutation JSON. |
| `MutationResultModesData` | `partialChance` + `fullChance` sub-object from mutation JSON. |
| `ProductionDefinitionData` | DTO for production JSON; holds primary and optional secondary output lists. |
| `ProductionOutputData` | Single output entry: item id, chance, min/max count. |
| `SpeciesVisualData` | Optional texture/model ID pair for species rendering. |
| `TraitAllelePairData` | Pair of allele IDs for a default trait entry in a species definition. |

### `validation` package

| Class | Role |
|-------|------|
| `ContentValidator` | Validates DTOs for structural correctness (non-blank IDs, valid dominance strings) and referential integrity (parent/result species ids known, trait allele ids known). Returns `ContentValidationResult` with collected error messages. |
| `ContentValidationResult` | Holds pass/fail status and an ordered list of error strings. |

### `json` package

| Class | Role |
|-------|------|
| `ContentDataJsonParser` | Hand-written recursive-descent JSON parser (no external JSON library). Parses trait, species, mutation, and production JSON strings into DTOs, optionally running validation. |
| `ContentJsonParseException` | Thrown on malformed JSON or schema violations during parsing. |

### `loading` package

| Class | Role |
|-------|------|
| `ContentJsonLoader` | Orchestrates the full load pipeline: receives lists of `ContentDefinitionSource`, parses + validates each, converts DTOs to domain objects via `ContentConverter`, and merges into a `ContentRegistry`. Collects per-file errors; a bad file rejects only its own definitions. |
| `ContentDefinitionSource` | Value object: file path string + raw JSON string from the resource manager. |
| `ContentLoadResult` | Result of a full load: the merged `ContentRegistry` plus any error messages collected. |

### `conversion` package

| Class | Role |
|-------|------|
| `ContentConverter` | Converts validated DTOs to domain objects (`Allele`, `BeeSpeciesDefinition`, `MutationDefinition`, `ProductionDefinition`). Performs the final referential lookups against the in-progress registry. |
| `ContentConversionException` | Thrown when conversion fails (e.g. allele ID not found after validation). |

### `registry` package

| Class | Role |
|-------|------|
| `ContentRegistry` | Immutable runtime registry: maps of species, trait alleles, all alleles, mutations, and production definitions. Built from built-ins first; `withLoadedDefinitions()` returns a new registry with externally loaded content merged in. Duplicate IDs are rejected with a WARNING and throw. |

---

## common/gameplay

### `breeding` sub-package

| Class | Role |
|-------|------|
| `BeeBreedingOrchestrator` | Thin coordinator: calls `BreedingService.breed()` then `MutationService.evaluate()`; returns a `BeeBreedingOutcome`. |
| `BeeBreedingRequest` | Value object: two parent genomes, available mutation definitions, and a `GeneticRandom`. |
| `BeeBreedingOutcome` | Result: child `Genome`, whether mutation occurred, and the `MutationDefinition` that fired. |

### `analysis` sub-package

| Class | Role |
|-------|------|
| `BeeAnalysisService` | Produces a `BeeAnalysisReport` from a `Genome` (pure Java; no rendering). |
| `GenomeReport` | Generates a multi-line plain-text genome summary (platform-neutral; lines are rendered by NeoForge or tests). |
| `GeneReport` | Value object for a single chromosome's display data (active name, inactive name, purity). |

### `production` sub-package

| Class | Role |
|-------|------|
| `ProductionResolver` | Rolls production outputs for a bee from its `Genome`. Active species drives primary outputs; inactive species contributes secondary outputs at 15% reduced chance. Productivity allele applies a global multiplier. Frame production multiplier is accepted as an external parameter. |
| `ProductionDefinition` | Immutable: species id, list of primary `ProductionOutput`, list of secondary `ProductionOutput`. |
| `ProductionOutput` | Single output entry: item id (namespaced string), base chance, count. |
| `ProductionResult` | Resolved list of outputs that were generated this tick, plus the active/inactive/productivity allele IDs for logging. |
| `ProductivityModifier` | Maps productivity allele ID to a numeric multiplier (Slow 0.75×, Normal 1.0×, Fast 1.25×). |

### `frames` sub-package

| Class | Role |
|-------|------|
| `FrameModifier` | Immutable: mutation multiplier + production multiplier for one frame item. |
| `FrameModifiers` | Utility class: `combine(List<FrameModifier>)` returns a `CombinedFrameModifier` that multiplies all modifiers together. |

### `spawn` sub-package

| Class | Role |
|-------|------|
| `WildBeeSpawnService` | Selects the correct wild species for a spawning bee from biome tags, Y, and light level via `HabitatPredicate` matching. Falls back to Meadow with a WARNING if nothing matches. `createWildGenomeForHabitat()` is the primary entry point; legacy `createWildGenome(String category, ...)` methods are deprecated. |

---

## neoforge/blocks

### Apiary blocks

| Class | Role |
|-------|------|
| `GeneticApiaryBlock` | Block class extending `BeehiveBlock`. Returns a custom ticker that calls `GeneticApiaryBlockEntity.serverTick()` to run vanilla hive logic + production rolls. |
| `GeneticApiaryBlockEntity` | Extends `BeehiveBlockEntity`. Overrides `getType()` to use `curiousbees:genetic_apiary` (prevents NBT mis-mapping). On `addOccupant()`: if the entering bee had nectar, rolls production via `ProductionResolver`, inserts items into the 6-slot output inventory, and damages frames. Exposes `automationOutputView` (side: frame insert + output extract) and `outputExtractView` (DOWN face: extract-only) for hopper/pipe automation. |
| `AdvancedApiaryBlock` | Subclass of `GeneticApiaryBlock`. Returns `AdvancedApiaryBlockEntity` and opens `AdvancedApiaryMenu`. |
| `AdvancedApiaryBlockEntity` | Thin subclass of `GeneticApiaryBlockEntity`. Overrides `getType()` and `createMenu()` to bind to the advanced apiary registry entry. All logic is inherited. |
| `ApiaryExtensionBlock` | Planned extension block. Adds slots or capacity to an adjacent apiary. Backed by `ApiaryExtensionBlockEntity`. |
| `ApiaryExtensionBlockEntity` | Block entity for the Beehive Expansion Box; exposes `IItemHandler` per face for automation. |

### Species bee nests

| Class | Role |
|-------|------|
| `SpeciesBeeNestBlock` | Generic species nest parameterised by `speciesId` and `NestVariant`. Extends `BeehiveBlock`. Bee entry policy is enforced by `BeeSpeciesHiveTargetHandler`, not in the block itself. No per-species subclass needed. |
| `SpeciesBeeNestBlockEntity` | Block entity for species nests; used by vanilla bee AI for occupant storage. |
| `NestVariant` | Enum: `STANDARD`, `LOG`, `SURFACE`, `HANGING`. Controls visual form only; data is in JSON assets. |

### Centrifuge

| Class | Role |
|-------|------|
| `CentrifugeBlock` | Plain block; opens `CentrifugeMenu` and owns the `CentrifugeBlockEntity` ticker. |
| `CentrifugeBlockEntity` | Processes combs into item outputs. Slot layout: 1 comb input, 1 bottle input, 4 output slots. On each `serverTick()`: looks up `CentrifugeRecipe` in the recipe manager, increments `processingProgress`, calls `processBatch()` on completion (consumes input, rolls outputs, increments `honeyCounter`). Bottles honey via `tryBottle()` when a glass bottle is present and `honeyCounter > 0`. Honey overflow (counter capped at 5) is discarded silently at FINE level. |

### Supporting data classes

| Class | Role |
|-------|------|
| `BeeOccupantData` | Record: `speciesId` + `analyzed` boolean. Cached per stored bee in the apiary for client sync. |
| `ApiaryState` | Enum: `IDLE`, `PRODUCING`, `OUTPUT_FULL`. Computed from occupant count and output space. |

---

## neoforge/items

| Class | Role |
|-------|------|
| `CapturedBeeItem` | Abstract base for bee capture items. On `interactLivingEntity()`: reads the bee's genome and analyzed flag, stores as `CapturedBeeData` component, discards the bee entity. On `use()`: spawns a new `Bee` entity, restores genome and analyzed state from the component. Shows species name in tooltip if bee is analyzed. |
| `BeeJarItem` | Single-use capture: on release, shrinks the item stack to empty. |
| `BeeTransporterItem` | Reusable capture: on release, removes the `CAPTURED_BEE` component and returns the item. |
| `BeeAnalyzerItem` | Right-click on a bee: consumes 1 honeycomb (free if already analyzed or in creative), marks the bee analyzed via `BeeAnalysisStorage`, generates a `BeeAnalysisReport`, sends it to the player via `ShowAnalyzerReportPayload`. |
| `CuriousBeeSpeciesSpawnEggItem` | Custom spawn egg for a specific species. Spawning creates a vanilla `Bee` entity; `BeeSpawnEventHandler` assigns the correct genome on `EntityJoinLevelEvent`. |
| `CuriousBeesGuideItem` | Guide book item; opens `CuriousBeesGuideScreen` on use. |

### Frame items

Registered in `ModItems` as plain `Item` instances with durability. `Basic Frame` (64 durability), `Mutation Frame` (32), `Productivity Frame` (48). Effect applied by `GeneticApiaryBlockEntity` via `BuiltinFrameModifiers.BY_ID` lookup.

### Comb items

Five plain `Item` registrations: `meadow_comb`, `forest_comb`, `arid_comb`, `cultivated_comb`, `hardy_comb`. Used as primary production outputs and as centrifuge inputs.

---

## neoforge/menus + screens

| Menu | Screen | Block |
|------|--------|-------|
| `GeneticApiaryMenu` | `GeneticApiaryScreen` | Genetic Apiary |
| `AdvancedApiaryMenu` | (reuses GeneticApiaryScreen layout) | Advanced Apiary |
| `CentrifugeMenu` | `CentrifugeScreen` | Centrifuge |
| — | `BeeAnalyzerScreen` | Item interaction (no block) |
| — | `CuriousBeesGuideScreen` | Item interaction |

**`GeneticApiaryMenu`** — sets up frame slots (insert-only via GUI) and output slots (extract-only). Syncs `homedBeeCount`, `analyzedBeeCount`, and `ApiaryState` via `ContainerData`.

**`AdvancedApiaryMenu`** — extends `GeneticApiaryMenu`. Adds a virtual bee insertion slot. On `clicked()`: if a loaded `CapturedBeeItem` is dropped onto the bee slot, calls `GeneticApiaryBlockEntity.addOccupantFromCapture()` and consumes the item (or clears the component for reusable items).

**`CentrifugeMenu`** — slot 0 = comb input, slot 1 = bottle input, slots 2–5 = outputs (extract-only). `ContainerData` syncs `processingProgress`, `processingTotal`, `honeyCounter`.

**`BeeAnalyzerScreen`** — opened client-side by `ClientNetworkHandlers` on receipt of `ShowAnalyzerReportPayload`. Renders the `BeeAnalysisReport` fields (species, productivity, flower type, purity).

---

## neoforge/registration

| Class | Role |
|-------|------|
| `ModBlocks` | `DeferredRegister<Block>` for: `genetic_apiary`, `advanced_apiary`, `apiary_extension`, `centrifuge`, `meadow_bee_nest`, `forest_bee_nest`, `arid_bee_nest`, `forest_bee_log_nest`. |
| `ModItems` | `DeferredRegister<Item>` for: block items, 5 comb items, 3 frame items, `bee_jar`, `bee_transporter`, `bee_analyzer`, `curious_bees_guide`, 5 species spawn eggs. Also registers dispenser behaviors for spawn eggs. |
| `ModBlockEntities` | `DeferredRegister<BlockEntityType>` for: `genetic_apiary`, `advanced_apiary`, `apiary_extension`, `centrifuge`, `species_bee_nest`. |
| `ModMenuTypes` | `DeferredRegister<MenuType>` for: `genetic_apiary`, `advanced_apiary`, `centrifuge`. |
| `ModRecipes` | `DeferredRegister<RecipeType>` + `DeferredRegister<RecipeSerializer>` for `centrifuge`. |
| `ModDataComponents` | `DeferredRegister<DataComponentType>` for `CAPTURED_BEE` (`CapturedBeeData` Codec). |
| `ModTags` | Item tag `curiousbees:frames` used by the apiary `isItemValid` check. |
| `ModSounds` | `DeferredRegister<SoundEvent>` for `apiary_work`, `centrifuge_work`, `analyzer_use`. |
| `ModFeatures` | `DeferredRegister<Feature>` for `species_bee_nest` world feature. |
| `ModPoiTypes` | `DeferredRegister<PoiType>` for species bee nests (POI for vanilla bee AI). |
| `ModCreativeTabs` | Registers a Curious Bees creative tab containing all mod items. |

---

## neoforge/capabilities

| Class | Role |
|-------|------|
| `ApiaryCapabilities` | Registers `Capabilities.ItemHandler.BLOCK` for `GeneticApiaryBlockEntity` and `AdvancedApiaryBlockEntity`. Direction-aware: `DOWN` returns `outputExtractView()` (combs only, extract-only); any other side returns `automationOutputView()` (frame insert + output extract). Also registers `ApiaryExtensionBlockEntity`'s handler. |

---

## neoforge/data (attachments + codec + components)

| Class | Role |
|-------|------|
| `BeeGenomeAttachments` | Registers `AttachmentType<GenomeData>` (`bee_genome`) on `Bee` entities. Persisted via `GenomeCodec.GENOME`. Absent by default — callers must call `hasData()` before `getData()`. |
| `BeeGenomeStorage` | Static helpers wrapping `bee.getData(BEE_GENOME)` and `GenomeSerializer` to return `Optional<Genome>` / set genome on a `Bee`. |
| `BeeAnalysisAttachments` | Registers `AttachmentType<Boolean>` (`bee_analyzed`) on `Bee` entities. |
| `BeeAnalysisStorage` | Static helpers: `isAnalyzed(Bee)`, `setAnalyzed(Bee)`. |
| `GenomeCodec` | `Codec<GenomeData>` implementation used by the attachment serialization. |
| `CapturedBeeData` | Record used as a data component on capture items. Contains `GenomeData` + `boolean analyzed`. Serialized with a Codec registered in `ModDataComponents`. |

---

## neoforge/events

| Class | Role |
|-------|------|
| `BeeSpawnEventHandler` | Subscribes to `EntityJoinLevelEvent`. If a `Bee` joins without a genome, collects biome tag strings + Y + light from the platform, delegates to `WildBeeSpawnService.createWildGenomeForHabitat()`, and attaches the result. |
| `BeeBreedingEventHandler` | Subscribes to `BabyEntitySpawnEvent`. Resolves parent genomes via `BeeParentResolver`, calls `BeeBreedingOrchestrator.breed()`, stores the child genome, and spawns happy-villager particles + levelup sound on mutation. Respects the `BEE_POPULATION_CAP` config limit. |
| `BeeSpeciesHiveTargetHandler` | Subscribes to `EntityTickEvent.Post` (every 10 ticks per bee). If a bee's hive pos points to a `SpeciesBeeNestBlock` whose species ID does not match the bee's active species allele, clears the hive pos and sets a 400-tick stay-out countdown. |

---

## neoforge/network

| Class | Role |
|-------|------|
| `CuriousBeesNetwork` | Static helper: `sendAnalyzerReport()` sends `ShowAnalyzerReportPayload` to a `ServerPlayer`; `syncAnalyzedToTracking()` sends `SyncBeeAnalysisPayload` to all tracking clients. |
| `ShowAnalyzerReportPayload` | Carries a `BeeAnalysisReport` from server → client to open `BeeAnalyzerScreen`. |
| `SyncBeeGenomePayload` | Carries `GenomeData` from server → client for tracked bees (used when client needs genome for rendering or tooltip). |
| `SyncBeeAnalysisPayload` | Carries analyzed-flag update for tracked bees. |
| `ClientNetworkHandlers` | Client-only handlers that open screens or update client-side entity state on payload receipt. |

---

## Data pipeline (ContentJsonLoader)

Flow from JSON file to runtime registry:

```
1. ContentReloadListener.prepare()          [NeoForge thread, on AddReloadListenerEvent]
   └── discovers JSON files via ResourceManager.listResources()
       for paths: curious_bees/traits, curious_bees/species,
                  curious_bees/mutations, curious_bees/production
   └── wraps each file as ContentDefinitionSource(path, rawJson)

2. ContentJsonLoader.load()                 [pure Java, no Minecraft]
   └── for each DefinitionSource:
       a. ContentDataJsonParser.parse*()    → DTO (e.g. SpeciesDefinitionData)
       b. ContentValidator.validate*()      → ContentValidationResult
          (structural: non-blank IDs, valid dominance/chromosome strings)
          (referential: allele IDs exist, parent species IDs known)
       c. ContentConverter.convert()        → domain object (Allele, BeeSpeciesDefinition, …)
   └── errors collected per file; bad file rejected, others continue
   └── returns ContentLoadResult(registry, errors)

3. ContentReloadListener.apply()            [main thread]
   └── NeoForgeContentRegistry.apply(result)
       → atomically replaces the live ContentRegistry instance
   └── logs summary (species count, traits, mutations, production)
   └── logs WARNING per species with missing visual definition

4. NeoForgeContentRegistry.current()       [anywhere, after load]
   └── returns the live ContentRegistry used by all NeoForge code
```

Built-in content is always seeded first (`ContentRegistry.builtIn()`). External JSON may add species/traits/mutations/production but cannot override built-in IDs.

---

## Test structure

All tests live under `common/src/test/java/`. None import `net.minecraft.*` or `net.neoforged.*`.

Run with: `./gradlew :common:test`

### Genetics tests

| Test class | Coverage |
|-----------|----------|
| `AlleleTest` | Constructor validation, equality, toString |
| `GenePairTest` | Dominance resolution (dominant beats recessive; equal dominance is random), `isPurebred`, `isHybrid`, `restored()` deserialization path |
| `GenomeTest` | Construction validation, `withGenePair()` immutability, chromosome access |
| `BreedingServiceTest` | Each chromosome picks exactly one allele from each parent; incompatible chromosome sets throw |
| `MutationServiceTest` | Matching definitions apply at correct rates; partial vs full modes; null definition in list is skipped |
| `GeneticsSimulationTest` | Statistical simulation over many crosses — 50/50 allele contribution within tolerance |
| `GenomeSerializerTest` | Round-trip `Genome → GenomeData → Genome`; unknown allele IDs return empty; missing SPECIES returns empty |

### Content tests

| Test class | Coverage |
|-----------|----------|
| `BuiltinBeeSpeciesTest` | Six MVP species present; IDs are stable |
| `BuiltinBeeTraitsTest` | All MVP trait alleles present; dominance values correct |
| `BuiltinBeeMutationsTest` | Two MVP mutations present; parent/result IDs reference known species |
| `BuiltinBeeContentTest` | Lookup by ID works; `findAllele` covers both species and trait alleles |
| `BuiltinBeeSpeciesHabitatTest` | Each wild species has a `HabitatPredicate` with non-empty biome tags |
| `BeeSpeciesDefinitionTest` | Validates definition constraints |
| `DefaultGenomeFactoryTest` | Created genomes have correct species and trait alleles |
| `ContentDataJsonParserTest` | Valid JSON parses correctly; malformed JSON throws `ContentJsonParseException` |
| `ContentValidatorTest` | Structural and referential validation rules |
| `ContentConverterTest` | DTOs convert to domain objects; bad IDs throw |
| `ContentRegistryTest` | `withLoadedDefinitions()` merges correctly; duplicate IDs throw |
| `ContentJsonLoaderTest` | Valid content extends registry; invalid content rejected per file; duplicate built-in IDs silently skipped |
| `DatapackReloadSmokeTest` | Two consecutive `ContentJsonLoader.load()` calls produce identical registries |
| `ContentExampleFilesTest` | Every shipped JSON in `neoforge/.../curious_bees/` validates against its DTO schema and mirrors the Java built-ins |
| `CentrifugeRecipeDataTest` | Every shipped centrifuge recipe JSON is schema-valid and covers all 5 combs |
| `LangKeyCompletenessTest` | Every `Component.translatable(key)` call in Java has an entry in `en_us.json` |

### Gameplay tests

| Test class | Coverage |
|-----------|----------|
| `BeeBreedingOrchestratorTest` | Orchestrator produces a genome; mutation flag set correctly |
| `BeeBreedingRequestTest` | Value object construction |
| `BeeBreedingOutcomeTest` | `mutated()` and `inherited()` factories |
| `BeeAnalysisServiceTest` | Report fields populated from genome |
| `BeeAnalysisFormatterTest` | Formatted lines match genome values |
| `BeeAnalysisReportRedactionTest` | Internal allele IDs never leak into report strings |
| `ProductionResolverTest` | Active species produces primary outputs; inactive adds secondary at reduced rate |
| `ProductionModelTest` | `ProductionDefinition` construction and output list |
| `ProductivityModifierTest` | Slow/Normal/Fast multipliers |
| `ApiaryProductionBalancingTest` | Statistical balance over many rolls |
| `FrameModifierTest` | Frame multipliers combine correctly |
| `WildBeeSpawnServiceTest` | Correct species selected for biome tags; fallback to Meadow when no match |

### Architecture boundary test

| Test class | Coverage |
|-----------|----------|
| `ArchitectureBoundaryTest` | Scans all `common/` classes and asserts zero references to `net.minecraft.*` or `net.neoforged.*` |

---

## Key data flows

### 1. Breeding genome assignment

```
Vanilla BabyEntitySpawnEvent fires
  └── BeeBreedingEventHandler.onBabyBeeSpawned()
        └── BeeParentResolver.resolve(parentA/B) → Optional<Genome>
              (reads BEE_GENOME attachment; assigns Common fallback if missing)
        └── BeeBreedingOrchestrator.breed(BeeBreedingRequest)
              └── BreedingService.breed(genomeA, genomeB, random)
                    → for each ChromosomeType: pick 1 allele from each parent → GenePair
                    → returns BreedingResult(childGenome)
              └── MutationService.evaluate(parentA, parentB, childGenome, mutations, random)
                    → matches parents' active SPECIES alleles against MutationDefinition.parents
                    → rolls baseChance; on hit: PARTIAL replaces active allele, FULL replaces both
                    → returns MutationResult
        └── BeeGenomeStorage.setGenome(child, outcome.childGenome())
        └── if mutation occurred: spawn HAPPY_VILLAGER particles + PLAYER_LEVELUP sound
```

### 2. Hive production tick

```
Bee with nectar enters GeneticApiaryBlockEntity (via vanilla AI or addOccupantFromCapture)
  └── GeneticApiaryBlockEntity.addOccupant(bee)
        └── hadNectar = bee.hasNectar()
        └── super.addOccupant(bee)   [vanilla hive logic]
        └── if hadNectar && hasAnyOutputSpace():
              └── resolveOrAssignGenome(bee) → Genome  [fallback assigns Common]
              └── combinedFrameModifier()     → CombinedFrameModifier
                    (reads frame items in slots 0-2; looks up BuiltinFrameModifiers.BY_ID)
              └── rollProduction(genome, frameProductionMultiplier)
                    └── ProductionResolver.resolve(genome, productionDefinitions, random, multiplier)
                          → active species drives primary outputs
                          → inactive species contributes secondary at 15% chance
                          → productivity allele multiplies all chances
                          → returns ProductionResult(generatedOutputs)
              └── insertProductionResult(result)
                    → resolves item ID via BuiltInRegistries.ITEM
                    → inserts into outputInventory slots 0-5
              └── damageFrames(outputCount)   [increments frame damage; breaks at maxDamage]
```

### 3. Comb centrifuge processing

```
Player places comb in CentrifugeBlockEntity input slot (slot 0)
  └── CentrifugeBlockEntity.serverTick() called every game tick
        └── tryBottle()
              if honeyCounter > 0 && glass bottle in slot 1 && output space:
                insert honey_bottle, remove 1 bottle, decrement honeyCounter
        └── tickProcessing()
              └── RecipeManager.getRecipeFor(CENTRIFUGE_TYPE, comb) → CentrifugeRecipe
              └── processingProgress++ per tick until >= processingTotal
              └── processBatch(recipe)
                    → consume inputCount combs from slot 0
                    → for each WeightedOutput: roll chance → insertIntoOutput
                    → honeyCounter += recipe.honeyPortions() (capped at 5; overflow discarded)
                    → play CENTRIFUGE_WORK sound
```

### 4. Bee capture and release

```
CAPTURE (BeeJar or BeeTransporter):
  Player right-clicks a Bee entity
    └── CapturedBeeItem.interactLivingEntity()
          └── BeeGenomeStorage.getGenome(bee) — skip if no genome
          └── CapturedBeeData(GenomeSerializer.toData(genome), isAnalyzed)
          └── stack.set(CAPTURED_BEE, data)
          └── bee.discard()

RELEASE to world:
  Player right-clicks with loaded item (empty hand or air)
    └── CapturedBeeItem.use()
          └── GenomeSerializer.fromData(data.genome(), registry::findAllele) → Genome
          └── new Bee(EntityType.BEE, serverLevel); bee.setPos(player.position)
          └── BeeGenomeStorage.setGenome(bee, genome)
          └── if data.analyzed(): BeeAnalysisStorage.setAnalyzed(bee)
          └── level.addFreshEntity(bee)
          └── BeeJar: stack.shrink(1) | BeeTransporter: stack.remove(CAPTURED_BEE)

RELEASE to Advanced Apiary:
  Player drops loaded item onto bee insertion slot in AdvancedApiaryMenu
    └── AdvancedApiaryMenu.clicked(beeInsertSlotIndex, ...)
          └── GeneticApiaryBlockEntity.addOccupantFromCapture(data, serverLevel)
                → creates Bee entity, restores genome + analyzed state, calls addOccupant()
```
