package net.vakror.asm.seal.seals;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.vakror.asm.seal.type.ActivatableSeal;

public class HoeingSeal extends ActivatableSeal {
    public HoeingSeal() {
        super("hoeing");
    }

    @Override
    public InteractionResultHolder<ItemStack> useAction(Level level, Player player, InteractionHand hand) {
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }
}
