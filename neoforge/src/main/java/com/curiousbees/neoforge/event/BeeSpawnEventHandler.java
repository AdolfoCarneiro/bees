package com.curiousbees.neoforge.event;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.common.content.builtin.BuiltinBeeContent;
import com.curiousbees.common.content.builtin.BuiltinBeeSpecies;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import com.curiousbees.neoforge.data.BeeGenomeStorage;
import net.minecraft.world.entity.animal.Bee;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.Random;

/**
 * Assigns the Common genome to any Bee entity that joins the world without one.
 *
 * <p>Per ADR-0019, biome does NOT determine which species a bee gets on join.
 * Mod spawn eggs (CuriousBeeSpeciesSpawnEggItem) set the genome before addFreshEntity,
 * so those bees already have a genome and skip this handler via the hasGenome guard.
 * Vanilla spawn eggs do not set a genome, so they reach this handler and receive Common.
 */
@EventBusSubscriber(modid = CuriousBeesMod.MOD_ID)
public final class BeeSpawnEventHandler {

    private BeeSpawnEventHandler() {}

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof Bee bee)) return;
        if (event.getLevel().isClientSide()) return;
        if (BeeGenomeStorage.hasGenome(bee)) return;

        Genome genome = BuiltinBeeContent.createDefaultGenome(
                BuiltinBeeSpecies.COMMON, new JavaGeneticRandom(new Random()));
        BeeGenomeStorage.setGenome(bee, genome);

        CuriousBeesMod.LOGGER.warn("No genome preset for bee {} — assigned Common species as fallback.",
                bee.getUUID());
    }
}
