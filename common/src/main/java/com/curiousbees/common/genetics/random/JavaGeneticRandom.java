package com.curiousbees.common.genetics.random;

import java.util.random.RandomGenerator;

public final class JavaGeneticRandom implements GeneticRandom {

    private final RandomGenerator random;

    public JavaGeneticRandom(RandomGenerator random) {
        if (random == null) throw new NullPointerException("random must not be null");
        this.random = random;
    }

    public static JavaGeneticRandom create() {
        return new JavaGeneticRandom(RandomGenerator.getDefault());
    }

    @Override
    public boolean nextBoolean() {
        return random.nextBoolean();
    }

    @Override
    public double nextDouble() {
        return random.nextDouble();
    }

    @Override
    public int nextInt(int bound) {
        if (bound <= 0) throw new IllegalArgumentException("bound must be positive, got: " + bound);
        return random.nextInt(bound);
    }
}
