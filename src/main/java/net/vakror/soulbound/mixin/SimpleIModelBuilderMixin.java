package net.vakror.soulbound.mixin;

import net.minecraft.client.resources.model.SimpleBakedModel;
import net.minecraftforge.client.model.IModelBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(IModelBuilder.Simple.class)
public interface SimpleIModelBuilderMixin {
    @Accessor
    public SimpleBakedModel.Builder getBuilder();
}
