package net.vakror.soulbound.mod.compat.hammerspace.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.vakror.soulbound.mod.SoulboundMod;
import net.vakror.soulbound.mod.compat.hammerspace.entity.GoblaggerEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GoblaggerModel extends AnimatedGeoModel<GoblaggerEntity> {
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