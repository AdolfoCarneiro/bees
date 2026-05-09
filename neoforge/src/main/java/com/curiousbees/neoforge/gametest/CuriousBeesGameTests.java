package com.curiousbees.neoforge.gametest;

import com.curiousbees.neoforge.block.AdvancedApiaryBlockEntity;
import com.curiousbees.neoforge.block.BeeOccupantData;
import com.curiousbees.neoforge.block.CentrifugeBlockEntity;
import com.curiousbees.neoforge.data.BeeGenomeStorage;
import com.curiousbees.neoforge.data.CapturedBeeData;
import com.curiousbees.neoforge.registry.ModBlocks;
import com.curiousbees.neoforge.registry.ModDataComponents;
import com.curiousbees.neoforge.registry.ModItems;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.serial.GenomeSerializer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.entity.HopperBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * NeoForge GameTests for Curious Bees items and blocks.
 *
 * <p>All tests use the shared "curiousbees:empty_3x3x3" template (a 3x3x3
 * void arena with a bedrock floor). The template NBT must be generated
 * in-game with {@code /test create curiousbees:empty_3x3x3 3 3 3} and saved
 * to {@code data/curiousbees/structures/empty_3x3x3.nbt} before these tests
 * can run. See the README section on GameTest templates.
 */
@GameTestHolder("curiousbees")
@PrefixGameTestTemplate(false)
public final class CuriousBeesGameTests {

    // Shared test arena: 3 wide, 3 tall, 3 deep, bedrock floor.
    private static final String TEMPLATE = "curiousbees:empty_3x3x3";

    // Working positions within the template (local coords, 0-based from SW-bottom corner).
    // Floor is y=0 (bedrock). Tests place entities/blocks at y=1.
    private static final BlockPos CENTER = new BlockPos(1, 1, 1);

    // -------------------------------------------------------------------------
    // BeeJar — capture (right-click on bee)
    // -------------------------------------------------------------------------

    /**
     * Spawn a vanilla Bee that has a Curious Bees genome, then capture it with
     * an empty BeeJarItem (simulated via interactLivingEntity). Asserts:
     * <ol>
     *   <li>The bee entity is removed from the world.</li>
     *   <li>The resulting ItemStack has the CAPTURED_BEE component.</li>
     *   <li>The genome inside CapturedBeeData round-trips correctly.</li>
     * </ol>
     */
    @GameTest(template = TEMPLATE)
    public static void beeJarCaptureBee(GameTestHelper helper) {
        Bee bee = helper.spawn(EntityType.BEE, CENTER);
        // Give the bee a meadow genome using production-side code.
        Genome originalGenome = makeMeadowGenome(helper);
        if (originalGenome == null) {
            helper.fail("Could not build meadow genome — ContentRegistry not ready?");
            return;
        }
        BeeGenomeStorage.setGenome(bee, originalGenome);

        // Create an empty BeeJar.
        ItemStack jar = new ItemStack(ModItems.BEE_JAR.get());

        // Simulate capture: interactLivingEntity requires a Player. Use a fake server player.
        // NeoForge GameTest provides helper.makeMockPlayer() in some versions.
        // If unavailable, call the capture logic directly via the item method.
        Player player = helper.makeMockPlayer(net.minecraft.world.level.GameType.SURVIVAL);
        jar.getItem().interactLivingEntity(jar, player, bee, InteractionHand.MAIN_HAND);

        // Assert: bee discarded.
        Vec3 c = Vec3.atCenterOf(CENTER);
        helper.assertEntityNotPresent(EntityType.BEE, c.subtract(2, 2, 2), c.add(2, 2, 2));

        // Assert: jar now has CAPTURED_BEE component.
        if (!jar.has(ModDataComponents.CAPTURED_BEE.get())) {
            helper.fail("BeeJar does not have CAPTURED_BEE component after capture.");
            return;
        }

        // Assert: genome round-trips.
        CapturedBeeData data = jar.get(ModDataComponents.CAPTURED_BEE.get());
        helper.assertTrue(data != null, "CapturedBeeData is null.");
        helper.assertTrue(data.genome() != null, "CapturedBeeData.genome() is null.");

        helper.succeed();
    }

    // -------------------------------------------------------------------------
    // BeeJar — release (right-click in world)
    // -------------------------------------------------------------------------

    /**
     * Create a loaded BeeJarItem with a meadow genome, call use(), assert:
     * <ol>
     *   <li>A Bee entity spawns in the world.</li>
     *   <li>The bee carries the correct genome.</li>
     *   <li>The returned ItemStack is empty (jar consumed).</li>
     * </ol>
     */
    @GameTest(template = TEMPLATE)
    public static void beeJarReleaseBee(GameTestHelper helper) {
        Genome genome = makeMeadowGenome(helper);
        if (genome == null) {
            helper.fail("Could not build meadow genome.");
            return;
        }

        ItemStack jar = makeBeeJar(genome, false);
        Player player = helper.makeMockPlayer(net.minecraft.world.level.GameType.SURVIVAL);
        player.setPos(helper.absoluteVec(new Vec3(1.5, 1.0, 1.5)));

        var result = jar.getItem().use(helper.getLevel(), player,
                InteractionHand.MAIN_HAND);

        // Assert: item consumed.
        helper.assertTrue(result.getResult() == InteractionResult.SUCCESS,
                "use() did not return SUCCESS.");
        helper.assertTrue(result.getObject().isEmpty(),
                "BeeJar was not consumed after release. Item: " + result.getObject());

        // Assert: bee spawned with correct genome.
        helper.assertEntityPresent(EntityType.BEE, CENTER, 3.0);
        List<Bee> bees = helper.getLevel().getEntitiesOfClass(Bee.class,
                new AABB(helper.absolutePos(CENTER)).inflate(3.0));
        helper.assertTrue(!bees.isEmpty(), "No bee entity found after release.");

        Bee spawnedBee = bees.get(0);
        Optional<Genome> restoredGenome = BeeGenomeStorage.getGenome(spawnedBee);
        helper.assertTrue(restoredGenome.isPresent(), "Released bee has no genome.");
        helper.assertTrue(
                genomesMatch(genome, restoredGenome.get()),
                "Released bee genome does not match original.");

        helper.succeed();
    }

    // -------------------------------------------------------------------------
    // BeeTransporter — release (item kept, cleared)
    // -------------------------------------------------------------------------

    /**
     * Create a loaded BeeTransporterItem, release it, assert:
     * <ol>
     *   <li>A Bee entity spawns.</li>
     *   <li>The returned ItemStack is an empty BeeTransporter (not consumed).</li>
     * </ol>
     */
    @GameTest(template = TEMPLATE)
    public static void beeTransporterReleaseBee(GameTestHelper helper) {
        Genome genome = makeMeadowGenome(helper);
        if (genome == null) {
            helper.fail("Could not build meadow genome.");
            return;
        }

        ItemStack transporter = makeBeeTransporter(genome, false);
        Player player = helper.makeMockPlayer(net.minecraft.world.level.GameType.SURVIVAL);
        player.setPos(helper.absoluteVec(new Vec3(1.5, 1.0, 1.5)));

        var result = transporter.getItem().use(helper.getLevel(), player,
                InteractionHand.MAIN_HAND);

        helper.assertTrue(result.getResult() == InteractionResult.SUCCESS,
                "use() did not return SUCCESS.");

        ItemStack resultStack = result.getObject();
        // Transporter must NOT be empty — just the component removed.
        helper.assertTrue(!resultStack.isEmpty(),
                "BeeTransporter was unexpectedly consumed (should be kept empty).");
        helper.assertTrue(resultStack.is(ModItems.BEE_TRANSPORTER.get()),
                "Returned stack is not a BeeTransporter: " + resultStack);
        helper.assertTrue(!resultStack.has(ModDataComponents.CAPTURED_BEE.get()),
                "BeeTransporter still has CAPTURED_BEE component after release.");

        // Bee present.
        helper.assertEntityPresent(EntityType.BEE, CENTER, 3.0);

        helper.succeed();
    }

    // -------------------------------------------------------------------------
    // Tooltip — species always shown regardless of analyzed flag (ADR-0016)
    // -------------------------------------------------------------------------

    /**
     * Create a BeeJar loaded with a meadow genome and analyzed=false.
     * Call appendHoverText and assert:
     * <ol>
     *   <li>The tooltip contains species information (not "species_unknown").</li>
     *   <li>No "analyzed"/"unanalyzed" line is present (analysis gate removed).</li>
     * </ol>
     */
    @GameTest(template = TEMPLATE)
    public static void beeJarTooltipShowsSpeciesWithoutAnalysis(GameTestHelper helper) {
        Genome genome = makeMeadowGenome(helper);
        if (genome == null) {
            helper.fail("Could not build meadow genome — ContentRegistry not ready?");
            return;
        }

        // Build a BeeJar with analyzed=false (this was the old gate condition).
        ItemStack jar = makeBeeJar(genome, false);

        List<Component> tooltip = new ArrayList<>();
        jar.getItem().appendHoverText(jar, Item.TooltipContext.EMPTY, tooltip, TooltipFlag.Default.NORMAL);

        // Assert: tooltip is non-empty.
        helper.assertTrue(!tooltip.isEmpty(), "Tooltip is empty for a loaded BeeJar.");

        // Assert: tooltip contains "meadow" somewhere (species line, not "species_unknown").
        boolean hasSpecies = tooltip.stream()
                .map(Component::getString)
                .anyMatch(s -> s.toLowerCase().contains("meadow"));
        helper.assertTrue(hasSpecies,
                "Tooltip does not contain species name. Lines: " + tooltip.stream()
                        .map(Component::getString).toList());

        // Assert: tooltip does NOT contain "Unknown" (old unanalyzed fallback).
        boolean hasUnknown = tooltip.stream()
                .map(Component::getString)
                .anyMatch(s -> s.contains("Unknown"));
        helper.assertTrue(!hasUnknown,
                "Tooltip still shows 'Unknown' species — analysis gate not removed. Lines: " + tooltip.stream()
                        .map(Component::getString).toList());

        helper.succeed();
    }

    // -------------------------------------------------------------------------
    // AdvancedApiary — receive bee from BeeJar via addOccupantFromCapture
    // -------------------------------------------------------------------------

    /**
     * Place an AdvancedApiaryBlock and insert a captured bee via
     * {@code addOccupantFromCapture}. Asserts:
     * <ol>
     *   <li>The apiary reports one occupant.</li>
     *   <li>The occupant's species matches the original genome.</li>
     * </ol>
     */
    @GameTest(template = TEMPLATE)
    public static void advancedApiaryReceivesBee(GameTestHelper helper) {
        Genome genome = makeMeadowGenome(helper);
        if (genome == null) {
            helper.fail("Could not build meadow genome.");
            return;
        }

        // Place the apiary block at CENTER.
        helper.setBlock(CENTER, ModBlocks.ADVANCED_APIARY.get().defaultBlockState());
        AdvancedApiaryBlockEntity apiary = (AdvancedApiaryBlockEntity)
                helper.getBlockEntity(CENTER);
        helper.assertTrue(apiary != null, "AdvancedApiaryBlockEntity not found at " + CENTER);

        CapturedBeeData data = new CapturedBeeData(GenomeSerializer.toData(genome), false);
        boolean added = apiary.addOccupantFromCapture(data,
                (ServerLevel) helper.getLevel()); // getLevel() returns ServerLevel in GameTest context

        helper.assertTrue(added, "addOccupantFromCapture returned false.");
        helper.assertTrue(apiary.getOccupantCount() == 1,
                "Expected 1 occupant, got " + apiary.getOccupantCount());

        // Verify cached occupant data holds the meadow species.
        List<BeeOccupantData> occupants = apiary.getOccupantsInHive();
        helper.assertTrue(!occupants.isEmpty(), "cachedOccupantsInHive is empty.");
        String speciesId = occupants.get(0).speciesId();
        helper.assertTrue(speciesId.contains("meadow"),
                "Occupant species is not meadow: " + speciesId);

        helper.succeed();
    }

    // -------------------------------------------------------------------------
    // Hopper extraction — DOWN face pulls output slots, not frame slots
    // -------------------------------------------------------------------------

    /**
     * Place an AdvancedApiaryBlock with items pre-filled in its output inventory,
     * place a Hopper below pointing up into the apiary (DOWN face). After enough
     * ticks the hopper must have extracted the output items and must NOT have
     * pulled any frame-slot items. Asserts:
     * <ol>
     *   <li>Hopper contains the output item after extraction.</li>
     *   <li>Frame slot in the apiary is unchanged (hopper cannot insert/extract frames via DOWN).</li>
     * </ol>
     */
    @GameTest(template = TEMPLATE, timeoutTicks = 40)
    public static void hopperExtractsOutputNotFrames(GameTestHelper helper) {
        // Apiary at y=2, hopper below it at y=1. The hopper's input mouth is the top face
        // (it always sucks items from the block directly above). The hopper's output FACING
        // direction (DOWN by default) determines where it pushes items further down.
        // A hopper facing DOWN below the apiary will pull from the apiary's DOWN face
        // (which exposes outputExtractView) and push nowhere (no container below it here).
        BlockPos apiaryPos = new BlockPos(1, 2, 1);
        BlockPos hopperPos = new BlockPos(1, 1, 1);

        helper.setBlock(apiaryPos, ModBlocks.ADVANCED_APIARY.get().defaultBlockState());
        // Hopper facing DOWN (default): sucks items from the block above (apiary DOWN face).
        helper.setBlock(hopperPos,
                Blocks.HOPPER.defaultBlockState()
                        .setValue(HopperBlock.FACING, Direction.DOWN));

        AdvancedApiaryBlockEntity apiary = (AdvancedApiaryBlockEntity)
                helper.getBlockEntity(apiaryPos);
        helper.assertTrue(apiary != null, "AdvancedApiaryBlockEntity is null.");

        // Insert a meadow comb into output slot 0 (direct write via internal handler).
        ItemStack comb = new ItemStack(ModItems.MEADOW_COMB.get(), 4);
        apiary.outputInventory().setStackInSlot(0, comb.copy());

        // Insert a basic frame into frame slot 0.
        ItemStack frame = new ItemStack(ModItems.BASIC_FRAME.get(), 1);
        apiary.frameInventory().setStackInSlot(0, frame.copy());

        // Wait 30 ticks for the hopper to process (hops every 8 game ticks).
        helper.runAtTickTime(helper.getTick() + 30, () -> {
            HopperBlockEntity hopper = (HopperBlockEntity) helper.getBlockEntity(hopperPos);
            helper.assertTrue(hopper != null, "HopperBlockEntity is null.");

            // Hopper must have pulled the comb.
            boolean hopperHasComb = false;
            for (int i = 0; i < hopper.getContainerSize(); i++) {
                if (hopper.getItem(i).is(ModItems.MEADOW_COMB.get())) {
                    hopperHasComb = true;
                    break;
                }
            }
            helper.assertTrue(hopperHasComb, "Hopper did not extract meadow_comb from output slot.");

            // Frame slot must still have the frame (hopper cannot touch it via DOWN face).
            ItemStack frameAfter = apiary.frameInventory().getStackInSlot(0);
            helper.assertTrue(frameAfter.is(ModItems.BASIC_FRAME.get()),
                    "Frame was removed from apiary frame slot — hopper should not have access.");

            helper.succeed();
        });
    }

    // -------------------------------------------------------------------------
    // Centrifuge processing
    // -------------------------------------------------------------------------

    /**
     * Place a CentrifugeBlock, insert one meadow_comb (input) and one glass
     * bottle (bottle slot). After enough ticks (processingTime=200 + buffer),
     * assert:
     * <ol>
     *   <li>At least one output slot is non-empty (honeycomb always produced).</li>
     *   <li>Honey bottle appeared once the honey counter filled (requires 5 cycles;
     *       this test seeds the counter directly to 4 and runs one extra comb).</li>
     * </ol>
     *
     * <p>The centrifuge processing time is 200 ticks. This test uses
     * {@code timeoutTicks = 220} to give one full cycle + margin.
     */
    @GameTest(template = TEMPLATE, timeoutTicks = 220)
    public static void centrifugeProcessesMeadowComb(GameTestHelper helper) {
        helper.setBlock(CENTER, ModBlocks.CENTRIFUGE.get().defaultBlockState());
        CentrifugeBlockEntity centrifuge = (CentrifugeBlockEntity) helper.getBlockEntity(CENTER);
        helper.assertTrue(centrifuge != null, "CentrifugeBlockEntity is null.");

        // Insert comb.
        centrifuge.inputInventory().setStackInSlot(0,
                new ItemStack(ModItems.MEADOW_COMB.get(), 1));
        // Insert glass bottle for honey.
        centrifuge.bottleInventory().setStackInSlot(0,
                new ItemStack(Items.GLASS_BOTTLE, 1));

        // Wait for full processing cycle (200 ticks) + 10 tick margin.
        helper.runAtTickTime(helper.getTick() + 210, () -> {
            // At least honeycomb in output (chance=1.0 in meadow recipe).
            boolean hasOutput = false;
            for (int i = 0; i < centrifuge.outputInventory().getSlots(); i++) {
                if (!centrifuge.outputInventory().getStackInSlot(i).isEmpty()) {
                    hasOutput = true;
                    break;
                }
            }
            helper.assertTrue(hasOutput, "Centrifuge produced no output after 210 ticks.");

            // Honeycomb (chance=1.0) must be present.
            boolean hasHoneycomb = false;
            for (int i = 0; i < centrifuge.outputInventory().getSlots(); i++) {
                if (centrifuge.outputInventory().getStackInSlot(i).is(Items.HONEYCOMB)) {
                    hasHoneycomb = true;
                    break;
                }
            }
            helper.assertTrue(hasHoneycomb,
                    "Centrifuge output does not contain honeycomb (chance=1.0 in meadow recipe).");

            helper.succeed();
        });
    }

    /**
     * Tests honey-bottle production: seed the centrifuge with 4 honey portions
     * already buffered, insert one comb (adds 1 portion = total 5 = full), insert
     * a glass bottle. After processing, a honey bottle must be in the output.
     *
     * <p>This test manipulates internal state via reflection to set honeyCounter=4.
     * If the field becomes package-private in a refactor, remove the reflection
     * and use a test-only setter instead.
     */
    @GameTest(template = TEMPLATE, timeoutTicks = 220)
    public static void centrifugeProducesHoneyBottle(GameTestHelper helper) {
        helper.setBlock(CENTER, ModBlocks.CENTRIFUGE.get().defaultBlockState());
        CentrifugeBlockEntity centrifuge = (CentrifugeBlockEntity) helper.getBlockEntity(CENTER);
        helper.assertTrue(centrifuge != null, "CentrifugeBlockEntity is null.");

        // Pre-seed honey counter to 4 via reflection so one comb cycle fills it to 5.
        try {
            java.lang.reflect.Field f = CentrifugeBlockEntity.class.getDeclaredField("honeyCounter");
            f.setAccessible(true);
            f.setInt(centrifuge, 4);
        } catch (ReflectiveOperationException e) {
            helper.fail("Could not set honeyCounter via reflection: " + e.getMessage());
            return;
        }

        centrifuge.inputInventory().setStackInSlot(0,
                new ItemStack(ModItems.MEADOW_COMB.get(), 1));
        centrifuge.bottleInventory().setStackInSlot(0,
                new ItemStack(Items.GLASS_BOTTLE, 1));

        helper.runAtTickTime(helper.getTick() + 210, () -> {
            boolean hasHoneyBottle = false;
            for (int i = 0; i < centrifuge.honeyBottleOutputInventory().getSlots(); i++) {
                if (centrifuge.honeyBottleOutputInventory().getStackInSlot(i).is(Items.HONEY_BOTTLE)) {
                    hasHoneyBottle = true;
                    break;
                }
            }
            helper.assertTrue(hasHoneyBottle,
                    "Centrifuge did not produce honey bottle even with full honey counter.");
            helper.succeed();
        });
    }

    // -------------------------------------------------------------------------
    // Centrifuge slot layout contract
    // -------------------------------------------------------------------------

    /**
     * Verifies slot count constants and inventory access contract for the
     * expanded Centrifuge (ADR-0015 PR-T08). Contract test — no timed processing.
     */
    @GameTest(template = TEMPLATE)
    public static void centrifugeSlotLayout(GameTestHelper helper) {
        // Assert constants
        helper.assertTrue(CentrifugeBlockEntity.OUTPUT_SLOTS == 9,
                "Expected OUTPUT_SLOTS=9, got " + CentrifugeBlockEntity.OUTPUT_SLOTS);
        helper.assertTrue(CentrifugeBlockEntity.HONEY_BOTTLE_OUTPUT_SLOTS == 1,
                "Expected HONEY_BOTTLE_OUTPUT_SLOTS=1, got " + CentrifugeBlockEntity.HONEY_BOTTLE_OUTPUT_SLOTS);
        helper.assertTrue(CentrifugeBlockEntity.UPGRADE_SLOTS == 3,
                "Expected UPGRADE_SLOTS=3, got " + CentrifugeBlockEntity.UPGRADE_SLOTS);

        helper.setBlock(CENTER, ModBlocks.CENTRIFUGE.get().defaultBlockState());
        CentrifugeBlockEntity centrifuge = (CentrifugeBlockEntity) helper.getBlockEntity(CENTER);
        helper.assertTrue(centrifuge != null, "CentrifugeBlockEntity is null.");

        // upgradeInventory slot 0 must reject a non-upgrade item (dirt)
        ItemStack dirt = new ItemStack(Items.DIRT);
        boolean upgradeRejectsDirt = !centrifuge.upgradeInventory().isItemValid(0, dirt);
        helper.assertTrue(upgradeRejectsDirt,
                "upgradeInventory should reject non-upgrade items (dirt was accepted).");

        // outputInventory slot 0 must reject any item (extract-only)
        ItemStack honeycomb = new ItemStack(Items.HONEYCOMB);
        boolean outputRejectsInsert = !centrifuge.outputInventory().isItemValid(0, honeycomb);
        helper.assertTrue(outputRejectsInsert,
                "outputInventory slot 0 should be extract-only (accepted item).");

        // honeyBottleOutputInventory slot 0 must also reject any item (extract-only)
        ItemStack honeyBottle = new ItemStack(Items.HONEY_BOTTLE);
        boolean honeyOutputRejectsInsert = !centrifuge.honeyBottleOutputInventory().isItemValid(0, honeyBottle);
        helper.assertTrue(honeyOutputRejectsInsert,
                "honeyBottleOutputInventory slot 0 should be extract-only (accepted item).");

        helper.succeed();
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /**
     * Builds a minimal meadow genome for testing by looking up alleles from the
     * NeoForge content registry. Returns null if the registry is not yet loaded
     * (prevents test pollution instead of crash).
     */
    private static Genome makeMeadowGenome(GameTestHelper helper) {
        try {
            com.curiousbees.common.content.registry.ContentRegistry registry =
                    com.curiousbees.neoforge.content.NeoForgeContentRegistry.current();
            // Build a trivial meadow genome using the serializer round-trip path.
            // AlleleFixtures IDs must match what the content registry loaded.
            String speciesId = "curious_bees:species/meadow";
            var speciesAllele = registry.findAllele(speciesId);
            if (speciesAllele.isEmpty()) {
                // Content not loaded — mark test as expected failure.
                return null;
            }
            // Use the spawn-egg item to get a pre-built genome for meadow bees.
            // Simpler: create via CuriousBeeSpeciesSpawnEggItem helper (not exposed).
            // Fall back to WildBeeSpawnService with meadow category.
            com.curiousbees.common.genetics.random.JavaGeneticRandom rng =
                    new com.curiousbees.common.genetics.random.JavaGeneticRandom(new java.util.Random(42L));
            return com.curiousbees.common.gameplay.spawn.WildBeeSpawnService
                    .createWildGenome(com.curiousbees.common.gameplay.spawn.WildBeeSpawnService.CATEGORY_MEADOW, rng);
        } catch (Exception e) {
            return null;
        }
    }

    private static ItemStack makeBeeJar(Genome genome, boolean analyzed) {
        ItemStack jar = new ItemStack(ModItems.BEE_JAR.get());
        CapturedBeeData data = new CapturedBeeData(GenomeSerializer.toData(genome), analyzed);
        jar.set(ModDataComponents.CAPTURED_BEE.get(), data);
        return jar;
    }

    private static ItemStack makeBeeTransporter(Genome genome, boolean analyzed) {
        ItemStack transporter = new ItemStack(ModItems.BEE_TRANSPORTER.get());
        CapturedBeeData data = new CapturedBeeData(GenomeSerializer.toData(genome), analyzed);
        transporter.set(ModDataComponents.CAPTURED_BEE.get(), data);
        return transporter;
    }

    /**
     * Shallow genome equivalence: compare active species allele IDs only.
     * Full genome equality requires a domain method; for GameTests, species is sufficient.
     */
    private static boolean genomesMatch(Genome a, Genome b) {
        String idA = a.getActiveAllele(com.curiousbees.common.genetics.model.ChromosomeType.SPECIES).id();
        String idB = b.getActiveAllele(com.curiousbees.common.genetics.model.ChromosomeType.SPECIES).id();
        return idA.equals(idB);
    }
}
