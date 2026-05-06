package com.curiousbees.neoforge.registry;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.common.content.builtin.BuiltinBeeSpecies;
import com.curiousbees.neoforge.block.AdvancedApiaryBlock;
import com.curiousbees.neoforge.block.ApiaryExtensionBlock;
import com.curiousbees.neoforge.block.GeneticApiaryBlock;
import com.curiousbees.neoforge.block.beenest.NestVariant;
import com.curiousbees.neoforge.block.beenest.SpeciesBeeNestBlock;
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

    public static final DeferredHolder<Block, AdvancedApiaryBlock> ADVANCED_APIARY =
            BLOCKS.register(
                    "advanced_apiary",
                    () -> new AdvancedApiaryBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEEHIVE)));

    public static final DeferredHolder<Block, ApiaryExtensionBlock> APIARY_EXTENSION =
            BLOCKS.register(
                    "apiary_extension",
                    () -> new ApiaryExtensionBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BEEHIVE)));

    // --- Species bee nest blocks ---
    // Each nest is a SpeciesBeeNestBlock parameterised by species ID and visual properties.
    // No per-species subclass needed — species identity lives in data.

    public static final DeferredHolder<Block, SpeciesBeeNestBlock> MEADOW_BEE_NEST =
            BLOCKS.register("meadow_bee_nest",
                    () -> new SpeciesBeeNestBlock(
                            BuiltinBeeSpecies.SPECIES_MEADOW.id(),
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.COLOR_YELLOW)
                                    .sound(SoundType.WOOD)
                                    .strength(0.6F)));

    public static final DeferredHolder<Block, SpeciesBeeNestBlock> FOREST_BEE_NEST =
            BLOCKS.register("forest_bee_nest",
                    () -> new SpeciesBeeNestBlock(
                            BuiltinBeeSpecies.SPECIES_FOREST.id(),
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.COLOR_GREEN)
                                    .sound(SoundType.WOOD)
                                    .strength(0.6F)));

    public static final DeferredHolder<Block, SpeciesBeeNestBlock> ARID_BEE_NEST =
            BLOCKS.register("arid_bee_nest",
                    () -> new SpeciesBeeNestBlock(
                            BuiltinBeeSpecies.SPECIES_ARID.id(),
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.COLOR_ORANGE)
                                    .sound(SoundType.WOOD)
                                    .strength(0.6F)));

    // LOG variant — forest bees that nest inside hollow tree trunks.
    // No new Java class; variant is declared here, visual form lives in JSON.
    public static final DeferredHolder<Block, SpeciesBeeNestBlock> FOREST_BEE_LOG_NEST =
            BLOCKS.register("forest_bee_log_nest",
                    () -> new SpeciesBeeNestBlock(
                            BuiltinBeeSpecies.SPECIES_FOREST.id(),
                            NestVariant.LOG,
                            BlockBehaviour.Properties.of()
                                    .mapColor(MapColor.COLOR_BROWN)
                                    .sound(SoundType.WOOD)
                                    .strength(0.6F)));

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
    }
}
