package com.curiousbees.common.content.builtin;

import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Dominance;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static com.curiousbees.common.content.builtin.BuiltinBeeTraits.*;
import static org.junit.jupiter.api.Assertions.*;

class BuiltinBeeTraitsTest {

    @Test
    void allLifespanAllelesExist() {
        assertNotNull(LIFESPAN_SHORT);
        assertNotNull(LIFESPAN_NORMAL);
        assertNotNull(LIFESPAN_LONG);
    }

    @Test
    void allProductivityAllelesExist() {
        assertNotNull(PRODUCTIVITY_SLOW);
        assertNotNull(PRODUCTIVITY_NORMAL);
        assertNotNull(PRODUCTIVITY_FAST);
    }

    @Test
    void allFertilityAllelesExist() {
        assertNotNull(FERTILITY_ONE);
        assertNotNull(FERTILITY_TWO);
        assertNotNull(FERTILITY_THREE);
    }

    @Test
    void allFlowerTypeAllelesExist() {
        assertNotNull(FLOWER_FLOWERS);
        assertNotNull(FLOWER_CACTUS);
        assertNotNull(FLOWER_LEAVES);
    }

    @Test
    void allTraitIdsAreUnique() {
        Set<String> ids = new HashSet<>();
        for (var allele : ALL) {
            assertTrue(ids.add(allele.id()), "Duplicate trait id: " + allele.id());
        }
    }

    @Test
    void lifespanAllelesHaveCorrectChromosomeType() {
        assertEquals(ChromosomeType.LIFESPAN, LIFESPAN_SHORT.chromosomeType());
        assertEquals(ChromosomeType.LIFESPAN, LIFESPAN_NORMAL.chromosomeType());
        assertEquals(ChromosomeType.LIFESPAN, LIFESPAN_LONG.chromosomeType());
    }

    @Test
    void productivityAllelesHaveCorrectChromosomeType() {
        assertEquals(ChromosomeType.PRODUCTIVITY, PRODUCTIVITY_SLOW.chromosomeType());
        assertEquals(ChromosomeType.PRODUCTIVITY, PRODUCTIVITY_NORMAL.chromosomeType());
        assertEquals(ChromosomeType.PRODUCTIVITY, PRODUCTIVITY_FAST.chromosomeType());
    }

    @Test
    void fertilityAllelesHaveCorrectChromosomeType() {
        assertEquals(ChromosomeType.FERTILITY, FERTILITY_ONE.chromosomeType());
        assertEquals(ChromosomeType.FERTILITY, FERTILITY_TWO.chromosomeType());
        assertEquals(ChromosomeType.FERTILITY, FERTILITY_THREE.chromosomeType());
    }

    @Test
    void flowerTypeAllelesHaveCorrectChromosomeType() {
        assertEquals(ChromosomeType.FLOWER_TYPE, FLOWER_FLOWERS.chromosomeType());
        assertEquals(ChromosomeType.FLOWER_TYPE, FLOWER_CACTUS.chromosomeType());
        assertEquals(ChromosomeType.FLOWER_TYPE, FLOWER_LEAVES.chromosomeType());
    }

    @Test
    void dominanceMatchesSpec() {
        assertEquals(Dominance.RECESSIVE, LIFESPAN_SHORT.dominance());
        assertEquals(Dominance.DOMINANT,  LIFESPAN_NORMAL.dominance());
        assertEquals(Dominance.RECESSIVE, LIFESPAN_LONG.dominance());

        assertEquals(Dominance.RECESSIVE, PRODUCTIVITY_SLOW.dominance());
        assertEquals(Dominance.DOMINANT,  PRODUCTIVITY_NORMAL.dominance());
        assertEquals(Dominance.RECESSIVE, PRODUCTIVITY_FAST.dominance());

        assertEquals(Dominance.RECESSIVE, FERTILITY_ONE.dominance());
        assertEquals(Dominance.DOMINANT,  FERTILITY_TWO.dominance());
        assertEquals(Dominance.RECESSIVE, FERTILITY_THREE.dominance());

        assertEquals(Dominance.DOMINANT,  FLOWER_FLOWERS.dominance());
        assertEquals(Dominance.RECESSIVE, FLOWER_CACTUS.dominance());
        assertEquals(Dominance.RECESSIVE, FLOWER_LEAVES.dominance());
    }

    @Test
    void allListHasTwelveEntries() {
        assertEquals(12, ALL.size());
    }
}
