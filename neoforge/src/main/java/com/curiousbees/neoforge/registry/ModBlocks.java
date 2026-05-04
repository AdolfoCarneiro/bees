package com.curiousbees.neoforge.registry;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.neoforge.block.GeneticApiaryBlock;
import com.curiousbees.neoforge.block.hive.AridHiveBlock;
import com.curiousbees.neoforge.block.hive.ForestHiveBlock;
import com.curiousbees.neoforge.block.hive.MeadowHiveBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlocks {

    private ModBlocks() {}

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(Registries.BLOCK, CuriousBeesMod.MOD_ID);

    public static final DeferredHolder<Block, GeneticApiaryBlock> GENETIC_APIARY =
            BLOCKS.register("genetic_apiary",
                    () -> new GeneticApiaryBlock(
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.WOOD)
                                    .sound(SoundType.WOOD)
                                    .strength(0.6F)));

    // --- Species Hive Blocks ---

    public static final DeferredHolder<Block, MeadowHiveBlock> MEADOW_HIVE =
            BLOCKS.register("meadow_hive",
                    () -> new MeadowHiveBlock(
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.COLOR_YELLOW)
                                    .sound(SoundType.WOOD)
                                    .strength(0.6F)));

    public static final DeferredHolder<Block, ForestHiveBlock> FOREST_HIVE =
            BLOCKS.register("forest_hive",
                    () -> new ForestHiveBlock(
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.COLOR_GREEN)
                                    .sound(SoundType.WOOD)
                                    .strength(0.6F)));

    public static final DeferredHolder<Block, AridHiveBlock> ARID_HIVE =
            BLOCKS.register("arid_hive",
                    () -> new AridHiveBlock(
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.COLOR_ORANGE)
                                    .sound(SoundType.WOOD)
                                    .strength(0.6F)));

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
