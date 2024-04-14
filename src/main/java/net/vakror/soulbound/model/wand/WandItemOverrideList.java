package net.vakror.soulbound.model.wand;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.vakror.soulbound.cap.ModAttachments;
import net.vakror.soulbound.items.custom.WandItem;
import net.vakror.soulbound.model.ModelUtils;
import net.vakror.soulbound.model.models.ActiveSealModels;
import net.vakror.soulbound.model.wand.api.AbstractWandLayer;
import net.vakror.soulbound.model.wand.api.LayerData;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Function;

public class WandItemOverrideList extends ItemOverrides {
	private final Function<Material, TextureAtlasSprite> spriteGetter;

	public WandItemOverrideList(Function<Material, TextureAtlasSprite> spriteGetterIn) {
		this.spriteGetter = spriteGetterIn;
	}

	@Override
	public BakedModel resolve(@NotNull BakedModel model, ItemStack stack, ClientLevel worldIn, LivingEntity entityIn, int seed) {
		if (stack.getItem() instanceof WandItem && model instanceof WandBakedModel wandModel) {

			for (AbstractWandLayer layer : wandModel.layers) {
				layer.setData(new LayerData(model, stack, worldIn, entityIn, seed, spriteGetter));
				layer.setModel(wandModel);
			}
			return wandModel.getNewBakedItemModel();
		}
		return model;
	}
}