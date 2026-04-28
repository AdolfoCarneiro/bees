package com.curiousbees.common.genetics.mutation;

import com.curiousbees.common.genetics.model.Genome;

import java.util.Objects;
import java.util.Optional;

public final class MutationResult {

    private final Genome resultGenome;
    private final boolean mutated;
    private final MutationDefinition appliedMutation;

    private MutationResult(Genome resultGenome, boolean mutated, MutationDefinition appliedMutation) {
        this.resultGenome = Objects.requireNonNull(resultGenome, "resultGenome must not be null");
        this.mutated = mutated;
        this.appliedMutation = appliedMutation;
    }

    public static MutationResult noMutation(Genome genome) {
        return new MutationResult(genome, false, null);
    }

    public static MutationResult mutated(Genome genome, MutationDefinition definition) {
        Objects.requireNonNull(definition, "definition must not be null");
        return new MutationResult(genome, true, definition);
    }

    public Genome resultGenome() { return resultGenome; }
    public boolean wasMutated() { return mutated; }
    public Optional<MutationDefinition> appliedMutation() { return Optional.ofNullable(appliedMutation); }
}
