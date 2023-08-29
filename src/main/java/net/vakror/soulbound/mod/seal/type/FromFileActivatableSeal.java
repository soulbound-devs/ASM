package net.vakror.soulbound.mod.seal.type;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.vakror.soulbound.mod.seal.SealType;
import net.vakror.soulbound.mod.seal.function.use.UseFunction;

public class FromFileActivatableSeal extends ActivatableSeal {
    public final int damage;
    public final UseFunction useFunction;
    public FromFileActivatableSeal(String id, float swingSpeed, int damage, UseFunction useFunction) {
        super(id, swingSpeed);
        this.damage = damage;
        this.useFunction = useFunction;
    }

    @Override
    public InteractionResult useAction(UseOnContext context) {
        useFunction.executeUseOn(context);
        return InteractionResult.PASS;
    }

    @Override
    public float getDamage() {
        return damage;
    }

    @Override
    public SealType getType() {
        return SealType.PASSIVE;
    }
}
