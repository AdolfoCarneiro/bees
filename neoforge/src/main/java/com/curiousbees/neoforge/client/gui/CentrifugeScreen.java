package com.curiousbees.neoforge.client.gui;

import com.curiousbees.neoforge.menu.CentrifugeMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI screen for the Centrifuge.
 *
 * DEV-PLACEHOLDER: uses vanilla furnace background until final art in E5.
 * Full layout (progress bar, honey counter) completed in E4-T03.
 */
public final class CentrifugeScreen extends AbstractContainerScreen<CentrifugeMenu> {

    // DEV-PLACEHOLDER — vanilla furnace bg until final asset is ready (E5)
    private static final ResourceLocation BG_TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/gui/container/furnace.png");

    public CentrifugeScreen(CentrifugeMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth  = 176;
        this.imageHeight = 166;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(BG_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
}
