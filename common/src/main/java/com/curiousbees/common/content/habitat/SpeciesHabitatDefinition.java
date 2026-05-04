package com.curiousbees.common.content.habitat;

import java.util.List;
import java.util.Objects;

/**
 * Minecraft-independent habitat metadata for a world-spawnable bee species.
 * Describes where the species' bee nest block exists in the world and which biomes it inhabits.
 * Uses plain string IDs — no ResourceLocation, Block, or registry references.
 */
public final class SpeciesHabitatDefinition {

    private final String beeNestBlockId;
    private final String beeNestRepresentativeTextureId;
    private final List<String> spawnBiomes;

    /**
     * @param beeNestBlockId                  registry ID of the bee nest block, e.g. {@code curiousbees:meadow_bee_nest}
     * @param beeNestRepresentativeTextureId 16×16 texture path for UI/preview; use the {@code side} face tile, e.g.
     *                                         {@code curiousbees:textures/block/meadow_bee_nest_side.png}
     * @param spawnBiomes                     non-empty list of vanilla biome IDs where this nest generates naturally
     */
    public SpeciesHabitatDefinition(
            String beeNestBlockId,
            String beeNestRepresentativeTextureId,
            List<String> spawnBiomes) {
        Objects.requireNonNull(beeNestBlockId, "beeNestBlockId must not be null");
        Objects.requireNonNull(beeNestRepresentativeTextureId, "beeNestRepresentativeTextureId must not be null");
        Objects.requireNonNull(spawnBiomes, "spawnBiomes must not be null");

        if (beeNestBlockId.isBlank()) throw new IllegalArgumentException("beeNestBlockId must not be blank");
        if (beeNestRepresentativeTextureId.isBlank()) {
            throw new IllegalArgumentException("beeNestRepresentativeTextureId must not be blank");
        }
        if (spawnBiomes.isEmpty()) throw new IllegalArgumentException("spawnBiomes must not be empty");

        this.beeNestBlockId = beeNestBlockId;
        this.beeNestRepresentativeTextureId = beeNestRepresentativeTextureId;
        this.spawnBiomes = List.copyOf(spawnBiomes);
    }

    /** Registry ID of the bee nest block, e.g. {@code curiousbees:meadow_bee_nest}. */
    public String beeNestBlockId() {
        return beeNestBlockId;
    }

    /**
     * Representative 16×16 texture (typically the {@code side} face) for future UI, e.g.
     * {@code curiousbees:textures/block/meadow_bee_nest_side.png}.
     */
    public String beeNestRepresentativeTextureId() {
        return beeNestRepresentativeTextureId;
    }

    /** Immutable list of vanilla biome IDs where this bee nest generates naturally. */
    public List<String> spawnBiomes() {
        return spawnBiomes;
    }

    @Override
    public String toString() {
        return "SpeciesHabitatDefinition{beeNestBlockId='" + beeNestBlockId
                + "', spawnBiomes=" + spawnBiomes + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SpeciesHabitatDefinition other)) return false;
        return beeNestBlockId.equals(other.beeNestBlockId)
                && beeNestRepresentativeTextureId.equals(other.beeNestRepresentativeTextureId)
                && spawnBiomes.equals(other.spawnBiomes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beeNestBlockId, beeNestRepresentativeTextureId, spawnBiomes);
    }
}
