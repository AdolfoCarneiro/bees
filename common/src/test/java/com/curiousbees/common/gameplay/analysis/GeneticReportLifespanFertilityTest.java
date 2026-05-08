package com.curiousbees.common.gameplay.analysis;

import com.curiousbees.common.genetics.fixtures.AlleleFixtures;
import com.curiousbees.common.genetics.fixtures.GenomeFixtures;
import com.curiousbees.common.genetics.model.ChromosomeType;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies that LIFESPAN and FERTILITY are present in BeeAnalysisReport and
 * formatted correctly — they must never be absent or show raw allele IDs.
 *
 * Related requirement: genetic data visible in Curious Bees UIs;
 * raw internal allele IDs must never appear in player-facing output.
 */
class GeneticReportLifespanFertilityTest {

    private final BeeAnalysisService service = new BeeAnalysisService();

    // --- BeeAnalysisReport contains lifespan and fertility ---

    @Test
    void reportContainsLifespanChromosome() {
        BeeAnalysisReport report = service.analyze(GenomeFixtures.pureMeadow());
        assertNotNull(report.lifespan(), "report.lifespan() must not be null");
        assertEquals(ChromosomeType.LIFESPAN, report.lifespan().chromosomeType());
    }

    @Test
    void reportContainsFertilityChromosome() {
        BeeAnalysisReport report = service.analyze(GenomeFixtures.pureMeadow());
        assertNotNull(report.fertility(), "report.fertility() must not be null");
        assertEquals(ChromosomeType.FERTILITY, report.fertility().chromosomeType());
    }

    @Test
    void lifespanActiveAlleleMatchesFixture() {
        BeeAnalysisReport report = service.analyze(GenomeFixtures.pureMeadow());
        assertEquals(AlleleFixtures.LIFESPAN_NORMAL.id(), report.lifespan().activeAlleleId());
    }

    @Test
    void fertilityActiveAlleleMatchesFixture() {
        BeeAnalysisReport report = service.analyze(GenomeFixtures.pureMeadow());
        assertEquals(AlleleFixtures.FERTILITY_TWO.id(), report.fertility().activeAlleleId());
    }

    // --- Formatter outputs lifespan and fertility as human-readable labels ---

    @Test
    void formattedOutputContainsLifespanLabel() {
        BeeAnalysisReport report = service.analyze(GenomeFixtures.pureMeadow());
        List<String> lines = BeeAnalysisFormatter.format(report);
        assertTrue(lines.stream().anyMatch(l -> l.contains("Lifespan")),
                "Formatted report must contain a Lifespan line");
    }

    @Test
    void formattedOutputContainsFertilityLabel() {
        BeeAnalysisReport report = service.analyze(GenomeFixtures.pureMeadow());
        List<String> lines = BeeAnalysisFormatter.format(report);
        assertTrue(lines.stream().anyMatch(l -> l.contains("Fertility")),
                "Formatted report must contain a Fertility line");
    }

    // --- Raw allele IDs must never appear in formatted output ---

    @Test
    void formattedLifespanLineDoesNotContainRawAlleleId() {
        BeeAnalysisReport report = service.analyze(GenomeFixtures.pureMeadow());
        List<String> lines = BeeAnalysisFormatter.format(report);
        String lifespanLine = lines.stream()
                .filter(l -> l.contains("Lifespan"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No Lifespan line found"));
        // Raw allele IDs contain "curious_bees:" namespace prefix
        assertFalse(lifespanLine.contains("curious_bees:"),
                "Lifespan line must not contain raw allele namespace: " + lifespanLine);
    }

    @Test
    void formattedFertilityLineDoesNotContainRawAlleleId() {
        BeeAnalysisReport report = service.analyze(GenomeFixtures.pureMeadow());
        List<String> lines = BeeAnalysisFormatter.format(report);
        String fertilityLine = lines.stream()
                .filter(l -> l.contains("Fertility"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No Fertility line found"));
        assertFalse(fertilityLine.contains("curious_bees:"),
                "Fertility line must not contain raw allele namespace: " + fertilityLine);
    }

    @Test
    void noLineInFullReportContainsRawAlleleNamespace() {
        BeeAnalysisReport report = service.analyze(GenomeFixtures.hybridMeadowForest());
        List<String> lines = BeeAnalysisFormatter.format(report);
        for (String line : lines) {
            assertFalse(line.contains("curious_bees:"),
                    "Player-facing line contains raw allele ID: " + line);
        }
    }

    // --- Unknown report: lifespan and fertility are redacted (UNKNOWN_ID sentinel) ---

    @Test
    void unknownReportLifespanIsRedacted() {
        BeeAnalysisReport report = BeeAnalysisReport.unknown();
        assertEquals(BeeAnalysisReport.UNKNOWN_ID, report.lifespan().activeAlleleId());
        assertEquals(BeeAnalysisReport.UNKNOWN_ID, report.lifespan().inactiveAlleleId());
    }

    @Test
    void unknownReportFertilityIsRedacted() {
        BeeAnalysisReport report = BeeAnalysisReport.unknown();
        assertEquals(BeeAnalysisReport.UNKNOWN_ID, report.fertility().activeAlleleId());
        assertEquals(BeeAnalysisReport.UNKNOWN_ID, report.fertility().inactiveAlleleId());
    }
}
