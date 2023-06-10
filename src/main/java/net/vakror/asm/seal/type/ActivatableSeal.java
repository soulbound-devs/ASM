package net.vakror.asm.seal.type;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.vakror.asm.seal.SealProperty;

import java.util.List;

public abstract class ActivatableSeal extends BaseSeal {

    public ActivatableSeal(String id) {
        super(id, true);
    }

    public abstract InteractionResult useAction(UseOnContext context);

    @Override
    public List<SealProperty> properties() {
        properties.add(new SealProperty("activatable"));
        return super.properties();
    }
}
