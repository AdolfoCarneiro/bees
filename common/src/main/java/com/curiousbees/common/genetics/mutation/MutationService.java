package com.curiousbees.common.genetics.mutation;

import com.curiousbees.common.genetics.model.Allele;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.GenePair;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.random.GeneticRandom;

import java.util.List;
import java.util.Objects;

public final class MutationService {

    /**
     * Evaluates whether a species mutation applies after Mendelian breeding.
     *
     * Uses the active species of each parent for matching.
     * Returns the child genome unchanged if no mutation applies or the chance roll fails.
     */
    public MutationResult evaluate(Genome parentA, Genome parentB, Genome childGenome,
                                   List<MutationDefinition> definitions, GeneticRandom random) {
        Objects.requireNonNull(parentA, "parentA must not be null");
        Objects.requireNonNull(parentB, "parentB must not be null");
        Objects.requireNonNull(childGenome, "childGenome must not be null");
        Objects.requireNonNull(definitions, "definitions must not be null");
        Objects.requireNonNull(random, "random must not be null");

        String activeA = parentA.getActiveAllele(ChromosomeType.SPECIES).id();
        String activeB = parentB.getActiveAllele(ChromosomeType.SPECIES).id();

        for (MutationDefinition def : definitions) {
            if (!def.matches(activeA, activeB)) continue;
            if (random.nextDouble() >= def.baseChance()) continue;

            Genome mutatedGenome = applyMutation(childGenome, def, random);
            return MutationResult.mutated(mutatedGenome, def);
        }

        return MutationResult.noMutation(childGenome);
    }

    private Genome applyMutation(Genome child, MutationDefinition def, GeneticRandom random) {
        Allele result = def.resultSpeciesAllele();
        GenePair currentSpecies = child.species();
        GenePair newSpecies;

        if (def.resultMode() == MutationResultMode.FULL) {
            newSpecies = new GenePair(result, result, random);
        } else {
            // PARTIAL: replace the active allele, keep the inactive
            newSpecies = new GenePair(result, currentSpecies.inactive(), random);
        }

        return child.withGenePair(ChromosomeType.SPECIES, newSpecies);
    }
}
