package net.vakror.soulbound.screen.slot;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import net.vakror.soulbound.items.custom.seals.SealItem;
import org.jetbrains.annotations.NotNull;

public class ModSealSlot extends SlotItemHandler {
    public ModSealSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.getItem() instanceof SealItem;
    }
}
