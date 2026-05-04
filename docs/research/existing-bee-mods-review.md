# Existing Bee Mods Review

> Purpose: design reference for Curious Bees post-MVP direction.
> This document summarizes what each major bee mod gets right, what it gets wrong
> (for our goals), and what Curious Bees should take, avoid, or do differently.
>
> This is NOT a copying guide. Curious Bees must use original implementation,
> assets, content, and identity.

## 1. Forestry (Minecraft mod)

### What it gets right

```text
- Diploid genomes with active/inactive alleles — the inspiration for Curious Bees genetics.
- Dominance rules that create meaningful purebred vs hybrid distinctions.
- Probabilistic mutation with chance values per pair — makes genetics feel emergent.
- Hidden genetic potential: inactive alleles the player can breed toward.
- Detailed species tree with clear biological roles (Forest, Meadow, Modest, Cultivated...).
- Analyzer as a real gameplay gate before genetic details are visible.
- Species have distinct hive blocks (Bee Hive, Alveary) with environmental requirements.
- Production-focused: combs, propolis, honey, pollen — species output identity.
```

### What it gets wrong (for our goals)

```text
- Overly complex item system: queens, princesses, drones — breaks the vanilla bee fantasy.
- Bees are not living entities in the world; they are item-based inputs.
- Forestry bees die after production cycles (queen lifecycle) — adds friction Curious Bees avoids.
- Temperature and humidity requirements for production — too much environmental complexity early.
- Requires an Alveary or Apiarist's Chest before anything meaningful happens — high entry cost.
- Not compatible with vanilla bee mechanics at all.
- Very high content density: 40+ species before the player understands the system.
```

### What Curious Bees takes from it

```text
- Diploid genome model (already implemented).
- Active/inactive alleles + dominance (already implemented).
- Probabilistic mutation (already implemented).
- Analyzer as a gameplay gate (Phase 13).
- Species-specific production identity (Phase 16 onward).
- The idea of purebred vs hybrid progression value.
```

### What Curious Bees avoids

```text
- Item-based bees (queens, princesses, drones).
- Mandatory lifecycle/death mechanics.
- Temperature/humidity simulation.
- Large species trees before UX is ready.
```

---

## 2. Productive Bees (Minecraft mod)

### What it gets right

```text
- Custom bee entities per species: each bee is visually distinct and selectable.
- Modern automation-ready design from the start: apiaries support hoppers/pipes naturally.
- Clean GUI: apiary shows bee slots, output slots, progress clearly.
- Spawn eggs per species: bees are obtainable without depending on vanilla spawning luck.
- Block hive per species: world-gen hives per biome for discovery.
- No lifecycle/death by default: bees work continuously without dying.
- Low entry barrier: catch a bee, put it in an apiary, get outputs.
- Good mod compatibility layer: JEI integration, FTB, Create, etc.
- Extensible: addon packs can add species via datapacks.
```

### What it gets wrong (for our goals)

```text
- Genetics is shallow: most bees are deterministic recipe outputs (A + B = C always).
- No diploid genome, no active/inactive alleles, no dominance, no hidden traits.
- Mutation is scripted/recipe-based, not probabilistic.
- Purebred vs hybrid has no meaning — no genetic depth.
- Species feel like colored resource generators, not living genetics subjects.
- Many bees feel like items with wings.
- Visual identity is strong but genetic identity is weak.
```

### What Curious Bees takes from it

```text
- Automation-ready apiary design by default (no automation upgrade required).
- Clean, informative apiary GUI layout (Phase 14).
- Block hive per species for world discovery (Phase 11.5 — done).
- Low friction UX: bees should feel usable without requiring complex setup.
- Spawn egg per species is a future goal (Phase 11.6 deferred).
```

### What Curious Bees avoids

```text
- Deterministic genetics (A + B = C always).
- Shallow bee identity (resource generator with no hidden potential).
- Replacing living-bee fantasy with pure item throughput.
```

---

## 3. Complicated Bees (Minecraft mod)

### What it gets right

```text
- Uses vanilla bee entities as the base — closest to Curious Bees philosophy.
- Gene-based approach: bees carry modifiable data.
- Relatively simple to understand at the start.
- No major new item types required to begin playing.
```

### What it gets wrong (for our goals)

```text
- Gene system is less structured than diploid Mendelian genetics.
- No dominant/recessive allele concept.
- Mutation is less interesting — more like random stat drift than meaningful species change.
- Visual differentiation is limited.
- Less production depth.
- Smaller community; less design pressure to be well-rounded.
```

### What Curious Bees takes from it

```text
- Validation that using vanilla bee entities as a base is viable.
- Confirmation that genome attachment (not replacement) can work.
```

---

## 4. Vanilla Minecraft Bees

### What they get right

```text
- Living entity with familiar AI, animations, and behavior.
- Natural world gen hives (bee nests) in biome-appropriate locations.
- Player already knows how to interact with them: smoker, shears, bottle.
- Pollination mechanic: bees actively do something in the world.
- Beloved by players — strong positive association.
```

### What they get wrong (for our goals)

```text
- No genetic system at all.
- All bees are identical (no species differentiation).
- No production depth beyond honey and honeycomb.
- Hive has no species restriction — any bee can use any hive.
```

### What Curious Bees builds on

```text
- Uses vanilla bees as the living foundation (all Curious Bees species are vanilla bees
  with genome attachments today; custom entities are a deferred future phase).
- Extends vanilla hive mechanics rather than replacing them.
- Adds genetic depth on top of the familiar vanilla bee experience.
- Respects vanilla bee AI so players do not need to learn a new mob behavior.
```

---

## 5. Summary Comparison Table

| Feature                          | Forestry  | Productive Bees | Complicated Bees | Vanilla   | Curious Bees (target) |
|----------------------------------|-----------|-----------------|-----------------|-----------|----------------------|
| Living entity base               | ❌        | ✅ (custom)     | ✅ (vanilla)    | ✅        | ✅ (vanilla→custom)  |
| Diploid genetics                 | ✅        | ❌              | partial         | ❌        | ✅                   |
| Dominant/recessive alleles       | ✅        | ❌              | ❌              | ❌        | ✅                   |
| Probabilistic mutation           | ✅        | ❌              | partial         | ❌        | ✅                   |
| Hidden genetic potential         | ✅        | ❌              | ❌              | ❌        | ✅                   |
| Species-specific hive blocks     | ✅        | ✅              | ❌              | ✅        | ✅                   |
| No lifecycle/death               | ❌        | ✅              | ✅              | ✅        | ✅                   |
| Modern automation-ready apiary   | ❌        | ✅              | partial         | ❌        | ✅ (Phase 14)        |
| Polished analyzer UI             | partial   | partial         | ❌              | ❌        | ✅ (Phase 13)        |
| No temperature/humidity gate     | ❌        | ✅              | ✅              | ✅        | ✅                   |
| Spawn eggs per species           | n/a       | ✅              | ❌              | partial   | deferred (11.6)      |
| Data-driven species expansion    | ❌        | ✅              | ❌              | ❌        | ✅ (Phase 16)        |
| Visual identity per species      | ✅        | ✅              | partial         | ❌        | ✅ (Phase 12)        |

---

## 6. Key Design Decisions Informed By This Review

### Take from Forestry

```text
- Keep diploid genetics, dominance, and probabilistic mutation as the core.
- Keep analyzer as a meaningful gameplay gate before genetic data is visible.
- Keep the idea that purebred bees are more predictable and valuable than hybrids.
```

### Take from Productive Bees

```text
- Automation-ready apiary from the start (no automation upgrade required).
- Clear apiary GUI showing bees, frames, outputs, and progress.
- Species hive blocks per biome for natural discovery.
- Low entry friction: playing with bees should feel accessible.
```

### Keep from vanilla

```text
- Living bees remain the fantasy. Bees are not primarily items or machine inputs.
- Vanilla bee AI (pollination, hive-seeking, smoker pacification) should continue working.
- Vanilla interaction model (shears for honeycomb, bottle for honey, smoker for safety).
```

### Avoid from all

```text
- Item-only bees that replace the living entity (Forestry's queen/drone/princess system).
- Lifecycle/death as a core loop mechanic.
- Temperature/humidity gates in the early game.
- Deterministic A+B=C mutation with no genetic depth (Productive Bees).
- Large species trees before UX and pipeline are ready.
```

---

## 7. Status

This research is a snapshot to inform Phase 11 direction decisions.
It should be updated when:

```text
- a new major bee mod releases;
- design decisions change and a reference point is needed;
- Phase 17 (first expanded species branch) planning begins.
```
