package com.curiousbees.common.genetics.model;

import com.curiousbees.common.genetics.random.DeterministicGeneticRandom;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenePairTest {

    private static final Allele MEADOW = new Allele("curious_bees:species/meadow", ChromosomeType.SPECIES, Dominance.DOMINANT);
    private static final Allele FOREST = new Allele("curious_bees:species/forest", ChromosomeType.SPECIES, Dominance.DOMINANT);
    private static final Allele ARID   = new Allele("curious_bees:species/arid",   ChromosomeType.SPECIES, Dominance.RECESSIVE);
    private static final Allele SLOW   = new Allele("curious_bees:productivity/slow", ChromosomeType.PRODUCTIVITY, Dominance.RECESSIVE);

    private DeterministicGeneticRandom det(boolean... booleans) {
        return new DeterministicGeneticRandom().withBooleans(booleans);
    }

    // --- validation ---

    @Test
    void nullFirstFails() {
        assertThrows(NullPointerException.class,
                () -> new GenePair(null, FOREST, det(true)));
    }

    @Test
    void nullSecondFails() {
        assertThrows(NullPointerException.class,
                () -> new GenePair(MEADOW, null, det(true)));
    }

    @Test
    void nullRandomFails() {
        assertThrows(NullPointerException.class,
                () -> new GenePair(MEADOW, FOREST, null));
    }

    @Test
    void mixedChromosomeTypesFail() {
        assertThrows(IllegalArgumentException.class,
                () -> new GenePair(MEADOW, SLOW, det(true)));
    }

    // --- dominance resolution ---

    @Test
    void dominantFirstBeatsRecessiveSecond() {
        // MEADOW=DOMINANT, ARID=RECESSIVE
        GenePair pair = new GenePair(MEADOW, ARID, det());
        assertEquals(MEADOW, pair.active());
        assertEquals(ARID, pair.inactive());
    }

    @Test
    void recessiveFirstLosesToDominantSecond() {
        // ARID=RECESSIVE, MEADOW=DOMINANT
        GenePair pair = new GenePair(ARID, MEADOW, det());
        assertEquals(MEADOW, pair.active());
        assertEquals(ARID, pair.inactive());
    }

    @Test
    void dominantPlusDominantUsesRandomTrue() {
        // both DOMINANT, random=true -> first is active
        GenePair pair = new GenePair(MEADOW, FOREST, det(true));
        assertEquals(MEADOW, pair.active());
        assertEquals(FOREST, pair.inactive());
    }

    @Test
    void dominantPlusDominantUsesRandomFalse() {
        // both DOMINANT, random=false -> second is active
        GenePair pair = new GenePair(MEADOW, FOREST, det(false));
        assertEquals(FOREST, pair.active());
        assertEquals(MEADOW, pair.inactive());
    }

    @Test
    void recessivePlusRecessiveUsesRandom() {
        Allele recessiveA = new Allele("curious_bees:lifespan/short", ChromosomeType.LIFESPAN, Dominance.RECESSIVE);
        Allele recessiveB = new Allele("curious_bees:lifespan/long",  ChromosomeType.LIFESPAN, Dominance.RECESSIVE);

        GenePair pairA = new GenePair(recessiveA, recessiveB, det(true));
        assertEquals(recessiveA, pairA.active());

        GenePair pairB = new GenePair(recessiveA, recessiveB, det(false));
        assertEquals(recessiveB, pairB.active());
    }

    // --- stability ---

    @Test
    void activeAlleleRemainsStableOverRepeatedReads() {
        GenePair pair = new GenePair(MEADOW, FOREST, det(true));
        Allele first = pair.active();
        for (int i = 0; i < 10; i++) {
            assertSame(first, pair.active());
        }
    }

    // --- purebred / hybrid ---

    @Test
    void sameAlleleIdIsPurebred() {
        // MEADOW+MEADOW are both DOMINANT, so random is consulted for ordering
        GenePair pair = new GenePair(MEADOW, MEADOW, det(true));
        assertTrue(pair.isPurebred());
        assertFalse(pair.isHybrid());
    }

    @Test
    void differentAlleleIdIsHybrid() {
        GenePair pair = new GenePair(MEADOW, FOREST, det(true));
        assertTrue(pair.isHybrid());
        assertFalse(pair.isPurebred());
    }

    // --- query methods ---

    @Test
    void containsAlleleWorks() {
        GenePair pair = new GenePair(MEADOW, FOREST, det(true));
        assertTrue(pair.containsAllele("curious_bees:species/meadow"));
        assertTrue(pair.containsAllele("curious_bees:species/forest"));
        assertFalse(pair.containsAllele("curious_bees:species/arid"));
    }

    @Test
    void hasActiveAlleleWorks() {
        GenePair pair = new GenePair(MEADOW, FOREST, det(true)); // MEADOW active
        assertTrue(pair.hasActiveAllele("curious_bees:species/meadow"));
        assertFalse(pair.hasActiveAllele("curious_bees:species/forest"));
    }

    @Test
    void hasInactiveAlleleWorks() {
        GenePair pair = new GenePair(MEADOW, FOREST, det(true)); // FOREST inactive
        assertTrue(pair.hasInactiveAllele("curious_bees:species/forest"));
        assertFalse(pair.hasInactiveAllele("curious_bees:species/meadow"));
    }

    @Test
    void chromosomeTypeReflectsAlleles() {
        GenePair pair = new GenePair(MEADOW, FOREST, det(true));
        assertEquals(ChromosomeType.SPECIES, pair.chromosomeType());
    }

    @Test
    void firstAndSecondAllelesArePreserved() {
        GenePair pair = new GenePair(MEADOW, ARID, det());
        assertEquals(MEADOW, pair.first());
        assertEquals(ARID, pair.second());
    }
}
