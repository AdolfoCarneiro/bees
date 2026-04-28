package com.curiousbees.common.genetics.model;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class Genome {

    private final Map<ChromosomeType, GenePair> genePairs;

    public Genome(Map<ChromosomeType, GenePair> genePairs) {
        Objects.requireNonNull(genePairs, "genePairs must not be null");
        if (!genePairs.containsKey(ChromosomeType.SPECIES)) {
            throw new IllegalArgumentException("Genome must contain a SPECIES chromosome");
        }
        EnumMap<ChromosomeType, GenePair> copy = new EnumMap<>(ChromosomeType.class);
        for (Map.Entry<ChromosomeType, GenePair> entry : genePairs.entrySet()) {
            ChromosomeType key = Objects.requireNonNull(entry.getKey(), "ChromosomeType key must not be null");
            GenePair pair = Objects.requireNonNull(entry.getValue(), "GenePair must not be null for key " + key);
            if (pair.chromosomeType() != key) {
                throw new IllegalArgumentException(
                        "GenePair chromosome type " + pair.chromosomeType()
                        + " does not match map key " + key);
            }
            copy.put(key, pair);
        }
        this.genePairs = Collections.unmodifiableMap(copy);
    }

    public GenePair getGenePair(ChromosomeType type) {
        Objects.requireNonNull(type, "type must not be null");
        GenePair pair = genePairs.get(type);
        if (pair == null) {
            throw new IllegalArgumentException("No GenePair for chromosome: " + type);
        }
        return pair;
    }

    public Allele getActiveAllele(ChromosomeType type) {
        return getGenePair(type).active();
    }

    public Allele getInactiveAllele(ChromosomeType type) {
        return getGenePair(type).inactive();
    }

    public GenePair species() {
        return genePairs.get(ChromosomeType.SPECIES);
    }

    public boolean isPurebred(ChromosomeType type) {
        return getGenePair(type).isPurebred();
    }

    public boolean isHybrid(ChromosomeType type) {
        return getGenePair(type).isHybrid();
    }

    public boolean hasChromosome(ChromosomeType type) {
        return genePairs.containsKey(type);
    }

    /** Returns a read-only view of all gene pairs. */
    public Map<ChromosomeType, GenePair> genePairs() {
        return genePairs;
    }

    /** Returns a new Genome with the given ChromosomeType replaced. Does not mutate this instance. */
    public Genome withGenePair(ChromosomeType type, GenePair pair) {
        Objects.requireNonNull(type, "type must not be null");
        Objects.requireNonNull(pair, "pair must not be null");
        EnumMap<ChromosomeType, GenePair> updated = new EnumMap<>(genePairs);
        updated.put(type, pair);
        return new Genome(updated);
    }

    @Override
    public String toString() {
        return "Genome" + genePairs;
    }
}
