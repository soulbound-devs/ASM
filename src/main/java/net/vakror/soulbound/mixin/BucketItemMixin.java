package net.vakror.soulbound.mixin;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.vakror.soulbound.block.entity.custom.SoulExtractorBlockEntity;
import net.vakror.soulbound.items.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Item.class)
public abstract class BucketItemMixin {
    @Shadow public abstract Item asItem();

    @Inject(method = "useOn", at = @At("HEAD"))
    public void getContentsOfSoulExtractor(UseOnContext context, CallbackInfoReturnable<InteractionResult> cir) {
        if (asItem().equals(Items.BUCKET) && !context.getLevel().isClientSide && context.getLevel().getBlockEntity(context.getClickedPos()) instanceof SoulExtractorBlockEntity entity) {
            if (Objects.requireNonNull(context.getPlayer()).isCrouching() && entity.DARK_SOUL_TANK.getFluid().getAmount() >= 1000) {
                context.getPlayer().addItem(new ItemStack(ModItems.DARK_SOUL_BUCKET.get(), 1));
                entity.DARK_SOUL_TANK.drain(1000, IFluidHandler.FluidAction.EXECUTE);
            } else if (!context.getPlayer().isCrouching() && entity.SOUL_TANK.getFluid().getAmount() >= 1000) {
                context.getPlayer().addItem(new ItemStack(ModItems.SOUL_BUCKET.get(), 1));
                entity.SOUL_TANK.drain(1000, IFluidHandler.FluidAction.EXECUTE);
            }
        }
    }

}