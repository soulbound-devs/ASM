/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 */

package net.vakror.soulbound.mod.model.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.BakedModelWrapper;

import javax.annotation.Nullable;
import java.util.List;

public class BakedItemModel implements BakedModel {
    protected final ImmutableList<BakedQuad> quads;
    protected final TextureAtlasSprite particle;
    protected final ImmutableMap<ItemTransforms.TransformType, ItemTransform> transforms;
    protected final ItemOverrides overrides;
    protected final BakedModel guiModel;
    protected final boolean isSideLit;

    public BakedItemModel(ImmutableList<BakedQuad> quads, TextureAtlasSprite particle, ImmutableMap<ItemTransforms.TransformType, ItemTransform> transforms, ItemOverrides overrides, boolean untransformed, boolean isSideLit) {
        this.quads = quads;
        this.particle = particle;
        this.transforms = transforms;
        this.overrides = overrides;
        this.isSideLit = isSideLit;
        this.guiModel = untransformed && hasGuiIdentity(transforms) ? new BakedGuiItemModel<>(this) : null;
    }

    private static boolean hasGuiIdentity(ImmutableMap<ItemTransforms.TransformType, ItemTransform> transforms) {
        ItemTransform guiTransform = transforms.get(ItemTransforms.TransformType.GUI);
        return guiTransform == null || guiTransform.equals(ItemTransform.NO_TRANSFORM);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return false;
    }

    @Override
    public boolean usesBlockLight() {
        return isSideLit;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return particle;
    }

    @Override
    public ItemOverrides getOverrides() {
        return overrides;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
        if (side == null) {
            return quads;
        }
        return ImmutableList.of();
    }

    @Override
    public BakedModel applyTransform(ItemTransforms.TransformType type, PoseStack poseStack, boolean applyLeftHandTransform) {
        if (type == ItemTransforms.TransformType.GUI && this.guiModel != null) {
            return this.guiModel.applyTransform(type, poseStack, applyLeftHandTransform);
        }
        return handlePerspective(this, transforms, type, poseStack, applyLeftHandTransform);
    }

    @SuppressWarnings("all")
    public static BakedModel handlePerspective(BakedModel model, ImmutableMap<ItemTransforms.TransformType, ItemTransform> transforms, ItemTransforms.TransformType cameraTransformType, PoseStack mat, boolean leftHand) {
        ItemTransform tr = transforms.getOrDefault(cameraTransformType, ItemTransform.NO_TRANSFORM);
        if (!tr.equals(tr.NO_TRANSFORM)) {
            tr.apply(leftHand, mat);
        }
        return model;
    }

    public static class BakedGuiItemModel<T extends BakedItemModel> extends BakedModelWrapper<T> {
        private final ImmutableList<BakedQuad> quads;

        public BakedGuiItemModel(T originalModel) {
            super(originalModel);
            ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();
            if (originalModel.quads != null) {
                for (BakedQuad quad : originalModel.quads) {
                    if (quad.getDirection() == Direction.SOUTH) {
                        builder.add(quad);
                    }
                }
            }
            this.quads = builder.build();
        }

        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
            if (side == null) {
                return quads;
            }
            return ImmutableList.of();
        }

        @Override
        public BakedModel applyTransform(ItemTransforms.TransformType type, PoseStack poseStack, boolean doLeftHandTransformation) {
            if (type == ItemTransforms.TransformType.GUI) {
                return handlePerspective(this, originalModel.transforms, type, poseStack, doLeftHandTransformation);
            }
            return handlePerspective(this.originalModel, this.originalModel.transforms, type, poseStack, doLeftHandTransformation);
        }
    }
}
