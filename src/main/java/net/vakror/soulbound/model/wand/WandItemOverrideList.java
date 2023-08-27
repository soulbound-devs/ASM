package net.vakror.soulbound.model.wand;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
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
import net.vakror.soulbound.mixin.SimpleBakedModelMixin;
import net.vakror.soulbound.model.WandBakedModel;
import net.vakror.soulbound.model.models.ActiveSealModels;
import net.vakror.soulbound.model.models.WandModels;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import static net.minecraft.client.renderer.texture.TextureAtlas.LOCATION_BLOCKS;

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
				owner.getTransforms(), this, particle, new RenderTypeGroup(RenderType.solid(), RenderType.itemEntityTranslucentCull(LOCATION_BLOCKS)));
		Resource wandResource = Minecraft.getInstance().getResourceManager().getResource(wand).orElseThrow(() -> new IllegalStateException("Wand Model File Not found: " + activeSeal));
		ResourceLocation outlineLocation;
		Resource sealResource = null;
		Resource sealOutlineResource = null;
		List<BakedQuad> wands = null;
		List<BakedQuad> spell = null;
		List<BakedQuad> outline = null;
		if (activeSeal != null) {
			outlineLocation = new ResourceLocation(activeSeal.getNamespace(), activeSeal.getPath().replace(".obj", "") + "_outline.obj");
			sealResource = Minecraft.getInstance().getResourceManager().getResource(activeSeal).orElseThrow(() -> new IllegalStateException("Active Seal Model File Not found: " + activeSeal));
			sealOutlineResource = Minecraft.getInstance().getResourceManager().getResource(outlineLocation).orElseThrow(() -> new IllegalStateException("Active Seal Outline Model File Not found: " + outlineLocation));
		} else {
            outlineLocation = null;
        }
        try {
            if (!wand.equals(MissingTextureAtlasSprite.getLocation())) {
				try (ObjTokenizer tokenizer = new ObjTokenizer(wandResource.open())) {
					ObjModel model = ObjModel.parse(tokenizer, new ObjModel.ModelSettings(wand, true, false, false, false, null));
					IModelBuilder.Simple builder1 = (IModelBuilder.Simple) IModelBuilder.of(owner.useAmbientOcclusion(), owner.useBlockLight(), owner.isGui3d(),
							owner.getTransforms(), this, particle, new RenderTypeGroup(RenderType.solid(), RenderType.itemEntityTranslucentCull(LOCATION_BLOCKS)));
					((AddQuadsInvoker) model).invokeAddQuads(owner, builder1, bakery, spriteGetter, modelTransform, modelLocation);
					wands = ((SimpleBakedModelMixin) builder1.build()).getUnculledFaces();
				}
			}
			if (sealOutlineResource != null) {
				try (ObjTokenizer tokenizer = new ObjTokenizer(sealOutlineResource.open())) {
					ObjModel model = ObjModel.parse(tokenizer, new ObjModel.ModelSettings(outlineLocation, true, false, false, true, null));
					IModelBuilder.Simple builder1 = (IModelBuilder.Simple) IModelBuilder.of(owner.useAmbientOcclusion(), owner.useBlockLight(), owner.isGui3d(),
							owner.getTransforms(), this, particle, new RenderTypeGroup(RenderType.solid(), RenderType.itemEntityTranslucentCull(LOCATION_BLOCKS)));
					((AddQuadsInvoker) model).invokeAddQuads(owner, builder1, bakery, spriteGetter, modelTransform, modelLocation);
					outline = ((SimpleBakedModelMixin) builder1.build()).getUnculledFaces();
				}
			}
            if (sealResource != null) {
				try (ObjTokenizer tokenizer = new ObjTokenizer(sealResource.open())) {
					ObjModel model = ObjModel.parse(tokenizer, new ObjModel.ModelSettings(activeSeal, true, false, false, true, null));
					IModelBuilder.Simple builder1 = (IModelBuilder.Simple) IModelBuilder.of(owner.useAmbientOcclusion(), owner.useBlockLight(), owner.isGui3d(),
							owner.getTransforms(), this, particle, new RenderTypeGroup(RenderType.solid(), RenderType.itemEntityTranslucentCull(LOCATION_BLOCKS)));
					((AddQuadsInvoker) model).invokeAddQuads(owner, builder1, bakery, spriteGetter, modelTransform, modelLocation);
					spell = ((SimpleBakedModelMixin) builder1.build()).getUnculledFaces();
				}
			}
		} catch (IOException e) {
			throw new IllegalStateException(e);
		}
		((SimpleBakedModelMixin) builder.build()).getUnculledFaces().sort((Comparator.comparingInt(BakedQuad::getTintIndex)));
		WandBakedModel model = new WandBakedModel((SimpleBakedModel) builder.build(), new RenderTypeGroup(RenderType.solid(), RenderType.itemEntityTranslucentCull(LOCATION_BLOCKS)), owner.getRootTransform());
		model.addWand(wands);
		if (spell != null) {
			model.addSpell(spell);
		} if (outline != null) {
			model.addOutline(outline);
		}

		/* Vanillad BakedItemModel but with custom MealItemOverrideList, used in store data, it'll display nothing */
		return model;
	}
}