package net.vakror.asm.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.LogoRenderer;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.resources.ResourceLocation;
import net.vakror.asm.ASMMod;
import net.vakror.asm.client.CustomLogoRenderer;
import net.vakror.asm.util.AnchorPoint;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TitleScreen.class)
public class LogoMixin {

    @Redirect(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/LogoRenderer;renderLogo(Lnet/minecraft/client/gui/GuiGraphics;IF)V"))
    public void renderASMLogo(LogoRenderer instance, GuiGraphics guiGraphics, int p_283270_, float p_282051_) {
        CustomLogoRenderer renderer = new CustomLogoRenderer(50, -75, 256, 79, 2047, 631, new ResourceLocation(ASMMod.MOD_ID, "textures/gui/title/asm_logo.png"), AnchorPoint.TITLE);
        renderer.draw((TitleScreen) (Object) this, guiGraphics);
    }

}
