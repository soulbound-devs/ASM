package net.vakror.soulbound.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.client.renderer.FluidTankRenderer;

import java.util.Optional;

public class SoulSolidifierScreen extends AbstractContainerScreen<SoulSolidifierMenu> {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(SoulboundMod.MOD_ID, "textures/gui/soul_solidifier.png");
    private FluidTankRenderer renderer;

    public SoulSolidifierScreen(SoulSolidifierMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        assignFluidRenderer();
    }

    private void assignFluidRenderer() {
        renderer = new FluidTankRenderer(menu.blockEntity.FLUID_TANK.getCapacity(), true, 16, 46);
    }

    @Override
    protected void renderBg(PoseStack matrices, float p_97788_, int p_97789_, int p_97790_) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        blit(matrices, x, y, 0, 0, imageWidth, imageHeight);

        if (menu.isCrafting()) {
            blit(matrices, x + 118, y + 37, 177, 0, menu.getScaledProgress(), 8);
        }
        renderer.render(matrices, x + 50, y + 8, menu.getStack());
    }

    @Override
    public void render(PoseStack matrices, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(matrices);
        super.render(matrices, pMouseX, pMouseY, pPartialTick);
        renderTooltip(matrices, pMouseX, pMouseY);
    }


    @Override
    protected void renderLabels(PoseStack matrices, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderFluidTooltips(matrices, pMouseX, pMouseY, x, y);
    }

    private void renderFluidTooltips(PoseStack matrices, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 50, 8)) {
            renderTooltip(matrices, renderer.getTooltip(menu.getStack(), TooltipFlag.Default.NORMAL),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY) {
        return isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, renderer.getWidth(), renderer.getHeight());
    }

    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int sizeX, int sizeY) {
        return (mouseX >= x && mouseX <= x + sizeX) && (mouseY >= y && mouseY <= y + sizeY);
    }
}