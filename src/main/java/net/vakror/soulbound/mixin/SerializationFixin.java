package net.vakror.soulbound.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * fixes ItemStack to serialize count as int instead of byte
 */
@Mixin (ItemStack.class)
public abstract class SerializationFixin {
    @Unique
    @OnlyIn(Dist.CLIENT) private static final NumberFormat FORMAT = NumberFormat.getNumberInstance(Locale.US);
    @Shadow private int count;

    @Inject (at = @At ("TAIL"), method = "<init>(Lnet/minecraft/nbt/CompoundTag;)V")
    void onDeserialization(CompoundTag tag, CallbackInfo callbackInformation) {
        if (tag.contains("countInteger")) {
            this.count = tag.getInt("countInteger");
        }
    }

    @Inject(at = @At ("TAIL"), method = "save", remap = false)
    void onSerialization(CompoundTag nbt, CallbackInfoReturnable<CompoundTag> cir) {
        if (this.count > Byte.MAX_VALUE) {
            nbt.putInt("countInteger", this.count);
            // make downgrading less painful
            nbt.putByte("Count", Byte.MAX_VALUE);
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Inject (method = "getTooltipLines", at = @At ("RETURN"), cancellable = true)
    private void addOverflowTooltip(Player player, TooltipFlag context, CallbackInfoReturnable<List<Component>> cir) {
        if (this.getCount() > 1000) {
            List<Component> texts = cir.getReturnValue();
            texts.add(1, Component.literal(FORMAT.format(this.getCount())).withStyle(ChatFormatting.GRAY));
        }
    }

    @Shadow
    public abstract int getCount();
}