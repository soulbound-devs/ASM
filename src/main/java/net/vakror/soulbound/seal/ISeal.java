package net.vakror.soulbound.seal;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ISeal {

    public default String getId() {
        return "null";
    }
}
