package com.curiousbees.common.gameplay.production;

import java.util.List;
import java.util.Objects;

/**
 * Defines what a species can produce.
 * Pure Java; no Minecraft item references.
 */
public final class ProductionDefinition {

    private final String speciesId;
    private final List<ProductionOutput> primaryOutputs;
    private final List<ProductionOutput> secondaryOutputs;

    public ProductionDefinition(String speciesId,
                                List<ProductionOutput> primaryOutputs,
                                List<ProductionOutput> secondaryOutputs) {
        if (speciesId == null || speciesId.isBlank()) {
            throw new IllegalArgumentException("speciesId must not be null or blank");
        }
        this.speciesId        = speciesId;
        this.primaryOutputs   = List.copyOf(
                Objects.requireNonNull(primaryOutputs, "primaryOutputs must not be null"));
        this.secondaryOutputs = List.copyOf(
                Objects.requireNonNull(secondaryOutputs, "secondaryOutputs must not be null"));
    }

    /** Convenience: no secondary outputs. */
    public ProductionDefinition(String speciesId, List<ProductionOutput> primaryOutputs) {
        this(speciesId, primaryOutputs, List.of());
    }

    public String speciesId()                       { return speciesId; }
    public List<ProductionOutput> primaryOutputs()  { return primaryOutputs; }
    public List<ProductionOutput> secondaryOutputs(){ return secondaryOutputs; }
}
