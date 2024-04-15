package net.vakror.soulbound.extension.wand.layer;

import com.google.common.collect.ImmutableList;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.vakror.soulbound.model.ModelUtils;
import net.vakror.soulbound.model.wand.api.AbstractWandLayer;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TextureRenderLayer extends AbstractWandLayer {
    public ResourceLocation textureLocation;

    public TextureRenderLayer(ResourceLocation textureLocation) {
        this.textureLocation = textureLocation;
    }

    @Override
    public void render(ImmutableList.Builder<BakedQuad> quads, Transformation transformation) {
        ModelUtils.genQuads(List.of(data.spriteGetter().apply(new Material(InventoryMenu.BLOCK_ATLAS, textureLocation))), quads, transformation);
    }

    @Override
    public @NotNull String getCacheKey() {
        return "";
    }
}
