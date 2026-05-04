package com.curiousbees.common.gameplay.analysis;

import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Dominance;

import java.util.Objects;

/**
 * Immutable analysis result for a single bee's genome, ready for formatting and display.
 * Use {@link #analyzed(GeneReport, GeneReport, GeneReport, GeneReport, GeneReport)} for full reports.
 * Use {@link #unknown()} for bees that have not been analyzed yet.
 */
public final class BeeAnalysisReport {

    /** Sentinel allele ID used in redacted (unknown) reports. */
    public static final String UNKNOWN_ID = "unknown";

    private final boolean analyzed;
    private final GeneReport species;
    private final GeneReport lifespan;
    private final GeneReport productivity;
    private final GeneReport fertility;
    private final GeneReport flowerType;

    private BeeAnalysisReport(boolean analyzed,
                               GeneReport species, GeneReport lifespan,
                               GeneReport productivity, GeneReport fertility,
                               GeneReport flowerType) {
        this.analyzed     = analyzed;
        this.species      = Objects.requireNonNull(species,      "species must not be null");
        this.lifespan     = Objects.requireNonNull(lifespan,     "lifespan must not be null");
        this.productivity = Objects.requireNonNull(productivity, "productivity must not be null");
        this.fertility    = Objects.requireNonNull(fertility,    "fertility must not be null");
        this.flowerType   = Objects.requireNonNull(flowerType,   "flowerType must not be null");
    }

    /** Creates a full analyzed report. */
    public static BeeAnalysisReport analyzed(GeneReport species, GeneReport lifespan,
                                              GeneReport productivity, GeneReport fertility,
                                              GeneReport flowerType) {
        return new BeeAnalysisReport(true, species, lifespan, productivity, fertility, flowerType);
    }

    /** Creates a redacted "unknown" report for bees that have not been analyzed. */
    public static BeeAnalysisReport unknown() {
        GeneReport redacted = redactedGene(ChromosomeType.SPECIES);
        return new BeeAnalysisReport(false,
                redactedGene(ChromosomeType.SPECIES),
                redactedGene(ChromosomeType.LIFESPAN),
                redactedGene(ChromosomeType.PRODUCTIVITY),
                redactedGene(ChromosomeType.FERTILITY),
                redactedGene(ChromosomeType.FLOWER_TYPE));
    }

    private static GeneReport redactedGene(ChromosomeType type) {
        return new GeneReport(type, UNKNOWN_ID, UNKNOWN_ID, Dominance.DOMINANT, Dominance.DOMINANT, false);
    }

    /** True when this report contains real genetic data; false means all fields are redacted. */
    public boolean isAnalyzed()      { return analyzed; }
    public GeneReport species()      { return species; }
    public GeneReport lifespan()     { return lifespan; }
    public GeneReport productivity() { return productivity; }
    public GeneReport fertility()    { return fertility; }
    public GeneReport flowerType()   { return flowerType; }

    /** True when the bee's species chromosome is purebred. Always false for unknown reports. */
    public boolean isSpeciesPurebred() { return species.isPurebred(); }
}
