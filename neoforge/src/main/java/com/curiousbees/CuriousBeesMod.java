package com.curiousbees;

import com.curiousbees.neoforge.capability.ApiaryCapabilities;
import com.curiousbees.neoforge.client.ClientEventHandler;
import com.curiousbees.neoforge.command.CuriousBeesCommands;
import com.curiousbees.neoforge.content.ContentReloadListener;
import com.curiousbees.neoforge.event.BeeSpeciesHiveTargetHandler;
import com.curiousbees.neoforge.data.BeeGenomeAttachments;
import com.curiousbees.neoforge.network.CuriousBeesNetwork;
import com.curiousbees.neoforge.registry.ModBlockEntities;
import com.curiousbees.neoforge.registry.ModBlocks;
import com.curiousbees.neoforge.registry.ModCreativeTabs;
import com.curiousbees.neoforge.registry.ModFeatures;
import com.curiousbees.neoforge.registry.ModItems;
import com.curiousbees.neoforge.registry.ModMenuTypes;
import com.curiousbees.neoforge.registry.ModPoiTypes;
import com.mojang.logging.LogUtils;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(CuriousBeesMod.MOD_ID)
public final class CuriousBeesMod {
    public static final String MOD_ID = "curiousbees";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CuriousBeesMod(IEventBus modEventBus, ModContainer modContainer) {
        BeeGenomeAttachments.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModPoiTypes.register(modEventBus);
        ModItems.register(modEventBus);
        ModMenuTypes.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        ModFeatures.register(modEventBus);
        modEventBus.addListener((FMLCommonSetupEvent event) ->
                event.enqueueWork(ModItems::registerBeeSpawnEggDispenserBehaviors));
        modEventBus.addListener(ApiaryCapabilities::register);
        modEventBus.addListener(CuriousBeesNetwork::onRegisterPayloads);
        NeoForge.EVENT_BUS.addListener(ContentReloadListener::addReloadListener);
        NeoForge.EVENT_BUS.addListener(CuriousBeesCommands::register);
        NeoForge.EVENT_BUS.addListener(CuriousBeesNetwork::onStartTracking);
        NeoForge.EVENT_BUS.register(BeeSpeciesHiveTargetHandler.class);
        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(ClientEventHandler::onRegisterRenderers);
            modEventBus.addListener(ClientEventHandler::onRegisterMenuScreens);
            modEventBus.addListener(ClientEventHandler::onRegisterItemColors);
        }
        LOGGER.info("Curious Bees loaded");
    }
}
