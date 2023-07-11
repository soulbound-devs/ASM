package net.vakror.soulbound.compat.hammerspace.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.compat.hammerspace.entity.GoblaggerEntity;
import software.bernie.geckolib.model.GeoModel;

public class GoblaggerModel extends GeoModel<GoblaggerEntity> {
    @Override
    public ResourceLocation getModelResource(GoblaggerEntity object) {
        return new ResourceLocation(SoulboundMod.MOD_ID, "geo/goblagger.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GoblaggerEntity object) {
        return new ResourceLocation(SoulboundMod.MOD_ID, "textures/entity/goblagger.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GoblaggerEntity animatable) {
        return new ResourceLocation(SoulboundMod.MOD_ID, "animations/goblagger.animation.json");
    }
}