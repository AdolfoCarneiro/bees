package com.curiousbees.common.genetics.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlleleTest {

    @Test
    void validAllelConstructionSucceeds() {
        Allele allele = new Allele("curious_bees:species/meadow", ChromosomeType.SPECIES, Dominance.DOMINANT);
        assertEquals("curious_bees:species/meadow", allele.id());
        assertEquals(ChromosomeType.SPECIES, allele.chromosomeType());
        assertEquals(Dominance.DOMINANT, allele.dominance());
    }

    @Test
    void nullIdFails() {
        assertThrows(NullPointerException.class,
                () -> new Allele(null, ChromosomeType.SPECIES, Dominance.DOMINANT));
    }

    @Test
    void blankIdFails() {
        assertThrows(IllegalArgumentException.class,
                () -> new Allele("   ", ChromosomeType.SPECIES, Dominance.DOMINANT));
    }

    @Test
    void emptyIdFails() {
        assertThrows(IllegalArgumentException.class,
                () -> new Allele("", ChromosomeType.SPECIES, Dominance.DOMINANT));
    }

    @Test
    void nullChromosomeTypeFails() {
        assertThrows(NullPointerException.class,
                () -> new Allele("curious_bees:species/meadow", null, Dominance.DOMINANT));
    }

    @Test
    void nullDominanceFails() {
        assertThrows(NullPointerException.class,
                () -> new Allele("curious_bees:species/meadow", ChromosomeType.SPECIES, null));
    }

    @Test
    void alleleExposesExpectedId() {
        Allele allele = new Allele("curious_bees:productivity/fast", ChromosomeType.PRODUCTIVITY, Dominance.RECESSIVE);
        assertEquals("curious_bees:productivity/fast", allele.id());
    }

    @Test
    void alleleExposesExpectedChromosomeType() {
        Allele allele = new Allele("curious_bees:fertility/two", ChromosomeType.FERTILITY, Dominance.DOMINANT);
        assertEquals(ChromosomeType.FERTILITY, allele.chromosomeType());
    }

    @Test
    void alleleExposesExpectedDominance() {
        Allele allele = new Allele("curious_bees:flower_type/flowers", ChromosomeType.FLOWER_TYPE, Dominance.RECESSIVE);
        assertEquals(Dominance.RECESSIVE, allele.dominance());
    }

    @Test
    void equalAllelesAreEqual() {
        Allele a = new Allele("curious_bees:species/forest", ChromosomeType.SPECIES, Dominance.DOMINANT);
        Allele b = new Allele("curious_bees:species/forest", ChromosomeType.SPECIES, Dominance.DOMINANT);
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void differentIdAllelesAreNotEqual() {
        Allele a = new Allele("curious_bees:species/meadow", ChromosomeType.SPECIES, Dominance.DOMINANT);
        Allele b = new Allele("curious_bees:species/forest", ChromosomeType.SPECIES, Dominance.DOMINANT);
        assertNotEquals(a, b);
    }
}
