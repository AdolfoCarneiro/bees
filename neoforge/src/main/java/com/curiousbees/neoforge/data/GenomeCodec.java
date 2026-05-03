package com.curiousbees.neoforge.data;

import com.curiousbees.common.genetics.serial.GenePairData;
import com.curiousbees.common.genetics.serial.GenomeData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.LinkedHashMap;
import java.util.Map;

/** NeoForge/DFU Codec and StreamCodec definitions for GenomeData. */
public final class GenomeCodec {

    private GenomeCodec() {}

    public static final Codec<GenePairData> GENE_PAIR = RecordCodecBuilder.create(i -> i.group(
            Codec.STRING.fieldOf("first").forGetter(GenePairData::firstAlleleId),
            Codec.STRING.fieldOf("second").forGetter(GenePairData::secondAlleleId),
            Codec.STRING.fieldOf("active").forGetter(GenePairData::activeAlleleId),
            Codec.STRING.fieldOf("inactive").forGetter(GenePairData::inactiveAlleleId)
    ).apply(i, GenePairData::new));

    public static final Codec<GenomeData> GENOME =
            Codec.unboundedMap(Codec.STRING, GENE_PAIR)
                 .xmap(GenomeData::new, GenomeData::chromosomes);

    public static final StreamCodec<ByteBuf, GenePairData> GENE_PAIR_STREAM = StreamCodec.composite(
            ByteBufCodecs.STRING_UTF8, GenePairData::firstAlleleId,
            ByteBufCodecs.STRING_UTF8, GenePairData::secondAlleleId,
            ByteBufCodecs.STRING_UTF8, GenePairData::activeAlleleId,
            ByteBufCodecs.STRING_UTF8, GenePairData::inactiveAlleleId,
            GenePairData::new);

    public static final StreamCodec<ByteBuf, GenomeData> GENOME_STREAM =
            ByteBufCodecs.map(LinkedHashMap::new, ByteBufCodecs.STRING_UTF8, GENE_PAIR_STREAM)
                         .map(GenomeData::new, m -> new LinkedHashMap<>(m.chromosomes()));
}
