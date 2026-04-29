# ADR-003 — NeoForge Bee Genome Storage

## Status

Accepted

## Context

Phase 3 requires vanilla `Bee` entities to carry, persist, and expose a `Genome` object across world save/load cycles in NeoForge 1.21.1.

The genetics core (`Genome`, `GenePair`, `Allele`) is pure Java and must stay free of Minecraft and NeoForge imports.

The storage mechanism must:

- persist through world save/load;
- survive entity unload/reload;
- be safe for existing bees without genome data;
- preserve the resolved active/inactive allele identity without rerolling it;
- keep platform-specific code out of `common/genetics` and `common/content`.

---

## Decision

### Storage Mechanism: NeoForge AttachmentType

Use NeoForge 1.21.1 **entity data attachments** (`AttachmentType<T>`) to store genome data on vanilla `Bee` entities.

This is the NeoForge-recommended mechanism for attaching custom persistent data to existing entities without subclassing.

#### Registration

```java
DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
    DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, MOD_ID);

DeferredHolder<AttachmentType<?>, AttachmentType<GenomeData>> BEE_GENOME =
    ATTACHMENT_TYPES.register("bee_genome",
        () -> AttachmentType.builder(() -> null)   // null = no genome by default
                            .serialize(GenomeData.CODEC)
                            .build());
```

#### Read/Write API

```java
// read (returns null if absent)
GenomeData data = bee.getData(BeeGenomeAttachments.BEE_GENOME);

// check
boolean has = bee.hasData(BeeGenomeAttachments.BEE_GENOME);

// write
bee.setData(BeeGenomeAttachments.BEE_GENOME, data);
```

#### Persistence

Passing `.serialize(Codec<T>)` to `AttachmentType.builder` makes NeoForge automatically read/write the attachment to the entity's NBT on save and load. No custom save/load event handling required.

#### Client Sync

**Not required for MVP.**

The `Genome` is only used server-side for breeding and debug inspection in Phase 3–4. The Analyzer item (Phase 5) will require the genome on the client; sync can be added then via a network packet or by adding `.syncWith(...)` to the attachment builder.

---

## Serialization Strategy

### Problem: GenePair Active/Inactive Must Be Preserved

The `GenePair` constructor currently resolves active/inactive using `GeneticRandom`. If deserialization simply reconstructs `GenePair(first, second, random)`, the active allele may differ from what was saved.

This must not happen. A bee that was `Meadow (active) / Forest (inactive)` before save must be exactly the same after load.

### Solution: Serialized Form Stores All Four Allele IDs

Introduce a platform-neutral data record:

```java
record GenePairData(
    String firstAlleleId,
    String secondAlleleId,
    String activeAlleleId,
    String inactiveAlleleId
) {}

record GenomeData(
    Map<String, GenePairData> chromosomes   // key = ChromosomeType.name()
) {}
```

`GenomeData` holds only stable string IDs — no Minecraft types, no domain objects.

### Solution: GenePair Needs a Deserialization Constructor

The current `GenePair` always calls `GeneticRandom`. To reconstruct a saved pair without rerolling:

Add a **package-private static factory method** to `GenePair` in the core:

```java
// In GenePair.java — for deserialization only
static GenePair restored(Allele first, Allele second, Allele active, Allele inactive) {
    return new GenePair(first, second, active, inactive);
}

private GenePair(Allele first, Allele second, Allele active, Allele inactive) {
    // direct assignment — no random resolution
}
```

This keeps the public API clean while allowing the serialization adapter to reconstruct exact state.

### Serializer Adapter

Introduce `GenomeSerializer` in `neoforge/` (or in a neutral adapter package if no Minecraft imports are needed):

```java
GenomeData toData(Genome genome)           // Genome -> serializable form
Genome fromData(GenomeData data)           // serializable form -> Genome
                                           // looks up Alleles by ID from BuiltinBeeContent
```

`GenomeSerializer.fromData` uses `BuiltinBeeContent.findSpecies/findTrait` to resolve alleles by stable ID.

Unknown allele IDs must fail with a clear logged warning rather than a crash in production.

### Codec

```java
// GenePairData codec
Codec<GenePairData> GENE_PAIR_CODEC = RecordCodecBuilder.create(i -> i.group(
    Codec.STRING.fieldOf("first").forGetter(GenePairData::firstAlleleId),
    Codec.STRING.fieldOf("second").forGetter(GenePairData::secondAlleleId),
    Codec.STRING.fieldOf("active").forGetter(GenePairData::activeAlleleId),
    Codec.STRING.fieldOf("inactive").forGetter(GenePairData::inactiveAlleleId)
).apply(i, GenePairData::new));

// GenomeData codec
Codec<GenomeData> CODEC = RecordCodecBuilder.create(i -> i.group(
    Codec.unboundedMap(Codec.STRING, GENE_PAIR_CODEC)
        .fieldOf("chromosomes")
        .forGetter(GenomeData::chromosomes)
).apply(i, GenomeData::new));
```

---

## Alternatives Considered

### Alternative A: Store Genome directly via NBT CompoundTag manually

Rejected. Requires NeoForge-specific serialization code mixed with domain logic, and does not benefit from Codec-based validation.

### Alternative B: Store genome as JSON string in a single NBT tag

Rejected. Fragile, hard to validate, harder to evolve.

### Alternative C: Use a custom Bee entity subclass

Rejected. Requires mixins or entity replacement, breaks compatibility with other mods, and is far more complex than attachments.

### Alternative D: Store only allele IDs without active/inactive

Rejected. On reload, active/inactive would be rerolled via random, changing genome identity. This would break bee consistency across sessions.

---

## Persistence Behavior

| Scenario | Expected Behavior |
|---|---|
| Bee has genome, world saved | Genome serialized to NBT via Codec |
| World loaded, bee entity loaded | Genome deserialized, active/inactive restored |
| Bee has no genome | `getData` returns null; code must check `hasData` |
| Allele ID unknown on load | Log WARNING, return `Optional.empty()` or null; do not crash |
| Bee killed and respawns | New entity, new genome assignment at spawn |

---

## Required Core Change

`GenePair` in `common/genetics` needs a **private constructor** and a **package-private static factory** for deserialization. This is a small, targeted change to Phase 1 code.

This change must:
- not break any existing tests;
- not expose a public API for bypassing random resolution;
- be clearly documented as for-deserialization-only use.

---

## Implementation Order

```text
1. Add restored() factory to GenePair                    (common/genetics — small Phase 1 fix)
2. Define GenePairData, GenomeData, codec                (neoforge or neutral adapter)
3. Implement GenomeSerializer (toData / fromData)        (neoforge or neutral adapter)
4. Register AttachmentType                               (neoforge)
5. Implement BeeGenomeStorage adapter                    (neoforge)
6. Validate persistence manually in-game
```

---

## Risks

| Risk | Mitigation |
|---|---|
| Unknown allele ID on load | Log WARNING, skip chromosome or return null genome |
| Active/inactive state lost | Serialized form stores activeAlleleId explicitly |
| Attachment registration order | Register in mod constructor via `modEventBus` |
| Client access before sync added | Guard client-side genome reads until Phase 5 sync is added |
