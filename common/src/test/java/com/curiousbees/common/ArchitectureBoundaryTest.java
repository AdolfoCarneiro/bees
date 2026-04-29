package com.curiousbees.common;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Verifies that the common module contains no platform-specific imports.
 * Complements the compile-time guarantee (common/build.gradle has no MC/NeoForge deps)
 * with an explicit source-level check that produces readable failure messages.
 */
class ArchitectureBoundaryTest {

    private static final List<String> FORBIDDEN_PREFIXES = List.of(
            "import net.minecraft.",
            "import net.neoforged.",
            "import net.fabricmc.",
            "import com.mojang."
    );

    @Test
    void commonModuleHasNoPlatformImports() throws IOException {
        Path sourceRoot = Path.of("src", "main", "java");
        assertTrue(Files.exists(sourceRoot),
                "Source root not found at " + sourceRoot.toAbsolutePath()
                + " — run this test via ./gradlew :common:test");

        List<String> violations = new ArrayList<>();

        try (Stream<Path> files = Files.walk(sourceRoot)) {
            files.filter(p -> p.toString().endsWith(".java"))
                 .forEach(file -> checkFile(file, violations));
        }

        assertTrue(violations.isEmpty(),
                "Platform imports found in common module:\n" + String.join("\n", violations));
    }

    private void checkFile(Path file, List<String> violations) {
        try {
            List<String> lines = Files.readAllLines(file);
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).stripLeading();
                for (String forbidden : FORBIDDEN_PREFIXES) {
                    if (line.startsWith(forbidden)) {
                        violations.add(file + ":" + (i + 1) + " — " + line.strip());
                    }
                }
            }
        } catch (IOException e) {
            violations.add(file + " — could not read file: " + e.getMessage());
        }
    }
}
