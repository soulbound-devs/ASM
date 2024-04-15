package net.vakror.soulbound.model.wand.api;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public record LayerRenderData(BakedModel model, ItemStack stack, ClientLevel worldIn, LivingEntity entityIn, int seed, Function<Material, TextureAtlasSprite> spriteGetter) {
}
