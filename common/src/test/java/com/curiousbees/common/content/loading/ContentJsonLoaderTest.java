package com.curiousbees.common.content.loading;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContentJsonLoaderTest {

    @Test
    void builtInsOnlyKeepsFallbackContent() {
        ContentLoadResult result = ContentJsonLoader.load(List.of(), List.of(), List.of(), List.of());

        assertFalse(result.hasErrors());
        assertEquals(5, result.registry().allSpecies().size());
        assertEquals(12, result.registry().allTraitAlleles().size());
        assertTrue(result.registry().findSpecies("curious_bees:species/meadow").isPresent());
    }

    @Test
    void validLoadedContentExtendsBuiltInRegistry() {
        ContentLoadResult result = ContentJsonLoader.load(
                List.of(trait("data/test/curious_bees/traits/color/amber.json", """
                        {
                          "id": "test:traits/flower_type/amber",
                          "chromosomeType": "FLOWER_TYPE",
                          "displayName": "Amber",
                          "dominance": "RECESSIVE"
                        }
                        """)),
                List.of(species("data/test/curious_bees/species/test.json", """
                        {
                          "id": "test:species/test",
                          "displayName": "Test Bee",
                          "dominance": "RECESSIVE",
                          "defaultTraits": {
                            "LIFESPAN": "curious_bees:traits/lifespan/normal",
                            "PRODUCTIVITY": "curious_bees:traits/productivity/normal",
                            "FERTILITY": "curious_bees:traits/fertility/two",
                            "FLOWER_TYPE": ["test:traits/flower_type/amber", "curious_bees:traits/flower_type/flowers"]
                          }
                        }
                        """)),
                List.of(mutation("data/test/curious_bees/mutations/test_from_meadow_forest.json", """
                        {
                          "id": "test:mutations/test_from_meadow_forest",
                          "parents": [
                            "curious_bees:species/meadow",
                            "curious_bees:species/forest"
                          ],
                          "result": "test:species/test",
                          "baseChance": 0.25,
                          "resultModes": {
                            "partialChance": 1.0,
                            "fullChance": 0.0
                          }
                        }
                        """)),
                List.of(production("data/test/curious_bees/production/test.json", """
                        {
                          "species": "test:species/test",
                          "primaryOutputs": [
                            {
                              "item": "test:test_comb",
                              "chance": 0.5
                            }
                          ]
                        }
                        """)));

        assertFalse(result.hasErrors(), result::combinedErrorMessage);
        assertEquals(6, result.registry().allSpecies().size());
        assertEquals(13, result.registry().allTraitAlleles().size());
        assertTrue(result.registry().findSpecies("test:species/test").isPresent());
        assertTrue(result.registry().findMutation("test:mutations/test_from_meadow_forest").isPresent());
        assertTrue(result.registry().findProduction("test:species/test").isPresent());
    }

    @Test
    void invalidLoadedContentReportsErrorsAndKeepsBuiltIns() {
        ContentLoadResult result = ContentJsonLoader.load(
                List.of(),
                List.of(species("data/test/curious_bees/species/bad.json", """
                        {
                          "id": "test:species/bad",
                          "displayName": "Bad Bee",
                          "dominance": "DOMINANT",
                          "defaultTraits": {
                            "LIFESPAN": "curious_bees:traits/lifespan/normal",
                            "PRODUCTIVITY": "curious_bees:traits/productivity/normal",
                            "FERTILITY": "curious_bees:traits/fertility/two",
                            "FLOWER_TYPE": "test:traits/flower_type/missing"
                          }
                        }
                        """)),
                List.of(),
                List.of());

        assertTrue(result.hasErrors());
        assertTrue(result.combinedErrorMessage().contains("bad.json"));
        assertTrue(result.combinedErrorMessage().contains("unknown trait allele"));
        assertEquals(5, result.registry().allSpecies().size());
        assertFalse(result.registry().findSpecies("test:species/bad").isPresent());
    }

    @Test
    void loadedDefinitionsCannotOverrideBuiltIns() {
        ContentLoadResult result = ContentJsonLoader.load(
                List.of(),
                List.of(species("data/test/curious_bees/species/meadow.json", """
                        {
                          "id": "curious_bees:species/meadow",
                          "displayName": "Fake Meadow",
                          "dominance": "DOMINANT",
                          "defaultTraits": {
                            "LIFESPAN": "curious_bees:traits/lifespan/normal",
                            "PRODUCTIVITY": "curious_bees:traits/productivity/normal",
                            "FERTILITY": "curious_bees:traits/fertility/two",
                            "FLOWER_TYPE": "curious_bees:traits/flower_type/flowers"
                          }
                        }
                        """)),
                List.of(),
                List.of());

        assertTrue(result.hasErrors());
        assertTrue(result.combinedErrorMessage().contains("duplicate built-in species id rejected"));
        assertEquals("Meadow Bee", result.registry().getSpecies("curious_bees:species/meadow").displayName());
    }

    private static ContentDefinitionSource trait(String path, String json) {
        return new ContentDefinitionSource(path, json);
    }

    private static ContentDefinitionSource species(String path, String json) {
        return new ContentDefinitionSource(path, json);
    }

    private static ContentDefinitionSource mutation(String path, String json) {
        return new ContentDefinitionSource(path, json);
    }

    private static ContentDefinitionSource production(String path, String json) {
        return new ContentDefinitionSource(path, json);
    }
}
