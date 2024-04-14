package net.vakror.soulbound.seal.seals.activatable;

import net.vakror.soulbound.seal.type.AttackSeal;

public class SwordSeal extends AttackSeal {
    public SwordSeal() {
        super("swording", 3f);
    }

    @Override
    public float getDamage() {
        return 7f;
    }
}
