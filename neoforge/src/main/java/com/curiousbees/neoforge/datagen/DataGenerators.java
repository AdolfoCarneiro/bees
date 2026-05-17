package com.curiousbees.neoforge.datagen;

import com.curiousbees.CuriousBeesMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = CuriousBeesMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class DataGenerators {

    private DataGenerators() {}

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        ExistingFileHelper efh = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        gen.addProvider(event.includeServer(), new LootTableProvider(output, Set.of(),
            List.of(new LootTableProvider.SubProviderEntry(
                CuriousBeesBlockLootTableProvider::new, LootContextParamSets.BLOCK)),
            lookupProvider));

        gen.addProvider(event.includeServer(), new CuriousBeesRecipeProvider(output, lookupProvider));

        CuriousBeesBlockTagProvider blockTags = new CuriousBeesBlockTagProvider(output, lookupProvider, efh);
        gen.addProvider(event.includeServer(), blockTags);
        gen.addProvider(event.includeServer(), new CuriousBeesItemTagProvider(output, lookupProvider, blockTags, efh));
        gen.addProvider(event.includeServer(), new CuriousBeesPoiTagProvider(output, lookupProvider, efh));
    }
}
