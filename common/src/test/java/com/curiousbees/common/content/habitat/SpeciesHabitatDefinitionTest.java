package com.curiousbees.common.content.habitat;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpeciesHabitatDefinitionTest {

    @Test
    void constructsWithValidArguments() {
        SpeciesHabitatDefinition def = new SpeciesHabitatDefinition(
                "curiousbees:meadow_hive",
                "curiousbees:textures/block/meadow_hive.png",
                List.of("minecraft:plains", "minecraft:meadow"));

        assertEquals("curiousbees:meadow_hive", def.hiveBlockId());
        assertEquals("curiousbees:textures/block/meadow_hive.png", def.hiveTextureId());
        assertEquals(List.of("minecraft:plains", "minecraft:meadow"), def.spawnBiomes());
    }

    @Test
    void spawnBiomesIsImmutable() {
        SpeciesHabitatDefinition def = new SpeciesHabitatDefinition(
                "curiousbees:meadow_hive",
                "curiousbees:textures/block/meadow_hive.png",
                List.of("minecraft:plains"));

        assertThrows(UnsupportedOperationException.class,
                () -> def.spawnBiomes().add("extra_biome"));
    }

    @Test
    void rejectsNullHiveBlockId() {
        assertThrows(NullPointerException.class, () ->
                new SpeciesHabitatDefinition(null, "tex", List.of("biome")));
    }

    @Test
    void rejectsBlankHiveBlockId() {
        assertThrows(IllegalArgumentException.class, () ->
                new SpeciesHabitatDefinition("  ", "tex", List.of("biome")));
    }

    @Test
    void rejectsNullHiveTextureId() {
        assertThrows(NullPointerException.class, () ->
                new SpeciesHabitatDefinition("block", null, List.of("biome")));
    }

    @Test
    void rejectsBlankHiveTextureId() {
        assertThrows(IllegalArgumentException.class, () ->
                new SpeciesHabitatDefinition("block", "", List.of("biome")));
    }

    @Test
    void rejectsNullSpawnBiomes() {
        assertThrows(NullPointerException.class, () ->
                new SpeciesHabitatDefinition("block", "tex", null));
    }

    @Test
    void rejectsEmptySpawnBiomes() {
        assertThrows(IllegalArgumentException.class, () ->
                new SpeciesHabitatDefinition("block", "tex", List.of()));
    }

    @Test
    void equalityBasedOnAllFields() {
        SpeciesHabitatDefinition a = new SpeciesHabitatDefinition(
                "curiousbees:meadow_hive", "tex", List.of("plains"));
        SpeciesHabitatDefinition b = new SpeciesHabitatDefinition(
                "curiousbees:meadow_hive", "tex", List.of("plains"));
        SpeciesHabitatDefinition c = new SpeciesHabitatDefinition(
                "curiousbees:forest_hive", "tex", List.of("plains"));

        assertEquals(a, b);
        assertNotEquals(a, c);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
