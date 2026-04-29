package com.curiousbees.common.gameplay.production;

import com.curiousbees.common.content.builtin.BuiltinBeeTraits;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductivityModifierTest {

    @Test
    void slowProductivityReturns075() {
        assertEquals(0.75, ProductivityModifier.forAlleleId(BuiltinBeeTraits.PRODUCTIVITY_SLOW.id()));
    }

    @Test
    void normalProductivityReturns100() {
        assertEquals(1.00, ProductivityModifier.forAlleleId(BuiltinBeeTraits.PRODUCTIVITY_NORMAL.id()));
    }

    @Test
    void fastProductivityReturns125() {
        assertEquals(1.25, ProductivityModifier.forAlleleId(BuiltinBeeTraits.PRODUCTIVITY_FAST.id()));
    }

    @Test
    void unknownProductivityFallsBackTo100() {
        assertEquals(1.00, ProductivityModifier.forAlleleId("curious_bees:traits/productivity/unknown"));
    }
}
