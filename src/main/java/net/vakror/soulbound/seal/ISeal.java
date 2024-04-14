package net.vakror.soulbound.seal;

import net.vakror.soulbound.seal.type.AttackSeal;

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
