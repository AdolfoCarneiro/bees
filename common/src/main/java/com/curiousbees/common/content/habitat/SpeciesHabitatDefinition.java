package com.curiousbees.common.content.habitat;

import java.util.List;
import java.util.Objects;

/**
 * Minecraft-independent habitat metadata for a world-spawnable bee species.
 * Describes where the species' hive block exists in the world and which biomes it inhabits.
 * Uses plain string IDs — no ResourceLocation, Block, or registry references.
 */
public final class SpeciesHabitatDefinition {

    private final String hiveBlockId;
    private final String hiveTextureId;
    private final List<String> spawnBiomes;

    /**
     * @param hiveBlockId   registry ID of the hive block, e.g. {@code curiousbees:meadow_hive}
     * @param hiveTextureId texture resource path, e.g. {@code curiousbees:textures/block/meadow_hive.png}
     * @param spawnBiomes   non-empty list of vanilla biome IDs where this hive generates naturally
     */
    public SpeciesHabitatDefinition(String hiveBlockId, String hiveTextureId, List<String> spawnBiomes) {
        Objects.requireNonNull(hiveBlockId, "hiveBlockId must not be null");
        Objects.requireNonNull(hiveTextureId, "hiveTextureId must not be null");
        Objects.requireNonNull(spawnBiomes, "spawnBiomes must not be null");

        if (hiveBlockId.isBlank()) throw new IllegalArgumentException("hiveBlockId must not be blank");
        if (hiveTextureId.isBlank()) throw new IllegalArgumentException("hiveTextureId must not be blank");
        if (spawnBiomes.isEmpty()) throw new IllegalArgumentException("spawnBiomes must not be empty");

        this.hiveBlockId = hiveBlockId;
        this.hiveTextureId = hiveTextureId;
        this.spawnBiomes = List.copyOf(spawnBiomes);
    }

    /** Registry ID of the hive block, e.g. {@code curiousbees:meadow_hive}. */
    public String hiveBlockId() { return hiveBlockId; }

    /** Texture resource path, e.g. {@code curiousbees:textures/block/meadow_hive.png}. */
    public String hiveTextureId() { return hiveTextureId; }

    /** Immutable list of vanilla biome IDs where this hive generates naturally. */
    public List<String> spawnBiomes() { return spawnBiomes; }

    @Override
    public String toString() {
        return "SpeciesHabitatDefinition{hiveBlockId='" + hiveBlockId
                + "', spawnBiomes=" + spawnBiomes + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpeciesHabitatDefinition other)) return false;
        return hiveBlockId.equals(other.hiveBlockId)
                && hiveTextureId.equals(other.hiveTextureId)
                && spawnBiomes.equals(other.spawnBiomes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(hiveBlockId, hiveTextureId, spawnBiomes);
    }
}
