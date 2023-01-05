package net.vakror.soulbound.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.IIngameOverlay;
import net.vakror.soulbound.SoulboundMod;

public class SoulHudOverlay {
    private static final ResourceLocation SOUL_BAR = new ResourceLocation(SoulboundMod.MOD_ID,
            "textures/gui/soul.png");
    private static final ResourceLocation SOUL_OVERLAY = new ResourceLocation(SoulboundMod.MOD_ID,
            "textures/gui/soul_overlay.png");
    private static final ResourceLocation DARK_SOUL_BAR = new ResourceLocation(SoulboundMod.MOD_ID,
            "textures/gui/dark_soul.png");

    public static final IIngameOverlay HUD_SOUL = ((gui, poseStack, partialTick, width, height) -> {
        int x = width / 2;
        int y = height;


        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, SOUL_BAR);

        double soul = ClientSoulData.getPlayerSoul();
        double maxSoul = (int) ClientSoulData.getPlayerMaxSoul();

        double darkSoul = ClientSoulData.getPlayerSoul();
        double maxDarkSoul = (int) ClientSoulData.getPlayerMaxSoul();

        GuiComponent.blit(poseStack, x - 175, y - 20,0, 0, Long.valueOf(Math.round((soul/maxSoul) * 73)).intValue(), 11,
                73, 11);

        RenderSystem.setShaderTexture(0, DARK_SOUL_BAR);

        GuiComponent.blit(poseStack, x - 175, y - 40,0, 0, Long.valueOf(Math.round((darkSoul/maxDarkSoul) * 73)).intValue(), 11,
                73, 11);

        RenderSystem.setShaderTexture(0, SOUL_OVERLAY);
        GuiComponent.blit(poseStack, x - 175, y - 20, 0, 0, 73, 11, 73, 11);
        GuiComponent.blit(poseStack, x - 175, y - 55, 0, 0, 73, 11, 73, 11);
    });

    public static int toColorInt(int R, int G, int B, int A) {
        return (A & 0xff) << 24 | (R & 0xff) << 16 | (G & 0xff) << 8 | (B & 0xff);
    }
}