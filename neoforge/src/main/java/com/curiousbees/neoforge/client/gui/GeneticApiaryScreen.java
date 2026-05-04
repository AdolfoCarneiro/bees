package com.curiousbees.neoforge.client.gui;

import com.curiousbees.neoforge.menu.GeneticApiaryMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * Gui for the genetic apiary: dispenser-style slot layout plus honey / occupancy readouts.
 */
public final class GeneticApiaryScreen extends AbstractContainerScreen<GeneticApiaryMenu> {

    private static final ResourceLocation DISPENSER_LOCATION =
            ResourceLocation.withDefaultNamespace("textures/gui/container/dispenser.png");

    public GeneticApiaryScreen(GeneticApiaryMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth = 176;
        imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        titleLabelY = 8;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        guiGraphics.blit(DISPENSER_LOCATION, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);
        int honey = menu.honeyLevel();
        guiGraphics.drawString(
                font,
                Component.translatable("gui.curiousbees.genetic_apiary.honey", honey, 5),
                8,
                70,
                0x404040,
                false);
        Component occ =
                menu.hasOccupants()
                        ? Component.translatable("gui.curiousbees.genetic_apiary.occupants_yes")
                        : Component.translatable("gui.curiousbees.genetic_apiary.occupants_no");
        guiGraphics.drawString(font, occ, 8, 82, 0x404040, false);
    }
}
