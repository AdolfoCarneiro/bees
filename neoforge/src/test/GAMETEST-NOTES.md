# GameTest Migration Notes

Tests that cannot run without a Minecraft server / NeoForge bootstrap must live in
NeoForge's `gametest` run configuration (or a dedicated GameTest class annotated with
`@GameTestHolder`). This file documents which test categories require that treatment
and **why**, so they can be implemented when the test infrastructure is set up.

---

## Tests viable WITHOUT game bootstrap (already implemented)

These live in `neoforge/src/test/java/com/curiousbees/neoforge/data/`:

| Test class | What it covers | Why viable |
|---|---|---|
| `GenomeCodecRoundtripTest` | DFU `Codec<GenomeData>` + `StreamCodec<GenomeData>` JSON and ByteBuf roundtrips | DFU (`com.mojang.serialization`) and Netty (`io.netty.buffer.Unpooled`) require no registry |
| `CapturedBeeDataCodecTest` | `CapturedBeeData.CODEC` and `CapturedBeeData.STREAM_CODEC` | Same — uses DFU + ByteBufCodecs on a plain Netty buffer |
| `GenePairDataContractTest` | `GenePairData` record validation, equality, accessor contracts | Pure Java record, no Minecraft types |

---

## Tests that require GameTest (NOT yet implemented)

The following contracts need `@GameTestHolder` tests but no stubs exist.
GameTest patterns are documented below.

### 1. `BeeJarItemReleaseTest` / `BeeTransporterItemReleaseTest`

**What to test:**
- `BeeJarItem.releaseItem(stack)` shrinks the stack to empty after releasing.
- `BeeTransporterItem.releaseItem(stack)` removes `ModDataComponents.CAPTURED_BEE` and
  returns the same (non-empty) stack — item is reusable.
- After release: `BeeTransporter` stack must NOT equal a "loaded" transporter
  (empty != carrying bee).

**Why blocked:**
- `ItemStack` constructor requires `Item` from the game registry (`BuiltInRegistries.ITEM`)
  which throws unless `Bootstrap.bootStrap()` has been called.
- `ModDataComponents.CAPTURED_BEE.get()` requires `DeferredRegister` to have fired
  on the mod event bus — only happens during NeoForge game startup.

**GameTest approach:**
```java
@GameTest(template = "curiousbees:empty")
public void beeJarReleaseShrinks(GameTestHelper helper) {
    ItemStack jar = new ItemStack(ModItems.BEE_JAR.get());
    jar.set(ModDataComponents.CAPTURED_BEE.get(),
            new CapturedBeeData(testGenome(), false));
    ItemStack result = new BeeJarItem(new Item.Properties()).releaseItem(jar);
    helper.assertTrue(result.isEmpty(), "BeeJar must be empty after release");
    helper.succeed();
}
```

---

### 2. `BeeSlotValidationTest`

**What to test:**
- `GeneticApiaryBlockEntity.frameInventory.isItemValid(slot, stack)` returns `true`
  only for items tagged `curiousbees:frames`.
- `GeneticApiaryBlockEntity.outputInventory.isItemValid(slot, stack)` always returns
  `false` (output-only).
- `automationOutputView.insertItem(outputSlot, stack, false)` returns the stack unchanged
  (no insertion into output slots from the side view).
- `outputExtractView.insertItem(slot, stack, false)` returns the stack unchanged
  (no insertion on DOWN face).

**Why blocked:**
- `GeneticApiaryBlockEntity` extends `BeehiveBlockEntity` whose constructor requires a
  `BlockPos` and `BlockState`.  `BlockState` is backed by the block registry; constructing
  it requires `Bootstrap.bootStrap()`.
- Tag resolution (`stack.is(ModTags.Items.FRAMES)`) requires the tag system to be loaded
  (server startup).

**GameTest approach:**
```java
@GameTest(template = "curiousbees:apiary_room")
public void outputSlotsAreExtractOnly(GameTestHelper helper) {
    BlockPos pos = new BlockPos(0, 1, 0);
    GeneticApiaryBlockEntity be = (GeneticApiaryBlockEntity)
            helper.getBlockEntity(pos);
    IItemHandler view = be.automationOutputView();
    int firstOutput = GeneticApiaryBlockEntity.FRAME_SLOTS;
    ItemStack honey = new ItemStack(Items.HONEYCOMB);
    ItemStack leftover = view.insertItem(firstOutput, honey, false);
    helper.assertTrue(leftover.getCount() == honey.getCount(),
            "Output slots must reject insertion");
    helper.succeed();
}
```

---

### 3. `ApiaryCapabilitiesContractTest`

**What to test:**
- DOWN face → `outputExtractView()` (only output slots, no frame slots).
- Non-DOWN faces → `automationOutputView()` (frames insertable in slots 0–2, outputs
  extract-only in slots 3–8).
- The capability lambda in `ApiaryCapabilities.register()` returns different handlers
  per direction.

**Why blocked:**
- `RegisterCapabilitiesEvent` is dispatched by NeoForge's mod bus — cannot be triggered
  outside a running game.
- Verifying the registered capability requires a `Level` to call
  `level.getCapability(Capabilities.ItemHandler.BLOCK, pos, state, be, side)`.

**GameTest approach:** Place a Genetic Apiary in a test structure, attach a hopper
below (`Direction.DOWN`) and pipes on the side, verify item routing through the
capability system.

---

### 4. `BeeGenomeStorageGameTest`

**What to test:**
- `setGenome` → `hasGenome` → `getGenome` round-trip on a live `Bee` entity.
- `clearGenome` removes the attachment; subsequent `hasGenome` returns `false`.
- Missing genome → `getGenome` returns `Optional.empty()` without throwing.

**Why blocked:**
- `Bee` entity must be spawned in a `ServerLevel` — requires a running server.
- `BeeGenomeAttachments.BEE_GENOME` requires the NeoForge attachment type to be
  registered via the mod event bus.

---

## Build configuration required to run the viable tests

Add to `neoforge/build.gradle`:

```groovy
sourceSets {
    test {
        java.srcDirs = ['src/test/java']
        compileClasspath += sourceSets.main.compileClasspath + sourceSets.main.output
        runtimeClasspath += sourceSets.main.runtimeClasspath + sourceSets.main.output
    }
}

dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter:5.10.0'
    testRuntimeOnly    'org.junit.platform:junit-platform-launcher'
}

tasks.named('test', Test) {
    useJUnitPlatform()
}
```

The NeoForge moddev plugin places `com.mojang.serialization` and `io.netty` on the
main compile classpath (via the NeoForge jar), so the test sourceSet inherits them
through `sourceSets.main.compileClasspath`.
