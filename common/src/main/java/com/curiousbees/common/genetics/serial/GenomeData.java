package com.curiousbees.common.genetics.serial;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Platform-neutral serialized form of a Genome.
 * Keys are ChromosomeType.name() strings for forward-compatible evolution.
 */
public record GenomeData(Map<String, GenePairData> chromosomes) {

    public GenomeData {
        Objects.requireNonNull(chromosomes, "chromosomes must not be null");
        chromosomes = Collections.unmodifiableMap(new LinkedHashMap<>(chromosomes));
    }
}
