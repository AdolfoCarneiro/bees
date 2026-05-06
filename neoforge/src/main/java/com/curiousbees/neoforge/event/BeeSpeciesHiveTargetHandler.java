package com.curiousbees.neoforge.event;

import com.curiousbees.common.content.habitat.BeeNestCompatibilityService;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.neoforge.block.beenest.SpeciesBeeNestBlock;
import com.curiousbees.neoforge.data.BeeGenomeStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.Optional;

/**
 * Enforces the species-targeting rule for bee hive pathfinding.
 *
 * <h3>Rule</h3>
 * A bee may only home to a {@link SpeciesBeeNestBlock} (or Genetic Apiary) whose
 * {@code speciesId} matches the bee’s own active SPECIES allele.
 * The exact match is delegated to {@link BeeNestCompatibilityService#canEnter}.
 *
 * <h3>Mechanism</h3>
 * Vanilla bees scan the {@code minecraft:bee_home} POI tag (see
 * {@code data/minecraft/tags/point_of_interest_type/bee_home.json}) to find candidate
 * hives. When a bee locks onto an incompatible nest, this handler clears the hive
 * position and imposes a stay-out countdown so the bee re-evaluates next tick cycle.
 *
 * <h3>Verification (manual checklist — 5 species)</h3>
 * In a creative world, for each species (meadow, forest, arid, cultivated, hardy):
 * <ol>
 *   <li>Spawn a bee with a species spawn egg; confirm genome via analyzer.</li>
 *   <li>Place its own nest nearby — bee should path to and enter it.</li>
 *   <li>Place a different species’ nest nearby — bee should ignore it (or clear after ≤ 10 ticks).</li>
 * </ol>
 *
 * <p>Cultivated and Hardy bees have no natural nest block; they accept the Genetic Apiary.
 */
public final class BeeSpeciesHiveTargetHandler {

    /** Throttle checks per bee (ticks). */
    private static final int INTERVAL_TICKS = 10;

    /** Matches vanilla “stay out of hive” backoff after a bad hive interaction. */
    private static final int STAY_OUT_OF_HIVE_TICKS = 400;

    private BeeSpeciesHiveTargetHandler() {}

    @SubscribeEvent
    public static void onEntityTickPost(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Bee bee)) {
            return;
        }
        Level level = bee.level();
        if (level.isClientSide() || !bee.isAlive()) {
            return;
        }
        if (bee.tickCount % INTERVAL_TICKS != 0) {
            return;
        }
        if (!bee.hasHive()) {
            return;
        }
        Optional<Genome> genomeOpt = BeeGenomeStorage.getGenome(bee);
        if (genomeOpt.isEmpty()) {
            return;
        }
        BlockPos hivePos = bee.getHivePos();
        if (hivePos == null) {
            return;
        }
        if (level instanceof ServerLevel serverLevel && !serverLevel.isLoaded(hivePos)) {
            return;
        }
        BlockState state = level.getBlockState(hivePos);
        if (!(state.getBlock() instanceof SpeciesBeeNestBlock nestBlock)) {
            return;
        }
        String beeSpeciesId = genomeOpt.get().getActiveAllele(ChromosomeType.SPECIES).id();
        if (BeeNestCompatibilityService.canEnter(beeSpeciesId, nestBlock.speciesId())) {
            return;
        }
        bee.setHivePos(null);
        bee.setStayOutOfHiveCountdown(STAY_OUT_OF_HIVE_TICKS);
    }
}
