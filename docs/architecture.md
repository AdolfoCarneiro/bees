# Architecture

Single technical contract for Curious Bees. Consolidates the previous `architecture/02–05` specs.

> Product framing: [`project-guide.md`](project-guide.md). Locked technical choices: [`decisions.md`](decisions.md). Phase plan: [`roadmap.md`](roadmap.md). Tasks: [`TASKS.md`](TASKS.md).

## Index

1. [Architecture goal](#1-architecture-goal)
2. [Module layout](#2-module-layout)
3. [Data flow](#3-data-flow)
4. [Genome storage & serialization](#4-genome-storage--serialization)
5. [Genetics system](#5-genetics-system)
6. [Breeding & mutation](#6-breeding--mutation)
7. [Content data (species, traits, mutations, products)](#7-content-data-species-traits-mutations-products)
8. [Naming guidelines](#8-naming-guidelines)
9. [Testing strategy](#9-testing-strategy)
10. [Anti-patterns](#10-anti-patterns)

---

## 1. Architecture goal

Clean separation between:

1. pure genetics logic;
2. Minecraft-independent gameplay orchestration;
3. NeoForge-specific integration;
4. future Fabric-specific integration;
5. documentation-driven AI implementation.

The single most important rule:

```text
The genetics core must not depend on Minecraft, NeoForge, or Fabric APIs.
```

This makes the most complex logic fast and safe to test, and keeps a future Fabric port a **port**, not a fork.

---

## 2. Module layout

### 2.1 Recommended repository layout

```text
curious-bees/
├── Readme.md
├── CLAUDE.md
├── docs/
│   ├── project-guide.md
│   ├── architecture.md           (this file)
│   ├── requirements.md
│   ├── roadmap.md
│   ├── TASKS.md
│   ├── decisions.md
│   └── asset-generation-guidelines.md
├── common/   (pure Java)
│   └── src/{main,test}/java/
├── neoforge/ (NeoForge integration)
│   └── src/{main,test}/java/
└── fabric/   (placeholder until DR-010 is greenlit)
    └── src/{main,test}/java/
```

### 2.2 Conceptual modules

#### `common/genetics`

Pure Java. **No Minecraft imports.** Owns:

- alleles, gene pairs, chromosome types, genomes;
- dominance resolution;
- Mendelian inheritance;
- mutation evaluation;
- purebred/hybrid detection;
- `GeneticRandom` abstraction.

Example packages:

```text
com.curiousbees.common.genetics.model
com.curiousbees.common.genetics.breeding
com.curiousbees.common.genetics.mutation
com.curiousbees.common.genetics.random
```

Required minimum classes:

```text
Allele · Dominance · ChromosomeType · GenePair · Genome
BreedingService · BreedingResult
MutationDefinition · MutationService · MutationResult
GeneticRandom
```

#### `common/content`

Minecraft-independent definitions for species / traits / mutations / production. Owns built-in initial content and (post-ADR-0010) the **data DTOs**, **validation**, **conversion**, and **runtime registry** (`ContentRegistry`).

Example packages:

```text
com.curiousbees.common.content.species
com.curiousbees.common.content.traits
com.curiousbees.common.content.mutations
com.curiousbees.common.content.products
com.curiousbees.common.content.builtin
com.curiousbees.common.content.data
com.curiousbees.common.content.validation
com.curiousbees.common.content.conversion
com.curiousbees.common.content.registry
```

#### `common/gameplay`

Minecraft-adjacent orchestration with no direct loader APIs:

- `BeeBreedingOrchestrator`;
- analyzer report generation;
- spawn genome selection rules;
- production resolver;
- environment abstraction usage.

#### `common/platform`

Interfaces platform modules implement (no Minecraft classes):

```java
public interface BeeGenomeStorage<B> {
    Optional<Genome> getGenome(B bee);
    void setGenome(B bee, Genome genome);
}

public interface EnvironmentContext {
    String biomeId();
    String dimensionId();
    boolean isRaining();
    boolean isDay();
}
```

#### `neoforge/`

NeoForge-specific implementation. Owns mod init, deferred registers, entity attachments, item data components, events, commands, items/blocks, networking, client rendering, resource/data loading.

#### `fabric/`

Placeholder until DR-010 is greenlit. When implemented, must reuse `common/*` services with no genetics logic in Fabric mixins.

---

## 3. Data flow

### 3.1 Wild bee spawn

```text
1. Minecraft spawns a Bee entity.
2. NeoForge spawn handler runs.
3. If the bee already has a genome → skip.
4. Otherwise, derive species candidates from biome/context.
5. Create a default wild genome (mostly purebred).
6. Store the genome on the bee entity (attachment).
```

### 3.2 Bee breeding

```text
1. Two bees enter love mode (vanilla).
2. Minecraft creates the baby bee.
3. NeoForge breeding hook identifies parents + child.
4. Read parent genomes (initialize fallback if missing — never crash).
5. Pass parents + EnvironmentContext + GeneticRandom to BreedingService.
6. BreedingService produces inherited child genome.
7. MutationService evaluates possible mutation, may modify species chromosome.
8. Final child genome is stored on the baby (attachment).
9. Optional subtle feedback (particles, sound, advancement) on mutation.
```

Event handlers stay **thin**; the core never sees `Bee`.

### 3.3 Analyzer

```text
1. Player uses Analyzer item on a bee.
2. NeoForge interaction reads the bee's genome.
3. Common analyzer service produces an AnalyzerReport (no Minecraft).
4. NeoForge renders it (chat, tooltip, screen).
```

### 3.4 Production (Genetic Apiary)

```text
1. A bee with nectar enters the apiary (vanilla addOccupant path).
2. After super.addOccupant, the block entity reads the bee genome.
3. ProductionResolver.resolve(species, traits, frame modifiers, random) → outputs.
4. Outputs are pushed into the apiary output inventory (extract-only via IItemHandler).
5. Vanilla honey fill still happens; mod production is additive.
```

(See [`decisions.md` → ADR-0009](decisions.md) for the apiary contract.)

---

## 4. Genome storage & serialization

### 4.1 Storage (NeoForge)

Use NeoForge **entity data attachments** (`AttachmentType<GenomeData>`). The attachment is registered per-mod, serialized via a `Codec`, automatically saved/loaded with the entity NBT, and never silently rerolls active/inactive on load.

Read/write API:

```java
GenomeData data = bee.getData(BeeGenomeAttachments.BEE_GENOME); // null if absent
boolean has = bee.hasData(BeeGenomeAttachments.BEE_GENOME);
bee.setData(BeeGenomeAttachments.BEE_GENOME, data);
```

Client sync: not required by default. Add when an analyzer screen or HUD needs it (see ADR-003).

### 4.2 Serialization shape

`GenomeData` is platform-neutral (no Minecraft types):

```java
record GenePairData(
    String firstAlleleId,
    String secondAlleleId,
    String activeAlleleId,
    String inactiveAlleleId
) {}

record GenomeData(
    Map<String, GenePairData> chromosomes // key = ChromosomeType.name()
) {}
```

`GenePair` exposes a **package-private** `restored(...)` factory used **only** by deserialization, so saved active/inactive state is preserved verbatim. Public construction always resolves via `GeneticRandom`.

A `GenomeSerializer` (NeoForge or neutral adapter) converts `Genome ↔ GenomeData` and looks up alleles by stable ID through the content registry. Unknown allele IDs **log a WARNING and skip** (or return null for the genome) — never crash.

### 4.3 Persistence behavior

| Scenario | Behavior |
|---|---|
| Bee has genome, world saved | Serialized to NBT via Codec |
| World loaded, bee entity loaded | Deserialized; active/inactive restored |
| Bee has no genome | `getData` returns null; code must check `hasData` |
| Allele ID unknown on load | Log WARNING, return empty/null; do not crash |
| Bee killed and respawns | New entity, new genome assignment at spawn |

### 4.4 Fabric (future)

Use Fabric API **Data Attachments** with a `Codec` over the **same** `GenomeData`. The common `GenomeSerializer` is reused. (See [`decisions.md` → DR-010](decisions.md).)

---

## 5. Genetics system

### 5.1 Core terms

- **Genome** — full set of chromosomes for one bee.
- **Chromosome** — category of genetic information.
- **Allele** — single value inside a chromosome (has stable ID + dominance).
- **Gene Pair** — two alleles for one chromosome plus the resolved active/inactive pair.
- **Active / Inactive** — which allele is expressed; resolved once at creation, persisted.
- **Purebred** — both alleles equal. **Hybrid** — alleles differ.

### 5.2 Chromosome types

MVP:

```text
SPECIES · LIFESPAN · PRODUCTIVITY · FERTILITY · FLOWER_TYPE
```

Future (do not implement until explicitly scoped):

```text
TEMPERATURE_TOLERANCE · HUMIDITY_TOLERANCE · TERRITORY · EFFECT
NOCTURNAL · CAVE_DWELLING · WEATHER_TOLERANCE
```

### 5.3 Dominance & resolution

Initial: `DOMINANT`, `RECESSIVE`. Future: `INCOMPLETE_DOMINANT`, `CO_DOMINANT` (not implemented).

```text
If one allele is dominant and the other recessive:
    active = dominant
    inactive = recessive

If both alleles share dominance:
    active = random allele
    inactive = the other
```

Resolved once and **persisted**. Do not recalculate on each read.

### 5.4 Mendelian inheritance

For each chromosome:

```text
childAlleleA = random allele from parent A's gene pair
childAlleleB = random allele from parent B's gene pair
childGenePair = GenePair.resolve(childAlleleA, childAlleleB, random)
```

Each parent contributes exactly one allele per chromosome; selection is 50/50.

### 5.5 Allele identity

Stable IDs, never display strings:

```text
curious_bees:species/meadow
curious_bees:productivity/fast
curious_bees:fertility/three
curious_bees:flower_type/flowers
```

### 5.6 Hybrid detection

- Species hybrid — species gene pair differs.
- Trait hybrid — any trait gene pair differs.
- Overall hybrid — at least one relevant chromosome is hybrid.

### 5.7 Randomness

```java
public interface GeneticRandom {
    double nextDouble();
    boolean nextBoolean();
    int nextInt(int bound);
}
```

Tests inject deterministic sequences; statistical tests use tolerances over many crosses (e.g. 10k runs, 25/50/25 within bucket tolerance).

---

## 6. Breeding & mutation

### 6.1 Player flow

```text
1. Find two bees.
2. Feed each a valid breeding item (vanilla flowers MVP).
3. Bees enter love mode → vanilla creates a baby.
4. Mod assigns the baby a genome from both parents.
5. Subtle feedback if mutation occurred.
6. Player inspects with analyzer later.
```

### 6.2 System flow

```text
1. Platform breeding hook identifies parents + child.
2. Read parent genomes (fallback if missing).
3. BreedingService(parentA, parentB, GeneticRandom) → inherited child genome.
4. MutationService(inherited, parentA, parentB, EnvironmentContext, mutations, random) → final genome.
5. Store on baby.
6. Optional feedback (particles/sound/advancement).
```

### 6.3 Inheritance algorithm

```text
for each chromosomeType:
    aGene = parentA.get(type); bGene = parentB.get(type)
    fromA = random.pick(aGene.alleleA, aGene.alleleB)
    fromB = random.pick(bGene.alleleA, bGene.alleleB)
    childGenome.set(type, GenePair.resolve(fromA, fromB, random))
```

### 6.4 Mutation timing

After inheritance:

```text
1. Generate inherited child genome.
2. Evaluate mutation rules (parents + environment).
3. If mutation occurs, modify species chromosome.
4. Re-resolve species active/inactive if needed.
5. Return final genome.
```

### 6.5 Mutation definition

```json
{
  "id": "curious_bees:cultivated_from_meadow_forest",
  "parents": ["curious_bees:meadow", "curious_bees:forest"],
  "result": "curious_bees:cultivated",
  "baseChance": 0.12,
  "allowedBiomes": ["minecraft:plains", "minecraft:forest", "minecraft:flower_forest"],
  "resultModes": { "partialChance": 0.95, "fullChance": 0.05 }
}
```

Parent matching is **order-independent**. For MVP, mutation considers each parent's **active** species. Future: also consider inactive alleles.

### 6.6 Mutation chance formula

```text
finalChance = baseChance * environmentMultiplier * frameMultiplier * otherModifiers
```

If a required biome is not present, `chance = 0`.

### 6.7 Partial vs full

- **Partial (~95%)** — one allele becomes the result species. Example: `Meadow / Forest` + mutation `Cultivated` → `Cultivated / Forest`.
- **Full (~5%)** — both alleles become the result. Example: → `Cultivated / Cultivated`.

Numbers are placeholders until balance pass.

### 6.8 Missing genome handling

Possible cases: pre-existing world bees, bees from other mods, deserialization failure. Behavior: assign biome-appropriate fallback wild genome, log WARNING, continue. Never crash.

### 6.9 Acceptance criteria

```text
- Two bees with genomes can produce a child genome.
- Each chromosome inherits one allele from each parent.
- Active/inactive are resolved and persisted.
- A mutation can occur after inheritance.
- Partial and full mutation results both supported.
- Mutation rules can be unit-tested without Minecraft.
- NeoForge integration assigns the final genome to the baby.
- Missing parent genomes are handled safely.
```

---

## 7. Content data (species, traits, mutations, products)

### 7.1 Philosophy

- **Start small** — MVP species set is fixed at the five below.
- **Avoid resource bees early** — gated by [`decisions.md` → ADR-0007 / 0012](decisions.md).
- **Species have identity** — environment, trait tendencies, production identity, mutation role, progression position. Avoid species that differ only by output item.
- **Production supports hybrids** — active species drives primary output, inactive species can contribute secondary output.
- **Built-ins first, JSON later** — built-ins always present; data-driven extends, never overrides (unless explicitly allowed).

### 7.2 Initial species (MVP)

| Species | Role | Spawn context | Default trait gist | Dominance |
|---------|------|----------------|--------------------|-----------|
| **Meadow** | Starter wild generalist | Plains / flower forest / fallback | Normal/Normal/Two/Flowers | Dominant |
| **Forest** | Starter wild forest progression | Forest / birch / dark forest | Normal/Normal/Two/Leaves | Dominant |
| **Arid** | Starter wild dry biomes | Desert / savanna / badlands | Normal/Slow→Normal/One→Two/Cactus | Recessive |
| **Cultivated** | First mutation result (Meadow + Forest, ~12%) | n/a | Normal/Fast/Two/Flowers | Dominant |
| **Hardy** | Environmental mutation (Forest + Arid, ~8%) | n/a | Long/Normal/Two/Flowers or Cactus | Recessive |

Mutation tree:

```text
Meadow ─┐
        ├── Cultivated   (~12%)
Forest ─┘

Forest ─┐
        ├── Hardy        (~8%)
Arid ───┘
```

### 7.3 Traits

| Trait | Values | MVP gameplay |
|-------|--------|---------------|
| **Lifespan** | Short / Normal / Long | Displayed; future: tech-apiary cycles. |
| **Productivity** | Slow (0.75x) / Normal (1.00x) / Fast (1.25x) | Multiplies production rate/chance. |
| **Fertility** | One / Two / Three | Displayed; future: extra larvae in tech apiaries. |
| **Flower Type** | Flowers / Cactus / Leaves | Displayed; future: gates breeding/production environment. |

Multipliers are **placeholders** until a balance pass.

### 7.4 Production

Recommended rule:

```text
Active species → primary output.
Inactive species → secondary output (smaller chance).
Productivity trait → output multiplier.
```

Example:

```text
Bee: Cultivated / Forest
Primary:   Cultivated Comb
Secondary: Forest by-product (small chance)
```

MVP product set: `Honeycomb`, `Meadow Comb`, `Forest Comb`, `Arid Comb`, `Cultivated Comb`, `Hardy Comb`, `Wax`. Subset acceptable during implementation.

### 7.5 Analyzer display

#### Field visibility table (E1-T05 audit)

| Field | Before analysis | After analysis |
|-------|----------------|----------------|
| Species active allele (display name) | hidden | shown |
| Species inactive allele (display name) | hidden | shown |
| Species purity (purebred / hybrid) | hidden | shown |
| Lifespan active + inactive + dominance | hidden | shown |
| Productivity active + inactive + dominance | hidden | shown |
| Fertility active + inactive + dominance | hidden | shown |
| Flower type active + inactive + dominance | hidden | shown |
| Raw genome / internal allele IDs | never shown | never shown |

**Rules:**
- "Before analysis" = `BeeAnalysisReport.unknown()` — all `GeneReport` fields carry `UNKNOWN_ID`; UI must not reveal any gene data.
- "After analysis" = `BeeAnalysisReport.analyzed(...)` — each `GeneReport` exposes `activeId`, `inactiveId`, `activeDominance`, `inactiveDominance`, `isPurebred`.
- Display names for species and traits are resolved from the content registry; internal IDs are never shown directly.
- The `isAnalyzed()` flag controls which report variant is sent to the client; the client receives the redacted or full report — it never receives both.

After analysis, the report shows species + purity + each trait pair, with dominance indicators:

```text
[D] Dominant   [R] Recessive
[A] Active     [I] Inactive

Species:
  [A][D] Cultivated
  [I][D] Forest
Lifespan:
  [A][D] Normal   [I][R] Long
...
```

Before analysis: identity is gated; tooltips/chat must not leak post-analysis fields.

### 7.6 Data-driven content (post-ADR-0010)

Recommended layout:

```text
data/curious_bees/
├── species/<id>.json
├── traits/<chromosome>/<value>.json
├── mutations/<id>.json
└── production/<species>.json
```

Pipeline (pure Java, common module):

```text
parse JSON → structural validation → referential validation → conversion → ContentRegistry.merge
```

Rules:

- Built-ins always registered first.
- External content **may not** override built-ins unless allowed.
- Duplicate IDs in the same source are rejected with a clear error.
- Errors are collected and reported per definition (file path + ID); a bad file rejects only its own definitions.

The NeoForge platform layer subscribes to `AddReloadListenerEvent` and triggers a reload through the same pipeline. Fabric uses the same pipeline through its resource API.

### 7.7 Naming guidelines

- Short, readable in tooltips, thematic but not opaque, distinct from MVP and from major existing bee mods.
- For **resource-adjacent** future content, prefer thematic names with player-facing explanation (e.g. `Ferric` → iron association). Direct names (`Iron Bee`) are allowed only when clarity outweighs identity.
- Detailed name strategy by category lives in [`decisions.md` → ADR-0011](decisions.md).

### 7.8 Acceptance criteria (content design)

```text
- Five MVP species defined.
- Two MVP mutations defined.
- MVP trait values defined.
- Wild spawn contexts defined.
- Basic product concepts defined.
- Content centralized (no scattered species ifs).
- Future JSON conversion remains possible.
```

---

## 8. Naming guidelines

Use clear over clever:

```text
Good:  BreedingService · MutationService · Genome · GenePair
       BeeSpeciesDefinition · MutationDefinition · AnalyzerReport
Avoid: BeeMagicManager · GeneticStuff · MutationThing · ForestryCompatSomething
```

---

## 9. Testing strategy

### 9.1 Unit (`common`)

- dominance resolution;
- Mendelian inheritance;
- hybrid/purebred detection;
- mutation probability;
- active/inactive persistence;
- invalid content definitions;
- approximate distributions over many simulations.

### 9.2 Integration (`neoforge`) / manual validation

- bee spawn receives genome;
- bee genome persists after save/load (and active/inactive identity is preserved);
- bee breeding event assigns child genome;
- analyzer reads genome and reports correctly;
- production resolver behaves per species/traits;
- apiary subclass quirks (`getType()` and ticker overrides per ADR-0009) survive refactors.

### 9.3 Multiplayer smoke

Dedicated server checklist: spawn → breed → analyze → hive insert → output extract. State that drives client UI must round-trip correctly.

---

## 10. Anti-patterns

Do not:

- implement genetics directly inside an event handler;
- store raw unstructured NBT everywhere;
- hardcode species checks across many classes;
- start with resource bees;
- start with GUI before analyzer logic exists;
- start with JSON loading before definitions stabilize;
- mix NeoForge and future Fabric logic in the same classes;
- let AI agents generate large unrelated systems in one pass;
- treat short backlog text as a substitute for implementation context.

_Last updated: 2026-05-04._
