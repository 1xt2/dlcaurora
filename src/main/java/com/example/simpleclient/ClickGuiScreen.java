package com.example.simpleclient;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ClickGuiScreen extends Screen {

    private static final int BG = 0xB90A0D14;
    private static final int PANEL = 0xE51A1D26;
    private static final int PANEL_HOVER = 0xF02A2E3A;
    private static final int PANEL_ACTIVE = 0xF0252A3B;
    private static final int HEADER = 0xE91A1D26;
    private static final int BORDER = 0xFF343947;
    private static final int TEXT = 0xFFF2F3F7;
    private static final int MUTED = 0xFF9DA3B2;
    private static final int ACCENT = 0xFF6675FF;
    private static final int ACCENT_LIGHT = 0xFFB9C0FF;
    private static final int TOGGLE_OFF = 0xFF3B404D;
    private static final int TOGGLE_KNOB_OFF = 0xFFD8DCE6;

    private static final int PANEL_GAP = 12;
    private static final int SIDE_MARGIN = 16;
    private static final int TOP_MARGIN = 18;
    private static final int HEADER_HEIGHT = 54;
    private static final int PANEL_TOP_GAP = 12;

    private final Category[] categories = {
            new Category("Combat", 0),
            new Category("Movement", 1),
            new Category("Render", 1),
            new Category("Player", 0),
            new Category("Misc", 0)
    };

    public ClickGuiScreen() {
        super(Text.literal("dlcaurora"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Keep the Minecraft world visible behind the GUI.
        renderBackground(context, mouseX, mouseY, delta);

        // Dark translucent overlay.
        context.fill(0, 0, width, height, BG);

        int outerX = SIDE_MARGIN;
        int outerY = TOP_MARGIN;
        int outerW = width - SIDE_MARGIN * 2;

        // Top header.
        drawRoundedRect(context, outerX, outerY, outerW, HEADER_HEIGHT, 14, HEADER);

        // Logo.
        drawRoundedRect(context, outerX + 14, outerY + 14, 26, 26, 13, ACCENT);
        context.drawTextWithShadow(
                textRenderer,
                Text.literal("●"),
                outerX + 20,
                outerY + 17,
                0xFFFFFFFF
        );

        context.drawTextWithShadow(
                textRenderer,
                Text.literal("dlcaurora"),
                outerX + 50,
                outerY + 15,
                TEXT
        );

        // Version badge.
        int versionX = outerX + 50 + textRenderer.getWidth("dlcaurora") + 10;
        drawRoundedRect(context, versionX, outerY + 13, 42, 28, 10, 0xFF30364A);
        context.drawCenteredTextWithShadow(
                textRenderer,
                Text.literal("1.0.0"),
                versionX + 21,
                outerY + 19,
                ACCENT_LIGHT
        );

        // Top-right controls.
        int closeX = outerX + outerW - 48;
        drawRoundedRect(context, closeX, outerY + 9, 38, 36, 10, 0xFF242936);
        context.drawCenteredTextWithShadow(
                textRenderer,
                Text.literal("×"),
                closeX + 19,
                outerY + 16,
                TEXT
        );

        int panelY = outerY + HEADER_HEIGHT + PANEL_TOP_GAP;
        int availableW = outerW - PANEL_GAP * 4;
        int panelW = availableW / 5;

        for (int i = 0; i < categories.length; i++) {
            int panelX = outerX + i * (panelW + PANEL_GAP);
            drawCategory(context, categories[i], panelX, panelY, panelW, mouseX, mouseY);
        }

        // Bottom hint.
        String hint = "Правый Shift — открыть меню";
        int hintW = textRenderer.getWidth(hint) + 38;
        int hintX = width / 2 - hintW / 2;
        int hintY = height - 48;

        drawRoundedRect(context, hintX, hintY, hintW, 32, 16, 0xE51A1D26);
        context.drawTextWithShadow(
                textRenderer,
                Text.literal("▣"),
                hintX + 12,
                hintY + 9,
                ACCENT_LIGHT
        );
        context.drawTextWithShadow(
                textRenderer,
                Text.literal(hint),
                hintX + 30,
                hintY + 9,
                MUTED
        );
    }

    private void drawCategory(
            DrawContext context,
            Category category,
            int x,
            int y,
            int w,
            int mouseX,
            int mouseY
    ) {
        int panelH = height - y - 62;

        drawRoundedRect(context, x, y, w, panelH, 14, PANEL);

        // Subtle border.
        drawRoundedRectOutline(context, x, y, w, panelH, 14, BORDER);

        // Category title.
        context.drawTextWithShadow(
                textRenderer,
                Text.literal(category.name),
                x + 16,
                y + 14,
                TEXT
        );

        String count = String.valueOf(category.count);
        context.drawTextWithShadow(
                textRenderer,
                Text.literal(count),
                x + w - 31 - textRenderer.getWidth(count),
                y + 15,
                MUTED
        );

        context.drawTextWithShadow(
                textRenderer,
                Text.literal("›"),
                x + w - 20,
                y + 13,
                ACCENT_LIGHT
        );

        // Header divider.
        context.fill(x + 12, y + 42, x + w - 12, y + 43, 0xFF303541);

        int rowY = y + 52;

        if (category.name.equals("Movement")) {
            drawModuleRow(
                    context,
                    "Auto Sprint",
                    x,
                    rowY,
                    w,
                    SimpleClient.isAutoSprint(),
                    mouseX,
                    mouseY,
                    true
            );
        } else if (category.name.equals("Render")) {
            drawModuleRow(
                    context,
                    "Full Bright",
                    x,
                    rowY,
                    w,
                    SimpleClient.isFullbright(),
                    mouseX,
                    mouseY,
                    true
            );
        } else {
            String empty = "No modules yet";
            context.drawCenteredTextWithShadow(
                    textRenderer,
                    Text.literal(empty),
                    x + w / 2,
                    y + 76,
                    MUTED
            );
        }
    }

    private void drawModuleRow(
            DrawContext context,
            String name,
            int x,
            int y,
            int w,
            boolean enabled,
            int mouseX,
            int mouseY,
            boolean toggle
    ) {
        int rowH = 40;
        boolean hovered = mouseX >= x + 5
                && mouseX <= x + w - 5
                && mouseY >= y
                && mouseY <= y + rowH;

        if (hovered || enabled) {
            drawRoundedRect(
                    context,
                    x + 6,
                    y,
                    w - 12,
                    rowH,
                    9,
                    hovered ? PANEL_HOVER : PANEL_ACTIVE
            );
        }

        context.drawTextWithShadow(
                textRenderer,
                Text.literal(name),
                x + 18,
                y + 13,
                TEXT
        );

        // Three dots like the reference design.
        context.drawTextWithShadow(
                textRenderer,
                Text.literal("•••"),
                x + w - 48,
                y + 13,
                MUTED
        );

        if (toggle) {
            drawToggle(context, x + w - 41, y + 9, enabled);
        }
    }

    private void drawToggle(DrawContext context, int x, int y, boolean enabled) {
        int w = 34;
        int h = 20;

        drawRoundedRect(
                context,
                x,
                y,
                w,
                h,
                10,
                enabled ? ACCENT : TOGGLE_OFF
        );

        int knobX = enabled ? x + w - 18 : x + 2;
        drawRoundedRect(
                context,
                knobX,
                y + 2,
                16,
                16,
                8,
                enabled ? 0xFFFFFFFF : TOGGLE_KNOB_OFF
        );
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) {
            return super.mouseClicked(mouseX, mouseY, button);
        }

        int outerX = SIDE_MARGIN;
        int outerY = TOP_MARGIN;
        int outerW = width - SIDE_MARGIN * 2;
        int panelY = outerY + HEADER_HEIGHT + PANEL_TOP_GAP;

        int availableW = outerW - PANEL_GAP * 4;
        int panelW = availableW / 5;

        // Movement -> Auto Sprint.
        int movementX = outerX + (panelW + PANEL_GAP);
        int rowY = panelY + 52;

        if (isInside(mouseX, mouseY, movementX + 6, rowY, panelW - 12, 40)) {
            SimpleClient.toggleAutoSprint(client);
            return true;
        }

        // Render -> Full Bright.
        int renderX = outerX + 2 * (panelW + PANEL_GAP);

        if (isInside(mouseX, mouseY, renderX + 6, rowY, panelW - 12, 40)) {
            SimpleClient.toggleFullbright(client);
            return true;
        }

        // Close button.
        int closeX = outerX + outerW - 48;
        if (isInside(mouseX, mouseY, closeX, outerY + 9, 38, 36)) {
            close();
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean isInside(
            double mouseX,
            double mouseY,
            int x,
            int y,
            int w,
            int h
    ) {
        return mouseX >= x
                && mouseX <= x + w
                && mouseY >= y
                && mouseY <= y + h;
    }

    private void drawRoundedRect(
            DrawContext context,
            int x,
            int y,
            int w,
            int h,
            int radius,
            int color
    ) {
        // Lightweight rounded rectangle made only from vanilla DrawContext fills.
        int r = Math.min(radius, Math.min(w, h) / 2);

        context.fill(x + r, y, x + w - r, y + h, color);
        context.fill(x, y + r, x + w, y + h - r, color);

        context.fill(x + 2, y + 1, x + r + 1, y + r + 1, color);
        context.fill(x + w - r - 1, y + 1, x + w - 2, y + r + 1, color);
        context.fill(x + 2, y + h - r - 1, x + r + 1, y + h - 1, color);
        context.fill(x + w - r - 1, y + h - r - 1, x + w - 2, y + h - 1, color);
    }

    private void drawRoundedRectOutline(
            DrawContext context,
            int x,
            int y,
            int w,
            int h,
            int radius,
            int color
    ) {
        context.fill(x + radius, y, x + w - radius, y + 1, color);
        context.fill(x + radius, y + h - 1, x + w - radius, y + h, color);
        context.fill(x, y + radius, x + 1, y + h - radius, color);
        context.fill(x + w - 1, y + radius, x + w, y + h - radius, color);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private static class Category {
        private final String name;
        private final int count;

        private Category(String name, int count) {
            this.name = name;
            this.count = count;
        }
    }
}

