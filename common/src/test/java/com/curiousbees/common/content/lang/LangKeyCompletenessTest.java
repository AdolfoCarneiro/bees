package com.curiousbees.common.content.lang;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies that every required lang key is present in en_us.json.
 * Add new keys here whenever code uses a new Component.translatable() or I18n.get() call.
 */
class LangKeyCompletenessTest {

    private static final Set<String> REQUIRED_KEYS = Set.of(
            // Species display names (used via SpeciesVisualDefinition.displayNameKey())
            "species.curiousbees.meadow",
            "species.curiousbees.forest",
            "species.curiousbees.arid",
            "species.curiousbees.cultivated",
            "species.curiousbees.hardy",
            // Items
            "item.curiousbees.bee_analyzer",
            "item.curiousbees.meadow_bee_spawn_egg",
            "item.curiousbees.forest_bee_spawn_egg",
            "item.curiousbees.arid_bee_spawn_egg",
            "item.curiousbees.cultivated_bee_spawn_egg",
            "item.curiousbees.hardy_bee_spawn_egg",
            "item.curiousbees.meadow_comb",
            "item.curiousbees.forest_comb",
            "item.curiousbees.arid_comb",
            "item.curiousbees.cultivated_comb",
            "item.curiousbees.hardy_comb",
            "item.curiousbees.basic_frame",
            "item.curiousbees.mutation_frame",
            "item.curiousbees.productivity_frame",
            // Analyzer item messages
            "item.curiousbees.bee_analyzer.no_genome",
            "item.curiousbees.bee_analyzer.no_honeycomb",
            "item.curiousbees.bee_analyzer.tooltip.analyzed",
            "item.curiousbees.bee_analyzer.tooltip.unanalyzed",
            "item.curiousbees.bee_analyzer.tooltip.use",
            "item.curiousbees.bee_analyzer.tooltip.cost",
            // Blocks
            "block.curiousbees.genetic_apiary",
            "block.curiousbees.meadow_bee_nest",
            "block.curiousbees.forest_bee_nest",
            "block.curiousbees.arid_bee_nest",
            // Creative tab
            "itemGroup.curiousbees.curious_bees",
            // Apiary GUI
            "gui.curiousbees.genetic_apiary.honey",
            "gui.curiousbees.genetic_apiary.no_bees",
            "gui.curiousbees.genetic_apiary.bees",
            "gui.curiousbees.genetic_apiary.analyzed",
            "gui.curiousbees.genetic_apiary.unanalyzed",
            "gui.curiousbees.genetic_apiary.frames",
            // Analyzer screen
            "screen.curiousbees.bee_analyzer",
            "screen.curiousbees.bee_analyzer.panel_title",
            "screen.curiousbees.bee_analyzer.label.species",
            "screen.curiousbees.bee_analyzer.label.lifespan",
            "screen.curiousbees.bee_analyzer.label.productivity",
            "screen.curiousbees.bee_analyzer.label.fertility",
            "screen.curiousbees.bee_analyzer.label.flower_type",
            "screen.curiousbees.bee_analyzer.purity.purebred",
            "screen.curiousbees.bee_analyzer.purity.hybrid",
            "screen.curiousbees.bee_analyzer.unanalyzed"
    );

    @Test
    void allRequiredLangKeysPresent() throws IOException {
        String json = readLangFile();
        Set<String> presentKeys = extractKeys(json);

        List<String> missing = REQUIRED_KEYS.stream()
                .filter(k -> !presentKeys.contains(k))
                .sorted()
                .collect(Collectors.toList());

        assertTrue(missing.isEmpty(),
                "Missing lang keys in en_us.json — add them before committing:\n  "
                + String.join("\n  ", missing));
    }

    private static Set<String> extractKeys(String json) {
        Pattern pattern = Pattern.compile("\"([^\"]+)\"\\s*:");
        Matcher matcher = pattern.matcher(json);
        Set<String> keys = new java.util.HashSet<>();
        while (matcher.find()) {
            keys.add(matcher.group(1));
        }
        return keys;
    }

    private static String readLangFile() throws IOException {
        Path path = Path.of("../neoforge/src/main/resources/assets/curiousbees/lang/en_us.json");
        if (!Files.exists(path)) {
            path = Path.of("neoforge/src/main/resources/assets/curiousbees/lang/en_us.json");
        }
        if (!Files.exists(path)) {
            throw new AssertionError("Cannot find en_us.json — tried both relative paths.");
        }
        return Files.readString(path);
    }
}
