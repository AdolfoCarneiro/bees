package com.curiousbees.common.gameplay.production;

import com.curiousbees.common.content.builtin.BuiltinBeeTraits;

import java.util.logging.Logger;

/**
 * Maps the active productivity allele ID to a production chance multiplier.
 * Unknown IDs fall back to 1.0 with a logged warning.
 */
public final class ProductivityModifier {

    private static final Logger LOGGER = Logger.getLogger(ProductivityModifier.class.getName());

    public static final double SLOW_MULTIPLIER   = 0.75;
    public static final double NORMAL_MULTIPLIER = 1.00;
    public static final double FAST_MULTIPLIER   = 1.25;

    private ProductivityModifier() {}

    /**
     * Returns the production chance multiplier for the given productivity allele ID.
     * Falls back to 1.0 for unknown IDs.
     */
    public static double forAlleleId(String productivityAlleleId) {
        if (BuiltinBeeTraits.PRODUCTIVITY_SLOW.id().equals(productivityAlleleId)) {
            return SLOW_MULTIPLIER;
        }
        if (BuiltinBeeTraits.PRODUCTIVITY_FAST.id().equals(productivityAlleleId)) {
            return FAST_MULTIPLIER;
        }
        if (BuiltinBeeTraits.PRODUCTIVITY_NORMAL.id().equals(productivityAlleleId)) {
            return NORMAL_MULTIPLIER;
        }
        LOGGER.warning("Unknown productivity allele ID '" + productivityAlleleId
                + "' — defaulting to 1.0 multiplier.");
        return NORMAL_MULTIPLIER;
    }
}
