package com.curiousbees.common.gameplay.analysis;

import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.GenePair;
import com.curiousbees.common.genetics.model.Genome;

import java.util.Objects;

/** Converts a Genome into a BeeAnalysisReport. Pure Java, no Minecraft dependencies. */
public final class BeeAnalysisService {

    public BeeAnalysisReport analyze(Genome genome) {
        Objects.requireNonNull(genome, "genome must not be null");
        return BeeAnalysisReport.analyzed(
                toGeneReport(genome, ChromosomeType.SPECIES),
                toGeneReport(genome, ChromosomeType.LIFESPAN),
                toGeneReport(genome, ChromosomeType.PRODUCTIVITY),
                toGeneReport(genome, ChromosomeType.FERTILITY),
                toGeneReport(genome, ChromosomeType.FLOWER_TYPE));
    }

    private GeneReport toGeneReport(Genome genome, ChromosomeType type) {
        GenePair pair = genome.getGenePair(type);
        return new GeneReport(
                type,
                pair.active().id(),
                pair.inactive().id(),
                pair.active().dominance(),
                pair.inactive().dominance(),
                pair.isPurebred());
    }
}
