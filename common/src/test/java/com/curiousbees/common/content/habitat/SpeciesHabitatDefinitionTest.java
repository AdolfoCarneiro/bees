package com.curiousbees.common.content.habitat;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SpeciesHabitatDefinitionTest {

    @Test
    void constructsWithValidArguments() {
        SpeciesHabitatDefinition def = new SpeciesHabitatDefinition(
                "curiousbees:meadow_bee_nest",
                "curiousbees:textures/block/meadow_bee_nest_side.png",
                List.of("minecraft:plains", "minecraft:meadow"));

        assertEquals("curiousbees:meadow_bee_nest", def.beeNestBlockId());
        assertEquals("curiousbees:textures/block/meadow_bee_nest_side.png", def.beeNestRepresentativeTextureId());
        assertEquals(List.of("minecraft:plains", "minecraft:meadow"), def.spawnBiomes());
    }

    @Test
    void spawnBiomesIsImmutable() {
        SpeciesHabitatDefinition def = new SpeciesHabitatDefinition(
                "curiousbees:meadow_bee_nest",
                "curiousbees:textures/block/meadow_bee_nest_side.png",
                List.of("minecraft:plains"));

        assertThrows(UnsupportedOperationException.class,
                () -> def.spawnBiomes().add("extra_biome"));
    }

    @Test
    void rejectsNullBeeNestBlockId() {
        assertThrows(NullPointerException.class, () ->
                new SpeciesHabitatDefinition(null, "tex", List.of("biome")));
    }

    @Test
    void rejectsBlankBeeNestBlockId() {
        assertThrows(IllegalArgumentException.class, () ->
                new SpeciesHabitatDefinition("  ", "tex", List.of("biome")));
    }

    @Test
    void rejectsNullRepresentativeTextureId() {
        assertThrows(NullPointerException.class, () ->
                new SpeciesHabitatDefinition("block", null, List.of("biome")));
    }

    @Test
    void rejectsBlankRepresentativeTextureId() {
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
                "curiousbees:meadow_bee_nest", "tex", List.of("plains"));
        SpeciesHabitatDefinition b = new SpeciesHabitatDefinition(
                "curiousbees:meadow_bee_nest", "tex", List.of("plains"));
        SpeciesHabitatDefinition c = new SpeciesHabitatDefinition(
                "curiousbees:forest_bee_nest", "tex", List.of("plains"));

        assertEquals(a, b);
        assertNotEquals(a, c);
        assertEquals(a.hashCode(), b.hashCode());
    }
}
