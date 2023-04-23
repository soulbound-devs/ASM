package net.vakror.asm.seal.type;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.vakror.asm.seal.ISeal;

public abstract class ActivatableSeal extends BaseSeal implements ISeal {

    public ActivatableSeal(String id) {
        super(id, true);
    }

    public abstract InteractionResult useAction(UseOnContext context);
}
