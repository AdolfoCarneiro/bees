package com.curiousbees.common.content.species;

import com.curiousbees.common.content.visual.SpeciesVisualDefinition;
import com.curiousbees.common.genetics.model.Allele;
import com.curiousbees.common.genetics.model.ChromosomeType;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Immutable definition of a bee species.
 * Contains species identity metadata and default trait allele pairs for genome creation.
 * Minecraft-independent: uses stable string IDs only.
 */
public final class BeeSpeciesDefinition {

    private static final Set<ChromosomeType> REQUIRED_TRAIT_TYPES = EnumSet.of(
            ChromosomeType.LIFESPAN,
            ChromosomeType.PRODUCTIVITY,
            ChromosomeType.FERTILITY,
            ChromosomeType.FLOWER_TYPE);

    private final String id;
    private final String displayName;
    private final Allele speciesAllele;
    private final Map<ChromosomeType, Allele[]> defaultTraitAlleles;
    private final List<String> spawnContextNotes;
    private final SpeciesVisualDefinition visualDefinition;

    /**
     * Full constructor including optional visual metadata.
     *
     * @param defaultTraitAlleles map from trait ChromosomeType to a two-element array [first, second].
     *                            Must contain LIFESPAN, PRODUCTIVITY, FERTILITY, and FLOWER_TYPE.
     *                            Must NOT contain SPECIES — species is set via speciesAllele.
     * @param visualDefinition    optional visual metadata; null if not yet defined.
     */
    public BeeSpeciesDefinition(String id, String displayName, Allele speciesAllele,
                                 Map<ChromosomeType, Allele[]> defaultTraitAlleles,
                                 List<String> spawnContextNotes,
                                 SpeciesVisualDefinition visualDefinition) {
        Objects.requireNonNull(id, "id must not be null");
        Objects.requireNonNull(displayName, "displayName must not be null");
        Objects.requireNonNull(speciesAllele, "speciesAllele must not be null");
        Objects.requireNonNull(defaultTraitAlleles, "defaultTraitAlleles must not be null");
        Objects.requireNonNull(spawnContextNotes, "spawnContextNotes must not be null");

        if (id.isBlank()) throw new IllegalArgumentException("id must not be blank");
        if (displayName.isBlank()) throw new IllegalArgumentException("displayName must not be blank");
        if (speciesAllele.chromosomeType() != ChromosomeType.SPECIES) {
            throw new IllegalArgumentException(
                    "speciesAllele must be ChromosomeType.SPECIES, got: " + speciesAllele.chromosomeType());
        }

        validateTraitAlleles(defaultTraitAlleles);

        this.id = id;
        this.displayName = displayName;
        this.speciesAllele = speciesAllele;
        this.defaultTraitAlleles = copyTraitAlleles(defaultTraitAlleles);
        this.spawnContextNotes = List.copyOf(spawnContextNotes);
        this.visualDefinition = visualDefinition;
    }

    /** Convenience constructor without visual metadata. */
    public BeeSpeciesDefinition(String id, String displayName, Allele speciesAllele,
                                 Map<ChromosomeType, Allele[]> defaultTraitAlleles,
                                 List<String> spawnContextNotes) {
        this(id, displayName, speciesAllele, defaultTraitAlleles, spawnContextNotes, null);
    }

    private static void validateTraitAlleles(Map<ChromosomeType, Allele[]> traits) {
        for (ChromosomeType required : REQUIRED_TRAIT_TYPES) {
            Allele[] pair = traits.get(required);
            if (pair == null) {
                throw new IllegalArgumentException("defaultTraitAlleles is missing chromosome: " + required);
            }
            if (pair.length != 2) {
                throw new IllegalArgumentException(
                        "defaultTraitAlleles[" + required + "] must have exactly 2 alleles, got: " + pair.length);
            }
            Objects.requireNonNull(pair[0], "first allele must not be null for chromosome: " + required);
            Objects.requireNonNull(pair[1], "second allele must not be null for chromosome: " + required);
            if (pair[0].chromosomeType() != required) {
                throw new IllegalArgumentException(
                        "first allele ChromosomeType " + pair[0].chromosomeType()
                        + " does not match key " + required);
            }
            if (pair[1].chromosomeType() != required) {
                throw new IllegalArgumentException(
                        "second allele ChromosomeType " + pair[1].chromosomeType()
                        + " does not match key " + required);
            }
        }
        if (traits.containsKey(ChromosomeType.SPECIES)) {
            throw new IllegalArgumentException(
                    "defaultTraitAlleles must not include SPECIES — use speciesAllele field instead");
        }
    }

    private static Map<ChromosomeType, Allele[]> copyTraitAlleles(Map<ChromosomeType, Allele[]> traits) {
        EnumMap<ChromosomeType, Allele[]> copy = new EnumMap<>(ChromosomeType.class);
        for (Map.Entry<ChromosomeType, Allele[]> entry : traits.entrySet()) {
            Allele[] original = entry.getValue();
            copy.put(entry.getKey(), new Allele[]{original[0], original[1]});
        }
        return Collections.unmodifiableMap(copy);
    }

    public String id() { return id; }
    public String displayName() { return displayName; }
    public Allele speciesAllele() { return speciesAllele; }

    /** Visual metadata for this species, if defined. Empty when no visual profile has been set. */
    public Optional<SpeciesVisualDefinition> visualDefinition() {
        return Optional.ofNullable(visualDefinition);
    }

    /** Returns the [first, second] default allele pair for the given trait chromosome. */
    public Allele[] defaultTraitAlleles(ChromosomeType type) {
        Objects.requireNonNull(type, "type must not be null");
        if (type == ChromosomeType.SPECIES) {
            throw new IllegalArgumentException("Use speciesAllele() for SPECIES chromosome");
        }
        Allele[] pair = defaultTraitAlleles.get(type);
        if (pair == null) {
            throw new IllegalArgumentException("No default trait alleles for chromosome: " + type);
        }
        return new Allele[]{pair[0], pair[1]};
    }

    public List<String> spawnContextNotes() { return spawnContextNotes; }

    @Override
    public String toString() {
        return "BeeSpeciesDefinition{id='" + id + "', displayName='" + displayName + "'}";
    }
}
