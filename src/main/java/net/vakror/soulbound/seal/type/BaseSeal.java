package net.vakror.soulbound.seal.type;

import net.vakror.soulbound.seal.ISeal;

public abstract class BaseSeal implements ISeal {
    private final String id;
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
}
