package com.curiousbees.common.content.frames;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BuiltinFrameModifiersTest {

    @Test
    void builtinFrameModifierMapContainsAllPhase7fFrames() {
        assertEquals(3, BuiltinFrameModifiers.BY_ID.size());
        assertTrue(BuiltinFrameModifiers.BY_ID.containsKey("curiousbees:basic_frame"));
        assertTrue(BuiltinFrameModifiers.BY_ID.containsKey("curiousbees:mutation_frame"));
        assertTrue(BuiltinFrameModifiers.BY_ID.containsKey("curiousbees:productivity_frame"));
    }

    @Test
    void builtinFrameValuesMatchPhase7fBaselineTuning() {
        assertEquals(1.03, BuiltinFrameModifiers.BASIC.mutationMultiplier(), 0.000001);
        assertEquals(1.03, BuiltinFrameModifiers.BASIC.productionMultiplier(), 0.000001);

        assertEquals(1.18, BuiltinFrameModifiers.MUTATION.mutationMultiplier(), 0.000001);
        assertEquals(1.00, BuiltinFrameModifiers.MUTATION.productionMultiplier(), 0.000001);

        assertEquals(1.00, BuiltinFrameModifiers.PRODUCTIVITY.mutationMultiplier(), 0.000001);
        assertEquals(1.18, BuiltinFrameModifiers.PRODUCTIVITY.productionMultiplier(), 0.000001);
    }
}
