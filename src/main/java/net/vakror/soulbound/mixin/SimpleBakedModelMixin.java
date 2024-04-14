package net.vakror.soulbound.mixin;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraft.core.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.Map;

@Mixin(SimpleBakedModel.class)
public interface SimpleBakedModelMixin {
    @Accessor
    List<BakedQuad> getUnculledFaces();
    @Accessor
    Map<Direction, List<BakedQuad>> getCulledFaces();
}
