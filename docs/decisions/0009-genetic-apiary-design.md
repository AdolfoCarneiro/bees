# ADR-0009 — Genetic Apiary First Version Design

## Status

Accepted

## Context

Phase 7 introduces the first controlled beekeeping block.

The project already has:
- vanilla breeding integration (living bees, flowers, inheritance, mutation);
- genome persistence on all Bee entities;
- BeeAnalyzer item for inspection;
- ProductionResolver with built-in species definitions;
- five comb items registered.

The spec (docs/implementation/07-tech-apiary-and-automation.md, section 7A)
requires a design decision before any code is written.

An earlier draft considered a "Caught Bee item" model (bees stored as items
with genome NBT). That approach was rejected after design review because it
creates unnecessary friction — the player would need a capture tool before
the apiary could be used at all, and it diverges from the vanilla bee fantasy.

---

## Decision

### Core Philosophy

The Genetic Apiary is a **production block**, not a breeding machine.

Breeding continues to happen naturally in the world — two bees, flowers,
vanilla mechanics, genetic inheritance via Curious Bees. The apiary gives
players a controlled, automatable place for bees to live and produce combs.

### Bee Entry — Vanilla-Style Housing

Bees recognize the Genetic Apiary as a valid home automatically.
They choose it, enter it, and leave it on their own — exactly as they do
with vanilla BeeHive and Beehive blocks.

No special capture item is required. No explicit "assign bee" mechanic.

Implementation: `GeneticApiaryBlock` extends `BeehiveBlock`.
`GeneticApiaryBlockEntity` extends `BeehiveBlockEntity`.
Vanilla bee AI pathfinding already supports custom BeehiveBlock subclasses.

### NeoForge Implementation Notes (7C Addendum)

`BeehiveBlockEntity` internally uses `BlockEntityType.BEEHIVE` in its
constructor. For a subclassed apiary block entity, this requires two explicit
overrides to keep vanilla behavior and correct persistence:

1. `GeneticApiaryBlockEntity#getType()` returns
   `ModBlockEntities.GENETIC_APIARY.get()` so NBT save/load uses
   `curiousbees:genetic_apiary` instead of `minecraft:beehive`.
2. `GeneticApiaryBlock#getTicker(...)` is overridden to use
   `createTickerHelper(type, ModBlockEntities.GENETIC_APIARY.get(), BeehiveBlockEntity::serverTick)`,
   because vanilla ticker wiring checks `BlockEntityType.BEEHIVE` equality.

This avoids mixins while preserving vanilla bee processing (`instanceof
BeehiveBlockEntity`) and proper custom block entity serialization.

### Vanilla Beehive Parity (Crafted Hive)

The Genetic Beehive is the mod’s **crafted hive** analog to `minecraft:beehive`:

- Same placement rules, block properties (`Properties.ofFullCopy(Blocks.BEEHIVE)`), `facing`, and `honey_level` blockstate variants.
- Vanilla honey progression (level 0–5), honey bottle and honeycomb harvest with shears/bottle, and bee occupancy behavior come from `BeehiveBlock` / `BeehiveBlockEntity` ticking.
- World assets reuse vanilla beehive textures so the block reads like the vanilla crafted hive.
- Recipe matches the vanilla beehive pattern (planks + honeycomb), producing `curiousbees:genetic_apiary`.

Species-specific wild nests remain separate (`SpeciesBeeNestBlock`); the Genetic Beehive has **no species filter** — all bees may enter.

### Curious Bees Production (Additive)

When a bee carrying nectar enters the hive, **after** `super.addOccupant`, the block entity may also run mod production:

1. Read the bee's genome via `BeeGenomeStorage.getGenome(bee)`.
2. Run `ProductionResolver.resolve(...)` with frame modifiers.
3. Add generated outputs to the apiary output inventory.

This is **in addition to** vanilla honey fill where applicable, not instead of it.

### No-Genome Fallback

With the mod running, every bee in the world should already have a genome
assigned at spawn by `BeeSpawnEventHandler`. However, if a bee enters the
apiary without a genome (edge case: pre-existing world bees, other mod
interactions), the apiary will:
1. Assign a biome-appropriate fallback genome (same logic as spawn handler).
2. Log a WARNING.
3. Continue with production using the assigned genome.

No bee is silently skipped. No crash occurs.

### Inventory Layout

```
Slots — Vanilla hive occupancy + honey via BeehiveBlockEntity (unchanged).
Slots — Frames + outputs exposed through GeneticBeehive menu / automation capability (NeoForge).
```

Bee occupant list remains tracked by the inherited BeehiveBlockEntity logic.

### GUI

Right-click with an **empty hand** opens the Genetic Beehive menu (shears / glass bottle still use vanilla harvest on `useItemOn`).

The screen shows frame slots, extract-only output slots, synced vanilla honey level, and a simple bees-present indicator. Deeper genome readouts can be layered in later without changing housing behavior.

### Energy

None. The apiary runs without any power requirement.

### Automation

Basic `IItemHandler` capability on the output slots.
Output slots: extract only (no insert from automation).
Manual extraction from GUI also works.

No input slots for automation — bees enter on their own.

---

## Out of Scope for First Version

```
- Caught Bee items or any bee capture mechanic
- Breeding inside the apiary
- Frame items and frame modifier effects
- Energy or power system
- Centrifuge
- Resource bees
- Fabric support
- Polished custom hive texture set (vanilla beehive textures are acceptable for parity)
- Complex GUI (progress bars, full genome display)
- Specific mod automation compat (Create, AE2, etc.)
```

---

## Alternatives Considered

### Caught Bee Item Model
Rejected. Requires a capture tool before the apiary is usable.
Diverges from the vanilla bee feel. Living bees entering voluntarily
is simpler for the player and already supported by vanilla AI.

### Modify Vanilla BeeHive Instead of New Block
Rejected. Mixins into vanilla block entities are fragile. A subclass
is cleaner, more maintainable, and avoids breaking other mods that
interact with vanilla hives.

### Apiary Does Both Breeding and Production
Deferred. Breeding happens naturally in the world. The apiary handles
production in the first version. A combined mode can be designed later
once both systems are stable independently.

---

## Consequences

- `GeneticApiaryBlock` and `GeneticApiaryBlockEntity` must extend vanilla
  hive classes, not be standalone blocks.
- Honey level logic stays on the inherited `BeehiveBlockEntity` tick path (not suppressed).
- The production intercept point is when a bee with nectar enters the hive
  (vanilla: `addOccupant` / `releaseAllOccupants` logic).
- A simple GUI screen + menu is needed for the first version.
- `BeeGenomeStorage` must be called from the block entity side — this is
  already platform-side code, so no architecture boundary is crossed.
