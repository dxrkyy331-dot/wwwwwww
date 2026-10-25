package com.example.targethud;

import net.minecraft.client.gui.DrawContext;

/** Screen effects: a soft fog at the screen edges and twinkling white crystals. */
public final class Effects {
    private Effects() {}

    /** strength 0.0 - 1.0 */
    public static void fog(DrawContext ctx, int w, int h, double strength) {
        if (strength <= 0.001) return;
        int steps = 24;
        int band = Math.min(w, h) / 5;
        double step = (double) band / steps;
        int thick = Math.max(1, (int) Math.ceil(step));
        for (int i = 0; i < steps; i++) {
            double t = 1.0 - (double) i / steps;
            int a = (int) (255 * strength * 0.75 * t * t);
            if (a <= 0) continue;
            int c = (a << 24) | 0xC8D2FF;
            int o = (int) (i * step);
            ctx.fill(0, o, w, o + thick, c);
            ctx.fill(0, h - o - thick, w, h - o, c);
            ctx.fill(o, 0, o + thick, h, c);
            ctx.fill(w - o - thick, 0, w - o, h, c);
        }
        int haze = (int) (255 * strength * 0.12);
        ctx.fill(0, 0, w, h, (haze << 24) | 0xC8D2FF);
    }

    /** Draws {@code count} twinkling white squares inside the given area. */
    public static void crystals(DrawContext ctx, int x, int y, int w, int h, int count) {
        long now = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            double fx = frac(Math.sin(i * 12.9898) * 43758.5453);
            double fy = frac(Math.sin(i * 78.233 + 1.7) * 12345.6789);
            int cx = x + (int) (fx * w);
            int cy = y + (int) (fy * h);
            long period = 900L + (i % 5) * 300L;
            double phase = ((now + i * 377L) % period) / (double) period;
            double a = Math.sin(Math.PI * phase);
            if (a <= 0.02) continue;
            int size = 2 + (i % 4) * 2;
            int glow = (int) (a * 70);
            int core = (int) (a * 255);
            ctx.fill(cx - size - 2, cy - size - 2, cx + size + 2, cy + size + 2, (glow << 24) | 0xBEDCFF);
            ctx.fill(cx - size, cy - size, cx + size, cy + size, (core << 24) | 0xFFFFFF);
        }
    }

    private static double frac(double d) {
        return d - Math.floor(d);
    }
}
