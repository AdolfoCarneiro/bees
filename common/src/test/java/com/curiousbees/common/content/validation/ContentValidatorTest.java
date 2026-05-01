package com.curiousbees.common.content.validation;

import com.curiousbees.common.content.data.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ContentValidatorTest {

    // -------------------------------------------------------------------------
    // Helpers — valid MVP data
    // -------------------------------------------------------------------------

    private static final Set<String> KNOWN_TRAIT_IDS = Set.of(
            "curious_bees:traits/lifespan/short",
            "curious_bees:traits/lifespan/normal",
            "curious_bees:traits/lifespan/long",
            "curious_bees:traits/productivity/slow",
            "curious_bees:traits/productivity/normal",
            "curious_bees:traits/productivity/fast",
            "curious_bees:traits/fertility/one",
            "curious_bees:traits/fertility/two",
            "curious_bees:traits/fertility/three",
            "curious_bees:traits/flower_type/flowers",
            "curious_bees:traits/flower_type/cactus",
            "curious_bees:traits/flower_type/leaves");

    private static final Set<String> KNOWN_SPECIES_IDS = Set.of(
            "curious_bees:species/meadow",
            "curious_bees:species/forest",
            "curious_bees:species/arid",
            "curious_bees:species/cultivated",
            "curious_bees:species/hardy");

    private static TraitAlleleDefinitionData validTrait(String id, String chromosomeType, String dominance) {
        return new TraitAlleleDefinitionData(id, chromosomeType, "Display Name", dominance);
    }

    private static SpeciesDefinitionData validMeadowData() {
        return new SpeciesDefinitionData(
                "curious_bees:species/meadow", "Meadow Bee", "DOMINANT",
                Map.of(
                        "LIFESPAN",     new TraitAllelePairData("curious_bees:traits/lifespan/normal",     "curious_bees:traits/lifespan/normal"),
                        "PRODUCTIVITY", new TraitAllelePairData("curious_bees:traits/productivity/normal", "curious_bees:traits/productivity/normal"),
                        "FERTILITY",    new TraitAllelePairData("curious_bees:traits/fertility/two",        "curious_bees:traits/fertility/two"),
                        "FLOWER_TYPE",  new TraitAllelePairData("curious_bees:traits/flower_type/flowers", "curious_bees:traits/flower_type/flowers")
                ));
    }

    private static MutationDefinitionData validCultivatedMutation() {
        return new MutationDefinitionData(
                "curious_bees:mutations/cultivated_from_meadow_forest",
                "curious_bees:species/meadow",
                "curious_bees:species/forest",
                "curious_bees:species/cultivated",
                0.12,
                new MutationResultModesData(0.95, 0.05));
    }

    private static ProductionDefinitionData validMeadowProduction() {
        return new ProductionDefinitionData(
                "curious_bees:species/meadow",
                List.of(new ProductionOutputData("curiousbees:meadow_comb", 0.80)),
                List.of(new ProductionOutputData("minecraft:honeycomb", 0.20)));
    }

    // -------------------------------------------------------------------------
    // Trait allele validation
    // -------------------------------------------------------------------------

    @Test
    void traitAllele_valid_passes() {
        var result = ContentValidator.validateTraitAllele(
                validTrait("curious_bees:traits/lifespan/normal", "LIFESPAN", "DOMINANT"));
        assertTrue(result.isValid());
    }

    @Test
    void traitAllele_blankId_fails() {
        var result = ContentValidator.validateTraitAllele(
                new TraitAlleleDefinitionData("", "LIFESPAN", "Normal", "DOMINANT"));
        assertFalse(result.isValid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("id must not be blank")));
    }

    @Test
    void traitAllele_blankDisplayName_fails() {
        var result = ContentValidator.validateTraitAllele(
                new TraitAlleleDefinitionData("curious_bees:traits/lifespan/normal", "LIFESPAN", "", "DOMINANT"));
        assertFalse(result.isValid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("displayName must not be blank")));
    }

    @Test
    void traitAllele_invalidDominance_fails() {
        var result = ContentValidator.validateTraitAllele(
                new TraitAlleleDefinitionData("id", "LIFESPAN", "Name", "SEMI_DOMINANT"));
        assertFalse(result.isValid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("invalid dominance")));
    }

    @Test
    void traitAllele_speciesChromosomeType_fails() {
        var result = ContentValidator.validateTraitAllele(
                new TraitAlleleDefinitionData("id", "SPECIES", "Name", "DOMINANT"));
        assertFalse(result.isValid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("invalid chromosomeType")));
    }

    @Test
    void traitAllele_unknownChromosomeType_fails() {
        var result = ContentValidator.validateTraitAllele(
                new TraitAlleleDefinitionData("id", "UNKNOWN_CHROM", "Name", "DOMINANT"));
        assertFalse(result.isValid());
    }

    @Test
    void traitAlleles_duplicateId_fails() {
        var dto = validTrait("curious_bees:traits/lifespan/normal", "LIFESPAN", "DOMINANT");
        var result = ContentValidator.validateTraitAlleles(List.of(dto, dto));
        assertFalse(result.isValid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("duplicate trait allele id")));
    }

    @Test
    void traitAlleles_validMvpSet_passes() {
        var alleles = List.of(
                validTrait("curious_bees:traits/lifespan/short",     "LIFESPAN",     "RECESSIVE"),
                validTrait("curious_bees:traits/lifespan/normal",    "LIFESPAN",     "DOMINANT"),
                validTrait("curious_bees:traits/lifespan/long",      "LIFESPAN",     "RECESSIVE"),
                validTrait("curious_bees:traits/productivity/slow",  "PRODUCTIVITY", "RECESSIVE"),
                validTrait("curious_bees:traits/productivity/normal","PRODUCTIVITY", "DOMINANT"),
                validTrait("curious_bees:traits/productivity/fast",  "PRODUCTIVITY", "RECESSIVE"),
                validTrait("curious_bees:traits/fertility/one",      "FERTILITY",    "RECESSIVE"),
                validTrait("curious_bees:traits/fertility/two",      "FERTILITY",    "DOMINANT"),
                validTrait("curious_bees:traits/fertility/three",    "FERTILITY",    "RECESSIVE"),
                validTrait("curious_bees:traits/flower_type/flowers","FLOWER_TYPE",  "DOMINANT"),
                validTrait("curious_bees:traits/flower_type/cactus", "FLOWER_TYPE",  "RECESSIVE"),
                validTrait("curious_bees:traits/flower_type/leaves", "FLOWER_TYPE",  "RECESSIVE")
        );
        assertTrue(ContentValidator.validateTraitAlleles(alleles).isValid());
    }

    // -------------------------------------------------------------------------
    // Species validation
    // -------------------------------------------------------------------------

    @Test
    void species_validMeadow_passes() {
        var result = ContentValidator.validateSpeciesDefinition(validMeadowData(), KNOWN_TRAIT_IDS);
        assertTrue(result.isValid(), result::combinedMessage);
    }

    @Test
    void species_blankId_fails() {
        var dto = new SpeciesDefinitionData("", "Meadow Bee", "DOMINANT", Map.of(
                "LIFESPAN",     new TraitAllelePairData("curious_bees:traits/lifespan/normal",     "curious_bees:traits/lifespan/normal"),
                "PRODUCTIVITY", new TraitAllelePairData("curious_bees:traits/productivity/normal", "curious_bees:traits/productivity/normal"),
                "FERTILITY",    new TraitAllelePairData("curious_bees:traits/fertility/two",        "curious_bees:traits/fertility/two"),
                "FLOWER_TYPE",  new TraitAllelePairData("curious_bees:traits/flower_type/flowers", "curious_bees:traits/flower_type/flowers")
        ));
        var result = ContentValidator.validateSpeciesDefinition(dto, KNOWN_TRAIT_IDS);
        assertFalse(result.isValid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("id must not be blank")));
    }

    @Test
    void species_missingTraitSlot_fails() {
        var dto = new SpeciesDefinitionData(
                "curious_bees:species/meadow", "Meadow Bee", "DOMINANT",
                Map.of(
                        "LIFESPAN",     new TraitAllelePairData("curious_bees:traits/lifespan/normal", "curious_bees:traits/lifespan/normal"),
                        "PRODUCTIVITY", new TraitAllelePairData("curious_bees:traits/productivity/normal", "curious_bees:traits/productivity/normal")
                        // FERTILITY and FLOWER_TYPE missing
                ));
        var result = ContentValidator.validateSpeciesDefinition(dto, KNOWN_TRAIT_IDS);
        assertFalse(result.isValid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("missing required trait slot")));
    }

    @Test
    void species_unknownTraitAlleleId_fails() {
        var dto = new SpeciesDefinitionData(
                "curious_bees:species/meadow", "Meadow Bee", "DOMINANT",
                Map.of(
                        "LIFESPAN",     new TraitAllelePairData("curious_bees:traits/lifespan/NONEXISTENT", "curious_bees:traits/lifespan/normal"),
                        "PRODUCTIVITY", new TraitAllelePairData("curious_bees:traits/productivity/normal",  "curious_bees:traits/productivity/normal"),
                        "FERTILITY",    new TraitAllelePairData("curious_bees:traits/fertility/two",         "curious_bees:traits/fertility/two"),
                        "FLOWER_TYPE",  new TraitAllelePairData("curious_bees:traits/flower_type/flowers",  "curious_bees:traits/flower_type/flowers")
                ));
        var result = ContentValidator.validateSpeciesDefinition(dto, KNOWN_TRAIT_IDS);
        assertFalse(result.isValid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("unknown trait allele id")));
    }

    @Test
    void species_duplicateId_fails() {
        var dto = validMeadowData();
        var result = ContentValidator.validateSpecies(List.of(dto, dto), KNOWN_TRAIT_IDS);
        assertFalse(result.isValid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("duplicate species id")));
    }

    @Test
    void species_invalidDominance_fails() {
        var dto = new SpeciesDefinitionData(
                "curious_bees:species/meadow", "Meadow Bee", "CODOMINANT",
                Map.of(
                        "LIFESPAN",     new TraitAllelePairData("curious_bees:traits/lifespan/normal",     "curious_bees:traits/lifespan/normal"),
                        "PRODUCTIVITY", new TraitAllelePairData("curious_bees:traits/productivity/normal", "curious_bees:traits/productivity/normal"),
                        "FERTILITY",    new TraitAllelePairData("curious_bees:traits/fertility/two",        "curious_bees:traits/fertility/two"),
                        "FLOWER_TYPE",  new TraitAllelePairData("curious_bees:traits/flower_type/flowers", "curious_bees:traits/flower_type/flowers")
                ));
        var result = ContentValidator.validateSpeciesDefinition(dto, KNOWN_TRAIT_IDS);
        assertFalse(result.isValid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("invalid dominance")));
    }

    // -------------------------------------------------------------------------
    // Mutation validation
    // -------------------------------------------------------------------------

    @Test
    void mutation_validCultivated_passes() {
        var result = ContentValidator.validateMutationDefinition(validCultivatedMutation(), KNOWN_SPECIES_IDS);
        assertTrue(result.isValid(), result::combinedMessage);
    }

    @Test
    void mutation_unknownParent_fails() {
        var dto = new MutationDefinitionData(
                "curious_bees:mutations/test",
                "curious_bees:species/unknown_parent",
                "curious_bees:species/forest",
                "curious_bees:species/cultivated",
                0.12,
                new MutationResultModesData(0.95, 0.05));
        var result = ContentValidator.validateMutationDefinition(dto, KNOWN_SPECIES_IDS);
        assertFalse(result.isValid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("unknown parent species")));
    }

    @Test
    void mutation_unknownResult_fails() {
        var dto = new MutationDefinitionData(
                "curious_bees:mutations/test",
                "curious_bees:species/meadow",
                "curious_bees:species/forest",
                "curious_bees:species/nonexistent",
                0.12,
                new MutationResultModesData(0.95, 0.05));
        var result = ContentValidator.validateMutationDefinition(dto, KNOWN_SPECIES_IDS);
        assertFalse(result.isValid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("unknown result species")));
    }

    @Test
    void mutation_chanceAboveOne_fails() {
        var dto = new MutationDefinitionData(
                "curious_bees:mutations/test",
                "curious_bees:species/meadow",
                "curious_bees:species/forest",
                "curious_bees:species/cultivated",
                1.5,
                new MutationResultModesData(0.95, 0.05));
        var result = ContentValidator.validateMutationDefinition(dto, KNOWN_SPECIES_IDS);
        assertFalse(result.isValid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("baseChance must be between 0.0 and 1.0")));
    }

    @Test
    void mutation_resultModesNotSummingToOne_fails() {
        var dto = new MutationDefinitionData(
                "curious_bees:mutations/test",
                "curious_bees:species/meadow",
                "curious_bees:species/forest",
                "curious_bees:species/cultivated",
                0.12,
                new MutationResultModesData(0.50, 0.30)); // sum = 0.80
        var result = ContentValidator.validateMutationDefinition(dto, KNOWN_SPECIES_IDS);
        assertFalse(result.isValid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("partial + full must sum to 1.0")));
    }

    @Test
    void mutation_duplicateId_fails() {
        var dto = validCultivatedMutation();
        var result = ContentValidator.validateMutations(List.of(dto, dto), KNOWN_SPECIES_IDS);
        assertFalse(result.isValid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("duplicate mutation id")));
    }

    // -------------------------------------------------------------------------
    // Production validation
    // -------------------------------------------------------------------------

    @Test
    void production_validMeadow_passes() {
        var result = ContentValidator.validateProductionDefinition(validMeadowProduction(), KNOWN_SPECIES_IDS);
        assertTrue(result.isValid(), result::combinedMessage);
    }

    @Test
    void production_unknownSpecies_fails() {
        var dto = new ProductionDefinitionData(
                "curious_bees:species/unknown_species",
                List.of(new ProductionOutputData("curiousbees:comb", 0.80)));
        var result = ContentValidator.validateProductionDefinition(dto, KNOWN_SPECIES_IDS);
        assertFalse(result.isValid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("unknown species")));
    }

    @Test
    void production_emptyPrimaryOutputs_fails() {
        var dto = new ProductionDefinitionData("curious_bees:species/meadow", List.of());
        var result = ContentValidator.validateProductionDefinition(dto, KNOWN_SPECIES_IDS);
        assertFalse(result.isValid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("primaryOutputs must not be empty")));
    }

    @Test
    void production_invalidOutputChance_fails() {
        var dto = new ProductionDefinitionData(
                "curious_bees:species/meadow",
                List.of(new ProductionOutputData("curiousbees:meadow_comb", 1.5)));
        var result = ContentValidator.validateProductionDefinition(dto, KNOWN_SPECIES_IDS);
        assertFalse(result.isValid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("chance must be 0.0–1.0")));
    }

    @Test
    void production_blankItemId_fails() {
        var dto = new ProductionDefinitionData(
                "curious_bees:species/meadow",
                List.of(new ProductionOutputData("", 0.80)));
        var result = ContentValidator.validateProductionDefinition(dto, KNOWN_SPECIES_IDS);
        assertFalse(result.isValid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("item must not be blank")));
    }

    @Test
    void production_duplicateSpecies_fails() {
        var dto = validMeadowProduction();
        var result = ContentValidator.validateProduction(List.of(dto, dto), KNOWN_SPECIES_IDS);
        assertFalse(result.isValid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("duplicate production definition")));
    }

    @Test
    void production_maxLessThanMin_fails() {
        var dto = new ProductionDefinitionData(
                "curious_bees:species/meadow",
                List.of(new ProductionOutputData("curiousbees:meadow_comb", 0.80, 3, 1)));
        var result = ContentValidator.validateProductionDefinition(dto, KNOWN_SPECIES_IDS);
        assertFalse(result.isValid());
        assertTrue(result.errors().stream().anyMatch(e -> e.contains("max must be >= min")));
    }

    // -------------------------------------------------------------------------
    // ContentValidationResult
    // -------------------------------------------------------------------------

    @Test
    void validationResult_ok_isValid() {
        assertTrue(ContentValidationResult.ok().isValid());
        assertEquals("OK", ContentValidationResult.ok().combinedMessage());
    }

    @Test
    void validationResult_withErrors_isNotValid() {
        var result = ContentValidationResult.of(List.of("error one", "error two"));
        assertFalse(result.isValid());
        assertTrue(result.combinedMessage().contains("error one"));
        assertTrue(result.combinedMessage().contains("error two"));
    }

    @Test
    void validationResult_multipleErrors_allSurface() {
        // A single call that produces 2 errors (missing trait slots)
        var dto = new SpeciesDefinitionData(
                "curious_bees:species/test", "Test Bee", "DOMINANT", Map.of());
        var result = ContentValidator.validateSpeciesDefinition(dto, KNOWN_TRAIT_IDS);
        // All 4 required slots are missing — we should get 4 errors
        assertFalse(result.isValid());
        assertTrue(result.errors().size() >= 4,
                "Expected at least 4 errors for 4 missing slots, got: " + result.errors().size());
    }
}
