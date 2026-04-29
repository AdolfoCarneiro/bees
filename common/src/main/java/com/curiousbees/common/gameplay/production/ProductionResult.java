package com.curiousbees.common.gameplay.production;

import java.util.List;
import java.util.Objects;

/** Result of one production roll for a single bee. */
public final class ProductionResult {

    private final String activeSpeciesId;
    private final String inactiveSpeciesId;
    private final String productivityAlleleId;
    private final List<ProductionOutput> generatedOutputs;

    public ProductionResult(String activeSpeciesId, String inactiveSpeciesId,
                            String productivityAlleleId,
                            List<ProductionOutput> generatedOutputs) {
        this.activeSpeciesId      = Objects.requireNonNull(activeSpeciesId,      "activeSpeciesId must not be null");
        this.inactiveSpeciesId    = Objects.requireNonNull(inactiveSpeciesId,    "inactiveSpeciesId must not be null");
        this.productivityAlleleId = Objects.requireNonNull(productivityAlleleId, "productivityAlleleId must not be null");
        this.generatedOutputs     = List.copyOf(
                Objects.requireNonNull(generatedOutputs, "generatedOutputs must not be null"));
    }

    public String activeSpeciesId()           { return activeSpeciesId; }
    public String inactiveSpeciesId()         { return inactiveSpeciesId; }
    public String productivityAlleleId()      { return productivityAlleleId; }
    public List<ProductionOutput> generatedOutputs() { return generatedOutputs; }
    public boolean hasOutput()                { return !generatedOutputs.isEmpty(); }
}
