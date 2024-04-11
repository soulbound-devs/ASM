package net.vakror.soulbound.model;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import org.joml.Transformation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.SimpleBakedModel;
import net.neoforged.neoforge.client.RenderTypeGroup;
import net.vakror.soulbound.mixin.SimpleBakedModelMixin;

import java.util.ArrayList;
import java.util.List;

public class WandBakedModel extends SimpleBakedModel {
    public List<BakedQuad> WAND = new ArrayList<>();
    public List<BakedQuad> SPELL = new ArrayList<>();
    public List<BakedQuad> OUTLINE = new ArrayList<>();

    public Transformation root;

    public WandBakedModel(SimpleBakedModel model, RenderTypeGroup renderTypes, Transformation root) {
        super(((SimpleBakedModelMixin) model).getUnculledFaces(), ((SimpleBakedModelMixin) model).getCulledFaces(), model.useAmbientOcclusion(), model.isGui3d(), model.usesBlockLight(), model.getParticleIcon(), model.getTransforms(), model.getOverrides(), renderTypes);
    }

    public void addWand(List<BakedQuad> wand) {
        this.WAND.addAll(wand);
    }

//


    @Override
    public boolean isCustomRenderer() {
        return true;
    }

    private static int findOffset(VertexFormatElement element)
    {
        // Divide by 4 because we want the int offset
        var index = DefaultVertexFormat.NEW_ENTITY.getElements().indexOf(element);
        return index < 0 ? -1 : DefaultVertexFormat.NEW_ENTITY.getOffset(index) / 4;
    }

    public void addSpell(List<BakedQuad> spell) {
        this.SPELL.addAll(spell);
    }

    public void addOutline(List<BakedQuad> outline) {
        this.OUTLINE.addAll(outline);
    }
}
