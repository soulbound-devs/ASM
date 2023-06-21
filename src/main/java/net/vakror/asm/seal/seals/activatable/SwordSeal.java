package net.vakror.asm.seal.seals.activatable;

import net.vakror.asm.seal.SealProperty;
import net.vakror.asm.seal.type.AttackSeal;

import java.util.List;

public class SwordSeal extends AttackSeal {
    public SwordSeal() {
        super("swording", 3f);
    }

    @Override
    public float getDamage() {
        return 7f;
    }

    @Override
    public List<SealProperty> properties() {
        properties.add(new SealProperty("weapon"));
        properties.add(new SealProperty("offensive"));
        return super.properties();
    }
}
