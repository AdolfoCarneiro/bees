package com.curiousbees.common.gameplay.analysis;

import com.curiousbees.common.genetics.fixtures.GenomeFixtures;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BeeAnalysisReportRedactionTest {

    private final BeeAnalysisService service = new BeeAnalysisService();

    @Test
    void fullReportIsAnalyzed() {
        BeeAnalysisReport report = service.analyze(GenomeFixtures.pureMeadow());
        assertTrue(report.isAnalyzed());
    }

    @Test
    void unknownReportIsNotAnalyzed() {
        BeeAnalysisReport report = BeeAnalysisReport.unknown();
        assertFalse(report.isAnalyzed());
    }

    @Test
    void unknownReportAlleleIdsAreUnknownSentinel() {
        BeeAnalysisReport report = BeeAnalysisReport.unknown();
        assertEquals(BeeAnalysisReport.UNKNOWN_ID, report.species().activeAlleleId());
        assertEquals(BeeAnalysisReport.UNKNOWN_ID, report.lifespan().activeAlleleId());
        assertEquals(BeeAnalysisReport.UNKNOWN_ID, report.productivity().activeAlleleId());
    }

    @Test
    void unknownReportIsNotPurebred() {
        BeeAnalysisReport report = BeeAnalysisReport.unknown();
        assertFalse(report.isSpeciesPurebred());
    }

    @Test
    void formatterShowsRedactedMessageForUnknownReport() {
        List<String> lines = BeeAnalysisFormatter.format(BeeAnalysisReport.unknown());
        assertTrue(lines.stream().anyMatch(l -> l.contains("Unknown")));
        assertEquals(2, lines.size(), "Unknown report should have title + one redacted line");
    }

    @Test
    void formatterShowsFullDataForAnalyzedReport() {
        BeeAnalysisReport report = service.analyze(GenomeFixtures.pureMeadow());
        List<String> lines = BeeAnalysisFormatter.format(report);
        assertTrue(lines.size() > 2, "Full report should have multiple lines");
        assertFalse(lines.stream().anyMatch(l -> l.contains("Analysis Required")));
    }

    /** Tooltip gate: unanalyzed report must never leak any real species name. */
    @Test
    void unknownReportNeverLeaksSpeciesNames() {
        List<String> lines = BeeAnalysisFormatter.format(BeeAnalysisReport.unknown());
        List<String> knownSpecies = List.of("Meadow", "Forest", "Arid", "Cultivated", "Hardy");
        for (String species : knownSpecies) {
            assertTrue(lines.stream().noneMatch(l -> l.contains(species)),
                    "Unanalyzed report leaked species name: " + species);
        }
    }

    /** Tooltip gate: analyzed report reveals species names. */
    @Test
    void analyzedReportRevealsMeadowSpecies() {
        BeeAnalysisReport report = service.analyze(GenomeFixtures.pureMeadow());
        List<String> lines = BeeAnalysisFormatter.format(report);
        assertTrue(lines.stream().anyMatch(l -> l.contains("Meadow")),
                "Analyzed meadow report should contain 'Meadow'");
    }
}
