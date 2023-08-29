package net.vakror.soulbound.mod.mixin;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.vakror.soulbound.mod.items.custom.SealableItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ItemStack.class)
public abstract class FixSealTooltipMixin {
    @Shadow public abstract Item getItem();

    @ModifyVariable(method = "getTooltipLines", name = "multimap", at = @At(value = "STORE"))
    public Multimap<Attribute, AttributeModifier> multimap(Multimap<Attribute, AttributeModifier> map) {
        if (getItem() instanceof SealableItem) {
            return ImmutableMultimap.of();
        }
        return map;
    }
}
