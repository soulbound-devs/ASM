package net.vakror.asm.backpack;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;
import net.vakror.asm.items.custom.WandItem;

public class BackpackHelper {
    public static void openBackpackGui(Player player, ItemStack stack) {
        if (player instanceof ServerPlayer && stack.getItem() instanceof WandItem wand) {
            NetworkHooks.openScreen(((ServerPlayer) player), wand);
        }
    }
}
