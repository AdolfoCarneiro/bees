package com.curiousbees.common.content.recipe;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Verifies that every shipped centrifuge recipe JSON is structurally valid.
 * Pure Java — no Minecraft registry required.
 */
class CentrifugeRecipeDataTest {

    private static final String RECIPE_DIR = "data/curiousbees/recipe/centrifuge";

    private static final int EXPECTED_RECIPE_COUNT = 5;

    private static final List<String> EXPECTED_COMB_ITEMS = List.of(
            "curiousbees:meadow_comb",
            "curiousbees:forest_comb",
            "curiousbees:arid_comb",
            "curiousbees:cultivated_comb",
            "curiousbees:hardy_comb"
    );

    @Test
    void allCombRecipesExist() throws IOException {
        List<Path> files = findRecipeFiles();
        assertEquals(EXPECTED_RECIPE_COUNT, files.size(),
                "Expected " + EXPECTED_RECIPE_COUNT + " centrifuge recipe files, found " + files.size()
                + " in " + resolveRecipeDir());
    }

    @Test
    void eachRecipeHasRequiredFields() throws IOException {
        List<String> errors = new ArrayList<>();

        for (Path file : findRecipeFiles()) {
            String json = Files.readString(file);
            String name = file.getFileName().toString();

            if (!json.contains("\"curiousbees:centrifuge\"")) {
                errors.add(name + ": missing or wrong \"type\" field (expected \"curiousbees:centrifuge\")");
            }
            if (!json.contains("\"ingredient\"")) {
                errors.add(name + ": missing \"ingredient\" field");
            }
            if (!json.contains("\"item\"")) {
                errors.add(name + ": ingredient missing \"item\" field");
            }
            if (!json.contains("\"outputs\"")) {
                errors.add(name + ": missing \"outputs\" field");
            }
            if (!json.contains("\"result\"")) {
                errors.add(name + ": outputs missing \"result\" field");
            }
            if (!json.contains("\"id\"")) {
                errors.add(name + ": result missing \"id\" field");
            }
        }

        assertTrue(errors.isEmpty(),
                "Centrifuge recipe schema errors:\n  " + String.join("\n  ", errors));
    }

    @Test
    void eachCombHasARecipe() throws IOException {
        // Collect all ingredient item ids from recipe files
        Pattern itemPattern = Pattern.compile("\"item\"\\s*:\\s*\"([^\"]+)\"");
        List<String> foundItems = new ArrayList<>();

        for (Path file : findRecipeFiles()) {
            String json = Files.readString(file);
            var matcher = itemPattern.matcher(json);
            if (matcher.find()) {
                foundItems.add(matcher.group(1));
            }
        }

        List<String> missingCombs = EXPECTED_COMB_ITEMS.stream()
                .filter(comb -> !foundItems.contains(comb))
                .toList();

        assertTrue(missingCombs.isEmpty(),
                "No centrifuge recipe found for combs: " + missingCombs);
    }

    @Test
    void eachRecipeHasAtLeastOneOutput() throws IOException {
        List<String> errors = new ArrayList<>();

        for (Path file : findRecipeFiles()) {
            String json = Files.readString(file);
            // Count "result" occurrences — each output entry has one
            long outputCount = Pattern.compile("\"result\"").matcher(json).results().count();
            if (outputCount < 1) {
                errors.add(file.getFileName().toString() + ": outputs array appears empty");
            }
        }

        assertTrue(errors.isEmpty(),
                "Centrifuge recipes with no outputs:\n  " + String.join("\n  ", errors));
    }

    // --- helpers ---

    private static List<Path> findRecipeFiles() throws IOException {
        Path dir = resolveRecipeDir();
        if (!Files.isDirectory(dir)) {
            fail("Centrifuge recipe directory not found: " + dir.toAbsolutePath());
        }
        try (Stream<Path> stream = Files.list(dir)) {
            return stream.filter(p -> p.toString().endsWith(".json")).sorted().toList();
        }
    }

    private static Path resolveRecipeDir() {
        for (String base : List.of(
                "../neoforge/src/generated/resources/",
                "neoforge/src/generated/resources/",
                "../neoforge/src/main/resources/",
                "neoforge/src/main/resources/")) {
            Path candidate = Path.of(base + RECIPE_DIR);
            if (Files.isDirectory(candidate)) return candidate;
        }
        return Path.of("neoforge/src/generated/resources/" + RECIPE_DIR);
    }
}
