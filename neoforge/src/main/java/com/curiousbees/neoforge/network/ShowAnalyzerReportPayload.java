package com.curiousbees.neoforge.network;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.common.gameplay.analysis.BeeAnalysisReport;
import com.curiousbees.common.gameplay.analysis.GeneReport;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Dominance;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/**
 * Sent server → client after a successful bee analysis.
 * The client opens {@link com.curiousbees.neoforge.client.screen.BeeAnalyzerScreen} with this data.
 */
public record ShowAnalyzerReportPayload(BeeAnalysisReport report) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ShowAnalyzerReportPayload> TYPE =
            new CustomPacketPayload.Type<>(
                    ResourceLocation.fromNamespaceAndPath(CuriousBeesMod.MOD_ID, "show_analyzer_report"));

    // --- StreamCodec helpers ---

    private static final StreamCodec<ByteBuf, Dominance> DOMINANCE_CODEC =
            ByteBufCodecs.INT.map(i -> Dominance.values()[i], Enum::ordinal);

    private static final StreamCodec<ByteBuf, ChromosomeType> CHROMOSOME_CODEC =
            ByteBufCodecs.INT.map(i -> ChromosomeType.values()[i], Enum::ordinal);

    private static final StreamCodec<ByteBuf, GeneReport> GENE_REPORT_CODEC =
            StreamCodec.composite(
                    CHROMOSOME_CODEC,        GeneReport::chromosomeType,
                    ByteBufCodecs.STRING_UTF8, GeneReport::activeAlleleId,
                    ByteBufCodecs.STRING_UTF8, GeneReport::inactiveAlleleId,
                    DOMINANCE_CODEC,           GeneReport::activeDominance,
                    DOMINANCE_CODEC,           GeneReport::inactiveDominance,
                    ByteBufCodecs.BOOL,        GeneReport::isPurebred,
                    GeneReport::new);

    private static final StreamCodec<ByteBuf, BeeAnalysisReport> REPORT_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.BOOL,  BeeAnalysisReport::isAnalyzed,
                    GENE_REPORT_CODEC,   BeeAnalysisReport::species,
                    GENE_REPORT_CODEC,   BeeAnalysisReport::lifespan,
                    GENE_REPORT_CODEC,   BeeAnalysisReport::productivity,
                    GENE_REPORT_CODEC,   BeeAnalysisReport::fertility,
                    GENE_REPORT_CODEC,   BeeAnalysisReport::flowerType,
                    (analyzed, species, lifespan, productivity, fertility, flowerType) ->
                            analyzed
                                    ? BeeAnalysisReport.analyzed(species, lifespan, productivity, fertility, flowerType)
                                    : BeeAnalysisReport.unknown());

    public static final StreamCodec<ByteBuf, ShowAnalyzerReportPayload> STREAM_CODEC =
            StreamCodec.composite(
                    REPORT_CODEC, ShowAnalyzerReportPayload::report,
                    ShowAnalyzerReportPayload::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
