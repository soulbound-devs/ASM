package net.vakror.soulbound.mixin;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;

@Mixin(SimpleBakedModel.Builder.class)
public interface SimpleModelBuilderMixin {
    @Accessor
    public ItemOverrides getOverrides();
    @Accessor
    public boolean getHasAmbientOcclusion();
    @Accessor
    public TextureAtlasSprite getParticleIcon();
    @Accessor
    public boolean getUsesBlockLight();
    @Accessor
    public boolean isIsGui3d();
    @Accessor
    public ItemTransforms getTransforms();
    @Accessor
    public List<BakedQuad> getUnculledFaces();

    @Accessor
    @Mutable
    public void setUnculledFaces(List<BakedQuad> quads);

    @Accessor
    @Mutable
    public void setCulledFaces(Map<Direction, BakedQuad> quads);
}
