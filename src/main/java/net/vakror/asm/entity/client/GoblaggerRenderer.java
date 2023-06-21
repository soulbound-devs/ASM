package net.vakror.asm.entity.client;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.vakror.asm.ASMMod;
import net.vakror.asm.entity.GoblaggerEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GoblaggerRenderer extends GeoEntityRenderer<GoblaggerEntity> {
    public GoblaggerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GoblaggerModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull GoblaggerEntity instance) {
        return new ResourceLocation(ASMMod.MOD_ID, "textures/entity/goblagger.png");
    }

    @Override
    public RenderType getRenderType(GoblaggerEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}