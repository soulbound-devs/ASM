package net.vakror.soulbound.mixin;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.vakror.soulbound.util.ItemCountRenderHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

//todo fix
@OnlyIn(Dist.CLIENT)
@Mixin (GuiGraphics.class)
public class RenderItemCountFixin {

	@Redirect (method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
			at = @At (value = "INVOKE", target = "Ljava/lang/String;valueOf(I)Ljava/lang/String;"))
	private String render(int i) {
		return ItemCountRenderHandler.getInstance().toConsiseString(i);
	}

	@Redirect (method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
			at = @At (value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;width(Ljava/lang/String;)I"))
	private int width(Font renderer, String text) {
		return (int) (renderer.width(text) * ItemCountRenderHandler.getInstance().scale(text));
	}

	@Inject (method = "renderGuiItemDecorations(Lnet/minecraft/client/gui/Font;Lnet/minecraft/world/item/ItemStack;IILjava/lang/String;)V",
			at = @At (value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(DDD)V", shift = At.Shift.AFTER),
			locals = LocalCapture.CAPTURE_FAILHARD)
	private void rescaleText(Font font, ItemStack stack, int x, int y, String String, CallbackInfo ci, GuiGraphics graphics, String s) {
		float f = ItemCountRenderHandler.getInstance().scale(s);
		if (f != 1f) {
			matrices.translate(x * (1 - f), y * (1 - f) + (1 - f) * 16, 0);
			matrices.scale(f, f, f);
		}
	}
}