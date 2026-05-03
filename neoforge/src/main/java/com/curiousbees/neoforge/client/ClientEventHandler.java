package com.curiousbees.neoforge.client;

import com.curiousbees.neoforge.client.render.CuriousBeeBeeRenderer;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

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
}
