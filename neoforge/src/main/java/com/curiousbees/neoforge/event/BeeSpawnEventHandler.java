package com.curiousbees.neoforge.event;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.common.gameplay.spawn.WildBeeSpawnService;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import com.curiousbees.neoforge.data.BeeGenomeStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.entity.animal.Bee;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Assigns a wild genome to vanilla Bee entities that join the world without one.
 * Collects biome tags, Y, and light from the platform; delegates species selection
 * to {@link WildBeeSpawnService#createWildGenomeForHabitat} via the HabitatPredicate system.
 */
@EventBusSubscriber(modid = CuriousBeesMod.MOD_ID)
public final class BeeSpawnEventHandler {

    private BeeSpawnEventHandler() {}

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (!(event.getEntity() instanceof Bee bee)) return;
        if (event.getLevel().isClientSide()) return;
        if (BeeGenomeStorage.hasGenome(bee)) return;

        BlockPos pos = bee.blockPosition();
        List<String> biomeTags = collectBiomeTags(event.getLevel().getBiome(pos));
        int y = pos.getY();
        int light = Math.max(
                event.getLevel().getBrightness(LightLayer.SKY, pos),
                event.getLevel().getBrightness(LightLayer.BLOCK, pos));

        Genome genome = WildBeeSpawnService.createWildGenomeForHabitat(
                biomeTags, y, light,
                new JavaGeneticRandom(new Random()));
        BeeGenomeStorage.setGenome(bee, genome);

        CuriousBeesMod.LOGGER.debug("Assigned genome to bee {} (biomeTags={} y={} light={})",
                bee.getUUID(), biomeTags, y, light);
    }

    /**
     * Converts a NeoForge biome holder's tag keys to plain string IDs
     * (e.g. {@code "minecraft:is_forest"}) for platform-neutral predicate evaluation.
     */
    private static List<String> collectBiomeTags(Holder<Biome> biomeHolder) {
        return biomeHolder.tags()
                .map(tagKey -> tagKey.location().toString())
                .collect(Collectors.toList());
    }
}
