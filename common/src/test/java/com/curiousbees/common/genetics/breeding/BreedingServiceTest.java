package com.curiousbees.common.genetics.breeding;

import com.curiousbees.common.genetics.fixtures.AlleleFixtures;
import com.curiousbees.common.genetics.fixtures.GenomeFixtures;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.random.DeterministicGeneticRandom;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BreedingServiceTest {

    private BreedingService service;

    @BeforeEach
    void setUp() {
        service = new BreedingService();
    }

    // --- validation ---

    @Test
    void nullParentAFails() {
        assertThrows(NullPointerException.class,
                () -> service.breed(null, GenomeFixtures.pureMeadow(),
                        new DeterministicGeneticRandom().withBooleans(true)));
    }

    @Test
    void nullParentBFails() {
        assertThrows(NullPointerException.class,
                () -> service.breed(GenomeFixtures.pureMeadow(), null,
                        new DeterministicGeneticRandom().withBooleans(true)));
    }

    @Test
    void nullRandomFails() {
        assertThrows(NullPointerException.class,
                () -> service.breed(GenomeFixtures.pureMeadow(), GenomeFixtures.pureForest(), null));
    }

    @Test
    void incompatibleChromosomeSetsFail() {
        // pureMeadow has 5 chromosomes; build a genome with only SPECIES
        Genome speciesOnly = new Genome(java.util.Map.of(
                ChromosomeType.SPECIES,
                new com.curiousbees.common.genetics.model.GenePair(
                        AlleleFixtures.MEADOW, AlleleFixtures.MEADOW,
                        new DeterministicGeneticRandom().withBooleans(true))));
        assertThrows(IllegalArgumentException.class,
                () -> service.breed(GenomeFixtures.pureMeadow(), speciesOnly,
                        new DeterministicGeneticRandom().withBooleans(true)));
    }

    // --- deterministic inheritance ---

    @Test
    void pureMeadowCrossForestProducesHybridSpecies() {
        // parentA: Meadow/Meadow, parentB: Forest/Forest
        // fromA always picks first (true), fromB always picks first (true)
        // child species pair: Meadow + Forest
        // For 5 chromosomes: 2 booleans per chromosome (fromA, fromB) + 1 for GenePair resolution = 3 per chrom
        // We just need enough booleans; use JavaGeneticRandom with fixed seed for simplicity
        Genome parentA = GenomeFixtures.pureMeadow();
        Genome parentB = GenomeFixtures.pureForest();

        JavaGeneticRandom random = new JavaGeneticRandom(new Random(42));
        BreedingResult result = service.breed(parentA, parentB, random);
        Genome child = result.childGenome();

        // Pure Meadow x Pure Forest -> child must have one Meadow and one Forest species allele
        assertTrue(child.species().containsAllele(AlleleFixtures.MEADOW.id()));
        assertTrue(child.species().containsAllele(AlleleFixtures.FOREST.id()));
    }

    @Test
    void allMvpChromosomesAreCrossed() {
        Genome parentA = GenomeFixtures.pureMeadow();
        Genome parentB = GenomeFixtures.pureForest();
        JavaGeneticRandom random = new JavaGeneticRandom(new Random(0));
        BreedingResult result = service.breed(parentA, parentB, random);
        Genome child = result.childGenome();

        assertTrue(child.hasChromosome(ChromosomeType.SPECIES));
        assertTrue(child.hasChromosome(ChromosomeType.LIFESPAN));
        assertTrue(child.hasChromosome(ChromosomeType.PRODUCTIVITY));
        assertTrue(child.hasChromosome(ChromosomeType.FERTILITY));
        assertTrue(child.hasChromosome(ChromosomeType.FLOWER_TYPE));
    }

    @Test
    void parentsRemainUnchangedAfterBreeding() {
        Genome parentA = GenomeFixtures.pureMeadow();
        Genome parentB = GenomeFixtures.pureForest();
        String activeSpeciesA = parentA.getActiveAllele(ChromosomeType.SPECIES).id();
        String activeSpeciesB = parentB.getActiveAllele(ChromosomeType.SPECIES).id();

        service.breed(parentA, parentB, new JavaGeneticRandom(new Random(1)));

        assertEquals(activeSpeciesA, parentA.getActiveAllele(ChromosomeType.SPECIES).id());
        assertEquals(activeSpeciesB, parentB.getActiveAllele(ChromosomeType.SPECIES).id());
    }

    @Test
    void deterministicRandomSelectsExpectedAlleles() {
        // parentA: Meadow/Meadow, parentB: Forest/Forest
        // For SPECIES only: fromA = true (first=Meadow), fromB = false (second=Forest)
        // GenePair(Meadow, Forest) - both DOMINANT -> random=true -> Meadow active
        // We need 3 booleans per chromosome * 5 chromosomes = 15 booleans minimum
        // We'll just validate the SPECIES result using a controlled sequence
        Genome parentA = GenomeFixtures.pureMeadow();
        Genome parentB = GenomeFixtures.pureForest();

        // enough trues so first allele always wins from each parent and Meadow is active
        boolean[] booleans = new boolean[30];
        java.util.Arrays.fill(booleans, true);
        DeterministicGeneticRandom det = new DeterministicGeneticRandom().withBooleans(booleans);

        BreedingResult result = service.breed(parentA, parentB, det);
        Genome child = result.childGenome();

        assertEquals(AlleleFixtures.MEADOW.id(), child.getActiveAllele(ChromosomeType.SPECIES).id());
        assertTrue(child.species().containsAllele(AlleleFixtures.FOREST.id()));
    }

    // --- statistical distribution ---

    @Test
    void hybridCrossApproximates25_50_25Distribution() {
        // Parent A: Meadow/Forest, Parent B: Meadow/Forest
        // Expected: ~25% Meadow/Meadow, ~50% Meadow/Forest, ~25% Forest/Forest
        Genome parentA = GenomeFixtures.hybridMeadowForest();
        Genome parentB = GenomeFixtures.hybridMeadowForest();
        JavaGeneticRandom random = new JavaGeneticRandom(new Random(12345));

        int meadowMeadow = 0, hybrid = 0, forestForest = 0;
        int total = 10_000;

        for (int i = 0; i < total; i++) {
            Genome child = service.breed(parentA, parentB, random).childGenome();
            boolean hasMeadow = child.species().containsAllele(AlleleFixtures.MEADOW.id());
            boolean hasForest = child.species().containsAllele(AlleleFixtures.FOREST.id());

            if (hasMeadow && hasForest) {
                hybrid++;
            } else if (hasMeadow) {
                meadowMeadow++;
            } else {
                forestForest++;
            }
        }

        double tolerancePct = 0.05; // ±5%
        assertApprox(meadowMeadow, total, 0.25, tolerancePct, "Meadow/Meadow");
        assertApprox(hybrid,       total, 0.50, tolerancePct, "Meadow/Forest hybrid");
        assertApprox(forestForest, total, 0.25, tolerancePct, "Forest/Forest");
    }

    private void assertApprox(int count, int total, double expected, double tolerance, String label) {
        double actual = (double) count / total;
        assertTrue(Math.abs(actual - expected) <= tolerance,
                label + ": expected ~" + expected + " but got " + actual
                + " (" + count + "/" + total + ")");
    }
}
