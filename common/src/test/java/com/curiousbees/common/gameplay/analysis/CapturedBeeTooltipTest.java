package com.curiousbees.common.gameplay.analysis;

import com.curiousbees.common.genetics.fixtures.AlleleFixtures;
import com.curiousbees.common.genetics.fixtures.GenomeFixtures;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the pure-Java tooltip / genetic-report layer accessible through
 * BeeAnalysisFormatter. Verifies the contract required by the Curious Bees
 * UI: species name visible, no raw allele IDs, no hidden "analyzed" gate.
 *
 * Note: The Minecraft-level tooltip (CapturedBeeItem#appendHoverText) calls
 * into NeoForge APIs and is tested separately in neoforge/src/test.
 * This class covers only the platform-neutral BeeAnalysisFormatter output.
 */
class CapturedBeeTooltipTest {

    private final BeeAnalysisService service = new BeeAnalysisService();

    // --- Species name appears in formatted output ---

    @Test
    void meadowSpeciesNameAppearsInFormattedReport() {
        BeeAnalysisReport report = service.analyze(GenomeFixtures.pureMeadow());
        List<String> lines = BeeAnalysisFormatter.format(report);
        assertTrue(lines.stream().anyMatch(l -> l.contains("Meadow")),
                "Species 'Meadow' must appear in the formatted report lines");
    }

    @Test
    void forestSpeciesNameAppearsInFormattedReport() {
        BeeAnalysisReport report = service.analyze(GenomeFixtures.pureForest());
        List<String> lines = BeeAnalysisFormatter.format(report);
        assertTrue(lines.stream().anyMatch(l -> l.contains("Forest")),
                "Species 'Forest' must appear in the formatted report lines");
    }

    @Test
    void hybridReportShowsBothSpeciesNames() {
        BeeAnalysisReport report = service.analyze(GenomeFixtures.hybridMeadowForest());
        List<String> lines = BeeAnalysisFormatter.format(report);
        String speciesLine = lines.stream()
                .filter(l -> l.contains("Species"))
                .findFirst()
                .orElseThrow(() -> new AssertionError("No Species line found"));
        assertTrue(speciesLine.contains("Meadow"), "Active species 'Meadow' must appear");
        assertTrue(speciesLine.contains("Forest"), "Inactive species 'Forest' must appear");
    }

    // --- Genetic report does not require any "analyzed" flag to produce output ---

    @Test
    void fullReportProducedDirectlyFromGenomeWithNoAnalyzedFlag() {
        // BeeAnalysisService.analyze() always returns an analyzed report.
        // There must be no additional gate — species name is always visible
        // when a genome is available (the Analyzer is optional, not a gate).
        BeeAnalysisReport report = service.analyze(GenomeFixtures.pureMeadow());
        assertTrue(report.isAnalyzed(),
                "A report produced from a genome is always fully analyzed");
        List<String> lines = BeeAnalysisFormatter.format(report);
        assertFalse(lines.stream().anyMatch(l -> l.contains("Analysis Required")),
                "Genome-sourced report must not show 'Analysis Required' message");
    }

    // --- Raw allele IDs never appear in player-facing output ---

    @Test
    void rawAlleleIdsNeverAppearInMeadowReport() {
        BeeAnalysisReport report = service.analyze(GenomeFixtures.pureMeadow());
        List<String> lines = BeeAnalysisFormatter.format(report);
        for (String line : lines) {
            assertFalse(line.contains(AlleleFixtures.MEADOW.id()),
                    "Raw allele ID leaked into output: " + line);
        }
    }

    @Test
    void rawAlleleIdsNeverAppearInHybridReport() {
        BeeAnalysisReport report = service.analyze(GenomeFixtures.hybridMeadowForest());
        List<String> lines = BeeAnalysisFormatter.format(report);
        // Check that none of the known raw allele IDs from the genome appear verbatim
        List<String> rawIds = List.of(
                AlleleFixtures.MEADOW.id(),
                AlleleFixtures.FOREST.id(),
                AlleleFixtures.PRODUCTIVITY_NORMAL.id(),
                AlleleFixtures.FLOWER_FLOWERS.id()
        );
        for (String line : lines) {
            for (String rawId : rawIds) {
                assertFalse(line.contains(rawId),
                        "Raw allele ID '" + rawId + "' leaked into player-facing line: " + line);
            }
        }
    }

    @Test
    void namespaceColonNeverAppearsInAnyFormattedLine() {
        List<List<String>> allReports = List.of(
                BeeAnalysisFormatter.format(service.analyze(GenomeFixtures.pureMeadow())),
                BeeAnalysisFormatter.format(service.analyze(GenomeFixtures.pureForest())),
                BeeAnalysisFormatter.format(service.analyze(GenomeFixtures.hybridMeadowForest()))
        );
        for (List<String> lines : allReports) {
            for (String line : lines) {
                assertFalse(line.contains("curious_bees:"),
                        "Namespace 'curious_bees:' found in player-facing output: " + line);
            }
        }
    }

    // --- Unknown report: no species leaked ---

    @Test
    void unknownReportDoesNotRevealAnySpeciesName() {
        List<String> lines = BeeAnalysisFormatter.format(BeeAnalysisReport.unknown());
        List<String> knownSpecies = List.of("Meadow", "Forest", "Arid", "Cultivated", "Hardy");
        for (String species : knownSpecies) {
            assertTrue(lines.stream().noneMatch(l -> l.contains(species)),
                    "Unknown report must not leak species name: " + species);
        }
    }

    @Test
    void unknownReportContainsAnalysisRequiredMessage() {
        List<String> lines = BeeAnalysisFormatter.format(BeeAnalysisReport.unknown());
        assertTrue(lines.stream().anyMatch(l -> l.contains("Analysis Required")),
                "Unknown report must show 'Analysis Required' message");
    }
}
