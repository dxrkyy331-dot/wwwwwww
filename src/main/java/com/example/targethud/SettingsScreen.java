package com.example.targethud;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.SliderWidget;
import net.minecraft.text.Text;

/** Two sliders: fog strength and number of crystals. Opens with Right Shift. */
public class SettingsScreen extends Screen {
    public SettingsScreen() {
        super(Text.literal("Target HUD"));
    }

    @Override
    protected void init() {
        int cx = this.width / 2;
        int cy = this.height / 2;

        this.addDrawableChild(new SliderWidget(cx - 100, cy - 24, 200, 20, Text.empty(), TargetHudConfig.fog) {
            { updateMessage(); }

            @Override
            protected void updateMessage() {
                setMessage(Text.literal("Fog: " + (int) Math.round(this.value * 100) + "%"));
            }

            @Override
            protected void applyValue() {
                TargetHudConfig.fog = this.value;
            }
        });

        this.addDrawableChild(new SliderWidget(cx - 100, cy + 4, 200, 20, Text.empty(),
                TargetHudConfig.crystals / (double) TargetHudConfig.MAX_CRYSTALS) {
            { updateMessage(); }

            @Override
            protected void updateMessage() {
                setMessage(Text.literal("Crystals: " + (int) Math.round(this.value * TargetHudConfig.MAX_CRYSTALS)));
            }

            @Override
            protected void applyValue() {
                TargetHudConfig.crystals = (int) Math.round(this.value * TargetHudConfig.MAX_CRYSTALS);
            }
        });

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Done"), b -> this.close())
                .dimensions(cx - 50, cy + 36, 100, 20)
                .build());
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        super.render(ctx, mouseX, mouseY, delta);
        int cx = this.width / 2;
        int cy = this.height / 2;
        Effects.crystals(ctx, cx - 130, cy - 80, 260, 160, TargetHudConfig.crystals);
        ctx.drawCenteredTextWithShadow(this.textRenderer, this.title, cx, cy - 48, 0xFFECEBFF);
    }

    @Override
    public void removed() {
        TargetHudConfig.save();
    }
}
