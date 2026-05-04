package com.curiousbees.neoforge.client.render;

import com.curiousbees.neoforge.client.texture.SpeciesTextureResolver;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.BeeModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Bee;

/**
 * Draws vanilla {@code bee_nectar.png} on top of species-skinned bees so carrying nectar stays visible
 * without per-species nectar textures.
 */
public final class CuriousBeePollenLayer extends RenderLayer<Bee, BeeModel<Bee>> {

    private static final ResourceLocation VANILLA_NECTAR_TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/entity/bee/bee_nectar.png");

    public CuriousBeePollenLayer(RenderLayerParent<Bee, BeeModel<Bee>> parent) {
        super(parent);
    }

    @Override
    public void render(
            PoseStack poseStack,
            MultiBufferSource buffer,
            int packedLight,
            Bee bee,
            float limbSwing,
            float limbSwingAmount,
            float partialTick,
            float ageInTicks,
            float netHeadYaw,
            float headPitch) {
        if (!bee.hasNectar()) {
            return;
        }
        if (SpeciesTextureResolver.resolve(bee).equals(SpeciesTextureResolver.VANILLA_FALLBACK)) {
            return;
        }
        BeeModel<Bee> model = getParentModel();
        model.setupAnim(bee, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(VANILLA_NECTAR_TEXTURE));
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, -1);
    }
}
