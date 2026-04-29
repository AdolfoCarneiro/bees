package com.curiousbees.common.gameplay.production;

import java.util.Objects;

/**
 * Represents one possible output from a production roll.
 * Uses a stable string ID; the platform layer resolves it to an actual item.
 */
public final class ProductionOutput {

    private final String outputId;
    private final double chance;
    private final int count;

    public ProductionOutput(String outputId, double chance, int count) {
        if (outputId == null || outputId.isBlank()) {
            throw new IllegalArgumentException("outputId must not be null or blank");
        }
        if (chance < 0.0 || chance > 1.0) {
            throw new IllegalArgumentException(
                    "chance must be between 0.0 and 1.0, got: " + chance);
        }
        if (count < 1) {
            throw new IllegalArgumentException("count must be >= 1, got: " + count);
        }
        this.outputId = outputId;
        this.chance   = chance;
        this.count    = count;
    }

    /** Convenience constructor: single item output. */
    public ProductionOutput(String outputId, double chance) {
        this(outputId, chance, 1);
    }

    public String outputId() { return outputId; }
    public double chance()   { return chance; }
    public int count()       { return count; }
}
