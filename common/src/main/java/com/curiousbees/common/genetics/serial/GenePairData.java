package com.curiousbees.common.genetics.serial;

import java.util.Objects;

/**
 * Platform-neutral serialized form of a GenePair.
 * Stores all four allele IDs to preserve active/inactive identity without rerolling.
 */
public record GenePairData(
        String firstAlleleId,
        String secondAlleleId,
        String activeAlleleId,
        String inactiveAlleleId) {

    public GenePairData {
        Objects.requireNonNull(firstAlleleId,    "firstAlleleId must not be null");
        Objects.requireNonNull(secondAlleleId,   "secondAlleleId must not be null");
        Objects.requireNonNull(activeAlleleId,   "activeAlleleId must not be null");
        Objects.requireNonNull(inactiveAlleleId, "inactiveAlleleId must not be null");
        if (firstAlleleId.isBlank())    throw new IllegalArgumentException("firstAlleleId must not be blank");
        if (secondAlleleId.isBlank())   throw new IllegalArgumentException("secondAlleleId must not be blank");
        if (activeAlleleId.isBlank())   throw new IllegalArgumentException("activeAlleleId must not be blank");
        if (inactiveAlleleId.isBlank()) throw new IllegalArgumentException("inactiveAlleleId must not be blank");
    }
}
