package com.example.targethud;

import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/** Two saved settings: fog strength (0.0 - 1.0) and number of crystals (0 - 30). */
public final class TargetHudConfig {
    public static final int MAX_CRYSTALS = 30;

    public static double fog = 0.3;
    public static int crystals = 9;

    private static Path file() {
        return FabricLoader.getInstance().getConfigDir().resolve("targethud.properties");
    }

    public static void load() {
        Path f = file();
        if (!Files.exists(f)) return;
        try (Reader r = Files.newBufferedReader(f)) {
            Properties p = new Properties();
            p.load(r);
            fog = clamp(Double.parseDouble(p.getProperty("fog", "0.3")), 0.0, 1.0);
            crystals = (int) clamp(Integer.parseInt(p.getProperty("crystals", "9")), 0, MAX_CRYSTALS);
        } catch (IOException | NumberFormatException ignored) {
            // keep defaults when the file is missing or broken
        }
    }

    public static void save() {
        Properties p = new Properties();
        p.setProperty("fog", String.valueOf(fog));
        p.setProperty("crystals", String.valueOf(crystals));
        try (Writer w = Files.newBufferedWriter(file())) {
            p.store(w, "Target HUD settings");
        } catch (IOException ignored) {
            // nothing to do if the config folder is not writable
        }
    }

    private static double clamp(double v, double lo, double hi) {
        return Math.max(lo, Math.min(hi, v));
    }
}
