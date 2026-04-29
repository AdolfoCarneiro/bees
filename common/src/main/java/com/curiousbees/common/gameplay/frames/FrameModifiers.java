package com.curiousbees.common.gameplay.frames;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * Utility service for combining multiple frame modifiers.
 */
public final class FrameModifiers {

    private static final Logger LOGGER = Logger.getLogger(FrameModifiers.class.getName());

    private FrameModifiers() {}

    public static CombinedFrameModifier combine(List<FrameModifier> modifiers) {
        Objects.requireNonNull(modifiers, "modifiers must not be null");

        double mutation = 1.0;
        double production = 1.0;
        int durabilityCost = 0;

        for (FrameModifier modifier : modifiers) {
            if (modifier == null) {
                LOGGER.warning("Null frame modifier entry encountered — skipping.");
                continue;
            }
            mutation *= modifier.mutationMultiplier();
            production *= modifier.productionMultiplier();
            durabilityCost += modifier.durabilityCostPerCycle();
        }

        return new CombinedFrameModifier(mutation, production, durabilityCost);
    }

    public record CombinedFrameModifier(
            double mutationMultiplier,
            double productionMultiplier,
            int durabilityCostPerCycle) {
        public CombinedFrameModifier {
            if (mutationMultiplier < 0.0) {
                throw new IllegalArgumentException("mutationMultiplier must be >= 0.0");
            }
            if (productionMultiplier < 0.0) {
                throw new IllegalArgumentException("productionMultiplier must be >= 0.0");
            }
            if (durabilityCostPerCycle < 0) {
                throw new IllegalArgumentException("durabilityCostPerCycle must be >= 0");
            }
        }
    }
}
