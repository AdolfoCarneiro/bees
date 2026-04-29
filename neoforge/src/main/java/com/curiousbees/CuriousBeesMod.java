package com.curiousbees;

import com.curiousbees.neoforge.data.BeeGenomeAttachments;
import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(CuriousBeesMod.MOD_ID)
public final class CuriousBeesMod {
    public static final String MOD_ID = "curiousbees";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CuriousBeesMod(IEventBus modEventBus, ModContainer modContainer) {
        BeeGenomeAttachments.register(modEventBus);
        LOGGER.info("Curious Bees loaded");
    }
}
