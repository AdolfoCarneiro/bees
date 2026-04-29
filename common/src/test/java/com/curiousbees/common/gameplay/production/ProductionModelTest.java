package com.curiousbees.common.gameplay.production;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductionModelTest {

    // --- ProductionOutput ---

    @Test
    void validOutputSucceeds() {
        var output = new ProductionOutput("curiousbees:meadow_comb", 0.8);
        assertEquals("curiousbees:meadow_comb", output.outputId());
        assertEquals(0.8, output.chance());
        assertEquals(1, output.count());
    }

    @Test
    void outputRejectsNullId() {
        assertThrows(IllegalArgumentException.class, () ->
                new ProductionOutput(null, 0.5));
    }

    @Test
    void outputRejectsBlankId() {
        assertThrows(IllegalArgumentException.class, () ->
                new ProductionOutput("  ", 0.5));
    }

    @Test
    void outputRejectsChanceBelowZero() {
        assertThrows(IllegalArgumentException.class, () ->
                new ProductionOutput("curiousbees:meadow_comb", -0.1));
    }

    @Test
    void outputRejectsChanceAboveOne() {
        assertThrows(IllegalArgumentException.class, () ->
                new ProductionOutput("curiousbees:meadow_comb", 1.1));
    }

    @Test
    void outputRejectsZeroCount() {
        assertThrows(IllegalArgumentException.class, () ->
                new ProductionOutput("curiousbees:meadow_comb", 0.5, 0));
    }

    @Test
    void outputAcceptsBoundaryChances() {
        assertDoesNotThrow(() -> new ProductionOutput("curiousbees:meadow_comb", 0.0));
        assertDoesNotThrow(() -> new ProductionOutput("curiousbees:meadow_comb", 1.0));
    }

    // --- ProductionDefinition ---

    @Test
    void validDefinitionSucceeds() {
        var def = new ProductionDefinition("curiousbees:species/meadow",
                List.of(new ProductionOutput("curiousbees:meadow_comb", 0.8)));
        assertEquals("curiousbees:species/meadow", def.speciesId());
        assertEquals(1, def.primaryOutputs().size());
        assertTrue(def.secondaryOutputs().isEmpty());
    }

    @Test
    void definitionRejectsNullSpeciesId() {
        assertThrows(IllegalArgumentException.class, () ->
                new ProductionDefinition(null, List.of()));
    }

    @Test
    void definitionRejectsBlankSpeciesId() {
        assertThrows(IllegalArgumentException.class, () ->
                new ProductionDefinition("", List.of()));
    }

    @Test
    void definitionListsAreImmutable() {
        var primary = new java.util.ArrayList<ProductionOutput>();
        primary.add(new ProductionOutput("curiousbees:meadow_comb", 0.8));
        var def = new ProductionDefinition("curiousbees:species/meadow", primary);
        primary.clear();
        assertEquals(1, def.primaryOutputs().size());
    }

    // --- ProductionResult ---

    @Test
    void resultWithOutputReportsHasOutput() {
        var result = new ProductionResult("active", "inactive", "normal",
                List.of(new ProductionOutput("curiousbees:meadow_comb", 0.8)));
        assertTrue(result.hasOutput());
        assertEquals(1, result.generatedOutputs().size());
    }

    @Test
    void resultWithNoOutputReportsEmpty() {
        var result = new ProductionResult("active", "inactive", "normal", List.of());
        assertFalse(result.hasOutput());
    }
}
