package net.vakror.soulbound.extension.wand;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.vakror.soulbound.cap.ModAttachments;
import net.vakror.soulbound.model.models.ActiveSealModels;
import net.vakror.soulbound.model.wand.api.LayerRenderData;

import java.util.List;

public class DefaultWandTextureRenderLayer extends TextureRenderLayer {
    public DefaultWandTextureRenderLayer(ResourceLocation... textures) {
        super(textures);
    }

    @Override
    public void getAdditionalTextures(List<TextureAtlasSprite> sprites, LayerRenderData data) {
        for (ResourceLocation texture : getTextures()) {
            sprites.add(data.spriteGetter().apply(new Material(InventoryMenu.BLOCK_ATLAS, texture)));
        }
        data.stack().getExistingData(ModAttachments.SEAL_ATTACHMENT).ifPresent(wand -> {
            if (wand.getActiveSeal() != null) {
                ResourceLocation sealModel = ActiveSealModels.getModels().get(wand.getActiveSeal().getId());
                sprites.add(data.spriteGetter().apply(new Material(InventoryMenu.BLOCK_ATLAS, sealModel)));
            }
        });
    }
}