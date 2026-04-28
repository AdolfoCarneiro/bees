package com.curiousbees.common.genetics.mutation;

import com.curiousbees.common.genetics.model.Allele;
import com.curiousbees.common.genetics.model.ChromosomeType;

import java.util.Objects;

public final class MutationDefinition {

    private final String id;
    private final String parentSpeciesAId;
    private final String parentSpeciesBId;
    private final Allele resultSpeciesAllele;
    private final double baseChance;
    private final MutationResultMode resultMode;

    public MutationDefinition(String id, String parentSpeciesAId, String parentSpeciesBId,
                               Allele resultSpeciesAllele, double baseChance,
                               MutationResultMode resultMode) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(parentSpeciesAId, "parentSpeciesAId must not be null");
        Objects.requireNonNull(parentSpeciesBId, "parentSpeciesBId must not be null");
        Objects.requireNonNull(resultSpeciesAllele, "resultSpeciesAllele must not be null");
        Objects.requireNonNull(resultMode, "resultMode must not be null");
        if (resultSpeciesAllele.chromosomeType() != ChromosomeType.SPECIES) {
            throw new IllegalArgumentException(
                    "resultSpeciesAllele must be of ChromosomeType SPECIES, got: "
                    + resultSpeciesAllele.chromosomeType());
        }
        if (id.isBlank()) throw new IllegalArgumentException("id must not be blank");
        if (parentSpeciesAId.isBlank()) throw new IllegalArgumentException("parentSpeciesAId must not be blank");
        if (parentSpeciesBId.isBlank()) throw new IllegalArgumentException("parentSpeciesBId must not be blank");
        if (baseChance < 0.0 || baseChance > 1.0) {
            throw new IllegalArgumentException("baseChance must be between 0.0 and 1.0, got: " + baseChance);
        }
        this.id = id;
        this.parentSpeciesAId = parentSpeciesAId;
        this.parentSpeciesBId = parentSpeciesBId;
        this.resultSpeciesAllele = resultSpeciesAllele;
        this.baseChance = baseChance;
        this.resultMode = resultMode;
    }

    /** Returns true if the given pair of active species IDs matches this mutation (order-insensitive). */
    public boolean matches(String speciesAId, String speciesBId) {
        return (parentSpeciesAId.equals(speciesAId) && parentSpeciesBId.equals(speciesBId))
                || (parentSpeciesAId.equals(speciesBId) && parentSpeciesBId.equals(speciesAId));
    }

    public String id() { return id; }
    public String parentSpeciesAId() { return parentSpeciesAId; }
    public String parentSpeciesBId() { return parentSpeciesBId; }
    public Allele resultSpeciesAllele() { return resultSpeciesAllele; }
    public double baseChance() { return baseChance; }
    public MutationResultMode resultMode() { return resultMode; }

    @Override
    public String toString() {
        return "MutationDefinition{id='" + id + "', " + parentSpeciesAId + "+" + parentSpeciesBId
                + "->" + resultSpeciesAllele.id() + " @" + baseChance + " " + resultMode + "}";
    }
}
