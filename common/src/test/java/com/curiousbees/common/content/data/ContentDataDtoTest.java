package com.curiousbees.common.content.data;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies that the content DTOs can represent the MVP built-in content
 * and enforce their basic null contracts.
 */
class ContentDataDtoTest {

    // -------------------------------------------------------------------------
    // TraitAlleleDefinitionData
    // -------------------------------------------------------------------------

    @Test
    void traitAllele_storesAllFields() {
        var dto = new TraitAlleleDefinitionData(
                "curious_bees:traits/lifespan/normal",
                "LIFESPAN",
                "Normal",
                "DOMINANT",
                Map.of("multiplier", 1.0));

        assertEquals("curious_bees:traits/lifespan/normal", dto.id());
        assertEquals("LIFESPAN", dto.chromosomeType());
        assertEquals("Normal", dto.displayName());
        assertEquals("DOMINANT", dto.dominance());
        assertEquals(1.0, dto.values().get("multiplier"));
    }

    @Test
    void traitAllele_noValues_returnsEmptyMap() {
        var dto = new TraitAlleleDefinitionData(
                "curious_bees:traits/productivity/fast",
                "PRODUCTIVITY",
                "Fast",
                "RECESSIVE");

        assertTrue(dto.values().isEmpty());
    }

    @Test
    void traitAllele_nullId_throws() {
        assertThrows(NullPointerException.class, () ->
                new TraitAlleleDefinitionData(null, "LIFESPAN", "Normal", "DOMINANT"));
    }

    @Test
    void traitAllele_canRepresentMvpLifespanShort() {
        var dto = new TraitAlleleDefinitionData(
                "curious_bees:traits/lifespan/short", "LIFESPAN", "Short", "RECESSIVE");
        assertEquals("curious_bees:traits/lifespan/short", dto.id());
        assertEquals("RECESSIVE", dto.dominance());
    }

    @Test
    void traitAllele_canRepresentMvpFlowerCactus() {
        var dto = new TraitAlleleDefinitionData(
                "curious_bees:traits/flower_type/cactus", "FLOWER_TYPE", "Cactus", "RECESSIVE");
        assertEquals("FLOWER_TYPE", dto.chromosomeType());
    }

    // -------------------------------------------------------------------------
    // TraitAllelePairData
    // -------------------------------------------------------------------------

    @Test
    void traitAllelePair_storesFirstAndSecond() {
        var pair = new TraitAllelePairData(
                "curious_bees:traits/lifespan/normal",
                "curious_bees:traits/lifespan/normal");

        assertEquals("curious_bees:traits/lifespan/normal", pair.first());
        assertEquals("curious_bees:traits/lifespan/normal", pair.second());
    }

    @Test
    void traitAllelePair_nullFirst_throws() {
        assertThrows(NullPointerException.class, () ->
                new TraitAllelePairData(null, "curious_bees:traits/lifespan/normal"));
    }

    // -------------------------------------------------------------------------
    // SpeciesDefinitionData — Meadow
    // -------------------------------------------------------------------------

    @Test
    void speciesData_meadow_storesAllFields() {
        var defaultTraits = Map.of(
                "LIFESPAN",     new TraitAllelePairData("curious_bees:traits/lifespan/normal",      "curious_bees:traits/lifespan/normal"),
                "PRODUCTIVITY", new TraitAllelePairData("curious_bees:traits/productivity/normal",  "curious_bees:traits/productivity/normal"),
                "FERTILITY",    new TraitAllelePairData("curious_bees:traits/fertility/two",         "curious_bees:traits/fertility/two"),
                "FLOWER_TYPE",  new TraitAllelePairData("curious_bees:traits/flower_type/flowers",  "curious_bees:traits/flower_type/flowers")
        );

        var dto = new SpeciesDefinitionData(
                "curious_bees:species/meadow", "Meadow Bee", "DOMINANT",
                defaultTraits, List.of("plains", "flower_forest", "meadow"));

        assertEquals("curious_bees:species/meadow", dto.id());
        assertEquals("Meadow Bee", dto.displayName());
        assertEquals("DOMINANT", dto.dominance());
        assertEquals(4, dto.defaultTraits().size());
        assertEquals(List.of("plains", "flower_forest", "meadow"), dto.spawnContextNotes());
    }

    @Test
    void speciesData_noSpawnContextNotes_defaultsToEmptyList() {
        var dto = new SpeciesDefinitionData(
                "curious_bees:species/cultivated", "Cultivated Bee", "DOMINANT",
                Map.of(
                        "LIFESPAN",     new TraitAllelePairData("curious_bees:traits/lifespan/normal",     "curious_bees:traits/lifespan/normal"),
                        "PRODUCTIVITY", new TraitAllelePairData("curious_bees:traits/productivity/fast",   "curious_bees:traits/productivity/normal"),
                        "FERTILITY",    new TraitAllelePairData("curious_bees:traits/fertility/two",        "curious_bees:traits/fertility/two"),
                        "FLOWER_TYPE",  new TraitAllelePairData("curious_bees:traits/flower_type/flowers", "curious_bees:traits/flower_type/flowers")
                ));

        assertTrue(dto.spawnContextNotes().isEmpty());
    }

    @Test
    void speciesData_nullId_throws() {
        assertThrows(NullPointerException.class, () ->
                new SpeciesDefinitionData(null, "X", "DOMINANT", Map.of()));
    }

    // -------------------------------------------------------------------------
    // MutationDefinitionData — Cultivated from Meadow + Forest
    // -------------------------------------------------------------------------

    @Test
    void mutationData_cultivatedFromMeadowForest_storesAllFields() {
        var resultModes = new MutationResultModesData(0.95, 0.05);
        var dto = new MutationDefinitionData(
                "curious_bees:mutations/cultivated_from_meadow_forest",
                "curious_bees:species/meadow",
                "curious_bees:species/forest",
                "curious_bees:species/cultivated",
                0.12,
                resultModes);

        assertEquals("curious_bees:mutations/cultivated_from_meadow_forest", dto.id());
        assertEquals("curious_bees:species/meadow", dto.parentSpeciesAId());
        assertEquals("curious_bees:species/forest", dto.parentSpeciesBId());
        assertEquals("curious_bees:species/cultivated", dto.resultSpeciesId());
        assertEquals(0.12, dto.baseChance());
        assertEquals(0.95, dto.resultModes().partialChance());
        assertEquals(0.05, dto.resultModes().fullChance());
    }

    @Test
    void mutationData_hardyFromForestArid_storesAllFields() {
        var resultModes = new MutationResultModesData(0.95, 0.05);
        var dto = new MutationDefinitionData(
                "curious_bees:mutations/hardy_from_forest_arid",
                "curious_bees:species/forest",
                "curious_bees:species/arid",
                "curious_bees:species/hardy",
                0.08,
                resultModes);

        assertEquals(0.08, dto.baseChance());
        assertEquals("curious_bees:species/hardy", dto.resultSpeciesId());
    }

    @Test
    void mutationData_nullId_throws() {
        assertThrows(NullPointerException.class, () ->
                new MutationDefinitionData(null, "a", "b", "c", 0.1,
                        new MutationResultModesData(0.95, 0.05)));
    }

    // -------------------------------------------------------------------------
    // ProductionDefinitionData — Cultivated
    // -------------------------------------------------------------------------

    @Test
    void productionData_cultivated_storesAllFields() {
        var dto = new ProductionDefinitionData(
                "curious_bees:species/cultivated",
                List.of(new ProductionOutputData("curiousbees:cultivated_comb", 0.90)),
                List.of(new ProductionOutputData("minecraft:honeycomb", 0.30)));

        assertEquals("curious_bees:species/cultivated", dto.speciesId());
        assertEquals(1, dto.primaryOutputs().size());
        assertEquals("curiousbees:cultivated_comb", dto.primaryOutputs().get(0).item());
        assertEquals(0.90, dto.primaryOutputs().get(0).chance());
        assertEquals(1, dto.secondaryOutputs().size());
    }

    @Test
    void productionData_forest_noSecondaryOutputs() {
        var dto = new ProductionDefinitionData(
                "curious_bees:species/forest",
                List.of(new ProductionOutputData("curiousbees:forest_comb", 0.80)));

        assertTrue(dto.secondaryOutputs().isEmpty());
    }

    @Test
    void productionOutput_defaultMinMax_isOne() {
        var output = new ProductionOutputData("curiousbees:meadow_comb", 0.80);
        assertEquals(1, output.min());
        assertEquals(1, output.max());
    }

    @Test
    void productionOutput_customMinMax() {
        var output = new ProductionOutputData("curiousbees:meadow_comb", 0.80, 1, 3);
        assertEquals(3, output.max());
    }

    @Test
    void productionData_nullSpeciesId_throws() {
        assertThrows(NullPointerException.class, () ->
                new ProductionDefinitionData(null, List.of()));
    }
}
