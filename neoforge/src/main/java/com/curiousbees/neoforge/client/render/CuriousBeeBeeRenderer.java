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
        ResourceLocation species = SpeciesTextureResolver.resolve(bee);
        if (!species.equals(SpeciesTextureResolver.VANILLA_FALLBACK)) {
            return species;
        }
        // Delegate to vanilla so angry/nectar textures still work for unregistered bees.
        return super.getTextureLocation(bee);
    }
}
