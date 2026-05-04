package com.curiousbees.neoforge.registry;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.neoforge.block.GeneticApiaryBlock;
import com.curiousbees.neoforge.block.beenest.AridBeeNestBlock;
import com.curiousbees.neoforge.block.beenest.ForestBeeNestBlock;
import com.curiousbees.neoforge.block.beenest.MeadowBeeNestBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
            BLOCKS.register(
                    "genetic_apiary",
                    () -> new GeneticApiaryBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEEHIVE)));

    // --- Species bee nest blocks ---

    public static final DeferredHolder<Block, MeadowBeeNestBlock> MEADOW_BEE_NEST =
            BLOCKS.register("meadow_bee_nest",
                    () -> new MeadowBeeNestBlock(
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.COLOR_YELLOW)
                                    .sound(SoundType.WOOD)
                                    .strength(0.6F)));

    public static final DeferredHolder<Block, ForestBeeNestBlock> FOREST_BEE_NEST =
            BLOCKS.register("forest_bee_nest",
                    () -> new ForestBeeNestBlock(
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.COLOR_GREEN)
                                    .sound(SoundType.WOOD)
                                    .strength(0.6F)));

    public static final DeferredHolder<Block, AridBeeNestBlock> ARID_BEE_NEST =
            BLOCKS.register("arid_bee_nest",
                    () -> new AridBeeNestBlock(
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.COLOR_ORANGE)
                                    .sound(SoundType.WOOD)
                                    .strength(0.6F)));

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
