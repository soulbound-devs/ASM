package net.vakror.asm.seal.seals.amplifying.sack;

import net.vakror.asm.seal.SealProperty;
import net.vakror.asm.seal.type.amplifying.SackAmplifyingSeal;

import java.util.List;

public class PickupSeal extends SackAmplifyingSeal {
    public PickupSeal() {
        super("pickup");
    }

    @Override
    public List<SealProperty> properties() {
        properties.add(new SealProperty("pickup"));
        return super.properties();
    }
}
