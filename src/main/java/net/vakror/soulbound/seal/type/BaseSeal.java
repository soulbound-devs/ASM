package net.vakror.soulbound.seal.type;

import net.minecraft.resources.ResourceLocation;
import net.vakror.soulbound.seal.ISeal;

public abstract class BaseSeal implements ISeal {
    private final ResourceLocation id;
    private final boolean canBeActivated;

    public BaseSeal(ResourceLocation id, boolean canBeActivated) {
        this.id = id;
        this.canBeActivated = canBeActivated;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public boolean canBeActivated() {
        return canBeActivated;
    }
}
