package com.curiousbees.neoforge.network;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.common.genetics.serial.GenomeData;
import com.curiousbees.neoforge.data.GenomeCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Sent from server to client to sync a bee entity's genome.
 * Identified by entity numeric ID so the client can look it up directly.
 */
public record SyncBeeGenomePayload(int entityId, GenomeData genomeData) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<SyncBeeGenomePayload> TYPE =
            new CustomPacketPayload.Type<>(
                    ResourceLocation.fromNamespaceAndPath(CuriousBeesMod.MOD_ID, "sync_bee_genome"));

    public static final StreamCodec<ByteBuf, SyncBeeGenomePayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, SyncBeeGenomePayload::entityId,
            GenomeCodec.GENOME_STREAM, SyncBeeGenomePayload::genomeData,
            SyncBeeGenomePayload::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
