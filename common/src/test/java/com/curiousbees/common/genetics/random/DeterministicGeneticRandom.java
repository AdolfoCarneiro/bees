package com.curiousbees.common.genetics.random;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Test-only GeneticRandom that returns pre-configured values in order.
 * Throws if exhausted so tests fail explicitly rather than silently.
 */
public final class DeterministicGeneticRandom implements GeneticRandom {

    private final Deque<Boolean> booleans = new ArrayDeque<>();
    private final Deque<Double> doubles = new ArrayDeque<>();
    private final Deque<Integer> ints = new ArrayDeque<>();

    public DeterministicGeneticRandom withBooleans(boolean... values) {
        for (boolean v : values) booleans.addLast(v);
        return this;
    }

    public DeterministicGeneticRandom withDoubles(double... values) {
        for (double v : values) doubles.addLast(v);
        return this;
    }

    public DeterministicGeneticRandom withInts(int... values) {
        for (int v : values) ints.addLast(v);
        return this;
    }

    @Override
    public boolean nextBoolean() {
        if (booleans.isEmpty()) throw new IllegalStateException("No more booleans configured");
        return booleans.removeFirst();
    }

    @Override
    public double nextDouble() {
        if (doubles.isEmpty()) throw new IllegalStateException("No more doubles configured");
        return doubles.removeFirst();
    }

    @Override
    public int nextInt(int bound) {
        if (ints.isEmpty()) throw new IllegalStateException("No more ints configured");
        return ints.removeFirst();
    }
}
