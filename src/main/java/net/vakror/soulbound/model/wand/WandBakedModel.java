package net.vakror.soulbound.model.wand;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.pipeline.QuadBakingVertexConsumer;
import net.minecraftforge.client.model.pipeline.TransformingVertexPipeline;
import net.vakror.soulbound.model.model.BakedItemModel;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

/* Do not use this baked model directly, it'll display nothing, use WandBakedModel#getNewBakedItemModel */
@SuppressWarnings("deprecation")
public class WandBakedModel extends BakedItemModel {
	private TextureAtlasSprite baseSprite = null;
	private int baseWandColor = 0;
	public Map<Pair<Integer, Integer>, Integer> colorOverrides = new HashMap<>();
	private List<TextureAtlasSprite> ingredientSprites = new ArrayList<TextureAtlasSprite>();
	private List<Pair<Integer, Integer>> pairs = new ArrayList<>();
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
			, ImmutableMap<ItemTransforms.TransformType, ItemTransform> transformMap
			, Transformation transformIn, boolean isSideLit
	, int defaultTint) {
		super(ImmutableList.of(), particle, transformMap, new WandItemOverrideList(materials, spriteGetter), transformIn.isIdentity(), isSideLit);

		this.transform = transformIn;

		TextureAtlasSprite sprite = spriteGetter.apply(new Material(TextureAtlas.LOCATION_BLOCKS, baseMateriallocaiton));
		if (!sprite.getName().equals(MissingTextureAtlasSprite.getLocation())) {
			this.baseSprite = sprite;
		}
		if (this.baseWandColor == 0) {
			this.baseWandColor = defaultTint;
		}
	}

	private WandBakedModel(WandBakedModel originalModel, List<TextureAtlasSprite> spritesIn) {
		super(ImmutableList.of(), originalModel.particle, originalModel.transforms, originalModel.overrides, originalModel.transform.isIdentity(), originalModel.isSideLit);

		this.baseSprite = originalModel.baseSprite;
		this.baseWandColor = originalModel.baseWandColor;
		this.pairs = originalModel.pairs;
		this.ingredientSprites = spritesIn;
		this.transform = originalModel.transform;
		this.colorOverrides = originalModel.colorOverrides;
	}


	public static Scanner scanner = new Scanner(System.in);

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

		if (this.baseSprite != null)
			sprites.add(this.baseSprite);
		sprites.addAll(this.ingredientSprites);

		if (sprites.size() > 0) {
			bakeZQuads(quads, sprites);

			boolean isTransparent = false;

			bakeYQuads(quads, sprites, isTransparent);
			bakeXQuads(quads, sprites, isTransparent);
		}

		ImmutableList<BakedQuad> returnQuads = quads.build();
		WandBakedModel.cache.put(cacheKey, returnQuads);

		return returnQuads;
	}

	private void bakeXQuads(ImmutableList.Builder<BakedQuad> quads, List<TextureAtlasSprite> sprites, boolean isTransparent) {
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

	private void bakeZQuads(ImmutableList.Builder<BakedQuad> quads, List<TextureAtlasSprite> sprites) {
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
	}

	private void bakeYQuads(ImmutableList.Builder<BakedQuad> quads, List<TextureAtlasSprite> sprites, boolean isTransparent) {
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
	}

	/* Give four corner, generate a quad */
	private BakedQuad createQuad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4
			, int xStart, int xEnd, int yStart, int yEnd, TextureAtlasSprite sprite
			, Direction orientation) {

		BakedQuad[] quad = new BakedQuad[1];
		QuadBakingVertexConsumer builder = new QuadBakingVertexConsumer(q -> quad[0] = q);
		builder.setTintIndex(this.ingredientSprites.contains(sprite) ? this.ingredientSprites.indexOf(sprite): 0);
		VertexConsumer consumer = new TransformingVertexPipeline(builder, this.transform);

		builder.setSprite(sprite);

		float redOverride = 0;
		float greenOverride = 0;
		float blueOverride = 0;

		if (sprite.equals(this.baseSprite)) {
			redOverride = (float) FastColor.ARGB32.red(baseWandColor) / 255;
			greenOverride = (float) FastColor.ARGB32.green(baseWandColor) / 255;
			blueOverride = (float) FastColor.ARGB32.blue(baseWandColor) / 255;
		}

		if (colorOverrides.containsKey(new Pair<>(xStart, yStart))) {
			int color = colorOverrides.get(new Pair<>(xStart, yStart));
			redOverride = (float) FastColor.ARGB32.red(color) / 255;
			greenOverride = (float) FastColor.ARGB32.green(color) / 255;
			blueOverride = (float) FastColor.ARGB32.blue(color) / 255;
		}

		WandBakedModel.putVertex(consumer, v1, xStart, yEnd, sprite, orientation, redOverride, greenOverride, blueOverride);
		WandBakedModel.putVertex(consumer, v2, xStart, yStart, sprite, orientation, redOverride, greenOverride, blueOverride);
		WandBakedModel.putVertex(consumer, v3, xEnd, yStart, sprite, orientation, redOverride, greenOverride, blueOverride);
		WandBakedModel.putVertex(consumer, v4, xEnd, yEnd, sprite, orientation, redOverride, greenOverride, blueOverride);

		return quad[0];
	}

	public int blend(int r1, int g1, int b1, int r2, int g2, int b2) {
		return FastColor.ARGB32.color(255, (r1 + r2) / 2,  (g1 + g2) / 2,  (b1 + b2) / 2);
	}

	/* Put data into the consumer */
	private static void putVertex(VertexConsumer consumer, Vec3 vec, double u, double v, TextureAtlasSprite sprite, Direction orientation, float redOverride, float greenOverride, float blueOverride) {
		float fu = sprite.getU(u);
		float fv = sprite.getV(v);

		consumer.vertex((float) vec.x, (float) vec.y, (float) vec.z)
				.color(redOverride == 0f? WandBakedModel.COLOR_R: redOverride, greenOverride == 0f?  WandBakedModel.COLOR_G: greenOverride, blueOverride == 0f? WandBakedModel.COLOR_B: blueOverride, 1)
				.normal((float) orientation.getStepX(), (float) orientation.getStepY(), (float) orientation.getStepZ())
				.uv(fu, fv)
				.uv2(0, 0)
				.endVertex();
	}

	@Override
	public BakedModel applyTransform(ItemTransforms.TransformType type, PoseStack poseStack, boolean applyLeftHandTransform) {
		return super.applyTransform(type, poseStack, applyLeftHandTransform);
	}

	/* Find the last sprite not transparent in sprites with given position */
	@Nullable
	private static TextureAtlasSprite findLastNotTransparent(int x, int y, List<TextureAtlasSprite> sprites){
		for(int spriteIndex = sprites.size() - 1; spriteIndex >= 0; spriteIndex--){
			TextureAtlasSprite sprite = sprites.get(spriteIndex);
			if (sprite != null) {
				if (!sprite.isTransparent(0, x, y)) {
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
	public WandBakedModel setIngredientSprites(List<TextureAtlasSprite> spritesIn){
		return new WandBakedModel(this, spritesIn);
	}

	/* Give a new MealBakedModel with sprites added */
	public WandBakedModel setColorOverrides(Map<Pair<Integer, Integer>, Integer> colorOverrides) {
		this.colorOverrides = colorOverrides;
		return this;
	}

	/* Give a new MealBakedModel with sprites added */
	public WandBakedModel setBaseWandColor(int baseColor) {
		this.baseWandColor = baseColor;
		return this;
	}

	public WandBakedModel setSealPairs(List<Pair<Integer, Integer>> pairs) {
		this.pairs = pairs;
		return this;
	}

	/* Get a combination string of locations, used in cache's key */
	private String getCacheKeyString(){
		List<String> locations = new ArrayList<String>();
		if(this.baseSprite != null)
			locations.add(this.baseSprite.getName().toString());

		for(TextureAtlasSprite sprite : this.ingredientSprites) {
			if (sprite != null) {
				locations.add(sprite.getName().toString());
			}
		}

		locations.add("BaseColor:" + String.valueOf(baseWandColor));
		colorOverrides.forEach((coords, color) -> locations.add("colorOverride:" + coords.getFirst() + " | " + coords.getSecond() + color));

		return String.join(", ", locations);
	}

	public WandBakedModel setBaseWand(TextureAtlasSprite wand) {
		this.baseSprite = wand;
		return this;
	}
}