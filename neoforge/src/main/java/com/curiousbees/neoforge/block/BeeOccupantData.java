package com.curiousbees.neoforge.block;

/**
 * Client-synced snapshot of one occupant bee's genetic report.
 * Captured once when the bee enters the hive. Never gated by isAnalyzed() — ADR-0016.
 */
public record BeeOccupantData(
        String speciesId,
        boolean analyzed,
        boolean isPurebred,
        String productivityId,
        String flowerTypeId
) {}
