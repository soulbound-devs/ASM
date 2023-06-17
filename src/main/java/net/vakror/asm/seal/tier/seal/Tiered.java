package net.vakror.asm.seal.tier.seal;

import net.vakror.asm.seal.ISeal;

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
