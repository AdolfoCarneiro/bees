package com.curiousbees.common.content.builtin;

import com.curiousbees.common.content.conversion.ContentConverter;
import com.curiousbees.common.content.data.TraitAlleleDefinitionData;
import com.curiousbees.common.content.species.BeeSpeciesDefinition;
import com.curiousbees.common.content.validation.ContentValidationResult;
import com.curiousbees.common.content.validation.ContentValidator;
import com.curiousbees.common.genetics.model.Allele;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Dominance;
import com.curiousbees.common.genetics.mutation.MutationDefinition;
import com.curiousbees.common.gameplay.production.ProductionDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies that the MVP built-in content can be represented and converted
 * through the data-driven pipeline (Task 8E).
 *
 * Also checks consistency between BuiltinContentData DTOs and the existing
 * runtime built-ins in BuiltinBeeTraits / BuiltinBeeSpecies / etc.
 */
class BuiltinContentDataTest {

    private Map<String, Allele> runtimeTraitAlleles;
    private Map<String, Allele> runtimeSpeciesAlleles;

    @BeforeEach
    void convertBuiltins() {
        // 1. Convert trait alleles
        List<Allele> traitList = ContentConverter.toTraitAlleles(BuiltinContentData.TRAIT_ALLELES);
        Map<String, Allele> traitMap = new HashMap<>();
        for (Allele a : traitList) traitMap.put(a.id(), a);
        runtimeTraitAlleles = Map.copyOf(traitMap);

        // 2. Convert species and collect species alleles.
        Map<String, Allele> speciesMap = new HashMap<>();
        for (var dto : BuiltinContentData.SPECIES) {
            BeeSpeciesDefinition def = ContentConverter.toSpeciesDefinition(dto, runtimeTraitAlleles);
            speciesMap.put(def.speciesAllele().id(), def.speciesAllele());
        }
        runtimeSpeciesAlleles = Map.copyOf(speciesMap);
    }

    // -------------------------------------------------------------------------
    // Trait validation
    // -------------------------------------------------------------------------

    @Test
    void builtinTraitAlleles_passValidation() {
        ContentValidationResult result = ContentValidator.validateTraitAlleles(BuiltinContentData.TRAIT_ALLELES);
        assertTrue(result.isValid(), result::combinedMessage);
    }

    @Test
    void builtinTraitAlleles_hasExpectedCount() {
        assertEquals(12, BuiltinContentData.TRAIT_ALLELES.size());
    }

    @Test
    void builtinTraitAlleles_convertToCorrectRuntimeAlleles() {
        assertEquals(12, runtimeTraitAlleles.size());

        Allele lifespanNormal = runtimeTraitAlleles.get("curious_bees:traits/lifespan/normal");
        assertNotNull(lifespanNormal);
        assertEquals(ChromosomeType.LIFESPAN, lifespanNormal.chromosomeType());
        assertEquals(Dominance.DOMINANT, lifespanNormal.dominance());

        Allele productivityFast = runtimeTraitAlleles.get("curious_bees:traits/productivity/fast");
        assertNotNull(productivityFast);
        assertEquals(Dominance.RECESSIVE, productivityFast.dominance());
    }

    @Test
    void builtinTraitAlleles_matchExistingRuntimeBuiltins() {
        // BuiltinBeeTraits still defines the same alleles; verify IDs match.
        Set<String> dataIds = BuiltinContentData.TRAIT_ALLELES.stream()
                .map(TraitAlleleDefinitionData::id).collect(Collectors.toSet());
        Set<String> runtimeIds = BuiltinBeeTraits.ALL.stream()
                .map(Allele::id).collect(Collectors.toSet());
        assertEquals(runtimeIds, dataIds);
    }

    // -------------------------------------------------------------------------
    // Species validation
    // -------------------------------------------------------------------------

    @Test
    void builtinSpecies_passValidation() {
        Set<String> knownTraitIds = runtimeTraitAlleles.keySet();
        ContentValidationResult result = ContentValidator.validateSpecies(
                BuiltinContentData.SPECIES, knownTraitIds);
        assertTrue(result.isValid(), result::combinedMessage);
    }

    @Test
    void builtinSpecies_hasExpectedCount() {
        assertEquals(5, BuiltinContentData.SPECIES.size());
    }

    @Test
    void builtinSpecies_meadow_convertsCorrectly() {
        var dto = BuiltinContentData.SPECIES.stream()
                .filter(s -> s.id().equals("curious_bees:species/meadow"))
                .findFirst().orElseThrow();
        BeeSpeciesDefinition def = ContentConverter.toSpeciesDefinition(dto, runtimeTraitAlleles);

        assertEquals("curious_bees:species/meadow", def.id());
        assertEquals("Meadow Bee", def.displayName());
        assertEquals(Dominance.DOMINANT, def.speciesAllele().dominance());

        // Mirrors BuiltinBeeSpecies.MEADOW
        Allele[] lifespan = def.defaultTraitAlleles(ChromosomeType.LIFESPAN);
        assertEquals(BuiltinBeeTraits.LIFESPAN_NORMAL, lifespan[0]);
        assertEquals(BuiltinBeeTraits.LIFESPAN_NORMAL, lifespan[1]);
    }

    @Test
    void builtinSpecies_arid_hasRecessiveSpeciesAllele() {
        var dto = BuiltinContentData.SPECIES.stream()
                .filter(s -> s.id().equals("curious_bees:species/arid"))
                .findFirst().orElseThrow();
        BeeSpeciesDefinition def = ContentConverter.toSpeciesDefinition(dto, runtimeTraitAlleles);
        assertEquals(Dominance.RECESSIVE, def.speciesAllele().dominance());
    }

    @Test
    void builtinSpecies_hardy_hasRecessiveSpeciesAllele() {
        var dto = BuiltinContentData.SPECIES.stream()
                .filter(s -> s.id().equals("curious_bees:species/hardy"))
                .findFirst().orElseThrow();
        BeeSpeciesDefinition def = ContentConverter.toSpeciesDefinition(dto, runtimeTraitAlleles);
        assertEquals(Dominance.RECESSIVE, def.speciesAllele().dominance());
    }

    @Test
    void builtinSpecies_matchExistingRuntimeBuiltinIds() {
        Set<String> dataIds = BuiltinContentData.SPECIES.stream()
                .map(s -> s.id()).collect(Collectors.toSet());
        Set<String> runtimeIds = BuiltinBeeSpecies.ALL.stream()
                .map(BeeSpeciesDefinition::id).collect(Collectors.toSet());
        assertEquals(runtimeIds, dataIds);
    }

    // -------------------------------------------------------------------------
    // Mutation validation
    // -------------------------------------------------------------------------

    @Test
    void builtinMutations_passValidation() {
        Set<String> knownSpeciesIds = runtimeSpeciesAlleles.keySet();
        ContentValidationResult result = ContentValidator.validateMutations(
                BuiltinContentData.MUTATIONS, knownSpeciesIds);
        assertTrue(result.isValid(), result::combinedMessage);
    }

    @Test
    void builtinMutations_cultivated_convertsCorrectly() {
        var dto = BuiltinContentData.MUTATIONS.get(0);
        MutationDefinition def = ContentConverter.toMutationDefinition(dto, runtimeSpeciesAlleles);

        assertEquals("curious_bees:mutations/cultivated_from_meadow_forest", def.id());
        assertEquals(0.12, def.baseChance());
        assertEquals("curious_bees:species/cultivated", def.resultSpeciesAllele().id());
        assertTrue(def.matches("curious_bees:species/meadow", "curious_bees:species/forest"));
    }

    @Test
    void builtinMutations_hardy_convertsCorrectly() {
        var dto = BuiltinContentData.MUTATIONS.get(1);
        MutationDefinition def = ContentConverter.toMutationDefinition(dto, runtimeSpeciesAlleles);

        assertEquals(0.08, def.baseChance());
        assertTrue(def.matches("curious_bees:species/forest", "curious_bees:species/arid"));
    }

    // -------------------------------------------------------------------------
    // Production validation
    // -------------------------------------------------------------------------

    @Test
    void builtinProduction_passValidation() {
        Set<String> knownSpeciesIds = runtimeSpeciesAlleles.keySet();
        ContentValidationResult result = ContentValidator.validateProduction(
                BuiltinContentData.PRODUCTION, knownSpeciesIds);
        assertTrue(result.isValid(), result::combinedMessage);
    }

    @Test
    void builtinProduction_hasExpectedCount() {
        assertEquals(5, BuiltinContentData.PRODUCTION.size());
    }

    @Test
    void builtinProduction_cultivated_convertsCorrectly() {
        var dto = BuiltinContentData.PRODUCTION.stream()
                .filter(p -> p.speciesId().equals("curious_bees:species/cultivated"))
                .findFirst().orElseThrow();
        ProductionDefinition def = ContentConverter.toProductionDefinition(dto);

        assertEquals("curious_bees:species/cultivated", def.speciesId());
        assertEquals(1, def.primaryOutputs().size());
        assertEquals("curiousbees:cultivated_comb", def.primaryOutputs().get(0).outputId());
        assertEquals(0.90, def.primaryOutputs().get(0).chance());
        assertEquals(1, def.secondaryOutputs().size());
    }

    @Test
    void existingTestsSurvive_builtinBeeContentIsStillValid() {
        // Smoke test: the original built-in Java constants still exist and have correct species IDs
        assertEquals("curious_bees:species/meadow",     BuiltinBeeSpecies.MEADOW.id());
        assertEquals("curious_bees:species/forest",     BuiltinBeeSpecies.FOREST.id());
        assertEquals("curious_bees:species/arid",       BuiltinBeeSpecies.ARID.id());
        assertEquals("curious_bees:species/cultivated", BuiltinBeeSpecies.CULTIVATED.id());
        assertEquals("curious_bees:species/hardy",      BuiltinBeeSpecies.HARDY.id());
    }
}
