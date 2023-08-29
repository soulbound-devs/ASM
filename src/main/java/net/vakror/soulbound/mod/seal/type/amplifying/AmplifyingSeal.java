package net.vakror.soulbound.mod.seal.type.amplifying;

import net.vakror.soulbound.mod.seal.ISeal;
import net.vakror.soulbound.mod.seal.SealProperty;
import net.vakror.soulbound.mod.seal.SealType;
import net.vakror.soulbound.mod.seal.type.BaseSeal;

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

    @Override
    public SealType getType() {
        return SealType.AMPLIFYING;
    }
}
