package net.vakror.asm.seal.seals;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.vakror.asm.seal.type.ActivatableSeal;

public class HoeingSeal extends ActivatableSeal {
    public HoeingSeal() {
        super("hoeing");
    }

    @Override
    public InteractionResult useAction(Level level, Player player, InteractionHand hand) {
        return InteractionResult.SUCCESS;
    }
}
