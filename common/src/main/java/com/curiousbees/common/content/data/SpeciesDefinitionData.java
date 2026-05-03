package com.curiousbees.common.content.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Serializable data object representing a bee species definition.
 * Maps to one JSON file under data/curious_bees/species/<name>.json.
 * Does not contain gameplay logic.
 */
public final class SpeciesDefinitionData {

    private final String id;
    private final String displayName;

    /**
     * Dominance of the species allele itself (e.g. "DOMINANT", "RECESSIVE").
     * This becomes the dominance of the Allele used as the species chromosome.
     */
    private final String dominance;

    /**
     * Default trait allele pairs keyed by chromosomeType name (e.g. "LIFESPAN").
     * Each entry contains the [first, second] allele IDs for that chromosome slot.
     */
    private final Map<String, TraitAllelePairData> defaultTraits;

    /** Optional spawn context notes (biome tags, region hints). May be empty. */
    private final List<String> spawnContextNotes;

    /** Optional visual metadata. Null when not declared in JSON. */
    private final SpeciesVisualData visual;

    public SpeciesDefinitionData(String id, String displayName, String dominance,
                                  Map<String, TraitAllelePairData> defaultTraits,
                                  List<String> spawnContextNotes,
                                  SpeciesVisualData visual) {
        this.id = Objects.requireNonNull(id, "id must not be null");
        this.displayName = Objects.requireNonNull(displayName, "displayName must not be null");
        this.dominance = Objects.requireNonNull(dominance, "dominance must not be null");
        Objects.requireNonNull(defaultTraits, "defaultTraits must not be null");
        this.defaultTraits = Collections.unmodifiableMap(new HashMap<>(defaultTraits));
        this.spawnContextNotes = spawnContextNotes == null ? List.of() : List.copyOf(spawnContextNotes);
        this.visual = visual;
    }

    /** Convenience constructor: no spawn context notes, no visual. */
    public SpeciesDefinitionData(String id, String displayName, String dominance,
                                  Map<String, TraitAllelePairData> defaultTraits,
                                  List<String> spawnContextNotes) {
        this(id, displayName, dominance, defaultTraits, spawnContextNotes, null);
    }

    /** Convenience constructor: no spawn context notes, no visual. */
    public SpeciesDefinitionData(String id, String displayName, String dominance,
                                  Map<String, TraitAllelePairData> defaultTraits) {
        this(id, displayName, dominance, defaultTraits, null, null);
    }

    public String id() { return id; }
    public String displayName() { return displayName; }
    public String dominance() { return dominance; }
    public Map<String, TraitAllelePairData> defaultTraits() { return defaultTraits; }
    public List<String> spawnContextNotes() { return spawnContextNotes; }

    /** Visual metadata, or null if not declared. */
    public SpeciesVisualData visual() { return visual; }

    @Override
    public String toString() {
        return "SpeciesDefinitionData{id='" + id + "', displayName='" + displayName + "'}";
    }
}
