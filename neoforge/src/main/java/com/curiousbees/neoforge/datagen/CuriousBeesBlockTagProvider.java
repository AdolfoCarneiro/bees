package com.curiousbees.neoforge.datagen;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.neoforge.registry.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public final class CuriousBeesBlockTagProvider extends BlockTagsProvider {

    public CuriousBeesBlockTagProvider(PackOutput output,
                                        CompletableFuture<HolderLookup.Provider> lookupProvider,
                                        @Nullable ExistingFileHelper efh) {
        super(output, lookupProvider, CuriousBeesMod.MOD_ID, efh);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        tag(BlockTags.BEEHIVES)
            .add(ModBlocks.GENETIC_APIARY.get())
            .add(ModBlocks.MEADOW_BEE_NEST.get())
            .add(ModBlocks.FOREST_BEE_NEST.get())
            .add(ModBlocks.ARID_BEE_NEST.get());
    }
}
