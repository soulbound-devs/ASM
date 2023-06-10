package net.vakror.asm.seal.seals.amplifying;

import net.vakror.asm.seal.SealProperty;
import net.vakror.asm.seal.type.BaseSeal;

import java.util.List;

public class MiningSpeedSeal extends BaseSeal {
    public MiningSpeedSeal() {
        super("mining_speed", false);
    }

    @Override
    public List<SealProperty> properties() {
        properties.add(new SealProperty("haste"));
        properties.add(new SealProperty("amplifying"));
        return super.properties();
    }
}
