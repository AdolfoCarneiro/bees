package com.curiousbees.common.content.conversion;

import com.curiousbees.common.content.data.*;
import com.curiousbees.common.content.species.BeeSpeciesDefinition;
import com.curiousbees.common.gameplay.production.ProductionDefinition;
import com.curiousbees.common.genetics.model.Allele;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Dominance;
import com.curiousbees.common.genetics.mutation.MutationDefinition;
import com.curiousbees.common.genetics.mutation.MutationResultMode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ContentConverterTest {

    // -------------------------------------------------------------------------
    // Known allele registries (mirrors MVP built-ins)
    // -------------------------------------------------------------------------

    private Map<String, Allele> knownTraitAlleles;
    private Map<String, Allele> knownSpeciesAlleles;

    @BeforeEach
    void setUp() {
        Map<String, Allele> traits = new HashMap<>();
        traits.put("curious_bees:traits/lifespan/short",      new Allele("curious_bees:traits/lifespan/short",      ChromosomeType.LIFESPAN,     Dominance.RECESSIVE));
        traits.put("curious_bees:traits/lifespan/normal",     new Allele("curious_bees:traits/lifespan/normal",     ChromosomeType.LIFESPAN,     Dominance.DOMINANT));
        traits.put("curious_bees:traits/lifespan/long",       new Allele("curious_bees:traits/lifespan/long",       ChromosomeType.LIFESPAN,     Dominance.RECESSIVE));
        traits.put("curious_bees:traits/productivity/slow",   new Allele("curious_bees:traits/productivity/slow",   ChromosomeType.PRODUCTIVITY, Dominance.RECESSIVE));
        traits.put("curious_bees:traits/productivity/normal", new Allele("curious_bees:traits/productivity/normal", ChromosomeType.PRODUCTIVITY, Dominance.DOMINANT));
        traits.put("curious_bees:traits/productivity/fast",   new Allele("curious_bees:traits/productivity/fast",   ChromosomeType.PRODUCTIVITY, Dominance.RECESSIVE));
        traits.put("curious_bees:traits/fertility/one",       new Allele("curious_bees:traits/fertility/one",       ChromosomeType.FERTILITY,    Dominance.RECESSIVE));
        traits.put("curious_bees:traits/fertility/two",       new Allele("curious_bees:traits/fertility/two",       ChromosomeType.FERTILITY,    Dominance.DOMINANT));
        traits.put("curious_bees:traits/fertility/three",     new Allele("curious_bees:traits/fertility/three",     ChromosomeType.FERTILITY,    Dominance.RECESSIVE));
        traits.put("curious_bees:traits/flower_type/flowers", new Allele("curious_bees:traits/flower_type/flowers", ChromosomeType.FLOWER_TYPE,  Dominance.DOMINANT));
        traits.put("curious_bees:traits/flower_type/cactus",  new Allele("curious_bees:traits/flower_type/cactus",  ChromosomeType.FLOWER_TYPE,  Dominance.RECESSIVE));
        traits.put("curious_bees:traits/flower_type/leaves",  new Allele("curious_bees:traits/flower_type/leaves",  ChromosomeType.FLOWER_TYPE,  Dominance.RECESSIVE));
        knownTraitAlleles = Map.copyOf(traits);

        knownSpeciesAlleles = Map.of(
                "curious_bees:species/meadow",     new Allele("curious_bees:species/meadow",     ChromosomeType.SPECIES, Dominance.DOMINANT),
                "curious_bees:species/forest",     new Allele("curious_bees:species/forest",     ChromosomeType.SPECIES, Dominance.DOMINANT),
                "curious_bees:species/arid",       new Allele("curious_bees:species/arid",       ChromosomeType.SPECIES, Dominance.RECESSIVE),
                "curious_bees:species/cultivated", new Allele("curious_bees:species/cultivated", ChromosomeType.SPECIES, Dominance.DOMINANT),
                "curious_bees:species/hardy",      new Allele("curious_bees:species/hardy",      ChromosomeType.SPECIES, Dominance.RECESSIVE)
        );
    }

    // -------------------------------------------------------------------------
    // Trait allele conversion
    // -------------------------------------------------------------------------

    @Test
    void toTraitAllele_lifespanNormal_convertsCorrectly() {
        var dto = new TraitAlleleDefinitionData(
                "curious_bees:traits/lifespan/normal", "LIFESPAN", "Normal", "DOMINANT");
        Allele allele = ContentConverter.toTraitAllele(dto);

        assertEquals("curious_bees:traits/lifespan/normal", allele.id());
        assertEquals(ChromosomeType.LIFESPAN, allele.chromosomeType());
        assertEquals(Dominance.DOMINANT, allele.dominance());
    }

    @Test
    void toTraitAllele_productivityFast_convertsCorrectly() {
        var dto = new TraitAlleleDefinitionData(
                "curious_bees:traits/productivity/fast", "PRODUCTIVITY", "Fast", "RECESSIVE");
        Allele allele = ContentConverter.toTraitAllele(dto);

        assertEquals(ChromosomeType.PRODUCTIVITY, allele.chromosomeType());
        assertEquals(Dominance.RECESSIVE, allele.dominance());
    }

    @Test
    void toTraitAllele_flowerTypeCactus_convertsCorrectly() {
        var dto = new TraitAlleleDefinitionData(
                "curious_bees:traits/flower_type/cactus", "FLOWER_TYPE", "Cactus", "RECESSIVE");
        Allele allele = ContentConverter.toTraitAllele(dto);

        assertEquals(ChromosomeType.FLOWER_TYPE, allele.chromosomeType());
    }

    @Test
    void toTraitAllele_unknownChromosomeType_throws() {
        var dto = new TraitAlleleDefinitionData("id", "UNKNOWN", "Name", "DOMINANT");
        assertThrows(ContentConversionException.class, () -> ContentConverter.toTraitAllele(dto));
    }

    @Test
    void toTraitAllele_unknownDominance_throws() {
        var dto = new TraitAlleleDefinitionData("id", "LIFESPAN", "Name", "UNKNOWN");
        assertThrows(ContentConversionException.class, () -> ContentConverter.toTraitAllele(dto));
    }

    @Test
    void toTraitAlleles_convertsList() {
        var dtos = List.of(
                new TraitAlleleDefinitionData("curious_bees:traits/lifespan/short",  "LIFESPAN", "Short",  "RECESSIVE"),
                new TraitAlleleDefinitionData("curious_bees:traits/lifespan/normal", "LIFESPAN", "Normal", "DOMINANT")
        );
        List<Allele> alleles = ContentConverter.toTraitAlleles(dtos);
        assertEquals(2, alleles.size());
        assertEquals(ChromosomeType.LIFESPAN, alleles.get(0).chromosomeType());
    }

    // -------------------------------------------------------------------------
    // Species conversion
    // -------------------------------------------------------------------------

    @Test
    void toSpeciesDefinition_meadow_convertsCorrectly() {
        var dto = meadowData();
        BeeSpeciesDefinition def = ContentConverter.toSpeciesDefinition(dto, knownTraitAlleles);

        assertEquals("curious_bees:species/meadow", def.id());
        assertEquals("Meadow Bee", def.displayName());
        assertEquals("curious_bees:species/meadow", def.speciesAllele().id());
        assertEquals(ChromosomeType.SPECIES, def.speciesAllele().chromosomeType());
        assertEquals(Dominance.DOMINANT, def.speciesAllele().dominance());
        assertEquals(List.of("plains", "flower_forest"), def.spawnContextNotes());
    }

    @Test
    void toSpeciesDefinition_meadow_traitAllelesResolveCorrectly() {
        BeeSpeciesDefinition def = ContentConverter.toSpeciesDefinition(meadowData(), knownTraitAlleles);

        Allele[] lifespan = def.defaultTraitAlleles(ChromosomeType.LIFESPAN);
        assertEquals("curious_bees:traits/lifespan/normal", lifespan[0].id());
        assertEquals(ChromosomeType.LIFESPAN, lifespan[0].chromosomeType());
    }

    @Test
    void toSpeciesDefinition_arid_hasRecessiveSpeciesAllele() {
        var dto = new SpeciesDefinitionData(
                "curious_bees:species/arid", "Arid Bee", "RECESSIVE",
                Map.of(
                        "LIFESPAN",     new TraitAllelePairData("curious_bees:traits/lifespan/normal",     "curious_bees:traits/lifespan/normal"),
                        "PRODUCTIVITY", new TraitAllelePairData("curious_bees:traits/productivity/slow",   "curious_bees:traits/productivity/normal"),
                        "FERTILITY",    new TraitAllelePairData("curious_bees:traits/fertility/one",        "curious_bees:traits/fertility/two"),
                        "FLOWER_TYPE",  new TraitAllelePairData("curious_bees:traits/flower_type/cactus",  "curious_bees:traits/flower_type/cactus")
                ));
        BeeSpeciesDefinition def = ContentConverter.toSpeciesDefinition(dto, knownTraitAlleles);
        assertEquals(Dominance.RECESSIVE, def.speciesAllele().dominance());
    }

    @Test
    void toSpeciesDefinition_unknownTraitAlleleId_throws() {
        var dto = new SpeciesDefinitionData(
                "curious_bees:species/meadow", "Meadow Bee", "DOMINANT",
                Map.of(
                        "LIFESPAN",     new TraitAllelePairData("curious_bees:traits/lifespan/NONEXISTENT", "curious_bees:traits/lifespan/normal"),
                        "PRODUCTIVITY", new TraitAllelePairData("curious_bees:traits/productivity/normal",  "curious_bees:traits/productivity/normal"),
                        "FERTILITY",    new TraitAllelePairData("curious_bees:traits/fertility/two",         "curious_bees:traits/fertility/two"),
                        "FLOWER_TYPE",  new TraitAllelePairData("curious_bees:traits/flower_type/flowers",  "curious_bees:traits/flower_type/flowers")
                ));
        assertThrows(ContentConversionException.class,
                () -> ContentConverter.toSpeciesDefinition(dto, knownTraitAlleles));
    }

    // -------------------------------------------------------------------------
    // Mutation conversion
    // -------------------------------------------------------------------------

    @Test
    void toMutationDefinition_cultivated_convertsCorrectly() {
        var dto = new MutationDefinitionData(
                "curious_bees:mutations/cultivated_from_meadow_forest",
                "curious_bees:species/meadow",
                "curious_bees:species/forest",
                "curious_bees:species/cultivated",
                0.12,
                new MutationResultModesData(0.95, 0.05));

        MutationDefinition def = ContentConverter.toMutationDefinition(dto, knownSpeciesAlleles);

        assertEquals("curious_bees:mutations/cultivated_from_meadow_forest", def.id());
        assertEquals("curious_bees:species/meadow", def.parentSpeciesAId());
        assertEquals("curious_bees:species/forest", def.parentSpeciesBId());
        assertEquals("curious_bees:species/cultivated", def.resultSpeciesAllele().id());
        assertEquals(0.12, def.baseChance());
        assertEquals(MutationResultMode.PARTIAL, def.resultMode()); // 0.95 partial >= 0.05 full
    }

    @Test
    void toMutationDefinition_hardy_convertsCorrectly() {
        var dto = new MutationDefinitionData(
                "curious_bees:mutations/hardy_from_forest_arid",
                "curious_bees:species/forest",
                "curious_bees:species/arid",
                "curious_bees:species/hardy",
                0.08,
                new MutationResultModesData(0.95, 0.05));

        MutationDefinition def = ContentConverter.toMutationDefinition(dto, knownSpeciesAlleles);
        assertEquals(0.08, def.baseChance());
        assertEquals("curious_bees:species/hardy", def.resultSpeciesAllele().id());
    }

    @Test
    void toMutationDefinition_fullModeWhenFullChanceHigher() {
        var dto = new MutationDefinitionData(
                "id", "curious_bees:species/meadow", "curious_bees:species/forest",
                "curious_bees:species/cultivated", 0.10,
                new MutationResultModesData(0.20, 0.80)); // full > partial
        MutationDefinition def = ContentConverter.toMutationDefinition(dto, knownSpeciesAlleles);
        assertEquals(MutationResultMode.FULL, def.resultMode());
    }

    @Test
    void toMutationDefinition_unknownResultSpecies_throws() {
        var dto = new MutationDefinitionData(
                "id", "curious_bees:species/meadow", "curious_bees:species/forest",
                "curious_bees:species/nonexistent", 0.12,
                new MutationResultModesData(0.95, 0.05));
        assertThrows(ContentConversionException.class,
                () -> ContentConverter.toMutationDefinition(dto, knownSpeciesAlleles));
    }

    // -------------------------------------------------------------------------
    // Production conversion
    // -------------------------------------------------------------------------

    @Test
    void toProductionDefinition_cultivated_convertsCorrectly() {
        var dto = new ProductionDefinitionData(
                "curious_bees:species/cultivated",
                List.of(new ProductionOutputData("curiousbees:cultivated_comb", 0.90)),
                List.of(new ProductionOutputData("minecraft:honeycomb", 0.30)));

        ProductionDefinition def = ContentConverter.toProductionDefinition(dto);

        assertEquals("curious_bees:species/cultivated", def.speciesId());
        assertEquals(1, def.primaryOutputs().size());
        assertEquals("curiousbees:cultivated_comb", def.primaryOutputs().get(0).outputId());
        assertEquals(0.90, def.primaryOutputs().get(0).chance());
        assertEquals(1, def.secondaryOutputs().size());
    }

    @Test
    void toProductionDefinition_forest_noSecondaryOutputs() {
        var dto = new ProductionDefinitionData(
                "curious_bees:species/forest",
                List.of(new ProductionOutputData("curiousbees:forest_comb", 0.80)));

        ProductionDefinition def = ContentConverter.toProductionDefinition(dto);
        assertTrue(def.secondaryOutputs().isEmpty());
    }

    @Test
    void toProductionDefinition_meadow_primaryAndSecondary() {
        var dto = new ProductionDefinitionData(
                "curious_bees:species/meadow",
                List.of(new ProductionOutputData("curiousbees:meadow_comb", 0.80)),
                List.of(new ProductionOutputData("minecraft:honeycomb", 0.20)));

        ProductionDefinition def = ContentConverter.toProductionDefinition(dto);
        assertEquals(0.80, def.primaryOutputs().get(0).chance());
        assertEquals(0.20, def.secondaryOutputs().get(0).chance());
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static SpeciesDefinitionData meadowData() {
        return new SpeciesDefinitionData(
                "curious_bees:species/meadow", "Meadow Bee", "DOMINANT",
                Map.of(
                        "LIFESPAN",     new TraitAllelePairData("curious_bees:traits/lifespan/normal",     "curious_bees:traits/lifespan/normal"),
                        "PRODUCTIVITY", new TraitAllelePairData("curious_bees:traits/productivity/normal", "curious_bees:traits/productivity/normal"),
                        "FERTILITY",    new TraitAllelePairData("curious_bees:traits/fertility/two",        "curious_bees:traits/fertility/two"),
                        "FLOWER_TYPE",  new TraitAllelePairData("curious_bees:traits/flower_type/flowers", "curious_bees:traits/flower_type/flowers")
                ),
                List.of("plains", "flower_forest"));
    }
}
