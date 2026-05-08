package com.curiousbees.common.genetics.fixtures;

import com.curiousbees.common.genetics.model.Allele;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Dominance;

/** Pre-built Allele instances for use in genetics unit tests. */
public final class AlleleFixtures {

    private AlleleFixtures() {}

    // --- Species (all DOMINANT per MVP spec) ---
    public static final Allele MEADOW     = new Allele("curious_bees:species/meadow",     ChromosomeType.SPECIES, Dominance.DOMINANT);
    public static final Allele FOREST     = new Allele("curious_bees:species/forest",     ChromosomeType.SPECIES, Dominance.DOMINANT);
    public static final Allele ARID       = new Allele("curious_bees:species/arid",       ChromosomeType.SPECIES, Dominance.DOMINANT);
    public static final Allele CULTIVATED = new Allele("curious_bees:species/cultivated", ChromosomeType.SPECIES, Dominance.DOMINANT);
    public static final Allele HARDY      = new Allele("curious_bees:species/hardy",      ChromosomeType.SPECIES, Dominance.DOMINANT);

    // --- Productivity ---
    public static final Allele PRODUCTIVITY_SLOW   = new Allele("curious_bees:traits/productivity/slow",   ChromosomeType.PRODUCTIVITY, Dominance.RECESSIVE);
    public static final Allele PRODUCTIVITY_NORMAL = new Allele("curious_bees:traits/productivity/normal", ChromosomeType.PRODUCTIVITY, Dominance.DOMINANT);
    public static final Allele PRODUCTIVITY_FAST   = new Allele("curious_bees:traits/productivity/fast",   ChromosomeType.PRODUCTIVITY, Dominance.RECESSIVE);

    // --- FlowerType ---
    public static final Allele FLOWER_FLOWERS = new Allele("curious_bees:traits/flower_type/flowers", ChromosomeType.FLOWER_TYPE, Dominance.DOMINANT);
    public static final Allele FLOWER_CACTUS  = new Allele("curious_bees:traits/flower_type/cactus",  ChromosomeType.FLOWER_TYPE, Dominance.RECESSIVE);
    public static final Allele FLOWER_LEAVES  = new Allele("curious_bees:traits/flower_type/leaves",  ChromosomeType.FLOWER_TYPE, Dominance.RECESSIVE);
}
