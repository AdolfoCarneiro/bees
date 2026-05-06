package com.curiousbees.common.gameplay.spawn;

import com.curiousbees.common.content.builtin.BuiltinBeeContent;
import com.curiousbees.common.content.builtin.BuiltinBeeSpecies;
import com.curiousbees.common.content.habitat.HabitatPredicate;
import com.curiousbees.common.content.species.BeeSpeciesDefinition;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.random.GeneticRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;

/**
 * Selects the appropriate wild species for a spawning bee based on its biome and environment.
 * Platform-neutral: receives biome tag strings, Y, and light level from the platform adapter.
 */
public final class WildBeeSpawnService {

    private static final Logger LOGGER = Logger.getLogger(WildBeeSpawnService.class.getName());

    /** Canonical biome category strings — kept for backward compatibility with existing callers. */
    public static final String CATEGORY_FOREST = "forest";
    public static final String CATEGORY_ARID   = "arid";
    public static final String CATEGORY_MEADOW = "meadow";

    private WildBeeSpawnService() {}

    /**
     * Returns the wild species definition that best matches the given environmental snapshot.
     * <p>
     * Selection priority:
     * <ol>
     *   <li>Species whose predicate requires specific biome tags AND those tags match — highest priority.</li>
     *   <li>Species whose predicate is a wildcard (no required tags) — fallback pool.</li>
     *   <li>Hard fallback to {@link BuiltinBeeSpecies#MEADOW} if nothing matches (warns).</li>
     * </ol>
     * Among candidates at the same priority level, selection is weighted by
     * {@link HabitatPredicate#weight()}.
     *
     * @param biomeTags string IDs of all tags carried by the current biome (e.g. {@code "minecraft:is_forest"})
     * @param y         block Y of the spawning bee
     * @param light     combined light level [0–15] at the spawn position
     */
    public static BeeSpeciesDefinition speciesForHabitat(List<String> biomeTags, int y, int light) {
        Objects.requireNonNull(biomeTags, "biomeTags must not be null");

        List<BeeSpeciesDefinition> specific  = new ArrayList<>();
        List<BeeSpeciesDefinition> wildcards = new ArrayList<>();

        for (BeeSpeciesDefinition species : BuiltinBeeSpecies.ALL) {
            species.habitat()
                   .flatMap(h -> h.spawnPredicate())
                   .ifPresent(predicate -> {
                       if (predicate.matchesY(y) && predicate.matchesLight(light)) {
                           if (predicate.requiredBiomeTags().isEmpty()) {
                               wildcards.add(species);
                           } else if (predicate.matchesBiomeTags(biomeTags)) {
                               specific.add(species);
                           }
                       }
                   });
        }

        List<BeeSpeciesDefinition> candidates = specific.isEmpty() ? wildcards : specific;
        if (candidates.isEmpty()) {
            LOGGER.warning("No habitat predicate matched biomeTags=" + biomeTags
                    + " y=" + y + " light=" + light + " — falling back to Meadow species.");
            return BuiltinBeeSpecies.MEADOW;
        }
        return weightedPick(candidates);
    }

    /**
     * Creates a default wild genome for a bee at the given position context.
     * Delegates species selection to {@link #speciesForHabitat}.
     *
     * @param biomeTags string IDs of all tags carried by the current biome
     * @param y         block Y of the spawning bee
     * @param light     combined light level [0–15] at the spawn position
     * @param random    used for genome allele resolution
     */
    public static Genome createWildGenomeForHabitat(List<String> biomeTags, int y, int light,
                                                     GeneticRandom random) {
        Objects.requireNonNull(random, "random must not be null");
        BeeSpeciesDefinition species = speciesForHabitat(biomeTags, y, light);
        LOGGER.fine("Creating wild genome for species: " + species.id()
                + " (biomeTags: " + biomeTags + " y=" + y + " light=" + light + ")");
        return BuiltinBeeContent.createDefaultGenome(species, random);
    }

    /**
     * Returns the wild species definition for a legacy biome category string.
     * Unknown categories fall back to Meadow with a logged warning.
     *
     * @param biomeCategory one of the CATEGORY_* constants
     * @deprecated Prefer {@link #speciesForHabitat(List, int, int)} for new call sites.
     */
    @Deprecated
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
     * Creates a default wild genome for the given legacy biome category.
     *
     * @param biomeCategory one of the CATEGORY_* constants
     * @param random        used for GenePair active/inactive resolution
     * @deprecated Prefer {@link #createWildGenomeForHabitat(List, int, int, GeneticRandom)}.
     */
    @Deprecated
    public static Genome createWildGenome(String biomeCategory, GeneticRandom random) {
        Objects.requireNonNull(random, "random must not be null");
        BeeSpeciesDefinition species = speciesForBiome(biomeCategory);
        LOGGER.fine("Creating wild genome for species: " + species.id()
                + " (biome category: " + biomeCategory + ")");
        return BuiltinBeeContent.createDefaultGenome(species, random);
    }

    private static BeeSpeciesDefinition weightedPick(List<BeeSpeciesDefinition> candidates) {
        if (candidates.size() == 1) return candidates.get(0);

        int total = candidates.stream()
                .mapToInt(s -> s.habitat()
                        .flatMap(h -> h.spawnPredicate())
                        .map(HabitatPredicate::weight)
                        .orElse(HabitatPredicate.DEFAULT_WEIGHT))
                .sum();

        int roll = ThreadLocalRandom.current().nextInt(total);
        int acc  = 0;
        for (BeeSpeciesDefinition candidate : candidates) {
            acc += candidate.habitat()
                    .flatMap(h -> h.spawnPredicate())
                    .map(HabitatPredicate::weight)
                    .orElse(HabitatPredicate.DEFAULT_WEIGHT);
            if (roll < acc) return candidate;
        }
        return candidates.get(candidates.size() - 1);
    }
}
