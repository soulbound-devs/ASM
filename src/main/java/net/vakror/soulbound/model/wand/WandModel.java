package net.vakror.soulbound.model.wand;

import com.google.common.collect.ImmutableMap;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.neoforged.neoforge.client.model.SimpleModelState;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import net.vakror.soulbound.model.model.CompositeModelState;

import java.util.EnumMap;
import java.util.function.Function;

@SuppressWarnings("deprecation")
public class WandModel implements IUnbakedGeometry<WandModel> {
	private ResourceLocation baseMaterial;

	public WandModel(ResourceLocation baseMaterialIn){
		this.baseMaterial = baseMaterialIn;
	}

	@Override
	public BakedModel bake(IGeometryBakingContext owner, ModelBaker bakery
			, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform
			, ItemOverrides overrides, ResourceLocation modelLocation){

		TextureAtlasSprite particle = spriteGetter.apply(owner.getMaterial("particle"));

		ModelState transformsFromModel = new SimpleModelState(owner.getRootTransform(), modelTransform.isUvLocked());
		ImmutableMap<ItemDisplayContext, ItemTransform> transformMap = getTransforms(owner, new CompositeModelState(transformsFromModel, modelTransform));

		modelTransform = new CompositeModelState(transformsFromModel, modelTransform);

		Transformation transform = modelTransform.getRotation();

		/* Vanillad BakedItemModel but with custom MealItemOverrideList, used in store data, it'll display nothing */
		return new WandBakedModel(this.baseMaterial, spriteGetter, particle, transformMap, transform, owner.useBlockLight());
	}

	public static ImmutableMap<ItemDisplayContext, ItemTransform> getTransforms(IGeometryBakingContext owner, ModelState state)
	{
		EnumMap<ItemDisplayContext, ItemTransform> map = new EnumMap<>(ItemDisplayContext.class);
		for(ItemDisplayContext type : ItemDisplayContext.values())
		{
			ItemTransform tr = owner.getTransforms().getTransform(type);
			map.put(type, tr);
		}
		return ImmutableMap.copyOf(map);
	}
}