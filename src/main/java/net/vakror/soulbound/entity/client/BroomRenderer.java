package net.vakror.soulbound.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.cap.ModAttachments;
import net.vakror.soulbound.entity.BroomEntity;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicReference;

public class BroomRenderer<T extends BroomEntity> extends EntityRenderer<T> {
    public final EntityRendererProvider.Context context;
    public static final ResourceLocation TEXTURE = new ResourceLocation(SoulboundMod.MOD_ID, "textures/entity/broom_basic.png");

    public BroomRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 1;
        this.context = context;
    }

    @Override
    public void render(@NotNull T entity, float p_114486_, float partialTicks, PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int packedLight) {
        poseStack.pushPose();

        BroomModel<BroomEntity> model = new BroomModel<>(context.bakeLayer(BroomModel.LAYER_LOCATION));
        model.setupAnim(entity, partialTicks, 0, entity.tickCount + partialTicks, 0, 0);
        VertexConsumer vertexConsumer = this.buffer(entity, model, TEXTURE, multiBufferSource);
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
        super.render(entity, p_114486_, partialTicks, poseStack, multiBufferSource, packedLight);
    }

    private VertexConsumer buffer(T entity, EntityModel<BroomEntity> model, ResourceLocation texture, MultiBufferSource multiBufferSource) {
        AtomicReference<VertexConsumer> buffer = new AtomicReference<>();
        ItemStack broomItem = entity.getItem();
        broomItem.getExistingData(ModAttachments.SEAL_ATTACHMENT).ifPresent((wand) -> {
            if (!wand.getAmplifyingSeals().isEmpty()) {
               buffer.set(VertexMultiConsumer.create(multiBufferSource.getBuffer(RenderType.entityGlint()), multiBufferSource.getBuffer(model.renderType(texture))));
            } else {
                buffer.set(multiBufferSource.getBuffer(model.renderType(texture)));
            }
        });
        return buffer.get();
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull T p_114482_) {
        return TEXTURE;
    }
}