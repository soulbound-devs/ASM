package net.vakror.soulbound.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.vakror.soulbound.SoulboundMod;

public class WandImbuingScreen extends AbstractContainerScreen<WandImbuingMenu> {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(SoulboundMod.MOD_ID, "textures/gui/imbuer_gui.png");

    public WandImbuingScreen(WandImbuingMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

        if (menu.isCrafting()) {
            blit(pPoseStack, x + 118, y + 37, 177, 38, menu.getScaledProgress(), 8);
        }

        if (menu.hasSoul()) {
            blit(pPoseStack, x + 19, y + 32 + 15 - menu.getScaledSoulProgress(), 177,
                    16 - menu.getScaledSoulProgress(), 14, menu.getScaledSoulProgress());
        }
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
    }
}
