package net.vakror.asm.seal.seals.activatable;

import net.minecraft.world.item.ItemStack;
import net.vakror.asm.seal.type.AttackSeal;

public class SwordSeal extends AttackSeal {
    public SwordSeal() {
        super("swording");
    }

    @Override
    public int getDamage(ItemStack stack) {
        return 0;
    }
}
