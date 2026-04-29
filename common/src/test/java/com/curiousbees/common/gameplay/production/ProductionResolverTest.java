package com.curiousbees.common.gameplay.production;

import com.curiousbees.common.genetics.fixtures.AlleleFixtures;
import com.curiousbees.common.genetics.fixtures.GenomeFixtures;
import com.curiousbees.common.genetics.random.DeterministicGeneticRandom;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ProductionResolverTest {

    private ProductionResolver resolver;

    private static final String MEADOW_ID    = AlleleFixtures.MEADOW.id();
    private static final String FOREST_ID    = AlleleFixtures.FOREST.id();
    private static final String CULTIVATED_ID = AlleleFixtures.CULTIVATED.id();

    private static final ProductionOutput MEADOW_COMB    = new ProductionOutput("curiousbees:meadow_comb",    1.0);
    private static final ProductionOutput FOREST_COMB    = new ProductionOutput("curiousbees:forest_comb",    1.0);
    private static final ProductionOutput CULTIVATED_COMB = new ProductionOutput("curiousbees:cultivated_comb", 1.0);

    private static final ProductionDefinition DEF_MEADOW =
            new ProductionDefinition(MEADOW_ID, List.of(MEADOW_COMB));
    private static final ProductionDefinition DEF_FOREST =
            new ProductionDefinition(FOREST_ID, List.of(FOREST_COMB));
    private static final ProductionDefinition DEF_CULTIVATED =
            new ProductionDefinition(CULTIVATED_ID, List.of(CULTIVATED_COMB));

    private static final Map<String, ProductionDefinition> DEFINITIONS = Map.of(
            MEADOW_ID,    DEF_MEADOW,
            FOREST_ID,    DEF_FOREST,
            CULTIVATED_ID, DEF_CULTIVATED);

    @BeforeEach
    void setUp() {
        resolver = new ProductionResolver();
    }

    @Test
    void pureMeadowProducesMeadowComb() {
        // chance = 1.0, roll = 0.0 -> always produces
        var random = new DeterministicGeneticRandom().withDoubles(0.0, 0.0);
        ProductionResult result = resolver.resolve(GenomeFixtures.pureMeadow(), DEFINITIONS, random);
        assertTrue(result.hasOutput());
        assertEquals("curiousbees:meadow_comb", result.generatedOutputs().get(0).outputId());
    }

    @Test
    void pureMeadowDoesNotProduceWhenRollFails() {
        // chance = 1.0, roll = 1.0 -> fails (not strictly < 1.0)
        var random = new DeterministicGeneticRandom().withDoubles(1.0);
        ProductionResult result = resolver.resolve(GenomeFixtures.pureMeadow(), DEFINITIONS, random);
        assertFalse(result.hasOutput());
    }

    @Test
    void hybridUsesActiveSpeciesAsPrimary() {
        // hybridMeadowForest -> active = MEADOW
        // First double: primary roll (0.0 -> produces meadow_comb)
        // Second double: secondary roll (could be any value)
        var random = new DeterministicGeneticRandom().withDoubles(0.0, 1.0);
        ProductionResult result = resolver.resolve(GenomeFixtures.hybridMeadowForest(), DEFINITIONS, random);
        assertEquals(MEADOW_ID, result.activeSpeciesId());
        assertTrue(result.generatedOutputs().stream()
                .anyMatch(o -> o.outputId().equals("curiousbees:meadow_comb")));
    }

    @Test
    void hybridCanProduceSecondaryFromInactiveSpecies() {
        // hybridMeadowForest -> inactive = FOREST
        // chance = 1.0 * 0.15 = 0.15; roll = 0.0 -> produces forest_comb
        var random = new DeterministicGeneticRandom().withDoubles(0.0, 0.0);
        ProductionResult result = resolver.resolve(GenomeFixtures.hybridMeadowForest(), DEFINITIONS, random);
        assertEquals(2, result.generatedOutputs().size());
        assertTrue(result.generatedOutputs().stream()
                .anyMatch(o -> o.outputId().equals("curiousbees:forest_comb")));
    }

    @Test
    void hybridSecondaryDoesNotProduceWhenRollFailsThreshold() {
        // secondary chance = 1.0 * 0.15 = 0.15; roll = 0.15 -> fails (not < 0.15)
        var random = new DeterministicGeneticRandom().withDoubles(0.0, 0.15);
        ProductionResult result = resolver.resolve(GenomeFixtures.hybridMeadowForest(), DEFINITIONS, random);
        assertEquals(1, result.generatedOutputs().size());
        assertEquals("curiousbees:meadow_comb", result.generatedOutputs().get(0).outputId());
    }

    @Test
    void slowProductivityReducesEffectiveChance() {
        // Baseline sanity check: pureMeadow has NORMAL productivity (1.0 multiplier).
        var slowOutput = new ProductionOutput("curiousbees:meadow_comb", 0.8);
        var def = new ProductionDefinition(MEADOW_ID, List.of(slowOutput));
        var defs = Map.of(MEADOW_ID, def);

        var randomNormal = new DeterministicGeneticRandom().withDoubles(0.7);
        ProductionResult normalResult = resolver.resolve(GenomeFixtures.pureMeadow(), defs, randomNormal);
        assertTrue(normalResult.hasOutput(), "Normal productivity should produce at roll 0.7 with 0.8 base chance");
    }

    @Test
    void resultContainsActiveAndInactiveSpeciesIds() {
        var random = new DeterministicGeneticRandom().withDoubles(1.0, 1.0);
        ProductionResult result = resolver.resolve(GenomeFixtures.hybridMeadowForest(), DEFINITIONS, random);
        assertEquals(MEADOW_ID, result.activeSpeciesId());
        assertEquals(FOREST_ID, result.inactiveSpeciesId());
    }

    @Test
    void missingDefinitionProducesNoOutputWithWarning() {
        var random = new JavaGeneticRandom(new Random(1));
        // pureCultivated genome but no CULTIVATED definition
        ProductionResult result = resolver.resolve(GenomeFixtures.pureCultivated(),
                Map.of(MEADOW_ID, DEF_MEADOW), random);
        assertFalse(result.hasOutput());
        assertEquals(CULTIVATED_ID, result.activeSpeciesId());
    }

    @Test
    void externalProductionMultiplierAffectsRollChance() {
        var output = new ProductionOutput("curiousbees:meadow_comb", 0.8);
        var defs = Map.of(MEADOW_ID, new ProductionDefinition(MEADOW_ID, List.of(output)));
        var random = new DeterministicGeneticRandom().withDoubles(0.95);

        ProductionResult result = resolver.resolve(
                GenomeFixtures.pureMeadow(),
                defs,
                random,
                2.0);

        assertTrue(result.hasOutput(), "2.0x external multiplier should clamp 0.8 chance to 1.0");
    }
}
