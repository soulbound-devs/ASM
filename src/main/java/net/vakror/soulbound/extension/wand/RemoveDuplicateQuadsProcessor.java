package net.vakror.soulbound.extension.wand;

import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.vakror.soulbound.model.wand.api.AbstractQuadsProcessor;
import net.vakror.soulbound.model.wand.api.AbstractWandLayer;
import net.vakror.soulbound.model.wand.api.LayerRenderData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RemoveDuplicateQuadsProcessor extends AbstractQuadsProcessor {
    @Override
    public void processQuads(List<BakedQuad> bakedQuads, List<AbstractWandLayer> layers, LayerRenderData data) {
        List<BakedQuad> copy = new ArrayList<>(bakedQuads);
        bakedQuads.clear();

        Multimap<int[], Direction> positions = Multimaps.newListMultimap(new HashMap<>(), ArrayList::new);

        for (BakedQuad quad : copy) {
            if (!positions.containsEntry(quad.getVertices(), quad.getDirection())) {
                bakedQuads.add(quad);
                positions.put(quad.getVertices(), quad.getDirection());
            }
        }
    }
}
