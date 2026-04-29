package com.curiousbees.common.gameplay.analysis;

import com.curiousbees.common.genetics.fixtures.GenomeFixtures;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BeeAnalysisFormatterTest {

    private BeeAnalysisService service;

    @BeforeEach
    void setUp() {
        service = new BeeAnalysisService();
    }

    private List<String> formatPureMeadow() {
        return BeeAnalysisFormatter.format(service.analyze(GenomeFixtures.pureMeadow()));
    }

    private List<String> formatHybridMeadowForest() {
        return BeeAnalysisFormatter.format(service.analyze(GenomeFixtures.hybridMeadowForest()));
    }

    @Test
    void outputIncludesTitle() {
        List<String> lines = formatPureMeadow();
        assertTrue(lines.stream().anyMatch(l -> l.contains("Bee Genetics")));
    }

    @Test
    void outputIncludesSpeciesLine() {
        List<String> lines = formatPureMeadow();
        assertTrue(lines.stream().anyMatch(l -> l.contains("Species")));
    }

    @Test
    void outputIncludesPurebredForPureSpecies() {
        List<String> lines = formatPureMeadow();
        assertTrue(lines.stream().anyMatch(l -> l.contains("Purebred")));
    }

    @Test
    void outputIncludesHybridForMixedSpecies() {
        List<String> lines = formatHybridMeadowForest();
        assertTrue(lines.stream().anyMatch(l -> l.contains("Hybrid")));
    }

    @Test
    void outputIncludesAllMvpTraits() {
        List<String> lines = formatPureMeadow();
        assertTrue(lines.stream().anyMatch(l -> l.contains("Lifespan")));
        assertTrue(lines.stream().anyMatch(l -> l.contains("Productivity")));
        assertTrue(lines.stream().anyMatch(l -> l.contains("Fertility")));
        assertTrue(lines.stream().anyMatch(l -> l.contains("Flower Type")));
    }

    @Test
    void outputIncludesActiveAndInactiveMarkers() {
        List<String> lines = formatHybridMeadowForest();
        String speciesLine = lines.stream().filter(l -> l.contains("Species")).findFirst().orElse("");
        assertTrue(speciesLine.contains("[A]"));
        assertTrue(speciesLine.contains("[I]"));
    }

    @Test
    void outputIncludesMeadowAndForestForHybrid() {
        List<String> lines = formatHybridMeadowForest();
        String speciesLine = lines.stream().filter(l -> l.contains("Species")).findFirst().orElse("");
        assertTrue(speciesLine.contains("Meadow"));
        assertTrue(speciesLine.contains("Forest"));
    }

    @Test
    void displayNameExtractsLastSegment() {
        assertEquals("Cultivated", BeeAnalysisFormatter.displayName("curious_bees:species/cultivated"));
        assertEquals("Normal", BeeAnalysisFormatter.displayName("curious_bees:lifespan/normal"));
        assertEquals("Flower type", BeeAnalysisFormatter.displayName("curious_bees:trait/flower_type"));
    }

    @Test
    void rejectsNullReport() {
        assertThrows(NullPointerException.class, () -> BeeAnalysisFormatter.format(null));
    }
}
