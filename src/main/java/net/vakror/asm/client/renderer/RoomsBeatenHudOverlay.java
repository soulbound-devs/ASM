package net.vakror.asm.client.renderer;

import net.minecraftforge.client.gui.overlay.IGuiOverlay;

public class RoomsBeatenHudOverlay {
    public static final IGuiOverlay DUNGEON_HUD = ((gui, graphics, partialTick, width, height) -> {
        if (gui.shouldDrawSurvivalElements()) {
            //TODO: Draw dungeon hud
        }
    });
}