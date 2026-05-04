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
 * Clears a bee’s saved hive when it points at another species’ {@link SpeciesBeeNestBlock},
 * so vanilla POI-driven pathfinding does not keep incompatible nests as the active target.
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
