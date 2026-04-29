package com.curiousbees.common.content.data;

import java.util.Objects;

/**
 * Serializable data object representing a mutation definition.
 * Maps to one JSON file under data/curious_bees/mutations/<name>.json.
 * Does not contain gameplay logic.
 */
public final class MutationDefinitionData {

    private final String id;
    private final String parentSpeciesAId;
    private final String parentSpeciesBId;

    /** ID of the species allele produced by this mutation (the result species ID). */
    private final String resultSpeciesId;

    private final double baseChance;
    private final MutationResultModesData resultModes;

    public MutationDefinitionData(String id, String parentSpeciesAId, String parentSpeciesBId,
                                   String resultSpeciesId, double baseChance,
                                   MutationResultModesData resultModes) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.parentSpeciesAId = Objects.requireNonNull(parentSpeciesAId, "parentSpeciesAId must not be null");
        this.parentSpeciesBId = Objects.requireNonNull(parentSpeciesBId, "parentSpeciesBId must not be null");
        this.resultSpeciesId = Objects.requireNonNull(resultSpeciesId, "resultSpeciesId must not be null");
        this.baseChance = baseChance;
        this.resultModes = Objects.requireNonNull(resultModes, "resultModes must not be null");
    }

    public String id() { return id; }
    public String parentSpeciesAId() { return parentSpeciesAId; }
    public String parentSpeciesBId() { return parentSpeciesBId; }
    public String resultSpeciesId() { return resultSpeciesId; }
    public double baseChance() { return baseChance; }
    public MutationResultModesData resultModes() { return resultModes; }

    @Override
    public String toString() {
        return "MutationDefinitionData{id='" + id + "', " + parentSpeciesAId + "+"
                + parentSpeciesBId + "->" + resultSpeciesId + " @" + baseChance + "}";
    }
}
