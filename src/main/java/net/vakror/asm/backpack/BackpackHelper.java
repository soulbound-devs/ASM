package net.vakror.asm.backpack;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkHooks;

public class BackpackHelper {
    public static void openBackpackGui(Player player, ItemStack stack) {
        if (player instanceof ServerPlayer) {
            NetworkHooks.openScreen(((ServerPlayer) player), null);
        }
    }
}
