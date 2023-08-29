package net.vakror.soulbound.mod.screen.slot;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.vakror.soulbound.mod.items.custom.SealableItem;
import org.jetbrains.annotations.NotNull;

public class ModWandSlot extends SlotItemHandler {
    public ModWandSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.getItem() instanceof SealableItem;
    }
}
