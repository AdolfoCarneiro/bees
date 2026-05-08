package com.curiousbees.common.content.loading;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Smoke test for EX-T02: verifies that two consecutive ContentJsonLoader.load() calls
 * (simulating a /reload cycle) produce identical, valid registries with no errors.
 * Pure Java — no Minecraft runtime required.
 */
class DatapackReloadSmokeTest {

    private static final String DATA_ROOT = "neoforge/src/main/resources/data/curiousbees/curious_bees";

    // Species: 5 named MVP + Common fallback (ADR-0019) = 6
    // Traits: PRODUCTIVITY (slow/normal/fast) + FLOWER_TYPE (flowers/cactus/leaves) = 6 (ADR-0017 removed Lifespan/Fertility)
    private static final int EXPECTED_SPECIES    = 6;
    private static final int EXPECTED_TRAITS     = 6;
    private static final int EXPECTED_MUTATIONS  = 2;
    private static final int EXPECTED_PRODUCTION = 5;

    @Test
    void firstLoadProducesValidRegistry() throws IOException {
        ContentLoadResult result = load();

        assertFalse(result.hasErrors(), "First load must have no errors: " + result.combinedErrorMessage());
        assertRegistryCounts(result, "first load");
    }

    @Test
    void secondLoadProducesSameRegistryAsFirst() throws IOException {
        ContentLoadResult first  = load();
        ContentLoadResult second = load();

        assertFalse(second.hasErrors(), "Second load (reload) must have no errors: " + second.combinedErrorMessage());
        assertRegistryCounts(second, "second load");

        assertEquals(first.registry().allSpecies().size(),
                     second.registry().allSpecies().size(),
                     "Species count must be stable across reloads");
        assertEquals(first.registry().allTraitAlleles().size(),
                     second.registry().allTraitAlleles().size(),
                     "Trait allele count must be stable across reloads");
        assertEquals(first.registry().allMutations().size(),
                     second.registry().allMutations().size(),
                     "Mutation count must be stable across reloads");
        assertEquals(first.registry().allProductionDefinitions().size(),
                     second.registry().allProductionDefinitions().size(),
                     "Production definition count must be stable across reloads");
    }

    @Test
    void reloadPreservesAllBuiltInSpeciesIds() throws IOException {
        ContentLoadResult result = load();
        assertFalse(result.hasErrors());

        List<String> expected = List.of(
            "curious_bees:species/meadow",
            "curious_bees:species/forest",
            "curious_bees:species/arid",
            "curious_bees:species/cultivated",
            "curious_bees:species/hardy",
            "curious_bees:species/common"
        );
        for (String id : expected) {
            assertTrue(result.registry().findSpecies(id).isPresent(),
                       "Species '" + id + "' must be present after reload");
        }
    }

    @Test
    void reloadProducesNoErrorsOnEmptySources() {
        // Simulates a reload where no external datapacks are present — only built-ins remain.
        ContentLoadResult result = ContentJsonLoader.load(
                List.of(), List.of(), List.of(), List.of());

        assertFalse(result.hasErrors());
        assertRegistryCounts(result, "empty-sources reload");
    }

    // --- helpers ---

    private static ContentLoadResult load() throws IOException {
        Path root = resolveDataRoot();
        return ContentJsonLoader.load(
                sources(root, "traits"),
                sources(root, "species"),
                sources(root, "mutations"),
                sources(root, "production")
        );
    }

    private static List<ContentDefinitionSource> sources(Path root, String subdir) throws IOException {
        Path dir = root.resolve(subdir);
        if (!Files.isDirectory(dir)) return List.of();
        try (Stream<Path> walk = Files.walk(dir)) {
            return walk
                    .filter(p -> p.toString().endsWith(".json"))
                    .sorted()
                    .map(p -> {
                        try {
                            return new ContentDefinitionSource(p.toString(), Files.readString(p));
                        } catch (IOException e) {
                            throw new RuntimeException("Failed to read " + p, e);
                        }
                    })
                    .toList();
        }
    }

    private static Path resolveDataRoot() {
        Path candidate = Path.of("../neoforge/src/main/resources/data/curiousbees/curious_bees");
        if (Files.isDirectory(candidate)) return candidate;
        return Path.of(DATA_ROOT);
    }

    private static void assertRegistryCounts(ContentLoadResult result, String label) {
        assertEquals(EXPECTED_SPECIES,    result.registry().allSpecies().size(),
                "Expected " + EXPECTED_SPECIES + " species in " + label);
        assertEquals(EXPECTED_TRAITS,     result.registry().allTraitAlleles().size(),
                "Expected " + EXPECTED_TRAITS + " trait alleles in " + label);
        assertEquals(EXPECTED_MUTATIONS,  result.registry().allMutations().size(),
                "Expected " + EXPECTED_MUTATIONS + " mutations in " + label);
        assertEquals(EXPECTED_PRODUCTION, result.registry().allProductionDefinitions().size(),
                "Expected " + EXPECTED_PRODUCTION + " production definitions in " + label);
    }
}
