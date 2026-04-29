package com.curiousbees.common.gameplay.breeding;

import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.mutation.MutationDefinition;

import java.util.Objects;
import java.util.Optional;

public final class BeeBreedingOutcome {

    private final Genome childGenome;
    private final boolean mutationOccurred;
    private final MutationDefinition appliedMutation;

    private BeeBreedingOutcome(Genome childGenome, boolean mutationOccurred,
                               MutationDefinition appliedMutation) {
        this.childGenome = Objects.requireNonNull(childGenome, "childGenome must not be null");
        this.mutationOccurred = mutationOccurred;
        this.appliedMutation = appliedMutation;
    }

    public static BeeBreedingOutcome inherited(Genome childGenome) {
        return new BeeBreedingOutcome(childGenome, false, null);
    }

    public static BeeBreedingOutcome mutated(Genome childGenome, MutationDefinition mutation) {
        Objects.requireNonNull(mutation, "mutation must not be null");
        return new BeeBreedingOutcome(childGenome, true, mutation);
    }

    public Genome childGenome() { return childGenome; }
    public boolean mutationOccurred() { return mutationOccurred; }
    public Optional<MutationDefinition> appliedMutation() { return Optional.ofNullable(appliedMutation); }
}
