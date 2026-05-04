package com.curiousbees.common.content.builtin;

import com.curiousbees.common.content.habitat.SpeciesHabitatDefinition;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/** Verifies that built-in species have the correct habitat configuration. */
class BuiltinBeeSpeciesHabitatTest {

    @Test
    void meadowHabitatPresent() {
        Optional<SpeciesHabitatDefinition> habitat = BuiltinBeeSpecies.MEADOW.habitat();
        assertTrue(habitat.isPresent(), "Meadow Bee must have a habitat definition");
        assertEquals("curiousbees:meadow_hive", habitat.get().hiveBlockId());
        assertFalse(habitat.get().spawnBiomes().isEmpty());
        assertTrue(habitat.get().spawnBiomes().contains("minecraft:plains"));
    }

    @Test
    void forestHabitatPresent() {
        Optional<SpeciesHabitatDefinition> habitat = BuiltinBeeSpecies.FOREST.habitat();
        assertTrue(habitat.isPresent(), "Forest Bee must have a habitat definition");
        assertEquals("curiousbees:forest_hive", habitat.get().hiveBlockId());
        assertTrue(habitat.get().spawnBiomes().contains("minecraft:forest"));
    }

    @Test
    void aridHabitatPresent() {
        Optional<SpeciesHabitatDefinition> habitat = BuiltinBeeSpecies.ARID.habitat();
        assertTrue(habitat.isPresent(), "Arid Bee must have a habitat definition");
        assertEquals("curiousbees:arid_hive", habitat.get().hiveBlockId());
        assertTrue(habitat.get().spawnBiomes().contains("minecraft:desert"));
    }

    @Test
    void cultivatedHasNoHabitat() {
        assertTrue(BuiltinBeeSpecies.CULTIVATED.habitat().isEmpty(),
                "Cultivated Bee is mutation-only and must not have a habitat");
    }

    @Test
    void hardyHasNoHabitat() {
        assertTrue(BuiltinBeeSpecies.HARDY.habitat().isEmpty(),
                "Hardy Bee is mutation-only and must not have a habitat");
    }

    @Test
    void habitatBiomesAreImmutable() {
        SpeciesHabitatDefinition habitat = BuiltinBeeSpecies.MEADOW.habitat().get();
        assertThrows(UnsupportedOperationException.class,
                () -> habitat.spawnBiomes().add("extra"));
    }
}
