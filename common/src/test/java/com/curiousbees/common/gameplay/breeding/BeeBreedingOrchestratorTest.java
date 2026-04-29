package com.curiousbees.common.gameplay.breeding;

import com.curiousbees.common.genetics.breeding.BreedingService;
import com.curiousbees.common.genetics.fixtures.AlleleFixtures;
import com.curiousbees.common.genetics.fixtures.GenomeFixtures;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.mutation.MutationDefinition;
import com.curiousbees.common.genetics.mutation.MutationResultMode;
import com.curiousbees.common.genetics.mutation.MutationService;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BeeBreedingOrchestratorTest {

    private BeeBreedingOrchestrator orchestrator;

    private static final MutationDefinition MEADOW_FOREST_TO_CULTIVATED = new MutationDefinition(
            "test:cultivated",
            AlleleFixtures.MEADOW.id(),
            AlleleFixtures.FOREST.id(),
            AlleleFixtures.CULTIVATED,
            1.0,   // 100% for deterministic tests
            MutationResultMode.PARTIAL);

    @BeforeEach
    void setUp() {
        orchestrator = new BeeBreedingOrchestrator(new BreedingService(), new MutationService());
    }

    @Test
    void returnsInheritedOutcomeWhenNoMutationApplies() {
        // 0% mutation chance -> never mutates
        MutationDefinition neverMutates = new MutationDefinition(
                "test:never", AlleleFixtures.MEADOW.id(), AlleleFixtures.FOREST.id(),
                AlleleFixtures.CULTIVATED, 0.0, MutationResultMode.PARTIAL);

        var request = new BeeBreedingRequest(
                GenomeFixtures.pureMeadow(), GenomeFixtures.pureForest(),
                List.of(neverMutates),
                new JavaGeneticRandom(new Random(42)));

        BeeBreedingOutcome outcome = orchestrator.breed(request);

        assertFalse(outcome.mutationOccurred());
        assertTrue(outcome.appliedMutation().isEmpty());
        assertNotNull(outcome.childGenome());
    }

    @Test
    void reportsMutationWhenMutationApplies() {
        // Mutation chance is 1.0 -> mutation always fires regardless of random seed.
        // JavaGeneticRandom avoids having to enumerate every boolean the breeding pipeline consumes.
        var request = new BeeBreedingRequest(
                GenomeFixtures.pureMeadow(), GenomeFixtures.pureForest(),
                List.of(MEADOW_FOREST_TO_CULTIVATED),
                new JavaGeneticRandom(new Random(42)));

        BeeBreedingOutcome outcome = orchestrator.breed(request);

        assertTrue(outcome.mutationOccurred());
        assertTrue(outcome.appliedMutation().isPresent());
        // Child's species chromosome must contain CULTIVATED (active or inactive depending on random)
        assertTrue(outcome.childGenome().getGenePair(ChromosomeType.SPECIES)
                .containsAllele(AlleleFixtures.CULTIVATED.id()));
    }

    @Test
    void parentGenomesAreNotMutatedByBreeding() {
        Genome parentA = GenomeFixtures.pureMeadow();
        Genome parentB = GenomeFixtures.pureForest();
        String originalA = parentA.getActiveAllele(ChromosomeType.SPECIES).id();
        String originalB = parentB.getActiveAllele(ChromosomeType.SPECIES).id();

        var request = new BeeBreedingRequest(parentA, parentB,
                List.of(MEADOW_FOREST_TO_CULTIVATED),
                new JavaGeneticRandom(new Random(1)));
        orchestrator.breed(request);

        assertEquals(originalA, parentA.getActiveAllele(ChromosomeType.SPECIES).id());
        assertEquals(originalB, parentB.getActiveAllele(ChromosomeType.SPECIES).id());
    }

    @Test
    void rejectsNullRequest() {
        assertThrows(NullPointerException.class, () -> orchestrator.breed(null));
    }

    @Test
    void emptyMutationListProducesInheritedChild() {
        var request = new BeeBreedingRequest(
                GenomeFixtures.pureMeadow(), GenomeFixtures.pureForest(),
                List.of(),
                new JavaGeneticRandom(new Random(7)));

        BeeBreedingOutcome outcome = orchestrator.breed(request);

        assertFalse(outcome.mutationOccurred());
        assertNotNull(outcome.childGenome());
    }
}
