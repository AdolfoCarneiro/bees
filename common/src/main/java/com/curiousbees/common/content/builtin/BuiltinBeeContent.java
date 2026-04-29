package com.curiousbees.common.content.builtin;

import com.curiousbees.common.content.species.BeeSpeciesDefinition;
import com.curiousbees.common.genetics.model.Allele;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.mutation.MutationDefinition;
import com.curiousbees.common.genetics.random.GeneticRandom;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Single access point for all built-in MVP content.
 * Provides lookup by ID for species and trait alleles, and delegates genome creation.
 */
public final class BuiltinBeeContent {

    private static final Logger LOGGER = Logger.getLogger(BuiltinBeeContent.class.getName());

    private static final Map<String, BeeSpeciesDefinition> SPECIES_BY_ID;
    private static final Map<String, Allele> TRAITS_BY_ID;
    private static final Map<String, Allele> ALL_ALLELES_BY_ID;

    static {
        Map<String, BeeSpeciesDefinition> species = new LinkedHashMap<>();
        for (BeeSpeciesDefinition def : BuiltinBeeSpecies.ALL) {
            species.put(def.id(), def);
        }
        SPECIES_BY_ID = Collections.unmodifiableMap(species);

        Map<String, Allele> traits = new LinkedHashMap<>();
        for (Allele allele : BuiltinBeeTraits.ALL) {
            traits.put(allele.id(), allele);
        }
        TRAITS_BY_ID = Collections.unmodifiableMap(traits);

        Map<String, Allele> allAlleles = new LinkedHashMap<>(traits);
        for (BeeSpeciesDefinition def : BuiltinBeeSpecies.ALL) {
            allAlleles.put(def.speciesAllele().id(), def.speciesAllele());
        }
        ALL_ALLELES_BY_ID = Collections.unmodifiableMap(allAlleles);
    }

    private BuiltinBeeContent() {}

    // --- species ---

    public static Optional<BeeSpeciesDefinition> findSpecies(String id) {
        Objects.requireNonNull(id, "id must not be null");
        return Optional.ofNullable(SPECIES_BY_ID.get(id));
    }

    public static BeeSpeciesDefinition getSpecies(String id) {
        Objects.requireNonNull(id, "id must not be null");
        BeeSpeciesDefinition def = SPECIES_BY_ID.get(id);
        if (def == null) {
            LOGGER.warning("Unknown built-in species requested: " + id);
            throw new IllegalArgumentException("No built-in species with id: " + id);
        }
        return def;
    }

    public static List<BeeSpeciesDefinition> allSpecies() {
        return BuiltinBeeSpecies.ALL;
    }

    // --- traits ---

    public static Optional<Allele> findTrait(String id) {
        Objects.requireNonNull(id, "id must not be null");
        return Optional.ofNullable(TRAITS_BY_ID.get(id));
    }

    public static List<Allele> allTraits() {
        return BuiltinBeeTraits.ALL;
    }

    /** Looks up any built-in allele by ID — covers both species alleles and trait alleles. */
    public static Optional<Allele> findAllele(String id) {
        Objects.requireNonNull(id, "id must not be null");
        return Optional.ofNullable(ALL_ALLELES_BY_ID.get(id));
    }

    // --- mutations ---

    public static List<MutationDefinition> allMutations() {
        return BuiltinBeeMutations.ALL;
    }

    // --- genomes ---

    public static Genome createDefaultGenome(String speciesId, GeneticRandom random) {
        Objects.requireNonNull(speciesId, "speciesId must not be null");
        Objects.requireNonNull(random, "random must not be null");
        return DefaultGenomeFactory.createDefault(getSpecies(speciesId), random);
    }

    public static Genome createDefaultGenome(BeeSpeciesDefinition definition, GeneticRandom random) {
        Objects.requireNonNull(definition, "definition must not be null");
        Objects.requireNonNull(random, "random must not be null");
        return DefaultGenomeFactory.createDefault(definition, random);
    }
}
