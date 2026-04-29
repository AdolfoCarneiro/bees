package com.curiousbees.common.gameplay.breeding;

import com.curiousbees.common.genetics.fixtures.GenomeFixtures;
import com.curiousbees.common.genetics.mutation.MutationDefinition;
import com.curiousbees.common.genetics.mutation.MutationResultMode;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static com.curiousbees.common.genetics.fixtures.AlleleFixtures.CULTIVATED;
import static com.curiousbees.common.genetics.fixtures.AlleleFixtures.MEADOW;
import static com.curiousbees.common.genetics.fixtures.AlleleFixtures.FOREST;
import static org.junit.jupiter.api.Assertions.*;

class BeeBreedingRequestTest {

    private final JavaGeneticRandom random = new JavaGeneticRandom(new Random(42));

    @Test
    void constructsWithValidArguments() {
        var request = new BeeBreedingRequest(
                GenomeFixtures.pureMeadow(),
                GenomeFixtures.pureForest(),
                List.of(),
                random);

        assertNotNull(request.parentA());
        assertNotNull(request.parentB());
        assertNotNull(request.availableMutations());
        assertNotNull(request.random());
    }

    @Test
    void rejectsNullParentA() {
        assertThrows(NullPointerException.class, () ->
                new BeeBreedingRequest(null, GenomeFixtures.pureForest(), List.of(), random));
    }

    @Test
    void rejectsNullParentB() {
        assertThrows(NullPointerException.class, () ->
                new BeeBreedingRequest(GenomeFixtures.pureMeadow(), null, List.of(), random));
    }

    @Test
    void rejectsNullMutations() {
        assertThrows(NullPointerException.class, () ->
                new BeeBreedingRequest(GenomeFixtures.pureMeadow(), GenomeFixtures.pureForest(), null, random));
    }

    @Test
    void rejectsNullRandom() {
        assertThrows(NullPointerException.class, () ->
                new BeeBreedingRequest(GenomeFixtures.pureMeadow(), GenomeFixtures.pureForest(), List.of(), null));
    }

    @Test
    void mutationsListIsDefensiveCopy() {
        var def = new MutationDefinition("id", MEADOW.id(), FOREST.id(), CULTIVATED, 0.5, MutationResultMode.PARTIAL);
        var mutable = new java.util.ArrayList<>(List.of(def));
        var request = new BeeBreedingRequest(
                GenomeFixtures.pureMeadow(), GenomeFixtures.pureForest(), mutable, random);
        mutable.clear();
        assertEquals(1, request.availableMutations().size());
    }
}
