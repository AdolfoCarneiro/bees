package com.curiousbees.neoforge.registry;

import com.curiousbees.CuriousBeesMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModCreativeTabs {

    private ModCreativeTabs() {}

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CuriousBeesMod.MOD_ID);

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CURIOUS_BEES_TAB =
            CREATIVE_MODE_TABS.register("curious_bees", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.curiousbees.curious_bees"))
                    .icon(() -> new ItemStack(ModItems.BEE_ANALYZER.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.BEE_ANALYZER.get());
                        for (var egg : ModItems.BEE_SPAWN_EGGS) {
                            output.accept(egg.get());
                        }
                        output.accept(ModItems.GENETIC_APIARY.get());
                        output.accept(ModItems.MEADOW_HIVE.get());
                        output.accept(ModItems.FOREST_HIVE.get());
                        output.accept(ModItems.ARID_HIVE.get());
                        output.accept(ModItems.MEADOW_COMB.get());
                        output.accept(ModItems.FOREST_COMB.get());
                        output.accept(ModItems.ARID_COMB.get());
                        output.accept(ModItems.CULTIVATED_COMB.get());
                        output.accept(ModItems.HARDY_COMB.get());
                        output.accept(ModItems.BASIC_FRAME.get());
                        output.accept(ModItems.MUTATION_FRAME.get());
                        output.accept(ModItems.PRODUCTIVITY_FRAME.get());
                    })
                    .build());

    public static void register(IEventBus modEventBus) {
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
