package net.vakror.soulbound.model.wand;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.RenderTypeGroup;
import net.minecraftforge.client.model.IModelBuilder;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.obj.ObjModel;
import net.minecraftforge.client.model.obj.ObjTokenizer;
import net.vakror.soulbound.capability.wand.ItemSealProvider;
import net.vakror.soulbound.items.custom.WandItem;
import net.vakror.soulbound.mixin.AddQuadsInvoker;
import net.vakror.soulbound.model.models.ActiveSealModels;
import net.vakror.soulbound.model.models.WandModels;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

public class WandItemOverrideList extends ItemOverrides {
	private Function<Material, TextureAtlasSprite> spriteGetter;
	private final IGeometryBakingContext owner;
	private final ModelBakery bakery;
	private final ModelState modelTransform;
	private final TextureAtlasSprite particle;
	private IModelBuilder.Simple builder;
	private final ResourceLocation modelLocation;
	private final ResourceLocation baseWand;
	private final RenderTypeGroup renderTypes;

	public WandItemOverrideList(Function<Material, TextureAtlasSprite> spriteGetter, IGeometryBakingContext owner, ModelBakery bakery, ModelState modelTransform, TextureAtlasSprite particle, ResourceLocation modelLocation, ResourceLocation baseWand, RenderTypeGroup renderTypes) {
		this.spriteGetter = spriteGetter;
		this.owner = owner;
		this.bakery = bakery;
		this.modelTransform = modelTransform;
		this.particle = particle;
		this.modelLocation = modelLocation;
		this.baseWand = baseWand;
		this.renderTypes = renderTypes;
	}

	/* Read NBT data from stack and choose what textures in use and merge them */
	@Override
	public BakedModel resolve(@NotNull BakedModel model, ItemStack stack, ClientLevel worldIn, LivingEntity entityIn, int seed) {
		if (stack.getItem() instanceof WandItem) {
			AtomicReference<BakedModel> finalWandModel = new AtomicReference<>();
			AtomicReference<ResourceLocation> activeSeal = new AtomicReference<>();
			AtomicReference<ResourceLocation> wandModel = new AtomicReference<>();
			stack.getCapability(ItemSealProvider.SEAL).ifPresent(wand -> {
				wandModel.set((stack.getTag().contains("customModel") && !stack.getTag().getString("customModel").isBlank()) ? WandModels.MODELS.get(stack.getTag().getString("customModel")): baseWand);
				if (!stack.getTag().getString("activeSeal").equals("")) {
					activeSeal.set(ActiveSealModels.MODELS.get(stack.getTag().getString("activeSeal")));
				}
			});
			finalWandModel.set(getBakedModel(wandModel.get() == null ? MissingTextureAtlasSprite.getLocation(): wandModel.get(), activeSeal.get() == MissingTextureAtlasSprite.getLocation() ? null: activeSeal.get()));
			return finalWandModel.get();
		}
		return model;
	}

	public void setBuilder(IModelBuilder.Simple builder) {
		this.builder = builder;
	}

	@SuppressWarnings("deprecation")
	private BakedModel getBakedModel(ResourceLocation wand, @Nullable ResourceLocation activeSeal) {
		builder = (IModelBuilder.Simple) IModelBuilder.of(owner.useAmbientOcclusion(), owner.useBlockLight(), owner.isGui3d(),
				owner.getTransforms(), this, particle, renderTypes);
		Resource wandResource = Minecraft.getInstance().getResourceManager().getResource(wand).orElseThrow(() -> new IllegalStateException("Wand Model File Not found: " + activeSeal));
		Resource sealResource = null;
		if (activeSeal != null) {
			sealResource = Minecraft.getInstance().getResourceManager().getResource(activeSeal).orElseThrow(() -> new IllegalStateException("Active Seal Model File Not found: " + activeSeal));
		}
		try {
            if (!wand.equals(MissingTextureAtlasSprite.getLocation())) {
				try (ObjTokenizer tokenizer = new ObjTokenizer(wandResource.open())) {
					ObjModel model = ObjModel.parse(tokenizer, new ObjModel.ModelSettings(wand, false, false, false, false, null));
					((AddQuadsInvoker) model).invokeAddQuads(owner, builder, bakery, spriteGetter, modelTransform, modelLocation);
				}
			}
            if (sealResource != null) {
				try (ObjTokenizer tokenizer = new ObjTokenizer(sealResource.open())) {
					ObjModel model = ObjModel.parse(tokenizer, new ObjModel.ModelSettings(activeSeal, false, false, false, true, null));
					((AddQuadsInvoker) model).invokeAddQuads(owner, builder, bakery, spriteGetter, modelTransform, modelLocation);
				}
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}

		/* Vanillad BakedItemModel but with custom MealItemOverrideList, used in store data, it'll display nothing */
		return builder.build();
	}
}