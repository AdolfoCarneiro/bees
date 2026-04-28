package com.curiousbees.common.genetics;

import com.curiousbees.common.genetics.breeding.BreedingService;
import com.curiousbees.common.genetics.fixtures.AlleleFixtures;
import com.curiousbees.common.genetics.fixtures.GenomeFixtures;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.mutation.MutationDefinition;
import com.curiousbees.common.genetics.mutation.MutationResult;
import com.curiousbees.common.genetics.mutation.MutationResultMode;
import com.curiousbees.common.genetics.mutation.MutationService;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Simulation-level tests for the genetics core.
 * Each test runs many crosses to validate statistical and deterministic behavior.
 * Probabilistic assertions use explicit tolerances to avoid flakiness.
 */
class GeneticsSimulationTest {

    private static final int LARGE = 10_000;
    private static final double TOLERANCE = 0.05; // ±5% absolute

    private BreedingService breedingService;
    private MutationService mutationService;

    @BeforeEach
    void setUp() {
        breedingService = new BreedingService();
        mutationService = new MutationService();
    }

    // -------------------------------------------------------------------
    // Scenario 1 — Pure Species Cross
    // Parent A: Meadow/Meadow  x  Parent B: Forest/Forest
    // Expected: 100% of children have one Meadow allele and one Forest allele
    // -------------------------------------------------------------------

    @Test
    void scenario1_pureCross_always_producesHybrid() {
        Genome parentA = GenomeFixtures.pureMeadow();
        Genome parentB = GenomeFixtures.pureForest();
        JavaGeneticRandom random = new JavaGeneticRandom(new Random(1));

        for (int i = 0; i < LARGE; i++) {
            Genome child = breedingService.breed(parentA, parentB, random).childGenome();
            assertTrue(child.species().containsAllele(AlleleFixtures.MEADOW.id()),
                    "Child must carry a Meadow allele");
            assertTrue(child.species().containsAllele(AlleleFixtures.FOREST.id()),
                    "Child must carry a Forest allele");
        }
    }

    // -------------------------------------------------------------------
    // Scenario 2 — Hybrid Species Cross
    // Parent A: Meadow/Forest  x  Parent B: Meadow/Forest
    // Expected: ~25% Meadow/Meadow, ~50% Meadow/Forest, ~25% Forest/Forest
    // -------------------------------------------------------------------

    @Test
    void scenario2_hybridCross_approximates_25_50_25() {
        Genome parentA = GenomeFixtures.hybridMeadowForest();
        Genome parentB = GenomeFixtures.hybridMeadowForest();
        JavaGeneticRandom random = new JavaGeneticRandom(new Random(2));

        int meadowMeadow = 0, hybrid = 0, forestForest = 0;

        for (int i = 0; i < LARGE; i++) {
            Genome child = breedingService.breed(parentA, parentB, random).childGenome();
            boolean hasMeadow = child.species().containsAllele(AlleleFixtures.MEADOW.id());
            boolean hasForest = child.species().containsAllele(AlleleFixtures.FOREST.id());

            if (hasMeadow && hasForest) hybrid++;
            else if (hasMeadow)         meadowMeadow++;
            else                        forestForest++;
        }

        assertApprox(meadowMeadow, LARGE, 0.25, TOLERANCE, "Meadow/Meadow");
        assertApprox(hybrid,       LARGE, 0.50, TOLERANCE, "Meadow/Forest hybrid");
        assertApprox(forestForest, LARGE, 0.25, TOLERANCE, "Forest/Forest");
    }

    // -------------------------------------------------------------------
    // Scenario 3 — 0% Mutation
    // Mutation: Meadow + Forest -> Cultivated at 0%
    // Expected: mutation never occurs
    // -------------------------------------------------------------------

    @Test
    void scenario3_zeroPercent_neverMutates() {
        Genome parentA = GenomeFixtures.pureMeadow();
        Genome parentB = GenomeFixtures.pureForest();
        MutationDefinition def = cultivatedDef(0.0, MutationResultMode.PARTIAL);
        JavaGeneticRandom random = new JavaGeneticRandom(new Random(3));

        for (int i = 0; i < LARGE; i++) {
            Genome child = breedingService.breed(parentA, parentB, random).childGenome();
            MutationResult result = mutationService.evaluate(parentA, parentB, child, List.of(def), random);
            assertFalse(result.wasMutated(), "0% chance must never mutate (iteration " + i + ")");
        }
    }

    // -------------------------------------------------------------------
    // Scenario 4 — 100% Mutation
    // Mutation: Meadow + Forest -> Cultivated at 100%
    // Expected: mutation always occurs when parents match
    // -------------------------------------------------------------------

    @Test
    void scenario4_hundredPercent_alwaysMutates() {
        Genome parentA = GenomeFixtures.pureMeadow();
        Genome parentB = GenomeFixtures.pureForest();
        MutationDefinition def = cultivatedDef(1.0, MutationResultMode.PARTIAL);
        JavaGeneticRandom random = new JavaGeneticRandom(new Random(4));

        for (int i = 0; i < LARGE; i++) {
            Genome child = breedingService.breed(parentA, parentB, random).childGenome();
            MutationResult result = mutationService.evaluate(parentA, parentB, child, List.of(def), random);
            assertTrue(result.wasMutated(), "100% chance must always mutate (iteration " + i + ")");
            assertTrue(result.resultGenome().species().containsAllele(AlleleFixtures.CULTIVATED.id()),
                    "Mutated child must carry Cultivated allele");
        }
    }

    // -------------------------------------------------------------------
    // Scenario 5 — MVP Mutation Chance Smoke Test
    // Mutation: Meadow + Forest -> Cultivated at 12%
    // Expected: mutation rate approximately 12% (±5%)
    // -------------------------------------------------------------------

    @Test
    void scenario5_twelvePercent_approximateRate() {
        Genome parentA = GenomeFixtures.pureMeadow();
        Genome parentB = GenomeFixtures.pureForest();
        MutationDefinition def = cultivatedDef(0.12, MutationResultMode.PARTIAL);
        JavaGeneticRandom random = new JavaGeneticRandom(new Random(5));

        int mutatedCount = 0;

        for (int i = 0; i < LARGE; i++) {
            Genome child = breedingService.breed(parentA, parentB, random).childGenome();
            MutationResult result = mutationService.evaluate(parentA, parentB, child, List.of(def), random);
            if (result.wasMutated()) mutatedCount++;
        }

        assertApprox(mutatedCount, LARGE, 0.12, TOLERANCE, "Cultivated mutation at 12%");
    }

    // -------------------------------------------------------------------
    // Bonus — Hardy mutation smoke test (Forest + Arid -> Hardy at 8%)
    // -------------------------------------------------------------------

    @Test
    void bonus_hardyMutation_approximateRate() {
        Genome parentA = GenomeFixtures.pureForest();
        Genome parentB = GenomeFixtures.pureArid();
        MutationDefinition def = new MutationDefinition(
                "curious_bees:mutation/hardy",
                AlleleFixtures.FOREST.id(),
                AlleleFixtures.ARID.id(),
                AlleleFixtures.HARDY,
                0.08,
                MutationResultMode.PARTIAL);
        JavaGeneticRandom random = new JavaGeneticRandom(new Random(6));

        int mutatedCount = 0;

        for (int i = 0; i < LARGE; i++) {
            Genome child = breedingService.breed(parentA, parentB, random).childGenome();
            MutationResult result = mutationService.evaluate(parentA, parentB, child, List.of(def), random);
            if (result.wasMutated()) mutatedCount++;
        }

        assertApprox(mutatedCount, LARGE, 0.08, TOLERANCE, "Hardy mutation at 8%");
    }

    // -------------------------------------------------------------------
    // helpers
    // -------------------------------------------------------------------

    private MutationDefinition cultivatedDef(double chance, MutationResultMode mode) {
        return new MutationDefinition(
                "curious_bees:mutation/cultivated",
                AlleleFixtures.MEADOW.id(),
                AlleleFixtures.FOREST.id(),
                AlleleFixtures.CULTIVATED,
                chance,
                mode);
    }

    private void assertApprox(int count, int total, double expected, double tolerance, String label) {
        double actual = (double) count / total;
        assertTrue(Math.abs(actual - expected) <= tolerance,
                label + ": expected ~" + expected
                + " (±" + tolerance + ") but got " + actual
                + " (" + count + "/" + total + ")");
    }
}
