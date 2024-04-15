package net.vakror.soulbound.model.wand.api;

import net.minecraft.client.renderer.block.model.BakedQuad;

import java.util.List;

public abstract class AbstractQuadsProcessor {
    public abstract void processQuads(List<BakedQuad> bakedQuads, List<AbstractWandLayer> layers, LayerRenderData data);
}
