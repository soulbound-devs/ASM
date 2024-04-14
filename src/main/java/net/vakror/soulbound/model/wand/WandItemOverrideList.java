package net.vakror.soulbound.model.wand;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
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
import net.vakror.soulbound.seal.SealRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class WandItemOverrideList extends ItemOverrides {
	private Function<Material, TextureAtlasSprite> spriteGetter;
	private ResourceLocation baseMaterialLocation;

	public WandItemOverrideList(ResourceLocation baseMaterialLocation, Function<Material, TextureAtlasSprite> spriteGetterIn) {
		this.spriteGetter = spriteGetterIn;
		this.baseMaterialLocation = baseMaterialLocation;
	}

	/* Read NBT data from stack and choose what textures in use and merge them */
	@Override
	public BakedModel resolve(BakedModel model, ItemStack stack, ClientLevel worldIn, LivingEntity entityIn, int seed) {
		if (stack.getItem() instanceof WandItem && model instanceof WandBakedModel wandModel) {

			TextureAtlasSprite wandSprite = ModelUtils.getSprite(spriteGetter, baseMaterialLocation);
			final TextureAtlasSprite[] activeSprite = new TextureAtlasSprite[1];

			stack.getExistingData(ModAttachments.SEAL_ATTACHMENT).ifPresent(wand -> {
				if (!stack.getTag().getString("activeSeal").equals("")) {
					activeSprite[0] = ModelUtils.getSprite(this.spriteGetter, ActiveSealModels.MODELS.get(stack.getTag().getString("activeSeal")));
				}
			});
			wandModel.setSprites(wandSprite, activeSprite[0]);
			return wandModel.getNewBakedItemModel();
		}
		return model;
	}
}