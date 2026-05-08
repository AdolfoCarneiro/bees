package com.curiousbees.common.content.habitat;

public record HabitatDiscoveryConfig(double baseChance, double partialChance, double fullChance) {
    public static final HabitatDiscoveryConfig DEFAULT = new HabitatDiscoveryConfig(0.03, 0.95, 0.05);
}
