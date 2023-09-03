package net.vakror.soulbound.mod.seal.tier.seal;

import net.vakror.soulbound.mod.seal.ISeal;

import javax.annotation.Nullable;
import java.util.List;

public interface Tiered {

    /**
     * @return the next seal; returns null if is already max tier
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

    public List<ISeal> getAllSeals();
}
