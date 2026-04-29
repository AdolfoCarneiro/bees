package com.curiousbees.common.genetics.serial;

import com.curiousbees.common.content.builtin.BuiltinBeeContent;
import com.curiousbees.common.content.builtin.BuiltinBeeSpecies;
import com.curiousbees.common.genetics.model.ChromosomeType;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.random.JavaGeneticRandom;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class GenomeSerializerTest {

    private Genome meadowGenome() {
        return BuiltinBeeContent.createDefaultGenome(BuiltinBeeSpecies.MEADOW,
                new JavaGeneticRandom(new Random(0)));
    }

    private Genome hybridMeadowForestGenome() {
        return BuiltinBeeContent.createDefaultGenome(BuiltinBeeSpecies.MEADOW,
                new JavaGeneticRandom(new Random(0)));
    }

    // --- toData ---

    @Test
    void toDataPreservesAllChromosomes() {
        GenomeData data = GenomeSerializer.toData(meadowGenome());
        assertTrue(data.chromosomes().containsKey("SPECIES"));
        assertTrue(data.chromosomes().containsKey("LIFESPAN"));
        assertTrue(data.chromosomes().containsKey("PRODUCTIVITY"));
        assertTrue(data.chromosomes().containsKey("FERTILITY"));
        assertTrue(data.chromosomes().containsKey("FLOWER_TYPE"));
    }

    @Test
    void toDataPreservesAlleleIds() {
        Genome genome = meadowGenome();
        GenomeData data = GenomeSerializer.toData(genome);

        GenePairData speciesData = data.chromosomes().get("SPECIES");
        assertNotNull(speciesData);
        assertEquals(genome.species().first().id(),   speciesData.firstAlleleId());
        assertEquals(genome.species().second().id(),  speciesData.secondAlleleId());
        assertEquals(genome.species().active().id(),  speciesData.activeAlleleId());
        assertEquals(genome.species().inactive().id(), speciesData.inactiveAlleleId());
    }

    @Test
    void toDataNullGenomeFails() {
        assertThrows(NullPointerException.class, () -> GenomeSerializer.toData(null));
    }

    // --- fromData round-trip ---

    @Test
    void roundTripPureMeadowPreservesGenome() {
        Genome original = meadowGenome();
        GenomeData data = GenomeSerializer.toData(original);
        Optional<Genome> restored = GenomeSerializer.fromData(data, BuiltinBeeContent::findAllele);

        assertTrue(restored.isPresent());
        Genome genome = restored.get();

        assertEquals(original.getActiveAllele(ChromosomeType.SPECIES).id(),
                genome.getActiveAllele(ChromosomeType.SPECIES).id());
        assertEquals(original.getInactiveAllele(ChromosomeType.SPECIES).id(),
                genome.getInactiveAllele(ChromosomeType.SPECIES).id());
    }

    @Test
    void roundTripPreservesActiveInactiveIdentity() {
        Genome original = meadowGenome();
        String originalActive   = original.getActiveAllele(ChromosomeType.SPECIES).id();
        String originalInactive = original.getInactiveAllele(ChromosomeType.SPECIES).id();

        GenomeData data = GenomeSerializer.toData(original);
        Genome restored = GenomeSerializer.fromData(data, BuiltinBeeContent::findAllele).orElseThrow();

        // active/inactive must match exactly — no reroll
        assertEquals(originalActive,   restored.getActiveAllele(ChromosomeType.SPECIES).id());
        assertEquals(originalInactive, restored.getInactiveAllele(ChromosomeType.SPECIES).id());
    }

    @Test
    void roundTripAllFiveMvpSpecies() {
        for (var def : BuiltinBeeContent.allSpecies()) {
            Genome original = BuiltinBeeContent.createDefaultGenome(def, new JavaGeneticRandom(new Random(1)));
            GenomeData data = GenomeSerializer.toData(original);
            Optional<Genome> restored = GenomeSerializer.fromData(data, BuiltinBeeContent::findAllele);
            assertTrue(restored.isPresent(), def.id() + " failed round-trip");
            assertEquals(original.getActiveAllele(ChromosomeType.SPECIES).id(),
                    restored.get().getActiveAllele(ChromosomeType.SPECIES).id(),
                    def.id() + " active species mismatch after round-trip");
        }
    }

    // --- fromData error cases ---

    @Test
    void unknownAlleleIdReturnsEmpty() {
        Genome original = meadowGenome();
        GenomeData data = GenomeSerializer.toData(original);

        // replace SPECIES first allele with an unknown id
        GenePairData bad = new GenePairData(
                "curious_bees:species/nonexistent",
                data.chromosomes().get("SPECIES").secondAlleleId(),
                data.chromosomes().get("SPECIES").activeAlleleId(),
                data.chromosomes().get("SPECIES").inactiveAlleleId());

        java.util.Map<String, GenePairData> chromosomes = new java.util.LinkedHashMap<>(data.chromosomes());
        chromosomes.put("SPECIES", bad);
        GenomeData badData = new GenomeData(chromosomes);

        Optional<Genome> result = GenomeSerializer.fromData(badData, BuiltinBeeContent::findAllele);
        assertTrue(result.isEmpty(), "Unknown allele ID must return empty");
    }

    @Test
    void missingSpeciesChromosomeReturnsEmpty() {
        Genome original = meadowGenome();
        GenomeData data = GenomeSerializer.toData(original);

        java.util.Map<String, GenePairData> chromosomes = new java.util.LinkedHashMap<>(data.chromosomes());
        chromosomes.remove("SPECIES");
        GenomeData badData = new GenomeData(chromosomes);

        Optional<Genome> result = GenomeSerializer.fromData(badData, BuiltinBeeContent::findAllele);
        assertTrue(result.isEmpty(), "Missing SPECIES chromosome must return empty");
    }

    @Test
    void unknownChromosomeTypeIsSkippedSafely() {
        Genome original = meadowGenome();
        GenomeData data = GenomeSerializer.toData(original);

        // add a future/unknown chromosome type — should be silently skipped
        java.util.Map<String, GenePairData> chromosomes = new java.util.LinkedHashMap<>(data.chromosomes());
        chromosomes.put("FUTURE_CHROMOSOME",
                new GenePairData("id/a", "id/b", "id/a", "id/b"));
        GenomeData withExtra = new GenomeData(chromosomes);

        // genome still has SPECIES so it should succeed (unknown key is skipped)
        Optional<Genome> result = GenomeSerializer.fromData(withExtra, BuiltinBeeContent::findAllele);
        assertTrue(result.isPresent(), "Unknown chromosome type should be skipped, not fail");
    }
}
