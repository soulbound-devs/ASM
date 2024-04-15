package net.vakror.soulbound.model.wand.api;

import com.google.gson.JsonObject;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public interface IWandModelReader {
    default List<AbstractWandLayer> getLayers(JsonObject object, IGeometryBakingContext owner, ModelBaker bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ResourceLocation modelLocation) {
        return getLayers(object);
    }

    List<AbstractWandLayer> getLayers(JsonObject object);

    default List<AbstractQuadsProcessor> getQuadsProcessors(JsonObject object, LayerRenderData data) {
        return new ArrayList<>();
    }
}
