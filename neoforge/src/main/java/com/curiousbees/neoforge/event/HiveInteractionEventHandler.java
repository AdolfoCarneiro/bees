package com.curiousbees.neoforge.event;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.common.content.builtin.BuiltinBeeContent;
import com.curiousbees.common.content.habitat.HiveCompatibilityService;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import com.curiousbees.neoforge.block.hive.SpeciesHiveBlock;
import com.curiousbees.neoforge.content.NeoForgeContentRegistry;
import com.curiousbees.neoforge.data.BeeGenomeStorage;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.AnimalEnterLeaveHiveEvent;

import java.util.Optional;
import java.util.Random;

/**
 * Enforces species compatibility rules for species hive blocks.
 *
 * <ul>
 *   <li>Entry: cancels bee entry if the bee's species does not match the hive's species.</li>
 *   <li>Leave: stamps the hive's species genome onto released bees that have no genome yet
 *       (covers wild bees that wandered into a hive before being assigned a species).</li>
 * </ul>
 *
 * Only fires for {@link SpeciesHiveBlock} instances; vanilla bee nests and the Genetic Apiary
 * are unaffected.
 */
@EventBusSubscriber(modid = CuriousBeesMod.MOD_ID)
public final class HiveInteractionEventHandler {

    private HiveInteractionEventHandler() {}

    @SubscribeEvent
    public static void onBeeEnterHive(AnimalEnterLeaveHiveEvent.Enter event) {
        if (!(event.getEntity() instanceof Bee bee)) return;

        BlockState state = event.getLevel().getBlockState(event.getHivePos());
        if (!(state.getBlock() instanceof SpeciesHiveBlock hiveBlock)) return;

        String hiveSpeciesId = hiveBlock.speciesId();
        String beeSpeciesId = resolveSpeciesId(bee);

        if (beeSpeciesId == null || !HiveCompatibilityService.canEnter(beeSpeciesId, hiveSpeciesId)) {
            event.setCanceled(true);
            CuriousBeesMod.LOGGER.debug(
                    "Blocked bee {} (species={}) from entering {} hive at {}",
                    bee.getUUID(), beeSpeciesId, hiveSpeciesId, event.getHivePos());
        }
    }

    @SubscribeEvent
    public static void onBeeLeaveHive(AnimalEnterLeaveHiveEvent.Leave event) {
        if (!(event.getEntity() instanceof Bee bee)) return;
        if (event.getLevel().isClientSide()) return;

        BlockState state = event.getLevel().getBlockState(event.getHivePos());
        if (!(state.getBlock() instanceof SpeciesHiveBlock hiveBlock)) return;

        if (BeeGenomeStorage.hasGenome(bee)) return;

        // Stamp the hive's species genome onto wild bees released with no genome.
        String hiveSpeciesId = hiveBlock.speciesId();
        NeoForgeContentRegistry.current().allSpecies().stream()
                .filter(s -> s.id().equals(hiveSpeciesId))
                .findFirst()
                .ifPresent(species -> {
                    Genome genome = BuiltinBeeContent.createDefaultGenome(
                            species, new JavaGeneticRandom(new Random()));
                    BeeGenomeStorage.setGenome(bee, genome);
                    CuriousBeesMod.LOGGER.debug(
                            "Stamped species {} genome onto bee {} leaving hive at {}",
                            hiveSpeciesId, bee.getUUID(), event.getHivePos());
                });
    }

    private static String resolveSpeciesId(Bee bee) {
        Optional<Genome> genome = BeeGenomeStorage.getGenome(bee);
        return genome.map(g -> g.getActiveAllele(ChromosomeType.SPECIES).id()).orElse(null);
    }
}
