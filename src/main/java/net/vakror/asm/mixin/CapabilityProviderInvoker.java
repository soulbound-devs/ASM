package net.vakror.asm.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.CapabilityProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(CapabilityProvider.class)
public interface CapabilityProviderInvoker {
    @Invoker("serializeCaps")
    CompoundTag invokeSerializeCaps();
}