package com.curiousbees.common.genetics.model;

import com.curiousbees.common.genetics.random.DeterministicGeneticRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GenomeTest {

    private static final Allele MEADOW   = new Allele("curious_bees:species/meadow",      ChromosomeType.SPECIES,      Dominance.DOMINANT);
    private static final Allele FOREST   = new Allele("curious_bees:species/forest",       ChromosomeType.SPECIES,      Dominance.DOMINANT);
    private static final Allele PROD     = new Allele("curious_bees:productivity/normal",  ChromosomeType.PRODUCTIVITY, Dominance.DOMINANT);

    private GenePair speciesPair;
    private GenePair productivityPair;

    @BeforeEach
    void setUp() {
        var det = new DeterministicGeneticRandom().withBooleans(true, true, true, true);
        speciesPair      = new GenePair(MEADOW, MEADOW, det);
        productivityPair = new GenePair(PROD, PROD, det);
    }

    private Map<ChromosomeType, GenePair> minimalMap() {
        EnumMap<ChromosomeType, GenePair> map = new EnumMap<>(ChromosomeType.class);
        map.put(ChromosomeType.SPECIES, speciesPair);
        return map;
    }

    // --- validation ---

    @Test
    void nullMapFails() {
        assertThrows(NullPointerException.class, () -> new Genome(null));
    }

    @Test
    void missingSpeciesFails() {
        EnumMap<ChromosomeType, GenePair> map = new EnumMap<>(ChromosomeType.class);
        map.put(ChromosomeType.PRODUCTIVITY, productivityPair);
        assertThrows(IllegalArgumentException.class, () -> new Genome(map));
    }

    @Test
    void nullGenePairValueFails() {
        EnumMap<ChromosomeType, GenePair> map = new EnumMap<>(ChromosomeType.class);
        map.put(ChromosomeType.SPECIES, speciesPair);
        map.put(ChromosomeType.PRODUCTIVITY, null);
        assertThrows(NullPointerException.class, () -> new Genome(map));
    }

    @Test
    void mismatchedKeyAndGenePairTypeFails() {
        var det = new DeterministicGeneticRandom().withBooleans(true);
        GenePair flowerPair = new GenePair(
                new Allele("curious_bees:flower_type/flowers", ChromosomeType.FLOWER_TYPE, Dominance.DOMINANT),
                new Allele("curious_bees:flower_type/flowers", ChromosomeType.FLOWER_TYPE, Dominance.DOMINANT),
                det);
        EnumMap<ChromosomeType, GenePair> map = new EnumMap<>(ChromosomeType.class);
        map.put(ChromosomeType.SPECIES, speciesPair);
        // intentionally store a FLOWER_TYPE pair under PRODUCTIVITY key
        map.put(ChromosomeType.PRODUCTIVITY, flowerPair);
        assertThrows(IllegalArgumentException.class, () -> new Genome(map));
    }

    @Test
    void validMinimalGenomeSucceeds() {
        assertDoesNotThrow(() -> new Genome(minimalMap()));
    }

    // --- read access ---

    @Test
    void speciesGenePairCanBeRead() {
        Genome genome = new Genome(minimalMap());
        assertSame(speciesPair, genome.species());
        assertSame(speciesPair, genome.getGenePair(ChromosomeType.SPECIES));
    }

    @Test
    void activeAlleleCanBeReadByChromosome() {
        Genome genome = new Genome(minimalMap());
        assertEquals(MEADOW, genome.getActiveAllele(ChromosomeType.SPECIES));
    }

    @Test
    void inactiveAlleleCanBeReadByChromosome() {
        Genome genome = new Genome(minimalMap());
        assertEquals(MEADOW, genome.getInactiveAllele(ChromosomeType.SPECIES));
    }

    @Test
    void getGenePairForMissingChromosomeFails() {
        Genome genome = new Genome(minimalMap());
        assertThrows(IllegalArgumentException.class,
                () -> genome.getGenePair(ChromosomeType.PRODUCTIVITY));
    }

    @Test
    void isPurebredWorksByChromosome() {
        Genome genome = new Genome(minimalMap());
        assertTrue(genome.isPurebred(ChromosomeType.SPECIES));
    }

    @Test
    void isHybridWorksByChromosome() {
        var det = new DeterministicGeneticRandom().withBooleans(true);
        GenePair hybridSpecies = new GenePair(MEADOW, FOREST, det);
        EnumMap<ChromosomeType, GenePair> map = new EnumMap<>(ChromosomeType.class);
        map.put(ChromosomeType.SPECIES, hybridSpecies);
        Genome genome = new Genome(map);
        assertTrue(genome.isHybrid(ChromosomeType.SPECIES));
        assertFalse(genome.isPurebred(ChromosomeType.SPECIES));
    }

    @Test
    void hasChromosomeWorks() {
        Genome genome = new Genome(minimalMap());
        assertTrue(genome.hasChromosome(ChromosomeType.SPECIES));
        assertFalse(genome.hasChromosome(ChromosomeType.PRODUCTIVITY));
    }

    // --- immutability ---

    @Test
    void externalMapMutationDoesNotAffectGenome() {
        EnumMap<ChromosomeType, GenePair> map = new EnumMap<>(ChromosomeType.class);
        map.put(ChromosomeType.SPECIES, speciesPair);
        Genome genome = new Genome(map);
        map.put(ChromosomeType.PRODUCTIVITY, productivityPair);
        assertFalse(genome.hasChromosome(ChromosomeType.PRODUCTIVITY));
    }

    @Test
    void genePairsViewIsUnmodifiable() {
        Genome genome = new Genome(minimalMap());
        assertThrows(UnsupportedOperationException.class,
                () -> genome.genePairs().put(ChromosomeType.PRODUCTIVITY, productivityPair));
    }

    // --- withGenePair ---

    @Test
    void withGenePairReturnsCopyWithChange() {
        Genome original = new Genome(minimalMap());
        Genome updated = original.withGenePair(ChromosomeType.PRODUCTIVITY, productivityPair);
        assertTrue(updated.hasChromosome(ChromosomeType.PRODUCTIVITY));
        assertSame(productivityPair, updated.getGenePair(ChromosomeType.PRODUCTIVITY));
    }

    @Test
    void withGenePairDoesNotMutateOriginal() {
        Genome original = new Genome(minimalMap());
        original.withGenePair(ChromosomeType.PRODUCTIVITY, productivityPair);
        assertFalse(original.hasChromosome(ChromosomeType.PRODUCTIVITY));
    }
}
