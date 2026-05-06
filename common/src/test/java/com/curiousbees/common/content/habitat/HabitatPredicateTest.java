package com.curiousbees.common.content.habitat;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HabitatPredicateTest {

    @Test
    void factoryDefaultsAreInRange() {
        HabitatPredicate p = HabitatPredicate.of(List.of("minecraft:is_forest"));
        assertEquals(List.of("minecraft:is_forest"), p.requiredBiomeTags());
        assertEquals(HabitatPredicate.DEFAULT_MIN_Y, p.minY());
        assertEquals(HabitatPredicate.DEFAULT_MAX_Y, p.maxY());
        assertEquals(HabitatPredicate.DEFAULT_MIN_LIGHT, p.minLight());
        assertEquals(HabitatPredicate.DEFAULT_MAX_LIGHT, p.maxLight());
        assertEquals(HabitatPredicate.DEFAULT_WEIGHT, p.weight());
    }

    @Test
    void emptyTagsIsWildcard() {
        HabitatPredicate p = HabitatPredicate.of(List.of());
        assertTrue(p.matchesBiomeTags(List.of()));
        assertTrue(p.matchesBiomeTags(List.of("minecraft:is_forest", "minecraft:is_overworld")));
    }

    @Test
    void biomeTagMatchingUsesOrSemantics() {
        HabitatPredicate p = HabitatPredicate.of(List.of("minecraft:is_savanna", "minecraft:is_badlands"));
        assertTrue(p.matchesBiomeTags(List.of("minecraft:is_savanna")));
        assertTrue(p.matchesBiomeTags(List.of("minecraft:is_badlands")));
        assertTrue(p.matchesBiomeTags(List.of("minecraft:is_savanna", "minecraft:is_badlands")));
        assertFalse(p.matchesBiomeTags(List.of("minecraft:is_forest")));
        assertFalse(p.matchesBiomeTags(List.of()));
    }

    @Test
    void yBoundsMatchCorrectly() {
        HabitatPredicate p = new HabitatPredicate(List.of(), 60, 200, 0, 15, 1);
        assertTrue(p.matchesY(60));
        assertTrue(p.matchesY(130));
        assertTrue(p.matchesY(200));
        assertFalse(p.matchesY(59));
        assertFalse(p.matchesY(201));
    }

    @Test
    void lightBoundsMatchCorrectly() {
        HabitatPredicate p = new HabitatPredicate(List.of(), 60, 320, 9, 15, 1);
        assertTrue(p.matchesLight(9));
        assertTrue(p.matchesLight(15));
        assertFalse(p.matchesLight(8));
    }

    @Test
    void biomeTagListIsImmutable() {
        HabitatPredicate p = HabitatPredicate.of(List.of("minecraft:is_forest"));
        assertThrows(UnsupportedOperationException.class, () -> p.requiredBiomeTags().add("extra"));
    }

    @Test
    void rejectsMinYGreaterThanMaxY() {
        assertThrows(IllegalArgumentException.class,
                () -> new HabitatPredicate(List.of(), 100, 50, 0, 15, 1));
    }

    @Test
    void rejectsMinLightGreaterThanMaxLight() {
        assertThrows(IllegalArgumentException.class,
                () -> new HabitatPredicate(List.of(), 60, 320, 12, 5, 1));
    }

    @Test
    void rejectsLightOutOfRange() {
        assertThrows(IllegalArgumentException.class,
                () -> new HabitatPredicate(List.of(), 60, 320, -1, 15, 1));
        assertThrows(IllegalArgumentException.class,
                () -> new HabitatPredicate(List.of(), 60, 320, 0, 16, 1));
    }

    @Test
    void rejectsYOutOfWorldRange() {
        assertThrows(IllegalArgumentException.class,
                () -> new HabitatPredicate(List.of(), -65, 320, 0, 15, 1));
        assertThrows(IllegalArgumentException.class,
                () -> new HabitatPredicate(List.of(), 60, 321, 0, 15, 1));
    }

    @Test
    void rejectsZeroWeight() {
        assertThrows(IllegalArgumentException.class,
                () -> new HabitatPredicate(List.of(), 60, 320, 0, 15, 0));
    }

    @Test
    void rejectsNullBiomeTags() {
        assertThrows(NullPointerException.class,
                () -> new HabitatPredicate(null, 60, 320, 0, 15, 1));
    }

    @Test
    void equalityBasedOnAllFields() {
        HabitatPredicate a = new HabitatPredicate(List.of("minecraft:is_forest"), 60, 320, 9, 15, 1);
        HabitatPredicate b = new HabitatPredicate(List.of("minecraft:is_forest"), 60, 320, 9, 15, 1);
        HabitatPredicate c = new HabitatPredicate(List.of("minecraft:is_forest"), 60, 320, 9, 15, 2);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }
}
