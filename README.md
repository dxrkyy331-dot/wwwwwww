# Target HUD (Fabric, Minecraft 1.21.4)

A client-side HUD card that appears next to your crosshair when you look at a living entity.
It shows the name, distance, a health bar, HP and armor. It only uses information the game
already gives you. There is no ESP, no tracking through walls and no combat automation.

## Get the .jar from GitHub

1. Unzip this file. GitHub does not unpack zips, so a zip uploaded as is will not build.
   Create a new repository and upload all files from this folder, including the hidden `.github` folder.
   If your system hides it, use Add file > Create new file on GitHub, type `.github/workflows/build.yml` as the name and paste the contents of that file.
2. Open the **Actions** tab. The `build` workflow starts by itself after the upload.
3. When it turns green, open **Releases** on the right side of the repository page and download `target-hud-1.1.0.jar` from "Latest build".
   (You can also use the run's Artifacts, but that is a zip that contains the jar.)

## Settings

Press **Right Shift** in game to open two sliders:
- **Fog** (0 to 100%): a soft light fog at the screen edges.
- **Crystals** (0 to 30): twinkling white squares around the target card.

The values are saved in `config/targethud.properties`. You can change the key under Options > Controls.

## Install

1. Install Fabric Loader for Minecraft 1.21.4 and put Fabric API in your `mods` folder.
2. Put `target-hud-1.1.0.jar` in the same `mods` folder.
3. Check the rules of any server you join. Most allow client-side HUD mods, but some do not.

## Other Minecraft versions

Edit `gradle.properties` with the values from https://fabricmc.net/develop and also the
`minecraft` version in `src/main/resources/fabric.mod.json`. Newer versions can change
some method names, so the code may need small fixes.
