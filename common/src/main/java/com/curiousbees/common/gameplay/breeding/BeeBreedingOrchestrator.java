package com.curiousbees.common.gameplay.breeding;

import com.curiousbees.common.genetics.breeding.BreedingService;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.mutation.MutationResult;
import com.curiousbees.common.genetics.mutation.MutationService;

import java.util.Objects;

public final class BeeBreedingOrchestrator {

    private final BreedingService breedingService;
    private final MutationService mutationService;

    public BeeBreedingOrchestrator(BreedingService breedingService, MutationService mutationService) {
        this.breedingService = Objects.requireNonNull(breedingService, "breedingService must not be null");
        this.mutationService = Objects.requireNonNull(mutationService, "mutationService must not be null");
    }

    public BeeBreedingOutcome breed(BeeBreedingRequest request) {
        Objects.requireNonNull(request, "request must not be null");

        Genome childGenome = breedingService.breed(
                request.parentA(), request.parentB(), request.random()).childGenome();

        MutationResult mutation = mutationService.evaluate(
                request.parentA(), request.parentB(), childGenome,
                request.availableMutations(), request.random());

        if (mutation.wasMutated()) {
            return BeeBreedingOutcome.mutated(
                    mutation.resultGenome(),
                    mutation.appliedMutation().orElseThrow());
        }
        return BeeBreedingOutcome.inherited(mutation.resultGenome());
    }
}
