package com.curiousbees.neoforge.client.gui;

import com.curiousbees.common.content.species.BeeSpeciesDefinition;
import com.curiousbees.common.content.visual.SpeciesVisualDefinition;
import com.curiousbees.neoforge.block.ApiaryState;
import com.curiousbees.neoforge.block.BeeOccupantData;
import com.curiousbees.neoforge.block.GeneticApiaryBlockEntity;
import com.curiousbees.neoforge.client.texture.SpeciesTextureResolver;
import com.curiousbees.neoforge.content.NeoForgeContentRegistry;
import com.curiousbees.neoforge.menu.AdvancedApiaryMenu;
import com.curiousbees.neoforge.menu.GeneticApiaryMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * GUI for the Genetic Apiary (and Advanced Apiary).
 * Layout: bee-panel left | 3×3 output centre | 3×1 frame column right.
 */
public final class GeneticApiaryScreen extends AbstractContainerScreen<GeneticApiaryMenu> {

    private static final ResourceLocation BG_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("curiousbees", "textures/gui/genetic_apiary.png");

    // Layout constants (relative to GUI origin / leftPos,topPos)
    private static final int BEE_PANEL_X = 7;
    private static final int BEE_PANEL_Y = 17;
    private static final int BEE_PANEL_W = 52;
    private static final int BEE_PANEL_H = 56;

    private static final int HONEY_BAR_X = 62;
    private static final int HONEY_BAR_Y = 73;  // moved down from 57 to clear 3rd output row (ADR-0018)
    private static final int HONEY_BAR_W = 54;
    private static final int HONEY_BAR_H = 6;

    private static final int FRAME_ORIGIN_X = 122;
    private static final int FRAME_ORIGIN_Y = 17;
    private static final int DUR_BAR_W = 16;
    private static final int DUR_BAR_H = 3;

    private static final int OCCUPANT_ICON_SIZE   = 8;
    private static final int BEE_SLOT_H           = 16;
    private static final int BEE_SLOT_GAP         = 2;
    private static final int BEE_SLOT_TEXT_X_OFFSET = 11;

    private static final int COL_LABEL      = 0x404040;
    private static final int COL_WARN       = 0x8B2020;
    private static final int COL_HONEY      = 0xB08020;
    private static final int COL_ANALYZED   = 0x1A7A1A;
    private static final int COL_UNANALYZED = 0x806020;

    // Set during renderBeePanel; consumed after super.render() to draw tooltip outside translated matrix.
    private BeeOccupantData hoveredBee = null;
    private int tooltipMouseX = 0;
    private int tooltipMouseY = 0;

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
        hoveredBee = null;
        super.render(g, mouseX, mouseY, partialTick);
        renderTooltip(g, mouseX, mouseY);
        if (hoveredBee != null) {
            // Matrix is NOT translated here — use absolute coords.
            g.renderComponentTooltip(font, buildBeeTooltip(hoveredBee), tooltipMouseX, tooltipMouseY);
        }
    }

    @Override
    protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
        int x = (width  - imageWidth)  / 2;
        int y = (height - imageHeight) / 2;
        g.blit(BG_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        renderBeePanelBg(g, x, y);
        if (menu instanceof AdvancedApiaryMenu) {
            renderBeeInsertSlot(g, x, y);
        }
        renderHoneyBar(g, x, y);
        renderFrameDurabilityBars(g, x, y);
    }

    private void renderBeePanelBg(GuiGraphics g, int ox, int oy) {
        int x = ox + BEE_PANEL_X;
        int y = oy + BEE_PANEL_Y;
        g.fill(x, y, x + BEE_PANEL_W, y + BEE_PANEL_H, 0x22_000000);
        g.fill(x,                   y,                   x + BEE_PANEL_W,     y + 1,             0x55_000000);
        g.fill(x,                   y + BEE_PANEL_H - 1, x + BEE_PANEL_W,     y + BEE_PANEL_H,   0x55_000000);
        g.fill(x,                   y,                   x + 1,               y + BEE_PANEL_H,   0x55_000000);
        g.fill(x + BEE_PANEL_W - 1, y,                   x + BEE_PANEL_W,     y + BEE_PANEL_H,   0x55_000000);
    }

    private void renderBeeInsertSlot(GuiGraphics g, int ox, int oy) {
        int x = ox + AdvancedApiaryMenu.BEE_INSERT_SLOT_X - 1;
        int y = oy + AdvancedApiaryMenu.BEE_INSERT_SLOT_Y - 1;
        g.fill(x,      y,      x + 18, y + 18, 0x66_FFFFFF);
        g.fill(x + 1,  y + 1,  x + 17, y + 17, 0x55_000000);
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
        // Skip super.renderLabels: inventory label at y=72 conflicts with honey bar at y=73 (ADR-0018).
        g.drawString(font, title, titleLabelX, titleLabelY, COL_LABEL, false);
        renderBeePanel(g, mouseX - leftPos, mouseY - topPos);
        renderHoneyLabel(g);
    }

    private void renderBeePanel(GuiGraphics g, int relMouseX, int relMouseY) {
        List<BeeOccupantData> occupants = menu.getOccupants();
        int px = BEE_PANEL_X + 2;

        for (int i = 0; i < 3; i++) {
            int slotY = BEE_PANEL_Y + 2 + i * (BEE_SLOT_H + BEE_SLOT_GAP);

            g.fill(px, slotY, px + BEE_PANEL_W - 4, slotY + BEE_SLOT_H, 0x22_000000);

            if (i >= occupants.size()) {
                g.drawString(font,
                        Component.translatable("gui.curiousbees.genetic_apiary.bee_slot_empty"),
                        px + 3, slotY + 4, 0x606060, false);
                continue;
            }

            BeeOccupantData bee = occupants.get(i);

            // Species icon — always shown, no analysis gate (ADR-0016)
            ResourceLocation icon = SpeciesTextureResolver.resolveById(bee.speciesId());
            g.blit(icon, px + 1, slotY + 4, 0, 0, OCCUPANT_ICON_SIZE, OCCUPANT_ICON_SIZE, 64, 64);

            // Species name — green if purebred, orange if hybrid
            Component name = resolveDisplayName(bee.speciesId());
            int nameColor = bee.isPurebred() ? COL_ANALYZED : COL_UNANALYZED;
            g.drawString(font, name, px + BEE_SLOT_TEXT_X_OFFSET, slotY + 4, nameColor, false);

            // Track hover; tooltip rendered from render() outside translated matrix
            if (relMouseX >= px && relMouseX < px + BEE_PANEL_W - 4
                    && relMouseY >= slotY && relMouseY < slotY + BEE_SLOT_H) {
                hoveredBee = bee;
                tooltipMouseX = relMouseX + leftPos;
                tooltipMouseY = relMouseY + topPos;
            }
        }

        if (menu.getState() == ApiaryState.OUTPUT_FULL) {
            // Overlay on bottom of panel — rendered last so it appears on top
            int wy = BEE_PANEL_Y + BEE_PANEL_H - 11;
            g.fill(px, wy - 1, px + BEE_PANEL_W - 4, wy + 9, 0xCC_000000);
            g.drawString(font,
                    Component.translatable("gui.curiousbees.genetic_apiary.output_full"),
                    px + 2, wy, COL_WARN, false);
        }
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
            String prod = formatSpeciesLabel(bee.productivityId());
            lines.add(Component.translatable("screen.curiousbees.bee_analyzer.label.productivity")
                    .append(Component.literal(": " + prod)));
        }
        if (!bee.flowerTypeId().isEmpty()) {
            String flower = formatSpeciesLabel(bee.flowerTypeId());
            lines.add(Component.translatable("screen.curiousbees.bee_analyzer.label.flower_type")
                    .append(Component.literal(": " + flower)));
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

    private void renderHoneyLabel(GuiGraphics g) {
        g.drawString(font,
                Component.translatable("gui.curiousbees.genetic_apiary.honey",
                        menu.honeyLevel(), 5),
                HONEY_BAR_X, HONEY_BAR_Y + HONEY_BAR_H + 2, COL_HONEY, false);
    }

    private static String formatSpeciesLabel(String speciesId) {
        int slash = speciesId.lastIndexOf('/');
        String name = slash >= 0 ? speciesId.substring(slash + 1) : speciesId;
        if (name.isEmpty()) return "?";
        return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
}
