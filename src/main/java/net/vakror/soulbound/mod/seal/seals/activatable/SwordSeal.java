package net.vakror.soulbound.mod.seal.seals.activatable;

import net.vakror.soulbound.mod.seal.type.AttackSeal;

public class SwordSeal extends AttackSeal {
    public SwordSeal() {
        super("swording", 3f);
    }

    @Override
    public float getDamage() {
        return 7f;
    }
}
