package com.curiousbees.neoforge.registry;

import com.curiousbees.CuriousBeesMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModSounds {

    private ModSounds() {}

    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(Registries.SOUND_EVENT, CuriousBeesMod.MOD_ID);

    /** Played periodically while bees are working inside an apiary. */
    public static final DeferredHolder<SoundEvent, SoundEvent> APIARY_WORK =
            SOUNDS.register("block.apiary.work",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(CuriousBeesMod.MOD_ID, "block.apiary.work")));

    /** Played when the centrifuge finishes processing a batch. */
    public static final DeferredHolder<SoundEvent, SoundEvent> CENTRIFUGE_WORK =
            SOUNDS.register("block.centrifuge.work",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(CuriousBeesMod.MOD_ID, "block.centrifuge.work")));

    /** Played when the analyzer is used on a bee. */
    public static final DeferredHolder<SoundEvent, SoundEvent> ANALYZER_USE =
            SOUNDS.register("item.analyzer.use",
                    () -> SoundEvent.createVariableRangeEvent(
                            ResourceLocation.fromNamespaceAndPath(CuriousBeesMod.MOD_ID, "item.analyzer.use")));

    public static void register(IEventBus modEventBus) {
        SOUNDS.register(modEventBus);
    }
}
