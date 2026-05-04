package com.curiousbees.neoforge.client;

import com.curiousbees.neoforge.client.gui.GeneticApiaryScreen;
import com.curiousbees.neoforge.client.render.CuriousBeeBeeRenderer;
import com.curiousbees.neoforge.item.CuriousBeeSpeciesSpawnEggItem;
import com.curiousbees.neoforge.registry.ModItems;
import com.curiousbees.neoforge.registry.ModMenuTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

/**
 * Client-side event handlers for Curious Bees.
 * Methods are registered programmatically on the MOD event bus from CuriousBeesMod,
 * inside a client-only dist check.
 */
public final class ClientEventHandler {

    private ClientEventHandler() {}

    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityType.BEE, CuriousBeeBeeRenderer::new);
    }

    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenuTypes.GENETIC_APIARY.get(), GeneticApiaryScreen::new);
    }

    public static void onRegisterItemColors(RegisterColorHandlersEvent.Item event) {
        event.register(
                (stack, tintIndex) -> {
                    if (stack.getItem() instanceof CuriousBeeSpeciesSpawnEggItem egg) {
                        return egg.eggTint(tintIndex);
                    }
                    return 0xFFFFFFFF;
                },
                ModItems.BEE_SPAWN_EGGS.stream().map(h -> (Item) h.get()).toArray(Item[]::new));
    }
}
