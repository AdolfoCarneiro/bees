package com.curiousbees.common.gameplay.analysis;

import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Dominance;

import java.util.Objects;

/**
 * Immutable analysis result for a single bee's genome, ready for formatting and display.
 * Use {@link #analyzed(GeneReport, GeneReport, GeneReport)} for full reports.
 * Use {@link #unknown()} for bees that have not been analyzed yet.
 */
public final class BeeAnalysisReport {

    /** Sentinel allele ID used in redacted (unknown) reports. */
    public static final String UNKNOWN_ID = "unknown";

    private final boolean analyzed;
    private final GeneReport species;
    private final GeneReport productivity;
    private final GeneReport flowerType;

    private BeeAnalysisReport(boolean analyzed,
                               GeneReport species,
                               GeneReport productivity,
                               GeneReport flowerType) {
        this.analyzed     = analyzed;
        this.species      = Objects.requireNonNull(species,      "species must not be null");
        this.productivity = Objects.requireNonNull(productivity, "productivity must not be null");
        this.flowerType   = Objects.requireNonNull(flowerType,   "flowerType must not be null");
    }

    /** Creates a full analyzed report. */
    public static BeeAnalysisReport analyzed(GeneReport species,
                                              GeneReport productivity,
                                              GeneReport flowerType) {
        return new BeeAnalysisReport(true, species, productivity, flowerType);
    }

    /** Creates a redacted "unknown" report for bees that have not been analyzed. */
    public static BeeAnalysisReport unknown() {
        return new BeeAnalysisReport(false,
                redactedGene(ChromosomeType.SPECIES),
                redactedGene(ChromosomeType.PRODUCTIVITY),
                redactedGene(ChromosomeType.FLOWER_TYPE));
    }

    private static GeneReport redactedGene(ChromosomeType type) {
        return new GeneReport(type, UNKNOWN_ID, UNKNOWN_ID, Dominance.DOMINANT, Dominance.DOMINANT, false);
    }

    /** True when this report contains real genetic data; false means all fields are redacted. */
    public boolean isAnalyzed()      { return analyzed; }
    public GeneReport species()      { return species; }
    public GeneReport productivity() { return productivity; }
    public GeneReport flowerType()   { return flowerType; }

    /** True when the bee's species chromosome is purebred. Always false for unknown reports. */
    public boolean isSpeciesPurebred() { return species.isPurebred(); }
}
