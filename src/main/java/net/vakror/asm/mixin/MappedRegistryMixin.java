package net.vakror.asm.mixin;

import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(MappedRegistry.class)
public class MappedRegistryMixin<T> {
    /**
     * @author vaylor27
     * @reason to fix a crash
     */
    @Overwrite()
    private void validateWrite(ResourceKey<T> pKey) {
        return;
    }
}
