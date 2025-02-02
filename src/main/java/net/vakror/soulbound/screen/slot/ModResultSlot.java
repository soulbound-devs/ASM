package net.vakror.soulbound.screen.slot;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

public class ModResultSlot extends SlotItemHandler {
    public ModResultSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public void onTake(Player pPlayer, ItemStack pStack) {
        super.onTake(pPlayer, pStack);
        pStack.onCraftedBy(pPlayer.level(), pPlayer, 1);
    }
}
