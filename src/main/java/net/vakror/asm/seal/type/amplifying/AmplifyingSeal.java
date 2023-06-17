package net.vakror.asm.seal.type.amplifying;

import net.vakror.asm.seal.ISeal;
import net.vakror.asm.seal.SealProperty;
import net.vakror.asm.seal.type.BaseSeal;

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
