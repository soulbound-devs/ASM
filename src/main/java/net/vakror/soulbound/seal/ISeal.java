package net.vakror.soulbound.seal;

import net.minecraft.resources.ResourceLocation;
import net.vakror.soulbound.seal.type.AttackSeal;

public interface ISeal {
    default ResourceLocation getId() {
        return new ResourceLocation("soulbound", "error");
    }

    default boolean canBeActivated() {
        return false;
    }

    default boolean isAttack() {
        return this instanceof AttackSeal;
    }

    SealType getType();
}
