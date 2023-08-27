package net.vakror.soulbound.model;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;
import net.vakror.soulbound.items.custom.WandItem;

import static org.lwjgl.opengl.GL11.*;

public class WandBewlr extends BlockEntityWithoutLevelRenderer {
    public static WandBewlr INSTANCE = null;
    public static boolean PREVENT_CLEAR = false;

    public WandBewlr(BlockEntityRenderDispatcher pBlockEntityRenderDispatcher, EntityModelSet pEntityModelSet) {
        super(pBlockEntityRenderDispatcher, pEntityModelSet);
    }

    @Override
    public void renderByItem(ItemStack pStack, ItemTransforms.TransformType pTransformType, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        if (pStack.getItem() instanceof WandItem && Minecraft.getInstance().getItemRenderer().getModel(pStack, null, null, 0) instanceof WandBakedModel wandBakedModel) {
            BufferBuilder builder = (BufferBuilder) pBuffer.getBuffer(RenderType.itemEntityTranslucentCull(InventoryMenu.BLOCK_ATLAS));
            pPoseStack.pushPose();
            PREVENT_CLEAR = true;
            BufferBuilder builder1 = (BufferBuilder) pBuffer.getBuffer(RenderType.itemEntityTranslucentCull(InventoryMenu.BLOCK_ATLAS));
            wandBakedModel.OUTLINE.forEach((outline -> {
                builder1.putBulkData(pPoseStack.last(), outline, 1, 1, 1, LightTexture.pack(15, 15), OverlayTexture.NO_OVERLAY);
            }));

            builder1.setQuadSortOrigin((float)0, (float)0, (float)0);
            render(builder1.end(), true);

            wandBakedModel.WAND.forEach((wand -> {
                builder.putBulkData(pPoseStack.last(), wand, 1, 1, 1, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
            }));
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.NEW_ENTITY);
            builder.setQuadSortOrigin((float)0, (float)0, (float)0);
            render(builder.end(), true);

            BufferBuilder builder2 = (BufferBuilder) pBuffer.getBuffer(RenderType.itemEntityTranslucentCull(InventoryMenu.BLOCK_ATLAS));
            wandBakedModel.SPELL.forEach((spell -> {
                builder2.putBulkData(pPoseStack.last(), spell, 1, 1, 1, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
            }));

            builder2.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.NEW_ENTITY);
            builder2.setQuadSortOrigin((float)0, (float)0, (float)0);
            render(builder2.end(), true);

            pPoseStack.popPose();


        }
        PREVENT_CLEAR = false;
        super.renderByItem(pStack, pTransformType, pPoseStack, pBuffer, pPackedLight, pPackedOverlay);
    }

    private void render(BufferBuilder.RenderedBuffer buffer, boolean depthTest) {
        RenderType.entityTranslucent(InventoryMenu.BLOCK_ATLAS).setupRenderState();

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        RenderSystem.enableCull();
        if (depthTest) {
            RenderSystem.disableDepthTest();
        }
        BufferUploader.drawWithShader(buffer);
        if (depthTest) {
            RenderSystem.enableDepthTest();
        }
        RenderType.entityTranslucent(InventoryMenu.BLOCK_ATLAS).clearRenderState();
    }
}
