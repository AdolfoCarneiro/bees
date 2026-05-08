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
    void allProductivityAllelesExist() {
        assertNotNull(PRODUCTIVITY_SLOW);
        assertNotNull(PRODUCTIVITY_NORMAL);
        assertNotNull(PRODUCTIVITY_FAST);
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
    void productivityAllelesHaveCorrectChromosomeType() {
        assertEquals(ChromosomeType.PRODUCTIVITY, PRODUCTIVITY_SLOW.chromosomeType());
        assertEquals(ChromosomeType.PRODUCTIVITY, PRODUCTIVITY_NORMAL.chromosomeType());
        assertEquals(ChromosomeType.PRODUCTIVITY, PRODUCTIVITY_FAST.chromosomeType());
    }

    @Test
    void flowerTypeAllelesHaveCorrectChromosomeType() {
        assertEquals(ChromosomeType.FLOWER_TYPE, FLOWER_FLOWERS.chromosomeType());
        assertEquals(ChromosomeType.FLOWER_TYPE, FLOWER_CACTUS.chromosomeType());
        assertEquals(ChromosomeType.FLOWER_TYPE, FLOWER_LEAVES.chromosomeType());
    }

    @Test
    void dominanceMatchesSpec() {
        assertEquals(Dominance.RECESSIVE, PRODUCTIVITY_SLOW.dominance());
        assertEquals(Dominance.DOMINANT,  PRODUCTIVITY_NORMAL.dominance());
        assertEquals(Dominance.RECESSIVE, PRODUCTIVITY_FAST.dominance());

        assertEquals(Dominance.DOMINANT,  FLOWER_FLOWERS.dominance());
        assertEquals(Dominance.RECESSIVE, FLOWER_CACTUS.dominance());
        assertEquals(Dominance.RECESSIVE, FLOWER_LEAVES.dominance());
    }

    @Test
    void allListHasSixEntries() {
        assertEquals(6, ALL.size());
    }
}
