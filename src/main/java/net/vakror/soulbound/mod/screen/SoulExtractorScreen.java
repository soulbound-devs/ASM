package net.vakror.soulbound.mod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.vakror.soulbound.mod.SoulboundMod;
import net.vakror.soulbound.mod.client.renderer.FluidTankRenderer;

import java.util.Optional;

public class SoulExtractorScreen extends AbstractContainerScreen<SoulExtractorMenu> {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(SoulboundMod.MOD_ID, "textures/gui/extractor.png");
    private FluidTankRenderer soulRenderer;
    private FluidTankRenderer darkSoulRenderer;

    public SoulExtractorScreen(SoulExtractorMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        assignFluidRenderer();
    }

    private void assignFluidRenderer() {
        soulRenderer = new FluidTankRenderer(((FluidTank) menu.blockEntity.SOUL_HANDLER.orElse(new FluidTank(0))).getCapacity(), true, 16, 46);
        darkSoulRenderer = new FluidTankRenderer(((FluidTank) menu.blockEntity.DARK_SOUL_HANDLER.orElse(new FluidTank(0))).getCapacity(), true, 16, 46);
    }

    @Override
    protected void renderBg(PoseStack matrices, float p_97788_, int p_97789_, int p_97790_) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        blit(matrices, x, y, 0, 0, imageWidth, imageHeight);

        soulRenderer.render(matrices, x + 43, y + 21, menu.getSoulStack());
        darkSoulRenderer.render(matrices, x + 98, y + 21, menu.getDarkSoulStack());
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
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 43, 21, soulRenderer)) {
            renderTooltip(matrices, soulRenderer.getTooltip(menu.getSoulStack(), TooltipFlag.Default.NORMAL),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 98, 21, darkSoulRenderer)) {
            renderTooltip(matrices, darkSoulRenderer.getTooltip(menu.getDarkSoulStack(), TooltipFlag.Default.NORMAL),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, FluidTankRenderer renderer) {
        return isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, renderer.getWidth(), renderer.getHeight());
    }

    public static boolean isMouseOver(double mouseX, double mouseY, int x, int y, int sizeX, int sizeY) {
        return (mouseX >= x && mouseX <= x + sizeX) && (mouseY >= y && mouseY <= y + sizeY);
    }
}