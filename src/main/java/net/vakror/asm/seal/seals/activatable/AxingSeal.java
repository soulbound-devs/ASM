package net.vakror.asm.seal.seals.activatable;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.vakror.asm.seal.SealProperty;
import net.vakror.asm.seal.type.AttackSeal;

import java.util.List;

public class AxingSeal extends AttackSeal {
    public AxingSeal() {
        super("axing");
    }

    @Override
    public float getDamage() {
        return 2.6f;
    }

    @Override
    public InteractionResult useAction(UseOnContext context) {
        return Items.DIAMOND_AXE.useOn(context);
    }

    @Override
    public List<SealProperty> properties() {
        properties.add(new SealProperty("weapon"));
        properties.add(new SealProperty("offensive"));
        return super.properties();
    }
}
