package net.vakror.soulbound.mod.screen;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SackInventory extends SimpleContainer {
    protected final ItemStack itemStack;
    protected final int SIZE;

    protected final int stackLimit;
    public SackInventory(ItemStack stack, int SIZE, int stackLimit) {
        super(getStacks(stack, SIZE).toArray(new ItemStack[SIZE]));
        itemStack = stack;
        this.SIZE = SIZE;
        this.stackLimit = stackLimit;
    }

    public ItemStack getHolderStack() {
        return itemStack;
    }

    public static String getNBTTag() {
        return "Inventory";
    }

    public static NonNullList<ItemStack> getStacks(ItemStack usedStack, int SIZE) {
        CompoundTag compoundTag = usedStack.getTagElement(getNBTTag());
        NonNullList<ItemStack> itemStacks = NonNullList.withSize(SIZE, ItemStack.EMPTY);
        if (compoundTag != null && compoundTag.contains("Items", 9)) {
            ContainerHelper.loadAllItems(compoundTag, itemStacks);
        }
        return itemStacks;
    }

    @Override
    public void setChanged() {
        super.setChanged();
        CompoundTag itemTag = itemStack.getTagElement(getNBTTag());
        if (itemTag == null)
            itemTag = itemStack.getOrCreateTagElement(getNBTTag());

        if (isEmpty()) {
            if (itemTag.contains("Items")) itemTag.remove("Items");
        } else {
            NonNullList<ItemStack> itemStacks = NonNullList.withSize(SIZE, ItemStack.EMPTY);
            for (int i = 0; i < size; i++) {
                itemStacks.set(i, getItem(i));
            }
            ContainerHelper.saveAllItems(itemTag, itemStacks);
        }

        if (shouldDeleteNBT(itemTag)) {
            itemStack.removeTagKey(getNBTTag());
        }
    }

    public boolean shouldDeleteNBT(CompoundTag blockEntityTag) {
        if (!blockEntityTag.contains("Items"))
            return blockEntityTag.getAllKeys().size() == 0;
        return isEmpty();
    }

    @Override
    public int getMaxStackSize() {
        return stackLimit;
    }

    @Override
    public void stopOpen(@NotNull Player playerEntity_1) {
        if (itemStack.getCount() > 1) {
            int count = itemStack.getCount();
            itemStack.setCount(1);
            playerEntity_1.addItem(new ItemStack(itemStack.getItem(), count - 1));
        }
        setChanged();
    }
}