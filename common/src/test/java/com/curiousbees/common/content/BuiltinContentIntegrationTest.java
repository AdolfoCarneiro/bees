package com.curiousbees.common.content;

import com.curiousbees.common.content.builtin.BuiltinBeeContent;
import com.curiousbees.common.content.builtin.BuiltinBeeSpecies;
import com.curiousbees.common.genetics.breeding.BreedingService;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.mutation.MutationResult;
import com.curiousbees.common.genetics.mutation.MutationService;
import com.curiousbees.common.genetics.random.DeterministicGeneticRandom;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration tests proving built-in Phase 2 content works with Phase 1 genetics core.
 */
class BuiltinContentIntegrationTest {

    private BreedingService breedingService;
    private MutationService mutationService;

    @BeforeEach
    void setUp() {
        breedingService = new BreedingService();
        mutationService = new MutationService();
    }

    private Genome defaultGenome(String speciesId) {
        return BuiltinBeeContent.createDefaultGenome(speciesId, new JavaGeneticRandom(new Random(0)));
    }

    // --- Meadow + Forest -> Cultivated ---

    @Test
    void meadowForestBreedingProducesValidChild() {
        Genome meadow = defaultGenome("curious_bees:species/meadow");
        Genome forest = defaultGenome("curious_bees:species/forest");
        Genome child = breedingService.breed(meadow, forest, new JavaGeneticRandom(new Random(1))).childGenome();
        assertTrue(child.hasChromosome(ChromosomeType.SPECIES));
        assertTrue(child.species().containsAllele(BuiltinBeeSpecies.SPECIES_MEADOW.id())
                || child.species().containsAllele(BuiltinBeeSpecies.SPECIES_FOREST.id()));
    }

    @Test
    void meadowForestForced100PercentProducesCultivated() {
        Genome meadow = defaultGenome("curious_bees:species/meadow");
        Genome forest = defaultGenome("curious_bees:species/forest");
        // breed
        Genome child = breedingService.breed(meadow, forest, new JavaGeneticRandom(new Random(2))).childGenome();

        // force mutation by using a double near 0 (below 0.12)
        MutationResult result = mutationService.evaluate(
                meadow, forest, child,
                BuiltinBeeContent.allMutations(),
                new DeterministicGeneticRandom().withDoubles(0.0).withBooleans(true));

        assertTrue(result.wasMutated());
        assertTrue(result.resultGenome().species()
                .containsAllele(BuiltinBeeSpecies.SPECIES_CULTIVATED.id()),
                "Mutated child must carry Cultivated allele");
    }

    // --- Forest + Arid -> Hardy ---

    @Test
    void forestAridBreedingProducesValidChild() {
        Genome forest = defaultGenome("curious_bees:species/forest");
        Genome arid   = defaultGenome("curious_bees:species/arid");
        Genome child  = breedingService.breed(forest, arid, new JavaGeneticRandom(new Random(3))).childGenome();
        assertTrue(child.hasChromosome(ChromosomeType.SPECIES));
    }

    @Test
    void forestAridForced100PercentProducesHardy() {
        Genome forest = defaultGenome("curious_bees:species/forest");
        Genome arid   = defaultGenome("curious_bees:species/arid");
        Genome child  = breedingService.breed(forest, arid, new JavaGeneticRandom(new Random(4))).childGenome();

        MutationResult result = mutationService.evaluate(
                forest, arid, child,
                BuiltinBeeContent.allMutations(),
                new DeterministicGeneticRandom().withDoubles(0.0).withBooleans(true));

        assertTrue(result.wasMutated());
        assertTrue(result.resultGenome().species()
                .containsAllele(BuiltinBeeSpecies.SPECIES_HARDY.id()),
                "Mutated child must carry Hardy allele");
    }

    // --- No-mutation pair: Meadow + Arid ---

    @Test
    void meadowAridProducesNoMvpMutation() {
        Genome meadow = defaultGenome("curious_bees:species/meadow");
        Genome arid   = defaultGenome("curious_bees:species/arid");
        Genome child  = breedingService.breed(meadow, arid, new JavaGeneticRandom(new Random(5))).childGenome();

        MutationResult result = mutationService.evaluate(
                meadow, arid, child,
                BuiltinBeeContent.allMutations(),
                new JavaGeneticRandom(new Random(5)));

        assertFalse(result.wasMutated(), "Meadow + Arid must not trigger any MVP mutation");
        assertSame(child, result.resultGenome());
    }

    // --- all 5 species produce valid genomes ---

    @Test
    void allFiveSpeciesProduceValidGenomes() {
        for (var def : BuiltinBeeContent.allSpecies()) {
            Genome g = BuiltinBeeContent.createDefaultGenome(def, new JavaGeneticRandom(new Random(0)));
            assertNotNull(g, def.id() + " produced null genome");
            assertTrue(g.hasChromosome(ChromosomeType.SPECIES), def.id() + " missing SPECIES");
            assertEquals(def.speciesAllele().id(),
                    g.getActiveAllele(ChromosomeType.SPECIES).id(),
                    def.id() + " active species mismatch");
        }
    }
}
