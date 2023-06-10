package net.vakror.asm.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.vakror.asm.capability.wand.ItemSealProvider;
import net.vakror.asm.items.custom.WandItem;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class WandItemOverrideList extends ItemOverrides {
	private ImmutableList<WandModelLoader.TypedTextures> materials;
	private Function<Material, TextureAtlasSprite> spriteGetter;

	public WandItemOverrideList(
			ImmutableList<WandModelLoader.TypedTextures> materialsIn
			, Function<Material, TextureAtlasSprite> spriteGetterIn) {

		this.materials = materialsIn;
		this.spriteGetter = spriteGetterIn;
	}

	/* Read NBT data from stack and choose what textures in use and merge them */
	@Override
	public BakedModel resolve(BakedModel model, ItemStack stack, ClientLevel worldIn, LivingEntity entityIn, int seed) {
		if (stack.getItem() instanceof WandItem && model instanceof WandBakedModel mealModel) {
			AtomicReference<WandBakedModel> finalMealModel = new AtomicReference<>(mealModel);
			List<TextureAtlasSprite> sprites = new ArrayList<TextureAtlasSprite>();

			List<WandModelLoader.TypedTextures> mutableTypedTextures = Lists.newArrayList(this.materials);
			for (WandModelLoader.TypedTextures typedTextures : mutableTypedTextures) {
				TextureAtlasSprite sprite = typedTextures.getSprite("wand", this.spriteGetter);
				mutableTypedTextures.remove(typedTextures);
				sprites.add(sprite);
				finalMealModel.set(finalMealModel.get().setIngredientSprites(sprites));
				break;
			}
			stack.getCapability(ItemSealProvider.SEAL).ifPresent(wand -> {
				List<WandModelLoader.TypedTextures> mutableTypedTextures1 = Lists.newArrayList(this.materials);
				if (!stack.getTag().getString("activeSeal").equals("")) {
					for (WandModelLoader.TypedTextures typedTextures : mutableTypedTextures1) {
						String  activeSeal = stack.getTag().getString("activeSeal");
						TextureAtlasSprite sprite = typedTextures.getSprite(activeSeal, this.spriteGetter);
						if (sprite != null) {
							mutableTypedTextures1.remove(typedTextures);
							sprites.add(sprite);
							break;
						}
					}
				}
				finalMealModel.set(finalMealModel.get().setIngredientSprites(sprites));
			});
			return finalMealModel.get().getNewBakedItemModel();
		}
		return model;
	}
}