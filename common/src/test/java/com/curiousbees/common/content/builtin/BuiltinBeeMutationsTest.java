package com.curiousbees.common.content.builtin;

import com.curiousbees.common.genetics.mutation.MutationDefinition;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static com.curiousbees.common.content.builtin.BuiltinBeeMutations.*;
import static com.curiousbees.common.content.builtin.BuiltinBeeSpecies.*;
import static org.junit.jupiter.api.Assertions.*;

class BuiltinBeeMutationsTest {

    @Test
    void cultivatedMutationExists() {
        assertNotNull(CULTIVATED_FROM_MEADOW_FOREST);
        assertEquals("curious_bees:mutations/cultivated_from_meadow_forest",
                CULTIVATED_FROM_MEADOW_FOREST.id());
    }

    @Test
    void hardyMutationExists() {
        assertNotNull(HARDY_FROM_FOREST_ARID);
        assertEquals("curious_bees:mutations/hardy_from_forest_arid",
                HARDY_FROM_FOREST_ARID.id());
    }

    @Test
    void mutationIdsAreUnique() {
        Set<String> ids = new HashSet<>();
        for (MutationDefinition def : BuiltinBeeMutations.ALL) {
            assertTrue(ids.add(def.id()), "Duplicate mutation id: " + def.id());
        }
    }

    @Test
    void cultivatedParentSpeciesAreValid() {
        assertTrue(CULTIVATED_FROM_MEADOW_FOREST.matches(SPECIES_MEADOW.id(), SPECIES_FOREST.id()));
        assertTrue(CULTIVATED_FROM_MEADOW_FOREST.matches(SPECIES_FOREST.id(), SPECIES_MEADOW.id()));
    }

    @Test
    void hardyParentSpeciesAreValid() {
        assertTrue(HARDY_FROM_FOREST_ARID.matches(SPECIES_FOREST.id(), SPECIES_ARID.id()));
        assertTrue(HARDY_FROM_FOREST_ARID.matches(SPECIES_ARID.id(), SPECIES_FOREST.id()));
    }

    @Test
    void cultivatedResultSpeciesIsValid() {
        assertEquals(SPECIES_CULTIVATED.id(), CULTIVATED_FROM_MEADOW_FOREST.resultSpeciesAllele().id());
    }

    @Test
    void hardyResultSpeciesIsValid() {
        assertEquals(SPECIES_HARDY.id(), HARDY_FROM_FOREST_ARID.resultSpeciesAllele().id());
    }

    @Test
    void mutationChancesAreWithinRange() {
        for (MutationDefinition def : BuiltinBeeMutations.ALL) {
            assertTrue(def.baseChance() >= 0.0 && def.baseChance() <= 1.0,
                    def.id() + " chance out of range: " + def.baseChance());
        }
    }

    @Test
    void cultivatedChanceMatchesSpec() {
        assertEquals(0.12, CULTIVATED_FROM_MEADOW_FOREST.baseChance(), 0.0001);
    }

    @Test
    void hardyChanceMatchesSpec() {
        assertEquals(0.08, HARDY_FROM_FOREST_ARID.baseChance(), 0.0001);
    }

    @Test
    void allListHasTwoEntries() {
        assertEquals(2, BuiltinBeeMutations.ALL.size());
    }
}
