package net.vakror.asm.seal.seals;

import net.minecraft.world.item.ItemStack;
import net.vakror.asm.seal.type.AttackSeal;

public class AxingSeal extends AttackSeal {
    public AxingSeal() {
        super("axing");
    }

    @Override
    public int getDamage(ItemStack stack) {
        return 0;
    }
}
