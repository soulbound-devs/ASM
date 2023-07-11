package net.vakror.soulbound.seal;

import net.vakror.soulbound.seal.type.AttackSeal;

import java.util.List;

public interface ISeal {
    public default String getId() {
        return "error";
    }

    public default boolean canBeActivated() {
        return false;
    }

    default boolean isAttack() {
        return this instanceof AttackSeal;
    };

    List<SealProperty> properties();
}
