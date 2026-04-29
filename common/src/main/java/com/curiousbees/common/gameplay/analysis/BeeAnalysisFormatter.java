package com.curiousbees.common.gameplay.analysis;

import com.curiousbees.common.genetics.model.Dominance;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Converts a BeeAnalysisReport into readable plain-text lines.
 * Platform-neutral: callers send these lines to chat, tooltips, or logs.
 */
public final class BeeAnalysisFormatter {

    private BeeAnalysisFormatter() {}

    /**
     * Formats a BeeAnalysisReport into a list of readable lines.
     *
     * Example output:
     *   === Bee Genetics ===
     *   Species:      [A] Cultivated (D) / [I] Forest (D)  — Hybrid
     *   Lifespan:     [A] Normal / [I] Long
     *   Productivity: [A] Fast / [I] Normal
     *   Fertility:    [A] Two / [I] Three
     *   Flower Type:  [A] Flowers / [I] Leaves
     */
    public static List<String> format(BeeAnalysisReport report) {
        Objects.requireNonNull(report, "report must not be null");
        List<String> lines = new ArrayList<>();

        lines.add("=== Bee Genetics ===");
        lines.add(speciesLine(report.species()));
        lines.add(traitLine("Lifespan",     report.lifespan()));
        lines.add(traitLine("Productivity", report.productivity()));
        lines.add(traitLine("Fertility",    report.fertility()));
        lines.add(traitLine("Flower Type",  report.flowerType()));

        return List.copyOf(lines);
    }

    private static String speciesLine(GeneReport gene) {
        String active   = displayName(gene.activeAlleleId());
        String inactive = displayName(gene.inactiveAlleleId());
        String dom      = dominanceTag(gene.activeDominance());
        String purity   = gene.isPurebred() ? "Purebred" : "Hybrid";
        return String.format("%-13s [A] %s %s / [I] %s  — %s",
                "Species:", active, dom, inactive, purity);
    }

    private static String traitLine(String label, GeneReport gene) {
        String active   = displayName(gene.activeAlleleId());
        String inactive = displayName(gene.inactiveAlleleId());
        return String.format("%-13s [A] %s / [I] %s",
                label + ":", active, inactive);
    }

    private static String dominanceTag(Dominance dominance) {
        return dominance == Dominance.DOMINANT ? "(D)" : "(R)";
    }

    /** Converts a stable allele ID like "curious_bees:species/cultivated" to "Cultivated". */
    static String displayName(String alleleId) {
        int slash = alleleId.lastIndexOf('/');
        String raw = slash >= 0 ? alleleId.substring(slash + 1) : alleleId;
        String spaced = raw.replace('_', ' ');
        if (spaced.isEmpty()) return spaced;
        return Character.toUpperCase(spaced.charAt(0)) + spaced.substring(1);
    }
}
