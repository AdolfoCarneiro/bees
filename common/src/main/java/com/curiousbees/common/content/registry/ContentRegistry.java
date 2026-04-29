package com.curiousbees.common.content.registry;

import com.curiousbees.common.content.builtin.BuiltinBeeContent;
import com.curiousbees.common.content.products.BuiltinProductionDefinitions;
import com.curiousbees.common.content.species.BeeSpeciesDefinition;
import com.curiousbees.common.gameplay.production.ProductionDefinition;
import com.curiousbees.common.genetics.model.Allele;
import com.curiousbees.common.genetics.mutation.MutationDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Runtime content facade for built-in and externally loaded definitions.
 * Loaded content may extend built-ins, but duplicate IDs are rejected.
 */
public final class ContentRegistry {

    private static final Logger LOGGER = Logger.getLogger(ContentRegistry.class.getName());

    private final Map<String, BeeSpeciesDefinition> speciesById;
    private final Map<String, Allele> traitAllelesById;
    private final Map<String, Allele> allAllelesById;
    private final Map<String, MutationDefinition> mutationsById;
    private final Map<String, ProductionDefinition> productionBySpeciesId;

    private ContentRegistry(
            Map<String, BeeSpeciesDefinition> speciesById,
            Map<String, Allele> traitAllelesById,
            Map<String, MutationDefinition> mutationsById,
            Map<String, ProductionDefinition> productionBySpeciesId) {
        this.speciesById = Collections.unmodifiableMap(new LinkedHashMap<>(speciesById));
        this.traitAllelesById = Collections.unmodifiableMap(new LinkedHashMap<>(traitAllelesById));
        this.mutationsById = Collections.unmodifiableMap(new LinkedHashMap<>(mutationsById));
        this.productionBySpeciesId = Collections.unmodifiableMap(new LinkedHashMap<>(productionBySpeciesId));

        Map<String, Allele> allAlleles = new LinkedHashMap<>(traitAllelesById);
        for (BeeSpeciesDefinition species : speciesById.values()) {
            allAlleles.put(species.speciesAllele().id(), species.speciesAllele());
        }
        this.allAllelesById = Collections.unmodifiableMap(allAlleles);
    }

    public static ContentRegistry builtIn() {
        Map<String, BeeSpeciesDefinition> species = new LinkedHashMap<>();
        for (BeeSpeciesDefinition definition : BuiltinBeeContent.allSpecies()) {
            species.put(definition.id(), definition);
        }

        Map<String, Allele> traits = new LinkedHashMap<>();
        for (Allele allele : BuiltinBeeContent.allTraits()) {
            traits.put(allele.id(), allele);
        }

        Map<String, MutationDefinition> mutations = new LinkedHashMap<>();
        for (MutationDefinition definition : BuiltinBeeContent.allMutations()) {
            mutations.put(definition.id(), definition);
        }

        return new ContentRegistry(
                species,
                traits,
                mutations,
                BuiltinProductionDefinitions.BY_SPECIES_ID);
    }

    public ContentRegistry withLoadedDefinitions(
            Collection<Allele> traitAlleles,
            Collection<BeeSpeciesDefinition> species,
            Collection<MutationDefinition> mutations,
            Collection<ProductionDefinition> productionDefinitions) {
        Objects.requireNonNull(traitAlleles, "traitAlleles must not be null");
        Objects.requireNonNull(species, "species must not be null");
        Objects.requireNonNull(mutations, "mutations must not be null");
        Objects.requireNonNull(productionDefinitions, "productionDefinitions must not be null");

        Map<String, BeeSpeciesDefinition> mergedSpecies = new LinkedHashMap<>(speciesById);
        Map<String, Allele> mergedTraits = new LinkedHashMap<>(traitAllelesById);
        Map<String, MutationDefinition> mergedMutations = new LinkedHashMap<>(mutationsById);
        Map<String, ProductionDefinition> mergedProduction = new LinkedHashMap<>(productionBySpeciesId);

        for (Allele allele : traitAlleles) {
            registerTraitAllele(mergedTraits, allele);
        }
        for (BeeSpeciesDefinition definition : species) {
            registerSpecies(mergedSpecies, definition);
        }
        for (MutationDefinition definition : mutations) {
            registerMutation(mergedMutations, definition);
        }
        for (ProductionDefinition definition : productionDefinitions) {
            registerProduction(mergedProduction, definition);
        }

        return new ContentRegistry(mergedSpecies, mergedTraits, mergedMutations, mergedProduction);
    }

    public Optional<BeeSpeciesDefinition> findSpecies(String id) {
        Objects.requireNonNull(id, "id must not be null");
        return Optional.ofNullable(speciesById.get(id));
    }

    public BeeSpeciesDefinition getSpecies(String id) {
        Objects.requireNonNull(id, "id must not be null");
        BeeSpeciesDefinition definition = speciesById.get(id);
        if (definition == null) {
            warnAndThrow("Unknown species id: " + id);
        }
        return definition;
    }

    public List<BeeSpeciesDefinition> allSpecies() {
        return List.copyOf(speciesById.values());
    }

    public Optional<Allele> findTraitAllele(String id) {
        Objects.requireNonNull(id, "id must not be null");
        return Optional.ofNullable(traitAllelesById.get(id));
    }

    public List<Allele> allTraitAlleles() {
        return List.copyOf(traitAllelesById.values());
    }

    public Optional<Allele> findAllele(String id) {
        Objects.requireNonNull(id, "id must not be null");
        return Optional.ofNullable(allAllelesById.get(id));
    }

    public Optional<MutationDefinition> findMutation(String id) {
        Objects.requireNonNull(id, "id must not be null");
        return Optional.ofNullable(mutationsById.get(id));
    }

    public List<MutationDefinition> allMutations() {
        return List.copyOf(mutationsById.values());
    }

    public Optional<ProductionDefinition> findProduction(String speciesId) {
        Objects.requireNonNull(speciesId, "speciesId must not be null");
        return Optional.ofNullable(productionBySpeciesId.get(speciesId));
    }

    public Map<String, ProductionDefinition> productionBySpeciesId() {
        return productionBySpeciesId;
    }

    public List<ProductionDefinition> allProductionDefinitions() {
        return new ArrayList<>(productionBySpeciesId.values());
    }

    private static void registerTraitAllele(Map<String, Allele> target, Allele allele) {
        Objects.requireNonNull(allele, "trait allele entries must not be null");
        if (target.containsKey(allele.id())) {
            warnAndThrow("Duplicate trait allele id rejected: " + allele.id());
        }
        target.put(allele.id(), allele);
    }

    private static void registerSpecies(Map<String, BeeSpeciesDefinition> target, BeeSpeciesDefinition definition) {
        Objects.requireNonNull(definition, "species entries must not be null");
        if (target.containsKey(definition.id())) {
            warnAndThrow("Duplicate species id rejected: " + definition.id());
        }
        target.put(definition.id(), definition);
    }

    private static void registerMutation(Map<String, MutationDefinition> target, MutationDefinition definition) {
        Objects.requireNonNull(definition, "mutation entries must not be null");
        if (target.containsKey(definition.id())) {
            warnAndThrow("Duplicate mutation id rejected: " + definition.id());
        }
        target.put(definition.id(), definition);
    }

    private static void registerProduction(Map<String, ProductionDefinition> target, ProductionDefinition definition) {
        Objects.requireNonNull(definition, "production entries must not be null");
        if (target.containsKey(definition.speciesId())) {
            warnAndThrow("Duplicate production species id rejected: " + definition.speciesId());
        }
        target.put(definition.speciesId(), definition);
    }

    private static void warnAndThrow(String message) {
        LOGGER.warning(message);
        throw new IllegalArgumentException(message);
    }
}
