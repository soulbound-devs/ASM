package net.vakror.soulbound.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
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
    public void renderBg(GuiGraphics graphics, float delta, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GENERIC_54);
        graphics.pose().pushPose();
        graphics.pose().translate(this.leftPos, this.topPos, 0.0D);

        // Background storage texture pseudo-generation //

        // Upper-left corner
        graphics.blit(GENERIC_54, 0, 0, 0, 0, 7, 17);

        // Top
        graphics.blit(GENERIC_54, 7, 0, menu.getWidth() * 18, 17, 7, 0, 1, 17, 256, 256);

        // Upper-right corner
        graphics.blit(GENERIC_54, 7 + menu.getWidth() * 18, 0, 169, 0, 7, 17);

        // Left
        graphics.blit(GENERIC_54, 0, 17, 7, (menu.getHeight() + 4) * 18 + 22, 0, 17, 7, 1, 256, 256);

        // Lower-left corner
        graphics.blit(GENERIC_54, 0, (menu.getHeight() + 4) * 18 + 34, 0, 215, 7, 7);

        // Lower
        graphics.blit(GENERIC_54, 7, (menu.getHeight() + 4) * 18 + 34, menu.getWidth() * 18, 7, 7, 215, 1, 7, 256, 256);

        // Lower-right corner
        graphics.blit(GENERIC_54, (menu.getWidth() * 18 + 7), (menu.getHeight() + 4) * 18 + 34, 169, 215, 7, 7);

        // Right
        graphics.blit(GENERIC_54, (menu.getWidth() * 18 + 7), 17, 7, (menu.getHeight() + 4) * 18 + 17, 169, 17, 7, 1, 256, 256);

        // Background fill
        graphics.fill(7, 17, this.imageWidth - 10, this.imageHeight - 7, 0xFFC6C6C6);

        this.menu.slots.forEach(s -> {
            graphics.blit(GENERIC_54, s.x - 1, s.y - 1, 7, 17, 18, 18);
        });

        graphics.pose().popPose();
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        super.render(graphics, mouseX, mouseY, delta);
        this.renderTooltip(graphics, mouseX, mouseY);
    }
}