package com.curiousbees.neoforge.data;

import com.curiousbees.common.genetics.serial.GenomeData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record CapturedBeeData(GenomeData genome, boolean analyzed) {

    public static final Codec<CapturedBeeData> CODEC = RecordCodecBuilder.create(i -> i.group(
            GenomeCodec.GENOME.fieldOf("genome").forGetter(CapturedBeeData::genome),
            Codec.BOOL.fieldOf("analyzed").forGetter(CapturedBeeData::analyzed)
    ).apply(i, CapturedBeeData::new));

    public static final StreamCodec<ByteBuf, CapturedBeeData> STREAM_CODEC = StreamCodec.composite(
            GenomeCodec.GENOME_STREAM, CapturedBeeData::genome,
            ByteBufCodecs.BOOL, CapturedBeeData::analyzed,
            CapturedBeeData::new);
}
