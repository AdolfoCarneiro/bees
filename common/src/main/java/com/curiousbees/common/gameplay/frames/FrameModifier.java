package com.curiousbees.common.gameplay.frames;

import java.util.Objects;

/**
 * Immutable frame modifier model for tech apiary behavior tuning.
 * Pure Java and platform-neutral.
 */
public final class FrameModifier {

    private final String id;
    private final double mutationMultiplier;
    private final double productionMultiplier;
    private final int durabilityCostPerCycle;

    public FrameModifier(String id,
                         double mutationMultiplier,
                         double productionMultiplier,
                         int durabilityCostPerCycle) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("id must not be null or blank");
        }
        if (mutationMultiplier < 0.0) {
            throw new IllegalArgumentException("mutationMultiplier must be >= 0.0");
        }
        if (productionMultiplier < 0.0) {
            throw new IllegalArgumentException("productionMultiplier must be >= 0.0");
        }
        if (durabilityCostPerCycle < 0) {
            throw new IllegalArgumentException("durabilityCostPerCycle must be >= 0");
        }

        this.id = id;
        this.mutationMultiplier = mutationMultiplier;
        this.productionMultiplier = productionMultiplier;
        this.durabilityCostPerCycle = durabilityCostPerCycle;
    }

    public String id() {
        return id;
    }

    public double mutationMultiplier() {
        return mutationMultiplier;
    }

    public double productionMultiplier() {
        return productionMultiplier;
    }

    public int durabilityCostPerCycle() {
        return durabilityCostPerCycle;
    }
}
