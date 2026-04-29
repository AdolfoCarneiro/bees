package com.curiousbees.neoforge.registry;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.neoforge.item.BeeAnalyzerItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModItems {

    private ModItems() {}

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, CuriousBeesMod.MOD_ID);

    public static final DeferredHolder<Item, BeeAnalyzerItem> BEE_ANALYZER =
            ITEMS.register("bee_analyzer",
                    () -> new BeeAnalyzerItem(new Item.Properties().stacksTo(1)));

    // --- Comb items ---
    public static final DeferredHolder<Item, Item> MEADOW_COMB =
            ITEMS.register("meadow_comb",    () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> FOREST_COMB =
            ITEMS.register("forest_comb",    () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> ARID_COMB =
            ITEMS.register("arid_comb",      () -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> CULTIVATED_COMB =
            ITEMS.register("cultivated_comb",() -> new Item(new Item.Properties()));
    public static final DeferredHolder<Item, Item> HARDY_COMB =
            ITEMS.register("hardy_comb",     () -> new Item(new Item.Properties()));

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
