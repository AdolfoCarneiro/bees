package com.curiousbees.neoforge.registry;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.neoforge.worldgen.SpeciesBeeNestFeature;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModFeatures {

    private ModFeatures() {}

    public static final DeferredRegister<Feature<?>> FEATURES =
            DeferredRegister.create(Registries.FEATURE, CuriousBeesMod.MOD_ID);

    public static final DeferredHolder<Feature<?>, SpeciesBeeNestFeature> SPECIES_BEE_NEST =
            FEATURES.register("species_bee_nest",
                    () -> new SpeciesBeeNestFeature(BlockStateConfiguration.CODEC));

    public static void register(IEventBus modEventBus) {
        FEATURES.register(modEventBus);
    }
}
