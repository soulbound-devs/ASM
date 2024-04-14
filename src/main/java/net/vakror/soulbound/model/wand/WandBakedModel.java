package net.vakror.soulbound.model.wand;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.vakror.soulbound.model.ModelUtils;
import net.vakror.soulbound.model.model.BakedItemModel;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/* Do not use this baked model directly, it'll display nothing, use WandBakedModel#getNewBakedItemModel */
@SuppressWarnings("deprecation")
public class WandBakedModel extends BakedItemModel {
	private TextureAtlasSprite baseSprite = null;
	private TextureAtlasSprite barkSprite = null;
	private TextureAtlasSprite wandSprite = null;

	private Transformation transform;
	/* Cache the result of quads, using a location combination */
	private static Map<String, ImmutableList<BakedQuad>> cache = new HashMap<String, ImmutableList<BakedQuad>>();

	public static float NORTH_Z = 7.496f / 16f;
	public static float SOUTH_Z = 8.504f / 16f;

	public static float COLOR_R = 1.0f;
	public static float COLOR_G = 1.0f;
	public static float COLOR_B = 1.0f;

	public WandBakedModel(
			ResourceLocation baseMaterialLocation
			, Function<Material, TextureAtlasSprite> spriteGetter, TextureAtlasSprite particle
			, ImmutableMap<ItemDisplayContext, ItemTransform> transformMap
			, Transformation transformIn, boolean isSideLit) {
		super(ImmutableList.of(), particle, transformMap, new WandItemOverrideList(baseMaterialLocation, spriteGetter), transformIn.isIdentity(), isSideLit);

		this.transform = transformIn;

		TextureAtlasSprite sprite = ModelUtils.getSprite(spriteGetter, baseMaterialLocation);
        assert sprite != null;
        if (!sprite.atlasLocation().equals(MissingTextureAtlasSprite.getLocation())) {
			this.baseSprite = sprite;
		}
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
		List<TextureAtlasSprite> sprites = new ArrayList<TextureAtlasSprite>();

		if (this.baseSprite != null) {
			sprites.add(this.baseSprite);
		}
		sprites.add(wandSprite);
		if (this.barkSprite != null) {
			sprites.add(this.barkSprite);
		}

        ModelUtils.genQuads(sprites, quads, this.transform);

        ImmutableList<BakedQuad> returnQuads = quads.build();
		WandBakedModel.cache.put(cacheKey, returnQuads);

		return returnQuads;
	}

	@Override
	public BakedModel applyTransform(ItemDisplayContext type, PoseStack poseStack, boolean applyLeftHandTransform) {
		return super.applyTransform(type, poseStack, applyLeftHandTransform);
	}

	/* Find the last sprite not transparent in sprites with given position */
	@Nullable
    public static TextureAtlasSprite findLastNotTransparent(int x, int y, List<TextureAtlasSprite> sprites) {
		for (TextureAtlasSprite sprite : sprites) {
			if (sprite != null) {
				if (!sprite.contents().isTransparent(0, x, y)) {
					return sprite;
				}
			}
		}
		return null;
	}

	/* Give a BakedItemModel base on data in this, can use directly to display */
	public BakedItemModel getNewBakedItemModel(){
		return new BakedItemModel(this.genQuads(), this.particle, this.transforms, this.overrides, this.transform.isIdentity(), this.isSideLit);
	}

	/* Get a combination string of locations, used in cache's key */
	private String getCacheKeyString(){
		List<String> locations = new ArrayList<String>();
		if(this.baseSprite != null) {
			locations.add(this.baseSprite.contents().name().toString());
		}
		locations.add(this.wandSprite.contents().name().toString());
		if(this.barkSprite != null) {
			locations.add(this.barkSprite.contents().name().toString());
		}

		String str = String.join(",", locations);
		return str;
	}

	public void setSprites(TextureAtlasSprite wandSprite, TextureAtlasSprite sealSprite) {
		baseSprite = sealSprite;
		this.wandSprite = wandSprite;
	}
}