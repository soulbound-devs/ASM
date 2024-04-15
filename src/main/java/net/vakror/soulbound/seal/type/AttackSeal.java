package net.vakror.soulbound.seal.type;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.UseOnContext;
import net.vakror.soulbound.seal.SealType;

public abstract class AttackSeal extends ActivatableSeal{

    public AttackSeal(ResourceLocation id, float swingSpeed) {
        super(id, swingSpeed);
    }

    @Override
    public InteractionResult useAction(UseOnContext context) {
        return InteractionResult.PASS;
    }

    @Override
    public SealType getType() {
        return SealType.OFFENSIVE;
    }
}
