package com.curiousbees.neoforge.datagen;

import com.curiousbees.CuriousBeesMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.PoiTypeTags;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public final class CuriousBeesPoiTagProvider extends TagsProvider<PoiType> {

    public CuriousBeesPoiTagProvider(PackOutput output,
                                      CompletableFuture<HolderLookup.Provider> lookupProvider,
                                      @Nullable ExistingFileHelper efh) {
        super(output, Registries.POINT_OF_INTEREST_TYPE, lookupProvider, CuriousBeesMod.MOD_ID, efh);
    }

    @Override
    protected void addTags(HolderLookup.Provider lookupProvider) {
        tag(PoiTypeTags.BEE_HOME)
            .addOptional(ResourceLocation.fromNamespaceAndPath(CuriousBeesMod.MOD_ID, "genetic_apiary"))
            .addOptional(ResourceLocation.fromNamespaceAndPath(CuriousBeesMod.MOD_ID, "meadow_bee_nest"))
            .addOptional(ResourceLocation.fromNamespaceAndPath(CuriousBeesMod.MOD_ID, "forest_bee_nest"))
            .addOptional(ResourceLocation.fromNamespaceAndPath(CuriousBeesMod.MOD_ID, "forest_bee_log_nest"))
            .addOptional(ResourceLocation.fromNamespaceAndPath(CuriousBeesMod.MOD_ID, "arid_bee_nest"));
    }
}
