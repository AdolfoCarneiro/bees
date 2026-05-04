package com.curiousbees.neoforge.network;

import com.curiousbees.neoforge.client.screen.BeeAnalyzerScreen;
import com.curiousbees.neoforge.data.BeeGenomeAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.Bee;
import net.neoforged.neoforge.network.handling.IPayloadContext;

/** Client-side handlers for Curious Bees network payloads. */
public final class ClientNetworkHandlers {

    private ClientNetworkHandlers() {}

    public static void onSyncBeeGenome(SyncBeeGenomePayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            var level = Minecraft.getInstance().level;
            if (level == null) return;
            Entity entity = level.getEntity(payload.entityId());
            if (entity instanceof Bee bee) {
                bee.setData(BeeGenomeAttachments.BEE_GENOME, payload.genomeData());
            }
        });
    }

    public static void onShowAnalyzerReport(ShowAnalyzerReportPayload payload, IPayloadContext ctx) {
        ctx.enqueueWork(() ->
                Minecraft.getInstance().setScreen(new BeeAnalyzerScreen(payload.report())));
    }
}
