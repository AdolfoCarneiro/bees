package com.curiousbees.neoforge.config;

import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * Common (server-side) configuration for Curious Bees.
 * Register via {@code modContainer.registerConfig(ModConfig.Type.COMMON, SPEC)}.
 */
public final class CuriousBeesConfig {

    private CuriousBeesConfig() {}

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    /**
     * Hard cap on Curious Bees (any species combined) within a 32-block radius.
     * When breeding would push the local count above this limit the baby bee
     * spawns without a genome and stays vanilla. Set to 0 to disable.
     */
    public static final ModConfigSpec.IntValue BEE_POPULATION_CAP = BUILDER
            .comment("Max Curious Bees within 32 blocks. Breeding is skipped when the cap is hit. 0 = unlimited.")
            .defineInRange("beePopulationCap", 24, 0, 512);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
