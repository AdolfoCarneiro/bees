package com.curiousbees.common.content.builtin;

import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Dominance;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static com.curiousbees.common.content.builtin.BuiltinBeeSpecies.*;
import static org.junit.jupiter.api.Assertions.*;

class BuiltinBeeSpeciesTest {

    @Test
    void allFiveMvpSpeciesExist() {
        assertNotNull(MEADOW);
        assertNotNull(FOREST);
        assertNotNull(ARID);
        assertNotNull(CULTIVATED);
        assertNotNull(HARDY);
        assertEquals(5, ALL.size());
    }

    @Test
    void speciesIdsAreUnique() {
        Set<String> ids = new HashSet<>();
        for (var def : ALL) {
            assertTrue(ids.add(def.id()), "Duplicate species id: " + def.id());
        }
    }

    @Test
    void speciesDominanceMatchesSpec() {
        assertEquals(Dominance.DOMINANT,  MEADOW.speciesAllele().dominance());
        assertEquals(Dominance.DOMINANT,  FOREST.speciesAllele().dominance());
        assertEquals(Dominance.RECESSIVE, ARID.speciesAllele().dominance());
        assertEquals(Dominance.DOMINANT,  CULTIVATED.speciesAllele().dominance());
        assertEquals(Dominance.RECESSIVE, HARDY.speciesAllele().dominance());
    }

    @Test
    void allSpeciesAllelesAreSpeciesType() {
        for (var def : ALL) {
            assertEquals(ChromosomeType.SPECIES, def.speciesAllele().chromosomeType(),
                    def.id() + " speciesAllele must be ChromosomeType.SPECIES");
        }
    }

    @Test
    void allSpeciesHaveAllMvpTraitChromosomes() {
        for (var def : ALL) {
            assertDoesNotThrow(() -> def.defaultTraitAlleles(ChromosomeType.LIFESPAN),
                    def.id() + " missing LIFESPAN");
            assertDoesNotThrow(() -> def.defaultTraitAlleles(ChromosomeType.PRODUCTIVITY),
                    def.id() + " missing PRODUCTIVITY");
            assertDoesNotThrow(() -> def.defaultTraitAlleles(ChromosomeType.FERTILITY),
                    def.id() + " missing FERTILITY");
            assertDoesNotThrow(() -> def.defaultTraitAlleles(ChromosomeType.FLOWER_TYPE),
                    def.id() + " missing FLOWER_TYPE");
        }
    }

    @Test
    void aridDefaultTraitsMatchSpec() {
        // Productivity: Slow / Normal  (hybrid)
        var prod = ARID.defaultTraitAlleles(ChromosomeType.PRODUCTIVITY);
        assertEquals(BuiltinBeeTraits.PRODUCTIVITY_SLOW.id(),   prod[0].id());
        assertEquals(BuiltinBeeTraits.PRODUCTIVITY_NORMAL.id(), prod[1].id());
        // Fertility: One / Two (hybrid)
        var fert = ARID.defaultTraitAlleles(ChromosomeType.FERTILITY);
        assertEquals(BuiltinBeeTraits.FERTILITY_ONE.id(), fert[0].id());
        assertEquals(BuiltinBeeTraits.FERTILITY_TWO.id(), fert[1].id());
    }

    @Test
    void hardyDefaultTraitsMatchSpec() {
        // Lifespan: Long / Normal (hybrid)
        var life = HARDY.defaultTraitAlleles(ChromosomeType.LIFESPAN);
        assertEquals(BuiltinBeeTraits.LIFESPAN_LONG.id(),   life[0].id());
        assertEquals(BuiltinBeeTraits.LIFESPAN_NORMAL.id(), life[1].id());
        // FlowerType: Flowers / Cactus (hybrid)
        var flower = HARDY.defaultTraitAlleles(ChromosomeType.FLOWER_TYPE);
        assertEquals(BuiltinBeeTraits.FLOWER_FLOWERS.id(), flower[0].id());
        assertEquals(BuiltinBeeTraits.FLOWER_CACTUS.id(),  flower[1].id());
    }

    @Test
    void noResourceSpeciesPresent() {
        for (var def : ALL) {
            assertFalse(def.id().contains("iron"),    "Unexpected resource species: " + def.id());
            assertFalse(def.id().contains("gold"),    "Unexpected resource species: " + def.id());
            assertFalse(def.id().contains("diamond"), "Unexpected resource species: " + def.id());
        }
    }

    @Test
    void allMvpSpeciesHaveVisualDefinition() {
        for (var def : ALL) {
            assertTrue(def.visualDefinition().isPresent(),
                    def.id() + " must have a visual definition");
        }
    }

    @Test
    void mvpSpeciesVisualTexturePathsFollowConvention() {
        assertEquals("curiousbees:textures/entity/bee/meadow.png",     MEADOW.visualDefinition().get().textureId());
        assertEquals("curiousbees:textures/entity/bee/forest.png",     FOREST.visualDefinition().get().textureId());
        assertEquals("curiousbees:textures/entity/bee/arid.png",       ARID.visualDefinition().get().textureId());
        assertEquals("curiousbees:textures/entity/bee/cultivated.png", CULTIVATED.visualDefinition().get().textureId());
        assertEquals("curiousbees:textures/entity/bee/hardy.png",      HARDY.visualDefinition().get().textureId());
    }

    @Test
    void mvpSpeciesVisualUseDefaultModel() {
        for (var def : ALL) {
            assertTrue(def.visualDefinition().get().usesDefaultModel(),
                    def.id() + " should use default model in MVP");
        }
    }
}
