package com.curiousbees.neoforge.registry;

import com.curiousbees.CuriousBeesMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.Set;

public final class ModPoiTypes {

    private ModPoiTypes() {}

    public static final DeferredRegister<PoiType> POI_TYPES =
            DeferredRegister.create(Registries.POINT_OF_INTEREST_TYPE, CuriousBeesMod.MOD_ID);

    public static final DeferredHolder<PoiType, PoiType> GENETIC_APIARY =
            POI_TYPES.register("genetic_apiary",
                    () -> new PoiType(
                            Set.copyOf(ModBlocks.GENETIC_APIARY.get().getStateDefinition().getPossibleStates()),
                            1,
                            1));

    public static void register(IEventBus modEventBus) {
        POI_TYPES.register(modEventBus);
    }
}
