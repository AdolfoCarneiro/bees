package com.curiousbees.common.content.conversion;

import com.curiousbees.common.content.data.MutationDefinitionData;
import com.curiousbees.common.content.data.ProductionDefinitionData;
import com.curiousbees.common.content.data.ProductionOutputData;
import com.curiousbees.common.content.data.SpeciesDefinitionData;
import com.curiousbees.common.content.data.TraitAlleleDefinitionData;
import com.curiousbees.common.content.data.TraitAllelePairData;
import com.curiousbees.common.content.species.BeeSpeciesDefinition;
import com.curiousbees.common.gameplay.production.ProductionDefinition;
import com.curiousbees.common.gameplay.production.ProductionOutput;
import com.curiousbees.common.genetics.model.Allele;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Dominance;
import com.curiousbees.common.genetics.mutation.MutationDefinition;
import com.curiousbees.common.genetics.mutation.MutationResultMode;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Converts validated content DTOs into runtime domain definitions.
 * Caller must ensure DTOs have been validated before conversion.
 * Pure Java; no Minecraft or NeoForge dependencies.
 */
public final class ContentConverter {

    private static final Logger LOGGER = Logger.getLogger(ContentConverter.class.getName());

    private ContentConverter() {}

    // -------------------------------------------------------------------------
    // Trait allele conversion
    // -------------------------------------------------------------------------

    /**
     * Converts a validated TraitAlleleDefinitionData into an Allele.
     *
     * @param dto a structurally valid trait allele DTO
     * @return the runtime Allele
     * @throws ContentConversionException if the chromosomeType or dominance string is unrecognized
     */
    public static Allele toTraitAllele(TraitAlleleDefinitionData dto) {
        Objects.requireNonNull(dto, "dto must not be null");
        ChromosomeType type = parseChromosomeType(dto.chromosomeType(), dto.id());
        Dominance dominance = parseDominance(dto.dominance(), dto.id());
        LOGGER.fine(() -> "Converting trait allele: " + dto.id());
        return new Allele(dto.id(), type, dominance);
    }

    /**
     * Converts a collection of validated trait allele DTOs.
     * Returns alleles in the same order as the input.
     */
    public static List<Allele> toTraitAlleles(List<TraitAlleleDefinitionData> dtos) {
        Objects.requireNonNull(dtos, "dtos must not be null");
        List<Allele> result = new ArrayList<>(dtos.size());
        for (TraitAlleleDefinitionData dto : dtos) {
            result.add(toTraitAllele(dto));
        }
        return List.copyOf(result);
    }

    // -------------------------------------------------------------------------
    // Species conversion
    // -------------------------------------------------------------------------

    /**
     * Converts a validated SpeciesDefinitionData into a BeeSpeciesDefinition.
     *
     * @param dto             a structurally valid species DTO
     * @param knownTraitAlleles map from allele id to runtime Allele (for trait lookups)
     * @return the runtime BeeSpeciesDefinition
     * @throws ContentConversionException if any referenced allele ID is not in knownTraitAlleles
     */
    public static BeeSpeciesDefinition toSpeciesDefinition(
            SpeciesDefinitionData dto, Map<String, Allele> knownTraitAlleles) {
        Objects.requireNonNull(dto, "dto must not be null");
        Objects.requireNonNull(knownTraitAlleles, "knownTraitAlleles must not be null");

        Dominance speciesDominance = parseDominance(dto.dominance(), dto.id());
        Allele speciesAllele = new Allele(dto.id(), ChromosomeType.SPECIES, speciesDominance);

        Map<ChromosomeType, Allele[]> traitAlleles = new EnumMap<>(ChromosomeType.class);
        for (Map.Entry<String, TraitAllelePairData> entry : dto.defaultTraits().entrySet()) {
            ChromosomeType type = parseChromosomeType(entry.getKey(), dto.id());
            TraitAllelePairData pair = entry.getValue();

            Allele first = resolveTraitAllele(pair.first(), knownTraitAlleles, dto.id());
            Allele second = resolveTraitAllele(pair.second(), knownTraitAlleles, dto.id());
            traitAlleles.put(type, new Allele[]{first, second});
        }

        LOGGER.fine(() -> "Converting species: " + dto.id());
        return new BeeSpeciesDefinition(dto.id(), dto.displayName(), speciesAllele,
                traitAlleles, dto.spawnContextNotes());
    }

    // -------------------------------------------------------------------------
    // Mutation conversion
    // -------------------------------------------------------------------------

    /**
     * Converts a validated MutationDefinitionData into a MutationDefinition.
     *
     * <p>Note: the current MutationDefinition model supports one MutationResultMode.
     * This converter selects PARTIAL when partialChance >= fullChance, FULL otherwise.
     * A follow-up task may extend MutationDefinition to support weighted result modes.
     *
     * @param dto                a structurally valid mutation DTO
     * @param knownSpeciesAlleles map from species ID to its runtime species Allele
     * @return the runtime MutationDefinition
     */
    public static MutationDefinition toMutationDefinition(
            MutationDefinitionData dto, Map<String, Allele> knownSpeciesAlleles) {
        Objects.requireNonNull(dto, "dto must not be null");
        Objects.requireNonNull(knownSpeciesAlleles, "knownSpeciesAlleles must not be null");

        Allele resultAllele = resolveSpeciesAllele(dto.resultSpeciesId(), knownSpeciesAlleles, dto.id());

        MutationResultMode mode = dto.resultModes().partialChance() >= dto.resultModes().fullChance()
                ? MutationResultMode.PARTIAL
                : MutationResultMode.FULL;

        LOGGER.fine(() -> "Converting mutation: " + dto.id() + " (resultMode=" + mode + ")");
        return new MutationDefinition(dto.id(), dto.parentSpeciesAId(), dto.parentSpeciesBId(),
                resultAllele, dto.baseChance(), mode);
    }

    // -------------------------------------------------------------------------
    // Production conversion
    // -------------------------------------------------------------------------

    /**
     * Converts a validated ProductionDefinitionData into a ProductionDefinition.
     * Stack size uses the output's max value as the count (MVP: single-item stacks).
     */
    public static ProductionDefinition toProductionDefinition(ProductionDefinitionData dto) {
        Objects.requireNonNull(dto, "dto must not be null");

        List<ProductionOutput> primary = convertOutputs(dto.primaryOutputs(), dto.speciesId());
        List<ProductionOutput> secondary = convertOutputs(dto.secondaryOutputs(), dto.speciesId());

        LOGGER.fine(() -> "Converting production: " + dto.speciesId());
        return new ProductionDefinition(dto.speciesId(), primary, secondary);
    }

    // -------------------------------------------------------------------------
    // Internal helpers
    // -------------------------------------------------------------------------

    private static List<ProductionOutput> convertOutputs(
            List<ProductionOutputData> outputs, String context) {
        if (outputs == null || outputs.isEmpty()) return List.of();
        List<ProductionOutput> result = new ArrayList<>(outputs.size());
        for (ProductionOutputData out : outputs) {
            result.add(new ProductionOutput(out.item(), out.chance()));
        }
        return List.copyOf(result);
    }

    private static Allele resolveTraitAllele(String id, Map<String, Allele> known, String context) {
        Allele allele = known.get(id);
        if (allele == null) {
            throw new ContentConversionException(
                    "Species '" + context + "' references unknown trait allele id: " + id);
        }
        return allele;
    }

    private static Allele resolveSpeciesAllele(String id, Map<String, Allele> known, String context) {
        Allele allele = known.get(id);
        if (allele == null) {
            throw new ContentConversionException(
                    "Mutation '" + context + "' references unknown species allele id: " + id);
        }
        return allele;
    }

    private static ChromosomeType parseChromosomeType(String name, String context) {
        try {
            return ChromosomeType.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new ContentConversionException(
                    "Unknown chromosomeType '" + name + "' in content '" + context + "'");
        }
    }

    private static Dominance parseDominance(String name, String context) {
        try {
            return Dominance.valueOf(name);
        } catch (IllegalArgumentException e) {
            throw new ContentConversionException(
                    "Unknown dominance '" + name + "' in content '" + context + "'");
        }
    }
}
