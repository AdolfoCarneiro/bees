package com.curiousbees.common.content.habitat;

/**
 * Stateless policy service: determines whether a bee of a given species may enter a given species bee nest.
 * Pure Java — no Minecraft, NeoForge, or Fabric imports.
 */
public final class BeeNestCompatibilityService {

    private BeeNestCompatibilityService() {}

    /**
     * Returns {@code true} if a bee of {@code beeSpeciesId} may enter a nest of {@code nestSpeciesId}.
     *
     * <p>Current rule: species IDs must match exactly.
     *
     * @param beeSpeciesId species ID carried by the bee, e.g. {@code curious_bees:species/meadow}
     * @param nestSpeciesId species ID for the nest block, e.g. {@code curious_bees:species/meadow}
     */
    public static boolean canEnter(String beeSpeciesId, String nestSpeciesId) {
        if (beeSpeciesId == null || nestSpeciesId == null) return false;
        return beeSpeciesId.equals(nestSpeciesId);
    }
}
