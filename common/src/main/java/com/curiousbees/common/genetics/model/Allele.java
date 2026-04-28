package com.curiousbees.common.genetics.model;

import java.util.Objects;

public final class Allele {

    private final String id;
    private final ChromosomeType chromosomeType;
    private final Dominance dominance;

    public Allele(String id, ChromosomeType chromosomeType, Dominance dominance) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(chromosomeType, "chromosomeType must not be null");
        Objects.requireNonNull(dominance, "dominance must not be null");
        if (id.isBlank()) {
            throw new IllegalArgumentException("id must not be blank");
        }
        this.id = id;
        this.chromosomeType = chromosomeType;
        this.dominance = dominance;
    }

    public String id() {
        return id;
    }

    public ChromosomeType chromosomeType() {
        return chromosomeType;
    }

    public Dominance dominance() {
        return dominance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Allele other)) return false;
        return id.equals(other.id)
                && chromosomeType == other.chromosomeType
                && dominance == other.dominance;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, chromosomeType, dominance);
    }

    @Override
    public String toString() {
        return "Allele{id='" + id + "', type=" + chromosomeType + ", dominance=" + dominance + "}";
    }
}
