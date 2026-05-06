package com.curiousbees.neoforge.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

import java.util.List;

/**
 * Simple in-house guide book screen for Curious Bees.
 * Content is driven by translation keys — adding pages requires no Java changes.
 */
public final class CuriousBeesGuideScreen extends Screen {

    private static final int PANEL_W = 256;
    private static final int PANEL_H = 190;
    private static final int PAD     = 12;
    private static final int LINE_H  = 11;

    // Palette — matches BeeAnalyzerScreen honey-gold theme
    private static final int BG_COLOR     = 0xEE_1A1006;
    private static final int BORDER_COL   = 0xFF_8B6914;
    private static final int TITLE_COL    = 0xFF_FFD860;
    private static final int HEADER_COL   = 0xFF_F0C030;
    private static final int BODY_COL     = 0xFF_F0E0A0;
    private static final int DIM_COL      = 0xFF_A08040;
    private static final int PAGE_NUM_COL = 0xFF_806020;

    /** Each page is a list of translation-key strings rendered top-to-bottom. */
    private static final List<List<String>> PAGES = List.of(
        // Page 0 — Hello / core loop
        List.of(
            "screen.curiousbees.guide.p0.intro",
            "",
            "screen.curiousbees.guide.p0.step1",
            "screen.curiousbees.guide.p0.step2",
            "screen.curiousbees.guide.p0.step3",
            "screen.curiousbees.guide.p0.step4",
            "screen.curiousbees.guide.p0.step5",
            "",
            "screen.curiousbees.guide.p0.tip"
        ),
        // Page 1 — Genetics primer
        List.of(
            "screen.curiousbees.guide.p1.intro",
            "",
            "screen.curiousbees.guide.p1.dominant",
            "screen.curiousbees.guide.p1.recessive",
            "screen.curiousbees.guide.p1.mutation",
            "",
            "screen.curiousbees.guide.p1.tip"
        )
    );

    private static final List<String> PAGE_TITLES = List.of(
        "screen.curiousbees.guide.title.p0",
        "screen.curiousbees.guide.title.p1"
    );

    private int currentPage = 0;
    private Button prevButton;
    private Button nextButton;

    public CuriousBeesGuideScreen() {
        super(Component.translatable("screen.curiousbees.guide"));
    }

    @Override
    protected void init() {
        super.init();
        int ox = (width  - PANEL_W) / 2;
        int oy = (height - PANEL_H) / 2;

        prevButton = Button.builder(
                Component.literal("◀"),
                b -> { if (currentPage > 0) currentPage--; updateButtons(); })
                .pos(ox + PAD, oy + PANEL_H - PAD - 20)
                .size(24, 20)
                .build();

        nextButton = Button.builder(
                Component.literal("▶"),
                b -> { if (currentPage < PAGES.size() - 1) currentPage++; updateButtons(); })
                .pos(ox + PANEL_W - PAD - 24, oy + PANEL_H - PAD - 20)
                .size(24, 20)
                .build();

        addRenderableWidget(prevButton);
        addRenderableWidget(nextButton);
        updateButtons();
    }

    private void updateButtons() {
        prevButton.active = currentPage > 0;
        nextButton.active = currentPage < PAGES.size() - 1;
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        super.render(g, mouseX, mouseY, partialTick);

        int ox = (width  - PANEL_W) / 2;
        int oy = (height - PANEL_H) / 2;

        // Background + border
        g.fill(ox, oy, ox + PANEL_W, oy + PANEL_H, BG_COLOR);
        g.fill(ox,              oy,              ox + PANEL_W, oy + 1,          BORDER_COL);
        g.fill(ox,              oy + PANEL_H - 1, ox + PANEL_W, oy + PANEL_H,  BORDER_COL);
        g.fill(ox,              oy,              ox + 1,        oy + PANEL_H,   BORDER_COL);
        g.fill(ox + PANEL_W - 1, oy,             ox + PANEL_W, oy + PANEL_H,   BORDER_COL);

        // Mod title
        String modTitle = "✦ " + I18n.get("screen.curiousbees.guide") + " ✦";
        int titleX = ox + (PANEL_W - font.width(modTitle)) / 2;
        g.drawString(font, modTitle, titleX, oy + PAD, TITLE_COL);

        // Page title
        String pageTitle = I18n.get(PAGE_TITLES.get(currentPage));
        int pageTitleX = ox + (PANEL_W - font.width(pageTitle)) / 2;
        g.drawString(font, pageTitle, pageTitleX, oy + PAD + LINE_H + 2, HEADER_COL);

        // Separator
        g.fill(ox + PAD, oy + PAD + LINE_H * 2 + 4, ox + PANEL_W - PAD, oy + PAD + LINE_H * 2 + 5, BORDER_COL);

        // Page body
        int lineY = oy + PAD + LINE_H * 2 + 10;
        for (String key : PAGES.get(currentPage)) {
            if (key.isEmpty()) {
                lineY += LINE_H / 2;
                continue;
            }
            String text = I18n.get(key);
            // Word-wrap to panel width - 2*PAD
            int maxW = PANEL_W - PAD * 2;
            for (var wrapped : font.split(net.minecraft.network.chat.Component.literal(text), maxW)) {
                g.drawString(font, wrapped, ox + PAD, lineY, BODY_COL, false);
                lineY += LINE_H;
            }
        }

        // Page number
        String pageNum = (currentPage + 1) + " / " + PAGES.size();
        int numX = ox + (PANEL_W - font.width(pageNum)) / 2;
        g.drawString(font, pageNum, numX, oy + PANEL_H - PAD - 22, PAGE_NUM_COL, false);

        // ESC hint
        g.drawString(font, "[ESC]", ox + PAD, oy + PANEL_H - PAD - font.lineHeight, DIM_COL, false);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
