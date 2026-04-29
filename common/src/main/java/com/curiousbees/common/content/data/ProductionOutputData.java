package com.curiousbees.common.content.data;

import java.util.Objects;

/**
 * Serializable data for one production output entry.
 * item: stable item ID (e.g. "curiousbees:meadow_comb")
 * chance: probability 0.0–1.0
 * min/max: output stack size range (default 1/1)
 */
public final class ProductionOutputData {

    private final String item;
    private final double chance;
    private final int min;
    private final int max;

    public ProductionOutputData(String item, double chance, int min, int max) {
        this.item = Objects.requireNonNull(item, "item must not be null");
        this.chance = chance;
        this.min = min;
        this.max = max;
    }

    /** Convenience constructor: min=1, max=1. */
    public ProductionOutputData(String item, double chance) {
        this(item, chance, 1, 1);
    }

    public String item() { return item; }
    public double chance() { return chance; }
    public int min() { return min; }
    public int max() { return max; }

    @Override
    public String toString() {
        return "ProductionOutputData{item='" + item + "', chance=" + chance
                + ", min=" + min + ", max=" + max + "}";
    }
}
