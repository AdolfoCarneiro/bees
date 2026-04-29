package com.curiousbees.common.gameplay.analysis;

import com.curiousbees.common.genetics.model.Allele;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.GenePair;
import com.curiousbees.common.genetics.model.Genome;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Produces a human-readable genome report as a list of text lines.
 * Platform-neutral: callers render the lines however they like (chat, log, tooltip, etc.).
 */
public final class GenomeReport {

    private GenomeReport() {}

    /**
     * Generates a readable multi-line report for the given genome.
     * Each line is a plain string with no platform-specific formatting.
     */
    public static List<String> generate(Genome genome) {
        Objects.requireNonNull(genome, "genome must not be null");
        List<String> lines = new ArrayList<>();

        lines.add("=== Bee Genome ===");
        lines.add(pairLine("Species",      genome.species()));
        lines.add(purityLine(genome.species()));

        if (genome.hasChromosome(ChromosomeType.LIFESPAN)) {
            lines.add(pairLine("Lifespan",     genome.getGenePair(ChromosomeType.LIFESPAN)));
        }
        if (genome.hasChromosome(ChromosomeType.PRODUCTIVITY)) {
            lines.add(pairLine("Productivity", genome.getGenePair(ChromosomeType.PRODUCTIVITY)));
        }
        if (genome.hasChromosome(ChromosomeType.FERTILITY)) {
            lines.add(pairLine("Fertility",    genome.getGenePair(ChromosomeType.FERTILITY)));
        }
        if (genome.hasChromosome(ChromosomeType.FLOWER_TYPE)) {
            lines.add(pairLine("Flower Type",  genome.getGenePair(ChromosomeType.FLOWER_TYPE)));
        }

        return List.copyOf(lines);
    }

    private static String pairLine(String label, GenePair pair) {
        return label + ": " + display(pair.active()) + " / " + display(pair.inactive());
    }

    private static String purityLine(GenePair speciesPair) {
        return "Purity: " + (speciesPair.isPurebred() ? "Purebred" : "Hybrid");
    }

    /** Extracts a readable name from a stable allele ID. */
    private static String display(Allele allele) {
        String id = allele.id();
        int slash = id.lastIndexOf('/');
        String raw = slash >= 0 ? id.substring(slash + 1) : id;
        // capitalize first letter, replace underscores with spaces
        String spaced = raw.replace('_', ' ');
        if (spaced.isEmpty()) return spaced;
        return Character.toUpperCase(spaced.charAt(0)) + spaced.substring(1);
    }
}
