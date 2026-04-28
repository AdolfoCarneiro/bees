package com.curiousbees.common.genetics.mutation;

import com.curiousbees.common.genetics.fixtures.AlleleFixtures;
import com.curiousbees.common.genetics.fixtures.GenomeFixtures;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.random.DeterministicGeneticRandom;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class MutationServiceTest {

    private MutationService service;

    // Meadow + Forest -> Cultivated
    private MutationDefinition meadowForestToCultivated(double chance, MutationResultMode mode) {
        return new MutationDefinition(
                "curious_bees:mutation/cultivated",
                AlleleFixtures.MEADOW.id(),
                AlleleFixtures.FOREST.id(),
                AlleleFixtures.CULTIVATED,
                chance,
                mode);
    }

    @BeforeEach
    void setUp() {
        service = new MutationService();
    }

    // --- MutationDefinition validation ---

    @Test
    void invalidChanceBelowZeroFails() {
        assertThrows(IllegalArgumentException.class,
                () -> meadowForestToCultivated(-0.01, MutationResultMode.PARTIAL));
    }

    @Test
    void invalidChanceAboveOneFails() {
        assertThrows(IllegalArgumentException.class,
                () -> meadowForestToCultivated(1.01, MutationResultMode.PARTIAL));
    }

    @Test
    void nullResultSpeciesFails() {
        assertThrows(NullPointerException.class,
                () -> new MutationDefinition("id", "a", "b", null, 0.5, MutationResultMode.PARTIAL));
    }

    @Test
    void nonSpeciesResultAlleleFails() {
        assertThrows(IllegalArgumentException.class,
                () -> new MutationDefinition(
                        "id",
                        AlleleFixtures.MEADOW.id(),
                        AlleleFixtures.FOREST.id(),
                        AlleleFixtures.PRODUCTIVITY_FAST, // wrong ChromosomeType
                        0.5,
                        MutationResultMode.PARTIAL));
    }

    @Test
    void nullDefinitionInListIsSkippedSafely() {
        Genome parentA = GenomeFixtures.pureMeadow();
        Genome parentB = GenomeFixtures.pureForest();
        Genome child = GenomeFixtures.hybridMeadowForest();

        // list contains a null entry followed by a real definition at 100%
        List<MutationDefinition> defs = new java.util.ArrayList<>();
        defs.add(null);
        defs.add(meadowForestToCultivated(1.0, MutationResultMode.PARTIAL));

        MutationResult result = service.evaluate(parentA, parentB, child, defs,
                new DeterministicGeneticRandom().withDoubles(0.0).withBooleans(true));

        assertTrue(result.wasMutated(), "Should mutate despite null entry in list");
    }

    // --- parent order insensitivity ---

    @Test
    void parentOrderDoesNotMatter() {
        MutationDefinition def = meadowForestToCultivated(1.0, MutationResultMode.PARTIAL);
        assertTrue(def.matches(AlleleFixtures.MEADOW.id(), AlleleFixtures.FOREST.id()));
        assertTrue(def.matches(AlleleFixtures.FOREST.id(), AlleleFixtures.MEADOW.id()));
    }

    // --- no matching mutation ---

    @Test
    void noMatchingMutationReturnsUnchangedChild() {
        Genome parentA = GenomeFixtures.pureMeadow();
        Genome parentB = GenomeFixtures.pureArid(); // Meadow+Arid has no definition
        Genome child = GenomeFixtures.hybridMeadowForest();
        List<MutationDefinition> defs = List.of(meadowForestToCultivated(1.0, MutationResultMode.PARTIAL));

        MutationResult result = service.evaluate(parentA, parentB, child, defs,
                new DeterministicGeneticRandom().withDoubles(0.0));

        assertFalse(result.wasMutated());
        assertSame(child, result.resultGenome());
    }

    // --- chance ---

    @Test
    void zeroChanceNeverMutates() {
        Genome parentA = GenomeFixtures.pureMeadow();
        Genome parentB = GenomeFixtures.pureForest();
        Genome child = GenomeFixtures.hybridMeadowForest();
        MutationDefinition def = meadowForestToCultivated(0.0, MutationResultMode.PARTIAL);

        for (int i = 0; i < 1000; i++) {
            MutationResult result = service.evaluate(parentA, parentB, child, List.of(def),
                    new JavaGeneticRandom(new Random(i)));
            assertFalse(result.wasMutated(), "Should never mutate at 0%");
        }
    }

    @Test
    void hundredPercentChanceAlwaysMutates() {
        Genome parentA = GenomeFixtures.pureMeadow();
        Genome parentB = GenomeFixtures.pureForest();
        Genome child = GenomeFixtures.hybridMeadowForest();
        MutationDefinition def = meadowForestToCultivated(1.0, MutationResultMode.PARTIAL);

        for (int i = 0; i < 100; i++) {
            MutationResult result = service.evaluate(parentA, parentB, child, List.of(def),
                    new JavaGeneticRandom(new Random(i)));
            assertTrue(result.wasMutated(), "Should always mutate at 100%");
        }
    }

    // --- PARTIAL mutation ---

    @Test
    void partialMutationReplacesActiveKeepsInactive() {
        // parentA=Meadow, parentB=Forest -> match
        // child has Meadow(active)/Forest(inactive) species
        // PARTIAL -> Cultivated replaces active -> new pair: Cultivated/Forest
        Genome parentA = GenomeFixtures.pureMeadow();
        Genome parentB = GenomeFixtures.pureForest();
        Genome child = GenomeFixtures.hybridMeadowForest(); // active=Meadow, inactive=Forest

        MutationDefinition def = meadowForestToCultivated(1.0, MutationResultMode.PARTIAL);

        // GenePair(Cultivated, Forest) - both DOMINANT -> need one boolean for resolution
        MutationResult result = service.evaluate(parentA, parentB, child, List.of(def),
                new DeterministicGeneticRandom().withDoubles(0.0).withBooleans(true));

        assertTrue(result.wasMutated());
        assertTrue(result.appliedMutation().isPresent());

        Genome mutated = result.resultGenome();
        assertTrue(mutated.species().containsAllele(AlleleFixtures.CULTIVATED.id()),
                "Child should contain Cultivated allele");
        assertTrue(mutated.species().containsAllele(AlleleFixtures.FOREST.id()),
                "Child should retain Forest inactive allele");
    }

    // --- FULL mutation ---

    @Test
    void fullMutationReplacesBothAlleles() {
        Genome parentA = GenomeFixtures.pureMeadow();
        Genome parentB = GenomeFixtures.pureForest();
        Genome child = GenomeFixtures.hybridMeadowForest();

        MutationDefinition def = meadowForestToCultivated(1.0, MutationResultMode.FULL);

        // GenePair(Cultivated, Cultivated) - same allele same dominance -> one boolean
        MutationResult result = service.evaluate(parentA, parentB, child, List.of(def),
                new DeterministicGeneticRandom().withDoubles(0.0).withBooleans(true));

        assertTrue(result.wasMutated());
        Genome mutated = result.resultGenome();
        assertTrue(mutated.species().isPurebred(), "FULL mutation should produce purebred result");
        assertEquals(AlleleFixtures.CULTIVATED.id(), mutated.getActiveAllele(ChromosomeType.SPECIES).id());
    }

    // --- immutability ---

    @Test
    void parentGenomesUnchangedAfterMutation() {
        Genome parentA = GenomeFixtures.pureMeadow();
        Genome parentB = GenomeFixtures.pureForest();
        Genome child = GenomeFixtures.hybridMeadowForest();
        String originalActiveA = parentA.getActiveAllele(ChromosomeType.SPECIES).id();
        String originalActiveB = parentB.getActiveAllele(ChromosomeType.SPECIES).id();

        service.evaluate(parentA, parentB, child,
                List.of(meadowForestToCultivated(1.0, MutationResultMode.PARTIAL)),
                new DeterministicGeneticRandom().withDoubles(0.0).withBooleans(true));

        assertEquals(originalActiveA, parentA.getActiveAllele(ChromosomeType.SPECIES).id());
        assertEquals(originalActiveB, parentB.getActiveAllele(ChromosomeType.SPECIES).id());
    }

    @Test
    void childGenomeUnchangedWhenMutationProducesNewGenome() {
        Genome child = GenomeFixtures.hybridMeadowForest();
        String originalActive = child.getActiveAllele(ChromosomeType.SPECIES).id();

        service.evaluate(GenomeFixtures.pureMeadow(), GenomeFixtures.pureForest(), child,
                List.of(meadowForestToCultivated(1.0, MutationResultMode.PARTIAL)),
                new DeterministicGeneticRandom().withDoubles(0.0).withBooleans(true));

        assertEquals(originalActive, child.getActiveAllele(ChromosomeType.SPECIES).id());
    }

    // --- appliedMutation metadata ---

    @Test
    void noMutationHasEmptyAppliedMutation() {
        Genome parentA = GenomeFixtures.pureMeadow();
        Genome parentB = GenomeFixtures.pureArid();
        MutationResult result = service.evaluate(parentA, parentB, GenomeFixtures.pureMeadow(),
                List.of(meadowForestToCultivated(1.0, MutationResultMode.PARTIAL)),
                new DeterministicGeneticRandom().withDoubles(0.5));
        assertTrue(result.appliedMutation().isEmpty());
    }

    @Test
    void mutatedResultContainsAppliedDefinition() {
        MutationDefinition def = meadowForestToCultivated(1.0, MutationResultMode.PARTIAL);
        MutationResult result = service.evaluate(
                GenomeFixtures.pureMeadow(), GenomeFixtures.pureForest(),
                GenomeFixtures.hybridMeadowForest(), List.of(def),
                new DeterministicGeneticRandom().withDoubles(0.0).withBooleans(true));
        assertTrue(result.appliedMutation().isPresent());
        assertEquals(def.id(), result.appliedMutation().get().id());
    }
}
