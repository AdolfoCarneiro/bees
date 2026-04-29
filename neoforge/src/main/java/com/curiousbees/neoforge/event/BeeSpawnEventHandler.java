package com.curiousbees.neoforge.event;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.common.gameplay.spawn.WildBeeSpawnService;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import com.curiousbees.neoforge.data.BeeGenomeStorage;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.biome.Biome;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.Random;

/**
 * Assigns a wild genome to vanilla Bee entities that join the world without one.
 * Event handler is thin: biome detection is isolated here; genome logic delegates to common.
 */
@EventBusSubscriber(modid = CuriousBeesMod.MOD_ID)
public final class BeeSpawnEventHandler {

    private BeeSpawnEventHandler() {}

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof Bee bee)) return;
        if (event.getLevel().isClientSide()) return;
        if (BeeGenomeStorage.hasGenome(bee)) return;

        String category = resolveBiomeCategory(event.getLevel().getBiome(bee.blockPosition()));
        Genome genome = WildBeeSpawnService.createWildGenome(
                category,
                new JavaGeneticRandom(new Random()));
        BeeGenomeStorage.setGenome(bee, genome);

        CuriousBeesMod.LOGGER.debug("Assigned {} genome to bee {}",
                category, bee.getUUID());
    }

    /**
     * Maps NeoForge biome holder to a platform-neutral category string.
     * Only runs server-side; uses vanilla biome tags for forward compatibility.
     */
    private static String resolveBiomeCategory(Holder<Biome> biomeHolder) {
        if (biomeHolder.is(BiomeTags.IS_FOREST)) {
            return WildBeeSpawnService.CATEGORY_FOREST;
        }
        if (biomeHolder.is(BiomeTags.IS_SAVANNA) || biomeHolder.is(BiomeTags.IS_BADLANDS)) {
            return WildBeeSpawnService.CATEGORY_ARID;
        }
        return WildBeeSpawnService.CATEGORY_MEADOW;
    }
}
