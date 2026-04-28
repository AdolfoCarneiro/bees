package com.curiousbees.common.genetics.breeding;

import com.curiousbees.common.genetics.model.Genome;

import java.util.Objects;

public final class BreedingResult {

    private final Genome childGenome;

    public BreedingResult(Genome childGenome) {
        this.childGenome = Objects.requireNonNull(childGenome, "childGenome must not be null");
    }

    public Genome childGenome() {
        return childGenome;
    }
}
