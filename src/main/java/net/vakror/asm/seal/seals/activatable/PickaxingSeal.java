package net.vakror.asm.seal.seals.activatable;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.vakror.asm.seal.SealProperty;
import net.vakror.asm.seal.type.ActivatableSeal;

import java.util.List;

public class PickaxingSeal extends ActivatableSeal {
    public PickaxingSeal() {
        super("pickaxing");
    }

    @Override
    public InteractionResult useAction(UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public List<SealProperty> properties() {
        properties.add(new SealProperty("tool"));
        properties.add(new SealProperty("passive"));
        return super.properties();
    }
}
