package com.curiousbees.neoforge.data;

import com.curiousbees.CuriousBeesMod;
import com.curiousbees.common.genetics.serial.GenomeData;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/** Registers the persistent genome attachment for vanilla Bee entities. */
public final class BeeGenomeAttachments {

    private BeeGenomeAttachments() {}

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, CuriousBeesMod.MOD_ID);

    /**
     * Stores the serialized genome on a Bee entity.
     * Absent by default (supplier returns null); callers must check hasData() before getData().
     * Persisted automatically to NBT via GenomeCodec.GENOME.
     */
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<GenomeData>> BEE_GENOME =
            ATTACHMENT_TYPES.register("bee_genome",
                    () -> AttachmentType.<GenomeData>builder(() -> null)
                            .serialize(GenomeCodec.GENOME)
                            .build());

    public static void register(IEventBus modEventBus) {
        ATTACHMENT_TYPES.register(modEventBus);
    }
}
