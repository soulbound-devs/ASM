package net.vakror.soulbound.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.math.Transformation;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeRenderTypes;
import net.minecraftforge.client.model.*;
import net.minecraftforge.client.model.geometry.IModelGeometry;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.wand.ItemWandProvider;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import static net.minecraftforge.client.model.ItemLayerModel.getQuadsForSprite;

public class WandModel implements IModelGeometry<WandModel> {

    public String activeTexture;

    public static final WandModel INSTANCE = new WandModel(ImmutableList.of());

    private ImmutableList<Material> textures;

    public WandModel(@Nullable ImmutableList<Material> textures) {
        this.textures = textures;
    }

    @Override
    public BakedModel bake(IModelConfiguration owner, ModelBakery bakery,
                           Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform,
                           ItemOverrides overrides, ResourceLocation modelLocation) {
        ImmutableMap<ItemTransforms.TransformType, Transformation> transformMap =
                PerspectiveMapWrapper.getTransforms(new CompositeModelState(owner.getCombinedTransform(), modelTransform));
        Transformation transform = modelTransform.getRotation();
        TextureAtlasSprite particle = spriteGetter.apply(
                owner.isTexturePresent("particle") ? owner.resolveTexture("particle") : textures.get(0)
        );

        ItemMultiLayerBakedModel.Builder wand = ItemMultiLayerBakedModel.builder(owner, particle, overrides, transformMap);
        for(int i = 0; i < textures.size(); i++) {
            TextureAtlasSprite tas = spriteGetter.apply(textures.get(i));
            RenderType rt = ForgeRenderTypes.ITEM_UNSORTED_UNLIT_TRANSLUCENT.get();
            wand.addQuads(rt, getQuadsForSprite(i, tas, transform, true));
        }

        return wand.build();
    }


    public ImmutableList<Material> getAllTextures(IModelConfiguration model) {
        ImmutableList.Builder<Material> builder = ImmutableList.builder();

        builder.add(model.resolveTexture("layer0"));
        builder.add(model.resolveTexture("pickaxing"));
        builder.add(model.resolveTexture("shoveling"));
        builder.add(model.resolveTexture("swording_seal"));
        builder.add(model.resolveTexture("axing"));
        builder.add(model.resolveTexture("scythe"));

        return builder.build();
    }

    public ImmutableList<Material> getTextures(IModelConfiguration model) {
        ImmutableList.Builder<Material> builder = ImmutableList.builder();

        builder.add(model.resolveTexture("wand"));
        if (activeTexture != null) {
            builder.add(model.resolveTexture(activeTexture));
        }
        return builder.build();
    }

    @Override
    public Collection<Material> getTextures(IModelConfiguration owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
        textures = getAllTextures(owner);
        return getTextures(owner);
    }

    public enum WandModelLoader implements IModelLoader<WandModel> {
        INSTANCE;

        @Override
        public WandModel read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
            return new WandModel(null);
        }

        @Override
        public void onResourceManagerReload(ResourceManager pResourceManager) {
            // Nothing to do
        }
    }

    private static final class WandOverrides extends ItemOverrides
    {
        private final Map<String, BakedModel> cache = Maps.newHashMap(); // contains all the baked models since they'll never change
        private final ItemOverrides nested;
        private final ModelBakery bakery;
        private final IModelConfiguration owner;
        private final WandModel parent;

        private WandOverrides(ItemOverrides nested, ModelBakery bakery, IModelConfiguration owner, WandModel parent)
        {
            this.nested = nested;
            this.bakery = bakery;
            this.owner = owner;
            this.parent = parent;
        }

        @Override
        public BakedModel resolve(BakedModel originalModel, ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int seed) {
            stack.getCapability(ItemWandProvider.WAND).ifPresent(wand -> {
                if (wand.getActiveSeal() != null) {
                    parent.activeTexture = wand.getActiveSeal().getId();
                }
            });
            return parent.bake(owner, bakery, ForgeModelBakery.defaultTextureGetter(), BlockModelRotation.X0_Y0, this, new ResourceLocation(SoulboundMod.MOD_ID, "wand_overrides"));
        }
    }
}
