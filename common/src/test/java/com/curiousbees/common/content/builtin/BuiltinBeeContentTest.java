package com.curiousbees.common.content.builtin;

import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BuiltinBeeContentTest {

    private JavaGeneticRandom random() {
        return new JavaGeneticRandom(new Random(0));
    }

    @Test
    void lookupExistingSpeciesSucceeds() {
        var meadow = BuiltinBeeContent.findSpecies("curious_bees:species/meadow");
        assertTrue(meadow.isPresent());
        assertEquals("Meadow Bee", meadow.get().displayName());
    }

    @Test
    void lookupMissingSpeciesReturnsEmpty() {
        var result = BuiltinBeeContent.findSpecies("curious_bees:species/nonexistent");
        assertTrue(result.isEmpty());
    }

    @Test
    void getSpeciesThrowsForMissingId() {
        assertThrows(IllegalArgumentException.class,
                () -> BuiltinBeeContent.getSpecies("curious_bees:species/nonexistent"));
    }

    @Test
    void allSpeciesAreAccessible() {
        assertEquals(5, BuiltinBeeContent.allSpecies().size());
    }

    @Test
    void lookupExistingTraitSucceeds() {
        var trait = BuiltinBeeContent.findTrait("curious_bees:traits/lifespan/normal");
        assertTrue(trait.isPresent());
        assertEquals(ChromosomeType.LIFESPAN, trait.get().chromosomeType());
    }

    @Test
    void lookupMissingTraitReturnsEmpty() {
        assertTrue(BuiltinBeeContent.findTrait("curious_bees:traits/nonexistent").isEmpty());
    }

    @Test
    void allTraitsAreAccessible() {
        assertEquals(12, BuiltinBeeContent.allTraits().size());
    }

    @Test
    void allMutationsAreAccessible() {
        assertEquals(2, BuiltinBeeContent.allMutations().size());
    }

    @Test
    void createDefaultGenomeByIdSucceeds() {
        var genome = BuiltinBeeContent.createDefaultGenome("curious_bees:species/meadow", random());
        assertNotNull(genome);
        assertTrue(genome.hasChromosome(ChromosomeType.SPECIES));
    }

    @Test
    void createDefaultGenomeByDefinitionSucceeds() {
        var genome = BuiltinBeeContent.createDefaultGenome(BuiltinBeeSpecies.FOREST, random());
        assertNotNull(genome);
        assertEquals(BuiltinBeeSpecies.SPECIES_FOREST.id(),
                genome.getActiveAllele(ChromosomeType.SPECIES).id());
    }
}
