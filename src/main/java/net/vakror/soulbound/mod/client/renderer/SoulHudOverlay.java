package net.vakror.soulbound.mod.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.vakror.soulbound.mod.SoulboundMod;
import net.vakror.soulbound.mod.client.ClientSoulData;

public class SoulHudOverlay {
    private static final ResourceLocation SOUL_BAR = new ResourceLocation(SoulboundMod.MOD_ID,
            "textures/gui/soul.png");
    private static final ResourceLocation SOUL_OVERLAY = new ResourceLocation(SoulboundMod.MOD_ID,
            "textures/gui/soul_overlay.png");
    private static final ResourceLocation DARK_SOUL_OVERLAY = new ResourceLocation(SoulboundMod.MOD_ID,
            "textures/gui/dark_soul_overlay.png");
    private static final ResourceLocation DARK_SOUL_BAR = new ResourceLocation(SoulboundMod.MOD_ID,
            "textures/gui/dark_soul.png");

    public static final IGuiOverlay HUD_SOUL = ((gui, matrices, partialTick, width, height) -> {
        if (gui.shouldDrawSurvivalElements()) {
            int x = width / 2;
            int y = height;

            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            double soul = ClientSoulData.getPlayerSoul();
            double maxSoul = (int) ClientSoulData.getPlayerMaxSoul();

            float darkSoul = ClientSoulData.getDarkPlayerSoul();
            float maxDarkSoul = (int) ClientSoulData.getDarkPlayerMaxSoul();

            int darkSoulHeight = (int) (darkSoul / maxDarkSoul * 56);
            int adjustedDarkSoulHeight = (int) Math.floor(darkSoul / maxDarkSoul * 56);
            adjustedDarkSoulHeight = adjustedDarkSoulHeight % 2 == 0 ? adjustedDarkSoulHeight : adjustedDarkSoulHeight + 1;

            int soulHeight = (int) (soul / maxSoul * 56);
            int adjustedSoulHeight = (int) Math.floor(soul / maxSoul * 56);
            RenderSystem.setShaderTexture(0, SOUL_BAR);
            GuiComponent.blit(matrices, x - 175, y - 70 + (56 - adjustedSoulHeight), 0, 0, 28, soulHeight,
                    28, soulHeight);

            int soulWidth = Minecraft.getInstance().font.width((int) soul + "/" + (int) maxSoul);
            int darkSoulWidth = Minecraft.getInstance().font.width((int) darkSoul + "/" + (int) maxDarkSoul);
            GuiComponent.drawString(matrices, Minecraft.getInstance().font, (int) soul + "/" + (int) maxSoul, x - 145 - soulWidth, y - 80, FastColor.ARGB32.color(255, 255, 255, 255));
            GuiComponent.drawString(matrices, Minecraft.getInstance().font, (int) darkSoul + "/" + (int) maxDarkSoul, x - 98 - darkSoulWidth, y - 80, FastColor.ARGB32.color(255, 255, 255, 255));

            RenderSystem.setShaderTexture(0, DARK_SOUL_BAR);
            GuiComponent.blit(matrices, x - 130, y - 70 + (56 - adjustedDarkSoulHeight), 0, 0, 28, darkSoulHeight,
                    28, darkSoulHeight);

            RenderSystem.setShaderTexture(0, SOUL_OVERLAY);
            GuiComponent.blit(matrices, x - 175, y - 70, 0, 0, 28, 56, 28, 56);

            RenderSystem.setShaderTexture(0, DARK_SOUL_OVERLAY);
            GuiComponent.blit(matrices, x - 130, y - 70, 0, 0, 28, 56, 28, 56);
        }
    });
}