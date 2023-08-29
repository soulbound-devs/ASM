package net.vakror.soulbound.mod.seal.type.amplifying;

import net.vakror.soulbound.mod.seal.SealProperty;

import java.util.List;

public class SackAmplifyingSeal extends AmplifyingSeal{
    public SackAmplifyingSeal(String id) {
        super(id);
    }

    @Override
    public List<SealProperty> properties() {
        properties.add(new SealProperty("sack"));
        return super.properties();
    }
}
