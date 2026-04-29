package com.curiousbees.common.content.registry;

import com.curiousbees.common.content.builtin.BuiltinBeeSpecies;
import com.curiousbees.common.content.builtin.BuiltinBeeTraits;
import com.curiousbees.common.content.species.BeeSpeciesDefinition;
import com.curiousbees.common.gameplay.production.ProductionDefinition;
import com.curiousbees.common.gameplay.production.ProductionOutput;
import com.curiousbees.common.genetics.model.Allele;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Dominance;
import com.curiousbees.common.genetics.mutation.MutationDefinition;
import com.curiousbees.common.genetics.mutation.MutationResultMode;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContentRegistryTest {

    @Test
    void builtInRegistryContainsMvpContent() {
        ContentRegistry registry = ContentRegistry.builtIn();

        assertEquals(5, registry.allSpecies().size());
        assertEquals(12, registry.allTraitAlleles().size());
        assertEquals(2, registry.allMutations().size());
        assertEquals(5, registry.allProductionDefinitions().size());
        assertTrue(registry.findSpecies(BuiltinBeeSpecies.MEADOW.id()).isPresent());
        assertTrue(registry.findTraitAllele(BuiltinBeeTraits.PRODUCTIVITY_FAST.id()).isPresent());
        assertTrue(registry.findProduction(BuiltinBeeSpecies.CULTIVATED.id()).isPresent());
    }

    @Test
    void loadedDefinitionsExtendBuiltIns() {
        ContentRegistry registry = ContentRegistry.builtIn();
        BeeSpeciesDefinition loadedSpecies = testSpecies();
        MutationDefinition loadedMutation = new MutationDefinition(
                "curious_bees:mutations/test_from_meadow_forest",
                BuiltinBeeSpecies.MEADOW.id(),
                BuiltinBeeSpecies.FOREST.id(),
                loadedSpecies.speciesAllele(),
                0.25,
                MutationResultMode.PARTIAL);
        ProductionDefinition loadedProduction = new ProductionDefinition(
                loadedSpecies.id(),
                List.of(new ProductionOutput("curiousbees:test_comb", 0.5)));

        ContentRegistry merged = registry.withLoadedDefinitions(
                List.of(),
                List.of(loadedSpecies),
                List.of(loadedMutation),
                List.of(loadedProduction));

        assertEquals(6, merged.allSpecies().size());
        assertTrue(merged.findSpecies(loadedSpecies.id()).isPresent());
        assertTrue(merged.findMutation(loadedMutation.id()).isPresent());
        assertEquals("curiousbees:test_comb",
                merged.findProduction(loadedSpecies.id()).orElseThrow().primaryOutputs().get(0).outputId());
        assertTrue(merged.findSpecies(BuiltinBeeSpecies.MEADOW.id()).isPresent());
    }

    @Test
    void loadedSpeciesCannotOverrideBuiltInSpecies() {
        ContentRegistry registry = ContentRegistry.builtIn();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> registry.withLoadedDefinitions(
                        List.of(),
                        List.of(BuiltinBeeSpecies.MEADOW),
                        List.of(),
                        List.of()));

        assertTrue(exception.getMessage().contains("Duplicate species id"));
    }

    @Test
    void duplicateLoadedMutationIsRejected() {
        ContentRegistry registry = ContentRegistry.builtIn();
        BeeSpeciesDefinition loadedSpecies = testSpecies();
        MutationDefinition first = testMutation("curious_bees:mutations/duplicate", loadedSpecies);
        MutationDefinition second = testMutation("curious_bees:mutations/duplicate", loadedSpecies);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> registry.withLoadedDefinitions(
                        List.of(),
                        List.of(loadedSpecies),
                        List.of(first, second),
                        List.of()));

        assertTrue(exception.getMessage().contains("Duplicate mutation id"));
    }

    @Test
    void missingLookupsAreExplicit() {
        ContentRegistry registry = ContentRegistry.builtIn();

        assertFalse(registry.findSpecies("curious_bees:species/missing").isPresent());
        assertThrows(IllegalArgumentException.class,
                () -> registry.getSpecies("curious_bees:species/missing"));
    }

    private static MutationDefinition testMutation(String id, BeeSpeciesDefinition resultSpecies) {
        return new MutationDefinition(
                id,
                BuiltinBeeSpecies.MEADOW.id(),
                BuiltinBeeSpecies.FOREST.id(),
                resultSpecies.speciesAllele(),
                0.2,
                MutationResultMode.PARTIAL);
    }

    private static BeeSpeciesDefinition testSpecies() {
        Allele speciesAllele = new Allele(
                "curious_bees:species/test",
                ChromosomeType.SPECIES,
                Dominance.RECESSIVE);

        Map<ChromosomeType, Allele[]> traits = new EnumMap<>(ChromosomeType.class);
        traits.put(ChromosomeType.LIFESPAN,
                new Allele[]{BuiltinBeeTraits.LIFESPAN_NORMAL, BuiltinBeeTraits.LIFESPAN_NORMAL});
        traits.put(ChromosomeType.PRODUCTIVITY,
                new Allele[]{BuiltinBeeTraits.PRODUCTIVITY_NORMAL, BuiltinBeeTraits.PRODUCTIVITY_NORMAL});
        traits.put(ChromosomeType.FERTILITY,
                new Allele[]{BuiltinBeeTraits.FERTILITY_TWO, BuiltinBeeTraits.FERTILITY_TWO});
        traits.put(ChromosomeType.FLOWER_TYPE,
                new Allele[]{BuiltinBeeTraits.FLOWER_FLOWERS, BuiltinBeeTraits.FLOWER_FLOWERS});

        return new BeeSpeciesDefinition(
                "curious_bees:species/test",
                "Test Bee",
                speciesAllele,
                traits,
                List.of("test"));
    }
}
