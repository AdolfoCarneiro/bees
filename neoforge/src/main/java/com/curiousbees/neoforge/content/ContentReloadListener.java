package com.curiousbees.neoforge.content;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.common.content.loading.ContentDefinitionSource;
import com.curiousbees.common.content.loading.ContentJsonLoader;
import com.curiousbees.common.content.loading.ContentLoadResult;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.neoforge.event.AddReloadListenerEvent;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class ContentReloadListener extends SimplePreparableReloadListener<ContentLoadResult> {

    private static final String TRAITS_PATH = "curious_bees/traits";
    private static final String SPECIES_PATH = "curious_bees/species";
    private static final String MUTATIONS_PATH = "curious_bees/mutations";
    private static final String PRODUCTION_PATH = "curious_bees/production";

    private static final ContentReloadListener INSTANCE = new ContentReloadListener();

    private ContentReloadListener() {}

    public static void addReloadListener(AddReloadListenerEvent event) {
        event.addListener(INSTANCE);
    }

    @Override
    protected ContentLoadResult prepare(ResourceManager resourceManager, ProfilerFiller profiler) {
        List<ContentDefinitionSource> traits = discover(resourceManager, TRAITS_PATH);
        List<ContentDefinitionSource> species = discover(resourceManager, SPECIES_PATH);
        List<ContentDefinitionSource> mutations = discover(resourceManager, MUTATIONS_PATH);
        List<ContentDefinitionSource> production = discover(resourceManager, PRODUCTION_PATH);

        return ContentJsonLoader.load(traits, species, mutations, production);
    }

    @Override
    protected void apply(ContentLoadResult result, ResourceManager resourceManager, ProfilerFiller profiler) {
        NeoForgeContentRegistry.apply(result);
        if (result.hasErrors()) {
            CuriousBeesMod.LOGGER.warn(
                    "Curious Bees content reload finished with errors. Built-ins remain available:\n{}",
                    result.combinedErrorMessage());
            return;
        }
        CuriousBeesMod.LOGGER.info(
                "Curious Bees content reload complete: {} species, {} traits, {} mutations, {} production definitions.",
                result.registry().allSpecies().size(),
                result.registry().allTraitAlleles().size(),
                result.registry().allMutations().size(),
                result.registry().allProductionDefinitions().size());
    }

    private static List<ContentDefinitionSource> discover(ResourceManager resourceManager, String path) {
        List<ContentDefinitionSource> sources = new ArrayList<>();
        Map<ResourceLocation, Resource> resources = resourceManager.listResources(
                path,
                location -> location.getPath().endsWith(".json"));

        for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {
            ResourceLocation location = entry.getKey();
            try (InputStream stream = entry.getValue().open()) {
                sources.add(new ContentDefinitionSource(
                        location.toString(),
                        new String(stream.readAllBytes(), StandardCharsets.UTF_8)));
            } catch (IOException e) {
                CuriousBeesMod.LOGGER.warn("Failed to read Curious Bees content file '{}'.", location, e);
            }
        }
        return sources;
    }
}
