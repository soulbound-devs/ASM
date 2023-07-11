package net.vakror.soulbound.seal.tier.seal;

import net.vakror.soulbound.seal.ISeal;

import javax.annotation.Nullable;

public interface Tiered {

    /**
     * @return the max tier for this seal
     */
    @Nullable
    public ISeal getNextSeal();

    /**
     * @return the tier
     */
    public int getTier();

    /**
     * @return the max tier for this seal
     */
    public String getTierId();
}
