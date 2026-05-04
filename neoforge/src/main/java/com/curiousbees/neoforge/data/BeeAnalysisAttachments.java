package com.curiousbees.neoforge.data;

import com.curiousbees.CuriousBeesMod;
import com.mojang.serialization.Codec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

/** Registers persistent per-bee data attachments related to analysis state. */
public final class BeeAnalysisAttachments {

    private BeeAnalysisAttachments() {}

    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, CuriousBeesMod.MOD_ID);

    /**
     * Stores whether this bee has been analyzed by the portable analyzer.
     * Default: false (unanalyzed). Persisted to NBT via Codec.BOOL.
     * New bees (wild spawn, offspring) start unanalyzed.
     */
    public static final DeferredHolder<AttachmentType<?>, AttachmentType<Boolean>> ANALYZED =
            ATTACHMENT_TYPES.register("analyzed",
                    () -> AttachmentType.<Boolean>builder(() -> false)
                            .serialize(Codec.BOOL)
                            .build());

    public static void register(IEventBus modEventBus) {
        ATTACHMENT_TYPES.register(modEventBus);
    }
}
