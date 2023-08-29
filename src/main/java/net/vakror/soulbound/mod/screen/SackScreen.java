package net.vakror.soulbound.mod.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class SackScreen extends AbstractContainerScreen<SackMenu> {

    public static final ResourceLocation GENERIC_54 = new ResourceLocation("textures/gui/container/generic_54.png");

    public SackScreen(SackMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
        this.imageWidth = handler.getWidth() * 18 + 17;
        this.imageHeight = (handler.getHeight() + 4) * 18 + 41;
        this.inventoryLabelY = handler.getHeight() * 18 + 20;
    }

    @Override
    public void renderBg(PoseStack matrices, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GENERIC_54);
        matrices.pushPose();
        matrices.translate(this.leftPos, this.topPos, 0.0D);

        // Background storage texture pseudo-generation //

        // Upper-left corner
        blit(matrices, 0, 0, 0, 0, 7, 17);

        // Top
        blit(matrices, 7, 0, menu.getWidth() * 18, 17, 7, 0, 1, 17, 256, 256);

        // Upper-right corner
        blit(matrices, 7 + menu.getWidth() * 18, 0, 169, 0, 7, 17);

        // Left
        blit(matrices, 0, 17, 7, (menu.getHeight() + 4) * 18 + 22, 0, 17, 7, 1, 256, 256);

        // Lower-left corner
        blit(matrices, 0, (menu.getHeight() + 4) * 18 + 34, 0, 215, 7, 7);

        // Lower
        blit(matrices, 7, (menu.getHeight() + 4) * 18 + 34, menu.getWidth() * 18, 7, 7, 215, 1, 7, 256, 256);

        // Lower-right corner
        blit(matrices, (menu.getWidth() * 18 + 7), (menu.getHeight() + 4) * 18 + 34, 169, 215, 7, 7);

        // Right
        blit(matrices, (menu.getWidth() * 18 + 7), 17, 7, (menu.getHeight() + 4) * 18 + 17, 169, 17, 7, 1, 256, 256);

        // Background fill
        fill(matrices, 7, 17, this.imageWidth - 10, this.imageHeight - 7, 0xFFC6C6C6);

        this.menu.slots.forEach(s -> {
            blit(matrices, s.x - 1, s.y - 1, 7, 17, 18, 18);
        });

//        renderInvSlots(matrices);

        matrices.popPose();
    }

    private void renderInvSlots(PoseStack matrices) {
        float amountOfPixelsToScaleSlotsTo = 18;

        if (menu.getHeight() * menu.getWidth() > 54) {
            amountOfPixelsToScaleSlotsTo = ((float) 54 / (menu.getWidth() * menu.getHeight())) * 18;
        }

        float scale = amountOfPixelsToScaleSlotsTo / 18;


        float newAmountOfPixelsToScaleTo = 18;
        if (menu.getHeight() > 6 || menu.getWidth() > 9) {
            if ((menu.getHeight() - 6) > (menu.getWidth() - 9)) {
                newAmountOfPixelsToScaleTo = ((float) 6 / menu.getHeight()) * 18;
            }
            if ((menu.getHeight() - 6) < (menu.getWidth() - 9)) {
                newAmountOfPixelsToScaleTo = ((float) 9 / menu.getWidth()) * 18;
            }
        }

        float newScale = newAmountOfPixelsToScaleTo / 18;



        matrices.scale(newScale, newScale, newScale);

        for (int n = 0; n < menu.getHeight(); ++n) {
            for (int m = 0; m < menu.getWidth(); ++m)  {
                blit(matrices, (int) (7 + (m + 1) * Math.floor(newAmountOfPixelsToScaleTo)), (int) (17 + (n + 1) * Math.floor(newAmountOfPixelsToScaleTo)), 7, 17, 18, 18);
            }
        }
    }

    @Override
    public void render(@NotNull PoseStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        super.render(matrices, mouseX, mouseY, delta);
        this.renderTooltip(matrices, mouseX, mouseY);
    }
}