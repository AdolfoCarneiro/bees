package com.curiousbees.neoforge.network;

import com.curiousbees.CuriousBeesMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** Sent server→client to sync a bee's analyzed state after analysis or on entity tracking. */
public record SyncBeeAnalysisPayload(int entityId, boolean analyzed) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncBeeAnalysisPayload> TYPE =
            new CustomPacketPayload.Type<>(
                    ResourceLocation.fromNamespaceAndPath(CuriousBeesMod.MOD_ID, "sync_bee_analysis"));

    public static final StreamCodec<ByteBuf, SyncBeeAnalysisPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SyncBeeAnalysisPayload::entityId,
            ByteBufCodecs.BOOL, SyncBeeAnalysisPayload::analyzed,
            SyncBeeAnalysisPayload::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
