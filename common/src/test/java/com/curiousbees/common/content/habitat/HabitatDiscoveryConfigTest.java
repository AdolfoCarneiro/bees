package com.curiousbees.common.content.habitat;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HabitatDiscoveryConfigTest {

    @Test
    void defaultMatchesSpec() {
        // PR-T05 spec: 3% base, 95% partial, 5% full (ADR-0019)
        assertEquals(0.03, HabitatDiscoveryConfig.DEFAULT.baseChance(),  1e-9);
        assertEquals(0.95, HabitatDiscoveryConfig.DEFAULT.partialChance(), 1e-9);
        assertEquals(0.05, HabitatDiscoveryConfig.DEFAULT.fullChance(),   1e-9);
    }

    @Test
    void partialAndFullSumToOne() {
        HabitatDiscoveryConfig cfg = HabitatDiscoveryConfig.DEFAULT;
        assertEquals(1.0, cfg.partialChance() + cfg.fullChance(), 1e-9,
                "partialChance + fullChance must sum to 1.0");
    }

    @Test
    void customConfigPreservesValues() {
        HabitatDiscoveryConfig cfg = new HabitatDiscoveryConfig(0.05, 0.8, 0.2);
        assertEquals(0.05, cfg.baseChance(),    1e-9);
        assertEquals(0.8,  cfg.partialChance(), 1e-9);
        assertEquals(0.2,  cfg.fullChance(),    1e-9);
    }
}
