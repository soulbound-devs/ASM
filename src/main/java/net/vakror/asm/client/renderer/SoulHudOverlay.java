package net.vakror.asm.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.vakror.asm.ASMMod;
import net.vakror.asm.client.ClientSoulData;

public class SoulHudOverlay {
    private static final ResourceLocation SOUL_BAR = new ResourceLocation(ASMMod.MOD_ID,
            "textures/gui/soul.png");
    private static final ResourceLocation SOUL_OVERLAY = new ResourceLocation(ASMMod.MOD_ID,
            "textures/gui/soul_overlay.png");
    private static final ResourceLocation DARK_SOUL_BAR = new ResourceLocation(ASMMod.MOD_ID,
            "textures/gui/dark_soul.png");

    public static final IGuiOverlay HUD_SOUL = ((gui, graphics, partialTick, width, height) -> {
        int x = width / 2;
        int y = height;


        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        double soul = ClientSoulData.getPlayerSoul();
        double maxSoul = (int) ClientSoulData.getPlayerMaxSoul();

        double darkSoul = ClientSoulData.getPlayerSoul();
        double maxDarkSoul = (int) ClientSoulData.getPlayerMaxSoul();
        
        graphics.blit(SOUL_BAR, x - 175, y - 20,0, 0, Long.valueOf(Math.round((soul/maxSoul) * 73)).intValue(), 11,
                73, 11);

        graphics.blit(DARK_SOUL_BAR, x - 175, y - 40,0, 0, Long.valueOf(Math.round((darkSoul/maxDarkSoul) * 73)).intValue(), 11,
                73, 11);

        graphics.blit(SOUL_OVERLAY, x - 175, y - 20, 0, 0, 73, 11, 73, 11);
        graphics.blit(SOUL_OVERLAY, x - 175, y - 55, 0, 0, 73, 11, 73, 11);
    });

    public static int toColorInt(int R, int G, int B, int A) {
        return (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
    }
}