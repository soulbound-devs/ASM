package net.vakror.soulbound.mixin;

import net.minecraft.world.item.DiggerItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(DiggerItem.class)
public interface DiggerItemAccessor {
    @Accessor("attackDamageBaseline")
    @Mutable
    public float getAttackDamageBaseline();
}
