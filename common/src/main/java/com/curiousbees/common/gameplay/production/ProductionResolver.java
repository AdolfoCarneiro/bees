package com.curiousbees.common.gameplay.production;

import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.random.GeneticRandom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Rolls production outputs for a bee based on its Genome and available production definitions.
 * Pure Java; no Minecraft/NeoForge/Fabric dependencies.
 *
 * Rules:
 *   Active species controls primary outputs.
 *   Inactive species may contribute secondary outputs at reduced chance.
 *   Productivity allele modifies all chances via a multiplier.
 */
public final class ProductionResolver {

    private static final Logger LOGGER = Logger.getLogger(ProductionResolver.class.getName());

    /** Fraction applied to every secondary output chance from the inactive species. */
    static final double INACTIVE_SPECIES_MULTIPLIER = 0.15;

    public ProductionResult resolve(Genome genome,
                                    Map<String, ProductionDefinition> definitions,
                                    GeneticRandom random) {
        Objects.requireNonNull(genome,      "genome must not be null");
        Objects.requireNonNull(definitions, "definitions must not be null");
        Objects.requireNonNull(random,      "random must not be null");

        String activeSpeciesId      = genome.getActiveAllele(ChromosomeType.SPECIES).id();
        String inactiveSpeciesId    = genome.getInactiveAllele(ChromosomeType.SPECIES).id();
        String productivityAlleleId = genome.getActiveAllele(ChromosomeType.PRODUCTIVITY).id();

        double productivityMultiplier = ProductivityModifier.forAlleleId(productivityAlleleId);

        List<ProductionOutput> generated = new ArrayList<>();

        // Primary: active species
        Optional<ProductionDefinition> activeDef = Optional.ofNullable(definitions.get(activeSpeciesId));
        if (activeDef.isEmpty()) {
            LOGGER.warning("No production definition for active species '" + activeSpeciesId + "'.");
        } else {
            for (ProductionOutput output : activeDef.get().primaryOutputs()) {
                if (roll(output.chance(), productivityMultiplier, random)) {
                    generated.add(output);
                }
            }
        }

        // Secondary: inactive species (reduced chance)
        if (!inactiveSpeciesId.equals(activeSpeciesId)) {
            Optional<ProductionDefinition> inactiveDef = Optional.ofNullable(definitions.get(inactiveSpeciesId));
            if (inactiveDef.isPresent()) {
                for (ProductionOutput output : inactiveDef.get().primaryOutputs()) {
                    double reducedChance = output.chance() * INACTIVE_SPECIES_MULTIPLIER;
                    if (roll(reducedChance, productivityMultiplier, random)) {
                        generated.add(output);
                    }
                }
            }
        }

        return new ProductionResult(activeSpeciesId, inactiveSpeciesId, productivityAlleleId, generated);
    }

    private boolean roll(double baseChance, double multiplier, GeneticRandom random) {
        double finalChance = Math.min(1.0, Math.max(0.0, baseChance * multiplier));
        return random.nextDouble() < finalChance;
    }
}
