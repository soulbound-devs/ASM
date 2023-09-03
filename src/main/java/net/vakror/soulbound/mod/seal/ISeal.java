package net.vakror.soulbound.mod.seal;

import net.vakror.soulbound.mod.seal.type.AttackSeal;

public interface ISeal {
    default String getId() {
        return "error";
    }

    default boolean canBeActivated() {
        return false;
    }

    default boolean isAttack() {
        return this instanceof AttackSeal;
    }

    SealType getType();
}
