package net.vakror.asm.seal.type;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.vakror.asm.seal.ISeal;

public abstract class AttackSeal extends ActivatableSeal implements ISeal {

    public AttackSeal(String id) {
        super(id);
    }

    @Override
    public InteractionResult useAction(UseOnContext context) {
        return InteractionResult.PASS;
    }

    public abstract int getDamage(ItemStack stack);
}
