package com.curiousbees.neoforge.client.render;

import com.curiousbees.neoforge.client.texture.SpeciesTextureResolver;
import com.curiousbees.neoforge.data.BeeAnalysisStorage;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.BeeModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.animal.Bee;

/**
 * Renders a desaturated overlay on bees that have not yet been analyzed.
 * Uses a constant tint colour — no per-species logic.
 * Skipped for vanilla bees (no mod genome).
 */
public final class UnanalyzedTintLayer extends RenderLayer<Bee, BeeModel<Bee>> {

    private static final int UNANALYZED_TINT = 0xFF_88_88_88;

    public UnanalyzedTintLayer(RenderLayerParent<Bee, BeeModel<Bee>> parent) {
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
        if (BeeAnalysisStorage.isAnalyzed(bee)) return;
        var tex = SpeciesTextureResolver.resolve(bee);
        if (tex.equals(SpeciesTextureResolver.VANILLA_FALLBACK)) return;

        BeeModel<Bee> model = getParentModel();
        model.setupAnim(bee, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
        VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(tex));
        model.renderToBuffer(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY, UNANALYZED_TINT);
    }
}
