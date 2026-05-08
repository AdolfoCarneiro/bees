package com.curiousbees.neoforge.lang;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies that player-visible lang values do not contain forbidden strings.
 *
 * <p>Internal block/item IDs may use "apiary" in their KEY names — only VALUES
 * of player-facing key prefixes (block.*, item.*, screen.*, gui.*) are checked.
 *
 * <p>ADR-0018: all player-facing hive names use "Advanced Beehive" /
 * "Beehive Expansion Box". "Apiary" and "Genetic Apiary" must not appear
 * in any player-visible string.
 */
class LangValueNamingTest {

    /** Key prefixes considered player-visible. */
    private static final List<String> PLAYER_FACING_PREFIXES = List.of(
            "block.curiousbees.",
            "item.curiousbees.",
            "screen.curiousbees.",
            "gui.curiousbees.",
            "itemGroup.curiousbees.",
            "subtitles."
    );

    /** Forbidden substrings in player-visible values (case-insensitive). */
    private static final List<String> FORBIDDEN_VALUE_SUBSTRINGS = List.of(
            "genetic apiary",
            "apiary"
    );

    @Test
    void playerFacingLangValuesDoNotContainForbiddenApiary() throws IOException {
        String json = readLangFile();

        // Matches: "some.key": "some value"
        Pattern entryPattern = Pattern.compile("\"([^\"]+)\"\\s*:\\s*\"([^\"]*)\"");
        Matcher matcher = entryPattern.matcher(json);

        List<String> violations = new ArrayList<>();

        while (matcher.find()) {
            String key = matcher.group(1);
            String value = matcher.group(2);

            boolean isPlayerFacing = PLAYER_FACING_PREFIXES.stream()
                    .anyMatch(key::startsWith);

            if (!isPlayerFacing) {
                continue;
            }

            String valueLower = value.toLowerCase();
            for (String forbidden : FORBIDDEN_VALUE_SUBSTRINGS) {
                if (valueLower.contains(forbidden.toLowerCase())) {
                    violations.add(
                            String.format("Key \"%s\" has value \"%s\" containing \"%s\"",
                                    key, value, forbidden));
                    break;
                }
            }
        }

        assertTrue(violations.isEmpty(),
                "Player-facing lang values must not contain 'Apiary' or 'Genetic Apiary' (ADR-0018).\n"
                + "Violations:\n  " + String.join("\n  ", violations));
    }

    private static String readLangFile() throws IOException {
        // Try relative paths from different CWD contexts (common module vs. repo root).
        for (String rel : List.of(
                "../neoforge/src/main/resources/assets/curiousbees/lang/en_us.json",
                "neoforge/src/main/resources/assets/curiousbees/lang/en_us.json")) {
            Path p = Path.of(rel);
            if (Files.exists(p)) {
                return Files.readString(p);
            }
        }
        throw new AssertionError(
                "Cannot find en_us.json — tried relative paths from common/ and repo root.");
    }
}
