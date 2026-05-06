package com.curiousbees.common.gameplay.spawn;

import com.curiousbees.common.content.builtin.BuiltinBeeSpecies;
import com.curiousbees.common.content.species.BeeSpeciesDefinition;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WildBeeSpawnServiceTest {

    private static final int SURFACE_Y    = 80;
    private static final int SURFACE_LIGHT = 15;

    @Test
    void forestTagsSelectForestSpecies() {
        BeeSpeciesDefinition result = WildBeeSpawnService.speciesForHabitat(
                List.of("minecraft:is_forest", "minecraft:is_overworld"),
                SURFACE_Y, SURFACE_LIGHT);
        assertEquals(BuiltinBeeSpecies.FOREST.id(), result.id());
    }

    @Test
    void savannaTagsSelectAridSpecies() {
        BeeSpeciesDefinition result = WildBeeSpawnService.speciesForHabitat(
                List.of("minecraft:is_savanna", "minecraft:is_overworld"),
                SURFACE_Y, SURFACE_LIGHT);
        assertEquals(BuiltinBeeSpecies.ARID.id(), result.id());
    }

    @Test
    void badlandsTagsSelectAridSpecies() {
        BeeSpeciesDefinition result = WildBeeSpawnService.speciesForHabitat(
                List.of("minecraft:is_badlands", "minecraft:is_overworld"),
                SURFACE_Y, SURFACE_LIGHT);
        assertEquals(BuiltinBeeSpecies.ARID.id(), result.id());
    }

    @Test
    void noSpecificTagsFallsBackToMeadow() {
        BeeSpeciesDefinition result = WildBeeSpawnService.speciesForHabitat(
                List.of("minecraft:is_overworld"),
                SURFACE_Y, SURFACE_LIGHT);
        assertEquals(BuiltinBeeSpecies.MEADOW.id(), result.id());
    }

    @Test
    void emptyTagListFallsBackToMeadow() {
        BeeSpeciesDefinition result = WildBeeSpawnService.speciesForHabitat(
                List.of(),
                SURFACE_Y, SURFACE_LIGHT);
        assertEquals(BuiltinBeeSpecies.MEADOW.id(), result.id());
    }

    @Test
    void undergroundYExcludesAllSpecificPredicates() {
        // Y = -30 is below DEFAULT_MIN_Y (60) for all predicates → wildcard (meadow) wins
        BeeSpeciesDefinition result = WildBeeSpawnService.speciesForHabitat(
                List.of("minecraft:is_forest"),
                -30, SURFACE_LIGHT);
        assertEquals(BuiltinBeeSpecies.MEADOW.id(), result.id());
    }

    @Test
    void darkLightExcludesAllPredicates_fallsBackToMeadow() {
        // light = 3 is below DEFAULT_MIN_LIGHT (9) → no predicate matches Y+light → hard fallback
        // Actually meadow predicate (wildcard) also has minLight=9, so no match → MEADOW hard fallback
        BeeSpeciesDefinition result = WildBeeSpawnService.speciesForHabitat(
                List.of("minecraft:is_forest"),
                SURFACE_Y, 3);
        // forest predicate fails light check → no specific match
        // meadow wildcard also fails light check → hard fallback to MEADOW
        assertEquals(BuiltinBeeSpecies.MEADOW.id(), result.id());
    }

    @RepeatedTest(20)
    void resultIsAlwaysOneOfTheKnownSpecies() {
        BeeSpeciesDefinition result = WildBeeSpawnService.speciesForHabitat(
                List.of("minecraft:is_overworld"), SURFACE_Y, SURFACE_LIGHT);
        assertTrue(BuiltinBeeSpecies.ALL.stream()
                .anyMatch(s -> s.id().equals(result.id())));
    }
}
