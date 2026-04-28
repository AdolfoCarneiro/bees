package com.curiousbees.common.genetics.random;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GeneticRandomTest {

    // --- JavaGeneticRandom ---

    @Test
    void javaRandomNullFails() {
        assertThrows(NullPointerException.class, () -> new JavaGeneticRandom(null));
    }

    @Test
    void javaRandomNextBooleanReturnsValue() {
        JavaGeneticRandom r = JavaGeneticRandom.create();
        // just verify it doesn't throw and returns a valid boolean
        boolean v = r.nextBoolean();
        assertTrue(v == true || v == false);
    }

    @Test
    void javaRandomNextDoubleInRange() {
        JavaGeneticRandom r = JavaGeneticRandom.create();
        for (int i = 0; i < 100; i++) {
            double v = r.nextDouble();
            assertTrue(v >= 0.0 && v < 1.0, "nextDouble out of range: " + v);
        }
    }

    @Test
    void javaRandomNextIntInRange() {
        JavaGeneticRandom r = JavaGeneticRandom.create();
        for (int i = 0; i < 100; i++) {
            int v = r.nextInt(10);
            assertTrue(v >= 0 && v < 10, "nextInt out of range: " + v);
        }
    }

    @Test
    void javaRandomNextIntZeroBoundFails() {
        JavaGeneticRandom r = JavaGeneticRandom.create();
        assertThrows(IllegalArgumentException.class, () -> r.nextInt(0));
    }

    @Test
    void javaRandomNextIntNegativeBoundFails() {
        JavaGeneticRandom r = JavaGeneticRandom.create();
        assertThrows(IllegalArgumentException.class, () -> r.nextInt(-1));
    }

    // --- DeterministicGeneticRandom ---

    @Test
    void deterministicReturnsConfiguredBooleans() {
        DeterministicGeneticRandom r = new DeterministicGeneticRandom().withBooleans(true, false, true);
        assertTrue(r.nextBoolean());
        assertFalse(r.nextBoolean());
        assertTrue(r.nextBoolean());
    }

    @Test
    void deterministicReturnsConfiguredDoubles() {
        DeterministicGeneticRandom r = new DeterministicGeneticRandom().withDoubles(0.1, 0.9, 0.5);
        assertEquals(0.1, r.nextDouble());
        assertEquals(0.9, r.nextDouble());
        assertEquals(0.5, r.nextDouble());
    }

    @Test
    void deterministicReturnsConfiguredInts() {
        DeterministicGeneticRandom r = new DeterministicGeneticRandom().withInts(3, 7, 0);
        assertEquals(3, r.nextInt(10));
        assertEquals(7, r.nextInt(10));
        assertEquals(0, r.nextInt(10));
    }

    @Test
    void deterministicExhaustedBooleansFails() {
        DeterministicGeneticRandom r = new DeterministicGeneticRandom();
        assertThrows(IllegalStateException.class, r::nextBoolean);
    }

    @Test
    void deterministicExhaustedDoublesFails() {
        DeterministicGeneticRandom r = new DeterministicGeneticRandom();
        assertThrows(IllegalStateException.class, r::nextDouble);
    }

    @Test
    void deterministicExhaustedIntsFails() {
        DeterministicGeneticRandom r = new DeterministicGeneticRandom();
        assertThrows(IllegalStateException.class, () -> r.nextInt(5));
    }
}
