package net.vakror.soulbound.mod.seal.type;

import net.vakror.soulbound.mod.seal.ISeal;
import net.vakror.soulbound.mod.seal.SealProperty;
import net.vakror.soulbound.mod.util.BetterArrayList;

import java.util.List;

public abstract class BaseSeal implements ISeal {
    private final String id;
    protected final List<SealProperty> properties = new BetterArrayList<>();
    private final boolean canBeActivated;

    public BaseSeal(String id, boolean canBeActivated) {
        this.id = id;
        this.canBeActivated = canBeActivated;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public boolean canBeActivated() {
        return canBeActivated;
    }

    @Override
    public List<SealProperty> properties() {
        return properties;
    }
}
