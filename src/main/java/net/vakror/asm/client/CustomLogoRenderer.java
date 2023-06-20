package net.vakror.asm.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.resources.ResourceLocation;
import net.vakror.asm.util.AnchorPoint;

public class CustomLogoRenderer {

    /**
     * Texture path, used for all buttons.
     */
    protected ResourceLocation texture;

    /**
     * X and Y offsets.
     */
    protected final int xOff, yOff, width, height;

    /**
     * Texture size and coordinates.
     */
    protected final int texWidth, texHeight;

    /**
     * The source anchor for the logo.
     */
    protected final AnchorPoint anchor;

    public CustomLogoRenderer(int xOff, int yOff, int width, int height, int texWidth, int texHeight, ResourceLocation texture, AnchorPoint anchor) {
        this.xOff = xOff;
        this.yOff = yOff;
        this.width = width;
        this.height = height;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
        this.texture = texture;
        this.anchor = anchor;
    }

    public void draw(TitleScreen screen, GuiGraphics gui, float transparency) {
        gui.setColor(1.0F, 1.0F, 1.0F, transparency);
        gui.pose().pushPose();
        gui.pose().translate(this.anchor.getX(screen), this.anchor.getY(screen), 0);
        gui.pose().scale((float) this.width / this.texWidth, (float) this.height / this.texHeight, 1);
        gui.blit(this.texture, this.xOff, this.yOff, 0, 0, this.texWidth, this.texHeight, this.texWidth, this.texHeight);
        gui.pose().popPose();
        RenderSystem.enableDepthTest();
    }
}
