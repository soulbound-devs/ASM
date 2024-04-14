package net.vakror.soulbound.model.wand;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonObject;
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
import net.vakror.soulbound.model.models.WandModelReaders;
import net.vakror.soulbound.model.wand.api.AbstractWandLayer;
import net.vakror.soulbound.model.wand.api.IWandModelReader;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Function;

public class WandModel implements IUnbakedGeometry<WandModel> {
	private final JsonObject object;

	public WandModel(JsonObject object){
		this.object = object;
	}

	@Override
	public @NotNull BakedModel bake(IGeometryBakingContext owner, @NotNull ModelBaker bakery
			, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform
			, @NotNull ItemOverrides overrides, ResourceLocation modelLocation){

		ResourceLocation item = new ResourceLocation(modelLocation.toString().split("#")[0]);
		IWandModelReader reader = WandModelReaders.READERS.get(item);
		List<AbstractWandLayer> layers = reader.getLayers(object);

		TextureAtlasSprite particle = spriteGetter.apply(owner.getMaterial("particle"));

		ModelState transformsFromModel = new SimpleModelState(owner.getRootTransform(), modelTransform.isUvLocked());
		ImmutableMap<ItemDisplayContext, ItemTransform> transformMap = getTransforms(owner);

		modelTransform = new CompositeModelState(transformsFromModel, modelTransform);

		Transformation transform = modelTransform.getRotation();

		/* Vanilla'd BakedItemModel but with custom Override List, used in store data, it'll display nothing */
		return new WandBakedModel(layers, spriteGetter, particle, transformMap, transform, owner.useBlockLight());
	}

	public static ImmutableMap<ItemDisplayContext, ItemTransform> getTransforms(IGeometryBakingContext owner)
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