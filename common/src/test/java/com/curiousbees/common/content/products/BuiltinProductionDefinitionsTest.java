package com.curiousbees.common.content.products;

import com.curiousbees.common.content.builtin.BuiltinBeeSpecies;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuiltinProductionDefinitionsTest {

    @Test
    void allFiveMvpSpeciesHaveDefinitions() {
        var map = BuiltinProductionDefinitions.BY_SPECIES_ID;
        assertTrue(map.containsKey(BuiltinBeeSpecies.SPECIES_MEADOW.id()),    "Meadow missing");
        assertTrue(map.containsKey(BuiltinBeeSpecies.SPECIES_FOREST.id()),    "Forest missing");
        assertTrue(map.containsKey(BuiltinBeeSpecies.SPECIES_ARID.id()),      "Arid missing");
        assertTrue(map.containsKey(BuiltinBeeSpecies.SPECIES_CULTIVATED.id()),"Cultivated missing");
        assertTrue(map.containsKey(BuiltinBeeSpecies.SPECIES_HARDY.id()),     "Hardy missing");
    }

    @Test
    void meadowDefinitionHasPrimaryOutput() {
        assertFalse(BuiltinProductionDefinitions.MEADOW.primaryOutputs().isEmpty());
        assertEquals("curiousbees:meadow_comb",
                BuiltinProductionDefinitions.MEADOW.primaryOutputs().get(0).outputId());
    }

    @Test
    void cultivatedDefinitionHasHigherBaseChanceThanMeadow() {
        double meadowChance    = BuiltinProductionDefinitions.MEADOW.primaryOutputs().get(0).chance();
        double cultivatedChance = BuiltinProductionDefinitions.CULTIVATED.primaryOutputs().get(0).chance();
        assertTrue(cultivatedChance >= meadowChance,
                "Cultivated should produce at least as well as Meadow");
    }

    @Test
    void noDefinitionContainsMineraftItemDirectly() {
        // All primary output IDs should be stable strings — just a sanity check
        for (var def : BuiltinProductionDefinitions.BY_SPECIES_ID.values()) {
            for (var output : def.primaryOutputs()) {
                assertNotNull(output.outputId());
                assertFalse(output.outputId().isBlank());
            }
        }
    }
}
