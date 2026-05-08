package com.curiousbees.neoforge.data;

// BUILD NOTE: This test compiles against the neoforge source set which already
// has NeoForge (and therefore DFU / com.mojang.serialization) on its classpath.
// To run with Gradle:
//
//   sourceSets {
//       test {
//           java.srcDirs = ['src/test/java']
//           compileClasspath += sourceSets.main.compileClasspath + sourceSets.main.output
//           runtimeClasspath += sourceSets.main.runtimeClasspath + sourceSets.main.output
//       }
//   }
//   dependencies {
//       testImplementation 'org.junit.jupiter:junit-jupiter:5.10.0'
//       testImplementation project(':common')          // for GenomeData / GenePairData
//   }
//   test { useJUnitPlatform() }
//
// DFU Codec (com.mojang.serialization) does NOT require Minecraft registry bootstrap.
// StreamCodec uses io.netty.buffer.Unpooled — also no bootstrap needed.
// These tests exercise pure serialisation contracts with no Level / Server involved.

import com.curiousbees.common.genetics.serial.GenePairData;
import com.curiousbees.common.genetics.serial.GenomeData;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.google.gson.JsonElement;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies GenomeCodec encode → decode roundtrips using pure DFU / Netty APIs.
 *
 * No Minecraft server, registry, or NeoForge event bus is required.
 */
class GenomeCodecRoundtripTest {

    // --- helpers ---

    private static GenePairData meadowPair() {
        return new GenePairData(
                "curious_bees:species/meadow",
                "curious_bees:species/meadow",
                "curious_bees:species/meadow",
                "curious_bees:species/meadow");
    }

    private static GenePairData hybridSpeciesPair() {
        return new GenePairData(
                "curious_bees:species/meadow",
                "curious_bees:species/forest",
                "curious_bees:species/meadow",   // active = first (dominant by convention)
                "curious_bees:species/forest");
    }

    private static GenomeData fullGenome() {
        Map<String, GenePairData> chromosomes = new LinkedHashMap<>();
        chromosomes.put("SPECIES",      meadowPair());
        chromosomes.put("PRODUCTIVITY", new GenePairData(
                "curious_bees:traits/productivity/normal",
                "curious_bees:traits/productivity/normal",
                "curious_bees:traits/productivity/normal",
                "curious_bees:traits/productivity/normal"));
        chromosomes.put("FLOWER_TYPE", new GenePairData(
                "curious_bees:traits/flower_type/flowers",
                "curious_bees:traits/flower_type/flowers",
                "curious_bees:traits/flower_type/flowers",
                "curious_bees:traits/flower_type/flowers"));
        return new GenomeData(chromosomes);
    }

    // --- GenePairData CODEC roundtrip (JSON) ---

    @Test
    void genePairCodecRoundtripJson() {
        GenePairData original = meadowPair();

        DataResult<JsonElement> encoded = GenomeCodec.GENE_PAIR.encodeStart(JsonOps.INSTANCE, original);
        assertTrue(encoded.result().isPresent(), "Encode must succeed");

        DataResult<GenePairData> decoded = GenomeCodec.GENE_PAIR.parse(JsonOps.INSTANCE, encoded.result().get());
        assertTrue(decoded.result().isPresent(), "Decode must succeed");

        GenePairData restored = decoded.result().get();
        assertEquals(original.firstAlleleId(),    restored.firstAlleleId());
        assertEquals(original.secondAlleleId(),   restored.secondAlleleId());
        assertEquals(original.activeAlleleId(),   restored.activeAlleleId());
        assertEquals(original.inactiveAlleleId(), restored.inactiveAlleleId());
    }

    @Test
    void genePairCodecRoundtripPreservesActiveInactive() {
        GenePairData original = hybridSpeciesPair();

        DataResult<JsonElement> encoded = GenomeCodec.GENE_PAIR.encodeStart(JsonOps.INSTANCE, original);
        GenePairData restored = GenomeCodec.GENE_PAIR
                .parse(JsonOps.INSTANCE, encoded.result().orElseThrow())
                .result()
                .orElseThrow();

        // The active allele must remain the one explicitly set — no reroll
        assertEquals("curious_bees:species/meadow", restored.activeAlleleId(),
                "Active allele must be preserved through roundtrip without rerolling");
        assertEquals("curious_bees:species/forest", restored.inactiveAlleleId(),
                "Inactive allele must be preserved through roundtrip without rerolling");
    }

    // --- GenomeData CODEC roundtrip (JSON) ---

    @Test
    void genomeCodecRoundtripAllChromosomesPreserved() {
        GenomeData original = fullGenome();

        DataResult<JsonElement> encoded = GenomeCodec.GENOME.encodeStart(JsonOps.INSTANCE, original);
        assertTrue(encoded.result().isPresent(), "GenomeData encode must succeed");

        DataResult<GenomeData> decoded = GenomeCodec.GENOME.parse(JsonOps.INSTANCE, encoded.result().get());
        assertTrue(decoded.result().isPresent(), "GenomeData decode must succeed");

        GenomeData restored = decoded.result().get();
        assertEquals(original.chromosomes().size(), restored.chromosomes().size(),
                "Chromosome count must be preserved");
        for (String key : original.chromosomes().keySet()) {
            assertTrue(restored.chromosomes().containsKey(key),
                    "Chromosome '" + key + "' must survive roundtrip");
        }
    }

    @Test
    void genomeCodecRoundtripPreservesSpeciesAlleleIds() {
        GenomeData original = fullGenome();

        JsonElement json = GenomeCodec.GENOME.encodeStart(JsonOps.INSTANCE, original)
                .result().orElseThrow(() -> new AssertionError("Encode failed"));
        GenomeData restored = GenomeCodec.GENOME.parse(JsonOps.INSTANCE, json)
                .result().orElseThrow(() -> new AssertionError("Decode failed"));

        GenePairData origSpecies    = original.chromosomes().get("SPECIES");
        GenePairData restoredSpecies = restored.chromosomes().get("SPECIES");

        assertNotNull(restoredSpecies, "SPECIES chromosome must be present after roundtrip");
        assertEquals(origSpecies.firstAlleleId(),    restoredSpecies.firstAlleleId());
        assertEquals(origSpecies.secondAlleleId(),   restoredSpecies.secondAlleleId());
        assertEquals(origSpecies.activeAlleleId(),   restoredSpecies.activeAlleleId());
        assertEquals(origSpecies.inactiveAlleleId(), restoredSpecies.inactiveAlleleId());
    }

    @Test
    void genomeCodecRoundtripSingleChromosome() {
        Map<String, GenePairData> chromosomes = new LinkedHashMap<>();
        chromosomes.put("SPECIES", hybridSpeciesPair());
        GenomeData minimal = new GenomeData(chromosomes);

        JsonElement json = GenomeCodec.GENOME.encodeStart(JsonOps.INSTANCE, minimal)
                .result().orElseThrow(() -> new AssertionError("Encode failed"));
        GenomeData restored = GenomeCodec.GENOME.parse(JsonOps.INSTANCE, json)
                .result().orElseThrow(() -> new AssertionError("Decode failed"));

        assertEquals(1, restored.chromosomes().size());
        assertEquals("curious_bees:species/meadow",
                restored.chromosomes().get("SPECIES").activeAlleleId());
    }

    // --- StreamCodec (ByteBuf) roundtrip ---

    @Test
    void genePairStreamCodecRoundtrip() {
        GenePairData original = hybridSpeciesPair();

        ByteBuf buf = Unpooled.buffer();
        try {
            GenomeCodec.GENE_PAIR_STREAM.encode(buf, original);
            GenePairData restored = GenomeCodec.GENE_PAIR_STREAM.decode(buf);

            assertEquals(original.firstAlleleId(),    restored.firstAlleleId());
            assertEquals(original.secondAlleleId(),   restored.secondAlleleId());
            assertEquals(original.activeAlleleId(),   restored.activeAlleleId());
            assertEquals(original.inactiveAlleleId(), restored.inactiveAlleleId());
        } finally {
            buf.release();
        }
    }

    @Test
    void genomeStreamCodecRoundtrip() {
        GenomeData original = fullGenome();

        ByteBuf buf = Unpooled.buffer();
        try {
            GenomeCodec.GENOME_STREAM.encode(buf, original);
            GenomeData restored = GenomeCodec.GENOME_STREAM.decode(buf);

            assertEquals(original.chromosomes().size(), restored.chromosomes().size());
            for (String key : original.chromosomes().keySet()) {
                assertTrue(restored.chromosomes().containsKey(key),
                        "Chromosome '" + key + "' missing after stream roundtrip");
                GenePairData origPair    = original.chromosomes().get(key);
                GenePairData restoredPair = restored.chromosomes().get(key);
                assertEquals(origPair.activeAlleleId(),   restoredPair.activeAlleleId(),
                        key + " active allele changed after stream roundtrip");
                assertEquals(origPair.inactiveAlleleId(), restoredPair.inactiveAlleleId(),
                        key + " inactive allele changed after stream roundtrip");
            }
        } finally {
            buf.release();
        }
    }

    @Test
    void genomeStreamCodecLeavesBufferEmpty() {
        GenomeData original = fullGenome();

        ByteBuf buf = Unpooled.buffer();
        try {
            GenomeCodec.GENOME_STREAM.encode(buf, original);
            GenomeCodec.GENOME_STREAM.decode(buf);
            assertEquals(0, buf.readableBytes(),
                    "Stream codec must consume all written bytes — no leftover data");
        } finally {
            buf.release();
        }
    }
}
