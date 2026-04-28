package com.curiousbees.common.content.species;

import com.curiousbees.common.content.builtin.BuiltinBeeSpecies;
import com.curiousbees.common.content.builtin.BuiltinBeeTraits;
import com.curiousbees.common.genetics.model.Allele;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Dominance;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class BeeSpeciesDefinitionTest {

    private static Map<ChromosomeType, Allele[]> validTraits() {
        Map<ChromosomeType, Allele[]> m = new EnumMap<>(ChromosomeType.class);
        m.put(ChromosomeType.LIFESPAN,     new Allele[]{BuiltinBeeTraits.LIFESPAN_NORMAL,     BuiltinBeeTraits.LIFESPAN_NORMAL});
        m.put(ChromosomeType.PRODUCTIVITY, new Allele[]{BuiltinBeeTraits.PRODUCTIVITY_NORMAL, BuiltinBeeTraits.PRODUCTIVITY_NORMAL});
        m.put(ChromosomeType.FERTILITY,    new Allele[]{BuiltinBeeTraits.FERTILITY_TWO,       BuiltinBeeTraits.FERTILITY_TWO});
        m.put(ChromosomeType.FLOWER_TYPE,  new Allele[]{BuiltinBeeTraits.FLOWER_FLOWERS,      BuiltinBeeTraits.FLOWER_FLOWERS});
        return m;
    }

    private static final Allele VALID_SPECIES = BuiltinBeeSpecies.SPECIES_MEADOW;

    @Test
    void validDefinitionSucceeds() {
        BeeSpeciesDefinition def = new BeeSpeciesDefinition(
                "curious_bees:species/test", "Test Bee", VALID_SPECIES, validTraits(), List.of());
        assertEquals("curious_bees:species/test", def.id());
        assertEquals("Test Bee", def.displayName());
        assertEquals(VALID_SPECIES, def.speciesAllele());
    }

    @Test
    void nullIdFails() {
        assertThrows(NullPointerException.class,
                () -> new BeeSpeciesDefinition(null, "Test", VALID_SPECIES, validTraits(), List.of()));
    }

    @Test
    void blankIdFails() {
        assertThrows(IllegalArgumentException.class,
                () -> new BeeSpeciesDefinition("  ", "Test", VALID_SPECIES, validTraits(), List.of()));
    }

    @Test
    void nullDisplayNameFails() {
        assertThrows(NullPointerException.class,
                () -> new BeeSpeciesDefinition("id", null, VALID_SPECIES, validTraits(), List.of()));
    }

    @Test
    void blankDisplayNameFails() {
        assertThrows(IllegalArgumentException.class,
                () -> new BeeSpeciesDefinition("id", "  ", VALID_SPECIES, validTraits(), List.of()));
    }

    @Test
    void nullSpeciesAlleleFails() {
        assertThrows(NullPointerException.class,
                () -> new BeeSpeciesDefinition("id", "Test", null, validTraits(), List.of()));
    }

    @Test
    void nonSpeciesAlleleFails() {
        Allele wrong = BuiltinBeeTraits.LIFESPAN_NORMAL;
        assertThrows(IllegalArgumentException.class,
                () -> new BeeSpeciesDefinition("id", "Test", wrong, validTraits(), List.of()));
    }

    @Test
    void missingTraitChromosomeFails() {
        Map<ChromosomeType, Allele[]> incomplete = validTraits();
        incomplete.remove(ChromosomeType.FERTILITY);
        assertThrows(IllegalArgumentException.class,
                () -> new BeeSpeciesDefinition("id", "Test", VALID_SPECIES, incomplete, List.of()));
    }

    @Test
    void speciesKeyInTraitMapFails() {
        Map<ChromosomeType, Allele[]> bad = validTraits();
        bad.put(ChromosomeType.SPECIES, new Allele[]{VALID_SPECIES, VALID_SPECIES});
        assertThrows(IllegalArgumentException.class,
                () -> new BeeSpeciesDefinition("id", "Test", VALID_SPECIES, bad, List.of()));
    }

    @Test
    void wrongArraySizeFails() {
        Map<ChromosomeType, Allele[]> bad = validTraits();
        bad.put(ChromosomeType.LIFESPAN, new Allele[]{BuiltinBeeTraits.LIFESPAN_NORMAL});
        assertThrows(IllegalArgumentException.class,
                () -> new BeeSpeciesDefinition("id", "Test", VALID_SPECIES, bad, List.of()));
    }

    @Test
    void mismatchedAlleleChrTypeFails() {
        Map<ChromosomeType, Allele[]> bad = validTraits();
        // put a PRODUCTIVITY allele under LIFESPAN key
        bad.put(ChromosomeType.LIFESPAN,
                new Allele[]{BuiltinBeeTraits.PRODUCTIVITY_NORMAL, BuiltinBeeTraits.LIFESPAN_NORMAL});
        assertThrows(IllegalArgumentException.class,
                () -> new BeeSpeciesDefinition("id", "Test", VALID_SPECIES, bad, List.of()));
    }

    @Test
    void defaultTraitAllelesAreImmutableView() {
        BeeSpeciesDefinition def = new BeeSpeciesDefinition(
                "id", "Test", VALID_SPECIES, validTraits(), List.of());
        // returned array is a copy — mutating it does not affect the definition
        Allele[] returned = def.defaultTraitAlleles(ChromosomeType.LIFESPAN);
        returned[0] = BuiltinBeeTraits.LIFESPAN_SHORT;
        assertNotEquals(BuiltinBeeTraits.LIFESPAN_SHORT,
                def.defaultTraitAlleles(ChromosomeType.LIFESPAN)[0]);
    }

    @Test
    void spawnContextNotesArePreserved() {
        BeeSpeciesDefinition def = new BeeSpeciesDefinition(
                "id", "Test", VALID_SPECIES, validTraits(), List.of("plains", "forest"));
        assertEquals(List.of("plains", "forest"), def.spawnContextNotes());
    }
}
