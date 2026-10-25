package com.example.targethud;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.lwjgl.glfw.GLFW;

/**
 * Small HUD card for the entity under the crosshair, plus a fog overlay and
 * twinkling crystals. Press Right Shift to open the sliders.
 */
public class TargetHudClient implements ClientModInitializer {
    private static final int W = 140;
    private static final int H = 38;

    private static final int PANEL = 0xCC1A1842;
    private static final int BORDER = 0x669696FF;
    private static final int TRACK = 0x44FFFFFF;
    private static final int ACCENT = 0xFF7C8CFF;
    private static final int LOW = 0xFFF0576B;
    private static final int TEXT = 0xFFECEBFF;
    private static final int MUTED = 0xFF9B9CCB;

    private float shownHealth = -1f;
    private float lagHealth = -1f;
    private KeyBinding settingsKey;

    @Override
    public void onInitializeClient() {
        TargetHudConfig.load();

        settingsKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.targethud.settings", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "category.targethud"));

        ClientTickEvents.END_CLIENT_TICK.register(mc -> {
            while (settingsKey.wasPressed()) {
                mc.setScreen(new SettingsScreen());
            }
        });

        HudRenderCallback.EVENT.register((ctx, tickCounter) -> render(ctx));
    }

    private void render(DrawContext ctx) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.options.hudHidden) return;

        Effects.fog(ctx, ctx.getScaledWindowWidth(), ctx.getScaledWindowHeight(), TargetHudConfig.fog);

        HitResult hit = mc.crosshairTarget;
        if (hit == null || hit.getType() != HitResult.Type.ENTITY) {
            shownHealth = -1f;
            return;
        }
        if (!(((EntityHitResult) hit).getEntity() instanceof LivingEntity target)) return;

        float max = Math.max(1f, target.getMaxHealth());
        float hp = Math.max(0f, target.getHealth());
        if (shownHealth < 0f) { shownHealth = hp; lagHealth = hp; }
        shownHealth += (hp - shownHealth) * 0.4f;
        lagHealth += (shownHealth - lagHealth) * 0.06f;

        TextRenderer tr = mc.textRenderer;
        int x = ctx.getScaledWindowWidth() / 2 + 28;
        int y = ctx.getScaledWindowHeight() / 2 - H / 2;

        // panel
        ctx.fill(x, y, x + W, y + H, PANEL);
        ctx.drawBorder(x, y, W, H, BORDER);

        // name and distance
        String name = target.getName().getString();
        String dist = String.format("%.1fm", mc.player.distanceTo(target));
        ctx.drawTextWithShadow(tr, tr.trimToWidth(name, W - 50), x + 6, y + 5, TEXT);
        ctx.drawTextWithShadow(tr, dist, x + W - 6 - tr.getWidth(dist), y + 5, MUTED);

        // health bar (white part is the delayed "lag" bar)
        int bx = x + 6, by = y + 17, bw = W - 12, bh = 5;
        ctx.fill(bx, by, bx + bw, by + bh, TRACK);
        ctx.fill(bx, by, bx + Math.round(bw * Math.min(1f, lagHealth / max)), by + bh, 0x88FFFFFF);
        int color = hp / max <= 0.3f ? LOW : ACCENT;
        ctx.fill(bx, by, bx + Math.round(bw * Math.min(1f, shownHealth / max)), by + bh, color);

        // numbers
        String hpText = String.format("HP %.1f / %.0f", hp, max);
        String armorText = "Armor " + target.getArmor();
        ctx.drawTextWithShadow(tr, hpText, x + 6, y + 26, MUTED);
        ctx.drawTextWithShadow(tr, armorText, x + W - 6 - tr.getWidth(armorText), y + 26, MUTED);

        // crystals around the card
        Effects.crystals(ctx, x - 24, y - 24, W + 48, H + 48, TargetHudConfig.crystals);
    }
}
