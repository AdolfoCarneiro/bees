package com.curiousbees.common.content.loading;

import com.curiousbees.common.content.conversion.ContentConversionException;
import com.curiousbees.common.content.conversion.ContentConverter;
import com.curiousbees.common.content.data.MutationDefinitionData;
import com.curiousbees.common.content.data.ProductionDefinitionData;
import com.curiousbees.common.content.data.SpeciesDefinitionData;
import com.curiousbees.common.content.data.TraitAlleleDefinitionData;
import com.curiousbees.common.content.json.ContentDataJsonParser;
import com.curiousbees.common.content.json.ContentJsonParseException;
import com.curiousbees.common.content.registry.ContentRegistry;
import com.curiousbees.common.content.species.BeeSpeciesDefinition;
import com.curiousbees.common.content.validation.ContentValidationResult;
import com.curiousbees.common.content.validation.ContentValidator;
import com.curiousbees.common.gameplay.production.ProductionDefinition;
import com.curiousbees.common.genetics.model.Allele;
import com.curiousbees.common.genetics.mutation.MutationDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Loads external JSON content into a runtime registry while preserving built-ins.
 */
public final class ContentJsonLoader {

    private static final Logger LOGGER = Logger.getLogger(ContentJsonLoader.class.getName());

    private ContentJsonLoader() {}

    public static ContentLoadResult load(
            Collection<ContentDefinitionSource> traitSources,
            Collection<ContentDefinitionSource> speciesSources,
            Collection<ContentDefinitionSource> mutationSources,
            Collection<ContentDefinitionSource> productionSources) {
        Objects.requireNonNull(traitSources, "traitSources must not be null");
        Objects.requireNonNull(speciesSources, "speciesSources must not be null");
        Objects.requireNonNull(mutationSources, "mutationSources must not be null");
        Objects.requireNonNull(productionSources, "productionSources must not be null");

        List<String> errors = new ArrayList<>();
        ContentRegistry builtIns = ContentRegistry.builtIn();

        Map<String, Allele> loadedTraits = loadTraits(builtIns, traitSources, errors);
        Map<String, BeeSpeciesDefinition> loadedSpecies = loadSpecies(
                builtIns, speciesSources, loadedTraits, errors);
        Map<String, MutationDefinition> loadedMutations = loadMutations(
                builtIns, mutationSources, loadedSpecies, errors);
        Map<String, ProductionDefinition> loadedProduction = loadProduction(
                builtIns, productionSources, loadedSpecies, errors);

        ContentRegistry registry = mergeLoadedDefinitions(
                builtIns,
                loadedTraits.values(),
                loadedSpecies.values(),
                loadedMutations.values(),
                loadedProduction.values(),
                errors);

        if (!errors.isEmpty()) {
            LOGGER.warning("Content JSON loading completed with errors:\n" + formatErrors(errors));
        } else {
            LOGGER.info("Content JSON loading completed successfully.");
        }
        return new ContentLoadResult(registry, errors);
    }

    public static ContentLoadResult builtInsOnly() {
        return new ContentLoadResult(ContentRegistry.builtIn(), List.of());
    }

    private static Map<String, Allele> loadTraits(
            ContentRegistry builtIns,
            Collection<ContentDefinitionSource> sources,
            List<String> errors) {
        Map<String, Allele> loaded = new LinkedHashMap<>();
        for (ContentDefinitionSource source : sources) {
            TraitAlleleDefinitionData data = parse(source, ContentDataJsonParser::parseTraitAllele, errors);
            if (data == null) {
                continue;
            }
            if (builtIns.findTraitAllele(data.id()).isPresent()) {
                errors.add(source.path() + ": duplicate built-in trait allele id rejected: " + data.id());
                continue;
            }
            if (loaded.containsKey(data.id())) {
                errors.add(source.path() + ": duplicate loaded trait allele id rejected: " + data.id());
                continue;
            }
            ContentValidationResult validation = ContentValidator.validateTraitAllele(data);
            if (!validation.isValid()) {
                errors.add(source.path() + ": " + validation.combinedMessage());
                continue;
            }
            convert(source, () -> loaded.put(data.id(), ContentConverter.toTraitAllele(data)), errors);
        }
        return loaded;
    }

    private static Map<String, BeeSpeciesDefinition> loadSpecies(
            ContentRegistry builtIns,
            Collection<ContentDefinitionSource> sources,
            Map<String, Allele> loadedTraits,
            List<String> errors) {
        Map<String, BeeSpeciesDefinition> loaded = new LinkedHashMap<>();
        Map<String, Allele> knownTraits = traitAlleleMap(builtIns, loadedTraits);
        Set<String> knownTraitIds = knownTraits.keySet();

        for (ContentDefinitionSource source : sources) {
            SpeciesDefinitionData data = parse(source, ContentDataJsonParser::parseSpecies, errors);
            if (data == null) {
                continue;
            }
            if (builtIns.findSpecies(data.id()).isPresent()) {
                errors.add(source.path() + ": duplicate built-in species id rejected: " + data.id());
                continue;
            }
            if (loaded.containsKey(data.id())) {
                errors.add(source.path() + ": duplicate loaded species id rejected: " + data.id());
                continue;
            }
            ContentValidationResult validation = ContentValidator.validateSpeciesDefinition(data, knownTraitIds);
            if (!validation.isValid()) {
                errors.add(source.path() + ": " + validation.combinedMessage());
                continue;
            }
            convert(source, () -> loaded.put(data.id(), ContentConverter.toSpeciesDefinition(data, knownTraits)), errors);
        }
        return loaded;
    }

    private static Map<String, MutationDefinition> loadMutations(
            ContentRegistry builtIns,
            Collection<ContentDefinitionSource> sources,
            Map<String, BeeSpeciesDefinition> loadedSpecies,
            List<String> errors) {
        Map<String, MutationDefinition> loaded = new LinkedHashMap<>();
        Map<String, Allele> speciesAlleles = speciesAlleleMap(builtIns, loadedSpecies);
        Set<String> knownSpeciesIds = speciesAlleles.keySet();
        Set<String> builtInMutationIds = idsOfMutations(builtIns.allMutations());

        for (ContentDefinitionSource source : sources) {
            MutationDefinitionData data = parse(source, ContentDataJsonParser::parseMutation, errors);
            if (data == null) {
                continue;
            }
            if (builtInMutationIds.contains(data.id())) {
                errors.add(source.path() + ": duplicate built-in mutation id rejected: " + data.id());
                continue;
            }
            if (loaded.containsKey(data.id())) {
                errors.add(source.path() + ": duplicate loaded mutation id rejected: " + data.id());
                continue;
            }
            ContentValidationResult validation = ContentValidator.validateMutationDefinition(data, knownSpeciesIds);
            if (!validation.isValid()) {
                errors.add(source.path() + ": " + validation.combinedMessage());
                continue;
            }
            convert(source, () -> loaded.put(data.id(), ContentConverter.toMutationDefinition(data, speciesAlleles)), errors);
        }
        return loaded;
    }

    private static Map<String, ProductionDefinition> loadProduction(
            ContentRegistry builtIns,
            Collection<ContentDefinitionSource> sources,
            Map<String, BeeSpeciesDefinition> loadedSpecies,
            List<String> errors) {
        Map<String, ProductionDefinition> loaded = new LinkedHashMap<>();
        Set<String> knownSpeciesIds = speciesAlleleMap(builtIns, loadedSpecies).keySet();

        for (ContentDefinitionSource source : sources) {
            ProductionDefinitionData data = parse(source, ContentDataJsonParser::parseProduction, errors);
            if (data == null) {
                continue;
            }
            if (builtIns.findProduction(data.speciesId()).isPresent()) {
                errors.add(source.path() + ": duplicate built-in production species id rejected: " + data.speciesId());
                continue;
            }
            if (loaded.containsKey(data.speciesId())) {
                errors.add(source.path() + ": duplicate loaded production species id rejected: " + data.speciesId());
                continue;
            }
            ContentValidationResult validation = ContentValidator.validateProductionDefinition(data, knownSpeciesIds);
            if (!validation.isValid()) {
                errors.add(source.path() + ": " + validation.combinedMessage());
                continue;
            }
            convert(source, () -> loaded.put(data.speciesId(), ContentConverter.toProductionDefinition(data)), errors);
        }
        return loaded;
    }

    private static ContentRegistry mergeLoadedDefinitions(
            ContentRegistry builtIns,
            Collection<Allele> traits,
            Collection<BeeSpeciesDefinition> species,
            Collection<MutationDefinition> mutations,
            Collection<ProductionDefinition> production,
            List<String> errors) {
        try {
            return builtIns.withLoadedDefinitions(traits, species, mutations, production);
        } catch (IllegalArgumentException e) {
            errors.add("registry merge failed: " + e.getMessage());
            return builtIns;
        }
    }

    private static Map<String, Allele> traitAlleleMap(ContentRegistry builtIns, Map<String, Allele> loadedTraits) {
        Map<String, Allele> known = new LinkedHashMap<>();
        for (Allele allele : builtIns.allTraitAlleles()) {
            known.put(allele.id(), allele);
        }
        known.putAll(loadedTraits);
        return known;
    }

    private static Map<String, Allele> speciesAlleleMap(
            ContentRegistry builtIns,
            Map<String, BeeSpeciesDefinition> loadedSpecies) {
        Map<String, Allele> known = new LinkedHashMap<>();
        for (BeeSpeciesDefinition species : builtIns.allSpecies()) {
            known.put(species.id(), species.speciesAllele());
        }
        for (BeeSpeciesDefinition species : loadedSpecies.values()) {
            known.put(species.id(), species.speciesAllele());
        }
        return known;
    }

    private static Set<String> idsOfMutations(List<MutationDefinition> mutations) {
        Set<String> ids = new LinkedHashSet<>();
        for (MutationDefinition mutation : mutations) {
            ids.add(mutation.id());
        }
        return ids;
    }

    private static <T> T parse(
            ContentDefinitionSource source,
            Parser<T> parser,
            List<String> errors) {
        try {
            return parser.parse(source.json());
        } catch (ContentJsonParseException | IllegalArgumentException e) {
            errors.add(source.path() + ": " + e.getMessage());
            return null;
        }
    }

    private static void convert(ContentDefinitionSource source, Runnable converter, List<String> errors) {
        try {
            converter.run();
        } catch (ContentConversionException | IllegalArgumentException e) {
            errors.add(source.path() + ": " + e.getMessage());
        }
    }

    private static String formatErrors(List<String> errors) {
        StringBuilder message = new StringBuilder();
        for (String error : errors) {
            message.append("  - ").append(error).append('\n');
        }
        return message.toString().stripTrailing();
    }

    @FunctionalInterface
    private interface Parser<T> {
        T parse(String json);
    }
}
