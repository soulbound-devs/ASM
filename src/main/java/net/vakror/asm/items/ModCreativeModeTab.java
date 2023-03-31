package net.vakror.asm.items;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModCreativeModeTab {
    public static final CreativeModeTab ASM = new CreativeModeTab("asm") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.PICKAXING_SEAL.get());
        }
    };
}