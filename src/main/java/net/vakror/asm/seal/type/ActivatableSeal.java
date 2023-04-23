package net.vakror.asm.seal.type;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.vakror.asm.seal.ISeal;

public abstract class ActivatableSeal extends BaseSeal implements ISeal {

    public ActivatableSeal(String id) {
        super(id, true);
    }

    public abstract InteractionResultHolder<ItemStack> useAction(Level level, Player player, InteractionHand hand);
}
