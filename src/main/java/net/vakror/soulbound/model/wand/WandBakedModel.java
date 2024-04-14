package net.vakror.soulbound.model.wand;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.item.ItemDisplayContext;
import net.vakror.soulbound.model.model.BakedItemModel;
import net.vakror.soulbound.model.wand.api.AbstractWandLayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/* Do not use this baked model directly, it'll display nothing, use WandBakedModel#getNewBakedItemModel */
public class WandBakedModel extends BakedItemModel {
	protected final List<AbstractWandLayer> layers;

	private final Transformation transform;
	/* Cache the result of quads, using a location combination */
	private static final Map<String, ImmutableList<BakedQuad>> cache = new HashMap<>();

	public WandBakedModel(
			List<AbstractWandLayer> layers
			, Function<Material, TextureAtlasSprite> spriteGetter, TextureAtlasSprite particle
			, ImmutableMap<ItemDisplayContext, ItemTransform> transformMap
			, Transformation transformIn, boolean isSideLit) {
		super(ImmutableList.of(), particle, transformMap, new WandItemOverrideList(spriteGetter), transformIn.isIdentity(), isSideLit);

		this.layers = layers;

		this.transform = transformIn;

	}

	/**
	 *
	 * When I wrote this god and me knew what it does
	 * Now, only god knows
	 *
	 * @return the quads
	 */
	private ImmutableList<BakedQuad> genQuads() {
		String cacheKey = this.getCacheKeyString();

		/* Check if this sprite location combination is already baked or not  */
		if (WandBakedModel.cache.containsKey(cacheKey))
			return WandBakedModel.cache.get(cacheKey);

		ImmutableList.Builder<BakedQuad> quads = ImmutableList.builder();

		for (AbstractWandLayer layer : layers) {
			layer.render(quads, this.transform);
		}

        ImmutableList<BakedQuad> returnQuads = quads.build();
		WandBakedModel.cache.put(cacheKey, returnQuads);

		return returnQuads;
	}

	@Override
	public BakedModel applyTransform(ItemDisplayContext type, PoseStack poseStack, boolean applyLeftHandTransform) {
		return super.applyTransform(type, poseStack, applyLeftHandTransform);
	}

	/* Give a BakedItemModel base on data in this, can use directly to display */
	public BakedItemModel getNewBakedItemModel(){
		return new BakedItemModel(this.genQuads(), this.particle, this.transforms, this.overrides, this.transform.isIdentity(), this.isSideLit);
	}

	/* Get a combination string of locations, used in cache's key */
	private String getCacheKeyString(){
		List<String> locations = new ArrayList<>();
		for (AbstractWandLayer layer : layers) {
			locations.add(layer.getCacheKey());
		}
        return String.join(",", locations);
	}
}