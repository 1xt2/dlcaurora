package com.example.simpleclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class SimpleClient implements ClientModInitializer {
    private static boolean autoSprint = false;
    private static boolean fullbright = false;
    private static KeyBinding guiKey;

    @Override
    public void onInitializeClient() {
        guiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.simpleclient.clickgui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.simpleclient"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (guiKey.wasPressed()) {
                client.setScreen(new ClickGuiScreen());
            }

            if (client.player != null) {
                if (autoSprint) {
                    boolean forward = client.options.forwardKey.isPressed();
                    boolean enoughFood = client.player.getHungerManager().getFoodLevel() > 6;
                    client.options.sprintKey.setPressed(forward && enoughFood);
                }

                client.options.getGamma().setValue(fullbright ? 16.0 : 1.0);
            }
        });
    }

    public static boolean isAutoSprint() { return autoSprint; }
    public static boolean isFullbright() { return fullbright; }

    public static void toggleAutoSprint(MinecraftClient client) {
        autoSprint = !autoSprint;
        if (client.player != null)
            client.player.sendMessage(Text.literal("Auto Sprint: " + (autoSprint ? "ON" : "OFF")), true);
    }

    public static void toggleFullbright(MinecraftClient client) {
        fullbright = !fullbright;
        if (client.player != null)
            client.player.sendMessage(Text.literal("Fullbright: " + (fullbright ? "ON" : "OFF")), true);
    }
}
