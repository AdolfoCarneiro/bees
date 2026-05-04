package com.curiousbees.neoforge.registry;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.neoforge.menu.GeneticApiaryMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModMenuTypes {

    private ModMenuTypes() {}

    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(Registries.MENU, CuriousBeesMod.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<GeneticApiaryMenu>> GENETIC_APIARY =
            MENUS.register(
                    "genetic_apiary",
                    () -> IMenuTypeExtension.create(GeneticApiaryMenu::fromNetwork));

    public static void register(IEventBus modEventBus) {
        MENUS.register(modEventBus);
    }
}
