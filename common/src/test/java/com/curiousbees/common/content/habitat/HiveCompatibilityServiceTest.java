package com.curiousbees.common.content.habitat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HiveCompatibilityServiceTest {

    @Test
    void matchingSpeciesCanEnter() {
        assertTrue(HiveCompatibilityService.canEnter(
                "curious_bees:species/meadow",
                "curious_bees:species/meadow"));
    }

    @Test
    void differentSpeciesCannotEnter() {
        assertFalse(HiveCompatibilityService.canEnter(
                "curious_bees:species/meadow",
                "curious_bees:species/forest"));
    }

    @Test
    void nullBeeSpeciesCannotEnter() {
        assertFalse(HiveCompatibilityService.canEnter(null, "curious_bees:species/meadow"));
    }

    @Test
    void nullHiveSpeciesCannotEnter() {
        assertFalse(HiveCompatibilityService.canEnter("curious_bees:species/meadow", null));
    }

    @Test
    void bothNullCannotEnter() {
        assertFalse(HiveCompatibilityService.canEnter(null, null));
    }

    @Test
    void aridVsForestCannotEnter() {
        assertFalse(HiveCompatibilityService.canEnter(
                "curious_bees:species/arid",
                "curious_bees:species/forest"));
    }

    @Test
    void forestVsForestCanEnter() {
        assertTrue(HiveCompatibilityService.canEnter(
                "curious_bees:species/forest",
                "curious_bees:species/forest"));
    }

    @Test
    void aridVsAridCanEnter() {
        assertTrue(HiveCompatibilityService.canEnter(
                "curious_bees:species/arid",
                "curious_bees:species/arid"));
    }
}
