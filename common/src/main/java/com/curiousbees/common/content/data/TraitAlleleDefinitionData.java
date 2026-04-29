package com.curiousbees.common.content.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Serializable data object representing a trait allele definition.
 * Maps to one JSON file under data/curious_bees/traits/<category>/<name>.json.
 * Does not contain gameplay logic.
 */
public final class TraitAlleleDefinitionData {

    private final String id;
    private final String chromosomeType;
    private final String displayName;
    private final String dominance;
    private final Map<String, Double> values;

    public TraitAlleleDefinitionData(String id, String chromosomeType, String displayName,
                                      String dominance, Map<String, Double> values) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.chromosomeType = Objects.requireNonNull(chromosomeType, "chromosomeType must not be null");
        this.displayName = Objects.requireNonNull(displayName, "displayName must not be null");
        this.dominance = Objects.requireNonNull(dominance, "dominance must not be null");
        this.values = values == null ? Collections.emptyMap()
                : Collections.unmodifiableMap(new HashMap<>(values));
    }

    /** Convenience constructor: no numeric values. */
    public TraitAlleleDefinitionData(String id, String chromosomeType, String displayName,
                                      String dominance) {
        this(id, chromosomeType, displayName, dominance, null);
    }

    public String id() { return id; }
    public String chromosomeType() { return chromosomeType; }
    public String displayName() { return displayName; }
    public String dominance() { return dominance; }

    /** Optional numeric values (e.g. multiplier). May be empty. */
    public Map<String, Double> values() { return values; }

    @Override
    public String toString() {
        return "TraitAlleleDefinitionData{id='" + id + "', type=" + chromosomeType
                + ", dominance=" + dominance + "}";
    }
}
