package com.curiousbees.common.content.json;

import com.curiousbees.common.content.builtin.BuiltinContentData;
import com.curiousbees.common.content.data.MutationDefinitionData;
import com.curiousbees.common.content.data.ProductionDefinitionData;
import com.curiousbees.common.content.data.SpeciesDefinitionData;
import com.curiousbees.common.content.data.TraitAlleleDefinitionData;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ContentDataJsonParserTest {

    private static final Set<String> KNOWN_TRAIT_IDS = BuiltinContentData.TRAIT_ALLELES.stream()
            .map(TraitAlleleDefinitionData::id)
            .collect(Collectors.toUnmodifiableSet());

    private static final Set<String> KNOWN_SPECIES_IDS = BuiltinContentData.SPECIES.stream()
            .map(SpeciesDefinitionData::id)
            .collect(Collectors.toUnmodifiableSet());

    @Test
    void parsesValidTraitAlleleJson() {
        String json = """
                {
                  "id": "curious_bees:traits/productivity/fast",
                  "chromosomeType": "PRODUCTIVITY",
                  "displayName": "Fast",
                  "dominance": "RECESSIVE",
                  "values": {
                    "multiplier": 1.25
                  }
                }
                """;

        TraitAlleleDefinitionData data = ContentDataJsonParser.parseValidatedTraitAllele(json);

        assertEquals("curious_bees:traits/productivity/fast", data.id());
        assertEquals("PRODUCTIVITY", data.chromosomeType());
        assertEquals(1.25, data.values().get("multiplier"));
    }

    @Test
    void parsesValidSpeciesJsonWithStringAndPairTraitForms() {
        String json = """
                {
                  "id": "curious_bees:species/meadow",
                  "displayName": "Meadow Bee",
                  "dominance": "DOMINANT",
                  "defaultTraits": {
                    "LIFESPAN": "curious_bees:traits/lifespan/normal",
                    "PRODUCTIVITY": [
                      "curious_bees:traits/productivity/normal",
                      "curious_bees:traits/productivity/normal"
                    ],
                    "FERTILITY": {
                      "first": "curious_bees:traits/fertility/two",
                      "second": "curious_bees:traits/fertility/two"
                    },
                    "FLOWER_TYPE": {
                      "alleleA": "curious_bees:traits/flower_type/flowers",
                      "alleleB": "curious_bees:traits/flower_type/flowers"
                    }
                  },
                  "spawnContextNotes": ["plains", "meadow"]
                }
                """;

        SpeciesDefinitionData data = ContentDataJsonParser.parseValidatedSpecies(json, KNOWN_TRAIT_IDS);

        assertEquals("curious_bees:species/meadow", data.id());
        assertEquals("curious_bees:traits/lifespan/normal",
                data.defaultTraits().get("LIFESPAN").first());
        assertEquals("curious_bees:traits/lifespan/normal",
                data.defaultTraits().get("LIFESPAN").second());
        assertEquals(2, data.spawnContextNotes().size());
    }

    @Test
    void parsesValidMutationJson() {
        String json = """
                {
                  "id": "curious_bees:mutations/cultivated_from_meadow_forest",
                  "parents": [
                    "curious_bees:species/meadow",
                    "curious_bees:species/forest"
                  ],
                  "result": "curious_bees:species/cultivated",
                  "baseChance": 0.12,
                  "resultModes": {
                    "partialChance": 0.95,
                    "fullChance": 0.05
                  }
                }
                """;

        MutationDefinitionData data = ContentDataJsonParser.parseValidatedMutation(json, KNOWN_SPECIES_IDS);

        assertEquals("curious_bees:species/meadow", data.parentSpeciesAId());
        assertEquals("curious_bees:species/forest", data.parentSpeciesBId());
        assertEquals("curious_bees:species/cultivated", data.resultSpeciesId());
        assertEquals(0.12, data.baseChance());
    }

    @Test
    void parsesValidProductionJson() {
        String json = """
                {
                  "species": "curious_bees:species/cultivated",
                  "primaryOutputs": [
                    {
                      "item": "curiousbees:cultivated_comb",
                      "chance": 0.9,
                      "min": 1,
                      "max": 1
                    }
                  ],
                  "secondaryOutputs": [
                    {
                      "item": "minecraft:honeycomb",
                      "chance": 0.3
                    }
                  ]
                }
                """;

        ProductionDefinitionData data = ContentDataJsonParser.parseValidatedProduction(json, KNOWN_SPECIES_IDS);

        assertEquals("curious_bees:species/cultivated", data.speciesId());
        assertEquals("curiousbees:cultivated_comb", data.primaryOutputs().get(0).item());
        assertEquals(1, data.secondaryOutputs().size());
    }

    @Test
    void invalidJsonFailsClearly() {
        ContentJsonParseException exception = assertThrows(ContentJsonParseException.class,
                () -> ContentDataJsonParser.parseTraitAllele("{ \"id\": "));

        org.junit.jupiter.api.Assertions.assertTrue(exception.getMessage().contains("position"));
    }

    @Test
    void invalidMutationChanceFailsValidation() {
        String json = """
                {
                  "id": "curious_bees:mutations/bad",
                  "parents": [
                    "curious_bees:species/meadow",
                    "curious_bees:species/forest"
                  ],
                  "result": "curious_bees:species/cultivated",
                  "baseChance": 1.5,
                  "resultModes": {
                    "partialChance": 0.95,
                    "fullChance": 0.05
                  }
                }
                """;

        ContentJsonParseException exception = assertThrows(ContentJsonParseException.class,
                () -> ContentDataJsonParser.parseValidatedMutation(json, KNOWN_SPECIES_IDS));

        org.junit.jupiter.api.Assertions.assertTrue(exception.getMessage().contains("baseChance"));
    }

    @Test
    void productionMissingOutputItemFailsParsing() {
        String json = """
                {
                  "species": "curious_bees:species/cultivated",
                  "primaryOutputs": [
                    {
                      "chance": 0.9
                    }
                  ]
                }
                """;

        ContentJsonParseException exception = assertThrows(ContentJsonParseException.class,
                () -> ContentDataJsonParser.parseValidatedProduction(json, KNOWN_SPECIES_IDS));

        org.junit.jupiter.api.Assertions.assertTrue(exception.getMessage().contains("item"));
    }

    @Test
    void speciesUnknownTraitFailsValidation() {
        String json = """
                {
                  "id": "curious_bees:species/bad",
                  "displayName": "Bad Bee",
                  "dominance": "DOMINANT",
                  "defaultTraits": {
                    "LIFESPAN": "curious_bees:traits/lifespan/normal",
                    "PRODUCTIVITY": "curious_bees:traits/productivity/normal",
                    "FERTILITY": "curious_bees:traits/fertility/two",
                    "FLOWER_TYPE": "curious_bees:traits/flower_type/missing"
                  }
                }
                """;

        ContentJsonParseException exception = assertThrows(ContentJsonParseException.class,
                () -> ContentDataJsonParser.parseValidatedSpecies(json, KNOWN_TRAIT_IDS));

        org.junit.jupiter.api.Assertions.assertTrue(exception.getMessage().contains("unknown trait allele"));
    }
}
