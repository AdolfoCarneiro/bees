package com.curiousbees.neoforge.registry;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.neoforge.data.CapturedBeeData;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModDataComponents {

    private ModDataComponents() {}

    public static final DeferredRegister<DataComponentType<?>> DATA_COMPONENTS =
            DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, CuriousBeesMod.MOD_ID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<CapturedBeeData>> CAPTURED_BEE =
            DATA_COMPONENTS.register("captured_bee",
                    () -> DataComponentType.<CapturedBeeData>builder()
                            .persistent(CapturedBeeData.CODEC)
                            .networkSynchronized(CapturedBeeData.STREAM_CODEC)
                            .build());

    public static void register(IEventBus modEventBus) {
        DATA_COMPONENTS.register(modEventBus);
    }
}
