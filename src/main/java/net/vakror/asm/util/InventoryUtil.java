package net.vakror.asm.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InventoryUtil {
    public static class PlayerInventory {

        public static boolean hasPlayerStackInInventory(Player player, Item item) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack currentStack = player.getInventory().getItem(i);
                if (!currentStack.isEmpty() && currentStack.sameItem(new ItemStack(item))) {
                    return true;
                }
            }

            return false;
        }

        public static int getFirstInventoryIndex(Player player, Item item) {
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack currentStack = player.getInventory().getItem(i);
                if (!currentStack.isEmpty() && currentStack.sameItem(new ItemStack(item))) {
                    return i;
                }
            }

            return -1;
        }

        public static List<Integer> getAllInventoryIndexes(Player player, Item item) {
            List<Integer> indexes = new ArrayList<>();
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack currentStack = player.getInventory().getItem(i);
                if (!currentStack.isEmpty() && currentStack.sameItem(new ItemStack(item))) {
                    indexes.add(i);
                }
            }

            return indexes;
        }
    }
}