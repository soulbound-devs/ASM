package net.vakror.soulbound.seal;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ISeal {
    public void apply(ItemStack stack, Entity entity, Level world);

    public void activateSeal(ItemStack stack, Entity entity, Level world, int slotNumber);
}
