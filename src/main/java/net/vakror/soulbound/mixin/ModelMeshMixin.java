package net.vakror.soulbound.mixin;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraftforge.client.model.IModelBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraftforge.client.model.obj.ObjModel.ModelMesh")
public class ModelMeshMixin<T extends IModelBuilder<T>> {

    @Redirect(method = "addQuads", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/model/IModelBuilder;addCulledFace(Lnet/minecraft/core/Direction;Lnet/minecraft/client/renderer/block/model/BakedQuad;)Lnet/minecraftforge/client/model/IModelBuilder;"), remap = false)
    private T fixThings(IModelBuilder<?> instance, Direction direction, BakedQuad bakedQuad) {
        System.out.println("hi!");
        return (T) instance.addUnculledFace(bakedQuad);
    }
}
