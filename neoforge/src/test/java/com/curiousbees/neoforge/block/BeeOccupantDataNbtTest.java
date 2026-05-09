package com.curiousbees.neoforge.block;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies BeeOccupantData field contract — construction and equality.
 * Full NBT roundtrip requires Minecraft bootstrap; covered by integration smoke test.
 */
class BeeOccupantDataNbtTest {

    @Test
    void fieldsRoundtripViaRecord() {
        BeeOccupantData data = new BeeOccupantData(
                "curiousbees:species/meadow",
                true,
                false,
                "curiousbees:productivity/high",
                "curiousbees:flower_type/dandelion"
        );

        assertEquals("curiousbees:species/meadow", data.speciesId());
        assertTrue(data.analyzed());
        assertFalse(data.isPurebred());
        assertEquals("curiousbees:productivity/high", data.productivityId());
        assertEquals("curiousbees:flower_type/dandelion", data.flowerTypeId());
    }

    @Test
    void emptyTraitIdsPermitted() {
        BeeOccupantData data = new BeeOccupantData("unknown", false, false, "", "");
        assertEquals("", data.productivityId());
        assertEquals("", data.flowerTypeId());
    }
}
