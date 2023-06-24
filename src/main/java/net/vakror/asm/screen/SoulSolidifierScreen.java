package net.vakror.asm.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.TooltipFlag;
import net.vakror.asm.ASMMod;
import net.vakror.asm.client.renderer.FluidTankRenderer;

import java.util.Optional;

public class SoulSolidifierScreen extends AbstractContainerScreen<SoulSolidifierMenu> {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(ASMMod.MOD_ID, "textures/gui/soul_solidifier.png");
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
    protected void renderBg(GuiGraphics graphics, float p_97788_, int p_97789_, int p_97790_) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        graphics.blit(BACKGROUND_TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        if (menu.isCrafting()) {
            graphics.blit(BACKGROUND_TEXTURE, x + 118, y + 37, 177, 38, menu.getScaledProgress(), 8);
        }
        renderer.render(graphics.pose(), x + 50, y + 8, menu.getStack());
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(graphics);
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
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 50, 8)) {
            graphics.renderTooltip(this.font, renderer.getTooltip(menu.getStack(), TooltipFlag.Default.NORMAL),
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