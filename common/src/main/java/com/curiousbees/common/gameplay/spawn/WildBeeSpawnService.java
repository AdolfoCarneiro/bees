package com.curiousbees.common.gameplay.spawn;

import com.curiousbees.common.content.builtin.BuiltinBeeContent;
import com.curiousbees.common.content.builtin.BuiltinBeeSpecies;
import com.curiousbees.common.content.species.BeeSpeciesDefinition;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.random.GeneticRandom;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * Selects the appropriate wild species for a spawning bee based on its biome category.
 * Platform-neutral: receives an abstract biome category string from the platform adapter.
 */
public final class WildBeeSpawnService {

    private static final Logger LOGGER = Logger.getLogger(WildBeeSpawnService.class.getName());

    /** Canonical biome category strings passed by the platform adapter. */
    public static final String CATEGORY_FOREST = "forest";
    public static final String CATEGORY_ARID   = "arid";
    public static final String CATEGORY_MEADOW = "meadow";

    private WildBeeSpawnService() {}

    /**
     * Returns the wild species definition for the given biome category.
     * Unknown categories fall back to Meadow with a logged warning.
     *
     * @param biomeCategory one of the CATEGORY_* constants, or any string from the platform adapter
     */
    public static BeeSpeciesDefinition speciesForBiome(String biomeCategory) {
        Objects.requireNonNull(biomeCategory, "biomeCategory must not be null");
        return switch (biomeCategory) {
            case CATEGORY_FOREST -> BuiltinBeeSpecies.FOREST;
            case CATEGORY_ARID   -> BuiltinBeeSpecies.ARID;
            case CATEGORY_MEADOW -> BuiltinBeeSpecies.MEADOW;
            default -> {
                LOGGER.warning("Unknown biome category '" + biomeCategory
                        + "' — falling back to Meadow species.");
                yield BuiltinBeeSpecies.MEADOW;
            }
        };
    }

    /**
     * Creates a default wild genome for the given biome category.
     *
     * @param biomeCategory one of the CATEGORY_* constants
     * @param random        used for GenePair active/inactive resolution
     */
    public static Genome createWildGenome(String biomeCategory, GeneticRandom random) {
        Objects.requireNonNull(random, "random must not be null");
        BeeSpeciesDefinition species = speciesForBiome(biomeCategory);
        LOGGER.fine("Creating wild genome for species: " + species.id()
                + " (biome category: " + biomeCategory + ")");
        return BuiltinBeeContent.createDefaultGenome(species, random);
    }
}
