package com.curiousbees.common.content.data;

import java.util.Objects;

/**
 * A pair of trait allele IDs representing the [first, second] default genome pair
 * for one chromosome slot within a species definition.
 */
public final class TraitAllelePairData {

    private final String first;
    private final String second;

    public TraitAllelePairData(String first, String second) {
        this.first = Objects.requireNonNull(first, "first must not be null");
        this.second = Objects.requireNonNull(second, "second must not be null");
    }

    public String first() { return first; }
    public String second() { return second; }

    @Override
    public String toString() {
        return "TraitAllelePairData{first='" + first + "', second='" + second + "'}";
    }
}
