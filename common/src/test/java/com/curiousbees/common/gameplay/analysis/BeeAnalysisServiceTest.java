package com.curiousbees.common.gameplay.analysis;

import com.curiousbees.common.genetics.fixtures.AlleleFixtures;
import com.curiousbees.common.genetics.fixtures.GenomeFixtures;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Genome;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BeeAnalysisServiceTest {

    private BeeAnalysisService service;

    @BeforeEach
    void setUp() {
        service = new BeeAnalysisService();
    }

    @Test
    void pureMeadowProducesPurebredSpeciesReport() {
        BeeAnalysisReport report = service.analyze(GenomeFixtures.pureMeadow());
        assertTrue(report.species().isPurebred());
        assertEquals(AlleleFixtures.MEADOW.id(), report.species().activeAlleleId());
        assertEquals(AlleleFixtures.MEADOW.id(), report.species().inactiveAlleleId());
        assertTrue(report.isSpeciesPurebred());
    }

    @Test
    void hybridMeadowForestProducesHybridSpeciesReport() {
        BeeAnalysisReport report = service.analyze(GenomeFixtures.hybridMeadowForest());
        assertFalse(report.species().isPurebred());
        assertFalse(report.isSpeciesPurebred());
        // active is MEADOW, inactive is FOREST (fixtures always put active first)
        assertEquals(AlleleFixtures.MEADOW.id(), report.species().activeAlleleId());
        assertEquals(AlleleFixtures.FOREST.id(), report.species().inactiveAlleleId());
    }

    @Test
    void allMvpChromosomesArePresentInReport() {
        BeeAnalysisReport report = service.analyze(GenomeFixtures.pureMeadow());
        assertEquals(ChromosomeType.SPECIES,      report.species().chromosomeType());
        assertEquals(ChromosomeType.LIFESPAN,     report.lifespan().chromosomeType());
        assertEquals(ChromosomeType.PRODUCTIVITY, report.productivity().chromosomeType());
        assertEquals(ChromosomeType.FERTILITY,    report.fertility().chromosomeType());
        assertEquals(ChromosomeType.FLOWER_TYPE,  report.flowerType().chromosomeType());
    }

    @Test
    void activeAndInactiveAllelesArePreserved() {
        BeeAnalysisReport report = service.analyze(GenomeFixtures.hybridMeadowForest());
        assertNotEquals(report.species().activeAlleleId(), report.species().inactiveAlleleId());
    }

    @Test
    void serviceDoesNotMutateGenome() {
        Genome genome = GenomeFixtures.pureMeadow();
        String originalSpecies = genome.getActiveAllele(ChromosomeType.SPECIES).id();
        service.analyze(genome);
        assertEquals(originalSpecies, genome.getActiveAllele(ChromosomeType.SPECIES).id());
    }

    @Test
    void rejectsNullGenome() {
        assertThrows(NullPointerException.class, () -> service.analyze(null));
    }
}
