package com.curiousbees.common.content.habitat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BeeNestCompatibilityServiceTest {

    @Test
    void matchingSpeciesCanEnter() {
        assertTrue(BeeNestCompatibilityService.canEnter(
                "curious_bees:species/meadow",
                "curious_bees:species/meadow"));
    }

    @Test
    void differentSpeciesCannotEnter() {
        assertFalse(BeeNestCompatibilityService.canEnter(
                "curious_bees:species/meadow",
                "curious_bees:species/forest"));
    }

    @Test
    void nullBeeSpeciesCannotEnter() {
        assertFalse(BeeNestCompatibilityService.canEnter(null, "curious_bees:species/meadow"));
    }

    @Test
    void nullNestSpeciesCannotEnter() {
        assertFalse(BeeNestCompatibilityService.canEnter("curious_bees:species/meadow", null));
    }

    @Test
    void bothNullCannotEnter() {
        assertFalse(BeeNestCompatibilityService.canEnter(null, null));
    }

    @Test
    void aridVsForestCannotEnter() {
        assertFalse(BeeNestCompatibilityService.canEnter(
                "curious_bees:species/arid",
                "curious_bees:species/forest"));
    }

    @Test
    void forestVsForestCanEnter() {
        assertTrue(BeeNestCompatibilityService.canEnter(
                "curious_bees:species/forest",
                "curious_bees:species/forest"));
    }

    @Test
    void aridVsAridCanEnter() {
        assertTrue(BeeNestCompatibilityService.canEnter(
                "curious_bees:species/arid",
                "curious_bees:species/arid"));
    }
}
