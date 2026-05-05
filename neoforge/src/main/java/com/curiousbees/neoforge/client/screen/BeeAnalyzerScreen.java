package com.curiousbees.neoforge.client.screen;

import com.curiousbees.common.gameplay.analysis.BeeAnalysisReport;
import com.curiousbees.common.gameplay.analysis.GeneReport;
import com.curiousbees.common.genetics.model.Dominance;
import com.curiousbees.neoforge.content.NeoForgeContentRegistry;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Client-side overlay screen for the portable Bee Analyzer.
 * Opened via a network packet — no container/menu required.
 * Displays the BeeAnalysisReport in labelled sections; no raw genome IDs shown.
 * DEV-PLACEHOLDER background — replace with Curious Bees GUI art in E5.
 */
public final class BeeAnalyzerScreen extends Screen {

    private static final int BG_COLOR    = 0xE0_1A1006;
    private static final int BORDER_COL  = 0xFF_8B6914;
    private static final int TITLE_COL   = 0xFF_FFD860;
    private static final int LABEL_COL   = 0xFF_C8A020;
    private static final int VALUE_COL   = 0xFF_F0E0A0;
    private static final int DIM_COL     = 0xFF_A08040;
    private static final int UNKNOWN_COL = 0xFF_808080;
    private static final int HYBRID_COL  = 0xFF_90C0FF;

    private static final int PANEL_W  = 240;
    private static final int PANEL_H  = 160;
    private static final int PAD      = 10;
    private static final int LINE_H   = 11;
    private static final int LABEL_W  = 84;

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
            lines.add(new ReportLine(null, I18n.get("screen.curiousbees.bee_analyzer.unanalyzed"), UNKNOWN_COL));
            return;
        }
        lines.add(speciesLine(report.species()));
        if (!report.isSpeciesPurebred()) {
            lines.add(new ReportLine(null,
                    "» " + I18n.get("screen.curiousbees.bee_analyzer.purity.hybrid"), HYBRID_COL));
        }
        lines.add(traitLine("screen.curiousbees.bee_analyzer.label.lifespan",     report.lifespan()));
        lines.add(traitLine("screen.curiousbees.bee_analyzer.label.productivity", report.productivity()));
        lines.add(traitLine("screen.curiousbees.bee_analyzer.label.fertility",    report.fertility()));
        lines.add(traitLine("screen.curiousbees.bee_analyzer.label.flower_type",  report.flowerType()));
    }

    private ReportLine speciesLine(GeneReport gene) {
        String active   = resolveSpeciesName(gene.activeAlleleId());
        String inactive = resolveSpeciesName(gene.inactiveAlleleId());
        String dom      = gene.activeDominance() == Dominance.DOMINANT ? "(D)" : "(R)";
        String purity   = gene.isPurebred()
                ? I18n.get("screen.curiousbees.bee_analyzer.purity.purebred")
                : I18n.get("screen.curiousbees.bee_analyzer.purity.hybrid");
        String value = active + " " + dom + " / " + inactive + "  [" + purity + "]";
        return new ReportLine("screen.curiousbees.bee_analyzer.label.species", value, VALUE_COL);
    }

    private ReportLine traitLine(String labelKey, GeneReport gene) {
        String active   = traitDisplayName(gene.activeAlleleId());
        String inactive = traitDisplayName(gene.inactiveAlleleId());
        return new ReportLine(labelKey, "[A] " + active + "  [I] " + inactive, DIM_COL);
    }

    /** Species name via registry → visual def → lang key → I18n. Falls back to suffix derivation. */
    private static String resolveSpeciesName(String alleleId) {
        return NeoForgeContentRegistry.current()
                .findSpecies(alleleId)
                .flatMap(s -> s.visualDefinition())
                .flatMap(v -> v.displayNameKey())
                .map(I18n::get)
                .orElseGet(() -> traitDisplayName(alleleId));
    }

    /** Fallback: derive display name from allele ID suffix (used for traits). */
    private static String traitDisplayName(String alleleId) {
        int slash = alleleId.lastIndexOf('/');
        String raw = slash >= 0 ? alleleId.substring(slash + 1) : alleleId;
        String spaced = raw.replace('_', ' ');
        if (spaced.isEmpty()) return spaced;
        return Character.toUpperCase(spaced.charAt(0)) + spaced.substring(1);
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);

        int x = (width  - PANEL_W) / 2;
        int y = (height - PANEL_H) / 2;

        g.fill(x, y, x + PANEL_W, y + PANEL_H, BG_COLOR);
        g.fill(x,            y,            x + PANEL_W, y + 1,          BORDER_COL);
        g.fill(x,            y + PANEL_H - 1, x + PANEL_W, y + PANEL_H, BORDER_COL);
        g.fill(x,            y,            x + 1,       y + PANEL_H,    BORDER_COL);
        g.fill(x + PANEL_W - 1, y,         x + PANEL_W, y + PANEL_H,   BORDER_COL);

        String panelTitle = "=== " + I18n.get("screen.curiousbees.bee_analyzer.panel_title") + " ===";
        g.drawString(font, panelTitle, x + PAD, y + PAD, TITLE_COL);

        int lineY = y + PAD + 14;
        for (ReportLine line : lines) {
            if (line.labelKey() != null) {
                g.drawString(font, I18n.get(line.labelKey()) + ":", x + PAD, lineY, LABEL_COL, false);
                g.drawString(font, line.value(), x + PAD + LABEL_W, lineY, line.color(), false);
            } else {
                g.drawString(font, line.value(), x + PAD, lineY, line.color(), false);
            }
            lineY += LINE_H;
        }

        g.drawString(font, "[ESC]", x + PAD, y + PANEL_H - PAD - font.lineHeight, DIM_COL, false);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private record ReportLine(String labelKey, String value, int color) {}
}
