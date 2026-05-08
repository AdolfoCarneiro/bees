package com.curiousbees.common.content.builtin;

import com.curiousbees.common.content.habitat.HabitatPredicate;
import com.curiousbees.common.content.habitat.SpeciesHabitatDefinition;
import com.curiousbees.common.content.species.BeeSpeciesDefinition;
import com.curiousbees.common.content.visual.SpeciesVisualDefinition;
import com.curiousbees.common.genetics.model.Allele;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Dominance;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static com.curiousbees.common.content.builtin.BuiltinBeeTraits.*;

/** Centralized built-in species definitions for the six bee species (five MVP + Common fallback). */
public final class BuiltinBeeSpecies {

    private BuiltinBeeSpecies() {}

    // Species alleles (production content with correct dominance per spec)
    public static final Allele SPECIES_MEADOW     = new Allele("curious_bees:species/meadow",     ChromosomeType.SPECIES, Dominance.DOMINANT);
    public static final Allele SPECIES_FOREST     = new Allele("curious_bees:species/forest",     ChromosomeType.SPECIES, Dominance.DOMINANT);
    public static final Allele SPECIES_ARID       = new Allele("curious_bees:species/arid",       ChromosomeType.SPECIES, Dominance.RECESSIVE);
    public static final Allele SPECIES_CULTIVATED = new Allele("curious_bees:species/cultivated", ChromosomeType.SPECIES, Dominance.DOMINANT);
    public static final Allele SPECIES_HARDY      = new Allele("curious_bees:species/hardy",      ChromosomeType.SPECIES, Dominance.RECESSIVE);
    public static final Allele SPECIES_COMMON     = new Allele("curious_bees:species/common",     ChromosomeType.SPECIES, Dominance.DOMINANT);

    // Visual definitions — texture paths + display name keys, both centralized here.
    // Renderer reads textureId(); tooltip/screen reads displayNameKey() via registry.
    // DEV-PLACEHOLDER: all five entity textures are flat-colour stand-ins; final art ships in E5 (P5).
    public static final SpeciesVisualDefinition VISUAL_MEADOW     = SpeciesVisualDefinition.ofTexture("curiousbees:textures/entity/bee/meadow.png",     "species.curiousbees.meadow");
    public static final SpeciesVisualDefinition VISUAL_FOREST     = SpeciesVisualDefinition.ofTexture("curiousbees:textures/entity/bee/forest.png",     "species.curiousbees.forest");
    public static final SpeciesVisualDefinition VISUAL_ARID       = SpeciesVisualDefinition.ofTexture("curiousbees:textures/entity/bee/arid.png",       "species.curiousbees.arid");
    public static final SpeciesVisualDefinition VISUAL_CULTIVATED = SpeciesVisualDefinition.ofTexture("curiousbees:textures/entity/bee/cultivated.png", "species.curiousbees.cultivated");
    public static final SpeciesVisualDefinition VISUAL_HARDY      = SpeciesVisualDefinition.ofTexture("curiousbees:textures/entity/bee/hardy.png",      "species.curiousbees.hardy");
    // DEV-PLACEHOLDER texture — reuse meadow sprite until Common bee art is finalized.
    public static final SpeciesVisualDefinition VISUAL_COMMON     = SpeciesVisualDefinition.ofTexture("curiousbees:textures/entity/bee/common.png",     "species.curiousbees.common");

    // Habitat definitions — world-spawnable species only.
    // spawnBiomes: specific IDs used by worldgen feature placement (E2-T07).
    // spawnPredicate: tag-based environmental model consumed by the entity spawn handler (E2-T02).
    //   Meadow uses empty tags (wildcard) — it is the fallback species when no specific tag matches.
    //   Forest matches any biome carrying the minecraft:is_forest tag.
    //   Arid matches savannas and badlands (OR semantics).
    public static final SpeciesHabitatDefinition HABITAT_MEADOW = new SpeciesHabitatDefinition(
            "curiousbees:meadow_bee_nest",
            "curiousbees:textures/block/meadow_bee_nest_side.png",
            List.of("minecraft:plains", "minecraft:flower_forest", "minecraft:meadow"),
            HabitatPredicate.of(List.of()));

    public static final SpeciesHabitatDefinition HABITAT_FOREST = new SpeciesHabitatDefinition(
            "curiousbees:forest_bee_nest",
            "curiousbees:textures/block/forest_bee_nest_side.png",
            List.of("minecraft:forest", "minecraft:birch_forest", "minecraft:dark_forest"),
            HabitatPredicate.of(List.of("minecraft:is_forest")));

    public static final SpeciesHabitatDefinition HABITAT_ARID = new SpeciesHabitatDefinition(
            "curiousbees:arid_bee_nest",
            "curiousbees:textures/block/arid_bee_nest_side.png",
            List.of("minecraft:desert", "minecraft:savanna", "minecraft:badlands"),
            HabitatPredicate.of(List.of("minecraft:is_savanna", "minecraft:is_badlands")));

    // Species definitions
    public static final BeeSpeciesDefinition MEADOW = new BeeSpeciesDefinition(
            "curious_bees:species/meadow",
            "Meadow Bee",
            SPECIES_MEADOW,
            traits(
                    pair(PRODUCTIVITY_NORMAL,  PRODUCTIVITY_NORMAL),
                    pair(FLOWER_FLOWERS,       FLOWER_FLOWERS)),
            List.of("plains", "flower_forest", "meadow"),
            VISUAL_MEADOW,
            HABITAT_MEADOW);

    public static final BeeSpeciesDefinition FOREST = new BeeSpeciesDefinition(
            "curious_bees:species/forest",
            "Forest Bee",
            SPECIES_FOREST,
            traits(
                    pair(PRODUCTIVITY_NORMAL,  PRODUCTIVITY_NORMAL),
                    pair(FLOWER_LEAVES,        FLOWER_LEAVES)),
            List.of("forest", "birch_forest", "dark_forest"),
            VISUAL_FOREST,
            HABITAT_FOREST);

    public static final BeeSpeciesDefinition ARID = new BeeSpeciesDefinition(
            "curious_bees:species/arid",
            "Arid Bee",
            SPECIES_ARID,
            traits(
                    pair(PRODUCTIVITY_SLOW,    PRODUCTIVITY_NORMAL),
                    pair(FLOWER_CACTUS,        FLOWER_CACTUS)),
            List.of("desert", "savanna", "badlands"),
            VISUAL_ARID,
            HABITAT_ARID);

    public static final BeeSpeciesDefinition CULTIVATED = new BeeSpeciesDefinition(
            "curious_bees:species/cultivated",
            "Cultivated Bee",
            SPECIES_CULTIVATED,
            traits(
                    pair(PRODUCTIVITY_FAST,    PRODUCTIVITY_NORMAL),
                    pair(FLOWER_FLOWERS,       FLOWER_FLOWERS)),
            List.of(),
            VISUAL_CULTIVATED);

    public static final BeeSpeciesDefinition HARDY = new BeeSpeciesDefinition(
            "curious_bees:species/hardy",
            "Hardy Bee",
            SPECIES_HARDY,
            traits(
                    pair(PRODUCTIVITY_NORMAL,  PRODUCTIVITY_NORMAL),
                    pair(FLOWER_FLOWERS,       FLOWER_CACTUS)),
            List.of(),
            VISUAL_HARDY);

    // COMMON has no habitat — it is the universal fallback species assigned when no
    // habitat predicate matches (ADR-0019). It is included in ALL so the content
    // registry registers its allele, but the spawn service skips species without
    // habitat predicates when building biome-based candidates.
    public static final BeeSpeciesDefinition COMMON = new BeeSpeciesDefinition(
            "curious_bees:species/common",
            "Common Bee",
            SPECIES_COMMON,
            traits(
                    pair(PRODUCTIVITY_NORMAL,  PRODUCTIVITY_NORMAL),
                    pair(FLOWER_FLOWERS,       FLOWER_FLOWERS)),
            List.of(),   // no spawn context notes — universal fallback
            VISUAL_COMMON);

    /** All species in definition order (includes COMMON for registry; COMMON has no habitat so it is not picked by biome matching). */
    public static final List<BeeSpeciesDefinition> ALL = List.of(MEADOW, FOREST, ARID, CULTIVATED, HARDY, COMMON);

    // --- helpers ---

    private static Map<ChromosomeType, Allele[]> traits(Allele[] productivity, Allele[] flowerType) {
        Map<ChromosomeType, Allele[]> map = new EnumMap<>(ChromosomeType.class);
        map.put(ChromosomeType.PRODUCTIVITY, productivity);
        map.put(ChromosomeType.FLOWER_TYPE,  flowerType);
        return map;
    }

    private static Allele[] pair(Allele first, Allele second) {
        return new Allele[]{first, second};
    }
}
