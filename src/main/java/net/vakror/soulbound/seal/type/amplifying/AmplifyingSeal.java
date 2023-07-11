package net.vakror.soulbound.seal.type.amplifying;

import net.vakror.soulbound.seal.ISeal;
import net.vakror.soulbound.seal.SealProperty;
import net.vakror.soulbound.seal.type.BaseSeal;

import java.util.ArrayList;
import java.util.List;

public abstract class AmplifyingSeal extends BaseSeal {
    public List<ISeal> requirements = new ArrayList<>();

    public AmplifyingSeal(String id) {
        super(id, false);
    }

    @Override
    public List<SealProperty> properties() {
        properties.add(new SealProperty("amplifying"));
        return super.properties();
    }
}
