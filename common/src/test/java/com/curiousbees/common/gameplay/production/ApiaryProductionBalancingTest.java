package com.curiousbees.common.gameplay.production;

import com.curiousbees.common.content.builtin.BuiltinBeeSpecies;
import com.curiousbees.common.content.builtin.DefaultGenomeFactory;
import com.curiousbees.common.content.frames.BuiltinFrameModifiers;
import com.curiousbees.common.content.products.BuiltinProductionDefinitions;
import com.curiousbees.common.content.species.BeeSpeciesDefinition;
import com.curiousbees.common.gameplay.frames.FrameModifiers;
import com.curiousbees.common.genetics.fixtures.GenomeFixtures;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Simulation tests documenting the Phase 7J production balancing baseline.
 *
 * Each test runs ROLLS production rolls and asserts the observed rate falls
 * within TOLERANCE of the theoretical expected value. Tolerance is wide enough
 * to avoid flakiness while still catching gross configuration errors.
 *
 * Frame baseline values (Phase 7F tuning):
 *   Basic Frame:       mutation x1.03 | production x1.03
 *   Mutation Frame:    mutation x1.18 | production x1.00
 *   Productivity Frame: mutation x1.00 | production x1.18
 */
class ApiaryProductionBalancingTest {

    private static final int    ROLLS     = 10_000;
    private static final double TOLERANCE = 0.07;   // ±7 percentage points
    private static final long   SEED      = 12345L;

    private final ProductionResolver resolver = new ProductionResolver();

    // --- pure-species genomes using builtin trait IDs ---

    private Genome pureGenome(BeeSpeciesDefinition species) {
        return DefaultGenomeFactory.createDefault(
                species, new JavaGeneticRandom(new Random(SEED)));
    }

    /**
     * Fraction of ROLLS that produced at least one item with the given outputId,
     * using the supplied external production multiplier.
     */
    private double hitRate(Genome genome, double frameProductionMultiplier, String outputId) {
        JavaGeneticRandom random = new JavaGeneticRandom(new Random(SEED));
        int hits = 0;
        for (int i = 0; i < ROLLS; i++) {
            ProductionResult result = resolver.resolve(
                    genome,
                    BuiltinProductionDefinitions.BY_SPECIES_ID,
                    random,
                    frameProductionMultiplier);
            boolean found = result.generatedOutputs().stream()
                    .anyMatch(o -> o.outputId().equals(outputId));
            if (found) {
                hits++;
            }
        }
        return (double) hits / ROLLS;
    }

    // --- no-frame baselines ---

    @Test
    void pureMeadowNoFramesHitsEightyPercentBase() {
        // meadow_comb: 80% base, normal productivity (x1.0), no frame
        double rate = hitRate(pureGenome(BuiltinBeeSpecies.MEADOW), 1.0,
                "curiousbees:meadow_comb");
        assertEquals(0.80, rate, TOLERANCE,
                "Pure Meadow without frames should produce at ~80%");
    }

    @Test
    void pureForestNoFramesHitsEightyPercentBase() {
        double rate = hitRate(pureGenome(BuiltinBeeSpecies.FOREST), 1.0,
                "curiousbees:forest_comb");
        assertEquals(0.80, rate, TOLERANCE,
                "Pure Forest without frames should produce at ~80%");
    }

    @Test
    void pureAridNoFramesHitsEightyPercentBase() {
        // Arid has SLOW productivity as active when active is NORMAL due to dominance,
        // but ARID definition has pair(PRODUCTIVITY_SLOW, PRODUCTIVITY_NORMAL):
        // SLOW=RECESSIVE, NORMAL=DOMINANT -> active = NORMAL (1.0x)
        double rate = hitRate(pureGenome(BuiltinBeeSpecies.ARID), 1.0,
                "curiousbees:arid_comb");
        assertEquals(0.80, rate, TOLERANCE,
                "Pure Arid without frames should produce at ~80% (NORMAL productivity wins)");
    }

    @Test
    void pureCultivatedNoFramesHitsNinetyPercentBase() {
        // cultivated_comb: 90% base, active productivity = NORMAL (FAST is RECESSIVE, NORMAL wins)
        double rate = hitRate(pureGenome(BuiltinBeeSpecies.CULTIVATED), 1.0,
                "curiousbees:cultivated_comb");
        assertEquals(0.90, rate, TOLERANCE,
                "Pure Cultivated without frames should produce at ~90%");
    }

    @Test
    void pureCultivatedHasHigherBaseRateThanPureMeadow() {
        double meadowRate     = hitRate(pureGenome(BuiltinBeeSpecies.MEADOW),     1.0, "curiousbees:meadow_comb");
        double cultivatedRate = hitRate(pureGenome(BuiltinBeeSpecies.CULTIVATED), 1.0, "curiousbees:cultivated_comb");
        assertTrue(cultivatedRate > meadowRate,
                "Cultivated (90% base) should produce more often than Meadow (80% base)");
    }

    // --- frame multiplier effects ---

    @Test
    void productivityFrameBoostsMeadowRateToNinetyFourPercent() {
        // 80% * 1.18 = 94.4%
        double rate = hitRate(pureGenome(BuiltinBeeSpecies.MEADOW),
                BuiltinFrameModifiers.PRODUCTIVITY.productionMultiplier(),
                "curiousbees:meadow_comb");
        assertEquals(0.944, rate, TOLERANCE,
                "Productivity frame (x1.18) should push Meadow to ~94.4%");
    }

    @Test
    void basicFrameBoostsMeadowRateSlightly() {
        // 80% * 1.03 = 82.4%
        double rate = hitRate(pureGenome(BuiltinBeeSpecies.MEADOW),
                BuiltinFrameModifiers.BASIC.productionMultiplier(),
                "curiousbees:meadow_comb");
        assertEquals(0.824, rate, TOLERANCE,
                "Basic frame (x1.03) should push Meadow to ~82.4%");
    }

    @Test
    void mutationFrameDoesNotChangeProductionRate() {
        // Mutation frame: production multiplier = 1.00 — no production effect
        Genome meadow = pureGenome(BuiltinBeeSpecies.MEADOW);
        double noFrameRate      = hitRate(meadow, 1.0, "curiousbees:meadow_comb");
        double mutationFrameRate = hitRate(meadow,
                BuiltinFrameModifiers.MUTATION.productionMultiplier(),
                "curiousbees:meadow_comb");
        assertEquals(noFrameRate, mutationFrameRate, TOLERANCE,
                "Mutation frame (x1.00 production) must not change production rate");
    }

    @Test
    void allThreeFramesCombinedMaximizesMeadowProductionRate() {
        // Production multiplier = 1.03 * 1.00 * 1.18 = 1.2154
        // 80% * 1.2154 = 97.2%
        FrameModifiers.CombinedFrameModifier combined = FrameModifiers.combine(
                List.of(
                        BuiltinFrameModifiers.BASIC,
                        BuiltinFrameModifiers.MUTATION,
                        BuiltinFrameModifiers.PRODUCTIVITY));
        double rate = hitRate(pureGenome(BuiltinBeeSpecies.MEADOW),
                combined.productionMultiplier(),
                "curiousbees:meadow_comb");
        assertEquals(0.972, rate, TOLERANCE,
                "All 3 frames combined should push Meadow to ~97.2%");
    }

    @Test
    void productivityFrameBoostsMoreThanBasicFrame() {
        Genome meadow = pureGenome(BuiltinBeeSpecies.MEADOW);
        double basicRate        = hitRate(meadow, BuiltinFrameModifiers.BASIC.productionMultiplier(),        "curiousbees:meadow_comb");
        double productivityRate = hitRate(meadow, BuiltinFrameModifiers.PRODUCTIVITY.productionMultiplier(), "curiousbees:meadow_comb");
        assertTrue(productivityRate > basicRate,
                "Productivity frame (x1.18) should outperform Basic frame (x1.03)");
    }

    // --- hybrid secondary output ---

    @Test
    void hybridMeadowForestProducesForestSecondaryAtTwelvePercent() {
        // Forest as inactive species: forest_comb primary chance = 80%
        // Secondary penalty = INACTIVE_SPECIES_MULTIPLIER = 0.15
        // Expected: 80% * 0.15 = 12%
        //
        // GenomeFixtures uses AlleleFixtures productivity IDs ("curious_bees:productivity/normal")
        // which differ from BuiltinBeeTraits ("curious_bees:traits/productivity/normal").
        // ProductivityModifier falls back to 1.0x for unknown IDs, matching NORMAL behavior.
        // The resulting rate calculation is still correct; only a WARNING is logged.
        Genome hybrid = GenomeFixtures.hybridMeadowForest();
        double rate = hitRate(hybrid, 1.0, "curiousbees:forest_comb");
        assertEquals(0.12, rate, TOLERANCE,
                "Hybrid Meadow/Forest should produce Forest secondary at ~12% per roll");
    }

    @Test
    void hybridPrimaryRateIsUnaffectedByInactiveSpecies() {
        // Primary output (meadow_comb) should still hit ~80% regardless of inactive species.
        // See hybridMeadowForestProducesForestSecondaryAtTwelvePercent for note on AlleleFixtures IDs.
        Genome hybrid = GenomeFixtures.hybridMeadowForest();
        double rate = hitRate(hybrid, 1.0, "curiousbees:meadow_comb");
        assertEquals(0.80, rate, TOLERANCE,
                "Hybrid primary output rate should match pure Meadow base rate");
    }
}
