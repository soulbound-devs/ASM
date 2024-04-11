package net.vakror.soulbound.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.IModelBuilder;
import net.neoforged.neoforge.client.model.geometry.IGeometryBakingContext;
import net.neoforged.neoforge.client.model.obj.ObjModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

//todo: fix
@Mixin(value = ObjModel.class, remap = false)
public class ObjModelMixin {
    @Inject(method = "addQuads", at = @At("HEAD"))
    public void d(IGeometryBakingContext owner, IModelBuilder<?> modelBuilder, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ResourceLocation modelLocation, CallbackInfo ci) {
        String[] path = modelLocation.getPath().split("/");
        String file = path[path.length - 1];
        if (file.equals("base.obj")) {
            RenderSystem.disableDepthTest();
        }
    }

    @Inject(method = "addQuads", at = @At("TAIL"))
    public void f(IGeometryBakingContext owner, IModelBuilder<?> modelBuilder, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ResourceLocation modelLocation, CallbackInfo ci) {
        String[] path = modelLocation.getPath().split("/");
        String file = path[path.length - 1];
        if (file.equals("base.obj")) {
            RenderSystem.disableDepthTest();
        }
    }
}
