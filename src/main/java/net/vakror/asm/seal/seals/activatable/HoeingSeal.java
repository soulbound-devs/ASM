package net.vakror.asm.seal.seals.activatable;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.vakror.asm.seal.type.ActivatableSeal;

public class HoeingSeal extends ActivatableSeal {
    public HoeingSeal() {
        super("hoeing");
    }

    @Override
    public InteractionResult useAction(UseOnContext context) {
        return InteractionResult.PASS;
    }
}
