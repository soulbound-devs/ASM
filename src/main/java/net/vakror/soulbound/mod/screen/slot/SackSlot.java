package net.vakror.soulbound.mod.screen.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.vakror.soulbound.mod.items.custom.SackItem;
import org.jetbrains.annotations.NotNull;

public class SackSlot extends Slot {
    private final int stackLimit;
    public SackSlot(Container inventory, int index, int x, int y, int stackLimit) {
        super(inventory, index, x, y);
        this.stackLimit = stackLimit;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return checkCanInsert(stack);
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack stack) {
        return stack.getMaxStackSize() == 1 ? 1: stackLimit;
    }

    public static boolean checkCanInsert(ItemStack stack) {
        if (stack.getItem() instanceof SackItem) {
            return false;
        }

        if (stack.getItem() instanceof final BlockItem itemb) {
            return !(itemb.getBlock() instanceof ShulkerBoxBlock);
        }

        return true;
    }
}