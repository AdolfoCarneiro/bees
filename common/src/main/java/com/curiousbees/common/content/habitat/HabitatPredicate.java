package com.curiousbees.common.content.habitat;

import java.util.List;
import java.util.Objects;

/**
 * Minecraft-free model describing environmental spawn conditions for a bee species.
 * <p>
 * {@code requiredBiomeTags} uses OR semantics: the biome must carry at least one listed tag.
 * An empty tag list is a wildcard that matches any biome (used for fallback species).
 * <p>
 * Light values are 0–15 combined light levels.
 * Y values use Minecraft world coordinates (−64 to 320).
 * {@code weight} drives weighted-random selection when multiple predicates match.
 */
public final class HabitatPredicate {

    public static final int WORLD_MIN_Y = -64;
    public static final int WORLD_MAX_Y = 320;
    public static final int LIGHT_MIN = 0;
    public static final int LIGHT_MAX = 15;

    /** Default surface band: above sea level. */
    public static final int DEFAULT_MIN_Y = 60;
    public static final int DEFAULT_MAX_Y = 320;

    /** Comfortable daylight threshold. */
    public static final int DEFAULT_MIN_LIGHT = 9;
    public static final int DEFAULT_MAX_LIGHT = 15;

    public static final int DEFAULT_WEIGHT = 1;

    private final List<String> requiredBiomeTags;
    private final int minY;
    private final int maxY;
    private final int minLight;
    private final int maxLight;
    private final int weight;

    /**
     * @param requiredBiomeTags biome tag IDs (e.g. {@code "minecraft:is_forest"}); empty = any biome
     * @param minY              inclusive lower Y bound; must be ≥ {@value #WORLD_MIN_Y}
     * @param maxY              inclusive upper Y bound; must be ≤ {@value #WORLD_MAX_Y} and ≥ minY
     * @param minLight          inclusive lower light level [0–15]; must be ≤ maxLight
     * @param maxLight          inclusive upper light level [0–15]
     * @param weight            positive spawn weight for weighted selection among matching predicates
     */
    public HabitatPredicate(List<String> requiredBiomeTags,
                             int minY, int maxY,
                             int minLight, int maxLight,
                             int weight) {
        Objects.requireNonNull(requiredBiomeTags, "requiredBiomeTags must not be null");
        if (minY < WORLD_MIN_Y || minY > WORLD_MAX_Y) {
            throw new IllegalArgumentException("minY " + minY + " out of world range [" + WORLD_MIN_Y + ", " + WORLD_MAX_Y + "]");
        }
        if (maxY < WORLD_MIN_Y || maxY > WORLD_MAX_Y) {
            throw new IllegalArgumentException("maxY " + maxY + " out of world range [" + WORLD_MIN_Y + ", " + WORLD_MAX_Y + "]");
        }
        if (minY > maxY) {
            throw new IllegalArgumentException("minY " + minY + " must be ≤ maxY " + maxY);
        }
        if (minLight < LIGHT_MIN || minLight > LIGHT_MAX) {
            throw new IllegalArgumentException("minLight " + minLight + " out of range [0, 15]");
        }
        if (maxLight < LIGHT_MIN || maxLight > LIGHT_MAX) {
            throw new IllegalArgumentException("maxLight " + maxLight + " out of range [0, 15]");
        }
        if (minLight > maxLight) {
            throw new IllegalArgumentException("minLight " + minLight + " must be ≤ maxLight " + maxLight);
        }
        if (weight < 1) {
            throw new IllegalArgumentException("weight must be ≥ 1, got: " + weight);
        }

        this.requiredBiomeTags = List.copyOf(requiredBiomeTags);
        this.minY = minY;
        this.maxY = maxY;
        this.minLight = minLight;
        this.maxLight = maxLight;
        this.weight = weight;
    }

    /**
     * Convenience factory with surface-daylight defaults.
     *
     * @param requiredBiomeTags biome tag IDs; empty = any biome
     */
    public static HabitatPredicate of(List<String> requiredBiomeTags) {
        return new HabitatPredicate(
                requiredBiomeTags,
                DEFAULT_MIN_Y, DEFAULT_MAX_Y,
                DEFAULT_MIN_LIGHT, DEFAULT_MAX_LIGHT,
                DEFAULT_WEIGHT);
    }

    /** OR-matched biome tag IDs. Empty list means any biome qualifies. */
    public List<String> requiredBiomeTags() {
        return requiredBiomeTags;
    }

    /** Inclusive lower Y bound for spawn eligibility. */
    public int minY() { return minY; }

    /** Inclusive upper Y bound for spawn eligibility. */
    public int maxY() { return maxY; }

    /** Inclusive minimum combined light level [0–15]. */
    public int minLight() { return minLight; }

    /** Inclusive maximum combined light level [0–15]. */
    public int maxLight() { return maxLight; }

    /** Positive weight for weighted-random selection among matching predicates. */
    public int weight() { return weight; }

    /** Returns true when {@code biomeTags} contains at least one of this predicate's required tags, or this predicate has no required tags. */
    public boolean matchesBiomeTags(List<String> biomeTags) {
        if (requiredBiomeTags.isEmpty()) return true;
        for (String required : requiredBiomeTags) {
            if (biomeTags.contains(required)) return true;
        }
        return false;
    }

    /** Returns true when {@code y} falls within [minY, maxY]. */
    public boolean matchesY(int y) {
        return y >= minY && y <= maxY;
    }

    /** Returns true when {@code light} falls within [minLight, maxLight]. */
    public boolean matchesLight(int light) {
        return light >= minLight && light <= maxLight;
    }

    @Override
    public String toString() {
        return "HabitatPredicate{tags=" + requiredBiomeTags
                + ", y=[" + minY + "," + maxY + "]"
                + ", light=[" + minLight + "," + maxLight + "]"
                + ", weight=" + weight + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HabitatPredicate other)) return false;
        return minY == other.minY
                && maxY == other.maxY
                && minLight == other.minLight
                && maxLight == other.maxLight
                && weight == other.weight
                && requiredBiomeTags.equals(other.requiredBiomeTags);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requiredBiomeTags, minY, maxY, minLight, maxLight, weight);
    }
}
