package net.vakror.soulbound.seal.seals.amplifying.sack;

import net.vakror.soulbound.seal.SealProperty;
import net.vakror.soulbound.seal.type.amplifying.SackAmplifyingSeal;

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
