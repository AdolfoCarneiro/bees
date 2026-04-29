package com.curiousbees;

import com.curiousbees.neoforge.command.CuriousBeesCommands;
import com.curiousbees.neoforge.capability.ApiaryCapabilities;
import com.curiousbees.neoforge.data.BeeGenomeAttachments;
import com.curiousbees.neoforge.registry.ModBlockEntities;
import com.curiousbees.neoforge.registry.ModBlocks;
import com.curiousbees.neoforge.registry.ModItems;
import com.curiousbees.neoforge.registry.ModPoiTypes;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
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
        modEventBus.addListener(ApiaryCapabilities::register);
        NeoForge.EVENT_BUS.addListener(CuriousBeesCommands::register);
        LOGGER.info("Curious Bees loaded");
    }
}
