package com.curiousbees.common.gameplay.frames;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FrameModifierTest {

    @Test
    void frameModifierRejectsBlankId() {
        assertThrows(IllegalArgumentException.class,
                () -> new FrameModifier(" ", 1.0, 1.0, 1));
    }

    @Test
    void frameModifierRejectsNegativeMutationMultiplier() {
        assertThrows(IllegalArgumentException.class,
                () -> new FrameModifier("curiousbees:basic_frame", -0.1, 1.0, 1));
    }

    @Test
    void frameModifierRejectsNegativeProductionMultiplier() {
        assertThrows(IllegalArgumentException.class,
                () -> new FrameModifier("curiousbees:basic_frame", 1.0, -0.1, 1));
    }

    @Test
    void frameModifierRejectsNegativeDurabilityCost() {
        assertThrows(IllegalArgumentException.class,
                () -> new FrameModifier("curiousbees:basic_frame", 1.0, 1.0, -1));
    }

    @Test
    void combineReturnsIdentityForEmptyList() {
        var combined = FrameModifiers.combine(List.of());
        assertEquals(1.0, combined.mutationMultiplier());
        assertEquals(1.0, combined.productionMultiplier());
        assertEquals(0, combined.durabilityCostPerCycle());
    }

    @Test
    void combineMultipliesMultipliersAndSumsDurability() {
        var basic = new FrameModifier("curiousbees:basic_frame", 1.05, 1.05, 1);
        var mutation = new FrameModifier("curiousbees:mutation_frame", 1.25, 1.00, 2);

        var combined = FrameModifiers.combine(List.of(basic, mutation));

        assertEquals(1.3125, combined.mutationMultiplier(), 0.000001);
        assertEquals(1.05, combined.productionMultiplier(), 0.000001);
        assertEquals(3, combined.durabilityCostPerCycle());
    }

    @Test
    void combineSkipsNullEntriesSafely() {
        var basic = new FrameModifier("curiousbees:basic_frame", 1.05, 1.05, 1);

        var combined = FrameModifiers.combine(Arrays.asList(basic, null));

        assertEquals(1.05, combined.mutationMultiplier(), 0.000001);
        assertEquals(1.05, combined.productionMultiplier(), 0.000001);
        assertEquals(1, combined.durabilityCostPerCycle());
    }
}
