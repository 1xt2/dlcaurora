package com.example.simpleclient;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

/**
 * Image-based ClickGUI.
 *
 * The supplied UI artwork is used as the visual base, while the two
 * module switches remain live and are controlled by mouse clicks.
 */
public class ClickGuiScreen extends Screen {

    private static final Identifier GUI_TEXTURE =
            Identifier.of("simpleclient", "textures/gui/clickgui.png");

    // Original generated artwork size.
    private static final int IMAGE_WIDTH = 1586;
    private static final int IMAGE_HEIGHT = 992;

    // Hitboxes are expressed in the artwork's original pixel coordinates.
    private static final int AUTO_SPRINT_X = 360;
    private static final int AUTO_SPRINT_Y = 300;
    private static final int AUTO_SPRINT_W = 265;
    private static final int AUTO_SPRINT_H = 62;

    private static final int FULLBRIGHT_X = 655;
    private static final int FULLBRIGHT_Y = 300;
    private static final int FULLBRIGHT_W = 270;
    private static final int FULLBRIGHT_H = 62;

    private static final int CLOSE_X = 1335;
    private static final int CLOSE_Y = 110;
    private static final int CLOSE_W = 125;
    private static final int CLOSE_H = 70;

    // Toggle locations in the artwork. These are repainted so the image's
    // static OFF state is replaced by the real module state.
    private static final int AUTO_TOGGLE_X = 540;
    private static final int AUTO_TOGGLE_Y = 309;
    private static final int FULL_TOGGLE_X = 840;
    private static final int FULL_TOGGLE_Y = 309;
    private static final int TOGGLE_W = 64;
    private static final int TOGGLE_H = 36;

    public ClickGuiScreen() {
        super(Text.literal("dlcaurora"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // The PNG is the actual visual layer of the GUI.
        context.drawTexture(
                GUI_TEXTURE,
                0,
                0,
                0.0F,
                0.0F,
                width,
                height,
                IMAGE_WIDTH,
                IMAGE_HEIGHT
        );

        float sx = (float) width / IMAGE_WIDTH;
        float sy = (float) height / IMAGE_HEIGHT;

        // Cover the static switches from the artwork and draw their live state.
        drawLiveToggle(
                context,
                Math.round(AUTO_TOGGLE_X * sx),
                Math.round(AUTO_TOGGLE_Y * sy),
                Math.round(TOGGLE_W * sx),
                Math.round(TOGGLE_H * sy),
                SimpleClient.isAutoSprint()
        );

        drawLiveToggle(
                context,
                Math.round(FULL_TOGGLE_X * sx),
                Math.round(FULL_TOGGLE_Y * sy),
                Math.round(TOGGLE_W * sx),
                Math.round(TOGGLE_H * sy),
                SimpleClient.isFullbright()
        );

        // A very subtle hover highlight over the active clickable rows.
        if (insideImageRect(mouseX, mouseY, AUTO_SPRINT_X, AUTO_SPRINT_Y, AUTO_SPRINT_W, AUTO_SPRINT_H)) {
            drawHover(context, AUTO_SPRINT_X, AUTO_SPRINT_Y, AUTO_SPRINT_W, AUTO_SPRINT_H, sx, sy);
        }

        if (insideImageRect(mouseX, mouseY, FULLBRIGHT_X, FULLBRIGHT_Y, FULLBRIGHT_W, FULLBRIGHT_H)) {
            drawHover(context, FULLBRIGHT_X, FULLBRIGHT_Y, FULLBRIGHT_W, FULLBRIGHT_H, sx, sy);
        }
    }

    private void drawLiveToggle(
            DrawContext context,
            int x,
            int y,
            int w,
            int h,
            boolean enabled
    ) {
        // Match the smooth pill style from the artwork.
        int track = enabled ? 0xFF6878FF : 0xFF343B50;
        int knob = 0xFFF4F6FF;

        drawRoundedRect(context, x, y, w, h, h / 2, track);

        int knobSize = Math.max(10, h - 8);
        int knobX = enabled ? x + w - knobSize - 4 : x + 4;
        int knobY = y + (h - knobSize) / 2;

        drawRoundedRect(
                context,
                knobX,
                knobY,
                knobSize,
                knobSize,
                knobSize / 2,
                knob
        );
    }

    private void drawHover(
            DrawContext context,
            int imageX,
            int imageY,
            int imageW,
            int imageH,
            float sx,
            float sy
    ) {
        int x = Math.round(imageX * sx);
        int y = Math.round(imageY * sy);
        int w = Math.round(imageW * sx);
        int h = Math.round(imageH * sy);

        // Very subtle, transparent highlight; the artwork remains visible.
        context.fill(x, y, x + w, y + h, 0x16000000);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button != 0) {
            return super.mouseClicked(mouseX, mouseY, button);
        }

        if (insideImageRect(mouseX, mouseY, AUTO_SPRINT_X, AUTO_SPRINT_Y, AUTO_SPRINT_W, AUTO_SPRINT_H)) {
            SimpleClient.toggleAutoSprint(client);
            return true;
        }

        if (insideImageRect(mouseX, mouseY, FULLBRIGHT_X, FULLBRIGHT_Y, FULLBRIGHT_W, FULLBRIGHT_H)) {
            SimpleClient.toggleFullbright(client);
            return true;
        }

        if (insideImageRect(mouseX, mouseY, CLOSE_X, CLOSE_Y, CLOSE_W, CLOSE_H)) {
            close();
            return true;
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    private boolean insideImageRect(
            double mouseX,
            double mouseY,
            int imageX,
            int imageY,
            int imageW,
            int imageH
    ) {
        float sx = (float) width / IMAGE_WIDTH;
        float sy = (float) height / IMAGE_HEIGHT;

        double x = imageX * sx;
        double y = imageY * sy;
        double w = imageW * sx;
        double h = imageH * sy;

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
        int r = Math.min(radius, Math.min(w, h) / 2);

        context.fill(x + r, y, x + w - r, y + h, color);
        context.fill(x, y + r, x + w, y + h - r, color);

        // Small corner blocks keep the shape smooth enough without
        // introducing an additional rendering library.
        context.fill(x + 1, y + 1, x + r, y + r, color);
        context.fill(x + w - r, y + 1, x + w - 1, y + r, color);
        context.fill(x + 1, y + h - r, x + r, y + h - 1, color);
        context.fill(x + w - r, y + h - r, x + w - 1, y + h - 1, color);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}


