package com.curiousbees.neoforge.data;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Contract tests for CapturedBeeData codec roundtrip.
 *
 * WHY THIS FILE IS HERE (not in common/src/test):
 * CapturedBeeData imports com.mojang.serialization.Codec and net.minecraft.network.codec.*
 * which are Minecraft/NeoForge API types. These cannot be instantiated without the full
 * game bootstrap. Tests therefore live in neoforge/src/test where game bootstrap
 * infrastructure can be provided (e.g. via a NeoForge test framework or mock bootstrap).
 *
 * WHAT TO DO when implementing:
 * 1. Add neoforge game-test or bootstrapped unit test infrastructure to the build.
 * 2. Remove @Disabled and implement each test using a real or mocked Codec runner.
 * 3. Use GenomeSerializer.toData() + GenomeFixtures to build CapturedBeeData instances.
 *
 * @see com.curiousbees.neoforge.data.CapturedBeeData
 */
@Disabled("Awaiting PR-T02: neoforge/src/test bootstrap infrastructure not yet wired — " +
          "CapturedBeeData uses net.minecraft codec types that require game bootstrap")
class StoredBeeDataCodecTest {

    /**
     * Roundtrip: serialise CapturedBeeData to NbtOps/JsonOps and deserialise back.
     * All fields (genome, analyzed flag) must be preserved exactly.
     */
    @Test
    void roundtripPreservesAllFields() {
        // TODO: build CapturedBeeData from GenomeFixtures.pureMeadow() + GenomeSerializer.toData()
        // TODO: encode via CapturedBeeData.CODEC + NbtOps.INSTANCE
        // TODO: decode and assert genome == original, analyzed == original
    }

    /**
     * The genome stored inside CapturedBeeData survives a full codec roundtrip intact:
     * all ChromosomeType entries and their active/inactive allele IDs are preserved.
     */
    @Test
    void genomePreservedIntactAfterRoundtrip() {
        // TODO: encode then decode; compare each ChromosomeType allele ID
    }

    /**
     * When a custom name is stored alongside the bee (future feature), it survives roundtrip.
     * Placeholder for when CapturedBeeData gains a name field.
     */
    @Test
    void customNamePreservedIfPresent() {
        // TODO: extend CapturedBeeData with optional name field, then verify roundtrip
    }

    /**
     * A missing or null genome in the serialised form must not crash the decoder.
     * Expected behaviour: decoder returns empty Optional / uses a fallback species.
     */
    @Test
    void missingGenomeDoesNotCrashDecoder() {
        // TODO: serialize a CapturedBeeData, strip the "genome" key, attempt decode
        // TODO: assert no exception is thrown; result may be empty or fallback
    }
}
