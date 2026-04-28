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

    // --- Lifespan ---
    public static final Allele LIFESPAN_SHORT  = new Allele("curious_bees:lifespan/short",  ChromosomeType.LIFESPAN, Dominance.RECESSIVE);
    public static final Allele LIFESPAN_NORMAL = new Allele("curious_bees:lifespan/normal", ChromosomeType.LIFESPAN, Dominance.DOMINANT);
    public static final Allele LIFESPAN_LONG   = new Allele("curious_bees:lifespan/long",   ChromosomeType.LIFESPAN, Dominance.DOMINANT);

    // --- Productivity ---
    public static final Allele PRODUCTIVITY_SLOW   = new Allele("curious_bees:productivity/slow",   ChromosomeType.PRODUCTIVITY, Dominance.RECESSIVE);
    public static final Allele PRODUCTIVITY_NORMAL = new Allele("curious_bees:productivity/normal", ChromosomeType.PRODUCTIVITY, Dominance.DOMINANT);
    public static final Allele PRODUCTIVITY_FAST   = new Allele("curious_bees:productivity/fast",   ChromosomeType.PRODUCTIVITY, Dominance.DOMINANT);

    // --- Fertility ---
    public static final Allele FERTILITY_ONE   = new Allele("curious_bees:fertility/one",   ChromosomeType.FERTILITY, Dominance.RECESSIVE);
    public static final Allele FERTILITY_TWO   = new Allele("curious_bees:fertility/two",   ChromosomeType.FERTILITY, Dominance.DOMINANT);
    public static final Allele FERTILITY_THREE = new Allele("curious_bees:fertility/three", ChromosomeType.FERTILITY, Dominance.DOMINANT);

    // --- FlowerType ---
    public static final Allele FLOWER_FLOWERS = new Allele("curious_bees:flower_type/flowers", ChromosomeType.FLOWER_TYPE, Dominance.DOMINANT);
    public static final Allele FLOWER_CACTUS  = new Allele("curious_bees:flower_type/cactus",  ChromosomeType.FLOWER_TYPE, Dominance.RECESSIVE);
    public static final Allele FLOWER_LEAVES  = new Allele("curious_bees:flower_type/leaves",  ChromosomeType.FLOWER_TYPE, Dominance.RECESSIVE);
}
