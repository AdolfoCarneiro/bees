package com.curiousbees.common.content.builtin;

import com.curiousbees.common.content.data.MutationDefinitionData;
import com.curiousbees.common.content.data.MutationResultModesData;
import com.curiousbees.common.content.data.ProductionDefinitionData;
import com.curiousbees.common.content.data.ProductionOutputData;
import com.curiousbees.common.content.data.SpeciesDefinitionData;
import com.curiousbees.common.content.data.TraitAlleleDefinitionData;
import com.curiousbees.common.content.data.TraitAllelePairData;

import java.util.List;
import java.util.Map;

/**
 * Represents the MVP built-in content as data objects that pass through
 * the same validation and conversion pipeline as externally loaded content.
 *
 * These definitions mirror BuiltinBeeTraits, BuiltinBeeSpecies, BuiltinBeeMutations
 * and BuiltinProductionDefinitions exactly.
 */
public final class BuiltinContentData {

    private BuiltinContentData() {}

    // -------------------------------------------------------------------------
    // Trait alleles
    // -------------------------------------------------------------------------

    public static final List<TraitAlleleDefinitionData> TRAIT_ALLELES = List.of(
            // Lifespan
            new TraitAlleleDefinitionData("curious_bees:traits/lifespan/short",  "LIFESPAN", "Short",  "RECESSIVE"),
            new TraitAlleleDefinitionData("curious_bees:traits/lifespan/normal", "LIFESPAN", "Normal", "DOMINANT"),
            new TraitAlleleDefinitionData("curious_bees:traits/lifespan/long",   "LIFESPAN", "Long",   "RECESSIVE"),
            // Productivity
            new TraitAlleleDefinitionData("curious_bees:traits/productivity/slow",   "PRODUCTIVITY", "Slow",   "RECESSIVE"),
            new TraitAlleleDefinitionData("curious_bees:traits/productivity/normal", "PRODUCTIVITY", "Normal", "DOMINANT"),
            new TraitAlleleDefinitionData("curious_bees:traits/productivity/fast",   "PRODUCTIVITY", "Fast",   "RECESSIVE"),
            // Fertility
            new TraitAlleleDefinitionData("curious_bees:traits/fertility/one",   "FERTILITY", "One",   "RECESSIVE"),
            new TraitAlleleDefinitionData("curious_bees:traits/fertility/two",   "FERTILITY", "Two",   "DOMINANT"),
            new TraitAlleleDefinitionData("curious_bees:traits/fertility/three", "FERTILITY", "Three", "RECESSIVE"),
            // Flower type
            new TraitAlleleDefinitionData("curious_bees:traits/flower_type/flowers", "FLOWER_TYPE", "Flowers", "DOMINANT"),
            new TraitAlleleDefinitionData("curious_bees:traits/flower_type/cactus",  "FLOWER_TYPE", "Cactus",  "RECESSIVE"),
            new TraitAlleleDefinitionData("curious_bees:traits/flower_type/leaves",  "FLOWER_TYPE", "Leaves",  "RECESSIVE")
    );

    // -------------------------------------------------------------------------
    // Species
    // -------------------------------------------------------------------------

    public static final List<SpeciesDefinitionData> SPECIES = List.of(
            new SpeciesDefinitionData(
                    "curious_bees:species/meadow", "Meadow Bee", "DOMINANT",
                    Map.of(
                            "LIFESPAN",     new TraitAllelePairData("curious_bees:traits/lifespan/normal",     "curious_bees:traits/lifespan/normal"),
                            "PRODUCTIVITY", new TraitAllelePairData("curious_bees:traits/productivity/normal", "curious_bees:traits/productivity/normal"),
                            "FERTILITY",    new TraitAllelePairData("curious_bees:traits/fertility/two",        "curious_bees:traits/fertility/two"),
                            "FLOWER_TYPE",  new TraitAllelePairData("curious_bees:traits/flower_type/flowers", "curious_bees:traits/flower_type/flowers")
                    ),
                    List.of("plains", "flower_forest", "meadow")),

            new SpeciesDefinitionData(
                    "curious_bees:species/forest", "Forest Bee", "DOMINANT",
                    Map.of(
                            "LIFESPAN",     new TraitAllelePairData("curious_bees:traits/lifespan/normal",     "curious_bees:traits/lifespan/normal"),
                            "PRODUCTIVITY", new TraitAllelePairData("curious_bees:traits/productivity/normal", "curious_bees:traits/productivity/normal"),
                            "FERTILITY",    new TraitAllelePairData("curious_bees:traits/fertility/two",        "curious_bees:traits/fertility/two"),
                            "FLOWER_TYPE",  new TraitAllelePairData("curious_bees:traits/flower_type/leaves",  "curious_bees:traits/flower_type/leaves")
                    ),
                    List.of("forest", "birch_forest", "dark_forest")),

            new SpeciesDefinitionData(
                    "curious_bees:species/arid", "Arid Bee", "RECESSIVE",
                    Map.of(
                            "LIFESPAN",     new TraitAllelePairData("curious_bees:traits/lifespan/normal",    "curious_bees:traits/lifespan/normal"),
                            "PRODUCTIVITY", new TraitAllelePairData("curious_bees:traits/productivity/slow",  "curious_bees:traits/productivity/normal"),
                            "FERTILITY",    new TraitAllelePairData("curious_bees:traits/fertility/one",       "curious_bees:traits/fertility/two"),
                            "FLOWER_TYPE",  new TraitAllelePairData("curious_bees:traits/flower_type/cactus", "curious_bees:traits/flower_type/cactus")
                    ),
                    List.of("desert", "savanna", "badlands")),

            new SpeciesDefinitionData(
                    "curious_bees:species/cultivated", "Cultivated Bee", "DOMINANT",
                    Map.of(
                            "LIFESPAN",     new TraitAllelePairData("curious_bees:traits/lifespan/normal",     "curious_bees:traits/lifespan/normal"),
                            "PRODUCTIVITY", new TraitAllelePairData("curious_bees:traits/productivity/fast",   "curious_bees:traits/productivity/normal"),
                            "FERTILITY",    new TraitAllelePairData("curious_bees:traits/fertility/two",        "curious_bees:traits/fertility/two"),
                            "FLOWER_TYPE",  new TraitAllelePairData("curious_bees:traits/flower_type/flowers", "curious_bees:traits/flower_type/flowers")
                    )),

            new SpeciesDefinitionData(
                    "curious_bees:species/hardy", "Hardy Bee", "RECESSIVE",
                    Map.of(
                            "LIFESPAN",     new TraitAllelePairData("curious_bees:traits/lifespan/long",       "curious_bees:traits/lifespan/normal"),
                            "PRODUCTIVITY", new TraitAllelePairData("curious_bees:traits/productivity/normal", "curious_bees:traits/productivity/normal"),
                            "FERTILITY",    new TraitAllelePairData("curious_bees:traits/fertility/two",        "curious_bees:traits/fertility/two"),
                            "FLOWER_TYPE",  new TraitAllelePairData("curious_bees:traits/flower_type/flowers", "curious_bees:traits/flower_type/cactus")
                    ))
    );

    // -------------------------------------------------------------------------
    // Mutations
    // -------------------------------------------------------------------------

    public static final List<MutationDefinitionData> MUTATIONS = List.of(
            new MutationDefinitionData(
                    "curious_bees:mutations/cultivated_from_meadow_forest",
                    "curious_bees:species/meadow",
                    "curious_bees:species/forest",
                    "curious_bees:species/cultivated",
                    0.12,
                    new MutationResultModesData(0.95, 0.05)),

            new MutationDefinitionData(
                    "curious_bees:mutations/hardy_from_forest_arid",
                    "curious_bees:species/forest",
                    "curious_bees:species/arid",
                    "curious_bees:species/hardy",
                    0.08,
                    new MutationResultModesData(0.95, 0.05))
    );

    // -------------------------------------------------------------------------
    // Production
    // -------------------------------------------------------------------------

    public static final List<ProductionDefinitionData> PRODUCTION = List.of(
            new ProductionDefinitionData(
                    "curious_bees:species/meadow",
                    List.of(new ProductionOutputData("curiousbees:meadow_comb", 0.80)),
                    List.of(new ProductionOutputData("minecraft:honeycomb", 0.20))),

            new ProductionDefinitionData(
                    "curious_bees:species/forest",
                    List.of(new ProductionOutputData("curiousbees:forest_comb", 0.80))),

            new ProductionDefinitionData(
                    "curious_bees:species/arid",
                    List.of(new ProductionOutputData("curiousbees:arid_comb", 0.80))),

            new ProductionDefinitionData(
                    "curious_bees:species/cultivated",
                    List.of(new ProductionOutputData("curiousbees:cultivated_comb", 0.90)),
                    List.of(new ProductionOutputData("minecraft:honeycomb", 0.30))),

            new ProductionDefinitionData(
                    "curious_bees:species/hardy",
                    List.of(new ProductionOutputData("curiousbees:hardy_comb", 0.80)))
    );
}
