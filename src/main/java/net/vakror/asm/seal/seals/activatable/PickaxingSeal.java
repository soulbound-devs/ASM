package net.vakror.asm.seal.seals.activatable;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.vakror.asm.seal.type.ActivatableSeal;

public class PickaxingSeal extends ActivatableSeal {
    public PickaxingSeal() {
        super("pickaxing");
    }

    @Override
    public InteractionResult useAction(UseOnContext context) {
        return InteractionResult.PASS;
    }
}
