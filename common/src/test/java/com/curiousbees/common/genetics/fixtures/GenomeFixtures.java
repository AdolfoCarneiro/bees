package com.curiousbees.common.genetics.fixtures;

import com.curiousbees.common.genetics.model.Allele;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.GenePair;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.random.DeterministicGeneticRandom;

import java.util.EnumMap;
import java.util.Map;

import static com.curiousbees.common.genetics.fixtures.AlleleFixtures.*;

/** Factory methods for building sample Genome instances in genetics unit tests. */
public final class GenomeFixtures {

    private GenomeFixtures() {}

    /**
     * Builds a purebred genome for a single species using normal defaults
     * for all other MVP chromosomes.
     */
    public static Genome pureMeadow() {
        return pureSpecies(MEADOW);
    }

    public static Genome pureForest() {
        return pureSpecies(FOREST);
    }

    public static Genome pureArid() {
        return pureSpecies(ARID);
    }

    public static Genome pureCultivated() {
        return pureSpecies(CULTIVATED);
    }

    public static Genome pureHardy() {
        return pureSpecies(HARDY);
    }

    /** Meadow (active) / Forest (inactive) hybrid species, normal traits. */
    public static Genome hybridMeadowForest() {
        return hybridSpecies(MEADOW, FOREST);
    }

    /** Forest (active) / Arid (inactive) hybrid species, normal traits. */
    public static Genome hybridForestArid() {
        return hybridSpecies(FOREST, ARID);
    }

    // --- helpers ---

    private static Genome pureSpecies(Allele species) {
        return build(pair(species, species), pair(LIFESPAN_NORMAL, LIFESPAN_NORMAL),
                pair(PRODUCTIVITY_NORMAL, PRODUCTIVITY_NORMAL),
                pair(FERTILITY_TWO, FERTILITY_TWO),
                pair(FLOWER_FLOWERS, FLOWER_FLOWERS));
    }

    /**
     * Creates a hybrid species genome where activeSpecies becomes active.
     * Both species alleles are DOMINANT so random chooses; we force activeSpecies first.
     */
    private static Genome hybridSpecies(Allele activeSpecies, Allele inactiveSpecies) {
        // DeterministicGeneticRandom(true) -> first allele wins -> activeSpecies is active
        GenePair speciesPair = new GenePair(activeSpecies, inactiveSpecies,
                new DeterministicGeneticRandom().withBooleans(true));
        return build(speciesPair, pair(LIFESPAN_NORMAL, LIFESPAN_NORMAL),
                pair(PRODUCTIVITY_NORMAL, PRODUCTIVITY_NORMAL),
                pair(FERTILITY_TWO, FERTILITY_TWO),
                pair(FLOWER_FLOWERS, FLOWER_FLOWERS));
    }

    private static GenePair pair(Allele a, Allele b) {
        // both same dominance -> need a boolean; true -> first wins
        return new GenePair(a, b, new DeterministicGeneticRandom().withBooleans(true));
    }

    private static Genome build(GenePair species, GenePair lifespan,
                                GenePair productivity, GenePair fertility,
                                GenePair flowerType) {
        Map<ChromosomeType, GenePair> map = new EnumMap<>(ChromosomeType.class);
        map.put(ChromosomeType.SPECIES,      species);
        map.put(ChromosomeType.LIFESPAN,     lifespan);
        map.put(ChromosomeType.PRODUCTIVITY, productivity);
        map.put(ChromosomeType.FERTILITY,    fertility);
        map.put(ChromosomeType.FLOWER_TYPE,  flowerType);
        return new Genome(map);
    }
}
