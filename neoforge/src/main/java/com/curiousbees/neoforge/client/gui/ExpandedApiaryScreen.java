package com.curiousbees.neoforge.client.gui;

import com.curiousbees.common.content.species.BeeSpeciesDefinition;
import com.curiousbees.common.content.visual.SpeciesVisualDefinition;
import com.curiousbees.neoforge.block.ApiaryState;
import com.curiousbees.neoforge.block.BeeOccupantData;
import com.curiousbees.neoforge.block.GeneticApiaryBlockEntity;
import com.curiousbees.neoforge.client.texture.SpeciesTextureResolver;
import com.curiousbees.neoforge.content.NeoForgeContentRegistry;
import com.curiousbees.neoforge.menu.ExpandedApiaryMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * GUI for the Advanced Apiary when the Beehive Expansion Box is installed below.
 * DEV-PLACEHOLDER: background drawn programmatically; no custom texture yet.
 * imageHeight=190 to fit 7-slot bee panel and shifted player inventory.
 */
public final class ExpandedApiaryScreen extends AbstractContainerScreen<ExpandedApiaryMenu> {

    // Bee panel (7 compact slots)
    private static final int BEE_PANEL_X  = 7;
    private static final int BEE_PANEL_Y  = 17;
    private static final int BEE_PANEL_W  = 52;
    private static final int BEE_PANEL_H  = 76;
    private static final int BEE_SLOT_H   = 9;
    private static final int BEE_SLOT_GAP = 1;
    private static final int BEE_SLOT_TEXT_X_OFFSET = 11;
    private static final int OCCUPANT_ICON_SIZE = 8;

    private static final int HONEY_BAR_X  = 62;
    private static final int HONEY_BAR_Y  = 93;
    private static final int HONEY_BAR_W  = 54;
    private static final int HONEY_BAR_H  = 6;

    private static final int FRAME_ORIGIN_X = 122;
    private static final int FRAME_ORIGIN_Y = 17;
    private static final int DUR_BAR_W = 16;
    private static final int DUR_BAR_H = 3;

    private static final int COL_LABEL      = 0x404040;
    private static final int COL_WARN       = 0x8B2020;
    private static final int COL_HONEY      = 0xB08020;
    private static final int COL_ANALYZED   = 0x1A7A1A;
    private static final int COL_UNANALYZED = 0x806020;

    private BeeOccupantData hoveredBee = null;
    private int tooltipMouseX = 0;
    private int tooltipMouseY = 0;

    public ExpandedApiaryScreen(ExpandedApiaryMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageWidth  = 176;
        imageHeight = 190;
    }

    @Override
    protected void init() {
        super.init();
        titleLabelY = 6;
        inventoryLabelY = imageHeight - 94;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        hoveredBee = null;
        super.render(g, mouseX, mouseY, partialTick);
        renderTooltip(g, mouseX, mouseY);
        if (hoveredBee != null) {
            g.renderComponentTooltip(font, buildBeeTooltip(hoveredBee), tooltipMouseX, tooltipMouseY);
        }
    }

    @Override
    protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        int x = (width  - imageWidth)  / 2;
        int y = (height - imageHeight) / 2;
        // DEV-PLACEHOLDER: programmatic background
        g.fill(x, y, x + imageWidth, y + imageHeight, 0xFF_C6C6C6);
        g.fill(x + 7, y + 7, x + imageWidth - 7, y + imageHeight - 7, 0xFF_8B8B8B);
        renderBeePanelBg(g, x, y);
        renderBeeInsertSlotHighlight(g, x, y);
        renderHoneyBar(g, x, y);
        renderFrameDurabilityBars(g, x, y);
    }

    private void renderBeePanelBg(GuiGraphics g, int ox, int oy) {
        int px = ox + BEE_PANEL_X;
        int py = oy + BEE_PANEL_Y;
        g.fill(px, py, px + BEE_PANEL_W, py + BEE_PANEL_H, 0x22_000000);
        g.fill(px,                   py,                    px + BEE_PANEL_W, py + 1,             0x55_000000);
        g.fill(px,                   py + BEE_PANEL_H - 1,  px + BEE_PANEL_W, py + BEE_PANEL_H,   0x55_000000);
        g.fill(px,                   py,                    px + 1,           py + BEE_PANEL_H,   0x55_000000);
        g.fill(px + BEE_PANEL_W - 1, py,                    px + BEE_PANEL_W, py + BEE_PANEL_H,   0x55_000000);
    }

    private void renderBeeInsertSlotHighlight(GuiGraphics g, int ox, int oy) {
        int x = ox + ExpandedApiaryMenu.BEE_INSERT_SLOT_X - 1;
        int y = oy + ExpandedApiaryMenu.BEE_INSERT_SLOT_Y - 1;
        g.fill(x,     y,     x + 18, y + 18, 0x66_FFFFFF);
        g.fill(x + 1, y + 1, x + 17, y + 17, 0x55_000000);
    }

    private void renderHoneyBar(GuiGraphics g, int ox, int oy) {
        int honey = menu.honeyLevel();
        int x = ox + HONEY_BAR_X;
        int y = oy + HONEY_BAR_Y;
        g.fill(x, y, x + HONEY_BAR_W, y + HONEY_BAR_H, 0xFF_6B4C0A);
        int filled = (int) (HONEY_BAR_W * (honey / 5.0f));
        if (filled > 0) {
            g.fill(x, y, x + filled, y + HONEY_BAR_H, 0xFF_F0C030);
        }
    }

    private void renderFrameDurabilityBars(GuiGraphics g, int ox, int oy) {
        for (int i = 0; i < GeneticApiaryBlockEntity.FRAME_SLOTS; i++) {
            ItemStack frame = menu.getSlot(i).getItem();
            if (frame.isEmpty()) continue;
            int x = ox + FRAME_ORIGIN_X;
            int y = oy + FRAME_ORIGIN_Y + i * 18 + 15;
            g.fill(x, y, x + DUR_BAR_W, y + DUR_BAR_H, 0xFF_222222);
            int maxDmg = frame.getMaxDamage();
            float frac = maxDmg > 0 ? 1.0f - ((float) frame.getDamageValue() / maxDmg) : 1.0f;
            int fillW = Math.max(1, (int) (DUR_BAR_W * frac));
            int color = frac > 0.5f ? 0xFF_40C040 : frac > 0.25f ? 0xFF_C0C040 : 0xFF_C04040;
            g.fill(x, y, x + fillW, y + DUR_BAR_H, color);
        }
    }

    @Override
    protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
        g.drawString(font, title, titleLabelX, titleLabelY, COL_LABEL, false);
        renderBeePanel(g, mouseX - leftPos, mouseY - topPos);
        renderHoneyLabel(g);
        g.drawString(font,
                Component.translatable("gui.curiousbees.expanded_apiary.upgrades"),
                HONEY_BAR_X, 65, COL_LABEL, false);
    }

    private void renderBeePanel(GuiGraphics g, int relMouseX, int relMouseY) {
        List<BeeOccupantData> occupants = menu.getOccupants();
        int px = BEE_PANEL_X + 2;

        for (int i = 0; i < 7; i++) {
            int slotY = BEE_PANEL_Y + 3 + i * (BEE_SLOT_H + BEE_SLOT_GAP);
            g.fill(px, slotY, px + BEE_PANEL_W - 4, slotY + BEE_SLOT_H, 0x22_000000);

            if (i >= occupants.size()) {
                g.drawString(font,
                        Component.translatable("gui.curiousbees.genetic_apiary.bee_slot_empty"),
                        px + 3, slotY + 1, 0x606060, false);
                continue;
            }

            BeeOccupantData bee = occupants.get(i);
            ResourceLocation icon = SpeciesTextureResolver.resolveById(bee.speciesId());
            g.blit(icon, px + 1, slotY, 0, 0, OCCUPANT_ICON_SIZE, OCCUPANT_ICON_SIZE, 64, 64);

            Component name = resolveDisplayName(bee.speciesId());
            int nameColor = bee.isPurebred() ? COL_ANALYZED : COL_UNANALYZED;
            g.drawString(font, name, px + BEE_SLOT_TEXT_X_OFFSET, slotY + 1, nameColor, false);

            if (relMouseX >= px && relMouseX < px + BEE_PANEL_W - 4
                    && relMouseY >= slotY && relMouseY < slotY + BEE_SLOT_H) {
                hoveredBee = bee;
                tooltipMouseX = relMouseX + leftPos;
                tooltipMouseY = relMouseY + topPos;
            }
        }

        if (menu.getState() == ApiaryState.OUTPUT_FULL) {
            int wy = BEE_PANEL_Y + BEE_PANEL_H - 11;
            g.fill(px, wy - 1, px + BEE_PANEL_W - 4, wy + 9, 0xCC_000000);
            g.drawString(font,
                    Component.translatable("gui.curiousbees.genetic_apiary.output_full"),
                    px + 2, wy, COL_WARN, false);
        }
    }

    private void renderHoneyLabel(GuiGraphics g) {
        g.drawString(font,
                Component.translatable("gui.curiousbees.genetic_apiary.honey",
                        menu.honeyLevel(), 5),
                HONEY_BAR_X, HONEY_BAR_Y + HONEY_BAR_H + 2, COL_HONEY, false);
    }

    private List<Component> buildBeeTooltip(BeeOccupantData bee) {
        List<Component> lines = new ArrayList<>();
        lines.add(resolveDisplayName(bee.speciesId()));
        lines.add(Component.translatable(
                bee.isPurebred()
                        ? "screen.curiousbees.bee_analyzer.purity.purebred"
                        : "screen.curiousbees.bee_analyzer.purity.hybrid").withStyle(
                style -> style.withColor(bee.isPurebred() ? 0x1A7A1A : 0x806020)));
        if (!bee.productivityId().isEmpty()) {
            lines.add(Component.translatable("screen.curiousbees.bee_analyzer.label.productivity")
                    .append(Component.literal(": " + formatSpeciesLabel(bee.productivityId()))));
        }
        if (!bee.flowerTypeId().isEmpty()) {
            lines.add(Component.translatable("screen.curiousbees.bee_analyzer.label.flower_type")
                    .append(Component.literal(": " + formatSpeciesLabel(bee.flowerTypeId()))));
        }
        return lines;
    }

    private Component resolveDisplayName(String speciesId) {
        return NeoForgeContentRegistry.current()
                .findSpecies(speciesId)
                .flatMap(BeeSpeciesDefinition::visualDefinition)
                .flatMap(SpeciesVisualDefinition::displayNameKey)
                .map(Component::translatable)
                .orElseGet(() -> Component.literal(formatSpeciesLabel(speciesId)));
    }

    private static String formatSpeciesLabel(String speciesId) {
        int slash = speciesId.lastIndexOf('/');
        String name = slash >= 0 ? speciesId.substring(slash + 1) : speciesId;
        if (name.isEmpty()) return "?";
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}
