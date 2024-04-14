package net.vakror.soulbound.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.vakror.soulbound.SoulboundMod;
import org.jetbrains.annotations.NotNull;

public class WandImbuingScreen extends AbstractContainerScreen<WandImbuingMenu> {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(SoulboundMod.MOD_ID, "textures/gui/imbuer_gui.png");

    public WandImbuingScreen(WandImbuingMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float p_97788_, int p_97789_, int p_97790_) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        graphics.blit(BACKGROUND_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        if (menu.isCrafting()) {
            graphics.blit(BACKGROUND_TEXTURE, x + 118, y + 37, 177, 38, menu.getScaledProgress(), 8);
        }

        if (menu.hasSoul()) {
            graphics.blit(BACKGROUND_TEXTURE, x + 19, y + 32 + 15 - menu.getScaledSoulProgress(), 177,
                    16 - menu.getScaledSoulProgress(), 14, menu.getScaledSoulProgress());
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(graphics, pMouseX, pMouseY);
    }
}
