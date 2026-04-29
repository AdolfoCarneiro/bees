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

### Vanilla Honey Production — Suppressed

The Genetic Apiary does **not** produce:
- honey level increments;
- honey bottles;
- vanilla honeycombs;
- any vanilla hive output.

When a bee with nectar enters the apiary, instead of incrementing the honey
level, the block entity runs the mod's production logic and places combs in
its output inventory.

### Production Logic

When a bee carrying nectar enters the apiary:
1. Read the bee's genome via `BeeGenomeStorage.getGenome(bee)`.
2. Run `ProductionResolver.resolve(genome, BuiltinProductionDefinitions.BY_SPECIES_ID, random)`.
3. Add generated `ProductionOutput` items to the apiary's output inventory.
4. Do not increment honey level.

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
Slot 0–5  — Output (combs produced)
Frame slots — Reserved for future Phase 7F; not implemented in first version.
```

Bee occupant list is tracked by the inherited BeehiveBlockEntity logic.

### GUI (First Version — Basic)

A minimal screen showing:
- Which bee species are currently inside the apiary (read from stored bee data + genome).
- The output inventory where players manually extract combs.

No genome detail, no production progress bar, no frame slots visible yet.
These are intentional deferrals — the GUI can be improved without
touching production logic.

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
- Polished Blockbench model (placeholder cube is acceptable)
- Complex GUI (progress bars, genome display, frame slots)
- Specific mod automation compat (Create, AE2, etc.)
- Vanilla honey bottle or honeycomb output
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
- Honey level logic must be overridden/suppressed in the block entity.
- The production intercept point is when a bee with nectar enters the hive
  (vanilla: `addOccupant` / `releaseAllOccupants` logic).
- A simple GUI screen + menu is needed for the first version.
- `BeeGenomeStorage` must be called from the block entity side — this is
  already platform-side code, so no architecture boundary is crossed.
