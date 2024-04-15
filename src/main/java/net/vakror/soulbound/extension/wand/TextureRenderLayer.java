package net.vakror.soulbound.extension.wand;

import com.google.common.collect.ImmutableList;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.vakror.soulbound.model.ModelUtils;
import net.vakror.soulbound.model.wand.api.AbstractWandLayer;
import net.vakror.soulbound.model.wand.api.LayerRenderData;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TextureRenderLayer extends AbstractWandLayer {
    private ResourceLocation[] textures;

    public TextureRenderLayer(ResourceLocation... textures) {
        this.textures = textures;
    }

    @Override
    public final void render(ImmutableList.Builder<BakedQuad> quads, Transformation transformation, LayerRenderData data) {
        List<TextureAtlasSprite> sprites = new ArrayList<>();
        getAdditionalTextures(sprites, data);
        ModelUtils.genQuads(sprites, quads, transformation);
    }

    public void getAdditionalTextures(List<TextureAtlasSprite> sprites, LayerRenderData data) {
        for (ResourceLocation texture : textures) {
            sprites.add(data.spriteGetter().apply(new Material(InventoryMenu.BLOCK_ATLAS, texture)));
        }
    }

    @Override
    public @NotNull String getCacheKey() {
        StringBuilder builder = new StringBuilder();
        for (ResourceLocation texture : textures) {
            builder.append(texture.toString());
        }
        List<TextureAtlasSprite> sprites = new ArrayList<>();
        getAdditionalTextures(sprites, data);
        for (TextureAtlasSprite sprite : sprites) {
            builder.append(sprite.contents().name());
        }
        return builder.toString();
    }

    public ResourceLocation[] getTextures() {
        return textures;
    }
}
