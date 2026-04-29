package com.curiousbees.common.genetics.serial;

import com.curiousbees.common.genetics.model.Allele;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.GenePair;
import com.curiousbees.common.genetics.model.Genome;

import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.logging.Logger;

/**
 * Converts between Genome (domain object) and GenomeData (serializable form).
 * Pure Java — no Minecraft or NeoForge imports.
 *
 * The alleleResolver function is injected by the caller so this serializer
 * is not tied to a specific content registry implementation.
 */
public final class GenomeSerializer {

    private static final Logger LOGGER = Logger.getLogger(GenomeSerializer.class.getName());

    private GenomeSerializer() {}

    /** Converts a Genome to its serializable form. Never returns null. */
    public static GenomeData toData(Genome genome) {
        Objects.requireNonNull(genome, "genome must not be null");

        Map<String, GenePairData> chromosomes = new LinkedHashMap<>();
        for (Map.Entry<ChromosomeType, GenePair> entry : genome.genePairs().entrySet()) {
            GenePair pair = entry.getValue();
            chromosomes.put(
                    entry.getKey().name(),
                    new GenePairData(
                            pair.first().id(),
                            pair.second().id(),
                            pair.active().id(),
                            pair.inactive().id()));
        }
        return new GenomeData(chromosomes);
    }

    /**
     * Restores a Genome from serialized data.
     * Returns empty if any chromosome type is unknown or any allele ID cannot be resolved.
     *
     * @param data           the serialized genome
     * @param alleleResolver function that looks up an Allele by its stable ID
     */
    public static Optional<Genome> fromData(GenomeData data,
                                            Function<String, Optional<Allele>> alleleResolver) {
        Objects.requireNonNull(data,           "data must not be null");
        Objects.requireNonNull(alleleResolver, "alleleResolver must not be null");

        Map<ChromosomeType, GenePair> pairs = new EnumMap<>(ChromosomeType.class);

        for (Map.Entry<String, GenePairData> entry : data.chromosomes().entrySet()) {
            ChromosomeType type;
            try {
                type = ChromosomeType.valueOf(entry.getKey());
            } catch (IllegalArgumentException e) {
                LOGGER.warning("Unknown ChromosomeType '" + entry.getKey()
                        + "' during genome deserialization — skipping chromosome.");
                continue;
            }

            GenePairData pairData = entry.getValue();

            Optional<Allele> first    = resolve(pairData.firstAlleleId(),    alleleResolver, type, "first");
            Optional<Allele> second   = resolve(pairData.secondAlleleId(),   alleleResolver, type, "second");
            Optional<Allele> active   = resolve(pairData.activeAlleleId(),   alleleResolver, type, "active");
            Optional<Allele> inactive = resolve(pairData.inactiveAlleleId(), alleleResolver, type, "inactive");

            if (first.isEmpty() || second.isEmpty() || active.isEmpty() || inactive.isEmpty()) {
                LOGGER.warning("Failed to resolve alleles for chromosome " + type
                        + " — genome deserialization aborted.");
                return Optional.empty();
            }

            try {
                pairs.put(type, GenePair.restored(first.get(), second.get(), active.get(), inactive.get()));
            } catch (IllegalArgumentException e) {
                LOGGER.warning("Invalid GenePair during deserialization for chromosome " + type
                        + ": " + e.getMessage() + " — genome deserialization aborted.");
                return Optional.empty();
            }
        }

        if (!pairs.containsKey(ChromosomeType.SPECIES)) {
            LOGGER.warning("Serialized genome is missing SPECIES chromosome — deserialization aborted.");
            return Optional.empty();
        }

        try {
            return Optional.of(new Genome(pairs));
        } catch (IllegalArgumentException e) {
            LOGGER.warning("Invalid genome during deserialization: " + e.getMessage());
            return Optional.empty();
        }
    }

    private static Optional<Allele> resolve(String id, Function<String, Optional<Allele>> resolver,
                                            ChromosomeType type, String role) {
        Optional<Allele> result = resolver.apply(id);
        if (result.isEmpty()) {
            LOGGER.warning("Unknown allele id '" + id + "' for " + role
                    + " of chromosome " + type + " — cannot resolve.");
        }
        return result;
    }
}
