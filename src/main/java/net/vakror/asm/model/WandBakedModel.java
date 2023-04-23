package net.vakror.asm.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.pipeline.QuadBakingVertexConsumer;
import net.minecraftforge.client.model.pipeline.TransformingVertexPipeline;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/* Do not use this baked model directly, it'll display nothing, use MealBakedModel#getNewBakedItemModel */
@SuppressWarnings("deprecation")
public class WandBakedModel extends BakedItemModel {
	private TextureAtlasSprite baseSprite = null;
	private List<TextureAtlasSprite> ingredientSprites = new ArrayList<TextureAtlasSprite>();
	private Transformation transform;
	/* Cache the result of quads, using a location combination */
	private static Map<String, ImmutableList<BakedQuad>> cache = new HashMap<String, ImmutableList<BakedQuad>>();

	private static float NORTH_Z = 7.496f / 16f;
	private static float SOUTH_Z = 8.504f / 16f;

	private static float COLOR_R = 1.0f;
	private static float COLOR_G = 1.0f;
	private static float COLOR_B = 1.0f;

	public WandBakedModel(
			ResourceLocation baseMateriallocaiton
			, ImmutableList<WandModelLoader.TypedTextures> materials
			, Function<Material, TextureAtlasSprite> spriteGetter, TextureAtlasSprite particle
			, ImmutableMap<ItemDisplayContext, ItemTransform> transformMap
			, Transformation transformIn, boolean isSideLit) {
		super(ImmutableList.of(), particle, transformMap, new WandItemOverrideList(materials, spriteGetter), transformIn.isIdentity(), isSideLit);

		this.transform = transformIn;

		TextureAtlasSprite sprite = spriteGetter.apply(new Material(TextureAtlas.LOCATION_BLOCKS, baseMateriallocaiton));
		if (!sprite.atlasLocation().equals(MissingTextureAtlasSprite.getLocation())) {
			this.baseSprite = sprite;
		}
	}

	private WandBakedModel(WandBakedModel originalModel, List<TextureAtlasSprite> spritesIn) {
		super(ImmutableList.of(), originalModel.particle, originalModel.transforms, originalModel.overrides, originalModel.transform.isIdentity(), originalModel.isSideLit);

		this.baseSprite = originalModel.baseSprite;
		this.ingredientSprites = spritesIn;
		this.transform = originalModel.transform;
	}

	private ImmutableList<BakedQuad> genQuads() {
		String cacheKey = this.getCacheKeyString();

		/* Check is this sprite location combination is already baked or not  */
		if (WandBakedModel.cache.containsKey(cacheKey))
			return WandBakedModel.cache.get(cacheKey);

		ImmutableList.Builder<BakedQuad> quads = ImmutableList.builder();
		List<TextureAtlasSprite> sprites = new ArrayList<TextureAtlasSprite>();

		if (this.baseSprite != null)
			sprites.add(this.baseSprite);
		sprites.addAll(this.ingredientSprites);

		if (sprites.size() > 0) {
			/* North & South Side */
			for (int ix = 0; ix <= 15; ix++) {
				for (int iy = 0; iy <= 15; iy++) {
					/* Find the last pixel not transparent in sprites, use that to build North/South quads */
					TextureAtlasSprite sprite = WandBakedModel.findLastNotTransparent(ix, iy, sprites);
					if (sprite == null) continue;

					float xStart = ix / 16.0f;
					float xEnd = (ix + 1) / 16.0f;

					float yStart = (16 - (iy + 1)) / 16.0f;
					float yEnd = (16 - iy) / 16.0f;

					BakedQuad a = this.createQuad(
							new Vec3(xStart, yStart, WandBakedModel.NORTH_Z)
							, new Vec3(xStart, yEnd, WandBakedModel.NORTH_Z)
							, new Vec3(xEnd, yEnd, WandBakedModel.NORTH_Z)
							, new Vec3(xEnd, yStart, WandBakedModel.NORTH_Z)
							, ix, ix + 1, iy, iy + 1
							, sprite, Direction.NORTH);

					BakedQuad b = this.createQuad(
							new Vec3(xStart, yStart, WandBakedModel.SOUTH_Z)
							, new Vec3(xEnd, yStart, WandBakedModel.SOUTH_Z)
							, new Vec3(xEnd, yEnd, WandBakedModel.SOUTH_Z)
							, new Vec3(xStart, yEnd, WandBakedModel.SOUTH_Z)
							, ix, ix + 1, iy, iy + 1
							, sprite, Direction.SOUTH);

					if (a != null) {
						quads.add(a);
					}
					if (b != null) {
						quads.add(b);
					}
				}
			}

			boolean isTransparent;

			/* Up & Down Side */
			for (int ix = 0; ix <= 15; ix++) {
				float xStart = ix / 16.0f;
				float xEnd = (ix + 1) / 16.0f;

				/* Scan from Up to Bottom, find the pixel not transparent, use that to build Top quads */
				isTransparent = true;
				for (int iy = 0; iy <= 15; iy++) {
					TextureAtlasSprite sprite = WandBakedModel.findLastNotTransparent(ix, iy, sprites);
					if (sprite == null) {
						isTransparent = true;
						continue;
					}

					if (isTransparent) {
						quads.add(this.createQuad(
								new Vec3(xStart, (16 - iy) / 16.0f, WandBakedModel.NORTH_Z)
								, new Vec3(xStart, (16 - iy) / 16.0f, WandBakedModel.SOUTH_Z)
								, new Vec3(xEnd, (16 - iy) / 16.0f, WandBakedModel.SOUTH_Z)
								, new Vec3(xEnd, (16 - iy) / 16.0f, WandBakedModel.NORTH_Z)
								, ix, ix + 1, iy, iy + 1
								, sprite, Direction.UP));

						isTransparent = false;
					}
				}

				/* Scan from Bottom to Up, find the pixel not transparent, use that to build Down quads */
				isTransparent = true;
				for (int iy = 15; iy >= 0; iy--) {
					TextureAtlasSprite sprite = WandBakedModel.findLastNotTransparent(ix, iy, sprites);
					if (sprite == null) {
						isTransparent = true;
						continue;
					}

					if (isTransparent) {
						quads.add(this.createQuad(
								new Vec3(xStart, (16 - (iy + 1)) / 16.0f, WandBakedModel.NORTH_Z)
								, new Vec3(xEnd, (16 - (iy + 1)) / 16.0f, WandBakedModel.NORTH_Z)
								, new Vec3(xEnd, (16 - (iy + 1)) / 16.0f, WandBakedModel.SOUTH_Z)
								, new Vec3(xStart, (16 - (iy + 1)) / 16.0f, WandBakedModel.SOUTH_Z)
								, ix, ix + 1, iy, iy + 1
								, sprite, Direction.DOWN));

						isTransparent = false;
					}
				}
			}

			/* West & East Side */
			for (int iy = 0; iy <= 15; iy++) {
				float yStart = (16 - (iy + 1)) / 16.0f;
				float yEnd = (16 - iy) / 16.0f;

				/* Scan from Left to Right, find the pixel not transparent, use that to build West quads */
				isTransparent = true;
				for (int ix = 0; ix <= 15; ix++) {
					TextureAtlasSprite sprite = WandBakedModel.findLastNotTransparent(ix, iy, sprites);
					if (sprite == null) {
						isTransparent = true;
						continue;
					}
					if (isTransparent) {
						quads.add(this.createQuad(
								new Vec3(ix / 16.0f, yStart, WandBakedModel.NORTH_Z)
								, new Vec3(ix / 16.0f, yStart, WandBakedModel.SOUTH_Z)
								, new Vec3(ix / 16.0f, yEnd, WandBakedModel.SOUTH_Z)
								, new Vec3(ix / 16.0f, yEnd, WandBakedModel.NORTH_Z)
								, ix, ix + 1, iy, iy + 1
								, sprite, Direction.WEST));

						isTransparent = false;
					}
				}
				/* Scan from Right to Left, find the pixel not transparent, use that to build East quads */
				isTransparent = true;
				for (int ix = 15; ix >= 0; ix--) {
					TextureAtlasSprite sprite = WandBakedModel.findLastNotTransparent(ix, iy, sprites);
					if (sprite == null) {
						isTransparent = true;
						continue;
					}
					if (isTransparent) {
						quads.add(this.createQuad(
								new Vec3((ix + 1) / 16.0f, yStart, WandBakedModel.NORTH_Z)
								, new Vec3((ix + 1) / 16.0f, yEnd, WandBakedModel.NORTH_Z)
								, new Vec3((ix + 1) / 16.0f, yEnd, WandBakedModel.SOUTH_Z)
								, new Vec3((ix + 1) / 16.0f, yStart, WandBakedModel.SOUTH_Z)
								, ix, ix + 1, iy, iy + 1
								, sprite, Direction.EAST));

						isTransparent = false;
					}
				}
			}
		}

		ImmutableList<BakedQuad> returnQuads = quads.build();
		WandBakedModel.cache.put(cacheKey, returnQuads);

		return returnQuads;
	}

	/* Give four corner, generate a quad */
	private BakedQuad createQuad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4
			, int xStart, int xEnd, int yStart, int yEnd, TextureAtlasSprite sprite
			, Direction orientation) {

		BakedQuad[] quad = new BakedQuad[1];
		QuadBakingVertexConsumer builder = new QuadBakingVertexConsumer(q -> quad[0] = q);
		VertexConsumer consumer = new TransformingVertexPipeline(builder, this.transform);

		builder.setSprite(sprite);

		WandBakedModel.putVertex(consumer, v1, xStart, yEnd, sprite, orientation);
		WandBakedModel.putVertex(consumer, v2, xStart, yStart, sprite, orientation);
		WandBakedModel.putVertex(consumer, v3, xEnd, yStart, sprite, orientation);
		WandBakedModel.putVertex(consumer, v4, xEnd, yEnd, sprite, orientation);

		return quad[0];
	}

	/* Put data into the consumer */
	private static void putVertex(VertexConsumer consumer, Vec3 vec, double u, double v, TextureAtlasSprite sprite, Direction orientation) {
		float fu = sprite.getU(u);
		float fv = sprite.getV(v);

		consumer.vertex((float) vec.x, (float) vec.y, (float) vec.z)
				.color(WandBakedModel.COLOR_R, WandBakedModel.COLOR_G, WandBakedModel.COLOR_B, 1.0f)
				.normal((float) orientation.getStepX(), (float) orientation.getStepY(), (float) orientation.getStepZ())
				.uv(fu, fv)
				.uv2(0, 0)
				.endVertex();
	}

	@Override
	public BakedModel applyTransform(ItemDisplayContext type, PoseStack poseStack, boolean applyLeftHandTransform) {
		return super.applyTransform(type, poseStack, applyLeftHandTransform);
	}

	public static void putVertex(VertexConsumer builder, Position normal, double x, double y, double z, float u, float v, TextureAtlasSprite sprite, float r, float g, float b, float a) {
		float iu = sprite.getU(u);
		float iv = sprite.getV(v);
		builder
				.vertex(x, y, z)
				.uv(iu, iv)
				.uv2(0, 0)
				.color(r, g, b, a)
				.normal((float)normal.x(), (float)normal.y(), (float)normal.z())
				.endVertex();
	}

	/* Find the last sprite not transparent in sprites with given position */
	@Nullable
	private static TextureAtlasSprite findLastNotTransparent(int x, int y, List<TextureAtlasSprite> sprites){
		for(int spriteIndex = sprites.size()-1; spriteIndex >= 0; spriteIndex --){
			TextureAtlasSprite sprite = sprites.get(spriteIndex);
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

	/* Give a new MealBakedModel with sprites added */
	public WandBakedModel setSprites(List<TextureAtlasSprite> spritesIn){
		return new WandBakedModel(this, spritesIn);
	}

	/* Get a combination string of loactions, used in cache's key */
	private String getCacheKeyString(){
		List<String> locations = new ArrayList<String>();
		if(this.baseSprite != null)
			locations.add(this.baseSprite.atlasLocation().toString());

		for(TextureAtlasSprite sprite : this.ingredientSprites) {
			if (sprite != null) {
				locations.add(sprite.atlasLocation().toString());
			}
		}

		String str = String.join(",", locations);
		return str;
	}
}