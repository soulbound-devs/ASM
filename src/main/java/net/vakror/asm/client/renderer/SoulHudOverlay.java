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
    private static final ResourceLocation DARK_SOUL_OVERLAY = new ResourceLocation(ASMMod.MOD_ID,
            "textures/gui/dark_soul_overlay.png");
    private static final ResourceLocation DARK_SOUL_BAR = new ResourceLocation(ASMMod.MOD_ID,
            "textures/gui/dark_soul.png");

    public static final IGuiOverlay HUD_SOUL = ((gui, graphics, partialTick, width, height) -> {
        int x = width / 2;
        int y = height;


        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        double soul = ClientSoulData.getPlayerSoul();
        double maxSoul = (int) ClientSoulData.getPlayerMaxSoul();

        float darkSoul = ClientSoulData.getDarkPlayerSoul();
        float maxDarkSoul = (int) ClientSoulData.getDarkPlayerMaxSoul();

        int darkSoulHeight = (int) (darkSoul/maxDarkSoul * 56);
        int adjustedDarkSoulHeight = (int)Math.ceil(darkSoul/maxDarkSoul * 56);
        adjustedDarkSoulHeight = adjustedDarkSoulHeight % 2 == 0 ? adjustedDarkSoulHeight: adjustedDarkSoulHeight + 1;

        int soulHeight = (int) (soul/maxSoul * 56);
        int adjustedSoulHeight = (int)Math.ceil(soul/maxSoul * 56);
        graphics.blit(SOUL_BAR, x - 175, y - 70 + (56 - adjustedSoulHeight),0, 0, 28, soulHeight,
                28, soulHeight);

        graphics.blit(DARK_SOUL_BAR, x - 125, y - 70 + (56 - adjustedDarkSoulHeight),0, 0, 28, darkSoulHeight,
                28, darkSoulHeight);

        graphics.blit(SOUL_OVERLAY, x - 175, y - 70, 0, 0, 28, 56, 28, 56);
        graphics.blit(DARK_SOUL_OVERLAY, x - 125, y - 70, 0, 0, 28, 56, 28, 56);
    });

    public static int toColorInt(int R, int G, int B, int A) {
        return (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
    }
}