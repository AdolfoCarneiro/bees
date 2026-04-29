package com.curiousbees.common.content.data;

import java.util.List;
import java.util.Objects;

/**
 * Serializable data object representing a production definition.
 * Maps to one JSON file under data/curious_bees/production/<name>.json.
 * Does not contain gameplay logic.
 */
public final class ProductionDefinitionData {

    private final String speciesId;
    private final List<ProductionOutputData> primaryOutputs;
    private final List<ProductionOutputData> secondaryOutputs;

    public ProductionDefinitionData(String speciesId, List<ProductionOutputData> primaryOutputs,
                                     List<ProductionOutputData> secondaryOutputs) {
        this.speciesId = Objects.requireNonNull(speciesId, "speciesId must not be null");
        this.primaryOutputs = List.copyOf(
                Objects.requireNonNull(primaryOutputs, "primaryOutputs must not be null"));
        this.secondaryOutputs = secondaryOutputs == null ? List.of() : List.copyOf(secondaryOutputs);
    }

    /** Convenience constructor: no secondary outputs. */
    public ProductionDefinitionData(String speciesId, List<ProductionOutputData> primaryOutputs) {
        this(speciesId, primaryOutputs, null);
    }

    public String speciesId() { return speciesId; }
    public List<ProductionOutputData> primaryOutputs() { return primaryOutputs; }
    public List<ProductionOutputData> secondaryOutputs() { return secondaryOutputs; }

    @Override
    public String toString() {
        return "ProductionDefinitionData{speciesId='" + speciesId + "'}";
    }
}
