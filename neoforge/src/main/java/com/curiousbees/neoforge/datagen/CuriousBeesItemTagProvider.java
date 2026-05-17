package com.curiousbees.neoforge.datagen;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.neoforge.registry.ModItems;
import com.curiousbees.neoforge.registry.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public final class CuriousBeesItemTagProvider extends ItemTagsProvider {

    public CuriousBeesItemTagProvider(PackOutput output,
                                       CompletableFuture<HolderLookup.Provider> lookupProvider,
                                       BlockTagsProvider blockTagProvider,
                                       @Nullable ExistingFileHelper efh) {
        super(output, lookupProvider, blockTagProvider.contentsGetter(), CuriousBeesMod.MOD_ID, efh);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        tag(ModTags.Items.FRAMES)
            .add(ModItems.BASIC_FRAME.get())
            .add(ModItems.MUTATION_FRAME.get())
            .add(ModItems.PRODUCTIVITY_FRAME.get());

        tag(ModTags.Items.BEEHIVE_UPGRADES);
        tag(ModTags.Items.CENTRIFUGE_UPGRADES);
    }
}
