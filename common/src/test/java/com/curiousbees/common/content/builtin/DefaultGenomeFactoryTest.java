package com.curiousbees.common.content.builtin;

import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static com.curiousbees.common.content.builtin.BuiltinBeeSpecies.*;
import static com.curiousbees.common.content.builtin.BuiltinBeeTraits.*;
import static org.junit.jupiter.api.Assertions.*;

class DefaultGenomeFactoryTest {

    private JavaGeneticRandom random() {
        return new JavaGeneticRandom(new Random(0));
    }

    @Test
    void createMeadowGenome() {
        Genome g = DefaultGenomeFactory.createDefault(MEADOW, random());
        assertEquals(SPECIES_MEADOW.id(), g.getActiveAllele(ChromosomeType.SPECIES).id());
        assertTrue(g.isPurebred(ChromosomeType.SPECIES));
    }

    @Test
    void createForestGenome() {
        Genome g = DefaultGenomeFactory.createDefault(FOREST, random());
        assertEquals(SPECIES_FOREST.id(), g.getActiveAllele(ChromosomeType.SPECIES).id());
        assertTrue(g.isPurebred(ChromosomeType.SPECIES));
    }

    @Test
    void createAridGenome() {
        Genome g = DefaultGenomeFactory.createDefault(ARID, random());
        // Species is purebred Arid/Arid
        assertTrue(g.isPurebred(ChromosomeType.SPECIES));
        assertTrue(g.species().containsAllele(SPECIES_ARID.id()));
        // Productivity: Slow/Normal - not purebred
        assertTrue(g.isHybrid(ChromosomeType.PRODUCTIVITY));
        assertTrue(g.getGenePair(ChromosomeType.PRODUCTIVITY).containsAllele(PRODUCTIVITY_SLOW.id()));
        assertTrue(g.getGenePair(ChromosomeType.PRODUCTIVITY).containsAllele(PRODUCTIVITY_NORMAL.id()));
        // Fertility: One/Two - not purebred
        assertTrue(g.isHybrid(ChromosomeType.FERTILITY));
    }

    @Test
    void createCultivatedGenome() {
        Genome g = DefaultGenomeFactory.createDefault(CULTIVATED, random());
        assertEquals(SPECIES_CULTIVATED.id(), g.getActiveAllele(ChromosomeType.SPECIES).id());
        assertTrue(g.isPurebred(ChromosomeType.SPECIES));
        // Productivity: Fast/Normal (hybrid)
        assertTrue(g.isHybrid(ChromosomeType.PRODUCTIVITY));
        assertTrue(g.getGenePair(ChromosomeType.PRODUCTIVITY).containsAllele(PRODUCTIVITY_FAST.id()));
    }

    @Test
    void createHardyGenome() {
        Genome g = DefaultGenomeFactory.createDefault(HARDY, random());
        assertTrue(g.isPurebred(ChromosomeType.SPECIES));
        assertTrue(g.species().containsAllele(SPECIES_HARDY.id()));
        // Lifespan: Long/Normal (hybrid)
        assertTrue(g.isHybrid(ChromosomeType.LIFESPAN));
        assertTrue(g.getGenePair(ChromosomeType.LIFESPAN).containsAllele(LIFESPAN_LONG.id()));
        // FlowerType: Flowers/Cactus (hybrid)
        assertTrue(g.isHybrid(ChromosomeType.FLOWER_TYPE));
    }

    @Test
    void allDefaultGenomesContainAllMvpChromosomes() {
        for (var def : BuiltinBeeSpecies.ALL) {
            Genome g = DefaultGenomeFactory.createDefault(def, random());
            assertTrue(g.hasChromosome(ChromosomeType.SPECIES),      def.id() + " missing SPECIES");
            assertTrue(g.hasChromosome(ChromosomeType.LIFESPAN),     def.id() + " missing LIFESPAN");
            assertTrue(g.hasChromosome(ChromosomeType.PRODUCTIVITY), def.id() + " missing PRODUCTIVITY");
            assertTrue(g.hasChromosome(ChromosomeType.FERTILITY),    def.id() + " missing FERTILITY");
            assertTrue(g.hasChromosome(ChromosomeType.FLOWER_TYPE),  def.id() + " missing FLOWER_TYPE");
        }
    }
}
