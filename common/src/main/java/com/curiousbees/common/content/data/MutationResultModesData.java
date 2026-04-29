package com.curiousbees.common.content.data;

/**
 * Serializable result mode split for a mutation definition.
 * partialChance + fullChance should sum to 1.0, validated separately.
 */
public final class MutationResultModesData {

    private final double partialChance;
    private final double fullChance;

    public MutationResultModesData(double partialChance, double fullChance) {
        this.partialChance = partialChance;
        this.fullChance = fullChance;
    }

    public double partialChance() { return partialChance; }
    public double fullChance() { return fullChance; }

    @Override
    public String toString() {
        return "MutationResultModesData{partial=" + partialChance + ", full=" + fullChance + "}";
    }
}
