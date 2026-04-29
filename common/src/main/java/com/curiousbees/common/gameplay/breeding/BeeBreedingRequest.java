package com.curiousbees.common.gameplay.breeding;

import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.mutation.MutationDefinition;
import com.curiousbees.common.genetics.random.GeneticRandom;

import java.util.List;
import java.util.Objects;

public final class BeeBreedingRequest {

    private final Genome parentA;
    private final Genome parentB;
    private final List<MutationDefinition> availableMutations;
    private final GeneticRandom random;

    public BeeBreedingRequest(Genome parentA, Genome parentB,
                              List<MutationDefinition> availableMutations,
                              GeneticRandom random) {
        this.parentA = Objects.requireNonNull(parentA, "parentA must not be null");
        this.parentB = Objects.requireNonNull(parentB, "parentB must not be null");
        this.availableMutations = List.copyOf(
                Objects.requireNonNull(availableMutations, "availableMutations must not be null"));
        this.random = Objects.requireNonNull(random, "random must not be null");
    }

    public Genome parentA() { return parentA; }
    public Genome parentB() { return parentB; }
    public List<MutationDefinition> availableMutations() { return availableMutations; }
    public GeneticRandom random() { return random; }
}
