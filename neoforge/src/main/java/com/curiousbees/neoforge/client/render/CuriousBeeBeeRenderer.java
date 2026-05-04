package com.curiousbees.neoforge.client.render;

import com.curiousbees.neoforge.client.texture.SpeciesTextureResolver;
import net.minecraft.client.renderer.entity.BeeRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Bee;

/**
 * Custom renderer for vanilla Bee entities that selects texture by active species genome.
 * Extends the vanilla BeeRenderer so all vanilla states (angry, nectar) are preserved.
 * When a species texture is not available, falls back to the vanilla bee texture.
 */
public final class CuriousBeeBeeRenderer extends BeeRenderer {

    public CuriousBeeBeeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Bee bee) {
        ResourceLocation resolved = SpeciesTextureResolver.resolve(bee);
        if (!resolved.equals(SpeciesTextureResolver.VANILLA_FALLBACK)) {
            // Species texture or mod fallback — use directly.
            return resolved;
        }
        // No mod genome: delegate to vanilla so angry/nectar variants still work.
        return super.getTextureLocation(bee);
    }
}
