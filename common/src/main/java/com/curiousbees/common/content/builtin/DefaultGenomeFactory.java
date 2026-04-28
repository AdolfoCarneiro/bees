package com.curiousbees.common.content.builtin;

import com.curiousbees.common.content.species.BeeSpeciesDefinition;
import com.curiousbees.common.genetics.model.Allele;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.GenePair;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.random.GeneticRandom;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/** Creates default purebred (or species-appropriate) genomes from built-in species definitions. */
public final class DefaultGenomeFactory {

    private static final Logger LOGGER = Logger.getLogger(DefaultGenomeFactory.class.getName());

    private DefaultGenomeFactory() {}

    /**
     * Creates a default genome for the given species definition.
     * The SPECIES chromosome is always purebred (speciesAllele / speciesAllele).
     * Trait chromosomes use the first and second default alleles from the definition.
     *
     * @param definition the species whose default genome to create
     * @param random     used for GenePair active/inactive resolution
     */
    public static Genome createDefault(BeeSpeciesDefinition definition, GeneticRandom random) {
        Objects.requireNonNull(definition, "definition must not be null");
        Objects.requireNonNull(random, "random must not be null");

        Map<ChromosomeType, GenePair> pairs = new EnumMap<>(ChromosomeType.class);

        // Species: always purebred
        Allele species = definition.speciesAllele();
        pairs.put(ChromosomeType.SPECIES, new GenePair(species, species, random));

        // Traits
        for (ChromosomeType type : new ChromosomeType[]{
                ChromosomeType.LIFESPAN,
                ChromosomeType.PRODUCTIVITY,
                ChromosomeType.FERTILITY,
                ChromosomeType.FLOWER_TYPE}) {
            Allele[] pair = definition.defaultTraitAlleles(type);
            pairs.put(type, new GenePair(pair[0], pair[1], random));
        }

        LOGGER.fine("Created default genome for species: " + definition.id());
        return new Genome(pairs);
    }
}
