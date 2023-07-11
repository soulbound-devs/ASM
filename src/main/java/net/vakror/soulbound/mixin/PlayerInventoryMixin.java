package net.vakror.soulbound.mixin;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.vakror.soulbound.util.PickupUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Inventory.class)
public class PlayerInventoryMixin {

    @Inject(method = "add(Lnet/minecraft/world/item/ItemStack;)Z", at = @At("HEAD"), cancellable = true)
    private void interceptItems(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (PickupUtil.interceptItem((Inventory) (Object) this, stack)) cir.setReturnValue(true);
    }
}