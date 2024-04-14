package net.vakror.soulbound.model;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.pipeline.QuadBakingVertexConsumer;
import net.neoforged.neoforge.client.model.pipeline.TransformingVertexPipeline;
import net.vakror.soulbound.model.wand.WandBakedModel;
import net.vakror.soulbound.model.wand.WandModel;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Function;

public class ModelUtils {

    public static float NORTH_Z = 7.496f / 16f;
    public static float SOUTH_Z = 8.504f / 16f;

    public static float COLOR_R = 1.0f;
    public static float COLOR_G = 1.0f;
    public static float COLOR_B = 1.0f;

    @Nullable
    public static TextureAtlasSprite getSprite(Function<Material, TextureAtlasSprite> spriteGetter, @NotNull ResourceLocation location) {
        @SuppressWarnings("deprecation")
        Material material = new Material(TextureAtlas.LOCATION_BLOCKS, location);
        TextureAtlasSprite sprite = spriteGetter.apply(material);
        if (sprite != null && !sprite.atlasLocation().equals(MissingTextureAtlasSprite.getLocation())) {
            return sprite;
        }
        return null;
    }

    public static void genQuads(List<TextureAtlasSprite> sprites, ImmutableList.Builder<BakedQuad> quads, Transformation transform) {
        genFrontBackQuads(sprites, quads, transform);
        genUpDownQuads(sprites, quads, transform);
        genLeftRightQuads(sprites, quads, transform);
    }

    private static void genFrontBackQuads(List<TextureAtlasSprite> sprites, ImmutableList.Builder<BakedQuad> quads, Transformation transform) {
        /* North & South Side */
        for (int ix = 0; ix <= 15; ix++) {
            for (int iy = 0; iy <= 15; iy++) {
                /* Find the last pixel not transparent in sprites, use that to build North/South quads */
                TextureAtlasSprite sprite = findLastNotTransparent(ix, iy, sprites);
                if (sprite == null) continue;

                float xStart = ix / 16.0f;
                float xEnd = (ix + 1) / 16.0f;

                float yStart = (16 - (iy + 1)) / 16.0f;
                float yEnd = (16 - iy) / 16.0f;

                BakedQuad a = createQuad(
                        new Vec3(xStart, yStart, NORTH_Z)
                        , new Vec3(xStart, yEnd, NORTH_Z)
                        , new Vec3(xEnd, yEnd, NORTH_Z)
                        , new Vec3(xEnd, yStart, NORTH_Z)
                        , ix, ix + 1, iy, iy + 1
                        , sprite, Direction.NORTH, transform);

                BakedQuad b = createQuad(
                        new Vec3(xStart, yStart, SOUTH_Z)
                        , new Vec3(xEnd, yStart, SOUTH_Z)
                        , new Vec3(xEnd, yEnd, SOUTH_Z)
                        , new Vec3(xStart, yEnd, SOUTH_Z)
                        , ix, ix + 1, iy, iy + 1
                        , sprite, Direction.SOUTH, transform);

                if (a != null) {
                    quads.add(a);
                }
                if (b != null) {
                    quads.add(b);
                }
            }
        }
    }

    private static void genUpDownQuads(List<TextureAtlasSprite> sprites, ImmutableList.Builder<BakedQuad> quads, Transformation transform) {
        for (int ix = 0; ix <= 15; ix++) {
            float xStart = ix / 16.0f;
            float xEnd = (ix + 1) / 16.0f;

            /* Scan from Up to Bottom, find the pixel not transparent, use that to build Top quads */
            for (int iy = 0; iy <= 15; iy++) {
                TextureAtlasSprite sprite = findLastNotTransparent(ix, iy, sprites);
                if (sprite == null) {
                    continue;
                }

                    quads.add(createQuad(
                            new Vec3(xStart, (16 - iy) / 16.0f, NORTH_Z)
                            , new Vec3(xStart, (16 - iy) / 16.0f, SOUTH_Z)
                            , new Vec3(xEnd, (16 - iy) / 16.0f, SOUTH_Z)
                            , new Vec3(xEnd, (16 - iy) / 16.0f, NORTH_Z)
                            , ix, ix + 1, iy, iy + 1
                            , sprite, Direction.UP, transform));

            }

            /* Scan from Bottom to Up, find the pixel not transparent, use that to build Down quads */
            for (int iy = 15; iy >= 0; iy--) {
                TextureAtlasSprite sprite = findLastNotTransparent(ix, iy, sprites);
                if (sprite == null) {
                    continue;
                }

                    quads.add(createQuad(
                            new Vec3(xStart, (16 - (iy + 1)) / 16.0f, NORTH_Z)
                            , new Vec3(xEnd, (16 - (iy + 1)) / 16.0f, NORTH_Z)
                            , new Vec3(xEnd, (16 - (iy + 1)) / 16.0f, SOUTH_Z)
                            , new Vec3(xStart, (16 - (iy + 1)) / 16.0f, SOUTH_Z)
                            , ix, ix + 1, iy, iy + 1
                            , sprite, Direction.DOWN, transform));

            }
        }
    }

    private static void genLeftRightQuads(List<TextureAtlasSprite> sprites, ImmutableList.Builder<BakedQuad> quads, Transformation transform) {
        for (int iy = 0; iy <= 15; iy++) {
            float yStart = (16 - (iy + 1)) / 16.0f;
            float yEnd = (16 - iy) / 16.0f;

            /* Scan from Left to Right, find the pixel not transparent, use that to build West quads */
            for (int ix = 0; ix <= 15; ix++) {
                TextureAtlasSprite sprite = findLastNotTransparent(ix, iy, sprites);
                if (sprite == null) {
                    continue;
                }
                quads.add(createQuad(
                        new Vec3(ix / 16.0f, yStart, NORTH_Z)
                        , new Vec3(ix / 16.0f, yStart, SOUTH_Z)
                        , new Vec3(ix / 16.0f, yEnd, SOUTH_Z)
                        , new Vec3(ix / 16.0f, yEnd, NORTH_Z)
                        , ix, ix + 1, iy, iy + 1
                        , sprite, Direction.WEST, transform));

            }
            /* Scan from Right to Left, find the pixel not transparent, use that to build East quads */
            for (int ix = 15; ix >= 0; ix--) {
                TextureAtlasSprite sprite = findLastNotTransparent(ix, iy, sprites);
                if (sprite == null) {
                    continue;
                }
                quads.add(createQuad(
                        new Vec3((ix + 1) / 16.0f, yStart, NORTH_Z)
                        , new Vec3((ix + 1) / 16.0f, yEnd, NORTH_Z)
                        , new Vec3((ix + 1) / 16.0f, yEnd, SOUTH_Z)
                        , new Vec3((ix + 1) / 16.0f, yStart, SOUTH_Z)
                        , ix, ix + 1, iy, iy + 1
                        , sprite, Direction.EAST, transform));

            }
        }
    }


    /* Give four corner, generate a quad */
    private static BakedQuad createQuad(Vec3 v1, Vec3 v2, Vec3 v3, Vec3 v4
            , int xStart, int xEnd, int yStart, int yEnd, TextureAtlasSprite sprite
            , Direction orientation, Transformation transform) {

        BakedQuad[] quad = new BakedQuad[1];
        QuadBakingVertexConsumer builder = new QuadBakingVertexConsumer(q -> quad[0] = q);
        VertexConsumer consumer = new TransformingVertexPipeline(builder, transform);

        builder.setSprite(sprite);

        putVertex(consumer, v1, xStart, yEnd, sprite, orientation);
        putVertex(consumer, v2, xStart, yStart, sprite, orientation);
        putVertex(consumer, v3, xEnd, yStart, sprite, orientation);
        putVertex(consumer, v4, xEnd, yEnd, sprite, orientation);

        return quad[0];
    }

    /* Put data into the consumer */
    private static void putVertex(VertexConsumer consumer, Vec3 vec, double u, double v, TextureAtlasSprite sprite, Direction orientation) {
        float fu = sprite.getU0() + (sprite.getU1() - sprite.getU0()) * (float)u / 16.0F;
        float fv = sprite.getV0() + (sprite.getV1() - sprite.getV0()) * (float)v / 16.0F;

        consumer.vertex((float) vec.x, (float) vec.y, (float) vec.z)
                .color(COLOR_R, COLOR_G, COLOR_B, 1)
                .normal((float) orientation.getStepX(), (float) orientation.getStepY(), (float) orientation.getStepZ())
                .uv(fu, fv)
                .uv2(0, 0)
                .endVertex();
    }

    @Nullable
    public static TextureAtlasSprite findLastNotTransparent(int x, int y, List<TextureAtlasSprite> sprites) {
        for (TextureAtlasSprite sprite : sprites) {
            if (sprite != null) {
                if (!sprite.contents().isTransparent(0, x, y)) {
                    return sprite;
                }
            }
        }
        return null;
    }
}