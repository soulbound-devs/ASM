package net.vakror.asm.seal.seals;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.vakror.asm.seal.type.AttackSeal;

public class AxingSeal extends AttackSeal {
    public AxingSeal() {
        super("axing");
    }

    @Override
    public InteractionResult useAction(Level level, Player player, InteractionHand hand) {
        return InteractionResult.SUCCESS;
    }

    @Override
    public int getDamage(ItemStack stack) {
        return 0;
    }
}
