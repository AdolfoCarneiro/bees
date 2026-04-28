package com.curiousbees.common.genetics.model;

import com.curiousbees.common.genetics.random.GeneticRandom;

import java.util.Objects;

public final class GenePair {

    private final Allele first;
    private final Allele second;
    private final Allele active;
    private final Allele inactive;

    public GenePair(Allele first, Allele second, GeneticRandom random) {
        Objects.requireNonNull(first, "first allele must not be null");
        Objects.requireNonNull(second, "second allele must not be null");
        Objects.requireNonNull(random, "random must not be null");
        if (first.chromosomeType() != second.chromosomeType()) {
            throw new IllegalArgumentException(
                    "alleles must share the same ChromosomeType, got: "
                    + first.chromosomeType() + " and " + second.chromosomeType());
        }
        this.first = first;
        this.second = second;

        Allele[] resolved = resolve(first, second, random);
        this.active = resolved[0];
        this.inactive = resolved[1];
    }

    private static Allele[] resolve(Allele a, Allele b, GeneticRandom random) {
        if (a.dominance() == Dominance.DOMINANT && b.dominance() == Dominance.RECESSIVE) {
            return new Allele[]{a, b};
        }
        if (a.dominance() == Dominance.RECESSIVE && b.dominance() == Dominance.DOMINANT) {
            return new Allele[]{b, a};
        }
        // equal dominance: DOMINANT+DOMINANT or RECESSIVE+RECESSIVE — random choice
        return random.nextBoolean() ? new Allele[]{a, b} : new Allele[]{b, a};
    }

    public Allele first() {
        return first;
    }

    public Allele second() {
        return second;
    }

    public Allele active() {
        return active;
    }

    public Allele inactive() {
        return inactive;
    }

    public ChromosomeType chromosomeType() {
        return first.chromosomeType();
    }

    public boolean isPurebred() {
        return first.id().equals(second.id());
    }

    public boolean isHybrid() {
        return !isPurebred();
    }

    public boolean containsAllele(String alleleId) {
        Objects.requireNonNull(alleleId, "alleleId must not be null");
        return first.id().equals(alleleId) || second.id().equals(alleleId);
    }

    public boolean hasActiveAllele(String alleleId) {
        Objects.requireNonNull(alleleId, "alleleId must not be null");
        return active.id().equals(alleleId);
    }

    public boolean hasInactiveAllele(String alleleId) {
        Objects.requireNonNull(alleleId, "alleleId must not be null");
        return inactive.id().equals(alleleId);
    }

    @Override
    public String toString() {
        return "GenePair{active=" + active.id() + ", inactive=" + inactive.id() + "}";
    }
}
