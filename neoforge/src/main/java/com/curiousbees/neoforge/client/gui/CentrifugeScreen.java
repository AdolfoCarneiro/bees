package com.curiousbees.neoforge.client.gui;

import com.curiousbees.neoforge.menu.CentrifugeMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

/**
 * GUI for the Centrifuge.
 *
 * <p>Layout (176×166):
 * <pre>
 *  [Comb in] [Bottle in]  → progress arrow →  [3x3 output grid]  [Honey bottle out]
 *                                                                  [Upgrade x3]
 *                                                                  [Honey counter]
 * </pre>
 */
public final class CentrifugeScreen extends AbstractContainerScreen<CentrifugeMenu> {

    // Slot positions must match CentrifugeMenu
    private static final int COMB_SLOT_X         = 30;
    private static final int COMB_SLOT_Y         = 35;
    private static final int BOTTLE_SLOT_X       = 30;
    private static final int BOTTLE_SLOT_Y       = 60;
    private static final int HONEY_BOTTLE_SLOT_X = 150;
    private static final int HONEY_BOTTLE_SLOT_Y = 35;
    private static final int OUTPUT_ORIGIN_X     = 80;
    private static final int OUTPUT_ORIGIN_Y     = 17;
    private static final int UPGRADE_ORIGIN_X    = 7;
    private static final int UPGRADE_ORIGIN_Y    = 17;

    // Progress arrow (between inputs and 3x3 output grid)
    private static final int ARROW_X = 55;
    private static final int ARROW_Y = 34;
    private static final int ARROW_W = 20;
    private static final int ARROW_H = 17;

    // Honey counter: 5 small cells stacked vertically, near honey bottle output
    private static final int HONEY_COL_X    = 153;
    private static final int HONEY_COL_Y    = 60;
    private static final int HONEY_CELL_W   = 8;
    private static final int HONEY_CELL_H   = 8;
    private static final int HONEY_CELL_GAP = 2;
    private static final int HONEY_MAX      = 5;

    // Colors
    private static final int COL_LABEL    = 0x404040;
    private static final int COL_IDLE     = 0x606060;
    private static final int COL_PROGRESS = 0x1A7A1A;
    private static final int COL_HONEY_FULL  = 0xFF_F0C030;
    private static final int COL_HONEY_EMPTY = 0xFF_6B4C0A;
    private static final int COL_ARROW_BG    = 0xFF_888888;
    private static final int COL_ARROW_FILL  = 0xFF_40C040;

    public CentrifugeScreen(CentrifugeMenu menu, Inventory playerInventory, Component title) {
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
        g.fill(leftPos, topPos, leftPos + imageWidth, topPos + imageHeight, 0xFF_C6C6C6);
        g.fill(leftPos + 7, topPos + 7, leftPos + imageWidth - 7, topPos + imageHeight - 7, 0xFF_8B8B8B);
        renderMachineSlots(g);
        renderProgressArrow(g);
        renderHoneyCounter(g);
    }

    private void renderMachineSlots(GuiGraphics g) {
        renderSlotBg(g, COMB_SLOT_X, COMB_SLOT_Y);
        renderSlotBg(g, BOTTLE_SLOT_X, BOTTLE_SLOT_Y);
        renderSlotBg(g, HONEY_BOTTLE_SLOT_X, HONEY_BOTTLE_SLOT_Y);
        for (int i = 0; i < 9; i++) {
            renderSlotBg(g, OUTPUT_ORIGIN_X + (i % 3) * 18, OUTPUT_ORIGIN_Y + (i / 3) * 18);
        }
        for (int i = 0; i < 3; i++) {
            renderSlotBg(g, UPGRADE_ORIGIN_X, UPGRADE_ORIGIN_Y + i * 18);
        }
    }

    private void renderSlotBg(GuiGraphics g, int slotX, int slotY) {
        int x = leftPos + slotX - 1;
        int y = topPos  + slotY - 1;
        g.fill(x, y, x + 18, y + 18, 0xFF_373737);
        g.fill(x + 1, y + 1, x + 17, y + 17, 0xFF_8B8B8B);
    }

    @Override
    protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
        super.renderLabels(g, mouseX, mouseY);
        renderStatusLabel(g);
    }

    private void renderProgressArrow(GuiGraphics g) {
        int x = leftPos + ARROW_X;
        int y = topPos  + ARROW_Y;

        // Background track
        g.fill(x, y, x + ARROW_W, y + ARROW_H, COL_ARROW_BG);

        // Fill proportional to progress
        int progress = menu.processingProgress();
        int total    = menu.processingTotal();
        if (total > 0 && progress > 0) {
            int filled = (int) (ARROW_W * ((float) progress / total));
            g.fill(x, y, x + filled, y + ARROW_H, COL_ARROW_FILL);
        }

        // Arrow tip (simple triangle via overlapping fills)
        g.fill(x + ARROW_W,     y + 4,  x + ARROW_W + 4, y + ARROW_H / 2 + 1, COL_ARROW_BG);
        g.fill(x + ARROW_W,     y + ARROW_H - 4, x + ARROW_W + 4, y + ARROW_H / 2, COL_ARROW_BG);
    }

    private void renderHoneyCounter(GuiGraphics g) {
        int counter = menu.honeyCounter();
        for (int i = 0; i < HONEY_MAX; i++) {
            int cx = leftPos + HONEY_COL_X;
            // Stacked bottom-to-top: index 0 = bottom cell
            int cy = topPos + HONEY_COL_Y + (HONEY_MAX - 1 - i) * (HONEY_CELL_H + HONEY_CELL_GAP);
            int color = (i < counter) ? COL_HONEY_FULL : COL_HONEY_EMPTY;
            g.fill(cx, cy, cx + HONEY_CELL_W, cy + HONEY_CELL_H, color);
            // Cell border
            g.fill(cx, cy, cx + HONEY_CELL_W, cy + 1, 0x88_000000);
            g.fill(cx, cy + HONEY_CELL_H - 1, cx + HONEY_CELL_W, cy + HONEY_CELL_H, 0x88_000000);
            g.fill(cx, cy, cx + 1, cy + HONEY_CELL_H, 0x88_000000);
            g.fill(cx + HONEY_CELL_W - 1, cy, cx + HONEY_CELL_W, cy + HONEY_CELL_H, 0x88_000000);
        }
    }

    private void renderStatusLabel(GuiGraphics g) {
        int progress = menu.processingProgress();
        int total    = menu.processingTotal();
        Component status = (total > 0 && progress > 0)
                ? Component.translatable("gui.curiousbees.centrifuge.processing")
                : Component.translatable("gui.curiousbees.centrifuge.idle");
        int color = (total > 0 && progress > 0) ? COL_PROGRESS : COL_IDLE;
        g.drawString(font, status, COMB_SLOT_X, COMB_SLOT_Y + 20, color, false);

        // Honey counter label — shown below the honey counter column
        g.drawString(font,
                Component.translatable("gui.curiousbees.centrifuge.honey", menu.honeyCounter()),
                HONEY_COL_X - 30, HONEY_COL_Y + HONEY_MAX * (HONEY_CELL_H + HONEY_CELL_GAP) + 2,
                0xFF_B08020, false);
    }
}
