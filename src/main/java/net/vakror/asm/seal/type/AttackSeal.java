package net.vakror.asm.seal.type;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;

public abstract class AttackSeal extends ActivatableSeal{

    public AttackSeal(String id) {
        super(id);
    }

    @Override
    public InteractionResult useAction(UseOnContext context) {
        return InteractionResult.PASS;
    }

    public abstract float getDamage();
}
