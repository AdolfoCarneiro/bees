package com.curiousbees.common.content.habitat;

/**
 * Stateless policy service: determines whether a bee of a given species may enter a given hive.
 * Pure Java — no Minecraft, NeoForge, or Fabric imports.
 * Both NeoForge and future Fabric layers call this single implementation.
 */
public final class HiveCompatibilityService {

    private HiveCompatibilityService() {}

    /**
     * Returns {@code true} if a bee of {@code beeSpeciesId} may enter a hive of {@code hiveSpeciesId}.
     *
     * <p>Current rule: species IDs must match exactly.
     * Isolated here so future cross-species rules (e.g. hybrid tolerance) can be added in one place.
     *
     * @param beeSpeciesId  species ID carried by the bee, e.g. {@code curious_bees:species/meadow}
     * @param hiveSpeciesId species ID that the hive belongs to, e.g. {@code curious_bees:species/meadow}
     */
    public static boolean canEnter(String beeSpeciesId, String hiveSpeciesId) {
        if (beeSpeciesId == null || hiveSpeciesId == null) return false;
        return beeSpeciesId.equals(hiveSpeciesId);
    }
}
