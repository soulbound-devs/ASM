package net.vakror.soulbound.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.vakror.soulbound.SoulboundMod;
import net.vakror.soulbound.items.custom.SackItem;
import net.vakror.soulbound.items.custom.SealableItem;

import java.util.ArrayList;
import java.util.List;

public class PickupUtil {

    /**
     * @param inv      Player Inventory to add the item to
     * @param incoming the itemstack being picked up
     * @return if the item was completely picked up by the sack(s)
     */
    public static boolean interceptItem(Inventory inv, ItemStack incoming) {
        Player player = inv.player;
        if (player.level().isClientSide || incoming.isEmpty()) {//thanks Hookshot
            return false;
        }
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack stack = inv.getItem(i);
            if (stack.getItem() instanceof SackItem && onItemPickup(player, incoming, stack)) {
                return true;
            }
        }
        return false;
    }

    public static boolean onItemPickup(Player player, ItemStack pickStack, ItemStack sack) {

        boolean canPickup = ((SealableItem) sack.getItem()).hasSeal(new ResourceLocation(SoulboundMod.MOD_ID, "pickup"), sack);
        if (!canPickup) {
            return false;
        }
        SackItem sackItem = (SackItem) sack.getItem();
        SackInventory inventory = SackItem.getInv(sack);

        int count = pickStack.getCount();
        List<ItemStack> existing = new ArrayList<>();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                boolean exists = false;
                for (ItemStack stack1 : existing) {
                    if (areItemStacksCompatible(stack, stack1)) {
                        exists = true;
                    }
                }
                if (!exists) {
                    existing.add(stack.copy());
                }
            }
        }

        switch (sackItem.getPickupMode()) {
            case ALL -> {
                for (int i = 0; i < inventory.getContainerSize(); i++) {
                    allPickup(inventory, i, pickStack);
                    if (pickStack.isEmpty()) break;
                }
            }
            case FILTERED -> {
                for (int i = 0; i < inventory.getContainerSize(); i++) {
                    filteredPickup(inventory, i, pickStack, existing);
                    if (pickStack.isEmpty()) break;
                }
            }
            case VOID -> {
                for (int i = 0; i < inventory.getContainerSize(); i++) {
                    voidPickup(inventory, i, pickStack, existing);
                    if (pickStack.isEmpty()) break;
                }
            }
        }

        //leftovers
        if (pickStack.getCount() != count) {
            sack.setPopTime(5);
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.PLAYERS, 0.2F, ((player.getRandom().nextFloat() - player.getRandom().nextFloat()) * 0.7F + 1.0F) * 2.0F);
        }
        return pickStack.isEmpty();
    }

    private static boolean areItemStacksCompatible(ItemStack stack, ItemStack stack1) {
        return ItemStack.isSameItemSameTags(stack, stack1) && ItemStack.isSameItem(stack, stack1);
    }

    public enum PickupMode {
        NONE,
        VOID,
        ALL,
        FILTERED;

        public static PickupMode next(PickupMode current) {
            return switch (current) {
                case NONE -> ALL;
                case ALL -> FILTERED;
                case FILTERED -> VOID;
                case VOID -> NONE;
            };
        }
    }

    public static void voidPickup(SackInventory inv, int slot, ItemStack toInsert, List<ItemStack> filter) {
        ItemStack existing = inv.getItem(slot);

        if (doesItemStackExist(toInsert, filter) && areItemStacksCompatible(existing, toInsert)) {
            int stackLimit = inv.getMaxStackSize();
            int total = Math.min(toInsert.getCount() + existing.getCount(), stackLimit);
            
            inv.setItem(slot, ItemHandlerHelper.copyStackWithSize(existing, total));
            toInsert.setCount(0);
        }
    }

    public static void allPickup(SackInventory inv, int slot, ItemStack pickup) {
        ItemStack existing = inv.getItem(slot);

        if (existing.isEmpty()) {
            int stackLimit = inv.getMaxStackSize();
            int total = pickup.getCount();
            int remainder = total - stackLimit;
            //no overflow
            if (remainder <= 0) {
                inv.setItem(slot, pickup.copy());
                pickup.setCount(0);
            } else {
                inv.setItem(slot, ItemHandlerHelper.copyStackWithSize(pickup, stackLimit));
                pickup.setCount(remainder);
            }
            return;
        }

        if (ItemHandlerHelper.canItemStacksStack(pickup, existing)) {
            int stackLimit = inv.getMaxStackSize();
            int total = pickup.getCount() + existing.getCount();
            int remainder = total - stackLimit;
            //no overflow
            if (remainder <= 0) {
                inv.setItem(slot, ItemHandlerHelper.copyStackWithSize(existing, total));
                pickup.setCount(0);
            } else {
                inv.setItem(slot, ItemHandlerHelper.copyStackWithSize(pickup, stackLimit));
                pickup.setCount(remainder);
            }
        }
    }

    public static void filteredPickup(SackInventory inv, int slot, ItemStack toInsert, List<ItemStack> filter) {
        ItemStack existing = inv.getItem(slot);

        if (existing.isEmpty() && doesItemStackExist(toInsert, filter)) {
            int stackLimit = inv.getMaxStackSize();
            int total = toInsert.getCount();
            int remainder = total - stackLimit;
            //no overflow
            if (remainder <= 0) {
                inv.setItem(slot, toInsert.copy());
                toInsert.setCount(0);
            } else {
                inv.setItem(slot, ItemHandlerHelper.copyStackWithSize(toInsert, stackLimit));
                toInsert.setCount(remainder);
            }
            return;
        }

        if (doesItemStackExist(toInsert, filter) && areItemStacksCompatible(existing, toInsert)) {
            int stackLimit = inv.getMaxStackSize();
            int total = toInsert.getCount() + existing.getCount();
            int remainder = total - stackLimit;
            //no overflow
            if (remainder <= 0) {
                inv.setItem(slot, ItemHandlerHelper.copyStackWithSize(existing, total));
                toInsert.setCount(0);
            } else {
                inv.setItem(slot, ItemHandlerHelper.copyStackWithSize(toInsert, stackLimit));
                toInsert.setCount(remainder);
            }
        }
    }


    public static boolean doesItemStackExist(ItemStack stack, List<ItemStack> filter) {
        for (ItemStack filterStack : filter) {
            if (areItemStacksCompatible(stack, filterStack)) return true;
        }
        return false;
    }
}
