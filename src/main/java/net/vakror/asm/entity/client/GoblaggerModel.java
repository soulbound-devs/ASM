package net.vakror.asm.entity.client;

import net.minecraft.resources.ResourceLocation;
import net.vakror.asm.ASMMod;
import net.vakror.asm.entity.GoblaggerEntity;
import software.bernie.geckolib.model.GeoModel;

public class GoblaggerModel extends GeoModel<GoblaggerEntity> {
    @Override
    public ResourceLocation getModelResource(GoblaggerEntity object) {
        return new ResourceLocation(ASMMod.MOD_ID, "geo/goblagger.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GoblaggerEntity object) {
        return new ResourceLocation(ASMMod.MOD_ID, "textures/entity/goblagger.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GoblaggerEntity animatable) {
        return new ResourceLocation(ASMMod.MOD_ID, "animations/goblagger.animation.json");
    }
}