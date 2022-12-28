package net.vakror.soulbound.items;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab SOULBOUND = new CreativeModeTab("soulbound") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.PICKAXING_SEAL.get());
        }
    };
}