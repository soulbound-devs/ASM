package net.vakror.asm.seal.type;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.vakror.asm.seal.SealProperty;

import java.util.List;

public abstract class AttackSeal extends ActivatableSeal{

    public AttackSeal(String id, float swingSpeed) {
        super(id, swingSpeed);
    }

    @Override
    public InteractionResult useAction(UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public List<SealProperty> properties() {
        properties.add(new SealProperty("offensive"));
        return super.properties();
    }
}
