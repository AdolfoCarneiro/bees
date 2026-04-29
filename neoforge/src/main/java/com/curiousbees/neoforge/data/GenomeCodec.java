package com.curiousbees.neoforge.data;

import com.curiousbees.common.genetics.serial.GenePairData;
import com.curiousbees.common.genetics.serial.GenomeData;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Map;

/** NeoForge/DFU Codec definitions for persisting GenomeData via AttachmentType. */
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
}
