package com.curiousbees.common.content.data;

import com.curiousbees.common.content.builtin.BuiltinContentData;
import com.curiousbees.common.content.conversion.ContentConverter;
import com.curiousbees.common.content.json.ContentDataJsonParser;
import com.curiousbees.common.content.validation.ContentValidator;
import com.curiousbees.common.genetics.model.Allele;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContentExampleFilesTest {

    private static final List<String> TRAIT_FILES = List.of(
            "traits/lifespan/short.json",
            "traits/lifespan/normal.json",
            "traits/lifespan/long.json",
            "traits/productivity/slow.json",
            "traits/productivity/normal.json",
            "traits/productivity/fast.json",
            "traits/fertility/one.json",
            "traits/fertility/two.json",
            "traits/fertility/three.json",
            "traits/flower_type/flowers.json",
            "traits/flower_type/cactus.json",
            "traits/flower_type/leaves.json");

    private static final List<String> SPECIES_FILES = List.of(
            "species/meadow.json",
            "species/forest.json",
            "species/arid.json",
            "species/cultivated.json",
            "species/hardy.json");

    private static final List<String> MUTATION_FILES = List.of(
            "mutations/cultivated_from_meadow_forest.json",
            "mutations/hardy_from_forest_arid.json");

    private static final List<String> PRODUCTION_FILES = List.of(
            "production/meadow.json",
            "production/forest.json",
            "production/arid.json",
            "production/cultivated.json",
            "production/hardy.json");

    @Test
    void mvpExampleFilesValidateAndMirrorBuiltins() {
        List<TraitAlleleDefinitionData> traits = parseAll(TRAIT_FILES, ContentDataJsonParser::parseTraitAllele);
        Set<String> traitIds = ids(traits, TraitAlleleDefinitionData::id);

        List<SpeciesDefinitionData> species = parseAll(SPECIES_FILES, ContentDataJsonParser::parseSpecies);
        Set<String> speciesIds = ids(species, SpeciesDefinitionData::id);

        List<MutationDefinitionData> mutations = parseAll(MUTATION_FILES, ContentDataJsonParser::parseMutation);
        List<ProductionDefinitionData> production = parseAll(PRODUCTION_FILES, ContentDataJsonParser::parseProduction);

        assertTrue(ContentValidator.validateTraitAlleles(traits).isValid());
        assertTrue(ContentValidator.validateSpecies(species, traitIds).isValid());
        assertTrue(ContentValidator.validateMutations(mutations, speciesIds).isValid());
        assertTrue(ContentValidator.validateProduction(production, speciesIds).isValid());

        assertEquals(ids(BuiltinContentData.TRAIT_ALLELES, TraitAlleleDefinitionData::id), traitIds);
        assertEquals(ids(BuiltinContentData.SPECIES, SpeciesDefinitionData::id), speciesIds);
        assertEquals(ids(BuiltinContentData.MUTATIONS, MutationDefinitionData::id),
                ids(mutations, MutationDefinitionData::id));
        assertEquals(ids(BuiltinContentData.PRODUCTION, ProductionDefinitionData::speciesId),
                ids(production, ProductionDefinitionData::speciesId));

        Map<String, Allele> traitAlleles = ContentConverter.toTraitAlleles(traits).stream()
                .collect(Collectors.toMap(Allele::id, Function.identity()));
        assertEquals(5, species.stream()
                .map(data -> ContentConverter.toSpeciesDefinition(data, traitAlleles))
                .count());
    }

    private static <T> List<T> parseAll(List<String> files, Parser<T> parser) {
        return files.stream()
                .map(file -> parser.parse(readExample(file)))
                .toList();
    }

    private static String readExample(String relativePath) {
        Path path = Path.of("src/test/resources/content-examples/curious_bees", relativePath);
        if (!Files.exists(path)) {
            path = Path.of("common/src/test/resources/content-examples/curious_bees", relativePath);
        }
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new AssertionError("Failed to read example file: " + path, e);
        }
    }

    private static <T> Set<String> ids(List<T> values, Function<T, String> idGetter) {
        return values.stream()
                .map(idGetter)
                .collect(Collectors.toUnmodifiableSet());
    }

    @FunctionalInterface
    private interface Parser<T> {
        T parse(String json);
    }
}
