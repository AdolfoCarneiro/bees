package com.curiousbees.neoforge.data;

// BUILD NOTE: Same classpath requirements as GenomeCodecRoundtripTest.
// See that file for the Gradle test sourceSet snippet.
//
// CapturedBeeData.CODEC uses DFU Codec.BOOL + GenomeCodec.GENOME — no registry bootstrap.
// CapturedBeeData.STREAM_CODEC uses ByteBufCodecs.BOOL + GenomeCodec.GENOME_STREAM.
// ByteBufCodecs is a net.minecraft class but operates on plain Netty ByteBuf — no registry.

import com.curiousbees.common.genetics.serial.GenePairData;
import com.curiousbees.common.genetics.serial.GenomeData;
import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Verifies CapturedBeeData serialization contracts via DFU CODEC and StreamCodec.
 *
 * Tests cover both the "carrying a bee" (genome present) and implicitly the
 * "empty" state — CapturedBeeData has no nullable genome field; an empty item
 * simply has no DataComponent attached (tested conceptually via absense, not a
 * null-genome instance). The struct is a plain record; null genome would cause
 * NPE in GenomeCodec before reaching the component layer.
 */
class CapturedBeeDataCodecTest {

    // --- helpers ---

    private static GenomeData meadowGenome() {
        Map<String, GenePairData> chromosomes = new LinkedHashMap<>();
        chromosomes.put("SPECIES", new GenePairData(
                "curious_bees:species/meadow",
                "curious_bees:species/meadow",
                "curious_bees:species/meadow",
                "curious_bees:species/meadow"));
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

    private static GenomeData hybridGenome() {
        Map<String, GenePairData> chromosomes = new LinkedHashMap<>();
        chromosomes.put("SPECIES", new GenePairData(
                "curious_bees:species/meadow",
                "curious_bees:species/forest",
                "curious_bees:species/meadow",
                "curious_bees:species/forest"));
        return new GenomeData(chromosomes);
    }

    // --- DFU CODEC roundtrip ---

    @Test
    void capturedBeeDataCodecRoundtripAnalyzed() {
        CapturedBeeData original = new CapturedBeeData(meadowGenome(), true);

        DataResult<JsonElement> encoded = CapturedBeeData.CODEC.encodeStart(JsonOps.INSTANCE, original);
        assertTrue(encoded.result().isPresent(), "Encode must succeed");

        DataResult<CapturedBeeData> decoded = CapturedBeeData.CODEC.parse(
                JsonOps.INSTANCE, encoded.result().get());
        assertTrue(decoded.result().isPresent(), "Decode must succeed");

        CapturedBeeData restored = decoded.result().get();
        assertTrue(restored.analyzed(), "analyzed=true must survive codec roundtrip");
    }

    @Test
    void capturedBeeDataCodecRoundtripUnanalyzed() {
        CapturedBeeData original = new CapturedBeeData(meadowGenome(), false);

        JsonElement json = CapturedBeeData.CODEC.encodeStart(JsonOps.INSTANCE, original)
                .result().orElseThrow(() -> new AssertionError("Encode failed"));
        CapturedBeeData restored = CapturedBeeData.CODEC.parse(JsonOps.INSTANCE, json)
                .result().orElseThrow(() -> new AssertionError("Decode failed"));

        assertFalse(restored.analyzed(), "analyzed=false must survive codec roundtrip");
    }

    @Test
    void capturedBeeDataCodecPreservesGenomeSpecies() {
        CapturedBeeData original = new CapturedBeeData(meadowGenome(), true);

        JsonElement json = CapturedBeeData.CODEC.encodeStart(JsonOps.INSTANCE, original)
                .result().orElseThrow(() -> new AssertionError("Encode failed"));
        CapturedBeeData restored = CapturedBeeData.CODEC.parse(JsonOps.INSTANCE, json)
                .result().orElseThrow(() -> new AssertionError("Decode failed"));

        assertNotNull(restored.genome(), "Genome must not be null after roundtrip");
        assertEquals(
                original.genome().chromosomes().get("SPECIES").activeAlleleId(),
                restored.genome().chromosomes().get("SPECIES").activeAlleleId(),
                "SPECIES active allele must survive codec roundtrip");
    }

    @Test
    void capturedBeeDataCodecPreservesHybridActiveInactive() {
        CapturedBeeData original = new CapturedBeeData(hybridGenome(), false);

        JsonElement json = CapturedBeeData.CODEC.encodeStart(JsonOps.INSTANCE, original)
                .result().orElseThrow(() -> new AssertionError("Encode failed"));
        CapturedBeeData restored = CapturedBeeData.CODEC.parse(JsonOps.INSTANCE, json)
                .result().orElseThrow(() -> new AssertionError("Decode failed"));

        GenePairData restoredSpecies = restored.genome().chromosomes().get("SPECIES");
        assertEquals("curious_bees:species/meadow", restoredSpecies.activeAlleleId(),
                "Hybrid active allele must not be rerolled by codec");
        assertEquals("curious_bees:species/forest", restoredSpecies.inactiveAlleleId(),
                "Hybrid inactive allele must not be rerolled by codec");
    }

    // --- StreamCodec (ByteBuf) roundtrip ---

    @Test
    void capturedBeeDataStreamCodecRoundtripAnalyzedFlag() {
        for (boolean flag : new boolean[]{true, false}) {
            CapturedBeeData original = new CapturedBeeData(meadowGenome(), flag);

            ByteBuf buf = Unpooled.buffer();
            try {
                CapturedBeeData.STREAM_CODEC.encode(buf, original);
                CapturedBeeData restored = CapturedBeeData.STREAM_CODEC.decode(buf);

                assertEquals(flag, restored.analyzed(),
                        "analyzed=" + flag + " must survive stream codec roundtrip");
                assertEquals(0, buf.readableBytes(),
                        "Stream codec must consume all written bytes");
            } finally {
                buf.release();
            }
        }
    }

    @Test
    void capturedBeeDataStreamCodecPreservesGenome() {
        CapturedBeeData original = new CapturedBeeData(hybridGenome(), true);

        ByteBuf buf = Unpooled.buffer();
        try {
            CapturedBeeData.STREAM_CODEC.encode(buf, original);
            CapturedBeeData restored = CapturedBeeData.STREAM_CODEC.decode(buf);

            assertNotNull(restored.genome(), "Genome must survive stream codec roundtrip");
            assertEquals(
                    original.genome().chromosomes().get("SPECIES").activeAlleleId(),
                    restored.genome().chromosomes().get("SPECIES").activeAlleleId(),
                    "Active allele must survive stream codec without reroll");
        } finally {
            buf.release();
        }
    }

    // --- Structural contract ---

    @Test
    void capturedBeeDataIsARecord() {
        // Record equality: two instances with the same data must be equal
        CapturedBeeData a = new CapturedBeeData(meadowGenome(), true);
        CapturedBeeData b = new CapturedBeeData(meadowGenome(), true);
        assertEquals(a, b, "CapturedBeeData with identical fields must be equal (record contract)");
        assertEquals(a.hashCode(), b.hashCode(), "Equal records must have equal hashCodes");
    }

    @Test
    void capturedBeeDataAnalyzedAndUnanalyzedAreDistinct() {
        CapturedBeeData analyzed   = new CapturedBeeData(meadowGenome(), true);
        CapturedBeeData unanalyzed = new CapturedBeeData(meadowGenome(), false);
        assertNotEquals(analyzed, unanalyzed,
                "analyzed=true and analyzed=false must not be equal");
    }
}
