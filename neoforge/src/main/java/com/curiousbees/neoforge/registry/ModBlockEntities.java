package com.curiousbees.neoforge.registry;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.neoforge.block.GeneticApiaryBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {

    private ModBlockEntities() {}

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
            DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CuriousBeesMod.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GeneticApiaryBlockEntity>>
            GENETIC_APIARY = BLOCK_ENTITY_TYPES.register("genetic_apiary",
                    () -> BlockEntityType.Builder
                            .of(GeneticApiaryBlockEntity::new, ModBlocks.GENETIC_APIARY.get())
                            .build(null));

    public static void register(IEventBus modEventBus) {
        BLOCK_ENTITY_TYPES.register(modEventBus);
    }
}
