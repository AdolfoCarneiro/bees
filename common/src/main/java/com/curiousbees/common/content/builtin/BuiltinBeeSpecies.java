package com.curiousbees.common.content.builtin;

import com.curiousbees.common.content.species.BeeSpeciesDefinition;
import com.curiousbees.common.content.visual.SpeciesVisualDefinition;
import com.curiousbees.common.genetics.model.Allele;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Dominance;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static com.curiousbees.common.content.builtin.BuiltinBeeTraits.*;

/** Centralized built-in species definitions for the five MVP bee species. */
public final class BuiltinBeeSpecies {

    private BuiltinBeeSpecies() {}

    // Species alleles (production content with correct dominance per spec)
    public static final Allele SPECIES_MEADOW     = new Allele("curious_bees:species/meadow",     ChromosomeType.SPECIES, Dominance.DOMINANT);
    public static final Allele SPECIES_FOREST     = new Allele("curious_bees:species/forest",     ChromosomeType.SPECIES, Dominance.DOMINANT);
    public static final Allele SPECIES_ARID       = new Allele("curious_bees:species/arid",       ChromosomeType.SPECIES, Dominance.RECESSIVE);
    public static final Allele SPECIES_CULTIVATED = new Allele("curious_bees:species/cultivated", ChromosomeType.SPECIES, Dominance.DOMINANT);
    public static final Allele SPECIES_HARDY      = new Allele("curious_bees:species/hardy",      ChromosomeType.SPECIES, Dominance.RECESSIVE);

    // Visual definitions — texture paths follow the convention: curiousbees:textures/entity/bee/<species>.png
    public static final SpeciesVisualDefinition VISUAL_MEADOW     = SpeciesVisualDefinition.ofTexture("curiousbees:textures/entity/bee/meadow.png");
    public static final SpeciesVisualDefinition VISUAL_FOREST     = SpeciesVisualDefinition.ofTexture("curiousbees:textures/entity/bee/forest.png");
    public static final SpeciesVisualDefinition VISUAL_ARID       = SpeciesVisualDefinition.ofTexture("curiousbees:textures/entity/bee/arid.png");
    public static final SpeciesVisualDefinition VISUAL_CULTIVATED = SpeciesVisualDefinition.ofTexture("curiousbees:textures/entity/bee/cultivated.png");
    public static final SpeciesVisualDefinition VISUAL_HARDY      = SpeciesVisualDefinition.ofTexture("curiousbees:textures/entity/bee/hardy.png");

    // Species definitions
    public static final BeeSpeciesDefinition MEADOW = new BeeSpeciesDefinition(
            "curious_bees:species/meadow",
            "Meadow Bee",
            SPECIES_MEADOW,
            traits(
                    pair(LIFESPAN_NORMAL,      LIFESPAN_NORMAL),
                    pair(PRODUCTIVITY_NORMAL,  PRODUCTIVITY_NORMAL),
                    pair(FERTILITY_TWO,        FERTILITY_TWO),
                    pair(FLOWER_FLOWERS,       FLOWER_FLOWERS)),
            List.of("plains", "flower_forest", "meadow"),
            VISUAL_MEADOW);

    public static final BeeSpeciesDefinition FOREST = new BeeSpeciesDefinition(
            "curious_bees:species/forest",
            "Forest Bee",
            SPECIES_FOREST,
            traits(
                    pair(LIFESPAN_NORMAL,      LIFESPAN_NORMAL),
                    pair(PRODUCTIVITY_NORMAL,  PRODUCTIVITY_NORMAL),
                    pair(FERTILITY_TWO,        FERTILITY_TWO),
                    pair(FLOWER_LEAVES,        FLOWER_LEAVES)),
            List.of("forest", "birch_forest", "dark_forest"),
            VISUAL_FOREST);

    public static final BeeSpeciesDefinition ARID = new BeeSpeciesDefinition(
            "curious_bees:species/arid",
            "Arid Bee",
            SPECIES_ARID,
            traits(
                    pair(LIFESPAN_NORMAL,      LIFESPAN_NORMAL),
                    pair(PRODUCTIVITY_SLOW,    PRODUCTIVITY_NORMAL),
                    pair(FERTILITY_ONE,        FERTILITY_TWO),
                    pair(FLOWER_CACTUS,        FLOWER_CACTUS)),
            List.of("desert", "savanna", "badlands"),
            VISUAL_ARID);

    public static final BeeSpeciesDefinition CULTIVATED = new BeeSpeciesDefinition(
            "curious_bees:species/cultivated",
            "Cultivated Bee",
            SPECIES_CULTIVATED,
            traits(
                    pair(LIFESPAN_NORMAL,      LIFESPAN_NORMAL),
                    pair(PRODUCTIVITY_FAST,    PRODUCTIVITY_NORMAL),
                    pair(FERTILITY_TWO,        FERTILITY_TWO),
                    pair(FLOWER_FLOWERS,       FLOWER_FLOWERS)),
            List.of(),
            VISUAL_CULTIVATED);

    public static final BeeSpeciesDefinition HARDY = new BeeSpeciesDefinition(
            "curious_bees:species/hardy",
            "Hardy Bee",
            SPECIES_HARDY,
            traits(
                    pair(LIFESPAN_LONG,        LIFESPAN_NORMAL),
                    pair(PRODUCTIVITY_NORMAL,  PRODUCTIVITY_NORMAL),
                    pair(FERTILITY_TWO,        FERTILITY_TWO),
                    pair(FLOWER_FLOWERS,       FLOWER_CACTUS)),
            List.of(),
            VISUAL_HARDY);

    /** All MVP species in definition order. */
    public static final List<BeeSpeciesDefinition> ALL = List.of(MEADOW, FOREST, ARID, CULTIVATED, HARDY);

    // --- helpers ---

    private static Map<ChromosomeType, Allele[]> traits(Allele[] lifespan, Allele[] productivity,
                                                         Allele[] fertility, Allele[] flowerType) {
        Map<ChromosomeType, Allele[]> map = new EnumMap<>(ChromosomeType.class);
        map.put(ChromosomeType.LIFESPAN,     lifespan);
        map.put(ChromosomeType.PRODUCTIVITY, productivity);
        map.put(ChromosomeType.FERTILITY,    fertility);
        map.put(ChromosomeType.FLOWER_TYPE,  flowerType);
        return map;
    }

    private static Allele[] pair(Allele first, Allele second) {
        return new Allele[]{first, second};
    }
}
