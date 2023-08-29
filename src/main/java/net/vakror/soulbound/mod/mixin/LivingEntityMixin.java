package net.vakror.soulbound.mod.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.vakror.soulbound.mod.entity.BroomEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow public abstract void setItemInHand(InteractionHand p_21009_, ItemStack p_21010_);

    @Inject(at = @At("TAIL"), method = "dismountVehicle")
    private void dismount(Entity entity, CallbackInfo ci) {
        if (entity instanceof BroomEntity) {
            setItemInHand(InteractionHand.MAIN_HAND, ((BroomEntity) entity).getItem());
            entity.discard();
        }
    }
}
