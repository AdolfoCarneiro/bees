package com.curiousbees.common.gameplay.analysis;

import java.util.Objects;

/** Immutable analysis result for a single bee's genome, ready for formatting and display. */
public final class BeeAnalysisReport {

    private final GeneReport species;
    private final GeneReport lifespan;
    private final GeneReport productivity;
    private final GeneReport fertility;
    private final GeneReport flowerType;

    public BeeAnalysisReport(GeneReport species, GeneReport lifespan,
                             GeneReport productivity, GeneReport fertility,
                             GeneReport flowerType) {
        this.species      = Objects.requireNonNull(species,      "species must not be null");
        this.lifespan     = Objects.requireNonNull(lifespan,     "lifespan must not be null");
        this.productivity = Objects.requireNonNull(productivity, "productivity must not be null");
        this.fertility    = Objects.requireNonNull(fertility,    "fertility must not be null");
        this.flowerType   = Objects.requireNonNull(flowerType,   "flowerType must not be null");
    }

    public GeneReport species()      { return species; }
    public GeneReport lifespan()     { return lifespan; }
    public GeneReport productivity() { return productivity; }
    public GeneReport fertility()    { return fertility; }
    public GeneReport flowerType()   { return flowerType; }

    /** True when the bee's species chromosome is purebred. */
    public boolean isSpeciesPurebred() { return species.isPurebred(); }
}
