package com.curiousbees.neoforge.data;

import com.curiousbees.common.content.builtin.BuiltinBeeContent;
import com.curiousbees.common.genetics.model.Genome;
import com.curiousbees.common.genetics.serial.GenomeData;
import com.curiousbees.common.genetics.serial.GenomeSerializer;
import net.minecraft.world.entity.animal.Bee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.Optional;

/**
 * Platform adapter for reading and writing Curious Bees genome data on vanilla Bee entities.
 * All NeoForge/Minecraft interaction is contained here; common genetics code stays platform-free.
 */
public final class BeeGenomeStorage {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeeGenomeStorage.class);

    private BeeGenomeStorage() {}

    /** Returns true if this bee entity has a Curious Bees genome attached. */
    public static boolean hasGenome(Bee bee) {
        Objects.requireNonNull(bee, "bee must not be null");
        return bee.hasData(BeeGenomeAttachments.BEE_GENOME)
                && bee.getData(BeeGenomeAttachments.BEE_GENOME) != null;
    }

    /**
     * Returns the genome attached to this bee, or empty if none is present or deserialization fails.
     * Never throws; failures are logged as warnings.
     */
    public static Optional<Genome> getGenome(Bee bee) {
        Objects.requireNonNull(bee, "bee must not be null");
        if (!hasGenome(bee)) {
            return Optional.empty();
        }
        GenomeData data = bee.getData(BeeGenomeAttachments.BEE_GENOME);
        Optional<Genome> genome = GenomeSerializer.fromData(data, BuiltinBeeContent::findAllele);
        if (genome.isEmpty()) {
            LOGGER.warn("Failed to deserialize genome for bee {} — returning empty.", bee.getUUID());
        }
        return genome;
    }

    /**
     * Attaches a genome to this bee entity.
     * The genome is serialized to GenomeData and stored via the NeoForge attachment system.
     */
    public static void setGenome(Bee bee, Genome genome) {
        Objects.requireNonNull(bee,    "bee must not be null");
        Objects.requireNonNull(genome, "genome must not be null");
        GenomeData data = GenomeSerializer.toData(genome);
        bee.setData(BeeGenomeAttachments.BEE_GENOME, data);
    }

    /** Removes the genome attachment from this bee entity. Useful for debug/test only. */
    public static void clearGenome(Bee bee) {
        Objects.requireNonNull(bee, "bee must not be null");
        bee.removeData(BeeGenomeAttachments.BEE_GENOME);
    }
}
