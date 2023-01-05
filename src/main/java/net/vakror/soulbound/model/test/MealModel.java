package net.vakror.soulbound.model.test;

import java.util.Set;
import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;

import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.CompositeModelState;
import net.minecraftforge.client.model.IModelConfiguration;
import net.minecraftforge.client.model.PerspectiveMapWrapper;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.client.model.geometry.IModelGeometry;

@SuppressWarnings("deprecation")
public class MealModel implements IModelGeometry<MealModel>{
	private ResourceLocation baseMaterial;
	private ImmutableList<MealModelLoader.TypedTextures> typedTexturesList;

	public MealModel(ResourceLocation baseMaterialIn, ImmutableList<MealModelLoader.TypedTextures> typedTexturesListIn){
		this.typedTexturesList = typedTexturesListIn;
		this.baseMaterial = baseMaterialIn;
	}

	@Override
	public BakedModel bake(IModelConfiguration owner, ModelBakery bakery
			, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform
			, ItemOverrides overrides, ResourceLocation modelLocation){

		TextureAtlasSprite particle = spriteGetter.apply(owner.resolveTexture("particle"));
		
		ModelState transformsFromModel = owner.getCombinedTransform();
		ImmutableMap<ItemTransforms.TransformType, Transformation> transformMap = transformsFromModel != null ?
                        PerspectiveMapWrapper.getTransforms(new CompositeModelState(transformsFromModel, modelTransform)) :
                        PerspectiveMapWrapper.getTransforms(modelTransform);

        modelTransform = transformsFromModel != null ? new CompositeModelState(transformsFromModel, modelTransform) : modelTransform;

		Transformation transform = modelTransform.getRotation();

        /* Vanillad BakedItemModel but with custom MealItemOverrideList, used in store data, it'll display nothing */
		return new WandBakedModel(this.baseMaterial, this.typedTexturesList, spriteGetter, particle, transformMap, transform, owner.isSideLit());
	}

	@Override
	public Collection<Material> getTextures(IModelConfiguration owner
		, Function<ResourceLocation, UnbakedModel> modelGetter
		, Set<Pair<String, String>> missingTextureErrors){

		return this.typedTexturesList.stream().map(
				typedTexture -> typedTexture.getTextures()
			).flatMap(
				textures -> textures.values().stream().map(
						location -> new Material(TextureAtlas.LOCATION_BLOCKS, location)
				)
			).collect(Collectors.toList());
	}
}