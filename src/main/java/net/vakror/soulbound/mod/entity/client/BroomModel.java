package net.vakror.soulbound.mod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.vakror.soulbound.mod.SoulboundMod;
import net.vakror.soulbound.mod.entity.BroomEntity;

public class BroomModel <T extends BroomEntity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(SoulboundMod.MOD_ID, "broom"), "master");
    private final ModelPart master;
    public BroomModel(ModelPart part) {
        this.master = part.getChild("master");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();

        PartDefinition main = partDefinition.addOrReplaceChild("master", CubeListBuilder.create().texOffs(0, 0).addBox(-1.0F, -7.0F, -3.0F, 2.0F, 2.0F, 11.0F, new CubeDeformation(0.0F))
                .texOffs(0, 13).addBox(-3.0F, -7.5F, -8.0F, 7.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshDefinition, 32, 32);
    }
    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        float tickAge = ageInTicks * 0.1F;
        if (entity.getPassengers().isEmpty()) {
            this.master.y = Mth.sin(tickAge) * 2;
        } else {
            this.master.y = 3.0F;
        }
            this.master.xRot = Mth.cos(tickAge * 2) * 0.1F;
            this.master.zRot = Mth.cos(tickAge * 2) * 0.1F;
    }

    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay) {
        renderToBuffer(poseStack, vertexConsumer, packedLight, packedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        master.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
