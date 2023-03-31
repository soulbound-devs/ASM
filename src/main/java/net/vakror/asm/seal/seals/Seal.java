package net.vakror.asm.seal.seals;

import net.vakror.asm.seal.ISeal;

public class Seal implements ISeal {
    private final String id;
    private final boolean canBeActivated;

    public Seal(String id, boolean canBeActivated) {
        this.id = id;
        this.canBeActivated = canBeActivated;
    }

    public Seal(String id) {
        this.id = id;
        this.canBeActivated = false;
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
