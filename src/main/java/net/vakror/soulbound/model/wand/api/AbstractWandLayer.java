package net.vakror.soulbound.model.wand.api;

import com.google.common.collect.ImmutableList;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.resources.ResourceLocation;
import net.vakror.soulbound.model.wand.WandBakedModel;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractWandLayer {
    public final ResourceLocation id;
    public LayerData data;
    public WandBakedModel model;

    public AbstractWandLayer(ResourceLocation id) {
        this.id = id;
    }

    public abstract void render(ImmutableList.Builder<BakedQuad> quads, Transformation transformation);

    @NotNull
    public abstract String getCacheKey();

    public void setData(LayerData data) {
        this.data = data;
    }

    public void setModel(WandBakedModel model) {
        this.model = model;
    }
}
