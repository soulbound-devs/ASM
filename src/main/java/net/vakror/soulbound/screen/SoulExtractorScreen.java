package net.vakror.soulbound.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.FluidTankRenderer;

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
    protected void renderBg(GuiGraphics graphics, float p_97788_, int p_97789_, int p_97790_) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, BACKGROUND_TEXTURE);
        graphics.blit(BACKGROUND_TEXTURE,  x, y, 0, 0, imageWidth, imageHeight);

        soulRenderer.render(graphics, x + 43, y + 21, menu.getSoulStack());
        darkSoulRenderer.render(graphics, x + 98, y + 21, menu.getDarkSoulStack());
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        super.render(graphics, pMouseX, pMouseY, pPartialTick);
        renderTooltip(graphics, pMouseX, pMouseY);
    }


    @Override
    protected void renderLabels(GuiGraphics graphics, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderFluidTooltips(graphics, pMouseX, pMouseY, x, y);
    }

    private void renderFluidTooltips(GuiGraphics graphics, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 43, 21, soulRenderer)) {
            graphics.renderTooltip(font, soulRenderer.getTooltip(menu.getSoulStack(), TooltipFlag.Default.NORMAL),
                    Optional.empty(), pMouseX - x, pMouseY - y);
        }
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 98, 21, darkSoulRenderer)) {
            graphics.renderTooltip(font, darkSoulRenderer.getTooltip(menu.getDarkSoulStack(), TooltipFlag.Default.NORMAL),
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