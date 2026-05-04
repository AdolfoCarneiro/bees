package com.curiousbees.neoforge.network;

import com.curiousbees.common.gameplay.analysis.BeeAnalysisReport;
import com.curiousbees.common.genetics.serial.GenomeData;
import com.curiousbees.neoforge.data.BeeGenomeAttachments;
import com.curiousbees.neoforge.data.BeeGenomeStorage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.animal.Bee;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * Registers Curious Bees network payloads and handles genome sync to clients.
 *
 * Genome data lives in a server-side data attachment and must be explicitly synced.
 * Two sync points:
 *   1. PlayerEvent.StartTracking — when a client starts seeing a bee (initial load, spawn).
 *   2. syncToTracking(bee) — called explicitly after an in-flight genome change (debug, breeding).
 */
public final class CuriousBeesNetwork {

    private CuriousBeesNetwork() {}

    public static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(
                SyncBeeGenomePayload.TYPE,
                SyncBeeGenomePayload.STREAM_CODEC,
                ClientNetworkHandlers::onSyncBeeGenome);
        registrar.playToClient(
                ShowAnalyzerReportPayload.TYPE,
                ShowAnalyzerReportPayload.STREAM_CODEC,
                ClientNetworkHandlers::onShowAnalyzerReport);
    }

    /** Sends the analyzer report to a specific player (after analysis). */
    public static void sendAnalyzerReport(ServerPlayer player, BeeAnalysisReport report) {
        PacketDistributor.sendToPlayer(player, new ShowAnalyzerReportPayload(report));
    }

    /** Called when a player begins tracking any entity — syncs genome if the entity is a bee with one. */
    public static void onStartTracking(PlayerEvent.StartTracking event) {
        if (!(event.getTarget() instanceof Bee bee)) return;
        if (!BeeGenomeStorage.hasGenome(bee)) return;
        if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;
        GenomeData data = bee.getData(BeeGenomeAttachments.BEE_GENOME);
        PacketDistributor.sendToPlayer(serverPlayer, new SyncBeeGenomePayload(bee.getId(), data));
    }

    /** Syncs the current genome to all players already tracking this bee. */
    public static void syncToTracking(Bee bee) {
        if (!BeeGenomeStorage.hasGenome(bee)) return;
        GenomeData data = bee.getData(BeeGenomeAttachments.BEE_GENOME);
        PacketDistributor.sendToPlayersTrackingEntity(bee, new SyncBeeGenomePayload(bee.getId(), data));
    }
}
