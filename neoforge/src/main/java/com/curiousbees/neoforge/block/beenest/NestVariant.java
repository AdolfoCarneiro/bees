package com.curiousbees.neoforge.block.beenest;

/**
 * Visual form of a bee nest block. All variants share the same Java class
 * ({@link SpeciesBeeNestBlock}) — the distinction lives entirely in the
 * block name and JSON model files, so new variants require no Java changes.
 */
public enum NestVariant {
    /** Standard orientable nest, like vanilla bee nest. */
    STANDARD,
    /** Nest embedded in a log or hollow tree trunk. */
    LOG,
    /** Nest flush with a ground or cliff surface. */
    SURFACE,
    /** Nest suspended from a branch or ceiling. */
    HANGING
}
