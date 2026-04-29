package com.curiousbees.common.gameplay.analysis;

import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Dominance;

import java.util.Objects;

/** Immutable snapshot of one chromosome's active/inactive state for display purposes. */
public final class GeneReport {

    private final ChromosomeType chromosomeType;
    private final String activeAlleleId;
    private final String inactiveAlleleId;
    private final Dominance activeDominance;
    private final Dominance inactiveDominance;
    private final boolean purebred;

    public GeneReport(ChromosomeType chromosomeType,
                      String activeAlleleId, String inactiveAlleleId,
                      Dominance activeDominance, Dominance inactiveDominance,
                      boolean purebred) {
        this.chromosomeType   = Objects.requireNonNull(chromosomeType,   "chromosomeType must not be null");
        this.activeAlleleId   = Objects.requireNonNull(activeAlleleId,   "activeAlleleId must not be null");
        this.inactiveAlleleId = Objects.requireNonNull(inactiveAlleleId, "inactiveAlleleId must not be null");
        this.activeDominance  = Objects.requireNonNull(activeDominance,  "activeDominance must not be null");
        this.inactiveDominance = Objects.requireNonNull(inactiveDominance, "inactiveDominance must not be null");
        this.purebred = purebred;
    }

    public ChromosomeType chromosomeType()   { return chromosomeType; }
    public String activeAlleleId()           { return activeAlleleId; }
    public String inactiveAlleleId()         { return inactiveAlleleId; }
    public Dominance activeDominance()       { return activeDominance; }
    public Dominance inactiveDominance()     { return inactiveDominance; }
    public boolean isPurebred()              { return purebred; }
}
