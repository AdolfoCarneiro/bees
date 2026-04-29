package com.curiousbees.common.gameplay.breeding;

import com.curiousbees.common.genetics.fixtures.AlleleFixtures;
import com.curiousbees.common.genetics.fixtures.GenomeFixtures;
import com.curiousbees.common.genetics.mutation.MutationDefinition;
import com.curiousbees.common.genetics.mutation.MutationResultMode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BeeBreedingOutcomeTest {

    private static final MutationDefinition MUTATION = new MutationDefinition(
            "id",
            AlleleFixtures.MEADOW.id(),
            AlleleFixtures.FOREST.id(),
            AlleleFixtures.CULTIVATED,
            0.5,
            MutationResultMode.PARTIAL);

    @Test
    void inheritedOutcomeHasNoMutation() {
        var outcome = BeeBreedingOutcome.inherited(GenomeFixtures.pureMeadow());
        assertFalse(outcome.mutationOccurred());
        assertTrue(outcome.appliedMutation().isEmpty());
        assertNotNull(outcome.childGenome());
    }

    @Test
    void mutatedOutcomeReportsMutation() {
        var outcome = BeeBreedingOutcome.mutated(GenomeFixtures.pureCultivated(), MUTATION);
        assertTrue(outcome.mutationOccurred());
        assertTrue(outcome.appliedMutation().isPresent());
        assertEquals(MUTATION, outcome.appliedMutation().get());
        assertNotNull(outcome.childGenome());
    }

    @Test
    void inheritedRejectsNullGenome() {
        assertThrows(NullPointerException.class, () -> BeeBreedingOutcome.inherited(null));
    }

    @Test
    void mutatedRejectsNullGenome() {
        assertThrows(NullPointerException.class, () -> BeeBreedingOutcome.mutated(null, MUTATION));
    }

    @Test
    void mutatedRejectsNullMutation() {
        assertThrows(NullPointerException.class, () ->
                BeeBreedingOutcome.mutated(GenomeFixtures.pureCultivated(), null));
    }
}
