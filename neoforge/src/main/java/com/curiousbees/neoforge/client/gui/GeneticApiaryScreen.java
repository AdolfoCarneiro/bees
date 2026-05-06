package com.curiousbees.neoforge.client.gui;

import com.curiousbees.neoforge.menu.GeneticApiaryMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI for the Genetic Beehive: frame slots, output slots, honey/occupancy readouts,
 * associated bee summary respecting analyzed state.
 *
 * DEV-PLACEHOLDER: uses vanilla dispenser texture as background. Final asset tracked outside
 * the repo (see CLAUDE.md Art section).
 */
public final class GeneticApiaryScreen extends AbstractContainerScreen<GeneticApiaryMenu> {

    // DEV-PLACEHOLDER — vanilla dispenser bg until final asset is ready
    private static final ResourceLocation BG_TEXTURE =
            ResourceLocation.withDefaultNamespace("textures/gui/container/dispenser.png");

    private static final int COL_LABEL  = 0x404040;
    private static final int COL_VALUE  = 0x1A5E1A;
    private static final int COL_WARN   = 0x8B4513;
    private static final int COL_HONEY  = 0xB08020;

    public GeneticApiaryScreen(GeneticApiaryMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth  = 176;
        imageHeight = 166;
    }

    @Override
    protected void init() {
        super.init();
        titleLabelY = 6;
        inventoryLabelY = imageHeight - 94;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);
        renderTooltip(g, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        int x = (width  - imageWidth)  / 2;
        int y = (height - imageHeight) / 2;
        g.blit(BG_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        // Honey progress bar (below frame row, 5-cell wide)
        renderHoneyBar(g, x, y);
    }

    private void renderHoneyBar(GuiGraphics g, int originX, int originY) {
        int honey = menu.honeyLevel();
        int barX = originX + 8;
        int barY = originY + 37;
        int barW = 50;
        int barH = 4;
        // Background
        g.fill(barX, barY, barX + barW, barY + barH, 0xFF_6B4C0A);
        // Fill
        int filled = (int) (barW * (honey / 5.0f));
        if (filled > 0) {
            g.fill(barX, barY, barX + filled, barY + barH, 0xFF_F0C030);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
        // Title (rendered by super through titleLabelY)
        super.renderLabels(g, mouseX, mouseY);

        // Honey label
        int honey = menu.honeyLevel();
        g.drawString(font,
                Component.translatable("gui.curiousbees.genetic_apiary.honey", honey, 5),
                8, 46, COL_HONEY, false);

        // Bee summary
        renderBeeSummary(g);

        // Frame summary
        renderFrameSummary(g);
    }

    private void renderBeeSummary(GuiGraphics g) {
        int total    = menu.homedBeeCount();
        int analyzed = menu.analyzedBeeCount();
        int unanalyzed = total - analyzed;

        if (total == 0) {
            g.drawString(font,
                    Component.translatable("gui.curiousbees.genetic_apiary.no_bees"),
                    8, 58, COL_WARN, false);
        } else {
            g.drawString(font,
                    Component.translatable("gui.curiousbees.genetic_apiary.bees", total),
                    8, 58, COL_LABEL, false);
            if (analyzed > 0) {
                g.drawString(font,
                        Component.translatable("gui.curiousbees.genetic_apiary.analyzed", analyzed),
                        8, 68, COL_VALUE, false);
            }
            if (unanalyzed > 0) {
                g.drawString(font,
                        Component.translatable("gui.curiousbees.genetic_apiary.unanalyzed", unanalyzed),
                        8, unanalyzed > 0 && analyzed > 0 ? 78 : 68, COL_WARN, false);
            }
        }
    }

    private void renderFrameSummary(GuiGraphics g) {
        // Count installed frames
        int framesInstalled = 0;
        for (int i = 0; i < com.curiousbees.neoforge.block.GeneticApiaryBlockEntity.FRAME_SLOTS; i++) {
            if (!menu.getSlot(i).getItem().isEmpty()) framesInstalled++;
        }
        if (framesInstalled > 0) {
            g.drawString(font,
                    Component.translatable("gui.curiousbees.genetic_apiary.frames", framesInstalled),
                    8, 88, COL_LABEL, false);
        }
    }
}
