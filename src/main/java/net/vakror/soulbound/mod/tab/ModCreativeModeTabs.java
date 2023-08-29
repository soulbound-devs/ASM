package net.vakror.soulbound.mod.tab;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.vakror.soulbound.mod.items.ModItems;

public class ModCreativeModeTabs {
    public static final CreativeModeTab SOULBOUND_TAB = new CreativeModeTab("soulbound") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ModItems.WAND.get());
        }
    };
}