package com.curiousbees.neoforge.registry;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.common.content.builtin.BuiltinBeeSpecies;
import com.curiousbees.neoforge.item.BeeAnalyzerItem;
import com.curiousbees.neoforge.item.CuriousBeeSpawnEggDispenseBehavior;
import com.curiousbees.neoforge.item.CuriousBeeSpeciesSpawnEggItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.DispenserBlock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public final class ModItems {

    private ModItems() {}

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(Registries.ITEM, CuriousBeesMod.MOD_ID);

    public static final DeferredHolder<Item, BeeAnalyzerItem> BEE_ANALYZER =
            ITEMS.register("bee_analyzer",
                    () -> new BeeAnalyzerItem(new Item.Properties().stacksTo(1)));

    // --- Block items ---
    public static final DeferredHolder<Item, BlockItem> GENETIC_APIARY =
            ITEMS.register("genetic_apiary",
                    () -> new BlockItem(ModBlocks.GENETIC_APIARY.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> MEADOW_BEE_NEST =
            ITEMS.register("meadow_bee_nest",
                    () -> new BlockItem(ModBlocks.MEADOW_BEE_NEST.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> FOREST_BEE_NEST =
            ITEMS.register("forest_bee_nest",
                    () -> new BlockItem(ModBlocks.FOREST_BEE_NEST.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> ARID_BEE_NEST =
            ITEMS.register("arid_bee_nest",
                    () -> new BlockItem(ModBlocks.ARID_BEE_NEST.get(), new Item.Properties()));

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

    // --- Frame items (Phase 7F) ---
    public static final DeferredHolder<Item, Item> BASIC_FRAME =
            ITEMS.register("basic_frame", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> MUTATION_FRAME =
            ITEMS.register("mutation_frame", () -> new Item(new Item.Properties().stacksTo(1)));
    public static final DeferredHolder<Item, Item> PRODUCTIVITY_FRAME =
            ITEMS.register("productivity_frame", () -> new Item(new Item.Properties().stacksTo(1)));

    // --- Species spawn eggs (vanilla Bee + fixed genome; not SpawnEggItem to avoid BY_ID conflicts) ---
    public static final DeferredHolder<Item, CuriousBeeSpeciesSpawnEggItem> MEADOW_BEE_SPAWN_EGG =
            ITEMS.register("meadow_bee_spawn_egg",
                    () -> new CuriousBeeSpeciesSpawnEggItem(BuiltinBeeSpecies.MEADOW, 0xFFFFC41E, 0xFF422E2C,
                            new Item.Properties()));
    public static final DeferredHolder<Item, CuriousBeeSpeciesSpawnEggItem> FOREST_BEE_SPAWN_EGG =
            ITEMS.register("forest_bee_spawn_egg",
                    () -> new CuriousBeeSpeciesSpawnEggItem(BuiltinBeeSpecies.FOREST, 0xFF2D5A27, 0xFF3D2914,
                            new Item.Properties()));
    public static final DeferredHolder<Item, CuriousBeeSpeciesSpawnEggItem> ARID_BEE_SPAWN_EGG =
            ITEMS.register("arid_bee_spawn_egg",
                    () -> new CuriousBeeSpeciesSpawnEggItem(BuiltinBeeSpecies.ARID, 0xFFE8D4B8, 0xFF7B4F2D,
                            new Item.Properties()));
    public static final DeferredHolder<Item, CuriousBeeSpeciesSpawnEggItem> CULTIVATED_BEE_SPAWN_EGG =
            ITEMS.register("cultivated_bee_spawn_egg",
                    () -> new CuriousBeeSpeciesSpawnEggItem(BuiltinBeeSpecies.CULTIVATED, 0xFFFFF2A8, 0xFF8B6914,
                            new Item.Properties()));
    public static final DeferredHolder<Item, CuriousBeeSpeciesSpawnEggItem> HARDY_BEE_SPAWN_EGG =
            ITEMS.register("hardy_bee_spawn_egg",
                    () -> new CuriousBeeSpeciesSpawnEggItem(BuiltinBeeSpecies.HARDY, 0xFFC0C0C0, 0xFF36454F,
                            new Item.Properties()));

    public static final List<DeferredHolder<Item, CuriousBeeSpeciesSpawnEggItem>> BEE_SPAWN_EGGS = List.of(
            MEADOW_BEE_SPAWN_EGG,
            FOREST_BEE_SPAWN_EGG,
            ARID_BEE_SPAWN_EGG,
            CULTIVATED_BEE_SPAWN_EGG,
            HARDY_BEE_SPAWN_EGG);

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }

    /** Call from {@code FMLCommonSetupEvent#enqueueWork}. */
    public static void registerBeeSpawnEggDispenserBehaviors() {
        for (DeferredHolder<Item, CuriousBeeSpeciesSpawnEggItem> egg : BEE_SPAWN_EGGS) {
            DispenserBlock.registerBehavior(egg.get(), CuriousBeeSpawnEggDispenseBehavior.INSTANCE);
        }
    }
}
