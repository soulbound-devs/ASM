package net.vakror.asm.seal.type;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.vakror.asm.seal.ISeal;

public abstract class AttackSeal extends ActivatableSeal implements ISeal {

    public AttackSeal(String id) {
        super(id);
    }

    public InteractionResultHolder<ItemStack> useAction(Level level, Player player, InteractionHand hand) {
        return InteractionResultHolder.pass(player.getItemInHand(hand));
    }

    public abstract int getDamage(ItemStack stack);
}
