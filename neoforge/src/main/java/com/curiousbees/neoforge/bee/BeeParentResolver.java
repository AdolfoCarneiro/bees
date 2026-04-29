package com.curiousbees.neoforge.bee;

import com.curiousbees.common.gameplay.spawn.WildBeeSpawnService;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import com.curiousbees.neoforge.data.BeeGenomeStorage;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.level.biome.Biome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

/**
 * Resolves or initializes parent Bee genomes before breeding.
 * If a parent has no genome, assigns a biome-appropriate fallback and logs a warning.
 * Never throws; always returns a usable genome for each parent.
 */
public final class BeeParentResolver {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeeParentResolver.class);

    private BeeParentResolver() {}

    /**
     * Returns the genome of the given Bee, initializing it from the biome if absent.
     * The fallback genome is stored on the bee so it persists normally.
     */
    public static Optional<Genome> resolve(Bee bee, ServerLevel level) {
        Objects.requireNonNull(bee,   "bee must not be null");
        Objects.requireNonNull(level, "level must not be null");

        Optional<Genome> existing = BeeGenomeStorage.getGenome(bee);
        if (existing.isPresent()) {
            return existing;
        }

        LOGGER.warn("Parent bee {} has no genome — assigning biome fallback before breeding.", bee.getUUID());
        String biomeCategory = resolveBiomeCategory(level.getBiome(bee.blockPosition()));
        Genome fallback = WildBeeSpawnService.createWildGenome(
                biomeCategory, new JavaGeneticRandom(new Random()));
        BeeGenomeStorage.setGenome(bee, fallback);
        return Optional.of(fallback);
    }

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
