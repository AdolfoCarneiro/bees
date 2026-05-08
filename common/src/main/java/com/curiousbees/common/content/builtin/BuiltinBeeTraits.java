package com.curiousbees.common.content.builtin;

import com.curiousbees.common.genetics.model.Allele;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Dominance;

import java.util.List;

/** Centralized built-in trait allele definitions for the MVP chromosomes. */
public final class BuiltinBeeTraits {

    private BuiltinBeeTraits() {}

    // --- Productivity ---
    public static final Allele PRODUCTIVITY_SLOW   = new Allele("curious_bees:traits/productivity/slow",   ChromosomeType.PRODUCTIVITY, Dominance.RECESSIVE);
    public static final Allele PRODUCTIVITY_NORMAL = new Allele("curious_bees:traits/productivity/normal", ChromosomeType.PRODUCTIVITY, Dominance.DOMINANT);
    public static final Allele PRODUCTIVITY_FAST   = new Allele("curious_bees:traits/productivity/fast",   ChromosomeType.PRODUCTIVITY, Dominance.RECESSIVE);

    // --- FlowerType ---
    public static final Allele FLOWER_FLOWERS = new Allele("curious_bees:traits/flower_type/flowers", ChromosomeType.FLOWER_TYPE, Dominance.DOMINANT);
    public static final Allele FLOWER_CACTUS  = new Allele("curious_bees:traits/flower_type/cactus",  ChromosomeType.FLOWER_TYPE, Dominance.RECESSIVE);
    public static final Allele FLOWER_LEAVES  = new Allele("curious_bees:traits/flower_type/leaves",  ChromosomeType.FLOWER_TYPE, Dominance.RECESSIVE);

    /** All built-in trait alleles for validation and lookup. */
    public static final List<Allele> ALL = List.of(
            PRODUCTIVITY_SLOW, PRODUCTIVITY_NORMAL, PRODUCTIVITY_FAST,
            FLOWER_FLOWERS, FLOWER_CACTUS, FLOWER_LEAVES);
}
