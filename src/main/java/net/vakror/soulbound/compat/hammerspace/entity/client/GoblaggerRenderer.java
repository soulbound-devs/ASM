package net.vakror.soulbound.compat.hammerspace.entity.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.compat.hammerspace.entity.GoblaggerEntity;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GoblaggerRenderer extends GeoEntityRenderer<GoblaggerEntity> {
    public GoblaggerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GoblaggerModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull GoblaggerEntity instance) {
        return new ResourceLocation(SoulboundMod.MOD_ID, "textures/entity/goblagger.png");
    }
}