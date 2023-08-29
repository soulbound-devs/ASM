package net.vakror.soulbound.mod.mixin;

import net.minecraft.core.IdMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.chunk.LinearPalette;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LinearPalette.class)
public class LinearPaletteMixin<T> {

    @Shadow @Final private IdMap<T> registry;

    @Redirect(method = "read" ,at = @At(value = "INVOKE", target = "Lnet/minecraft/network/FriendlyByteBuf;readVarInt()I"))
    public int readVarInt(FriendlyByteBuf instance) {
        int i = instance.readVarInt();
        if (i == -1) {
            return 0;
        }
        return i;
    }
}
