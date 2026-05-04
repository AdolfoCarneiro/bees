package com.curiousbees.neoforge.client.screen;

import com.curiousbees.common.gameplay.analysis.BeeAnalysisReport;
import com.curiousbees.common.gameplay.analysis.GeneReport;
import com.curiousbees.common.genetics.model.Dominance;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Client-side overlay screen for the portable Bee Analyzer.
 * Opened via a network packet — no container/menu required.
 * Displays the BeeAnalysisReport with active/inactive alleles and purity.
 */
public final class BeeAnalyzerScreen extends Screen {

    // DEV PLACEHOLDER background — awaiting final asset from docs/art/prompts/gui/analyzer-screen-bg.md
    private static final int BG_COLOR   = 0xE0_1A1006;
    private static final int BORDER_COL = 0xFF_8B6914;
    private static final int TEXT_COL   = 0xFF_FFD860;
    private static final int DIM_COL    = 0xFF_A08040;
    private static final int LABEL_COL  = 0xFF_C8A020;
    private static final int UNKNOWN_COL = 0xFF_808080;

    private static final int PANEL_W = 220;
    private static final int PANEL_H = 148;
    private static final int PAD     = 10;

    private final BeeAnalysisReport report;
    private final List<ReportLine> lines = new ArrayList<>();

    public BeeAnalyzerScreen(BeeAnalysisReport report) {
        super(Component.translatable("screen.curiousbees.bee_analyzer"));
        this.report = report;
    }

    @Override
    protected void init() {
        super.init();
        buildLines();
    }

    private void buildLines() {
        lines.clear();
        if (!report.isAnalyzed()) {
            lines.add(new ReportLine("Genetics", "Unknown — Analyze Required", UNKNOWN_COL));
            return;
        }
        lines.add(speciesLine());
        lines.add(traitLine("Lifespan",     report.lifespan()));
        lines.add(traitLine("Productivity", report.productivity()));
        lines.add(traitLine("Fertility",    report.fertility()));
        lines.add(traitLine("Flower",       report.flowerType()));
    }

    private ReportLine speciesLine() {
        GeneReport gene = report.species();
        String active   = displayName(gene.activeAlleleId());
        String inactive = displayName(gene.inactiveAlleleId());
        String dom      = gene.activeDominance() == Dominance.DOMINANT ? "(D)" : "(R)";
        String purity   = gene.isPurebred() ? "Purebred" : "Hybrid";
        return new ReportLine("Species", active + " " + dom + " / " + inactive + "  [" + purity + "]", TEXT_COL);
    }

    private ReportLine traitLine(String label, GeneReport gene) {
        String active   = displayName(gene.activeAlleleId());
        String inactive = displayName(gene.inactiveAlleleId());
        return new ReportLine(label, "[A] " + active + "  [I] " + inactive, DIM_COL);
    }

    private static String displayName(String alleleId) {
        int slash = alleleId.lastIndexOf('/');
        String raw = slash >= 0 ? alleleId.substring(slash + 1) : alleleId;
        String spaced = raw.replace('_', ' ');
        if (spaced.isEmpty()) return spaced;
        return Character.toUpperCase(spaced.charAt(0)) + spaced.substring(1);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        // super.render calls renderBackground once; drawing the panel before super would get blurred again.
        super.render(g, mouseX, mouseY, partialTick);

        int x = (width  - PANEL_W) / 2;
        int y = (height - PANEL_H) / 2;

        // Background panel
        g.fill(x, y, x + PANEL_W, y + PANEL_H, BG_COLOR);
        // Border
        g.fill(x, y, x + PANEL_W, y + 1, BORDER_COL);
        g.fill(x, y + PANEL_H - 1, x + PANEL_W, y + PANEL_H, BORDER_COL);
        g.fill(x, y, x + 1, y + PANEL_H, BORDER_COL);
        g.fill(x + PANEL_W - 1, y, x + PANEL_W, y + PANEL_H, BORDER_COL);

        // Title
        g.drawString(font, "=== Bee Genetics ===", x + PAD, y + PAD, LABEL_COL);

        // Lines
        int lineY = y + PAD + 14;
        for (ReportLine line : lines) {
            g.drawString(font, line.label + ":", x + PAD, lineY, LABEL_COL, false);
            g.drawString(font, line.value, x + PAD + 72, lineY, line.color, false);
            lineY += 11;
        }

        // Dismiss hint
        g.drawString(font, "[ESC] close", x + PAD, y + PANEL_H - PAD - font.lineHeight, DIM_COL, false);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private record ReportLine(String label, String value, int color) {}
}
