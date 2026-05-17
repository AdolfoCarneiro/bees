package com.curiousbees.neoforge.datagen;

import com.curiousbees.neoforge.registry.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Set;

public final class CuriousBeesBlockLootTableProvider extends BlockLootSubProvider {

    protected CuriousBeesBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.GENETIC_APIARY.get());
        dropSelf(ModBlocks.ADVANCED_APIARY.get());
        dropSelf(ModBlocks.APIARY_EXTENSION.get());
        dropSelf(ModBlocks.CENTRIFUGE.get());
        dropSelf(ModBlocks.MEADOW_BEE_NEST.get());
        dropSelf(ModBlocks.FOREST_BEE_NEST.get());
        dropSelf(ModBlocks.ARID_BEE_NEST.get());
        dropSelf(ModBlocks.FOREST_BEE_LOG_NEST.get());
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return List.of(
            ModBlocks.GENETIC_APIARY.get(),
            ModBlocks.ADVANCED_APIARY.get(),
            ModBlocks.APIARY_EXTENSION.get(),
            ModBlocks.CENTRIFUGE.get(),
            ModBlocks.MEADOW_BEE_NEST.get(),
            ModBlocks.FOREST_BEE_NEST.get(),
            ModBlocks.ARID_BEE_NEST.get(),
            ModBlocks.FOREST_BEE_LOG_NEST.get()
        );
    }
}
