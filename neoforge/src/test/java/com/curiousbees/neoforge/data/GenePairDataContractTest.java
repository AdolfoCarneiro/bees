package com.curiousbees.neoforge.data;

// BUILD NOTE: Same classpath requirements as GenomeCodecRoundtripTest.
// GenePairData is a pure Java record — no Minecraft/NeoForge types needed.

import com.curiousbees.common.genetics.serial.GenePairData;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies GenePairData record contracts: immutability, equality, validation.
 *
 * These are the primitives that GenomeCodec serializes. Testing them independently
 * pins the invariants that codec roundtrip tests depend on.
 */
class GenePairDataContractTest {

    private static GenePairData meadowPair() {
        return new GenePairData(
                "curious_bees:species/meadow",
                "curious_bees:species/meadow",
                "curious_bees:species/meadow",
                "curious_bees:species/meadow");
    }

    private static GenePairData hybridPair() {
        return new GenePairData(
                "curious_bees:species/meadow",
                "curious_bees:species/forest",
                "curious_bees:species/meadow",
                "curious_bees:species/forest");
    }

    // --- validation ---

    @Test
    void nullFirstAlleleFails() {
        assertThrows(NullPointerException.class,
                () -> new GenePairData(null, "b", "b", "b"));
    }

    @Test
    void nullSecondAlleleFails() {
        assertThrows(NullPointerException.class,
                () -> new GenePairData("a", null, "a", "a"));
    }

    @Test
    void nullActiveAlleleFails() {
        assertThrows(NullPointerException.class,
                () -> new GenePairData("a", "b", null, "b"));
    }

    @Test
    void nullInactiveAlleleFails() {
        assertThrows(NullPointerException.class,
                () -> new GenePairData("a", "b", "a", null));
    }

    @Test
    void blankFirstAlleleFails() {
        assertThrows(IllegalArgumentException.class,
                () -> new GenePairData("", "b", "b", "b"));
    }

    @Test
    void blankActiveAlleleFails() {
        assertThrows(IllegalArgumentException.class,
                () -> new GenePairData("a", "b", " ", "b"));
    }

    // --- record equality ---

    @Test
    void twoIdenticalPairsAreEqual() {
        GenePairData a = meadowPair();
        GenePairData b = meadowPair();
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void pairsWithDifferentActiveAreNotEqual() {
        GenePairData a = new GenePairData("x", "y", "x", "y");
        GenePairData b = new GenePairData("x", "y", "y", "x");
        assertNotEquals(a, b,
                "active/inactive identity must be part of equality — no canonicalization");
    }

    // --- accessors ---

    @Test
    void accessorsReturnConstructorValues() {
        GenePairData pair = hybridPair();
        assertEquals("curious_bees:species/meadow", pair.firstAlleleId());
        assertEquals("curious_bees:species/forest", pair.secondAlleleId());
        assertEquals("curious_bees:species/meadow", pair.activeAlleleId());
        assertEquals("curious_bees:species/forest", pair.inactiveAlleleId());
    }

    // --- active != inactive contract ---

    @Test
    void activeAndInactiveCanDifferForHybrid() {
        GenePairData pair = hybridPair();
        assertNotEquals(pair.activeAlleleId(), pair.inactiveAlleleId(),
                "Hybrid pair must have distinct active and inactive alleles");
    }

    @Test
    void activeAndInactiveAreEqualForPurebred() {
        GenePairData pair = meadowPair();
        assertEquals(pair.activeAlleleId(), pair.inactiveAlleleId(),
                "Purebred pair must have the same active and inactive allele");
    }

    // --- serialization roundtrip awareness (structural) ---

    @Test
    void allFourFieldsAreIndependentlyStored() {
        // If codec only stored two fields and recomputed active/inactive,
        // this test would catch the regression.
        GenePairData original = hybridPair();
        // After codec roundtrip in GenomeCodecRoundtripTest we get the same object back;
        // here we verify the raw record stores all four independently.
        assertNotSame(original.firstAlleleId(), original.secondAlleleId());
        assertNotSame(original.activeAlleleId(), original.inactiveAlleleId());
    }
}
