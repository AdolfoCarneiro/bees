package com.curiousbees.neoforge.registry;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.neoforge.menu.AdvancedApiaryMenu;
import com.curiousbees.neoforge.menu.CentrifugeMenu;
import com.curiousbees.neoforge.menu.ExpandedApiaryMenu;
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

    public static final DeferredHolder<MenuType<?>, MenuType<AdvancedApiaryMenu>> ADVANCED_APIARY =
            MENUS.register(
                    "advanced_apiary",
                    () -> IMenuTypeExtension.create(AdvancedApiaryMenu::fromNetwork));

    public static final DeferredHolder<MenuType<?>, MenuType<ExpandedApiaryMenu>> EXPANDED_APIARY =
            MENUS.register(
                    "expanded_apiary",
                    () -> IMenuTypeExtension.create(ExpandedApiaryMenu::fromNetwork));

    public static final DeferredHolder<MenuType<?>, MenuType<CentrifugeMenu>> CENTRIFUGE =
            MENUS.register(
                    "centrifuge",
                    () -> IMenuTypeExtension.create(CentrifugeMenu::fromNetwork));

    public static void register(IEventBus modEventBus) {
        MENUS.register(modEventBus);
    }
}
