package com.example.simpleclient;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class ClickGuiScreen extends Screen {
    public ClickGuiScreen() {
        super(Text.literal("Simple Client"));
    }

    @Override
    protected void init() {
        int x = this.width / 2 - 100;
        int y = this.height / 2 - 45;

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Auto Sprint: " + (SimpleClient.isAutoSprint() ? "ON" : "OFF")),
                button -> {
                    SimpleClient.toggleAutoSprint(client);
                    button.setMessage(Text.literal("Auto Sprint: " + (SimpleClient.isAutoSprint() ? "ON" : "OFF")));
                }).dimensions(x, y, 200, 20).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Fullbright: " + (SimpleClient.isFullbright() ? "ON" : "OFF")),
                button -> {
                    SimpleClient.toggleFullbright(client);
                    button.setMessage(Text.literal("Fullbright: " + (SimpleClient.isFullbright() ? "ON" : "OFF")));
                }).dimensions(x, y + 25, 200, 20).build());

        addDrawableChild(ButtonWidget.builder(
                Text.literal("Close"),
                button -> close()).dimensions(x, y + 50, 200, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(textRenderer, title, width / 2, height / 2 - 75, 0xFFFFFF);
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
