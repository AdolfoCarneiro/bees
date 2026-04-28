package com.curiousbees.common.genetics.random;

public interface GeneticRandom {
    boolean nextBoolean();
    double nextDouble();
    int nextInt(int bound);
}
