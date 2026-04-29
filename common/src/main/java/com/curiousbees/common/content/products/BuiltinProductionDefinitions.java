package com.curiousbees.common.content.products;

import com.curiousbees.common.gameplay.production.ProductionDefinition;
import com.curiousbees.common.gameplay.production.ProductionOutput;

import java.util.List;
import java.util.Map;

import static com.curiousbees.common.content.builtin.BuiltinBeeSpecies.*;

/**
 * Centralized built-in production definitions for the five MVP species.
 * Uses stable output IDs; the platform layer resolves them to actual items.
 */
public final class BuiltinProductionDefinitions {

    private BuiltinProductionDefinitions() {}

    public static final ProductionDefinition MEADOW = new ProductionDefinition(
            SPECIES_MEADOW.id(),
            List.of(new ProductionOutput("curiousbees:meadow_comb", 0.80)),
            List.of(new ProductionOutput("minecraft:honeycomb",     0.20)));

    public static final ProductionDefinition FOREST = new ProductionDefinition(
            SPECIES_FOREST.id(),
            List.of(new ProductionOutput("curiousbees:forest_comb", 0.80)));

    public static final ProductionDefinition ARID = new ProductionDefinition(
            SPECIES_ARID.id(),
            List.of(new ProductionOutput("curiousbees:arid_comb", 0.80)));

    public static final ProductionDefinition CULTIVATED = new ProductionDefinition(
            SPECIES_CULTIVATED.id(),
            List.of(new ProductionOutput("curiousbees:cultivated_comb", 0.90)),
            List.of(new ProductionOutput("minecraft:honeycomb",         0.30)));

    public static final ProductionDefinition HARDY = new ProductionDefinition(
            SPECIES_HARDY.id(),
            List.of(new ProductionOutput("curiousbees:hardy_comb", 0.80)));

    /** All definitions keyed by species ID, for use with ProductionResolver. */
    public static final Map<String, ProductionDefinition> BY_SPECIES_ID = Map.of(
            SPECIES_MEADOW.id(),     MEADOW,
            SPECIES_FOREST.id(),     FOREST,
            SPECIES_ARID.id(),       ARID,
            SPECIES_CULTIVATED.id(), CULTIVATED,
            SPECIES_HARDY.id(),      HARDY);
}
