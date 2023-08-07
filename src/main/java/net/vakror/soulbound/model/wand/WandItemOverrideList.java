package net.vakror.soulbound.model.wand;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.vakror.soulbound.capability.wand.ItemSealProvider;
import net.vakror.soulbound.items.custom.SealableItem;
import net.vakror.soulbound.items.custom.WandItem;
import org.jetbrains.annotations.NotNull;

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
	public BakedModel resolve(@NotNull BakedModel model, ItemStack stack, ClientLevel worldIn, LivingEntity entityIn, int seed) {
		if (stack.getItem() instanceof WandItem && model instanceof WandBakedModel mealModel) {
			AtomicReference<WandBakedModel> finalWandModel = new AtomicReference<>(mealModel);
			List<TextureAtlasSprite> sprites = new ArrayList<TextureAtlasSprite>();

			List<WandModelLoader.TypedTextures> mutableTypedTextures = Lists.newArrayList(this.materials);
			stack.getCapability(ItemSealProvider.SEAL).ifPresent(wand -> {
				for (WandModelLoader.TypedTextures typedTextures : mutableTypedTextures) {
					TextureAtlasSprite sprite = (stack.getTag().contains("customModel") && !stack.getTag().getString("customModel").isBlank()) ? typedTextures.getSprite(stack.getTag().getString("customModel"), this.spriteGetter, true) : typedTextures.getSprite("wand", this.spriteGetter);
					mutableTypedTextures.remove(typedTextures);
					sprites.add(sprite);
					finalWandModel.set(finalWandModel.get().setIngredientSprites(sprites));
					break;
				}
				List<WandModelLoader.TypedTextures> mutableTypedTextures1 = Lists.newArrayList(this.materials);
				if (!stack.getTag().getString("activeSeal").equals("")) {
					for (WandModelLoader.TypedTextures typedTextures : mutableTypedTextures1) {
						String activeSeal = stack.getTag().getString("activeSeal");
						TextureAtlasSprite sprite = typedTextures.getSprite(activeSeal, this.spriteGetter, false);
						if (sprite != null) {
							mutableTypedTextures1.remove(typedTextures);
							sprites.add(sprite);
							break;
						}
					}
				}
				SealableItem sealableItem = (SealableItem) stack.getItem();
				finalWandModel.set(finalWandModel.get().setSealPairs(List.of(new Pair<>(wand.getPassiveSeals().size(), sealableItem.tier.getPassiveSlots()), new Pair<>(wand.getAttackSeals().size(), sealableItem.tier.getAttackSlots()), new Pair<>(wand.getAmplifyingSeals().size(), sealableItem.tier.getAmplificationSlots()))));
				finalWandModel.set(finalWandModel.get().setBaseWandColor(sealableItem.tier.getColor()));
//				finalWandModel.set(finalWandModel.get().setColorOverrides(wand.getColorOverrides()));
				finalWandModel.set(finalWandModel.get().setIngredientSprites(sprites));
			});
			return finalWandModel.get().getNewBakedItemModel();
		}
		return model;
	}
}