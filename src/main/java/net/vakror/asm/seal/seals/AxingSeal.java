package net.vakror.asm.seal.seals;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.vakror.asm.seal.type.AttackSeal;

public class AxingSeal extends AttackSeal {
    public AxingSeal() {
        super("axing");
    }

    @Override
    public int getDamage(ItemStack stack) {
        return 0;
    }

    @Override
    public InteractionResult useAction(UseOnContext context) {
        return Items.DIAMOND_AXE.useOn(context);
    }
}
