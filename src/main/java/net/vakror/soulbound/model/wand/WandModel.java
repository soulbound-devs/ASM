package net.vakror.soulbound.model.wand;

import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.neoforged.neoforge.client.RenderTypeGroup;
import net.neoforged.neoforge.client.model.IModelBuilder;
import net.neoforged.neoforge.client.model.SimpleModelState;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.geometry.IUnbakedGeometry;
import net.neoforged.neoforge.client.model.obj.ObjModel;
import net.neoforged.neoforge.client.model.obj.ObjTokenizer;
import net.vakror.soulbound.mixin.AddQuadsInvoker;
import net.vakror.soulbound.model.WandBakedModel;
import net.vakror.soulbound.model.model.CompositeModelState;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Set;
import java.util.function.Function;

public class WandModel implements IUnbakedGeometry<WandModel> {
	private ResourceLocation baseMaterial;

	public WandModel(ResourceLocation baseMaterialIn) {
		this.baseMaterial = baseMaterialIn;
	}

	@Override
	public BakedModel bake(IGeometryBakingContext owner, ModelBakery bakery
			, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform
			, ItemOverrides overrides, ResourceLocation modelLocation) {

		TextureAtlasSprite particle = spriteGetter.apply(owner.getMaterial("particle"));

		ModelState transformsFromModel = new SimpleModelState(owner.getRootTransform(), modelTransform.isUvLocked());
		modelTransform = new CompositeModelState(transformsFromModel, modelTransform);

		var renderTypeHint = owner.getRenderTypeHint();
		var renderTypes = renderTypeHint != null ? owner.getRenderType(renderTypeHint) : RenderTypeGroup.EMPTY;
		WandItemOverrideList wandOverrides = new WandItemOverrideList(spriteGetter, owner, bakery, modelTransform, particle, modelLocation, this.baseMaterial);
		IModelBuilder<?> builder = IModelBuilder.of(owner.useAmbientOcclusion(), owner.useBlockLight(), owner.isGui3d(),
				owner.getTransforms(), wandOverrides, particle, renderTypes);
		wandOverrides.setBuilder((IModelBuilder.Simple) builder);
		Resource wand = Minecraft.getInstance().getResourceManager().getResource(this.baseMaterial).orElseThrow();
		try (ObjTokenizer tokenizer = new ObjTokenizer(wand.open())){
			ObjModel model = ObjModel.parse(tokenizer, new ObjModel.ModelSettings(this.baseMaterial, true, true, false, false, null));
			((AddQuadsInvoker) model).invokeAddQuads(owner, builder, bakery, spriteGetter, modelTransform, modelLocation);
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

		/* Vanillad BakedItemModel but with custom MealItemOverrideList, used in store data, it'll display nothing */
		return new WandBakedModel((SimpleBakedModel) builder.build(), renderTypes, owner.getRootTransform());
	}

	@Override
	public Collection<Material> getMaterials(IGeometryBakingContext context, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
		return new ArrayList<>();
	}


	public static ImmutableMap<ItemDisplayContext, ItemTransform> getTransforms(IGeometryBakingContext owner, ModelState state) {
		EnumMap<ItemDisplayContext, ItemTransform> map = new EnumMap<>(ItemDisplayContext.class);
		for (ItemDisplayContext type : ItemDisplayContext.values()) {
			ItemTransform tr = owner.getTransforms().getTransform(type);
			map.put(type, tr);
		}
		return ImmutableMap.copyOf(map);
	}
}