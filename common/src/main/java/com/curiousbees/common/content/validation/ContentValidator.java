package com.curiousbees.common.content.validation;

import com.curiousbees.common.content.data.MutationDefinitionData;
import com.curiousbees.common.content.data.ProductionDefinitionData;
import com.curiousbees.common.content.data.ProductionOutputData;
import com.curiousbees.common.content.data.SpeciesDefinitionData;
import com.curiousbees.common.content.data.SpeciesVisualData;
import com.curiousbees.common.content.data.TraitAlleleDefinitionData;
import com.curiousbees.common.content.data.TraitAllelePairData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Stateless validator for content data DTOs.
 * Accumulates all errors in a pass before returning — never fails on first error only.
 * Pure Java; no Minecraft or NeoForge dependencies.
 */
public final class ContentValidator {

    private static final Logger LOGGER = Logger.getLogger(ContentValidator.class.getName());

    private static final Set<String> VALID_DOMINANCE = Set.of("DOMINANT", "RECESSIVE");

    private static final Set<String> VALID_TRAIT_CHROMOSOME_TYPES =
            Set.of("LIFESPAN", "PRODUCTIVITY", "FERTILITY", "FLOWER_TYPE");

    private static final Set<String> REQUIRED_TRAIT_SLOTS =
            Set.of("LIFESPAN", "PRODUCTIVITY", "FERTILITY", "FLOWER_TYPE");

    private ContentValidator() {}

    // -------------------------------------------------------------------------
    // Trait alleles
    // -------------------------------------------------------------------------

    /**
     * Validates a collection of trait allele definitions.
     * Checks structural validity and duplicate IDs.
     */
    public static ContentValidationResult validateTraitAlleles(
            Collection<TraitAlleleDefinitionData> alleles) {
        Objects.requireNonNull(alleles, "alleles must not be null");
        List<String> errors = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        for (TraitAlleleDefinitionData dto : alleles) {
            if (dto == null) { errors.add("null entry in trait allele collection"); continue; }
            validateTraitAllele(dto, errors);
            if (!dto.id().isBlank() && !seen.add(dto.id())) {
                errors.add("duplicate trait allele id: " + dto.id());
            }
        }

        if (!errors.isEmpty()) LOGGER.warning("Trait allele validation failed:\n" + String.join("\n", errors));
        return ContentValidationResult.of(errors);
    }

    /** Validates a single trait allele definition (structural only). */
    public static ContentValidationResult validateTraitAllele(TraitAlleleDefinitionData dto) {
        Objects.requireNonNull(dto, "dto must not be null");
        List<String> errors = new ArrayList<>();
        validateTraitAllele(dto, errors);
        return ContentValidationResult.of(errors);
    }

    private static void validateTraitAllele(TraitAlleleDefinitionData dto, List<String> errors) {
        String ctx = "trait allele '" + dto.id() + "'";
        if (dto.id() == null || dto.id().isBlank()) errors.add(ctx + ": id must not be blank");
        if (dto.displayName() == null || dto.displayName().isBlank()) errors.add(ctx + ": displayName must not be blank");
        if (!VALID_DOMINANCE.contains(dto.dominance())) {
            errors.add(ctx + ": invalid dominance '" + dto.dominance() + "' (must be DOMINANT or RECESSIVE)");
        }
        if (!VALID_TRAIT_CHROMOSOME_TYPES.contains(dto.chromosomeType())) {
            errors.add(ctx + ": invalid chromosomeType '" + dto.chromosomeType()
                    + "' (trait alleles must not use SPECIES; valid: " + VALID_TRAIT_CHROMOSOME_TYPES + ")");
        }
    }

    // -------------------------------------------------------------------------
    // Species
    // -------------------------------------------------------------------------

    /**
     * Validates a collection of species definitions.
     * knownTraitAlleleIds: the set of all known trait allele IDs for referential checks.
     */
    public static ContentValidationResult validateSpecies(
            Collection<SpeciesDefinitionData> species,
            Set<String> knownTraitAlleleIds) {
        Objects.requireNonNull(species, "species must not be null");
        Objects.requireNonNull(knownTraitAlleleIds, "knownTraitAlleleIds must not be null");
        List<String> errors = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        for (SpeciesDefinitionData dto : species) {
            if (dto == null) { errors.add("null entry in species collection"); continue; }
            validateSpeciesDefinition(dto, knownTraitAlleleIds, errors);
            if (!dto.id().isBlank() && !seen.add(dto.id())) {
                errors.add("duplicate species id: " + dto.id());
            }
        }

        if (!errors.isEmpty()) LOGGER.warning("Species validation failed:\n" + String.join("\n", errors));
        return ContentValidationResult.of(errors);
    }

    /** Validates a single species definition. */
    public static ContentValidationResult validateSpeciesDefinition(
            SpeciesDefinitionData dto, Set<String> knownTraitAlleleIds) {
        Objects.requireNonNull(dto, "dto must not be null");
        Objects.requireNonNull(knownTraitAlleleIds, "knownTraitAlleleIds must not be null");
        List<String> errors = new ArrayList<>();
        validateSpeciesDefinition(dto, knownTraitAlleleIds, errors);
        return ContentValidationResult.of(errors);
    }

    private static void validateSpeciesDefinition(SpeciesDefinitionData dto,
                                                   Set<String> knownTraitAlleleIds,
                                                   List<String> errors) {
        String ctx = "species '" + dto.id() + "'";
        if (dto.id() == null || dto.id().isBlank()) errors.add(ctx + ": id must not be blank");
        if (dto.displayName() == null || dto.displayName().isBlank()) errors.add(ctx + ": displayName must not be blank");
        if (!VALID_DOMINANCE.contains(dto.dominance())) {
            errors.add(ctx + ": invalid dominance '" + dto.dominance() + "'");
        }

        Map<String, TraitAllelePairData> traits = dto.defaultTraits();
        if (traits == null) {
            errors.add(ctx + ": defaultTraits must not be null");
            return;
        }

        for (String required : REQUIRED_TRAIT_SLOTS) {
            TraitAllelePairData pair = traits.get(required);
            if (pair == null) {
                errors.add(ctx + ": missing required trait slot '" + required + "'");
                continue;
            }
            if (pair.first() == null || pair.first().isBlank()) {
                errors.add(ctx + ": first allele id must not be blank for slot '" + required + "'");
            } else if (!knownTraitAlleleIds.contains(pair.first())) {
                errors.add(ctx + ": unknown trait allele id '" + pair.first() + "' for slot '" + required + "'");
            }
            if (pair.second() == null || pair.second().isBlank()) {
                errors.add(ctx + ": second allele id must not be blank for slot '" + required + "'");
            } else if (!knownTraitAlleleIds.contains(pair.second())) {
                errors.add(ctx + ": unknown trait allele id '" + pair.second() + "' for slot '" + required + "'");
            }
        }

        // Trait chromosomeType consistency: if the allele id belongs to a known allele,
        // that allele's chromosomeType must match the slot. This is checked in conversion;
        // validation here only checks referential presence.

        SpeciesVisualData visual = dto.visual();
        if (visual != null) {
            if (visual.textureId() == null || visual.textureId().isBlank()) {
                errors.add(ctx + ": visual.texture must not be blank");
            }
            if (visual.modelId() != null && visual.modelId().isBlank()) {
                errors.add(ctx + ": visual.model must not be blank when present");
            }
        }
    }

    // -------------------------------------------------------------------------
    // Mutations
    // -------------------------------------------------------------------------

    /**
     * Validates a collection of mutation definitions.
     * knownSpeciesIds: all known species IDs (built-ins + loaded).
     */
    public static ContentValidationResult validateMutations(
            Collection<MutationDefinitionData> mutations,
            Set<String> knownSpeciesIds) {
        Objects.requireNonNull(mutations, "mutations must not be null");
        Objects.requireNonNull(knownSpeciesIds, "knownSpeciesIds must not be null");
        List<String> errors = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        for (MutationDefinitionData dto : mutations) {
            if (dto == null) { errors.add("null entry in mutation collection"); continue; }
            validateMutationDefinition(dto, knownSpeciesIds, errors);
            if (!dto.id().isBlank() && !seen.add(dto.id())) {
                errors.add("duplicate mutation id: " + dto.id());
            }
        }

        if (!errors.isEmpty()) LOGGER.warning("Mutation validation failed:\n" + String.join("\n", errors));
        return ContentValidationResult.of(errors);
    }

    /** Validates a single mutation definition. */
    public static ContentValidationResult validateMutationDefinition(
            MutationDefinitionData dto, Set<String> knownSpeciesIds) {
        Objects.requireNonNull(dto, "dto must not be null");
        Objects.requireNonNull(knownSpeciesIds, "knownSpeciesIds must not be null");
        List<String> errors = new ArrayList<>();
        validateMutationDefinition(dto, knownSpeciesIds, errors);
        return ContentValidationResult.of(errors);
    }

    private static void validateMutationDefinition(MutationDefinitionData dto,
                                                    Set<String> knownSpeciesIds,
                                                    List<String> errors) {
        String ctx = "mutation '" + dto.id() + "'";
        if (dto.id() == null || dto.id().isBlank()) errors.add(ctx + ": id must not be blank");

        if (dto.parentSpeciesAId() == null || dto.parentSpeciesAId().isBlank()) {
            errors.add(ctx + ": parentSpeciesAId must not be blank");
        } else if (!knownSpeciesIds.contains(dto.parentSpeciesAId())) {
            errors.add(ctx + ": unknown parent species '" + dto.parentSpeciesAId() + "'");
        }

        if (dto.parentSpeciesBId() == null || dto.parentSpeciesBId().isBlank()) {
            errors.add(ctx + ": parentSpeciesBId must not be blank");
        } else if (!knownSpeciesIds.contains(dto.parentSpeciesBId())) {
            errors.add(ctx + ": unknown parent species '" + dto.parentSpeciesBId() + "'");
        }

        if (dto.resultSpeciesId() == null || dto.resultSpeciesId().isBlank()) {
            errors.add(ctx + ": resultSpeciesId must not be blank");
        } else if (!knownSpeciesIds.contains(dto.resultSpeciesId())) {
            errors.add(ctx + ": unknown result species '" + dto.resultSpeciesId() + "'");
        }

        if (dto.baseChance() < 0.0 || dto.baseChance() > 1.0) {
            errors.add(ctx + ": baseChance must be between 0.0 and 1.0, got " + dto.baseChance());
        }

        if (dto.resultModes() == null) {
            errors.add(ctx + ": resultModes must not be null");
        } else {
            double partial = dto.resultModes().partialChance();
            double full = dto.resultModes().fullChance();
            if (partial < 0.0 || partial > 1.0) {
                errors.add(ctx + ": resultModes.partialChance must be 0.0–1.0, got " + partial);
            }
            if (full < 0.0 || full > 1.0) {
                errors.add(ctx + ": resultModes.fullChance must be 0.0–1.0, got " + full);
            }
            double sum = partial + full;
            if (Math.abs(sum - 1.0) > 1e-9) {
                errors.add(ctx + ": resultModes partial + full must sum to 1.0, got " + sum);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Production
    // -------------------------------------------------------------------------

    /**
     * Validates a collection of production definitions.
     * knownSpeciesIds: all known species IDs.
     */
    public static ContentValidationResult validateProduction(
            Collection<ProductionDefinitionData> production,
            Set<String> knownSpeciesIds) {
        Objects.requireNonNull(production, "production must not be null");
        Objects.requireNonNull(knownSpeciesIds, "knownSpeciesIds must not be null");
        List<String> errors = new ArrayList<>();
        Set<String> seen = new HashSet<>();

        for (ProductionDefinitionData dto : production) {
            if (dto == null) { errors.add("null entry in production collection"); continue; }
            validateProductionDefinition(dto, knownSpeciesIds, errors);
            if (dto.speciesId() != null && !dto.speciesId().isBlank() && !seen.add(dto.speciesId())) {
                errors.add("duplicate production definition for species: " + dto.speciesId());
            }
        }

        if (!errors.isEmpty()) LOGGER.warning("Production validation failed:\n" + String.join("\n", errors));
        return ContentValidationResult.of(errors);
    }

    /** Validates a single production definition. */
    public static ContentValidationResult validateProductionDefinition(
            ProductionDefinitionData dto, Set<String> knownSpeciesIds) {
        Objects.requireNonNull(dto, "dto must not be null");
        Objects.requireNonNull(knownSpeciesIds, "knownSpeciesIds must not be null");
        List<String> errors = new ArrayList<>();
        validateProductionDefinition(dto, knownSpeciesIds, errors);
        return ContentValidationResult.of(errors);
    }

    private static void validateProductionDefinition(ProductionDefinitionData dto,
                                                      Set<String> knownSpeciesIds,
                                                      List<String> errors) {
        String ctx = "production '" + dto.speciesId() + "'";
        if (dto.speciesId() == null || dto.speciesId().isBlank()) {
            errors.add(ctx + ": speciesId must not be blank");
        } else if (!knownSpeciesIds.contains(dto.speciesId())) {
            errors.add(ctx + ": unknown species '" + dto.speciesId() + "'");
        }

        if (dto.primaryOutputs() == null || dto.primaryOutputs().isEmpty()) {
            errors.add(ctx + ": primaryOutputs must not be empty");
        } else {
            int i = 0;
            for (ProductionOutputData out : dto.primaryOutputs()) {
                validateOutput(ctx + " primaryOutputs[" + i + "]", out, errors);
                i++;
            }
        }

        if (dto.secondaryOutputs() != null) {
            int i = 0;
            for (ProductionOutputData out : dto.secondaryOutputs()) {
                validateOutput(ctx + " secondaryOutputs[" + i + "]", out, errors);
                i++;
            }
        }
    }

    private static void validateOutput(String ctx, ProductionOutputData out, List<String> errors) {
        if (out == null) { errors.add(ctx + ": null output entry"); return; }
        if (out.item() == null || out.item().isBlank()) errors.add(ctx + ": item must not be blank");
        if (out.chance() < 0.0 || out.chance() > 1.0) {
            errors.add(ctx + ": chance must be 0.0–1.0, got " + out.chance());
        }
        if (out.min() < 1) errors.add(ctx + ": min must be >= 1, got " + out.min());
        if (out.max() < out.min()) errors.add(ctx + ": max must be >= min, got max=" + out.max() + " min=" + out.min());
    }
}
