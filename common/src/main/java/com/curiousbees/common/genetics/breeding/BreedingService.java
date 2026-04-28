package com.curiousbees.common.genetics.breeding;

import com.curiousbees.common.genetics.model.Allele;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.GenePair;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.random.GeneticRandom;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class BreedingService {

    /**
     * Crosses two parent genomes using Mendelian inheritance.
     * Each chromosome contributes one randomly selected allele from each parent.
     * Both parents must share the same set of chromosome types.
     */
    public BreedingResult breed(Genome parentA, Genome parentB, GeneticRandom random) {
        Objects.requireNonNull(parentA, "parentA must not be null");
        Objects.requireNonNull(parentB, "parentB must not be null");
        Objects.requireNonNull(random, "random must not be null");

        validateCompatibleChromosomeSets(parentA, parentB);

        Map<ChromosomeType, GenePair> childPairs = new EnumMap<>(ChromosomeType.class);

        for (ChromosomeType type : parentA.genePairs().keySet()) {
            GenePair pairA = parentA.getGenePair(type);
            GenePair pairB = parentB.getGenePair(type);

            Allele fromA = random.nextBoolean() ? pairA.first() : pairA.second();
            Allele fromB = random.nextBoolean() ? pairB.first() : pairB.second();

            childPairs.put(type, new GenePair(fromA, fromB, random));
        }

        return new BreedingResult(new Genome(childPairs));
    }

    private void validateCompatibleChromosomeSets(Genome parentA, Genome parentB) {
        for (ChromosomeType type : parentA.genePairs().keySet()) {
            if (!parentB.hasChromosome(type)) {
                throw new IllegalArgumentException(
                        "Parent genomes have incompatible chromosome sets: parentB is missing " + type);
            }
        }
        for (ChromosomeType type : parentB.genePairs().keySet()) {
            if (!parentA.hasChromosome(type)) {
                throw new IllegalArgumentException(
                        "Parent genomes have incompatible chromosome sets: parentA is missing " + type);
            }
        }
    }
}
